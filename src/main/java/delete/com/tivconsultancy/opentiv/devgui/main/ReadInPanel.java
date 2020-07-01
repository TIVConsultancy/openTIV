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
package delete.com.tivconsultancy.opentiv.devgui.main;

import delete.com.tivconsultancy.opentiv.devgui.frames.Frame_Main;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Settings.StandardSettingNames;
import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ReadInPanel extends JPanel {

    //Title
    JLabel Title;

    //List
    public DefaultListModel jLM;
    public JList jL;
    public JScrollPane jSP;

    //Buttons
//    PlusButton jBPlus;
    MinusButton jBMinus;
    PlusButton jBPlus;
    JButton ReadInButton;

    //Input
    JLabel JLInput1;
    JTextField JTInput1;

    //Seperator
    JSeparator JSepBot = new JSeparator();

    //PlaceHolder
    JLabel jSpace1 = new JLabel("sd");
    
    
    Frame_Main MainFrame;
    
    /**
     * 
     * @param sTitle
     * @param Identifier     
     * @param MainFrame 
     */

    public ReadInPanel(String sTitle, Enum Identifier, Frame_Main MainFrame) {        
        this.MainFrame = MainFrame;
        Title = new JLabel(sTitle);
        jLM = new DefaultListModel();
        jL = new javax.swing.JList(jLM);
        jSP = new JScrollPane(jL);
        jBPlus = new PlusButton("+", Identifier, jLM);
        jBMinus = new MinusButton("-", jLM);
        ReadInButton = new JButton("Read Images");
        JLInput1 = new JLabel(" ");
        JTInput1 = new JTextField(" ");
        JTInput1.setEnabled(false);
        ReadInButton.setEnabled(false);

        setPrefSizes();

        this.add(Title);
        this.add(jSP);
        this.add(JLInput1);
        this.add(JTInput1);
        this.add(jBMinus);
        this.add(jBPlus);
        this.add(ReadInButton);

        setLayout();

        //Add Listeners
        ReadInButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ReadInButtonClicked(evt);
            }
        });

        //Add Listeners
        jBPlus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PlusButtonClicked(evt);
            }
        });

        jBMinus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                MinusButtonClicked(evt);
            }
        });

        jL.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                changedListSelection();
            }
        });

//        this.setBackground(Color.red);
    }

    protected void setDefault() {
        JLInput1.setText(" ");
        JTInput1.setText(" ");
        JTInput1.setEnabled(false);
    }

    public void PlusButtonClicked(java.awt.event.MouseEvent evt) {
        MainFrame.getFeatureFrame().callWindow(jBPlus);
    }

    public void MinusButtonClicked(java.awt.event.MouseEvent evt) {
        if (!jL.isSelectionEmpty() && jL.getSelectedValue() != null) {
            jLM.remove(jL.getSelectedIndex());
        }

        MainFrame.getImageViewFrame().emptyImagePaths();

        for (int i = 0; i < jLM.getSize(); i++) {
            Feature selectedFeature = (Feature) jLM.getElementAt(i);
            selectedFeature.setSettings1(selectedFeature.getSettings1());
            List<String> ls = getPictures((String) MainFrame.getSettingsModel().getSettings(StandardSettingNames.ReadIn).getSettingsValue("ReadInFolder"));
            MainFrame.getImageViewFrame().addImagePaths(ls);        
        }
        
        setDefault();

    }

    public void ReadInButtonClicked(java.awt.event.MouseEvent evt) {
        if (ReadInButton.isEnabled()) {
            if (!jL.isSelectionEmpty() && jL.getSelectedValue() != null) {
                Feature selectedFeature = (Feature) jL.getSelectedValue();
                selectedFeature.setSettings1(JTInput1.getText().trim());
                List<String> ls = getPictures((String) MainFrame.getSettingsModel().getSettings(StandardSettingNames.ReadIn).getSettingsValue("ReadInFolder"));
                MainFrame.getImageViewFrame().addImagePaths(ls);
            }
        }
    }

    public void changedListSelection() {
        if (jL.isSelectionEmpty()) {
            setDefault();
        }
        setVisibilityTextFields();
    }

    public final void setPrefSizes() {

        this.setPreferredSize(new Dimension(290, 175));
        Title.setPreferredSize(new Dimension(260, 35));
//        Title.setFont(javax.swing.UIManager.getDefaults().getFont("Label.font").deriveFont(15f));
        Title.setFont(javax.swing.UIManager.getDefaults().getFont("Label.font").deriveFont(Font.TYPE1_FONT, 15f));
        jSP.setPreferredSize(new Dimension(110, 135));
        JLInput1.setPreferredSize(new Dimension(135, 30));
        JTInput1.setPreferredSize(new Dimension(135, 30));

    }
    
    public List<Feature> getAllFeatures() {
        List<Feature> loList = new ArrayList<>();
        for (int i = 0; i < this.jLM.getSize(); i++) {
            loList.add((Feature) jLM.get(i));
        }
        return loList;
    }

    public void setLayout() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = -5;
        c.ipady = 0;
        c.gridwidth = 4; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
//        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.WEST;

        ((GridBagLayout) this.getLayout()).setConstraints(Title, c);

        c = new GridBagConstraints();
        c.ipadx = 10;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 3; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 1;
//        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;

        ((GridBagLayout) this.getLayout()).setConstraints(jSP, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
//        c.weightx = 1;
//        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(jBPlus, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
//        c.weightx = 1;
//        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(jBMinus, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;

        ((GridBagLayout) this.getLayout()).setConstraints(JLInput1, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
//        c.weightx = 1;
//        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(JTInput1, c);

        c = new GridBagConstraints();
        c.ipadx = -5;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;

        ((GridBagLayout) this.getLayout()).setConstraints(ReadInButton, c);

    }

    public void setVisibilityTextFields() {
        if (!jL.isSelectionEmpty() && jL.getSelectedValue() != null) {
            Feature selectedFeature = (Feature) jL.getSelectedValue();
            String sSettingsText1 = selectedFeature.getSettingsText1();
            String sSettings1 = selectedFeature.getSettings1();
            if (sSettingsText1 != null && sSettings1 != null) {
                JLInput1.setVisible(true);
                JTInput1.setVisible(true);
                JTInput1.setEnabled(true);

                JLInput1.setText(sSettingsText1);
                JTInput1.setText(sSettings1);

                JLInput1.setToolTipText(selectedFeature.getSettingsDescriptions1());
                JTInput1.setToolTipText(selectedFeature.getValueDescriptions1());

                ReadInButton.setEnabled(true);

            } else {
                JLInput1.setText("");
                JLInput1.setToolTipText(null);

                JTInput1.setText("");
                JTInput1.setToolTipText(null);
                JTInput1.disable();

                ReadInButton.setEnabled(false);

            }

        }

        revalidate();
        repaint();

    }

    public List<String> getPictures(String sPWDIn) {
        String sFileFormat = (String) MainFrame.getSettingsModel().getSettings(StandardSettingNames.ReadIn).getSettingsValue("ReadInFileType");
        List<String> ls = new ArrayList<>();
        File[] of = new File(sPWDIn + java.io.File.separator).listFiles();
        if(of==null){
            return ls;
        }
        for (File f : of) {
            if (f.getPath().contains(sFileFormat)) {
                ls.add(f.getAbsolutePath());
            }
        }
        return ls;
    }
    
    public void cleanList(){
        jLM.clear();
    }

}
