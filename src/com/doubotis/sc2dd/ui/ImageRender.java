/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.ui;

import com.doubotis.sc2dd.data.SPoint;
import com.doubotis.sc2dd.data.SRect;
import com.doubotis.sc2dd.data.SSize;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Christophe
 */
public class ImageRender
{
    public enum RotateFlipType
    {
        Rotate90FlipNone,
        Rotate90Flip
    }
    
   public enum ImagePart
    {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight,
        Top,
        Bottom,
        Left,
        Right,
        Center
    }

    public enum ButtonDesignState
    {
        None,
        Unpressed,
        Pressed
    }
    
    public enum BorderType
    {
        None,
        Normal,
        Border,
        HorizontalBorder,
        EndCap
    }
    
    protected HashMap<ImagePart, BufferedImage> m_CuttedImages = new HashMap<ImagePart, BufferedImage>();
    protected BufferedImage m_OriginalImage;
    protected int m_DivX;
    protected int m_DivY;
    protected BorderType m_TypeRender;
    protected ButtonDesignState m_ButtonState;
    protected boolean m_IsButton = false;
    protected boolean m_Pressed = false;

    // CONSTRUCTOR - DESTRUCTOR
    public ImageRender(BorderType p_TypeRender, BufferedImage p_Image, boolean p_IsButton, ButtonDesignState p_Bs)
    {
        if (p_Image != null)
            m_OriginalImage = p_Image;

        m_ButtonState = p_Bs;
        m_IsButton = p_IsButton;
        m_TypeRender = p_TypeRender;

        build();
    }
    
    public BufferedImage getOriginalImage() { return m_OriginalImage; }
    public int getDividerX() { return m_DivX; }
    public int getDividerY() { return m_DivY; }
    public boolean isButton() { return m_IsButton; }
    public boolean isButtonPressed() { return m_Pressed; }
    public BorderType getBorderType() { return m_TypeRender; }
    
    private void build()
    {
        int IntIsButton = 1;
        if (m_ButtonState != ButtonDesignState.None)
            IntIsButton = 2;

        if (m_TypeRender == BorderType.Border)
        {
            // Cut data.
            int divx = (m_OriginalImage.getWidth() / 4);
            int divy = (m_OriginalImage.getHeight() / (8 * IntIsButton));

            int maxx = m_OriginalImage.getWidth();
            int maxy = m_OriginalImage.getHeight();

            m_DivX = divx;
            m_DivY = divy;

            int i, j, k;
            Graphics2D l;
            BufferedImage b;
            List<BufferedImage> imgs = new ArrayList<BufferedImage>();
            k = 0;

            for (j = 0; j < 8 * IntIsButton; j++)
            {
                for (i = 0; i < 4; i++)
                {
                    b = new BufferedImage(divx, divy, BufferedImage.TYPE_INT_ARGB);
                    l = b.createGraphics();
                    drawImageUnscaled(l, m_OriginalImage, new SPoint(-(divx * i), -(divy * j)));
                    //l.DrawImageUnscaled(m_OriginalImage, new Point(-(divx * i), -(divy * j)));
                    imgs.add(b);
                    k++;
                }
            }

            /*BufferedImage a;
            Graphics2D ag;
            // Draw Top.
            a = new BufferedImage(m_OriginalImage.getWidth(), m_DivY, BufferedImage.TYPE_INT_ARGB);
            ag = a.createGraphics();
            drawImageUnscaled(ag, imgs.get(4), new SPoint(0, 0));
            drawImageUnscaled(ag, imgs.get(5), new SPoint(m_DivX, 0));
            drawImageUnscaled(ag, imgs.get(6), new SPoint(m_DivX * 2, 0));
            drawImageUnscaled(ag, imgs.get(7), new SPoint(m_DivX * 3, 0));
            imgs.set(4, a);

            // Draw Bottom.
            a = new BufferedImage(m_OriginalImage.getWidth(), m_DivY, BufferedImage.TYPE_INT_ARGB);
            ag = a.createGraphics();
            drawImageUnscaled(ag, imgs.get(8), new SPoint(0, 0));
            drawImageUnscaled(ag, imgs.get(9), new SPoint(m_DivX, 0));
            drawImageUnscaled(ag, imgs.get(10), new SPoint(m_DivX * 2, 0));
            drawImageUnscaled(ag, imgs.get(11), new SPoint(m_DivX * 3, 0));
            imgs.set(8, a);

            // Draw Left.
            a = new BufferedImage(m_OriginalImage.getWidth(), m_DivY, BufferedImage.TYPE_INT_ARGB);
            ag = a.createGraphics();
            drawImageUnscaled(ag, imgs.get(16), new SPoint(0, 0));
            drawImageUnscaled(ag, imgs.get(17), new SPoint(m_DivX, 0));
            drawImageUnscaled(ag, imgs.get(18), new SPoint(m_DivX * 2, 0));
            drawImageUnscaled(ag, imgs.get(19), new SPoint(m_DivX * 3, 0));
            imgs.set(16, a);

            // Draw Right.
            a = new BufferedImage(m_OriginalImage.getWidth(), m_DivY, BufferedImage.TYPE_INT_ARGB);
            ag = a.createGraphics();
            drawImageUnscaled(ag, imgs.get(12), new SPoint(0, 0));
            drawImageUnscaled(ag, imgs.get(13), new SPoint(m_DivX, 0));
            drawImageUnscaled(ag, imgs.get(14), new SPoint(m_DivX * 2, 0));
            drawImageUnscaled(ag, imgs.get(15), new SPoint(m_DivX * 3, 0));
            imgs.set(12, a);*/

            m_CuttedImages.clear();

            if (m_ButtonState == ButtonDesignState.Unpressed || m_ButtonState == ButtonDesignState.None)
            {
                imgs.set(12, rotateFlip(imgs.get(12), RotateFlipType.Rotate90FlipNone));
                imgs.set(16, rotateFlip(imgs.get(16), RotateFlipType.Rotate90FlipNone));
                try { ImageIO.write(imgs.get(12), "png", new File("E:\\samples\\rotateLeft.png")); } catch (Exception e) {}
                try { ImageIO.write(imgs.get(16), "png", new File("E:\\samples\\rotateRight.png")); } catch (Exception e) {}
                m_CuttedImages.put(ImagePart.TopLeft, imgs.get(0));
                m_CuttedImages.put(ImagePart.TopRight, imgs.get(1));
                m_CuttedImages.put(ImagePart.BottomLeft, imgs.get(2));
                m_CuttedImages.put(ImagePart.BottomRight, imgs.get(3));
                m_CuttedImages.put(ImagePart.Top, imgs.get(4));
                m_CuttedImages.put(ImagePart.Bottom, imgs.get(8));
                m_CuttedImages.put(ImagePart.Left, imgs.get(16));
                m_CuttedImages.put(ImagePart.Right, imgs.get(12));
                m_CuttedImages.put(ImagePart.Center, imgs.get(20));
            }
            else if (m_ButtonState == ButtonDesignState.Pressed)
            {
                imgs.set(44, rotateFlip(imgs.get(44), RotateFlipType.Rotate90FlipNone));
                imgs.set(48, rotateFlip(imgs.get(48), RotateFlipType.Rotate90FlipNone));
                m_CuttedImages.put(ImagePart.TopLeft, imgs.get(32));
                m_CuttedImages.put(ImagePart.TopRight, imgs.get(33));
                m_CuttedImages.put(ImagePart.BottomLeft, imgs.get(34));
                m_CuttedImages.put(ImagePart.BottomRight, imgs.get(35));
                m_CuttedImages.put(ImagePart.Top, imgs.get(36));
                m_CuttedImages.put(ImagePart.Bottom, imgs.get(40));
                m_CuttedImages.put(ImagePart.Left, imgs.get(48));
                m_CuttedImages.put(ImagePart.Right, imgs.get(44));
                m_CuttedImages.put(ImagePart.Center, imgs.get(52));
            }
            return;
        }

        if (m_TypeRender == BorderType.HorizontalBorder)
        {
            // Cut data.
            int divx = m_OriginalImage.getWidth() / 10;
            int divy = (m_OriginalImage.getHeight() / (1 * IntIsButton));
            int maxx = m_OriginalImage.getWidth();
            int maxy = m_OriginalImage.getHeight();

            m_DivX = divx;
            m_DivY = divy;

            int i, j, k;
            Graphics2D l;
            BufferedImage b;
            List<BufferedImage> imgs = new ArrayList<BufferedImage>();
            k = 0;
            for (j = 0; j < 1 * IntIsButton; j++)
            {
                for (i = 0; i < 10; i++)
                {
                    b = new BufferedImage(divx, divy, BufferedImage.TYPE_INT_ARGB);
                    l = b.createGraphics();
                    l.drawImage(m_OriginalImage, -(divx * i), 0, null);
                    imgs.add(b);
                    k++;
                }
            }

            m_CuttedImages.clear();
            if (m_ButtonState == ButtonDesignState.Unpressed || m_ButtonState == ButtonDesignState.None)
            {
                imgs.set(4, rotateFlip(imgs.get(4), RotateFlipType.Rotate90FlipNone));
                imgs.set(5, rotateFlip(imgs.get(5), RotateFlipType.Rotate90FlipNone));
                m_CuttedImages.put(ImagePart.TopLeft, imgs.get(0));
                m_CuttedImages.put(ImagePart.TopRight, imgs.get(1));
                m_CuttedImages.put(ImagePart.BottomLeft, imgs.get(2));
                m_CuttedImages.put(ImagePart.BottomRight, imgs.get(3));
                m_CuttedImages.put(ImagePart.Top, imgs.get(4));
                m_CuttedImages.put(ImagePart.Bottom, imgs.get(5));
                m_CuttedImages.put(ImagePart.Left, imgs.get(6));
                m_CuttedImages.put(ImagePart.Right, imgs.get(7));
                m_CuttedImages.put(ImagePart.Center, imgs.get(8));
            }
            else if (m_ButtonState == ButtonDesignState.Pressed)
            {
                imgs.set(14, rotateFlip(imgs.get(14), RotateFlipType.Rotate90FlipNone));
                imgs.set(15, rotateFlip(imgs.get(15), RotateFlipType.Rotate90FlipNone));
                m_CuttedImages.put(ImagePart.TopLeft, imgs.get(10));
                m_CuttedImages.put(ImagePart.TopRight, imgs.get(11));
                m_CuttedImages.put(ImagePart.BottomLeft, imgs.get(12));
                m_CuttedImages.put(ImagePart.BottomRight, imgs.get(13));
                m_CuttedImages.put(ImagePart.Top, imgs.get(14));
                m_CuttedImages.put(ImagePart.Bottom, imgs.get(15));
                m_CuttedImages.put(ImagePart.Left, imgs.get(16));
                m_CuttedImages.put(ImagePart.Right, imgs.get(17));
                m_CuttedImages.put(ImagePart.Center, imgs.get(18));
            }
            return;
        }

        if (m_TypeRender == BorderType.EndCap)
        {
            // Cut data.
            int divx = m_OriginalImage.getWidth() / 2;
            int divy = m_OriginalImage.getHeight() / (2 * IntIsButton);
            int maxx = m_OriginalImage.getWidth();
            int maxy = m_OriginalImage.getHeight();

            m_DivX = divx;
            m_DivY = divy;

            int i, j, k;
            Graphics2D l;
            BufferedImage b;
            List<BufferedImage> imgs = new ArrayList<BufferedImage>();
            k = 0;
            for (j = 0; j < 2 * IntIsButton; j++)
            {
                for (i = 0; i < 2; i++)
                {
                    b = new BufferedImage(divx, divy, BufferedImage.TYPE_INT_ARGB);
                    l = b.createGraphics();
                    drawImageUnscaled(l, m_OriginalImage, new SPoint(-(divx * i), -(divy * j)));
                    imgs.add(b);
                    k++;
                }
            }

            //Imgs[4].RotateFlip(RotateFlipType.Rotate90FlipNone);
            //Imgs[5].RotateFlip(RotateFlipType.Rotate90FlipNone);

            m_CuttedImages.clear();
            if (m_ButtonState == ButtonDesignState.Unpressed || m_ButtonState == ButtonDesignState.None)
            {
                m_CuttedImages.put(ImagePart.Left, imgs.get(0));
                m_CuttedImages.put(ImagePart.Right, imgs.get(1));
                m_CuttedImages.put(ImagePart.Center, imgs.get(2));
            }
            else if (m_ButtonState == ButtonDesignState.Pressed)
            {
                m_CuttedImages.put(ImagePart.Left, imgs.get(4));
                m_CuttedImages.put(ImagePart.Right, imgs.get(5));
                m_CuttedImages.put(ImagePart.Center, imgs.get(6));
            }
            return;
        }
    }
    
    public BufferedImage getRenderImage(SSize p_Size)
    {
        BufferedImage b = new BufferedImage(p_Size.width, p_Size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D finalGraphics = b.createGraphics();
        //finalGraphics.CompositingMode = System.Drawing.Drawing2D.CompositingMode.SourceCopy;
        //finalGraphics.PixelOffsetMode = System.Drawing.Drawing2D.PixelOffsetMode.HighQuality;
        //finalGraphics.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;

        if (m_TypeRender == BorderType.Border)
        {
            // Draw the top borders.
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.TopLeft), new SRect(0, 0, getImagePart(ImagePart.TopLeft).getWidth(), getImagePart(ImagePart.TopLeft).getHeight()));
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.TopRight), new SRect(b.getWidth() - getImagePart(ImagePart.TopRight).getWidth(), 0, getImagePart(ImagePart.TopLeft).getWidth(), getImagePart(ImagePart.TopLeft).getHeight()));
            drawImageFill(finalGraphics, getImagePart(ImagePart.Top), new SRect(new SPoint(m_DivX, 0), new SSize((b.getWidth() - getImagePart(ImagePart.TopLeft).getWidth() - getImagePart(ImagePart.TopRight).getWidth()), getImagePart(ImagePart.Top).getHeight())));

            // Draw the side borders.
            drawImageFill(finalGraphics, getImagePart(ImagePart.Left), new SRect(new SPoint(0, m_DivY), new SSize(getImagePart(ImagePart.Left).getWidth(), b.getHeight() - getImagePart(ImagePart.TopLeft).getHeight() - getImagePart(ImagePart.BottomLeft).getHeight())));
            drawImageFill(finalGraphics, getImagePart(ImagePart.Right), new SRect(new SPoint(b.getWidth() - getImagePart(ImagePart.Right).getWidth(), m_DivY), new SSize(getImagePart(ImagePart.Left).getWidth(), b.getHeight() - getImagePart(ImagePart.TopRight).getHeight() - getImagePart(ImagePart.BottomRight).getHeight())));

            // Draw the bottom borders.
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.BottomLeft), new SRect(new SPoint(0, (b.getHeight() - getImagePart(ImagePart.BottomLeft).getHeight())), getImagePart(ImagePart.BottomLeft).getWidth(), getImagePart(ImagePart.BottomLeft).getHeight()));
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.BottomRight), new SRect(new SPoint((b.getWidth() - getImagePart(ImagePart.BottomRight).getWidth()), (b.getHeight() - getImagePart(ImagePart.BottomLeft).getHeight())), getImagePart(ImagePart.BottomRight).getWidth(), getImagePart(ImagePart.BottomRight).getHeight()));
            drawImageFill(finalGraphics, getImagePart(ImagePart.Bottom), new SRect(new SPoint(m_DivX, b.getHeight() - getImagePart(ImagePart.Bottom).getHeight()), new SSize((b.getWidth() - getImagePart(ImagePart.BottomLeft).getWidth() - getImagePart(ImagePart.BottomRight).getWidth()), getImagePart(ImagePart.Bottom).getHeight())));

            // Draw the center.
            drawImageFill(finalGraphics, getImagePart(ImagePart.Center), new SRect(new SPoint(m_DivY, m_DivY), new SSize((b.getWidth() - getImagePart(ImagePart.Left).getWidth() - getImagePart(ImagePart.Right).getWidth()), (b.getHeight() - getImagePart(ImagePart.Top).getHeight() - getImagePart(ImagePart.Bottom).getHeight()))));

            // Finish drawing.
            return b;
        }

        if (m_TypeRender == BorderType.HorizontalBorder)
        {
            // Draw the top borders.
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.TopLeft), new SRect(new SPoint(0, 0), getImagePart(ImagePart.TopLeft).getWidth(), getImagePart(ImagePart.TopLeft).getHeight()));
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.TopRight), new SRect(new SPoint((b.getWidth() - getImagePart(ImagePart.TopRight).getWidth()), 0), getImagePart(ImagePart.TopLeft).getWidth(), getImagePart(ImagePart.TopLeft).getHeight()));
            drawImageFill(finalGraphics, getImagePart(ImagePart.Top), new SRect(m_DivX, 0, b.getWidth() - getImagePart(ImagePart.TopLeft).getWidth() - getImagePart(ImagePart.TopRight).getWidth(), getImagePart(ImagePart.Top).getHeight()));

            // Draw the side borders.
            drawImageFill(finalGraphics, getImagePart(ImagePart.Left), new SRect(new SPoint(0, m_DivY), new SSize(getImagePart(ImagePart.Left).getWidth(), b.getHeight() - getImagePart(ImagePart.TopLeft).getHeight() - getImagePart(ImagePart.BottomLeft).getHeight())));
            drawImageFill(finalGraphics, getImagePart(ImagePart.Right), new SRect(new SPoint(b.getWidth() - getImagePart(ImagePart.Right).getWidth(), m_DivY), new SSize(getImagePart(ImagePart.Left).getWidth(), b.getHeight() - getImagePart(ImagePart.TopRight).getHeight() - getImagePart(ImagePart.BottomRight).getHeight())));

            // Draw the bottom borders.
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.BottomLeft), new SRect(0, b.getHeight() - getImagePart(ImagePart.BottomLeft).getHeight(), getImagePart(ImagePart.BottomLeft).getWidth(), getImagePart(ImagePart.BottomLeft).getHeight()));
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.BottomRight), new SRect(new SPoint(b.getWidth() - getImagePart(ImagePart.BottomRight).getWidth(), b.getHeight() - getImagePart(ImagePart.BottomRight).getHeight()), getImagePart(ImagePart.BottomRight).getWidth(), getImagePart(ImagePart.BottomRight).getHeight()));
            drawImageFill(finalGraphics, getImagePart(ImagePart.Bottom), new SRect(new SPoint(m_DivX, b.getHeight() - getImagePart(ImagePart.Bottom).getHeight()), b.getWidth() - getImagePart(ImagePart.BottomLeft).getWidth() - getImagePart(ImagePart.BottomRight).getWidth(), getImagePart(ImagePart.Bottom).getHeight()));

            // Draw the center.
            drawImageFill(finalGraphics, getImagePart(ImagePart.Center), new SRect(new SPoint(m_DivX, m_DivY), new SSize(b.getWidth() - getImagePart(ImagePart.Left).getWidth() - getImagePart(ImagePart.Right).getWidth(), b.getHeight() - getImagePart(ImagePart.Top).getHeight() - getImagePart(ImagePart.Bottom).getHeight())));

            // Finish drawing.
            return b;
        }

        if (m_TypeRender == BorderType.Normal)
        {
            finalGraphics.drawImage(m_OriginalImage, 0, 0, p_Size.width, p_Size.height, null);
            return b;
        }

        if (m_TypeRender == BorderType.EndCap)
        {
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.Left), new SRect(new SPoint(0, 0), new SSize(getImagePart(ImagePart.Left).getWidth(), b.getHeight())));
            drawImageFill(finalGraphics, getImagePart(ImagePart.Center), new SRect(new SPoint(m_DivX, 0), new SSize(b.getWidth() - getImagePart(ImagePart.Left).getWidth() - getImagePart(ImagePart.Right).getWidth(), getImagePart(ImagePart.Center).getHeight())));
            drawImageUnscaledAndClipped(finalGraphics, getImagePart(ImagePart.Right), new SRect(new SPoint(b.getWidth() - getImagePart(ImagePart.Right).getWidth(), 0), new SSize(getImagePart(ImagePart.Right).getWidth(), b.getHeight())));
        }

        return b;
    }
    
    public BufferedImage getImagePart(ImagePart p_ImagePart)
    {
        if (m_CuttedImages.containsKey(p_ImagePart))
            return m_CuttedImages.get(p_ImagePart);

        return null;
    }
    
    private void drawImageFill(Graphics2D p_Graphics, BufferedImage p_Image, SRect p_Rectangle)
    {
        int curx = p_Rectangle.origin.x;
        int maxx = p_Rectangle.origin.x + p_Rectangle.size.width;

        int cury = p_Rectangle.origin.y;
        int maxy = p_Rectangle.origin.y + p_Rectangle.size.height;

        while (cury < maxy)
        {
            while (curx < maxx)
            {

                if (curx + p_Image.getWidth() <= maxx && cury + p_Image.getHeight() <= maxy)
                {
                    drawImageUnscaled(p_Graphics, p_Image, new SPoint(curx, cury));
                    //p_Graphics.DrawRectangle(new Pen(Brushes.Red, 2f), new Rectangle(new Point(curx, cury), new Size(Math.Min(maxx - curx,p_Image.Width), Math.Min(maxy - cury, p_Image.Height))));
                }
                else
                {
                    drawImageUnscaledAndClipped(p_Graphics, p_Image, new SRect(new SPoint(curx, cury), new SSize(Math.min(maxx - curx,p_Image.getWidth()), Math.min(maxy - cury, p_Image.getHeight()))));
                    //p_Graphics.DrawRectangle(new Pen(Brushes.Yellow, 2f), new Rectangle(new Point(curx, cury), new Size(Math.Min(maxx - curx,p_Image.Width), Math.Min(maxy - cury, p_Image.Height))));
                }

                curx = curx + p_Image.getWidth();

            }

            curx = p_Rectangle.origin.x;
            cury = cury + p_Image.getHeight();
        }
    }
    
    private void drawImageUnscaled(Graphics2D g, BufferedImage img, SPoint pt)
    {
        //TODO
        g.drawImage(img, pt.x, pt.y, null);
    }
    
    private void drawImageUnscaledAndClipped(Graphics2D g, BufferedImage img, SRect r)
    {
        //TODO
        g.clipRect(r.origin.x, r.origin.y, r.size.width, r.size.height);
        g.drawImage(img, r.origin.x, r.origin.y, null);
        g.setClip(null);
    }
    
    private BufferedImage rotateFlip(BufferedImage img, RotateFlipType rotateFlipType)
    {
        float angle = 0;
        if (rotateFlipType == RotateFlipType.Rotate90FlipNone)
            angle = 90;
        
        angle = (float)Math.toRadians(angle);
        
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = img.getWidth(), h = img.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
        BufferedImage result = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(angle, w/2, h/2);
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return result;
    }
    
    
}
