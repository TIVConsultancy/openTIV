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
package com.tivconsultancy.opentiv.math.specials;

import com.tivconsultancy.opentiv.math.interfaces.Normable;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class NameObject<T> implements Normable<NameObject>, Serializable{
    
    private static final long serialVersionUID = 46123491L;
    
    public String name;
    public T o;
    
    public NameObject(String name, T o){
        this.name = name;
        this.o = o;
    }

    @Override
    public Double getNorm(NameObject o2) {
        return 1.0*Math.abs(name.hashCode() - o2.name.hashCode());
    }

    @Override
    public Double getNorm2(NameObject o2, String sNormType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        return name + ", " + o.toString();
    }        
    
    
    
}
