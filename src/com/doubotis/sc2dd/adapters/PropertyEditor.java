/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.adapters;

import com.doubotis.sc2dd.data.SAnchor;
import com.doubotis.sc2dd.data.Guid;
import com.doubotis.sc2dd.data.IPropertyMore;
import com.doubotis.sc2dd.data.SPoint;
import com.doubotis.sc2dd.data.SSize;
import com.doubotis.sc2dd.dialogs.IDialogReturn;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Christophe
 */
public class PropertyEditor extends AbstractCellEditor implements TableCellEditor
{
    private TableCellEditor mEditor;
    Color backgroundColor = Color.LIGHT_GRAY;
    JFrame mDialog;
    private Class mClassEditing;
    public Object mOldValue;

    public PropertyEditor(JFrame dialog)
    {
        mDialog = dialog;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (mEditor != null) {
            if (mClassEditing == Integer.class)
            {
                try
                {
                    int val = Integer.parseInt(mEditor.getCellEditorValue().toString());
                    return val;
                    
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                    return mOldValue;
                }
            }
            else
                return mEditor.getCellEditorValue();
        }
        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        
        mOldValue = value;
        
        if (value instanceof String) {
            JTextField field = new JTextField();
            Action action = new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    stopCellEditing();
                }
            };
            field.addActionListener(action);
            mEditor = new DefaultCellEditor(field);
            mClassEditing = String.class;
        } else if (value instanceof Integer) {
            JTextField field = new JTextField();
            Action action = new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    stopCellEditing();
                }
            };
            field.addActionListener(action);
            mEditor = new DefaultCellEditor(field);
            mClassEditing = Integer.class;
        } else if (value instanceof Boolean) {
            mEditor = new DefaultCellEditor(new JCheckBox());
            mClassEditing = Boolean.class;
        } else if (value instanceof IPropertyMore) {
            mEditor = new DialogCellEditor(mDialog);
            mClassEditing = value.getClass();
        } else if (value instanceof SSize) {
            mEditor = new DialogCellEditor(mDialog);
        } else if (value.getClass().isEnum()) {
            JComboBox comboBox = new JComboBox();
            DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
            model.removeAllElements();
            ArrayList<Object> objects = enumValues(((Enum)value).getClass());
            for (int i=0; i < objects.size(); i++)
            {
                model.addElement(objects.get(i));
            }
            mEditor = new DefaultCellEditor(comboBox);
            mClassEditing = Enum.class;
        }
        else {
            mEditor = new DefaultCellEditor(new JTextField());
        }

        return mEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
    
    static <E extends Enum <E>> ArrayList<Object> enumValues(Class<E> elemType) {
        
        ArrayList<Object> objects = new ArrayList<Object>();
        
        for (E e : java.util.EnumSet.allOf(elemType)) {
            objects.add(e);
        }
        return objects;
    }
    
    public static class NumericCellEditor extends DefaultCellEditor
    {
        public NumericCellEditor(JTextField textField) {
            super(textField);
        }
        
        @Override
        public Object getCellEditorValue()
        {
            return Integer.parseInt(super.getCellEditorValue().toString());
        }
    }
    
    public static class DialogCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
    {
        Object mObject;
        JPanel mPanel;
        JLabel mLabel;
        JButton mButton;
        JFrame mDialog;
        
        public DialogCellEditor(JFrame dialog) {
            
            mDialog = dialog;
            
            mLabel = new JLabel();
            mLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

            mButton = new JButton();
            mButton = new JButton();
            mButton.setText("...");
            mButton.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
            mButton.addActionListener(this);

            mPanel = new JPanel();
            mPanel.setLayout(new BorderLayout());
            mPanel.add(mLabel, BorderLayout.WEST);
            mPanel.add(mButton, BorderLayout.EAST);

            mPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
        {
            PropertyAdapter model = (PropertyAdapter) table.getModel();
            Object rowValue = model.getValueAt(row, 0);
            boolean isRowEditable = model.isCellEditable(row, 1);
            
            mObject = value;
            mLabel.setText(mObject.toString());
            return mPanel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame owner = mDialog;
            IPropertyMore more = (IPropertyMore)mObject;
            Dialog d = more.createDialog(owner);
            IDialogReturn idr = (IDialogReturn)d;
            Object res = idr.showDialog();
            if (res != null)
            {
                mObject = res;
                System.out.println("Selected Object");
            }
            
            mLabel.setText(getCellEditorValue().toString());
            //fireEditingStopped();
            stopCellEditing();
        }

        @Override
        public Object getCellEditorValue() {
            
            System.out.println("Returning " + mObject.toString());
            return mObject;
        }
        
        private String computeInput(Object aValue)
        {
            SSize s = (SSize)aValue;
            String input = s.width + ";" + s.height;
            return input;
        }
    }
}
