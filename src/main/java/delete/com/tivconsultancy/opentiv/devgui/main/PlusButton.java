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

import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

/**
 *
 * @author Thomas Ziegenhein
 */
public class PlusButton extends JButton implements CallingFeatureWindow{
    
    Enum sCategory;
    DefaultListModel oMasterList;
    
    public PlusButton(String sText, Enum sCategory, DefaultListModel oMaster){
        super(sText);
        this.sCategory = sCategory;
        this.oMasterList = oMaster;
        this.setBorder(null);
        this.setFont(javax.swing.UIManager.getDefaults().getFont("Button.font").deriveFont(20f));
        this.setPreferredSize(new Dimension(30, 30));
        this.setText("+");
    }    
    

    @Override
    public void addFeature(Feature oF) {
        oMasterList.addElement(oF);        
    }

    @Override
    public Enum getCategory() {
        return sCategory;
    }
    
    
    
}
