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
public class trigonometrics {

    public static int getDiscreteDiretion(int iX, int iY) {

        /*
         * Analysis in the first and second quadrant. Returns the multiplication of pi/4
         */

        int iReturn = 0;

        if (iX != 0) {
            
            double dFraction = (double) iY / (double) iX;

            double dArc = Math.atan(dFraction);

            if (Math.abs(dArc) > (Math.PI * 3.0 / 8.0)) {
                iReturn = 2;
            }

            if (Math.abs(dArc) < (Math.PI * 1.0 / 8.0)) {
                iReturn = 0;
            }

            if (Math.abs(dArc) <= (Math.PI * 3.0 / 8.0) && Math.abs(dArc) >= (Math.PI * 1.0 / 8.0)) {

                if (dArc < 0) {
                    iReturn = 3;
                } else {
                    iReturn = 1;
                }

            }
        } else {
            
            if(iY != 0){
                iReturn = 2;
            } else {
                iReturn = -1;
            }
            
            
        }

        /*if (x != 0) {

         if (Math.abs(Math.tan(((double) y) / ((double) x))) < Math.tan(1.0 / 8.0 * Math.PI)) {
         iReturn = 0;
         }

         if (Math.tan(((double) y) / ((double) x)) <= Math.tan(3.0 / 8.0 * Math.PI) && Math.tan(((double) y) / ((double) x)) >= Math.tan(1.0 / 8.0 * Math.PI)) {
         iReturn = 1;
         }

         if (Math.abs(Math.tan(((double) y) / ((double) x))) > Math.tan(3.0 / 8.0 * Math.PI)) {
         iReturn = 2;
         }

         if (Math.tan(((double) y) / ((double) x)) <= Math.tan(-1.0 / 8.0 * Math.PI) && Math.tan(((double) y) / ((double) x)) >= Math.tan(-3.0 / 8.0 * Math.PI)) {
         iReturn = 3;
         }
         }*/



        return iReturn;

    }

    public static int[][] getDirection(int[][] iaInputGradientX, int[][] iaInputGradientY) {
        /*
         * Analysis the direction of all X, Y Values
         */
        int[][] iaReturn = new int[iaInputGradientX.length][iaInputGradientX[0].length];

        for (int i = 0; i < iaInputGradientX.length; i++) {

            for (int j = 0; j < iaInputGradientX[0].length; j++) {

                iaReturn[i][j] = getDiscreteDiretion(iaInputGradientX[i][j], iaInputGradientY[i][j]);

            }

        }

        return iaReturn;

    }
    
    public static int[] getDirection(int[] iaInputGradientX, int[] iaInputGradientY) {
        /*
         * Analysis the direction of all X, Y Values
         */
        int[]iaReturn = new int[iaInputGradientX.length];

        for (int i = 0; i < iaInputGradientX.length; i++) {


                iaReturn[i] = getDiscreteDiretion(iaInputGradientX[i], iaInputGradientY[i]);

            

        }

        return iaReturn;

    }
}
