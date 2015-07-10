/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.dialogs.DialogPropertyImage;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.HashMap;
import javax.swing.WindowConstants;

/**
 *
 * @author Christophe
 */
public class SImage implements SResource, IProperty, IPropertyMore
{
    public static final SImage NONE = new SImage();
    
    public String imagePath;
    public String mod;
    public SSize imageSize;
    
    private SImage()
    {
        this.imagePath = null;
        this.mod = null;
    }
    
    public SImage(String mod, String imagePath)
    {
        this.imagePath = imagePath;
        this.mod = mod;
    }
    
    @Override
    public String toString()
    {
        if (this.imagePath == null)
            return "No Image";
        
        return "Image (" + imageSize.width + "x" + imageSize.height + ")";
    }
    
    @Override
    public HashMap<String, Object> loadProperties()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("imagePath", imagePath);
        map.put("mod", mod);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map)
    {
        imagePath = (String)map.get("imagePath");
        mod = (String)map.get("mod");
    }

    @Override
    public Dialog createDialog(Frame owner)
    {
        DialogPropertyImage d = new DialogPropertyImage(owner, true);
        d.setSize(600, 500);
        d.setModal(true);
        d.setTitle("Set Image");
        d.setResizable(false);
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        d.setLocationRelativeTo(owner);
        d.setEditingInfo(this);
        return d;
    }
    
}
