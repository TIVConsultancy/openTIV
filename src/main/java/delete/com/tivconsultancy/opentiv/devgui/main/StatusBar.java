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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StatusBar extends JLabel {
    
    double dNumber = 0;
    String sPostText = "%";
    
    public StatusBar(String sText){
        super(sText);
        this.setPreferredSize(new Dimension(100, 20));
        this.setMinimumSize(new Dimension(100, 20));
    }
    
    private void setStatus(double dFraction){
        dNumber = (double)((int) (dFraction *1000.0))/10.0;        
    }
    
    public double getStatus(){
        return dNumber*10.0/1000.0;
    }
    
    public void updateStatus(double dFraction){
        setStatus(dFraction);
        this.repaint();
    }
    
    public void reset(){
        dNumber = 0.0;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(47, 135, 215));
        g.drawRect(0, 0, (int)(dNumber), getHeight()-1);
        g.fillRect(0, 0, (int)(dNumber), getHeight()-1);
        this.setText(dNumber + sPostText);
        super.paintComponent(g);
        g.dispose();
    }

}
