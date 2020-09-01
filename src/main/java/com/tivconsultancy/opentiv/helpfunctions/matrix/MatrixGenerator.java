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


import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class MatrixGenerator {

    public static int[][] createRandomInteger(int iSizeX, int iSizeY, int iFactor) {
        int[][] iaReturn = new int[iSizeY][iSizeX];

        for (int i = 0; i < iSizeY; i++) {

            for (int j = 0; j < iSizeX; j++) {

                iaReturn[i][j] = (int) (Math.random() * iFactor);

            }

        }

        return iaReturn;
    }
    
    public static int[][] createInteger(int iSizeX, int iSizeY, int iValue) {
        int[][] iaReturn = new int[iSizeY][iSizeX];

        for (int i = 0; i < iSizeY; i++) {

            for (int j = 0; j < iSizeX; j++) {

                iaReturn[i][j] = iValue;

            }

        }

        return iaReturn;
    }

    public static double[][] createQuadraticEqualizationMatrix(int iSize) {

        double[][] daReturn = new double[iSize][iSize];

        for (int i = 0; i < iSize; i++) {

            for (int j = 0; j < iSize; j++) {

                daReturn[i][j] = 1 / ((double) iSize * iSize);

            }

        }

        return daReturn;

    }

    public static double[][] createQuadraticMatrix(int iSize, double dValue) {

        double[][] daReturn = new double[iSize][iSize];

        for (int i = 0; i < iSize; i++) {

            for (int j = 0; j < iSize; j++) {

                daReturn[i][j] = dValue;

            }
        }
        return daReturn;
    }
    
    public static double[][] createRecMatrix(int iSize_i, int iSize_j, double dValue) {

        double[][] daReturn = new double[iSize_i][iSize_j];

        for (int i = 0; i < iSize_i; i++) {

            for (int j = 0; j < iSize_j; j++) {

                daReturn[i][j] = dValue;

            }
        }
        return daReturn;
    }

    public static double[] createLinearMatrix(int iSize, double dValue) {

        double[] daReturn = new double[iSize];
        for (int i = 0; i < iSize; i++) {
            daReturn[i] = dValue;
        }
        return daReturn;
        
    }

    public static int[][] createQuadraticMatrix(int iSize, int iValue) {

        int[][] iaReturn = new int[iSize][iSize];

        for (int i = 0; i < iSize; i++) {

            for (int j = 0; j < iSize; j++) {

                iaReturn[i][j] = iValue;

            }

        }

        return iaReturn;

    }

    public static int[][] createMatrix(int iSizeI, int iSizeJ, int iValue) {

        int[][] iaReturn = new int[iSizeI][iSizeJ];

        for (int i = 0; i < iSizeI; i++) {

            for (int j = 0; j < iSizeJ; j++) {

                iaReturn[i][j] = iValue;

            }

        }

        return iaReturn;

    }

    public static Integer[][] createMatrixObjectInt(int iSizeI, int iSizeJ, int iValue) {

        Integer[][] iaReturn = new Integer[iSizeI][iSizeJ];

        for (int i = 0; i < iSizeI; i++) {

            for (int j = 0; j < iSizeJ; j++) {

                iaReturn[i][j] = iValue;

            }

        }

        return iaReturn;

    }

    public static boolean[][] createQuadraticMatrix(int iSize, boolean bValue) {

        boolean[][] daReturn = new boolean[iSize][iSize];

        for (int i = 0; i < iSize; i++) {

            for (int j = 0; j < iSize; j++) {

                daReturn[i][j] = bValue;

            }

        }

        return daReturn;

    }

    public static boolean[][] createMatrix(int iHeight, int iWidth, boolean bValue) {

        boolean[][] baReturn = new boolean[iHeight][iWidth];

        for (int i = 0; i < iHeight; i++) {

            for (int j = 0; j < iWidth; j++) {

                baReturn[i][j] = bValue;

            }

        }

        return baReturn;

    }

    public int[][] createArbitaryBinaryArray(int iSizeX, int iSizeY, int iMultiplicator) {

        int[][] iaArbitaryArray = new int[iSizeY][iSizeX];

        for (int i = 0; i < iSizeY; i++) {

            for (int j = 0; j < iSizeX; j++) {

                double dTempoRandom = Math.random() - 0.5;

                if (dTempoRandom > 0) {
                    iaArbitaryArray[i][j] = iMultiplicator;
                } else {
                    iaArbitaryArray[i][j] = 0;
                }

            }

        }

        return iaArbitaryArray;

    }

    public static List<int[][]> getSobelOperator() {

        List<int[][]> liaSobel = new ArrayList<int[][]>();

        int[][] iaSobelX = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};

        liaSobel.add(iaSobelX);

        int[][] iaSobelY = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

        liaSobel.add(iaSobelY);

        return liaSobel;
    }
    
    public static int[][] getLaplaceOperator() {        

        int[][] iaLaplace = {{0, -1, 0}, {-1, 4, -1}, {0, -1, 0}};

        return iaLaplace;
    }

    public static List<int[][]> getPrewittOperator() {

        List<int[][]> liaPrewitt = new ArrayList<int[][]>();

        int[][] iaPrewittX = {{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};

        liaPrewitt.add(iaPrewittX);

        int[][] iaPrewittY = {{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};

        liaPrewitt.add(iaPrewittY);

        return liaPrewitt;
    }

    public static int[][] get5PointDifStar2DOperator() {

        int[][] iaDifStar2 = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

        return iaDifStar2;
    }

    public static int[][] GradX2DOperator() {

        int[][] iaDifX = {{0, 0, 0}, {1, 0, -1}, {0, 0, 0}};

        return iaDifX;
    }

    public static int[][] GradY2DOperator() {

        int[][] iaDifX = {{0, 1, 0}, {0, 0, 0}, {0, -1, 0}};

        return iaDifX;
    }

    public static double[][] get9PointDifStar2DOperator() {

        double[][] daDifStar2 = {{1 / 36.0, 1 / 9.0, 1 / 36.0}, {1 / 9.0, -4 / 9, 1 / 9}, {1 / 36, 1 / 9, 1 / 36}};

        return daDifStar2;
    }

    public static List<int[][]> getScharrOperator() {

        List<int[][]> liaScharr = new ArrayList<int[][]>();

        int[][] iaSharrX = {{3, 0, -3}, {10, 0, -10}, {3, 0, -3}};

        int[][] iaSharrY = {{3, 10, 3}, {0, 0, 0}, {-3, -10, -3}};

        liaScharr.add(iaSharrX);
        liaScharr.add(iaSharrY);

        return liaScharr;
    }

    public static int[][] getSharpenFil() {

        int[][] iaShaprF = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};

        return iaShaprF;

    }

    public static int[][] getSharpenFilStrong() {

        int[][] iaShaprF = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};

        return iaShaprF;

    }

    public static double[][] getSharpenFilVariable(double Neighbours4Point, double Neighbours8Point) {

        double dCenter = 4.0 * Neighbours4Point + 4.0 * Neighbours8Point + 1.0;

        double[][] iaShaprF = {{-Neighbours8Point, -Neighbours4Point, -Neighbours8Point}, {-Neighbours4Point, dCenter, -Neighbours4Point}, {-Neighbours8Point, -Neighbours4Point, -Neighbours8Point}};

        return iaShaprF;

    }

    public static double[][] getEdgeEnhance9(double dAmplification) {

        double dCenter = (9.0 * dAmplification - 1.0) / 9.0;

        double[][] iaShaprF = {{-1.0 / 9.0, -1.0 / 9.0, -1.0 / 9.0}, {-1.0 / 9.0, dCenter, -1.0 / 9.0}, {-1.0 / 9.0, -1.0 / 9.0, -1.0 / 9.0}};

        return iaShaprF;

    }

    public static double[][] getSharpenFilGauss() {
        double dFac = 1.0 / 273.0;
        double[][] iaSharpF = {{1 * dFac, 4 * dFac, 7 * dFac, 4 * dFac, 1 * dFac}, {4 * dFac, 16 * dFac, 26 * dFac, 16 * dFac, 4 * dFac}, {7 * dFac, 26 * dFac, 41 * dFac, 26 * dFac, 7 * dFac}, {4 * dFac, 16 * dFac, 26 * dFac, 16 * dFac, 4 * dFac}, {1 * dFac, 4 * dFac, 7 * dFac, 4 * dFac, 1 * dFac}};
        return iaSharpF;
    }
    
    public static double[][] getLoG(double sigma, int size){
        double[][] LoG = new double[2*size+1][2*size+1];
        for(int i = -1*size; i < size;i++){
            for(int j = -1*size; j < size;j++){
                double dFrac = -1.0*(Math.pow(i, 2)+ Math.pow(j, 2))/(2* Math.pow(sigma, 2));
                LoG[i+size][j+size] = -1.0/(Math.PI * Math.pow(sigma, 4)) * (1-dFrac) * Math.exp(dFrac);
            }            
        }
        return LoG;
    }
    
    public static int[][] getLoG(){
        return new int[][]{ 
            {0,1,1,2,2,2,1,1,0},
            {1,2,4,5,5,5,4,2,1},
            {1,4,5,3,0,3,5,4,1},
            {2,5,3,-12,-24,-12,3,5,2},
            {2,5,0,-24,-40,-24,0,5,2},
            {2,5,3,-12,-24,-12,3,5,2},
            {1,4,5,3,0,3,5,4,1},
            {1,2,4,5,5,5,4,2,1},
            {0,1,1,2,2,2,1,1,0}
        };
    }

    public static int[][] getRefliefFil() {

        int[][] iaShaprF = {{-2, -1, 0}, {-1, 1, 1}, {0, 1, 2}};

        return iaShaprF;

    }

    public static double[][] get3x3NormalDistribution() {

        double[][] daNormal = {{1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0}, {2.0 / 16.0, 4.0 / 16.0, 2.0 / 16.0}, {1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0}};

        return daNormal;
    }

    public static double[][] get5x5NormalDistribution() {

        double[][] daNormal = {{1.0 / 256.0, 2.0 / 256.0, 6.0 / 256.0, 4.0 / 256.0, 1.0 / 256.0}, {4.0 / 256.0, 16.0 / 256.0, 24.0 / 256.0, 16.0 / 256.0, 4.0 / 256.0}, {6.0 / 256.0, 24.0 / 256.0, 36.0 / 256.0, 24.0 / 256.0, 6.0 / 256.0}, {1.0 / 256.0, 2.0 / 256.0, 6.0 / 256.0, 4.0 / 256.0, 1.0 / 256.0}, {4.0 / 256.0, 16.0 / 256.0, 24.0 / 256.0, 16.0 / 256.0, 4.0 / 256.0}};

        return daNormal;
    }

    public static double[][] get3x3Smear() {

        double dS = (1.0 / 9.0);

        double[][] daNormal = {{dS, dS, dS}, {dS, dS, dS}, {dS, dS, dS}};

        return daNormal;
    }


    public static int[] create8PointCartesianX() {

        int[] iaCX = {1, 1, 0, -1, -1, -1, 0, 1};

        return iaCX;

    }

    /*public static int[] create8PointCartesianY(){
        
     int[] iaCY = {0,1,1,1,0,-1,-1,-1};
        
     return iaCY;
        
     }*/
    public static int[] create8PointMatrixY() {

        int[] iaCY = {0, -1, -1, -1, 0, 1, 1, 1};

        return iaCY;

    }

    public static int[][] createQuadraticMatrixCircleShape(int iRadius, int iValue) {

        int[][] iaQMCS = Matrix.initialize(2 * iRadius + 1, 2 * iRadius + 1, 0);

        for (int i = 0; i < 2 * iRadius; i++) {

            for (int j = 0; j < 2 * iRadius; j++) {

                if (Math.sqrt(((i - iRadius) * (i - iRadius)) + ((j - iRadius) * (j - iRadius))) < iRadius) {

                    iaQMCS[i][j] = iValue;
                    //iaQMCS[iRadius/2-i][iRadius/2-j] = iValue;

                }

            }

        }

        return iaQMCS;

    }

    public static int[][] fillAreas(int iSizeI, int iSizeJ, List<List<OrderedPair>> llopiAreas) {

        return null;
    }

    public static int[][] fillAreas(int[][] iaSample, List<List<OrderedPair>> llopiAreas, int iValue) {

        int[][] iaReturn = new int[iaSample.length][iaSample[0].length];

        for (List<OrderedPair> lopi : llopiAreas) {

            for (OrderedPair opi : lopi) {

                iaReturn[(int) opi.y][(int) opi.x] = iValue;

            }

        }

        return iaReturn;
    }

    public static int[][] fillMatrixAreas(int[][] iaSample, List<List<MatrixEntry>> llmeiAreas, int iValue) {

        int[][] iaReturn = new int[iaSample.length][iaSample[0].length];

        for (List<MatrixEntry> lmei : llmeiAreas) {

            for (MatrixEntry mei : lmei) {

                iaReturn[(int) mei.i][(int) mei.j] = iaReturn[(int) mei.i][(int) mei.j] + iValue;

            }

        }

        return iaReturn;
    }

    public static double[][] createCubicWithCoordAndSphereValue(int iChamberSize, int iSphereSize) {

        double[][] ioa3DCubic = new double[iChamberSize * iChamberSize * iChamberSize][4];

        int iCount = 0;

        for (int i = 0; i < iChamberSize; i++) {
            for (int j = 0; j < iChamberSize; j++) {
                for (int t = 0; t < iChamberSize; t++) {
                    ioa3DCubic[iCount][0] = i;
                    ioa3DCubic[iCount][1] = j;
                    ioa3DCubic[iCount][2] = t;
                    ioa3DCubic[iCount][3] = 0;
                    iCount++;
                }
            }
        }

        for (int i = 0; i < iChamberSize * iChamberSize * iChamberSize; i++) {
            double dDistance = Math.sqrt((ioa3DCubic[i][0] - (iChamberSize / 2)) * (ioa3DCubic[i][0] - (iChamberSize / 2))
                    + (ioa3DCubic[i][1] - (iChamberSize / 2)) * (ioa3DCubic[i][1] - (iChamberSize / 2))
                    + (ioa3DCubic[i][2] - (iChamberSize / 2)) * (ioa3DCubic[i][2] - (iChamberSize / 2)));
            if (dDistance <= iSphereSize) {
                ioa3DCubic[i][3] = 1 - (dDistance / iSphereSize);
            }
        }

        return ioa3DCubic;

    }

}
