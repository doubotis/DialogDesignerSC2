/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.util;

import java.io.File;

/**
 *
 * @author Christophe
 */
public class PreferenceUtils
{
    public static String getUserDataDirectory()
    {
        return System.getProperty("user.home") + File.separator + ".ssc2dd" + File.separator;
    }
    
}
