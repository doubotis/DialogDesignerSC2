/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.ui.ImageRender;
import com.doubotis.sc2dd.util.UIUtils;
import com.mundi4.mpq.MpqEntry;
import com.mundi4.mpq.MpqFile;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author Christophe
 */
public class SDialogItemImage extends SDialogItem {

    public SImage image;
    public SImageType imageType;
    public int rotation;
    
    public SDialogItemImage(SDialog dialog)
    {
        this("Image" + App.getApp().getOpenedProject().makeNewUniqueName(), dialog);
    }
    
    public SDialogItemImage(String name, SDialog dialog) {
        super(name, dialog);
        
        image = SImage.NONE;
        imageType = SImageType.Normal;
    }

    @Override
    public HashMap<String, Object> loadProperties() {
        HashMap<String, Object> map = super.loadProperties();
        map.put("data.image", image);
        map.put("data.imageType", imageType);
        map.put("data.rotation", rotation);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map) {
        super.saveProperties(map); //To change body of generated methods, choose Tools | Templates.
        
        System.out.println("Update:" + map.toString());
        image = (SImage)map.get("data.image");
        imageType = (SImageType)map.get("data.imageType");
        rotation = (int)map.get("data.rotation");
    }
    
    @Override
    public ImageIcon getIconImage() {
        return new ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_image.png"));
    }

    @Override
    public void onDraw(Graphics2D canvas, Paint paint, Object o, int width, int height)
    {
        if (this.visibility == false)
            return;
        
        // Apply SDialogItem drawing (transparency).
        super.onDraw(canvas, paint, o, width, height);
        
        ImageRender.BorderType borderType = ImageRender.BorderType.Normal;
        if (this.imageType == SImageType.Border)
            borderType = ImageRender.BorderType.Border;
        else if (this.imageType == SImageType.HorizontalBorder)
            borderType = ImageRender.BorderType.HorizontalBorder;
        else if (this.imageType == SImageType.EndCap)
            borderType = ImageRender.BorderType.EndCap;
        else
            borderType = ImageRender.BorderType.Normal;
        
        try
        {
            MpqFile mpq = new MpqFile(this.image.imagePath);
            MpqEntry entry = mpq.getEntry(this.image.mod);
            BufferedImage img = UIUtils.imageFromImage(mpq.getInputStream(entry));
            
            // Apply Rotation (+ Correction)
            float angle = (float)Math.toRadians(this.rotation);
            double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
            int w = img.getWidth(), h = img.getHeight();
            int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
            canvas.translate((neww-w)/2, (newh-h)/2);
            canvas.rotate(angle, w/2, h/2);
            
            ImageRender ir = new ImageRender(borderType, img, false, ImageRender.ButtonDesignState.None);
            BufferedImage resultImg = ir.getRenderImage(this.size);
            canvas.drawImage(resultImg, this.offset.x, this.offset.y, null);
            
            canvas.rotate(0);
            canvas.translate(-((neww-w)/2), -((newh-h)/2));
                
        } catch (Exception e) {}
        
        // Restore transparency from the SDialogItem class.
        super.onFinishDraw(canvas);
    }
    
    
    
    
    
}
