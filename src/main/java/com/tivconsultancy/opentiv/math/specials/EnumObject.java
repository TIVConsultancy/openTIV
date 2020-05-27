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

/**
 *
 * @author Thomas Ziegenhein
 */
public class EnumObject implements Normable<EnumObject>, Serializable{

    private static final long serialVersionUID = -6982847300053745602L;
    public Double dEnum;
    public Object o;
    
    public EnumObject(Double dEnum, Object o){
        this.dEnum = dEnum;
        this.o = o;
    }

    @Override
    public Double getNorm(EnumObject o2) {
        return Math.abs(this.dEnum - o2.dEnum);
    }

    @Override
    public Double getNorm2(EnumObject o2, String sNormType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        return dEnum + ", " + o.toString();
    }
    
}
