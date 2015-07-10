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
public class PropertyObject {
    private HashMap<String, Object> mMap;
    
    public PropertyObject(HashMap<String, Object> map)
    {
        mMap = map;
    }
    
    public HashMap<String, Object> getMap()
    {
        return mMap;
    }
}
