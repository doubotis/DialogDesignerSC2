/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

import com.doubotis.sc2dd.data.Page;
import com.doubotis.sc2dd.data.SDialog;
import com.doubotis.sc2dd.data.SDialogItem;
import com.doubotis.sc2dd.data.SDialogItemImage;
import com.doubotis.sc2dd.data.SDialogItemLabel;
import com.doubotis.sc2dd.data.SDialogItemList;
import com.doubotis.sc2dd.data.SObject;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Christophe
 */
public class SObjectListCellRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        SObject o = (SObject)value;
        setIcon(o.getIconImage());
        return this;
    }
}
