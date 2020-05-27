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
package com.tivconsultancy.opentiv.math.primitives;

import java.io.Serializable;


/**
 *
 * @author Thomas
 */
public class Tensor2D implements Serializable{

    private static final long serialVersionUID = 2774436619577630422L;
    
    public double a11 = 0.0;
     public double a12 = 0.0;
     public double a21 = 0.0;
     public double a22 = 0.0;
    
     public Tensor2D(){
         
     }
     
     public OrderedPair product(OrderedPair op){
         double dX = op.x * a11 + op.y * a12;
         double dY = op.x * a21 + op.y * a22;
         
         return (new OrderedPair(dX, dY));
     }
     
     public static Tensor2D getRotTensor(double dAlpha){
         
         Tensor2D oRotX = new Tensor2D();
         
         oRotX.a11 = Math.cos(dAlpha);
         oRotX.a12 = (-1.0) * Math.sin(dAlpha);
         oRotX.a21 = Math.sin(dAlpha);
         oRotX.a22 = Math.cos(dAlpha);

         
         return oRotX;
         
     }
     
     
    
}
