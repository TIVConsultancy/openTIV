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
package com.tivconsultancy.opentiv.imageproc.algorithms.algorithms;

import com.tivconsultancy.opentiv.imageproc.contours.CPX;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Sorting_IMG {        
    
    public static EnumObject getclosest(CPX oReference, HashSet<CPX> loInput) throws EmptySetException {

        EnumObject oHelp = Sorting.getMinCharacteristic(loInput, oReference, new Sorting.Characteristic2<CPX>() {

            @Override
            public Double getCharacteristicValue(CPX pParameter, CPX pParameter2) {
                if (pParameter.oEnd == null || pParameter.oStart == null || pParameter2.oEnd == null || pParameter2.oStart == null) {
                    return Double.MAX_VALUE;
                }
                if (pParameter.equals(pParameter2)) {
                    return Double.MAX_VALUE;
                }
                Double d1 = pParameter.oEnd.getNorm(pParameter2.oStart);
                Double d2 = pParameter.oEnd.getNorm(pParameter2.oEnd);
                Double d3 = pParameter.oStart.getNorm(pParameter2.oStart);
                Double d4 = pParameter.oStart.getNorm(pParameter2.oEnd);
                return Math.abs(Math.min(d1, Math.min(d2, Math.min(d3, d4))));
            }
        });

        return oHelp;

    }

    public static List<EnumObject> getclosest(CPX oReference, HashSet<CPX> lo, double dNorm) throws EmptySetException {

        if (oReference.oEnd == null || oReference.oStart == null) {
            return new ArrayList<>();
        }
        ArrayList<EnumObject> loHelp = new ArrayList<>();
        for (CPX o : lo) {
            if (o == null || o.oStart == null || o.oEnd == null) {
                continue;
            }
            Double d1 = oReference.oEnd.getNorm(o.oStart);
            Double d2 = oReference.oEnd.getNorm(o.oEnd);
            Double d3 = oReference.oStart.getNorm(o.oStart);
            Double d4 = oReference.oStart.getNorm(o.oEnd);
            loHelp.add(new EnumObject(Math.abs(Math.min(d1, Math.min(d2, Math.min(d3, d4)))), o));
        }

        Collections.sort(loHelp, new Comparator<EnumObject>() {
            @Override
            public int compare(EnumObject o1, EnumObject o2) {
                return ((int) (o1.dEnum - o2.dEnum));
            }
        });

        List<EnumObject> loReturn = new ArrayList<>();

        for (EnumObject o : loHelp) {
            if (o.dEnum <= dNorm) {
                loReturn.add(o);
            }
        }

        return loReturn;

    }
}
