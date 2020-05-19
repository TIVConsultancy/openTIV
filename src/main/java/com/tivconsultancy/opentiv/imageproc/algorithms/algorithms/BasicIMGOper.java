/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package com.tivconsultancy.opentiv.imageproc.algorithms.algorithms;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.contours.BasicOperations;
import com.tivconsultancy.opentiv.imageproc.contours.CPX;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class BasicIMGOper {

    public static ImageGrid threshold(ImageGrid oGrid, double dThres) {
        for (ImagePoint op : oGrid.oa) {
            if (op.iValue > dThres) {
                op.iValue = 255;
            } else {
                op.iValue = 0;
            }
        }
        return oGrid;
    }

    public static ImageInt threshold(ImageInt oGrid, double dThres) {
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                if (oGrid.iaPixels[i][j] > dThres) {
                    oGrid.iaPixels[i][j] = 255;
                } else {
                    oGrid.iaPixels[i][j] = 0;
                }
            }
        }
        return oGrid;
    }

    public static void threshold2(ImageInt oGrid, double dThres, ThresholdOperation o) {
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                if (oGrid.iaPixels[i][j] > dThres) {
                    oGrid.iaPixels[i][j] = o.isAbove(oGrid.iaPixels, i, j);
                } else {
                    oGrid.iaPixels[i][j] = o.isBelow(oGrid.iaPixels, i, j);
                }
            }
        }
    }

    public static ImageInt doubleThreshold(ImageInt oGrid, double dThresLow, double dThresHigh) {
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                if (oGrid.iaPixels[i][j] > dThresLow) {
                    if (oGrid.iaPixels[i][j] > dThresHigh) {
                        oGrid.iaPixels[i][j] = 255;
                    } else {
                        oGrid.iaPixels[i][j] = 127;
                    }
                } else {

                    oGrid.iaPixels[i][j] = 0;
                }
            }
        }
        return oGrid;
    }

    public static ImageInt invert(ImageInt oInput) {
        for (int i = 0; i < oInput.iaPixels.length; i++) {
            for (int j = 0; j < oInput.iaPixels[0].length; j++) {
                int iNewValue;
                if (oInput.iaPixels[i][j] < 128) {
                    iNewValue = 127 + (128 - oInput.iaPixels[i][j]);
                } else {
                    iNewValue = 127 - (oInput.iaPixels[i][j] - 128);
                }
                oInput.iaPixels[i][j] = iNewValue;
            }
        }

        return oInput;
    }

    public static ImageInt hysteresis(ImageInt oInput, int thresholdLow, int thresholdHigh) {

        ImageInt imgReturn = new ImageInt(new int[oInput.iaPixels.length][oInput.iaPixels[0].length]);

        ImageInt imgThreshold = threshold(oInput.clone(), thresholdLow);
        Ziegenhein_2018.thinoutEdges(imgThreshold);

        List<CPX> lo = BasicOperations.getAllContours(imgThreshold);
        for (CPX o : lo) {
            for (ImagePoint op : o.lo) {
                op.bMarker = false;
            }
        }

        for (CPX o : lo) {
            for (ImagePoint op : o.lo) {
                int iValue = oInput.getValue(MatrixEntry.valuOf(op.getPos()));
                if (!op.bMarker && iValue >= thresholdHigh) {
                    for (ImagePoint opSet : o.lo) {
                        opSet.bMarker = true;
                        imgReturn.setPoint(MatrixEntry.valuOf(opSet.getPos()), 255);
                    }
                    break;
                }
            }
        }

        return imgReturn;

    }

    public static ImageInt adaptiveAverage(ImageInt preproc, int iRadius) {

        ImageInt threshold = new ImageInt(preproc.iaPixels.length, preproc.iaPixels[0].length, 0);
        
        for (int i = 0; i < preproc.iaPixels.length; i++) {
            for (int j = 0; j < preproc.iaPixels[0].length; j++) {
                List<MatrixEntry> lme = preproc.getsubArea(i, j, iRadius);
                double avg = 0.0;
                double weight = 0.0;
                for(MatrixEntry me : lme){
                    avg = avg + preproc.getValue(me);
                    weight = weight + 1.0;
                }
                if(weight == 0.0){
                    continue;
                }
                avg = avg/weight;
                if(preproc.iaPixels[i][j] >= avg){
                    threshold.iaPixels[i][j] = 255;
                }
                
            }
        }

        return threshold;
    }

    public static interface ThresholdOperation {

        public int isBelow(int[][] iaInput, int i, int j);

        public int isAbove(int[][] iaInput, int i, int j);
    }

}
