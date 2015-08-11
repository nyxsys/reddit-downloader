/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sites.manager;

import java.util.List;

/**
 *
 * @author Jacob
 */
public interface Site {

    public boolean fitsURLPattern(String url);

    public List<String> findImages(String url);
}
