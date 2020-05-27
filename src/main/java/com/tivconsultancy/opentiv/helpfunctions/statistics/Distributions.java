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
import java.util.List;

/**
 *
 * @author Thomas
 */
public class Distributions {

    public static List<? extends DistributionClass> calcDistribution(List<? extends Distributable> loInput, List<? extends DistributionClass> loClasses, String sIdent) {

        for (Distributable I : loInput) {

            Double dVal = I.getRandomVar(sIdent);

            for (DistributionClass Iclass : loClasses) {
                if (Iclass.isInside(dVal)) {
                    Iclass.addContent(I);
                    break;
                }
            }
        }

        return loClasses;

    }
    
    public static List<? extends DistributionClass> calcDistributionNumber(List<? extends Number> loInput, List<? extends DistributionClass> loClasses) {

        for (Number I : loInput) {

            Double dVal = I.doubleValue();

            for (DistributionClass Iclass : loClasses) {
                if (Iclass.isInside(dVal)) {
                    Iclass.addContent(I);
                    break;
                }
            }
        }

        return loClasses;

    }
    
    public static List<? extends DistributionClass> calcDistributionDoubleArray(List<Double[]> loInput, List<? extends DistributionClass> loClasses, int iCol) {

        for (Double[] da : loInput) {

            Double dVal = da[iCol];

            for (DistributionClass Iclass : loClasses) {
                if (Iclass.isInside(dVal)) {
                    Iclass.addContent(dVal);
                    break;
                }
            }
        }

        return loClasses;

    }  
    
    public static <T> List<? extends DistributionClass> calcDistributionWithFunction(List<T> loInput, List<? extends DistributionClass> loClasses, Operation<T> O) {

        for (final T d : loInput) {            

            for (DistributionClass Iclass : loClasses) {
                if (Iclass.isInside(O.getCharacteristicValue(d))) {
                    Iclass.addContent(O.perform(d));
                    break;
                }
            }
        }

        return loClasses;
    }
    
    public static <T> List<? extends DistributionClass> calcDistributionWithValue(List<T> loInput, List<? extends DistributionClass> loClasses, Value<T> O) {

        for (final T d : loInput) {            

            for (DistributionClass Iclass : loClasses) {
                if (Iclass.isInside(O.getValue(d))) {
                    Iclass.addContent(d);
                    break;
                }
            }
        }

        return loClasses;
    }  

    public static List<? extends DistributionClass> calcDistributionWithFilter(List<? extends Distributable> loInput, List<? extends DistributionClass> loClasses, String sIdent, double dMinValue, double dMaxValue, String sFilterIdent) {

        for (Distributable I : loInput) {

            Double dFilterVal = I.getRandomVar(sFilterIdent);

            if (dFilterVal >= dMinValue && dFilterVal < dMaxValue) {

                Double dVal = I.getRandomVar(sIdent);

                for (DistributionClass Iclass : loClasses) {
                    if (Iclass.isInside(dVal)) {
                        Iclass.addContent(I);
                        break;
                    }
                }
            }
        }

        return loClasses;

    }
    
    public static int getg_min(List<? extends DistributionClass> loClasses){
        /*
        first class that is filled with content
        */
        int iCount = 0;
        for(DistributionClass o : loClasses){
            if(!o.getContent().isEmpty()) break;
            iCount++;
        }
        return iCount;
    }
    
    public static int getc_max(List<? extends DistributionClass> loClasses){
        /*
        last class that is filled with content
        */        
        int iCount = 0;
        for(int i = loClasses.size()-1; i >= 0; i--){
            DistributionClass o = loClasses.get(i);
            iCount=i;
            if(!o.getContent().isEmpty()) break;            
        }
        return iCount;
    }
    
    public static int getg_max(List<? extends DistributionClass> loClasses){
        /*
        class that is filled with the most elements
        */
        int iClass = 0;
        int iElementCount = 0;
        for(int i = 0; i < loClasses.size(); i++){
            DistributionClass o = loClasses.get(i);
            if(o.getContent().size() > iElementCount){
                iClass = i;
                iElementCount = o.getContent().size();
            }            
        }
        return iClass;
    }
    
    public static interface Operation<T> {
        public Double getCharacteristicValue(T pParameter);
        public Double perform(T pParameter);        
    }

}
