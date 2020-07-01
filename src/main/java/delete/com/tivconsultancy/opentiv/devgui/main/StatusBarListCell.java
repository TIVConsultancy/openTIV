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
import java.awt.Graphics;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StatusBarListCell extends JLabel {
    
    public StatusBarListCell() {
        super();        
    }
    
    public StatusBarListCell(JLabel o) {
        super(o.getText());
        super.setBackground(o.getBackground());
        super.setForeground(o.getForeground());
//        super.get
//        super = o;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(47, 135, 215));
        g.drawRect(0, 0, getWidth()/2, getHeight()-1);
        g.fillRect(0, 0, getWidth()/2, getHeight()-1);
        super.paintComponent(g);
        g.dispose();
    }
    
}
