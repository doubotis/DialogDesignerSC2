/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd;

import com.doubotis.sc2dd.adapters.PropertyAdapter;
import com.doubotis.sc2dd.adapters.PropertyEditor;
import com.doubotis.sc2dd.adapters.PropertyRenderer;
import com.doubotis.sc2dd.adapters.SObjectAdapter;
import com.doubotis.sc2dd.adapters.SObjectTreeCellRenderer;
import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.app.Preloader;
import com.doubotis.sc2dd.data.SAnchor;
import com.doubotis.sc2dd.data.SDialogItem;
import com.doubotis.sc2dd.data.Guid;
import com.doubotis.sc2dd.data.IProperty;
import com.doubotis.sc2dd.data.Page;
import com.doubotis.sc2dd.data.Project;
import com.doubotis.sc2dd.data.SPoint;
import com.doubotis.sc2dd.data.PropertyObject;
import com.doubotis.sc2dd.data.SDialog;
import com.doubotis.sc2dd.data.SDialogItemButton;
import com.doubotis.sc2dd.data.SDialogItemImage;
import com.doubotis.sc2dd.data.SDialogItemLabel;
import com.doubotis.sc2dd.data.SDialogItemList;
import com.doubotis.sc2dd.data.SObject;
import com.doubotis.sc2dd.data.SSize;
import com.doubotis.sc2dd.data.SText;
import com.doubotis.sc2dd.dialogs.DialogPropertySObject;
import com.doubotis.sc2dd.locales.LocaleUtils;
import com.doubotis.sc2dd.ui.ImageCaching;
import com.doubotis.sc2dd.ui.JPageDrawer;
import com.doubotis.sc2dd.util.ResourceUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Christophe
 */
public class FrameMain extends javax.swing.JFrame {
    
    // Variables

    /**
     * Creates new form NewJFrame
     */
    public FrameMain() {
        initComponents();
        
        jSplitPane1.setResizeWeight(1d);
        
        Dimension d = lblPropertiesInfoTitle.getPreferredSize();
        d.width = 0;
        lblPropertiesInfoTitle.setPreferredSize(d);
        lblPropertiesInfoTitle.setMaximumSize(d);
        lblPropertiesInfoTitle.setMinimumSize(d);
        
        d = lblPropertiesInfoCaption.getPreferredSize();
        
        d.width = 0;
        lblPropertiesInfoCaption.setPreferredSize(d);
        lblPropertiesInfoCaption.setMaximumSize(d);
        lblPropertiesInfoCaption.setMinimumSize(d);

        PropertyAdapter adapter = new PropertyAdapter(null);
        PropertyRenderer renderer = new PropertyRenderer();
        PropertyEditor editor = new PropertyEditor(this);
        editor.addCellEditorListener(new CellEditorListener() {

            @Override
            public void editingStopped(ChangeEvent e) {
                // When done. Let's save!
                PropertyObject po = ((PropertyAdapter)tableProperties.getModel()).getProperty();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)treObjects.getLastSelectedPathComponent();
                IProperty o = (IProperty)node.getUserObject();
                o.saveProperties(po.getMap());
                
                refreshTree();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                // When cancelled. Do nothing.
                
                
            }
        });
        tableProperties.setModel(adapter);
        tableProperties.setDefaultRenderer(Object.class, renderer);
        tableProperties.setDefaultEditor(Object.class, editor);
        tableProperties.getTableHeader().setVisible(false);
        //tableProperties.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tableProperties.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;
                
                int chosen = tableProperties.getSelectedRow();
                
                PropertyAdapter pa = (PropertyAdapter)tableProperties.getModel();
                if (chosen == -1)
                {
                    lblPropertiesInfoTitle.setText("");
                    lblPropertiesInfoCaption.setText("");
                }
                else
                {
                    String key = pa.getRowKey(chosen);

                    String title = LocaleUtils.translateInto("AttributesTitle", key, "en");
                    String caption = LocaleUtils.translateInto("AttributesCaption", key, "en");
                    lblPropertiesInfoTitle.setText(title + " [" + key + "]");
                    lblPropertiesInfoCaption.setText("<html>" + caption + "</html>");
                }
            }
        });
        
        treObjects.setCellRenderer(new SObjectTreeCellRenderer());
        BasicTreeUI treeUI = (BasicTreeUI) treObjects.getUI();
        treeUI.setCollapsedIcon(null);
        treeUI.setExpandedIcon(null);
        
        treObjects.addTreeWillExpandListener(new TreeWillExpandListener() {

            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                throw new ExpandVetoException(event);
            }
        });
        
        refreshUI();
    }
    
    // Methods
    private void createProject()
    {
        try
        {
            if (App.getApp().getOpenedProject() != null)
                App.getApp().setOpenedProject(null);
            
            refreshUI();
            
            App.getApp().setOpenedProject(Project.create());
            refreshUI();
            
        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString(), "Error while creating project", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openProject()
    {
        try
        {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File f = fileChooser.getSelectedFile();
                if (!f.exists())
                    throw new Exception("File doesn't exists!");
                
                String path = f.getAbsolutePath();
                
                if (App.getApp().getOpenedProject() != null)
                App.getApp().setOpenedProject(null);
                refreshUI();
            
                App.getApp().setOpenedProject(Project.load(path));
                refreshUI();
            }
            
        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString(), "Error while opening project", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void closeProject()
    {
        if (App.getApp().getOpenedProject() == null)
            return;
        
        try
        {
            App.getApp().setOpenedProject(null);
            refreshUI();
            
        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString(), "Error while closing project", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveProject()
    {
        if (App.getApp().getOpenedProject() == null)
            return;
                
        try
        {
            if (!App.getApp().getOpenedProject().isSaved())
            {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    File f = fileChooser.getSelectedFile();
                    String path = f.getAbsolutePath();

                    App.getApp().getOpenedProject().save(path);
                    refreshUI();
                }
            }
            else
            {
                App.getApp().getOpenedProject().save();
                refreshUI();
            }
            
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString(), "Error while saving project", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveAsProject()
    {
        if (App.getApp().getOpenedProject() == null)
            return;
        
        try
        {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File f = fileChooser.getSelectedFile();
                String path = f.getAbsolutePath();

                App.getApp().getOpenedProject().save(path);
                refreshUI();
            }
            
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString(), "Error while saving project", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exit()
    {
        
    }
    
    private void addSC2Object(Class theClass)
    {
        if (App.getApp().getOpenedProject() == null)
            return;
        
        try
        {
        if (theClass == SDialog.class)
        {
            SObject o = (SObject)theClass.newInstance();
            App.getApp().getOpenedProject().getCurrentPage().getObjects().add(o);
            refreshTree();
        }
        else
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treObjects.getLastSelectedPathComponent();
            if (node != null)
            {
                SObject theObject = (SObject) node.getUserObject();
                if (theObject instanceof SDialog)
                {
                    SDialog selectedDialog =  (SDialog)theObject;
                    Constructor c = theClass.getConstructor(SDialog.class);
                    SObject o = (SObject)c.newInstance(new Object[] { selectedDialog });
                    App.getApp().getOpenedProject().getCurrentPage().getObjects().add(o);
                    refreshTree();
                }
                else if (theObject instanceof SDialogItem)
                {
                    SDialog selectedDialog = ((SDialogItem)theObject).parentDialog;
                    Constructor c = theClass.getConstructor(SDialog.class);
                    SObject o = (SObject)c.newInstance(new Object[] { selectedDialog });
                    App.getApp().getOpenedProject().getCurrentPage().getObjects().add(o);
                    refreshTree();
                }
                else
                {
                    // Ask to the user which dialog to use.
                    DialogPropertySObject dialog = new DialogPropertySObject(this, true, App.getApp().getOpenedProject().getCurrentPage(), SDialog.class);
                    dialog.setSize(400,300);
                    dialog.setLocationRelativeTo(null);
                    dialog.setTitle("Create Dialog Item");
                    SDialog selectedDialog = (SDialog)dialog.showDialog();
                    if (selectedDialog != null)
                    {
                        Constructor c = theClass.getConstructor(SDialog.class);
                        SObject o = (SObject)c.newInstance(new Object[] { selectedDialog });
                        App.getApp().getOpenedProject().getCurrentPage().getObjects().add(o);
                        refreshTree();
                    }
                }
            }
            else
            {
                // Ask to the user which dialog to use.
                DialogPropertySObject dialog = new DialogPropertySObject(this, true, App.getApp().getOpenedProject().getCurrentPage(), SDialog.class);
                dialog.setSize(400,300);
                dialog.setLocationRelativeTo(null);
                dialog.setTitle("Create Dialog Item");
                SDialog selectedDialog = (SDialog)dialog.showDialog();
                if (selectedDialog != null)
                {
                    Constructor c = theClass.getConstructor(SDialog.class);
                    SObject o = (SObject)c.newInstance(new Object[] { selectedDialog });
                    App.getApp().getOpenedProject().getCurrentPage().getObjects().add(o);
                    refreshTree();
                }
            }
        }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        refreshCanvas();
    }
    
    private void refreshUI()
    {
        if (App.getApp().getOpenedProject() == null)
        {
            pnlProperties.setVisible(false);
            tbpPages.setVisible(false);
            while (tbpPages.getTabCount() > 0)
                tbpPages.removeTabAt(0);
            
            treObjects.clearSelection();
            treObjects.setModel(null);
        }
        else
        {
            pnlProperties.setVisible(true);
            tbpPages.setVisible(true);
            
            for (int i=0; i < App.getApp().getOpenedProject().getPages().size(); i++)
            {
                Page p = App.getApp().getOpenedProject().getPages().get(i);
                JPageDrawer panel = new JPageDrawer();
                tbpPages.addTab(p.name, panel);
            }
            
            treObjects.clearSelection();
            tbpPagesStateChanged(null);
            
        }
    }
    
    private void refreshTree()
    {
        int[] rows = treObjects.getSelectionRows();
        SObject o = null;
        if (rows.length > 0)
        {
            TreePath path = treObjects.getPathForRow(rows[0]);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treObjects.getLastSelectedPathComponent();
            o = (SObject)node.getUserObject();
        }
            
        // Expand all
        SObjectAdapter treeModel = (SObjectAdapter) treObjects.getModel();
        treeModel.reload();
        for (int i = 0; i < treObjects.getRowCount(); i++) {
            treObjects.expandRow(i);
        }

        if (o != null)
        {
            DefaultMutableTreeNode nodeToSelect = (DefaultMutableTreeNode)treeModel.findTreeNodeForSObject(o);
            TreeNode[] tn = nodeToSelect.getPath();
            if (tn != null)
            {
                treObjects.setSelectionPath(new TreePath(tn));
            }
        }
    }
    
    private void refreshPropertyForObject(SObject object)
    {
        PropertyAdapter oldAdapter = (PropertyAdapter)tableProperties.getModel();
        PropertyObject oldObject = (PropertyObject)oldAdapter.getProperty();
        if (oldObject != null && object != null)
        {
            String oldGUID = oldObject.getMap().get("patronym.guid").toString();
            String newGUID = object.guid.toString();

            if (oldGUID.equals(newGUID))
                return;
        }
        
        PropertyObject po;
        
        if (object == null)
        {
            po = null;
            lblSelectedItem.setText("");
            lblSelectedItemType.setText("");
        }
        else
        {
            po = new PropertyObject(object.loadProperties());
            lblSelectedItem.setText(object.toString());
            lblSelectedItemType.setText(" (" + object.getClass().getSimpleName() + ")");
        }
        
        
        PropertyAdapter adapter = new PropertyAdapter(po);
        tableProperties.setModel(adapter);
    }
    
    private void refreshCanvas()
    {
        Component c = tbpPages.getSelectedComponent();
        if (c != null)
            c.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jSeparator8 = new javax.swing.JSeparator();
        jToolBar1 = new javax.swing.JToolBar();
        btnFileNew = new javax.swing.JButton();
        btnFileOpen = new javax.swing.JButton();
        btnFileClose = new javax.swing.JButton();
        btnFileSave = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        btnEditUndo = new javax.swing.JButton();
        btnEditRedo = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jToolBar5 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        toolBtnCreateDialog = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        toolBtnCreateDialogItem = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        toolBtnCreateImage = new javax.swing.JButton();
        toolBtnCreateLabel = new javax.swing.JButton();
        toolBtnCreateButton = new javax.swing.JButton();
        toolBtnCreateTextField = new javax.swing.JButton();
        toolBtnCreateList = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        tbpPages = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jButton18 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        treObjects = new javax.swing.JTree();
        jToolBar4 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        lblSelectedItem = new javax.swing.JLabel();
        lblSelectedItemType = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        btnLockSelectedItem = new javax.swing.JButton();
        pnlProperties = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableProperties = new javax.swing.JTable();
        panelPropertiesInfo = new javax.swing.JPanel();
        lblPropertiesInfoTitle = new javax.swing.JLabel();
        lblPropertiesInfoCaption = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        tsmiNew = new javax.swing.JMenuItem();
        tsmiOpen = new javax.swing.JMenuItem();
        tsmiClose = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        tsmiSave = new javax.swing.JMenuItem();
        tsmiSaveAs = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        tsmiExit = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        tsmiOptions = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        tsmiImageViewer = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem30 = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SC2 Dialog Designer");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnFileNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_new.png"))); // NOI18N
        btnFileNew.setFocusable(false);
        btnFileNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFileNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileNewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFileNew);

        btnFileOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_open.png"))); // NOI18N
        btnFileOpen.setFocusable(false);
        btnFileOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFileOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnFileOpen);

        btnFileClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_delete.png"))); // NOI18N
        btnFileClose.setFocusable(false);
        btnFileClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFileClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnFileClose);

        btnFileSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_save.png"))); // NOI18N
        btnFileSave.setFocusable(false);
        btnFileSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFileSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnFileSave);
        jToolBar1.add(jSeparator7);

        btnEditUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_undo.png"))); // NOI18N
        btnEditUndo.setFocusable(false);
        btnEditUndo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditUndo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnEditUndo);

        btnEditRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_redo.png"))); // NOI18N
        btnEditRedo.setFocusable(false);
        btnEditRedo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditRedo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnEditRedo);
        jToolBar1.add(jSeparator9);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_cut.png"))); // NOI18N
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_copy.png"))); // NOI18N
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton8);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_paste.png"))); // NOI18N
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton9);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/btn_delete.png"))); // NOI18N
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton10);

        jToolBar5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, java.awt.SystemColor.controlHighlight));
        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/icon_info.png"))); // NOI18N
        jLabel4.setText("File Untitled Document is opened.");
        jToolBar5.add(jLabel4);

        jToolBar2.setFloatable(false);
        jToolBar2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar2.setRollover(true);

        toolBtnCreateDialog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_dialog.png"))); // NOI18N
        toolBtnCreateDialog.setFocusable(false);
        toolBtnCreateDialog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBtnCreateDialog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBtnCreateDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBtnCreateDialogActionPerformed(evt);
            }
        });
        jToolBar2.add(toolBtnCreateDialog);
        jToolBar2.add(jSeparator10);

        toolBtnCreateDialogItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_panel.png"))); // NOI18N
        toolBtnCreateDialogItem.setFocusable(false);
        toolBtnCreateDialogItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBtnCreateDialogItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBtnCreateDialogItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBtnCreateDialogItemActionPerformed(evt);
            }
        });
        jToolBar2.add(toolBtnCreateDialogItem);
        jToolBar2.add(jSeparator11);

        toolBtnCreateImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_image.png"))); // NOI18N
        toolBtnCreateImage.setFocusable(false);
        toolBtnCreateImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBtnCreateImage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBtnCreateImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBtnCreateImageActionPerformed(evt);
            }
        });
        jToolBar2.add(toolBtnCreateImage);

        toolBtnCreateLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_label.png"))); // NOI18N
        toolBtnCreateLabel.setFocusable(false);
        toolBtnCreateLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBtnCreateLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBtnCreateLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBtnCreateLabelActionPerformed(evt);
            }
        });
        jToolBar2.add(toolBtnCreateLabel);

        toolBtnCreateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_button.png"))); // NOI18N
        toolBtnCreateButton.setEnabled(false);
        toolBtnCreateButton.setFocusable(false);
        toolBtnCreateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBtnCreateButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBtnCreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBtnCreateButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(toolBtnCreateButton);

        toolBtnCreateTextField.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_textbox.png"))); // NOI18N
        toolBtnCreateTextField.setEnabled(false);
        toolBtnCreateTextField.setFocusable(false);
        toolBtnCreateTextField.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBtnCreateTextField.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(toolBtnCreateTextField);

        toolBtnCreateList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_listbox.png"))); // NOI18N
        toolBtnCreateList.setEnabled(false);
        toolBtnCreateList.setFocusable(false);
        toolBtnCreateList.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBtnCreateList.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBtnCreateList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBtnCreateListActionPerformed(evt);
            }
        });
        jToolBar2.add(toolBtnCreateList);

        jSplitPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSplitPane1.setDividerLocation(600);

        tbpPages.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tbpPagesStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 661, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        tbpPages.addTab("tab2", jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 661, Short.MAX_VALUE)
        );

        tbpPages.addTab("New Page [Design]", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpPages)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpPages)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/tools_treeview.png"))); // NOI18N
        jButton18.setFocusable(false);
        jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton18);

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/tools_fullscreen.png"))); // NOI18N
        jButton20.setFocusable(false);
        jButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton20);

        treObjects.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treObjectsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treObjects);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Selected Object:");
        jToolBar4.add(jLabel1);

        lblSelectedItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/comp_label.png"))); // NOI18N
        lblSelectedItem.setText("Label2");
        jToolBar4.add(lblSelectedItem);

        lblSelectedItemType.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblSelectedItemType.setText(" (Label)");
        jToolBar4.add(lblSelectedItemType);
        jToolBar4.add(filler1);

        btnLockSelectedItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/tools_lock.png"))); // NOI18N
        btnLockSelectedItem.setFocusable(false);
        btnLockSelectedItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLockSelectedItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(btnLockSelectedItem);

        pnlProperties.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tableProperties.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tableProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Visibility", "True"},
                {"Anchor", "LEFT"},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tableProperties.setToolTipText("");
        tableProperties.setGridColor(java.awt.Color.lightGray);
        tableProperties.setName(""); // NOI18N
        tableProperties.setRowHeight(20);
        tableProperties.setRowMargin(0);
        tableProperties.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tableProperties);

        panelPropertiesInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelPropertiesInfo.setMaximumSize(new java.awt.Dimension(265, 77));
        panelPropertiesInfo.setMinimumSize(new java.awt.Dimension(265, 77));

        lblPropertiesInfoTitle.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPropertiesInfoTitle.setText(" ");
        lblPropertiesInfoTitle.setFocusable(false);

        lblPropertiesInfoCaption.setText(" ");
        lblPropertiesInfoCaption.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblPropertiesInfoCaption.setFocusable(false);

        javax.swing.GroupLayout panelPropertiesInfoLayout = new javax.swing.GroupLayout(panelPropertiesInfo);
        panelPropertiesInfo.setLayout(panelPropertiesInfoLayout);
        panelPropertiesInfoLayout.setHorizontalGroup(
            panelPropertiesInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPropertiesInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPropertiesInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPropertiesInfoCaption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPropertiesInfoTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelPropertiesInfoLayout.setVerticalGroup(
            panelPropertiesInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPropertiesInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPropertiesInfoTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPropertiesInfoCaption, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlPropertiesLayout = new javax.swing.GroupLayout(pnlProperties);
        pnlProperties.setLayout(pnlPropertiesLayout);
        pnlPropertiesLayout.setHorizontalGroup(
            pnlPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPropertiesInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        pnlPropertiesLayout.setVerticalGroup(
            pnlPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPropertiesLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPropertiesInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .addComponent(pnlProperties, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlProperties, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel4);

        menuFile.setText("File");

        tsmiNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        tsmiNew.setText("New");
        tsmiNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsmiNewActionPerformed(evt);
            }
        });
        menuFile.add(tsmiNew);

        tsmiOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        tsmiOpen.setText("Open...");
        menuFile.add(tsmiOpen);

        tsmiClose.setText("Close");
        menuFile.add(tsmiClose);
        menuFile.add(jSeparator1);

        tsmiSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        tsmiSave.setText("Save");
        menuFile.add(tsmiSave);

        tsmiSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        tsmiSaveAs.setText("Save As...");
        tsmiSaveAs.setToolTipText("");
        menuFile.add(tsmiSaveAs);
        menuFile.add(jSeparator2);

        tsmiExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        tsmiExit.setText("Exit");
        tsmiExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsmiExitActionPerformed(evt);
            }
        });
        menuFile.add(tsmiExit);

        jMenuBar1.add(menuFile);

        jMenu3.setText("Edit");

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Undo");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Redo");
        jMenu3.add(jMenuItem8);
        jMenu3.add(jSeparator3);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Cut");
        jMenu3.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText("Copy");
        jMenu3.add(jMenuItem10);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Paste");
        jMenu3.add(jMenuItem11);

        jMenuItem12.setText("Template Pasting...");
        jMenu3.add(jMenuItem12);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        jMenuItem13.setText("Delete");
        jMenu3.add(jMenuItem13);
        jMenu3.add(jSeparator4);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem14.setText("Select All");
        jMenu3.add(jMenuItem14);

        jMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem15.setText("Select Subitems");
        jMenu3.add(jMenuItem15);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Pages");

        jMenuItem16.setText("Add");
        jMenu4.add(jMenuItem16);

        jMenuItem17.setText("Rename...");
        jMenu4.add(jMenuItem17);

        jMenuItem18.setText("Delete");
        jMenu4.add(jMenuItem18);
        jMenu4.add(jSeparator5);

        jMenuItem19.setText("Page Properties...");
        jMenu4.add(jMenuItem19);

        jMenuItem20.setText("Design Mode");
        jMenu4.add(jMenuItem20);

        jMenuItem21.setText("XML Mode");
        jMenu4.add(jMenuItem21);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Compiling");
        jMenu5.setToolTipText("");

        jMenuItem22.setText("Run Galaxy...");
        jMenu5.add(jMenuItem22);

        jMenuItem23.setText("Run MACRO...");
        jMenu5.add(jMenuItem23);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Tools");

        tsmiOptions.setText("Options...");
        tsmiOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsmiOptionsActionPerformed(evt);
            }
        });
        jMenu6.add(tsmiOptions);

        jMenuItem25.setText("Data Explorer...");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem25);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Window");

        tsmiImageViewer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/tools_imageviewer.png"))); // NOI18N
        tsmiImageViewer.setText("Dialog Texture Viewer...");
        tsmiImageViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsmiImageViewerActionPerformed(evt);
            }
        });
        jMenu7.add(tsmiImageViewer);

        jMenuBar1.add(jMenu7);

        jMenu8.setText("?");

        jMenuItem27.setText("Online Help");
        jMenu8.add(jMenuItem27);

        jMenuItem28.setText("Show Debug Log...");
        jMenu8.add(jMenuItem28);

        jMenuItem29.setText("Check for Updates...");
        jMenu8.add(jMenuItem29);
        jMenu8.add(jSeparator6);

        jMenuItem30.setText("About...");
        jMenu8.add(jMenuItem30);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1))
            .addComponent(jToolBar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSplitPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void btnFileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileNewActionPerformed
        createProject();
    }//GEN-LAST:event_btnFileNewActionPerformed

    private void tsmiNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tsmiNewActionPerformed
        createProject();
    }//GEN-LAST:event_tsmiNewActionPerformed

    private void toolBtnCreateDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBtnCreateDialogActionPerformed
        addSC2Object(SDialog.class);
    }//GEN-LAST:event_toolBtnCreateDialogActionPerformed

    private void toolBtnCreateDialogItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBtnCreateDialogItemActionPerformed
        addSC2Object(SDialogItem.class);
    }//GEN-LAST:event_toolBtnCreateDialogItemActionPerformed

    private void treObjectsValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treObjectsValueChanged
        
        SObjectAdapter adapter = (SObjectAdapter)treObjects.getModel();
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)treObjects.getLastSelectedPathComponent();
        if (node == null)
        {
            if (!adapter.isReloading())
               refreshPropertyForObject(null);
        }
        else
        {
            SObject o = (SObject)node.getUserObject();
            if (o != null)
                refreshPropertyForObject(o);
        }
        
        refreshCanvas();
        
    }//GEN-LAST:event_treObjectsValueChanged

    private void toolBtnCreateImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBtnCreateImageActionPerformed
        addSC2Object(SDialogItemImage.class);
    }//GEN-LAST:event_toolBtnCreateImageActionPerformed

    private void toolBtnCreateLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBtnCreateLabelActionPerformed
        addSC2Object(SDialogItemLabel.class);
    }//GEN-LAST:event_toolBtnCreateLabelActionPerformed

    private void toolBtnCreateListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBtnCreateListActionPerformed
        addSC2Object(SDialogItemList.class);
    }//GEN-LAST:event_toolBtnCreateListActionPerformed

    private void tbpPagesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tbpPagesStateChanged
        if (App.getApp().getOpenedProject() == null)
            return;
        
        ImageCaching.getCache().clearCache();
        
        App.getApp().getOpenedProject().setCurrentPage(App.getApp().getOpenedProject().getPages().get(tbpPages.getSelectedIndex()));
        
        SObjectAdapter adapter = new SObjectAdapter(App.getApp().getOpenedProject().getCurrentPage());
        treObjects.setModel(adapter);
        
        refreshTree();
        refreshPropertyForObject(null);
        
        refreshCanvas();
        
    }//GEN-LAST:event_tbpPagesStateChanged

    private void tsmiOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tsmiOptionsActionPerformed
        
        DialogOptions frm = new DialogOptions(this, true);
        frm.setLocationRelativeTo(this);
        frm.setVisible(true);
        
        // Refresh rendering
        refreshCanvas();
        
    }//GEN-LAST:event_tsmiOptionsActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        DialogDataExplorer frm = new DialogDataExplorer(this, true);
        frm.setSize(800,600);
        frm.setLocationRelativeTo(this);
        frm.setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void tsmiImageViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tsmiImageViewerActionPerformed
        
        FrameDialogTextureViewer dtv = new FrameDialogTextureViewer();
        dtv.setIconImage(ResourceUtils.getImageResource("dtv-icon.png").getImage());
        dtv.setLocationRelativeTo(null);
        dtv.show();
        
    }//GEN-LAST:event_tsmiImageViewerActionPerformed

    private void tsmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tsmiExitActionPerformed
       
        System.exit(0);
    }//GEN-LAST:event_tsmiExitActionPerformed

    private void toolBtnCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBtnCreateButtonActionPerformed
        
        addSC2Object(SDialogItemButton.class);
    }//GEN-LAST:event_toolBtnCreateButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditRedo;
    private javax.swing.JButton btnEditUndo;
    private javax.swing.JButton btnFileClose;
    private javax.swing.JButton btnFileNew;
    private javax.swing.JButton btnFileOpen;
    private javax.swing.JButton btnFileSave;
    private javax.swing.JButton btnLockSelectedItem;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JLabel lblPropertiesInfoCaption;
    private javax.swing.JLabel lblPropertiesInfoTitle;
    private javax.swing.JLabel lblSelectedItem;
    private javax.swing.JLabel lblSelectedItemType;
    private javax.swing.JMenu menuFile;
    private javax.swing.JPanel panelPropertiesInfo;
    private javax.swing.JPanel pnlProperties;
    private javax.swing.JTable tableProperties;
    private javax.swing.JTabbedPane tbpPages;
    private javax.swing.JButton toolBtnCreateButton;
    private javax.swing.JButton toolBtnCreateDialog;
    private javax.swing.JButton toolBtnCreateDialogItem;
    private javax.swing.JButton toolBtnCreateImage;
    private javax.swing.JButton toolBtnCreateLabel;
    private javax.swing.JButton toolBtnCreateList;
    private javax.swing.JButton toolBtnCreateTextField;
    private javax.swing.JTree treObjects;
    private javax.swing.JMenuItem tsmiClose;
    private javax.swing.JMenuItem tsmiExit;
    private javax.swing.JMenuItem tsmiImageViewer;
    private javax.swing.JMenuItem tsmiNew;
    private javax.swing.JMenuItem tsmiOpen;
    private javax.swing.JMenuItem tsmiOptions;
    private javax.swing.JMenuItem tsmiSave;
    private javax.swing.JMenuItem tsmiSaveAs;
    // End of variables declaration//GEN-END:variables
}
