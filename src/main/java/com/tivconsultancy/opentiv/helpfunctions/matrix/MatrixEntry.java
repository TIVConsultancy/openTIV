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

import com.tivconsultancy.opentiv.helpfunctions.strings.StringWorker;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ziegen60
 */
public class MatrixEntry implements Serializable, Additionable<MatrixEntry>,Substractable<MatrixEntry> ,Normable<MatrixEntry> ,Multipliable<MatrixEntry> , Position, Value {

    private static final long serialVersionUID = 111132151L;
    
    public int i;
    public int j;
    public double dValue;

    public MatrixEntry(int i, int j) {

        this.i = i;
        this.j = j;

    }

    public MatrixEntry(int i, int j, double dVal) {
        this.i = i;
        this.j = j;
        this.dValue = dVal;
    }

    public MatrixEntry(double i, double j) {

        this.i = (int) i;
        this.j = (int) j;

    }
    
    public MatrixEntry(String standardString) {
        String[] splits = standardString.split(",");
        this.i = Integer.valueOf(splits[0]);
        this.j = Integer.valueOf(splits[1]);

    }

    public static List<MatrixEntry> doUniqueME(List<MatrixEntry> lme) {
        boolean bCheck = false;

        List<MatrixEntry> opErase = new ArrayList<MatrixEntry>();

        for (MatrixEntry meOuter : lme) {
            if (!(opErase.contains(meOuter))) {
                int iCount = 0;
                for (MatrixEntry meInner : lme) {
                    if (meOuter.equals(meInner)) {
                        iCount++;
                        if (iCount > 1) {
                            opErase.add(meInner);
                        }
                    }
                }
            }
        }

        lme.removeAll(opErase);
        return lme;

    }

    public MatrixEntry(MatrixEntry me) {
        if (me != null) {
            this.i = me.i;
            this.j = me.j;
        }
    }
    
    public MatrixEntry(OrderedPair me) {
        if (me != null) {
            this.i = (int) me.y;
            this.j = (int) me.x;
        }
    }

    public MatrixEntry(String sLine, String sDelimiter) {

        super();

        List<String> lsMatrixEntries = new ArrayList<String>();

        lsMatrixEntries = StringWorker.seperateTwoElements(sLine, sDelimiter);

        this.i = Integer.valueOf(StringWorker.clean(lsMatrixEntries.get(0)));
        this.j = Integer.valueOf(StringWorker.clean(lsMatrixEntries.get(1)));

    }

    public MatrixEntry() {
        super();

        this.i = Integer.MIN_VALUE;
        this.j = Integer.MIN_VALUE;
    }

    public double SecondCartesian() {

        double SecondCart = Math.sqrt((this.i * this.i) + (this.j * this.j));

        return SecondCart;
    }

    public double getAngle(MatrixEntry me) {
        double dY = -1.0 * (this.i - me.i);
        double dX = (this.j - me.j);
        return Math.atan2(dY, dX);
    }

    public int[] getSign() {
        int[] iReturn = new int[2];
        if (this.i >= 0) {
            iReturn[0] = 1;
        } else {
            iReturn[0] = -1;
        }
        if (this.j >= 0) {
            iReturn[1] = 1;
        } else {
            iReturn[1] = -1;
        }
        return iReturn;
    }

    public boolean compare(MatrixEntry meToCompare) {

        boolean bIsBigger = false;

        if ((meToCompare.i > this.i) && (meToCompare.j > this.j)) {
            bIsBigger = true;
        }

        return bIsBigger;

    }

    public boolean compareWeak(MatrixEntry meToCompare) {

        boolean bIsBigger = false;

        if ((meToCompare.i >= this.i) && (meToCompare.j >= this.j)) {
            bIsBigger = true;
        }

        return bIsBigger;

    }

    public static MatrixEntry getMaxIJPoint(List<MatrixEntry> laInputPoints) {

        MatrixEntry meMaxIJ = new MatrixEntry(0, 0);

        for (MatrixEntry a : laInputPoints) {

            if (a.i > meMaxIJ.i) {
                meMaxIJ.i = a.i;
            }

            if (a.j > meMaxIJ.j) {
                meMaxIJ.j = a.j;
            }

        }

        return meMaxIJ;

    }

    public boolean equalsMatrixEntry(MatrixEntry meEntry) {

        if (this.i == meEntry.i && this.j == meEntry.j) {
            return true;
        } else {
            return false;
        }

    }

    public static double IMinusJ(MatrixEntry me) {

        double iSub = me.i - me.j;

        return iSub;

    }

    public static double SecondCartesian(MatrixEntry opInput, MatrixEntry opReference) {

        double SecondCart = Math.sqrt((((double) (opInput.j - opReference.j)) * ((double) (opInput.j - opReference.j))) + (((double) (opInput.i - opReference.i)) * ((double) (opInput.i - opReference.i))));

        return SecondCart;
    }

    public void switchEntry() {

        double itemp = this.i;

        this.i = this.j;
        this.j = (int) itemp;

    }

    public static MatrixEntry addition(MatrixEntry me1, MatrixEntry me2) {

        MatrixEntry oReturn = new MatrixEntry(me1.i + me2.i, me1.j + me2.j);

        return oReturn;

    }

    public static MatrixEntry substraction(MatrixEntry me1, MatrixEntry me2) {

        MatrixEntry oReturn = new MatrixEntry(me1.i - me2.i, me1.j - me2.j);

        return oReturn;

    }

    public void substraction(MatrixEntry me1) {

        this.i = this.i - me1.i;
        this.j = this.j - me1.j;

    }

    public void multiScalar(MatrixEntry me1, int iScalar) {

        this.i = this.i * iScalar;
        this.j = this.j * iScalar;

    }

    public boolean isBigger(MatrixEntry meEntry) {

        boolean bIsBigger = true;

        if ((this.i <= meEntry.i) && (this.j <= meEntry.j)) {
            bIsBigger = false;
        }

        return bIsBigger;

    }

    public static boolean isPositive(MatrixEntry me) {

        boolean bReturn = false;

        if (me.i > 0 && me.j > 0) {
            bReturn = true;
        }

        return bReturn;

    }

    public static boolean isInRectangle(MatrixEntry meStart, MatrixEntry meEnd, MatrixEntry meToProve) {

        boolean iComp = false;
        boolean jComp = false;

        if ((meStart.i <= meToProve.i) && (meEnd.i >= meToProve.i)) {
            iComp = true;
        }

        if ((meStart.j <= meToProve.j) && (meEnd.j >= meToProve.j)) {
            jComp = true;
        }

        return ((iComp) && (jComp));

    }

    public boolean isInVicinity(MatrixEntry me, double dVicinity) {
        return this.SecondCartesian(me) < dVicinity;
    }

    public static boolean isAtBorderRectangle(MatrixEntry meStart, MatrixEntry meEnd, MatrixEntry meToProve) {

        boolean iComp = false;
        boolean jComp = false;

        if ((meStart.i <= meToProve.i + 1) && (meEnd.i >= meToProve.i - 1)) {
            iComp = true;
        }

        if ((meStart.j <= meToProve.j + 1) && (meEnd.j >= meToProve.j - 1)) {
            jComp = true;
        }

        return ((iComp) && (jComp));

    }

    public static boolean isAtBorderRectangle(int iStarti, int iStartj, int iEndi, int iEndj, int iProvei, int iProvej) {
        boolean iComp = false;
        boolean jComp = false;

        if ((iStarti <= iProvei + 1) && (iEndi >= iProvei - 1)) {
            iComp = true;
        }

        if ((iStartj <= iProvej + 1) && (iEndj >= iProvej - 1)) {
            jComp = true;
        }

        return ((iComp) && (jComp));
    }

    public static boolean isAtBorderWithoutCorner(MatrixEntry meStart, MatrixEntry meEnd, MatrixEntry meToProveStart, MatrixEntry meToProveEnd) {

        boolean AtBorder = isAtBorderRectangle(meStart, meEnd, meToProveStart) || isAtBorderRectangle(meStart, meEnd, meToProveEnd);

        boolean onCorner = false;

        if ((meStart.i == meToProveEnd.i + 1) && (meStart.j == meToProveEnd.j + 1)) {
            onCorner = true;
        }

        if ((meStart.i == meToProveEnd.i + 1) && (meEnd.j == meToProveStart.j - 1)) {
            onCorner = true;
        }

        if ((meEnd.i == meToProveStart.i - 1) && (meEnd.j == meToProveStart.j - 1)) {
            onCorner = true;
        }

        if ((meEnd.i == meToProveStart.i - 1) && (meStart.j == meToProveEnd.j + 1)) {
            onCorner = true;
        }

        return (!(onCorner) && (AtBorder));

    }

    public static boolean isAtBorder(MatrixEntry meStart, MatrixEntry meEnd, MatrixEntry meToProveStart, MatrixEntry meToProveEnd) {

        boolean AtBorder = isAtBorderRectangle(meStart, meEnd, meToProveStart) || isAtBorderRectangle(meStart, meEnd, meToProveEnd);

        return (AtBorder);

    }

    public static boolean isAtBorder(int iStarti, int iStartj, int iEndi, int iEndj, int iStartProvei, int iStartProvej, int iEndProvei, int iEndProvej) {

        boolean AtBorder = isAtBorderRectangle(iStarti, iStartj, iEndi, iEndj, iStartProvei, iStartProvej) || isAtBorderRectangle(iStarti, iStartj, iEndi, iEndj, iEndProvei, iEndProvej);

        return (AtBorder);

    }

    public static boolean isRectangleCollisionWithoutCorner(MatrixEntry meStart, MatrixEntry meEnd, MatrixEntry meToProveStart, MatrixEntry meToProveEnd) {

        boolean Collision = isInRectangle(meStart, meEnd, meToProveStart) || isInRectangle(meStart, meEnd, meToProveEnd);

        boolean onCorner = false;

        if (meStart.equals(meToProveEnd)) {
            onCorner = true;
        }

        if ((meStart.i == meToProveEnd.i) && (meEnd.j == meToProveStart.j)) {
            onCorner = true;
        }

        if (meEnd.equals(meToProveStart)) {
            onCorner = true;
        }

        if ((meEnd.i == meToProveStart.i) && (meStart.j == meToProveEnd.j)) {
            onCorner = true;
        }

        return ((Collision) && !(onCorner));

    }

    public static boolean isInRectangle(MatrixEntry meStart, MatrixEntry meEnd, MatrixEntry meToProveStart, MatrixEntry meToProveEnd) {

        boolean bInside = isInRectangle(meStart, meEnd, meToProveStart) && isInRectangle(meStart, meEnd, meToProveEnd);

        return (bInside);

    }

//    public void print(Integer[][] iaInput, int iCol) {
//        iaInput[this.i][this.j] = iCol;
//    }
    public void translate(MatrixEntry meTrans) {
        this.i = this.i + meTrans.i;
        this.j = this.j + meTrans.j;
    }

    public double SecondCartesian(MatrixEntry meReference) {

        double SecondCart = Math.sqrt(((this.i - meReference.i) * (this.i - meReference.i)) + ((this.j - meReference.j) * (this.j - meReference.j)));

        return SecondCart;
    }

    public double SecondCartesian(OrderedPair opReference) {

        double SecondCart = Math.sqrt(((this.i - opReference.y) * (this.i - opReference.y)) + ((this.j - opReference.x) * (this.j - opReference.x)));

        return SecondCart;
    }

    public static boolean isInList(List<MatrixEntry> lmeInput, MatrixEntry meToProve) {
        boolean bCheck = false;

        for (MatrixEntry me : lmeInput) {
            if (me.equalsMatrixEntry(meToProve)) {
                bCheck = true;
                break;
            }
        }

        return bCheck;

    }

    public static boolean isConnected(List<MatrixEntry> lmeInput, MatrixEntry meIn) {

        for (MatrixEntry me : lmeInput) {
            if (isConnected(me, meIn)) {
                return true;
            }
        }

        return false;

    }

    public static boolean isConnected(MatrixEntry me1, MatrixEntry me2) {

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((me1.i + i == me2.i) && (me1.j + j == me2.j)) {
                    return true;
                }
            }
        }

        return false;

    }

    public static boolean touchBorder(List<MatrixEntry> lmeIn, int[][] iaMatrix, double dBorderDistance) {
        for (MatrixEntry me : lmeIn) {
            if (touchBorder(me, iaMatrix, dBorderDistance)) {
                return true;
            }
        }

        return false;
    }

    public static boolean touchBorder(MatrixEntry meIn, int[][] iaMatrix, double dBorderDistance) {
        if (iaMatrix[0].length == 0 || iaMatrix.length == 0) {
            return true;
        }
        if (meIn.i <= dBorderDistance) {
            return true;
        }
        if (meIn.j <= dBorderDistance) {
            return true;
        }
        if ((iaMatrix.length - meIn.i) <= dBorderDistance) {
            return true;
        }
        if ((iaMatrix[0].length - meIn.j) <= dBorderDistance) {
            return true;
        }

        return false;

    }

    public static MatrixEntry getMinJPos(List<MatrixEntry> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntry opMinJ = new MatrixEntry(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (MatrixEntry op : lop) {
            if (op.j < opMinJ.j) {
                opMinJ = op;
            }
        }

        return new MatrixEntry(opMinJ.j, opMinJ.i);
    }

    public static MatrixEntry getMaxJPos(List<MatrixEntry> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntry opMaxJ = new MatrixEntry(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntry op : lop) {
            if (op.j > opMaxJ.j) {
                opMaxJ = op;
            }
        }

        return new MatrixEntry(opMaxJ.j, opMaxJ.i);
    }

    public static MatrixEntry getMaxJPos_2(List<MatrixEntry> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntry opMaxJ = new MatrixEntry(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntry op : lop) {
            if (op.j > opMaxJ.j) {
                opMaxJ = op;
            }
        }

        return opMaxJ;
    }

    public static MatrixEntry getMinIPos(List<MatrixEntry> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntry opMinI = new MatrixEntry(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (MatrixEntry op : lop) {
            if (op.i < opMinI.i) {
                opMinI = op;
            }
        }

        return new MatrixEntry(opMinI.j, opMinI.i);
    }

    public static MatrixEntry getMaxIPos(List<MatrixEntry> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntry opMaxI = new MatrixEntry(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntry op : lop) {
            if (op.i > opMaxI.i) {
                opMaxI = op;
            }
        }

        return new MatrixEntry(opMaxI.j, opMaxI.i);
    }

    public static MatrixEntry getMaxIPos_2(List<MatrixEntry> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntry opMaxI = new MatrixEntry(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntry op : lop) {
            if (op.i > opMaxI.i) {
                opMaxI = op;
            }
        }

        return opMaxI;
    }

    public MatrixEntry ijDistance(MatrixEntry meReference) {
        return new MatrixEntry(meReference.i - this.i, meReference.j - this.j);
    }

    @Override
    public boolean equals(Object Other) {

        if (Other instanceof MatrixEntry) {
            MatrixEntry meInput = (MatrixEntry) Other;
            return (this.equalsMatrixEntry(meInput));
        } else {
            return (this.equals(Other));
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.i) ^ (Double.doubleToLongBits(this.i) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.j) ^ (Double.doubleToLongBits(this.j) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.dValue) ^ (Double.doubleToLongBits(this.dValue) >>> 32));
        return hash;
    }

//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 89 * hash + this.i;
//        hash = 89 * hash + this.j;
//        return hash;
//    }
    public String toString(String sDelimiter) {

        String sReturn = "";

        sReturn = sReturn + this.i + sDelimiter + this.j;

        return sReturn;

    }

    public static List<String> toString(List<MatrixEntry> lme, String sDelimiter) {

        List<String> ls = new ArrayList<String>();

        for (MatrixEntry me : lme) {
            ls.add(me.i + sDelimiter + me.j);
        }

        return ls;

    }

    @Override
    public String toString() {

        String sReturn = "i: " + this.i + " j: " + this.j;

        return sReturn;

    }
    
    public OrderedPair toOrderedPair(){
        return new OrderedPair(this.j, this.i, this.dValue);
    }

    @Override
    public MatrixEntry add(MatrixEntry o2) {
        return (new MatrixEntry((this.i + o2.i), (this.j + o2.j), (this.dValue + o2.dValue)));
    }

    @Override
    public MatrixEntry add2(MatrixEntry o2, String sType) {
        if (sType.equals("j")) {
            return (new MatrixEntry(this.i, this.j + o2.j, this.dValue));
        }
        if (sType.equals("i")) {
            return (new MatrixEntry(this.i + o2.i, this.j, this.dValue));
        }
        if (sType.equals("Value")) {
            return (new MatrixEntry(this.i, this.j, this.dValue + o2.dValue));
        }
        if (sType.equals("ij")) {
            return (new MatrixEntry(this.i + o2.i, this.j + o2.j, this.dValue));
        }

        throw new UnsupportedOperationException("Type of addition not supported");
    }

    @Override
    public MatrixEntry sub(MatrixEntry o2) {
        return (new MatrixEntry(this.i - o2.i, this.j - o2.j, this.dValue - o2.dValue));
    }

    @Override
    public MatrixEntry sub2(MatrixEntry o2, String sType) {
        if (sType.equals("j")) {
            return (new MatrixEntry(this.i, this.j - o2.j, this.dValue));
        }
        if (sType.equals("i")) {
            return (new MatrixEntry(this.i - o2.i, this.j, this.dValue));
        }
        if (sType.equals("Value")) {
            return (new MatrixEntry(this.i, this.j, this.dValue - o2.dValue));
        }
        if (sType.equals("ij")) {
            return (new MatrixEntry(this.i - o2.i, this.j - o2.j, this.dValue));
        }

        throw new UnsupportedOperationException("Type of addition not supported");
    }

    @Override
    public Double getNorm(MatrixEntry o2) {
        double SecondCart = Math.sqrt(((this.i - o2.i) * (this.i - o2.i)) + ((this.j - o2.j) * (this.j - o2.j)));

        return SecondCart;
    }

    @Override
    public Double getNorm2(MatrixEntry o2, String sNormType) {
        if (sNormType.equals("0")) {
            return getNorm(o2);
        }

        if (sNormType.equals("i")) {
            return 1.0 * (Math.abs(this.i - o2.i));
        }

        if (sNormType.equals("j")) {
            return 1.0 * (Math.abs(this.j - o2.j));
        }

        if (sNormType.equals("Value")) {
            return (Math.abs(this.dValue - o2.dValue));
        }

        if (sNormType.equals("X-axis")) { // Return angle between position vector and x-axis
            double di = (this.i - o2.i);
            double dj = (this.j - o2.j);
            double dAngle = (dj * dj) / (Math.abs(di) * Math.sqrt(dj * dj + di * di));
            return Math.acos(dAngle);
        }
        if (sNormType.equals("Y-axis")) { // Return angle between position vector and y-axis
            double di = (this.i - o2.i);
            double dj = (this.j - o2.j);
            double dAngle = (di * di) / (Math.abs(dj) * Math.sqrt(dj * dj + di * di));
            return Math.acos(dAngle);
        }
        if (sNormType.equals("Min")) { // Return angle between position vector and y-axis
            double di = (this.i - o2.i);
            double dj = (this.j - o2.j);
            return Math.min(di, dj);
        }
        if (sNormType.equals("Max")) { // Return angle between position vector and y-axis
            double di = (this.i - o2.i);
            double dj = (this.j - o2.j);
            return Math.max(di, dj);
        }
        try {
            double dPow = Double.valueOf(sNormType);
            double SecondCartesian = Math.pow((Math.pow(Math.abs(this.j - o2.j), dPow) + Math.pow(Math.abs(this.i - o2.i), dPow)), 1.0 / dPow);
            return SecondCartesian;
        } catch (Exception e) {
        }

        throw new UnsupportedOperationException("Class: getNorm2(): Type of norm not supported");
    }

    @Override
    public MatrixEntry mult(Double d) {
        return (new MatrixEntry((int) (this.i * d), (int) (this.j * d), this.dValue));
    }

    @Override
    public MatrixEntry mult2(Double d, String sType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getPosX() {
        return this.j * 1.0;
    }

    @Override
    public double getPosY() {
        return this.i * 1.0;
    }

    @Override
    public Double getValue(Object pParameter) {
        return this.dValue;
    }

    public static MatrixEntry valuOf(OrderedPair op) {
        if (op == null) {
            return null;
        }
        if (op.dValue != null) {
            return new MatrixEntry((int) op.y, (int) op.x, op.dValue);
        }
        return new MatrixEntry((int) op.y, (int) op.x);
    }
    
    public static MatrixEntry valuOf(ImagePoint op) {
        if (op == null) {
            return null;
        }
        
       return new MatrixEntry(op.getPos());
    }
    
    public String getStandardString(){
        return this.i + "," + this.j;
    }

}
