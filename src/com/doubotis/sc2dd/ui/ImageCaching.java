/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.ui;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 *
 * @author Christophe
 */
public class ImageCaching
{
    private static ImageCaching SINGLETON;
    
    private HashMap<String, BufferedImage> mCache = new HashMap<String, BufferedImage>();
    
    public static ImageCaching getCache()
    {
        if (SINGLETON == null)
            SINGLETON = new ImageCaching();
        return SINGLETON;
    }
    
    public void storeImage(String identifier, BufferedImage image)
    {
        mCache.put(identifier, image);
    }
    
    public BufferedImage retreive(String identifier)
    {
        return mCache.get(identifier);
    }
    
    public void clearCache()
    {
        mCache.clear();
    }
}
