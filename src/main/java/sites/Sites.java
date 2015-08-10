/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sites;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        //Set<Class<? extends Object>> subTypes = reflections.getSubTypesOf(Object.class);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Site.class);
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
        System.out.println(annotated);
        System.out.println(sites);
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
