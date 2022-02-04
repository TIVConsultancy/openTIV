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
package com.tivconsultancy.opentiv.masking.main;

import com.tivconsultancy.opentiv.helpfunctions.hpc.Stopwatch;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixGenerator;
import com.tivconsultancy.opentiv.helpfunctions.operations.Convolution;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.BasicIMGOper;
import static com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EdgeDetections.getThinEdge;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Morphology;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.NoiseReduction;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.masking.data.SettingsMasking;
import com.tivconsultancy.opentiv.masking.help.SimpleShapes;
import static com.tivconsultancy.opentiv.preprocessor.OpenTIV_PreProc.Histogram;
import com.tivconsultancy.opentiv.preprocessor.SettingsPreProc;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class OpenTIV_Masking {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public static ImageInt performMasking(ImageInt Input, Settings oSettings) {

        Logger log = setupLogger(oSettings);
        ImageInt oNew = null;

        Stopwatch.addTimmer("AutoShape");
        try {
            if (oSettings.getSettingsValue("Mask").toString().contains("Ziegenhein2018")) {
                oNew = Input.clone();
                oNew = getMask(oNew, oSettings);
            } else if (oSettings.getSettingsValue("Mask").toString().contains("Hessenkemper2018")) {
                oNew = Input.clone();
                oNew = getMask2(oNew, oSettings);
            }
        } catch (Exception e) {
            log.severe("------------------------------");
            log.severe("Masking not succesfull");
            log.severe("------------------------------");
            log.severe(e.getLocalizedMessage());
            log.severe(e.getMessage());
            e.printStackTrace();
        }
        Stopwatch.stop("AutoShape");

        // Do not delete, must be implemented later
//        Stopwatch.addTimmer("SimpleShapes");
//        try {
//            if (!oSettings.loShapes.isEmpty()) {
//                if (oNew == null) {
//                    oNew = new ImageInt(Input.iaPixels.length, Input.iaPixels[0].length, 255);
//                }
//                Stopwatch.addTimmer("setShape");
//                oNew = getSimpleShapes(oNew, oSettings);
//                Stopwatch.stop("setShape");
//            }
//        } catch (Exception e) {
//            log.severe("------------------------------");
//            log.severe("Masking not succesfull");
//            log.severe("------------------------------");
//            log.severe(e.getLocalizedMessage());
//            log.severe(e.getMessage());
//        }
//        Stopwatch.stop("SimpleShapes");
        return oNew;

    }

    public static Logger setupLogger(Settings oSettings) {
        Logger log = Logger.getLogger("openTIV_Masking");
        log.info("Entering masking process");
        log.info("Settings: ");
        for (SettingObject o : oSettings.loSettings) {
            log.log(Level.INFO, "Name: Value", new Object[]{o.getName(), o.sValue});
        }
        return log;
    }

    public static ImageInt getSimpleShapes(ImageInt oInput, SettingsMasking oSettings) {
//        ImageGrid oReturn = new ImageGrid(oInput.getMatrix());
//        for(ImagePoint o : oInput.oa){
//            oReturn.oa[o.i].bMarker = o.bMarker;
//        }
        for (SimpleShapes o : oSettings.loShapes) {
            if (o.dNorm == 0.0) {
                continue;
            }
            o.setOnGrid(oInput);
        }
        return oInput;
    }

    public static ImageInt getMask(ImageInt oInput, Settings oSettings) throws IOException {
//        ImageGrid o = IMG_Reader.readImageGrey(new File(sFile));

        ImageInt o = oInput.clone();
        SettingsPreProc oPreProcSettings = new SettingsPreProc();

        oPreProcSettings.setSettingsValue("HGBrightness", true);
        oPreProcSettings.setSettingsValue("Brightness", 80);
        oPreProcSettings.setSettingsValue("HGContrast", true);
        oPreProcSettings.setSettingsValue("BlackMin", 80);
        oPreProcSettings.setSettingsValue("WhiteMax", 180);

        o = Histogram(o, oPreProcSettings);
//        IMG_Writer.PaintGreyPNG(o, new File("E:\\Goattec\\PIVGUITest\\masking_pre.png"));

        int iEdgeThreshold = 50;
        int iSmallestStructure = 300;
//        boolean[][] baMask = new boolean[o.iaPixels.length][o.iaPixels[0].length];

        int[][] iaProcess = o.iaPixels;
        iaProcess = NoiseReduction.Gau(iaProcess);
        o = new ImageInt(getThinEdge(iaProcess, Boolean.FALSE, null, null, 0));
//        IMG_Writer.PaintGreyPNG(o, new File("E:\\Goattec\\PIVGUITest\\masking_thinEdge.png"));
        BasicIMGOper.threshold(o, iEdgeThreshold);
//        IMG_Writer.PaintGreyPNG(o, new File("E:\\Goattec\\PIVGUITest\\masking_thinEdgeAfterThres.png"));
        o.resetMarkers();
        Morphology.dilatation(o);
        Morphology.dilatation(o);
        Morphology.erosion(o);
//        IMG_Writer.PaintGreyPNG(o, new File("E:\\Goattec\\PIVGUITest\\masking_thinEdgeAfterMorph.png"));
        o.resetMarkers();
        (new Morphology()).markFillN4(o, 0, 0);
        (new Morphology()).markFillN4(o, o.iaPixels.length - 1, 0);
        (new Morphology()).markFillN4(o, 0, o.iaPixels[0].length - 1);
        (new Morphology()).markFillN4(o, o.iaPixels.length - 1, o.iaPixels[0].length - 1);
        Morphology.setNotMarkedPoints(o, 255);
//        IMG_Writer.PaintGreyPNG(o, new File("E:\\Goattec\\PIVGUITest\\masking.png"));
        boolean[][] boa = o.baMarker;
        int[][] iaBlackBoard = new int[boa.length][boa[0].length];

        for (int i = 0; i < iaBlackBoard.length; i++) {
            for (int j = 0; j < iaBlackBoard[0].length; j++) {
                if (boa[i][j]) {
                    iaBlackBoard[i][j] = 255;
                } else {
                    iaBlackBoard[i][j] = 0;
                }
            }
        }

        ImageInt oBlackBoard = new ImageInt(iaBlackBoard);

//        IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\Goattec\\PIVGUITest\\masking2.png"));
        MatrixEntry oStartFill = oBlackBoard.containsValue(0);
        while (oStartFill != null) {
            List<MatrixEntry> loStructure = (new Morphology()).markFillN4(oBlackBoard, oStartFill.i, oStartFill.j);
            loStructure.add(oStartFill);
            if (loStructure.size() < iSmallestStructure) {
                oBlackBoard.setPoints(loStructure, 255);
            }
            if (loStructure.size() >= iSmallestStructure) {
                oBlackBoard.setPoints(loStructure, 1);
//                IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\Goattec\\PIVGUITest\\masking3.png"));
            }
            oStartFill = oBlackBoard.containsValue(0);
        }

        for (int i = 0; i < oBlackBoard.iaPixels.length; i++) {
            for (int j = 0; j < oBlackBoard.iaPixels[0].length; j++) {
                if (oBlackBoard.iaPixels[i][j] == 1) {
                    oInput.iaPixels[i][j] = 0;
                    oBlackBoard.baMarker[i][j] = true;
                } else {
                    oBlackBoard.baMarker[i][j] = false;
                }
            }
        }

//        int iCounter = 0;
//        for (int i = 0; i < oBlackBoard.iLength; i++) {
//            for (int j = 0; j < oBlackBoard.jLength; j++) {
//                if (!baMask[i][j]) {
//                    baMask[i][j] = oBlackBoard.oa[iCounter].iValue < 127;
//                }
//                iCounter++;
//            }
//        }
//        IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\Goattec\\PIVGUITest\\maskingOut.png"));
        return oBlackBoard;

    }

    public static ImageInt getMask2(ImageInt oInput, Settings oSettings) {
        ImageInt iReturn = oInput.clone();
        int iThresh =  Integer.valueOf((String)oSettings.getSettingsValue("thresh"));
        int iErosion = Integer.valueOf((String)oSettings.getSettingsValue("ero"));
        int iDilation = Integer.valueOf((String)oSettings.getSettingsValue("dila"));
        iReturn = BasicIMGOper.WhiteCut(iReturn, iThresh);
        iReturn.iaPixels = Convolution.Convolution(iReturn.iaPixels, MatrixGenerator.get5x5NormalDistribution());
        iReturn.iaPixels = getMedian(iReturn.iaPixels, 5, 5);
        iReturn = BasicIMGOper.threshold(iReturn, iThresh);
        iReturn.iaPixels = getMedian(iReturn.iaPixels, 5, 5);
        iReturn = BasicIMGOper.threshold(iReturn, iThresh);
        iReturn.iaPixels = Convolution.Convolution(iReturn.iaPixels, MatrixGenerator.get5x5NormalDistribution());
        iReturn = BasicIMGOper.threshold(iReturn, iThresh);
        for (int i = 0; i < iErosion; i++) {
            Morphology.erosion(iReturn);
        }
        for (int i = 0; i < iDilation; i++) {
            Morphology.dilatation(iReturn);
        }
        return iReturn;
    }

    public static int[][] getMedian(int[][] iaInput, int iMedianSizeX, int iMedianSizeY) {

        int[][] iaReturn = new int[iaInput.length][iaInput[0].length];

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                int iMDown = i - iMedianSizeY;

                if (iMDown < 0) {
                    iMDown = 0;
                }

                int iMUp = i + iMedianSizeY;

                if (iMUp > iaInput.length) {
                    iMUp = iaInput.length;
                }

                int jMLeft = j - iMedianSizeX;

                if (jMLeft < 0) {
                    jMLeft = 0;
                }

                int jMRight = j + iMedianSizeX;

                if (jMRight > iaInput[0].length) {
                    jMRight = iaInput[0].length;
                }

                //List<Integer> liVicinity = new ArrayList<Integer>();
                int[] iaVicinity = new int[(iMUp - iMDown) * (jMRight - jMLeft)];

                int runner = 0;

                for (int iM = iMDown; iM < iMUp; iM++) {

                    for (int jM = jMLeft; jM < jMRight; jM++) {

                        iaVicinity[runner] = iaInput[iM][jM];
                        runner = runner + 1;

                    }

                }

                java.util.Arrays.sort(iaVicinity);

                iaReturn[i][j] = iaVicinity[(int) (((double) iaVicinity.length) / 2.0)];

            }

        }
        return iaReturn;
    }

}
