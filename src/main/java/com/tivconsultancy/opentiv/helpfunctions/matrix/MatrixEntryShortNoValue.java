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
import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ziegen60
 */
public class MatrixEntryShortNoValue implements Serializable, Additionable<MatrixEntryShortNoValue>,Substractable<MatrixEntryShortNoValue>,Normable<MatrixEntryShortNoValue> ,Multipliable<MatrixEntryShortNoValue>, Position, Value {

    private static final long serialVersionUID = -4865201592031946577L;

    public short i;
    public short j;

    public MatrixEntryShortNoValue(short i, short j) {
        this.i = i;
        this.j = j;
    }

    public MatrixEntryShortNoValue(short i, short j, double dVal) {
        this.i = i;
        this.j = j;
    }

    public MatrixEntryShortNoValue(double i, double j) {

        this.i = (short) i;
        this.j = (short) j;

    }

    public static List<MatrixEntryShortNoValue> doUniqueME(List<MatrixEntryShortNoValue> lme) {
        boolean bCheck = false;

        List<MatrixEntryShortNoValue> opErase = new ArrayList<MatrixEntryShortNoValue>();

        for (MatrixEntryShortNoValue meOuter : lme) {
            if (!(opErase.contains(meOuter))) {
                int iCount = 0;
                for (MatrixEntryShortNoValue meInner : lme) {
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

    public MatrixEntryShortNoValue(MatrixEntryShortNoValue me) {
        if (me != null) {
            this.i = me.i;
            this.j = me.j;
        }
    }
    
    public MatrixEntryShortNoValue(OrderedPair me) {
        if (me != null) {
            this.i = (short) me.y;
            this.j = (short) me.x;
        }
    }

    public MatrixEntryShortNoValue(String sLine, String sDelimiter) {

        super();

        List<String> lsMatrixEntries = new ArrayList<String>();

        lsMatrixEntries = StringWorker.seperateTwoElements(sLine, sDelimiter);

        this.i = Short.valueOf(StringWorker.clean(lsMatrixEntries.get(0)));
        this.j = Short.valueOf(StringWorker.clean(lsMatrixEntries.get(1)));

    }

    public MatrixEntryShortNoValue() {
        super();

        this.i = Short.MIN_VALUE;
        this.j = Short.MIN_VALUE;
    }

    public double SecondCartesian() {

        double SecondCart = Math.sqrt((this.i * this.i) + (this.j * this.j));

        return SecondCart;
    }

    public double getAngle(MatrixEntryShortNoValue me) {
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

    public boolean compare(MatrixEntryShortNoValue meToCompare) {

        boolean bIsBigger = false;

        if ((meToCompare.i > this.i) && (meToCompare.j > this.j)) {
            bIsBigger = true;
        }

        return bIsBigger;

    }

    public boolean compareWeak(MatrixEntryShortNoValue meToCompare) {

        boolean bIsBigger = false;

        if ((meToCompare.i >= this.i) && (meToCompare.j >= this.j)) {
            bIsBigger = true;
        }

        return bIsBigger;

    }

    public static MatrixEntryShortNoValue getMaxIJPoint(List<MatrixEntryShortNoValue> laInputPoints) {

        MatrixEntryShortNoValue meMaxIJ = new MatrixEntryShortNoValue(0, 0);

        for (MatrixEntryShortNoValue a : laInputPoints) {

            if (a.i > meMaxIJ.i) {
                meMaxIJ.i = a.i;
            }

            if (a.j > meMaxIJ.j) {
                meMaxIJ.j = a.j;
            }

        }

        return meMaxIJ;

    }

    public boolean equalsMatrixEntry(MatrixEntryShortNoValue meEntry) {

        if (this.i == meEntry.i && this.j == meEntry.j) {
            return true;
        } else {
            return false;
        }

    }

    public static double IMinusJ(MatrixEntryShortNoValue me) {

        double iSub = me.i - me.j;

        return iSub;

    }

    public static double SecondCartesian(MatrixEntryShortNoValue opInput, MatrixEntryShortNoValue opReference) {

        double SecondCart = Math.sqrt((((double) (opInput.j - opReference.j)) * ((double) (opInput.j - opReference.j))) + (((double) (opInput.i - opReference.i)) * ((double) (opInput.i - opReference.i))));

        return SecondCart;
    }

    public void switchEntry() {

        double itemp = this.i;

        this.i = this.j;
        this.j = (short) itemp;

    }

    public static MatrixEntryShortNoValue addition(MatrixEntryShortNoValue me1, MatrixEntryShortNoValue me2) {

        MatrixEntryShortNoValue oReturn = new MatrixEntryShortNoValue(me1.i + me2.i, me1.j + me2.j);

        return oReturn;

    }

    public static MatrixEntryShortNoValue substraction(MatrixEntryShortNoValue me1, MatrixEntryShortNoValue me2) {

        MatrixEntryShortNoValue oReturn = new MatrixEntryShortNoValue(me1.i - me2.i, me1.j - me2.j);

        return oReturn;

    }

    public void substraction(MatrixEntryShortNoValue me1) {

        this.i = (short) (this.i - me1.i);
        this.j = (short) (this.j - me1.j);

    }

    public void multiScalar(MatrixEntryShortNoValue me1, short iScalar) {

        this.i = (short) (this.i * iScalar);
        this.j = (short) (this.j * iScalar);

    }

    public boolean isBigger(MatrixEntryShortNoValue meEntry) {

        boolean bIsBigger = true;

        if ((this.i <= meEntry.i) && (this.j <= meEntry.j)) {
            bIsBigger = false;
        }

        return bIsBigger;

    }

    public static boolean isPositive(MatrixEntryShortNoValue me) {

        boolean bReturn = false;

        if (me.i > 0 && me.j > 0) {
            bReturn = true;
        }

        return bReturn;

    }

    public static boolean isInRectangle(MatrixEntryShortNoValue meStart, MatrixEntryShortNoValue meEnd, MatrixEntryShortNoValue meToProve) {

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

    public boolean isInVicinity(MatrixEntryShortNoValue me, double dVicinity) {
        return this.SecondCartesian(me) < dVicinity;
    }

    public static boolean isAtBorderRectangle(MatrixEntryShortNoValue meStart, MatrixEntryShortNoValue meEnd, MatrixEntryShortNoValue meToProve) {

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

    public static boolean isAtBorderWithoutCorner(MatrixEntryShortNoValue meStart, MatrixEntryShortNoValue meEnd, MatrixEntryShortNoValue meToProveStart, MatrixEntryShortNoValue meToProveEnd) {

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

    public static boolean isAtBorder(MatrixEntryShortNoValue meStart, MatrixEntryShortNoValue meEnd, MatrixEntryShortNoValue meToProveStart, MatrixEntryShortNoValue meToProveEnd) {

        boolean AtBorder = isAtBorderRectangle(meStart, meEnd, meToProveStart) || isAtBorderRectangle(meStart, meEnd, meToProveEnd);

        return (AtBorder);

    }

    public static boolean isAtBorder(int iStarti, int iStartj, int iEndi, int iEndj, int iStartProvei, int iStartProvej, int iEndProvei, int iEndProvej) {

        boolean AtBorder = isAtBorderRectangle(iStarti, iStartj, iEndi, iEndj, iStartProvei, iStartProvej) || isAtBorderRectangle(iStarti, iStartj, iEndi, iEndj, iEndProvei, iEndProvej);

        return (AtBorder);

    }

    public static boolean isRectangleCollisionWithoutCorner(MatrixEntryShortNoValue meStart, MatrixEntryShortNoValue meEnd, MatrixEntryShortNoValue meToProveStart, MatrixEntryShortNoValue meToProveEnd) {

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

    public static boolean isInRectangle(MatrixEntryShortNoValue meStart, MatrixEntryShortNoValue meEnd, MatrixEntryShortNoValue meToProveStart, MatrixEntryShortNoValue meToProveEnd) {

        boolean bInside = isInRectangle(meStart, meEnd, meToProveStart) && isInRectangle(meStart, meEnd, meToProveEnd);

        return (bInside);

    }

//    public void print(Integer[][] iaInput, int iCol) {
//        iaInput[this.i][this.j] = iCol;
//    }
    public void translate(MatrixEntryShortNoValue meTrans) {
        this.i = (short) (this.i + meTrans.i);
        this.j = (short) (this.j + meTrans.j);
    }

    public double SecondCartesian(MatrixEntryShortNoValue meReference) {

        double SecondCart = Math.sqrt(((this.i - meReference.i) * (this.i - meReference.i)) + ((this.j - meReference.j) * (this.j - meReference.j)));

        return SecondCart;
    }

    public double SecondCartesian(OrderedPair opReference) {

        double SecondCart = Math.sqrt(((this.i - opReference.y) * (this.i - opReference.y)) + ((this.j - opReference.x) * (this.j - opReference.x)));

        return SecondCart;
    }

    public static boolean isInList(List<MatrixEntryShortNoValue> lmeInput, MatrixEntryShortNoValue meToProve) {
        boolean bCheck = false;

        for (MatrixEntryShortNoValue me : lmeInput) {
            if (me.equalsMatrixEntry(meToProve)) {
                bCheck = true;
                break;
            }
        }

        return bCheck;

    }

    public static boolean isConnected(List<MatrixEntryShortNoValue> lmeInput, MatrixEntryShortNoValue meIn) {

        for (MatrixEntryShortNoValue me : lmeInput) {
            if (isConnected(me, meIn)) {
                return true;
            }
        }

        return false;

    }

    public static boolean isConnected(MatrixEntryShortNoValue me1, MatrixEntryShortNoValue me2) {

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((me1.i + i == me2.i) && (me1.j + j == me2.j)) {
                    return true;
                }
            }
        }

        return false;

    }

    public static boolean touchBorder(List<MatrixEntryShortNoValue> lmeIn, int[][] iaMatrix, double dBorderDistance) {
        for (MatrixEntryShortNoValue me : lmeIn) {
            if (touchBorder(me, iaMatrix, dBorderDistance)) {
                return true;
            }
        }

        return false;
    }

    public static boolean touchBorder(MatrixEntryShortNoValue meIn, int[][] iaMatrix, double dBorderDistance) {
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

    public static MatrixEntryShortNoValue getMinJPos(List<MatrixEntryShortNoValue> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntryShortNoValue opMinJ = new MatrixEntryShortNoValue(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (MatrixEntryShortNoValue op : lop) {
            if (op.j < opMinJ.j) {
                opMinJ = op;
            }
        }

        return new MatrixEntryShortNoValue(opMinJ.j, opMinJ.i);
    }

    public static MatrixEntryShortNoValue getMaxJPos(List<MatrixEntryShortNoValue> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntryShortNoValue opMaxJ = new MatrixEntryShortNoValue(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntryShortNoValue op : lop) {
            if (op.j > opMaxJ.j) {
                opMaxJ = op;
            }
        }

        return new MatrixEntryShortNoValue(opMaxJ.j, opMaxJ.i);
    }

    public static MatrixEntryShortNoValue getMaxJPos_2(List<MatrixEntryShortNoValue> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntryShortNoValue opMaxJ = new MatrixEntryShortNoValue(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntryShortNoValue op : lop) {
            if (op.j > opMaxJ.j) {
                opMaxJ = op;
            }
        }

        return opMaxJ;
    }

    public static MatrixEntryShortNoValue getMinIPos(List<MatrixEntryShortNoValue> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntryShortNoValue opMinI = new MatrixEntryShortNoValue(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (MatrixEntryShortNoValue op : lop) {
            if (op.i < opMinI.i) {
                opMinI = op;
            }
        }

        return new MatrixEntryShortNoValue(opMinI.j, opMinI.i);
    }

    public static MatrixEntryShortNoValue getMaxIPos(List<MatrixEntryShortNoValue> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntryShortNoValue opMaxI = new MatrixEntryShortNoValue(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntryShortNoValue op : lop) {
            if (op.i > opMaxI.i) {
                opMaxI = op;
            }
        }

        return new MatrixEntryShortNoValue(opMaxI.j, opMaxI.i);
    }

    public static MatrixEntryShortNoValue getMaxIPos_2(List<MatrixEntryShortNoValue> lop) {

        if (lop.isEmpty()) {
            return null;
        }

        MatrixEntryShortNoValue opMaxI = new MatrixEntryShortNoValue(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (MatrixEntryShortNoValue op : lop) {
            if (op.i > opMaxI.i) {
                opMaxI = op;
            }
        }

        return opMaxI;
    }

    public MatrixEntryShortNoValue ijDistance(MatrixEntryShortNoValue meReference) {
        return new MatrixEntryShortNoValue(meReference.i - this.i, meReference.j - this.j);
    }

    @Override
    public boolean equals(Object Other) {

        if (Other instanceof MatrixEntryShortNoValue) {
            MatrixEntryShortNoValue meInput = (MatrixEntryShortNoValue) Other;
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

    public static List<String> toString(List<MatrixEntryShortNoValue> lme, String sDelimiter) {

        List<String> ls = new ArrayList<String>();

        for (MatrixEntryShortNoValue me : lme) {
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
        return new OrderedPair(this.j, this.i, 0.0);
    }

    @Override
    public MatrixEntryShortNoValue add(MatrixEntryShortNoValue o2) {
        return (new MatrixEntryShortNoValue(( (short) (this.i + o2.i)), (short) (this.j + o2.j)));
    }

    @Override
    public MatrixEntryShortNoValue add2(MatrixEntryShortNoValue o2, String sType) {
        if (sType.equals("j")) {
            return (new MatrixEntryShortNoValue(this.i, (short) (this.j + o2.j)));
        }
        if (sType.equals("i")) {
            return (new MatrixEntryShortNoValue((short) (this.i + o2.i), this.j));
        }
        if (sType.equals("Value")) {
            return (new MatrixEntryShortNoValue(this.i, this.j));
        }
        if (sType.equals("ij")) {
            return (new MatrixEntryShortNoValue((short) (this.i + o2.i), (short) (this.j + o2.j)));
        }

        throw new UnsupportedOperationException("Type of addition not supported");
    }

    @Override
    public MatrixEntryShortNoValue sub(MatrixEntryShortNoValue o2) {
        return (new MatrixEntryShortNoValue((short) (this.i - o2.i), (short) (this.j - o2.j)));
    }

    @Override
    public MatrixEntryShortNoValue sub2(MatrixEntryShortNoValue o2, String sType) {
        if (sType.equals("j")) {
            return (new MatrixEntryShortNoValue(this.i, (short) (this.j - o2.j)));
        }
        if (sType.equals("i")) {
            return (new MatrixEntryShortNoValue((short) (this.i - o2.i), this.j));
        }
        if (sType.equals("Value")) {
            return (new MatrixEntryShortNoValue(this.i, this.j));
        }
        if (sType.equals("ij")) {
            return (new MatrixEntryShortNoValue((short) (this.i - o2.i), (short) (this.j - o2.j)));
        }

        throw new UnsupportedOperationException("Type of addition not supported");
    }

    @Override
    public Double getNorm(MatrixEntryShortNoValue o2) {
        double SecondCart = Math.sqrt(((this.i - o2.i) * (this.i - o2.i)) + ((this.j - o2.j) * (this.j - o2.j)));

        return SecondCart;
    }

    @Override
    public Double getNorm2(MatrixEntryShortNoValue o2, String sNormType) {
        if (sNormType.equals("0")) {
            return getNorm(o2);
        }

        if (sNormType.equals("i")) {
            return 1.0 * (Math.abs(this.i - o2.i));
        }

        if (sNormType.equals("j")) {
            return 1.0 * (Math.abs(this.j - o2.j));
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
    public MatrixEntryShortNoValue mult(Double d) {
        return (new MatrixEntryShortNoValue((short) (this.i * d), (short) (this.j * d)));
    }

    @Override
    public MatrixEntryShortNoValue mult2(Double d, String sType) {
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
        return Double.MAX_VALUE;
    }

    public static MatrixEntryShortNoValue valuOf(OrderedPair op) {
        if (op == null) {
            return null;
        }
        if (op.dValue != null) {
            return new MatrixEntryShortNoValue((short) op.y, (short) op.x, op.dValue);
        }
        return new MatrixEntryShortNoValue((int) op.y, (int) op.x);
    }
    
    public MatrixEntry asMatrixEntry(){
        return new MatrixEntry(i, j);
    }
    
    public static List<MatrixEntryShortNoValue> asMatrixEntrySNO(List<MatrixEntry> lme){
        List<MatrixEntryShortNoValue> lmeSNO = new ArrayList<>();
        for(MatrixEntry me : lme){
            lmeSNO.add(new MatrixEntryShortNoValue(me.i, me.j));
        }
        return lmeSNO;
    }

}
