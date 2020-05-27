/* 
 * Copyright 2020 TIVConsultancy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package delete.com.tivconsultancy.opentiv.devgui.frames;

import delete.com.tivconsultancy.opentiv.devgui.main.ImagePath;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardEnum;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingWorker;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Images;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StandardFrame_ImageView extends JInternalFrame {

    public JLabel Image = new JLabel();
    public JScrollPane ImageScrollPane = new JScrollPane();
    public JPanel MainPanel;
    public JPanel ImagePanel = new JPanel();

    public DefaultListModel jListModelLayers = new DefaultListModel();
    public JList jListLayers = new javax.swing.JList(jListModelLayers);

    public JScrollPane jListScrollerLayers = new JScrollPane(jListLayers);

    public DefaultListModel jListModelImages = new DefaultListModel();
    public JList jListImages = new javax.swing.JList(jListModelImages);
    public JScrollPane jListScrollerImages = new JScrollPane(jListImages);
    public JButton jButtonSTop = new JButton("Stop");

    JLabel MousePos = new JLabel("[]");

    JLabel a = new JLabel("");

    private int mouseX = 0;
    private int mouseY = 0;

    public Frame_Main MainFrame;

    public StandardFrame_ImageView(Frame_Main MainFrame) {
        super("Image View");
        this.MainFrame = MainFrame;
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        initComponents();

        setIconifiable(true);
        setMaximizable(true);
        setEnabled(true);
        setResizable(true);

        //Listeners
        jListImages.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    changedImageListSelection();
                }
            }
        });

        jListLayers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    changedLayerListSelection();
                }
            }
        });

        jButtonSTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                StopButtonClicked(evt);
            }
        });

        Image.addMouseMotionListener(new MyMouseAdapter());

        jListLayers.setSelectedIndex(0);

        ImageScrollPane.setPreferredSize(new Dimension(200, 200));
        jListScrollerLayers.setPreferredSize(new Dimension(200, 200));
        jListScrollerLayers.setMinimumSize(new Dimension(200, 200));

        jListScrollerImages.setPreferredSize(new Dimension(200, 200));
        jListScrollerImages.setMinimumSize(new Dimension(200, 200));
        jButtonSTop.setPreferredSize(new Dimension(100, 30));

        Image.setIcon(new ImageIcon(getClass().getResource("/Logo/Logo.png")));
        ImageScrollPane = new JScrollPane(Image);
        getContentPane().add(ImageScrollPane);
        getContentPane().add(MousePos);
        getContentPane().add(jListScrollerLayers);
        getContentPane().add(jListScrollerImages);
        getContentPane().add(jButtonSTop);
        setSize(300, 450);
        setVisible(true);
        this.setLayer(0);

        setLayout();
    }

    private void initComponents() {
        List<? extends Object> lo = MainFrame.getImageLayers();
        for (Object o : lo) {
            ((DefaultListModel) jListLayers.getModel()).addElement(o);
        }
    }

    public void setDefault() {
        MousePos.setText("[]");
        int iWidth = Image.getWidth();
        int iHeight = Image.getHeight();
        Image.setText(" Image ");
        Image.setSize(new Dimension(iWidth, iHeight));
    }

    public void StopButtonClicked(java.awt.event.MouseEvent evt) {
        SwingWorker process = MainFrame.getBackgroundTask();
        if (process != null) {
            process.cancel(true);
        }
        process = null;
        updateLayerList();
    }

    public void changedImageListSelection() {
        Image.setText("");
        if (jListModelImages.isEmpty() || jListImages.isSelectionEmpty()) {
            setDefault();
        } else {
            showSelectedImage();
            MainFrame.getDataModel().clearImages();
            ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
            MainFrame.recalc(StandardEnum.ReadIn);
        }
        jListLayers.setSelectedIndex(0);
    }

    public void changedLayerListSelection() {
        if (!jListModelImages.isEmpty() && !jListImages.isSelectionEmpty()) {
            Enum s = (Enum) jListLayers.getSelectedValue();
            if (s == null) {
                return;
            }

            updateImage(s);

//            if (s.equals(StandardEnum.ReadIn)) {
//                showSelectedImage();
//            }
//            if (s.equals(StandardEnum.Transform)) {
//                if (Data.GUI_Data.TransEnd != null) {
//                    updateImage(Data.GUI_Data.TransEnd);
//                }
////                ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
////                Data.PIVGUI_Data.PIV_Processes.run(oPath.o, Data.CONSTANTS.PTRANSF);
//            }
//            if (s.equals(Data.CONSTANTS.PREPROC)) {
//                if (Data.GUI_Data.PreProcEnd != null) {
//                    updateImage(Data.GUI_Data.PreProcEnd);
//                }
////                ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
////                Data.PIVGUI_Data.PIV_Processes.run(oPath.o, Data.CONSTANTS.PREPROC);
//            }
//
//            if (s.equals(Data.CONSTANTS.Masking)) {
//                if (Data.GUI_Data.MaskingEnd != null) {
//                    updateImage(Data.GUI_Data.MaskingEnd);
//                }
//            }
//            if (s.equals(Data.CONSTANTS.PPIVPreProc) || s.equals(Data.CONSTANTS.PPIVGrid)) {
//                if (Data.GUI_Data.PIVGrid != null) {
//                    updateImage(Data.GUI_Data.PIVGrid);
//                }
//            }
//
//            if (s.equals(Data.CONSTANTS.PPIVProcessor) || s.equals(Data.CONSTANTS.PPIVCorr)) {
//                if (Data.GUI_Data.PIVVectors != null) {
//                    updateImage(Data.GUI_Data.PIVVectors);
//                }
//            }
//
//            if (s.equals(Data.CONSTANTS.EDGES)) {
//                if (Data.GUI_Data.CD_Edges != null) {
//                    updateImage(Data.GUI_Data.CD_Edges);
//                }
//            }
//
//            if (s.equals(Data.CONSTANTS.FITTING)) {
//                if (Data.GUI_Data.CD_ShapeFit != null) {
//                    updateImage(Data.GUI_Data.CD_ShapeFit);
//                }
//            }
//                ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
//                Data.PIVGUI_Data.PIV_Processes.run(oPath.o, Data.CONSTANTS.PMasking);
        }
    }

    public void changeLayerListSelection(Enum Ident) {
        jListModelLayers.indexOf(Ident);
        jListLayers.setSelectedIndex(jListModelLayers.indexOf(Ident));
    }

    public void updateFromRecalc(Enum Ident) {
        int indexUpdatedImage = jListModelLayers.indexOf(Ident);
        int indexCurrentView = jListLayers.getSelectedIndex();
        if (indexCurrentView == indexUpdatedImage) {
            updateImage(Ident);
        }
    }

    public void updateCurrentView() {
        Enum Selcted = ((Enum) jListModelLayers.get(jListLayers.getSelectedIndex()));
        updateImage(Selcted);

    }

    public void showSelectedImage() {
        ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
        ImageIcon newImage = new ImageIcon(oPath.o.getAbsolutePath());
        Image.setIcon(newImage);
    }

//    public void updateOriginal(boolean bRecalc) {
////        if (Data.PIVGUI_Data.tPreProcIMGLoader != null) {
////            Data.PIVGUI_Data.tPreProcIMGLoader.interrupt();
////        }
////
////        if (Data.PIVGUI_Data.tTransform != null) {
////            Data.PIVGUI_Data.tTransform.interrupt();
////        }
////        
////        if (Data.PIVGUI_Data.tMasking != null) {
////            Data.PIVGUI_Data.tMasking.interrupt();
////        }
//
//        ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
//        ImageIcon newImage = new ImageIcon(oPath.o.getAbsolutePath());
//        Image.setIcon(newImage);
//        Image.setText("");
//        if (bRecalc) {
//            Data.PIVGUI_Data.PIV_Processes.run(oPath.o, CONSTANTS.PTRANSF);
////            TransFormIMG oTransCalc = new TransFormIMG(oPath.o);
////            Data.PIVGUI_Data.tTransform = new Thread(oTransCalc);
////            Data.PIVGUI_Data.tTransform.start();
//        }
//
//        PaintHistograms oHist = new PaintHistograms(Data.PIVGUI_Data.Main.oPIVFrame.PreProc.oHist, getPresentImage(), null);
//        Data.PIVGUI_Data.tPaintHist = new Thread(oHist);
//        Data.PIVGUI_Data.tPaintHist.start();
//
//    }
    public ImageGrid getPresentImage() {
        ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
        try {
            return IMG_Reader.readImageGrey(oPath.o);
        } catch (IOException ex) {
            Logger.getLogger(StandardFrame_ImageView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public void updateTransformation() {
////        if (Data.PIVGUI_Data.tTransform != null) {
////            Data.PIVGUI_Data.tTransform.interrupt();
////        }
//
//        ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
//        Data.PIVGUI_Data.PIV_Processes.run(oPath.o, CONSTANTS.PTRANSF);
//        if (Data.PIVGUI_Data.TransEnd != null) {
//            Image.setIcon(new ImageIcon(Data.PIVGUI_Data.TransEnd.getBuffImage()));
//
////            PaintHistograms oHist = new PaintHistograms(Data.PIVGUI_Data.Main.oPIVFrame.PreProc.oHist, Data.PIVGUI_Data.TransEnd, null);
////        Data.PIVGUI_Data.tPaintHist = new Thread(oHist);
////        Data.PIVGUI_Data.tPaintHist.start();
//        }
//
//    }
//    public void updatePreProc() {
//
////        Data.PIVGUI_Data.PreProcEnd = Data.PIVGUI_Data.PreProcStart.clone();
////        Data.PIVGUI_Data.Main.oPIVFrame.updateImage("PreProc");
//        ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
//        Data.PIVGUI_Data.PIV_Processes.run(oPath.o, CONSTANTS.PREPROC);
//        if (Data.PIVGUI_Data.PreProcEnd != null) {
//            Image.setIcon(new ImageIcon(Data.PIVGUI_Data.PreProcEnd.getBuffImage()));
//
////            PaintHistograms oHist = new PaintHistograms(Data.PIVGUI_Data.Main.oPIVFrame.PreProc.oHist, Data.PIVGUI_Data.PreProcEnd, null);
////        Data.PIVGUI_Data.tPaintHist = new Thread(oHist);
////        Data.PIVGUI_Data.tPaintHist.start();
//        }
//
////        ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
////        ImageGrid oPP;
////        System.out.println("PING");
////        try {           
////            oPP = IMG_Reader.readImageGrey(oPath.o);
////            IMG_Writer.PaintGreyPNG(oPP, new File("E:\\Goattec\\PIVGUITest\\orig.png"));
//////            Image.setIcon(new ImageIcon("E:\\Work\\BubbleBoundaryVelo\\Tergitol\\0p5slpmin\\00000.jpg"));
////            oPP = opentiv_preproc.OpenTIV_PreProc.performPreProc(AllSettingsContainer.oSetPre, oPP);
////            IMG_Writer.PaintGreyPNG(oPP, new File("E:\\Goattec\\PIVGUITest\\contrast.png"));
////            Image.setIcon(new ImageIcon(oPP.getBuffImage()));
////            Image.setText("");
////        } catch (IOException ex) {
////            Logger.getLogger(ImageView.class.getName()).log(Level.SEVERE, null, ex);
////            Image.setText("Error performin pre processor, please contact support \n" + ex.getMessage());
////        }        
//    }
//    public void updateMasking() {
//        ImagePath oPath = (ImagePath) jListImages.getSelectedValue();
//        Data.PIVGUI_Data.PIV_Processes.run(oPath.o, CONSTANTS.PMasking);
//        if (Data.PIVGUI_Data.MaskingEnd != null) {
//            Image.setIcon(new ImageIcon(Data.PIVGUI_Data.MaskingEnd.getBuffImage()));
//        }
//    }
//    public void updateImage(ImageGrid oImage) {
//        if (oImage != null) {
//            Image.setIcon(new ImageIcon(oImage.getBuffImage()));
//            if (Data.PIVGUI_Data.Main.oPIVFrame.PreProc.isShowing()) {
//                Processes.updateHist(Data.PIVGUI_Data.Main.oPIVFrame.PreProc.oHist, oImage, null);
//            }
//        }
//    }
    public void updateImage(ImageInt oImage) {
        if (oImage != null) {
            Image.setIcon(new ImageIcon(oImage.getBuffImage()));
        }
    }

    public void updateImage(Enum sIdent) {
        Model_Images DataModel = MainFrame.getDataModel();
        ImageInt ImgToUpdate = DataModel.getImage(sIdent);
        updateImage(ImgToUpdate);

    }

    public void updateImage(String sText) {
        if (sText != null) {
            Image.setText(sText);
        }
    }

    private void setLayout() {

//        this.setLayout(new BorderLayout(20, 20));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 3; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(ImageScrollPane, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
//        c.anchor = GridBagConstraints.NORTHWEST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jListScrollerLayers, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
//        c.anchor = GridBagConstraints.NORTHWEST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jListScrollerLayers, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 2; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 2;
//        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHEAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jButtonSTop, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 3;
//        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        c.anchor = GridBagConstraints.EAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(MousePos, c);

//        c = new GridBagConstraints();
//        c.ipadx = 0;
//        c.ipady = 0;
//        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
//        c.gridheight = 1; //GridBagConstraints.REMAINDER;
//        c.gridx = 0;
//        c.gridy = GridBagConstraints.RELATIVE;
//        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 0;
//        c.weighty = 1.0;
//        c.anchor = GridBagConstraints.NORTH;
//        
//        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(a, c);
    }

    private class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            // get Point location and turn into a String
            String location = String.format("[%d, %d]", mouseY, mouseX);

            // set the label's text with this String
            MousePos.setText(location);

            repaint();
        }
    }

    public void addImagePaths(List<String> ls) {
        List<String> lsContent = new ArrayList<>();
        for (int i = 0; i < jListModelImages.getSize(); i++) {
            lsContent.add(((ImagePath) jListModelImages.getElementAt(i)).sInput);
        }

        lsContent.addAll(ls);
        Set<String> uniqueImages = new LinkedHashSet<String>(lsContent);
        jListModelImages.clear();
        for (String s : uniqueImages) {
            jListModelImages.addElement(new ImagePath(s));
        }

        try {
            this.setIcon(false);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(StandardFrame_ImageView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void emptyImagePaths() {
        jListModelImages.clear();
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

//                if (Data.PIVGUI_Data.tPreProcIMGLoader != null && Data.PIVGUI_Data.tPreProcIMGLoader.isAlive()) {
//                    listCellRendererComponent.setBackground(Color.red);
//                }
//                StatusBarListCell o = new StatusBarListCell(listCellRendererComponent);
//                listCellRendererComponent = new JLabel("sdsdf");
//                Color bg = null;
//                Color fg = null;
//
//                JList.DropLocation dropLocation = list.getDropLocation();
//                if (dropLocation != null
//                        && !dropLocation.isInsert()
//                        && dropLocation.getIndex() == index) {
//
//                    bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
//                    fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");
//
//                }
//                
////                bg = Color.BLACK;
//
//                if (isSelected) {
//                    listCellRendererComponent.setBackground(bg == null ? list.getSelectionBackground() : bg);
//                    listCellRendererComponent.setForeground(fg == null ? list.getSelectionForeground() : fg);
//                } else {
//                    listCellRendererComponent.setBackground(list.getBackground());
//                    listCellRendererComponent.setForeground(list.getForeground());
//                }
////                listCellRendererComponent.setBackground(Color.BLACK);
//                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
                return listCellRendererComponent;
            }
        };
    }

    public void updateLayerList() {
        jListModelLayers.clear();
        jListModelLayers.addElement(StandardEnum.ReadIn);
        jListModelLayers.addElement(StandardEnum.Transform);
        jListModelLayers.addElement(StandardEnum.PreProcessor);
        jListModelLayers.addElement(StandardEnum.Mask);
//            jListModelLayers.addElement(Data.CONSTANTS.PPIVPreProc);
//            jListModelLayers.addElement(Data.CONSTANTS.PPIVProcessor);        
    }

    public void cleanUp() {
        jListModelImages.clear();
    }

}
