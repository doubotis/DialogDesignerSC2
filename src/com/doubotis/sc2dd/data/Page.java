/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author Christophe
 */
public class Page extends SObject implements IProperty
{
    public String name;
    private ArrayList<SObject> mObjects = new ArrayList<SObject>();
    
    public Page(String name)
    {
        this.name = name;
        this.guid = Guid.EMPTY;
    }
    
    public List<SObject> getObjects() { return mObjects; }
    
    @Override
    public HashMap<String, Object> loadProperties()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("patronym.guid", guid);
        map.put("patronym.name", name);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map)
    {
        name = (String)map.get("patronym.name");
        guid = (Guid)map.get("patronym.guid");
    }

    @Override
    public ImageIcon getIconImage() {
        return null;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    
    
}
