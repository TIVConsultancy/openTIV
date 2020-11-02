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
package com.tivconsultancy.opentiv.imageproc.contours;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EdgeDetections;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.N8;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018.CNCP;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.imageproc.shapes.Line2;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.functions.PLF;
import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 *
 * @author Thomas Ziegenhein
 */
public class CPX implements Normable<CPX>, Serializable {

    private static final long serialVersionUID = -8523294306720673934L;

    public ArrayList<ImagePoint> lo = new ArrayList<>();
    public ImagePoint oStart = null;
    public ImagePoint oEnd = null;
    public PLF oPLF = null;
    public boolean bMarker = false;

    public CPX(){
        
    }
    
    public CPX(ImagePoint oStart) throws EmptySetException {
        this.oStart = oStart;
        CNCP o = Ziegenhein_2018.getCNCPToStartingPoint(oStart);
        this.lo = o.lo;
        this.oEnd = o.oEnd;
    }

    public void addPoint(ImagePoint o) {
        lo.add(o);
    }

    public void closeCNCP(ImagePoint oEnd) {
        this.oEnd = oEnd;
    }

    public List<ImagePoint> getPoints() {
        List<ImagePoint> loPoints = new ArrayList<>();
        loPoints.add(oStart);
        loPoints.addAll(lo);
        return loPoints;
    }
    
    public List<MatrixEntry> getPointsME() {        
        List<MatrixEntry> loPoints = new ArrayList<>();
        loPoints.add(oStart.getME());
        for(ImagePoint op : lo){
            loPoints.add(op.getME());
        }        
        return loPoints;
    }        

    public static boolean checkIfStart(ImagePoint o) {
        N8 oN8 = new N8(o.getGrid(), o);
        if (oN8.getBP() == 1
                || (oN8.getBP() == 2 && oN8.getC2P() == 1)) {
            return true;
        }
        return false;
    }

    public boolean isClosedContour() {
        if (oEnd == null || oStart == null) {
            return false;
        }
        return oEnd.equals(oStart);
    }

    public static ImageGrid setOnGrid(ImageGrid oGrid, Collection<CPX> oCNCPs, int iValue) {
        List<ImagePoint> lo = new ArrayList<>();
        for (CPX p : oCNCPs) {
            lo.addAll(p.lo);
        }
        oGrid.setPoint(lo, iValue);
        return oGrid;
    }

    public ImagePoint getFocalPoint() {
        double dX = 0.0;
        double dY = 0.0;
        int iCount = 0;
        for (ImagePoint o : this.lo) {
            dX = dX + o.getPos().x;
            dY = dY + o.getPos().y;
            iCount++;
        }
        if (iCount > 0) {
            return new ImagePoint((int) (dX / iCount), (int) (dY / iCount), 255, this.oStart.getGrid());
        } else {
            return null;
        }
    }

    public static ImagePoint getFocalPoint(List<ImagePoint> lo) {
        double dX = 0.0;
        double dY = 0.0;
        int iCount = 0;
        for (ImagePoint o : lo) {
            dX = dX + o.getPos().x;
            dY = dY + o.getPos().y;
            iCount++;
        }
        if (iCount > 0) {
            return new ImagePoint((int) (dX / iCount), (int) (dY / iCount), 255, lo.get(0).getGrid());
        } else {
            return null;
        }
    }

    public OrderedPair getDerivationOutwardsEndPoints(ImagePoint oRef, int iLength) {

        if (oRef.equals(this.oStart)) {
            //Directional of Start
            double dX1 = 0;
            double dY1 = 0;
            int iCount = 0;
            for (int i = Math.min(lo.size() - iLength, iLength); i >= 1; i--) {
                dX1 = dX1 + (lo.get(i - 1).getPos().x - lo.get(i).getPos().x);
                dY1 = dY1 + (lo.get(i - 1).getPos().y - lo.get(i).getPos().y);
                iCount++;
            }
            dX1 = dX1 + (oStart.getPos().x - lo.get(0).getPos().x);
            dY1 = dY1 + (oStart.getPos().y - lo.get(0).getPos().y);
            iCount++;
            dX1 = dX1 / (1.0 * (iCount));
            dY1 = dY1 / (1.0 * (iCount));
            return new OrderedPair(dX1, dY1);
            //Directional of End
        } else if (oRef.equals(this.oEnd)) {
            int iLeft = Math.max(0, lo.size() - iLength);
            int iRight = this.lo.size() - 1;
            int iCount = 0;
            double dX = 0;
            double dY = 0;
            for (int i = iLeft; i < iRight; i++) {
                dX = dX + (lo.get(i + 1).getPos().x - lo.get(i).getPos().x);
                dY = dY + (lo.get(i + 1).getPos().y - lo.get(i).getPos().y);
                iCount++;
            }
            dX = dX / (1.0 * (iCount));
            dY = dY / (1.0 * (iCount));
            return new OrderedPair(dX, dY);
        } else {
            throw new UnsupportedOperationException("Input is not end or start point");
        }
    }

    public OrderedPair getUnitTangent(ImagePoint o, int iLength) {
        double dX = 0;
        double dY = 0;
        int iCount = 0;
        for (int i = Math.max(this.lo.indexOf(o) - iLength, 0); i < Math.min(this.lo.indexOf(o) + iLength, lo.size() - 1); i++) {
            dX = dX + (lo.get(i).getPos().x - lo.get(i + 1).getPos().x) / lo.get(i).getPos().getNorm(lo.get(i + 1).getPos());
            dY = dY + (lo.get(i).getPos().y - lo.get(i + 1).getPos().y) / lo.get(i).getPos().getNorm(lo.get(i + 1).getPos());
            iCount++;
        }
        if (iCount == 0) {
            return null;
        }
        dX = dX / (1.0 * iCount);
        dY = dY / (1.0 * iCount);
        return new OrderedPair(dX, dY, dY / dX);
    }

    public Double getSlope(ImagePoint o, int iLength) {
        double dX = 0;
        double dY = 0;
        int iCount = 0;
        for (int i = Math.max(this.lo.indexOf(o) - iLength, 0); i < Math.min(this.lo.indexOf(o) + iLength, lo.size() - 1); i++) {
            dX = dX + (lo.get(i).getPos().x - lo.get(i + 1).getPos().x);
            dY = dY + (lo.get(i).getPos().y - lo.get(i + 1).getPos().y);
            iCount++;
        }
        if (iCount == 0) {
            return null;
        }
        dX = dX / (1.0 * iCount);
        dY = dY / (1.0 * iCount);
        return dY / dX;
    }

    public void closeEndPointsSmooth(int iLength, int iValueOnGrid, ImageGrid oGrid) {

        if (oGrid == null) {
            oGrid = oStart.getGrid();
        }

        Double distX = oStart.getPos().x - oEnd.getPos().x;
        Double distY = oStart.getPos().y - oEnd.getPos().y;

        if (Math.abs(distX) < 4 || Math.abs(distY) < 4) {
            Line2 oLine = new Line2(oStart, oEnd);
            oLine.setLine(oGrid, iValueOnGrid);
            return;
        }

        //Directional of Start
        double dX1 = getDerivationOutwardsEndPoints(oStart, iLength).x;
        double dY1 = getDerivationOutwardsEndPoints(oStart, iLength).y;

        double dX2 = getDerivationOutwardsEndPoints(oEnd, iLength).x;
        double dY2 = getDerivationOutwardsEndPoints(oEnd, iLength).y;

        System.out.println(oStart.getPos());
        Double dCutY = (oStart.getPos().y + oEnd.getPos().y) / 2.0;
        Double dCutX = (oStart.getPos().x + oEnd.getPos().x) / 2.0;
        boolean bRotate = false;
        double[] x = new double[3];
        double[] y = new double[3];

        if (Math.abs(distX) > Math.abs(distY)) {

            if (Math.abs(dY1) > Math.abs(dY2)) {
                Double dDistX = Math.abs(oStart.getPos().x - dCutX);
                dCutY = dCutY + dY1 * dDistX / 2.0;

            } else {
                Double dDistX = Math.abs(oEnd.getPos().x - dCutX);
                dCutY = dCutY + dY2 * dDistX / 2.0;

            }

            if (oEnd.getPos().x > oStart.getPos().x) {
                x = new double[]{oStart.getPos().x, dCutX, oEnd.getPos().x};
                y = new double[]{oStart.getPos().y, dCutY, oEnd.getPos().y};
            } else {
                x = new double[]{oEnd.getPos().x, dCutX, oStart.getPos().x};
                y = new double[]{oEnd.getPos().y, dCutY, oStart.getPos().y};
            }

        } else {
            bRotate = true;
            if (Math.abs(dX1) > Math.abs(dX2)) {
                Double dDistY = Math.abs(oStart.getPos().y - dCutY);
                dCutX = dCutX + dX1 * dDistY / 2.0;
            } else {
                Double dDistY = Math.abs(oEnd.getPos().y - dCutY);
                dCutX = dCutX + dX2 * dDistY / 2.0;
            }

            if (oEnd.getPos().y > oStart.getPos().y) {
                y = new double[]{oStart.getPos().x, dCutX, oEnd.getPos().x};
                x = new double[]{oStart.getPos().y, dCutY, oEnd.getPos().y};
            } else {
                y = new double[]{oEnd.getPos().x, dCutX, oStart.getPos().x};
                x = new double[]{oEnd.getPos().y, dCutY, oStart.getPos().y};
            }

        }

        SplineInterpolator o = new SplineInterpolator();
        PolynomialSplineFunction oSpline = o.interpolate(x, y);

        List<ImagePoint> loSplinePoints = new ArrayList<>();

        if (!bRotate) {
            if (oEnd.getPos().x < oStart.getPos().x) {
                for (int iXIter = (int) oEnd.getPos().x; iXIter <= oStart.getPos().x; iXIter++) {
                    int dYSplien = (int) oSpline.value(iXIter);
                    loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                }
            } else {
                for (int iXIter = (int) oStart.getPos().x; iXIter <= oEnd.getPos().x; iXIter++) {
                    int dYSplien = (int) oSpline.value(iXIter);
                    loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                }
            }
        } else {
            if (oEnd.getPos().y < oStart.getPos().y) {
                for (int iYIter = (int) oEnd.getPos().y; iYIter <= oStart.getPos().y; iYIter++) {
                    int dXSplien = (int) oSpline.value(iYIter);
                    loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                }
            } else {
                for (int iYIter = (int) oStart.getPos().y; iYIter <= oEnd.getPos().y; iYIter++) {
                    int dXSplien = (int) oSpline.value(iYIter);
                    loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                }
            }
        }

        HashSet<ImagePoint> loPointsToSet = new HashSet<>();

        for (int iST = 0; iST < loSplinePoints.size() - 1; iST++) {
            if (loSplinePoints.get(iST).getNorm(loSplinePoints.get(iST + 1)) > 1.5) {
                Line2 oLine = new Line2(loSplinePoints.get(iST), loSplinePoints.get(iST + 1));
                loPointsToSet.addAll(oLine.lmeLine);
            } else {
                loPointsToSet.add(loSplinePoints.get(iST));
            }
        }

        oGrid.setPoint(loPointsToSet, iValueOnGrid);

    }
    
    public List<Double> getEdgeStrength(ImageGrid oSource){        
        List<Double> loReturn = new ArrayList<>();
        for(ImagePoint op : lo){
            loReturn.add(EdgeDetections.sobelOperator(oSource, op));                        
        }
        return loReturn;
    }

    public Double getCurv(int i, int iCurvOrder, int iTangentOrder) {
        List<OrderedPair> lopHelp = new ArrayList<>();
        if (this.oPLF == null) {
            for (ImagePoint op : this.lo) {
                lopHelp.add(op.getPos());
            }
            this.oPLF = new PLF(lopHelp);
        }
        return oPLF.getCurvCentral(iCurvOrder, i, iTangentOrder).dValue;
    }

    public boolean islocallyconvex(int iJump, int iPos, ImageGrid oArea) {
        int iLeft = Math.max(iPos - (iJump / 2), 0);
        int iRight = Math.min(iPos + (iJump / 2), this.lo.size() - 1);

        List<ImagePoint> loLocal = this.lo.subList(iLeft, iRight);
        
        ImagePoint oFocalLocal = getFocalPoint(loLocal);
        
        for(ImagePoint o : oArea.oa){
            if(o.bMarker){
                if(oFocalLocal.getPos().equals(o.getPos(), new Comparison<OrderedPair>() {

                    @Override
                    public int compare(OrderedPair o1, OrderedPair o2) {
                        if(o1.x == o2.x && o1.y == o2.y) return 0;
                        return 1;
                    }
                })) return true;
            }            
        }
        return false;        

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CPX)) {
            return false;
        }

        boolean bStart = false;
        boolean bEnd = false;
        boolean blo = true;
        CPX oComp = (CPX) obj;

        if (this.oStart != null && oComp.oStart != null) {
            if (this.oStart.equals(((CPX) obj).oStart)) {
                bStart = true;
            }
        } else {
            if (this.oStart == null && oComp.oStart == null) {
                bStart = true;
            }
        }

        if (this.oEnd != null && oComp.oEnd != null) {
            if (this.oEnd.equals(((CPX) obj).oEnd)) {
                bEnd = true;
            }
        } else {
            if (this.oEnd == null && oComp.oEnd == null) {
                bEnd = true;
            }
        }
        
        if (this.lo != null && oComp.lo != null) {
            if (this.lo.size() == oComp.lo.size()) {
                blo = true;
                for (ImagePoint oOuter : this.lo) {
                    if(!oComp.lo.contains(oOuter)){
                        blo=false;
                        break;
                    }
//                    for (ImagePoint oInner : oComp.lo) {
//                        if (!oOuter.equals(oInner)) {
//                            blo = true;
//                        }
//                    }
                }
            } else {
                blo = false;
            }
        } else {
            if (!(this.lo == null && oComp.lo == null)) {
                blo = false;
            }
        }

        return bStart && bEnd && blo;

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.lo);
        hash = 97 * hash + Objects.hashCode(this.oStart);
        hash = 97 * hash + Objects.hashCode(this.oEnd);
        return hash;
    }
    
    public OrderedPair getPosition(){
        OrderedPair oPosRef = new OrderedPair(oStart.getPos());
        int iCount = 0;
        for(ImagePoint o : this.lo){
            oPosRef.add(o.getPos());
            iCount++;
        }
        if(iCount == 0) return oStart.getPos();
        return oPosRef.mult(1.0 /(double) iCount);
    }

    @Override
    public Double getNorm(CPX o2) {
        Double dDist = 0.0;
        int iCount = 0;
        for(ImagePoint o : lo){
            try {
                dDist = dDist + ((EnumObject) Sorting.getMinCharacteristic(o2.lo, o, new Sorting.Characteristic2<ImagePoint>() {
                    
                    @Override
                    public Double getCharacteristicValue(ImagePoint pParameter, ImagePoint pParameter2) {
                        return pParameter.getPos().getNorm(pParameter2.getPos());
                    }
                })).dEnum;
                iCount++;
            } catch (EmptySetException ex) {
                Logger.getLogger(CPX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(iCount == 0) return null;
        return dDist / ((double) iCount);
    }

    @Override
    public Double getNorm2(CPX o2, String sNormType) {
        return this.getPosition().getNorm(o2.getPosition());
    }

}
