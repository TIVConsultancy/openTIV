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

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Linear implements Serializable{

    private static final long serialVersionUID = 3987942574959070411L;
    
    public Double dydx;    
    public OrderedPair opStart;
    public Set1D Intveral;
    public byte sign = 0;
    
//    public Linear(Double dydx, OrderedPair opStart, Set1D Interval){
//        this.dydx = dydx;
//        this.opStart = opStart;
//    }
    
    public Linear(OrderedPair opStart, OrderedPair opEnd){        
        this.opStart = opStart;
        this.Intveral = new Set1D(opStart.x, opEnd.x);        
        this.sign = (byte) Math.signum(opEnd.x-opStart.x);
        if(opEnd.x == opStart.x){   
            if( (opEnd.y - opStart.y) > 0) this.dydx = Double.POSITIVE_INFINITY;
            else this.dydx = Double.NEGATIVE_INFINITY;
        }else{
            this.dydx = (opEnd.y - opStart.y)/(opEnd.x-opStart.x);
        }
    }
    
    public double getVlaue(double x){
        if(dydx.isInfinite()) return opStart.y;
        return opStart.y + (x - opStart.x) * dydx;
    }
    
    public OrderedPair getTangent(){
        if(dydx.isInfinite()) return new OrderedPair(0, Math.signum(dydx) * 1.0);
        double dNorm = Math.sqrt(Math.pow(dydx, 2) + 1);
        double Tx = Math.signum(sign) * 1.0/dNorm;
        double Ty = Math.signum(sign) * dydx/dNorm;
        return new OrderedPair(Tx, Ty);
    }
    
    
}
