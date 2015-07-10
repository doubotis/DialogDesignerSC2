/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.util;

import static com.doubotis.sc2dd.JPanelDataExplorer.IMAGES;
import static com.doubotis.sc2dd.JPanelDataExplorer.TEXTS;
import static com.doubotis.sc2dd.JPanelDataExplorer.XML_LAYOUTS;
import com.doubotis.sc2dd.data.SFile;
import com.doubotis.sc2dd.data.SFontStyle;
import com.doubotis.sc2dd.data.SResource;
import ddsutil.DDSUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import jogl.DDSImage;

/**
 *
 * @author Christophe
 */
public class UIUtils {
    
    public enum ResourceType {
        FONT,
        STYLE,
        IMAGE,
        XML,
        UNKNOWN,
        STYLES
    }
    
    public static Component createComponentForResource(SResource s, ResourceType resourceType)
    {
        InputStream is = null;
        try
        {
            if (resourceType == ResourceType.FONT)
            {
                SFile f = (SFile)s;
                is = f.open();
                Font myFont = Font.createFont(Font.TRUETYPE_FONT, is);
                myFont = myFont.deriveFont(20.0f);
                JLabel label = new JLabel();
                label.setLayout(new FlowLayout());
                label.setFont(myFont);
                label.setText("This is an example");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                is.close();
                return label;
            }
            else if (resourceType == ResourceType.IMAGE)
            {
                SFile f = (SFile)s;
                is = f.open();
                BufferedImage image = imageFromImage(is);
                JLabel label = new JLabel();
                label.setLayout(new FlowLayout());
                label.setText("");
                label.setIcon(new ImageIcon(image));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                is.close();
                return label;
            }
            else if (resourceType == ResourceType.XML)
            {
                SFile f = (SFile)s;
                is = f.open();
                String str = stringFromInputStream(is);
                JTextArea txtArea = new JTextArea(str);
                txtArea.setLayout(new FlowLayout());
                is.close();
                return txtArea;
            }
            else if (resourceType == ResourceType.STYLE)
            {
                SFontStyle fs = (SFontStyle)s;
                System.out.println(" -Style-");
                System.out.println(fs.completeDescription());
                //JPanel panel = new JPanel();
                //panel.setLayout(new FlowLayout());
                BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = image.createGraphics();
                Paint pt = graphics.getPaint();
                fs.onDraw(graphics, pt, "This is an exemple", 250, 250);
                JLabel label = new JLabel();
                label.setLayout(new FlowLayout());
                label.setText("");
                label.setIcon(new ImageIcon(image));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                return label;
                /*String str = fs.completeDescription();
                JTextArea txtArea = new JTextArea(str);
                txtArea.setLayout(new FlowLayout());
                return txtArea;*/
            }
            else
                throw new Exception("");
            

        } catch (Exception e)
        {
            e.printStackTrace();
            JLabel label = new JLabel();
            label.setLayout(new FlowLayout());
            label.setText("Unable to load resource.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            if (is != null)
                try { is.close(); } catch (IOException ioe) {}
            return label;
        }
    }
    
    public static BufferedImage imageFromImage(InputStream is) throws IOException
    {
        Vector<Byte> byteV = new Vector<Byte>();
        byte[] tmp1 = new byte[1024];
        while (true) {
            int r = is.read(tmp1, 0, 1024);
            if (r == -1) break;
            for (int i=0; i<r; i++) {
                byteV.add(tmp1[i]);
            }
        }
        byte[] tmp2 = new byte[byteV.size()];
        for (int i=0; i<byteV.size(); i++) {
            tmp2[i] = byteV.elementAt(i);
        }
        ByteBuffer buf = ByteBuffer.wrap(tmp2);

        return DDSUtil.loadBufferedImage(DDSImage.read(buf));
    }
    
    private static BufferedImage imageFromFontStyle(SFontStyle fs) throws IOException
    {
        return null;
    }
    
    public static String stringFromInputStream(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }
}
