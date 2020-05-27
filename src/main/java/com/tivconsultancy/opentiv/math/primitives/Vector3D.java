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
public class Vector3D extends OrderdTripplet implements Serializable{

    private static final long serialVersionUID = 3030096958408788079L;

    public Vector3D(double x, double y, double z) {
        super(x, y, z);
    }

    public static double Angle(Vector3D Vec1, Vector3D Vec2) {

        double dAngle = ScalarProduct(Vec1, Vec2) / (modulus(Vec1) * modulus(Vec2));

        return Math.acos(dAngle);
    }

    public static double ScalarProduct(Vector3D Vec1, Vector3D Vec2) {

        double dScalar = Vec1.x * Vec2.x + Vec1.y * Vec2.y + Vec1.z * Vec2.z;

        return dScalar;

    }

    public static Double modulus(Vector3D oVecInput) {
        Double Modulus = 0.0;

        Modulus = (oVecInput.x) * (oVecInput.x) + (oVecInput.y) * (oVecInput.y) + (oVecInput.z) * (oVecInput.z);

        Modulus = Math.sqrt(Modulus);

        return Modulus;

    }
    
    public static Double modulus(Vector3D oVecInput1, Vector3D oVecInput2) {
        //|Vec1-Vec2|
        Double Modulus = 0.0;

        Modulus = (oVecInput1.x-oVecInput2.x) * (oVecInput1.x-oVecInput2.x) + (oVecInput1.y-oVecInput2.y) * (oVecInput1.y-oVecInput2.y) + (oVecInput1.z-oVecInput2.z) * (oVecInput1.z-oVecInput2.z);

        Modulus = Math.sqrt(Modulus);

        return Modulus;

    }
    
    public Double modulusAbsDerivateX() {
        //|Vec1-Vec2|
        Double Modulus = modulus();

        return (1/(2*Modulus) * 2.0 * (this.x));
    }
    
    public Double modulusAbsDerivateY() {
        //|Vec1-Vec2|
        Double Modulus = modulus();

        return (1/(2*Modulus) * 2.0 * (this.y));
    }
    
    public Double modulusAbsDerivateZ() {
        //|Vec1-Vec2|
        Double Modulus = modulus();

        return (1/(2*Modulus) * 2.0 * (this.z));
    }
    
    
    public static Double modulusDerivateVec2X(Vector3D oVecInput1, Vector3D oVecInput2) {
        //|Vec1-Vec2|
        Double Modulus = modulus(oVecInput1, oVecInput2);

        return (Modulus * 2.0 * (oVecInput1.x-oVecInput2.x) * (-1));

    }
    
    public static Double modulusDerivateVec2Y(Vector3D oVecInput1, Vector3D oVecInput2) {
        //|Vec1-Vec2|
        Double Modulus = modulus(oVecInput1, oVecInput2);

        return (Modulus * 2.0 * (oVecInput1.y-oVecInput2.y) * (-1));

    }
    
    public static Double modulusDerivateVec2Z(Vector3D oVecInput1, Vector3D oVecInput2) {
        //|Vec1-Vec2|
        Double Modulus = modulus(oVecInput1, oVecInput2);

        return (Modulus * 2.0 * (oVecInput1.z-oVecInput2.z) * (-1));

    }

    public Double modulus() {
        Double Modulus = 0.0;

        Modulus = (this.x) * (this.x) + (this.y) * (this.y) + (this.z) * (this.z);

        Modulus = Math.sqrt(Modulus);

        return Modulus;

    }
    
    public void setVec(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString(){
        return("x: " + this.x + "y: "+ this.y + "z: "+ this.z);
    }
    
    public String toString(String sDeli){
        return(this.x + sDeli+ this.y + sDeli+ this.z);
    }

}
