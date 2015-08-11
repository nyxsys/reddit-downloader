/*
 * Imgur single image non direct link support for reddit downloader
 */
package sites;

import sites.manager.Site;
import sites.manager.Sites;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sites.manager.SitePlugin;

/**
 *
 * @author Jacob
 */
//@SitePlugin
public class Imgur implements Site {
    
    Pattern p = Pattern.compile(".*?\\:\\/\\/imgur\\.com(?!(\\/a\\/)).*?");
    
    @Override
    public boolean fitsURLPattern(String url) {
        Matcher m = p.matcher(url);
        return m.matches() && !url.contains(".gifv");
    }
    
    @Override
    public List<String> findImages(String url) {
        List<String> urls = new ArrayList<>();
        try {
            Document doc = Sites.getDocumentFromSite(url);
            Element el = doc.getElementsByClass("image").get(0);
            Element imageTag = el.getElementsByTag("img").get(0);
            String imageUrl = imageTag.attr("src");
            urls.add("http:" + imageUrl);
        } catch (Exception ex) {
            System.out.println("Exception on url: " + url);
            return urls;
        }
        return urls;
    }
    
    @Override
    public String toString() {
        return "Imgur Site Instance";
    }
    
}
