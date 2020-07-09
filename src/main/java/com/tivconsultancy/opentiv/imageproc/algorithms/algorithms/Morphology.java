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
package com.tivconsultancy.opentiv.imageproc.algorithms.algorithms;


import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.contours.CPX;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.imageproc.shapes.Line2;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition2;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Morphology implements Serializable{

    private static final long serialVersionUID = 433814034743056550L;

    public final List<ImagePoint> loHelp = new ArrayList<>();
    public List<List<ImagePoint>> lloHelp = new ArrayList<>();

    public final List<MatrixEntry> lmHelp = new ArrayList<>();
    public List<List<MatrixEntry>> llmHelp = new ArrayList<>();

    public static void dilatation(ImageGrid oInput) {
        oInput.resetMarkers();
        for (ImagePoint o : oInput.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                o.bMarker = true;
                List<ImagePoint> loN8 = oInput.getNeighborsN8(o);
                for (ImagePoint oN8 : loN8) {
                    if (oN8 == null) {
                        continue;
                    }
                    oN8.iValue = 255;
                    oN8.bMarker = true;
                }
            }
        }
        oInput.resetMarkers();
    }

    public static void dilatation(ImageInt oInput) {
        oInput.resetMarkers();
        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                if (oInput.iaPixels[i][j] > 127 && !oInput.baMarker[i][j]) {
                    oInput.baMarker[i][j] = true;
                    oInput.setNeighborsN8(i, j, 255);
                    oInput.setNeighborsN8(i, j, true);
                }
            }
        }

        oInput.resetMarkers();
    }

    public static void erosion(ImageGrid oInput) {
        oInput.resetMarkers();
        for (ImagePoint o : oInput.oa) {
            if (o.iValue < 127 && !o.bMarker) {
                o.bMarker = true;
                List<ImagePoint> loN8 = oInput.getNeighborsN8(o);
                for (ImagePoint oN8 : loN8) {
                    if (oN8 == null) {
                        continue;
                    }
                    oN8.iValue = 0;
                    oN8.bMarker = true;
                }
            }
        }
        oInput.resetMarkers();
    }

    public static void erosion(ImageInt oInput) {
        oInput.resetMarkers();
        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                if (oInput.iaPixels[i][j] < 127 && !oInput.baMarker[i][j]) {
                    oInput.baMarker[i][j] = true;
                    oInput.setNeighborsN8(i, j, 0);
                    oInput.setNeighborsN8(i, j, true);
                }
            }
        }
        oInput.resetMarkers();
    }

    public List<ImagePoint> markFillN4(ImageGrid oInput, ImagePoint oStart) {
        List<ImagePoint> loReturn = new ArrayList<>();
        loHelp.clear();
        loHelp.add(oStart);
        oStart.bMarker = true;
        while (!loHelp.isEmpty()) {
            ImagePoint o = loHelp.get(0);
            loReturn.add(o);
            loHelp.remove(o);
            for (ImagePoint oN4 : oInput.getNeighborsN4(o)) {
                if (oN4 != null && !oN4.bMarker && oN4.iValue < 127) {
                    oN4.bMarker = true;
                    loHelp.add(oN4);
                }
            }
        }
        return loReturn;
    }

    public List<ImagePoint> markFillN8(ImageGrid oInput, ImagePoint oStart) {
        List<ImagePoint> loReturn = new ArrayList<>();
        loHelp.clear();
        loHelp.add(oStart);
        oStart.bMarker = true;
        while (!loHelp.isEmpty()) {
            ImagePoint o = loHelp.get(0);
            loReturn.add(o);
            loHelp.remove(o);
            for (ImagePoint oN8 : oInput.getNeighborsN8(o)) {
                if (oN8 != null && !oN8.bMarker && oN8.iValue < 127) {
                    oN8.bMarker = true;
                    loHelp.add(oN8);
                }
            }
        }
        return loReturn;
    }
    

    public List<MatrixEntry> markFillN4(ImageInt oInput, int i, int j) {
        if (!oInput.isInside(i, j) || oInput.iaPixels[i][j] >= 127) {
            return new ArrayList<>();
        }
        List<MatrixEntry> loReturn = new ArrayList<>();
        lmHelp.clear();
        lmHelp.add(new MatrixEntry(i, j));
        oInput.baMarker[i][j] = true;
        while (!lmHelp.isEmpty()) {
            MatrixEntry o = lmHelp.get(0);
            lmHelp.remove(o);
            loReturn.add(o);
//            oInput.setNeighborsN4(o.i, o.j, true, new SideCondition<MatrixEntry>() {
//
//                @Override
//                public boolean check(MatrixEntry pParameter) {
//                    return oInput.iaPixels[pParameter.i][pParameter.j] < 127 && oInput.baMarker[pParameter.i][pParameter.j];
//                }
//            });

//            List<MatrixEntry> lmeNeigh = oInput.getNeighborsN4(o.i, o.j, new SideCondition<MatrixEntry>() {
//
//                @Override
//                public boolean check(MatrixEntry pParameter) {
//                    return oInput.iaPixels[pParameter.i][pParameter.j] < 127 && oInput.baMarker[pParameter.i][pParameter.j];
//                }
//            });
            for (MatrixEntry me : oInput.getNeighborsN4(o.i, o.j)) {
                if (me != null && !oInput.baMarker[me.i][me.j] && oInput.iaPixels[me.i][me.j] < 127) {
                    oInput.baMarker[me.i][me.j] = true;
                    lmHelp.add(me);
                }
            }
        }
        return loReturn;
    }
    
    public List<MatrixEntry> markFillN8(ImageInt oInput, int i, int j, SideCondition<MatrixEntry> sc) {
        if (!oInput.isInside(i, j)) {
            return new ArrayList<>();
        }
        List<MatrixEntry> loReturn = new ArrayList<>();
        lmHelp.clear();
        lmHelp.add(new MatrixEntry(i, j));
        oInput.baMarker[i][j] = true;
        while (!lmHelp.isEmpty()) {
            MatrixEntry o = lmHelp.get(0);
            lmHelp.remove(o);
            loReturn.add(o);
            for (MatrixEntry me : oInput.getNeighborsN8(o.i, o.j)) {
                if (me != null && !oInput.baMarker[me.i][me.j] && sc.check(me)) {
                    oInput.baMarker[me.i][me.j] = true;
                    lmHelp.add(me);
                }
            }
        }
        return loReturn;
    }
    
    public List<MatrixEntry> markFillN8(ImageInt oInput, int i, int j) {
        if (!oInput.isInside(i, j) || oInput.iaPixels[i][j] >= 127) {
            return new ArrayList<>();
        }
        List<MatrixEntry> loReturn = new ArrayList<>();
        lmHelp.clear();
        lmHelp.add(new MatrixEntry(i, j));
        oInput.baMarker[i][j] = true;
        while (!lmHelp.isEmpty()) {
            MatrixEntry o = lmHelp.get(0);
            lmHelp.remove(o);
            loReturn.add(o);
//            oInput.setNeighborsN4(o.i, o.j, true, new SideCondition<MatrixEntry>() {
//
//                @Override
//                public boolean check(MatrixEntry pParameter) {
//                    return oInput.iaPixels[pParameter.i][pParameter.j] < 127 && oInput.baMarker[pParameter.i][pParameter.j];
//                }
//            });

//            List<MatrixEntry> lmeNeigh = oInput.getNeighborsN4(o.i, o.j, new SideCondition<MatrixEntry>() {
//
//                @Override
//                public boolean check(MatrixEntry pParameter) {
//                    return oInput.iaPixels[pParameter.i][pParameter.j] < 127 && oInput.baMarker[pParameter.i][pParameter.j];
//                }
//            });
            for (MatrixEntry me : oInput.getNeighborsN8(o.i, o.j)) {
                if (me != null && !oInput.baMarker[me.i][me.j] && oInput.iaPixels[me.i][me.j] < 127) {
                    oInput.baMarker[me.i][me.j] = true;
                    lmHelp.add(me);
                }
            }
        }
        return loReturn;
    }
    

    public List<List<ImagePoint>> markFillN4(ImageGrid oInput, List<ImagePoint> loStart) {
        List<List<ImagePoint>> loReturn = new ArrayList<>();
        int iCount = 0;
        for (ImagePoint oStart : loStart) {
            loReturn.add(new ArrayList<>());
            lloHelp.add(new ArrayList<>());
            lloHelp.get(iCount).add(oStart);
            oStart.bMarker = true;
            iCount++;
        }

        while (true) {
            iCount = 0;
            boolean bStop = true;
            for (List<ImagePoint> lo : lloHelp) {
                if (lo.isEmpty()) {
                    continue;
                }
                bStop = false;
                ImagePoint o = lo.get(0);
                loReturn.get(iCount).add(o);
                lo.remove(o);
                for (ImagePoint oN4 : oInput.getNeighborsN4(o)) {
                    if (oN4 != null && !oN4.bMarker) {
                        oN4.bMarker = true;
                        lo.add(oN4);
                    }
                }
                iCount++;
            }
            if (bStop) {
                break;
            }
        }
        return loReturn;
    }

    public static void markEdgesBinarizeImage(ImageGrid oInput) {

        for (ImagePoint o : oInput.oa) {
            if (o.iValue > 127) {
                List<ImagePoint> loN8 = oInput.getNeighborsN8(o);
                boolean bEdge = false;
                for (ImagePoint oN8 : loN8) {
                    if (oN8 != null && oN8.iValue < 127) {
                        bEdge = true;
                        break;
                    }
                }
                if (bEdge) {
                    o.bMarker = true;
                }
            }
        }
    }

    public static void markEdgesBinarizeImage(ImageInt oInput) {

        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                if (oInput.iaPixels[i][j] > 127) {
                    List<MatrixEntry> loN8 = oInput.getNeighborsN8(i, j);
                    boolean bEdge = false;
                    for (MatrixEntry oN8 : loN8) {
                        if (oN8 != null && oInput.iaPixels[oN8.i][oN8.j] < 127) {
                            bEdge = true;
                            break;
                        }
                    }
                    if (bEdge) {
                        oInput.baMarker[i][j] = true;
                    }
                }
            }
        }
    }

    public static void setmarkedPoints(ImageGrid oInput, int iValue, SideCondition<ImagePoint> oSC) {
        for (ImagePoint o : oInput.oa) {
            if (oSC.check(o)) {
                o.iValue = iValue;
            }
        }
    }

    public static void setNotMarkedPoints(ImageGrid oInput, int iValue) {
        for (ImagePoint o : oInput.oa) {
            if (!o.bMarker) {
                o.iValue = iValue;
            }
        }
    }

    public static void setmarkedPoints(ImageInt oInput, int iValue, SideCondition<MatrixEntry> oSC) {
        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                if (oSC.check(new MatrixEntry(i, j))) {
                    oInput.iaPixels[i][j] = iValue;
                }
            }
        }
    }

    public static void setmarkedPoints(ImageInt oInput, int iValue) {
        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                if (oInput.baMarker[i][j]) {
                    oInput.iaPixels[i][j] = iValue;
                }
            }
        }
    }

    public static void setNotMarkedPoints(ImageInt oInput, int iValue) {
        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                if (!oInput.baMarker[i][j]) {
                    oInput.iaPixels[i][j] = iValue;
                }
            }
        }
    }

    public static List<ImagePoint> connectCPX(CPX o1, CPX o2, ImagePoint pConnect1, ImagePoint pConnect2, ImageGrid oGrid) {

        int iStart = o1.lo.indexOf(pConnect1);
        int iEnd = o2.lo.indexOf(pConnect2);

        if (iStart < 0 || iEnd < 0) {
            return new ArrayList<>();
        }

        Double dX = pConnect1.getPos().x - pConnect2.getPos().x;
        Double dY = pConnect1.getPos().y - pConnect2.getPos().y;

        if (Math.abs(dX) < 2 || Math.abs(dY) < 2) {
            Line2 oLine = new Line2(pConnect1, pConnect2);
            return oLine.lmeLine;
        }

        //Directional of Start
        OrderedPair opAbbr1 = o1.getDerivationOutwardsEndPoints(pConnect1, 2);
        double dX1 = opAbbr1.x;
        double dY1 = opAbbr1.y;

        //Directional of End
        OrderedPair opAbbr2 = o2.getDerivationOutwardsEndPoints(pConnect2, 2);
        double dX2 = opAbbr2.x;
        double dY2 = opAbbr2.y;

        Double dCutY = (pConnect1.getPos().y + pConnect2.getPos().y) / 2.0;
        Double dCutX = (pConnect1.getPos().x + pConnect2.getPos().x) / 2.0;
        boolean bRotate = false;
        double[] x = new double[3];
        double[] y = new double[3];

        if (Math.abs(dX) > Math.abs(dY)) {

            if (Math.abs(dX1) > Math.abs(dX2)) {
                Double dDistX = Math.abs(pConnect1.getPos().x - dCutX);
                dCutY = dCutY + dY1 * dDistX;

            } else {
                Double dDistX = Math.abs(pConnect2.getPos().x - dCutX);
                dCutY = dCutY + dY2 * dDistX;

            }

            if (pConnect2.getPos().x > pConnect1.getPos().x) {
                x = new double[]{pConnect1.getPos().x, dCutX, pConnect2.getPos().x};
                y = new double[]{pConnect1.getPos().y, dCutY, pConnect2.getPos().y};
            } else {
                x = new double[]{pConnect2.getPos().x, dCutX, pConnect1.getPos().x};
                y = new double[]{pConnect2.getPos().y, dCutY, pConnect1.getPos().y};
            }

        } else {
            bRotate = true;
            if (Math.abs(dY1) > Math.abs(dY2)) {
                Double dDistY = Math.abs(pConnect1.getPos().y - dCutY);
                dCutX = dCutX + dX1 * dDistY;
            } else {
                Double dDistY = Math.abs(pConnect2.getPos().y - dCutY);
                dCutX = dCutX + dX2 * dDistY;
            }

            if (pConnect2.getPos().y > pConnect1.getPos().y) {
                y = new double[]{pConnect1.getPos().x, dCutX, pConnect2.getPos().x};
                x = new double[]{pConnect1.getPos().y, dCutY, pConnect2.getPos().y};
            } else {
                y = new double[]{pConnect2.getPos().x, dCutX, pConnect1.getPos().x};
                x = new double[]{pConnect2.getPos().y, dCutY, pConnect1.getPos().y};
            }

        }

        SplineInterpolator o = new SplineInterpolator();
        PolynomialSplineFunction oSpline = o.interpolate(x, y);

        List<ImagePoint> loSplinePoints = new ArrayList<>();

        if (!bRotate) {
            if (pConnect2.getPos().x < pConnect1.getPos().x) {
                for (int iXIter = (int) pConnect2.getPos().x; iXIter <= pConnect1.getPos().x; iXIter++) {
                    int dYSplien = (int) oSpline.value(iXIter);
                    loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                }
            } else {
                for (int iXIter = (int) pConnect1.getPos().x; iXIter <= pConnect2.getPos().x; iXIter++) {
                    int dYSplien = (int) oSpline.value(iXIter);
                    loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                }
            }
        } else {
            if (pConnect2.getPos().y < pConnect1.getPos().y) {
                for (int iYIter = (int) pConnect2.getPos().y; iYIter <= pConnect1.getPos().y; iYIter++) {
                    int dXSplien = (int) oSpline.value(iYIter);
                    loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                }
            } else {
                for (int iYIter = (int) pConnect1.getPos().y; iYIter <= pConnect2.getPos().y; iYIter++) {
                    int dXSplien = (int) oSpline.value(iYIter);
                    loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                }
            }
        }

        List<ImagePoint> loPointsToSet = new ArrayList<>();

        for (int iST = 0; iST < loSplinePoints.size() - 1; iST++) {
            if (loSplinePoints.get(iST).getNorm(loSplinePoints.get(iST + 1)) > 1.5) {
                Line2 oLine = new Line2(loSplinePoints.get(iST), loSplinePoints.get(iST + 1));
                loPointsToSet.addAll(oLine.lmeLine);
            } else {
                loPointsToSet.add(loSplinePoints.get(iST));
            }
        }

        return loPointsToSet;
    }

    public static void removeSinglePoints(ImageGrid oGrid) {
        for (ImagePoint o : oGrid.oa) {
            if ((new N8(oGrid, o)).getBP() == 0) {
                oGrid.setPoint(o, 0);
            }
        }
    }

    public static void removeSinglePoints(ImageInt oInput) {
        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                if ((new N8(oInput, i, j)).getBP() == 0) {
                    oInput.iaPixels[i][j] = 0;
                }
            }
        }
    }

}
