/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author Christophe
 */
public class CheckedListAdapter extends DefaultListModel<String>
{
    private HashMap<String, Boolean> mCheckedMap = new HashMap<String, Boolean>();
    private List<String> mValues = null;
    
    public CheckedListAdapter(List<String> values)
    {
        mValues = values;
        mCheckedMap.clear();
        for (String o : mValues)
        {
            addElement(o);
            mCheckedMap.put(o, Boolean.TRUE);
        }
    }
    
    public boolean isChecked(String o)
    {
        return mCheckedMap.get(o);
    }
    
    public void setChecked(String o, boolean b)
    {
        mCheckedMap.put(o, b);
    }
    
}
