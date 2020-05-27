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
package com.tivconsultancy.opentiv.math.grids;

import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class CellRec1D extends Set1D implements Serializable{

    private static final long serialVersionUID = 5627326099743247588L;
    public List<Object> loContent = new ArrayList<>();
    public Double dAverage = null;
    public Double dSTDDev = null;
    
    public CellRec1D(double dLeftBorder, double dRightBorder){
        super(dLeftBorder, dRightBorder);
    }
    
    public void addContent(Object o, Value oValue) {
        if (o != null && isInside(o, oValue)) {
            loContent.add(o);
        }
    }
    
    public boolean isInside (Object o, Value oValue) {
        if(oValue == null)  return super.isInside((Double) o);
        return super.isInside(oValue.getValue(o));
    }
    
    public Double average(Value oValue, Value oWeight) {
        double dIntegrant = 0.0;
        double dWeight = 0.0;
        for (Object oPos : loContent) {
            if(oPos == null) continue;
            dIntegrant = dIntegrant + (oValue.getValue(oPos) * oWeight.getValue(oPos));
            dWeight = dWeight + oWeight.getValue(oPos);
        }
        if (dWeight != 0) {
            this.dAverage = dIntegrant / dWeight;
        }
        return this.dAverage;
    }
    
    public Double STDDev(Value oValue, Value oWeight) {
        double dIntegrant = 0.0;
        double dWeight = 0.0;
        this.average(oValue, oWeight);
        for (Object oPos : loContent) {
            dIntegrant = dIntegrant + oWeight.getValue(oPos) * (oValue.getValue(oPos) - this.dAverage) * (oValue.getValue(oPos) - this.dAverage);
            dWeight = dWeight + oWeight.getValue(oPos);
        }
        if (dWeight != 0) {
            this.dSTDDev = dIntegrant / dWeight;
        }
        return this.dSTDDev;
    }
    
}
