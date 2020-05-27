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
package com.tivconsultancy.opentiv.velocimetry.helpfunctions;


//import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;


import com.tivconsultancy.opentiv.helpfunctions.matrix.Matrix;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.velocimetry.directtracking.Positioning;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hendrik Hessenkemper and Thomas Ziegenhein
 */
public class FFT implements Serializable {

    private static final long serialVersionUID = 676134122L;
    public MatrixEntryWithDouble meDisplacment = new MatrixEntryWithDouble();
    public double dXcorrCoeff;
    public double dXcorrCoeffRatio;
    public double[][] CompleteMatrix;
//    public double[][] NCompleteMatrix;
    public boolean bIsNotCentered = false;

//    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, int[][] isecondIm, MatrixEntry meStart, int iWindowsize) {
//        double[][] f = transfforCP(dIntensityValues1);
//        double[][] s = transfforCP(dIntensityValues2);
//        double[][] f1 = new double[f.length][];
//        double[][] s1 = new double[s.length][];
//        for (int k = 0; k < f.length; k++) {
//            double[] aMatrix = f[k];
//            double[] bMatrix = s[k];
//            int aLength = aMatrix.length;
//            f1[k] = new double[aLength];
//            System.arraycopy(aMatrix, 0, f1[k], 0, aLength);
//            s1[k] = new double[aLength];
//            System.arraycopy(bMatrix, 0, s1[k], 0, aLength);
//        }
//        double[] isecCon = doFFT(f, s, 0);
//        double[] ifirstCon = doFFT(f1, s1, 1);
//        peakLoc(isecCon, ifirstCon, dIntensityValues1, meStart, iWindowsize, isecondIm);
//        int[] iSign = this.meDisplacment.getSign();
//        this.meDisplacment.y = this.meDisplacment.y * iSign[0];
//        this.meDisplacment.x = this.meDisplacment.x * iSign[0];
////        this.meDisplacment = new MatrixEntry(iVec[0], iVec[1]);
////        return meReturn;
//    }
//    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, int[][] isecondIm, MatrixEntry meStart, int iWindowsize, boolean btest) {
//        double[][] f = transfforCP(dIntensityValues1);
//        double[][] s = transfforCP(dIntensityValues2);
//        this.CompleteMatrix = new double[iWindowsize][iWindowsize];
//        double[][] d = doFFT(f, s);
//        double max = 0.0;
//        for (int i = 0; i < dIntensityValues1.length; i++) {
//            for (int j = 0; j < dIntensityValues1.length; j++) {
//                max += (dIntensityValues1[i][j]*dIntensityValues1[i][j]);
//            }
//        }
////        this.CompleteMatrix = d;
//        for (int i = 0; i < d.length; i++) {
//            for (int j = 0; j < d[0].length; j++) {
//                if (i<d.length/2){
//                    if (j<d[0].length/2){
//                        this.CompleteMatrix[i][j] = d[i+d.length/2][j+d.length/2]/max;
//                    } else {
//                       this.CompleteMatrix[i][j] = d[i+d.length/2][j-d.length/2]/max; 
//                    }
//                } else {
//                    if (j<d[0].length/2){
//                        this.CompleteMatrix[i][j] = d[i-d.length/2][j+d.length/2]/max;
//                    } else {
//                       this.CompleteMatrix[i][j] = d[i-d.length/2][j-d.length/2]/max; 
//                    }
//                }
//            }
//        }
//        double[] e = findMax(d);
//        MatrixEntry mePeak = new MatrixEntry(e[0], e[1]);
//        List<MatrixEntry> lmeBorders = new ArrayList<>();
//        MatrixEntry meTopLeft = new MatrixEntry(0, 0);
//        lmeBorders.add(meTopLeft);
//        MatrixEntry meTopRight = new MatrixEntry(0, iWindowsize);
//        lmeBorders.add(meTopRight);
//        MatrixEntry meBottomLeft = new MatrixEntry(iWindowsize, 0);
//        lmeBorders.add(meBottomLeft);
//        MatrixEntry meBottomRight = new MatrixEntry(iWindowsize, iWindowsize);
//        lmeBorders.add(meBottomRight);
//        MatrixEntry meNearest = Entries.getNearest(lmeBorders, mePeak);
//        this.meDisplacment = meNearest.ijDistance(mePeak);
////        double dMax = 0.0;
////        for (int i = 0; i < dIntensityValues1.length; i++) {
////            for (int j = 0; j < dIntensityValues1[0].length; j++) {
////                dMax += (dIntensityValues2[i][j] * dIntensityValues2[i][j]);
////            }
////        }
//        double[] e2 = {this.meDisplacment.i, this.meDisplacment.j};
//        this.dXcorrCoeff = testPeak(isecondIm, e2, dIntensityValues1, meStart, iWindowsize);
//        double dx0 = 0.0;
//        double dx1 = 0.0;
//        if (e2[1] != 0) {
//            double[] d0 = {this.meDisplacment.i, this.meDisplacment.j - 1};
//            dx0 = testPeak(isecondIm, d0, dIntensityValues1, meStart, iWindowsize);
//            this.CoeffMatrix[0] = dx0;
//            double[] d1 = {this.meDisplacment.i, this.meDisplacment.j + 1};
//            dx1 = testPeak(isecondIm, d1, dIntensityValues1, meStart, iWindowsize);
//            this.CoeffMatrix[1] = dx1;
//            double x1 = (0.5 * (Math.log(dx0) - Math.log(dx1))) / (Math.log(dx0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dx1));
//            if (x1 <= 0.5 && x1 >= -0.5) {
//                this.meDisplacment.x = this.meDisplacment.j + x1;
//                this.meDisplacment.j = (int) Math.round(this.meDisplacment.x);
//            } else {
//                this.meDisplacment.x = this.meDisplacment.j;
//            }
//        } else {
//            this.meDisplacment.x = 0.0;
//        }
//        if (e2[0] != 0) {
//            double[] d0 = {this.meDisplacment.i - 1, this.meDisplacment.j};
//            dx0 = testPeak(isecondIm, d0, dIntensityValues1, meStart, iWindowsize);
//            this.CoeffMatrix[2] = dx0;
//            double[] d1 = {this.meDisplacment.i + 1, this.meDisplacment.j};
//            dx1 = testPeak(isecondIm, d1, dIntensityValues1, meStart, iWindowsize);
//            this.CoeffMatrix[3] = dx1;
//            double x1 = (0.5 * (Math.log(dx0) - Math.log(dx1))) / (Math.log(dx0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dx1));
//            if (x1 <= 0.5 && x1 >= -0.5) {
//                this.meDisplacment.y = this.meDisplacment.i + x1;
//                this.meDisplacment.i = (int) Math.round(this.meDisplacment.y);
//            } else {
//                this.meDisplacment.y = this.meDisplacment.i;
//            }
//        } else {
//            this.meDisplacment.y = 0.0;
//        }
////        System.out.println(e2[0]+" "+e[1]);
////        System.out.println("dx "+x1);
//    }
    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, int[][] isecondIm, MatrixEntry meStart, int iWindowsize, boolean btest) {
        double[][] f = transfforCP(dIntensityValues1);
        double[][] s = transfforCP(dIntensityValues2);
        this.CompleteMatrix = new double[iWindowsize][iWindowsize];
        double[][] d = doFFT(f, s);
        double max0 = 0.0;
        double max1 = 0.0;
        double[][] CompleteMatrix2 = new double[iWindowsize][iWindowsize];
        for (int i = 0; i < dIntensityValues1.length; i++) {
            for (int j = 0; j < dIntensityValues1.length; j++) {
                max0 += (dIntensityValues1[i][j] * dIntensityValues1[i][j]);
                max1 += (dIntensityValues2[i][j] * dIntensityValues2[i][j]);
            }
        }
        double max = Math.sqrt(max1 * max0);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (i < d.length / 2) {
                    if (j < d[0].length / 2) {
                        CompleteMatrix2[i][j] = d[i + d.length / 2][j + d.length / 2] / max;
                    } else {
                        CompleteMatrix2[i][j] = d[i + d.length / 2][j - d.length / 2] / max;
                    }
                } else if (j < d[0].length / 2) {
                    CompleteMatrix2[i][j] = d[i - d.length / 2][j + d.length / 2] / max;
                } else {
                    CompleteMatrix2[i][j] = d[i - d.length / 2][j - d.length / 2] / max;
                }
            }
        }

        double[] ee = findMax(CompleteMatrix2);
        MatrixEntry mePeak2 = new MatrixEntry(ee[0], ee[1]);
        this.meDisplacment = new MatrixEntryWithDouble(new MatrixEntry(iWindowsize / 2, iWindowsize / 2).ijDistance(mePeak2));
        double[] e2 = {this.meDisplacment.i, this.meDisplacment.j};
        double[][] dIntensityValues3 = new double[iWindowsize][iWindowsize];
        for (int i = 0; i < dIntensityValues3.length; i++) {
            for (int j = 0; j < dIntensityValues3.length; j++) {
                dIntensityValues3[i][j] = isecondIm[i + meStart.i + (int) e2[0]][j + meStart.j + (int) e2[1]];
            }
        }
        double[][] f1 = transfforCP(dIntensityValues1);
        double[][] s1 = transfforCP(dIntensityValues3);
        double[][] dd = doFFT(f1, s1);
        double max11 = 0.0;
        for (int i = 0; i < dIntensityValues1.length; i++) {
            for (int j = 0; j < dIntensityValues1.length; j++) {
                max11 += (dIntensityValues3[i][j] * dIntensityValues3[i][j]);
            }
        }
        double maxx = Math.sqrt(max11 * max0);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (i < d.length / 2) {
                    if (j < d[0].length / 2) {
                        this.CompleteMatrix[i][j] = dd[i + d.length / 2][j + d.length / 2] / maxx;
                    } else {
                        this.CompleteMatrix[i][j] = dd[i + d.length / 2][j - d.length / 2] / maxx;
                    }
                } else if (j < d[0].length / 2) {
                    this.CompleteMatrix[i][j] = dd[i - d.length / 2][j + d.length / 2] / maxx;
                } else {
                    this.CompleteMatrix[i][j] = dd[i - d.length / 2][j - d.length / 2] / maxx;
                }
            }
        }
        double[] e = findMax(CompleteMatrix);
//        if (e[0] != iWindowsize / 2 || e[1] != iWindowsize / 2) {
        this.bIsNotCentered = true;
//        }

        this.dXcorrCoeff = this.CompleteMatrix[iWindowsize / 2][iWindowsize / 2];
        double dSecondMax = find2Max(CompleteMatrix, dXcorrCoeff);
        this.dXcorrCoeffRatio = dXcorrCoeff / dSecondMax;
        double dx0 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 - 1];
        double dx1 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 + 1];
        double x1 = (0.5 * (Math.log(dx0) - Math.log(dx1))) / (Math.log(dx0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dx1));
        if (x1 <= 0.5 && x1 >= -0.5) {
            this.meDisplacment.x = this.meDisplacment.j + x1;
        } else {
            this.meDisplacment.x = this.meDisplacment.j;
        }
        double dy0 = CompleteMatrix[iWindowsize / 2 - 1][iWindowsize / 2];
        double dy1 = CompleteMatrix[iWindowsize / 2 + 1][iWindowsize / 2];
        double y1 = (0.5 * (Math.log(dy0) - Math.log(dy1))) / (Math.log(dy0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dy1));
        if (y1 <= 0.5 && y1 >= -0.5) {
            this.meDisplacment.y = this.meDisplacment.i + y1;
        } else {
            this.meDisplacment.y = this.meDisplacment.i;
        }
    }

    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, List<double[][]> ldOverlap1, List<double[][]> ldOverlap2, int iWindowsize, MatrixEntry mePredictor) {
        double[][] f = transfforCP(dIntensityValues1);
        double[][] s = transfforCP(dIntensityValues2);
        List<double[][]> ld = new ArrayList<>();
        for (int i = 0; i < ldOverlap1.size(); i++) {
            double[][] f2 = transfforCP(ldOverlap1.get(i));
            double[][] s2 = transfforCP(ldOverlap2.get(i));
            ld.add(doFFT(f2, s2));
        }
        this.CompleteMatrix = new double[iWindowsize][iWindowsize];
        double[][] d = doFFT(f, s);
        for (double[][] dOverlap : ld) {
            d = Matrix.multiplication(d, dOverlap);
        }

        double max0 = 0.0;
        double max1 = 0.0;
        for (int i = 0; i < dIntensityValues1.length; i++) {
            for (int j = 0; j < dIntensityValues1.length; j++) {
                max0 += (dIntensityValues1[i][j] * dIntensityValues1[i][j]);
                max1 += (dIntensityValues2[i][j] * dIntensityValues2[i][j]);
            }
        }
        double max = Math.sqrt(max1 * max0);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (i < d.length / 2) {
                    if (j < d[0].length / 2) {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j + d.length / 2] / max;
                    } else {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j - d.length / 2] / max;
                    }
                } else if (j < d[0].length / 2) {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j + d.length / 2] / max;
                } else {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j - d.length / 2] / max;
                }
            }
        }
        double[] ee = findMax(CompleteMatrix);
        MatrixEntry mePeak2 = new MatrixEntry(ee[0], ee[1]);
        this.meDisplacment = new MatrixEntryWithDouble(new MatrixEntry(iWindowsize / 2, iWindowsize / 2).ijDistance(mePeak2));
        if (meDisplacment.i != 0 || meDisplacment.j != 0) {
            this.bIsNotCentered = true;
        } else {
            this.bIsNotCentered = false;
        }
        meDisplacment.i += mePredictor.i;
        meDisplacment.j += mePredictor.j;
        this.dXcorrCoeff = this.CompleteMatrix[mePeak2.i][mePeak2.j];
        double dSecondMax = find2Max(CompleteMatrix, dXcorrCoeff);
        this.dXcorrCoeffRatio = dXcorrCoeff / dSecondMax;
        double dx0 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 - 1];
        double dx1 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 + 1];
        double x1 = (0.5 * (Math.log(dx0) - Math.log(dx1))) / (Math.log(dx0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dx1));
        if (x1 <= 0.5 && x1 > -0.5) {
            this.meDisplacment.x = this.meDisplacment.j + x1;
        } else {
            this.meDisplacment.x = this.meDisplacment.j;
        }
        double dy0 = CompleteMatrix[iWindowsize / 2 - 1][iWindowsize / 2];
        double dy1 = CompleteMatrix[iWindowsize / 2 + 1][iWindowsize / 2];
        double y1 = (0.5 * (Math.log(dy0) - Math.log(dy1))) / (Math.log(dy0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dy1));
        if (y1 <= 0.5 && y1 > -0.5) {
            this.meDisplacment.y = this.meDisplacment.i + y1;
        } else {
            this.meDisplacment.y = this.meDisplacment.i;
        }
//        this.bIsNotCentered = true;
    }

    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, int iWindowsize, String sSubPixelType) {
//        sSubPixelType = "2DGauss";
        double[][] f = transfforCP(dIntensityValues1);
        double[][] s = transfforCP(dIntensityValues2);
        this.CompleteMatrix = new double[iWindowsize][iWindowsize];
        double[][] d = doFFT(f, s);
        double max = 0.0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[i][j] > max) {
                    max = d[i][j];
                }
                if (d[i][j] < min) {
                    min = d[i][j];
                }
            }
        }

        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = 255.0 * ((d[i][j] - min) / (max - min));
            }
        }

        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (i < d.length / 2) {
                    if (j < d[0].length / 2) {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j + d.length / 2];
                    } else {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j - d.length / 2];
                    }
                } else if (j < d[0].length / 2) {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j + d.length / 2];
                } else {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j - d.length / 2];
                }
            }
        }

        double[] ee = findMax(CompleteMatrix);
        MatrixEntry mePeak2 = new MatrixEntry(ee[0], ee[1]);

        //3 variables fit
        this.dXcorrCoeff = this.CompleteMatrix[mePeak2.i][mePeak2.j];
        double y0 = mePeak2.i;
        double dRiMinus1;
        double dRiPlus1;
        if ((mePeak2.i + 1) >= CompleteMatrix.length || mePeak2.i == 0) {
            dRiMinus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
            dRiPlus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
        } else {
            dRiMinus1 = CompleteMatrix[mePeak2.i - 1][mePeak2.j];
            dRiPlus1 = CompleteMatrix[mePeak2.i + 1][mePeak2.j];
        }

        if (Math.abs(dRiMinus1 - dRiPlus1) > 1 / (10 * iWindowsize)) {
            if (sSubPixelType.equals("None")) {
                y0 = mePeak2.i;
            } else if (sSubPixelType.equals("Parabolic")) {
                y0 = mePeak2.i + (dRiMinus1 - dRiPlus1) / (2 * dRiMinus1 - 4 * this.dXcorrCoeff + 2 * dRiPlus1);
            } else if (sSubPixelType.equals("Centroid")) {
                y0 = ((mePeak2.i - 1) * dRiMinus1 + (mePeak2.i) * this.dXcorrCoeff + (mePeak2.i + 1) * dRiPlus1) / (dRiMinus1 + this.dXcorrCoeff + dRiPlus1);
            } else if (sSubPixelType.equals("2DGauss")) {
            } else {
                y0 = mePeak2.i + (Math.log(dRiMinus1) - Math.log(dRiPlus1)) / (2 * Math.log(dRiMinus1) - 4 * Math.log(this.dXcorrCoeff) + 2 * Math.log(dRiPlus1));
            }
        }

        double dRjMinus1;
        double dRjPlus1;
        if ((mePeak2.j + 1) >= CompleteMatrix[0].length || mePeak2.j == 0) {
            dRjMinus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
            dRjPlus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
        } else {
            dRjMinus1 = CompleteMatrix[mePeak2.i][mePeak2.j - 1];
            dRjPlus1 = CompleteMatrix[mePeak2.i][mePeak2.j + 1];
        }
        double x0 = mePeak2.j;
        if (Math.abs(dRjMinus1 - dRjPlus1) > 1 / (10 * iWindowsize)) {
            if (sSubPixelType.equals("None")) {
                x0 = mePeak2.j;
            } else if (sSubPixelType.equals("Parabolic")) {
                x0 = mePeak2.j + (dRjMinus1 - dRjPlus1) / (2 * dRjMinus1 - 4 * this.dXcorrCoeff + 2 * dRjPlus1);
            } else if (sSubPixelType.equals("Centroid")) {
                x0 = ((mePeak2.j - 1) * dRjMinus1 + (mePeak2.j) * this.dXcorrCoeff + (mePeak2.j + 1) * dRjPlus1) / (dRjMinus1 + this.dXcorrCoeff + dRjPlus1);
            } else if (sSubPixelType.equals("2DGauss")) {
            } else {
                x0 = mePeak2.j + (Math.log(dRjMinus1) - Math.log(dRjPlus1)) / (2 * Math.log(dRjMinus1) - 4 * Math.log(this.dXcorrCoeff) + 2 * Math.log(dRjPlus1));
            }
        }

        if ((mePeak2.j + 2) >= CompleteMatrix[0].length || mePeak2.j <= 1 || (mePeak2.i + 2) >= CompleteMatrix.length || mePeak2.i <= 1) {
        } else {
            if (sSubPixelType.equals("2DGauss")) {
                List<OrderedPair> lop = new ArrayList<>();
                for (int i = mePeak2.i - 1; i <= mePeak2.i + 1; i++) {
                    for (int j = mePeak2.j - 1; j <= mePeak2.j + 1; j++) {
                        lop.add(new OrderedPair(j - mePeak2.j, i - mePeak2.i, CompleteMatrix[i][j]));
                    }
                }
//                OrderedPair op = Positioning.ellipticalGausFit(lop);
                OrderedPair op = Positioning.weightedPosition(lop, (List<? extends OrderedPair> loIn, OrderedPair op1) -> Math.abs(255.0 - op1.dValue));
                x0 = mePeak2.j + op.x;
                y0 = mePeak2.i + op.y;
            }
        }

        this.meDisplacment = new MatrixEntryWithDouble(x0 - iWindowsize / 2.0, y0 - iWindowsize / 2.0);
        if (meDisplacment.i != 0 || meDisplacment.j != 0) {
            this.bIsNotCentered = true;
        } else {
            this.bIsNotCentered = false;
        }

//        this.bIsNotCentered = true;
    }

    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, double[][] dIntensityValues1N, double[][] dIntensityValues2N, double[][] dIntensityValues1E, double[][] dIntensityValues2E, double[][] dIntensityValues1S, double[][] dIntensityValues2S, double[][] dIntensityValues1W, double[][] dIntensityValues2W, int iWindowsize, String sSubPixelType) {
//        sSubPixelType = "2DGauss";

        boolean bIncludeNorth = true;
        boolean bIncludeEast = true;
        boolean bIncludeSouth = true;
        boolean bIncludeWest = true;

        if (dIntensityValues1N == null || dIntensityValues2N == null || dIntensityValues1N.length != dIntensityValues1.length || dIntensityValues1N[0].length != dIntensityValues1[0].length) {
            bIncludeNorth = false;
        }

        if (dIntensityValues1E == null || dIntensityValues2E == null || dIntensityValues1E.length != dIntensityValues1.length || dIntensityValues1E[0].length != dIntensityValues1[0].length) {
            bIncludeEast = false;
        }

        if (dIntensityValues1W == null || dIntensityValues2W == null || dIntensityValues1W.length != dIntensityValues1.length || dIntensityValues1W[0].length != dIntensityValues1[0].length) {
            bIncludeWest = false;
        }

        if (dIntensityValues1S == null || dIntensityValues2S == null || dIntensityValues1S.length != dIntensityValues1.length || dIntensityValues1S[0].length != dIntensityValues1[0].length) {
            bIncludeSouth = false;
        }

        double[][] fC;
        double[][] sC;
        double[][] dC;
        //North        
        double[][] fN;
        double[][] sN;
        double[][] dN = new double[iWindowsize][iWindowsize];
        //East
        double[][] fE;
        double[][] sE;
        double[][] dE = new double[iWindowsize][iWindowsize];
        //South
        double[][] fS;
        double[][] sS;
        double[][] dS = new double[iWindowsize][iWindowsize];
        //West
        double[][] fW;
        double[][] sW;
        double[][] dW = new double[iWindowsize][iWindowsize];

        //Center
        fC = transfforCP(dIntensityValues1);
        sC = transfforCP(dIntensityValues2);
        dC = doFFT(fC, sC);
        //North
        if (bIncludeNorth) {
            fN = transfforCP(dIntensityValues1N);
            sN = transfforCP(dIntensityValues2N);
            dN = doFFT(fN, sN);
        }
        //East
        if (bIncludeEast) {
            fE = transfforCP(dIntensityValues1E);
            sE = transfforCP(dIntensityValues2E);
            dE = doFFT(fE, sE);
        }
        //South
        if (bIncludeSouth) {
            fS = transfforCP(dIntensityValues1S);
            sS = transfforCP(dIntensityValues2S);
            dS = doFFT(fS, sS);
        }
        //West
        if (bIncludeWest) {
            fW = transfforCP(dIntensityValues1W);
            sW = transfforCP(dIntensityValues2W);
            dW = doFFT(fW, sW);
        }

        double[][] d = new double[dC.length][dC[0].length];

        for (int i = 0; i < dC.length; i++) {
            for (int j = 0; j < dC[0].length; j++) {
                d[i][j] = d[i][j] + dC[i][j];
                if (bIncludeNorth) {
                    d[i][j] = d[i][j] + dN[i][j];
                }
                if (bIncludeEast) {
                    d[i][j] = d[i][j] + dE[i][j];
                }
                if (bIncludeSouth) {
                    d[i][j] = d[i][j] + dS[i][j];
                }
                if (bIncludeWest) {
                    d[i][j] = d[i][j] + dW[i][j];
                }
            }
        }

        double max = 0.0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[i][j] > max) {
                    max = d[i][j];
                }
                if (d[i][j] < min) {
                    min = d[i][j];
                }
            }
        }

        this.CompleteMatrix = new double[iWindowsize][iWindowsize];
//        for (int i = 0; i < d.length; i++) {
//            for (int j = 0; j < d[0].length; j++) {
//                d[i][j] = 255.0 * ((d[i][j] - min) / (max - min));
//            }
//        }

        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (i < d.length / 2) {
                    if (j < d[0].length / 2) {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j + d.length / 2];
                    } else {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j - d.length / 2];
                    }
                } else if (j < d[0].length / 2) {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j + d.length / 2];
                } else {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j - d.length / 2];
                }
            }
        }

        double[] ee = findMax(CompleteMatrix);
        MatrixEntry mePeak2 = new MatrixEntry(ee[0], ee[1]);

        //3 variables fit
        this.dXcorrCoeff = this.CompleteMatrix[mePeak2.i][mePeak2.j];
        double y0 = mePeak2.i;
        double dRiMinus1;
        double dRiPlus1;
        if ((mePeak2.i + 1) >= CompleteMatrix.length || mePeak2.i == 0) {
            dRiMinus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
            dRiPlus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
        } else {
            dRiMinus1 = CompleteMatrix[mePeak2.i - 1][mePeak2.j];
            dRiPlus1 = CompleteMatrix[mePeak2.i + 1][mePeak2.j];
        }

        if (Math.abs(dRiMinus1 - dRiPlus1) > 1 / (10 * iWindowsize)) {
            if (sSubPixelType.equals("None")) {
                y0 = mePeak2.i;
            } else if (sSubPixelType.equals("Parabolic")) {
                y0 = mePeak2.i + (dRiMinus1 - dRiPlus1) / (2 * dRiMinus1 - 4 * this.dXcorrCoeff + 2 * dRiPlus1);
            } else if (sSubPixelType.equals("Centroid")) {
                y0 = ((mePeak2.i - 1) * dRiMinus1 + (mePeak2.i) * this.dXcorrCoeff + (mePeak2.i + 1) * dRiPlus1) / (dRiMinus1 + this.dXcorrCoeff + dRiPlus1);
            } else if (sSubPixelType.equals("2DGauss")) {
            } else {
                y0 = mePeak2.i + (Math.log(dRiMinus1) - Math.log(dRiPlus1)) / (2 * Math.log(dRiMinus1) - 4 * Math.log(this.dXcorrCoeff) + 2 * Math.log(dRiPlus1));
            }
        }

        double dRjMinus1;
        double dRjPlus1;
        if ((mePeak2.j + 1) >= CompleteMatrix[0].length || mePeak2.j == 0) {
            dRjMinus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
            dRjPlus1 = CompleteMatrix[mePeak2.i][mePeak2.j];
        } else {
            dRjMinus1 = CompleteMatrix[mePeak2.i][mePeak2.j - 1];
            dRjPlus1 = CompleteMatrix[mePeak2.i][mePeak2.j + 1];
        }
        double x0 = mePeak2.j;
        if (Math.abs(dRjMinus1 - dRjPlus1) > 1 / (10 * iWindowsize)) {
            if (sSubPixelType.equals("None")) {
                x0 = mePeak2.j;
            } else if (sSubPixelType.equals("Parabolic")) {
                x0 = mePeak2.j + (dRjMinus1 - dRjPlus1) / (2 * dRjMinus1 - 4 * this.dXcorrCoeff + 2 * dRjPlus1);
            } else if (sSubPixelType.equals("Centroid")) {
                x0 = ((mePeak2.j - 1) * dRjMinus1 + (mePeak2.j) * this.dXcorrCoeff + (mePeak2.j + 1) * dRjPlus1) / (dRjMinus1 + this.dXcorrCoeff + dRjPlus1);
            } else if (sSubPixelType.equals("2DGauss")) {
            } else {
                x0 = mePeak2.j + (Math.log(dRjMinus1) - Math.log(dRjPlus1)) / (2 * Math.log(dRjMinus1) - 4 * Math.log(this.dXcorrCoeff) + 2 * Math.log(dRjPlus1));
            }
        }

        if ((mePeak2.j + 2) >= CompleteMatrix[0].length || mePeak2.j <= 1 || (mePeak2.i + 2) >= CompleteMatrix.length || mePeak2.i <= 1) {
        } else {
            if (sSubPixelType.equals("2DGauss")) {
                List<OrderedPair> lop = new ArrayList<>();
                for (int i = mePeak2.i - 1; i <= mePeak2.i + 1; i++) {
                    for (int j = mePeak2.j - 1; j <= mePeak2.j + 1; j++) {
                        lop.add(new OrderedPair(j - mePeak2.j, i - mePeak2.i, CompleteMatrix[i][j]));
                    }
                }
//                OrderedPair op = Positioning.ellipticalGausFit(lop);
                OrderedPair op = Positioning.weightedPosition(lop, (List<? extends OrderedPair> loIn, OrderedPair op1) -> Math.abs(255.0 - op1.dValue));
                x0 = mePeak2.j + op.x;
                y0 = mePeak2.i + op.y;
            }
        }

        this.meDisplacment = new MatrixEntryWithDouble(x0 - iWindowsize / 2.0, y0 - iWindowsize / 2.0);
        if (meDisplacment.i != 0 || meDisplacment.j != 0) {
            this.bIsNotCentered = true;
        } else {
            this.bIsNotCentered = false;
        }

//        this.bIsNotCentered = true;
    }

//    //Peak finding troubles, delete when others are working
//    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, int iWindowsize) {
//        double[][] f = transfforCP(dIntensityValues1);
//        double[][] s = transfforCP(dIntensityValues2);
//        this.CompleteMatrix = new double[iWindowsize][iWindowsize];
//        double[][] d = doFFT(f, s);
//        double max0 = 0.0;
//        double max1 = 0.0;
//        for (int i = 0; i < dIntensityValues1.length; i++) {
//            for (int j = 0; j < dIntensityValues1.length; j++) {
//                max0 += (dIntensityValues1[i][j] * dIntensityValues1[i][j]);
//                max1 += (dIntensityValues2[i][j] * dIntensityValues2[i][j]);
//            }
//        }
//        double max = Math.sqrt(max1 * max0);
//        for (int i = 0; i < d.length; i++) {
//            for (int j = 0; j < d[0].length; j++) {
//                if (i < d.length / 2) {
//                    if (j < d[0].length / 2) {
//                        CompleteMatrix[i][j] = d[i + d.length / 2][j + d.length / 2] / max;
//                    } else {
//                        CompleteMatrix[i][j] = d[i + d.length / 2][j - d.length / 2] / max;
//                    }
//                } else if (j < d[0].length / 2) {
//                    CompleteMatrix[i][j] = d[i - d.length / 2][j + d.length / 2] / max;
//                } else {
//                    CompleteMatrix[i][j] = d[i - d.length / 2][j - d.length / 2] / max;
//                }
//            }
//        }
//        double[] ee = findMax(CompleteMatrix);
//        MatrixEntry mePeak2 = new MatrixEntry(ee[0], ee[1]);
//        this.meDisplacment = new MatrixEntryWithDouble(new MatrixEntry(iWindowsize / 2, iWindowsize / 2).ijDistance(mePeak2));
//        if (meDisplacment.i != 0 || meDisplacment.j != 0) {
//            this.bIsNotCentered = true;
//        } else {
//            this.bIsNotCentered = false;
//        }
//        this.dXcorrCoeff = this.CompleteMatrix[mePeak2.i][mePeak2.j];
//        double dSecondMax = find2Max(CompleteMatrix, dXcorrCoeff);
//        this.dXcorrCoeffRatio = dXcorrCoeff / dSecondMax;
//        double dx0 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 - 1];
//        double dx1 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 + 1];
//        double x1 = (0.5 * (Math.log(dx0) - Math.log(dx1))) / (Math.log(dx0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dx1));
//        if (x1 <= 0.5 && x1 > -0.5) {
//            this.meDisplacment.x = this.meDisplacment.j + x1;
//        } else {
//            this.meDisplacment.x = this.meDisplacment.j;
//        }
//        double dy0 = CompleteMatrix[iWindowsize / 2 - 1][iWindowsize / 2];
//        double dy1 = CompleteMatrix[iWindowsize / 2 + 1][iWindowsize / 2];
//        double y1 = (0.5 * (Math.log(dy0) - Math.log(dy1))) / (Math.log(dy0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dy1));
//        if (y1 <= 0.5 && y1 > -0.5) {
//            this.meDisplacment.y = this.meDisplacment.i + y1;
//        } else {
//            this.meDisplacment.y = this.meDisplacment.i;
//        }
////        this.bIsNotCentered = true;
//    }
    public FFT(double[][] dIntensityValues1, double[][] dIntensityValues2, int iWindowsize, OrderedPair mePredictor) {
        double[][] f = transfforCP(dIntensityValues1);
        double[][] s = transfforCP(dIntensityValues2);
        this.CompleteMatrix = new double[iWindowsize][iWindowsize];
        double[][] d = doFFT(f, s);
        double max0 = 0.0;
        double max1 = 0.0;
        for (int i = 0; i < dIntensityValues1.length; i++) {
            for (int j = 0; j < dIntensityValues1.length; j++) {
                max0 += (dIntensityValues1[i][j] * dIntensityValues1[i][j]);
                max1 += (dIntensityValues2[i][j] * dIntensityValues2[i][j]);
            }
        }
        double max = Math.sqrt(max1 * max0);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (i < d.length / 2) {
                    if (j < d[0].length / 2) {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j + d.length / 2] / max;
                    } else {
                        CompleteMatrix[i][j] = d[i + d.length / 2][j - d.length / 2] / max;
                    }
                } else if (j < d[0].length / 2) {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j + d.length / 2] / max;
                } else {
                    CompleteMatrix[i][j] = d[i - d.length / 2][j - d.length / 2] / max;
                }
            }
        }
        double[] ee = findMax(CompleteMatrix);
        MatrixEntry mePeak2 = new MatrixEntry(ee[0], ee[1]);
//        this.meDisplacment = new MatrixEntry(iWindowsize / 2, iWindowsize / 2).ijDistance(mePeak2);
//this.meDisplacment.x=mePredictor.x;
//this.meDisplacment.y=mePredictor.y
//        meDisplacment.i +=mePredictor.i;
//        meDisplacment.j +=mePredictor.j;
        this.dXcorrCoeff = this.CompleteMatrix[mePeak2.i][mePeak2.j];
        double dSecondMax = find2Max(CompleteMatrix, dXcorrCoeff);
        this.dXcorrCoeffRatio = dXcorrCoeff / dSecondMax;
        double dx0 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 - 1];
        double dx1 = CompleteMatrix[iWindowsize / 2][iWindowsize / 2 + 1];
        double x1 = (0.5 * (Math.log(dx0) - Math.log(dx1))) / (Math.log(dx0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dx1));
        if (x1 <= 0.5 && x1 > -0.5) {
            this.meDisplacment.x = mePredictor.x + x1;
        } else {
            this.meDisplacment.x = mePredictor.x;
        }

        double dy0 = CompleteMatrix[iWindowsize / 2 - 1][iWindowsize / 2];
        double dy1 = CompleteMatrix[iWindowsize / 2 + 1][iWindowsize / 2];
        double y1 = (0.5 * (Math.log(dy0) - Math.log(dy1))) / (Math.log(dy0) - 2 * Math.log(this.dXcorrCoeff) + Math.log(dy1));
        if (y1 <= 0.5 && y1 > -0.5) {
            this.meDisplacment.y = mePredictor.y + y1;
        } else {
            this.meDisplacment.y = mePredictor.y;
        }
//        meDisplacment.y += mePredictor.y;
//        this.bIsNotCentered = true;
    }

    public static double[][] transfforCP(double[][] a) {
        double[][] b = new double[a.length][a[0].length * 2];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if ((j * 2) % 2 == 0) {
                    b[i][2 * j] = a[i][j];
                }
            }
        }
        return b;
    }

    public double[] doFFT(double[][] f, double[][] s, int a) {
        DoubleFFT_2D r = new DoubleFFT_2D(f.length, f[0].length / 2);
        r.complexForward(f);
        r.complexForward(s);
        switch (a) {
            case 0:
                conjugCP(f);
                break;
            case 1:
                conjugCP(s);
                break;

            default:
                break;
        }
        double[][] c = multpCP(f, s);
        r.complexInverse(c, true);
        double[][] d = absValCP(c);
        double[] e = findMax(d);
        double[] ff = new double[2];
        ff[0] = Math.round(e[0]);
        ff[1] = Math.round(e[1]);

        return ff;
    }

    public double[][] doFFT(double[][] f, double[][] s) {
        /*
         f x s = abs( inv( inv(F(f))*F(s) ) )
         */
        DoubleFFT_2D r = new DoubleFFT_2D(f.length, f[0].length / 2);
        r.complexForward(f);
        r.complexForward(s);
        conjugCP(f);
        double[][] c = multpCP(f, s);
        r.complexInverse(c, true);
        double[][] d = absValCP(c);

        return d;
    }

    public double[] findMax(double[][] a) {
        double[] b = new double[2];
        double test = 0;
        int[][] iaInput = new int[a.length][a[0].length];
//        List<MatrixEntry> lo = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
//                MatrixEntry op = new MatrixEntry(j, i, a[i][j]);
//                lo.add(op);
                iaInput[i][j] = (int) a[i][j];
                if (a[i][j] > test) {
                    test = a[i][j];
                    b[0] = i;
                    b[1] = j;
                }
            }
        }

//        System.out.println("Current "+test);
//        System.out.println(b[0] + " " + b[1]);
////        for (int i = 0; i < a.length; i++) {
////            for (int j = 0; j < a[0].length; j++) {
////                iaInput[i][j] =(int) (a[i][j] * 255 / test);
////            }
////        }
////        List<MatrixEntry> lme2 = Entries.getPointsInVicinity(lo, new MatrixEntry(b[0], b[1]));
//        List<MatrixEntry> lme = Entries.getPointsInVicinity(iaInput, new MatrixEntry(b[0], b[1]), 1, null);
//        int iph = 0;
//        for (int i = 0; i < lme.size(); i++) {
//            if (lme.get(i).equalsMatrixEntry(new MatrixEntry(b[0], b[1]))) {
//                iph = i;
//            }
//        }
//        lme.remove(iph);
//        //        lme.remove(0);
//        OrderedPair opMid = Positioning.ellipticalGausFit(lme, test, new Positioning.Weight() {
//
//            @Override
//            public Double getWeight(List<? extends OrderedPair> loIn, OrderedPair op) {
//                return Math.abs(1.0);
//            }
//        });
////        OrderedPair opMid2 = Positioning.ellipticalGausFit(lme2, test, new Positioning.Weight() {
////
////            @Override
////            public Double getWeight(List<? extends OrderedPair> loIn, OrderedPair op) {
////                return Math.abs(1.0);
////            }
////        });
//
////        System.out.println(b[0] + " " + b[1] + "  " + opMid.y + " " + opMid.x);
////        System.out.println(b[0] + " " + b[1] + "  " + opMid2.y + " " + opMid2.x);
//        b[0] = opMid.y;
//        b[1] = opMid.x;
        return b;
    }

    public double find2Max(double[][] a, double dMax) {
//        double[] b = new double[2];
        double test = 0;
        int[][] iaInput = new int[a.length][a[0].length];
//        List<MatrixEntry> lo = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
//                MatrixEntry op = new MatrixEntry(j, i, a[i][j]);
//                lo.add(op);
                iaInput[i][j] = (int) a[i][j];
                if (a[i][j] > test && a[i][j] < dMax) {
                    test = a[i][j];
//                    b[0] = i;
//                    b[1] = j;
                }
            }
        }
        return test;
    }

    public static double xcorr(double[][] array1, double[][] array2) {
        int y = array1.length;
        int x = array1[1].length;
        // Cross-Correlation  
        double xcorrCoeff, norm;
        double sum = 0;
        double suma = 0;
        double sumb = 0;
        for (int ii = 0; ii < y; ii++) {
            for (int jj = 0; jj < x; jj++) {
                sum += array1[ii][jj] * array2[ii][jj];
                suma += array1[ii][jj] * array1[ii][jj];
                sumb += array2[ii][jj] * array2[ii][jj];
            }
        }
        norm = Math.sqrt(suma * sumb);
        xcorrCoeff = sum / norm;
        return xcorrCoeff;
    }

    public static void conjugCP(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (j % 2 != 0) {
                    a[i][j] = -a[i][j];
                }
            }
        }
    }

    public static double[][] multpCP(double[][] a, double[][] b) {
        double[][] c = new double[a.length][a[0].length];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                if (j % 2 != 0) {
                    c[i][j] = a[i][j - 1] * b[i][j] + a[i][j] * b[i][j - 1];
                } else {
                    c[i][j] = a[i][j] * b[i][j] - a[i][j + 1] * b[i][j + 1];
                }
            }
        }
        return c;
    }

    public static double[][] absValCP(double[][] a) {
        double[][] b = new double[a.length][a[0].length / 2];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                double f1 = a[i][j * 2];
                double f2 = a[i][j * 2 + 1];
                b[i][j] = Math.sqrt((f1 * f1) + (f2 * f2));
            }
        }
        return b;
    }

    public void peakLoc(double[] a, double[] b, double[][] dIntensityValues, MatrixEntry meStart, int iWindowsize, int[][] second1) {

        double[] a1 = new double[2];
        double[] b1 = new double[2];
        double[] c1 = new double[2];
        a1[0] = -b[0];
        a1[1] = -b[1];
        b1[0] = a[0];
        b1[1] = -b[1];
        int[] t = new int[2];
        double test1 = 0;
        double f1 = testPeak(second1, a, dIntensityValues, meStart, iWindowsize);
        double f2 = testPeak(second1, a1, dIntensityValues, meStart, iWindowsize);
        if (f1 > f2) {
            t[0] = (int) a[0];
            t[1] = (int) a[1];
            test1 = f1;
        } else {
            t[0] = (int) a1[0];
            t[1] = (int) a1[1];
            test1 = f2;
        }
        double f3 = testPeak(second1, b1, dIntensityValues, meStart, iWindowsize);
        if (f3 > test1) {
            t[0] = (int) b1[0];
            t[1] = (int) b1[1];
            test1 = f3;
        }
        c1[0] = -b[0];
        c1[1] = a[1];
        double f4 = testPeak(second1, c1, dIntensityValues, meStart, iWindowsize);
        if (f4 > test1) {
            t[0] = (int) c1[0];
            t[1] = (int) c1[1];
            test1 = f4;
        }
        this.dXcorrCoeff = test1;
        this.meDisplacment.i = t[0];
        this.meDisplacment.j = t[1];
//        return t;
        int ph = 0;
    }

    public static double testPeak(int[][] second1, double[] a, double[][] dIntensityValues, MatrixEntry meStart, int iWindowsize) {
        double f = 0;
        if (meStart.i + (int) a[0] >= 0 && meStart.i + (int) a[0] < second1.length - iWindowsize && meStart.j + (int) a[1] >= 0 && meStart.j + (int) a[1] < second1[0].length - iWindowsize) {
//            InterrogationArea sSub2 = new InterrogationArea( second1,  sSubfirst.ostartPoint.i + (int) a[0],  sSubfirst.ostartPoint.j + (int) a[1], sSubfirst.iWindowsize);
            double[][] fromsecond = new double[iWindowsize][iWindowsize];
            for (int i = 0; i < iWindowsize; i++) {
                for (int j = 0; j < iWindowsize; j++) {
                    fromsecond[i][j] = second1[i + meStart.i + (int) a[0]][j + meStart.j + (int) a[1]];
                }
            }
            f = xcorr(dIntensityValues, fromsecond);
            if (Double.isNaN(f)) {
                f = 0;
            }
        }
        return f;
    }

    public static double calccoeff(MatrixEntry a, double[][] dIntensityValues1, int[][] isecondIm, MatrixEntry meStart, int iWindowsize) {
        double[][] fromsecond = new double[iWindowsize][iWindowsize];
        for (int i = 0; i < iWindowsize; i++) {
            for (int j = 0; j < iWindowsize; j++) {
                fromsecond[i][j] = isecondIm[i + meStart.i + a.i][j + meStart.j + a.j];
            }
        }
        double f = xcorr(dIntensityValues1, fromsecond);
        return f;
    }

    public static double[] calcMatrix(MatrixEntry a, double[][] dIntensityValues1, int[][] isecondIm, MatrixEntry meStart, int iWindowsize) {
        double[] neigh = new double[4];
        if (a.j != 0) {
            MatrixEntry a1 = new MatrixEntry(a.i, a.j - 1);
            neigh[0] = calccoeff(a1, dIntensityValues1, isecondIm, meStart, iWindowsize);
            MatrixEntry a2 = new MatrixEntry(a.i, a.j + 1);
            neigh[1] = calccoeff(a2, dIntensityValues1, isecondIm, meStart, iWindowsize);
        }
        if (a.i != 0) {
            MatrixEntry a3 = new MatrixEntry(a.i - 1, a.j);
            neigh[3] = calccoeff(a3, dIntensityValues1, isecondIm, meStart, iWindowsize);
            MatrixEntry a4 = new MatrixEntry(a.i + 1, a.j);
            neigh[4] = calccoeff(a4, dIntensityValues1, isecondIm, meStart, iWindowsize);
        }
        return neigh;
    }

//    public static int[] doFFTbigImage(int[][] ifirstIm, int[][] isecondIm, MatrixEntry meStartfirst, MatrixEntry meStartsecond, int iWindowsize, int[][] iMaskfirst, int[][] iMasksecond, boolean bMask) {
//
//        double[][] f = transfforCPbig(ifirstIm, meStartfirst, iWindowsize, iMaskfirst, bMask);
//        double[][] s = transfforCPbig(isecondIm, meStartsecond, iWindowsize, iMasksecond, bMask);
//        if (bMask) {
//            return new int[2];
//        }
//        double[][] f1 = new double[f.length][];
//        double[][] s1 = new double[s.length][];
//        for (int k = 0; k < f.length; k++) {
//            double[] aMatrix = f[k];
//            double[] bMatrix = s[k];
//            int aLength = aMatrix.length;
//            f1[k] = new double[aLength];
//            System.arraycopy(aMatrix, 0, f1[k], 0, aLength);
//            s1[k] = new double[aLength];
//            System.arraycopy(bMatrix, 0, s1[k], 0, aLength);
//        }
//        double[] isecCon = doFFT(f, s, 0);
//        double[] ifirstCon = doFFT(f1, s1, 1);
//        int[] iVec = peakLocbig(isecCon, ifirstCon, ifirstIm, isecondIm, meStartfirst, meStartsecond, iWindowsize);
//        return iVec;
//    }
//    public static double[][] transfforCPbig(int[][] a, MatrixEntry meStartfirst, int iWindowsize, int[][] iMaskfirst, boolean bMask) {
//        double[][] b = new double[iWindowsize][iWindowsize * 2];
//        outer:
//        for (int i = 0; i < iWindowsize; i++) {
//            for (int j = 0; j < iWindowsize; j++) {
//                if ((j * 2) % 2 == 0) {
//                    b[i][2 * j] = a[i + meStartfirst.i][j + meStartfirst.j];
//                    if (iMaskfirst[i + meStartfirst.i][j + meStartfirst.j] != 255) {
//                        bMask = true;
//                        break outer;
//                    }
//                }
//
//            }
//        }
//        return b;
//    }
//
//    public static int[] peakLocbig(double[] a, double[] b, int[][] ifirstIm, int[][] isecondIm, MatrixEntry meStartfirst, MatrixEntry meStartsecond, int iWindowsize) {
//
//        double[] a1 = new double[2];
//        double[] b1 = new double[2];
//        double[] c1 = new double[2];
//        a1[0] = -b[0];
//        a1[1] = -b[1];
//        b1[0] = a[0];
//        b1[1] = -b[1];
//        int[] t = new int[2];
//        double test1 = 0;
//        double f1 = testPeakbig(a, ifirstIm, isecondIm, meStartfirst, meStartsecond, iWindowsize);
//        double f2 = testPeakbig(a1, ifirstIm, isecondIm, meStartfirst, meStartsecond, iWindowsize);
//        if (f1 > f2) {
//            t[0] = (int) a[0];
//            t[1] = (int) a[1];
//            test1 = f1;
//        } else {
//            t[0] = (int) a1[0];
//            t[1] = (int) a1[1];
//            test1 = f2;
//        }
//        double f3 = testPeakbig(b1, ifirstIm, isecondIm, meStartfirst, meStartsecond, iWindowsize);
//        if (f3 > test1) {
//            t[0] = (int) b1[0];
//            t[1] = (int) b1[1];
//            test1 = f3;
//        }
//        c1[0] = -b[0];
//        c1[1] = a[1];
//        double f4 = testPeakbig(c1, ifirstIm, isecondIm, meStartfirst, meStartsecond, iWindowsize);
//        if (f4 > test1) {
//            t[0] = (int) c1[0];
//            t[1] = (int) c1[1];
//            test1 = f4;
//        }
//
//        return t;
//    }
//
//    public static double testPeakbig(double[] a, int[][] ifirstIm, int[][] isecondIm, MatrixEntry meStartfirst, MatrixEntry meStartsecond, int iWindowsize) {
//        double f = 0;
//        if (meStartfirst.i + (int) a[0] >= 0 && meStartfirst.i + (int) a[0] < ifirstIm.length - iWindowsize && meStartfirst.j + (int) a[1] >= 0 && meStartfirst.j + (int) a[1] < ifirstIm[0].length - iWindowsize) {
//            f = xcorrbig(ifirstIm, isecondIm, meStartfirst, meStartsecond, iWindowsize);
//            if (Double.isNaN(f)) {
//                f = 0;
//            }
//        }
//        return f;
//    }
//
//    public static double xcorrbig(int[][] ifirstIm, int[][] isecondIm, MatrixEntry meStartfirst, MatrixEntry meStartsecond, int iWindowsize) {
//        // Cross-Correlation  
//        double xcorrCoeff, norm;
//        double sum = 0;
//        double suma = 0;
//        double sumb = 0;
//        for (int ii = 0; ii < iWindowsize; ii++) {
//            for (int jj = 0; jj < iWindowsize; jj++) {
//                sum += ifirstIm[ii + meStartfirst.i][jj + meStartfirst.j] * isecondIm[ii + meStartsecond.i][jj + meStartsecond.j];
//                suma += ifirstIm[ii + meStartfirst.i][jj + meStartfirst.j] * ifirstIm[ii + meStartfirst.i][jj + meStartfirst.j];
//                sumb += isecondIm[ii + meStartsecond.i][jj + meStartsecond.j] * isecondIm[ii + meStartsecond.i][jj + meStartsecond.j];
//            }
//        }
//        norm = Math.sqrt(suma * sumb);
//        xcorrCoeff = sum / norm;
//        return xcorrCoeff;
//    }
//
//    public static double calccoeffbig(int[] a, int[][] ifirstIm, int[][] isecondIm, MatrixEntry meStartfirst, MatrixEntry meStartsecond, int iWindowsize) {
//        meStartsecond.i = meStartsecond.i + a[0];
//        meStartsecond.j = meStartsecond.j + a[1];
//        double f = xcorrbig(ifirstIm, isecondIm, meStartfirst, meStartsecond, iWindowsize);
//        return f;
//    }
}
