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
import com.tivconsultancy.opentiv.helpfunctions.statistics.HistogramClass;
import com.tivconsultancy.opentiv.imageproc.img_properties.GrayHistogram;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.functions.Spline;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class HistogramOperations {

    public static ImageGrid equalize(ImageGrid oGrid, int iMaxValue) {
        GrayHistogram oHist = new GrayHistogram(oGrid);
        int g_min = oHist.getg_min();
        List<Double> oCumValue = oHist.getCumulativeValues((HistogramClass pParameter) -> 1.0 * pParameter.loContent.size());
        int iCount = 0;
        for (HistogramClass lo : oHist.loClasses) {
            for (Object o : lo.loContent) {
                ((ImagePoint) o).iValue = (int) (((oCumValue.get(iCount) - oCumValue.get(g_min)) / (oGrid.oa.length - oCumValue.get(g_min))) * iMaxValue);
            }
            iCount++;
        }
        return oGrid;
    }

    public static void equalize(ImageInt oGrid, int iMaxValue) {
        GrayHistogram oHist = new GrayHistogram(oGrid);
        int g_min = oHist.getg_min();
        List<Double> oCumValue = oHist.getCumulativeValues((HistogramClass pParameter) -> 1.0 * pParameter.loContent.size());
        int iCount = 0;
        for (HistogramClass lo : oHist.loClasses) {
            for (Object o : lo.loContent) {
                MatrixEntry me = ((MatrixEntry) o);
                oGrid.iaPixels[me.i][me.j] = (int) (((oCumValue.get(iCount) - oCumValue.get(g_min)) / ((oGrid.iaPixels.length * oGrid.iaPixels[0].length) - oCumValue.get(g_min))) * iMaxValue);
            }
            iCount++;
        }
    }

    public static ImageGrid Brightness(ImageGrid oGrid, int iValue) {
        for (ImagePoint o : oGrid.oa) {
            int iNewValue = o.iValue + iValue;
            o.iValue = iNewValue < 255 ? iNewValue : 255;
        }
        return oGrid;
    }

    public static void Brightness(ImageInt oGrid, int iValue) {
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                int iNewValue = oGrid.iaPixels[i][j] + iValue;
                if(iValue>0){
                    oGrid.iaPixels[i][j] = iNewValue < 255 ? iNewValue : 255;
                }else{
                    oGrid.iaPixels[i][j] = iNewValue < 0 ? 0 : iNewValue;
                }
                
            }
        }
    }

    public static ImageGrid Contrast(ImageGrid oGrid, int iBlackMin, int iWhiteMax) {
        for (ImagePoint o : oGrid.oa) {
            int iNewValue = (int) (Math.max(0, o.iValue - iBlackMin) * (255.0 / (255.0 - iBlackMin)));
            iNewValue = iNewValue > iWhiteMax ? iWhiteMax : iNewValue;
            iNewValue = (int) (iNewValue * 255.0 / iWhiteMax);
            o.iValue = iNewValue;
        }
        return oGrid;
    }

    public static void Contrast(ImageInt oGrid, int iBlackMin, int iWhiteMax) {
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                int iNewValue = (int) (Math.max(0, oGrid.iaPixels[i][j] - iBlackMin) * (255.0 / (255.0 - iBlackMin)));
                iNewValue = iNewValue > iWhiteMax ? iWhiteMax : iNewValue;
                iNewValue = (int) (iNewValue * 255.0 / iWhiteMax);
                oGrid.iaPixels[i][j] = iNewValue;
            }
        }
    }

    public static ImageGrid stretchBlack(ImageGrid oGrid, double dStretchFactor, GrayHistogram oHist) {
        Double dCut = 127.0;
        try {
            dCut = ((HistogramClass) (Sorting.getMaxCharacteristic(oHist.loClasses, new Sorting.Characteristic() {

                @Override
                public Double getCharacteristicValue(Object pParameter) {
                    return 1.0 * ((HistogramClass) pParameter).loContent.size();
                }
            }).o)).getCenter();
        } catch (EmptySetException ex) {
            Logger.getLogger(HistogramOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (ImagePoint o : oGrid.oa) {
            if (o.iValue < dCut) {
                o.iValue = (int) Math.max(0, o.iValue * (1.0 - dStretchFactor * (dCut - (double) o.iValue) / ((double) dCut))); // (int) (o.iValue * dStretchFactor * (1 - (iBlackThreshold - o.iValue)/(iBlackThreshold)));

            }
        }
        return oGrid;
    }

    public static void stretchBlack(ImageInt oGrid, double dStretchFactor, GrayHistogram oHist) {
        Double dCut = 127.0;
        try {
            dCut = ((HistogramClass) (Sorting.getMaxCharacteristic(oHist.loClasses, new Sorting.Characteristic() {

                @Override
                public Double getCharacteristicValue(Object pParameter) {
                    return 1.0 * ((HistogramClass) pParameter).loContent.size();
                }
            }).o)).getCenter();
        } catch (EmptySetException ex) {
            Logger.getLogger(HistogramOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                if (oGrid.iaPixels[i][j] < dCut) {
                    oGrid.iaPixels[i][j] = (int) Math.max(0, oGrid.iaPixels[i][j] * (1.0 - dStretchFactor * (dCut - (double) oGrid.iaPixels[i][j]) / ((double) dCut))); // (int) (o.iValue * dStretchFactor * (1 - (iBlackThreshold - o.iValue)/(iBlackThreshold)));

                }
            }
        }
    }

    public static ImageGrid stretchWhite(ImageGrid oGrid, double dStretchFactor, GrayHistogram oHist) {
        Double dCut = 127.0;
        try {
            dCut = ((HistogramClass) (Sorting.getMaxCharacteristic(oHist.loClasses, new Sorting.Characteristic() {

                @Override
                public Double getCharacteristicValue(Object pParameter) {
                    return 1.0 * ((HistogramClass) pParameter).loContent.size();
                }
            }).o)).getCenter();
        } catch (EmptySetException ex) {
            Logger.getLogger(HistogramOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (ImagePoint o : oGrid.oa) {
            if (o.iValue > dCut) {
                o.iValue = (int) Math.min(255, o.iValue * (1.0 + dStretchFactor * (((double) o.iValue - (double) dCut) / (255.0 - (double) dCut))));
            }
        }
        return oGrid;
    }

    public static void stretchWhite(ImageInt oGrid, double dStretchFactor, GrayHistogram oHist) {
        Double dCut = 127.0;
        try {
            dCut = ((HistogramClass) (Sorting.getMaxCharacteristic(oHist.loClasses, new Sorting.Characteristic() {

                @Override
                public Double getCharacteristicValue(Object pParameter) {
                    return 1.0 * ((HistogramClass) pParameter).loContent.size();
                }
            }).o)).getCenter();
        } catch (EmptySetException ex) {
            Logger.getLogger(HistogramOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                if (oGrid.iaPixels[i][j] > dCut) {
                    oGrid.iaPixels[i][j] = (int) Math.min(255, oGrid.iaPixels[i][j] * (1.0 + dStretchFactor * (((double) oGrid.iaPixels[i][j] - (double) dCut) / (255.0 - (double) dCut))));
                }
            }
        }
    }
    
    public static void curveCorrection(ImageInt oGrid, Spline spline){
        if(spline == null){
            spline = new Spline(new double[]{0, 127, 255}, new double[]{0, 127, 255});
        }
        
        for(int i = 0; i < oGrid.iaPixels.length; i++){
            for(int j = 0; j < oGrid.iaPixels[0].length; j++){
                oGrid.iaPixels[i][j] = (int) Math.max(Math.min(spline.getValue(1.0*oGrid.iaPixels[i][j]), 255), 0);
            }            
        }
    }

}
