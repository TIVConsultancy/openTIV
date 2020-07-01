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
package delete.com.tivconsultancy.opentiv.devgui.frames;


import com.tivconsultancy.opentiv.helpfunctions.hpc.Stopwatch;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.masking.data.SettingsMasking;
import com.tivconsultancy.opentiv.masking.main.OpenTIV_Masking;
import com.tivconsultancy.opentiv.preprocessor.OpenTIV_PreProc;
import com.tivconsultancy.opentiv.preprocessor.SettingsPreProc;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public final class Processes {

    public static ImageInt Transformation(File oFile, ImageInt oSource, SettingsPreProc oSet) {

        Stopwatch.addTimmer(new Throwable()
                .getStackTrace()[0]
                .getMethodName());

        if (oSource == null) {
            if (oFile == null) {
                Stopwatch.stop(new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
                return null;
            }
            try {
                oSource = new ImageInt(IMG_Reader.readImageGrayScale(oFile));
            } catch (IOException ex) {
                Logger.getLogger(Processes.class.getName()).log(Level.SEVERE, null, ex);
                Stopwatch.stop(new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
            }
        }

        try {
            ImageInt oReturn = OpenTIV_PreProc.performTransformation(oSet, oSource.clone());
            Stopwatch.stop(new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return oReturn;
        } catch (Exception ex) {
//            Logger.getLogger(TransFormIMG.class.getName()).log(Level.SEVERE, null, ex);
            Stopwatch.stop(new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return null;
        }
    }

    public static ImageInt PreProcessor(ImageInt Input, SettingsPreProc oSet) {

        Stopwatch.addTimmer(new Throwable()
                .getStackTrace()[0]
                .getMethodName());

        if (Input == null) {
            Stopwatch.stop(new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return null;
        }

        try {
            ImageInt oGrid = OpenTIV_PreProc.performPreProc(oSet, Input.clone());
            Stopwatch.stop(new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return oGrid;
        } catch (Exception ex) {
//            Logger.getLogger(PreProcIMGLoader.class.getName()).log(Level.SEVERE, null, ex);
            Stopwatch.stop(new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return null;
        }

    }

    public static ImageInt masking(ImageInt Input, int iNext, SettingsMasking oSet) {

        Stopwatch.addTimmer(new Throwable()
                .getStackTrace()[0]
                .getMethodName());

        if (Input == null) {
            Stopwatch.stop(new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return null;
        }

        try {
//            List<Feature> lo = Data.GUI_Data.Main.oPIVFrame.Mask.oSimpleMasking.getAllFeatures();
//            AllSettingsContainer.oSetMask.loShapes.clear();
//            for (Feature o : lo) {
//                if (o instanceof MaskingCircle) {
//                    AllSettingsContainer.oSetMask.loShapes.add(new SimpleShapes(((MaskingCircle) o).iPosX, ((MaskingCircle) o).iPosY, ((MaskingCircle) o).dNorm, ((MaskingCircle) o).dNormPot));
//                }
//                if (o instanceof MaskingRhombus) {
//                    AllSettingsContainer.oSetMask.loShapes.add(new SimpleShapes(((MaskingRhombus) o).iPosX, ((MaskingRhombus) o).iPosY, ((MaskingRhombus) o).dNorm, ((MaskingRhombus) o).dNormPot));
//                }
//                if (o instanceof MaskingSquare) {
//                    AllSettingsContainer.oSetMask.loShapes.add(new SimpleShapes(((MaskingSquare) o).iPosX, ((MaskingSquare) o).iPosY, ((MaskingSquare) o).dNorm, ((MaskingSquare) o).dNormPot));
//                }
//            }

            Stopwatch.addTimmer("ProcessMask");
            ImageInt oGrid = OpenTIV_Masking.performMasking(Input, oSet);
            Stopwatch.stop("ProcessMask");
            return oGrid;
//            try {
//                if (iNext < Data.GUI_Data.Main.oImageViewer.jListModelImages.getSize() && (boolean) AllSettingsContainer.oSetMask.getSettingsValue("Ziegenhein2018")) {
//                    ImagePath oNextPath = (ImagePath) Data.GUI_Data.Main.oImageViewer.jListModelImages.getElementAt(iNext);
//                    ImageInt oGridNext = Transformation(oNextPath.o, null);
//                    oGridNext = PreProcessor(oGridNext);
//                    oGridNext = opentiv_masking.OpenTIV_Masking.performMasking(oGridNext, AllSettingsContainer.oSetMask);
//                    for (int i = 0; i < oGrid.iaPixels.length; i++) {
//                        for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
//                            if (oGridNext.baMarker[i][j]) {
//                                oGrid.baMarker[i][j] = true;
//                                oGrid.iaPixels[i][j] = 127;
//                            }
//                        }
//                    }
//                }
//                Stopwatch.stop(new Throwable()
//                        .getStackTrace()[0]
//                        .getMethodName());
//                return oGrid;
//            } catch (Exception ex) {
////                Logger.getLogger(MaskLoader.class.getName()).log(Level.SEVERE, null, ex);
//                Stopwatch.stop(new Throwable()
//                        .getStackTrace()[0]
//                        .getMethodName());
//                return null;
//            }
        } catch (Exception ex) {
//            Logger.getLogger(MaskLoader.class.getName()).log(Level.SEVERE, null, ex);
            Stopwatch.stop(new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return null;
        }

            

        }
        //    public static boolean updateHist(HistogramPane oBlackBoard, ImageGrid oSourceImage, File oFile) {
        //
        //        Stopwatch.addTimmer(new Throwable()
        //                .getStackTrace()[0]
        //                .getMethodName());
        //
        //        if (oSourceImage == null) {
        //            if (Data.PIVGUI_Data.Input == null || oFile != null) {
        //                try {
        //                    Data.PIVGUI_Data.Input = IMG_Reader.readImageGrey(oFile);
        //                } catch (IOException ex) {
        //                    Logger.getLogger(Processes.class.getName()).log(Level.SEVERE, null, ex);
        //                }
        //            }
        //            if (Data.PIVGUI_Data.Input != null) {
        //                oSourceImage = Data.PIVGUI_Data.Input;
        //            }
        //        }
        //
        //        if (oSourceImage != null) {
        //            oBlackBoard.setText("");
        //            oBlackBoard.updateStatus(oSourceImage);
        //            Stopwatch.stop(new Throwable()
        //                .getStackTrace()[0]
        //                .getMethodName());
        //            return true;
        //        } else {
        //            oBlackBoard.setForeground(Color.WHITE);
        //            oBlackBoard.setText("Error calculating histogram");
        //            Stopwatch.stop(new Throwable()
        //                .getStackTrace()[0]
        //                .getMethodName());
        //            return false;
        //        }
        //    }
//    public static boolean updateHist(HistogramPane oBlackBoard, ImageInt oSourceImage, File oFile) {
//
//        Stopwatch.addTimmer(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//
//        if (oSourceImage == null) {
//            if (Data.GUI_Data.Input == null || oFile != null) {
//                try {
//                    Data.GUI_Data.Input = new ImageInt(IMG_Reader.readImageGrayScale(oFile));
//                } catch (IOException ex) {
//                    Logger.getLogger(Processes.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            if (Data.GUI_Data.Input != null) {
//                oSourceImage = Data.GUI_Data.Input;
//            }
//        }
//
//        if (oSourceImage != null) {
//            oBlackBoard.setText("");
//            oBlackBoard.updateStatus(oSourceImage);
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return true;
//        } else {
//            oBlackBoard.setForeground(Color.WHITE);
//            oBlackBoard.setText("Error calculating histogram");
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return false;
//        }
//    }

//    public static InterrGrid getGrid(ImageInt oSourceImage, DataPIV oDataPIV) {
//        Stopwatch.addTimmer(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//
//        InterrGrid oReturn;
//
//        String sType = (String) AllSettingsContainer.oSetPIV.getSettingsValue("GridType");
//        if (sType.equals("Standard")) {
//            oReturn = divideIntoAreas(oSourceImage.iaPixels, oDataPIV);
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return oReturn;
//        } else if (sType.equals("50Overlap")) {
//            oReturn = divideIntoAreas50Overlap(oSourceImage.iaPixels, oDataPIV);
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return oReturn;
//        } else if (sType.equals("PIV_Ziegenhein2018")) {
//            oReturn = divideIntoAreas2(oSourceImage.iaPixels, oDataPIV);
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return oReturn;
//        } else {
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return null;
//        }
//    }

//    public static ImageInt checkPIVMasking(InterrGrid oPIVGrid, ImageInt oSourceImage, ImageInt oMask, DataPIV oDataPIV) {
//        Stopwatch.addTimmer(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//        if (oDataPIV.baMask != null) {
//            for (int i = 0; i < oDataPIV.baMask.length; i++) {
//                for (int j = 0; j < oDataPIV.baMask[0].length; j++) {
//                    oDataPIV.baMask[i][j] = false;
//                }
//            }
//        }
//        if (oMask != null) {
//            for (int i = 0; i < oMask.iaPixels.length; i++) {
//                for (int j = 0; j < oMask.iaPixels[0].length; j++) {
//                    if (oMask.iaPixels[i][j] < 255) {
//                        oMask.baMarker[i][j] = true;
//                    }
//                }
//            }
//
//            oDataPIV.baMask = oMask.baMarker;
//            oPIVGrid.checkMask(oDataPIV);
//        }
//        Stopwatch.stop(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//        return Data.GUI_Data.oPIVGrid.paintOnImageGrid(oSourceImage.clone(), 255);
//
//    }

//    public static boolean processPIV(ImageInt oSourceImage, ImageInt oMask) throws IOException {
//
//        Stopwatch.addTimmer(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//
//        if (oSourceImage == null) {
//            return false;
//        }
//
//        List<Feature> loProcessFeatures = Data.GUI_Data.Main.oPIVFrame.PIVProc.getProcessFeatures();
//        int iLeap = 1;
//
//        for (Feature o : loProcessFeatures) {
//            if (o instanceof Process_ImageStep) {
//                iLeap = ((Process_ImageStep) o).iLeap;
//            }
//        }
//
//        DataPIV oDataPIV = AllSettingsContainer.oSetPIV.setSettingsToPIV(new DataPIV());
//        int iNext = Data.GUI_Data.Main.oImageViewer.jListImages.getSelectedIndex() + iLeap;
//        if (iNext >= Data.GUI_Data.Main.oImageViewer.jListModelImages.getSize()) {
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return false;
//        }
//
//        ImagePath oNextPath = (ImagePath) Data.GUI_Data.Main.oImageViewer.jListModelImages.getElementAt(iNext);
//        ImageInt oGrid = Transformation(oNextPath.o, null);
//        oGrid = PreProcessor(oGrid);
//        oDataPIV.iaReadInFirst = Data.GUI_Data.PreProcEnd.iaPixels;
//        oDataPIV.iaReadInSecond = oGrid.iaPixels;
//        if (oMask != null) {
//            for (int i = 0; i < oSourceImage.iaPixels.length; i++) {
//                for (int j = 0; j < oSourceImage.iaPixels[0].length; j++) {
//                    if (oSourceImage.iaPixels[i][j] == 0) {
//                        oSourceImage.baMarker[i][j] = true;
//                    }
//                }
//            }
//            oDataPIV.baMask = oMask.baMarker;
//        } else {
//            oDataPIV.baMask = new boolean[oSourceImage.iaPixels.length][oSourceImage.iaPixels[0].length];
//            for (int i = 0; i < oSourceImage.iaPixels.length; i++) {
//                for (int j = 0; j < oSourceImage.iaPixels[0].length; j++) {
//                    oDataPIV.baMask[i][j] = false;
//                }
//            }
//        }
//        ZiegenheinHessenkemper2018.calcIntensityValues(oDataPIV);
//
//        /*
//         Calculate displacement
//         */
//        Data.GUI_Data.oPIVGrid.getFFT(oDataPIV);
//
//        /*
//         Improve result
//         */
//        Data.GUI_Data.oPIVGrid = posProc(Data.GUI_Data.oPIVGrid, oDataPIV);
//
//        try {
//            int iGrayValue = Data.GUI_Data.iGreyValueVec;
//            int[][] iaBackground = oSourceImage.iaPixels;
//            if ((boolean) AllSettingsContainer.oSetPIV.getSettingsValue("BlanckBackground")) {
//                int iBackGroundValue = (int) AllSettingsContainer.oSetPIV.getSettingsValue("BlanckBackgroundGrayValue2");
//                iaBackground = MatrixGenerator.createMatrix(oSourceImage.iaPixels.length, oSourceImage.iaPixels[0].length, iBackGroundValue);
//            }
//            List<Color> loColors = Colorbar.StartEndLinearColorBar.getCustom(iGrayValue, iGrayValue, iGrayValue, iGrayValue, iGrayValue, iGrayValue);
//            double dSize = Data.GUI_Data.oPIVGrid.getCellSize();
//            int iOffSet = 0;
//            if ("50Overlap".equals((String) AllSettingsContainer.oSetPIV.getSettingsValue("GridType"))) {
//                dSize = dSize / 2.0;
//                iOffSet = (int) (dSize / 2.0);
//            }
//            VelocityGrid oOutputGrid = new VelocityGrid(iOffSet, oSourceImage.iaPixels[0].length, oSourceImage.iaPixels.length, iOffSet, (int) (oSourceImage.iaPixels[0].length / dSize), (int) (oSourceImage.iaPixels.length / dSize));
//            if ("PIV_Ziegenhein2018".equals((String) AllSettingsContainer.oSetPIV.getSettingsValue("GridType"))) {
//                Colorbar oColBar = new Colorbar.StartEndLinearColorBar(0.0, 6.0, Colorbar.StartEndLinearColorBar.getColdToWarmRainbow2(), new ColorSpaceCIEELab(), (Colorbar.StartEndLinearColorBar.ColorOperation<Double>) (Double pParameter) -> pParameter);
//                Vector.SVG.paintVectors(Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sPictures + java.io.File.separator + "Vec.svg", Data.GUI_Data.oPIVGrid.getVectors(), oColBar, 5);
//                Data.GUI_Data.PIVVectors = new ImageInt(Data.GUI_Data.oPIVGrid.paintVecs(iaBackground, Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sPictures + java.io.File.separator + "Vec.png", Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sData + java.io.File.separator + "Vec.csv", loColors, oDataPIV));
//            } else {
//                Data.GUI_Data.PIVVectors = new ImageInt(Data.GUI_Data.oPIVGrid.paintVecs(iaBackground, Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sPictures + java.io.File.separator + "Vec.png", Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sData + java.io.File.separator + "Vec.csv", loColors, oOutputGrid, oDataPIV));
//            }
//
//        } catch (Exception ex) {
//            Logger log = Logger.getLogger("openTIV_GUI_PIV_Process");
//            log.log(Level.SEVERE, ex.getMessage());
//            Stopwatch.stop(new Throwable()
//                    .getStackTrace()[0]
//                    .getMethodName());
//            return false;
//        }
//        Stopwatch.stop(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//        return true;
//    }

//    public static boolean PIV_processAll(int iIndex, int iLeap) throws IOException {
//
//        DataPIV oDataPIV = AllSettingsContainer.oSetPIV.setSettingsToPIV(new DataPIV());
//
//        int iNext = iIndex + iLeap;
//        if (iNext >= Data.GUI_Data.Main.oImageViewer.jListModelImages.getSize()) {
//            return false;
//        }
//        //First Image
//        ImagePath oPath = (ImagePath) Data.GUI_Data.Main.oImageViewer.jListModelImages.getElementAt(iIndex);
//        ImageInt FirstIMG = Transformation(oPath.o, null);
//        FirstIMG = PreProcessor(FirstIMG);
//        ImageInt oMask = null;
//        oMask = masking(FirstIMG, iNext);
//        InterrGrid oPIVGrid = getGrid(FirstIMG, oDataPIV);
//        ImageInt IMGPIVGridMasked = checkPIVMasking(oPIVGrid, FirstIMG, oMask, oDataPIV);
//        oDataPIV.iaReadInFirst = FirstIMG.iaPixels;
//
//        //Second Image            
//        ImagePath oNextPath = (ImagePath) Data.GUI_Data.Main.oImageViewer.jListModelImages.getElementAt(iNext);
//        ImageInt oGrid = Transformation(oNextPath.o, null);
//        oGrid = PreProcessor(oGrid);
//        oDataPIV.iaReadInSecond = oGrid.iaPixels;
//
//        if (oMask != null) {
//            for (int i = 0; i < oMask.iaPixels.length; i++) {
//                for (int j = 0; j < oMask.iaPixels[0].length; j++) {
//                    if (oMask.iaPixels[i][j] == 0) {
//                        oMask.baMarker[i][j] = true;
//                    }
//                }
//            }
//            oDataPIV.baMask = oMask.baMarker;
//        } else {
//            oDataPIV.baMask = new boolean[FirstIMG.iaPixels.length][FirstIMG.iaPixels[0].length];
//            for (int i = 0; i < FirstIMG.iaPixels.length; i++) {
//                for (int j = 0; j < FirstIMG.iaPixels[0].length; j++) {
//                    oDataPIV.baMask[i][j] = false;
//                }
//            }
//        }
//
//        ZiegenheinHessenkemper2018.calcIntensityValues(oDataPIV);
//        /*
//         Calculate displacement
//         */
//        oPIVGrid.getFFT(oDataPIV);
//        /*
//         Improve result
//         */
//        oPIVGrid = posProc(oPIVGrid, oDataPIV);
//
//        try {
//            int iGrayValue = Data.GUI_Data.iGreyValueVec;
//            List<Color> loColors = Colorbar.StartEndLinearColorBar.getCustom(iGrayValue, iGrayValue, iGrayValue, iGrayValue, iGrayValue, iGrayValue);
//            double dSize = Data.GUI_Data.oPIVGrid.getCellSize();
//            if ("50Overlap".equals((String) AllSettingsContainer.oSetPIV.getSettingsValue("GridType"))) {
//                dSize = dSize / 2.0;
//            }
//            VelocityGrid oOutputGrid = new VelocityGrid(0, FirstIMG.iaPixels[0].length, FirstIMG.iaPixels.length, 0, (int) (FirstIMG.iaPixels[0].length / dSize), (int) (FirstIMG.iaPixels.length / dSize));
//            ImageInt oPIVVectors = new ImageInt(oPIVGrid.paintVecs(FirstIMG.iaPixels, Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sPictures + java.io.File.separator + oPath.o.getName().substring(0, oPath.o.getName().lastIndexOf(".")) + ".png", Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sData + java.io.File.separator + oPath.o.getName().substring(0, oPath.o.getName().lastIndexOf(".")) + ".csv", loColors, oOutputGrid, oDataPIV));
//        } catch (IOException ex) {
//            Logger.getLogger(Processes.class.getName()).log(Level.SEVERE, null, ex);
////            return false;
//        }
//
//        return true;
//    }
//
//    public static boolean CD_processAll(int iIndex) throws IOException {
//
//        int iNext = iIndex + 1;
//        if (iNext >= Data.GUI_Data.Main.oImageViewer.jListModelImages.getSize()) {
//            return false;
//        }
//        //First Image
//        ImagePath oPath = (ImagePath) Data.GUI_Data.Main.oImageViewer.jListModelImages.getElementAt(iIndex);
//        ImageInt FirstIMG = Transformation(oPath.o, null);
//        ImageInt PreProc = PreProcessor(FirstIMG);
//        ImageInt EdgeDetecting = Processes.EdgeDetecting(PreProc);
//        ImageInt ShapeFit = Processes.ShapeFitting(EdgeDetecting);
//        writeShapeFittingResult(ShapeFit, oPath);
//
//        return true;
//    }
//
//    public static ImageInt EdgeDetecting(ImageInt oSourceImage) {
//        Stopwatch.addTimmer(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//
//        ImageInt oReturn = OpenTIV_Edges.performEdgeDetecting(oSetEdges, oSourceImage);
//
//        oReturn = OpenTIV_Edges.performEdgeOperations(oSetEdges, oReturn, oSourceImage);
//
//        Stopwatch.stop(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//
//        return oReturn;
//    }
//
//    public static ImageInt ShapeFitting(ImageInt oSourceImage) {
//        Stopwatch.addTimmer(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//
//        returnCotnainer_EllipseFit o = OpenTIV_Edges.performShapeFitting(oSetEdges, oSourceImage);
//        Data.GUI_Data.loEllipseFit = o.loCircles;
//
//        Stopwatch.stop(new Throwable()
//                .getStackTrace()[0]
//                .getMethodName());
//
//        return o.oImage;
//    }
//
//    public static void writeShapeFittingResult(ImageInt ShapeFit, ImagePath oPath) throws IOException {
//        IMG_Writer.PaintGreyPNG(ShapeFit, new File(Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sPictures + java.io.File.separator + oPath.o.getName().substring(0, oPath.o.getName().lastIndexOf(".")) + ".png"));
//
//        List<String> lsOutput = new ArrayList<>();
//        lsOutput.add("x [Px], y [Px], Major Axis [Px], Minor Axis [Px], Orientation Angle [rad]");
//        for (Circle oFit : Data.GUI_Data.loEllipseFit) {
//            lsOutput.add(oFit.getOutputString());
//        }
//
//        Writer oWrite = new Writer(new File(Data.GUI_Data.sOUT + java.io.File.separator + Data.GUI_Data.sData + java.io.File.separator + oPath.o.getName().substring(0, oPath.o.getName().lastIndexOf(".")) + ".csv"));
//        oWrite.write(lsOutput);
//    }

}
