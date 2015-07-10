/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

import com.doubotis.sc2dd.data.Page;
import com.doubotis.sc2dd.data.SDialog;
import com.doubotis.sc2dd.data.SDialogItem;
import com.doubotis.sc2dd.data.SObject;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Christophe
 */
public class SObjectAdapter extends DefaultTreeModel
{
    private Page mPage;
    private boolean mReloading = false;
    
    public SObjectAdapter(Page page) {
        super(new DefaultMutableTreeNode(page));
        mPage = page;
    }
    
    public boolean isReloading() { return mReloading; }
    
    public Object findTreeNodeForSObject(SObject o)
    {
        if (o instanceof Page)
        {
            return getRoot();
        }
        else if (o instanceof SDialog)
        {
            for (int i=0; i < getChildCount(getRoot()); i++)
            {
                DefaultMutableTreeNode n = (DefaultMutableTreeNode)getChild(getRoot(), i);
                if (n.getUserObject() == o)
                    return n;
            }
        }
        else if (o instanceof SDialogItem)
        {
            SDialogItem di = (SDialogItem)o;
            SDialog dialog = di.parentDialog;
            for (int i=0; i < getChildCount(getRoot()); i++)
            {
                DefaultMutableTreeNode n = (DefaultMutableTreeNode)getChild(getRoot(), i);
                if (n.getUserObject() == dialog)
                {
                    for (int j=0; j < getChildCount(n); j++)
                    {
                        DefaultMutableTreeNode n2 = (DefaultMutableTreeNode)getChild(n, j);
                        if (n2.getUserObject() == di)
                            return n2;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void reload()
    {
        mReloading = true;
        
        // Refresh all elements.
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)getRoot();
        root.removeAllChildren();
        
        List<SDialog> dialogs = getDialogs();
        for (int i=0; i < dialogs.size(); i++)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dialogs.get(i));
           
            List<SDialogItem> dialogItems = getDialogItemsForDialog(dialogs.get(i));
            for (int j=0; j < dialogItems.size(); j++)
            {
                node.add(new DefaultMutableTreeNode(dialogItems.get(j)));
            }
            root.add(node);
        }
        
        super.reload();
        mReloading = false;
    }

    @Override
    public Object getRoot() {
        return super.getRoot();
    }
    
    private int getRootCount()
    {
        return getDialogs().size();
    }
    
    private List<SDialog> getDialogs()
    {
        ArrayList<SDialog> list = new ArrayList<SDialog>();
        for (int i=0; i < mPage.getObjects().size(); i++)
        {
            if (mPage.getObjects().get(i) instanceof SDialog)
                list.add((SDialog)mPage.getObjects().get(i));
        }
        return list;
    }
    
    private List<SDialogItem> getDialogItemsForDialog(SDialog dialog)
    {
        ArrayList<SDialogItem> list = new ArrayList<SDialogItem>();
        for (int i=0; i < mPage.getObjects().size(); i++)
        {
            if (mPage.getObjects().get(i) instanceof SDialog)
                continue;
            
            SDialogItem di = (SDialogItem)mPage.getObjects().get(i);
            if (di.parentDialog == dialog)
                list.add(di);
        }
        return list;
    }
    
}
