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
package com.tivconsultancy.opentiv.imageproc.shapes;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.NeighborIterator;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.math.interfaces.Normable;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Line3 implements Normable<MatrixEntry>, Serializable {

    private static final long serialVersionUID = 7419251794086924592L;

    public MatrixEntry meStart;
    public MatrixEntry meEnd;

    public double dSlope;
    public double dIntercept;
    public double dZero;
    public int iInterceptCompare;
    public int iSlopeCompare;

    public double dDistance;
    public double dAngle;

    double A;
    double B;
    double C;

    public int iDistance;
    public int iAngle;

    double dRoundConstant = 10.0;

    public int iSector = 1;

    public List<MatrixEntry> lmeSetLine = new ArrayList<>();

    public Line3(MatrixEntry meStart, MatrixEntry meEnd) {

        this.meStart = meStart;
        this.meEnd = meEnd;

        //Slope intercept form
        dSlope = (1.0 * (meEnd.i - meStart.i)) / (1.0 * (meEnd.j - meStart.j));
        dIntercept = 1.0 * meStart.i - dSlope * meStart.j;
        dZero = -1.0 * dIntercept / dSlope;

        iSector = (int) Math.signum(dZero);

        double y1 = dIntercept;
        double x2 = dZero;

        //Hessian normal form
        if (y1 == 0 && x2 == 0) {
            dDistance = 0;
        } else {
            dDistance = Math.abs(x2 * y1) / Math.sqrt(Math.pow(y1, 2) + Math.pow(x2, 2));
        }

//        dDistance = (1.0* (meEnd.j*meStart.i + meEnd.i*meStart.j)) / Math.sqrt( 1.0*((meEnd.i - meStart.i) * (meEnd.i - meStart.i)) + 1.0*((meEnd.j - meStart.j) * (meEnd.j - meStart.j)));                                        
        dAngle = Math.asin(dDistance / dIntercept);

        //General Form Ax+Bx+C = 0
//        
//        A = Math.cos(dAngle);
//        B = Math.sin(dAngle);
//        C = -1.0*dDistance;
        //Integer values
        iSlopeCompare = (int) (dSlope * dRoundConstant);
        iInterceptCompare = (int) (dIntercept * dRoundConstant);

        iDistance = (int) Math.round(dDistance);
        iAngle = (int) Math.round((dAngle / Math.PI) * 180.0);

    }

    public Line3(double dDistance, double dAngle, int iSector) {

        this.dDistance = dDistance;
        this.dAngle = dAngle;

        this.iSector = iSector;
        this.dIntercept = dDistance / Math.sin(dAngle);
        this.dZero = Math.sqrt((-1.0 * Math.pow(dIntercept, 2)) / (1.0 - (Math.pow(dIntercept, 2) / Math.pow(dDistance, 2)))) * Math.signum(iSector);

        this.dSlope = dIntercept / (0.0 - dZero);
    }

    public Line3(double dSlope, double dIntercept) {
        this.dSlope = dSlope;
        this.dIntercept = dIntercept;
        dZero = -1.0 * dIntercept / dSlope;

        iSector = (int) Math.signum(dZero);

        double y1 = dIntercept;
        double x2 = dZero;

        //Hessian normal form
        if (y1 == 0 && x2 == 0) {
            dDistance = 0;
        } else {
            dDistance = Math.abs(x2 * y1) / Math.sqrt(Math.pow(y1, 2) + Math.pow(x2, 2));
        }

        dAngle = Math.asin(dDistance / dIntercept);

        //General Form Ax+Bx+C = 0
//        
//        A = Math.cos(dAngle);
//        B = Math.sin(dAngle);
//        C = -1.0*dDistance;
        //Integer values
        iSlopeCompare = (int) (dSlope * dRoundConstant);
        iInterceptCompare = (int) (dIntercept * dRoundConstant);

        iDistance = (int) Math.round(dDistance);
        iAngle = (int) Math.round((dAngle / Math.PI) * 180.0);

    }

    public List<MatrixEntry> setLine(ImageInt oGrid, int iValue) {
        return setLine(oGrid, iValue, false);
    }

    public List<MatrixEntry> setLine(MatrixEntry upperLeft, MatrixEntry bottomRight, ImageInt oGrid, int iValue) {
        List<MatrixEntry> lmeLine = setLine(oGrid, iValue, true);
        List<MatrixEntry> lmeRemove = new ArrayList<>();
        Set1D intervalY = new Set1D(upperLeft.i, bottomRight.i);
        Set1D intervalX = new Set1D(upperLeft.j, bottomRight.j);
        for (MatrixEntry me : lmeLine) {
            boolean bInsideX = intervalX.isInside(me.j);
            boolean bInsideY = intervalY.isInside(me.i);
            if (!bInsideX || !bInsideY) {
                lmeRemove.add(me);
            }
        }
        lmeLine.removeAll(lmeRemove);
        return lmeLine;
    }

    public List<MatrixEntry> setLine(ImageInt oGrid, int iValue, boolean forceRedraw) {

        if (meStart != null && meEnd != null && !forceRedraw) {
            Line oLine = new Line(this.meStart, this.meEnd);
            if (iValue >= 0 && oGrid != null) {
                oLine.setLine(oGrid, iValue);
            }
            this.lmeSetLine = oLine.lmeLine;
            return oLine.lmeLine;
        }

        MatrixEntry meStartlocal;
        if (dIntercept > 0) {
            meStartlocal = new MatrixEntry((int) dIntercept, 0);
        } else {
            meStartlocal = new MatrixEntry(0, dZero);
        }

        double dX = oGrid.iaPixels[0].length - 1;
        double dY = oGrid.iaPixels.length - 1;
        MatrixEntry meEndlocal;
        if (dSlope > 0) {
            double dYCross = dSlope * dX + dIntercept;
            double dXCross = (dY - dIntercept) / dSlope;
            if (dYCross <= dY) {
                meEndlocal = new MatrixEntry(dYCross, dX);
            } else {
                meEndlocal = new MatrixEntry(dY, dXCross);
            }
        } else {
            meEndlocal = new MatrixEntry(0, dZero);
        }

        this.meStart = meStartlocal;
        this.meEnd = meEndlocal;
//        meEnd = new MatrixEntry(dSlope * dX + dIntercept, dX);
        Line oLine = new Line(meStartlocal, meEndlocal);
        if (iValue >= 0 && oGrid != null) {
            oLine.setLine(oGrid, iValue);
        }
        this.lmeSetLine = oLine.lmeLine;
        return oLine.lmeLine;
    }

    public static List<Line3> getLinesHoughTransformPositiveZeros(List<MatrixEntry> loEdgesPixels, int iLengthI, int iLengthJ, int iMinNorm, int iMaxNorm, int iMinCount, int iMinDistancebetweenPeaks) {

        int[][] iaHoughTransform = new int[(int) Math.ceil(Math.sqrt(iLengthI * iLengthI + iLengthJ * iLengthJ))][181];

//        for (int i = 0; i < loEdgesPixels.size(); i++) {
//            MatrixEntry oStart = loEdgesPixels.get(i);
//            for (int j = 0; j < loEdgesPixels.size(); j++) {
//                if (i == j) {
//                    continue;
//                }
//                MatrixEntry oEnd = loEdgesPixels.get(j);
//                double dNorm = oStart.getNorm(oEnd);
//                if (dNorm > iMinNorm && dNorm < iMaxNorm) {
//                    Line3 oPossibleLine = new Line3(oStart, oEnd);
//                    if (oPossibleLine.dZero >= 0) {
//                        iaHoughTransform[oPossibleLine.iDistance][oPossibleLine.iAngle + 90]++;
//                    }
//                }
//            }
//        }
        NeighborIterator.iterate(iLengthI, iLengthJ, loEdgesPixels, iMaxNorm, new NeighborIterator.Action() {
                             @Override
                             public void perform(MatrixEntry me1, MatrixEntry me2) {
                                 if (me1.equals(me2)) {
                                     return;
                                 }
                                 double dNorm = me1.getNorm(me2);
                                 if (dNorm > iMinNorm && dNorm < iMaxNorm) {
                                     Line3 oPossibleLine = new Line3(me1, me2);
                                     if (oPossibleLine.dZero >= 0) {
                                         iaHoughTransform[oPossibleLine.iDistance][oPossibleLine.iAngle + 90]++;
                                     }
                                 }
                             }
                         });

        List<MatrixEntry> lmeMax = getMax(iaHoughTransform, iMinCount, iMinDistancebetweenPeaks);
        List<Line3> loPossibleLines = new ArrayList<>();

        for (MatrixEntry meMax : lmeMax) {
            Line3 oMaxLine = new Line3(meMax.i, ((meMax.j - 90.0) / 180.0) * Math.PI, 1);
            loPossibleLines.add(oMaxLine);
        }

        return loPossibleLines;
    }

    public static List<Line3> getLinesHoughTransformNegativeZeros(List<MatrixEntry> loEdgesPixels, int iLengthI, int iLengthJ, int iMinNorm, int iMaxNorm, int iMinCount, int iMinDistancebetweenPeaks) {

        int[][] iaHoughTransform = new int[(int) Math.ceil(Math.sqrt(iLengthI * iLengthI + iLengthJ * iLengthJ))][181];

//        for (int i = 0; i < loEdgesPixels.size(); i++) {
//            MatrixEntry oStart = loEdgesPixels.get(i);
//            for (int j = 0; j < loEdgesPixels.size(); j++) {
//                if (i == j) {
//                    continue;
//                }
//                MatrixEntry oEnd = loEdgesPixels.get(j);
//                double dNorm = oStart.getNorm(oEnd);
//                if (dNorm > iMinNorm && dNorm < iMaxNorm) {
//                    Line3 oPossibleLine = new Line3(oStart, oEnd);
//                    if (oPossibleLine.dZero <= 0) {
//                        iaHoughTransform[oPossibleLine.iDistance][oPossibleLine.iAngle + 90]++;
//                    }
//                }
//            }
//        }
        NeighborIterator.iterate(iLengthI, iLengthJ, loEdgesPixels, iMaxNorm, new NeighborIterator.Action() {
                             @Override
                             public void perform(MatrixEntry me1, MatrixEntry me2) {

                                 if (me1.equals(me2)) {
                                     return;
                                 }
                                 double dNorm = me1.getNorm(me2);
                                 if (dNorm > iMinNorm && dNorm < iMaxNorm) {
                                     Line3 oPossibleLine = new Line3(me1, me2);
                                     if (oPossibleLine.dZero <= 0) {
                                         iaHoughTransform[oPossibleLine.iDistance][oPossibleLine.iAngle + 90]++;
                                     }
                                 }
                             }
                         });

        List<MatrixEntry> lmeMax = getMax(iaHoughTransform, iMinCount, iMinDistancebetweenPeaks);
        List<Line3> loPossibleLines = new ArrayList<>();

        for (MatrixEntry meMax : lmeMax) {
            Line3 oMaxLine = new Line3(meMax.i, ((meMax.j - 90.0) / 180.0) * Math.PI, -1);
            loPossibleLines.add(oMaxLine);
        }

        return loPossibleLines;
    }

    public static List<MatrixEntry> getMax(int[][] iaInput, int iMin, int iOrder) {
        List<MatrixEntry> lmeMax = new ArrayList<>();
        for (int i = 1; i < iaInput.length; i++) {
            for (int j = 1; j < iaInput[0].length; j++) {
                if (iaInput[i][j] > iMin) {
                    boolean bLocalMax = true;
                    internal:
                    for (int t = (i - iOrder < 1 ? 1 : i - iOrder); t < (i + iOrder > iaInput.length ? iaInput.length : i + iOrder); t++) {
                        for (int z = (j - iOrder < 1 ? 1 : j - iOrder); z < (j + iOrder > iaInput[0].length ? iaInput[0].length : j + iOrder); z++) {
                            if (t == i && j == z) {
                                continue;
                            }
                            if (iaInput[i][j] < iaInput[t][z]) {
                                bLocalMax = false;
                                break internal;
                            }
                        }
                    }
                    if (!bLocalMax) {
                        continue;
                    }
                    lmeMax.add(new MatrixEntry(i, j, iaInput[i][j]));
                }
            }
        }
        return lmeMax;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Line3) {
            Line3 oLineCompare = (Line3) obj;
            if (this.iInterceptCompare != 0 && this.iSlopeCompare != 0) {
                if (this.iInterceptCompare == oLineCompare.iInterceptCompare && this.iSlopeCompare == oLineCompare.iSlopeCompare) {
                    return true;
                }
            }
            if (this.dZero == oLineCompare.dZero && this.dSlope == oLineCompare.dSlope) {
                return true;
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.iInterceptCompare;
        hash = 97 * hash + this.iSlopeCompare;
        return hash;
    }

    @Override
    public Double getNorm(MatrixEntry o2) {
        double y0 = o2.i;
        double y1 = meStart.i;
        double y2 = meEnd.i;

        double x0 = o2.j;
        double x1 = meStart.j;
        double x2 = meEnd.j;

        return Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1) / Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

    }

    @Override
    public Double getNorm2(MatrixEntry o2, String sNormType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String toString() {
        double dLength = meStart.getNorm(meEnd);
        return dLength + "," + meStart.i + "," + meStart.j + "," + meEnd.i + "," + meEnd.j;
    }

}
