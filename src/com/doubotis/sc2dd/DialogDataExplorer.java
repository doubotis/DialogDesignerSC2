/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd;

import com.doubotis.sc2dd.data.SFile;
import com.doubotis.sc2dd.data.SFontStyle;
import com.doubotis.sc2dd.data.SResource;
import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.util.FontStyleXMLAnalyzer;
import com.doubotis.sc2dd.util.ResourceUtils;
import com.doubotis.sc2dd.util.UIUtils;
import com.mundi4.mpq.MpqEntry;
import com.mundi4.mpq.MpqFile;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Christophe
 */
public class DialogDataExplorer extends javax.swing.JDialog {

    private JPanelDataExplorer mTab0;
    private JPanelDataExplorer mTab1;
    private JPanelDataExplorer mTab2;
    private JPanelDataExplorer mTab3;
    private JPanelDataExplorer mTab4;
    
    /**
     * Creates new form DialogDataExplorer
     */
    public DialogDataExplorer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        prepareTabs();
        loadList();
    }
    
    private void prepareTabs()
    {
        mTab0 = new JPanelDataExplorer(JPanelDataExplorer.TEXTS);
        ImageIcon ic0 = ResourceUtils.getImageResource("comp_label.png");
        tbpData.addTab("Fonts",ic0, mTab0);
        
        mTab1 = new JPanelDataExplorer(JPanelDataExplorer.IMAGES);
        ImageIcon ic1 = ResourceUtils.getImageResource("comp_image.png");
        tbpData.addTab("Images", ic1, mTab1);
        
        mTab2 = new JPanelDataExplorer(JPanelDataExplorer.XML_LAYOUTS);
        ImageIcon ic2 = ResourceUtils.getImageResource("comp_template.png");
        tbpData.addTab("XML Layouts", ic2, mTab2);
        
        mTab3 = new JPanelDataExplorer(JPanelDataExplorer.STYLES);
        ImageIcon ic3 = ResourceUtils.getImageResource("comp_label.png");
        tbpData.addTab("Font Styles", ic3, mTab3);
        
        mTab4 = new JPanelDataExplorer(JPanelDataExplorer.XML_LAYOUTS);
        ImageIcon ic4 = ResourceUtils.getImageResource("comp_panel.png");
        tbpData.addTab("Others", ic4, mTab4);
    }
    
    private void loadList()
    {
        List<SFile> files = new ArrayList<SFile>();
        
        List<String> mpqStrings = App.getApp().getPreferences().getArrayString("options.path.mpq.list", null);
        if (mpqStrings == null)
            return;
        
        List<MpqFile> mpqFiles = new ArrayList<MpqFile>();
        for (int i=0; i < mpqStrings.size(); i++)
        {
            try {
                MpqFile mpq = new MpqFile(mpqStrings.get(i));
                Iterator<MpqEntry> it = mpq.iterator();
                while (it.hasNext())
                {
                    String fileName = it.next().getName();
                    if (fileName != null)
                        files.add(new SFile(mpqStrings.get(i),fileName));
                }
                
            } catch (Exception e) {}
        }
        
        // Now send lists to good ones.
        List<SResource> listForTab0 = new ArrayList<SResource>();
        List<SResource> listForTab1 = new ArrayList<SResource>();
        List<SResource> listForTab2 = new ArrayList<SResource>();
        List<SResource> listForTab3 = new ArrayList<SResource>();
        List<SResource> listForTab4 = new ArrayList<SResource>();
        
        for (SFile file : files)
        {
            if (file.getFilePath().endsWith(".otf") || file.getFilePath().endsWith(".ttf"))
                listForTab0.add(file);
            
            if (file.getFilePath().endsWith(".dds") || file.getFilePath().endsWith(".tga"))
                listForTab1.add(file);
            
            if (file.getFilePath().endsWith(".xml"))
                listForTab2.add(file);
            
            if (file.getFilePath().endsWith(".SC2Style"))
                listForTab3.add(file);
            
            if (!listForTab0.contains(file) && !listForTab1.contains(file) && !listForTab2.contains(file) && !listForTab3.contains(file))
                listForTab4.add(file);
        }
        // Populate tabs
        mTab0.sendResources(listForTab0);
        mTab1.sendResources(listForTab1);
        mTab2.sendResources(listForTab2);
        mTab3.sendResources(listForTab3);
        mTab4.sendResources(listForTab4);
        
        // The third tab is a font style type, we must get the styles.
        List<SResource> fontStyles = new ArrayList<SResource>();
        fontStyles.addAll(loadFontStylesFromResourceList(listForTab3));
        //for (SResource res : fontStyles)
        //    System.out.println(res.toString());
        
        mTab3.sendResources(fontStyles);
    }
    
    private List<SFontStyle> loadFontStylesFromResourceList(List<SResource> files)
    {
        FontStyleXMLAnalyzer analyzer = new FontStyleXMLAnalyzer();
        
        for (SResource res : files)
        {
            try
            {
                SFile f = (SFile)res;
                MpqFile mpqFile = new MpqFile(f.getMpqFilePath());
                MpqEntry mpqEntry = mpqFile.getEntry(f.getFilePath());
                InputStream is = mpqFile.getInputStream(mpqEntry);
                analyzer.analyze(is);
            } catch (Exception e) {}
        }
        
        return analyzer.getStyles();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tbpData = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        chkShowPreview = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Explorer");

        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(70, 23));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        chkShowPreview.setSelected(true);
        chkShowPreview.setText("Show Preview");
        chkShowPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowPreviewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(chkShowPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 284, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(chkShowPreview))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbpData)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbpData, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chkShowPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowPreviewActionPerformed
        
        mTab0.setPreviewVisibility(chkShowPreview.isSelected());
        mTab1.setPreviewVisibility(chkShowPreview.isSelected());
        mTab2.setPreviewVisibility(chkShowPreview.isSelected());
        mTab3.setPreviewVisibility(chkShowPreview.isSelected());
        mTab4.setPreviewVisibility(chkShowPreview.isSelected());
        mTab0.revalidate();
        mTab1.revalidate();
        mTab2.revalidate();
        mTab3.revalidate();
        mTab4.revalidate();
        
    }//GEN-LAST:event_chkShowPreviewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JCheckBox chkShowPreview;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JTabbedPane tbpData;
    // End of variables declaration//GEN-END:variables
}
