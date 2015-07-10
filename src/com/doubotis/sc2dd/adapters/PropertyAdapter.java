/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

import com.doubotis.sc2dd.data.SDialogItem;
import com.doubotis.sc2dd.data.Guid;
import com.doubotis.sc2dd.data.Page;
import com.doubotis.sc2dd.data.PropertyObject;
import com.doubotis.sc2dd.data.SText;
import com.doubotis.sc2dd.locales.LocaleUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Christophe
 */
public class PropertyAdapter extends AbstractTableModel
{
    private PropertyObject mPO;
    private ArrayList<String> mFinalList;
    
    public PropertyAdapter(PropertyObject po)
    {
        mPO = po;
        compute();
    }
    
    public PropertyObject getProperty(){ return mPO; }
    
    private void compute()
    {
        String lastPrefix = "";
        
        mFinalList = new ArrayList<String>();
        
        if (mPO == null)
            return;
        
        List<String> properties = computeMapToOrderedList(mPO.getMap());
        for (int i=0; i < properties.size(); i++)
        {
            String property = properties.get(i);
            String[] splits = property.split("\\.");
            String prefix;
            if (splits.length > 0)
                prefix = splits[0];
            else
                prefix = "";
            
            if (lastPrefix.equals(prefix))
            {
                mFinalList.add(property);
            }
            else
            {
                mFinalList.add(prefix);
                lastPrefix = prefix;
                i--;
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return null;
    }
    
    @Override
    public int getRowCount() {
        return mFinalList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }
    
    public String getRowKey(int row)
    {
        return mFinalList.get(row);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        
        if (columnIndex == 0)
            return false;
        
        String prop = mFinalList.get(rowIndex);
        if (prop.split("\\.").length == 1)
            return false;
        
        if (getValueAt(rowIndex, columnIndex) instanceof Guid)
            return false;
        if (getValueAt(rowIndex, columnIndex) instanceof Page)
            return false;
        
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 0)
        {            
            String prop = mFinalList.get(rowIndex);
            if (prop.split("\\.").length == 2)
            {
                String completeName = LocaleUtils.translateInto("AttributesTitle", prop, "en");
                if (completeName != null)
                    return completeName;
                return capitalizeFirstLetter(prop.split("\\.")[1]);
            }
            else
            {
                String completeName = LocaleUtils.translateInto("AttributesTitle", prop, "en");
                if (completeName != null)
                    return new PropertyCategory(completeName);
                return new PropertyCategory(capitalizeFirstLetter(prop));
            }
            
        }
        else
        {
            Object o =  mPO.getMap().get(mFinalList.get(rowIndex));
            if (o == null)
                return "";
            return o;
        }
    }
    
    public void setValueAt(Object aValue, int rowIndex, int colIndex) {
        
        if (colIndex == 0)
            return;
        
        System.out.println("Setting " + aValue.toString() + "at " + rowIndex + "," + colIndex);
        String identifier = mFinalList.get(rowIndex);
        mPO.getMap().put(identifier, aValue);
    }
    
    private List<String> computeMapToOrderedList(HashMap<String, Object> map)
    {
        ArrayList<String> properties = new ArrayList<String>();
        
        // Retreive the list
        Iterator<String> it = mPO.getMap().keySet().iterator();
        while (it.hasNext())
        {
            properties.add(it.next());
        }
        
        // Now order the list
        properties.sort(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                 return o1.compareToIgnoreCase(o2);
            }
        });
        
        // Now back the list
        return properties;
    }
    
    private static String capitalizeFirstLetter(String str)
    {
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
    
}
