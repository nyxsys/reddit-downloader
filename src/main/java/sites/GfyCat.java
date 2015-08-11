/*
 * Gfycat support for reddit downloader :)
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
@SitePlugin
public class GfyCat implements Site {

    Pattern p = Pattern.compile(".*?\\:\\/\\/gfycat\\.com.*?");
    Pattern imageUrl = Pattern.compile(".*?\\:*?\\/\\/gfycat\\.com\\/(\\S+?)");

    @Override
    public boolean fitsURLPattern(String url) {
        Matcher m = p.matcher(url);
        return m.matches();
    }

    @Override
    public List<String> findImages(String url) {
        List<String> urls = new ArrayList<>();
        try {
            Matcher m = imageUrl.matcher(url);
            if (m.matches() && m.groupCount() > 0) {
                String imageName = m.group(1);
                urls.add("http://giant.gfycat.com/" + imageName + ".gif");
            }
        } catch (Exception e) {
            //Nope
            return urls;
        }
        return urls;
    }

    @Override
    public String toString() {
        return "Imgur Site Instance";
    }

}
