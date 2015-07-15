/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.util;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Christophe
 */
public class ResourceUtils {
    
    public static ImageIcon getImageResource(String name)
    {
        URL url = ResourceUtils.class.getResource("../res/" + name);
        if (url == null)
            return null;
        
        ImageIcon ic = new ImageIcon(url);
        return ic;
    }
    
    public static URL getURLResource(String name)
    {
        URL url = ResourceUtils.class.getResource("../res/" + name);
        if (url == null)
            return null;
        
        return url;
    }
}
