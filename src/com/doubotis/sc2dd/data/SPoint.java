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
public class SPoint implements IProperty
{
    public int x;
    public int y;
    
    public SPoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString()
    {
        return "[" + x + "," + x + "]";
    }

    @Override
    public HashMap<String, Object> loadProperties()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("x", x);
        map.put("y", y);
        return map;
    }
    
    @Override
    public void saveProperties(HashMap<String, Object> map)
    {
        x = (int)map.get("x");
        y = (int)map.get("y");
    }
    
    
}
