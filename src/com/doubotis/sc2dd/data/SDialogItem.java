/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.dialogs.DialogPropertySObject;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;

/**
 *
 * @author Christophe
 */
public class SDialogItem extends SObject implements IProperty, IPropertyMore, IDrawable
{
    public static final SDialogItem NONE = new SDialogItem();
    
    public boolean visibility;
    public SAnchor anchor;
    public SPoint offset;
    public SDialog parentDialog;
    public SDialogItem relativeDialogItem;
    public SSize size;
    public int zChannel;
    public SText text;
    public SText tooltip;
    public Page pageOwner;
    public String name;
    public int transparency;
    
    private SDialogItem()
    {
        this.name = "No Dialog Item";
        this.guid = Guid.EMPTY;
    }
    
    public SDialogItem(SDialog dialog)
    {
        this("DialogItem" + App.getApp().getOpenedProject().makeNewUniqueName(), dialog);
    }
    
    public SDialogItem(String name, SDialog dialog)
    {
        this.name = name;
        this.parentDialog = dialog;
        this.relativeDialogItem = SDialogItem.NONE;
        this.visibility = true;
        this.anchor = SAnchor.TopLeft;
        this.size = new SSize(50, 50);
        this.offset = new SPoint(50, 50);
        this.guid = new Guid(UUID.randomUUID());
        this.text = new SText("");
        this.tooltip = new SText("");
        this.transparency = 0;
        this.zChannel = 512;
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }

    @Override
    public HashMap<String, Object> loadProperties()
    {
        HashMap<String, Object> map = new HashMap<>();
        map.put("behavior.visibility", visibility);
        map.put("disposition.anchor", anchor);
        map.put("disposition.offsetX", offset.x);
        map.put("disposition.offsetY", offset.y);
        map.put("disposition.sizeWidth", size.width);
        map.put("disposition.sizeHeight", size.height);
        map.put("disposition.parentDialog", parentDialog);
        map.put("disposition.relativeDialogItem", relativeDialogItem);
        map.put("disposition.zChannel", zChannel);
        map.put("disposition.transparency", transparency);
        map.put("data.tooltip", tooltip);
        map.put("patronym.guid", guid);
        map.put("patronym.name", name);
        map.put("patronym.pageOwner", pageOwner);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map)
    {
        visibility = (boolean)map.get("behavior.visibility");
        anchor = (SAnchor)map.get("disposition.anchor");
        offset = new SPoint((int)map.get("disposition.offsetX"), (int)map.get("disposition.offsetY"));
        size = new SSize((int)map.get("disposition.sizeWidth"), (int)map.get("disposition.sizeHeight"));
        parentDialog = (SDialog)map.get("disposition.parentDialog");
        relativeDialogItem = (SDialogItem)map.get("disposition.relativeDialogItem");
        zChannel = (int)map.get("disposition.zChannel");
        tooltip = (SText)map.get("data.tooltip");
        guid = (Guid)map.get("patronym.guid");
        name = (String)map.get("patronym.name");
        pageOwner = (Page)map.get("patronym.pageOwner");
        transparency = (int)map.get("disposition.transparency");
    }
    
    @Override
    public Dialog createDialog(Frame owner)
    {
        DialogPropertySObject d = new DialogPropertySObject(owner, true, App.getApp().getOpenedProject().getCurrentPage(), SDialogItem.class);
        d.setSize(600, 300);
        d.setModal(true);
        d.setTitle("Set Dialog Item");
        d.setResizable(false);
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        d.setLocationRelativeTo(owner);
        d.setEditingInfo(this);
        return d;
    }

    @Override
    public ImageIcon getIconImage() {
        return new ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_panel.png"));
    }

    @Override
    public void onDraw(Graphics2D canvas, Paint paint, Object o, int width, int height)
    {
        if (this.visibility == false)
            return;
        
        float alphaDialog = 1f - (this.parentDialog.transparency / 100f);
        float alphaDialogItem = 1f - (this.transparency / 100f);
        float alpha = alphaDialog * alphaDialogItem;
        if (alpha > 1) alpha = 1; if (alpha < 0) alpha = 0;     // Protection, AlphaComposite cannot support out of range values.
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        canvas.setComposite(ac);
    }
    
    protected void onFinishDraw(Graphics2D canvas)
    {
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        canvas.setComposite(ac);
    }
    
    protected SRect rectForParent(int width, int height)
    {
        int offsetWidth = Math.min( (this.size.width + this.offset.x ), width);
        int offsetHeight = Math.min( (this.size.height + this.offset.y), height);
        
        if (this.offset.x + offsetWidth > width)
            offsetWidth = Math.abs(offsetWidth - this.offset.x);
        
        if (this.offset.y + offsetHeight > height)
            offsetHeight = Math.abs(offsetHeight - this.offset.y);
        
        SRect rect = new SRect(new SPoint(this.offset.x, this.offset.y),new SSize(offsetWidth, offsetHeight));
        return rect;
    }
}
