/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.dialogs.DialogPropertyText;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 *
 * @author Christophe
 */
public class SList implements IPropertyMore
{
    public String mText = "";
    
    public SList(String text)
    {
        mText = text;
    }
    
    @Override
    public String toString()
    {
        return mText;
    }
    
    @Override
    public Dialog createDialog(Frame owner)
    {
        DialogPropertyText d = new DialogPropertyText(owner, true);
        d.setSize(600, 555);
        d.setModal(true);
        d.setTitle("Set Text");
        d.setResizable(false);
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        d.setLocationRelativeTo(owner);
        d.setEditingInfo(mText);
        return d;
    }
    
}
