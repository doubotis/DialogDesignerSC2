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
public class SRect implements IProperty
{
    public SPoint origin;
    public SSize size;
    
    public SRect(SPoint origin, SSize size)
    {
        this.origin = origin;
        this.size = size;
    }
    
    public SRect(int x, int y, int width, int height)
    {
        this(new SPoint(x,y), new SSize(width, height));
    }
    
    public SRect(SPoint origin, int width, int height)
    {
        this(origin, new SSize(width, height));
    }
    
    public SRect(int x, int y, SSize size)
    {
        this(new SPoint(x,y), size);
    }
    
    @Override
    public String toString()
    {
        return "[" + origin.x + "; " + origin.y + "; " + size.width + "; " + size.height + "]";
    }

    @Override
    public HashMap<String, Object> loadProperties()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("origin", origin.loadProperties());
        map.put("size", size.loadProperties());
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map)
    {
        origin = (SPoint)map.get("origin");
        size = (SSize)map.get("size");
    }
}
