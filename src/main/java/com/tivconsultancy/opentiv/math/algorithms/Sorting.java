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
package com.tivconsultancy.opentiv.math.algorithms;

import com.tivconsultancy.opentiv.math.exceptions.*;
import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Sorting {

    public static <T> EnumObject getNearestInList(Collection<T> lop, T oIn) throws EmptySetException, NotImplementedInterface {
        if (oIn == null) {
            throw new NullPointerException("Null input not allowed in getNearestInList");
        }
        if (lop.isEmpty()) {
            throw new EmptySetException("Empty list input in getNearestInList");
        }
        if (!(oIn instanceof Normable)) {
            throw new NotImplementedInterface("Object is not implementing Normable");
        }
        for (Object o : lop) {
            if (o == null) {
                throw new NullPointerException("Null input not allowed");
            }
            
            if (!(o instanceof Normable)) {
                throw new NotImplementedInterface("Object is not implementing Normable");
            }
        }

        EnumObject oE = new EnumObject(Double.MIN_VALUE, null);
        for (T o : lop) {
            double dDistance = ((Normable) o).getNorm(oIn);
            if (dDistance < oE.dEnum) {
                oE.dEnum = dDistance;
                oE.o = o;
            }
        }

        return oE;
    }

    public static <T> boolean checkInCol(Collection<T> lo, T oIn) throws EmptySetException {

        if (oIn == null) {
            throw new NullPointerException("Null input not allowed in getNearestInList");
        }
        if (lo.isEmpty()) {
            throw new EmptySetException("Empty list input in getNearestInList");
        }

        for (T o : lo) {
            if (o != null && o.equals(oIn)) {
                return true;
            }
        }
        return false;
    }

    public static <T> Collection<T> getEntriesWithSameCharacteristic(Collection<T> lop, Double dCharacteristic, Characteristic O) throws EmptySetException {
        
        if (lop.isEmpty()) {
            throw new EmptySetException("Empty list input in getNearestInList");
        }
        
        Collection<T> lopSameCharacteristic = new HashSet<>();
        
        for (T o : lop) {
            if (o != null && Objects.equals(dCharacteristic, O.getCharacteristicValue(o))) {
                lopSameCharacteristic.add(o);
            }
        }
        return lopSameCharacteristic;
    }
    
    public static <T> Collection<T> getEntriesWithSameCharacteristic(T oRef, Collection<T> lop, Double dCharacteristic, Characteristic2 O) throws EmptySetException {
        
        if (lop.isEmpty()) {
            throw new EmptySetException("Empty list input in getNearestInList");
        }
        
        Collection<T> lopSameCharacteristic = new HashSet<>();
        
        for (T o : lop) {
            if (o != null && Objects.equals(dCharacteristic, O.getCharacteristicValue(oRef, o))) {
                lopSameCharacteristic.add(o);
            }
        }
        return lopSameCharacteristic;
    }

    public static <T> EnumObject getMaxNorm(Collection<T> lop, T oReference) throws EmptySetException, NotImplementedInterface {

        if (oReference == null) {
            throw new NullPointerException("Null input not allowed in getNearestInList");
        }
        if (lop.isEmpty()) {
            throw new EmptySetException("Empty list input in getNearestInList");
        }
        if (!(oReference instanceof Normable)) {
            throw new NotImplementedInterface("Object is not implementing Normable");
        }

        for (Object o : lop) {
            if (o == null) {
                throw new NullPointerException("Null input not allowed");
            }
            if (!(o instanceof Normable)) {
                throw new NotImplementedInterface("Object is not implementing Normable");
            }
        }

        EnumObject oE = new EnumObject(Double.MIN_VALUE, null);

        for (T o : lop) {
            Double dNorm = ((Normable) o).getNorm(oReference);
            if (dNorm > oE.dEnum) {
                oE.dEnum = dNorm;
                oE.o = o;
            }
        }

        return oE;
    }

    public static <T> EnumObject getEntriesWithMaxNorm(Collection<T> lop) throws EmptySetException, NotImplementedInterface {

        if (lop.isEmpty()) {
            throw new EmptySetException("Empty list input in getEntriesWithMaxNorm");
        }

        for (Object o : lop) {
            if (o == null) {
                throw new NullPointerException("Null input not allowed");
            }
            if (!(o instanceof Normable)) {
                throw new NotImplementedInterface("Object is not implementing Normable");
            }
        }

        EnumObject oEnum = new EnumObject(Double.MIN_VALUE, null);

        for (T Inner : lop) {
            for (T Outer : lop) {
                double dNorm = ((Normable) Inner).getNorm(Outer);
                if (dNorm > oEnum.dEnum) {
                    oEnum.dEnum = dNorm;
                    oEnum.o = new Object[]{Inner, Outer};
                }
            }
        }

        return oEnum;
    }
    
    public static <T> EnumObject getEntriesWithMinNorm(Collection<T> lop) throws EmptySetException, NotImplementedInterface {

        if (lop.isEmpty()) {
            throw new EmptySetException("Empty list input in getEntriesWithMaxNorm");
        }

        for (Object o : lop) {
            if (o == null) {
                throw new NullPointerException("Null input not allowed");
            }
            if (!(o instanceof Normable)) {
                throw new NotImplementedInterface("Object is not implementing Normable");
            }
        }

        EnumObject oEnum = new EnumObject(Double.MAX_VALUE, null);

        for (T Inner : lop) {
            for (T Outer : lop) {
                double dNorm = ((Normable) Inner).getNorm(Outer);
                if (dNorm < oEnum.dEnum) {
                    oEnum.dEnum = dNorm;
                    oEnum.o = new Object[]{Inner, Outer};
                }
            }
        }

        return oEnum;
    }
    
    public static <T> Collection<T> removeList2FromList1(Collection<T> oCol1, Collection<T> oCol2) {

        Collection<T> lopRemovePoints = new HashSet<>();
        for (T opThis : oCol1) {
            for (T opRef : oCol2) {
                if (opThis.equals(opRef)) {
                    lopRemovePoints.add(opThis);
                    break;
                }
            }
        }

        oCol1.removeAll(lopRemovePoints);
        return oCol1;
    }
    
    public static <T> List<T> doUnique(List<T> loInput) {
        
        HashSet oSet = new HashSet(loInput);
        return(new ArrayList(oSet));
    }
    
    public static <T> List<T> sortMaxCharacteristic(List<T> lo, Characteristic O) throws EmptySetException {
        
        if (lo.isEmpty()) {
            throw new EmptySetException("Empty list input in getEntriesWithMaxNorm");
        }
        
        Collections.sort(lo, new Comparator<T>() {

            @Override
            public int compare(T o1, T o2) {
                try {
                    return (int) Math.signum(O.getCharacteristicValue(o2) - O.getCharacteristicValue(o1));
                } catch (Exception e) {
                    return 0;
                }
                
            }
        });        

        return lo;
    }
    
    public static <T> EnumObject getMaxCharacteristic(Collection<T> lo, Characteristic O) throws EmptySetException {
        
        if (lo.isEmpty()) {
            throw new EmptySetException("Empty list input in getEntriesWithMaxNorm");
        }

        EnumObject oEnum = new EnumObject( (-1.0) * Double.MAX_VALUE, null);

        for (T o : lo) {
            if (O.getCharacteristicValue(o) > oEnum.dEnum) {
                oEnum.dEnum = O.getCharacteristicValue(o);
                oEnum.o = o;
            }
        }

        return oEnum;
    }
    
    public static <T> EnumObject getMaxCharacteristic2(Collection<T> lo, Characteristic O) {        

        EnumObject oEnum = new EnumObject( (-1.0) * Double.MAX_VALUE, null);

        for (T o : lo) {
            if (O.getCharacteristicValue(o) > oEnum.dEnum) {
                oEnum.dEnum = O.getCharacteristicValue(o);
                oEnum.o = o;
            }
        }

        return oEnum;
    }
    
    public static <T> EnumObject getMinCharacteristic(Collection<T> lo, Characteristic O) throws EmptySetException {
        
        if (lo.isEmpty()) {
            throw new EmptySetException("Empty list input in getEntriesWithMaxNorm");
        }

        EnumObject oEnum = new EnumObject( Double.MAX_VALUE, null);

        for (T o : lo) {
            if (O.getCharacteristicValue(o) < oEnum.dEnum) {
                oEnum.dEnum = O.getCharacteristicValue(o);
                oEnum.o = o;
            }
        }

        return oEnum;
    }
    
    public static <T> EnumObject getMinCharacteristic(Collection<T> lo, T oRef, Characteristic2 O) throws EmptySetException {
        
        if (lo.isEmpty()) {
            throw new EmptySetException("Empty list input in getEntriesWithMaxNorm");
        }

        EnumObject oEnum = new EnumObject( Double.MAX_VALUE, null);

        for (T o : lo) {
            Double dChar = O.getCharacteristicValue(oRef,o) ;
            if (dChar < oEnum.dEnum) {
                oEnum.dEnum = dChar;
                oEnum.o = o;
            }
        }

        return oEnum;
    }
    
    public static <T> Collection<T> getNeighborsInList(Collection<T> lo, T oRef, Double dDiameter) {
        Collection<T> loReturn = new HashSet<>();

        for (T o : lo) {
            Double dDist = ((Normable) o).getNorm(oRef);
            if (dDist < dDiameter) {
                loReturn.add(o);
            }
        }
        return loReturn;
    }
    

    public static interface Characteristic<T> {
        public Double getCharacteristicValue(T pParameter);
    }
    
    public static interface Characteristic2<T> {
        public Double getCharacteristicValue(T pParameter, T pParameter2);
    }

}
