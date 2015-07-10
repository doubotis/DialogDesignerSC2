/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.locales;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Christophe
 */
public class LocaleUtils {
    
    public static String translateInto(String dictionaryFile, String key, String locale)
    {
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle("com.doubotis.sc2dd.locales." + dictionaryFile, new Locale(locale));
            return bundle.getString(key);
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
}
