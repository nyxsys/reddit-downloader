import requests, time
from datetime import datetime
from bs4 import BeautifulSoup
def main():
    username = "nitethemooks"
    password = "testtest"    
    session = requests.Session()
    response = session.get("https://twitter.com/login")
    soup1 = BeautifulSoup(response.text, 'html.parser')
    loginToken = soup1.find('input', {'name': 'authenticity_token'})
    token = loginToken.get('value')
    response = session.post("https://twitter.com/sessions", data={
        'session[username_or_email]': username,
        'session[password]': password,
        'authenticity_token': token
    })
    soup2 = BeautifulSoup(response.text, 'html.parser')
    print soup2.title
    print soup2.find_all(class_="ProfileCardStats-statValue")
    return 0
    if __name__ == "__main__":
        start_time = time.time()
        exit_code = main()
        if(exit_code == 0):
            print "--- Completed in %d seconds ---" % (time.time() - start_time) 
            
main()