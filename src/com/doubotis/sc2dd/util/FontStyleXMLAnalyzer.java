/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.util;

import com.doubotis.sc2dd.data.SFontStyle;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Christophe
 */
public class FontStyleXMLAnalyzer {
    
    private HashMap<String, SFontStyle> mStyles;
    private HashMap<String, SFontStyle.SFontStyleConstant> mConstants;
    private String mXML;
    private Document mDocument;
    private Element mRacine;
    
    public FontStyleXMLAnalyzer()
    {
        mStyles = new HashMap<String, SFontStyle>();
        mConstants = new HashMap<String, SFontStyle.SFontStyleConstant>();
    }
    
    public void analyze(InputStream is)
    {
        SAXBuilder sxb = new SAXBuilder();
        try
        {
           Document document = sxb.build(is);
           computeAnalyze(document);
        }
        catch(Exception e){ e.printStackTrace(); }
    }
    
    public List<SFontStyle> getStyles()
    {
        Iterator<SFontStyle> it = mStyles.values().iterator();
        List<SFontStyle> fontStyles = new ArrayList<SFontStyle>();
        while (it.hasNext())
        {
            fontStyles.add(it.next());
        }
        return fontStyles;
    }
    
    private void computeAnalyze(Document doc)
    {
        mRacine = doc.getRootElement();
        List<Element> els = mRacine.getChildren();
        for (Element el : els)
        {
            if (el.getName().equals("Constant"))
            {
                String constantName = el.getAttributeValue("name");
                String constantValue = el.getAttributeValue("val");
                SFontStyle.SFontStyleConstant constant = new SFontStyle.SFontStyleConstant(constantName, constantValue);
                System.out.println(" -XML- Created Constant " + constant.toString());
                mConstants.put(constant.name, constant);
                continue;
            }
            
            if (el.getName().equals("Style"))
            {
                String styleName = el.getAttributeValue("name");
                SFontStyle fontStyle = new SFontStyle(styleName);
                
                // Reading Styles on XML.
                String template = el.getAttributeValue("template");
                String font = el.getAttributeValue("font");
                String height = el.getAttributeValue("height");
                String vjustify = el.getAttributeValue("vjustify");
                String hjustify = el.getAttributeValue("hjustify");
                String fontflags = el.getAttributeValue("fontflags");
                String styleflags = el.getAttributeValue("styleflags");
                String textcolor = el.getAttributeValue("textcolor");
                String disabledcolor = el.getAttributeValue("disabledcolor");
                String highlightcolor = el.getAttributeValue("highlightcolor");
                String hotkeycolor = el.getAttributeValue("hotkeycolor");
                String hyperlinkcolor = el.getAttributeValue("hyperlinkcolor");
                String glowcolor = el.getAttributeValue("glowcolor");
                String shadowoffset = el.getAttributeValue("shadowoffset");
                
                fontStyle.template = template;
                fontStyle.font = font;
                fontStyle.height = height;
                fontStyle.vJustify = vjustify;
                fontStyle.hJustify = hjustify;
                fontStyle.fontFlags = fontflags;
                fontStyle.styleFlags = styleflags;
                fontStyle.textColor = textcolor;
                fontStyle.disabledColor = disabledcolor;
                fontStyle.highlightColor = highlightcolor;
                fontStyle.hotkeyColor = hotkeycolor;
                fontStyle.hyperlinkColor = hyperlinkcolor;
                fontStyle.glowColor = glowcolor;
                fontStyle.shadowOffset = shadowoffset;
                
                if (template != null && !template.equals(""))
                {
                    SFontStyle templateStyle = loadTemplate(template);
                    if (templateStyle != null)
                    {
                        SFontStyle foreignStyle = copy(templateStyle);
                        foreignStyle.name = styleName;
                        foreignStyle.template = template;
                        foreignStyle.font = (fontStyle.font != null) ? fontStyle.font : foreignStyle.font;
                        foreignStyle.height = (fontStyle.height != null) ? fontStyle.height : foreignStyle.height;
                        foreignStyle.vJustify = (fontStyle.vJustify != null) ? fontStyle.vJustify : foreignStyle.vJustify;
                        foreignStyle.hJustify = (fontStyle.hJustify != null) ? fontStyle.hJustify : foreignStyle.hJustify;
                        foreignStyle.fontFlags = (fontStyle.fontFlags != null) ? fontStyle.fontFlags : foreignStyle.fontFlags;
                        foreignStyle.styleFlags = (fontStyle.styleFlags != null) ? fontStyle.styleFlags : foreignStyle.styleFlags;
                        foreignStyle.textColor = (fontStyle.textColor != null) ? fontStyle.textColor : foreignStyle.textColor;
                        foreignStyle.disabledColor = (fontStyle.disabledColor != null) ? fontStyle.disabledColor : foreignStyle.disabledColor;
                        foreignStyle.highlightColor = (fontStyle.highlightColor != null) ? fontStyle.highlightColor : foreignStyle.highlightColor;
                        foreignStyle.hotkeyColor = (fontStyle.hotkeyColor != null) ? fontStyle.hotkeyColor : foreignStyle.hotkeyColor;
                        foreignStyle.hyperlinkColor = (fontStyle.hyperlinkColor != null) ? fontStyle.hyperlinkColor : foreignStyle.hyperlinkColor;
                        foreignStyle.glowColor = (fontStyle.glowColor != null) ? fontStyle.glowColor : foreignStyle.glowColor;
                        foreignStyle.shadowOffset = (fontStyle.shadowOffset != null) ? fontStyle.shadowOffset : foreignStyle.shadowOffset;
                        // Transfer the values into fontStyle.
                        fontStyle = foreignStyle;
                    }
                }
                
                // Now convert constants to true values.
                fontStyle.font = getConstantValueIfExists(fontStyle.font);
                fontStyle.height = getConstantValueIfExists(fontStyle.height);
                fontStyle.vJustify = getConstantValueIfExists(fontStyle.vJustify);
                fontStyle.hJustify = getConstantValueIfExists(fontStyle.hJustify);
                fontStyle.fontFlags = getConstantValueIfExists(fontStyle.fontFlags);
                fontStyle.styleFlags = getConstantValueIfExists(fontStyle.styleFlags);
                fontStyle.textColor = getConstantValueIfExists(fontStyle.textColor);
                fontStyle.disabledColor = getConstantValueIfExists(fontStyle.disabledColor);
                fontStyle.highlightColor = getConstantValueIfExists(fontStyle.highlightColor);
                fontStyle.hotkeyColor = getConstantValueIfExists(fontStyle.hotkeyColor);
                fontStyle.hyperlinkColor = getConstantValueIfExists(fontStyle.hyperlinkColor);
                fontStyle.glowColor = getConstantValueIfExists(fontStyle.glowColor);
                fontStyle.shadowOffset = getConstantValueIfExists(fontStyle.shadowOffset);
                
                // The Font Style is computed! Add it to the list of done styles!
                System.out.println(" -XML- Created Style " + fontStyle.toString());
                mStyles.put(fontStyle.name, fontStyle);
                continue;
            }
        }
    }
    
    private SFontStyle loadTemplate(String template)
    {
        SFontStyle parent = mStyles.get(template);
        if (parent == null)
            return null;
        if (parent.template != null && !parent.template.equals(""))
        {
            SFontStyle grandParent = loadTemplate(parent.template);
            SFontStyle foreignStyle = copy(grandParent);
            foreignStyle.name = parent.name;
            foreignStyle.font = parent.font;
            foreignStyle.height = parent.height;
            foreignStyle.vJustify = parent.vJustify;
            foreignStyle.hJustify = parent.hJustify;
            foreignStyle.fontFlags = parent.fontFlags;
            foreignStyle.styleFlags = parent.styleFlags;
            foreignStyle.textColor = parent.textColor;
            foreignStyle.disabledColor = parent.disabledColor;
            foreignStyle.highlightColor = parent.highlightColor;
            foreignStyle.hotkeyColor = parent.hotkeyColor;
            foreignStyle.hyperlinkColor = parent.hyperlinkColor;
            foreignStyle.glowColor = parent.glowColor;
            foreignStyle.shadowOffset = parent.shadowOffset;
            return foreignStyle;
        }
        return parent;
    }
    
    private String getConstantValueIfExists(String maybeConstant)
    {
        if (maybeConstant == null)
            return "";
        
        if (!maybeConstant.startsWith("#"))
            return maybeConstant;
        
        SFontStyle.SFontStyleConstant cst = mConstants.get(maybeConstant.substring(1));
        if (cst == null)
            return maybeConstant;
        return cst.val;
    }
    
    private SFontStyle copy(SFontStyle fs)
    {
        SFontStyle fontStyle = new SFontStyle(fs.name);
        fontStyle.font = fs.font;
        fontStyle.height = fs.height;
        fontStyle.vJustify = fs.vJustify;
        fontStyle.hJustify = fs.hJustify;
        fontStyle.fontFlags = fs.fontFlags;
        fontStyle.styleFlags = fs.styleFlags;
        fontStyle.textColor = fs.textColor;
        fontStyle.disabledColor = fs.disabledColor;
        fontStyle.highlightColor = fs.highlightColor;
        fontStyle.hotkeyColor = fs.hotkeyColor;
        fontStyle.hyperlinkColor = fs.hyperlinkColor;
        fontStyle.glowColor = fs.glowColor;
        fontStyle.shadowOffset = fs.shadowOffset;
        return fontStyle;
    }
    
}
