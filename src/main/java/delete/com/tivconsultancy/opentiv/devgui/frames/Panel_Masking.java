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
import delete.com.tivconsultancy.opentiv.devgui.main.StandardPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author Thomas Ziegenhein
 */
public class Panel_Masking extends JPanel {  
    
    public StandardPanel oSimpleMasking;
    Frame_Main MainFrame;
    
    JLabel a = new JLabel("");
    
    public Panel_Masking(String sType, Frame_Main MainFrame){
        super();
        this.MainFrame = MainFrame;
        oSimpleMasking = new StandardPanel("Simple Shapes", StandardEnum.Mask, sType, MainFrame);
        this.add(oSimpleMasking);
        this.add(a);
        setLayout();   
    }
    
    public void setLayout(){
        this.setLayout(new GridBagLayout());        
        GridBagConstraints c;
                        
        
        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 0;
        
        ((GridBagLayout) this.getLayout()).setConstraints(oSimpleMasking, c);        
        
        c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;
        
        ((GridBagLayout) this.getLayout()).setConstraints(a, c);
        
    }
    
        public void cleanup(){
        oSimpleMasking.cleanList();
    }

}
