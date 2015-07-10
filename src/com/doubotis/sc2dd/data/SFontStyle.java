/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.dialogs.DialogPropertyText;
import com.doubotis.sc2dd.util.ColorUtils;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 *
 * @author Christophe
 */
public class SFontStyle implements SResource, IDrawable, IPropertyMore
{
    public String name = "";
    public String template = "";
    public String font = "";
    public String height = "";
    public String vJustify = "";
    public String hJustify = "";
    public String fontFlags = "";
    public String styleFlags = "";
    public String textColor = "";
    public String disabledColor = "";
    public String highlightColor = "";
    public String hotkeyColor = "";
    public String hyperlinkColor = "";
    public String glowColor = "";
    public String shadowOffset = "";
    
    public SFontStyle(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
    
    public String completeDescription()
    {
        return  "SFontStyle {\n" +
                "\tname: " + this.name + ",\n" +
                "\ttemplate: " + this.template + ",\n" +
                "\tfont: " + this.font + ",\n" +
                "\theight: " + this.height + ",\n" +
                "\tvJustify: " + this.vJustify + ",\n" +
                "\thJustify: " + this.hJustify + ",\n" +
                "\tfontFlags: " + this.fontFlags + ",\n" +
                "\tstyleFlags: " + this.styleFlags + ",\n" +
                "\ttextColor: " + this.textColor + ",\n" +
                "\tdisabledColor: " + this.disabledColor + ",\n" +
                "\thighlightColor: " + this.highlightColor + ",\n" +
                "\thotkeyColor: " + this.hotkeyColor + ",\n" +
                "\thyperlinkColor: " + this.hyperlinkColor + ",\n" +
                "\tglowColor: " + this.glowColor + ",\n" +
                "\tshadowOffset: " + this.shadowOffset + ",\n" +
                "}";
    }
    
    @Override
    public Dialog createDialog(Frame owner)
    {
        DialogPropertyText d = new DialogPropertyText(owner, true);
        d.setSize(600, 555);
        d.setModal(true);
        d.setTitle("Set Text Style");
        d.setResizable(false);
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        d.setLocationRelativeTo(owner);
        d.setEditingInfo(name);
        return d;
    }

    @Override
    public void onDraw(Graphics2D canvas, Paint paint, Object o, int width, int height)
    {
        Font ft = (Font)App.getApp().getPreloader().getObject("Font__" + font);
        if (ft == null)
            return;
        
        String text = o.toString();
        
        String[] styles = this.styleFlags.split(",");
        boolean shadow = (Arrays.asList(styles).indexOf("Shadow") != -1) ? true : false;
        
        ft = ft.deriveFont(Float.parseFloat(this.height) * 1f);
        canvas.setFont(ft);
        FontMetrics fontMetrics = canvas.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, canvas);
        
        int ptX;
        if (this.hJustify.equalsIgnoreCase("Center"))
        {
            // Center Side
            ptX = (int) ((width * 0.5) - (rect.getWidth() * 0.5));
        }
        else if (this.hJustify.equalsIgnoreCase("Right"))
        {
            // Right Side
            ptX = (int) ((width * 1) - (rect.getWidth()));
        }
        else
        {
            // Left Side
            ptX = (int) (width * 0);
        }
        
        int ptY;
        if (this.vJustify.equalsIgnoreCase("Middle"))
        {
            // Middle Side
            ptY = (int) ((height * 0.5) - (rect.getHeight() * 0.5));
        }
        else if (this.vJustify.equalsIgnoreCase("Bottom"))
        {
            // Bottom Side
            ptY = (int) ((height * 1) - (rect.getHeight()));
        }
        else
        {
            // Top Side
            ptY = (int) ((height * 0) + (rect.getHeight()));
        }
        
        if (shadow)
        {
            canvas.setColor(Color.BLACK);
            int shadowOffset = Integer.parseInt(this.shadowOffset);
            canvas.drawString(text, (ptX + shadowOffset), (ptY + shadowOffset));
        }
        
        Color c = ColorUtils.fromString(this.textColor);
        canvas.setColor(c);
        canvas.drawString(text, ptX, ptY);
    }
    
    public static class SFontStyleConstant
    {
        public String name;
        public String val;
        
        public SFontStyleConstant(String name, String val)
        {
            this.name = name;
            this.val = val;
        }
        
        @Override
        public String toString()
        {
            return "Constant {name: " + this.name + ",val: " + this.val + "}";
        }
    }
    
}
