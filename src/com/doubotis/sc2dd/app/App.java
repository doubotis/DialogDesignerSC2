/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.app;

import com.doubotis.sc2dd.data.Project;
import com.doubotis.sc2dd.app.Preferences;

/**
 *
 * @author Christophe
 */
public class App
{
    private static App SINGLETON = null;
    
    private Project mOpenedProject;
    private Preferences mPrefs;
    private Preloader mPreloader;
    
    public static App getApp()
    {
        if (SINGLETON == null)
            SINGLETON = new App();
            return SINGLETON;
    }
    
    private App()
    {
        mPreloader = new Preloader();
        mPrefs = new Preferences();
    }
    
    public Project getOpenedProject()
    {
        return mOpenedProject;
    }
    
    public void setOpenedProject(Project opd)
    {
        mOpenedProject = opd;
    }
    
    public Preferences getPreferences() { return mPrefs; }
    public Preloader getPreloader() { return mPreloader; }
}
