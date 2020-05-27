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

import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.math.interfaces.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Histogram implements Serializable{

    private static final long serialVersionUID = 9012662222213151893L;
    public List<HistogramClass> loClasses = new ArrayList<>();
    double dStart = 0;
    double dEnd = 0;
    
    public Histogram(double dStart, double dEnd, double dSize){
        /*
          Size must be > 0, not tested      
        */
        this.dStart = dStart;
        this.dEnd = dEnd;
        
        for(int i = 0; i<Math.ceil((dEnd-dStart)/dSize); i++){
            loClasses.add(new HistogramClass(dStart + i*dSize, dStart + i*dSize+dSize));
        }
    }
    
    public int getg_min(){
        /*
        first class that is filled with content
        */
        int iCount = 0;
        for(HistogramClass o : loClasses){
            if(!o.loContent.isEmpty()) break;
            iCount++;
        }
        return iCount;
    }
    
    public int getc_max(){
        /*
        last class that is filled with content
        */        
        int iCount = 0;
        for(int i = loClasses.size()-1; i >= 0; i--){
            HistogramClass o = loClasses.get(i);
            iCount=i;
            if(!o.loContent.isEmpty()) break;            
        }
        return iCount;
    }
    
    public int getg_max(){
        /*
        class that is filled with the most elements
        */
        int iClass = 0;
        int iElementCount = 0;
        for(int i = 0; i < loClasses.size(); i++){
            HistogramClass o = loClasses.get(i);
            if(o.loContent.size() > iElementCount){
                iClass = i;
                iElementCount = o.loContent.size();
            }            
        }
        return iClass;
    }
    
    public void addContent(Object oContent, Value oValue){
        //Implement a faster algorithm --> Fin dthe closest Histogram class cloe to the value first and than loop
        for(HistogramClass o : loClasses){
            o.addContent(oContent, oValue);
        }
    }
    
    public List<Double> getCumulativeValues(Value<HistogramClass> oValue){
        Double dCumulative = 0.0;
        List<Double> loCum = new ArrayList<>();
        for(HistogramClass o : loClasses){
            loCum.add(dCumulative + oValue.getValue(o));
            dCumulative += oValue.getValue(o);
        }
        return loCum;
    }
    
    public void write(String sFile){
        List<String> lsOut = new ArrayList<>();
        lsOut.add("Center, Count");
        for(HistogramClass o : this.loClasses){
            lsOut.add(o.getCenter() + "," + o.loContent.size());
        }
        Writer oWrite = new Writer(sFile);
        oWrite.write(lsOut);
    }
    
    public void writePDF(String sFile){
        int iCounter = 0;
        for(HistogramClass o : this.loClasses){
            iCounter = iCounter + o.loContent.size();
        }
        
        List<String> lsOut = new ArrayList<>();
        lsOut.add("Center, PDF");
        for(HistogramClass o : this.loClasses){
            if(o.loContent.isEmpty()){
                lsOut.add(o.getCenter() + "," + Double.MIN_VALUE);
            }else{
                lsOut.add(o.getCenter() + "," + ((1.0*o.loContent.size())/(1.0*iCounter))/o.getSize());
            }
            
        }
        Writer oWrite = new Writer(sFile);
        oWrite.write(lsOut);
    }
    
    public double median(){
        Double dCumulative = 0.0;
        for(HistogramClass o : loClasses){
            dCumulative += o.loContent.size();
        }
        
        Double dCumHalf = 0.0;
        int iCount = 0;
        for(HistogramClass o : loClasses){
            dCumHalf += o.loContent.size();
            if(dCumHalf > dCumulative) break;
            iCount++;
        }
        
        return loClasses.get(iCount).getCenter();
        
    }
    
}
