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
import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class StandardPanel extends JPanel {

    Enum Ident;
    String sType;

    //Title
    JLabel Title;

    //List
    public DefaultListModel jLM;
    public JList jL;
    public JScrollPane jSP;

    //Buttons
    PlusButton jBPlus;
    MinusButton jBMinus;
    JButton jCheckButton;

    //Input
    JLabel JLInput1;
    JLabel JLInput2;
    JTextField JTInput1;
    JTextField JTInput2;

    /**
     * Separator
     */
    JSeparator JSepBot = new JSeparator();
    
    Frame_Main MainFrame;

    public StandardPanel(String sTitle, Enum Identifier, String sType, Frame_Main MainFrame) {
        this.MainFrame = MainFrame;
        this.Ident = Identifier;
        this.sType = sType;

        Title = new JLabel(sTitle);
        jLM = new DefaultListModel();
        jL = new javax.swing.JList(jLM);
        jSP = new JScrollPane(jL);
        jBPlus = new PlusButton("+", Identifier, jLM);
        jBMinus = new MinusButton("-", jLM);
        jCheckButton = new CheckButton();
        jCheckButton.setToolTipText("applies changes");
        JLInput1 = new JLabel("");
        JLInput2 = new JLabel("");
        JTInput1 = new JTextField("");
        JTInput1.setEnabled(false);
        JTInput2 = new JTextField("");
        JTInput2.setEnabled(false);

        setPrefSizes();

        this.add(Title);
        this.add(jSP);
        this.add(jBPlus);
        this.add(jBMinus);
        this.add(jCheckButton);
        this.add(JLInput1);
        this.add(JLInput2);
        this.add(JTInput1);
        this.add(JTInput2);
        this.add(JSepBot);

        setLayout();

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

        jCheckButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CheckButtonClicked(evt);
            }
        });

        JTInput1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                updateSettings();
            }
        });

        JTInput2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                updateSettings();
            }
        });

        JTInput1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                updateSettings();
            }
        });

        JTInput2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                updateSettings();
            }
        });

        jL.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                changedListSelection();
            }
        });

        jL.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                return;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                ShiftUpDownEntries(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                return;
            }
        });

//        this.setBackground(Color.red);
    }

    public void PlusButtonClicked(java.awt.event.MouseEvent evt) {
        MainFrame.getFeatureFrame().callWindow(jBPlus);
        updateSettings();
    }

    public void MinusButtonClicked(java.awt.event.MouseEvent evt) {
        if (!jL.isSelectionEmpty() && jL.getSelectedValue() != null) {
            ((Feature) jL.getSelectedValue()).removeFeature();
            jLM.remove(jL.getSelectedIndex());
        }
//        recalc();
    }

    public void updateSettings() {
        if (!jL.isSelectionEmpty() && jL.getSelectedValue() != null) {
            if (JTInput1.isEnabled() && !JTInput1.getText().isEmpty()) {
                ((Feature) jL.getSelectedValue()).setSettings1(JTInput1.getText().trim());
            }
            if (JTInput2.isEnabled() && !JTInput2.getText().isEmpty()) {
                ((Feature) jL.getSelectedValue()).setSettings2(JTInput2.getText().trim());
            }
        }
    }

    public void CheckButtonClicked(java.awt.event.MouseEvent evt) {
        updateSettings();
        recalc();        
    }

    
    public void recalc() {
        
        MainFrame.recalc(Ident);
        
//        if (sType.equals("PIV")) {
//            ImagePath oPath = (ImagePath) MainFrame.getImageViewFrame().jListImages.getSelectedValue();
//            if (oPath == null) {
//                return;
//            }
//            if (Data.GUI_Data.PIV_Processes != null) {
//                Data.GUI_Data.PIV_Processes.cancel(true);
//            }
//            Data.GUI_Data.PIV_Processes = null;
//            Data.GUI_Data.PIV_Processes = new ProcessHandler_PIV(oPath.o, this.sIdent);
////        Data.PIVGUI_Data.PIV_Processes.run(null, sIdent);
//        }
//        if (sType.equals("ContourDetect")) {
//            ImagePath oPath = (ImagePath) MainFrame.getImageViewFrame().jListImages.getSelectedValue();
//            if (oPath == null) {
//                return;
//            }
//            if (Data.GUI_Data.CD_Processes != null) {
//                Data.GUI_Data.CD_Processes.cancel(true);
//            }
//            Data.GUI_Data.CD_Processes = null;
//            Data.GUI_Data.CD_Processes = new ProcessHandler_ContourDetect(oPath.o, this.sIdent);
//        Data.PIVGUI_Data.PIV_Processes.run(null, sIdent);
//        }

    }

    public void changedListSelection() {
        setVisibilityTextFields();
    }

    public final void setPrefSizes() {

        this.setPreferredSize(new Dimension(290, 175));
        Title.setPreferredSize(new Dimension(260, 35));
//        Title.setFont(javax.swing.UIManager.getDefaults().getFont("Label.font").deriveFont(15f));
        Title.setFont(javax.swing.UIManager.getDefaults().getFont("Label.font").deriveFont(Font.TYPE1_FONT, 15f));
        jSP.setPreferredSize(new Dimension(110, 135));
        JLInput1.setPreferredSize(new Dimension(135, 30));
        JLInput2.setPreferredSize(new Dimension(135, 30));
        JTInput1.setPreferredSize(new Dimension(135, 30));
        JTInput2.setPreferredSize(new Dimension(135, 30));
    }

    public void cleanList() {
        jLM.clear();
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
        c.gridheight = 4; //GridBagConstraints.REMAINDER;
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
        c.weightx = 1;
        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(jBPlus, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1;
        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(jBMinus, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1;
        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(jCheckButton, c);

        c = new GridBagConstraints();
        c.ipadx = -5;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.SOUTH;

        ((GridBagLayout) this.getLayout()).setConstraints(JLInput1, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1;
        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(JTInput1, c);

        c = new GridBagConstraints();
        c.ipadx = -5;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 3;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;

        ((GridBagLayout) this.getLayout()).setConstraints(JLInput2, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 4;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1;
        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(JTInput2, c);

        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 4; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1;
        c.weighty = 1;

        ((GridBagLayout) this.getLayout()).setConstraints(JSepBot, c);

    }

    public void setVisibilityTextFields() {
        if (!jL.isSelectionEmpty() && jL.getSelectedValue() != null) {
            Feature oPreProc = (Feature) jL.getSelectedValue();
            String sSettingsText1 = oPreProc.getSettingsText1();
            String sSettingsText2 = oPreProc.getSettingsText2();
            String sSettings1 = oPreProc.getSettings1();
            String sSettings2 = oPreProc.getSettings2();
            if (sSettingsText1 != null && sSettings1 != null) {
                JLInput1.setVisible(true);
                JTInput1.setVisible(true);
                JTInput1.setEnabled(true);

                JLInput1.setText(sSettingsText1);
                JTInput1.setText(sSettings1);

                JLInput1.setToolTipText(oPreProc.getSettingsDescriptions1());
                JTInput1.setToolTipText(oPreProc.getValueDescriptions1());

            } else {
                JLInput1.setText("");
                JLInput1.setToolTipText(null);

                JTInput1.setText("");
                JTInput1.setToolTipText(null);
                JTInput1.disable();

            }

            if (sSettingsText1 != null && sSettings2 != null) {
                JLInput2.setVisible(true);
                JTInput2.setVisible(true);
                JTInput2.setEnabled(true);

                JLInput2.setText(sSettingsText2);
                JTInput2.setText(sSettings2);

                JLInput2.setToolTipText(oPreProc.getSettingsDescriptions2());
                JTInput2.setToolTipText(oPreProc.getValueDescriptions2());

            } else {
                JLInput2.setText("");
                JLInput2.setToolTipText(null);

                JTInput2.setText("");
                JTInput2.setToolTipText(null);
                JTInput2.disable();
            }

        }

        revalidate();
        repaint();

    }

    public void ShiftUpDownEntries(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int Index = jL.getSelectedIndex();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (Index > 0) {
                    swap(Index, Index - 1);
//                    jL.setSelectedIndex(Index - 1);
                    jL.ensureIndexIsVisible(Index - 1);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (Index < jLM.getSize() - 1) {
                    swap(Index, Index + 1);
//                    jL.setSelectedIndex(Index + 1);
                    jL.ensureIndexIsVisible(Index + 1);
                }
                break;
        }
    }

    //Swap two elements in the list.
    private void swap(int a, int b) {
        Object aObject = this.jLM.getElementAt(a);
        Object bObject = this.jLM.getElementAt(b);
        this.jLM.set(a, bObject);
        this.jLM.set(b, aObject);
    }

}
