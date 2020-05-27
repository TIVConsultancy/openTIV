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
package com.tivconsultancy.opentiv.helpfunctions.statistics;

import com.tivconsultancy.opentiv.math.interfaces.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class DoubleDistributionClass implements DistributionClass<Double>, Serializable {

    private static final long serialVersionUID = -1315226299443400356L;

    public List<Double> loContent = new ArrayList<Double>();
    Double dMinBorder;
    Double dMaxBorder;

    Double dOffSet;

    public Double dDensity = null;
    public Double dAverage = null;   
    
    public Double dCharacteristicValue = null;

    public DoubleDistributionClass(Double dMinBorder, Double dMaxBorder, Double dOffSet) {
        this.dMinBorder = dMinBorder;
        this.dMaxBorder = dMaxBorder;
        this.dOffSet = dOffSet;
    }
    
    public DoubleDistributionClass(Double dMinBorder, Double dMaxBorder, Double dOffSet, Double dCharacteristicValue) {
        this.dMinBorder = dMinBorder;
        this.dMaxBorder = dMaxBorder;
        this.dOffSet = dOffSet;
        this.dCharacteristicValue = dCharacteristicValue;
    }

    @Override
    public void addContent(Double loAdd) {
        loContent.add(loAdd);
    }

    @Override
    public Double getMinBorder() {
        return dMinBorder;
    }

    @Override
    public Double getMax() {
        return dMaxBorder;
    }

    @Override
    public boolean isInside(Double dValue) {
        if (dValue != null && (dMaxBorder - dOffSet) > dValue && (dMinBorder - dOffSet) <= dValue) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (dDensity != null) {
            return "" + dMaxBorder + "," + dDensity;
        }
        return "" + dMaxBorder + "," + loContent.size();
    }
    
    public Double getCharValue(){
        if(dCharacteristicValue!= null){
            return dCharacteristicValue;
        }
        return getMinBorder() + (getMax()-getMinBorder())/2.0;
    }
    
    public static List<String> getList(List<? extends DoubleDistributionClass> l) {
        List<String> ls = new ArrayList<String>();
        for (DoubleDistributionClass d : l) {
            ls.add(d.toString());
        }
        return ls;
    }

    public static List<String> getList(List<? extends DoubleDistributionClass> l, StringOperation<DoubleDistributionClass> o) {
        List<String> ls = new ArrayList<String>();
        for (DoubleDistributionClass d : l) {
            ls.add(o.getString(d));
        }
        return ls;
    }
        

    public static List<? extends DistributionClass> generateLinearClasses(Double dStepSize, Double dAbsMin, Double dAbsMax, Double dOffSet) {
        List<DistributionClass> loBubClass = new ArrayList<DistributionClass>();
        for (Double d = dAbsMin; d <= (dAbsMax - dStepSize); d = d + dStepSize) {
            loBubClass.add(new DoubleDistributionClass(d, d + dStepSize, dOffSet));
        }
        return loBubClass;
    }

    public Double calcNumberDensityForClass(List<? extends DoubleDistributionClass> loAllClasses) {

        double dSum = 0.0;
        for (DoubleDistributionClass odD : loAllClasses) {
            dSum = dSum + odD.loContent.size();
        }
        return ((double) this.loContent.size()) / (dSum);
    }

    public static void calcNumberDensity(List<? extends DoubleDistributionClass> loAllClasses) {

        double dSum = 0.0;
        for (DoubleDistributionClass odD : loAllClasses) {
            dSum = dSum + odD.loContent.size();
        }

        for (DoubleDistributionClass odD : loAllClasses) {
            odD.dDensity = ((double) odD.loContent.size()) / (dSum * (odD.getMax() - odD.getMinBorder()));
        }

    }

    public static void calcDensity(List<? extends DoubleDistributionClass> loAllClasses) {

        double dSum = 0.0;
        for (DoubleDistributionClass odD : loAllClasses) {
            for (Double d : odD.loContent) {
                dSum = dSum + d;
            }

        }

        for (DoubleDistributionClass odD : loAllClasses) {
            Double dClassSum = 0.0;
            for (Double d : odD.loContent) {
                if(d!= null){
                    dClassSum = dClassSum + d;
                }
            }
            odD.dDensity = (dClassSum) / (dSum * (odD.getMax() - odD.getMinBorder()));
        }
    }

    public static void calcArithmeticAverage(List<? extends DoubleDistributionClass> loAllClasses) {

        for (DoubleDistributionClass odD : loAllClasses) {
            Double dClassSum = 0.0;
            int iCount = 0;
            for (Double d : odD.loContent) {                
                if(d!= null){
                    dClassSum = dClassSum + d;
                    iCount++;
                }                
            }
            odD.dAverage = (dClassSum) / (iCount++);
        }
    }
    
    public int getCount(){
        return loContent.size();
    }
    
    public static DoubleDistributionClass getClassWithMaxCount(List<? extends DoubleDistributionClass> lo){
        int iMaxCount = 0;
        DoubleDistributionClass oReturn = lo.get(0);
        for(DoubleDistributionClass o : lo){
            if(o.getCount() > iMaxCount){
                iMaxCount =o.getCount();
                oReturn = o;
            }
        }
        return oReturn;
    }

    @Override
    public double getPosX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getPosY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Double> getContent() {
        return loContent;
    }
    
    public static interface StringOperation<DoubleDistributionClass>{
        public String getString(DoubleDistributionClass pParameter);
    };

}
