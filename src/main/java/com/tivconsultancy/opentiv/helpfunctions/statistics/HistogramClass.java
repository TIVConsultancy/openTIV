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
import com.tivconsultancy.opentiv.math.sets.Set1D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class HistogramClass extends Set1D implements Iterator<Object>, Serializable {

    private static final long serialVersionUID = 7872589908163277150L;
    public List<Object> loContent = new ArrayList<>(); //~
    public Double dHelp = null; // Helpparameter that helps for easily implement many algorithms
    
    public HistogramClass(double dLeft, double dRight){
        super(dLeft, dRight);        
    }
    
    public boolean addContent(Object o, Value oValue){
        if(isInsideLR(oValue.getValue(o))){
            loContent.add(o);
            return true;
        }
        return false;
    }
    
    public List<Object> getContent(){
        return loContent;
    }
    
    public int getCount(){
        return loContent.size();        
    }
    
    public Double getAverage(Value oValue){
        return Basics.Arithmetic(loContent, oValue);
    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object next() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
