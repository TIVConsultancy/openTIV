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
package com.tivconsultancy.opentiv.velocimetry.helpfunctions;

import com.tivconsultancy.opentiv.helpfunctions.colorspaces.ColorSpaceCIEELab;
import com.tivconsultancy.opentiv.helpfunctions.colorspaces.Colorbar;
import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.grids.CellRec;
import com.tivconsultancy.opentiv.math.grids.RecOrtho2D;
import com.tivconsultancy.opentiv.math.interfaces.Position;
import com.tivconsultancy.opentiv.math.interfaces.Value;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import com.tivconsultancy.opentiv.postproc.vector.PaintVectors;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class VelocityGrid implements Serializable {

    private static final long serialVersionUID = 4600722142141334933L;

    public RecOrtho2D GridVeloX;
    public RecOrtho2D GridVeloY;
    public int iVectorStretch = 7;
    List<Color> loColors = Colorbar.StartEndLinearColorBar.getColdToWarmRainbow2();
    Colorbar oColBar = new Colorbar.StartEndLinearColorBar(0.0, 5.0, loColors, new ColorSpaceCIEELab(), (Colorbar.StartEndLinearColorBar.ColorOperation<Double>) (Double pParameter) -> pParameter);

    public VelocityGrid(double dLeftBorder, double dRightBorder, double dTopBorder, double dBottomBorder, int iCellCountX, int iCellCountY) {
        GridVeloX = new RecOrtho2D(dLeftBorder, dRightBorder, dTopBorder, dBottomBorder, iCellCountX, iCellCountY);
        GridVeloY = new RecOrtho2D(dLeftBorder, dRightBorder, dTopBorder, dBottomBorder, iCellCountX, iCellCountY);
        GridVeloX.oValue = (Value) (Object pParameter) -> ((VelocityVec) pParameter).getVelocityX();
        GridVeloY.oValue = (Value) (Object pParameter) -> ((VelocityVec) pParameter).getVelocityY();
    }

    public void generate50OverlapGrid() {
        GridVeloX.generate50OverlapGrid();
        GridVeloY.generate50OverlapGrid();
    }

    public void addContent(VelocityVec oVec) {
        GridVeloX.addContent(oVec);
        GridVeloY.addContent(oVec);
    }

    public void addContent(List<? extends VelocityVec> loVec) {
        for (VelocityVec oVec : loVec) {
            GridVeloX.addContent(oVec);
            GridVeloY.addContent(oVec);
        }
    }

    public void clearContent() {
        GridVeloX.clearContent();
        GridVeloY.clearContent();
    }

    public BufferedImage paintVecs(int[][] iaBlackBoard, String sFileOut) throws IOException {
        List<VelocityVec> oVeloVecs = new ArrayList<>();
        GridVeloX.calcAverage();
        GridVeloY.calcAverage();
        for (int i = 0; i < GridVeloX.oCells.length; i++) {
            for (int j = 0; j < GridVeloX.oCells[0].length; j++) {
                if (GridVeloX.oCells[i][j].bRefined && GridVeloY.oCells[i][j].bRefined) {
                    for (int iSub = 0; iSub < 2; iSub++) {
                        for (int jSub = 0; jSub < 2; jSub++) {
                            if (GridVeloX.oCells[i][j].oRefinedCells[iSub][jSub].dAverage != null && GridVeloY.oCells[i][j].oRefinedCells[iSub][jSub].dAverage != null) {
                                oVeloVecs.add(new VelocityVec(GridVeloX.oCells[i][j].oRefinedCells[iSub][jSub].dAverage, GridVeloY.oCells[i][j].oRefinedCells[iSub][jSub].dAverage, GridVeloX.oCells[i][j].oRefinedCells[iSub][jSub].getCenter()));
                            }
                        }
                    }
                } else {
                    if (GridVeloX.oCells[i][j].dAverage != null && GridVeloY.oCells[i][j].dAverage != null) {
                        oVeloVecs.add(new VelocityVec(GridVeloX.oCells[i][j].dAverage, GridVeloY.oCells[i][j].dAverage, GridVeloX.oCells[i][j].getCenter()));
                    }
                }

            }
        }
        AdjustColBar(oVeloVecs);
        return PaintVectors.paintOnImage(oVeloVecs, oColBar, iaBlackBoard, sFileOut, iVectorStretch);
    }

    public List<VelocityVec> getVectors() {
        List<VelocityVec> oVeloVecs = new ArrayList<>();
        GridVeloX.calcAverage();
        GridVeloY.calcAverage();
        for (int i = 0; i < GridVeloX.oCells.length; i++) {
            for (int j = 0; j < GridVeloX.oCells[0].length; j++) {
                if (GridVeloX.oCells[i][j].bRefined && GridVeloY.oCells[i][j].bRefined) {
                    for (int iSub = 0; iSub < 2; iSub++) {
                        for (int jSub = 0; jSub < 2; jSub++) {
                            if (GridVeloX.oCells[i][j].oRefinedCells[iSub][jSub].dAverage != null && GridVeloY.oCells[i][j].oRefinedCells[iSub][jSub].dAverage != null) {
                                oVeloVecs.add(new VelocityVec(GridVeloX.oCells[i][j].oRefinedCells[iSub][jSub].dAverage, GridVeloY.oCells[i][j].oRefinedCells[iSub][jSub].dAverage, GridVeloX.oCells[i][j].oRefinedCells[iSub][jSub].getCenter()));
                            }
                        }
                    }
                } else {
                    if (GridVeloX.oCells[i][j].dAverage != null && GridVeloY.oCells[i][j].dAverage != null) {
                        oVeloVecs.add(new VelocityVec(GridVeloX.oCells[i][j].dAverage, GridVeloY.oCells[i][j].dAverage, GridVeloX.oCells[i][j].getCenter()));
                    }
                }

            }
        }
        return oVeloVecs;
    }

    public VelocityVec getVector(Position pos) {
        Double dVX = GridVeloX.getCell(pos).dAverage;
        Double dVY = GridVeloX.getCell(pos).dAverage;
        OrderedPair opVecPos = GridVeloX.getCell(pos).getCenter();
        return new VelocityVec(dVX, dVY, opVecPos);
    }

    public List<VelocityVec> getNeighbors(Position pos) {
        List<VelocityVec> loReturn = new ArrayList<>();
//        loReturn.add(getVector(pos));
        List<CellRec> loNeighX = GridVeloX.getNeighbors(pos);
        List<CellRec> loNeighY = GridVeloY.getNeighbors(pos);
        for (int i = 0; i < loNeighX.size(); i++) {
            Double dVX = loNeighX.get(i).dAverage;
            Double dVY = loNeighY.get(i).dAverage;
            OrderedPair opVecPos = loNeighX.get(i).getCenter();
            if (dVX == null || dVY == null || opVecPos == null) {
                continue;
            }
            loReturn.add(new VelocityVec(dVX, dVY, opVecPos));
        }
        return loReturn;
    }

    public void AdjustColBar(List<VelocityVec> oVeloVecs) {
        try {
            EnumObject o = Sorting.getMaxCharacteristic(oVeloVecs, new Sorting.Characteristic() {

                @Override
                public Double getCharacteristicValue(Object pParameter) {
                    return ((VelocityVec) pParameter).opUnitTangent.dValue;
                }
            });
            oColBar = new Colorbar.StartEndLinearColorBar(0.0, o.dEnum * 1.1, loColors, new ColorSpaceCIEELab(), (Colorbar.StartEndLinearColorBar.ColorOperation<Double>) (Double pParameter) -> pParameter);
        } catch (EmptySetException ex) {
            Logger.getLogger(VelocityGrid.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setColors() {
        loColors = Colorbar.StartEndLinearColorBar.getCustom(0, 0, 0, 1, 1, 1);
        //oColBar = new Colorbar.StartEndLinearColorBar(0.0, 5.0, loColors, new ColorSpaceCIEELab(), (Colorbar.StartEndLinearColorBar.ColorOperation<Double>) (Double pParameter) -> pParameter);
    }

    public Colorbar getColorbar() {
        return oColBar;
    }

    public void writeAverage(String sFile) {

        OrderedPair[][] oaVeloX = GridVeloX.calcAverage();
        OrderedPair[][] oaVeloY = GridVeloY.calcAverage();
        List<String> lsOut = new ArrayList<>();

        lsOut.add("X,Y,VeloX,VeloY");
        for (int i = 0;
                i < oaVeloX.length;
                i++) {
            for (int j = 0; j < oaVeloX[0].length; j++) {
                if(oaVeloX[i][j] == null) continue;
                String sOut = oaVeloX[i][j].x + "," + oaVeloX[i][j].y + "," + oaVeloX[i][j].dValue + "," + oaVeloY[i][j].dValue;
                lsOut.add(sOut);
            }
        }

        Writer oWrite = new Writer(sFile);

        oWrite.write(lsOut);
    }

    public void writeVariance(String sFile) {

        OrderedPair[][] oaVarX = GridVeloX.calcVariance();
        OrderedPair[][] oaVarY = GridVeloY.calcVariance();
        List<String> lsOut = new ArrayList<>();

        lsOut.add("X,Y,VarX,VarY");
        for (int i = 0;
                i < oaVarX.length;
                i++) {
            for (int j = 0; j < oaVarX[0].length; j++) {
                String sOut = oaVarX[i][j].x + "," + oaVarX[i][j].y + "," + oaVarX[i][j].dValue + "," + oaVarY[i][j].dValue;
                lsOut.add(sOut);
            }
        }

        Writer oWrite = new Writer(sFile);
        oWrite.write(lsOut);
    }
    
    public void writeVariance(String sFile, VelocityGrid oAVG) {
        
        
        OrderedPair[][] oaVarX = GridVeloX.calcVariance(oAVG.GridVeloX.calcAverage());
        OrderedPair[][] oaVarY = GridVeloY.calcVariance(oAVG.GridVeloY.calcAverage());
        List<String> lsOut = new ArrayList<>();

        lsOut.add("X,Y,STDX,STDY");
        for (int i = 0;
                i < oaVarX.length;
                i++) {
            for (int j = 0; j < oaVarX[0].length; j++) {
                String sOut = oaVarX[i][j].x + "," + oaVarX[i][j].y + "," + oaVarX[i][j].dValue + "," + oaVarY[i][j].dValue;
                lsOut.add(sOut);
            }
        }

        Writer oWrite = new Writer(sFile);
        oWrite.write(lsOut);
    }
    

    public void writeCrossCorr(String sFile) {

        Value oValue2 = new Value() {

            @Override
            public Double getValue(Object pParameter) {
                VelocityVec o = (VelocityVec) pParameter;
                return o.getVelocityX();
            }
        };

        OrderedPair[][] oaCrossCorr = GridVeloY.calcCrossCorr(oValue2, GridVeloX.oWeight);        
        List<String> lsOut = new ArrayList<>();

        lsOut.add("X,Y,CrossCorr (Vx'Vy')");
        for (int i = 0;
                i < oaCrossCorr.length;
                i++) {
            for (int j = 0; j < oaCrossCorr[0].length; j++) {
                String sOut = oaCrossCorr[i][j].x + "," + oaCrossCorr[i][j].y + "," + oaCrossCorr[i][j].dValue;
                lsOut.add(sOut);
            }
        }

        Writer oWrite = new Writer(sFile);

        oWrite.write(lsOut);
    }
    
    public boolean isfilled(int iMinFilled){        
        for(int i = 0; i< GridVeloX.oCells.length; i++){
            for(int j = 0; j< GridVeloX.oCells[0].length; j++){
                if(GridVeloX.oCells[i][j].loContent.size() < iMinFilled || GridVeloY.oCells[i][j].loContent.size() < iMinFilled ){
                    return false;
                }
            }            
        }
        return true;
    }
    

}
