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
package com.tivconsultancy.opentiv.helpfunctions.hpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public final class Stopwatch implements Serializable{
    
    public static List<StopUnit> loUnits = new ArrayList<>();
    private static final long serialVersionUID = -6451469104259335385L;
    
    
    public static void clearUnits(){
        loUnits.clear();
    }
    
    public static void addTimmer(String sName){
        loUnits.add(new StopUnit(sName));       
    }
    
    public static void stopLast(){
        loUnits.get(loUnits.size()-1).end();
    }
    
    public static void stop(String sName){
        for(StopUnit o : loUnits){
            if(o.sName.equals(sName)){
                o.end();
            }
        }        
    }
    
    public static void write(){
        Collections.sort(loUnits, new Comparator<StopUnit>() {

            @Override
            public int compare(StopUnit o1, StopUnit o2) {
                return (int) Math.signum((o2.fEndTime - o2.fStartTime) - (o1.fEndTime - o1.fStartTime));
            }
        });
        for(StopUnit o : loUnits){
            System.out.println(o.toString());
        }
    }
    
}
