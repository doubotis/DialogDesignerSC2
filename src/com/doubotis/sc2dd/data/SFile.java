/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import com.mundi4.mpq.MpqFile;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Christophe
 */
public class SFile implements SResource
{
    String mMpqFile = "";
    String mFileName = "";
    
    public SFile(String mpqFile, String fileName)
    {
        mMpqFile = mpqFile;
        mFileName = fileName;
    }
    
    public String getMpqFilePath() { return mMpqFile; }
    public String getFilePath() { return mFileName; }
    
    @Override
    public String toString()
    {
        return mFileName;
    }
    
    public InputStream open() throws IOException
    {
        MpqFile mpqFile = new MpqFile(mMpqFile);
        return mpqFile.getInputStream(mpqFile.getEntry(mFileName));
    }
}
