/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

import com.doubotis.sc2dd.data.Guid;
import com.doubotis.sc2dd.data.IPropertyMore;
import com.sun.scenario.effect.impl.state.AccessHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import sun.security.krb5.JavaxSecurityAuthKerberosAccess;
import sun.swing.DefaultLookup;

/**
 *
 * @author Christophe
 */
public class PropertyRenderer extends DefaultTableCellRenderer
{    
    Color backgroundColor = Color.LIGHT_GRAY;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {     
        PropertyAdapter model = (PropertyAdapter) table.getModel();
        Object rowValue = model.getValueAt(row, 0);
        boolean isRowEditable = model.isCellEditable(row, 1);
        
        if (rowValue.getClass() == PropertyCategory.class)
        {
            Component label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBackground(backgroundColor);
            label.setForeground(Color.BLACK);
            label.setFont(new Font(label.getFont().getName(), Font.BOLD, 11));
            setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
            return label;
        }
        else
        {
            Component c = null;
            if (column == 0)
            {
                c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
            else if (column == 1)
            {
                if (value instanceof Boolean)
                {
                    JCheckBox checkbox = new JCheckBox();
                    checkbox.setSelected((Boolean)value);
                    
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());
                    panel.add(checkbox, BorderLayout.WEST);
                    
                    panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                    c = checkbox;
                }
                else if (value instanceof Guid)
                {
                    c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    Guid guid = (Guid)value;
                    c.setForeground(Color.LIGHT_GRAY);
                }
                else if (value instanceof IPropertyMore)
                {
                    JLabel label = new JLabel();
                    label.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
                    label.setText(value.toString());
                    
                    if (isSelected)
                        label.setForeground(Color.WHITE);
                    else
                        label.setForeground(Color.BLACK);
                    
                    JButton button = new JButton();
                    button = new JButton();
                    button.setText("...");
                    button.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
                    
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());
                    panel.add(label, BorderLayout.WEST);
                    panel.add(button, BorderLayout.EAST);
                    
                    panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                    
                    c = panel;
                }
                else
                {
                   c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
            
            setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
            
            if (isSelected)
            {
                c.setBackground(UIManager.getColor("EditorPane.selectionBackground"));
                c.setForeground(Color.WHITE);
            }
            else
            {
                c.setBackground(Color.WHITE);
                if (!isRowEditable && column == 1)
                    c.setForeground(Color.LIGHT_GRAY);
                else
                    c.setForeground(Color.BLACK);
            }
            return c;   
        }
    }
    
    
}
