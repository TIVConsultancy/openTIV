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
package com.tivconsultancy.opentiv.helpfunctions.matrix;


import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein HZDR Fluidynamics, t.ziegenhein@hzdr.de
 */
public class Entries {

    static public Double[][] setArea(Double[][] array, OrderedPair left, OrderedPair right, Number Value) {

        for (int lefty = (int) left.y; lefty <= right.y; lefty++) {
            for (int leftx = (int) left.x; leftx <= right.x; leftx++) {

                array[lefty][leftx] = (Double) Value;

            }
        }

        return array;
    }

    static public Double[][] setValue(Double[][] array, OrderedPair Position, Number Value) {

        array[(int) Position.x][(int) Position.y] = (Double) Value;

        return array;
    }

    static public Double[][] setValue(Double[][] array, int iRowNumber, int iColumnNumber, Number Value) {

        array[iRowNumber][iColumnNumber] = (Double) Value;

        return array;
    }

    static public int[][] getArea(int[][] array, OrderedPair left, OrderedPair right) {

        int deltay = (int) (right.y - left.y) + 1;
        int deltax = (int) (right.x - left.x) + 1;
        int[][] returnarray = new int[deltay][deltax];
        int i = 0;
        int j = 0;

        for (int lefty = (int) left.y; lefty <= right.y; lefty++) {
            for (int leftx = (int) left.x; leftx <= right.x; leftx++) {

                returnarray[i][j] = array[lefty][leftx];
                j++;

            }
            j = 0;
            i++;
        }

        return returnarray;
    }
    
    public static List<MatrixEntry> getAllPointAboveThres(int[][] iaGrey, int iThresh) {
        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();
        for (int i = 0; i < iaGrey.length; i++) {
            for (int j = 0; j < iaGrey[0].length; j++) {
                if (iaGrey[i][j] >= iThresh) {
                    MatrixEntry me = new MatrixEntry(0, 0);
                    me.i = i;
                    me.j = j;
                    me.dValue = iaGrey[i][j];
                    lmeReturn.add(me);
                }
            }
        }
        return lmeReturn;
    }
    
    public static List<MatrixEntry> getPointsInVicinity(List<MatrixEntry> lme, MatrixEntry meRef){
        Double dVicinity = 2.0;
        List<MatrixEntry> lmeVicinity = new ArrayList<MatrixEntry>();
        
        for(MatrixEntry me : lme){
            if(!meRef.equals(me)){
                double dDist = me.SecondCartesian(meRef);
                if(dDist<dVicinity){
                    lmeVicinity.add(me);
                }
            }
        }
        
        return lmeVicinity;
    } 
    
    public static List<MatrixEntry> getPointsInVicinity(int[][] iaInput, MatrixEntry meRef, int iRadius, OperationME op){
        
        List<MatrixEntry> lmeVicinity = new ArrayList<MatrixEntry>();
        
        for(int i = (meRef.i-iRadius > 0 ? meRef.i-iRadius : 0); i<= (meRef.i+iRadius < iaInput.length ? meRef.i+iRadius : iaInput.length-1); i++ ){
            for(int j = (meRef.j-iRadius > 0 ? meRef.j-iRadius : 0); j<= (meRef.j+iRadius < iaInput[0].length ? meRef.j+iRadius : iaInput[0].length-1); j++ ){
                MatrixEntry me;
                if(op != null){
                    me = op.perform(meRef, new MatrixEntry(i, j, iaInput[i][j]));  
                }else{
                    me = new MatrixEntry(i, j, iaInput[i][j]); 
                }                
                lmeVicinity.add(me);
            }
        }
        
        return lmeVicinity;
    }
    
    public static void getPointsInVicinity(int[][] iaInput, MatrixEntry meRef, int iRadius, List<MatrixEntry> lmeVicinity, OperationME op){                
        
        int iCount = 0;
        for(int i = (meRef.i-iRadius > 0 ? meRef.i-iRadius : 0); i<= (meRef.i+iRadius < iaInput.length ? meRef.i+iRadius : iaInput.length-1); i++ ){
            for(int j = (meRef.j-iRadius > 0 ? meRef.j-iRadius : 0); j<= (meRef.j+iRadius < iaInput[0].length ? meRef.j+iRadius : iaInput[0].length-1); j++ ){
                MatrixEntry me;
                if(op != null){
                    me = op.perform(meRef, new MatrixEntry(i, j, iaInput[i][j]));  
                }else{
                    me = new MatrixEntry(i, j, iaInput[i][j]); 
                }                
                lmeVicinity.set(iCount, me);
                iCount++;
            }
        }
        
    }
            

    static public void setPoints(List<OrderedPair> lopInput, int[][] iaInput, int iValue) {

        if (lopInput != null) {
            for (OrderedPair a : lopInput) {

                iaInput[(int) a.y][(int) a.x] = iValue;

            }
        }

    }

    static public void setEntries(List<MatrixEntry> lmeInput, int[][] iaInput, int iValue) {

        if (lmeInput != null) {
            for (MatrixEntry a : lmeInput) {
                if (a != null && Matrix.inBounds(iaInput, a)) {
                    iaInput[(int) a.i][(int) a.j] = iValue;
                }
            }
        }
    }
    
    static public void setEntriesOpague(List<MatrixEntry> lmeInput, int[][] iaInput, int iShift) {

        if (lmeInput != null) {
            for (MatrixEntry a : lmeInput) {
                if (a != null && Matrix.inBounds(iaInput, a)) {
                    iaInput[(int) a.i][(int) a.j] = iaInput[(int) a.i][(int) a.j] + iShift ;
                }
            }
        }
    }
    
    static public void setEntries(List<MatrixEntry> lmeInput, int[][] iaInput) {

        if (lmeInput != null) {
            for (MatrixEntry a : lmeInput) {

                if (a != null && Matrix.inBounds(iaInput, a)) {
                    iaInput[(int) a.i][(int) a.j] = (int) a.dValue;
                }

            }
        }

    }

    static public void setEntrieslle(List<List<MatrixEntry>> lmeInput, int[][] iaInput, int iValue) {

        if (lmeInput != null) {
            for (List<MatrixEntry> la : lmeInput) {

                setEntries(la, iaInput, iValue);

            }
        }

    }

    static public void setEntriesWithThres(List<MatrixEntry> lmeInput, int[][] iaInput, int iValue, int iThres) {

        if (lmeInput != null) {
            for (MatrixEntry a : lmeInput) {

                if (Matrix.inBounds(iaInput, a) && iaInput[(int) a.i][(int) a.j] < iThres) {
                    iaInput[(int) a.i][(int) a.j] = iValue;
                }

            }
        }

    }

    static public void setEntriesWithThresFromSource(List<MatrixEntry> lmeInput, int[][] iaInput, int iValue, int iThres, int[][] iaThresSource) {

        if (lmeInput != null) {
            for (MatrixEntry a : lmeInput) {

                if (Matrix.inBounds(iaInput, a) && iaThresSource[(int) a.i][(int) a.j] < iThres) {
                    iaInput[(int) a.i][(int) a.j] = iValue;
                }

            }
        }

    }

    static public void setEntriesllme(List<List<MatrixEntry>> lmeInput, int[][] iaInput, int iValue) {

        if (lmeInput != null) {
            for (List<MatrixEntry> lme : lmeInput) {

                setEntries(lme, iaInput, iValue);

            }
        }

    }

    static public void setEntries(List<MatrixEntry> lmeInput, int[][] iaInput, List<Integer> iValue) {

        int iCount = 0;
        if (lmeInput != null) {
            for (MatrixEntry a : lmeInput) {

                iaInput[(int) a.i][(int) a.j] = iValue.get(iCount);

                iCount = iCount + 1;

            }
        }

    }

    static public void setEntries(MatrixEntry meInput, int[][] iaInput, int iValue) {

        if (meInput != null) {

            iaInput[(int) meInput.i][(int) meInput.j] = iValue;

        }

    }

    static public void setEntries(MatrixEntry meInput, int[][] iaInput, int iValue, int iEntrieSize) {

        if (meInput != null) {
            for (int i = (-1) * iEntrieSize; i <= iEntrieSize; i++) {
                for (int j = (-1) * iEntrieSize; j <= iEntrieSize; j++) {
                    if (Matrix.inBounds(iaInput, (int) meInput.j + j, (int) meInput.i + i)) {
                        iaInput[(int) meInput.i + i][(int) meInput.j + j] = iValue;
                    }
                }
            }
        }

    }

    static public void setPoint(OrderedPair opInput, int[][] iaInput, int iValue) {

        if (opInput != null) {

            iaInput[(int) opInput.y][(int) opInput.x] = iValue;

        }

    }

    static public void setPoint(OrderedPair opInput, int[][] iaInput, int iValue, int iPointSize) {

        if (opInput != null) {
            for (int i = (-1) * iPointSize; i <= iPointSize; i++) {
                for (int j = (-1) * iPointSize; i <= iPointSize; i++) {
                    if (Matrix.inBounds(iaInput, (int) opInput.y + i, (int) opInput.y + j)) {
                        iaInput[(int) opInput.y + i][(int) opInput.x + j] = iValue;
                    }
                }
            }
        }
    }

    static public int[][] rasta(int[][] Input, int rastaWidth, int rastaHeight) {
        /**
         * no comments, will break down and removed in next version
         */
        int[][] rasta = new int[rastaHeight][rastaWidth];

        int inputWidth = Input[0].length;
        int inputHeight = Input.length;
        NumberFormat nf = new DecimalFormat("0");
        nf.setRoundingMode(RoundingMode.DOWN);

        final double W = ((double) inputWidth) / rastaWidth;
        final double H = ((double) inputHeight) / rastaHeight;

        int wRound = Integer.valueOf(nf.format(W));
        double restW = Integer.valueOf(nf.format(W)) - W;
        int hRound = Integer.valueOf(nf.format(H));
        double restH = Integer.valueOf(nf.format(H)) - H;

        //System.out.println(wRound);
        //System.out.println(hRound);
        int positionOldX = 0;
        int positionOldY = 0;
        int counterY = 0;
        double restleft = 0;
        double restleftValue = 0;
        double restright = restH;
        double restrightValue = 0;
        double restabove = 0;
        double restaboveValue = 0;
        double restdown = 0;
        double restdownValue = 0;
        double sum = 0;
        double Quotient = 0;

        if (W > 0) {

            for (int counterX = 0; counterX < rastaWidth; counterX++) {

                sum = 0;

                restright = W * (counterX + 1) - Integer.valueOf(nf.format(W * (counterX + 1)));

                System.out.print("rleft" + restleft + " ||" + "rright" + restright);
                for (int i = 0; i < hRound; i++) {
                    sum = sum + restleft * restrightValue;
                    Quotient = Quotient + restleft;
                    for (int j = 0; j < wRound; j++) {
                        if (i == 0) {
                            sum = sum + restabove * restaboveValue;
                            Quotient = Quotient + restabove;
                        }
                        if (i == hRound - 1) {
                            sum = sum + restdown * restdownValue;
                            Quotient = Quotient + restdown;
                        }
                        Quotient = Quotient + 1;

                        //System.out.println(positionOldY + i);
                        //System.out.println(positionOldX + j);
                        if (Input[positionOldY + i][positionOldX + j] != 0) {
                            sum = sum + Input[positionOldY + i][positionOldX + j];
                        }
                        restrightValue = Input[positionOldY + i][positionOldX + j];
                    }

                    sum = sum + restright * restrightValue;
                    Quotient = Quotient + restright;
                }
                if (restW > 0) {
                    positionOldX = positionOldX + wRound + 1;
                } else {
                    positionOldX = positionOldX + wRound;
                }

                System.out.print("s/q" + sum / Quotient);

                if (sum / Quotient >= 0.5) {
                    rasta[counterY][counterX] = 1;
                }

                if (restright != 0) {
                    restleft = 1 - restright;
                } else {
                    restleft = 0;
                }

            }

            positionOldX = 0;
            if (restH > 0) {
                positionOldY = positionOldY + hRound + 1;
            } else {
                positionOldY = positionOldY + hRound;
            }

        }

        return rasta;

    }

    public static List<MatrixEntry> getUniqueEntries(List<MatrixEntry> lmeInput) {

        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();

        HashSet hs = new HashSet();
        hs.addAll(lmeInput);
        lmeReturn.addAll(hs);

        /*for (int i = 0; i < lmeReturn.size(); i++) {
         for (int j = 0; j < lmeReturn.size(); j++) {

         if (i != j) {

                    
         if (lmeReturn.get(i).equalsMatrixEntry(lmeReturn.get(j))) {
         System.out.println("Index: " + i + " " + lmeReturn.get(i));

         System.out.println("Index: " + j + " " + lmeReturn.get(j));
         }

         }

         }

         }*/
        return lmeReturn;
    }

    public static List<List<MatrixEntry>> getUniqueArrayEntries(List<List<MatrixEntry>> lmeInput) {

        List<List<MatrixEntry>> llmeReturn = new ArrayList<List<MatrixEntry>>();

        for (List<MatrixEntry> lme : lmeInput) {

            llmeReturn.add(Entries.getUniqueEntries(lme));

        }

        return llmeReturn;

    }
    
    public static MatrixEntry getMin(List<MatrixEntry> lme){
        MatrixEntry meMin = new MatrixEntry(Integer.MAX_VALUE,Integer.MAX_VALUE);
        for(MatrixEntry me : lme){
            if(me.i<meMin.i){
                meMin.i = me.i;
            }
            if(me.j<meMin.j){
                meMin.j = me.j;
            }
        }
        return meMin;
    }
    
    public static MatrixEntry getMax(List<MatrixEntry> lme){
        MatrixEntry meMax = new MatrixEntry(Integer.MIN_VALUE,Integer.MIN_VALUE);
        for(MatrixEntry me : lme){
            if(me.i>meMax.i){
                meMax.i = me.i;
            }
            if(me.j>meMax.j){
                meMax.j = me.j;
            }
        }
        return meMax;
    }

    public static List<MatrixEntry> getMaxAxis(List<MatrixEntry> lmeInputArea) {

        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();

        int iMin = Integer.MAX_VALUE;
        int ijMin = Integer.MAX_VALUE;
        int iMax = 0;
        int ijMax = 0;
        int jMin = Integer.MAX_VALUE;
        int jiMin = Integer.MAX_VALUE;
        int jMax = 0;
        int jiMax = 0;

        for (MatrixEntry me : lmeInputArea) {

            if (me.i > iMax) {
                iMax = me.i;
                ijMax = me.j;
            }

            if (me.i < iMin) {
                iMin = me.i;
                ijMin = me.j;
            }

            if (me.j > jMax) {
                jMax = me.j;
                jiMax = me.i;
            }

            if (me.j < jMin) {
                jMin = me.j;
                jiMin = me.i;
            }

        }

        lmeReturn.add(new MatrixEntry(iMax, ijMax));
        lmeReturn.add(new MatrixEntry(iMin, ijMin));
        lmeReturn.add(new MatrixEntry(jiMax, jMax));
        lmeReturn.add(new MatrixEntry(jiMin, jMin));

        return lmeReturn;

    }

//    public static List<MatrixEntry> getMajorMinorAxis(List<MatrixEntry> lmeInputArea) {
//
//        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();
//        //        List<Double> ldNorm = new ArrayList<Double>();
////        List<OrderedPair> ldPositionInListOuterInner = new ArrayList<OrderedPair>();
//        double dMaxNorm = (-1) * Double.MAX_VALUE;
//        double dNorm = (-1) * Double.MAX_VALUE;
//        MatrixEntry meMajorP1 = null;
//        MatrixEntry meMajorP2 = null;
//
////        int iCounterOuter = 0;
//        for (MatrixEntry meOuter : lmeInputArea) {
////            int iCounterInner = 0;
//            for (MatrixEntry meInner : lmeInputArea) {
//                dNorm = MatrixEntry.SecondCartesian(meInner, meOuter);
//                if(dMaxNorm < dNorm){
//                    meMajorP1 = meOuter;
//                    meMajorP2 = meInner;
//                    dMaxNorm = dNorm;
//                }
////                ldNorm.add(MatrixEntry.SecondCartesian(meInner, meOuter));
////                ldPositionInListOuterInner.add(new OrderedPair(iCounterOuter, iCounterInner));
////                iCounterInner++;
//            }
////            iCounterOuter++;
//        }
//
////        Double dMaxNorm = Organize.getMax(ldNorm);
////        int iIndexOfMax = ldNorm.indexOf(dMaxNorm);
////        MatrixEntry meMajorP1 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).x);
////        MatrixEntry meMajorP2 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).y);
////
////        List<Double> ldNorm = new ArrayList<Double>();
////        List<OrderedPair> ldPositionInListOuterInner = new ArrayList<OrderedPair>();
////
////        int iCounterOuter = 0;
////        for (MatrixEntry meOuter : lmeInputArea) {
////            int iCounterInner = 0;
////            for (MatrixEntry meInner : lmeInputArea) {
////                ldNorm.add(MatrixEntry.SecondCartesian(meInner, meOuter));
////                ldPositionInListOuterInner.add(new OrderedPair(iCounterOuter, iCounterInner));
////                iCounterInner++;
////            }
////            iCounterOuter++;
////        }
////
////        Double dMaxNorm = Organize.getMax(ldNorm);
////        int iIndexOfMax = ldNorm.indexOf(dMaxNorm);
////        MatrixEntry meMajorP1 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).x);
////        MatrixEntry meMajorP2 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).y);
//
//        Vector oVecMajor = new Vector(meMajorP1, meMajorP2);
//
//        Vector oVecMinor = new Vector(0.0, 0.0);
//        MatrixEntry meMinorP1 = null;
//        MatrixEntry meMinorP2 = null;
//
//        for (MatrixEntry meOuter : lmeInputArea) {
//
//            for (MatrixEntry me : lmeInputArea) {
//
//                Vector oTemp = new Vector(meOuter, me);
//
//                if (oVecMinor.modulus() < oTemp.modulus()) {
//
//                    Line oLine = new Line(meOuter, me);
//
//                    if ((Math.abs(Vector.cosAngle(oTemp, oVecMajor)) < 2.0 / oVecMajor.modulus())) {
//
//                        // 2/oVecMajor.modulus() is the minimm Angle through discretization
//                        oVecMinor = oTemp;
//
//                        meMinorP1 = meOuter;
//                        meMinorP2 = me;
//
//                    }
//
//                }
//
//            }
//
//        }
//        
//        if(meMinorP1 == null || meMinorP2 == null){
//            Tensor2D RotTensor = Tensor2D.getRotTensor(Math.PI/2);
//            int iTranslateI = -1;
//            if((meMajorP1.i-meMajorP2.i) > 0){
//                iTranslateI = (int) (meMajorP1.i-(meMajorP1.i-meMajorP2.i)/2.0);
//            }else{
//                iTranslateI = (int) (meMajorP1.i+(meMajorP2.i-meMajorP1.i)/2.0);
//            }
//            int iTranslateJ = -1;
//            MatrixEntry meMajorAxisCenter;
//            if((meMajorP1.j-meMajorP2.j) > 0){
//                iTranslateJ = (int) (meMajorP1.j-Math.abs((meMajorP1.j-meMajorP2.j))/2.0);
//                meMajorAxisCenter = new MatrixEntry(iTranslateI, meMajorP1.j-iTranslateJ);                
//            }else{
//                iTranslateJ = (int) (meMajorP1.j+Math.abs((meMajorP2.j-meMajorP1.j))/2.0);
//                meMajorAxisCenter = new MatrixEntry(iTranslateI, iTranslateJ);   
//            }
//            
//            MatrixEntry meMajorP1ToMAC = new MatrixEntry(meMajorP1.i-meMajorAxisCenter.i, meMajorP1.j-meMajorAxisCenter.j);
//            MatrixEntry meMajorP2ToMAC = new MatrixEntry(meMajorP2.i-meMajorAxisCenter.i, meMajorP2.j-meMajorAxisCenter.j);
//            meMinorP1 = new MatrixEntry(RotTensor.product(meMajorP1ToMAC));           
//            meMinorP1.i = meMinorP1.i + meMajorAxisCenter.i;
//            meMinorP1.j = meMinorP1.j + meMajorAxisCenter.j;
//            meMinorP2 = new MatrixEntry(RotTensor.product(meMajorP2ToMAC));
//            meMinorP2.i = meMinorP2.i + meMajorAxisCenter.i;
//            meMinorP2.j = meMinorP2.j + meMajorAxisCenter.j;
//        }
//
//        lmeReturn.add(meMajorP1);
//        lmeReturn.add(meMajorP2);
//        lmeReturn.add(meMinorP1);
//        lmeReturn.add(meMinorP2);
//
//        return lmeReturn;
//
//    }
    
    public static List<MatrixEntry> getMajorAxis(List<MatrixEntry> lmeInputArea, MatrixEntry meCenter) {        
        
        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();
        
        if(lmeInputArea.size()<4){
            lmeReturn.add(meCenter);
            lmeReturn.add(meCenter);
            lmeReturn.add(meCenter);
            lmeReturn.add(meCenter);
            return lmeReturn;
        }

//        List<Double> ldNorm = new ArrayList<Double>();
//        List<OrderedPair> ldPositionInListOuterInner = new ArrayList<OrderedPair>();
        double dMaxNorm = (-1) * Double.MAX_VALUE;
        double dNorm = (-1) * Double.MAX_VALUE;
        MatrixEntry meMajorP1 = null;
        MatrixEntry meMajorP2 = null;
        double dRadius = Math.sqrt(lmeInputArea.size()/Math.PI);
        
        List<MatrixEntry> lmeBoarder = new ArrayList<MatrixEntry>();
        
        for (MatrixEntry me : lmeInputArea) {
            if(MatrixEntry.SecondCartesian(me, meCenter)> dRadius*0.9){
                lmeBoarder.add(me);
            }
        }

//        int iCounterOuter = 0;
        for (MatrixEntry meOuter : lmeBoarder) {
            
//            int iCounterInner = 0;
            for (MatrixEntry meInner : lmeBoarder) {
                dNorm = MatrixEntry.SecondCartesian(meInner, meOuter);
                if(dMaxNorm < dNorm){
                    meMajorP1 = meOuter;
                    meMajorP2 = meInner;
                    dMaxNorm = dNorm;
                }
//                ldNorm.add(MatrixEntry.SecondCartesian(meInner, meOuter));
//                ldPositionInListOuterInner.add(new OrderedPair(iCounterOuter, iCounterInner));
//                iCounterInner++;
            }
            
//            iCounterOuter++;
        }

//        Double dMaxNorm = Organize.getMax(ldNorm);
//        int iIndexOfMax = ldNorm.indexOf(dMaxNorm);
//        MatrixEntry meMajorP1 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).x);
//        MatrixEntry meMajorP2 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).y);


        lmeReturn.add(meMajorP1);
        lmeReturn.add(meMajorP2);
        

        return lmeReturn;

    }

//    public static List<MatrixEntry> getMajorMinorAxis_Previous(List<MatrixEntry> lmeInputArea, MatrixEntry meCenter) {        
//        
//        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();
//        
//        if(lmeInputArea.size()<4){
//            lmeReturn.add(meCenter);
//            lmeReturn.add(meCenter);
//            lmeReturn.add(meCenter);
//            lmeReturn.add(meCenter);
//            return lmeReturn;
//        }
//
////        List<Double> ldNorm = new ArrayList<Double>();
////        List<OrderedPair> ldPositionInListOuterInner = new ArrayList<OrderedPair>();
//        double dMaxNorm = (-1) * Double.MAX_VALUE;
//        double dNorm = (-1) * Double.MAX_VALUE;
//        MatrixEntry meMajorP1 = null;
//        MatrixEntry meMajorP2 = null;
//        double dRadius = Math.sqrt(lmeInputArea.size()/Math.PI);
//        
//        List<MatrixEntry> lmeBoarder = new ArrayList<MatrixEntry>();
//        
//        Double dHorizontalAxis = getHorizontalAxis(lmeInputArea);
//        Double dVertical = getVerticalAxis(lmeInputArea);
//        
//        Double dMinAxis = dHorizontalAxis < dVertical ? dHorizontalAxis:dVertical;
////        Double dMaxAxis = dHorizontalAxis > dVertical ? dHorizontalAxis:dVertical;
//        
//        for (MatrixEntry me : lmeInputArea) {
//            if(MatrixEntry.SecondCartesian(me, meCenter)>= (dMinAxis/2.0)){
////            if(MatrixEntry.SecondCartesian(me, meCenter)> dRadius){
//                lmeBoarder.add(me);
//            }
//        }
//        
//        if(lmeBoarder.size()<5){
//            lmeBoarder = lmeInputArea;
//        }
//
////        int iCounterOuter = 0;
//        for (MatrixEntry meOuter : lmeBoarder) {
//            
////            int iCounterInner = 0;
//            for (MatrixEntry meInner : lmeBoarder) {
//                dNorm = MatrixEntry.SecondCartesian(meInner, meOuter);
//                if(dMaxNorm < dNorm){
//                    meMajorP1 = meOuter;
//                    meMajorP2 = meInner;
//                    dMaxNorm = dNorm;
//                }
////                ldNorm.add(MatrixEntry.SecondCartesian(meInner, meOuter));
////                ldPositionInListOuterInner.add(new OrderedPair(iCounterOuter, iCounterInner));
////                iCounterInner++;
//            }
//            
////            iCounterOuter++;
//        }
//
////        Double dMaxNorm = Organize.getMax(ldNorm);
////        int iIndexOfMax = ldNorm.indexOf(dMaxNorm);
////        MatrixEntry meMajorP1 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).x);
////        MatrixEntry meMajorP2 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).y);
//
//        Vector oVecMajor = new Vector(meMajorP1, meMajorP2);
//
//        Vector oVecMinor = new Vector(0.0, 0.0);
//        MatrixEntry meMinorP1 = null;
//        MatrixEntry meMinorP2 = null;
//        
//        Vector oVecPerpendicularToMajAxis = new Vector((-1)*oVecMajor.y, oVecMajor.x);
//        List<MatrixEntry> lmePerdenicular = new ArrayList<MatrixEntry>();
//        
//        for (MatrixEntry me : lmeInputArea) {
//            double dDistToPerpAxis = oVecPerpendicularToMajAxis.getDistanceToPoint(me, meCenter);
//            if(dDistToPerpAxis<1.1*1.41){
//                lmePerdenicular.add(me);
//            };
//        }
//
//        for (MatrixEntry meOuter : lmePerdenicular) {
//
//            for (MatrixEntry me : lmePerdenicular) {
//
//                Vector oTemp = new Vector(meOuter, me);
//
//                if (oVecMinor.modulus() < oTemp.modulus()) {
//
//                    Line oLine = new Line(meOuter, me);
//
//                    //if ((Math.abs(Vector.Angle(oTemp, oVecMajor)) < 2.0 / oVecMajor.modulus()) && Entries.ifContains(oLine.lmeLine, meCenter)) {
//                    if ((Math.abs(Vector.cosAngle(oTemp, oVecMajor)) < (2.0 / oVecMajor.modulus())) && Entries.ifContainsEightBlock(oLine.lmeLine, meCenter)) {
////                    if (Entries.ifContainsEightBlock(oLine.lmeLine, meCenter)) {
//
//                        // 2/oVecMajor.modulus() is the minimm Angle through discretization
//                        oVecMinor = oTemp;
//
//                        meMinorP1 = meOuter;
//                        meMinorP2 = me;
//
//                    }
//
//                }
//
//            }
//
//        }
//        
//        if(meMinorP1 == null || meMinorP2 == null){
//            Tensor2D RotTensor = Tensor2D.getRotTensor(Math.PI/2);
////            int iTranslateI = -1;
////            if((meMajorP1.i-meMajorP2.i) > 0){
////                iTranslateI = (int) (meMajorP1.i-(meMajorP1.i-meMajorP2.i)/2.0);
////            }else{
////                iTranslateI = (int) (meMajorP1.i+(meMajorP2.i-meMajorP1.i)/2.0);
////            }
////            int iTranslateJ = -1;
////            MatrixEntry meMajorAxisCenter;
////            if((meMajorP1.j-meMajorP2.j) > 0){
////                iTranslateJ = (int) (meMajorP1.j-Math.abs((meMajorP1.j-meMajorP2.j))/2.0);
////                meMajorAxisCenter = new MatrixEntry(iTranslateI, meMajorP1.j-iTranslateJ);                
////            }else{
////                iTranslateJ = (int) (meMajorP1.j+Math.abs((meMajorP2.j-meMajorP1.j))/2.0);
////                meMajorAxisCenter = new MatrixEntry(iTranslateI, iTranslateJ);   
////            }
//            MatrixEntry meMajorAxisCenter = meCenter;
//            
//            MatrixEntry meMajorP1ToMAC = new MatrixEntry(meMajorP1.i-meMajorAxisCenter.i, meMajorP1.j-meMajorAxisCenter.j);
//            MatrixEntry meMajorP2ToMAC = new MatrixEntry(meMajorP2.i-meMajorAxisCenter.i, meMajorP2.j-meMajorAxisCenter.j);
//            meMinorP1 = new MatrixEntry(RotTensor.product(meMajorP1ToMAC));           
//            meMinorP1.i = meMinorP1.i + meMajorAxisCenter.i;
//            meMinorP1.j = meMinorP1.j + meMajorAxisCenter.j;
//            meMinorP2 = new MatrixEntry(RotTensor.product(meMajorP2ToMAC));
//            meMinorP2.i = meMinorP2.i + meMajorAxisCenter.i;
//            meMinorP2.j = meMinorP2.j + meMajorAxisCenter.j;
//        }
//
//        lmeReturn.add(meMajorP1);
//        lmeReturn.add(meMajorP2);
//        lmeReturn.add(meMinorP1);
//        lmeReturn.add(meMinorP2);
//
//        return lmeReturn;
//
//    }
    
//    public static List<MatrixEntry> getMajorMinorAxis(List<MatrixEntry> lmeInputArea, MatrixEntry meCenter) {        
//        
//        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();
//        
//        if(lmeInputArea.size()<4){
//            lmeReturn.add(meCenter);
//            lmeReturn.add(meCenter);
//            lmeReturn.add(meCenter);
//            lmeReturn.add(meCenter);
//            return lmeReturn;
//        }
//
////        List<Double> ldNorm = new ArrayList<Double>();
////        List<OrderedPair> ldPositionInListOuterInner = new ArrayList<OrderedPair>();
//        double dMaxNorm = (-1) * Double.MAX_VALUE;
//        double dNorm = (-1) * Double.MAX_VALUE;
//        MatrixEntry meMajorP1 = null;
//        MatrixEntry meMajorP2 = null;
//        double dRadius = Math.sqrt(lmeInputArea.size()/Math.PI);
//        
//        List<MatrixEntry> lmeBoarder = new ArrayList<MatrixEntry>();
//        
//        Double dHorizontalAxis = getHorizontalAxis(lmeInputArea);
//        Double dVertical = getVerticalAxis(lmeInputArea);
//        
//        Double dMinAxis = dHorizontalAxis < dVertical ? dHorizontalAxis:dVertical;
////        Double dMaxAxis = dHorizontalAxis > dVertical ? dHorizontalAxis:dVertical;
//        
//        for (MatrixEntry me : lmeInputArea) {
//            if(MatrixEntry.SecondCartesian(me, meCenter)>= (dMinAxis/2.5)){
////            if(MatrixEntry.SecondCartesian(me, meCenter)> dRadius){
//                lmeBoarder.add(me);
//            }
//        }
//        
//        if(lmeBoarder.size()<5){
//            lmeBoarder = lmeInputArea;
//        }
//
////        int iCounterOuter = 0;
//        for (MatrixEntry meOuter : lmeBoarder) {
//            
////            int iCounterInner = 0;
//            for (MatrixEntry meInner : lmeBoarder) {
//                dNorm = MatrixEntry.SecondCartesian(meInner, meOuter);
//                if(dMaxNorm < dNorm){
//                    meMajorP1 = meOuter;
//                    meMajorP2 = meInner;
//                    dMaxNorm = dNorm;
//                }
////                ldNorm.add(MatrixEntry.SecondCartesian(meInner, meOuter));
////                ldPositionInListOuterInner.add(new OrderedPair(iCounterOuter, iCounterInner));
////                iCounterInner++;
//            }
//            
////            iCounterOuter++;
//        }
//
////        Double dMaxNorm = Organize.getMax(ldNorm);
////        int iIndexOfMax = ldNorm.indexOf(dMaxNorm);
////        MatrixEntry meMajorP1 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).x);
////        MatrixEntry meMajorP2 = lmeInputArea.get((int) ldPositionInListOuterInner.get(iIndexOfMax).y);
//
//        Vector oVecMajor = new Vector(meMajorP1, meMajorP2);        
//
//        Vector oVecMinor = new Vector(0.0, 0.0);
//        MatrixEntry meMinorP1 = null;
//        MatrixEntry meMinorP2 = null;
//        
//        List<MatrixEntry> lmePerdenicular = new ArrayList<MatrixEntry>();
//        
//        for (MatrixEntry me : lmeInputArea) {
//            Vector oVecTmp = new Vector(meCenter, me);
//            double dSkalarProduct = Vector.ScalarProduct(oVecMajor, oVecTmp );
//            if(Math.abs(dSkalarProduct)/(oVecMajor.modulus()*oVecTmp.modulus())<1.0/oVecTmp.modulus()){
//                lmePerdenicular.add(me);
//            };
//        }
//
//        for (MatrixEntry meOuter : lmePerdenicular) {
//            for (MatrixEntry me : lmePerdenicular) {
//                Vector oTemp = new Vector(meOuter, me);
//
//                if (oVecMinor.modulus() < oTemp.modulus()) {
//
//                    Line oLine = new Line(meOuter, me);
//
//                    //if ((Math.abs(Vector.Angle(oTemp, oVecMajor)) < 2.0 / oVecMajor.modulus()) && Entries.ifContains(oLine.lmeLine, meCenter)) {
////                    if ((Math.abs(Vector.cosAngle(oTemp, oVecMajor)) < (2.0 / oVecMajor.modulus())) && Entries.ifContainsEightBlock(oLine.lmeLine, meCenter)) {
////                    if (Entries.ifContainsEightBlock(oLine.lmeLine, meCenter)) {
//
//                        // 2/oVecMajor.modulus() is the minimm Angle through discretization
//                        oVecMinor = oTemp;
//
//                        meMinorP1 = meOuter;
//                        meMinorP2 = me;
//
////                    }
//
//                }
//
//            }
//
//        }
//        
//        if(meMinorP1 == null || meMinorP2 == null){
//            Tensor2D RotTensor = Tensor2D.getRotTensor(Math.PI/2);
////            int iTranslateI = -1;
////            if((meMajorP1.i-meMajorP2.i) > 0){
////                iTranslateI = (int) (meMajorP1.i-(meMajorP1.i-meMajorP2.i)/2.0);
////            }else{
////                iTranslateI = (int) (meMajorP1.i+(meMajorP2.i-meMajorP1.i)/2.0);
////            }
////            int iTranslateJ = -1;
////            MatrixEntry meMajorAxisCenter;
////            if((meMajorP1.j-meMajorP2.j) > 0){
////                iTranslateJ = (int) (meMajorP1.j-Math.abs((meMajorP1.j-meMajorP2.j))/2.0);
////                meMajorAxisCenter = new MatrixEntry(iTranslateI, meMajorP1.j-iTranslateJ);                
////            }else{
////                iTranslateJ = (int) (meMajorP1.j+Math.abs((meMajorP2.j-meMajorP1.j))/2.0);
////                meMajorAxisCenter = new MatrixEntry(iTranslateI, iTranslateJ);   
////            }
//            MatrixEntry meMajorAxisCenter = meCenter;
//            
//            MatrixEntry meMajorP1ToMAC = new MatrixEntry(meMajorP1.i-meMajorAxisCenter.i, meMajorP1.j-meMajorAxisCenter.j);
//            MatrixEntry meMajorP2ToMAC = new MatrixEntry(meMajorP2.i-meMajorAxisCenter.i, meMajorP2.j-meMajorAxisCenter.j);
//            meMinorP1 = new MatrixEntry(RotTensor.product(meMajorP1ToMAC));           
//            meMinorP1.i = meMinorP1.i + meMajorAxisCenter.i;
//            meMinorP1.j = meMinorP1.j + meMajorAxisCenter.j;
//            meMinorP2 = new MatrixEntry(RotTensor.product(meMajorP2ToMAC));
//            meMinorP2.i = meMinorP2.i + meMajorAxisCenter.i;
//            meMinorP2.j = meMinorP2.j + meMajorAxisCenter.j;
//        }
//
//        lmeReturn.add(meMajorP1);
//        lmeReturn.add(meMajorP2);
//        lmeReturn.add(meMinorP1);
//        lmeReturn.add(meMinorP2);
//
//        return lmeReturn;
//
//    }
    
//    public static List<MatrixEntry> getHorizontalVerticalAxis(List<MatrixEntry> lmeInputArea){
//        List<MatrixEntry> lme = new ArrayList<MatrixEntry>();
//        
//        OrderedPair opMinX = OrderedPair.getMinXPos(lmeInputArea);
//        OrderedPair opMaxX = OrderedPair.getMaxXPos(lmeInputArea);
//        OrderedPair opMinY = OrderedPair.getMinXPos(lmeInputArea);
//        OrderedPair opMaxY = OrderedPair.getMaxXPos(lmeInputArea);
//        
//        lme.add(new MatrixEntry(opMinX));
//        lme.add(new MatrixEntry(opMaxX));
//        lme.add(new MatrixEntry(opMinY));
//        lme.add(new MatrixEntry(opMaxY));
//        
//        return lme;
//        
//    }
    
    public static Double getHorizontalAxis(List<MatrixEntry> lmeInputArea){
        
        MatrixEntry opMinX = MatrixEntry.getMinJPos(lmeInputArea);
        MatrixEntry opMaxX = MatrixEntry.getMaxJPos(lmeInputArea);
        
        return MatrixEntry.SecondCartesian(opMaxX, opMinX);
        
    }
    
    public static Double getVerticalAxis(List<MatrixEntry> lmeInputArea){
        
        MatrixEntry opMinY = MatrixEntry.getMinIPos(lmeInputArea);
        MatrixEntry opMaxY = MatrixEntry.getMaxIPos(lmeInputArea);
        
        return MatrixEntry.SecondCartesian(opMaxY, opMinY);
        
    }


//    public static List<MatrixEntry> getMajorMinorAxis_old(List<MatrixEntry> lmeInputArea) {
//
//        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();
//
//        MatrixEntry meMajorP1;
//        MatrixEntry meMajorP2;
//
//        int iMin = Integer.MAX_VALUE;
//        int ijMin = Integer.MAX_VALUE;
//        int iMax = 0;
//        int ijMax = 0;
//        int jMin = Integer.MAX_VALUE;
//        int jiMin = Integer.MAX_VALUE;
//        int jMax = 0;
//        int jiMax = 0;
//        MatrixEntry meMinorP1 = new MatrixEntry(0, 0);
//        MatrixEntry meMinorP2 = new MatrixEntry(0, 0);
//
//        for (MatrixEntry me : lmeInputArea) {
//
//            if (me.i > iMax) {
//                iMax = me.i;
//                ijMax = me.j;
//            }
//
//            if (me.i < iMin) {
//                iMin = me.i;
//                ijMin = me.j;
//            }
//
//            if (me.j > jMax) {
//                jMax = me.j;
//                jiMax = me.i;
//            }
//
//            if (me.j < jMin) {
//                jMin = me.j;
//                jiMin = me.i;
//            }
//
//        }
//
//        MatrixEntry opMaxI = new MatrixEntry(iMax, ijMax);
//        MatrixEntry opMinI = new MatrixEntry(iMin, ijMin);
//        MatrixEntry opMaxJ = new MatrixEntry(jiMax, jMax);
//        MatrixEntry opMinJ = new MatrixEntry(jiMin, jMin);
//
//        if (MatrixEntry.SecondCartesian(opMaxI, opMinI) > MatrixEntry.SecondCartesian(opMaxJ, opMinJ)) {
//            meMajorP1 = opMaxI;
//            meMajorP2 = opMinI;
//        } else {
//            meMajorP1 = opMaxJ;
//            meMajorP2 = opMinJ;
//        }
//
//        Vector oVecMajor = new Vector(meMajorP1, meMajorP2);
//
//        Vector oVecMinor = new Vector(0.0, 0.0);
//
//        for (MatrixEntry meOuter : lmeInputArea) {
//
//            for (MatrixEntry me : lmeInputArea) {
//
//                Vector oTemp = new Vector(meOuter, me);
//
//                if (oVecMinor.modulus() < oTemp.modulus()) {
//
//                    if (Math.abs(Vector.cosAngle(oTemp, oVecMajor)) < 2 / oVecMajor.modulus()) {
//
//                        // 2/oVecMajor.modulus() is the minimm Angle through discretization
//                        oVecMinor = oTemp;
//
//                        meMinorP1 = meOuter;
//                        meMinorP2 = me;
//
//                    }
//
//                }
//
//            }
//
//        }
//
//        lmeReturn.add(meMajorP1);
//        lmeReturn.add(meMajorP2);
//        lmeReturn.add(meMinorP1);
//        lmeReturn.add(meMinorP2);
//
//        return lmeReturn;
//
//    }

    public static void paintLine(int[][] iaInput, List<MatrixEntry> lmeInputMatrixEntryPairs, int iValue) {
        /*
         * Not working correctly
         */
        for (int i = 0; i < lmeInputMatrixEntryPairs.size(); i++) {

            if (lmeInputMatrixEntryPairs.size() > i + 1) {

                MatrixEntry me1;
                MatrixEntry me2;

                if (lmeInputMatrixEntryPairs.get(i).i < lmeInputMatrixEntryPairs.get(i + 1).i) {
                    me1 = lmeInputMatrixEntryPairs.get(i);
                    me2 = lmeInputMatrixEntryPairs.get(i + 1);
                } else {
                    me2 = lmeInputMatrixEntryPairs.get(i);
                    me1 = lmeInputMatrixEntryPairs.get(i + 1);
                }
                ;

                if ((me1.j - me2.j) != 0) {

                    double dGrad = (me1.i - me2.i) / (me1.j - me2.j);

                    double dRest = dGrad - (int) dGrad;

                    double dLost = 0.0;

                    for (int j = 0; j < (me1.j - me2.j); j++) {

                        int iGrad = (int) dGrad;

                        if (dLost >= 1.0) {
                            iGrad = iGrad + 1;
                            dLost = dLost - 1;
                        }

                        if (me1.i + j + 1 < iaInput.length && me1.i + (j + 1) * iGrad < iaInput[0].length) {
                            iaInput[me1.i + j + 1][me1.j + (j + 1) * iGrad] = iValue;
                        }

                        dLost = dLost + dRest;

                    }

                } else {
                    System.out.println(me1.SecondCartesian(me2));
                    for (int j = 0; j < me1.SecondCartesian(me2); j++) {
                        if (me1.i + j + 1 < iaInput.length) {

                            iaInput[me1.i + j + 1][me1.j] = iValue;
                        }
                    }

                }
            } else {
                break;
            }

        }

    }

    public static Double[][] MultiplicationEntries(Number[][] naInput1, Number[][] naInput2) {
        /**
         * naInput1_ij * naInput2_ij
         */
        Double[][] daOutput = new Double[naInput1.length][naInput2[0].length];

        for (int i = 0; i < naInput1.length; i++) {

            for (int j = 0; j < naInput1[0].length; j++) {

                daOutput[i][j] = (Double) naInput1[i][j] * (Double) naInput2[i][j];

            }

        }

        return (daOutput);

    }

    public static Double[] MultiplicationEntries(Number[] naInput1, Number[] naInput2) {
        /**
         * naInput1_i * naInput2_i
         */
        Double[] daOutput = new Double[naInput1.length];

        for (int i = 0; i < naInput1.length; i++) {

            daOutput[i] = (Double) naInput1[i] * (Double) naInput2[i];

        }

        return (daOutput);

    }

    public static Double[] MultiplicationEntries(Number[][] naInput1, Number[][] naInput2, int iColumn) {
        /**
         * naInput1_iiColumn * naInput2_iiColumn
         */
        Double[] daOutput = new Double[naInput1.length];

        for (int i = 0; i < naInput1.length; i++) {

            daOutput[i] = (Double) naInput1[i][iColumn] * (Double) naInput2[i][iColumn];

        }

        return (daOutput);

    }

    public static Double[] ScalarSubstraction(Number[][] naInput, Number nScalar, int iColumn) {

        Double[] daReturn = new Double[naInput.length];

        for (int i = 0; i < naInput.length; i++) {

            daReturn[i] = (Double) naInput[i][iColumn] - (Double) nScalar;

        }

        return daReturn;

    }

    public static Double[] ScalarSubstraction(Number[][] naInput, Number nScalar, int iColumn, int iSize) {

        Double[] daReturn = new Double[iSize];

        for (int i = 0; i < iSize; i++) {

            daReturn[i] = (Double) naInput[i][iColumn] - (Double) nScalar;

        }

        return daReturn;

    }

    public static Double[] ScalarSubstraction(Number[] naInput, Number nScalar, int iSize) {

        Double[] daReturn = new Double[iSize];

        for (int i = 0; i < iSize; i++) {

            daReturn[i] = (Double) naInput[i] - (Double) nScalar;

        }

        return daReturn;

    }

    public static Double[] ScalarAbsoluteSubstraction(Number[][] naInput, Number nScalar, int iColumn, int iSize) {

        Double[] daReturn = new Double[iSize];

        for (int i = 0; i < iSize; i++) {

            daReturn[i] = Math.abs((Double) naInput[i][iColumn] - (Double) nScalar);

        }

        return daReturn;

    }

    public static Double[] squareRoot(Number[] naInput) {

        Double[] daReturn = new Double[naInput.length];

        for (int i = 0; i < naInput.length; i++) {

            daReturn[i] = Math.sqrt((Double) naInput[i]);

        }

        return daReturn;

    }

    public static OrderedPair getsmallestInX(List<OrderedPair> lopInput) {

        OrderedPair opSmallestX = new OrderedPair();

        opSmallestX = lopInput.get(0);

        for (OrderedPair op : lopInput) {

            if (op.x < opSmallestX.x) {
                opSmallestX = op;
            }

        }
        return opSmallestX;

    }

    public static MatrixEntry getsmallestInJ(List<MatrixEntry> lmepInput) {

        MatrixEntry meSmallestJ;

        meSmallestJ = lmepInput.get(0);

        for (MatrixEntry op : lmepInput) {

            if (op.j < meSmallestJ.j) {
                meSmallestJ = op;
            }

        }
        return meSmallestJ;

    }

    public static MatrixEntry getlargestInJ(List<MatrixEntry> lmepInput) {

        MatrixEntry meLargestJ;

        meLargestJ = lmepInput.get(0);

        for (MatrixEntry op : lmepInput) {

            if (op.j > meLargestJ.j) {
                meLargestJ = op;
            }

        }
        return meLargestJ;

    }

    public static OrderedPair getsmallestInY(List<OrderedPair> lopInput) {

        OrderedPair opSmallestY = new OrderedPair();

        opSmallestY = lopInput.get(0);

        for (OrderedPair op : lopInput) {

            if (op.y < opSmallestY.y) {
                opSmallestY = op;
            }

        }
        return opSmallestY;

    }

    public static MatrixEntry getsmallestInIJSimpleAdditiveNorm(List<MatrixEntry> lmeInput) {

        MatrixEntry meSmallestIJ;
        int iSum = lmeInput.get(0).i + lmeInput.get(0).j;

        meSmallestIJ = lmeInput.get(0);

        for (MatrixEntry me : lmeInput) {

            if (iSum < (me.i + me.j)) {
                meSmallestIJ = me;
            }

        }
        return meSmallestIJ;

    }

    public static int getValue(int[][] iaInput, MatrixEntry me) {

        return iaInput[me.i][me.j];

    }

    public static List<MatrixEntry> getEntriesThreshold(int[][] iaInput, int iThreshold) {

        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] >= iThreshold) {
                    lmeReturn.add(new MatrixEntry(i, j));
                }

            }

        }

        return lmeReturn;

    }

    public static List<MatrixEntry> getEntriesThresholdBelow(int[][] iaInput, int iThreshold) {

        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] < iThreshold) {
                    lmeReturn.add(new MatrixEntry(i, j));
                }

            }

        }

        return lmeReturn;

    }

    public static List<MatrixEntry> Threshold(List<MatrixEntry> lmeInput, int[][] iaInput, int iThreshold) {

        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();

        for (MatrixEntry me : lmeInput) {
            if (Matrix.inBounds(iaInput, me) && iaInput[me.i][me.j] > iThreshold) {
                lmeReturn.add(me);
            }
        }

        return lmeReturn;

    }

    public static List<MatrixEntry> getEntriesValue(int[][] iaInput, int iValue) {

        List<MatrixEntry> lmeReturn = new ArrayList<MatrixEntry>();

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] == iValue) {
                    lmeReturn.add(new MatrixEntry(i, j));
                }

            }

        }

        return lmeReturn;

    }

    public static void getRelativeComplementAWithoutB(List<MatrixEntry> lmeInputA, List<MatrixEntry> lmeInputB) {
        //return stored in A
        for (MatrixEntry me : lmeInputB) {

            lmeInputA.remove(me);

        }

    }

    public static boolean ifContains(List<MatrixEntry> lmeInputA, MatrixEntry meToCheck) {

        boolean bContains = false;

        for (MatrixEntry me : lmeInputA) {

            if (me.equalsMatrixEntry(meToCheck)) {
                bContains = true;
                break;
            }

        }

        return bContains;

    }
    
    public static boolean ifContainsEightBlock(List<MatrixEntry> lmeInputA, MatrixEntry meToCheck) {

        List<MatrixEntry> lmeEightBlock = new ArrayList<MatrixEntry>();
        
        for(int i=-1; i<=1; i++){
            for(int j = -1; j<=1; j++){
                lmeEightBlock.add(new MatrixEntry(meToCheck.i+i, meToCheck.j+j));
            }
        }
        
        return ifContains(lmeInputA, lmeEightBlock);

    }
    

    public static boolean ifContains(List<MatrixEntry> lmeInputA, List<MatrixEntry> lmeInputB) {

        //Check if the Differenz A\B is empty (return false) or not (return true)
        boolean bContains = false;

        outer:
        for (MatrixEntry meO : lmeInputA) {
            for (MatrixEntry meI : lmeInputB) {

                if (meO.equalsMatrixEntry(meI)) {
                    bContains = true;
                    break outer;
                }

            }

        }

        return bContains;

    }

    public static void addUnique(List<MatrixEntry> lmeMainList, List<MatrixEntry> lmeSideList) {

        for (MatrixEntry meS : lmeSideList) {

            if (!(lmeMainList.contains(meS))) {
                lmeMainList.add(meS);
            }

        }

    }

    public static double getMinimumDistance(List<MatrixEntry> lmeInputSet, MatrixEntry meReference) {

        double dDistance = MatrixEntry.SecondCartesian(lmeInputSet.get(0), meReference);

        for (MatrixEntry me : lmeInputSet) {

            if (MatrixEntry.SecondCartesian(me, meReference) < dDistance) {
                dDistance = MatrixEntry.SecondCartesian(me, meReference);
            }

        }

        return dDistance;

    }

    public static MatrixEntry getNearest(List<MatrixEntry> lmeInputSet, MatrixEntry meReference) {

        double dDistance = MatrixEntry.SecondCartesian(lmeInputSet.get(0), meReference);
        MatrixEntry meNearest = lmeInputSet.get(0);

        for (MatrixEntry me : lmeInputSet) {

            if (MatrixEntry.SecondCartesian(me, meReference) < dDistance) {
                dDistance = MatrixEntry.SecondCartesian(me, meReference);
                meNearest = me;
            }

        }

        return meNearest;

    }

    public static MatrixEntry getNearest(List<MatrixEntry> lmeInputSet, MatrixEntry meReference, List<MatrixEntry> lmeForbiddenEntries) {

        double dDistance = Double.MAX_VALUE;
        MatrixEntry meNearest = null;

        for (MatrixEntry me : lmeInputSet) {
            if (!lmeForbiddenEntries.contains(me)) {
                if (MatrixEntry.SecondCartesian(me, meReference) < dDistance) {
                    dDistance = MatrixEntry.SecondCartesian(me, meReference);
                    meNearest = me;
                }
            }

        }

        return meNearest;

    }

//    public static OrderedPair getNearest(List<OrderedPair> lopInputSet, OrderedPair opReference, List<OrderedPair> lopForbiddenEntries) {
//
//        double dDistance = Double.MAX_VALUE;
//        OrderedPair opNearest = null;
//
//        for (OrderedPair op : lopInputSet) {
//            if (!lopForbiddenEntries.contains(op)) {
//                if (opReference != op && OrderedPair.SecondCartesian(op, opReference) < dDistance) {
//                    dDistance = MatrixEntry.SecondCartesian(op, opReference);
//                    opNearest = op;
//                }
//            }
//
//        }
//
//        return opNearest;
//
//    }

    public static OrderedPair getNearestY(List<OrderedPair> lopInputSet, OrderedPair opReference, List<OrderedPair> lopForbiddenEntries) {

        double dDistance = Double.MAX_VALUE;
        OrderedPair opNearest = null;

        for (OrderedPair op : lopInputSet) {
            if (!lopForbiddenEntries.contains(op)) {
                if (opReference != op && Math.abs(opReference.y - op.y) < dDistance) {
                    dDistance = Math.abs(opReference.y - op.y);
                    opNearest = op;
                }
            }

        }

        return opNearest;

    }

    public static double getMaximumDistance(List<MatrixEntry> lmeInputSet, MatrixEntry meReference) {

        double dDistance = MatrixEntry.SecondCartesian(lmeInputSet.get(0), meReference);

        for (MatrixEntry me : lmeInputSet) {

            if (MatrixEntry.SecondCartesian(me, meReference) > dDistance) {
                dDistance = MatrixEntry.SecondCartesian(me, meReference);
            }

        }

        return dDistance;

    }

    public static double getMaximumDistance(List<MatrixEntry> lmeInputSet, List<MatrixEntry> lmeReference) {

        double dDistance = MatrixEntry.SecondCartesian(lmeInputSet.get(0), lmeReference.get(0));

        for (MatrixEntry meOuter : lmeInputSet) {

            for (MatrixEntry meInner : lmeReference) {
                if (MatrixEntry.SecondCartesian(meOuter, meInner) > dDistance) {
                    dDistance = MatrixEntry.SecondCartesian(meOuter, meInner);
                }
            }

        }

        return dDistance;

    }

    public static int NormIDistance(MatrixEntry me1, MatrixEntry me2) {

        return Math.abs(me1.i - me2.i);

    }

    public static double Distance(int i1, int j1, int i2, int j2) {

        return Math.sqrt(((i1 - i2) * (i1 - i2)) + ((j1 - j2) * (j1 - j2)));

    }
    
    public static double Distance(MatrixEntry me1, MatrixEntry me2) {

        return Math.sqrt(((me1.i - me2.i) * (me1.i - me2.i)) + ((me1.j - me2.j) * (me1.j - me2.j)));

    }

    public static int NormMaxIDistance(List<MatrixEntry> lmeMatrixEntry) {

        int iMaxIDistance = 0;

        for (MatrixEntry meOuter : lmeMatrixEntry) {

            for (MatrixEntry meInner : lmeMatrixEntry) {

                if ((NormIDistance(meInner, meOuter)) > iMaxIDistance) {
                    iMaxIDistance = (NormIDistance(meInner, meOuter));
                }

            }

        }

        return iMaxIDistance;

    }

    public static double NormMinIDistance(List<MatrixEntry> lmeMatrixEntry, MatrixEntry meRef) {

        double dMinIDistance = Double.MAX_VALUE;

        for (MatrixEntry meInner : lmeMatrixEntry) {
            double dDistance = NormIDistance(meInner, meRef);

            if (dDistance < dMinIDistance) {
                dMinIDistance = dDistance;
            }

        }

        return dMinIDistance;

    }
    
    public static double MinDistance(List<MatrixEntry> lmeMatrixEntry, MatrixEntry meRef) {

        double dMinIDistance = Double.MAX_VALUE;

        for (MatrixEntry meInner : lmeMatrixEntry) {
            double dDistance = Distance(meInner, meRef);

            if (dDistance < dMinIDistance) {
                dMinIDistance = dDistance;
            }

        }

        return dMinIDistance;

    }

    public static MatrixEntry getNearesPoint(List<MatrixEntry> lmeList1, List<MatrixEntry> lmeList2) {

        double dMinIDistance = Double.MAX_VALUE;
        MatrixEntry meNearestPoint = null;

        for (MatrixEntry meInner : lmeList2) {
            double dDistance = MinDistance(lmeList1, meInner);

            if (dDistance < dMinIDistance) {
                dMinIDistance = dDistance;
                meNearestPoint = meInner;
                meNearestPoint.dValue = dDistance;
            }

        }

        return meNearestPoint;
    }

    public static double NormMinIDistance(List<MatrixEntry> lmeMatrixEntry, MatrixEntry meRef, int[][] iaInput, int iThres) {

        Double dMinIDistance = null;

        for (MatrixEntry meInner : lmeMatrixEntry) {
            double dDistance = NormIDistance(meInner, meRef);

            if (dDistance < dMinIDistance && iaInput[meInner.i][meInner.j] >= iThres) {
                dMinIDistance = (Double) dDistance;
            }
        }

        return dMinIDistance;

    }

    public static List<MatrixEntry> getPointsWithMoreThanCountNeighAboveThres(int[][] iaGrey, int iCount, int iThresPoint, int iThres) {
        List<MatrixEntry> lmeReturnPoints = new ArrayList<MatrixEntry>();
        for (int i = 0; i < iaGrey.length; i++) {
            for (int j = 0; j < iaGrey[0].length; j++) {
                if (iaGrey[i][j] >= iThresPoint) {
                    int iCountAboveThres = 0;
                    for (int t = -1; t <= 1; t++) {
                        for (int z = -1; z <= 1; z++) {
                            if (!(t == 0 && z == 0) && Matrix.inBounds(iaGrey, j + z, i + t)) {
                                if (iaGrey[i + t][j + z] > iThres) {
                                    iCountAboveThres = iCountAboveThres + 1;
                                }
                            }
                        }
                    }
                    if (iCountAboveThres > iCount) {
                        lmeReturnPoints.add(new MatrixEntry(i, j));
                    }
                }
            }
        }
        return lmeReturnPoints;
    }

    public static List<MatrixEntry> getNeibhoursThres(int[][] iaGrey, MatrixEntry meReference, int iThres) {
        List<MatrixEntry> lmeReturnPoints = new ArrayList<MatrixEntry>();

        int i = meReference.i;
        int j = meReference.j;
        for (int t = -1; t <= 1; t++) {
            for (int z = -1; z <= 1; z++) {
                if (!(t == 0 && z == 0) && Matrix.inBounds(iaGrey, j + z, i + t) && iaGrey[i + t][j + z] > iThres) {
                    lmeReturnPoints.add(new MatrixEntry(i + t, j + z));
                }
            }
        }

        return lmeReturnPoints;
    }

    public static int CountEntriesInSetWOnull(List<List<MatrixEntry>> llme) {
        int iC = 0;

        for (List<MatrixEntry> lme : llme) {
            for (MatrixEntry me : lme) {
                if (me != null) {
                    iC++;
                }
            }
        }
        return iC;
    }

    public static int CountListInSetWOnull(List<List<MatrixEntry>> llme) {
        int iC = 0;

        for (List<MatrixEntry> lme : llme) {
            if (lme != null) {
                iC++;

            }
        }
        return iC;
    }
    
    public static void setPoints(List<MatrixEntry> lme, int[][] input, int iValue, int iRadius) {
        if (iRadius == 0) {
            for (MatrixEntry me : lme) {
                if (me.i >= 0 && me.j >= 0 && me.i < input.length && me.j < input[0].length) {
                    input[me.i][me.j] = iValue;
                }
            }
        } else {
            for (MatrixEntry meO : lme) {
                if (meO.i >= 0 && meO.j >= 0 && meO.i < input.length && meO.j < input[0].length) {
                    List<MatrixEntry> expand = getsubArea(meO.i, meO.j, iRadius, input);
                    for (MatrixEntry me : expand) {
                        input[me.i][me.j] = iValue;
                    }
                }
            }
        }
    }
    
    public static List<MatrixEntry> getsubArea(int iCenter, int jCenter, int iRadius, int[][] input) {
        List<MatrixEntry> lmeReturn = new ArrayList<>();
        for (int i = Math.max(iCenter - iRadius, 0); i < Math.min(iCenter + iRadius, input.length); i++) {
            for (int j = Math.max(jCenter - iRadius, 0); j < Math.min(jCenter + iRadius, input[0].length); j++) {
                lmeReturn.add(new MatrixEntry(i, j, input[i][j]));
            }
        }
        return lmeReturn;
    }
    
    public static interface OperationME {        
        public MatrixEntry perform(MatrixEntry me, MatrixEntry me1);        
    }
        

    /*      static public int[][] getArea(int[][] array, OrderedPair leftUp, OrderedPair rightDown){
    
     int deltax = (int)(rightDown.y-leftUp.y)+1;
     int deltay = (int)(rightDown.x-leftUp.x)+1;
    
     System.out.println(deltax + " " + deltay);
    
     int[][] returnarray = new int[deltay][deltax];
    
    
     int up=(int)leftUp.y;
     int left = (int)leftUp.x;
    
     for (int i=0; i<deltay;i++){
    
     for(int j=0; j<deltax; j++){
     returnarray[i][j]=array[up+i][left+j];
     }
     }
    
     return returnarray;
    
     }*/
}
