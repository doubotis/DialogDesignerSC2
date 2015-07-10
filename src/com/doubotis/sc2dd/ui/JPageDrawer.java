/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.ui;

import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.data.IDrawable;
import com.doubotis.sc2dd.data.SDialog;
import com.doubotis.sc2dd.data.SObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Christophe
 */
public class JPageDrawer extends JPanel
{
    @Override
    protected void paintComponent(Graphics g)
    {
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = buffer.createGraphics();
        Paint pt = graphics.getPaint();
        
        List<SObject> objects = App.getApp().getOpenedProject().getCurrentPage().getObjects();
        System.out.println(" -Draw- Start drawing");
        for (int i=0; i < objects.size(); i++)
        {
            SObject object = objects.get(i);
            if (object instanceof SDialog)
            {
                ((IDrawable)object).onDraw(graphics, pt, null, getWidth(), getHeight());
            }
        }
        System.out.println(" -Draw- Finished drawing");
        
        // Now draw the whole buffer into the panel.
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(buffer, 0, 0, null);
    }
}
