'''
Initial stages of new python api caller
'''
import json, urllib2, time, praw, datetime
from collections import namedtuple

Submission = namedtuple('Submission', 'url id score author')
after = None

def get_json(subreddit, after=None, feed_type="hot"):
	try:
		#time.sleep(2)
		opener = urllib2.build_opener()
		opener.addheaders = [('User-agent', 'Cool Cat App')]
		url = ""
		if after == None:
			url = "https://www.reddit.com/r/"+ subreddit +"/%s.json" % feed_type
		else:
			url = "https://www.reddit.com/r/"+ subreddit +"/%s.json?after=%s" % (feed_type, after)
		return json.load(opener.open(url))
	except Exception as exception:
		logging.error("%s at url: %s", exception, url)

def get_submissions(subreddit, count, feed_type="hot"):
	global after
	submissions = []
	while(len(submissions) < count):
		json = get_json(subreddit, after, feed_type)
		for submission in json["data"]["children"]:
			submissions.append(Submission(submission["data"]["url"],
			submission["data"]["id"], submission["data"]["score"], submission["data"]["author"]))
		after = json["data"]["after"]
	return submissions

def unix_time(dt):
    epoch = datetime.datetime.utcfromtimestamp(0)
    delta = dt - epoch
    return delta.total_seconds()

def getSubmissionsFromDaysAgo(targetSubreddit, daysAgo, limit=25, feed_type="hot"):
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
        searchresults = list(r.search(query, subreddit=targetSubreddit, sort=feed_type, limit=limit, syntax='cloudsearch'))
        return searchresults
        #print len(searchresults)
    except Exception as exception:
        print exception
        return []
