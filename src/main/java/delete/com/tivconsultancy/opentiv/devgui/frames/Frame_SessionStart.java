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

import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardEnum;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_FolderStructure.StandardFolders;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Settings.StandardSettingNames;
import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Frame_SessionStart extends JInternalFrame {

    public JTextField jTextFieldFolder = new JTextField("");
    public JButton jButtonLoadFolder = new JButton(UIManager.getIcon("FileChooser.newFolderIcon"));

    public JButton JButtonLoadSession = new JButton("Load Session");
    public JButton jButtonStartNewSession = new JButton("Start New");

    public JTextArea jTextAreaExplainArea = new JTextArea();

    final JFileChooser fc = new JFileChooser();

    public JLabel jLabelWD = new JLabel("    Working Directory");
    public JLabel jLabelPlaceHolder = new JLabel("");
    
    public JCheckBox jCheckBoxDemandMode = new JCheckBox("On Demand Mode - Slowed down processes but memory effiient");
    
    public Frame_Main MainFrame;

    public Frame_SessionStart(Frame_Main MainFrame) {
        super("openTIV Dev GUI");
        this.MainFrame = MainFrame;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        setIconifiable(false);
        setResizable(false);
        setClosable(true);

        jTextFieldFolder.setPreferredSize(new Dimension(350, 30));
        jButtonLoadFolder.setPreferredSize(new Dimension(30, 30));

        jButtonStartNewSession.setPreferredSize(new Dimension(120, 30));
        JButtonLoadSession.setPreferredSize(new Dimension(120, 30));
        JButtonLoadSession.setToolTipText("Will be availavble when a working directory with PIV settings is given, click on text field to update");
//        JButtonLoadSession.setEnabled(false);

        jTextAreaExplainArea.setPreferredSize(new Dimension(300, 200));
        jTextAreaExplainArea.setLineWrap(true);
        jTextAreaExplainArea.setEditable(false);
        jTextAreaExplainArea.setOpaque(true);
        jTextAreaExplainArea.setFont(UIManager.getFont("Label.font"));
        jTextAreaExplainArea.setBackground(new Color(UIManager.getColor("Label.background").getRed(), UIManager.getColor("Label.background").getGreen(), UIManager.getColor("Label.background").getBlue()));
        jTextAreaExplainArea.setWrapStyleWord(true);
        jTextAreaExplainArea.setBorder(null);

        this.add(jTextFieldFolder);
        this.add(jButtonLoadFolder);
        this.add(jButtonStartNewSession);
        this.add(JButtonLoadSession);
        this.add(jTextAreaExplainArea);
        this.add(jLabelWD);
        this.add(jLabelPlaceHolder);
        jCheckBoxDemandMode.setEnabled(false);
        jCheckBoxDemandMode.setSelected(true);
        this.add(jCheckBoxDemandMode);

        //Listeners
        jButtonLoadFolder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CallFolderPic(evt);
                checkForSettingsFile();
            }
        });
        
        JButtonLoadSession.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loadSettings();                
            }
        });
        
        jButtonStartNewSession.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                startNewSession();                
            }
        });
        
        jTextFieldFolder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                checkForSettingsFile();
            }
        });

        setLayout();
        setText();
        setSize(430, 350);
//        setVisible(true);

    }

    public void CallFolderPic(java.awt.event.MouseEvent evt) {
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fChosenFolder = fc.getSelectedFile();
            jTextFieldFolder.setText(fChosenFolder.getAbsolutePath());
        }
    }

    public void checkForSettingsFile() {
        String sWD = jTextFieldFolder.getText().trim();
        File oFileSettings = new File(sWD + java.io.File.separator + MainFrame.getFoldersModel().getFolder(StandardFolders.Settings) + java.io.File.separator + "Features.gtt");
        if (oFileSettings.exists()) {
            JButtonLoadSession.setEnabled(true);
        } else {
//            JButtonLoadSession.setEnabled(false);
        }
    }

    public void startNewSession(){        
        String sWD = jTextFieldFolder.getText().trim();               

        boolean bSuccess = createFolderStructure(sWD);
        
        
        if(!bSuccess){
            jTextFieldFolder.setText("Error : Cannot create folder structure");
            setText();
            return;
        }
        
        MainFrame.getFoldersModel().setOutFolder(sWD);
        setVisible(false);
        MainFrame.getControlFrame().setEnabled(true);
        MainFrame.getControlFrame().setVisible(true);
        MainFrame.getImageViewFrame().updateLayerList();                
    }
    
    public boolean createFolderStructure(String sWD){
        
        boolean bSuccess = false;
        
        File oFileSettings = new File(sWD + java.io.File.separator + MainFrame.getFoldersModel().getFolder(StandardFolders.Settings));
        File oFileData = new File(sWD + java.io.File.separator + MainFrame.getFoldersModel().getFolder(StandardFolders.Data));
        File oFileDebug = new File(sWD + java.io.File.separator + MainFrame.getFoldersModel().getFolder(StandardFolders.Debug));
        File oFilePictures = new File(sWD + java.io.File.separator + MainFrame.getFoldersModel().getFolder(StandardFolders.Pictures));
        
        try {
            bSuccess = true;
            if(!oFileData.exists()){
                bSuccess = bSuccess && oFileData.mkdirs();
            }
            if(!oFileDebug.exists()){
                bSuccess = bSuccess && oFileDebug.mkdirs();
            }
            if(!oFilePictures.exists()){
                bSuccess = bSuccess && oFilePictures.mkdirs();
            }
            if(!oFileSettings.exists()){
                bSuccess = bSuccess && oFileSettings.mkdirs();
            }            
            MainFrame.getSaveModel().saveState(MainFrame.getSettingsModel(), MainFrame.getFeatureModel(), MainFrame.getFoldersModel());
        } catch (Exception e) {
            jTextFieldFolder.setText("Error : Cannot create folder structure");
            setText();
            return bSuccess;
        }
        
        return bSuccess;
    }
    
    public void loadSettings() {        
        String sWD = jTextFieldFolder.getText().trim();
        File oFileFeatures = new File(sWD + java.io.File.separator + MainFrame.getFoldersModel().getFolder(StandardFolders.Settings) + java.io.File.separator + "Features.gtt");
        File oFileSettings = new File(sWD + java.io.File.separator + MainFrame.getFoldersModel().getFolder(StandardFolders.Settings) + java.io.File.separator + "Settings.gtt");
        
        if (!oFileFeatures.exists() || !oFileSettings.exists()) {
//            jTextFieldFolder.setText("Error : No Settings found");
            return;
        }   
        
        createFolderStructure(sWD);
        

        List<Feature> loFeatures = new ArrayList<>();
        List<SettingObject> loSettings = new ArrayList<>();

                     

        FileInputStream fisFeatures = null;
        ObjectInputStream oisFeatures = null;
        
        try {
            fisFeatures = new FileInputStream(oFileSettings.getAbsolutePath());
            oisFeatures = new ObjectInputStream(fisFeatures);
            loSettings = (ArrayList) oisFeatures.readObject();
            oisFeatures.close();
            fisFeatures.close();
        } catch (IOException ioe) {
            try {
                oisFeatures.close();
                fisFeatures.close();
            } catch (Exception ex) {
                Logger.getLogger(Frame_SessionStart.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTextFieldFolder.setText("Error : No Settings found or byte stream corrupt");
            setText();
            return;
        } catch (ClassNotFoundException c) {
            try {
                oisFeatures.close();
                fisFeatures.close();
            } catch (Exception ex) {
                Logger.getLogger(Frame_SessionStart.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTextFieldFolder.setText("Error reading in Settings");
            setText();
            return;
        }
        
        HashSet loHash = new HashSet(loSettings);
        loSettings = new ArrayList<>(loHash);
        
        MainFrame.getSettingsModel().getSettings(StandardSettingNames.ReadIn).setReadIn(loSettings);
        MainFrame.getSettingsModel().getSettings(StandardSettingNames.PreProc).setReadIn(loSettings);
        MainFrame.getSettingsModel().getSettings(StandardSettingNames.Mask).setReadIn(loSettings);
//        AllSettingsContainer.oSetPIV.setReadIn(loSettings);
        
        try {
            fisFeatures = new FileInputStream(oFileFeatures.getAbsolutePath());
            oisFeatures = new ObjectInputStream(fisFeatures);
            loFeatures = (ArrayList) oisFeatures.readObject();
            oisFeatures.close();
            fisFeatures.close();
        } catch (IOException ioe) {
            jTextFieldFolder.setText("Error : No Settings found or byte stream corrupt");
            ioe.printStackTrace();
            try {
                oisFeatures.close();
                fisFeatures.close();
            } catch (Exception ex) {
                Logger.getLogger(Frame_SessionStart.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            setText();
            return;
        } catch (ClassNotFoundException c) {
            jTextFieldFolder.setText("Error: Unknown Settings");
            try {
                oisFeatures.close();
                fisFeatures.close();
            } catch (Exception ex) {
                Logger.getLogger(Frame_SessionStart.class.getName()).log(Level.SEVERE, null, ex);
            }
            setText();
            return;
        }
        
        loHash.clear();
        loHash = new HashSet(loFeatures);
        loFeatures = new ArrayList<>(loHash);
        
        for(Feature f : loFeatures){
            if(MainFrame.getFeatureModel().getFeatures(StandardEnum.ReadIn).contains(f)){
                MainFrame.getControlFrame().ReadIn.oReadIn.jLM.addElement(f);
            }
            if(MainFrame.getFeatureModel().getFeatures(StandardEnum.Transform).contains(f)){
                MainFrame.getControlFrame().ReadIn.oTransform.jLM.addElement(f);
            }
            if(MainFrame.getFeatureModel().getFeatures(StandardEnum.Hist).contains(f)){
                MainFrame.getControlFrame().PreProc.oHistPanel.jLM.addElement(f);
            }
            if(MainFrame.getFeatureModel().getFeatures(StandardEnum.Smoothing).contains(f)){
                MainFrame.getControlFrame().PreProc.oSmoothing.jLM.addElement(f);
            }
            if(MainFrame.getFeatureModel().getFeatures(StandardEnum.NR).contains(f)){
                MainFrame.getControlFrame().PreProc.oNR.jLM.addElement(f);
            }
            if(MainFrame.getFeatureModel().getFeatures(StandardEnum.Mask).contains(f)){
                MainFrame.getControlFrame().Mask.oSimpleMasking.jLM.addElement(f);
            }
//            if(f instanceof MaskingCircle){
//                Data.GUI_Data.Main.oPIVFrame.Mask.oSimpleMasking.jLM.addElement(f);
//            }
//            if(f instanceof MaskingRhombus){
//                Data.GUI_Data.Main.oPIVFrame.Mask.oSimpleMasking.jLM.addElement(f);
//            }
//            if(f instanceof MaskingSquare){
//                Data.GUI_Data.Main.oPIVFrame.Mask.oSimpleMasking.jLM.addElement(f);
//            }
//            if(Data.FeatureContainer.loPIVGrid.contains(f)){
//                Data.GUI_Data.Main.oPIVFrame.PIVPre.oPIVPreProc.jLM.addElement(f);
//            }
//            if(Data.FeatureContainer.loPIVCorr.contains(f)){
//                Data.GUI_Data.Main.oPIVFrame.PIVProc.oPIV.jLM.addElement(f);
//            }
//            if(Data.FeatureContainer.loPIVDispAndVali.contains(f)){
//                Data.GUI_Data.Main.oPIVFrame.PIVProc.oDispAndVali.jLM.addElement(f);
//            }
//            if(Data.FeatureContainer.loPIVProcess.contains(f)){
//                Data.GUI_Data.Main.oPIVFrame.PIVProc.oProcess.jLM.addElement(f);
//            }
            
        }

        MainFrame.getFoldersModel().setOutFolder(sWD);
        setVisible(false);
        MainFrame.getControlFrame().setVisible(true);
        MainFrame.getImageViewFrame().updateLayerList();
        
    }

    private void setLayout() {

        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 0;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jLabelWD, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 3; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 1;
//        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.EAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jTextFieldFolder, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 3;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        c.anchor = GridBagConstraints.EAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jButtonLoadFolder, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        c.anchor = GridBagConstraints.EAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(JButtonLoadSession, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 2;
//        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHEAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jButtonStartNewSession, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 3; //GridBagConstraints.REMAINDER;
        c.gridheight = 2; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 3;
//        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jTextAreaExplainArea, c);
        
        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 3; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
//        c.anchor = GridBagConstraints.EAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jCheckBoxDemandMode, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
//        c.anchor = GridBagConstraints.EAST;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jLabelPlaceHolder, c);

    }

    public void setText() {
        this.jTextAreaExplainArea.setText("\n Raw development GUI in the openTIV project"
                + "\n"
                + "Developed by TVIConsultancy 2019");
    }

}
