/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.app;

import com.doubotis.sc2dd.util.PreferenceUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christophe
 */
public class Preferences
{
    private static Preferences SINGLETON = null;
    private static final String PROP_FILENAME = "config.properties";
    private Properties mProps;
    
    /*public static Preferences getPreferences()
    {
        if (SINGLETON == null)
            SINGLETON = new Preferences();
        return SINGLETON;
    }*/
    
    public Preferences()
    {
        mProps = new Properties();
        
        try
        {
            InputStream is = new FileInputStream(new File(PreferenceUtils.getUserDataDirectory() + PROP_FILENAME));
            mProps.load(is);
            
        } catch (Exception e)
        {
            
        }
    }
    
    public Editor editor()
    {
        Editor ed = new Editor(this);
        return ed;
    }
    
    public int getInt(String key, int defaultValue)
    {
        String a = mProps.getProperty(key, "" + defaultValue);
        
        try
        {
            Integer i = Integer.parseInt(a);
            return i;
            
        } catch (NumberFormatException nfe)
        {
            return defaultValue;
        }
    }
    
    public double getDouble(String key, double defaultValue)
    {
        String a = mProps.getProperty(key, "" + defaultValue);
        
        try
        {
            Double i = Double.parseDouble(a);
            return i;
            
        } catch (NumberFormatException nfe)
        {
            return defaultValue;
        }
    }
    
    public String getString(String key, String defaultValue)
    {
        String a = mProps.getProperty(key, "" + defaultValue);
        return a;
    }
    
    public List<String> getArrayString(String key, List<String> defaultValues)
    {
        String res = mProps.getProperty(key);
        if (res == null)
            return defaultValues;
        
        String[] array = res.split("\\|");
        List<String> theList = new ArrayList<String>();
        for (String s : array)
        {
            if (!s.equals(""))
                theList.add(s);
        }
        return theList;
    }
    
    public static class Editor
    {
        private Preferences mPrefs;
        
        private Editor(Preferences p)
        {
            mPrefs = p;
        }
        
        public void putInt(String key, int value)
        {
            mPrefs.mProps.setProperty(key, "" + value);
        }
        
        public void putDouble(String key, double value)
        {
            mPrefs.mProps.setProperty(key, "" + value);
        }
        
        public void putString(String key, String value)
        {
            mPrefs.mProps.setProperty(key, "" + value);
        }
        
        public void putArrayString(String key, List<String> array)
        {
            String value = "";
            for (String s : array)
                value += s + "|";
            mPrefs.mProps.setProperty(key, value);
        }
        
        public void remove(String key)
        {
            mPrefs.mProps.remove(key);
        }
        
        public void commit()
        {
                File dir = new File(PreferenceUtils.getUserDataDirectory());
                if (!dir.exists())
                    dir.mkdirs();
                File f = new File(PreferenceUtils.getUserDataDirectory() + PROP_FILENAME);
                if(!f.exists())
                {
                    try {
                        f.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
                OutputStream os;
            try {
                os = new FileOutputStream(f);
                mPrefs.mProps.store(os, null);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
