/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.data;

import java.util.HashMap;

/**
 *
 * @author Christophe
 */
public interface IProperty
{
    public HashMap<String, Object> loadProperties();
    public void saveProperties(HashMap<String, Object> map);
}
