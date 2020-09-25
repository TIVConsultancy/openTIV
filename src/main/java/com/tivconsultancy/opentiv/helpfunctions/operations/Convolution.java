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

import com.tivconsultancy.opentiv.helpfunctions.hpc.Parallel;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Convolution {
    public static int[][] Convolution(int[][] iaInput, final double[][] daConvolution) {

        if (iaInput.length >= daConvolution.length && iaInput[0].length >= daConvolution[0].length) {
            int[][] iaReturn = new int[iaInput.length][iaInput[0].length];
            for (int i = 0; i < iaReturn.length; i++) {
                for (int j = 0; j < iaReturn[0].length; j++) {

                    boolean bTouchBorder = false;

                    int iMDown = (int) (i - (daConvolution.length - 1) / 2);
                    int iMUp = (int) (i + (daConvolution.length - 1) / 2);

                    if ((iMUp - iMDown) < daConvolution.length) {
                        iMUp = iMUp + 1;
                    }
                    if (iMDown < 0) {
                        iMDown = 0;
                        bTouchBorder = true;
                    }
                    if (iMUp > iaInput.length) {
                        iMUp = iaInput.length;
                        bTouchBorder = true;
                    }

                    int jMLeft = (int) (j - (daConvolution[0].length - 1) / 2);
                    int jMRight = (int) (j + (daConvolution[0].length - 1) / 2);

                    if ((jMRight - jMLeft) < daConvolution[0].length) {
                        jMRight = jMRight + 1;
                    }
                    if (jMLeft < 0) {
                        jMLeft = 0;
                        bTouchBorder = true;
                    }
                    if (jMRight > iaInput[0].length) {
                        jMRight = iaInput[0].length;
                        bTouchBorder = true;
                    }

                    double dTemp = 0;

                    int iIrunner = 0;

                    for (int iM = iMDown; iM < iMUp; iM++) {
                        int iJRunner = 0;
                        for (int jM = jMLeft; jM < jMRight; jM++) {
                            dTemp = dTemp + (double) iaInput[iM][jM] * daConvolution[iIrunner][iJRunner];
                            iJRunner = iJRunner + 1;
                        }
                        iIrunner = iIrunner + 1;
                    }
                    if (!bTouchBorder) {
                        iaReturn[i][j] = (int) dTemp;
                    } else {
                        iaReturn[i][j] = iaInput[i][j];
                    }
                }
            }
            return (iaReturn);
        } else {
            return null;
        }

    }                
    
    public static double[][] Convolution(double[][] daInput, final double[][] daConvolution) {

        if (daInput.length >= daConvolution.length && daInput[0].length >= daConvolution[0].length) {
            double[][] daReturn = new double[daInput.length][daInput[0].length];
            for (int i = 0; i < daReturn.length; i++) {
                for (int j = 0; j < daReturn[0].length; j++) {

                    boolean bTouchBorder = false;

                    int iMDown = (int) (i - (daConvolution.length - 1.0) / 2.0);
                    int iMUp = (int) (i + (daConvolution.length - 1.0) / 2.0);

                    if ((iMUp - iMDown) < daConvolution.length) {
                        iMUp = iMUp + 1;
                    }
                    if (iMDown < 0) {
                        iMDown = 0;
                        bTouchBorder = true;
                    }
                    if (iMUp > daInput.length) {
                        iMUp = daInput.length;
                        bTouchBorder = true;
                    }

                    int jMLeft = (int) (j - (daConvolution[0].length - 1.0) / 2.0);
                    int jMRight = (int) (j + (daConvolution[0].length - 1.0) / 2.0);

                    if ((jMRight - jMLeft) < daConvolution[0].length) {
                        jMRight = jMRight + 1;
                    }
                    if (jMLeft < 0) {
                        jMLeft = 0;
                        bTouchBorder = true;
                    }
                    if (jMRight > daInput[0].length) {
                        jMRight = daInput[0].length;
                        bTouchBorder = true;
                    }

                    double dTemp = 0;

                    int iIrunner = 0;

                    for (int iM = iMDown; iM < iMUp; iM++) {
                        int iJRunner = 0;
                        for (int jM = jMLeft; jM < jMRight; jM++) {
                            dTemp = dTemp + (double) daInput[iM][jM] * daConvolution[iIrunner][iJRunner];
                            iJRunner = iJRunner + 1;
                        }
                        iIrunner = iIrunner + 1;
                    }
                    if (!bTouchBorder) {
                        daReturn[i][j] = dTemp;
                    } else {
                        daReturn[i][j] = daInput[i][j];
                    }
                }
            }
            return (daReturn);
        } else {
            return null;
        }

    }
    
    public static double[][] Convolution_d(int[][] iaInput, final double[][] daConvolution) {

        if (iaInput.length >= daConvolution.length && iaInput[0].length >= daConvolution[0].length) {
            double[][] daReturn = new double[iaInput.length][iaInput[0].length];
            for (int i = 0; i < daReturn.length; i++) {
                for (int j = 0; j < daReturn[0].length; j++) {

                    boolean bTouchBorder = false;

                    int iMDown = (int) (i - (daConvolution.length - 1.0) / 2.0);
                    int iMUp = (int) (i + (daConvolution.length - 1.0) / 2.0);

                    if ((iMUp - iMDown) < daConvolution.length) {
                        iMUp = iMUp + 1;
                    }
                    if (iMDown < 0) {
                        iMDown = 0;
                        bTouchBorder = true;
                    }
                    if (iMUp > iaInput.length) {
                        iMUp = iaInput.length;
                        bTouchBorder = true;
                    }

                    int jMLeft = (int) (j - (daConvolution[0].length - 1.0) / 2.0);
                    int jMRight = (int) (j + (daConvolution[0].length - 1.0) / 2.0);

                    if ((jMRight - jMLeft) < daConvolution[0].length) {
                        jMRight = jMRight + 1;
                    }
                    if (jMLeft < 0) {
                        jMLeft = 0;
                        bTouchBorder = true;
                    }
                    if (jMRight > iaInput[0].length) {
                        jMRight = iaInput[0].length;
                        bTouchBorder = true;
                    }

                    double dTemp = 0;

                    int iIrunner = 0;

                    for (int iM = iMDown; iM < iMUp; iM++) {
                        int iJRunner = 0;
                        for (int jM = jMLeft; jM < jMRight; jM++) {
                            dTemp = dTemp + (double) iaInput[iM][jM] * daConvolution[iIrunner][iJRunner];
                            iJRunner = iJRunner + 1;
                        }
                        iIrunner = iIrunner + 1;
                    }
                    if (!bTouchBorder) {
                        daReturn[i][j] = dTemp;
                    } else {
                        daReturn[i][j] = iaInput[i][j];
                    }
                }
            }
            return (daReturn);
        } else {
            return null;
        }

    }

    public static int[][] ConvolutionParallel(final int[][] iaInput, final double[][] daConvolution) {

        if (iaInput.length >= daConvolution.length && iaInput[0].length >= daConvolution[0].length) {
            final int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

            Collection<Integer> elems = new LinkedList<Integer>();

            for (int i = 0; i < iaReturn.length; i++) {
                elems.add(i);
            }

            Parallel.For(elems, new Parallel.Operation<Integer>() {

                @Override
                public void perform(Integer pParameter) {
                    int i = pParameter;
                    for (int j = 0; j < iaReturn[0].length; j++) {

                        boolean bTouchBorder = false;

                        int iMDown = (int) (i - (daConvolution.length - 1) / 2);
                        int iMUp = (int) (i + (daConvolution.length - 1) / 2);

                        if ((iMUp - iMDown) < daConvolution.length) {
                            iMUp = iMUp + 1;
                        }
                        if (iMDown < 0) {
                            iMDown = 0;
                            bTouchBorder = true;
                        }
                        if (iMUp > iaInput.length) {
                            iMUp = iaInput.length;
                            bTouchBorder = true;
                        }

                        int jMLeft = (int) (j - (daConvolution[0].length - 1) / 2);
                        int jMRight = (int) (j + (daConvolution[0].length - 1) / 2);

                        if ((jMRight - jMLeft) < daConvolution[0].length) {
                            jMRight = jMRight + 1;
                        }
                        if (jMLeft < 0) {
                            jMLeft = 0;
                            bTouchBorder = true;
                        }
                        if (jMRight > iaInput[0].length) {
                            jMRight = iaInput[0].length;
                            bTouchBorder = true;
                        }

                        double dTemp = 0;

                        int iIrunner = 0;

                        for (int iM = iMDown; iM < iMUp; iM++) {
                            int iJRunner = 0;
                            for (int jM = jMLeft; jM < jMRight; jM++) {
                                dTemp = dTemp + (double) iaInput[iM][jM] * daConvolution[iIrunner][iJRunner];
                                iJRunner = iJRunner + 1;
                            }
                            iIrunner = iIrunner + 1;
                        }
                        if (!bTouchBorder) {
                            iaReturn[i][j] = (int) dTemp;
                        } else {
                            iaReturn[i][j] = iaInput[i][j];
                        }
                    }

                }

            });

            return (iaReturn);
        } else {
            return null;
        }

    }

    public static Integer[][] Convolution(Integer[][] iaInput, final double[][] daConvolution) {

        if (iaInput.length >= daConvolution.length && iaInput[0].length >= daConvolution[0].length) {
            Integer[][] iaReturn = new Integer[iaInput.length][iaInput[0].length];

            for (int i = 0; i < iaReturn.length; i++) {

                for (int j = 0; j < iaReturn[0].length; j++) {

                    int iMDown = (int) (i - (daConvolution.length - 1) / 2.0);

                    int iMUp = (int) (i + (daConvolution.length - 1) / 2.0);

                    if ((iMUp - iMDown) < daConvolution.length - 1) {
                        iMUp = iMUp + 1;
                    }

                    if (iMDown < 0) {
                        iMDown = 0;
                    }

                    if (iMUp > iaInput.length - 1) {
                        iMUp = iaInput.length - 1;
                    }

                    int jMLeft = (int) (j - (daConvolution[0].length - 1) / 2.0);

                    int jMRight = (int) (j + (daConvolution[0].length - 1) / 2.0);

                    if ((jMRight - jMLeft) < daConvolution[0].length - 1) {
                        jMRight = jMRight + 1;
                    }

                    if (jMLeft < 0) {
                        jMLeft = 0;
                    }

                    if (jMRight > iaInput[0].length - 1) {
                        jMRight = iaInput[0].length - 1;
                    }

                    double dTemp = 0;

                    int iIrunner = 0;

                    for (int iM = iMDown; iM <= iMUp; iM++) {

                        int iJRunner = 0;

                        for (int jM = jMLeft; jM <= jMRight; jM++) {

//                            System.out.println(daConvolution[iIrunner][iJRunner]);
                            dTemp = dTemp + (double) iaInput[iM][jM] * daConvolution[iIrunner][iJRunner];
//                            System.out.println(jM + "/" + iaInput[0].length);

                            iJRunner = iJRunner + 1;

                        }

//                        System.out.println(iM + "/" + iaInput.length);
                        iIrunner = iIrunner + 1;

                    }

                    //System.out.println(dTemp);
                    iaReturn[i][j] = (int) dTemp;

                }

            }
            return (iaReturn);
        } else {
            return null;
        }

    }

    public static int[][] Convolution(int[][] iaInput, final int[][] iaConvolution) {

        if (iaInput.length >= iaConvolution.length && iaInput[0].length >= iaConvolution[0].length) {
            int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

            for (int i = 0; i < iaReturn.length; i++) {

                for (int j = 0; j < iaReturn[0].length; j++) {

                    int iMDown = (int) (i - Math.floor(((double) iaConvolution.length - 1) / 2.0));

                    int iMUp = (int) (i + Math.round(((double) iaConvolution.length - 1) / 2.0));

                    // System.out.println("UpdDown");
                    // System.out.println(iMDown + "|" + i + "|" + iMUp);

                    /*if ((iMUp - iMDown) < (iaConvolution.length-1)){
                     System.out.println("boing");
                     iMUp = iMUp +1;
                     }*/

 /*if (iMDown < 0) {
                     iMDown = 0;
                     }

                     if (iMUp > iaInput.length) {
                     iMUp = iaInput.length;
                     }*/
                    int jMLeft = (int) (j - Math.floor(((double) iaConvolution[0].length - 1) / 2.0));

                    int jMRight = (int) (j + Math.round(((double) iaConvolution[0].length - 1) / 2.0));

                    //System.out.println(jMLeft + "|" + j + "|" + jMRight);

                    /*if (jMLeft < 0) {
                     jMLeft = 0;
                     }

                     if (jMRight > iaInput[0].length) {
                     jMRight = iaInput[0].length;
                     }*/
                    int dTemp = 0;

                    int iIrunner = 0;

                    int iMClean = 0;

                    for (int iM = iMDown; iM <= iMUp; iM++) {

                        if (iM < 0) {
                            iMClean = iaInput.length + iM;
                        } else if (iM >= iaInput.length) {
                            iMClean = (iM - iaInput.length);
                        } else {
                            iMClean = iM;
                        }

                        int iJRunner = 0;

                        int jMClean = 0;

                        for (int jM = jMLeft; jM <= jMRight; jM++) {

                            if (jM < 0) {
                                jMClean = iaInput[0].length + jM;
                            } else if (jM >= iaInput[0].length) {
                                jMClean = (jM - iaInput[0].length);
                            } else {
                                jMClean = jM;
                            }

                            //System.out.println(daConvolution[iIrunner][iJRunner]);
                            dTemp = dTemp + iaInput[iMClean][jMClean] * iaConvolution[iIrunner][iJRunner];

                            iJRunner = iJRunner + 1;

                        }

                        iIrunner = iIrunner + 1;

                    }

                    //System.out.println(dTemp);
                    iaReturn[i][j] = dTemp;

                }

            }
            return (iaReturn);
        } else {
            return null;
        }

    }
    
    public static double[][] Convolution(double[][] iaInput, final int[][] iaConvolution) {

        if (iaInput.length >= iaConvolution.length && iaInput[0].length >= iaConvolution[0].length) {
            double[][] daReturn = new double[iaInput.length][iaInput[0].length];

            for (int i = 0; i < daReturn.length; i++) {

                for (int j = 0; j < daReturn[0].length; j++) {

                    int iMDown = (int) (i - Math.floor(((double) iaConvolution.length - 1) / 2.0));

                    int iMUp = (int) (i + Math.round(((double) iaConvolution.length - 1) / 2.0));

                    // System.out.println("UpdDown");
                    // System.out.println(iMDown + "|" + i + "|" + iMUp);

                    /*if ((iMUp - iMDown) < (iaConvolution.length-1)){
                     System.out.println("boing");
                     iMUp = iMUp +1;
                     }*/

 /*if (iMDown < 0) {
                     iMDown = 0;
                     }

                     if (iMUp > iaInput.length) {
                     iMUp = iaInput.length;
                     }*/
                    int jMLeft = (int) (j - Math.floor(((double) iaConvolution[0].length - 1) / 2.0));

                    int jMRight = (int) (j + Math.round(((double) iaConvolution[0].length - 1) / 2.0));

                    //System.out.println(jMLeft + "|" + j + "|" + jMRight);

                    /*if (jMLeft < 0) {
                     jMLeft = 0;
                     }

                     if (jMRight > iaInput[0].length) {
                     jMRight = iaInput[0].length;
                     }*/
                    double dTemp = 0;

                    int iIrunner = 0;

                    int iMClean = 0;

                    for (int iM = iMDown; iM <= iMUp; iM++) {

                        if (iM < 0) {
                            iMClean = iaInput.length + iM;
                        } else if (iM >= iaInput.length) {
                            iMClean = (iM - iaInput.length);
                        } else {
                            iMClean = iM;
                        }

                        int iJRunner = 0;

                        int jMClean = 0;

                        for (int jM = jMLeft; jM <= jMRight; jM++) {

                            if (jM < 0) {
                                jMClean = iaInput[0].length + jM;
                            } else if (jM >= iaInput[0].length) {
                                jMClean = (jM - iaInput[0].length);
                            } else {
                                jMClean = jM;
                            }

                            //System.out.println(daConvolution[iIrunner][iJRunner]);
                            dTemp = dTemp + iaInput[iMClean][jMClean] * iaConvolution[iIrunner][iJRunner];

                            iJRunner = iJRunner + 1;

                        }

                        iIrunner = iIrunner + 1;

                    }

                    //System.out.println(dTemp);
                    daReturn[i][j] = dTemp;

                }

            }
            return (daReturn);
        } else {
            return null;
        }

    }

    public static void Convolution2(int[][] iaInput, final double[][] daConvolution) {

        if (iaInput.length >= daConvolution.length && iaInput[0].length >= daConvolution[0].length) {
//            int[][] iaReturn = new int[iaInput.length][iaInput[0].length];
            for (int i = 0; i < iaInput.length; i++) {
                for (int j = 0; j < iaInput[0].length; j++) {

                    boolean bTouchBorder = false;

                    int iMDown = (int) (i - (daConvolution.length - 1) / 2);
                    int iMUp = (int) (i + (daConvolution.length - 1) / 2);

                    if ((iMUp - iMDown) < daConvolution.length) {
                        iMUp = iMUp + 1;
                    }
                    if (iMDown < 0) {
                        iMDown = 0;
                        bTouchBorder = true;
                    }
                    if (iMUp > iaInput.length) {
                        iMUp = iaInput.length;
                        bTouchBorder = true;
                    }

                    int jMLeft = (int) (j - (daConvolution[0].length - 1) / 2);
                    int jMRight = (int) (j + (daConvolution[0].length - 1) / 2);

                    if ((jMRight - jMLeft) < daConvolution[0].length) {
                        jMRight = jMRight + 1;
                    }
                    if (jMLeft < 0) {
                        jMLeft = 0;
                        bTouchBorder = true;
                    }
                    if (jMRight > iaInput[0].length) {
                        jMRight = iaInput[0].length;
                        bTouchBorder = true;
                    }

                    double dTemp = 0;

                    int iIrunner = 0;

                    for (int iM = iMDown; iM < iMUp; iM++) {
                        int iJRunner = 0;
                        for (int jM = jMLeft; jM < jMRight; jM++) {
                            dTemp = dTemp + (double) iaInput[iM][jM] * daConvolution[iIrunner][iJRunner];
                            iJRunner = iJRunner + 1;
                        }
                        iIrunner = iIrunner + 1;
                    }
                    if (!bTouchBorder) {
                        iaInput[i][j] = (int) dTemp;
                    } else {
                        iaInput[i][j] = iaInput[i][j];
                    }
                }
            }
//            return (iaReturn);
        } else {
//            return null;
        }

    }

    public static int[][] ConvolutionParallel(final int[][] iaInput, final int[][] iaConvolution) {

        if (iaInput.length >= iaConvolution.length && iaInput[0].length >= iaConvolution[0].length) {
            final int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

            Collection<Integer> elems = new LinkedList<Integer>();

            for (int i = 0; i < iaReturn.length; i++) {
                elems.add(i);
            }

            Parallel.For(elems, new Parallel.Operation<Integer>() {

                @Override
                public void perform(Integer pParameter) {
                    int i = pParameter;
                    for (int j = 0; j < iaReturn[0].length; j++) {

                        int iMDown = (int) (i - Math.floor(((double) iaConvolution.length - 1) / 2.0));

                        int iMUp = (int) (i + Math.round(((double) iaConvolution.length - 1) / 2.0));

                        // System.out.println("UpdDown");
                        // System.out.println(iMDown + "|" + i + "|" + iMUp);

                        /*if ((iMUp - iMDown) < (iaConvolution.length-1)){
                         System.out.println("boing");
                         iMUp = iMUp +1;
                         }*/

 /*if (iMDown < 0) {
                         iMDown = 0;
                         }

                         if (iMUp > iaInput.length) {
                         iMUp = iaInput.length;
                         }*/
                        int jMLeft = (int) (j - Math.floor(((double) iaConvolution[0].length - 1) / 2.0));

                        int jMRight = (int) (j + Math.round(((double) iaConvolution[0].length - 1) / 2.0));

                        //System.out.println(jMLeft + "|" + j + "|" + jMRight);

                        /*if (jMLeft < 0) {
                         jMLeft = 0;
                         }

                         if (jMRight > iaInput[0].length) {
                         jMRight = iaInput[0].length;
                         }*/
                        int dTemp = 0;

                        int iIrunner = 0;

                        int iMClean = 0;

                        for (int iM = iMDown; iM <= iMUp; iM++) {

                            if (iM < 0) {
                                iMClean = iaInput.length + iM;
                            } else if (iM >= iaInput.length) {
                                iMClean = (iM - iaInput.length);
                            } else {
                                iMClean = iM;
                            }

                            int iJRunner = 0;

                            int jMClean = 0;

                            for (int jM = jMLeft; jM <= jMRight; jM++) {

                                if (jM < 0) {
                                    jMClean = iaInput[0].length + jM;
                                } else if (jM >= iaInput[0].length) {
                                    jMClean = (jM - iaInput[0].length);
                                } else {
                                    jMClean = jM;
                                }

                                //System.out.println(daConvolution[iIrunner][iJRunner]);
                                dTemp = dTemp + iaInput[iMClean][jMClean] * iaConvolution[iIrunner][iJRunner];

                                iJRunner = iJRunner + 1;

                            }

                            iIrunner = iIrunner + 1;

                        }

                        //System.out.println(dTemp);
                        iaReturn[i][j] = dTemp;

                    }
                }

            });

            return (iaReturn);
        } else {
            return null;
        }

    }
    
    public static double[][] ConvolutionParallel(final double[][] iaInput, final int[][] iaConvolution) {

        if (iaInput.length >= iaConvolution.length && iaInput[0].length >= iaConvolution[0].length) {
            final double[][] iaReturn = new double[iaInput.length][iaInput[0].length];

            Collection<Integer> elems = new LinkedList<Integer>();

            for (int i = 0; i < iaReturn.length; i++) {
                elems.add(i);
            }

            Parallel.For(elems, new Parallel.Operation<Integer>() {

                @Override
                public void perform(Integer pParameter) {
                    int i = pParameter;
                    for (int j = 0; j < iaReturn[0].length; j++) {

                        int iMDown = (int) (i - Math.floor(((double) iaConvolution.length - 1) / 2.0));

                        int iMUp = (int) (i + Math.round(((double) iaConvolution.length - 1) / 2.0));

                        // System.out.println("UpdDown");
                        // System.out.println(iMDown + "|" + i + "|" + iMUp);

                        /*if ((iMUp - iMDown) < (iaConvolution.length-1)){
                         System.out.println("boing");
                         iMUp = iMUp +1;
                         }*/

 /*if (iMDown < 0) {
                         iMDown = 0;
                         }

                         if (iMUp > iaInput.length) {
                         iMUp = iaInput.length;
                         }*/
                        int jMLeft = (int) (j - Math.floor(((double) iaConvolution[0].length - 1) / 2.0));

                        int jMRight = (int) (j + Math.round(((double) iaConvolution[0].length - 1) / 2.0));

                        //System.out.println(jMLeft + "|" + j + "|" + jMRight);

                        /*if (jMLeft < 0) {
                         jMLeft = 0;
                         }

                         if (jMRight > iaInput[0].length) {
                         jMRight = iaInput[0].length;
                         }*/
                        double dTemp = 0;

                        int iIrunner = 0;

                        int iMClean = 0;

                        for (int iM = iMDown; iM <= iMUp; iM++) {

                            if (iM < 0) {
                                iMClean = iaInput.length + iM;
                            } else if (iM >= iaInput.length) {
                                iMClean = (iM - iaInput.length);
                            } else {
                                iMClean = iM;
                            }

                            int iJRunner = 0;

                            int jMClean = 0;

                            for (int jM = jMLeft; jM <= jMRight; jM++) {

                                if (jM < 0) {
                                    jMClean = iaInput[0].length + jM;
                                } else if (jM >= iaInput[0].length) {
                                    jMClean = (jM - iaInput[0].length);
                                } else {
                                    jMClean = jM;
                                }

                                //System.out.println(daConvolution[iIrunner][iJRunner]);
                                dTemp = dTemp + iaInput[iMClean][jMClean] * iaConvolution[iIrunner][iJRunner];

                                iJRunner = iJRunner + 1;

                            }

                            iIrunner = iIrunner + 1;

                        }

                        //System.out.println(dTemp);
                        iaReturn[i][j] = dTemp;

                    }
                }

            });

            return (iaReturn);
        } else {
            return null;
        }

    }
    
//    public static Integer Convolution(final MatrixEntry me, final int[][] iaInput, final int[][] iaConvolution) {
//
//        if(iaConvolution.length != 3 || iaConvolution[0].length != 3){
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        }
//        
//        if (iaInput.length >= iaConvolution.length && iaInput[0].length >= iaConvolution[0].length) {            
//
//            int iSum = 0;
//            
//            for( int ii = -1; ii<=1; ii++){
//                for(int jj = -1; jj<=1; jj++){
//                    int i = Math.min(Math.max(me.i+ii,0), iaInput.length);
//                    int j = Math.min(Math.max(me.j+jj,0), iaInput[0].length);
//                    iSum = iSum + ( iaInput[i][j]*iaConvolution[ii+1][jj+1]);
//                }
//            }            
//
//            return iSum/3;
//        } else {
//            return null;
//        }
//
//    }
}
