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

import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import delete.com.tivconsultancy.opentiv.devgui.main.CallingFeatureWindow;
import delete.com.tivconsultancy.opentiv.devgui.main.StatusBar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Frame_Feature extends JInternalFrame {

    DefaultListModel listModel = new DefaultListModel();
    JList jList1 = new javax.swing.JList(listModel);
    JTextArea jTextAreaExplanation = new JTextArea("");

    JPanel MainTab = new JPanel();
    JScrollPane jListScroller = new JScrollPane(jList1);
    JScrollPane jTextScroller = new JScrollPane(jTextAreaExplanation);

    JButton AddButton = new JButton("Add");

    CallingFeatureWindow oCaller;
    
    Frame_Main MainFrame;

    public Frame_Feature(Frame_Main MainFrame) {        
        super("Features", true);
        this.MainFrame = MainFrame;
        setClosable(true);
        setMaximizable(true);
        setEnabled(true);
        setResizable(true);
        this.setLayer(1);

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        //Dimensions
        MainTab.setPreferredSize(new Dimension(300, 300));
        jListScroller.setPreferredSize(new Dimension(280, 100));
        jTextScroller.setPreferredSize(new Dimension(280, 100));

        jTextAreaExplanation.setText("");
        jTextAreaExplanation.setLineWrap(true);
        jTextAreaExplanation.setEditable(false);
        jTextAreaExplanation.setOpaque(true);
        jTextAreaExplanation.setFont(UIManager.getFont("Label.font"));
        jTextAreaExplanation.setBackground(new Color(UIManager.getColor("Label.background").getRed(), UIManager.getColor("Label.background").getGreen(), UIManager.getColor("Label.background").getBlue()));
        jTextAreaExplanation.setWrapStyleWord(true);

        //Add the listeners
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        AddButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addFeature(evt);
            }
        });

//        setHist();

        JLabel jText = new JLabel("Test2");
        jText.setPreferredSize(new Dimension(20, 10));

        StatusBar jText2 = new StatusBar("test");
        jText2.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY),
                                            new EmptyBorder(4, 4, 10, 10)));

        //Positions
        MainTab.add(jListScroller);
        MainTab.add(jTextScroller);
        MainTab.add(AddButton);

        add(MainTab);
        pack();
        setLayout();

        jText2.updateStatus(0.999);

    }

    public void callWindow(CallingFeatureWindow o) {
        this.oCaller = o;
        listModel.clear();
        int iCount = 0;
        List<Feature> lo = MainFrame.getFeatureModel().getFeatures(o.getCategory());
        for (Feature oFeature : lo) {
            listModel.add(iCount, oFeature);
            iCount++;
        }
//        if (this.oCaller.getCategory().equals(CONSTANTS.PHIST)) {
//            setHist();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PReadIn)) {
//            setReadIn();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PSMOTH)) {
//            setSmoothing();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PNR)) {
//            setNR();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PTRANSF)) {
//            setTransform();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.Masking)) {
//            setSimpleMasking();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PPIVGrid)) {
//            setPIVGrid();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PPIVCorr)) {
//            setPIVCorr();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PIVDisplayAndVali)) {
//            setPIVDispAndVali();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.PIVProcessing)) {
//            setPIVProcess();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.EdgeDetector)) {
//            setEdgeDetectors();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.EdgeOperations)) {
//            setEdgeOperations();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.ShapeFitting)) {
//            setShapeFitting();
//        } else if (this.oCaller.getCategory().equals(CONSTANTS.ShapeFilter)) {
//            setShapeFilter();
//        } else {
//            listModel.clear();
//        }
        this.setVisible(true);
    }
    
//    private void setFeatures() {
//        
//    }
//
//    private void setTransform() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loTransform) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    public void setPIVGrid() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loPIVGrid) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    public void setPIVProcess() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loPIVProcess) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    public void setPIVCorr() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loPIVCorr) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    public void setPIVDispAndVali() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loPIVDispAndVali) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    private void setSimpleMasking() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loSimpleMasking) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    private void setHist() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loHistFeatures) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    private void setReadIn() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loReadInFeatures) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    private void setSmoothing() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loSmoothing) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    private void setNR() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loNR) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    public void setEdgeDetectors() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loEdgeDetectors) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    public void setEdgeOperations() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loEdgeOperations) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//
//    public void setShapeFitting() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loShapeFitting) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }
//    
//    public void setShapeFilter() {
//        listModel.clear();
//        int iCount = 0;
//        for (Feature o : GUI_Data.oFeatures.loShapeFilter) {
//            listModel.add(iCount, o);
//            iCount++;
//        }
//    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
        Feature oSelceted = (Feature) jList1.getSelectedValue();
        if (oSelceted != null) {
            jTextAreaExplanation.setText(oSelceted.getDescription());
        }

    }

    private void addFeature(java.awt.event.MouseEvent evt) {
        if (!jList1.isSelectionEmpty()) {
            Feature oFeature = (Feature) jList1.getSelectedValue();
            oCaller.addFeature(((Feature) jList1.getSelectedValue()).clone());
            if (oFeature.getSettings1() == null) {
                oFeature.setSettings1(null);
            }
            if (oFeature.getSettings2() == null) {
                oFeature.setSettings2(null);
            }
        }
//        Data.AllSettingsContainer.oSetPIV.setSettingsToPIV();
        this.hide();
    }

    private void setLayout() {

        MainTab.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 10;
        c.ipady = 10;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        ((GridBagLayout) MainTab.getLayout()).setConstraints(jListScroller, c);

        c = new GridBagConstraints();
        c.ipadx = 10;
        c.ipady = 10;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        ((GridBagLayout) MainTab.getLayout()).setConstraints(jTextScroller, c);

        c = new GridBagConstraints();
        c.ipadx = 10;
        c.ipady = 10;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0;
        c.weighty = 0;

        ((GridBagLayout) MainTab.getLayout()).setConstraints(AddButton, c);

        pack();
    }

}
