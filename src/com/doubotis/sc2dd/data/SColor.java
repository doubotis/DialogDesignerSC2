/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.dialogs.DialogPropertyColor;
import com.doubotis.sc2dd.dialogs.DialogPropertyImage;
import com.doubotis.sc2dd.util.ColorUtils;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.HashMap;
import javax.swing.WindowConstants;

/**
 *
 * @author Christophe
 */
public class SColor implements SResource, IProperty, IPropertyMore
{
    public static final SColor WHITE = new SColor(Color.WHITE);
    
    private Color color;
    
    public SColor(Color c)
    {
        color = c;
    }
    
    public SColor(String hexString)
    {
        color = ColorUtils.fromString(hexString);
    }
    
    public Color getColor() { return color; }
    
    @Override
    public String toString()
    {
        if (color == null)
            return "Unknown Color";
        
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        
        return red + "," + green + "," + blue;
    }
    
    @Override
    public HashMap<String, Object> loadProperties()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("color", color);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map)
    {
        color = (Color)map.get("color");
    }

    @Override
    public Dialog createDialog(Frame owner)
    {
        DialogPropertyColor dc = new DialogPropertyColor(null, true);
        return dc;
    }
    
}
