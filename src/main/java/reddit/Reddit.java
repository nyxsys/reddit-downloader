/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reddit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

/**
 *
 * @author Jacob
 */
public class Reddit {
    
    private final String userAgent;
    
    public Reddit(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Gets a list of urls from posts in the subreddit
     *
     * @param subreddit
     * @param limit
     * @return
     */
    public List<String> getURLsFromSubreddit(String subreddit, int limit) {
        ArrayList<String> urls = new ArrayList<>();
        String after = null;
        Main.output("Finding data for downloading...");
        while (urls.size() < limit) {
            after = this.addURLsAfter(subreddit, limit, after, urls);
        }
        return urls;
    }
    
    private String addURLsAfter(String subreddit, int limit, String after, List<String> urls) {
        String nextAfter = null;
        try {
            Thread.sleep(2000); //Sleep first
            String urlString = "";
            urlString += "https://www.reddit.com/r/" + subreddit + "/.json";
            urlString += "?limit=" + limit;
            if (after != null) {
                urlString += "&after=" + after;
            }
            String doc = Jsoup.connect(urlString).userAgent(this.userAgent)
                    .ignoreContentType(true).timeout(5000).get().body().text();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = ((JSONObject) parser.parse(doc));
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray submissions = (JSONArray) data.get("children");
            for (Object submission : submissions) {
                JSONObject jsonSub = (JSONObject) submission;
                String url = (String) ((JSONObject) jsonSub.get("data")).get("url");
                urls.add(url);
            }
            nextAfter = (String) data.get("after");
            //System.out.println(after);
        } catch (IOException | ParseException | InterruptedException ex) {
            return nextAfter;
        }
        return nextAfter;
    }
}
