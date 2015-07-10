/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.util;

import java.awt.Color;

/**
 *
 * @author Christophe
 */
public class ColorUtils
{
    public static Color fromString(String s)
    {
        // Is it a hex or a R,G,B ?
        String[] components = s.split(",");
        if (components.length == 3)
        {
            // By R,G,B ?
            Color c = new Color(Integer.parseInt(components[0].trim()), 
                                Integer.parseInt(components[1].trim()), 
                                Integer.parseInt(components[2].trim()));
            return c;
        }
        else
        {
            if (s.length() > 6)
            {
                // By Hex with Alpha
                Color c = Color.decode("0x" + s.substring(0, 6));
                return c;
            }
            else if (s.length() == 6)
            {
                // By Hex
                Color c = Color.decode("0x" + s);
                return c;
            }
            else
            {
                // Default color.
                Color c = Color.WHITE;
                return c;
            }
        }
    }
    
    public static String fromColor(Color c)
    {
        return Integer.toHexString(c.getRGB());
    }
}
