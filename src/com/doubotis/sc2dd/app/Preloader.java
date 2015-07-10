/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.app;

import com.doubotis.sc2dd.data.SFile;
import com.doubotis.sc2dd.data.SResource;
import com.mundi4.mpq.MpqEntry;
import com.mundi4.mpq.MpqFile;
import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Christophe
 */
public class Preloader
{
    private HashMap<String, Object> mHash = new HashMap<String, Object>();
    
    public Preloader()
    {
        
    }
    
    public void preload() throws PreloaderException
    {
        mHash.clear();
        
        List<SFile> files = new ArrayList<SFile>();
        
        List<String> mpqStrings = App.getApp().getPreferences().getArrayString("options.path.mpq.list", null);
        if (mpqStrings == null)
            return;
        
        List<MpqFile> mpqFiles = new ArrayList<MpqFile>();
        for (int i=0; i < mpqStrings.size(); i++)
        {
            try {
                MpqFile mpq = new MpqFile(mpqStrings.get(i));
                Iterator<MpqEntry> it = mpq.iterator();
                while (it.hasNext())
                {
                    String fileName = it.next().getName();
                    if (fileName != null)
                        files.add(new SFile(mpqStrings.get(i),fileName));
                }
                
            } catch (Exception e) { throw new PreloaderException(e); }
        }
        
        // Now send lists to good ones.
        List<SResource> list = new ArrayList<SResource>();
        
        for (SFile file : files)
        {
            if (file.getFilePath().endsWith(".otf") || file.getFilePath().endsWith(".ttf"))
            {
                list.add(file);
                try
                {
                    InputStream is = file.open();
                    Font ft = Font.createFont(Font.TRUETYPE_FONT, is);
                    mHash.put("Font__" + file.getFilePath(), ft);
                
                } catch (Exception e) { throw new PreloaderException(e); }
            }
        }
        
        
    }
    
    public Object getObject(String key)
    {
        return mHash.get(key);
    }
    
    
    
    public static class PreloaderException extends Exception
    {

        public PreloaderException() {
        }

        public PreloaderException(String message) {
            super(message);
        }

        public PreloaderException(Throwable cause) {
            super(cause);
        }

        public PreloaderException(String message, Throwable cause) {
            super(message, cause);
        }
        
    }
}
