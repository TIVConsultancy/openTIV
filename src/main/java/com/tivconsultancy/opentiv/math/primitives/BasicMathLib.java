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

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;

/**
 *
 * @author Thomas Ziegenhein
 */
public class BasicMathLib {

    public static double SecondCartesian(double x1, double y1, double x2, double y2) {
        return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
    }

    public static OrderedPair pqformulat(double p, double q) {
        double w = (Math.pow(p, 2) - 4.0 * q);
        if (w < 0) {
            return null;
        }
        return new OrderedPair(0.5 * (-1.0 * p + Math.sqrt(w)), 0.5 * (-1.0 * p - Math.sqrt(w)));
    }
}
