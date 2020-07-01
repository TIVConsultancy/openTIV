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

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsView;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class Frame_SettingsView extends JInternalFrame {        
    SettingsView oSettingsView;    
    Frame_Main oMainFrame;
    
    public Frame_SettingsView(Frame_Main oMainFrame){
        
        this.oMainFrame = oMainFrame;
        setIconifiable(false);
        setResizable(false);
        setClosable(true);
        
        this.oMainFrame = oMainFrame;
        oSettingsView = new SettingsView(oMainFrame.getSettingsModel().getAllSettings());
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        oSettingsView.setPreferredSize(new Dimension(500, 500));
        oSettingsView.oClusterView.setPreferredSize(new Dimension(500, 250));
        this.add(oSettingsView);
        this.add(oSettingsView.oClusterView);
        
//        JPanel settingsViewPannel = new JPanel();
//        
////        settingsViewPannel.setPreferredSize(new Dimension(500, 250));
////        settingsViewPannel.add(oSettingsView, BorderLayout.CENTER);                
//        
////	settingsViewPannel.add(oSettingsView.oClusterView, BorderLayout.SOUTH);
//        
//        JPanel panel = new JPanel(new GridLayout(1,0));
//        panel.add(settingsViewPannel.oClusterView);
//	add(panel, BorderLayout.SOUTH);
//        
////        oSettingsView.setOpaque(true);
////        this.setContentPane(oSettingsView);
//        this.setContentPane(settingsViewPannel);

        setLayout();
        setSize(430, 350);
        pack();
//        setVisible(true);
    }
    
    private void setLayout() {

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
        c.weightx = 1;
        c.weighty = 1;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(oSettingsView, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 3; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 4;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
//        c.anchor = GridBagConstraints.SOUTH;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(oSettingsView.oClusterView, c);
    }
    
    
}
