import datetime, re, requests, glob, os, urllib
from bs4 import BeautifulSoup

def downloadImage(targetSubreddit, imageUrl, localFileName):
    filedir = "downloads" + "/" + targetSubreddit + "/"
    filepath = filedir + localFileName
    try:
        if not os.path.isfile(filepath):
            if not os.path.exists(filedir):
                os.makedirs(filedir)
            testfile = urllib.URLopener()
            testfile.retrieve(imageUrl, filepath)
    except Exception as exception:
        print exception

#Worker process function
def findImage(submission, min_score, targetSubreddit):
    imgurUrlPattern = re.compile(r'(http://i.imgur.com/(.*))(\?.*)?')
    imageUrl = None

    try:
        if "imgur.com/" not in submission.url:
            return
        if submission.score < min_score:
            return # skip submissions that haven't even reached 100 (thought this should be rare if we're collecting the "hot" submission)

        # This is an album submission.
        if 'http://imgur.com/a/' in submission.url:
            try:

                albumId = submission.url[len('http://imgur.com/a/'):]
                htmlSource = requests.get(submission.url).text

                soup = BeautifulSoup(htmlSource, "html.parser")
                matches = soup.select('.album-view-image-link a')
                for match in matches:
                    imageUrl = match['href']
                    if '?' in imageUrl:
                        imageFile = imageUrl[imageUrl.rfind('/') + 1:imageUrl.rfind('?')]
                    else:
                        imageFile = imageUrl[imageUrl.rfind('/') + 1:]
                    localFileName = '%s_%s_album_%s_imgur_%s' % (targetSubreddit, submission.id, albumId, imageFile)
                    downloadImage(targetSubreddit, 'http:' + match['href'], localFileName)
            except Exception as exception:
                print "Exception url: %s" % submission.url
                print exception

        # The URL is a direct link to the image.
        elif 'http://i.imgur.com/' in submission.url:
            try:
                mo = imgurUrlPattern.search(submission.url) # using regex here instead of BeautifulSoup because we are pasing a url, not html

                imgurFilename = mo.group(2)
                imageUrl = submission.url
                if '?' in imgurFilename:
                    # The regex doesn't catch a "?" at the end of the filename, so we remove it here.
                    imgurFilename = imgurFilename[:imgurFilename.find('?')]
                elif '.gifv' in imgurFilename:
                    htmlSource = requests.get(submission.url).text # download the image's page
                    soup = BeautifulSoup(htmlSource, "html.parser")
                    imageUrl = soup.select('.controls')[0].find_all('a')[0]['href']
                    imgurFilename = imgurFilename[:-1]
                    if imageUrl.startswith('//'):
                        # if no schema is supplied in the url, prepend 'http:' to it
                        imageUrl = 'http:' + imageUrl

                localFileName = '%s_%s_album_None_imgur_%s' % (targetSubreddit, submission.id, imgurFilename)
                downloadImage(targetSubreddit, imageUrl, localFileName)
            except Exception as exception:
                print "Exception direct link url: %s" % submission.url
                print exception

        # This is an Imgur page with a single image.
        elif 'http://imgur.com/' in submission.url:
            try:
                htmlSource = requests.get(submission.url).text # download the image's page
                soup = BeautifulSoup(htmlSource, "html.parser")
                imageUrl = soup.select('.post-image')[0].find_all('img')[0]['src']
                if imageUrl.startswith('//'):
                    # if no schema is supplied in the url, prepend 'http:' to it
                    imageUrl = 'http:' + imageUrl
                imageId = imageUrl[imageUrl.rfind('/') + 1:imageUrl.rfind('.')]

                if '?' in imageUrl:
                    imageFile = imageUrl[imageUrl.rfind('/') + 1:imageUrl.rfind('?')]
                else:
                    imageFile = imageUrl[imageUrl.rfind('/') + 1:]

                localFileName = '%s_%s_album_None_imgur_%s' % (targetSubreddit, submission.id, imageFile)
                downloadImage(targetSubreddit, imageUrl, localFileName)
            except Exception as exception:
                print "Exception url: %s" % submission.url
                print exception
    except (KeyboardInterrupt, SystemExit):
            print "Exiting..."
