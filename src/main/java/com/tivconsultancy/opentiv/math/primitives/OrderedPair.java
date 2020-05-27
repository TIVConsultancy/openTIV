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
package com.tivconsultancy.opentiv.math.primitives;

import com.tivconsultancy.opentiv.math.interfaces.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class OrderedPair implements Serializable, Additionable<OrderedPair>,Substractable<OrderedPair> ,Normable<OrderedPair> ,Multipliable<OrderedPair> , Position, Value {

    private static final long serialVersionUID = -2432094969948054078L;

    public double x;
    public double y;
    public Double dValue;

    public OrderedPair() {
        x = Double.MIN_VALUE;
        y = Double.MIN_VALUE;
        dValue = Double.MIN_VALUE;
    }

    public OrderedPair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public OrderedPair(double x, double y, Double dValue) {
        this.x = x;
        this.y = y;
        this.dValue = dValue;
    }

    public OrderedPair(OrderedPair p) {
        super();
        if (p != null) {
            this.x = p.x;
            this.y = p.y;
            this.dValue = p.dValue;
        }
    }

    public OrderedPair(OrderedPair p, double deltaX, double deltaY) {
        super();
        if (p != null) {
            this.x = p.x + deltaX;
            this.y = p.y + deltaY;
            this.dValue = p.dValue;
        }
    }

    public boolean checkIfNaN() {
        if (Double.isNaN(this.x)) {
            return true;
        }

        if (Double.isNaN(this.y)) {
            return true;
        }

        return Double.isNaN(this.dValue);
    }

    public static double Angle(OrderedPair op1, OrderedPair op2) {
        double dAngle = (op1.x * op2.x + op1.y * op2.y) / (op1.getNorm2(null, "0") * op2.getNorm2(null, "0"));
        return Math.acos(dAngle);
    }

    public static List<Integer> sortToNearest(List<OrderedPair> lop, OrderedPair op) {
        throw new UnsupportedOperationException("Revise");
//        List<OrderedPair> lopDiff = new ArrayList<OrderedPair>();
//        for (int i = 0; i < lop.size(); i++) {
//            OrderedPair opI = lop.get(i);
//            lopDiff.add(new OrderedPair(i, i, opI.SecondCartesian(op)));
//        }
//        Collections.sort(lopDiff, new ValueOrderedPairComparator());
//        List<Integer> li = new ArrayList<Integer>();
//        for (OrderedPair opDiff : lopDiff) {
//            li.add((int) opDiff.x);
//        }
//        return li;
    }

    @Override
    public String toString() {
        return ("x: " + x + " y: " + y + " Value: " + dValue);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderedPair)) {
            return false;
        }
        OrderedPair op = (OrderedPair) o;
        if (op.x == this.x && op.y == this.y && op.dValue == this.dValue) {
            return (true);
        } else {
            return (false);
        }
    }

    public boolean equals(Object o, Comparison oSide) {
        if (!(o instanceof OrderedPair)) {
            return false;
        }
        OrderedPair op = (OrderedPair) o;
        if (oSide.compare(op, this) == 0) {
            return (true);
        } else {
            return (false);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.dValue) ^ (Double.doubleToLongBits(this.dValue) >>> 32));
        return hash;
    }

    public void rotateAroundReference(OrderedPair opReference, double dAlpha) {
        double dTempX = this.x - opReference.x;
        double dTempY = this.y - opReference.y;

        this.x = (dTempX * Math.cos(dAlpha) - dTempY * Math.sin(dAlpha)) + opReference.x;
        this.y = (dTempX * Math.sin(dAlpha) + dTempY * Math.cos(dAlpha)) + opReference.y;
    }

    public void rotateAroundZero(double dAlpha) {
        double dTempX = this.x;
        double dTempY = this.y;

        this.x = (dTempX * Math.cos(dAlpha) - dTempY * Math.sin(dAlpha));
        this.y = (dTempX * Math.sin(dAlpha) + dTempY * Math.cos(dAlpha));
    }

//    public OrderedPair getVariance(List<OrderedPair> lop, OrderedPair dRef){
//        List<Double> ldX = new ArrayList<Double>();
//        List<Double> ldY = new ArrayList<Double>();
//        List<Double> ldValue = new ArrayList<Double>();
//        for(OrderedPair op : lop){
//            ldX.add(op.x);
//            ldY.add(op.y);
//            ldValue.add(op.dValue);
//        }
//        AveragePackage.avgArithmetic.getVariance(daInput, dRef);
//    }
    @Override
    public OrderedPair add(OrderedPair o2) {
        return (new OrderedPair((this.x + o2.x), (this.y + o2.y), (this.dValue + o2.dValue)));
    }

    @Override
    public OrderedPair add2(OrderedPair o2, String sType) {
        if (sType.equals("x")) {
            return (new OrderedPair(this.x + o2.x, this.y, this.dValue));
        }
        if (sType.equals("y")) {
            return (new OrderedPair(this.x, this.y + o2.y, this.dValue));
        }
        if (sType.equals("Value")) {
            return (new OrderedPair(this.x, this.y, this.dValue + o2.dValue));
        }
        if (sType.equals("xy")) {
            return (new OrderedPair(this.x + o2.x, this.y + o2.y, this.dValue));
        }

        throw new UnsupportedOperationException("Type of addition not supported");

    }

    @Override
    public OrderedPair sub(OrderedPair o2) {
        if(this.dValue == null || o2.dValue == null){
            return (new OrderedPair(this.x - o2.x, this.y - o2.y, null));
        }
        return (new OrderedPair(this.x - o2.x, this.y - o2.y, this.dValue - o2.dValue));
    }

    @Override
    public OrderedPair sub2(OrderedPair o2, String sType) {
        if (sType.equals("x")) {
            return (new OrderedPair(this.x - o2.x, this.y, this.dValue));
        }
        if (sType.equals("y")) {
            return (new OrderedPair(this.x, this.y - o2.y, this.dValue));
        }
        if (sType.equals("Value")) {
            return (new OrderedPair(this.x, this.y, this.dValue - o2.dValue));
        }
        if (sType.equals("xy")) {
            return (new OrderedPair(this.x - o2.x, this.y - o2.y, this.dValue));
        }

        throw new UnsupportedOperationException("Type of addition not supported");
    }

    @Override
    public Double getNorm(OrderedPair o2) {
        if (o2 == null) {
            return Math.sqrt(((this.x - 0.0) * (this.x - 0.0)) + ((this.y - 0.0) * (this.y - 0.0)));
        }
        double SecondCartesian = Math.sqrt(((this.x - o2.x) * (this.x - o2.x)) + ((this.y - o2.y) * (this.y - o2.y)));
        return SecondCartesian;
    }

    @Override
    public Double getNorm2(OrderedPair o2, String sNormType) {

        if (sNormType.equals("0")) {
            double SecondCartesian = Math.sqrt((this.x * this.x) + (this.y * this.y));
            return SecondCartesian;
        }

        if (sNormType.equals("x")) {
            return (Math.abs(this.x - o2.x));
        }

        if (sNormType.equals("y")) {
            return (Math.abs(this.y - o2.y));
        }

        if (sNormType.equals("Value")) {
            return (Math.abs(this.dValue - o2.dValue));
        }

        if (sNormType.equals("X-axis")) { // Return angle between position vector and x-axis
            double dY = (this.y - o2.y);
            double dX = (this.x - o2.x);
            double dAngle = (dX * dX) / (Math.abs(dX) * Math.sqrt(dX * dX + dY * dY));
            return Math.acos(dAngle);
        }
        if (sNormType.equals("Y-axis")) { // Return angle between position vector and y-axis
            double dY = (this.y - o2.y);
            double dX = (this.x - o2.x);
            double dAngle = (dY * dY) / (Math.abs(dY) * Math.sqrt(dX * dX + dY * dY));
            return Math.acos(dAngle);
        }
        if (sNormType.equals("Min")) { // Return angle between position vector and y-axis
            double dY = (this.y - o2.y);
            double dX = (this.x - o2.x);
            return Math.min(dY, dX);
        }
        try {
            double dPow = Double.valueOf(sNormType);
            double SecondCartesian = Math.pow((Math.pow(Math.abs(this.x - o2.x), dPow) + Math.pow(Math.abs(this.y - o2.y), dPow)), 1.0 / dPow);
            return SecondCartesian;
        } catch (Exception e) {
        }

        throw new UnsupportedOperationException("Class: OrderedPair, getNorm2(): Type of norm not supported");
    }

    @Override
    public OrderedPair mult(Double d) {
        return new OrderedPair(this.x * d, this.y * d, this.dValue);
    }

    @Override
    public OrderedPair mult2(Double d, String sType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderedPair clone() {
        return new OrderedPair(this.x, this.y, this.dValue);
    }

    @Override
    public double getPosX() {
        return this.x;
    }

    @Override
    public double getPosY() {
        return this.y;
    }

    @Override
    public Double getValue(Object pParameter) {
        return this.dValue;
    }

}
