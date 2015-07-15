/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.app.Preferences;
import static com.doubotis.sc2dd.app.Preferences.FILENAME_PROPS;
import com.doubotis.sc2dd.util.PreferenceUtils;
import com.doubotis.sc2dd.util.ResourceUtils;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Christophe
 */
public class DefaultProperties
{
    private Properties mDefaultProps;
    
    public static DefaultProperties defaultProperties()
    {
        return new DefaultProperties();
    }
    
    private DefaultProperties()
    {
        mDefaultProps = new Properties();
        try
        {
            File f = new File(PreferenceUtils.getUserDataDirectory() + Preferences.FILENAME_DEFAULTS);
            if (!f.exists())
            {
                // Copy the version inside /res package folders.
                f.createNewFile();
                Path src = Paths.get(ResourceUtils.getURLResource("defaults.properties").toURI());
                Path dst = Paths.get(PreferenceUtils.getUserDataDirectory() + Preferences.FILENAME_DEFAULTS);
                Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            }
        
            mDefaultProps.load(new FileInputStream(f));
            
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public SResource getDefault(String identifier)
    {
        if (identifier.endsWith("image"))
        {
            String prop = mDefaultProps.getProperty(identifier, "Liberty.SC2Mod" + File.separator + "Base.SC2Assets|Assets\\Textures\\white32.dds");
            String[] strs = prop.split("\\|");
            SFile f = new SFile(findMPQ(strs[0]).getAbsolutePath(), strs[1]);
            SImage image = new SImage(f.getFilePath(), f.getMpqFilePath());
            image.imageSize = new SSize(0,0);
            return image;
        }
        return null;
    }
    
    private static File findMPQ(String identifier)
    {
        List<String> selectedMPQ = App.getApp().getPreferences().getArrayString("options.path.mpq.list", null);
        for (String mpq : selectedMPQ)
        {
            if (mpq.endsWith(identifier))
                return new File(mpq);
        }
        return null;
    }
}
