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
package com.tivconsultancy.opentiv.preprocessor;

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.helpfunctions.strings.StringWorker;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.HistogramOperations;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.NoiseReduction;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Writer;
import com.tivconsultancy.opentiv.imageproc.img_properties.GrayHistogram;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.logging.TIVLog;
import com.tivconsultancy.opentiv.math.functions.Spline;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class OpenTIV_PreProc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public static ImageInt performTransformation(Settings oSettings, ImageInt Input) {
//        Logger log = setupLogger(oSettings);

        try {
            Input = Transformation(Input, oSettings);
        } catch (Exception e) {
            TIVLog.tivLogger.severe("------------------------------");
            TIVLog.tivLogger.severe("Transformation not succesfull");
            TIVLog.tivLogger.severe("------------------------------");
            TIVLog.tivLogger.severe(e.getLocalizedMessage());
            TIVLog.tivLogger.severe(e.getMessage());
        }
        return Input;
    }

    public static ImageInt performPreProc(Settings oSettings, ImageInt Input) {

        Logger log = setupLogger(oSettings);
        boolean bNoiseReduction = true; // (boolean) oSettings.getSettingsValue("NR");
        boolean bSmoothFilter = true; //(boolean) oSettings.getSettingsValue("SF");
        boolean bHG = true; //(boolean) oSettings.getSettingsValue("HG");

        try {
            if (bNoiseReduction) {
                Input = reducenoise(Input, oSettings);
            }
        } catch (Exception e) {
            TIVLog.tivLogger.severe("------------------------------");
            TIVLog.tivLogger.severe("noise reduction not succesfull");
            TIVLog.tivLogger.severe("------------------------------");
            TIVLog.tivLogger.severe(e.getLocalizedMessage());
            TIVLog.tivLogger.severe(e.getMessage());
        }

        try {
            if (bSmoothFilter) {
                Input = smoothing(Input, oSettings);
            }
        } catch (Exception e) {
            TIVLog.tivLogger.severe("------------------------");
            TIVLog.tivLogger.severe("smoothing not succesfull");
            TIVLog.tivLogger.severe("------------------------");
            TIVLog.tivLogger.severe(e.getLocalizedMessage());
            TIVLog.tivLogger.severe(e.getMessage());
        }

        try {
            if (bHG) {
                Input = Histogram(Input, oSettings);
            }
        } catch (Exception e) {
            TIVLog.tivLogger.severe("------------------------");
            TIVLog.tivLogger.severe("Histogram not succesfull");
            TIVLog.tivLogger.severe("------------------------");
            TIVLog.tivLogger.severe(e.getLocalizedMessage());
            TIVLog.tivLogger.severe(e.getMessage());
        }

        return Input;

    }

    public static Logger setupLogger(Settings oSettings) {
        Logger log = Logger.getLogger("openTIV_PreProc");
        log.setUseParentHandlers(false);
//        log.addHandler(new StreamHandler(System.err, new SimpleFormatter()));
        log.info("Entering PreProcessing");
        log.info("Settings: ");
        for (SettingObject o : oSettings.loSettings) {
            log.log(Level.INFO, "Name: " + o.getName() + "Value: " + o.sValue, new Object[]{o.getName(), o.sValue});
        }
        return log;
    }

    public static ImageInt Transformation(ImageInt oInput, Settings oSettings) {
        boolean bCutTop = ((boolean) oSettings.getSettingsValue("BcutyTop"));
        boolean bCutBottom = ((boolean) oSettings.getSettingsValue("BcutyBottom"));
        boolean bCutLeft = ((boolean) oSettings.getSettingsValue("BcutxLeft"));
        boolean bCutRight = ((boolean) oSettings.getSettingsValue("BcutxRight"));

        if (bCutTop) {
            int iCutTop = (int) oSettings.getSettingsValue("cutyTop");
            oInput = oInput.getsubY(iCutTop, oInput.iaPixels.length);
        }
        if (bCutBottom) {
            int iCutTop = (int) oSettings.getSettingsValue("cutyTop");
            int iCutBottom = (int) oSettings.getSettingsValue("cutyBottom");
            if ((iCutBottom - iCutTop) < oInput.iaPixels.length - 1) {
                oInput = oInput.getsubY(0, iCutBottom - iCutTop);
            }
        }
        if (bCutLeft) {
            int iCutLeft = (int) oSettings.getSettingsValue("cutxLeft");
            oInput = oInput.getsubX(iCutLeft, oInput.iaPixels[0].length);
        }
        if (bCutRight) {
            int iCutLeft = (int) oSettings.getSettingsValue("cutxLeft");
            int iCutRight = (int) oSettings.getSettingsValue("cutxRight");
            if ((iCutRight - iCutLeft) < oInput.iaPixels[0].length - 1) {
                oInput = oInput.getsubX(0, iCutRight - iCutLeft);
            }
        }

        return oInput;
    }

    public static ImageInt Histogram(ImageInt Input, Settings oSettings) {

        if (oSettings.getSettingsValue("HGBrightness") != null && (boolean) oSettings.getSettingsValue("HGBrightness")) {
            int iBrightness = (int) oSettings.getSettingsValue("Brightness");
            HistogramOperations.Brightness(Input, iBrightness);
        }

        if (oSettings.getSettingsValue("HGContrast") != null && (boolean) oSettings.getSettingsValue("HGContrast")) {
            int iBlackMin = (int) oSettings.getSettingsValue("BlackMin");
            int iWhiteMax = (int) oSettings.getSettingsValue("WhiteMax");
            HistogramOperations.Contrast(Input, iBlackMin, iWhiteMax);
        }

        if (oSettings.getSettingsValue("HGEqualize") != null && (boolean) oSettings.getSettingsValue("HGEqualize")) {
            int iWhiteMax = (int) oSettings.getSettingsValue("Equalize");
            HistogramOperations.equalize(Input, iWhiteMax);
        }

        if (oSettings.getSettingsValue("HGBlackStretch") != null && (boolean) oSettings.getSettingsValue("HGBlackStretch")) {
            GrayHistogram oHist = new GrayHistogram(Input);
            double BlackStretchFactor = (double) oSettings.getSettingsValue("BlackStretchFactor");
            HistogramOperations.stretchBlack(Input, BlackStretchFactor, oHist);
        }

        if (oSettings.getSettingsValue("HGWhiteStretch") != null && (boolean) oSettings.getSettingsValue("HGWhiteStretch")) {
            GrayHistogram oHist = new GrayHistogram(Input);
            double WhiteStretchFactor = (double) oSettings.getSettingsValue("WhiteStretchFactor");
            HistogramOperations.stretchWhite(Input, WhiteStretchFactor, oHist);
        }

        if (oSettings.getSettingsValue("CurveCorrection") != null && (boolean) oSettings.getSettingsValue("CurveCorrection")) {
            String yCSV = oSettings.getSettingsValue("GreyOldValues").toString();
            String xCSV = oSettings.getSettingsValue("GreyNewValues").toString();
            Spline interpolSpline = getSpline(yCSV, xCSV);
            HistogramOperations.curveCorrection(Input, interpolSpline);
        }
        
        if (oSettings.getSettingsValue("LinNormalization") != null && (boolean) oSettings.getSettingsValue("LinNormalization")) {
            HistogramOperations.linNormalization(Input);
        }
        
        if (oSettings.getSettingsValue("NonLinNormalization") != null && (boolean) oSettings.getSettingsValue("NonLinNormalization")) {
            HistogramOperations.NonlinNormalization(Input);
        }

        return Input;
    }

    public static Spline getSpline(String yCSV, String xCSV) {

        List<String> xValues = StringWorker.seperate(xCSV, ",");
        List<String> yValues = StringWorker.seperate(yCSV, ",");

        int iSize = Math.min(xValues.size(), yValues.size());

        double[] xSpline = new double[iSize];
        double[] ySpline = new double[iSize];

        for (int i = 0; i < iSize; i++) {
            xSpline[i] = Double.valueOf(xValues.get(i));
            ySpline[i] = Double.valueOf(yValues.get(i));
        }

        return new Spline(xSpline, ySpline);
    }

    public static ImageGrid reducenoise(ImageGrid Input, Settings oSettings) {
        List<Object> NRType = oSettings.getALLSettingsValues("NRType");
        for (Object s : NRType) {
            if (s.toString().equals("Simple1")) {
                if ((boolean) oSettings.getSettingsValue("NRSimple1")) {
                    int iValue = (int) oSettings.getSettingsValue("NRSimple1Threshold");
                    Input = NoiseReduction.simple1(Input, iValue);
                }
            }
        }
        return Input;
    }

    public static ImageInt reducenoise(ImageInt Input, Settings oSettings) {
        List<Object> NRType = oSettings.getALLSettingsValues("NRType");
        for (Object s : NRType) {
            if (s.toString().equals("Simple1")) {
                if ((boolean) oSettings.getSettingsValue("NRSimple1")) {
                    int iValue = (int) oSettings.getSettingsValue("NRSimple1Threshold");
                    NoiseReduction.simple1(Input, iValue);
                }
            }
        }
        return Input;
    }

    public static ImageGrid smoothing(ImageGrid Input, Settings oSettings) {
        List<Object> SFType = oSettings.getALLSettingsValues("SFType");
        for (Object s : SFType) {
            if (s.toString().equals("Gauss")) {
                if ((boolean) oSettings.getSettingsValue("SFGauss")) {
                    Input = NoiseReduction.Gauß(Input);
                }
            }
        }
        return Input;
    }

    public static ImageInt smoothing(ImageInt Input, Settings oSettings) {
        List<Object> SFType = oSettings.getALLSettingsValues("SFType");
        for (Object s : SFType) {
            if ((boolean) oSettings.getSettingsValue("SFGauss")) {
                Input = NoiseReduction.Gauß(Input);
            }

            if ((boolean) oSettings.getSettingsValue("SF5x5Gauss")) {
                Input = NoiseReduction.Gauß5x5(Input);
            }

            if ((boolean) oSettings.getSettingsValue("SF3x3Box")) {
                Input = NoiseReduction.Box3x3(Input);
            }

        }
        return Input;
    }

}
