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
package com.tivconsultancy.opentiv.math.sets;

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Set2D implements Serializable {

    private static final long serialVersionUID = -8608716579078619124L;
    public Set1D oIntervalX;
    public Set1D oIntervalY;

    public Set2D(Set1D oIntervalX, Set1D oIntervalY) {
        this.oIntervalX = oIntervalX;
        this.oIntervalY = oIntervalY;
    }

    public Set2D() {
        this.oIntervalX = new Set1D();
        this.oIntervalY = new Set1D();
    }

    public static boolean overlap(Set2D Rec1, Set2D Rec2) {

        if (Rec1.oIntervalX.overlap(Rec2.oIntervalX)) {
            return true;
        }

        if (Rec2.oIntervalX.overlap(Rec1.oIntervalX)) {
            return true;
        }

        if (Rec1.oIntervalY.overlap(Rec2.oIntervalY)) {
            return true;
        }

        if (Rec2.oIntervalY.overlap(Rec1.oIntervalY)) {
            return true;
        }

        return false;
    }
    
    public boolean overlap(Set2D Rec2) {
        if (this.oIntervalX.overlap(Rec2.oIntervalX) && this.oIntervalY.overlap(Rec2.oIntervalY)) {
            return true;
        }
        if (Rec2.oIntervalX.overlap(this.oIntervalX) && Rec2.oIntervalY.overlap(this.oIntervalY)) {
            return true;
        }
        return false;
    }
    
    public boolean isInside(Set2D Rec2) {
        if (this.oIntervalX.isInside(Rec2.oIntervalX) && this.oIntervalY.isInside(Rec2.oIntervalY)) {
            return true;
        }
        if (Rec2.oIntervalX.isInside(this.oIntervalX) && Rec2.oIntervalY.isInside(this.oIntervalY)) {
            return true;
        }
        return false;
    }

    public static boolean isInside(Set2D Rec1, Set2D Rec2) {
        if (Rec1.oIntervalX.isInside(Rec2.oIntervalX) && Rec1.oIntervalY.isInside(Rec2.oIntervalY)) {
            return true;
        }
        if (Rec2.oIntervalX.isInside(Rec1.oIntervalX) && Rec2.oIntervalY.isInside(Rec1.oIntervalY)) {
            return true;
        }
        return false;
    }

    public <T> boolean isInsideWOBorder(T oIn, Characteristic O) {
        OrderedPair op = O.getCharacteristicValue(oIn);
        if (this.oIntervalX.isInsideWOBorder(op.x) && this.oIntervalY.isInsideWOBorder(op.y)) {
            return true;
        }
        return false;
    }

    public <T> boolean isInside(T oIn, Characteristic O) {
        OrderedPair op = O.getCharacteristicValue(oIn);
        if (this.oIntervalX.isInside(op.x) && this.oIntervalY.isInside(op.y)) {
            return true;
        }
        return false;
    }

    public <T> Collection<T> removePointsInSet(Collection<T> lop, Characteristic O) {

        List<T> lopInSet = new ArrayList<>();

        for (T op : lop) {
            if (this.isInside(op, O)) {
                lopInSet.add(op);
            }
        }
        lop.removeAll(lopInSet);
        return lop;

    }

    public <T> void removePointsInSetWOBorder(List<T> lop, Characteristic O) {

        List<T> lopInSet = new ArrayList<>();

        for (T op : lop) {
            if (this.isInsideWOBorder(op, O)) {
                lopInSet.add(op);
            }
        }

        lop.removeAll(lopInSet);

    }

    public <T> Collection<T> getPointsInSetWOBorder(Collection<T> lop, Characteristic O) {

        List<T> lopInSet = new ArrayList<>();

        for (T op : lop) {
            if (this.isInsideWOBorder(op, O)) {
                lopInSet.add(op);
            }
        }

        return lopInSet;

    }

    public <T> List<T> getPointsInSet(List<T> lop , Characteristic O) {

        List<T> lopInSet = new ArrayList<>();

        for (T op : lop) {
            if (this.isInside(op, O)) {
                lopInSet.add(op);
            }
        }

        return lopInSet;

    }
    
    public OrderedPair getHullCenter(){
        double dXCenter = this.oIntervalX.getCenter();
        double dYCenter = this.oIntervalY.getCenter();
        
        return (new OrderedPair(dXCenter, dYCenter));
    }
    
    public OrderedPair getCenter(){
        return new OrderedPair(oIntervalX.getCenter(), oIntervalY.getCenter());
    }

    public static Set2D getOverlap(Set2D Rec1, Set2D Rec2) {

        if (overlap(Rec1, Rec2)) {
            if (isInside(Rec1, Rec2)) {
                return null;
            } else {
                Set2D oRecOverlap = new Set2D(new Set1D(0, 0), new Set1D(0, 0));
                oRecOverlap.oIntervalX = Rec1.oIntervalX.getIntersection(Rec2.oIntervalX);
                oRecOverlap.oIntervalY = Rec1.oIntervalY.getIntersection(Rec2.oIntervalY);
                return oRecOverlap;

            }
        }
        return null;
    }
    
    public Double getMaxCircleWhichIsInsideSet(OrderedPair op, Characteristic O){
        
        if(this.isInside(op, O)){
            return this.oIntervalX.getMaxCircleInSet(op.x) < this.oIntervalY.getMaxCircleInSet(op.y) ? this.oIntervalX.getMaxCircleInSet(op.x) : this.oIntervalY.getMaxCircleInSet(op.y);
        }else{
            return null;
        }
    }

    public boolean CheckEmptySet() {
        if (this.oIntervalX != null && this.oIntervalY != null) {
            if (oIntervalX.checkEmptySet()) {
                return true;
            }
            if (oIntervalY.checkEmptySet()) {
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "X: " + this.oIntervalX.toString()
                + "; Y : " + this.oIntervalY.toString();
    }
    
    public static interface Characteristic<T> {
        public OrderedPair getCharacteristicValue(T pParameter);
    }
    
}
