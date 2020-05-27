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
public class Tensor3D implements Serializable{

    private static final long serialVersionUID = 1L;
    
     public double a11 = 0.0;
     public double a12 = 0.0;
     public double a13 = 0.0;
     public double a21 = 0.0;
     public double a22 = 0.0;
     public double a23 = 0.0;
     public double a31 = 0.0;
     public double a32 = 0.0;
     public double a33 = 0.0;
    
     public Tensor3D(){
         
     }
     
     public OrderdTripplet product(OrderdTripplet ot){
         double dX = ot.x * a11 + ot.y * a12 + ot.z * a13;
         double dY = ot.x * a21 + ot.y * a22 + ot.z * a23;
         double dZ = ot.x * a31 + ot.y * a32 + ot.z * a33;
         
         return (new OrderdTripplet(dX, dY, dZ));
     }
     
     public double[] product(double[] da){
         double dX = da[0] * a11 + da[1] * a12 + da[2] * a13;
         double dY = da[0] * a21 + da[1] * a22 + da[2] * a23;
         double dZ = da[0] * a31 + da[1] * a32 + da[2] * a33;
         
         double[] daRet = new double[3];
         daRet[0] = dX;
         daRet[1] = dY;
         daRet[2] = dZ;
        
         return daRet;
     }
     
     public static Tensor3D getInverse(Tensor3D oTensor){
         Tensor3D oInv = new Tensor3D();
         oInv.a11 = oTensor.a11;
         oInv.a12 = oTensor.a21;
         oInv.a13 = oTensor.a31;
         
         oInv.a21 = oTensor.a12;
         oInv.a22 = oTensor.a22;
         oInv.a23 = oTensor.a32;
         
         oInv.a31 = oTensor.a13;
         oInv.a32 = oTensor.a23;
         oInv.a33 = oTensor.a33;
         
         return oInv;
         
     }
     
     public Tensor3D getInverse(){
         Tensor3D oInv = new Tensor3D();
         oInv.a11 = this.a11;
         oInv.a12 = this.a21;
         oInv.a13 = this.a31;
         
         oInv.a21 = this.a12;
         oInv.a22 = this.a22;
         oInv.a23 = this.a32;
         
         oInv.a31 = this.a13;
         oInv.a32 = this.a23;
         oInv.a33 = this.a33;
         
         return oInv;
         
     }
     
     public static Tensor3D getXRotTensor(double dAlpha){
         
         Tensor3D oRotX = new Tensor3D();
         
         oRotX.a11 = 1.0;
         oRotX.a22 = Math.cos(dAlpha);
         oRotX.a23 = (-1) *  Math.sin(dAlpha);
         oRotX.a32 = Math.sin(dAlpha);
         oRotX.a33 = Math.cos(dAlpha);
         
         return oRotX;
         
     }
     
     public static Tensor3D getYRotTensor(double dAlpha){
         
         Tensor3D oRotX = new Tensor3D();
         
         oRotX.a11 = Math.cos(dAlpha);
         oRotX.a13 = Math.sin(dAlpha);
         oRotX.a22 = 1.0;
         oRotX.a31 = (-1) *  Math.sin(dAlpha);
         oRotX.a33 = Math.cos(dAlpha);
         
         return oRotX;
         
     }
}
