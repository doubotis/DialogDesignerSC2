/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd;

import com.doubotis.sc2dd.app.Preferences;
import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.app.Preloader.PreloaderException;
import com.doubotis.sc2dd.util.ResourceUtils;
import com.mundi4.mpq.MpqEntry;
import com.mundi4.mpq.MpqFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Christophe
 */
public class DialogDesignerSC2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        // Prepares the Application.
        App.getApp();
        
        DialogWaiting dw = new DialogWaiting(null, true);
        dw.setLocationRelativeTo(null);
        dw.show();
        
        // Let's preload, based on preferences.
        try
        {
            App.getApp().getPreloader().preload();
            
        } catch (PreloaderException pe)
        {
            JOptionPane.showMessageDialog(null, "An error has occured while trying to preload MPQ files. Please set MPQ paths correctly in Options.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Prepares preferences.
        try {
            Preferences p = App.getApp().getPreferences();
            Preferences.Editor ed = p.editor();
            ed.putInt("applicationVersion", 1000);
            ed.commit();
        } catch (Exception e) {}
        
        /*try {
            File f = new File("C:\\Program Files\\StarCraft II\\Mods\\War3.SC2Mod\\Base.SC2Assets");
            if (!f.exists())
                throw new Exception("!!!");
            
            MpqFile mpq = new MpqFile(f);
            Enumeration<MpqEntry> entries = mpq.entries();
            while (entries.hasMoreElements())
                System.out.println(entries.nextElement().getName());
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }*/
        
        FrameMain frame = new FrameMain();
        ImageIcon ic = ResourceUtils.getImageResource("sc2dd-icon.png");
        frame.setIconImage(ic.getImage());
        frame.setLocationRelativeTo(null);
        frame.show(true);
        
        dw.hide();
    }
    
}
