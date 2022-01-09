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
package com.tivconsultancy.opentiv.imageproc.algorithms.algorithms;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.contours.BasicOperations;
import com.tivconsultancy.opentiv.imageproc.contours.CPX;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
    
        public static ImageInt WhiteCut(ImageInt oGrid, double dThres) {
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                if (oGrid.iaPixels[i][j] > dThres) {
                    oGrid.iaPixels[i][j] = 255;
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
                for (MatrixEntry me : lme) {
                    avg = avg + preproc.getValue(me);
                    weight = weight + 1.0;
                }
                if (weight == 0.0) {
                    continue;
                }
                avg = avg / weight;
                if (preproc.iaPixels[i][j] >= avg) {
                    threshold.iaPixels[i][j] = 255;
                }

            }
        }

        return threshold;
    }

    public static ImageInt rotate(ImageInt img, double angle) {
        BufferedImage buffImg = img.getBuffImage();
        int w = buffImg.getWidth();
        int h = buffImg.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, buffImg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w / 2, h / 2);
        graphic.drawImage(buffImg, null, 0, 0);
        graphic.dispose();
        return new ImageInt(rotated);
    }

    public static interface ThresholdOperation {

        public int isBelow(int[][] iaInput, int i, int j);

        public int isAbove(int[][] iaInput, int i, int j);
    }

}
