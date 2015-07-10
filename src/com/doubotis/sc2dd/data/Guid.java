/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import java.util.UUID;

/**
 *
 * @author Christophe
 */
public class Guid
{
    public static final Guid EMPTY = new Guid(new UUID(0, 0));
    
    private UUID mUUID;
    
    public Guid(UUID uuid)
    {
        mUUID = uuid;
    }
    
    @Override
    public String toString()
    {
        return mUUID.toString();
    }
}
