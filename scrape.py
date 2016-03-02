from bs4 import BeautifulSoup
import requests, certifi, urllib3
#nitethemooks
#testtest

user = "nitethemooks"


def main():
    global user
    login_url = "https://twitter.com/login"
    session_requests = requests.session()
    getpage(session_requests, login_url)
    result = session_requests.get(
    "https://twitter.com/{0}/following".format(user),
    headers = dict(referer=login_url))
    soup = BeautifulSoup(result.text,"html.parser")
    #followers = soup.find_all(attrs={"class":"ProfileCard-screennameLink"})
    print(soup.title)
    
    
def getpage(sr, url):
    payload = {}
    
    result = sr.get(url)
    soup = BeautifulSoup(result.text,'html.parser')
    payload["session[username_or_email]"] = "nitethemooks"
    payload["session[password]"] = "testtest"
    payload["authenticity_token"] = str(soup.find(attrs={"name":"authenticity_token"})["value"])
    payload["ui_metrics_seed"] = str(soup.find(attrs={"name":"ui_metrics_seed"})["value"])
    print(payload)
    result = sr.post(
    url,
    data=payload,
    headers = dict(referer=url)
    )
    print(result.status_code)
    soup = BeautifulSoup(result.text,"html.parser")
    print(soup.title)
    return sr
main();