/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sites;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jacob
 */
public class SiteManager {

    public List<Site> sites;

    public SiteManager() {
        this.sites = new ArrayList<>();

    }

    private void setupSites() {
        this.sites.add(new Site() {
            @Override
            public boolean fitsURLPattern(String url) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    /**
     * If site is supported, pass to site handler and get images(s)
     *
     * @return
     */
    public static List<String> downloadURL() {
        return null;
    }

}
