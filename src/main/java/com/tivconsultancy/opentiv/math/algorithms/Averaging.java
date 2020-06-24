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

import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Averaging {

    public static double getMeanAverage(List<?> lo, Value<Object> oValue) {
        if (lo.isEmpty()) {
            return Double.NaN;
        }
        double dIntegrate = 0.0;
        if (oValue != null) {
            for (Object o : lo) {
                dIntegrate = dIntegrate + oValue.getValue(o);
            }
        }else{
            for (Object o : lo) {
                dIntegrate = dIntegrate + Double.valueOf(o.toString());
            }
        }

        return dIntegrate / (1.0 * lo.size());
    }

    public static List<OrderedPair> LinearSmooth(List<OrderedPair> lop, String sType) {

        if (sType.equals("FWD")) {
            List<OrderedPair> lopSmooth = new ArrayList<>();

            for (int i = 0; i < lop.size() - 1; i++) {
                double dX = (lop.get(i).x + lop.get(i + 1).x) / 2;
                double dY = (lop.get(i).y + lop.get(i + 1).y) / 2;
                double dVal = (lop.get(i).dValue + lop.get(i + 1).dValue) / 2;
                OrderedPair op = new OrderedPair(dX, dY);
                op.dValue = dVal;
                lopSmooth.add(op);
            }
            return lopSmooth;
        }

        throw new UnsupportedOperationException("not yet implemented for type: " + sType);

    }

    public static Double linear(List<? extends Number> lo) {
        double dSum = 0.0;
        double dWeight = 0.0;
        for (Number o : lo) {
            dSum = dSum + o.doubleValue();
            dWeight = dWeight + 1.0;
        }
        return dSum / dWeight;
    }

}
