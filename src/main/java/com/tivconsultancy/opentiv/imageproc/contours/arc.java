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
//
//import Fundamentals.Entries;

import java.io.Serializable;

//import Fundamentals.Matrix;
//import Fundamentals.OrderdPair;
//import Fundamentals.Vector;
//import InOut.Writer;
//import SortRules.ValueOrderedPairComparator;
//import SortRules.ValueOrderedPairComparatorWithY;
//import java.io.File;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import utilities.MatrixEntry;
//import utilities.MatrixGenerator;
//
///**
// *
// * @author Thomas Ziegenhein
// */
public class arc implements Serializable {
    

//
//    public List<MatrixEntry> lmeSorted;
//    private final double dMetric = 1.0;
//
//    public arc() {
//
//    }
//
//    public arc(List<MatrixEntry> lme, boolean bClosedCurve) {
//        MatrixEntry meStart;
//        if (bClosedCurve) {
//            meStart = getLeftestPoint(lme);
//        } else {
//            meStart = getLeftStart(lme);
//        }
//
//        lmeSorted = new ArrayList<MatrixEntry>();
////        lmeSorted.add(meStart);
////        lme.remove(meStart);
//
//        MatrixEntry meNextConnected = getNextConnectedPositive(meStart, lme);
//        while (meNextConnected != null) {
//            lmeSorted.add(meNextConnected);
//            lme.remove(meNextConnected);
//            meNextConnected = getNextConnectedPositive(meNextConnected, lme);
//        }
//        if (lmeSorted.isEmpty()) {
//            lme.remove(meStart);
//        }
//
//    }
//
//    public arc(List<MatrixEntry> lme) {
//        MatrixEntry meStart;
//        meStart = getLeftestPoint(lme);
//
//        lmeSorted = new ArrayList<MatrixEntry>();
////        lmeSorted.add(meStart);
////        lme.remove(meStart);
//
//        HashSet<MatrixEntry> Hash = new HashSet<MatrixEntry>(lme);
//        lme = new ArrayList<MatrixEntry>(Hash);
//
//        MatrixEntry meNextConnected = getNexNearest(meStart, lme);        
//        while (meNextConnected != null) {
//            lmeSorted.add(meNextConnected);
//            lme.remove(meNextConnected);
//            meNextConnected = getNexNearest(meNextConnected, lme);
//            if(meNextConnected == null){
//                meNextConnected = getNexNearest(meStart, lme);
//            }
//        }
//        if (lmeSorted.isEmpty()) {
//            lme.remove(meStart);
//        }
//
//    }
//
//    public final MatrixEntry getLeftestPoint(List<MatrixEntry> lme) {
//        Integer iRunner = Integer.MAX_VALUE;
//        MatrixEntry meLeft = null;
//        for (MatrixEntry me : lme) {
//            if (me.j < iRunner) {
//                meLeft = me;
//                iRunner = me.j;
//            }
//        }
//        return meLeft;
//    }
//
//    public final MatrixEntry getLeftStart(List<MatrixEntry> lme) {
//        List<MatrixEntry> lmeEndPoints = getEndPoints(lme);
//        for (MatrixEntry me : lmeEndPoints) {
//            MatrixEntry meRightConnected = getNextConnectedPositive(me, lme);
//            if (meRightConnected != null) {
//                return meRightConnected;
//            }
//        }
//        return getLeftestPoint(lme);
//
////        int iRunner = 0;
////        MatrixEntry meStart = getLeftestPoint(lme);                
////        lmeSorted = new ArrayList<MatrixEntry>();
////        List<MatrixEntry> lmeTemp = new ArrayList<MatrixEntry>();
////        lmeTemp.addAll(lme);
////        
////        lmeTemp.remove(meStart);
////        MatrixEntry meNextConnected = getNextConnectedNegative(meStart, lmeTemp);                  
////        while(meNextConnected != null && iRunner<lmeTemp.size()){
////            lmeSorted.add(meNextConnected);   
////            lmeTemp.remove(meNextConnected);
////            meNextConnected = getNextConnectedNegative(meNextConnected, lmeTemp);
////            iRunner++;
////        }
////        
////        return lmeSorted.isEmpty() ? meStart : lmeSorted.get(lmeSorted.size()-1);
//    }
//
//    public final List<MatrixEntry> getEndPoints(List<MatrixEntry> lme) {
//        List<MatrixEntry> lmeEndPoints = new ArrayList<MatrixEntry>();
//        for (MatrixEntry me : lme) {
//            List<MatrixEntry> lmeVic = Entries.getPointsInVicinity(lme, me);
//            if (lmeVic.size() == 1) {
//                lmeEndPoints.add(me);
//            }
//        }
//        return lmeEndPoints;
//    }
//
//    public final MatrixEntry getNextConnectedPositive(MatrixEntry meRef, List<MatrixEntry> lme) {
//
//        List<OrderdPair> lopAngles = new ArrayList<OrderdPair>();
//
//        int iRunner = 0;
//        for (MatrixEntry me : lme) {
//            if (!me.equals(meRef) && me.SecondCartesian(meRef) > 0 && me.SecondCartesian(meRef) < 2 * dMetric) {
//                lopAngles.add(new OrderdPair(iRunner, me.getAngle(meRef) < 0 ? (me.getAngle(meRef) + 2 * Math.PI) : me.getAngle(meRef), me.SecondCartesian(meRef)));
//            }
//            iRunner++;
//        }
//
//        Collections.sort(lopAngles, new ValueOrderedPairComparatorWithY());
//        if (!lopAngles.isEmpty()) {
//            return lme.get((int) lopAngles.get(0).x);
//        }
//        return null;
//
//    }
//
//    public final MatrixEntry getNexNearest(MatrixEntry meRef, List<MatrixEntry> lme) {
//
//        List<OrderdPair> lopAngles = new ArrayList<OrderdPair>();
//
//        int iRunner = 0;
//        for (MatrixEntry me : lme) {
//            double dCart = me.SecondCartesian(meRef);
//            if (!me.equals(meRef) && dCart > 0 && dCart < 4 * dMetric) {
//                lopAngles.add(new OrderdPair(iRunner, dCart));
//            }
//            iRunner++;
//        }
//
//        Collections.sort(lopAngles, new ValueOrderedPairComparatorWithY());
//        if (!lopAngles.isEmpty()) {
//            return lme.get((int) lopAngles.get(0).x);
//        }
//        return null;
//
//    }
//
//    public final MatrixEntry getNextConnectedNegative(MatrixEntry meRef, List<MatrixEntry> lme) {
//
//        List<OrderdPair> lopAngles = new ArrayList<OrderdPair>();
//
//        int iRunner = 0;
//        for (MatrixEntry me : lme) {
//            if (!me.equals(meRef) && me.SecondCartesian(meRef) > 0 && me.SecondCartesian(meRef) < 2 * dMetric) {
//                lopAngles.add(new OrderdPair(iRunner, me.getAngle(meRef) < 0 ? (me.getAngle(meRef) + 2 * Math.PI) : me.getAngle(meRef), me.SecondCartesian(meRef)));
//            }
//            iRunner++;
//        }
//
//        Collections.sort(lopAngles, new ValueOrderedPairComparatorWithY());
//        if (!lopAngles.isEmpty()) {
//            return lme.get((int) lopAngles.get(lopAngles.size() - 1).x);
//        }
//        return null;
//
//    }
//
//    public List<MatrixEntry> calcSimpleLinearCurvature(int iOrder) {
//
//        List<MatrixEntry> lmeCurvature = new ArrayList<MatrixEntry>();
//        for (int i = iOrder; i < (this.lmeSorted.size() - iOrder); i++) {
//            Vector oV1 = new Vector(lmeSorted.get(i - iOrder), lmeSorted.get(i));
//            Vector oV2 = new Vector(lmeSorted.get(i), lmeSorted.get(i + iOrder));
//            MatrixEntry me = new MatrixEntry(lmeSorted.get(i).i, lmeSorted.get(i).j);
//            double dCosAngle = Vector.get360DegreeAngleMPiPi(oV1, oV2);
//            me.dValue = dCosAngle < 0 ? 255 : 127;
//            lmeCurvature.add(me);
//        }
//
//        return lmeCurvature;
//    }
//
////    public void calcSimpleLinearCurvatureClosedCurve(int iOrder, double dCosThreshold, int iNonSplitIdent) {
////
////        if (this.lmeSorted.isEmpty() || lmeSorted.size() <= 2 * iOrder) {
////            for (MatrixEntry me : lmeSorted) {
////                me.dValue = iNonSplitIdent;
////            }
////            return;
////        }
////
////        List<MatrixEntry> lmeTemp = new ArrayList<MatrixEntry>();
////        lmeTemp.addAll(this.lmeSorted);
////        for (int i = lmeSorted.size() - 1; i > lmeSorted.size() - (iOrder); i--) {
////            lmeTemp.add(0, lmeSorted.get(i));
////        }
////
////        for (int i = 0; i < iOrder; i++) {
////            lmeTemp.add(lmeSorted.get(i));
////        }
////
////        for (int i = iOrder; i < (lmeTemp.size() - iOrder); i++) {
////            Vector oV1 = new Vector(lmeTemp.get(i - iOrder), lmeTemp.get(i));
////            Vector oV2 = new Vector(lmeTemp.get(i), lmeTemp.get(i + iOrder));
////            double dCosAngle = Vector.get360DegreeAngleMPiPi(oV1, oV2);
//////            lmeSorted.get(i - iOrder).dValue = dCosAngle < 0 ? 255 : iNonSplitIdent;
////            lmeSorted.get(i - iOrder).dValue = dCosAngle;
////        }
////
////        double iMaxValue = -1.0 * Math.PI;
////        int iMaxPos = -1;
////        boolean bMaximaSearch = false;
////
////        for (int i = 0; i < lmeSorted.size(); i++) {
////            MatrixEntry me = lmeSorted.get(i);
////
////            if (me.dValue <= dCosThreshold && bMaximaSearch && iMaxPos > 0) {
////                lmeSorted.get(iMaxPos + 1).dValue = 255;
////            }
////
////            if (me.dValue > dCosThreshold) {
////                bMaximaSearch = true;
////            } else {
////                bMaximaSearch = false;
////            }
////
////            if (bMaximaSearch) {
////                if (me.dValue > iMaxValue) {
////                    iMaxValue = me.dValue;
////                    iMaxPos = i;
////                }
////            } else {
////                iMaxValue = -1.0 * Math.PI;
////                iMaxPos = -1;
////            }
////
////            me.dValue = iNonSplitIdent;
////
////        }
////
////    }
//    public void calcSimpleLinearCurvatureClosedCurve(int iOrder, double dCosThreshold, int iPaintColor) {
//
//        if (this.lmeSorted.isEmpty() || lmeSorted.size() <= 2 * iOrder) {
//            for (MatrixEntry me : lmeSorted) {
//                me.dValue = iPaintColor;
//            }
//            return;
//        }
//
//        List<MatrixEntry> lmeTemp = new ArrayList<MatrixEntry>();
//        lmeTemp.addAll(this.lmeSorted);
//        for (int i = lmeSorted.size() - 1; i > lmeSorted.size() - (iOrder); i--) {
//            lmeTemp.add(0, lmeSorted.get(i));
//        }
//
//        for (int i = 0; i < iOrder; i++) {
//            lmeTemp.add(lmeSorted.get(i));
//        }
//
//        for (int i = iOrder; i < (lmeTemp.size() - iOrder); i++) {
//            Vector oV1 = new Vector(lmeTemp.get(i - iOrder), lmeTemp.get(i));
//            Vector oV2 = new Vector(lmeTemp.get(i), lmeTemp.get(i + iOrder));
//            double dCosAngle = Vector.get360DegreeAngleMPiPi(oV1, oV2);
////            lmeSorted.get(i - iOrder).dValue = dCosAngle < 0 ? 255 : 127;
//            lmeSorted.get(i - iOrder).dValue = dCosAngle;
//        }
//
//        double iMaxValue = -1.0 * Math.PI;
//        int iMaxPos = -1;
//        boolean bMaximaSearch = false;
//
//        for (int i = 0; i < lmeSorted.size(); i++) {
//            MatrixEntry me = lmeSorted.get(i);
//
//            if (me.dValue <= dCosThreshold && bMaximaSearch && iMaxPos > 0) {
//                lmeSorted.get(iMaxPos + 1).dValue = 255;
//            }
//
//            if (me.dValue > dCosThreshold) {
//                bMaximaSearch = true;
//            } else {
//                bMaximaSearch = false;
//            }
//
//            if (bMaximaSearch) {
//                if (me.dValue > iMaxValue) {
//                    iMaxValue = me.dValue;
//                    iMaxPos = i;
//                }
//            } else {
//                iMaxValue = -1.0 * Math.PI;
//                iMaxPos = -1;
//            }
//
//            me.dValue = iPaintColor;
//
//        }
//
//    }
//
//    public void getCurvHF(int[][] img, int iPaintColor) {
//
//        if (this.lmeSorted.isEmpty()) {
//            return;
//        }
//
//        List<Double> ld = new ArrayList<Double>();
//
//        System.out.println("4.1");
//        for (int i = 0; i < lmeSorted.size(); i++) {
//            MatrixEntry me = lmeSorted.get(i);
//            ld.add(getHF(me, img));
//        }
//        
//        System.out.println("4.2");
//
//        for (int i = 0; i < lmeSorted.size(); i++) {
//            MatrixEntry me = lmeSorted.get(i);
//            if (i == 0) {
//                me.dValue = (ld.get(i) + ld.get(i + 1) + ld.get(ld.size() - 1)) / 2.0;
//            } else if (i == lmeSorted.size() - 1) {
//                me.dValue = (ld.get(i) + ld.get(0) + ld.get(i - 1)) / 2.0;
//            } else {
//                me.dValue = (ld.get(i) + ld.get(i + 1) + ld.get(i) + ld.get(i) + ld.get(i - 1)) / 3.0;
//            }
//        }
//    }
//    
//    public void getCurvHF(int[][] img, int iPaintColor, double[][] img_smear) {
//
//        if (this.lmeSorted.isEmpty()) {
//            return;
//        }
//
//        List<Double> ld = new ArrayList<Double>();
//
//        System.out.println("4.1");
//        for (int i = 0; i < lmeSorted.size(); i++) {
//            MatrixEntry me = lmeSorted.get(i);
//            ld.add(getHF(me, img, img_smear));
//        }
//        
//        System.out.println("4.2");
//
//        for (int i = 0; i < lmeSorted.size(); i++) {
//            MatrixEntry me = lmeSorted.get(i);
//            if (i == 0) {
//                me.dValue = (ld.get(i) + ld.get(i + 1) + ld.get(ld.size() - 1)) / 2.0;
//            } else if (i == lmeSorted.size() - 1) {
//                me.dValue = (ld.get(i) + ld.get(0) + ld.get(i - 1)) / 2.0;
//            } else {
//                me.dValue = (ld.get(i) + ld.get(i + 1) + ld.get(i) + ld.get(i) + ld.get(i - 1)) / 3.0;
//            }
//        }
//    }
//
//    public double getHF(MatrixEntry me, int[][] img_in) {
//        /*
//         Numbering of the stencils:
//         vertical (0° rotated):
//         * FL    FC    FR
//         *     |     |
//         * v40 | v41 | v42
//         * ____|_____|_____        
//         *     |     |
//         * v30 | v31 | v32
//         * ____|_____|_____        
//         *     |     |
//         * v20 | v21 | v22
//         * ____|_____|_____        
//         *     |     |
//         * v10 | v11 | v12
//         * ____|_____|_____
//         *     |     |
//         * v00 | v01 | v02
//         *     |     |
//        
//         270° rotated
//         * _______ _______ _______ _______ _______ 
//         *        |       |       |       |       |
//         *   h20  |  h21  |  h22  |  h23  |  h24  | FL
//         * _______|_______|_______|_______|_______|
//         *        |       |       |       |       |
//         *   h10  |  h11  |  h12  |  h13  |  h14  | FC
//         * _______|_______|_______|_______|_______|
//         *        |       |       |       |       |
//         *   h00  |  h01  |  h02  |  h03  |  h04  | FR
//         * _______|_______|_______|_______|_______|
//        
//         */
//        //Calc the four possible 3x5 stencils
//        //Upright
//
//        double[][] img = Fundamentals.Operations.Convolution_d(img_in, MatrixGenerator.get3x3Smear());
//        img = Fundamentals.Operations.Convolution(img, MatrixGenerator.get3x3Smear());
//
//        img = Fundamentals.Matrix.normalizeMatrix(img);
//
//        double[][] v = new double[5][3];
//        for (int i = 2; i >= -2; i--) {
//            for (int j = -1; j <= 1; j++) {
//                int iCell = Math.min(Math.max(me.i + i, 0), img.length - 1);
//                int jCell = Math.min(Math.max(me.j + j, 0), img[0].length - 1);
//                v[i + 2][j + 1] = img[iCell][jCell];
//            }
//        }
//
//        double[][] h = new double[3][5];
//        for (int i = 1; i >= -1; i--) {
//            for (int j = -2; j <= 2; j++) {
//                int iCell = Math.min(Math.max(me.i + i, 0), img.length - 1);
//                int jCell = Math.min(Math.max(me.j + j, 0), img[0].length - 1);
//                h[i + 1][j + 2] = img[iCell][jCell];
//            }
//        }
//
//        // Calculate the sum of the bottom of each stencil to decide the direction
//        double Summ0Rotation = v[0][0] + v[0][1] + v[0][2] + v[1][0] + v[1][1] + v[1][2];
//        double Summ90Rotation = h[0][4] + h[1][4] + h[2][4] + h[0][3] + h[1][3] + h[2][3];
//        double Summ180Rotation = v[4][0] + v[4][1] + v[4][2] + v[3][0] + v[3][1] + v[3][2];
//        double Summ270Rotation = h[0][0] + h[1][0] + h[2][0] + h[0][1] + h[1][1] + h[2][1];
//
//        // 0 Rotation true by default
//        boolean b90Rot = false;
//        boolean b180Rot = false;
//        boolean b270Rot = false;
//
//        if (Summ90Rotation > Summ0Rotation && Summ90Rotation > Summ180Rotation && Summ90Rotation > Summ270Rotation) {
//            b90Rot = true;
//        }
//
//        if (Summ180Rotation > Summ0Rotation && Summ180Rotation > Summ90Rotation && Summ180Rotation > Summ270Rotation) {
//            b180Rot = true;
//        }
//
//        if (Summ270Rotation > Summ0Rotation && Summ270Rotation > Summ90Rotation && Summ270Rotation > Summ180Rotation) {
//            b270Rot = true;
//        }
//
//        //Calculate the sum along the columns
//        double dFL = 0.0;
//        double dFC = 0.0;
//        double dFR = 0.0;
//
//        for (int i = 0; i < 5; i++) {
//            dFL = dFL + v[i][0];
//            dFC = dFC + v[i][1];
//            dFR = dFR + v[i][2];
//        }
//
//        if (b180Rot) {
//            for (int i = 0; i < 5; i++) {
//                dFR = dFR + v[i][0];
//                dFC = dFC + v[i][1];
//                dFL = dFL + v[i][2];
//            }
//        }
//
//        if (b270Rot) {
//            for (int j = 0; j < 5; j++) {
//                dFR = dFR + h[0][j];
//                dFC = dFC + h[1][j];
//                dFL = dFL + h[2][j];
//            }
//        }
//
//        if (b90Rot) {
//            for (int j = 0; j < 5; j++) {
//                dFL = dFL + h[0][j];
//                dFC = dFC + h[1][j];
//                dFR = dFR + h[2][j];
//            }
//        }
//
//        //Direction of Surface
//        double dSign = Math.signum(dFR - dFL);
//        //Check for Minima in Height Function
//        double dPhi = (dFC - dFL) * (dFR - dFC);
//        //define slope
//        double p = 0.0;
//        if (dPhi > 0) {
//            p = dSign * Math.min(Math.abs(2 * (dFR - dFC)), Math.min(Math.abs(2 * (dFC - dFL)), Math.abs(0.5 * (dFR - dFL)))) * 1.0 / 1.0;
//        }
//        //calculate curvature
//        double kappa = (2 * dFC - dFL - dFR) / Math.pow((1 + p * p), 3.0 / 2.0) * 1.0 / (1.0 * 1.0);
//
//        return kappa;
//
//    }
//    
//    public double getHF(MatrixEntry me, int[][] img_in, double[][] img_smear) {
//        /*
//         Numbering of the stencils:
//         vertical (0° rotated):
//         * FL    FC    FR
//         *     |     |
//         * v40 | v41 | v42
//         * ____|_____|_____        
//         *     |     |
//         * v30 | v31 | v32
//         * ____|_____|_____        
//         *     |     |
//         * v20 | v21 | v22
//         * ____|_____|_____        
//         *     |     |
//         * v10 | v11 | v12
//         * ____|_____|_____
//         *     |     |
//         * v00 | v01 | v02
//         *     |     |
//        
//         270° rotated
//         * _______ _______ _______ _______ _______ 
//         *        |       |       |       |       |
//         *   h20  |  h21  |  h22  |  h23  |  h24  | FL
//         * _______|_______|_______|_______|_______|
//         *        |       |       |       |       |
//         *   h10  |  h11  |  h12  |  h13  |  h14  | FC
//         * _______|_______|_______|_______|_______|
//         *        |       |       |       |       |
//         *   h00  |  h01  |  h02  |  h03  |  h04  | FR
//         * _______|_______|_______|_______|_______|
//        
//         */
//        //Calc the four possible 3x5 stencils
//        //Upright
//        
//
//        double[][] v = new double[5][3];
//        for (int i = 2; i >= -2; i--) {
//            for (int j = -1; j <= 1; j++) {
//                int iCell = Math.min(Math.max(me.i + i, 0), img_smear.length - 1);
//                int jCell = Math.min(Math.max(me.j + j, 0), img_smear[0].length - 1);
//                v[i + 2][j + 1] = img_smear[iCell][jCell];
//            }
//        }
//
//        double[][] h = new double[3][5];
//        for (int i = 1; i >= -1; i--) {
//            for (int j = -2; j <= 2; j++) {
//                int iCell = Math.min(Math.max(me.i + i, 0), img_smear.length - 1);
//                int jCell = Math.min(Math.max(me.j + j, 0), img_smear[0].length - 1);
//                h[i + 1][j + 2] = img_smear[iCell][jCell];
//            }
//        }
//
//        // Calculate the sum of the bottom of each stencil to decide the direction
//        double Summ0Rotation = v[0][0] + v[0][1] + v[0][2] + v[1][0] + v[1][1] + v[1][2];
//        double Summ90Rotation = h[0][4] + h[1][4] + h[2][4] + h[0][3] + h[1][3] + h[2][3];
//        double Summ180Rotation = v[4][0] + v[4][1] + v[4][2] + v[3][0] + v[3][1] + v[3][2];
//        double Summ270Rotation = h[0][0] + h[1][0] + h[2][0] + h[0][1] + h[1][1] + h[2][1];
//
//        // 0 Rotation true by default
//        boolean b90Rot = false;
//        boolean b180Rot = false;
//        boolean b270Rot = false;
//
//        if (Summ90Rotation > Summ0Rotation && Summ90Rotation > Summ180Rotation && Summ90Rotation > Summ270Rotation) {
//            b90Rot = true;
//        }
//
//        if (Summ180Rotation > Summ0Rotation && Summ180Rotation > Summ90Rotation && Summ180Rotation > Summ270Rotation) {
//            b180Rot = true;
//        }
//
//        if (Summ270Rotation > Summ0Rotation && Summ270Rotation > Summ90Rotation && Summ270Rotation > Summ180Rotation) {
//            b270Rot = true;
//        }
//
//        //Calculate the sum along the columns
//        double dFL = 0.0;
//        double dFC = 0.0;
//        double dFR = 0.0;
//
//        for (int i = 0; i < 5; i++) {
//            dFL = dFL + v[i][0];
//            dFC = dFC + v[i][1];
//            dFR = dFR + v[i][2];
//        }
//
//        if (b180Rot) {
//            for (int i = 0; i < 5; i++) {
//                dFR = dFR + v[i][0];
//                dFC = dFC + v[i][1];
//                dFL = dFL + v[i][2];
//            }
//        }
//
//        if (b270Rot) {
//            for (int j = 0; j < 5; j++) {
//                dFR = dFR + h[0][j];
//                dFC = dFC + h[1][j];
//                dFL = dFL + h[2][j];
//            }
//        }
//
//        if (b90Rot) {
//            for (int j = 0; j < 5; j++) {
//                dFL = dFL + h[0][j];
//                dFC = dFC + h[1][j];
//                dFR = dFR + h[2][j];
//            }
//        }
//
//        //Direction of Surface
//        double dSign = Math.signum(dFR - dFL);
//        //Check for Minima in Height Function
//        double dPhi = (dFC - dFL) * (dFR - dFC);
//        //define slope
//        double p = 0.0;
//        if (dPhi > 0) {
//            p = dSign * Math.min(Math.abs(2 * (dFR - dFC)), Math.min(Math.abs(2 * (dFC - dFL)), Math.abs(0.5 * (dFR - dFL)))) * 1.0 / 1.0;
//        }
//        //calculate curvature
//        double kappa = (2 * dFC - dFL - dFR) / Math.pow((1 + p * p), 3.0 / 2.0) * 1.0 / (1.0 * 1.0);
//
//        return kappa;
//
//    }
//
//    public double getHF_Helmsen(MatrixEntry me, int[][] img_in) {
//        /*
//         Numbering of the stencils:
//         vertical (0° rotated):
//         * FL    FC    FR
//         *     |     |
//         * v40 | v41 | v42
//         * ____|_____|_____        
//         *     |     |
//         * v30 | v31 | v32
//         * ____|_____|_____        
//         *     |     |
//         * v20 | v21 | v22
//         * ____|_____|_____        
//         *     |     |
//         * v10 | v11 | v12
//         * ____|_____|_____
//         *     |     |
//         * v00 | v01 | v02
//         *     |     |
//        
//         270° rotated
//         * _______ _______ _______ _______ _______ 
//         *        |       |       |       |       |
//         *   h20  |  h21  |  h22  |  h23  |  h24  | FL
//         * _______|_______|_______|_______|_______|
//         *        |       |       |       |       |
//         *   h10  |  h11  |  h12  |  h13  |  h14  | FC
//         * _______|_______|_______|_______|_______|
//         *        |       |       |       |       |
//         *   h00  |  h01  |  h02  |  h03  |  h04  | FR
//         * _______|_______|_______|_______|_______|
//        
//         */
//        //Calc the four possible 3x5 stencils
//        //Upright
//
//        double[][] img = Fundamentals.Operations.Convolution_d(img_in, MatrixGenerator.get3x3Smear());
//        img = Fundamentals.Operations.Convolution(img, MatrixGenerator.get3x3Smear());
//
//        img = Fundamentals.Matrix.normalizeMatrix(img);
//
//        double[][] v = new double[5][3];
//        for (int i = -2; i <= 2; i++) {
//            for (int j = -1; j <= 1; j++) {
//                int iCell = Math.min(Math.max(me.i + i, 0), img.length - 1);
//                int jCell = Math.min(Math.max(me.j + j, 0), img[0].length - 1);
//                v[i + 2][j + 1] = img[iCell][jCell];
//            }
//        }
//
//        double[][] h = new double[3][5];
//        for (int i = -1; i <= 1; i++) {
//            for (int j = -2; j <= 2; j++) {
//                int iCell = Math.min(Math.max(me.i + i, 0), img.length - 1);
//                int jCell = Math.min(Math.max(me.j + j, 0), img[0].length - 1);
//                h[i + 1][j + 2] = img[iCell][jCell];
//            }
//        }
//
//        double Summ0Rotation = v[0][0] + v[0][1] + v[0][2] + v[1][0] + v[1][1] + v[1][2];
//        double Summ90Rotation = h[0][4] + h[1][4] + h[2][4] + h[0][3] + h[1][3] + h[2][3];
//        double Summ180Rotation = v[4][0] + v[4][1] + v[4][2] + v[3][0] + v[3][1] + v[3][2];
//        double Summ270Rotation = h[0][0] + h[1][0] + h[2][0] + h[0][1] + h[1][1] + h[2][1];
//
//        // 0 Rotation true by default
//        boolean b90Rot = false;
//        boolean b180Rot = false;
//        boolean b270Rot = false;
//
//        if (Summ90Rotation > Summ0Rotation && Summ90Rotation > Summ180Rotation && Summ90Rotation > Summ270Rotation) {
//            b90Rot = true;
//        }
//
//        if (Summ180Rotation > Summ0Rotation && Summ180Rotation > Summ90Rotation && Summ180Rotation > Summ270Rotation) {
//            b180Rot = true;
//        }
//
//        if (Summ270Rotation > Summ0Rotation && Summ270Rotation > Summ90Rotation && Summ270Rotation > Summ180Rotation) {
//            b270Rot = true;
//        }
//
//        double dFL = 0.0;
//        double dFC = 0.0;
//        double dFR = 0.0;
//
//        for (int i = 0; i < 5; i++) {
//            dFL = dFL + v[i][0];
//            dFC = dFC + v[i][1];
//            dFR = dFR + v[i][2];
//        }
//
//        if (b180Rot) {
//            dFL = 0.0;
//            dFC = 0.0;
//            dFR = 0.0;
//            for (int i = 0; i < 5; i++) {
//                dFR = dFR + v[i][0];
//                dFC = dFC + v[i][1];
//                dFL = dFL + v[i][2];
//            }
//        }
//
//        if (b270Rot) {
//            dFL = 0.0;
//            dFC = 0.0;
//            dFR = 0.0;
//            for (int j = 0; j < 5; j++) {
//                dFR = dFR + h[0][j];
//                dFC = dFC + h[1][j];
//                dFL = dFL + h[2][j];
//            }
//        }
//
//        if (b90Rot) {
//            dFL = 0.0;
//            dFC = 0.0;
//            dFR = 0.0;
//            for (int j = 0; j < 5; j++) {
//                dFL = dFL + h[0][j];
//                dFC = dFC + h[1][j];
//                dFR = dFR + h[2][j];
//            }
//        }
//
//        double kappa = (2 * dFC - dFL - dFR) / Math.pow(1 + 0.5 * (dFR - dFL) * 0.5 * (dFR - dFL), 3.0 / 2.0);
//
//        return kappa;
//
//    }
//
////    public List<? extends arc> split(int iPieceLength) {
////        List<arc> loSplits = new ArrayList<arc>();
////
////        for (int i = 0;; i = i + iPieceLength) {
////            arc ob = new arc();
////            if (i + (2 * iPieceLength) < lmeSorted.size()) {
////                ob.lmeSorted = lmeSorted.subList(i, i + iPieceLength);
////            } else if ((lmeSorted.size() - i) > 1.5 * iPieceLength) {
////                ob.lmeSorted = lmeSorted.subList(i, i + iPieceLength);
////                loSplits.add(ob);
////                ob = new arc();
////                ob.lmeSorted = lmeSorted.subList(i + iPieceLength, lmeSorted.size());
////                loSplits.add(ob);
////                break;
////            } else {
////                ob.lmeSorted = lmeSorted.subList(i, lmeSorted.size());
////                loSplits.add(ob);
////                break;
////            }
////
////            loSplits.add(ob);
////        }
////
////        return loSplits;
////
////    }
}
