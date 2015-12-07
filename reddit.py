'''
Initial stages of new python api caller
'''
import json, urllib2, time
from collections import namedtuple

Submission = namedtuple('Submission', 'url id score')
after = None

def get_json(subreddit, after=None, feed_type="hot"):
	#time.sleep(2)
	opener = urllib2.build_opener()
	opener.addheaders = [('User-agent', 'Cool Cat App')]
	url = ""
	if after == None:
		url = "https://www.reddit.com/r/"+ subreddit +"/%s.json" % feed_type
	else:
		url = "https://www.reddit.com/r/"+ subreddit +"/%s.json?after=%s" % (feed_type, after)
	return json.load(opener.open(url))

def get_submissions(subreddit, count, feed_type="hot"):
	global after
	submissions = []
	while(len(submissions) < count):
		json = get_json(subreddit, after, feed_type)
		for submission in json["data"]["children"]:
			submissions.append(Submission(submission["data"]["url"], submission["data"]["id"], submission["data"]["score"]))
		after = json["data"]["after"]
	return submissions
