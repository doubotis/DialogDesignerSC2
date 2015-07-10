/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christophe
 */
public class Project implements Serializable
{
    // Variables
    
    // (Status)
    private boolean mIsSaved = false;
    private String mSavedPath = null;
    private Page mCurrentPage = null;
    // (Data)
    private ArrayList<Page> mPages = new ArrayList<Page>();
    private ArrayList<SObject> mObjects = new ArrayList<SObject>();
    private int mUniqueNameIndex = 0;
    
    private Project()
    {
        
    }
    
    public boolean isSaved() { return mIsSaved; }
    
    public List<Page> getPages() { return mPages; }
    public Page getCurrentPage() { return mCurrentPage; }
    public void setCurrentPage(Page page) { mCurrentPage = page; }
    
    public static Project load(String path) throws Exception
    {
        FileInputStream streamIn = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(streamIn);
        Project readObject = (Project) ois.readObject();
        readObject.mIsSaved = true;
        readObject.mSavedPath = path;
        if (readObject.mPages.size() > 0)
        {
            readObject.mCurrentPage = readObject.mPages.get(0);
        }
        return readObject;
    }
    
    public static Project create() throws Exception
    {
        Project p = new Project();
        p.mPages.add(new Page("Page 1"));
        p.mCurrentPage = p.mPages.get(0);
        p.mIsSaved = false;
        p.mSavedPath = null;
        return p;
    }
    
    public void save(String path) throws Exception
    {
        FileOutputStream streamIn = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(streamIn);
        this.mIsSaved = true;
        this.mSavedPath = path;
        oos.writeObject(this);
    }
    
    public void save() throws Exception
    {
        if (mSavedPath == null)
        {
            throw new IllegalStateException("Cannot save without parameter. Must call the save(String path) method before.");
        }
    }
    
    public String makeNewUniqueName()
    {
        String uniqueName = mUniqueNameIndex + "";
        mUniqueNameIndex++;
        return uniqueName;
    }
}
