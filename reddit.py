'''
Initial stages of new python api caller
'''
import json, urllib2, time
from collections import namedtuple

Submission = namedtuple('Submission', 'url id score')

def get_json(subreddit, after=None):
	#time.sleep(2)
	opener = urllib2.build_opener()
	opener.addheaders = [('User-agent', 'Cool Cat App')]
	url = ""
	if after == None:
		url = "https://www.reddit.com/r/"+ subreddit +"/new.json"
	else:
		url = "https://www.reddit.com/r/"+ subreddit +"/new.json?after=" + after
	return json.load(opener.open(url))

def get_submissions(subreddit, count):
	submissions = []
	after = None
	while(len(submissions) < count):
		json = get_json(subreddit, after)
		for submission in json["data"]["children"]:
			submissions.append(Submission(submission["data"]["url"], submission["data"]["id"], submission["data"]["score"]))
		after = json["data"]["after"]
	#print len(submissions)
	return submissions
