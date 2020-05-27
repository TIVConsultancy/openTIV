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
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Rec1D implements Serializable{

    private static final long serialVersionUID = 7053731468304358700L;

    CellRec1D[] oCells;
    List<Object> loContent = new ArrayList<>();
    public Value oValue = (Value) (Object pParameter) -> (Double) pParameter;
    public Value oAverageValue = (Value) (Object pParameter) -> (Double) pParameter;
    public Value oWeight = (Value<Object>) (Object pParameter) -> 1.0;

    public Rec1D(int iCellCount, double dLeftBorder, double dRightBorder) {
        double dCellSize = (dRightBorder - dLeftBorder) / ((double) iCellCount);
        oCells = new CellRec1D[iCellCount];

        for (int j = 0; j < iCellCount; j++) {
            double dXL = dLeftBorder + j * dCellSize;
            double dXR = dXL + dCellSize;
            oCells[j] = new CellRec1D(dXL, dXR);
        }
    }

    public void addContent(Object oContent) {
        if (oContent instanceof Collection<?>) {
            for (Object o : (Collection<?>) oContent) {
                this.addContent(o);
            }
        } else {
            for (CellRec1D o : oCells) {
                if (o.isInside(oContent, oValue)) {
                    o.addContent(oContent, oValue);
                    return;
                }
            }
        }

    }

    public CellRec1D getCell(Double xValue) {
        for (CellRec1D o : oCells) {
            if (o.isInside(xValue)) {
                return o;
            }
        }
        return null;
    }

    public Double[][] calcAverage() {
        Double[][] opReturn = new Double[this.oCells.length][2];
        for (int i = 0; i < this.oCells.length; i++) {

            if (oCells[i].loContent.isEmpty()) {
                continue;
            }
            double dAVG = oCells[i].average(oAverageValue, oWeight);
            oCells[i].dAverage = dAVG;
            opReturn[i][0] = oCells[i].getCenter();
            opReturn[i][1] = dAVG;

        }
        return opReturn;
    }
    
    public Double[][] getInformationCount() {
        Double[][] opReturn = new Double[this.oCells.length][2];
        for (int i = 0; i < this.oCells.length; i++) {

            if (oCells[i].loContent.isEmpty()) {
                continue;
            }
            double dCount = oCells[i].loContent.size();            
            opReturn[i][0] = oCells[i].getCenter();
            opReturn[i][1] = dCount;

        }
        return opReturn;
    }

    public Double[][] calcSTDDev() {
        Double[][] opReturn = new Double[this.oCells.length][2];
        for (int i = 0; i < this.oCells.length; i++) {
            double dSTD = oCells[i].STDDev(oAverageValue, oWeight);
            oCells[i].dSTDDev = dSTD;
            opReturn[i][0] = oCells[i].getCenter();
            opReturn[i][1] = dSTD;

        }
        return opReturn;
    }

    public List<String> toString(Value o) {

        List<String> lsOut = new ArrayList<>();

        lsOut.add("X,Value");
        for (CellRec1D oCell : oCells) {
            String sOut;
            if (o == null) {
                sOut = oCell.getCenter() + "," + oCell.dAverage;
            } else {
                sOut = oCell.getCenter() + "," + o.getValue(oCell);
            }
            lsOut.add(sOut);
        }

        return lsOut;
    }

}
