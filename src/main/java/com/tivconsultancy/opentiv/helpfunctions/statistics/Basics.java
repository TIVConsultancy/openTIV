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
package com.tivconsultancy.opentiv.helpfunctions.statistics;

import com.tivconsultancy.opentiv.math.interfaces.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Basics {

    public static double median(Double[] p) {
        double a;
        Arrays.sort(p);
        if (p.length % 2 == 0) {
            a = (p[p.length / 2] + p[p.length / 2 - 1]) / 2;
        } else {
            a = p[p.length / 2];
        }
        return a;
    }

    public static Double median(List<? extends Number> ln) {
        Collections.sort(ln, new Comparator<Number>() {

            @Override
            public int compare(Number o1, Number o2) {
                Double d1 = o1.doubleValue();
                Double d2 = o2.doubleValue();
                if (d1 < d2) {
                    return -1;
                }
                if (Objects.equals(d1, d2)) {
                    return 0;
                }
                return 1;
            }
        });
        if (ln.isEmpty()) {
            return null;
        }
        return ln.get((int) ln.size() / 2).doubleValue();

    }

    public static double median(List<?> ln, Value oValue) {
        Collections.sort(ln, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                Double d1 = oValue.getValue(o1);
                Double d2 = oValue.getValue(o2);
                if (d1 < d2) {
                    return -1;
                }
                if (Objects.equals(d1, d2)) {
                    return 0;
                }
                return 1;
            }
        });
        return oValue.getValue(ln.get((int) ln.size() / 2));

    }

    public static double median(Object[] oIn, Value oValue) {
        List<Object> ln = Arrays.asList(oIn);
        Collections.sort(ln, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                Double d1 = oValue.getValue(o1);
                Double d2 = oValue.getValue(o2);
                if (d1 < d2) {
                    return -1;
                }
                if (Objects.equals(d1, d2)) {
                    return 0;
                }
                return 1;
            }
        });
        return oValue.getValue(ln.get((int) ln.size() / 2));

    }

    public static Double getVariance(List<? extends Number> ldaInput, Double daAvg) {
        if (daAvg != null) {
            Double daVariance = 0.0;
            int iCount = 0;
            for (int i = 0; i < ldaInput.size(); i++) {
                daVariance = daVariance + (ldaInput.get(i).doubleValue() - daAvg) * (ldaInput.get(i).doubleValue() - daAvg);
                iCount++;

            }
            if (iCount > 0) {
                return daVariance / iCount;
            }
        }
        return null;
    }

    public static Double getVariance(List<?> loInput, Value oValue, Double daAvg) {
        if (daAvg != null) {
            Double daVariance = 0.0;
            int iCount = 0;
            for (int i = 0; i < loInput.size(); i++) {
                daVariance = daVariance + (oValue.getValue(loInput.get(i)) - daAvg) * (oValue.getValue(loInput.get(i)) - daAvg);
                iCount++;
            }
            if (iCount > 0) {
                return daVariance / iCount;
            }
        }
        return null;
    }

    public static Double Arithmetic(List<? extends Number> liaInput) {

        Double dReturn = 0.0;

        for (int i = 0; i < liaInput.size(); i++) {
            dReturn = dReturn + liaInput.get(i).doubleValue();
        }

        dReturn = dReturn / liaInput.size();

        return dReturn;
    }

    public static Double Arithmetic(List<?> loInput, Value oValue) {

        Double dReturn = 0.0;

        for (int i = 0; i < loInput.size(); i++) {
            dReturn = dReturn + oValue.getValue(loInput.get(i));
        }

        dReturn = dReturn / loInput.size();

        return dReturn;
    }

    public static Double Arithmetic(Object[] loInput, Value oValue) {

        Double dReturn = 0.0;

        for (int i = 0; i < loInput.length; i++) {
            dReturn = dReturn + oValue.getValue(loInput[i]);
        }

        dReturn = dReturn / loInput.length;

        return dReturn;
    }

}
