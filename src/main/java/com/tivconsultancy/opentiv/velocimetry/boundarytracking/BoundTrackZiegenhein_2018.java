/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.velocimetry.boundarytracking;

import com.tivconsultancy.opentiv.helpfunctions.colorspaces.ColorSpaceCIEELab;
import com.tivconsultancy.opentiv.helpfunctions.colorspaces.Colorbar;
import com.tivconsultancy.opentiv.helpfunctions.io.Reader;
import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EdgeDetections;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Morphology;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import com.tivconsultancy.opentiv.math.sets.Set2D;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition2;
import com.tivconsultancy.opentiv.math.interfaces.Value;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import com.tivconsultancy.opentiv.postproc.vector.PaintVectors;
import static com.tivconsultancy.opentiv.postproc.vector.SVG.paintVectors;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018.CNCP;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.shapes.Line2;
import com.tivconsultancy.opentiv.logging.TIVLog;
import com.tivconsultancy.opentiv.math.grids.RecOrtho2D;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.velocimetry.directtracking.Nearest;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class BoundTrackZiegenhein_2018 {

    //Input-Output
    public static List<String> Pictures = new ArrayList<>();

    //Data
    public static List<CPXTr> loOpen = new ArrayList<>();
    public static List<CPXTr> loClosed = new ArrayList<>();

    //HelpFunction
    public static ImageGrid oHelp = null;

    public static void perform() throws IOException, EmptySetException {

        Settings_BoundTrack oSettings = new Settings_BoundTrack();

        List<String> lsPWDIn = new ArrayList<>();
//        lsPWDIn.add("E:\\Work\\GentopVali\\N_01");
//        lsPWDIn.add("E:\\Work\\GentopVali\\N_04");
//        lsPWDIn.add("E:\\Work\\GentopVali\\N_07");
//        lsPWDIn.add("E:\\Work\\GentopVali\\N_09");
//        lsPWDIn.add("E:\\Work\\GentopVali\\N_025");
//        lsPWDIn.add("E:\\Work\\GentopVali\\N_09");
//        lsPWDIn.add("E:\\Work\\GentopVali\\S_06");
//        lsPWDIn.add("E:\\Work\\BubbleBoundaryVelo\\Tergitol\\0p5slpmin");
        lsPWDIn.add("C:\\TempData\\HZDR_BoundTrack\\1p0");

        List<String> lsPWDOut = new ArrayList<>();
//        lsPWDOut.add("E:\\Work\\openTIV\\BubbleTracking\\N_01");
//        lsPWDOut.add("E:\\Work\\openTIV\\BubbleTracking\\N_04");
//        lsPWDOut.add("E:\\Work\\openTIV\\BubbleTracking\\N_07");
//        lsPWDOut.add("E:\\Work\\openTIV\\BubbleTracking\\N_09");
//        lsPWDOut.add("E:\\Work\\openTIV\\BubbleTracking\\N_025");
//        lsPWDOut.add("E:\\Work\\openTIV\\BubbleTracking\\N_09");
//        lsPWDOut.add("E:\\Work\\openTIV\\BubbleTracking\\S_06");
        lsPWDOut.add("C:\\TempData\\HZDR_BoundTrack\\1p0\\ContourTracking");

        List<String> sPositions = new ArrayList<>();
//        sPositions.add("600mm");
//        sPositions.add("1200mm");
//        sPositions.add("1600mm");

        List<String> lsReadInFolders = new ArrayList<>();
        List<String> lsOutputFolders = new ArrayList<>();

//        for (int i = 0; i < lsPWDIn.size(); i++) {
//            for (String s : sPositions) {
//                lsReadInFolders.add(lsPWDIn.get(i) + "\\" + s + "\\blasengr\\1");
//                lsOutputFolders.add(lsPWDOut.get(i) + "\\" + s);
//            }
//        }
        lsReadInFolders.add("C:\\TempData\\HZDR_BoundTrack\\1p0");
        lsOutputFolders.add("C:\\TempData\\HZDR_BoundTrack\\1p0\\ContourTracking");
//        lsReadInFolders.add("E:\\Work\\BubbleBoundaryVelo\\Tergitol\\0p5slpmin");
//        lsOutputFolders.add("E:\\Work\\openTIV\\SharpBoundaryTracking\\0p5slpmin\\ContourTracking");
//        lsReadInFolders.add("E:\\Sync\\openTIV\\Projects\\06-2019_MicroBubbles\\Pictures");
//        lsOutputFolders.add("E:\\Sync\\openTIV\\Projects\\06-2019_MicroBubbles\\Debug");

//        Colorbar oColBar2 = new Colorbar.StartEndLinearColorBar(0.0, 40.0, Colorbar.StartEndLinearColorBar.getGrey(), new ColorSpaceCIEELab(), (Colorbar.StartEndLinearColorBar.ColorOperation<Double>) (Double pParameter) -> pParameter);
//        oColBar2.printTestbar(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Colorbar.png", 100);        
        for (int t = 0; (t < lsReadInFolders.size()); t++) {
            Pictures.clear();
            Pictures = new ArrayList<>();
            System.out.println(lsReadInFolders.get(t));
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            oSettings.setSettingsValue("sPWDIn", lsReadInFolders.get(t));
            oSettings.setSettingsValue("sPWDOut", lsOutputFolders.get(t));
            (new File(oSettings.getSettingsValue("sPWDOut") + java.io.File.separator + oSettings.getSettingsValue("sOutputFolder"))).mkdirs();

            getPictures(oSettings);
            int iCount = 0;

            for (int i = 0; i < Pictures.size() - 3; i = i + 3) {
                String sIn1 = Pictures.get(i);
                String sIn2 = Pictures.get(i + 1);
                List<CPXTr> loFrame1 = getTrackableCNCP(sIn1, 1, oSettings);
                if (loFrame1.isEmpty()) {
                    continue;
                }
                List<CPXTr> loFrame2 = getCNCP(sIn2, 2, oSettings);
                loFrame1 = getvalidCPXListFirst(loFrame1);
                loFrame2 = getvalidCPXListSecond(loFrame2);

                List<VelocityVec> oVelocityVectors = new ArrayList<>();
                for (CPXTr oContoursToTrack : loFrame1) {

                    Collection<CPXTr> lo = Sorting.getEntriesWithSameCharacteristic(oContoursToTrack, loFrame2, 1.0, (Sorting.Characteristic2<CPXTr>) (CPXTr pParameter, CPXTr pParameter2) -> {
                                                                                if (pParameter.getNorm(pParameter2) < 40) {
                                                                                    return 1.0;
                                                                                }
                                                                                return 0.0;
                                                                            });

                    List<CPXTr> loSort = new ArrayList<>();
                    loSort.addAll(lo);

                    setHelpFunction(oSettings);
//        oHelp.setPoint( ((CPXTr) oVecSubPix.VelocityObject).lo, 255);
//            oHelp.setPoint(oContoursToTrack.lo, 127);
//        for(CPXTr o : loFrame1){
//            oHelp.setPoint(o.lo, 255);
//        }
//            for (CPXTr o : loSort) {
//                oHelp.setPoint(o.lo, 255);
//            }
//            Writer.PaintGreyPNG(oHelp, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Frame1Curv1.png"));

//            VelocityVec oVec = Nearest.getNearestForComplexObjectsParallel(oContoursToTrack, loSort, 2, new Set2D(new Set1D(-20, 20), new Set1D(-20, 20)), (SideCondition2) (Object pParameter1, Object pParameter2) -> ((CPXTr) pParameter1).getDistance((CPXTr) pParameter2) < 40);
                    VelocityVec oVec = getNearestForCPXTr(oContoursToTrack, loSort, 1, new Set2D(new Set1D(-20, 20), new Set1D(-20, -5)), 600, 600, (SideCondition2) (Object pParameter1, Object pParameter2) -> ((CPXTr) pParameter1).getDistance((CPXTr) pParameter2) < 40);
                    if (oVec == null) {
                        continue;
                    }
                    OrderedPair opSubPixDist = getSubPixelDist((CPXTr) oVec.VelocityObject1, (CPXTr) oVec.VelocityObject2);
                    VelocityVec oVecSubPix = (VelocityVec) oVec.add(opSubPixDist);
                    oVecSubPix.VelocityObject1 = oVec.VelocityObject1;
                    oVecSubPix.VelocityObject2 = oVec.VelocityObject2;
                    oVelocityVectors.add(oVecSubPix);
//            System.out.println(oVec);
//            System.out.println(oVec.opUnitTangent);
//
//            setHelpFunction(cutyBottom - cutyTop, cutxRight - cutxLeft);
//            oHelp.setPoint(((CPXTr) oVecSubPix.VelocityObject1).lo, 255);
//            oHelp.setPoint(oContoursToTrack.shift(new OrderedPair(oVec.getVelocityX(), oVec.getVelocityY(), 0.0)).lo, 127);
//        for(CPXTr o : loFrame1){
//            oHelp.setPoint(o.lo, 255);
//        }
//            for (CPXTr o : loSort) {
//                oHelp.setPoint(o.lo, 255);
//            }
//            Writer.PaintGreyPNG(oHelp, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Frame1Curv1_track.png"));

                }

                setHelpFunction(oSettings);
                for (CPXTr o : loFrame1) {
                    oHelp.setPoint(o.lo, 255);
                }

                String sPWDOut = (String) oSettings.getSettingsValue("sPWDOut");
                String sOutputFolder = (String) oSettings.getSettingsValue("sOutputFolder");

                List<Color> loColors = Colorbar.StartEndLinearColorBar.getColdToWarmRainbow2();
                Colorbar oColBar = new Colorbar.StartEndLinearColorBar(0.0, 40.0, loColors, new ColorSpaceCIEELab(), (Colorbar.StartEndLinearColorBar.ColorOperation<Double>) (Double pParameter) -> pParameter);
                PaintVectors.paintOnImage(oVelocityVectors, oColBar, oHelp.getMatrix(), sPWDOut + java.io.File.separator + sOutputFolder + java.io.File.separator + iCount + "_Vec.png", 2);
                paintVectors(sPWDOut + java.io.File.separator + sOutputFolder + java.io.File.separator + iCount + "Vec.svg", oVelocityVectors, oColBar, 2);
                VelocityVec.writeToFile(sPWDOut + java.io.File.separator + sOutputFolder + java.io.File.separator + iCount + "_vec.csv", oVelocityVectors, "X[Px], Y[Px], Z[Px], VelX [Px], VelY[Px], Size[Px]", new Value<VelocityVec>() {

                                    @Override
                                    public Double getValue(VelocityVec pParameter) {
                                        return 1.0 * ((CPXTr) pParameter.VelocityObject1).lo.size();
                                    }
                                });
                VelocityVec.writeToFile(sPWDOut + java.io.File.separator + sOutputFolder + java.io.File.separator + iCount + "_vec.ser", oVelocityVectors);

                iCount++;
//            if(iCount>2) break;
            }
        }
//        paintVectors(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Vec.svg", oVelocityVectors, oColBar, 2);

//        setHelpFunction(cutyBottom-cutyTop, cutxRight - cutxLeft);        
//        oHelp.setPoint( ((CPXTr) oVecSubPix.VelocityObject).lo, 255);
//        oHelp.setPoint(loFrame1.get(0).lo, 127);
//        for(CPXTr o : loFrame1){
//            oHelp.setPoint(o.lo, 255);
//        }
//        for(CPXTr o : loFrame2){
//            oHelp.setPoint(o.lo, 127);
//        }
//        Writer.PaintGreyPNG(oHelp, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Frame1Curv1.png"));        
    }

    public static ReturnContainerBoundaryTracking runBoundTrack(Settings oSettings, ImageGrid oEdges1, ImageGrid oEdges2) throws EmptySetException {
        List<CPXTr> loFrame1 = getTrackableCNCP(oEdges1, oSettings);
        if (loFrame1.isEmpty()) {
            return null;
        }
        List<CPXTr> loFrame2 = getCNCP(oEdges2);
        loFrame1 = getvalidCPXListFirst(loFrame1);
        loFrame2 = getvalidCPXListSecond(loFrame2);
        
        int searchYPlus = -1*Integer.valueOf(oSettings.getSettingsValue("BUBSRadiusYPlus").toString());
        int searchYMinus = -1*Integer.valueOf(oSettings.getSettingsValue("BUBSRadiusYMinus").toString());
        
        int searchXPlus = Integer.valueOf(oSettings.getSettingsValue("BUBSRadiusXPlus").toString());
        int searchXMinus = Integer.valueOf(oSettings.getSettingsValue("BUBSRadiusXMinus").toString());

        Map<CPXTr, VelocityVec> oVelocityVectors = new HashMap<>();
        for (CPXTr oContoursToTrack : loFrame1) {

            Collection<CPXTr> lo = Sorting.getEntriesWithSameCharacteristic(oContoursToTrack, loFrame2, 1.0, (Sorting.Characteristic2<CPXTr>) (CPXTr pParameter, CPXTr pParameter2) -> {
                                                                        if (pParameter.getNorm(pParameter2) < 40) {
                                                                            return 1.0;
                                                                        }
                                                                        return 0.0;
                                                                    });

            List<CPXTr> loSort = new ArrayList<>();
            loSort.addAll(lo);

            setHelpFunction(oEdges1.iLength, oEdges1.jLength);
            VelocityVec oVec = getNearestForCPXTr(oContoursToTrack, loSort, 1, new Set2D(new Set1D(searchXMinus, searchXPlus), new Set1D(searchYPlus, searchYMinus)), oEdges1.iLength, oEdges1.jLength, (SideCondition2) (Object pParameter1, Object pParameter2) -> ((CPXTr) pParameter1).getDistance((CPXTr) pParameter2) < 40);
            if (oVec == null) {
                continue;
            }
            OrderedPair opSubPixDist = getSubPixelDist((CPXTr) oVec.VelocityObject1, (CPXTr) oVec.VelocityObject2);
            VelocityVec oVecSubPix = (VelocityVec) oVec.add(opSubPixDist);
            oVecSubPix.VelocityObject1 = oVec.VelocityObject1;
            oVecSubPix.VelocityObject2 = oVec.VelocityObject2;
            oVelocityVectors.put(oContoursToTrack, oVecSubPix);
        }

        setHelpFunction(oEdges1.iLength, oEdges1.jLength);
        for (CPXTr o : loFrame1) {
            oHelp.setPoint(o.lo, 255);
        }
        
        ImageInt secContours = new ImageInt(oEdges2.iLength, oEdges2.jLength, 0);
        for(CPXTr c : loFrame2){
            secContours.setPointsIMGP(c.lo, 255);
        }

        return new ReturnContainerBoundaryTracking(oVelocityVectors, new ImageInt(oHelp.getMatrix()), secContours);

    }

    public static List<CPXTr> getvalidCPXListFirst(List<CPXTr> loIn) {
        List<CPXTr> lo = new ArrayList<>();
        for (CPXTr o : loIn) {
            if (o.oStart != null && !o.lo.isEmpty() && o.lo.size() > 20 && !o.isClosedContour()) {
                lo.add(o);
            }
        }
        return lo;
    }

    public static List<CPXTr> getvalidCPXListSecond(List<CPXTr> loIn) {
        List<CPXTr> lo = new ArrayList<>();
        for (CPXTr o : loIn) {
            if (o.oStart != null && !o.lo.isEmpty() && o.lo.size() > 20) {
                lo.add(o);
            }
        }
        return lo;
    }

    public static OrderedPair getSubPixelDist(CPXTr oSmaller, CPXTr oBigger) throws EmptySetException {
        List<ImagePoint> loNearest = new ArrayList<>();
        for (ImagePoint o : oSmaller.lo) {
            loNearest.add((ImagePoint) ((EnumObject) Sorting.getMinCharacteristic(oBigger.lo, o, new Sorting.Characteristic2<ImagePoint>() {

                                                                              @Override
                                                                              public Double getCharacteristicValue(ImagePoint pParameter, ImagePoint pParameter2) {
                                                                                  return pParameter.getPos().getNorm(pParameter2.getPos());
                                                                              }
                                                                          })).o);
        }

        Double dX = 0.0;
        Double dY = 0.0;
        int iCount = 0;
        for (int i = 0; i < oSmaller.lo.size(); i++) {
            ImagePoint o1 = oSmaller.lo.get(i);
            ImagePoint o2 = loNearest.get(i);

            dX = dX + o2.getPos().x - o1.getPos().x;
            dY = dY + o2.getPos().y - o1.getPos().y;
            iCount++;
        }
        if (iCount == 0) {
            return null;
        }
        return new OrderedPair(-1.0 * dX / (double) iCount, -1.0 * dY / (double) iCount);

    }

    public static List<CPXTr> getTrackableCNCP(ImageGrid oEdges, Settings oSettings) throws EmptySetException {
        int iCurvOrder = (int) oSettings.getSettingsValue("iCurvOrder");
        int iTangOrder = (int) oSettings.getSettingsValue("iTangOrder");
        double dCurvThresh = (double) oSettings.getSettingsValue("dCurvThresh");

        loClosed.clear();
        loOpen.clear();
//        IMG_Writer.PaintGreyPNG(oEdges, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\1p5slpmin\\Debug" + java.io.File.separator + "EdgesFirstFrame.png"));
        setHelpFunction(oEdges.iLength, oEdges.jLength);

        //get all open Edges
        HashSet<ImagePoint> loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        for (ImagePoint o : loStarts) {
            loOpen.add(new CPXTr(o));
        }

        ImageGrid oBlackBoard = new ImageGrid(oEdges.iLength, oEdges.jLength);
        for (CPXTr o : loOpen) {
            oBlackBoard.setPoint(o.lo, 127);
            oEdges.setPoint(o.lo, 0);
            oEdges.setPoint(o.oStart, 0);
        }

        oEdges.resetMarkers();
//        if(true) return loOpen;

        setHelpGrid(oEdges);
        //        
        HashSet loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);
        loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);

        loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        int iCount = 0;
        for (ImagePoint o : loStarts) {
            CPXTr oTemp = new CPXTr(o);
            loClosed.add(oTemp);
            oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
        }

        for (ImagePoint o : oEdges.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                CPXTr oTemp = new CPXTr(o);
                loClosed.add(oTemp);
                oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
                iCount++;
            }
        }

        oEdges.resetMarkers();

        List<ImagePoint> loBreakPointsCurv = new ArrayList<>();

        for (CPXTr o : loClosed) {
            if (o.lo.size() < 2 * iCurvOrder) {
                continue;
            }
            for (int i = 0; i < o.lo.size(); i++) {
//                if(Math.abs(o.getCurv(i, iCuvrOrder, iTangOrder)) > 1 || o.islocallyconvex(iConvOrder, i, oHelp) ){
                if (Math.abs(o.getCurv(i, iCurvOrder, iTangOrder)) > dCurvThresh) {
                    loBreakPointsCurv.add(o.lo.get(i));
                }
            }
        }

        oEdges.setPoint(loBreakPointsCurv, 0);
        loOpen.clear();
        loStarts.clear();

//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_BeforeClosing" + iIMG + ".png"));
        oEdges = connectOpenEdges(oEdges);
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_AfterClosing" + iIMG + ".png"));

        Ziegenhein_2018.thinoutEdges(oEdges);

        loStarts = Ziegenhein_2018.getStart(oEdges);
        for (ImagePoint o : loStarts) {
            loOpen.add(new CPXTr(o));
        }

        for (CPXTr o : loOpen) {
            if (o != null && o.lo.size() < 10) {
                oEdges.setPoint(o.oStart, 0);
                oEdges.setPoint(o.lo, 0);
            }
        }

//        Morphology.removeSinglePoints(oEdges);                
        loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        loClosed.clear();
        for (ImagePoint o : loStarts) {
            CPXTr oTemp = new CPXTr(o);
            loClosed.add(oTemp);
//            oEdges.setPoint(oTemp.lo, 127 + 0*iCount * 50);
        }

        for (ImagePoint o : oEdges.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                CPXTr oTemp = new CPXTr(o);
                loClosed.add(oTemp);
//                oEdges.setPoint(oTemp.lo, 255 + 0*iCount * 50);
            }
        }
//        oEdges.setPoint(loClosed.get(0).lo, 127 + 0 * iCount * 50);
//        oEdges.setPoint(loClosed.get(0).oStart, 127 + 0 * iCount * 50);
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_Final" + iIMG + ".png"));
        List<CPXTr> loReturn = new ArrayList<>();
        loReturn.addAll(loClosed);
        return loReturn;
    }

    public static List<CPXTr> getTrackableCNCP(String sFile, int iIMG, Settings_BoundTrack oSettings) throws IOException, EmptySetException {
        int EdgeThreshold = (int) oSettings.getSettingsValue("EdgeThreshold");
        int cutyTop = (int) oSettings.getSettingsValue("cutyTop");
        int cutyBottom = (int) oSettings.getSettingsValue("cutyBottom");
        int cutxLeft = (int) oSettings.getSettingsValue("cutxLeft");
        int cutxRight = (int) oSettings.getSettingsValue("cutxRight");
        int iCurvOrder = (int) oSettings.getSettingsValue("iCurvOrder");
        int iTangOrder = (int) oSettings.getSettingsValue("iTangOrder");
        double dCurvThresh = (double) oSettings.getSettingsValue("dCurvThresh");

        loClosed.clear();
        loOpen.clear();
        ImageGrid oEdges = EdgeDetections.getEdgesWithNoisReduction1(sFile, EdgeThreshold, cutyTop, cutyBottom, cutxLeft, cutxRight);
//        IMG_Writer.PaintGreyPNG(oEdges, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\1p5slpmin\\Debug" + java.io.File.separator + "EdgesFirstFrame.png"));
        setHelpFunction(oEdges.iLength, oEdges.jLength);

        //get all open Edges
        HashSet<ImagePoint> loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        for (ImagePoint o : loStarts) {
            loOpen.add(new CPXTr(o));
        }

        ImageGrid oBlackBoard = new ImageGrid(oEdges.iLength, oEdges.jLength);
        for (CPXTr o : loOpen) {
            oBlackBoard.setPoint(o.lo, 127);
            oEdges.setPoint(o.lo, 0);
            oEdges.setPoint(o.oStart, 0);
        }

        oEdges.resetMarkers();
//        if(true) return loOpen;

        setHelpGrid(oEdges);
        //        
        HashSet loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);
        loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);

        loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        int iCount = 0;
        for (ImagePoint o : loStarts) {
            CPXTr oTemp = new CPXTr(o);
            loClosed.add(oTemp);
            oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
        }

        for (ImagePoint o : oEdges.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                CPXTr oTemp = new CPXTr(o);
                loClosed.add(oTemp);
                oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
                iCount++;
            }
        }
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector.png"));        
//        fillAreas(oHelp, loClosed);
//        Writer.PaintGreyPNG(oHelp, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Area.png"));
        oEdges.resetMarkers();

        List<ImagePoint> loBreakPointsCurv = new ArrayList<>();

        for (CPXTr o : loClosed) {
            if (o.lo.size() < 2 * iCurvOrder) {
                continue;
            }
            for (int i = 0; i < o.lo.size(); i++) {
//                if(Math.abs(o.getCurv(i, iCuvrOrder, iTangOrder)) > 1 || o.islocallyconvex(iConvOrder, i, oHelp) ){
                if (Math.abs(o.getCurv(i, iCurvOrder, iTangOrder)) > dCurvThresh) {
                    loBreakPointsCurv.add(o.lo.get(i));
                }
            }
        }

        oEdges.setPoint(loBreakPointsCurv, 0);
        loOpen.clear();
        loStarts.clear();

//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_BeforeClosing" + iIMG + ".png"));
        oEdges = connectOpenEdges(oEdges);
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_AfterClosing" + iIMG + ".png"));

        Ziegenhein_2018.thinoutEdges(oEdges);

        loStarts = Ziegenhein_2018.getStart(oEdges);
        for (ImagePoint o : loStarts) {
            loOpen.add(new CPXTr(o));
        }

        for (CPXTr o : loOpen) {
            if (o != null && o.lo.size() < 10) {
                oEdges.setPoint(o.oStart, 0);
                oEdges.setPoint(o.lo, 0);
            }
        }

//        Morphology.removeSinglePoints(oEdges);                
        loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        loClosed.clear();
        for (ImagePoint o : loStarts) {
            CPXTr oTemp = new CPXTr(o);
            loClosed.add(oTemp);
//            oEdges.setPoint(oTemp.lo, 127 + 0*iCount * 50);
        }

        for (ImagePoint o : oEdges.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                CPXTr oTemp = new CPXTr(o);
                loClosed.add(oTemp);
//                oEdges.setPoint(oTemp.lo, 255 + 0*iCount * 50);
            }
        }
//        oEdges.setPoint(loClosed.get(0).lo, 127 + 0 * iCount * 50);
//        oEdges.setPoint(loClosed.get(0).oStart, 127 + 0 * iCount * 50);
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_Final" + iIMG + ".png"));
        List<CPXTr> loReturn = new ArrayList<>();
        loReturn.addAll(loClosed);
        return loReturn;
    }

    public static List<CPXTr> getCNCP(ImageGrid oEdges) throws EmptySetException {

        loClosed.clear();
        loOpen.clear();
//        IMG_Writer.PaintGreyPNG(oEdges, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\1p5slpmin\\Debug" + java.io.File.separator + "EdgesSecondFrame.png"));
        setHelpFunction(oEdges.iLength, oEdges.jLength);

        //get all open Edges
        HashSet<ImagePoint> loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        for (ImagePoint o : loStarts) {
            loOpen.add(new CPXTr(o));
        }

        ImageGrid oBlackBoard = new ImageGrid(oEdges.iLength, oEdges.jLength);
//        for (CPXTr o : loOpen) {
//            oBlackBoard.setPoint(o.lo, 127);
//            oEdges.setPoint(o.lo, 0);
//            oEdges.setPoint(o.oStart, 0);
//        }

        oEdges.resetMarkers();

//        if(true) return loOpen;
        setHelpGrid(oEdges);
        //        
        HashSet loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);
        loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);

        loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        int iCount = 0;
        for (ImagePoint o : loStarts) {
            CPXTr oTemp = new CPXTr(o);
            loClosed.add(oTemp);
            oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
        }

        for (ImagePoint o : oEdges.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                CPXTr oTemp = new CPXTr(o);
                loClosed.add(oTemp);
                oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
                iCount++;
            }
        }
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector.png"));        
//        fillAreas(oHelp, loClosed);
//        Writer.PaintGreyPNG(oHelp, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Area.png"));
        oEdges.resetMarkers();
        oEdges.setPoint(loClosed.get(0).lo, 255 + 0 * iCount * 50);
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_Final" + iIMG + ".png"));
        List<CPXTr> loReturn = new ArrayList<>();
        loReturn.addAll(loClosed);
        return loReturn;
    }

    public static List<CPXTr> getCNCP(String sFile, int iIMG, Settings_BoundTrack oSettings) throws IOException, EmptySetException {

        int EdgeThreshold = (int) oSettings.getSettingsValue("EdgeThreshold");
        int cutyTop = (int) oSettings.getSettingsValue("cutyTop");
        int cutyBottom = (int) oSettings.getSettingsValue("cutyBottom");
        int cutxLeft = (int) oSettings.getSettingsValue("cutxLeft");
        int cutxRight = (int) oSettings.getSettingsValue("cutxRight");

        loClosed.clear();
        loOpen.clear();
        ImageGrid oEdges = EdgeDetections.getEdgesWithNoisReduction1(sFile, EdgeThreshold, cutyTop, cutyBottom, cutxLeft, cutxRight);
//        IMG_Writer.PaintGreyPNG(oEdges, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\1p5slpmin\\Debug" + java.io.File.separator + "EdgesSecondFrame.png"));
        setHelpFunction(oEdges.iLength, oEdges.jLength);

        //get all open Edges
        HashSet<ImagePoint> loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        for (ImagePoint o : loStarts) {
            loOpen.add(new CPXTr(o));
        }

        ImageGrid oBlackBoard = new ImageGrid(oEdges.iLength, oEdges.jLength);
//        for (CPXTr o : loOpen) {
//            oBlackBoard.setPoint(o.lo, 127);
//            oEdges.setPoint(o.lo, 0);
//            oEdges.setPoint(o.oStart, 0);
//        }

        oEdges.resetMarkers();

//        if(true) return loOpen;
        setHelpGrid(oEdges);
        //        
        HashSet loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);
        loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);

        loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        int iCount = 0;
        for (ImagePoint o : loStarts) {
            CPXTr oTemp = new CPXTr(o);
            loClosed.add(oTemp);
            oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
        }

        for (ImagePoint o : oEdges.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                CPXTr oTemp = new CPXTr(o);
                loClosed.add(oTemp);
                oEdges.setPoint(oTemp.lo, 255 + 0 * iCount * 50);
                iCount++;
            }
        }
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector.png"));        
//        fillAreas(oHelp, loClosed);
//        Writer.PaintGreyPNG(oHelp, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "Area.png"));
        oEdges.resetMarkers();
        oEdges.setPoint(loClosed.get(0).lo, 255 + 0 * iCount * 50);
//        Writer.PaintGreyPNG(oEdges, new File(sPWD + java.io.File.separator + sDebugFolder + java.io.File.separator + "EdgeDetector_Final" + iIMG + ".png"));
        List<CPXTr> loReturn = new ArrayList<>();
        loReturn.addAll(loClosed);
        return loReturn;
    }

    public static ImageGrid connectOpenEdges(ImageGrid oClean) throws EmptySetException {

        oClean = Ziegenhein_2018.CNCPNetwork.OpenCNCP(oClean, new Ziegenhein_2018.RuleWithDoubleAction<Ziegenhein_2018.CNCP>() {

                                                  @Override
                                                  public List<Ziegenhein_2018.CNCP> isValid(Ziegenhein_2018.CNCP o, HashSet<Ziegenhein_2018.CNCP> lo) {

                                                      List<Ziegenhein_2018.CNCP> loReturn = new ArrayList<>();
                                                      try {
                                                          for (EnumObject oEnum : o.getclosest(lo, 10)) {
                                                              if (oEnum.dEnum > 1.5 && oEnum.o != null && ((CNCP) o).lo.size() > 10) {
                                                                  loReturn.add((Ziegenhein_2018.CNCP) oEnum.o);
                                                              }
                                                          }
                                                      } catch (EmptySetException ex) {
                                                          TIVLog.tivLogger.log(Level.SEVERE, null, ex);
                                                      }

                                                      return loReturn;

                                                  }

                                                  @Override
                                                  public void operate(Ziegenhein_2018.CNCP o1, List<Ziegenhein_2018.CNCP> lo2, ImageGrid oGrid) {
                                                      if (lo2.isEmpty()) {
                                                          return;
                                                      }
                                                      for (Ziegenhein_2018.CNCP o2 : lo2) {
                                                          if (o1.oStart == null || o1.oEnd == null || o2.oStart == null || o2.oEnd == null) {
                                                              continue;
                                                          }
                                                          List<EnumObject> loHelp = new ArrayList<>();
                                                          loHelp.add(new EnumObject(o1.oStart.getNorm(o2.oEnd), new Object[]{o1.oStart, o2.oEnd}));
                                                          loHelp.add(new EnumObject(o1.oStart.getNorm(o2.oStart), new Object[]{o1.oStart, o2.oStart}));
                                                          loHelp.add(new EnumObject(o1.oEnd.getNorm(o2.oEnd), new Object[]{o1.oEnd, o2.oEnd}));
                                                          loHelp.add(new EnumObject(o1.oEnd.getNorm(o2.oStart), new Object[]{o1.oEnd, o2.oStart}));
                                                          EnumObject oHelp = null;
                                                          try {
                                                              oHelp = Sorting.getMinCharacteristic(loHelp, (Sorting.Characteristic<EnumObject>) (EnumObject pParameter) -> {
                                                                                               return pParameter.dEnum;
                                                                                           });
                                                          } catch (EmptySetException ex) {
                                                              TIVLog.tivLogger.log(Level.SEVERE, null, ex);
                                                          }
                                                          if (oHelp != null && oHelp.o != null) {
                                                              oHelp = (EnumObject) oHelp.o;
                                                              ImagePoint oConnect1 = (ImagePoint) ((Object[]) oHelp.o)[0];
                                                              ImagePoint oConnect2 = (ImagePoint) ((Object[]) oHelp.o)[1];

                                                              OrderedPair opDeri1 = o1.getDerivationOutwardsEndPoints(oConnect1, 10);
                                                              OrderedPair opDeri2 = o2.getDerivationOutwardsEndPoints(oConnect2, 10);
                                                              if (opDeri1 == null || opDeri2 == null) {
                                                                  continue;
                                                              }
                                                              Double dSmoothness = (Math.abs(opDeri1.x + opDeri2.x) + Math.abs(opDeri1.y + opDeri2.y));
                                                              double dDist = oConnect1.getNorm(oConnect2);
                                                              double dDistDeri1 = opDeri1.getNorm(new OrderedPair(0.00, 0.0));
                                                              Double dDirectionalDistX1 = ((oConnect2.getPos().x - oConnect1.getPos().x) / dDist);
                                                              Double dDirectionalDistY1 = ((oConnect2.getPos().y - oConnect1.getPos().y) / dDist);
                                                              OrderedPair opDirecDist1 = new OrderedPair(dDirectionalDistX1 - (opDeri1.x / dDistDeri1), dDirectionalDistY1 - (opDeri1.y / dDistDeri1));
                                                              Double dDirecDist1 = opDirecDist1.getNorm(new OrderedPair(0.00, 0.0));

                                                              double dDistDeri2 = opDeri2.getNorm(new OrderedPair(0.00, 0.0));
                                                              Double dDirectionalDistX2 = ((oConnect1.getPos().x - oConnect2.getPos().x) / dDist);
                                                              Double dDirectionalDistY2 = ((oConnect1.getPos().y - oConnect2.getPos().y) / dDist);
                                                              OrderedPair opDirecDist2 = new OrderedPair(dDirectionalDistX2 - (opDeri2.x / dDistDeri2), dDirectionalDistY2 - (opDeri2.y / dDistDeri2));
                                                              Double dDirecDist2 = opDirecDist2.getNorm(new OrderedPair(0.00, 0.0));

//                        System.out.println("Ping");
                                                              if (dSmoothness <= 1.1 && (Math.min(dDirecDist1, dDirecDist2) * dDist) < 5) {
                                                                  Line2 oLine = new Line2(oConnect1, oConnect2);
                                                                  oLine.setLine(oGrid, 255);
                                                              }
//                        CNCP.connectCNCP(o1, o2, oConnect1, oConnect2, oGrid, 127);

                                                          }
                                                      }
                                                  }
                                              });
        return oClean;
    }

    public static void setHelpFunction(int iLength, int jLength) {
        oHelp = new ImageGrid(iLength, jLength);
    }

    public static void setHelpFunction(Settings_BoundTrack oSettings) {
        int iLength = ((int) oSettings.getSettingsValue("cutyBottom")) - ((int) oSettings.getSettingsValue("cutyTop"));
        int jLength = ((int) oSettings.getSettingsValue("cutxRight")) - ((int) oSettings.getSettingsValue("cutxLeft"));
        oHelp = new ImageGrid(iLength, jLength);
    }

    public static void setHelpGrid(ImageGrid oGrid) {
        for (ImagePoint o : oGrid.oa) {
            if (o.bMarker || o.iValue > 127) {
                oHelp.setPoint(o, 255);
            }
        }
    }

    public static void fillAreas(ImageGrid oGrid, List<CPXTr> lo) {
        for (CPXTr o : lo) {
            if (o.getFocalPoint() != null && o.lo.size() > 30) {
                (new Morphology()).markFillN4(oGrid, o.getFocalPoint());
            }
        }

        for (ImagePoint o : oGrid.oa) {
            if (o.bMarker) {
                oGrid.setPoint(o, 255);
            }
        }
    }

    public static VelocityVec getNearestForCPXTr(CPXTr otRef, List<CPXTr> loSecondFrame, double dStepSize, Set2D oSearchArea, int iLength, int jLength, SideCondition2 o) {

        List<Nearest.OPwithCont> lopDisplacementAndSum = new ArrayList<>();

        List<CPXTr> loSecondFrameInRadius = new ArrayList<>();

        for (CPXTr ot : loSecondFrame) {
            if (ot != null && o.check(otRef, ot)) {
                loSecondFrameInRadius.add(ot);
            }
        }

        if (loSecondFrameInRadius.isEmpty()) {
            return null;
        }

        Collection<OrderedPair> elems = new LinkedList<>();

        for (int i = (int) (oSearchArea.oIntervalY.dLeftBorder); i <= (int) (oSearchArea.oIntervalY.dRightBorder); i = i + (int) dStepSize) {
            for (int j = (int) (oSearchArea.oIntervalX.dLeftBorder); j <= (int) (oSearchArea.oIntervalX.dRightBorder); j = j + (int) dStepSize) {
                elems.add(new OrderedPair(j, i, 0.0));
            }
        }

//        long timeStart = Calendar.getInstance().getTimeInMillis();
//        double dTimeShift = 0.0;
        setHelpFunction(iLength, jLength);
        for (CPXTr oSecondFrame : loSecondFrameInRadius) {
            oHelp.setPoint(oSecondFrame.lo, 127);
        }
        for (OrderedPair pParameter : elems) {
            CPXTr oShiftRef = otRef.shift(pParameter);
            if (oShiftRef == null) {
                continue;
            }
            int iCountMatchPixel = 0;
            oHelp.addPoint(oShiftRef.lo, 127);
            for (ImagePoint oHelpPoint : oShiftRef.lo) {
                if (oHelpPoint.i < oHelp.oa.length && oHelp.oa[oHelpPoint.i].iValue > 130) {
                    iCountMatchPixel++;
                }
            }
            if (iCountMatchPixel < oShiftRef.lo.size() * 0.15) {
                oHelp.addPoint(oShiftRef.lo, -127);
                continue;
            }
            oHelp.addPoint(oShiftRef.lo, -127);
//            System.out.println(iCountMatchPixel++);
            Trackable oNearest = oShiftRef.getNearest(loSecondFrameInRadius);
            lopDisplacementAndSum.add(new Nearest.OPwithCont(pParameter.x, pParameter.y, oShiftRef.getDistance(oNearest), oShiftRef, oNearest));
        }
//        System.out.println("++++++++++++");
//        System.out.println((Calendar.getInstance().getTimeInMillis() - timeStart) / 1000.0);
//        System.out.println("------------");
//        System.out.println(dTimeShift / 1000.0);
//      double dMin = Sorting.getMinCharacteristic(lopDisplacementAndSum, new Sorting.Characteristic<OPwithCont>() {
//
//            @Override
//            public Double getCharacteristicValue(OPwithCont pParameter) {
//                return pParameter.dValue;
//            }
//        }).dEnum;
//  
//        
//        double dMax = Sorting.getMaxCharacteristic(lopDisplacementAndSum, new Sorting.Characteristic<OPwithCont>() {
//
//            @Override
//            public Double getCharacteristicValue(OPwithCont pParameter) {
//                return pParameter.dValue;
//            }
//        }).dEnum;
//        
//        ImageGrid oGrid = new ImageGrid(81, 81);
//        for(OPwithCont oOP : lopDisplacementAndSum){
//            OrderedPair opTemp = new OrderedPair(oOP.x + 40, oOP.y + 40, (oOP.dValue - dMin)/dMax * 255.0);            
//            oGrid.oa[oGrid.getIndex(opTemp)] = new ImagePoint(oGrid.getIndex(opTemp), opTemp.dValue.intValue(), oGrid);
//        }
//        
//        Writer.PaintGreyPNG(oGrid, new File("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Data\\BubbleCase1\\Debug\\Correlation.png"));

        Collections.sort(lopDisplacementAndSum, new Comparator<Nearest.OPwithCont>() {

                     @Override
                     public int compare(Nearest.OPwithCont o1, Nearest.OPwithCont o2) {
                         if (o1 == null && o2 == null) {
                             return 0;
                         }

                         if (o1 == null && o2 != null) {
                             return 1;
                         }

                         if (o1 != null && o2 == null) {
                             return -1;
                         }

                         if (o1.dValue < o2.dValue) {
                             return -1;
                         }

                         if (o1.dValue > o2.dValue) {
                             return 1;
                         }

                         return 0;
                     }
                 }
        );

        List<Nearest.OPwithCont> lopSimilarValue = new ArrayList<>();
        for (Nearest.OPwithCont op : lopDisplacementAndSum) {
            if (op != null && op.dValue == lopDisplacementAndSum.get(0).dValue) {
                lopSimilarValue.add(op);
            }
        }

        Collections.sort(lopSimilarValue, new Comparator<Nearest.OPwithCont>() {

                     @Override
                     public int compare(Nearest.OPwithCont o1, Nearest.OPwithCont o2) {
                         if (o1.getNorm(new OrderedPair(0.0, 0.0)) < o2.getNorm(new OrderedPair(0.0, 0.0))) {
                             return -1;
                         }

                         if (o1.getNorm(new OrderedPair(0.0, 0.0)) > o2.getNorm(new OrderedPair(0.0, 0.0))) {
                             return 1;
                         }

                         return 0;
                     }
                 });
        if (lopSimilarValue.isEmpty()) {
            return null;
        }

        VelocityVec oVelo = new VelocityVec(lopSimilarValue.get(0).x, lopSimilarValue.get(0).y, otRef.getPosition(), lopSimilarValue.get(0).oContent1, lopSimilarValue.get(0).oContent2);
//        oVelo.setQuality(lopSimilarValue.get(0).dValue);

        return oVelo;
    }

    public static void getPictures(Settings_BoundTrack oSettings) {
        String sPWDIn = (String) oSettings.getSettingsValue("sPWDIn");
        Pictures.clear();
        Pictures = new ArrayList<>();
        File[] of = new File(sPWDIn).listFiles();
        for (File f : of) {
            if (f.getPath().contains(".jpg")) {
                Pictures.add(f.getAbsolutePath());
            }
        }
    }

    public static void postproc() throws IOException {

        List<String> lsCases = new ArrayList<>();
        lsCases.add("N_01");
        lsCases.add("N_025");
        lsCases.add("N_04");
        lsCases.add("N_07");
        lsCases.add("N_09");
        lsCases.add("S_06");

        List<String> lsHeights = new ArrayList<>();

        lsHeights.add("600mm");
        lsHeights.add("1200mm");
        lsHeights.add("1600mm");

        for (String sCase : lsCases) {
            for (String sHeight : lsHeights) {
                String sPWDPost = "E:\\Work\\openTIV\\BubbleTracking\\" + sCase + "\\" + sHeight + "\\";
                Double dMetrik = 0.0;
                String sPWDSettigsObj = null;
                if ((new File("E:\\Work\\GentopVali\\" + sCase + "\\" + sHeight + "\\blasengr\\1\\BubFind_ByHand\\Settings.txt").exists())) {
                    sPWDSettigsObj = "E:\\Work\\GentopVali\\" + sCase + "\\" + sHeight + "\\blasengr\\1\\BubFind_ByHand\\Settings.txt";
                } else if (new File("E:\\Work\\GentopVali\\" + sCase + "\\" + sHeight + "\\blasengr\\1\\BubFind_ByHand_Thomas\\Settings.txt").exists()) {
                    sPWDSettigsObj = "E:\\Work\\GentopVali\\" + sCase + "\\" + sHeight + "\\blasengr\\1\\BubFind_ByHand_Thomas\\Settings.txt";
                } else {
                    System.out.println("No File Found for: " + sCase + sHeight);
                }
                Reader oReadSettings = new Reader(sPWDSettigsObj);
                List<String[]> lsa = oReadSettings.readBigFileInListStringSeperate();
                for (String[] sa : lsa) {
                    if (sa[0].equals("Metrik")) {
                        dMetrik = Double.valueOf(sa[1]);
                    }
                }
                File[] of = new File(sPWDPost + "Data").listFiles();
                List<String> lsVecsOut = new ArrayList<>();
                for (File f : of) {
                    if (f.getPath().contains(".csv")) {
                        lsVecsOut.add(f.getAbsolutePath());
                    }
                }

                List<VelocityVec> lo = new ArrayList<>();

                for (String sFile : lsVecsOut) {
                    Reader oRead = new Reader(sFile);
                    Double[][] daInt;
                    try {
                        daInt = oRead.readBigFile(1);
                    } catch (Exception e) {
                        continue;
                    }

                    for (Double[] da : daInt) {
                        lo.add(new VelocityVec(da[3], da[4], new OrderedPair(da[0], da[1], da[5])));
                    }
                }

                RecOrtho2D oGrid = new RecOrtho2D(0, 1280, 680, 0, 10, 1);
                oGrid.addContent(lo);

                // Vy & Vy'Vy'
                oGrid.oValue = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return o.getVelocityY();
                    }
                };

                oGrid.oWeight = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return o.dValue;
                    }
                };

                OrderedPair[][] daAverage = oGrid.calcAverage();
                List<String> lsOut = new ArrayList<>();
                lsOut.add("X,Vy");
                for (OrderedPair[] aop : daAverage) {
                    for (OrderedPair op : aop) {
                        lsOut.add((op.getPosX() * dMetrik) + "," + -1.0 * op.getValue(null) * dMetrik * 250.0);
                    }
                }
                Writer oWrite = new Writer(sPWDPost + "Vy.csv");
                oWrite.write(lsOut);

                //VyVy
                OrderedPair[][] daSTD = oGrid.calcVariance();
                lsOut = new ArrayList<>();
                lsOut.add("X,VyVy");
                for (OrderedPair[] aop : daSTD) {
                    for (OrderedPair op : aop) {
                        lsOut.add((op.getPosX() * dMetrik) + "," + op.getValue(null) * dMetrik * 250.0 * dMetrik * 250.0);
                    }
                }
                oWrite = new Writer(sPWDPost + "VyVy.csv");
                oWrite.write(lsOut);

                //VyVx
                Value oValue2 = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return o.getVelocityX();
                    }
                };

                OrderedPair[][] daCrossCorr = oGrid.calcCrossCorr(oValue2, oGrid.oWeight);
                lsOut = new ArrayList<>();
                lsOut.add("X,VyVx");
                for (OrderedPair[] aop : daCrossCorr) {
                    for (OrderedPair op : aop) {
                        lsOut.add((op.getPosX() * dMetrik) + "," + -1.0 * op.getValue(null) * dMetrik * 250.0 * dMetrik * 250.0);
                    }
                }
                oWrite = new Writer(sPWDPost + "VyVx.csv");
                oWrite.write(lsOut);

                // Vx
                oGrid.oValue = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return o.getVelocityX();
                    }
                };

                oGrid.oWeight = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return o.dValue;
                    }
                };

                daAverage = oGrid.calcAverage();
                lsOut = new ArrayList<>();
                lsOut.add("X,Vx");
                for (OrderedPair[] aop : daAverage) {
                    for (OrderedPair op : aop) {
                        lsOut.add((op.getPosX() * dMetrik) + "," + op.getValue(null) * dMetrik * 250.0);
                    }
                }
                oWrite = new Writer(sPWDPost + "Vx.csv");
                oWrite.write(lsOut);

                //VxVx
                daSTD = oGrid.calcVariance();
                lsOut = new ArrayList<>();
                lsOut.add("X,VxVx");
                for (OrderedPair[] aop : daSTD) {
                    for (OrderedPair op : aop) {
                        lsOut.add((op.getPosX() * dMetrik) + "," + op.getValue(null) * dMetrik * 250.0 * dMetrik * 250.0);
                    }
                }
                oWrite = new Writer(sPWDPost + "VxVx.csv");
                oWrite.write(lsOut);

                //Density
                oGrid.oValue = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return o.dValue;
                    }
                };
                oGrid.oWeight = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return 1.0; //* o.dValue;
                    }
                };
                daAverage = oGrid.calcAverage();
                lsOut = new ArrayList<>();
                lsOut.add("X,Density");
                double dSum = 0.0;
                for (OrderedPair[] aop : daAverage) {
                    for (OrderedPair op : aop) {
                        dSum = dSum + op.getValue(null);
                    }
                }
                for (OrderedPair[] aop : daAverage) {
                    for (OrderedPair op : aop) {
                        lsOut.add((op.getPosX() * dMetrik) + "," + op.getValue(null) / dSum);
                    }
                }
                oWrite = new Writer(sPWDPost + "Density.csv");
                oWrite.write(lsOut);

                //Size
                oGrid.oValue = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return o.dValue;
                    }
                };
                oGrid.oWeight = new Value() {

                    @Override
                    public Double getValue(Object pParameter) {
                        VelocityVec o = (VelocityVec) pParameter;
                        return 1.0; //* o.dValue;
                    }
                };
                daAverage = oGrid.calcAverage();
                lsOut = new ArrayList<>();
                lsOut.add("X,Size");
                for (OrderedPair[] aop : daAverage) {
                    for (OrderedPair op : aop) {
                        lsOut.add((op.getPosX() * dMetrik) + "," + op.getValue(null));
                    }
                }
                oWrite = new Writer(sPWDPost + "Size.csv");
                oWrite.write(lsOut);
            }
        }

    }
}
