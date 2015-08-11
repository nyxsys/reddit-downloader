/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sites.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.reflections.Reflections;

/**
 *
 * @author Jacob
 */
public class Sites {

    public static List<Site> sites;

    public static void initialize() {
        sites = new ArrayList<>();
        setupSites();
    }

    private static void setupSites() {
        Reflections reflections = new Reflections("sites");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SitePlugin.class);
        for (Class c : annotated) {
            Constructor con;
            try {
                con = c.getConstructor();
                Site site = (Site) con.newInstance();
                sites.add(site);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Sites.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * If site is supported, pass to site handler and get images(s)
     *
     * @param url
     */
    public static void downloadURL(String url) {
        for (Site site : sites) {
            if (site.fitsURLPattern(url)) {
                for (String imageUrl : site.findImages(url)) {
                    downloadImage(imageUrl);
                }
                return;
            }
        }
    }

    public static Document getDocumentFromSite(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    private static void downloadImage(String url) {
        String folder = "images";
        File dir = new File(folder);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            //Open a URL Stream
            Connection.Response resultImageResponse = Jsoup.connect(url)
                    .ignoreContentType(true).execute();
            FileOutputStream out = (new FileOutputStream(new java.io.File(folder + File.separatorChar + Math.random() + ".jpg")));
            out.write(resultImageResponse.bodyAsBytes());
            out.close();
        } catch (Exception e) {
            //Nope
        }
    }
}
