from multiprocessing import Process, Queue, current_process, freeze_support, cpu_count, Pool
import time, random, os, imgur, datetime, sys, reddit, logging, itertools, ConfigParser

#Diable logging from requests
logging.getLogger("requests").setLevel(logging.ERROR)

#Set up own logging
logdir = "logs"

if not os.path.exists(logdir):
    os.makedirs(logdir)
logfilename = logdir + "/" + datetime.datetime.now().strftime('redditd_%H_%M_%S_%d_%m_%Y.log')
logging.basicConfig(
    filename=logfilename,
    format='%(levelname)s:%(message)s',
    level=logging.INFO
)

#Configure progress file
progressfiledir = ""
progressfilelocation = ""

def loadconfigfile(config, targetSubreddit, limit, feed_type):
    if os.path.isfile(progressfilelocation):
        config.read(progressfilelocation)
        return True
    else:
        createProgressFile(config, targetSubreddit, limit, feed_type)
        return False

def createProgressFile(config, targetSubreddit, limit, feed_type):
    if not os.path.exists(progressfiledir):
        os.makedirs(progressfiledir)
    config.add_section(targetSubreddit)
    config.set(targetSubreddit, 'count', limit)
    config.set(targetSubreddit, 'daysAgo', 0)
    config.set(targetSubreddit, 'finished', False)
    with open(progressfilelocation, 'wb') as configfile:
        config.write(configfile)

def updateProgressFile(progressfile, targetSubreddit, count, daysAgo, finished):
    progressfile.set(targetSubreddit, 'count', count)
    progressfile.set(targetSubreddit, 'daysAgo', daysAgo)
    progressfile.set(targetSubreddit, 'finished', finished)
    with open(progressfilelocation, 'wb') as configfile:
        progressfile.write(configfile)

def getImgurImages(targetSubreddit, limit=25, feed_type="hot"):
    logging.info("[ --- Searching %s most recent %s posts from %s for imgur files --- ]", limit, feed_type, targetSubreddit)
    daysAgo = 0
    count = limit
    finished = False
    #Look for progress file and restore data if we find one
    config = ConfigParser.RawConfigParser()
    if loadconfigfile(config, targetSubreddit, limit, feed_type):
        finished = config.getboolean(targetSubreddit, 'finished')
        if not finished:
            count = config.getint(targetSubreddit, 'count')
        else:
            finished = False
        daysAgo = config.getint(targetSubreddit, 'daysAgo')
    updateProgressFile(config, targetSubreddit, count, daysAgo, finished)

    pool = Pool(processes=(cpu_count()-1))              # start 4 worker processes
    while count > 0:
        #submissions = reddit.get_submissions(targetSubreddit, count, feed_type)
        submissions = reddit.getSubmissionsFromDaysAgo(targetSubreddit, daysAgo, count, feed_type)
        results = pool.imap(getImageWrapper, itertools.izip(submissions, itertools.repeat(targetSubreddit)))
        for result in results:
            if result:
                count -= 1
        daysAgo+=1
        updateProgressFile(config, targetSubreddit, count, daysAgo, finished)
    pool.close()
    finished = True
    updateProgressFile(config, targetSubreddit, count, daysAgo, finished)

def getImageWrapper(args):
    return imgur.getImage(*args)

def main():
    global progressfiledir
    global progressfilelocation
    if len(sys.argv) >= 3:
        targetSubreddit = sys.argv[1]
        limit = int(sys.argv[2])
        feed_type = "hot"
        if(len(sys.argv) > 3):
            feed_type = sys.argv[3]
        progressfiledir = "downloads" + "/" + targetSubreddit + "/"
        progressfilelocation = progressfiledir + "progress.meta"
        getImgurImages(targetSubreddit, limit, feed_type)
    else:
        print "Usage: python redditd.py (targetSubreddit) (Limit) (new/hot/rising)"
        exit(0)

if __name__ == '__main__':
    start_time = time.time()
    freeze_support()
    main()
    logging.info("--- %s seconds ---", (time.time() - start_time))
