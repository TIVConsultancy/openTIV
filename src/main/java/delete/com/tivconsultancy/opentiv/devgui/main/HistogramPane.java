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

import com.tivconsultancy.opentiv.helpfunctions.statistics.HistogramClass;
import com.tivconsultancy.opentiv.imageproc.img_properties.GrayHistogram;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.interfaces.Value;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Thomas Ziegenhein
 */
public class HistogramPane extends JLabel {
    
    int iHeight = 140;
    
    int iLeftBorder = 10;
    int iRightBorder = 10;
        
    int iTopBorder = 10;
    int iBottomBorder = 10;
    
    GrayHistogram o;
    
    public HistogramPane(){
        super("fhf");       
        super.setSize(255+iLeftBorder+iRightBorder, iHeight+ iTopBorder + iBottomBorder);
        super.setPreferredSize( new Dimension(255+iLeftBorder+iRightBorder, iHeight+ iTopBorder + iBottomBorder));
        super.setMinimumSize( new Dimension(255+iLeftBorder+iRightBorder, iHeight+ iTopBorder + iBottomBorder));
        super.setMaximumSize(new Dimension(255+iLeftBorder+iRightBorder, iHeight+ iTopBorder + iBottomBorder));
    }
    
    public void updateStatus(ImageGrid oInput){
        o = new GrayHistogram(oInput);
        this.repaint();
    }
    
    public void updateStatus(ImageInt oInput){
        o = new GrayHistogram(oInput);
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setVerticalAlignment(JLabel.TOP);
        setText("<html><nobr><font color='#ffff00'>Cumulative </font> <br> <font color='#ffffff'>Histogram</font></nobr></html>");
        g.setColor(Color.BLACK);
        for(int i = 0; i < this.getHeight(); i++){
            for(int j = 0; j < this.getWidth(); j++){
                g.drawLine(j,i,j,i);
            }
            
        }

        try {
            EnumObject oEnum;
            if(o == null || o.loClasses.isEmpty()){
                super.paintComponent(g);
                g.dispose();
                return;
            }
            g.setColor(Color.WHITE);
            oEnum = Sorting.getMaxCharacteristic(o.loClasses, (Sorting.Characteristic) (Object pParameter) -> 1.0*((HistogramClass) pParameter).loContent.size());            
            for(int i = 0; i< o.loClasses.size()-1; i++){
                HistogramClass oClass1 = o.loClasses.get(i);
                HistogramClass oClass2 = o.loClasses.get(i+1);
                int iY1 = (int) (1.0 * iHeight* ((double) oClass1.loContent.size()) / oEnum.dEnum);
                int iY2 = (int) (1.0 * iHeight* ((double) oClass2.loContent.size()) / oEnum.dEnum);
                g.drawLine(i+iLeftBorder, iHeight+iTopBorder-iY1, i+1+iLeftBorder, iHeight+iTopBorder-iY2);             
            }
            g.setColor(Color.YELLOW);
            List<Double> loCumulative = o.getCumulativeValues(new Value<HistogramClass>() {

                @Override
                public Double getValue(HistogramClass pParameter) {
                    return 1.0*pParameter.loContent.size();
                }
            });
            double dMax = loCumulative.get(loCumulative.size()-1);
            for(int i = 0; i< loCumulative.size()-1; i++){
                int iY1 = (int) (1.0 * iHeight* loCumulative.get(i) / dMax);
                int iY2 = (int) (1.0 * iHeight* loCumulative.get(i+1) / dMax);
                g.drawLine(i+iLeftBorder, iHeight+iTopBorder-iY1, i+1+iLeftBorder, iHeight+iTopBorder-iY2);
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(HistogramPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.paintComponent(g);
        g.dispose();
    }
}
