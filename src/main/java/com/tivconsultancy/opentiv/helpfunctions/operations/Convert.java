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
package com.tivconsultancy.opentiv.helpfunctions.operations;

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Convert {

    public static List<OrderedPair> convert(Double[][] ddaIn, int iXColumn, int iYColumn, int iValueColumn) {

        List<OrderedPair> lop = new ArrayList<>();
        
        for (Double[] da : ddaIn) {
            if (da != null && da[iXColumn] != null && da[iYColumn] != null) {
                OrderedPair op = new OrderedPair(da[iXColumn], da[iYColumn]);
                op.dValue = da[iValueColumn];
                lop.add(op);
            }
        }

        return lop;
    }

    public static List<List<OrderedPair>> convert(List<Double[][]> ldaIn, int iXColumn, int iYColumn, int iValueColumn) {

        List<List<OrderedPair>> llop = new ArrayList<>();
        for (Double[][] dda : ldaIn) {
            List<OrderedPair> lop = convert(dda, iXColumn, iYColumn, iValueColumn);
            llop.add(lop);
        }
        return llop;
    }

    public static List<Double> getValues(List<OrderedPair> lopInput) {
        List<Double> ldReturn = new ArrayList<>();
        for (OrderedPair op : lopInput) {
            ldReturn.add(op.dValue);
        }
        return ldReturn;
    }
    
    public static OrderedPair convertToOP(String[] sa, int iX, int iY, int iValue) {
        OrderedPair op = new OrderedPair();
        if (sa != null && !sa.equals("")) {
            op.x = Double.valueOf(sa[iX]);
            op.y = Double.valueOf(sa[iY]);
            op.dValue = Double.valueOf(sa[iValue]);
        }
        return op;
    }

}
