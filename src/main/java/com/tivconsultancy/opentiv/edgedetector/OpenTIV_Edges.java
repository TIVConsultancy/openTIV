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
package com.tivconsultancy.opentiv.edgedetector;


import com.tivconsultancy.opentiv.helpfunctions.statistics.Basics;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.BasicIMGOper;
import static com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EdgeDetections.getEdgesTechnobis;
import static com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EdgeDetections.getThinEdge;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EllipseDetection;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Morphology;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.NoiseReduction;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018;
import com.tivconsultancy.opentiv.imageproc.contours.BasicOperations;
import com.tivconsultancy.opentiv.imageproc.contours.CPX;
import com.tivconsultancy.opentiv.imageproc.contours.ContourSplitting;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.shapes.Circle;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class OpenTIV_Edges {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public static ImageInt performEdgeDetecting(SettingsEdges oSettings, ImageInt Input) {
        Logger log = setupLogger(oSettings);
        ImageInt oNew = null;

//        Stopwatch.addTimmer("EdgeDetecting");
        try {
            if ((boolean) oSettings.getSettingsValue("OuterEdges")) {
                oNew = Input.clone();
                oNew = getOuterEdges_1(oNew, oSettings);
            } else if ((boolean) oSettings.getSettingsValue("SimpleEdges")) {
                oNew = Input.clone();
                oNew = getEdges(oNew, oSettings);
            }

        } catch (Exception e) {
            log.severe("------------------------------");
            log.severe("Masking not succesfull");
            log.severe("------------------------------");
            log.severe(e.getLocalizedMessage());
            log.severe(e.getMessage());
        }

        return oNew;
    }

    public static ImageInt performEdgeOperations(SettingsEdges oSettings, ImageInt oEdgesInput, ImageInt SourceImage) {
                        
        
        Logger log = setupLogger(oSettings);
//        Stopwatch.addTimmer("EgdeOperations");
        ImageGrid oEdges = new ImageGrid(oEdgesInput.iaPixels);
        oEdges = Ziegenhein_2018.thinoutEdges(oEdges);
        try {
            List<CPX> allContours = BasicOperations.getAllContours(oEdges);
            List<String> loOrder = (List<String>) oSettings.getSettingsValue("ExecutionOrder");
            if (!loOrder.isEmpty()) {
                for (String s : loOrder) {
                    if (s.equals("SortOutSmallEdges")) {
                        if ((boolean) oSettings.getSettingsValue("SortOutSmallEdges")) {
                            allContours = sortoutSmallEdges(allContours, (Integer) oSettings.getSettingsValue("MinSize"));
                        }
                    }
                    if (s.equals("SortOutLargeEdges")) {
                        if ((boolean) oSettings.getSettingsValue("SortOutLargeEdges")) {
                            allContours = sortoutSmallEdges(allContours, (Integer) oSettings.getSettingsValue("MaxSize"));
                        }
                    }
                    if (s.equals("CloseOpenContours")) {
                        if ((boolean) oSettings.getSettingsValue("CloseOpenContours")) {
                            allContours = closeContours(allContours, oEdges.iLength, oEdges.jLength, (Integer) oSettings.getSettingsValue("DistanceCloseContours"));
                        }
                    }
                    if (s.equals("ConnectOpenContours")) {
                        if ((boolean) oSettings.getSettingsValue("CloseOpenContours")) {
                            allContours = connectContours(allContours, oEdges.iLength, oEdges.jLength, (Integer) oSettings.getSettingsValue("DistanceConnectContours"));
                        }
                    }
                    if (s.equals("RemoveOpenContours")) {
                        if ((boolean) oSettings.getSettingsValue("RemoveOpenContours")) {
                            allContours = removeOpenContours(allContours);
                        }
                    }
                    if (s.equals("RemoveClosedContours")) {
                        if ((boolean) oSettings.getSettingsValue("RemoveClosedContours")) {
                            allContours = removeClosedContours(allContours);
                        }
                    }
                    if (s.equals("SplitByCurv")) {
                        if ((boolean) oSettings.getSettingsValue("SplitByCurv")) {
                            allContours = splitByCurve(allContours, oEdges, Integer.valueOf(oSettings.getSettingsValue("OrderCurvature").toString()), Double.valueOf(oSettings.getSettingsValue("ThresCurvSplitting").toString()));
                        }
                    }
                    if (s.equals("RemoveWeakEdges")) {
                        if ((boolean) oSettings.getSettingsValue("RemoveWeakEdges")) {
                            allContours = RemoveWeakEdges(allContours, Integer.valueOf(oSettings.getSettingsValue("ThresWeakEdges").toString()), SourceImage);
                        }
                    }
                }
            } else {
                if ((boolean) oSettings.getSettingsValue("SortOutSmallEdges")) {
                    allContours = sortoutSmallEdges(allContours, (Integer) oSettings.getSettingsValue("MinSize"));
                }
                if ((boolean) oSettings.getSettingsValue("SortOutLargeEdges")) {
                    allContours = sortoutLargeEdges(allContours, (Integer) oSettings.getSettingsValue("MaxSize"));
                }
                if ((boolean) oSettings.getSettingsValue("CloseOpenContours")) {
                    allContours = closeContours(allContours, oEdges.iLength, oEdges.jLength, (Integer) oSettings.getSettingsValue("DistanceCloseContours"));
                }
                if ((boolean) oSettings.getSettingsValue("RemoveOpenContours")) {
                    allContours = removeOpenContours(allContours);
                }
                if ((boolean) oSettings.getSettingsValue("RemoveClosedContours")) {
                    allContours = removeClosedContours(allContours);
                }
                if ((boolean) oSettings.getSettingsValue("SplitByCurv")) {
                    allContours = splitByCurve(allContours, oEdges, Integer.valueOf(oSettings.getSettingsValue("OrderCurvature").toString()), Double.valueOf(oSettings.getSettingsValue("ThresCurvSplitting").toString()));
                }
                if ((boolean) oSettings.getSettingsValue("RemoveWeakEdges")) {
                    allContours = RemoveWeakEdges(allContours, Integer.valueOf(oSettings.getSettingsValue("ThresWeakEdges").toString()), SourceImage);
                }
            }

            oEdges.resetMarkers();
            Morphology.setNotMarkedPoints(oEdges, 0);
            CPX.setOnGrid(oEdges, allContours, 255);

        } catch (Exception e) {
            log.severe("------------------------------");
            log.severe("Edge Detection or Edge Operation not succesfull");
            log.severe("------------------------------");
            log.severe(e.getLocalizedMessage().toString());
            log.severe(e.getMessage().toString());
        }

//        try {
//            IMG_Writer.PaintGreyPNG(oEdges, new File("E:\\New folder\\Pics\\PICS_BubbleDetect\\EdgesAfterOperation.png"));
//        } catch (Exception e) {
//        }
        return new ImageInt(oEdges.getMatrix());
    }

    public static returnCotnainer_EllipseFit performShapeFitting(SettingsEdges oSettings, ImageInt oInput) {
        Logger log = setupLogger(oSettings);
        ImageGrid oEdges = new ImageGrid(oInput.iaPixels);
        ImageInt oBlackBoard = new ImageInt(oInput.iaPixels);
        List<Circle> loFit = new ArrayList<>();
        try {
            loFit = EllipseDetection.Ziegenhein_2019_GUI(oEdges, oSettings);
            for (Circle o : loFit) {
                oBlackBoard.setPoints(o.lmeCircle, 127);
            }

        } catch (Exception e) {
            log.severe("------------------------------");
            log.severe("Shape Fitting not succesfull");
            log.severe("------------------------------");
            log.severe(e.getLocalizedMessage().toString());
            log.severe(e.getMessage().toString());
        }
        return new returnCotnainer_EllipseFit(loFit, oBlackBoard);
    }

    public static List<CPX> splitByCurve(List<CPX> loInput, ImageGrid oEdges, int iOrder, Double dThresholdCurv) throws EmptySetException {
        loInput = ContourSplitting.splitByCurvature(iOrder, iOrder, dThresholdCurv, loInput);
        oEdges.resetMarkers();
        Morphology.setNotMarkedPoints(oEdges, 0);
        CPX.setOnGrid(oEdges, loInput, 255);
        return BasicOperations.getAllContours(oEdges);
    }

    public static ImageInt getOuterEdges_1(ImageInt oInput, SettingsEdges oSettings) throws IOException, EmptySetException {
        return getEdges(oInput, (SettingsEdges) oSettings.getSettingsValue("OuterEdgesThreshold"));
    }
    
    public static ImageInt getTechnobisEdges(ImageInt oInput, SettingsEdges oSettings) throws IOException, EmptySetException {
        return getEdgesTechnobis(oInput, (Integer) oSettings.getSettingsValue("OuterEdgesThreshold"));
    }
    
    public static ImageInt fillSurrounding(ImageInt oInput){
        ImageInt o = oInput.clone();
        (new Morphology()).markFillN4(o, 0, 0);
        (new Morphology()).markFillN4(o, o.iaPixels.length-1, 0);
        (new Morphology()).markFillN4(o, 0, o.iaPixels[0].length-1);
        (new Morphology()).markFillN4(o, o.iaPixels.length-1, o.iaPixels[0].length-1);        
        Morphology.setNotMarkedPoints(o, 255);
        return o;
    }

    public static ImageInt getEdges(ImageInt oInput, SettingsEdges oSettings) throws IOException, EmptySetException {
        int EdgeThreshold = (Integer) oSettings.getSettingsValue("SimpleEdgesThreshold");
        oInput.iaPixels = NoiseReduction.Gau(oInput.iaPixels);
        oInput.iaPixels = getThinEdge(oInput.iaPixels, Boolean.FALSE, null, null, 0);
        BasicIMGOper.threshold(oInput, EdgeThreshold);
        return oInput;
    }

    public static List<CPX> sortoutSmallEdges(List<CPX> loInput, int iMinSize) {
        List<CPX> loReturn = new ArrayList<>();
        for (CPX o : loInput) {
            if (o.lo.size() > iMinSize) {
                loReturn.add(o);
            }
        }
        return loReturn;
    }

//    public static List<CPX> sortoutWeakEdges(List<CPX> loInput, int iMinSize) {
//        List<CPX> loReturn = new ArrayList<>();
//        for (CPX o : loInput) {
//            if (o.lo.size() > iMinSize) {
//                loReturn.add(o);
//            }
//        }
//        return loReturn;
//    }
    public static List<CPX> sortoutLargeEdges(List<CPX> loInput, int iMaxSize) {
        List<CPX> loReturn = new ArrayList<>();
        for (CPX o : loInput) {
            if (o.lo.size() <= iMaxSize) {
                loReturn.add(o);
            }
        }
        return loReturn;
    }

    public static List<CPX> removeOpenContours(List<CPX> loInput) {
        List<CPX> loReturn = new ArrayList<>();
        for (CPX o : loInput) {
            if (o.isClosedContour()) {
                loReturn.add(o);
            }
        }
        return loReturn;
    }

    public static List<CPX> RemoveWeakEdges(List<CPX> loInput, int iEdgeThres, ImageInt oSource) {
        List<CPX> loReturn = new ArrayList<>();
        ImageGrid oSourceGrid = new ImageGrid(oSource.iaPixels);
        for (CPX o : loInput) {
            List<Double> ldEdgeStrength = o.getEdgeStrength(oSourceGrid);
            Double d50Edge = Basics.median(ldEdgeStrength);            
            if (d50Edge != null && d50Edge > iEdgeThres) {
                loReturn.add(o);
            }
        }
        return loReturn;
    }

    public static List<CPX> removeClosedContours(List<CPX> loInput) {
        List<CPX> loReturn = new ArrayList<>();
        for (CPX o : loInput) {
            if (!o.isClosedContour()) {
                loReturn.add(o);
            }
        }
        return loReturn;
    }

    public static List<CPX> closeContours(List<CPX> loInput, int iLength, int jLength, int iDist) throws EmptySetException {
        ImageGrid oBlackBGrid = new ImageGrid(iLength, jLength);
        CPX.setOnGrid(oBlackBGrid, loInput, 255);
        oBlackBGrid = Ziegenhein_2018.closeOpenContourLinear(oBlackBGrid, iDist);
        return BasicOperations.getAllContours(oBlackBGrid);
    }
    
    public static List<CPX> connectContours(List<CPX> loInput, int iLength, int jLength, int iDist) throws EmptySetException {
        ImageGrid oBlackBGrid = new ImageGrid(iLength, jLength);
        CPX.setOnGrid(oBlackBGrid, loInput, 255);
        oBlackBGrid = Ziegenhein_2018.connectContours(oBlackBGrid, iDist);
        return BasicOperations.getAllContours(oBlackBGrid);
    }

    public static Logger setupLogger(SettingsEdges oSettings) {
        Logger log = Logger.getLogger("openTIV_Edges");
        log.info("Entering masking process");
        log.info("Settings: ");
//        for (SettingObject o : oSettings.loSettings) {
//            log.log(Level.INFO, "Name: Value", new Object[]{o.getName(), o.sValue});
//        }
        return log;
    }

    public static class returnCotnainer_EllipseFit {

        public List<Circle> loCircles = new ArrayList<>();
        public ImageInt oImage = null;

        public returnCotnainer_EllipseFit(List<Circle> loCircles, ImageInt oImage) {
            this.loCircles = loCircles;
            this.oImage = oImage;
        }

    }

}
