/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 *
 * @author Christophe
 */
public interface IDrawable
{
    public void onDraw(Graphics2D canvas, Paint paint, Object o, int width, int height);
}
