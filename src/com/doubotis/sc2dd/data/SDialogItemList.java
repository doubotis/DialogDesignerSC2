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
public class SDialogItemList extends SDialogItem {

    public SList list;
    
    public SDialogItemList(SDialog dialog)
    {
        this("List" + App.getApp().getOpenedProject().makeNewUniqueName(), dialog);
    }
    
    public SDialogItemList(String name, SDialog dialog) {
        super(name, dialog);
        
        list = new SList("");
    }

    @Override
    public HashMap<String, Object> loadProperties() {
        HashMap<String, Object> map = super.loadProperties();
        map.put("data.list", list);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map) {
        super.saveProperties(map); //To change body of generated methods, choose Tools | Templates.
        
        list = (SList)map.get("data.list");
    }
    
    @Override
    public ImageIcon getIconImage() {
        return new ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_listbox.png"));
    }
    
    
    
}
