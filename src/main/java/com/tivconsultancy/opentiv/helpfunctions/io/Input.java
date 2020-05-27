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

import com.tivconsultancy.opentiv.helpfunctions.operations.Convert;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Input {
    
    public static List<OrderedPair> readIn(String sFile) throws IOException {
        Reader oRead = new Reader(sFile);
        List<OrderedPair> lopReadIn = new ArrayList<>();
        List<String[]> lsIn = oRead.readFileStringa();
        for (int i = 1; i < lsIn.size(); i++) {
            lopReadIn.add(Convert.convertToOP(lsIn.get(i),0,1,2));
        }

        return lopReadIn;
    }
    
}
