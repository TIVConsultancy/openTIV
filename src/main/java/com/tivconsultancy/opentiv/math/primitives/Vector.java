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

import com.tivconsultancy.opentiv.math.interfaces.*;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Vector extends OrderedPair implements VectorNorm, Serializable {

    private static final long serialVersionUID = 3027566421645978610L;
    public OrderedPair opUnitTangent;

    public Vector(OrderedPair opPosition, OrderedPair opDirection, double dLength) {
        super(opPosition);
        double dNormDirec = opDirection.getNorm(new OrderedPair(0, 0));
        if (dNormDirec > 0) {
            opUnitTangent = new OrderedPair(opDirection.x / dNormDirec, opDirection.y / dNormDirec, dLength);
        } else {
            opUnitTangent = new OrderedPair(0, 0, 0.0);
        }
    }

    public Vector(OrderedPair opPosition1, OrderedPair opPosition2) {
        super(opPosition1);
        double dLength = opPosition1.getNorm(opPosition2);
        OrderedPair opDirection = opPosition2.sub(opPosition1);
        opUnitTangent = new OrderedPair(opDirection.x / dLength, opDirection.y / dLength, dLength);
    }

    public Vector(double x, double y) {
        opUnitTangent = new OrderedPair(x, y,0.0);
    }

    @Override
    public Vector clone() {
        return new Vector(super.clone(), this.opUnitTangent.clone(), this.opUnitTangent.dValue);
    }

    @Override
    public Vector mult(Double d) {
        if (d > 0) {
            return new Vector(super.clone(), this.opUnitTangent.clone(), this.opUnitTangent.dValue * d);
        } else {
            return new Vector(super.clone(), this.opUnitTangent.clone().mult(-1.0), this.opUnitTangent.dValue * Math.abs(d));
        }
    }

    @Override
    public Vector mult2(Double d, String sType) {
        if (sType.equals("Vx")) {
            OrderedPair opNewUnitTangen = this.opUnitTangent.clone();
            opNewUnitTangen.x = opNewUnitTangen.x * d;
            return new Vector(super.clone(), opNewUnitTangen, this.opUnitTangent.dValue * Math.abs(d));
        } else if (sType.equals("Vy")) {
            OrderedPair opNewUnitTangen = this.opUnitTangent.clone();
            opNewUnitTangen.y = opNewUnitTangen.y * d;
            return new Vector(super.clone(), opNewUnitTangen, this.opUnitTangent.dValue * Math.abs(d));
        }
        return this.mult(d);
    }

    public double getX() {
        return this.opUnitTangent.x * this.getLength();
    }

    public double getY() {
        return this.opUnitTangent.y * this.getLength();
    }

    public double getAngle() {
        return Math.atan2(opUnitTangent.y, opUnitTangent.x);
    }

    @Override
    public Double getLength() {
        return this.opUnitTangent.dValue;
    }

    public Vector add(Vector o2) {
        return new Vector(super.clone(), new OrderedPair(this.getX() + o2.getX(), this.getY() + o2.getY()), this.opUnitTangent.dValue + o2.opUnitTangent.dValue);
    }

    @Override
    public Vector add(OrderedPair o2) {
        return new Vector(super.clone(), new OrderedPair(this.getX() + o2.x, this.getY() + o2.y), this.opUnitTangent.dValue + o2.getNorm(new OrderedPair(0.0, 0.0)));
    }

    @Override
    public OrderedPair sub(OrderedPair o2) {
        return new Vector(super.clone(), new OrderedPair(opUnitTangent.x - o2.x, opUnitTangent.y - o2.y), this.opUnitTangent.dValue);
    }

    public Vector sub(Vector o2) {
        double dX1 = this.getX();
        double dY1 = this.getY();
        double dX2 = o2.getX();
        double dY2 = o2.getY();
        double dXNew = dX2 - dX1;
        double dYNew = dY2 - dY1;
        double dLength = Math.sqrt(dXNew * dXNew + dYNew * dYNew);
        return new Vector(super.clone(), new OrderedPair(dXNew, dYNew), dLength);
    }

////    @Override
////    public Double getNorm(String sType) {
////        return this.opUnitTangent.dValue;
////    }
    public double cosAngle(Vector Vec2) {
        double dAngle = this.ScalarProduct(Vec2) / (this.getLength() * Vec2.getLength());
        return dAngle;
    }

    public double ScalarProduct(Vector Vec2) {
        double dScalar = this.getX() * Vec2.getX() + this.getY() * Vec2.getY();
        return dScalar;

    }

}
