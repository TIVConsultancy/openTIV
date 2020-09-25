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

/**
 *
 * @author Thomas Ziegenhein
 */
public class MatrixOperations {
    public static Double[][] addition(Number[][] naInput1, Number[][] naInput2) {
        /**
         * daInput1 * daInput2
         */
        Double[][] daOutput = new Double[naInput1.length][naInput1[0].length];

        int iRowNumber = naInput1.length;
        int iColNumber = naInput1[0].length;

        for (int i = 0; i < iRowNumber; i++) {

            for (int j = 0; j < iColNumber; j++) {

                daOutput[i][j] = (Double) naInput1[i][j] + (Double) naInput2[i][j];
            }
        }
        return (daOutput);

    }

    public static Integer[][] additionInt(Number[][] naInput1, Number[][] naInput2) {
        /**
         * daInput1 * daInput2
         */
        Integer[][] iaOutput = new Integer[naInput1.length][naInput1[0].length];

        int iRowNumber = naInput1.length;
        int iColNumber = naInput1[0].length;

        for (int i = 0; i < iRowNumber; i++) {

            for (int j = 0; j < iColNumber; j++) {

                iaOutput[i][j] = (Integer) naInput1[i][j] + (Integer) naInput2[i][j];
            }
        }
        return (iaOutput);

    }

    public static int[][] addition(int[][] iaInput1, int iaAdd) {

        int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaReturn.length; i++) {

            for (int j = 0; j < iaReturn[0].length; j++) {

                iaReturn[i][j] = iaInput1[i][j] + iaAdd;

            }

        }

        return iaReturn;

    }

    public static int[][] addition(int[][] iaInput1, int[][] iaInput2) {

        if (iaInput1.length > 0 && iaInput2.length > 0) {

            if (iaInput1.length == iaInput2.length && iaInput1[0].length == iaInput2[0].length) {

                int[][] iaReturn = new int[iaInput1.length][iaInput1[0].length];

                for (int i = 0; i < iaReturn.length; i++) {

                    for (int j = 0; j < iaReturn[0].length; j++) {

                        iaReturn[i][j] = iaInput1[i][j] + iaInput2[i][j];

                    }

                }

                return iaReturn;

            }

        }

        return null;

    }

    public static void addition2(int[][] iaInput1, int[][] iaInput2) {

        if (iaInput1.length > 0 && iaInput2.length > 0) {

            if (iaInput1.length == iaInput2.length && iaInput1[0].length == iaInput2[0].length) {

                for (int i = 0; i < iaInput1.length; i++) {

                    for (int j = 0; j < iaInput1[0].length; j++) {

                        iaInput1[i][j] = iaInput1[i][j] + iaInput2[i][j];

                    }

                }

            }

        }

    }

    public static Double[] addition(Number[] naInput1, Number[] naInput2) {
        /**
         * daInput1 * daInput2
         */
        Double[] daOutput = new Double[naInput1.length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            daOutput[i] = (Double) naInput1[i] + (Double) naInput2[i];

        }

        return (daOutput);

    }

    public static Double[] substraction(Number[] naInput1, Number[] naInput2) {
        /**
         * daInput1 + daInput2
         */
        Double[] daOutput = new Double[naInput1.length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            daOutput[i] = (Double) naInput1[i] - (Double) naInput2[i];

        }

        return (daOutput);

    }

    public static Double[] substraction(Number[] naInput1, Number nInput) {
        /**
         * daInput1 + daInput2
         */
        Double[] daOutput = new Double[naInput1.length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            daOutput[i] = (Double) naInput1[i] - (Double) nInput;

        }

        return (daOutput);

    }

    public static Double[][] substraction(Number[][] naInput1, Number[][] naInput2) {
        /**
         * daInput1 * daInput2
         */
        Double[][] daOutput = new Double[naInput1.length][naInput1[0].length];

        int iRowNumber = naInput1.length;
        int iColNumber = naInput1[0].length;

        for (int i = 0; i < iRowNumber; i++) {

            for (int j = 0; j < iColNumber; j++) {

                daOutput[i][j] = (Double) naInput1[i][j] - (Double) naInput2[i][j];

            }

        }

        return (daOutput);

    }

    public static int[][] substraction(int[][] iaInput1, int[][] iaInput2) {
        /**
         * daInput1 * daInput2
         */
        int[][] iaOutput = new int[iaInput1.length][iaInput1[0].length];

        int iRowNumber = iaInput1.length;
        int iColNumber = iaInput1[0].length;

        for (int i = 0; i < iRowNumber; i++) {

            for (int j = 0; j < iColNumber; j++) {

                iaOutput[i][j] = iaInput1[i][j] - iaInput2[i][j];

            }

        }

        return (iaOutput);

    }

    public static int[][] substraction(int[][] iaInput1, int iValue) {
        /**
         * daInput1 * daInput2
         */
        int[][] iaOutput = new int[iaInput1.length][iaInput1[0].length];

        int iRowNumber = iaInput1.length;
        int iColNumber = iaInput1[0].length;

        for (int i = 0; i < iRowNumber; i++) {

            for (int j = 0; j < iColNumber; j++) {

                iaOutput[i][j] = iaInput1[i][j] - iValue;

            }

        }

        return (iaOutput);

    }

    public static double[][] division(int[][] iaInput1, int[][] iaInput2) {
        /**
         * daInput1/daInput2
         */
        double[][] iaOutput = new double[iaInput1.length][iaInput1[0].length];

        int iRowNumber = iaInput1.length;
        int iColNumber = iaInput1[0].length;

        for (int i = 0; i < iRowNumber; i++) {

            for (int j = 0; j < iColNumber; j++) {

                iaOutput[i][j] = ((double) iaInput1[i][j]) / ((double) iaInput2[i][j]);

            }

        }

        return (iaOutput);

    }

    public static Double[] Multiplication(Number[] naInput1, Double dMulti) {
        /**
         * daInput1 * dMulti
         */
        Double[] daOutput = new Double[naInput1.length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            daOutput[i] = (Double) naInput1[i] * dMulti;

        }

        return (daOutput);

    }

    public static int[] Multiplication(Number[] naInput1, int iMulti) {
        /**
         * daInput1 * dMulti
         */
        int[] iaOutput = new int[naInput1.length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            iaOutput[i] = (Integer) naInput1[i] * iMulti;

        }

        return (iaOutput);

    }

    public static int[] Multiplication(int[] naInput1, int iMulti) {
        /**
         * daInput1 * dMulti
         */
        int[] iaOutput = new int[naInput1.length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            iaOutput[i] = naInput1[i] * iMulti;

        }

        return (iaOutput);

    }

    public static double[] Multiplication(int[] naInput1, double iMulti) {
        /**
         * daInput1 * dMulti
         */
        double[] iaOutput = new double[naInput1.length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            iaOutput[i] = (naInput1[i] * iMulti);

        }

        return (iaOutput);

    }

    public static Double[][] Multiplication(Number[][] naInput1, Double dMulti) {
        /**
         * daInput1 * dMulti
         */
        Double[][] daOutput = new Double[naInput1.length][naInput1[0].length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            daOutput[i] = Multiplication(naInput1[i], dMulti);

        }

        return (daOutput);

    }

    public static int[][] Multiplication(Number[][] naInput1, int iMulti) {
        /**
         * daInput1 * dMulti
         */
        int[][] iaOutput = new int[naInput1.length][naInput1[0].length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            iaOutput[i] = Multiplication(naInput1[i], iMulti);

        }

        return (iaOutput);

    }

    public static int[][] Multiplication(int[][] naInput1, int iMulti) {
        /**
         * daInput1 * dMulti
         */
        int[][] iaOutput = new int[naInput1.length][naInput1[0].length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            iaOutput[i] = Multiplication(naInput1[i], iMulti);

        }

        return (iaOutput);

    }

    public static double[][] Multiplication(int[][] naInput1, double iMulti) {
        /**
         * daInput1 * dMulti
         */
        double[][] iaOutput = new double[naInput1.length][naInput1[0].length];

        int iRowNumber = naInput1.length;

        for (int i = 0; i < iRowNumber; i++) {

            iaOutput[i] = Multiplication(naInput1[i], iMulti);

        }

        return iaOutput;

    }

    public static Double[][] Multiplication(Number[][] naInput1, Number[][] naInput2) {
        /**
         * naInput1 * naInput2
         */
        Double[][] daOutput = new Double[naInput1.length][naInput2[0].length];

        if (naInput1[0].length == naInput2.length) {

            for (int i = 0; i < naInput1.length; i++) {

                for (int j = 0; j < naInput2[0].length; j++) {

                    double temp = 0.0;

                    for (int k = 0; k < naInput1[0].length; k++) {

                        temp = temp + (Double) naInput1[i][k] * (Double) naInput2[k][j];

                    }

                    daOutput[i][j] = temp;

                }

            }

        }

        return (daOutput);

    }

    public static int[][] Multiplication(int[][] iaInput1, int[][] iaInput2) {
        /**
         * naInput1 * naInput2
         */
        int[][] iaOutput = new int[iaInput1.length][iaInput2[0].length];

        if (iaInput1[0].length == iaInput2.length) {

            for (int i = 0; i < iaInput1.length; i++) {

                for (int j = 0; j < iaInput2[0].length; j++) {

                    int temp = 0;

                    for (int k = 0; k < iaInput1[0].length; k++) {

                        temp = temp + iaInput1[i][k] * iaInput2[k][j];

                    }

                    iaOutput[i][j] = temp;

                }

            }

        }

        return (iaOutput);

    }

    public static int[][] MultiplicationPiecewise(int[][] iaInput1, int[][] iaInput2) {
        /**
         * naInput1 * naInput2
         */
        int[][] iaOutput = new int[iaInput1.length][iaInput2[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput2[0].length; j++) {

                iaOutput[i][j] = iaInput1[i][j] * iaInput2[i][j];

            }

        }

        return (iaOutput);

    }

    public static Double[] multiplicationPiecewise(Double[] daInput1, Double[] daInput2) {

        Double[] daOutput = new Double[daInput1.length];

        for (int i = 0; i < daInput1.length; i++) {
            daOutput[i] = daInput1[i] * daInput2[i];
        }
        return (daOutput);
    }

    public static Double[] divisionPiecewise(Double[] daInput1, Double[] daInput2) {

        Double[] daOutput = new Double[daInput1.length];

        for (int i = 0; i < daInput1.length; i++) {
            daOutput[i] = daInput1[i] / daInput2[i];
        }
        return (daOutput);
    }

    public static int[][] powerPiecewise(int[][] iaInput1, double dPow) {
        /**
         * naInput1 * naInput2
         */
        int[][] iaOutput = new int[iaInput1.length][iaInput1[0].length];

        for (int i = 0; i < iaInput1.length; i++) {

            for (int j = 0; j < iaInput1[0].length; j++) {

                iaOutput[i][j] = (int) Math.pow(iaInput1[i][j], dPow);

            }

        }

        return (iaOutput);

    }
}
