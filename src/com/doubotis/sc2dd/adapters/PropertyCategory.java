/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

/**
 *
 * @author Christophe
 */
public class PropertyCategory
{
    public String category = "";
    
    public PropertyCategory(String category)
    {
        this.category = category;
    }
    
    @Override
    public String toString()
    {
        return category;
    }
}
