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
package com.tivconsultancy.opentiv.math.functions;

import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class PLF implements Serializable{

    private static final long serialVersionUID = -794507681708488105L;

    List<Linear> loFunction = new ArrayList<>();
    List<OrderedPair> lopPoints;
    List<Double> ldS = new ArrayList<>();

    public PLF(List<OrderedPair> loInput) {
        for (int i = 0; i < loInput.size(); i++) {
            if (i == 0) {
                loFunction.add(new Linear(loInput.get(i), loInput.get(i + 1)));
                ldS.add(0.0);
            } else if (i == loInput.size() - 1) {
                loFunction.add(new Linear(loInput.get(i - 1), loInput.get(i)));
                ldS.add(ldS.get(ldS.size() - 1) + loInput.get(i - 1).getNorm(loInput.get(i)));
            } else {
                loFunction.add(new Linear(loInput.get(i - 1), loInput.get(i + 1)));
                ldS.add(ldS.get(ldS.size() - 1) + loInput.get(i - 1).getNorm(loInput.get(i)));
            }
        }
        this.lopPoints = loInput;
    }

    public List<OrderedPair> getPoints() {
        return lopPoints;
    }

    public List<Linear> getFunctions() {
        return loFunction;
    }

    public List<Double> getParametrization() {
        return ldS;
    }

    public Double getValue(double x) {
        int iCount = 0;
        double dValue = 0.0;
        for (Linear o : loFunction) {
            if (o.Intveral.isInside(x)) {
                dValue = dValue + o.getVlaue(x);
                iCount++;
            }
        }
        if (iCount != 0) {
            return dValue / (1.0 * iCount);
        }
        return null;
    }

    public Double averageValue(int iPos, int iRadius, Value o) {
        int iAvgRadius = Math.min(Math.min(loFunction.size() - 1 - iPos, iRadius), Math.min(iRadius, iPos));
        double dAVG = 0.0;
        double dWeightInt = 0.0;
        for (int i = iPos - iAvgRadius; i <= iPos + iAvgRadius; i++) {
            double dS = Math.abs(ldS.get(i) - ldS.get(iPos));
            if (dS > 1) {
                dAVG = dAVG + (o.getValue(loFunction.get(i)) * 1.0 / dS);
                dWeightInt = dWeightInt + 1.0 / dS;
            } else {
                dAVG = dAVG + o.getValue(loFunction.get(i));
                dWeightInt = dWeightInt + 1.0;
            }
        }
        if (dWeightInt == 0) {
            return o.getValue(loFunction.get(iPos));
        }
        return dAVG / dWeightInt;
    }

    public Double skewedAverageValue(int iPos, int iRadius, Value o) {
        int iLeft = Math.min(iRadius, iPos);
        int iRight = Math.min(loFunction.size() - 1 - iPos, iRadius);
        double dAVG = 0.0;
        double dWeightInt = 0.0;
        for (int i = iPos - iLeft; i <= iPos + iRight; i++) {
            double dS = Math.abs(ldS.get(i) - ldS.get(iPos));
            if (dS > 1) {
                dAVG = dAVG + (o.getValue(loFunction.get(i)) * 1.0 / dS);
                dWeightInt = dWeightInt + 1.0 / dS;
            } else {
                dAVG = dAVG + o.getValue(loFunction.get(i));
                dWeightInt = dWeightInt + 1.0;
            }
        }
        if (dWeightInt == 0) {
            return o.getValue(loFunction.get(iPos));
        }
        return dAVG / dWeightInt;
    }

    public OrderedPair getCurv(int iJump, int iPos, int iAvgRadius) {
        OrderedPair opRef = this.lopPoints.get(iPos);
        double dTx;
        double dTy;
        if (iJump + iPos < lopPoints.size() / 2.0) {
            int iNext = Math.min(iJump, this.loFunction.size() - 1 - iPos);
            double Tx1 = averageValue(iPos, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().x);
            double Ty1 = averageValue(iPos, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().y);
            double Tx2 = averageValue(iPos + iNext, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().x);
            double Ty2 = averageValue(iPos + iNext, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().y);
            double ddS = ldS.get(iPos + iNext) - ldS.get(iPos);

            dTx = (Tx2 - Tx1) / ddS;
            dTy = (Ty2 - Ty1) / ddS;
            return new OrderedPair(ldS.get(iPos), iNext, Math.sqrt(Math.pow(dTx, 2) + Math.pow(dTy, 2)));
        } else {
            int iNext = Math.min(iJump, iPos);
            double Tx1 = averageValue(iPos - iNext, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().x);
            double Ty1 = averageValue(iPos - iNext, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().y);
            double Tx2 = averageValue(iPos, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().x);
            double Ty2 = averageValue(iPos, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().y);
            double ddS = ldS.get(iPos) - ldS.get(iPos - iNext);

            dTx = (Tx2 - Tx1) / ddS;
            dTy = (Ty2 - Ty1) / ddS;
            return new OrderedPair(ldS.get(iPos), iNext, Math.sqrt(Math.pow(dTx, 2) + Math.pow(dTy, 2)));
        }
    }

    public OrderedPair getCurvCentral(int iJump, int iPos, int iAvgRadius) {
        double dTx;
        double dTy;
        int iLeft = iPos - (iJump / 2);
        int iRight = iPos + (iJump / 2);
        if ( iLeft < 0) {
            return getCurv(iJump, iPos, iAvgRadius);
        }
        if ( iRight > lopPoints.size() - 1) {
            return getCurv(iJump, iPos, iAvgRadius);
        }              
        double Tx1 = averageValue(iLeft, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().x);
        double Ty1 = averageValue(iLeft, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().y);
        double Tx2 = averageValue(iRight, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().x);
        double Ty2 = averageValue(iRight, iAvgRadius, (Value) (Object pParameter) -> ((Linear) pParameter).getTangent().y);
        double ddS = ldS.get(iLeft) - ldS.get(iRight);

        dTx = (Tx2 - Tx1) / ddS;
        dTy = (Ty2 - Ty1) / ddS;
        return new OrderedPair(ldS.get(iPos), iRight-iLeft, Math.sqrt(Math.pow(dTx, 2) + Math.pow(dTy, 2)));
    }        

//    public List<OrderedPair> getCurv(){
//        List<OrderedPair> lopCurv = new ArrayList<>();
//        for(int i = 0; i < this.lopPoints.size(); i++){
//            OrderedPair opRef = this.lopPoints.get(i);
//            double dT;
//            double dS;
//            if(i == 0){
//                dT = (loFunction.get(i+1).dydx - loFunction.get(i).dydx);                
//                dS = ldS.get(i+1) - ldS.get(i);                
//            }else if( i == this.lopPoints.size()-1){
//                dT = (loFunction.get(i).dydx - loFunction.get(i-1).dydx);
//                dS = ldS.get(i) - ldS.get(i-1);
//            }else{
//                dT = (loFunction.get(i+1).dydx - loFunction.get(i-1).dydx);
//                dS = ldS.get(i+1) - ldS.get(i-1);
//            }            
//            lopCurv.add(new OrderedPair(opRef.x, opRef.y, dT/dS ));
//        }
//        return lopCurv;
//    }
//    public List<OrderedPair> getCurv(int iOrder){
//        List<OrderedPair> lopCurv = new ArrayList<>();
//        for(int i = 0; i < this.lopPoints.size(); i++){
//            OrderedPair opRef = this.lopPoints.get(i);
//            double dT;
//            double dX;
//            double dY;
//            if(i == 0){
//                dT = (loFunction.get(i+1).dydx - loFunction.get(i).dydx);                
//                dX = lopPoints.get(i+1).x - lopPoints.get(i).x;
//                dY = lopPoints.get(i+1).y - lopPoints.get(i).y;
//                
//            }else if( i == this.lopPoints.size()-1){
//                dT = (loFunction.get(i).dydx - loFunction.get(i-1).dydx);
//                dX = lopPoints.get(i).x - lopPoints.get(i-1).x;
//                dY = lopPoints.get(i).y - lopPoints.get(i-1).y;
//            }else{
//                int iMaxOrderLeft = Math.min( iOrder, i);
//                int iMaxOrderRight = Math.min( iOrder, lopPoints.size()-1-i);
//                int iMaxOrder = Math.min(iMaxOrderLeft, iMaxOrderRight);
//                dT = (loFunction.get(i+iMaxOrder).dydx - loFunction.get(i-iMaxOrder).dydx);
//                dX = lopPoints.get(i+iMaxOrder).x - lopPoints.get(i-iMaxOrder).x;
//                dY = lopPoints.get(i+iMaxOrder).y - lopPoints.get(i-iMaxOrder).y;
//            }
//            Double dydx = dY/dX;
//            lopCurv.add(new OrderedPair(opRef.x, opRef.y, (dT/dY)/Math.pow(1+Math.pow(dydx,2), 3.0/2.0) ));
//        }
//        return lopCurv;
//    }
//    public Double getValue(double x){
//        double dCount = 0.0;
//        double dValue = 0.0;
//        for(Linear o : loFunction){
//            if(o.Intveral.isInside(x)){                
//                dValue = dValue + o.getVlaue(x)* Math.abs(o.Intveral.getCenter() - x);
//                dCount = dCount + Math.abs(o.Intveral.getCenter() - x);
//            }
//        }
//        if(dCount != 0.0) return dValue/(dCount);
//        return null;
//    }
}
