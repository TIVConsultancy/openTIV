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

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class RecOrtho2D implements Serializable{

    private static final long serialVersionUID = -2661796203394511986L;

    public CellRec[][] oCells;
    public Value oValue = (Value<OrderedPair> & Serializable) (OrderedPair pParameter) -> pParameter.getValue(pParameter);
    public Value oWeight = (Value<OrderedPair> & Serializable) (OrderedPair pParameter) -> 1.0;
    public Double dCellSizeX;
    public Double dCellSizeY;
    public double dLeftBorder;
    public double dRightborder;
    public double dTopBroder;
    public double dBottomBorder;
    int iCellCountX;
    int iCellCountY;

    public RecOrtho2D(double dLeftBorder, double dRightBorder, double dTopBorder, double dBottomBorder, int iCellCountX, int iCellCountY) {
        this.dRightborder = dRightBorder;
        this.dLeftBorder = dLeftBorder;
        this.dTopBroder = dTopBorder;
        this.dBottomBorder = dBottomBorder;
        this.iCellCountX = iCellCountX;
        this.iCellCountY = iCellCountY;
        dCellSizeX = (dRightBorder - dLeftBorder) / ((double) iCellCountX);
        dCellSizeY = (dTopBorder - dBottomBorder) / ((double) iCellCountY);
        oCells = new CellRec[iCellCountY][iCellCountX];
        for (int i = 0; i < iCellCountY; i++) {
            for (int j = 0; j < iCellCountX; j++) {
                double dXL = dLeftBorder + j * dCellSizeX;
                double dXR = dXL + dCellSizeX;
                double dYL = dBottomBorder + i * dCellSizeY;
                double dYR = dYL + dCellSizeY;
                oCells[i][j] = new CellRec(new Set1D(dXL, dXR), new Set1D(dYL, dYR));
            }
        }
    }

    public void generate50OverlapGrid() {
        iCellCountX = 2 * iCellCountX - 1;
        iCellCountY = 2 * iCellCountY - 1;
        oCells = new CellRec[iCellCountY][iCellCountX];
        oCells = new CellRec[iCellCountY][iCellCountX];
        double dXL;
        double dXR;
        double dYL;
        double dYR;
        dYL = dBottomBorder - 0.5 * dCellSizeY;
        for (int i = 0; i < iCellCountY; i++) {
            dYL = dYL + 0.5 * dCellSizeY;
            dYR = dYL + dCellSizeY;
            dXL = dLeftBorder - 0.5 * dCellSizeX;
            for (int j = 0; j < iCellCountX; j++) {
                dXL = dXL + 0.5 * dCellSizeX;
                dXR = dXL + dCellSizeX;
                oCells[i][j] = new CellRec(new Set1D(dXL, dXR), new Set1D(dYL, dYR));
            }
        }
    }

    public void addContent(Collection<? extends Position> loContent) {
        for (Position o : loContent) {
            this.addContent(o);
        }
    }

    public void addContent(Position oContent) {
        for (CellRec[] a : oCells) {
            for (CellRec o : a) {
                if (o.isInside(oContent)) {
                    if (o.bRefined) {
                        for (int i = 0; i < 2; i++) {
                            for (int j = 0; j < 2; j++) {
                                o.oRefinedCells[i][j].addContent(oContent);
                            }
                        }
                    }
                    o.addContent(oContent);
                }
            }
        }
    }

    public void clearContent() {
        for (CellRec[] a : oCells) {
            for (CellRec o : a) {
                o.loContent = new ArrayList<>();
            }
        }
    }

    public CellRec getCell(Position oPos) {
        for (CellRec[] a : oCells) {
            for (CellRec o : a) {
                if (o.isInside(oPos)) {
                    return o;
                }
            }
        }
        return null;
    }
    
    public MatrixEntry getPositonInGrid(Position oPos) {        
        for (int i = 0; i< oCells.length; i++) {
            for (int j = 0; j < oCells[0].length; j++) {
                CellRec o = oCells[i][j];
                if (o.isInside(oPos)) {
                    return new MatrixEntry(i, j);
                }
            }
        }
        return null;
    }
    
    public List<CellRec> getNeighbors(Position oPos) {
        List<CellRec> loNeigh = new ArrayList();
        MatrixEntry opPos = this.getPositonInGrid(oPos);
        if(opPos == null) return loNeigh;        
        for (int i = -1; i<= 1; i++) {
            for (int j = -1; j <=1; j++) {
                if((opPos.i + i) >= 0 && (opPos.i + i) < oCells.length){
                    if((opPos.j + j) >= 0 && (opPos.j + j) < oCells[0].length){
                        if(i == 0 && j == 0) continue;
                        loNeigh.add(oCells[(int) opPos.i + i][(int) opPos.j + j]);
                    }
                }                
            }
        }
        return loNeigh;
    }

    public OrderedPair[][] calcAverage() {
        OrderedPair[][] opReturn = new OrderedPair[this.oCells.length][this.oCells[0].length];
        for (int i = 0; i < this.oCells.length; i++) {
            for (int j = 0; j < this.oCells[0].length; j++) {
                if (oCells[i][j].loContent.isEmpty()) {
                    continue;
                }
                if (oCells[i][j].bRefined) {
                    for (int iSub = 0; iSub < 2; iSub++) {
                        for (int jSub = 0; jSub < 2; jSub++) {
                            oCells[i][j].oRefinedCells[iSub][jSub].dAverage = oCells[i][j].oRefinedCells[iSub][jSub].average(oValue, oWeight);
                        }
                    }
                }
                Double dAVG = oCells[i][j].average(oValue, oWeight);
                oCells[i][j].dAverage = dAVG;
                opReturn[i][j] = new OrderedPair(oCells[i][j].oIntervalX.getCenter(), oCells[i][j].oIntervalY.getCenter(), dAVG);
            }
        }
        return opReturn;
    }

    public OrderedPair[][] calcVariance() {
        OrderedPair[][] opReturn = new OrderedPair[this.oCells.length][this.oCells[0].length];
        for (int i = 0; i < this.oCells.length; i++) {
            for (int j = 0; j < this.oCells[0].length; j++) {
                Double dSTD = oCells[i][j].Variance(oValue, oWeight);                
                opReturn[i][j] = new OrderedPair(oCells[i][j].oIntervalX.getCenter(), oCells[i][j].oIntervalY.getCenter(), dSTD);
            }
        }
        return opReturn;
    }
    
    public OrderedPair[][] calcVariance(OrderedPair[][] opAVG) {
        OrderedPair[][] opReturn = new OrderedPair[this.oCells.length][this.oCells[0].length];
        for (int i = 0; i < this.oCells.length; i++) {
            for (int j = 0; j < this.oCells[0].length; j++) {
                Double dSTD = oCells[i][j].Variance(oValue, oWeight, opAVG[i][j].dValue);
                opReturn[i][j] = new OrderedPair(oCells[i][j].oIntervalX.getCenter(), oCells[i][j].oIntervalY.getCenter(), dSTD);
            }
        }
        return opReturn;
    }
    

    public OrderedPair[][] calcCrossCorr(Value oValue2, Value oWeight2) {
        OrderedPair[][] opReturn = new OrderedPair[this.oCells.length][this.oCells[0].length];
        for (int i = 0; i < this.oCells.length; i++) {
            for (int j = 0; j < this.oCells[0].length; j++) {
                opReturn[i][j] = new OrderedPair(oCells[i][j].oIntervalX.getCenter(), oCells[i][j].oIntervalY.getCenter(), oCells[i][j].CrossCorr(oValue, oWeight, oValue2, oWeight2));
            }
        }
        return opReturn;
    }

}
