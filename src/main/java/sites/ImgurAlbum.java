/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sites;

import sites.manager.Site;
import sites.manager.SitePlugin;
import sites.manager.Sites;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jacob
 */
public class ImgurAlbum implements Site {

    Pattern p = Pattern.compile(".*?\\:\\/\\/imgur\\.com\\/a\\/.*?");

    @Override
    public boolean fitsURLPattern(String url) {
        Matcher m = p.matcher(url);
        return m.matches();
    }

    @Override
    public List<String> findImages(String url) {
        List<String> urls = new ArrayList<>();
        try {
            Document doc = Sites.getDocumentFromSite(url);
            Elements els = doc.getElementsByClass("album-view-image-link");
            for (Element imageClass : els) {
                Element imageTag = imageClass.getElementsByTag("a").get(0);
                String imageUrl = imageTag.attr("href");
                urls.add("http:" + imageUrl);
            }
            //System.out.println(urls);
        } catch (Exception ex) {
            ex.printStackTrace();
            return urls;
        }
        return urls;
    }

    @Override
    public String toString() {
        return "Imgur Site Instance";
    }

}
