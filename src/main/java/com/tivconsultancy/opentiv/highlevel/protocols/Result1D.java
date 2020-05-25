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
package com.tivconsultancy.opentiv.highlevel.protocols;

import com.tivconsultancy.opentiv.math.specials.LookUp;
import com.tivconsultancy.opentiv.math.specials.NameObject;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class Result1D {
    
    private LookUp<Double> result;
    
    public Result1D(){
        result = new LookUp<>();
    }
    
    public void addResult(String name, Double d){
        NameObject<Double> o = new NameObject<>(name,d);
        result.add(o);
    }
    
    public boolean setResult(String name, Double d){
        if(result.get(name) != null){
            result.getEntry(name).o = d;
            return true;
        }
        return false;
    }
    
    public Double getRes(String name){
        return result.get(name);
    }
    
    public String getName(Double value){
        return result.get(value).name;
    }
    
    public List<String> getAllNames(){
        return result.getNames();
    }
    
    public void remove(String name){
        result.remove(name);
    }
    
}
