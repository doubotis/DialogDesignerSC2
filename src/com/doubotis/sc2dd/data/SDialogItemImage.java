/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.ui.ImageCaching;
import com.doubotis.sc2dd.ui.ImageRender;
import com.doubotis.sc2dd.util.UIUtils;
import com.mundi4.mpq.MpqEntry;
import com.mundi4.mpq.MpqFile;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
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
    public SColor color;
    public boolean tiled;
    
    public SDialogItemImage(SDialog dialog)
    {
        this("Image" + App.getApp().getOpenedProject().makeNewUniqueName(), dialog);
    }
    
    public SDialogItemImage(String name, SDialog dialog) {
        super(name, dialog);
        
        DefaultProperties defaults = DefaultProperties.defaultProperties();
        
        imageType = SImageType.Normal;
        color = SColor.WHITE;
        tiled = true;
        
        this.image = (SImage)defaults.getDefault("image.appareance.background.image");
    }

    @Override
    public HashMap<String, Object> loadProperties() {
        HashMap<String, Object> map = super.loadProperties();
        map.put("data.image", image);
        map.put("data.imageType", imageType);
        map.put("data.rotation", rotation);
        map.put("data.color", color);
        map.put("data.tiled", tiled);
        return map;
    }

    @Override
    public void saveProperties(HashMap<String, Object> map) {
        super.saveProperties(map); //To change body of generated methods, choose Tools | Templates.
        
        image = (SImage)map.get("data.image");
        imageType = (SImageType)map.get("data.imageType");
        rotation = (int)map.get("data.rotation");
        color = (SColor)map.get("data.color");
        tiled = (boolean)map.get("data.tiled");
    
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
        
        SRect locationToDraw = manageAnchorInside(new SSize(width, height), this.anchor);
        
        ImageRender.BorderType borderType = ImageRender.BorderType.Normal;
        if (this.imageType == SImageType.Border)
            borderType = ImageRender.BorderType.Border;
        else if (this.imageType == SImageType.HorizontalBorder)
            borderType = ImageRender.BorderType.HorizontalBorder;
        else if (this.imageType == SImageType.EndCap)
            borderType = ImageRender.BorderType.EndCap;
        else
            borderType = ImageRender.BorderType.Normal;
        
        System.out.println(" -Draw- Image Timestamp Start " + System.currentTimeMillis());
        
        try
        {
            BufferedImage img = ImageCaching.getCache().retreive(this.image.imagePath + "|" + this.image.mod);
            if (img == null)
            {
                MpqFile mpq = new MpqFile(this.image.imagePath);
                MpqEntry entry = mpq.getEntry(this.image.mod);
                img = UIUtils.imageFromImage(mpq.getInputStream(entry));
                ImageCaching.getCache().storeImage(this.image.imagePath + "|" + this.image.mod, img);
            }
            
            // Apply Rotation (+ Correction)
            
            float angle = (float)Math.toRadians(this.rotation);
            double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
            int w = img.getWidth(), h = img.getHeight();
            int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
            if (this.rotation != 0)
            {
                canvas.translate((neww-w)/2, (newh-h)/2);
                canvas.rotate(angle, w/2, h/2);
            }
            
            ImageRender ir = new ImageRender(borderType, img, this.tiled, false, ImageRender.ButtonDesignState.None);
            BufferedImage resultImg = ir.getRenderImage(this.size);
            //BufferedImage mask = generateMask(resultImg, this.color.getColor(), 1);
            Color c = new Color(255 - this.color.getColor().getRed(),
                                255 - this.color.getColor().getGreen(),
                                255 - this.color.getColor().getBlue());
            //canvas.setXORMode(c);
            canvas.drawImage(resultImg, locationToDraw.origin.x, locationToDraw.origin.y, null);
            //canvas.setXORMode(null);
            
            if (this.rotation != 0)
            {
                canvas.rotate(0);
                canvas.translate(-((neww-w)/2), -((newh-h)/2));
            }
                
        } catch (Exception e) {}
        
        System.out.println(" -Draw- Image Timestamp End " + System.currentTimeMillis());
        
        // Restore transparency from the SDialogItem class.
        super.onFinishDraw(canvas);
    }
    
    private BufferedImage generateMask(BufferedImage imgSource, Color color, float alpha) {
        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D g2 = imgMask.createGraphics();
        applyQualityRenderingHints(g2);

        g2.drawImage(imgSource, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        g2.setColor(color);

        g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
        g2.dispose();

        return imgMask;
    }
    
    private void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
    
    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image =  GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
                .createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }
    
    
    
}
