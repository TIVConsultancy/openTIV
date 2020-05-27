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

import static com.tivconsultancy.opentiv.helpfunctions.operations.MatrixOperations.addition;
import static com.tivconsultancy.opentiv.helpfunctions.operations.MatrixOperations.substraction;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein HZDR Fluidynamics, t.ziegenhein@hzdr.de
 */
public class Matrix {

    public static int[][] summation(int[][] ia1, int[][] ia2) {
        int[][] iaReturn = new int[ia1.length][ia1[0].length];
        for (int i = 0; i < ia1.length; i++) {
            for (int j = 0; j < ia1[0].length; j++) {
                iaReturn[i][j] = ia1[i][j] + ia2[i][j];
            }
        }
        return iaReturn;
    }
    
    public static int sum(int[][] ia1) {
        int iaReturn = 0;
        for (int i = 0; i < ia1.length; i++) {
            for (int j = 0; j < ia1[0].length; j++) {
                iaReturn = iaReturn + ia1[i][j];
            }
        }
        return iaReturn;
    }

    public static Double[][] reshape(Number[] naD1, int iwidth, int iheight) {
        /**
         * n=height , m=width, M_s -> M_nm s>= m*n
         */
        Double[][] D2Array = new Double[iheight][iwidth];
        int i = 0;
        int j = 0;

        for (int k = 0; k < naD1.length; k++) {
            D2Array[j][i] = (Double) naD1[k];
            i++;
            if (i == iwidth) {
                i = 0;
                j++;
            }
        }

        return D2Array;

    }

    public static Double[] reshape(Number[][] naArray, int iRow) {
        /**
         * return the n-th row of naArray , n=iRow s = line M_nm -> M_1m (n=s),
         * s <= n
         */
        if (iRow < naArray.length) {
            Double[] naArrayline = new Double[naArray[iRow].length];
            for (int i = 0; i < naArrayline.length; i++) {
                naArrayline[i] = (Double) naArray[iRow][i];
            }
            return naArrayline;
        } else {
            System.out.println(iRow + " as input extends Array, null given");
            return null;
        }
    }

    public static Double[][] reshape(Number[][] naInput, int[] iaColumns) {
        /**
         * get the columns in iaColumns s_1,s_2,...s_t = iaColumns , M_nm =>
         * M_nt (m = s_1,..s_t), s <= t <=n
         */
        Double[][] daExtractedArray = new Double[naInput.length][iaColumns.length];

        for (int i = 0; i < naInput.length; i++) {

            for (int j = 0; j < iaColumns.length; j++) {

                daExtractedArray[i][j] = (Double) naInput[i][iaColumns[j]];
            }

        }
        return (daExtractedArray);
    }

    public static Double[][] reshape(Number[][] naInput, Integer[] iaColumns) {
        /**
         * get the columns in iaColumns s_1,s_2,...s_t = iaColumns , M_nm =>
         * M_nt (m = s_1,..s_t), s <= t <=n
         */
        Double[][] daExtractedArray = new Double[naInput.length][iaColumns.length];

        for (int i = 0; i < naInput.length; i++) {

            for (int j = 0; j < iaColumns.length; j++) {

                daExtractedArray[i][j] = (Double) naInput[i][iaColumns[j]];
            }

        }
        return (daExtractedArray);
    }

    public static Double[][] reshapeDeleteColumn(Number[][] naInput, int iColumn) {
        /**
         * get the columns in iaColumns s_1,s_2,...s_t = iaColumns , M_nm =>
         * M_nt (m = s_1,..s_t), s <= t <=n
         */
        Double[][] daExtractedArray = new Double[naInput.length][naInput[0].length - 1];

        for (int i = 0; i < naInput.length; i++) {

            int iZae = 0;
            for (int j = 0; j < naInput[0].length; j++) {

                if (j != iColumn) {
                    daExtractedArray[i][iZae] = (Double) naInput[i][j];
                    iZae = iZae + 1;
                }
            }

        }
        return (daExtractedArray);
    }

    public static Integer[][] reshapeDeleteColumnI(Number[][] naInput, int iColumn) {
        /**
         * get the columns in iaColumns s_1,s_2,...s_t = iaColumns , M_nm =>
         * M_nt (m = s_1,..s_t), s <= t <=n
         */
        Integer[][] daExtractedArray = new Integer[naInput.length][naInput[0].length - 1];

        for (int i = 0; i < naInput.length; i++) {

            int iZae = 0;
            for (int j = 0; j < naInput[0].length; j++) {

                if (j != iColumn) {
                    daExtractedArray[i][iZae] = (Integer) naInput[i][j];
                    iZae = iZae + 1;
                }
            }

        }
        return (daExtractedArray);
    }

    public static int[] reshapeDeleteColumnI(int[] naInput, int iColumn) {
        /**
         * get the columns in iaColumns s_1,s_2,...s_t = iaColumns , M_nm =>
         * M_nt (m = s_1,..s_t), s <= t <=n
         */
        int[] iaExtractedArray = new int[naInput.length - 1];

        int iZae = 0;
        for (int i = 0; i < naInput.length; i++) {

            if (i != iColumn) {
                iaExtractedArray[iZae] = (int) naInput[i];
                iZae = iZae + 1;
            }
        }

        return (iaExtractedArray);
    }

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

    public static int[][] reshape(int[] naInput, int iHeight, int iWidth) {
        /**
         *
         */
        int[][] iaExtractedArray = new int[iHeight][iWidth];

        int iZae = 0;
        for (int i = 0; i < iHeight; i++) {
            for (int j = 0; j < iWidth; j++) {

                iaExtractedArray[i][j] = (int) naInput[iZae];

                iZae = iZae + 1;

            }

        }

        return (iaExtractedArray);
    }

    public static List<int[][]> reshape(int[] naInput, int iHeight, int iWidth, int iCount) {
        /**
         *
         */
        List<int[][]> iaReturn = new ArrayList<int[][]>();

        int iZae = 0;

        for (int t = 0; t < iCount; t++) {

            int[][] iaExtractedArray = new int[iHeight][iWidth];

            for (int i = 0; i < iHeight; i++) {
                for (int j = 0; j < iWidth; j++) {

                    iaExtractedArray[i][j] = (int) naInput[iZae];

                    iZae = iZae + 1;

                }
            }

            iaReturn.add(iaExtractedArray);

        }

        return iaReturn;
    }

    public static int[] reshape(List<int[][]> liaInput) {
        /**
         *
         */
        int[][] iabsp = liaInput.get(0);

        int[] iaExtractedArray = new int[iabsp.length * iabsp[0].length * liaInput.size()];

        int iZae = 0;
        for (int t = 0; t < liaInput.size(); t++) {

            int[][] naInput = liaInput.get(t);

            for (int i = 0; i < naInput.length; i++) {
                for (int j = 0; j < naInput[0].length; j++) {

                    iaExtractedArray[iZae] = (int) naInput[i][j];

                    iZae = iZae + 1;

                }

            }
        }

        return (iaExtractedArray);
    }

    public static Double[][] reshapeRows(Number[][] naInput, int[] iaRows) {
        /**
         * get the columns in iaColumns s_1,s_2,...s_t = iaColumns , M_nm =>
         * M_nt (m = s_1,..s_t), s <= t <=n
         */
        Double[][] daExtractedArray = new Double[iaRows.length][naInput[0].length];

        for (int i = 0; i < iaRows.length; i++) {

            for (int j = 0; j < naInput[0].length; j++) {

                daExtractedArray[i][j] = (Double) naInput[iaRows[i]][j];
            }

        }
        return (daExtractedArray);
    }

    public static Double[][] reshapeRows(Number[][] naInput, Double[] daRows) {
        /**
         * get the columns in iaColumns s_1,s_2,...s_t = iaColumns , M_nm =>
         * M_nt (m = s_1,..s_t), s <= t <=n
         */
        Double[][] daExtractedArray = new Double[daRows.length][naInput[0].length];

        for (int i = 0; i < daRows.length; i++) {

            for (int j = 0; j < naInput[0].length; j++) {

                daExtractedArray[i][j] = (Double) naInput[(int) (double) daRows[i]][j];
            }

        }
        return (daExtractedArray);
    }

    public static Double[] reshapeC(Number[][] naInput, int iColumn) {
        /**
         * get the column in iColumn M_nm => M_n1
         */
        Double[] daExtractedArray = new Double[naInput.length];

        for (int i = 0; i < naInput.length; i++) {

            //System.out.println(i+ ", " + j+ ", " + iaColumns[j]);
            //System.out.println(daExtractedArray[i][j]);
            daExtractedArray[i] = (Double) naInput[i][iColumn];

        }

        return (daExtractedArray);
    }

    public static Double[] reshapeC(Number[] naInput, int iStart, int iEnd) {
        /**
         * get the column in iColumn M_nm => M_n1
         */
        Double[] daExtractedArray = new Double[iEnd - iStart];

        for (int i = 0; i < (iEnd - iStart); i++) {

            //System.out.println(i+ ", " + j+ ", " + iaColumns[j]);
            //System.out.println(daExtractedArray[i][j]);
            daExtractedArray[i] = (Double) naInput[iStart + i];

        }

        return (daExtractedArray);
    }

    public static Double[] reshapeVec(Number[] naInput, int iStart, int iEnd) {
        /**
         * get the column in iColumn M_nm => M_n1
         */
        Double[] daExtractedArray = new Double[iEnd - iStart];

        // System.out.println((iEnd - iStart));
        // System.out.println(naInput.length);
        for (int i = 0; i < daExtractedArray.length; i++) {

            //System.out.println(i+ ", " + j+ ", " + iaColumns[j]);
            //System.out.println(daExtractedArray[i][j]);
            daExtractedArray[i] = (Double) naInput[i + iStart];

        }

        return (daExtractedArray);
    }

    public static Double[][] reshape(Double[][] daInput, int iRowNumber, int iSwitch) {
        /**
         * s = iRowNumber, M_nm -> M_(n-s)m | iSwitch not 0 else M_nm -> M_sm
         * Cut column at iRowNumber, if iSwitch == 0 or null return ([i >
         * iRowNumber]) else return(daOutput[i < iRowNumber]), iRowNumber
         * counted from zero
         *
         */
        Double[][] daCutInput;

        if (iSwitch == 0) {
            daCutInput = new Double[daInput.length - (iRowNumber)][daInput[0].length];

            for (int i = iRowNumber; i < daInput.length; i++) {
                System.arraycopy(daInput[i], 0, daCutInput[i - iRowNumber], 0, daInput[0].length);

            }

        } else {
            daCutInput = new Double[(iRowNumber + 1)][daInput[0].length];

            for (int i = 0; i < daInput.length - (iRowNumber + 1); i++) {
                System.arraycopy(daInput[i], 0, daCutInput[i], 0, daInput[0].length);

            }

        }

        return (daCutInput);

    }

    public static Double[][] reshape(Double[] daInput1, Double[] daInput2) {
        /**
         * M_n and M_m -> M_nm
         */
        if (daInput1 != null && daInput2 != null && daInput1.length == daInput2.length) {

            Double[][] daUnitArray = new Double[daInput1.length][2];

            for (int i = 0; i < daInput1.length; i++) {

                daUnitArray[i][0] = daInput1[i];
                daUnitArray[i][1] = daInput2[i];

            }

            return (daUnitArray);

        } else {
            System.out.println("Warning object: null return");
            return (null);

        }

    }

    public static Double[][] reshape(Double[][] daInput1, Double[][] daInput2) {
        /**
         * M_nm and M_kh -> M_(n+k)(m+h)
         */
        if (daInput1 != null && daInput2 != null && daInput1.length == daInput2.length) {
            Double[][] daUnitArray = new Double[daInput1.length][daInput1[0].length + daInput2[0].length];

            for (int i = 0; i < daInput1.length; i++) {

                for (int j = 0; j < daInput1[0].length; j++) {

                    daUnitArray[i][j] = daInput1[i][j];

                }

                for (int k = 0; k < daInput2[0].length; k++) {

                    daUnitArray[i][daInput1[0].length + k] = daInput2[i][k];

                }

            }

            return (daUnitArray);

        } else {
            System.out.println("Warning object: null return");
            return (null);

        }

    }

    public static Double[][] reshapeRow(Double[][] daInput1, Double[][] daInput2) {
        /**
         * M_nm and M_kh -> M_(n+k)(m+h)
         */
        if (daInput1 != null && daInput2 != null && daInput1[0].length == daInput2[0].length) {
            Double[][] daUnitArray = new Double[daInput1.length + daInput2.length][daInput1[0].length];

            for (int i = 0; i < daInput1[0].length; i++) {

                for (int j = 0; j < daInput1.length; j++) {

                    daUnitArray[j][i] = daInput1[j][i];

                }

                for (int k = 0; k < daInput2.length; k++) {

                    daUnitArray[daInput1.length + k][i] = daInput2[k][i];

                }

            }

            return (daUnitArray);

        } else {
            System.out.println("Warning object: null return");
            return (null);

        }

    }

    public static Double[][] reshape(Double[] daInput1, Double[][] daInput2) {
        /**
         * M_nm and M_kh -> M_(n+k)(m+h)
         */
        if (daInput1 != null && daInput2 != null && daInput1.length == daInput2.length) {
            Double[][] daUnitArray = new Double[daInput1.length][1 + daInput2[0].length];

            for (int i = 0; i < daInput1.length; i++) {

                for (int j = 0; j < 1; j++) {

                    daUnitArray[i][j] = daInput1[i];

                }

                for (int k = 0; k < daInput2[0].length; k++) {

                    daUnitArray[i][1 + k] = daInput2[i][k];

                }

            }

            return (daUnitArray);

        } else {
            System.out.println("Warning object: null return");
            return (null);

        }

    }

    public static Double[][] reshape(Double[][] daInput1, Double[] daInput2) {
        /**
         * M_nm and M_kh -> M_(n+k)(m+h)
         */
        if (daInput1 != null && daInput2 != null && daInput1.length == daInput2.length) {
            Double[][] daUnitArray = new Double[daInput1.length][1 + daInput1[0].length];

            for (int i = 0; i < daInput1.length; i++) {

                for (int j = 0; j < daInput1[0].length; j++) {

                    daUnitArray[i][j] = daInput1[i][j];

                }

                for (int k = daInput1[0].length; k < daInput1[0].length + 1; k++) {

                    daUnitArray[i][k] = daInput2[i];

                }

            }

            return (daUnitArray);

        } else {
            System.out.println("Warning object: null return");
            return (null);

        }

    }

    /* public static Double[][] reshape(Double[] daInput1, Double[] daInput2){
    
     Double[][] daReshape=null;
    
     if(daInput1.length==daInput2.length){
    
     daReshape=new Double[daInput1.length][2];
    
     }
    
     return daReshape;
    
     }*/
    public static Double[][] tosort(Double[][] daToSort, final int iColumn) {
        /**
         * Sort an Array for column iCOlumn
         */
        java.util.Arrays.sort(daToSort, new java.util.Comparator<Double[]>() {
            @Override
            public int compare(Double[] a, Double[] b) {
                int istatus = 0;
                if ((a[iColumn] - b[iColumn]) < 0) {
                    istatus = -1;
                } else if ((a[iColumn] - b[iColumn]) > 0) {
                    istatus = 1;
                }
                return (istatus);
            }
        });
        return daToSort;
    }

    public static int[][] tosort(int[][] iaToSort, final int iColumn) {
        /**
         * Sort an Array for column iCOlumn
         */
        java.util.Arrays.sort(iaToSort, new java.util.Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return (a[iColumn] - b[iColumn]);
            }
        });
        return iaToSort;
    }

    public static Double[] tosort(Double[] daToSort) {
        /**
         * Sort an Array for column iCOlumn
         */
        java.util.Arrays.sort(daToSort);
        /*java.util.Arrays.sort(daToSort, new java.util.Comparator<Double>() {
        
         @Override
         public Double compare(a, b) {
         return (a - b);
         }
         });*/
        return daToSort;
    }

    /*   public int compareTo(Matrix oMatrix, int row){
     int state = 0;
    
    
     return(state);
    
     }*/
 /*   public static int[] getLineArray2D(int[][] array, orderdPair left, orderdPair right) {
     int line = (int) left.y;
     if (line < array.length && (int) left.x > 0 && (int) right.x < array[0].length) {
     int[] arrayline = new int[((int) right.x - (int) left.x) + 1];
     int j = 0;
     for (int i = (int) left.x; i < (int) right.x + 1; i++) {
     arrayline[j] = array[line][i];
     j++;
     }
     return arrayline;
     } else {
     System.out.println(line + " as input extends Array, null given");
     return null;
     }
     }*/
    public static Double[][] getIdentity(int iHeight, int iWidth) {
        Double[][] daIdentity = new Double[iHeight][iWidth];

        for (int i = 0; i < iHeight; i++) {

            for (int j = 0; j < iWidth; j++) {

                if (i == j) {

                    daIdentity[i][j] = 1.0;

                } else {

                    daIdentity[i][j] = 0.0;
                }
            }

        }

        return daIdentity;
    }

    public static Double[][] getElementarMatrix(int iHeight, int iWidth, int iScalarIndexM, int iScalarIndexN, Double dScalar) {
        /**
         * Return an Matrix M_mn m=iHeight,n=iWidth , with a one entry at
         * iScalarIndexM and iScalarIndexN with dScalar
         */
        Double[][] daElementarMatrix = new Double[iHeight][iWidth];

        for (int i = 0; i < iHeight; i++) {

            for (int j = 0; j < iWidth; j++) {

                daElementarMatrix[i][j] = 0.0;

            }

        }

        daElementarMatrix[iScalarIndexM][iScalarIndexN] = dScalar;

        return daElementarMatrix;
    }

    public static Double[][] getElementarMatrixRowChange(int iHeight, int iWidth, int iRowNumberi, int iRowNumberj) {
        /**
         * Return elementary matrix T_ij for row change
         * http://en.wikipedia.org/wiki/Elementary_matrix#Row-switching_transformations
         */
        Double[][] ElementarMatrixColumnChange = new Double[iHeight][iWidth];
        {
            Double[][] daIdentiy = Matrix.getIdentity(iHeight, iWidth);

            Double[][] ElementarMatrixii = getElementarMatrix(iHeight, iWidth, iRowNumberi, iRowNumberi, 1.0);

            ElementarMatrixColumnChange = substraction(daIdentiy, ElementarMatrixii);
        }
        {
            Double[][] ElementarMatrixij = getElementarMatrix(iHeight, iWidth, iRowNumberi, iRowNumberj, 1.0);
            ElementarMatrixColumnChange = addition(ElementarMatrixColumnChange, ElementarMatrixij);
        }
        {
            Double[][] ElementarMatrixjj = getElementarMatrix(iHeight, iWidth, iRowNumberj, iRowNumberj, 1.0);
            ElementarMatrixColumnChange = substraction(ElementarMatrixColumnChange, ElementarMatrixjj);
        }
        {
            Double[][] ElementarMatrixji = getElementarMatrix(iHeight, iWidth, iRowNumberj, iRowNumberi, 1.0);
            ElementarMatrixColumnChange = addition(ElementarMatrixColumnChange, ElementarMatrixji);
        }

        return ElementarMatrixColumnChange;

    }

    public static Double[][] getElementarMatrixRowAddition(int iHeight, int iWidth, int iRowNumberi, int iRowNumberj, Double dSCalar) {

        Double[][] ElementarMatrixColumnChange = new Double[iHeight][iWidth];
        {
            Double[][] daIdentiy = Matrix.getIdentity(iHeight, iWidth);

            Double[][] ElementarMatrixii = getElementarMatrix(iHeight, iWidth, iRowNumberi, iRowNumberj, dSCalar);

            ElementarMatrixColumnChange = addition(daIdentiy, ElementarMatrixii);
        }

        return ElementarMatrixColumnChange;

    }

    public static Double[][] initialize(int iHeight, int iWidth, Double dScalar) {

        Double[][] daOutput = new Double[iHeight][iWidth];

        for (int i = 0; i < daOutput.length; i++) {
            for (int j = 0; j < daOutput[0].length; j++) {

                daOutput[i][j] = dScalar;

            }

        }

        return daOutput;

    }

    public static int[][] initialize(int iHeight, int iWidth, int iScalar) {

        int[][] iaOutput = new int[iHeight][iWidth];

        for (int i = 0; i < iaOutput.length; i++) {
            for (int j = 0; j < iaOutput[0].length; j++) {

                iaOutput[i][j] = iScalar;

            }

        }

        return iaOutput;

    }

    public static Integer[][] initializeObj(int iHeight, int iWidth, int iScalar) {

        Integer[][] iaOutput = new Integer[iHeight][iWidth];

        for (int i = 0; i < iaOutput.length; i++) {
            for (int j = 0; j < iaOutput[0].length; j++) {

                iaOutput[i][j] = iScalar;

            }

        }

        return iaOutput;

    }

    public static int[] initialize(int iHeight, int iScalar) {

        int[] iaOutput = new int[iHeight];

        for (int i = 0; i < iaOutput.length; i++) {

            iaOutput[i] = iScalar;

        }

        return iaOutput;

    }

    public static Double[] initialize(int iHeight, Double dScalar) {

        Double[] daOutput = new Double[iHeight];

        for (int i = 0; i < daOutput.length; i++) {

            daOutput[i] = dScalar;

        }

        return daOutput;

    }

    public static Double[][] reshapeNoDoubleEntries(Double[][] daInput, int iColumn) {
        List<Integer> ilNoteven = new ArrayList<Integer>();
        int iRef = 0;
        ilNoteven.add(iRef);
        for (int i = 0; i < daInput.length; i++) {

            /*System.out.println(daInput[i][iColumn]);
             System.out.println(daInput[iRef][iColumn]);
             System.out.println(!((daInput[i][iColumn]).equals((daInput[iRef][iColumn]))));*/
            if (!((daInput[i][iColumn]).equals((daInput[iRef][iColumn])))) {
                iRef = i;
                ilNoteven.add(iRef);

            }
        }

        Double[][] daOutput = new Double[ilNoteven.size()][daInput[0].length];

        for (int j = 0; j < daOutput.length; j++) {

            daOutput[j] = daInput[ilNoteven.get(j)];

        }

        return daOutput;
    }

    public static Double[][] roundMatrix(Double[][] daInput, int iPrecision) {
        Double[][] daRoundMatrix = new Double[daInput.length][daInput[0].length];

        double dPrecision = 10;
        for (int k = 0; k < iPrecision; k++) {
            dPrecision = dPrecision * 10;
        }

        for (int i = 0; i < daInput.length; i++) {

            for (int j = 0; j < daInput[0].length; j++) {

                daRoundMatrix[i][j] = Math.round((daInput[i][j] * dPrecision)) / dPrecision;

            }

        }

        return daRoundMatrix;
    }

    public static int[][] binarizeAtFirstFound(double[][] daInput, int iCriticalValue, int iBinar1, int iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, all 
         following entries are set to Binar2.
         */
        int[][] iaReturn = new int[daInput.length][daInput[0].length];

        for (int j = 0; j < daInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < daInput.length; i++) {

                if (iSwitch == 0) {
                    if (daInput[i][j] >= iCriticalValue) {
                        iSwitch = 1;

                    }
                }

                if (iSwitch == 0) {
                    iaReturn[i][j] = iBinar1;
                } else {
                    iaReturn[i][j] = iBinar2;
                }

            }

        }

        return iaReturn;

    }

    public static double[][] binarizeAtFirstFound(Double[][] daInput, double dCriticalValue, double dBinar1, double dBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, all 
         following entries are set to Binar2.
         */
        double[][] daReturn = new double[daInput.length][daInput[0].length];

        for (int j = 0; j < daInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < daInput.length; i++) {

                if (iSwitch == 0) {
                    if (daInput[i][j] >= dCriticalValue) {
                        iSwitch = 1;

                    }
                }

                if (iSwitch == 0) {
                    daReturn[i][j] = dBinar1;
                } else {
                    daReturn[i][j] = dBinar2;
                }

            }

        }

        return daReturn;

    }

    public static double[][] GlidingHalfbinarizeAtFirstFound(Double[][] daInput, double dCriticalValue, double dBinar1) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, all 
         following entries are set to Binar2.
         */
        double[][] daReturn = new double[daInput.length][daInput[0].length];

        for (int j = 0; j < daInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < daInput.length; i++) {

                if (iSwitch == 0) {
                    if (daInput[i][j] >= dCriticalValue) {
                        iSwitch = 1;

                    }
                }

                if (iSwitch == 0) {
                    daReturn[i][j] = daInput[i][j];
                } else {
                    daReturn[i][j] = dBinar1;
                }

            }

        }

        return daReturn;

    }

    public static int[][] binarize(int[][] iaInput, int iCriticalValue, int iBinar1, int iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, all 
         following entries are set to Binar2.
         */
        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] >= iCriticalValue) {

                    iaReturn[i][j] = iBinar1;

                } else {

                    iaReturn[i][j] = iBinar2;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] binarizeWithAbs(int[][] iaInput, int iCriticalValue, int iBinar1, int iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, all 
         following entries are set to Binar2.
         */
        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (Math.abs(iaInput[i][j]) >= iCriticalValue) {

                    iaReturn[i][j] = iBinar1;

                } else {

                    iaReturn[i][j] = iBinar2;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] binarizeAtFirstFound(int[][] daInput, int iCriticalValue, int iBinar1, int iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, 
         all following entries are set to Binar2.
         */
        int[][] iaReturn = new int[daInput.length][daInput[0].length];

        for (int j = 0; j < daInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < daInput.length; i++) {

                if (iSwitch == 0) {

                    if (daInput[i][j] >= iCriticalValue) {
                        iSwitch = 1;
                    }

                }

                if (iSwitch == 0) {
                    iaReturn[i][j] = iBinar1;
                } else {
                    iaReturn[i][j] = iBinar2;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] binarizeSmallerAtFirstFound(int[][] iaInput, int iCriticalValue, int iBinar1, int iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, 
         all following entries are set to Binar2.
         */
        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int j = 0; j < iaInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < iaInput.length; i++) {

                if (iSwitch == 0) {

                    if (iaInput[i][j] <= iCriticalValue) {
                        iSwitch = 1;
                    }

                }

                if (iSwitch == 0) {
                    iaReturn[i][j] = iBinar1;
                } else {
                    iaReturn[i][j] = iBinar2;
                }

            }

        }

        return iaReturn;

    }

    public static double[][] binarizeSmallerAtFirstFound(Double[][] daInput, double dCriticalValue, double iBinar1, double iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, 
         all following entries are set to Binar2.
         */
        double[][] daReturn = new double[daInput.length][daInput[0].length];

        for (int j = 0; j < daInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < daInput.length; i++) {

                if (iSwitch == 0) {

                    if (daInput[i][j] <= dCriticalValue) {
                        iSwitch = 1;
                    }

                }

                if (iSwitch == 0) {
                    daReturn[i][j] = iBinar1;
                } else {
                    daReturn[i][j] = iBinar2;
                }

            }

        }

        return daReturn;

    }

    public static int[][] binarizeSmallerAtFirstFound(Double[][] daInput, double dCriticalValue, int iBinar1, int iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, 
         all following entries are set to Binar2.
         */
        int[][] iaReturn = new int[daInput.length][daInput[0].length];

        for (int j = 0; j < daInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < daInput.length; i++) {

                if (iSwitch == 0) {

                    if (daInput[i][j] <= dCriticalValue) {
                        iSwitch = 1;
                    }

                }

                if (iSwitch == 0) {
                    iaReturn[i][j] = iBinar1;
                } else {
                    iaReturn[i][j] = iBinar2;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] normalizeMatrix(int[][] iaInput) {

        int iMax = getMax(iaInput);

        int iMin = getMin(iaInput);

        if (iMax < Math.abs(iMin)) {
            iMax = Math.abs(iMin);
        }

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                iaReturn[i][j] = (int) (iaInput[i][j] / iMax);

            }

        }

        return (iaReturn);
    }
    
    public static double[][] normalizeMatrix(double[][] daInput) {

        double dMax = getMax(daInput);

        double dMin = getMin(daInput);

        if (dMax < Math.abs(dMin)) {
            dMax = Math.abs(dMin);
        }

        double[][] daReturn = new double[daInput.length][daInput[0].length];

        for (int i = 0; i < daInput.length; i++) {

            for (int j = 0; j < daInput[0].length; j++) {

                daReturn[i][j] = (daInput[i][j] / dMax);

            }

        }

        return (daReturn);
    }
    
    public static double[][] normalizeMatrix_toD(int[][] iaInput) {

        double dMax = getMax(iaInput);

        double dMin = getMin(iaInput);

        if (dMax < Math.abs(dMin)) {
            dMax = Math.abs(dMin);
        }

        double[][] daReturn = new double[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                daReturn[i][j] = (int) (iaInput[i][j] / dMax);

            }

        }

        return (daReturn);
    }

    public static int[][] absoluteMatrix(int[][] iaInput) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                iaReturn[i][j] = (Math.abs(iaInput[i][j]));

            }

        }

        return (iaReturn);
    }


    public static int[][] normalizeMatrix(int[][] iaInput, int iNorm) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                iaReturn[i][j] = (int) (iaInput[i][j] / iNorm);

            }

        }

        return (iaReturn);
    }

    public static int[][] normalizeMatrixToFactor(int[][] iaInput, int iFactor) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];
        int iMax = Matrix.getMax(iaInput);

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                iaReturn[i][j] = (int) (((double) iaInput[i][j]) / ((double) iMax) * iFactor);

            }

        }

        return (iaReturn);
    }

    public static Integer[][] normalizeMatrix(Integer[][] ioInput, Integer iNorm) {

        Integer[][] iaReturn = new Integer[ioInput.length][ioInput[0].length];

        for (int i = 0; i < ioInput.length; i++) {

            for (int j = 0; j < ioInput[0].length; j++) {

                iaReturn[i][j] = (int) (ioInput[i][j] / iNorm);

            }

        }

        return (iaReturn);
    }

    public static int[][] absoluteNormalizeMatrix(final int[][] iaInput, double dNorm) {

        int[][] iaTemp = getAbsolute(iaInput);

        int[][] iaReturn = Matrix.normalizeMatrix(iaTemp, (double) Matrix.getAbsoluteMax(iaTemp) / dNorm);

        return iaReturn;

    }

    public static int[][] normalizeMatrix(int[][] iaInput, double dNorm) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                iaReturn[i][j] = (int) (iaInput[i][j] / dNorm);

            }

        }

        return (iaReturn);
    }

    public static int getMax(int[][] iaInput) {
        int max = Integer.MIN_VALUE;

        for (int[] i : iaInput) {
            for (int j : i) {
                if (j > max) {
                    max = j;
                }
            }

        }

        return max;
    }
    
    public static double getMax(double[][] iaInput) {
        double max = Integer.MIN_VALUE;

        for (double[] i : iaInput) {
            for (double j : i) {
                if (j > max) {
                    max = j;
                }
            }

        }

        return max;
    }

    public static int getMax(int[][] iaInput, int iSizeOfArea) {
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < iaInput.length; i++) {
            for (int j = 0; j < iaInput[0].length; j++) {
                if (iaInput[i][j] > max) {
                    boolean bVicinity = true;
                    Outer:
                    for (int t = i - iSizeOfArea < 0 ? 0 : i - iSizeOfArea; t < (i + iSizeOfArea < iaInput.length ? i + iSizeOfArea : iaInput.length); t++) {
                        for (int z = j - iSizeOfArea < 0 ? 0 : j - iSizeOfArea; z < (j + iSizeOfArea < iaInput[0].length ? j + iSizeOfArea : iaInput[0].length); z++) {
                            if (iaInput[t][z] < max) {
                                bVicinity = false;
                                break Outer;
                            }
                        }
                    }

                    if (bVicinity) {
                        max = iaInput[i][j];
                    }
                }
            }

        }

        return max;
    }

    public static double getMax(double[] daInput) {
        double max = (-1) * Double.MAX_VALUE;

        for (double d : daInput) {
            if (d > max) {
                max = d;
            }
        }

        return max;
    }

    public static double getMax(List<double[]> daInput) {
        double max = (-1) * Double.MAX_VALUE;

        for (double[] da : daInput) {
            double dTempMax = getMax(da);
            if (dTempMax > max) {
                max = dTempMax;
            }
        }

        return max;
    }

    public static double getMin(double[] daInput) {
        double min = Double.MAX_VALUE;

        for (double d : daInput) {
            if (d < min) {
                min = d;
            }
        }

        return min;
    }

    public static double getMin(List<double[]> daInput) {
        double min = Double.MAX_VALUE;

        for (double[] da : daInput) {
            double dTempMin = getMin(da);
            if (dTempMin < min) {
                min = dTempMin;
            }
        }

        return min;
    }

    public static Integer getMax(Integer[][] ioInput) {
        Integer max = Integer.MIN_VALUE;

        for (Integer[] i : ioInput) {
            for (Integer j : i) {
                if (j > max) {
                    max = j;
                }
            }

        }

        return max;
    }

    public static Double getMax(Number[][] ioInput) {
        Double max = (-1) * Double.MAX_VALUE;

        for (Number[] i : ioInput) {
            for (Number j : i) {
                if ((Double) j > max) {
                    max = (Double) j;
                }
            }

        }

        return max;
    }

    public static Double getMax(Number[][] ioInput, int iColumn) {
        Double max = (-1) * Double.MAX_VALUE;

        for (Number[] i : ioInput) {
            if ((Double) i[iColumn] > max) {
                max = (Double) i[iColumn];
            }
        }

        return max;
    }

    public static Double getMin(Number[][] ioInput) {
        Double min = Double.MAX_VALUE;

        for (Number[] i : ioInput) {
            for (Number j : i) {
                if ((Double) j < min) {
                    min = (Double) j;
                }
            }
        }

        return min;
    }

    public static Double getMin(Number[][] ioInput, int iColumn) {
        Double min = Double.MAX_VALUE;

        for (Number[] i : ioInput) {
            if ((Double) i[iColumn] < min) {
                min = (Double) i[iColumn];
            }
        }
        return min;
    }

    public static int getAbsoluteMax(int[][] iaInput) {
        int max = Integer.MIN_VALUE;

        for (int[] i : iaInput) {
            for (int j : i) {
                if (Math.abs(j) > max) {
                    max = Math.abs(j);
                }
            }

        }

        return max;
    }

    public static int[][] getAbsolute(int[][] iaInput) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {
            for (int j = 0; j < iaInput[0].length; j++) {

                iaReturn[i][j] = Math.abs(iaInput[i][j]);

            }

        }

        return iaReturn;
    }

    public static int getMin(int[][] iaInput) {
        int min = Integer.MAX_VALUE;

        for (int[] i : iaInput) {
            for (int j : i) {
                if (j < min) {
                    min = j;
                }
            }

        }

        return min;
    }
    
    public static double getMin(double[][] daInput) {
        double min = Integer.MAX_VALUE;

        for (double[] i : daInput) {
            for (double j : i) {
                if (j < min) {
                    min = j;
                }
            }

        }

        return min;
    }

    public static int[][] binarizeReverseAtFirstFound(int[][] daInput, int iCriticalValue, int iBinar1, int iBinar2) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, 
         all following entries are set to Binar2.
         */
        int[][] iaReturn = new int[daInput.length][daInput[0].length];

        for (int j = 0; j < daInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = daInput.length - 1; i >= 0; i--) {

                if (iSwitch == 0) {

                    if (daInput[i][j] >= iCriticalValue) {
                        iSwitch = 1;
                    }

                }

                if (iSwitch == 0) {
                    iaReturn[i][j] = iBinar1;
                } else {
                    iaReturn[i][j] = iBinar2;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] addPythagoras(int[][] iaInput1, int[][] iaInput2) {

        int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {
            for (int j = 0; j < iaInput1[0].length; j++) {

                iaReturn[i][j] = (int) Math.sqrt(iaInput1[i][j] * iaInput1[i][j] + iaInput2[i][j] * iaInput2[i][j]);

            }

        }

        return iaReturn;
    }

    public static int[][] HalfbinarizeValue(int[][] iaInput, int iCriticalValue, int iBinar) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, all 
         following entries are set to Binar2.
         */
        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int j = 0; j < iaInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < iaInput.length; i++) {

                if (iSwitch == 0) {
                    if (iaInput[i][j] >= iCriticalValue) {
                        iSwitch = 1;
                    }
                }

                if (iSwitch == 0) {

                    iaReturn[i][j] = iaInput[i][j];

                } else {

                    iaReturn[i][j] = iBinar;

                }

            }

        }

        return iaReturn;

    }

    public static int[][] HalfbinarizeExactValue(int[][] iaInput, int iCriticalValue, int iBinar) {

        /*
         Columnwise Binarisation of a Matrix, Start of the top in every column,
         set all entries to iBinar1, if the iCriticalValue is found, all 
         following entries are set to Binar2.
         */
        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int j = 0; j < iaInput[0].length; j++) {

            int iSwitch = 0;

            for (int i = 0; i < iaInput.length; i++) {

                if (iSwitch == 0) {
                    if (iaInput[i][j] == iCriticalValue) {
                        iSwitch = 1;
                    }
                }

                if (iSwitch == 0) {

                    iaReturn[i][j] = iaInput[i][j];

                } else {

                    iaReturn[i][j] = iBinar;

                }

            }

        }

        return iaReturn;

    }

    public static byte[] castToByteprimitive(int[] iaInput) {

        byte[] byReturn = new byte[iaInput.length];

        for (int i = 0; i < byReturn.length; i++) {

            byReturn[i] = (byte) iaInput[i];

        }

        return byReturn;
    }

    public static byte[][] castToByteprimitive(int[][] iaInput) {

        byte[][] byReturn = new byte[iaInput.length][iaInput[0].length];

        for (int i = 0; i < byReturn.length; i++) {
            for (int j = 0; j < byReturn[0].length; j++) {
                byReturn[i][j] = (byte) iaInput[i][j];
            }

        }

        return byReturn;
    }

    public static int[][] castToIntprimitive(byte[][] baInput) {

        int[][] iaReturn = new int[baInput.length][baInput[0].length];

        for (int i = 0; i < iaReturn.length; i++) {
            for (int j = 0; j < iaReturn[0].length; j++) {
                iaReturn[i][j] = (int) baInput[i][j];
            }

        }

        return iaReturn;
    }

    public static int[][] castToIntGrayScale(byte[][] baInput) {

        int[][] iaReturn = new int[baInput.length][baInput[0].length];

        for (int i = 0; i < iaReturn.length; i++) {
            for (int j = 0; j < iaReturn[0].length; j++) {
                int iTemp = (int) baInput[i][j];
                if (iTemp >= 0) {
                    iaReturn[i][j] = iTemp;
                } else {
                    iaReturn[i][j] = 256 + iTemp;
                }
            }

        }

        return iaReturn;
    }

    public static byte[] castToByteFromUnsignedGray(int[] iaUnsignedGray) {

        byte[] byReturn = new byte[iaUnsignedGray.length];

        for (int i = 0; i < byReturn.length; i++) {
            System.out.println(iaUnsignedGray[i]);
            System.out.println(iaUnsignedGray[i] - 128);
            byReturn[i] = (byte) (iaUnsignedGray[i] - 128);
            System.out.println(byReturn[i]);
            System.out.println("--------------");
        }

        return byReturn;
    }

    public static byte[] castToByteCut(int[] iaInput) {

        byte[] byReturn = new byte[iaInput.length];

        for (int i = 0; i < byReturn.length; i++) {

            if (iaInput[i] > 127) {
                byReturn[i] = 127;
            } else if (iaInput[i] < -128) {
                byReturn[i] = -128;
            } else {
                byReturn[i] = (byte) iaInput[i];
            }

        }

        return byReturn;
    }

    public static int[][] shiftMatrix(int[][] iaInput, int ishift) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                iaReturn[i][j] = iaInput[i][j] + ishift;

            }

        }

        return iaReturn;
    }

    public static int[][] complianceReturnBinar(int[][] iaInput1, int[][] iaInput2, int iValue) {

        int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput1[0].length; j++) {

                if (iaInput1[i][j] == iValue && iaInput2[i][j] == iValue) {
                    iaReturn[i][j] = 1;
                } else {
                    iaReturn[i][j] = 0;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] antonomyComplianceReturnBinar(int[][] iaInput1, int[][] iaInput2, int iValue) {

        int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput1[0].length; j++) {

                if (iaInput1[i][j] == iValue && iaInput2[i][j] != iValue) {
                    iaReturn[i][j] = 1;
                } else {
                    iaReturn[i][j] = 0;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] asymmetricComplianceReturnBinar(int[][] iaInput1, int[][] iaInput2, int iValue1, int iValue2) {

        int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput1[0].length; j++) {

                if (iaInput1[i][j] == iValue1 && iaInput2[i][j] == iValue2) {
                    iaReturn[i][j] = 1;
                } else {
                    iaReturn[i][j] = 0;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] asymmetricAntonomyComplianceReturnBinar(int[][] iaInput1, int[][] iaInput2, int iValue1, int iValue2) {

        int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput1[0].length; j++) {

                if (iaInput1[i][j] == iValue1 && iaInput2[i][j] != iValue2) {
                    iaReturn[i][j] = 1;
                } else {
                    iaReturn[i][j] = 0;
                }

            }

        }

        return iaReturn;

    }

    public static int[][] cloneInt(int[][] iaInput) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaReturn.length; i++) {

            System.arraycopy(iaInput[i], 0, iaReturn[i], 0, iaReturn[0].length);

        }

        return iaReturn;

    }

    /*public static int[][] getDirectionNormalized(int[][] iaInputGradientX, int[][] iaInputGradientY){
     /*
     * Clockwise output, North : 0 ; North-East: 3 ; East : 6 ; South-East : 9
     *//*
     int[][] iaReturn = new int[iaInputGradientX.length][iaInputGradientX[0].length];
        
     for (int i = 0; i<iaInputGradientX.length; i++){
            
     for (int j = 0; j<iaInputGradientX[0].length; j++){
                
     if (iaInputGradientY[i][j] == 0){
     iaReturn[i][j] = 0;
     } else if (iaInputGradientX[i][j] == 0){
     iaReturn[i][j] = 9;
     } else {
                    
     double dArc = Math.atan( ((double) iaInputGradientY[i][j] ) / ((double) iaInputGradientX[i][j] ));
                    
     dArc = Math.round(Math.abs(dArc) / (Math.PI/4.0));
                    
     iaReturn[i][j] = (int)(dArc * 3);
                    
     }
                
     }
            
     }
        
     return iaReturn;
        
     }*/

    public static int[][] treshold(int iaInput[][], int iTreshold, int iValue) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 1; i < iaInput.length; i++) {

            for (int j = 1; j < iaInput[0].length; j++) {

                if (iaInput[i][j] < iTreshold) {
                    iaReturn[i][j] = iValue;
                } else {
                    iaReturn[i][j] = iaInput[i][j];
                }
            }

        }

        return iaReturn;

    }

    public static int[][] tresholdBinary(int iaInput[][], int iTreshold, int iValueDown, int iValueUp) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 1; i < iaInput.length; i++) {

            for (int j = 1; j < iaInput[0].length; j++) {

                if (iaInput[i][j] < iTreshold) {
                    iaReturn[i][j] = iValueDown;
                } else {
                    iaReturn[i][j] = iValueUp;
                }
            }

        }

        return iaReturn;

    }

    public static int[][] tresholdAbove(int iaInput[][], int iTreshold, int iValue) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] > iTreshold) {
                    iaReturn[i][j] = iValue;
                } else {
                    iaReturn[i][j] = iaInput[i][j];
                }
            }

        }

        return iaReturn;

    }

    public static int[][] adaptiveTresholdAbove(int iaInput[][], int iSize, int iValue, double dOffSet) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {
            for (int j = 0; j < iaInput[0].length; j++) {
                int iSum = 0;
                int iCount = 0;
                for (int t = (i - iSize) > 0 ? (i - iSize) : 0; t < ((i + iSize) < iaInput.length ? (i + iSize) : iaInput.length); t++) {
                    for (int k = ((j - iSize) > 0 ? (j - iSize) : 0); k < ((j + iSize) < iaInput[0].length ? (j + iSize) : iaInput[0].length); k++) {
                        iSum = iSum + iaInput[t][k];
                        iCount++;
                    }
                }
                int iThres = iSum / iCount;
                if (iaInput[i][j] < (iThres - dOffSet)) {
                    iaReturn[i][j] = iaInput[i][j];
                } else {
                    iaReturn[i][j] = iValue;
                }
            }
        }

        return iaReturn;

    }

    public static int[][] getLocalMax(int iaInput[][], int iSize, int iThres) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {
            for (int j = 0; j < iaInput[0].length; j++) {
                if (iaInput[i][j] > iThres) {
                    boolean bMax = true;
                    outer:
                    for (int t = (i - iSize) > 0 ? (i - iSize) : 0; t < ((i + iSize) < iaInput.length ? (i + iSize) : iaInput.length); t++) {
                        for (int k = ((j - iSize) > 0 ? (j - iSize) : 0); k < ((j + iSize) < iaInput[0].length ? (j + iSize) : iaInput[0].length); k++) {
                            if (iaInput[t][k] > iaInput[i][j]) {
                                bMax = false;
                                break outer;
                            }
                        }
                    }
                    if (bMax) {
                        iaReturn[i][j] = 255;
                    }
                }
            }
        }

        return iaReturn;

    }

    public static int[][] tresholdAbsolute(int iaInput[][], int iTreshold, int iValue) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        int[][] iaAbsolute = getAbsolute(iaInput);

        for (int i = 1; i < iaInput.length; i++) {

            for (int j = 1; j < iaInput[0].length; j++) {

                if (iaAbsolute[i][j] < iTreshold) {
                    iaReturn[i][j] = iValue;
                } else {
                    iaReturn[i][j] = iaInput[i][j];
                }
            }

        }

        return iaReturn;

    }

    public static int[][] CrossTreshold(int iaInput[][], int[][] iaInputMask, int iTreshold, int iValue) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 1; i < iaInput.length; i++) {

            for (int j = 1; j < iaInput[0].length; j++) {

                if (iaInputMask[i][j] < iTreshold) {
                    iaReturn[i][j] = iValue;
                } else {
                    iaReturn[i][j] = iaInput[i][j];
                }
            }

        }

        return iaReturn;

    }

    public static Double[][] mergeArray(List<Double[][]> lsaInput) {

        List<Double[]> lsaTemp = new ArrayList<Double[]>();

        for (int i = 0; i < lsaInput.size(); i++) {

            Double[][] daTemp = lsaInput.get(i);
            lsaTemp.addAll(Arrays.asList(daTemp));

        }

        return lsaTemp.toArray(new Double[1][1]);

    }

    public static Double[][] mergeArrayLeft(Double[] daLeft, Double[][] daRight) {

        Double[][] daReturn = new Double[daLeft.length][daRight[0].length + 1];

        for (int i = 0; i < daLeft.length; i++) {

            daReturn[i][0] = daLeft[i];

            for (int j = 0; j < daRight[0].length; j++) {
                daReturn[i][j + 1] = daRight[i][j];
            }

        }

        return daReturn;

    }

    public static Double[][] mergeArrayToBottom(List<Double[][]> lsaInput) {

        Double[][] daTemp = lsaInput.get(0);

        Integer iLengthTot = lsaInput.size() * daTemp.length;

        Double[][] daOutput = new Double[iLengthTot][daTemp[0].length];

        int iCount = 0;

        for (Double[][] da : lsaInput) {

            for (int i = 0; i < daTemp.length; i++) {

                for (int j = 0; j < daTemp[0].length; j++) {

                    daOutput[iCount][j] = da[i][j];

                }

                iCount = iCount + 1;

            }
        }

        return daOutput;

    }

    public static Double[][] mergeVec(List<Double[]> lsaInput) {

        Double[][] daOutput = initialize(getMaxVecLength(lsaInput), lsaInput.size(), 0.0);
        int iCol = 0;

        for (Double[] da : lsaInput) {

            int ilength = lsaInput.get(iCol).length;

            for (int i = 0; i < ilength; i++) {

                daOutput[i][iCol] = da[i];

            }

            iCol = iCol + 1;

        }

        return daOutput;

    }

    public static int getMaxVecLength(List<Double[]> liaInput) {

        int iLength = 1;

        for (Double[] na : liaInput) {

            if (na.length > iLength) {
                iLength = na.length;
            }

        }

        return iLength;

    }

    public static boolean inBounds(int[][] iaInput, int j, int i) {

        boolean bInBounds = false;

        if (j >= 0 && i >= 0) {

            if (i < iaInput.length) {

                if (j < iaInput[0].length) {

                    bInBounds = true;

                }

            }

        }

        return bInBounds;

    }

    public static boolean inBounds(Number[][] noInput, int iX, int iY) {

        boolean bInBounds = false;

        if (iX >= 0 && iY >= 0) {

            if (iY < noInput.length) {

                if (iX < noInput[0].length) {

                    bInBounds = true;

                }

            }

        }

        return bInBounds;

    }

    public static boolean inBounds(int[][] iaInput, OrderedPair opPoint) {

        int iX = (int) opPoint.x;
        int iY = (int) opPoint.y;

        boolean bInBounds = false;

        if (iX >= 0 && iY >= 0) {

            if ((int) iY < iaInput.length) {

                if ((int) iX < iaInput[0].length) {

                    bInBounds = true;

                }

            }

        }

        return bInBounds;

    }

    public static boolean inBounds(int[][] iaInput, List<MatrixEntry> lmePoint) {

        for (MatrixEntry me : lmePoint) {
            if (!inBounds(iaInput, me)) {
                return false;
            }
        }
        return true;

    }

    public static boolean inBounds(int[][] iaInput, MatrixEntry mpPoint) {

        int i = (int) mpPoint.i;
        int j = (int) mpPoint.j;

        boolean bInBounds = false;

        if (i >= 0 && j >= 0) {

            if ((int) i < iaInput.length) {

                if ((int) j < iaInput[0].length) {

                    bInBounds = true;

                }

            }

        }

        return bInBounds;

    }

    /*public static boolean inBounds(int[][] iaInput, int i, int j){
        
        
        
     boolean bInBounds = false;
        
     if (i >= 0 && i >= 0){
            
     if ((int) i < iaInput.length){
                
     if ((int) j < iaInput[0].length){
                    
     bInBounds = true;
                    
     }
                
     }
            
     }
        
     return bInBounds;
        
     }*/
    public static int[][] subset(int[][] iaInput, OrderedPair opLeftUpperCorner, OrderedPair opRightDownCorner) {

        int[][] iaSubset = new int[(int) (opRightDownCorner.y - opLeftUpperCorner.y)][(int) (opRightDownCorner.x - opLeftUpperCorner.x)];

        for (int i = 0; i < iaSubset.length; i++) {

            for (int j = 0; j < iaSubset[0].length; j++) {

                iaSubset[i][j] = iaInput[(int) opLeftUpperCorner.y + i][(int) opLeftUpperCorner.x + j];

            }

        }

        return iaSubset;
    }

    public static void cutAbove(int[][] iaInput, int iSupremum, int iValue) {

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] >= iSupremum) {

                    iaInput[i][j] = iValue;

                }

            }

        }

    }

    public static int[][] cutAboveReturn(int[][] iaInput, int iSupremum, int iValue) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] >= iSupremum) {

                    iaReturn[i][j] = iValue;

                } else {
                    iaReturn[i][j] = iaInput[i][j];
                }

            }

        }

        return iaReturn;

    }

    public static void cutBelowAndAbove(int[][] iaInput, int iMin, int iMax, int iMinValue, int iMaxValue) {

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] >= iMax) {

                    iaInput[i][j] = iMaxValue;

                }
                if (iaInput[i][j] <= iMin) {

                    iaInput[i][j] = iMinValue;

                }

            }

        }

    }

    public static Double[][] getColumns(Double[][] daInput, int[] iaColumns) {

        Double[][] daExtractedArray = new Double[daInput.length][iaColumns.length];

        for (int i = 0; i < daInput.length; i++) {

            for (int j = 0; j < iaColumns.length; j++) {

                //System.out.println(i+ ", " + j+ ", " + iaColumns[j]);
                //System.out.println(daExtractedArray[i][j]);
                daExtractedArray[i][j] = daInput[i][iaColumns[j]];

            }

        }

        return (daExtractedArray);
    }

    public static Double[] getColumns(Double[][] daInput, int iColumn) {

        Double[] daExtractedArray = new Double[daInput.length];

        for (int i = 0; i < daInput.length; i++) {

            //System.out.println(i+ ", " + j+ ", " + iaColumns[j]);
            //System.out.println(daExtractedArray[i][j]);
            daExtractedArray[i] = daInput[i][iColumn];

        }

        return (daExtractedArray);
    }

    public static Double[][] cutRow(Double[][] daInput, int iRowNumber, int iSwitch) {
        // Cut column at iRowNumber, if iSwitch == 0 or null return ([i > iRowNumber]) 
        // else  return(daInput[i < iRowNumber]), iRowNumber counted from zero

        Double[][] daCutInput;

        if (iSwitch == 0) {
            daCutInput = new Double[daInput.length - (iRowNumber)][daInput[0].length];

            for (int i = iRowNumber; i < daInput.length; i++) {
                System.arraycopy(daInput[i], 0, daCutInput[i - iRowNumber], 0, daInput[0].length);

            }

        } else {
            daCutInput = new Double[(iRowNumber + 1)][daInput[0].length];

            for (int i = 0; i < daInput.length - (iRowNumber + 1); i++) {
                System.arraycopy(daInput[i], 0, daCutInput[i], 0, daInput[0].length);

            }

        }

        return (daCutInput);

    }

    public static int[][] getRowSetorSmallArrays(int[][] daInput, int iRowStart, int iRowEnd) {
        // get Sector of f[(a_ij)] = b_kj  iRowStart<=k<=iRowEnd
        int[][] iaSector = new int[iRowEnd - iRowStart][daInput[0].length];
        for (int i = iRowStart; i < iRowEnd; i++) {
            if (iRowStart >= 0 && iRowEnd <= daInput.length) {
                iaSector[i - iRowStart] = daInput[i];
            }
        }
        return iaSector;
    }

    public static Number[][] getRowSetorSmallArrays(Number[][] naInput, int iRowStart, int iRowEnd) {
        // get Sector of f[(a_ij)] = b_kj  iRowStart<=k<=iRowEnd
        Number[][] iaSector = new Number[iRowEnd - iRowStart][naInput[0].length];

        for (int i = iRowStart; i < iRowEnd; i++) {
            if (iRowStart >= 0 && iRowEnd <= naInput.length) {
                iaSector[i - iRowStart] = naInput[i];
            }
        }
        return iaSector;
    }

    public static Double[][] getRowSetorSmallArrays(Double[][] daInput, int iRowStart, int iRowEnd) {
        // get Sector of f[(a_ij)] = b_kj  iRowStart<=k< iRowEnd
        // exkl. uppwer bound !
        Double[][] daSector = new Double[iRowEnd - iRowStart][daInput[0].length];

        for (int i = iRowStart; i < iRowEnd; i++) {
            if (iRowStart >= 0 && iRowEnd <= daInput.length) {
                daSector[i - iRowStart] = daInput[i];
            }

        }

        return daSector;

    }

    public static int[][] getSetorColSmallArrays(int[][] daInput, int iColStart, int iColEnd) {
        // get Sector of f[(a_ij)] = b_kj  iRowStart<=k<=iRowEnd
        int[][] iaSector = new int[daInput.length][iColEnd - iColStart];
        for (int i = 0; i < daInput.length; i++) {
            for (int j = iColStart; j < iColEnd; j++) {
                if (iColStart >= 0 && iColEnd <= daInput[0].length) {
                    iaSector[i][j - iColStart] = daInput[i][j];
                }
            }
        }
        return iaSector;
    }

    public static Number[][] getSub(Number[][] oaInput, MatrixEntry meStart, MatrixEntry meEnd) {

        Number[][] sub = new Number[(int) (meEnd.i - meStart.i)][(int) (meEnd.j - meStart.j)];

        for (int i = (int) meStart.i; i < meEnd.i; i++) {

            for (int j = (int)  meStart.j; j < meEnd.j; j++) {

                sub[(int)  (i - meStart.i) ][(int) (j - meStart.j)] = oaInput[i][j];

            }

        }

        return sub;

    }

    public static int[][] getSub(int[][] iaInput, MatrixEntry meStart, MatrixEntry meEnd) {

        int[][] sub = new int[(int)  (meEnd.i - meStart.i)][(int) (meEnd.j - meStart.j)];

        for (int i = (int) meStart.i; i < meEnd.i; i++) {

            for (int j = (int)  meStart.j; j < meEnd.j; j++) {

                sub[(int) (i - meStart.i)][(int) (j - meStart.j)] = iaInput[i][j];

            }

        }

        return sub;

    }

    public static Number[][] getSub2(Number[][] iaInput, MatrixEntry meStart, MatrixEntry meEnd) {

        Number[][] sub = new Number[(int) (meEnd.i - meStart.i + 1)][(int) (meEnd.j - meStart.j + 1)];

        for (int i = (int) meStart.i; i <= meEnd.i; i++) {

            for (int j = (int)  meStart.j; j <= meEnd.j; j++) {

                sub[(int)  (i - meStart.i)][(int) (j - meStart.j)] = iaInput[i][j];

            }

        }

        return sub;

    }

    public static Double[][] unitArray(Double[] Input1, Double[] Input2) {

        if (Input1 != null && Input2 != null && Input1.length == Input2.length) {
            Double[][] daUnitArray = new Double[Input1.length][2];

            for (int i = 0; i < Input1.length; i++) {

                daUnitArray[i][0] = Input1[i];
                daUnitArray[i][1] = Input2[i];

            }

            return (daUnitArray);

        } else {
            System.out.println("This is unitArray in Matrix: Warning object: null return");
            return (null);

        }

    }

    public static Double[][] unitArray(Double[] Input1, Double[][] Input2) {

        if (Input1 != null && Input2 != null && Input1.length == Input2[0].length) {
            Double[][] daUnitArray = new Double[Input1.length][Input2.length + 1];

            for (int i = 0; i < Input1.length; i++) {

                daUnitArray[i][0] = Input1[i];

                for (int j = 0; j < Input2.length; j++) {

                    daUnitArray[i][j + 1] = Input2[j][i];
                }

            }

            return (daUnitArray);

        } else {
            System.out.println("Warning object: null return");
            return (null);

        }

    }

    public static int[][] multiplication(int[][] iaInput1, int[][] iaInput2) {

        int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput1[0].length; j++) {

                iaReturn[i][j] = iaInput1[i][j] * iaInput2[i][j];
            }

        }

        return iaReturn;
    }
    
    public static double[][] multiplication(double[][] iaInput1, double[][] iaInput2) {

        double[][] iaReturn = new double[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput1[0].length; j++) {

                iaReturn[i][j] = iaInput1[i][j] * iaInput2[i][j];
            }

        }

        return iaReturn;
    }

    public static int[][] reshape(int[][] iaInput, int i1, int j1, int i2, int j2) {
        int[][] iaReturn = new int[i2 - i1][j2 - j1];

        for (int i = i1; i < i2; i++) {
            for (int j = j1; j < j2; j++) {
                iaReturn[i - i1][j - j1] = iaInput[i][j];
            }

        }

        return iaReturn;
    }

    public static Double[][] scalarMultiplikation(Number[][] iaInput, Double dS) {

        Double[][] daOutput = new Double[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                daOutput[i][j] = dS * (Double) iaInput[i][j];

            }

        }

        return daOutput;

    }

    public static void scalarMultiplikation(Number[][] iaInput, int iColumn, Double dS) {

        for (int i = 0; i < iaInput.length; i++) {

            iaInput[i][iColumn] = dS * (Double) iaInput[i][iColumn];

        }

    }

    public static Double[][] Merge(List<Double[][]> liaInput) {

        List<Double[]> liaTemp = new ArrayList<Double[]>();

        for (Double[][] ia : liaInput) {

            for (int j = 0; j < ia.length; j++) {

                liaTemp.add(ia[j]);

            }

        }

        Double[][] iaRet = liaTemp.toArray(new Double[1][1]);

        return iaRet;

    }

    public static void setSub(Number[][] oaInput, Number[][] oaSub, MatrixEntry meStart, MatrixEntry meEnd) {

        for (int i = 0; i < meEnd.i; i++) {

            for (int j = 0; j < meEnd.j; j++) {

                oaInput[(int) (meStart.i + i)][(int) (meStart.j + j)] = oaSub[i][j];

            }

        }

    }

    public static void setMatrix(int[][] iaToSet, int[][] iaInput, int iThresHold, int iToSet) {

        for (int i = 0; i < iaToSet.length; i++) {
            for (int j = 0; j < iaToSet[0].length; j++) {
                if (iaToSet[i][j] >= iThresHold) {
                    if (Matrix.inBounds(iaInput, j, i)) {
                        iaInput[i][j] = iToSet;
                    }
                }
            }
        }

    }

    public static void setSub(Integer[][] oaInput, Integer value, MatrixEntry meStart, MatrixEntry meEnd) {

        if (meEnd.i - meStart.i > 0) {
            outer:
            for (int i = 0; i < (meEnd.i - meStart.i); i++) {

                if (i + meStart.i == oaInput.length) {
                    break outer;
                }

                inner:
                for (int j = 0; j < (meEnd.j - meStart.j); j++) {

                    if (j + meStart.j == oaInput[0].length) {
                        break inner;
                    }

                    oaInput[meStart.i + i][meStart.j + j] = value;

                }

            }
        } else {
            inner:
            for (int j = 0; j < (meEnd.j - meStart.j); j++) {
                if (j + meStart.j == oaInput[0].length) {
                    break inner;
                }
                oaInput[meStart.i][meStart.j + j] = value;
            }
        }
    }

    public static void setSub2(Integer[][] oaInput, Integer value, MatrixEntry meStart, MatrixEntry meEnd) {
        outer:
        for (int i = 0; i <= (meEnd.i - meStart.i); i++) {

            if (i + meStart.i == oaInput.length) {
                break outer;
            }

            inner:
            for (int j = 0; j <= (meEnd.j - meStart.j); j++) {

                if (j + meStart.j == oaInput[0].length) {
                    break inner;
                }

                if (Matrix.inBounds(oaInput, meStart.j + j, meStart.i + i)) {
                    oaInput[meStart.i + i][meStart.j + j] = value;
                }

            }
        }
    }

    public static void setSub2WithSource(Integer[][] ioClipBoard, Integer[][] ioSource, MatrixEntry meStartClipBoard, MatrixEntry meEndClipbaord,
            MatrixEntry meStartSource) {
        outer:
        for (int i = 0; i <= (meEndClipbaord.i - meStartClipBoard.i); i++) {

            if (i + meStartClipBoard.i == ioClipBoard.length) {
                break outer;
            }

            inner:
            for (int j = 0; j <= (meEndClipbaord.j - meStartClipBoard.j); j++) {

                if (j + meStartClipBoard.j == ioClipBoard[0].length) {
                    break inner;
                }

                ioClipBoard[meStartClipBoard.i + i][meStartClipBoard.j + j] = ioSource[meStartSource.i + i][meStartSource.j + j];
            }
        }
    }

    public static int[][] setSub2WithSourceNative(int[][] ioClipBoard, int[][] ioSource, MatrixEntry meStartClipBoard) {
        outer:
        for (int i = 0; i < ioSource.length && i <= (ioClipBoard.length - meStartClipBoard.i); i++) {

            if (i + meStartClipBoard.i == ioClipBoard.length) {
                break outer;
            }

            inner:
            for (int j = 0; j < ioSource[0].length && j <= (ioClipBoard[0].length - meStartClipBoard.j); j++) {

                if (j + meStartClipBoard.j == ioClipBoard[0].length) {
                    break inner;
                }

                ioClipBoard[meStartClipBoard.i + i][meStartClipBoard.j + j] = ioSource[i][j];
            }
        }
        return ioClipBoard;
    }

//    public static void setSub2WithSource(List<int[][]> liClipBoard, int[][] ioSource, List<MatrixEntry> lmeStartClipBoard, List<MatrixEntry> lmeEndClipbaord,
//            List<MatrixEntry> lmeStartSource) {
//        for(int i=0; i<liClipBoard.size(); i++){
//            setSub2WithSourceNative(liClipBoard.get(i), ioSource, lmeStartClipBoard.get(i), lmeEndClipbaord.get(i), lmeStartSource.get(i));
//        }                
//    }
    public static void setSub2WithSource(List<int[][]> liSource, int[][] liClipBoard, List<MatrixEntry> lmeStartSource) throws IOException {
        for (int i = 0; i < liSource.size(); i++) {
            int[][] iSource = liSource.get(i);
//            Writer.PaintGreyPNG(iSource, new File("C:\\TesCase\\Bub_ByHand_MultiPic\\_0" + System.getProperty("file.separator") + "iClip" + i + ".png"));
            liClipBoard = setSub2WithSourceNative(liClipBoard, iSource, lmeStartSource.get(i));
//            Writer.PaintGreyPNG(liClipBoard, new File("C:\\TesCase\\Bub_ByHand_MultiPic\\_0" + System.getProperty("file.separator") + "Mult" + i + ".png"));
        }
    }

    public static void setSubNeqZeroPrim(int[][] ioInput, Integer[][] ioSub, MatrixEntry mePointToSet) {
        /*
         * The matrix ioSub is set to ioInput. The center of ioSub is set to mePointToSet.
         * Only values not equal 0 are set.
         */

        int iStartPointerI = mePointToSet.i - (int) Math.ceil(ioSub.length / 2);
        int iStartPointerJ = mePointToSet.j - (int) Math.ceil(ioSub[0].length / 2);

        for (int i = 0; i < ioSub.length; i++) {
            for (int j = 0; j < ioSub[0].length; j++) {

                if (ioSub[i][j] != 0 && Matrix.inBounds(ioInput, iStartPointerJ + j, iStartPointerI + i)) {
                    ioInput[iStartPointerI + i][iStartPointerJ + j] = ioSub[i][j];
                }

            }
        }

    }

    public static void setSubNeqZero(Integer[][] ioInput, Integer[][] ioSub, MatrixEntry mePointToSet) {
        /*
         * The matrix ioSub is set to ioInput. The center of ioSub is set to mePointToSet.
         * Only values not equal 0 are set.
         */

        int iStartPointerI = mePointToSet.i - (int) Math.ceil(ioSub.length / 2);
        int iStartPointerJ = mePointToSet.j - (int) Math.ceil(ioSub[0].length / 2);

        for (int i = 0; i < ioSub.length; i++) {
            for (int j = 0; j < ioSub[0].length; j++) {

                if (ioSub[i][j] != 0 && Matrix.inBounds(ioInput, iStartPointerJ + j, iStartPointerI + i)) {
                    ioInput[iStartPointerI + i][iStartPointerJ + j] = ioSub[i][j];
                }

            }
        }

    }

    public static void setSubNeqZero(int[][] iaInput, Integer[][] ioSub, MatrixEntry mePointToSet) {
        /*
         * The matrix ioSub is set to ioInput. The center of ioSub is set to mePointToSet.
         * Only values not equal 0 are set.
         */

        int iStartPointerI = mePointToSet.i - (int) Math.ceil(ioSub.length / 2);
        int iStartPointerJ = mePointToSet.j - (int) Math.ceil(ioSub[0].length / 2);

        for (int i = 0; i < ioSub.length; i++) {
            for (int j = 0; j < ioSub[0].length; j++) {

                if (ioSub[i][j] != 0 && Matrix.inBounds(iaInput, iStartPointerJ + j, iStartPointerI + i)) {
                    iaInput[iStartPointerI + i][iStartPointerJ + j] = ioSub[i][j];
                }

            }
        }

    }

    public static void setSubThreshold(int[][] iaInput, int[][] iaSub, MatrixEntry meStartPoint, int iThres) {
        setSubThreshold(iaInput, iaSub, meStartPoint, iThres, 0);
    }

    public static void setSubThreshold(int[][] iaInput, int[][] iaSub, MatrixEntry meStartPoint, int iThres, int iRim) {
        /*
         * The matrix iaSub is set to iaInput. Start from meStartPoint till end of iaSub or iaInput
         * Only values greater Threshold are set
         */

        for (int i = meStartPoint.i + iRim; (i < iaInput.length) && ((i - meStartPoint.i) < (iaSub.length - iRim)); i++) {
            for (int j = meStartPoint.j + iRim; j < iaInput[0].length && (j - meStartPoint.j) < (iaSub[0].length - iRim); j++) {

                if (iaSub[i - meStartPoint.i][j - meStartPoint.j] > iThres) {
                    iaInput[i][j] = iaSub[i - meStartPoint.i][j - meStartPoint.j];
                }

            }
        }

    }

    public static int[][] swell(int[][] iaBinaryCanny) {
        int[][] iCopy = cloneInt(iaBinaryCanny);
        for (int i = 0; i < iaBinaryCanny.length; i++) {
            for (int j = 0; j < iaBinaryCanny[0].length; j++) {
                if (iaBinaryCanny[i][j] == 255) {
                    if (inBounds(iCopy, j, i + 1)) {
                        iCopy[i + 1][j] = 255;
                    }
                    if (inBounds(iCopy, j, i - 1)) {
                        iCopy[i - 1][j] = 255;
                    }
                    if (inBounds(iCopy, j + 1, i)) {
                        iCopy[i][j + 1] = 255;
                    }
                    if (inBounds(iCopy, j - 1, i)) {
                        iCopy[i][j - 1] = 255;
                    }
                }
            }
        }
        return iCopy;
    }

    public static void histogramspreading(int[][] iIm, int[][] iSwell, int iMin,int imax) {
//        int imax = Matrix.getMax(iIm);
        for (int i = 0; i < iIm.length; i++) {
            for (int j = 0; j < iIm[0].length; j++) {
                if (iIm[i][j] < iMin) {
                    iIm[i][j] = iMin;
                }
                if (iSwell[i][j] == 255) {
                    iIm[i][j] = 255 * (iIm[i][j] - iMin) / (imax - iMin);
                }
            }
        }
    }

    /*public static int[][] binarizeAtFirstFound(int[][] iaInput, int iCriticalValue, int iBinar, String sKey) {
    
     /*
     Columnwise Binarisation of a Matrix, Start of the top in every column,
     set all entries to iBinar1, if the iCriticalValue is found, all 
     following entries are set to Binar2.
    
    
     int[][] iaReturn = new int[iaInput.length][iaInput[0].length];
    
     for (int j = 0; j < iaInput[0].length; j++) {
    
     int iSwitch = 0;
    
     for (int i = 0; i < iaInput.length; i++) {
    
     if (iSwitch == 0) {
     if (iaInput[i][j] >= iCriticalValue) {
     iSwitch = 1;
    
     }
     }
    
     if (iSwitch == 0){
    
     if(sKey.contains("1")){
     iaReturn[i][j] = iaInput[i][j];
     } else {
     iaReturn[i][j] = iBinar;
     }
    
     } else {
     if(sKey.contains("2")){
     iaReturn[i][j] = iaInput[i][j];
     } else {
     iaReturn[i][j] = iBinar;
     }
     }
    
     }
    
     }
    
    
     return iaReturn;
    
    
     }*/
 /* public static Double[][] reshapeNoDoubleEntries(Double[][] daInput, int iColumn1, int iColumn2){
     List<Integer> ilNoteven = new ArrayList<Integer>();
     int iRef =0;
     ilNoteven.add(iRef);
     for (int i=0; i<daInput.length;i++){
    
     /*System.out.println(daInput[i][iColumn]);
     System.out.println(daInput[iRef][iColumn]);
     System.out.println(!((daInput[i][iColumn]).equals((daInput[iRef][iColumn]))));*//*
    
    
     if(!((daInput[i][iColumn1]).equals((daInput[iRef][iColumn1])))){
     iRef=i;
     ilNoteven.add(iRef);
    
    
     }     
     }
    
     Double[][] daOutput = new Double[ilNoteven.size()][daInput[0].length];
    
     for (int j=0; j<daOutput.length;j++){
    
     daOutput[j] = daInput[ilNoteven.get(j)];
    
     }
    
     return daOutput;
     } */

}
