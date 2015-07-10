/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.app.App;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author Christophe
 */
public class SDialogItemButton extends SDialogItem {

    public SImage image;
    public SImage imageOver;
    public SImageType imageType;
    
    public SDialogItemButton(SDialog dialog)
    {
        this("Button" + App.getApp().getOpenedProject().makeNewUniqueName(), dialog);
    }
    
    public SDialogItemButton(String name, SDialog dialog) {
        super(name, dialog);
        
        text = new SText("Button");
        image = SImage.NONE;
        imageOver = SImage.NONE;
        imageType = SImageType.Normal;
    }

    @Override
    public HashMap<String, Object> loadProperties() {
        HashMap<String, Object> map = super.loadProperties();
        map.put("data.image", image);
        map.put("data.imageOver", imageOver);
        map.put("data.imageType", imageType);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map) {
        super.saveProperties(map); //To change body of generated methods, choose Tools | Templates.
        
        image = (SImage)map.get("data.image");
        imageOver = (SImage)map.get("data.imageOver");
        imageType = (SImageType)map.get("data.imageType");
    }
    
    @Override
    public ImageIcon getIconImage() {
        return new ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_button.png"));
    }
    
    
    
}
