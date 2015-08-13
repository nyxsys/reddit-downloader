/*
 * Imgur direct image link support for reddit downloader
 */
package sites;

import sites.manager.Site;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sites.manager.SitePlugin;

/**
 *
 * @author Jacob
 */
@SitePlugin
public class ImgurDirect implements Site {

    Pattern p = Pattern.compile(".*?\\:\\/\\/i\\.imgur\\.com\\/.*?(?!.gifv)");

    @Override
    public boolean fitsURLPattern(String url) {
        Matcher m = p.matcher(url);
        return m.matches();
    }

    @Override
    public List<String> findImages(String url) {
        List<String> urls = new ArrayList<>();
        urls.add(url);
        return urls;
    }

    @Override
    public String toString() {
        return "Imgur Site Instance";
    }

}
