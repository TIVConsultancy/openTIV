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
package com.tivconsultancy.opentiv.helpfunctions.io;

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Output {
    public static void WriteListOrderedPair(String sFile, List<? extends OrderedPair> lop, boolean bHeader) {
        List<String[]> lsSave = new ArrayList<String[]>();

        if (bHeader) {
            {
                String[] saTemp = new String[3];
                saTemp[0] = "Position x, ";
                saTemp[1] = "Position y, ";
                saTemp[1] = "Value";
                lsSave.add(saTemp);
            }
        }

        for (OrderedPair op : lop) {
            if (op != null) {
                String[] saTemp = new String[2];
                saTemp[0] = String.valueOf(op.x) + ",";
                saTemp[0] = String.valueOf(op.y) + ",";
                saTemp[1] = String.valueOf(op.dValue);
                lsSave.add(saTemp);
            }
        }

        Writer oWriter = new Writer(sFile);

        oWriter.writels(lsSave);
    }
}
