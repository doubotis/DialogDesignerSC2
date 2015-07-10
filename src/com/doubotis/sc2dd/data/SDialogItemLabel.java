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
public class SDialogItemLabel extends SDialogItem {

    public SText text;
    public SFontStyle textStyle;
    
    public SDialogItemLabel(SDialog dialog)
    {
        this("Label" + App.getApp().getOpenedProject().makeNewUniqueName(), dialog);
    }
    
    public SDialogItemLabel(String name, SDialog dialog) {
        super(name, dialog);
        
        text = new SText("");
        textStyle = new SFontStyle("DefaultTextStyle");
    }

    @Override
    public HashMap<String, Object> loadProperties() {
        HashMap<String, Object> map = super.loadProperties();
        map.put("data.text", text);
        map.put("data.textStyle", textStyle);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map) {
        super.saveProperties(map); //To change body of generated methods, choose Tools | Templates.
        
        text = (SText)map.get("data.text");
        textStyle = (SFontStyle)map.get("data.textStyle");
    }
    
    @Override
    public ImageIcon getIconImage() {
        return new ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_label.png"));
    }
    
    
    
}
