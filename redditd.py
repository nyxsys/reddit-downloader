from multiprocessing import Process, Queue, current_process, freeze_support, cpu_count
import time, random, os, imgur, datetime, sys, reddit, logging

#Diable logging from requests
logging.getLogger("requests").setLevel(logging.WARNING)

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
    while count > 0:
        for submission in reddit.get_submissions(targetSubreddit, count, feed_type):
            if(imgur.getImage(submission, targetSubreddit)):
                count -= 1

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
    main()
    logging.info("--- %s seconds ---", (time.time() - start_time))
