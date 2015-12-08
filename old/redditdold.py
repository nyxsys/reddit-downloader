#
# Now uses task queue :)
#

from multiprocessing import Process, Queue, current_process, freeze_support, cpu_count
import time, random, os, imgur, datetime, praw, sys

def unix_time(dt):
    epoch = datetime.datetime.utcfromtimestamp(0)
    delta = dt - epoch
    return delta.total_seconds()

def getSubmissionsFromDaysAgo(targetSubreddit, daysAgo, limit=25):
    try:
        now = datetime.datetime.utcnow()
        now -= datetime.timedelta(hours=4,days=daysAgo)
        #print now
        now2 = now - datetime.timedelta(hours=now.hour,minutes=now.minute,seconds=now.second)
        #print now2
        now3 = now + datetime.timedelta(hours=(23 - now.hour),minutes=(59-now.minute),seconds=(59-now.second))
        #print now3
        lower = unix_time(now2)
        upper = unix_time(now3)
        #query = '(and author:"%s" (and timestamp:%d..%d))' % (usermode, lower, upper)
        query = 'timestamp:%d..%d' % (lower, upper)
        r = praw.Reddit(user_agent='CHANGE THIS TO A UNIQUE VALUE') # Note: Be sure to change the user-agent to something unique.
        searchresults = list(r.search(query, subreddit=targetSubreddit, sort='new', limit=limit, syntax='cloudsearch'))
        return searchresults
        #print len(searchresults)
    except Exception as exception:
        print exception
        return []

#
# Function run by worker processes
#
def worker(input, output):
    for args in iter(input.get, 'STOP'):
        doWork(args)
#
# Function used to calculate result
#
def doWork(args):
    try:
        imgur.findImage(*args)
    except KeyboardInterrupt:
        exit(0)

def getImgurImages(targetSubreddit, limit=25, min_score=0):
    NUMBER_OF_PROCESSES = cpu_count()
    PROCESSES = []

    # Create queues
    task_queue = Queue()

    #Print and make directory for files
    print "[ --- Searching", limit, "newest posts from", targetSubreddit, "for imgur files --- ]"
    if not os.path.exists(targetSubreddit):
        os.makedirs(targetSubreddit)

    # Start worker processes
    for i in range(NUMBER_OF_PROCESSES):
        process = Process(target=worker, args=(task_queue, None))
        PROCESSES.append(process)
        process.start()


    daysAgo = 0
    gotCount = 0
    while gotCount <= limit:
        submissions = getSubmissionsFromDaysAgo(targetSubreddit, daysAgo, limit)
        for submission in submissions:
            gotCount+=1
            task_queue.put((submission, min_score, targetSubreddit))
        daysAgo+=1

    # Tell child processes to stop
    for i in range(NUMBER_OF_PROCESSES):
        task_queue.put('STOP')

    # Wait for workers
    for i in range(NUMBER_OF_PROCESSES):
        PROCESSES[i].join()

def main():
    if len(sys.argv) == 3:
        targetSubreddit = sys.argv[1]
        limit = int(sys.argv[2])
        getImgurImages(targetSubreddit, limit)
    else:
        print "Nope"
        exit(0)

if __name__ == '__main__':
    start_time = time.time()
    freeze_support()
    main()
    print("--- %s seconds ---" % (time.time() - start_time))