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
package com.tivconsultancy.opentiv.helpfunctions.matrix;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Reshape {
    public static int[] reshape(int[][] naInput) {
        /**
         *
         */
        int[] iaExtractedArray = new int[naInput.length * naInput[0].length];

        int iZae = 0;
        for (int i = 0; i < naInput.length; i++) {
            for (int j = 0; j < naInput[0].length; j++) {

                iaExtractedArray[iZae] = (int) naInput[i][j];

                iZae = iZae + 1;

            }

        }

        return (iaExtractedArray);
    }
}
