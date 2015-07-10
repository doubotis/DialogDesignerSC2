/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.dialogs.DialogPropertySObject;
import com.doubotis.sc2dd.dialogs.DialogPropertyText;
import com.doubotis.sc2dd.ui.ImageCaching;
import com.doubotis.sc2dd.ui.ImageRender;
import com.doubotis.sc2dd.util.UIUtils;
import com.mundi4.mpq.MpqEntry;
import com.mundi4.mpq.MpqFile;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;

/**
 *
 * @author Christophe
 */
public class SDialog extends SObject implements IProperty, IPropertyMore, IDrawable
{
    private static final int TYPE_NONE = 1;
    
    public static final SDialog NONE = new SDialog(TYPE_NONE);
    
    public boolean visibility;
    public boolean backgroundVisibility;
    public SAnchor anchor;
    public SPoint offset;
    public SDialog relativeDialog;
    public SSize size;
    public int zChannel;
    public SText title;
    public SImage backgroundImage;
    public SImageType backgroundImageType;
    public Page pageOwner;
    public String name;
    public int transparency;
    
    private SDialog(int predefinedType)
    {
        this.name = "No Dialog";
    }
    
    public SDialog(String name)
    {
        DefaultProperties defaults = DefaultProperties.defaultProperties();
        
        this.name = name;
        this.relativeDialog = SDialog.NONE;
        this.visibility = true;
        this.anchor = SAnchor.TopLeft;
        this.size = new SSize(200, 200);
        this.offset = new SPoint(50, 50);
        this.guid = new Guid(UUID.randomUUID());
        this.title = new SText("");        
        this.backgroundImageType = SImageType.Border;
        this.backgroundVisibility = true;
        this.transparency = 0;
        this.zChannel = 512;
        this.backgroundImage = (SImage)defaults.getDefault("dialog.appareance.background.image");
    }
    
    public SDialog()
    {
        this("Dialog" + App.getApp().getOpenedProject().makeNewUniqueName());
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
        map.put("appareance.backgroundVisibility", backgroundVisibility);
        map.put("appareance.backgroundImage", backgroundImage);
        map.put("appareance.backgroundImageType", backgroundImageType);
        map.put("behavior.visibility", visibility);
        map.put("disposition.anchor", anchor);
        map.put("disposition.offsetX", offset.x);
        map.put("disposition.offsetY", offset.y);
        map.put("disposition.sizeWidth", size.width);
        map.put("disposition.sizeHeight", size.height);
        map.put("disposition.relativeDialog", relativeDialog);
        map.put("disposition.zChannel", zChannel);
        map.put("disposition.transparency", transparency);
        map.put("data.title", title);
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
        backgroundVisibility = (boolean)map.get("appareance.backgroundVisibility");
        backgroundImage = (SImage)map.get("appareance.backgroundImage");
        backgroundImageType = (SImageType)map.get("appareance.backgroundImageType");
        offset = new SPoint((int)map.get("disposition.offsetX"), (int)map.get("disposition.offsetY"));
        size = new SSize((int)map.get("disposition.sizeWidth"), (int)map.get("disposition.sizeHeight"));
        relativeDialog = (SDialog)map.get("disposition.relativeDialog");
        zChannel = (int)map.get("disposition.zChannel");
        title = (SText)map.get("data.title");
        guid = (Guid)map.get("patronym.guid");
        name = (String)map.get("patronym.name");
        pageOwner = (Page)map.get("patronym.pageOwner");
        transparency = (int)map.get("disposition.transparency");
    }
    
    @Override
    public Dialog createDialog(Frame owner)
    {
        DialogPropertySObject d = new DialogPropertySObject(owner, true, App.getApp().getOpenedProject().getCurrentPage(), SDialog.class);
        d.setSize(600, 300);
        d.setModal(true);
        d.setTitle("Set Dialog");
        d.setResizable(false);
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        d.setLocationRelativeTo(owner);
        d.setEditingInfo(this);
        return d;
    }

    @Override
    public ImageIcon getIconImage() {
        return new ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_dialog.png"));
    }
    
    @Override
    public void onDraw(Graphics2D canvas, Paint paint, Object o, int width, int height)
    {
        if (this.visibility == false)
            return;
        
        SRect locationToDraw = manageAnchorInside(new SSize(width, height), this.anchor);
        canvas.setClip(locationToDraw.origin.x, locationToDraw.origin.y, locationToDraw.size.width, locationToDraw.size.height);
        
        float alpha = 1f - (this.transparency / 100f);
        if (alpha > 1) alpha = 1; if (alpha < 0) alpha = 0;     // Protection, AlphaComposite cannot support out of range values.
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        canvas.setComposite(ac);
        
        ImageRender.BorderType borderType = ImageRender.BorderType.Normal;
        if (this.backgroundImageType == SImageType.Border)
            borderType = ImageRender.BorderType.Border;
        else if (this.backgroundImageType == SImageType.HorizontalBorder)
            borderType = ImageRender.BorderType.HorizontalBorder;
        else if (this.backgroundImageType == SImageType.EndCap)
            borderType = ImageRender.BorderType.EndCap;
        else
            borderType = ImageRender.BorderType.Normal;
        
        // Draw background
        if (this.backgroundVisibility)
        {
            try
            {
                BufferedImage img = ImageCaching.getCache().retreive(this.backgroundImage.imagePath + "|" + this.backgroundImage.mod);
                if (img == null)
                {
                    MpqFile mpq = new MpqFile(this.backgroundImage.imagePath);
                    MpqEntry entry = mpq.getEntry(this.backgroundImage.mod);
                    img = UIUtils.imageFromImage(mpq.getInputStream(entry));
                    ImageCaching.getCache().storeImage(this.backgroundImage.imagePath + "|" + this.backgroundImage.mod, img);
                }
                ImageRender ir = new ImageRender(borderType, img, false, ImageRender.ButtonDesignState.None);
                BufferedImage resultImg = ir.getRenderImage(this.size);
                canvas.drawImage(resultImg, locationToDraw.origin.x, locationToDraw.origin.y, null);

            } catch (Exception e) {}
        }
        
        canvas.setClip(0, 0, width, height);
        
        // Loop for dialog items.
        canvas.translate(locationToDraw.origin.x, locationToDraw.origin.y);
        canvas.setClip(0, 0, this.size.width, this.size.height);
        
        List<SDialogItem> dialogItems = getDialogItemsForDialog(this);
        for (SDialogItem di : dialogItems)
        {
            di.onDraw(canvas, paint, null, this.size.width, this.size.height);
        }
        canvas.setClip(0, 0, width, height);
        canvas.translate(-locationToDraw.origin.x, -locationToDraw.origin.y);
        
        ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        canvas.setComposite(ac);
        
    }
    
    private SRect manageAnchorInside(SSize size, SAnchor anchor)
    {
        int x = 0;
        int y = 0;
        
        if (anchor == SAnchor.TopLeft)
        {
            x = 0 + this.offset.x;
            y = 0 + this.offset.y;
        }
        else if (anchor == SAnchor.Top)
        {
            x = (size.width / 2) - (this.size.width / 2) + this.offset.x;
            y = 0 + this.offset.y;
        }
        else if (anchor == SAnchor.TopRight)
        {
            x = size.width - this.size.width - this.offset.x;
            y = 0 + this.offset.y;
        }
        else if (anchor == SAnchor.Left)
        {
            x = 0 + this.offset.x;
            y = (size.height / 2) - (this.size.height / 2) + this.offset.y;
        }
        else if (anchor == SAnchor.Center)
        {
            x = (size.width / 2) - (this.size.width / 2) + this.offset.x;
            y = (size.height / 2) - (this.size.height / 2) + this.offset.y;
        }
        else if (anchor == SAnchor.Right)
        {
            x = size.width - this.size.width - this.offset.x;
            y = (size.height / 2) - (this.size.height / 2) + this.offset.y;
        }
        else if (anchor == SAnchor.BottomLeft)
        {
            x = 0 + this.offset.x;
            y = size.height - this.size.height - this.offset.y;
        }
        else if (anchor == SAnchor.Bottom)
        {
            x = (size.width / 2) - (this.size.width / 2) + this.offset.x;
            y = size.height - this.size.height - this.offset.y;
        }
        else if (anchor == SAnchor.BottomRight)
        {
            x = size.width - this.size.width - this.offset.x;
            y = size.height - this.size.height - this.offset.y;
        }
        return new SRect(x,y, this.size.width, this.size.height);
    }
    
    private List<SDialogItem> getDialogItemsForDialog(SDialog dialog)
    {
        List<SObject> objects = App.getApp().getOpenedProject().getCurrentPage().getObjects();
        ArrayList<SDialogItem> list = new ArrayList<SDialogItem>();
        for (int i=0; i < objects.size(); i++)
        {
            if (objects.get(i) instanceof SDialog)
                continue;
            
            SDialogItem di = (SDialogItem)objects.get(i);
            if (di.parentDialog == dialog)
                list.add(di);
        }
        return list;
    }
}
