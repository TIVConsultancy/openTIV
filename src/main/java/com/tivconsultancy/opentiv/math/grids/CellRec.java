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
import com.tivconsultancy.opentiv.math.sets.Set2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class CellRec extends Set2D implements Serializable {

    private static final long serialVersionUID = 6427708836157549071L;

    public List<Position> loContent = new ArrayList<>();
    public Double dAverage = null;
    public Double dSTDDev = null;
    
    public boolean bRefined = false;
    public CellRec[][] oRefinedCells;

    public CellRec(Set1D oIntervalX, Set1D oIntervalY) {
        super(oIntervalX, oIntervalY);
    }
    
    public void refine(){
        oRefinedCells = new CellRec[2][2];
        oRefinedCells[0][0] = new CellRec(new Set1D(this.oIntervalX.dLeftBorder, this.oIntervalX.getCenter()), new Set1D(this.oIntervalY.dLeftBorder, this.oIntervalY.getCenter()));
        oRefinedCells[0][1] = new CellRec(new Set1D(this.oIntervalX.getCenter(), this.oIntervalX.dRightBorder), new Set1D(this.oIntervalY.dLeftBorder, this.oIntervalY.getCenter()));

        oRefinedCells[1][0] = new CellRec(new Set1D(this.oIntervalX.dLeftBorder, this.oIntervalX.getCenter()), new Set1D(this.oIntervalY.getCenter(), this.oIntervalY.dRightBorder));
        oRefinedCells[1][1] = new CellRec(new Set1D(this.oIntervalX.getCenter(), this.oIntervalX.dRightBorder), new Set1D(this.oIntervalY.getCenter(), this.oIntervalY.dRightBorder));
        bRefined = true;
    }

    public boolean isInside(Position o) {
        return this.oIntervalX.isInsideLR(o.getPosX()) && this.oIntervalY.isInsideLR(o.getPosY());
    }

    public void addContent(Position o) {
        if (o != null && isInside(o)) {
            loContent.add(o);
        }
    }

    public Double average(Value oValue, Value oWeight) {
        double dIntegrant = 0.0;
        double dWeight = 0.0;
        for (Position oPos : loContent) {
            if(oPos == null) continue;
            dIntegrant = dIntegrant + (oValue.getValue(oPos) * oWeight.getValue(oPos));
            dWeight = dWeight + oWeight.getValue(oPos);
        }
        if (dWeight != 0) {
            this.dAverage = dIntegrant / dWeight;
        }else if(dIntegrant == 0){
            this.dAverage = 0.0;
        }
        return this.dAverage;
    }

    public Double Variance(Value oValue, Value oWeight) {
        double dIntegrant = 0.0;
        double dWeight = 0.0;
        this.average(oValue, oWeight);
        for (Position oPos : loContent) {
            dIntegrant = dIntegrant + oWeight.getValue(oPos) * (oValue.getValue(oPos) - this.dAverage) * (oValue.getValue(oPos) - this.dAverage);
            dWeight = dWeight + oWeight.getValue(oPos);
        }
        if (dWeight != 0) {
            this.dSTDDev = dIntegrant / dWeight;
        }
        return this.dSTDDev;
    }
    
    public Double Variance(Value oValue, Value oWeight, Double dAVG) {
        double dIntegrant = 0.0;
        double dWeight = 0.0;        
        for (Position oPos : loContent) {
            dIntegrant = dIntegrant + oWeight.getValue(oPos) * (oValue.getValue(oPos) - dAVG) * (oValue.getValue(oPos) - dAVG);
            dWeight = dWeight + oWeight.getValue(oPos);
        }
        if (dWeight != 0) {
            this.dSTDDev = dIntegrant / dWeight;
        }
        return this.dSTDDev;
    }
    

    public Double CrossCorr(Value oValue, Value oWeight, Value oValue2, Value oWeight2) {
        double dIntegrant = 0.0;
        double dWeight = 0.0;
        double dAverage1 = this.average(oValue, oWeight);
        double dAverage2 = this.average(oValue2, oWeight2);
        for (Position oPos : loContent) {
            dIntegrant = dIntegrant + oWeight.getValue(oPos) * (oValue.getValue(oPos) - dAverage1) * (oValue2.getValue(oPos) - dAverage2);
            dWeight = dWeight + oWeight.getValue(oPos);
        }
        this.dAverage = null;
        if (dWeight != 0) {
            this.dSTDDev = dIntegrant / dWeight;
        }
        return this.dSTDDev;
    }    

}
