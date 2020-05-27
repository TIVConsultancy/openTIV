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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class OrderdTripplet implements Serializable {

    private static final long serialVersionUID = 4265610105250816641L;

    public double x;
    public double y;
    public double z;
    public double dValue;

    public double dChrono;

    public OrderdTripplet() {
        x = Double.MIN_VALUE;
        y = Double.MIN_VALUE;
        z = Double.MIN_VALUE;

    }

    public OrderdTripplet(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dValue = 0.0;
    }

    public OrderdTripplet(double x, double y, double z, double dValue) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dValue = dValue;
    }

    public OrderdTripplet(OrderdTripplet p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
        this.dValue = 0.0;

    }

    public OrderdTripplet(OrderdTripplet p, double deltax, double deltay, double deltaz) {
        this.x = p.x + deltax;
        this.y = p.y + deltay;
        this.z = p.z + deltaz;
        this.dValue = 0.0;

    }

    public OrderdTripplet(double[] daxyzTripplet) {
        this.x = daxyzTripplet[0];
        this.y = daxyzTripplet[1];
        this.z = daxyzTripplet[2];
        this.dValue = 0.0;
    }

    void setX(double x) {
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
    }

    public static void add(OrderdTripplet op1, OrderdTripplet op2) {

        op2.x = op2.x + op1.x;
        op2.y = op2.y + op1.y;
        op2.z = op2.z + op1.z;
    }

    public static void substract(OrderdTripplet op, OrderdTripplet opSubtrahend) {

        op.x = op.x - opSubtrahend.x;
        op.y = op.y - opSubtrahend.y;
        op.z = op.z - opSubtrahend.z;
    }

    public double SecondCartesian() {

        double SecondCart = Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));

        return SecondCart;
    }

    public double SecondCartesian(OrderdTripplet opReference) {

        double SecondCart = Math.sqrt(((this.x - opReference.x) * (this.x - opReference.x)) + ((this.y - opReference.y) * (this.y - opReference.y)) + ((this.z - opReference.z) * (this.z - opReference.z)));

        return SecondCart;
    }

    public static double SecondCartesian(OrderdTripplet opInput, OrderdTripplet opReference) {

        double SecondCart = Math.sqrt(((opInput.x - opReference.x) * (opInput.x - opReference.x)) + ((opInput.y - opReference.y) * (opInput.y - opReference.y)) + ((opInput.z - opReference.z) * (opInput.z - opReference.z)));

        return SecondCart;
    }

    void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double getX() {
        return (this.x);
    }

    double getY() {
        return (this.y);
    }

    @Override
    public String toString() {
        return (x + ", " + y + ", " + z + ", " + dValue);
    }

    public boolean equalsOrderedPair(OrderdTripplet obj) {

        if (obj.x == this.x && obj.y == this.y && obj.z == this.z) {
            return (true);
        } else {
            return (false);
        }
    }

    public static double getMaxX(List<?> loOrderedTripplet) {
        double dMaxX = (-1) * Double.MAX_VALUE;
        for (Object o : loOrderedTripplet) {
            double dXPos = ((OrderdTripplet) o).x;
            if (dMaxX < dXPos) {
                dMaxX = dXPos;
            }
        }

        return dMaxX;
    }
    
    public static double getMaxValue(List<?> loOrderedTripplet) {
        double dMaxValue = (-1) * Double.MAX_VALUE;
        for (Object o : loOrderedTripplet) {
            double dValue = ((OrderdTripplet) o).dValue;
            if (dMaxValue < dValue) {
                dMaxValue = dValue;
            }
        }

        return dMaxValue;
    }

    public static double getMinX(List<?> loOrderedTripplet) {
        double dMinX = Double.MAX_VALUE;
        for (Object o : loOrderedTripplet) {
            double dXPos = ((OrderdTripplet) o).x;
            if (dMinX > dXPos) {
                dMinX = dXPos;
            }
        }

        return dMinX;
    }

    public static double getMaxY(List<?> loOrderedTripplet) {
        double dMaxY = (-1) * Double.MAX_VALUE;
        for (Object o : loOrderedTripplet) {
            double dYPos = ((OrderdTripplet) o).y;
            if (dMaxY < dYPos) {
                dMaxY = dYPos;
            }
        }

        return dMaxY;
    }

    public static double getMinY(List<?> loOrderedTripplet) {
        double dMinY = Double.MAX_VALUE;
        for (Object o : loOrderedTripplet) {
            double dYPos = ((OrderdTripplet) o).y;
            if (dMinY > dYPos) {
                dMinY = dYPos;
            }
        }

        return dMinY;
    }

    public static double getMaxZ(List<?> loOrderedTripplet) {
        double dMaxZ = (-1) * Double.MAX_VALUE;
        for (Object o : loOrderedTripplet) {
            double dZPos = ((OrderdTripplet) o).z;
            if (dMaxZ < dZPos) {
                dMaxZ = dZPos;
            }
        }

        return dMaxZ;
    }

    public static double getMinZ(List<?> loOrderedTripplet) {
        double dMinZ = Double.MAX_VALUE;
        for (Object o : loOrderedTripplet) {
            double dZPos = ((OrderdTripplet) o).z;
            if (dMinZ > dZPos) {
                dMinZ = dZPos;
            }
        }

        return dMinZ;
    }

//    public static void write(String sFile, List<?> lot) {
//        List<String[]> lsSave = new ArrayList<String[]>();
//        String[] saTemp = new String[1];
//        saTemp[0] = "x, y, z, Value, abs(vecx)";
//        lsSave.add(saTemp);
//        
//        double dMaxValue = OrderdTripplet.getMaxValue(lot);
//
//        for (Object o : lot) {
//            OrderdTripplet ot = (OrderdTripplet) o;
//            saTemp = new String[1];
//            saTemp[0] = (ot.x + ", " + ot.y + ", " + ot.z + ", " + (ot.dValue/dMaxValue) + ","+ Math.sqrt(ot.y*ot.y + (ot.x-0.137)*(ot.x-0.137)));
//            lsSave.add(saTemp);
//        }
//
//        Writer oWriter = new Writer(sFile);
//
//        oWriter.writels(lsSave);
//    }

//    public static void write(String sFile, List<?> lot, String sHeader) {
//        List<String[]> lsSave = new ArrayList<String[]>();
//        String[] saTemp = new String[1];
//        saTemp[0] = sHeader;
//        lsSave.add(saTemp);
//
//        for (Object o : lot) {
//            OrderdTripplet ot = (OrderdTripplet) o;
//            saTemp = new String[1];
//            saTemp[0] = (ot.x + ", " + ot.y + ", " + ot.z + ", " + ot.dValue);
//            lsSave.add(saTemp);
//        }
//
//        InOut.Writer oWriter = new Writer(sFile);
//
//        oWriter.writels(lsSave);
//    }

//    public static List<OrderdTripplet> read(String sFile, int iStartRowReadIn, int iColX, int iColY, int iColZ) throws IOException {
//
//        List<OrderdTripplet> loReadIn = new ArrayList<OrderdTripplet>();
//        Reader oRead = new Reader(sFile);
//        List<String[]> lsIn = oRead.readFileStringa();
//
//        for (int i = iStartRowReadIn; i < lsIn.size(); i++) {
//            OrderdTripplet ot = new OrderdTripplet();
//            ot.x = Double.valueOf(lsIn.get(i)[iColX]);
//            ot.y = Double.valueOf(lsIn.get(i)[iColY]);
//            ot.z = Double.valueOf(lsIn.get(i)[iColZ]);
//            loReadIn.add(ot);
//        }
//
//        return loReadIn;
//    }

    public static int getNearestPointInZ(List<OrderdTripplet> lot, OrderdTripplet ot) {
        double dDist = Double.MAX_VALUE;
        int iPoint = -1;
        for (int i = 0; i < lot.size(); i++) {
            OrderdTripplet otList = lot.get(i);
            if (Math.abs(otList.z - ot.z) <= dDist) {
                iPoint = i;
                dDist = Math.abs(otList.z - ot.z);
            }
        }

        return iPoint;
    }

    public static int getNearestPointInZ(List<OrderdTripplet> lot, double dZ) {
        double dDist = Double.MAX_VALUE;
        int iPoint = -1;
        for (int i = 0; i < lot.size(); i++) {
            OrderdTripplet otList = lot.get(i);
            if (Math.abs(otList.z - dZ) <= dDist) {
                iPoint = i;
                dDist = Math.abs(otList.z - dZ);
            }
        }

        return iPoint;
    }

    public static List<OrderdTripplet> maxSlidingAverage(List<OrderdTripplet> lot) {
        List<OrderdTripplet> lotSmooth = doubleSmooth(lot);
        for (int i = 1; i < lot.size() / 2; i++) {
            lotSmooth = doubleSmooth(lotSmooth);
        }

        return lotSmooth;

    }

    public static List<OrderdTripplet> doubleSmooth(List<OrderdTripplet> lot) {

        List<OrderdTripplet> lotSmmoth = LinearSmooth(lot);
//        OrderdTripplet otStart = new OrderdTripplet(lot.get(0).x-lotSmmoth.get(0).x, lot.get(0).y-lotSmmoth.get(0).y, lot.get(0).z-lotSmmoth.get(0).z);        
//        OrderdTripplet otEnd = new OrderdTripplet(lot.get(lot.size()-1).x+lotSmmoth.get(lotSmmoth.size()-1).x, lot.get(lot.size()-1).y-lotSmmoth.get(lotSmmoth.size()-1).y, lot.get(lot.size()-1).z-lotSmmoth.get(lotSmmoth.size()-1).z);
        OrderdTripplet otStart = lot.get(0);
        OrderdTripplet otEnd = lot.get(lot.size() - 1);
        lotSmmoth.add(0, otStart);
        lotSmmoth.add(otEnd);
        return LinearSmooth(lotSmmoth);

    }

    public static List<OrderdTripplet> LinearSmooth(List<OrderdTripplet> lop) {

        List<OrderdTripplet> lotSmooth = new ArrayList<OrderdTripplet>();

        for (int i = 0; i < lop.size() - 1; i++) {
            double dX = (lop.get(i).x + lop.get(i + 1).x) / 2;
            double dY = (lop.get(i).y + lop.get(i + 1).y) / 2;
            double dZ = (lop.get(i).z + lop.get(i + 1).z) / 2;
            double dValue = (lop.get(i).dValue + lop.get(i + 1).dValue) / 2;
            OrderdTripplet ot = new OrderdTripplet(dX, dY, dZ);
            ot.dValue = dValue;
            lotSmooth.add(ot);
        }

        return lotSmooth;

    }
    
    public static List<OrderdTripplet> getTresholdValue(List<OrderdTripplet> loInput, double dMinValue, double dMaxValue){
        List<OrderdTripplet> loRet = new ArrayList();
        
        for(OrderdTripplet ot : loInput){
            if(ot.dValue>=dMinValue && ot.dValue<dMaxValue){
                loRet.add(ot);
            }
        }
        
        return loRet;
    }

}
