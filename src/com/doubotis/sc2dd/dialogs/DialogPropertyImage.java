/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.sc2dd.dialogs;

import com.doubotis.sc2dd.FrameDialogTextureViewer;
import static com.doubotis.sc2dd.JPanelDataExplorer.IMAGES;
import static com.doubotis.sc2dd.JPanelDataExplorer.TEXTS;
import static com.doubotis.sc2dd.JPanelDataExplorer.XML_LAYOUTS;
import com.doubotis.sc2dd.data.SDialog;
import com.doubotis.sc2dd.data.SFile;
import com.doubotis.sc2dd.data.SImage;
import com.doubotis.sc2dd.data.SImageType;
import com.doubotis.sc2dd.data.SSize;
import com.doubotis.sc2dd.data.SText;
import com.doubotis.sc2dd.app.App;
import com.doubotis.sc2dd.util.ResourceUtils;
import com.doubotis.sc2dd.util.UIUtils;
import com.mundi4.mpq.MpqEntry;
import com.mundi4.mpq.MpqFile;
import ddsutil.DDSUtil;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import jogl.DDSImage;

/**
 *
 * @author Christophe
 */
public class DialogPropertyImage extends javax.swing.JDialog implements IDialogReturn {
    
    private SImage mImage = SImage.NONE;

    /**
     * Creates new form DialogPropertyText
     */
    public DialogPropertyImage(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        listImages();
    }
    
    @Override
    public SImage showDialog()
    {
        setVisible(true);
        return mImage;
    }
    
    @Override
    public void setEditingInfo(Object o)
    {
        mImage = (SImage)o;
    }
    
    private void listImages()
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
        List<SFile> imageList = new ArrayList<SFile>();
        
        DefaultListModel<SFile> adapter = new DefaultListModel<SFile>();
        lstImages.setModel(adapter);
        
        for (SFile file : files)
        {
            if (file.getFilePath().endsWith(".dds") || file.getFilePath().endsWith(".tga"))
                adapter.addElement(file);
        }
    }
    
    private BufferedImage imageFromImage(InputStream is) throws IOException
    {
        Vector<Byte> byteV = new Vector<Byte>();
        byte[] tmp1 = new byte[1024];
        while (true) {
            int r = is.read(tmp1, 0, 1024);
            if (r == -1) break;
            for (int i=0; i<r; i++) {
                byteV.add(tmp1[i]);
            }
        }
        byte[] tmp2 = new byte[byteV.size()];
        for (int i=0; i<byteV.size(); i++) {
            tmp2[i] = byteV.elementAt(i);
        }
        ByteBuffer buf = ByteBuffer.wrap(tmp2);

        return DDSUtil.loadBufferedImage(DDSImage.read(buf));
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstImages = new javax.swing.JList();
        txtSearch = new javax.swing.JTextField();
        pnlPreview = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOpenDTV = new javax.swing.JButton();
        chkShowPreview = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Property"));

        lstImages.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstImages.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstImagesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstImages);

        txtSearch.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel1.setText("Search :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(pnlPreview))
                .addContainerGap())
        );

        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(80, 23));
        btnOK.setMinimumSize(new java.awt.Dimension(80, 23));
        btnOK.setPreferredSize(new java.awt.Dimension(70, 23));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(70, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOpenDTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/doubotis/sc2dd/res/tools_imageviewer.png"))); // NOI18N
        btnOpenDTV.setText("Dialog Texture Viewer...");
        btnOpenDTV.setEnabled(false);
        btnOpenDTV.setMaximumSize(new java.awt.Dimension(80, 23));
        btnOpenDTV.setMinimumSize(new java.awt.Dimension(80, 23));
        btnOpenDTV.setPreferredSize(new java.awt.Dimension(70, 23));
        btnOpenDTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenDTVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(btnOpenDTV, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnOpenDTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        chkShowPreview.setSelected(true);
        chkShowPreview.setText("Show Preview");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chkShowPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkShowPreview))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        setVisible(false);
        
        try
        {
            SFile s = (SFile)lstImages.getModel().getElementAt(lstImages.getSelectedIndex());

            SImage image = new SImage(s.getFilePath(), s.getMpqFilePath());

            InputStream is = s.open();
            BufferedImage bi = imageFromImage(is);
            is.close();
            image.imageSize = new SSize(bi.getWidth(), bi.getHeight());
            
            mImage = image;
            
            dispose();
            
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Sorry, an error has occured to open this image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        setVisible(false);
        
        mImage = null;
        
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void lstImagesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstImagesValueChanged
        
        SFile s = (SFile)lstImages.getModel().getElementAt(lstImages.getSelectedIndex());
        
        if (s != null)
            btnOpenDTV.setEnabled(true);
        
        UIUtils.ResourceType resType = UIUtils.ResourceType.IMAGE;
        
        Component c = UIUtils.createComponentForResource(s, resType);
        
        pnlPreview.getViewport().setView(c);
    }//GEN-LAST:event_lstImagesValueChanged

    private void btnOpenDTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenDTVActionPerformed
        
        SFile s = (SFile)lstImages.getModel().getElementAt(lstImages.getSelectedIndex());
        
        FrameDialogTextureViewer dtv = new FrameDialogTextureViewer();
        dtv.setIconImage(ResourceUtils.getImageResource("dtv-icon.png").getImage());
        dtv.setLocationRelativeTo(null);
        dtv.setFile(s);
        dtv.show();
        
    }//GEN-LAST:event_btnOpenDTVActionPerformed

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
            java.util.logging.Logger.getLogger(DialogPropertyImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogPropertyImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogPropertyImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogPropertyImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogPropertyImage dialog = new DialogPropertyImage(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnOpenDTV;
    private javax.swing.JCheckBox chkShowPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lstImages;
    private javax.swing.JScrollPane pnlPreview;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
