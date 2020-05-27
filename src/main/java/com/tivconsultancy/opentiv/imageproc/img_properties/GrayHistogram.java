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
package com.tivconsultancy.opentiv.imageproc.img_properties;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.helpfunctions.statistics.Histogram;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.interfaces.Value;

/**
 *
 * @author Thomas Ziegenhein
 */
public class GrayHistogram extends Histogram {
    
    public GrayHistogram(ImageGrid oGrid){
        super(0,256,1);        
        for(ImagePoint o : oGrid.oa){
            addContent(o, new Value() {

                @Override
                public Double getValue(Object pParameter) {
                    return (double) ((ImagePoint) pParameter).iValue;
                }
            });
        }
    }
    
    public GrayHistogram(ImageInt oGrid){
        super(0,256,1);        
        for(int i = 0; i < oGrid.iaPixels.length; i++){
            for(int j = 0; j< oGrid.iaPixels[0].length; j++){                
            addContent(new MatrixEntry(i, j, oGrid.iaPixels[i][j]), null);
            }
        }
    }

    @Override
    public void addContent(Object oContent, Value oValue) {
        
        if(oContent instanceof MatrixEntry){
            loClasses.get( (int) ((MatrixEntry) oContent).dValue).loContent.add(oContent);
        }
        
        if(oContent instanceof ImagePoint){
            loClasses.get(((ImagePoint) oContent).iValue).loContent.add(oContent);
        }
                
                
    }
    
    
    
}
