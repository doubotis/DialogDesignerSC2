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
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Christophe
 */
public class SObjectTreeCellRenderer extends DefaultTreeCellRenderer
{
    @Override
    public Component getTreeCellRendererComponent(JTree tree,
        Object value, boolean selected, boolean expanded,
        boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected,expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            SObject o = (SObject)node.getUserObject();
            if (o instanceof Page)
            {
                setIcon(o.getIconImage());
                Page p = (Page)o;
                setText("   " + p.name);
            }
            else
            {
                setIcon(o.getIconImage());
            }
            return this;
    }
}
