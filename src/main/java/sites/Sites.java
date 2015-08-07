/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sites;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        System.out.println(annotated);
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
