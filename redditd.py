#
# Now uses task queue :)
# Removed PRAW requirement
# NEED to fix process shutdown issue (Tasks stay running after ctrl-c)
#

from multiprocessing import Process, Queue, current_process, freeze_support, cpu_count
import time, random, os, imgur, datetime, sys, reddit

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

    # Start worker processes
    for i in range(NUMBER_OF_PROCESSES):
        process = Process(target=worker, args=(task_queue, None))
        PROCESSES.append(process)
        process.start()

    for submission in reddit.get_submissions(targetSubreddit, limit):
        task_queue.put((submission, min_score, targetSubreddit))

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
        print "Usage: python redditd.py (targetSubreddit) (Limit)"
        exit(0)

if __name__ == '__main__':
    start_time = time.time()
    freeze_support()
    main()
    print("--- %s seconds ---" % (time.time() - start_time))
