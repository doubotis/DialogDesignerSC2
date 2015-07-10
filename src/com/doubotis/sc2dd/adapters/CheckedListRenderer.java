/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Christophe
 */
public class CheckedListRenderer implements ListCellRenderer<Object>
{
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JCheckBox checkBox = new JCheckBox();
        CheckedListAdapter adapter = (CheckedListAdapter)list.getModel();
        

      // Drawing checkbox, change the appearance here
        checkBox.setFocusPainted(false);
        checkBox.setBorderPainted(true);
        checkBox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : new EmptyBorder(1, 1, 1, 1));
        checkBox.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        checkBox.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        checkBox.setFont(list.getFont());
        checkBox.setEnabled(list.isEnabled());
        checkBox.setText(value.toString());
        checkBox.setSelected(adapter.isChecked((String)value));
        checkBox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox chk = (JCheckBox)e.getSource();
                adapter.setChecked((String)value, chk.isSelected());
            }
        });
        return checkBox;
    }
  }
