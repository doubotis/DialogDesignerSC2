/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import java.util.HashMap;

/**
 *
 * @author Christophe
 */
public class SSize implements IProperty
{
    public int width;
    public int height;
    
    public SSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public String toString()
    {
        return "[" + width + "," + height + "]";
    }

    @Override
    public HashMap<String, Object> loadProperties()
    {
        HashMap<String, Object> map = new HashMap<>();
        map.put("width", width);
        map.put("height", height);
        return map;
    }
    
    @Override
    public void saveProperties(HashMap<String, Object> map)
    {
        width = (int)map.get("width");
        height = (int)map.get("height");
    }
}
