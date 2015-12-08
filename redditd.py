from multiprocessing import Process, Queue, current_process, freeze_support, cpu_count, Pool
import time, random, os, imgur, datetime, sys, reddit, logging, itertools

#Diable logging from requests
logging.getLogger("requests").setLevel(logging.ERROR)

logdir = "logs"
#Set up own logging
if not os.path.exists(logdir):
    os.makedirs(logdir)
logfilename = logdir + "/" + datetime.datetime.now().strftime('redditd_%H_%M_%S_%d_%m_%Y.log')
logging.basicConfig(
    filename=logfilename,
    format='%(levelname)s:%(message)s',
    level=logging.INFO
)

def getImgurImages(targetSubreddit, limit=25, feed_type="hot"):
    logging.info("[ --- Searching %s most recent %s posts from %s for imgur files --- ]", limit, feed_type, targetSubreddit)
    count = limit
    daysAgo = 0
    pool = Pool(processes=(cpu_count()-1))              # start 4 worker processes
    while count > 0:
        #submissions = reddit.get_submissions(targetSubreddit, count, feed_type)
        submissions = reddit.getSubmissionsFromDaysAgo(targetSubreddit, daysAgo, count, feed_type)
        results = pool.imap(getImageWrapper, itertools.izip(submissions, itertools.repeat(targetSubreddit)))
        for result in results:
            if result:
                count -= 1
        daysAgo+=1
    pool.close()

def getImageWrapper(args):
    return imgur.getImage(*args)

def main():
    if len(sys.argv) >= 3:
        targetSubreddit = sys.argv[1]
        limit = int(sys.argv[2])
        feed_type = "hot"
        if(len(sys.argv) > 3):
            feed_type = sys.argv[3]
        getImgurImages(targetSubreddit, limit, feed_type)
    else:
        print "Usage: python redditd.py (targetSubreddit) (Limit) (new/hot/rising)"
        exit(0)

if __name__ == '__main__':
    start_time = time.time()
    freeze_support()
    main()
    logging.info("--- %s seconds ---", (time.time() - start_time))
