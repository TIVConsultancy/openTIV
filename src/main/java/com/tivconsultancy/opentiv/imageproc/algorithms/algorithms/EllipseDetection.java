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
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixGenerator;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.helpfunctions.statistics.Histogram;
import com.tivconsultancy.opentiv.helpfunctions.statistics.HistogramClass;
import com.tivconsultancy.opentiv.imageproc.contours.BasicOperations;
import static com.tivconsultancy.opentiv.imageproc.contours.BasicOperations.getAllContours;
import com.tivconsultancy.opentiv.imageproc.contours.CPX;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.imageproc.shapes.Circle;
import com.tivconsultancy.opentiv.imageproc.shapes.Line;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.interfaces.Value;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class EllipseDetection {

    public static List<Circle> Xi_2002(ImageGrid oEdges, int iMinDistance, int iVoteMax) {
        //A list of circles/ellipses will be returned
        List<Circle> loReturn = new ArrayList<>();
        //Interface to put the "Edge Pixels" in the "Accumulator"
        Value oInterfaceValue = (Value) (Object pParameter) -> ((EnumObject) pParameter).dEnum;
        //Egde Particles that are considered in an ellipse are marked
        oEdges.resetMarkers();
        //Create a list of "Exdge Pixels"
        List<ImagePoint> loEdges = new ArrayList<>();
        for (ImagePoint op : oEdges.oa) {
            if (op.iValue > 127) {
                loEdges.add(op);
            }
        }

        for (int i = 0; i < loEdges.size(); i++) {
            ImagePoint opFirst = loEdges.get(i);
            //Mark Edge Pixel as considered
            opFirst.bMarker = true;
            //Create an "Accumulator" matrix for voting, here as an Histogram implemented           
            for (int j = 0; j < loEdges.size(); j++) {
                if (j == i) {
                    continue;
                }
                Histogram oAccumulator = new Histogram(0, 1.5 * iMinDistance, 5);
                int iX = -1;
                int iY = -1;
                double alpha = -1;
                ImagePoint opSecond = loEdges.get(j);
                double dMajorAxis = opFirst.getNorm(opSecond);
                //Find an "Edge Pixel" that fullfills the condition of MinDistance
                if (!opSecond.bMarker && dMajorAxis >= iMinDistance) {
                    //Center of possible ellipse
                    iX = (int) ((opSecond.getPos().x + opFirst.getPos().x) / 2.0);
                    iY = (int) ((opSecond.getPos().y + opFirst.getPos().y) / 2.0);
                    //Half "Major Axis" of possible ellipse
                    double a = opFirst.getNorm(opSecond) / 2.0;
                    //Orientation of possible ellipse
                    double dRatio = (opSecond.getPos().y - opFirst.getPos().y) / (opSecond.getPos().x - opFirst.getPos().x);
                    alpha = Math.atan(dRatio);
                    for (int t = 0; t < loEdges.size(); t++) {
                        ImagePoint opThird = loEdges.get(t);
                        double d = customNormImagePoint(opThird, iX, iY);
//                        if(a<d) continue;
                        //find pixels that are on the circle with radius of half major axis around the possible center
                        if (!opThird.bMarker && d <= a) {
                            double f1 = opFirst.getNorm(opThird);
                            double f2 = opSecond.getNorm(opThird);
                            if (f1 == f2 || Math.abs(f1 - f2) < 0.05 * a) {
                                oAccumulator.addContent(new EnumObject(d, opThird), oInterfaceValue);
                            }
                            if (f1 < f2) {
                                double dRatio2 = (a * a + d * d - f1 * f1) / (2 * a * d);
                                double tau = Math.acos(dRatio2);
                                double b = Math.sqrt((a * a * d * d * Math.sin(tau) * Math.sin(tau)) / (a * a - d * d * Math.cos(tau) * Math.cos(tau)));
                                oAccumulator.addContent(new EnumObject(b, opThird), oInterfaceValue);
                            }
                            if (f1 > f2) {
                                double tau = Math.acos((a * a + d * d + f2 * f2) / (2 * a * d));
                                double b = Math.sqrt((a * a * d * d * Math.sin(tau) * Math.sin(tau)) / (a * a - d * d * Math.cos(tau) * Math.cos(tau)));
                                oAccumulator.addContent(new EnumObject(b, opThird), oInterfaceValue);
                            }
                        }
                    }

                    HistogramClass oBestFit = oAccumulator.loClasses.get(oAccumulator.getg_max());
                    if (oBestFit.loContent.size() > iVoteMax && iX > 0 && iY > 0 && alpha > 0) {
                        double dMinorAxis = oBestFit.getCenter();
                        loReturn.add(new Circle(new MatrixEntry(iY, iX), dMinorAxis, dMajorAxis, alpha));
                        for (Object oFit : oBestFit.loContent) {
                            ((ImagePoint) ((EnumObject) oFit).o).bMarker = true;
                        }
                    }
                }
            }

        }
        return loReturn;
    }

    public static double customNormImagePoint(ImagePoint op, int iX, int iY) {
        OrderedPair opRef = new OrderedPair(iX, iY);
        return op.getPos().getNorm(opRef);
    }

    public static List<Circle> Ziegenhein_2019_GUI(ImageGrid oEdges, Settings oSettings) throws EmptySetException, IOException {
        Double dDistance = Double.valueOf(oSettings.getSettingsValue("EllipseFit_Ziegenhein2019_Distance").toString());
        double dLeadingDistance = Double.valueOf(oSettings.getSettingsValue("EllipseFit_Ziegenhein2019_LeadingSize").toString());

        //for Debug: ImageInt oBlackBoard
        List<Circle> loReturn = new ArrayList<>();
        List<CPX> allContours = getAllContours(oEdges);
        oEdges.resetMarkers();

        for (CPX o : allContours) {
            double dTimer1 = 0.0;
            double dTimer2 = 0.0;
            double dTimer3 = 0.0;
            double dTimer4 = 0.0;
            double dTimer5 = 0.0;
            dTimer1 = System.currentTimeMillis();
            List<CPX> loCPXToConsider = new ArrayList<>();
            List<Circle_Fit> loPotentialEllipses = new ArrayList<>();
            if (o.lo.size() > dLeadingDistance && !o.bMarker) {
                ImagePoint opFocal = o.getFocalPoint();

                dTimer2 = System.currentTimeMillis();
                // Find the potential contours that would fit
                for (CPX osub : allContours) {
                    if (osub == null || osub.lo.size() < 5 || osub.bMarker || o.equals(osub) || o.oStart.getNorm(osub.oStart) > 3 * dDistance) {
                        continue;
                    }
                    List<MatrixEntry> lmeAllEdges = new ArrayList<>();
                    for (CPX oExclude : allContours) {
//                        if (oExclude.equals(osub)) {
//                            continue;
//                        }
                        lmeAllEdges.addAll(convert(oExclude.getPoints()));
                    }
                    if (opFocal.getNorm(osub.getFocalPoint()) < dDistance && RayTracingCheck3(convert(o.getFocalPoint()), convert(osub.getFocalPoint()), lmeAllEdges)) {
                        loCPXToConsider.add(osub);
                    }
                }
                dTimer2 = dTimer2 - System.currentTimeMillis();
//                IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\Sync\\openTIV\\Tests\\ImageProc\\EllipseDetect\\_3\\" + "Debug.png"));
                dTimer3 = System.currentTimeMillis();
                // Find the potential ellipses that would fit
                if (loCPXToConsider.isEmpty()) {
                    List<CPX> loContoursToFit = new ArrayList<>();
                    if (o.isClosedContour() && false) {
                        double dAVG = 0.0;
                        for (int i = 0; i < o.lo.size(); i++) {
                            dAVG = dAVG + o.getCurv(i, 5, 5);
                        }
                        dAVG = dAVG / o.lo.size();
                        CPX onew = new CPX();
                        onew.oStart = o.oStart;
                        for (int isub = 1; isub < o.lo.size(); isub++) {
//                            Random rand = new Random();
//                            onew.lo.add(o.lo.get(rand.nextInt(o.lo.size())));                            
                            if (o.getCurv(isub, 5, 5) > dAVG) {
                                onew.lo.add(o.lo.get(isub));
                            }
                        }
//                        onew.oEnd = o.lo.get(iEnd - 1);
                        loContoursToFit.add(onew);
                    } else {
                        loContoursToFit.add(o);
                    }
                    Circle_Fit oCosnider_0 = EllipseFit_Contours(loContoursToFit);
                    if (filterEllipse(oSettings, oCosnider_0)) {
                        loPotentialEllipses.add(oCosnider_0);
                    }
                }
                for (CPX oPot : loCPXToConsider) {
                    List<CPX> loContoursToFit = new ArrayList<>();
                    loContoursToFit.add(o);
                    loContoursToFit.add(oPot);
                    Circle_Fit oCosnider_1 = EllipseFit_Contours(loContoursToFit);
                    if (filterEllipse(oSettings, oCosnider_1)) {
                        loPotentialEllipses.add(oCosnider_1);
                    }

                    for (CPX oPot_sub : loCPXToConsider) {
                        if (oPot.equals(oPot_sub)) {
                            continue;
                        }
                        loContoursToFit.add(oPot_sub);
                        Circle_Fit oCosnider_2 = EllipseFit_Contours(loContoursToFit);
                        if (filterEllipse(oSettings, oCosnider_2)) {
                            loPotentialEllipses.add(oCosnider_2);
                        }
                    }
                }

                if (loPotentialEllipses.isEmpty()) {
                    continue;
                }

                dTimer3 = dTimer3 - System.currentTimeMillis();

                List<Circle_Fit> Ellipse_PassedTest = new ArrayList<>();
                for (Circle_Fit ofit : loPotentialEllipses) {
                    if (ofit == null) {
                        continue;
                    }
//                    if ((double) ofit.lmeEntriesToFit.size() / (double) ofit.lmeCircle.size() > dPercantagePoints) {
//                        if (CheckDistance(ofit, ofit.lmeEntriesToFit, (int) (0.01 * Math.max(ofit.dDiameterI, ofit.dDiameterJ)), (int) (0.011 * Math.max(ofit.dDiameterI, ofit.dDiameterJ)))) {
                    Ellipse_PassedTest.add(ofit);
//                        }
//                    }
                }

                dTimer4 = System.currentTimeMillis();
                //Find the rigth ellipse of all fitted ellipses
                List<EnumObject> lo = new ArrayList<>();
                for (Circle_Fit oPassedFit : Ellipse_PassedTest) {
                    if (oPassedFit == null || oPassedFit.loContours.isEmpty() || oPassedFit.lmeCircle.isEmpty()) {
                        continue;
                    }
                    lo.add(new EnumObject(1.0 * getPixelsOnCircle(oPassedFit, convert_CPX(oPassedFit.loContours), 1), oPassedFit));
                }
                if (lo.isEmpty()) {
                    continue;
                }

                EnumObject oCircWithMaxCirc = Sorting.getMaxCharacteristic(lo, new Sorting.Characteristic<EnumObject>() {

                    @Override
                    public Double getCharacteristicValue(EnumObject pParameter) {
                        return pParameter.dEnum;
                    }
                });
                dTimer4 = dTimer4 - System.currentTimeMillis();
                dTimer5 = System.currentTimeMillis();
                //Check Contours that are close to the ellipse
                Circle_Fit oBestFit = (Circle_Fit) ((EnumObject) oCircWithMaxCirc.o).o;
                loReturn.add(oBestFit);
                for (CPX oInEllipse : oBestFit.loContours) {
                    oInEllipse.bMarker = true;
                }
                for (CPX oCheckInEllipse : allContours) {
                    if (oCheckInEllipse == null || oCheckInEllipse.lo.size() < 5 || oCheckInEllipse.bMarker || o.oStart.getNorm(oCheckInEllipse.oStart) > 3 * dDistance) {
                        continue;
                    }
                    if (checkIfInside(oBestFit, oCheckInEllipse, 2, oCheckInEllipse.lo.size() / 3)) {
                        oCheckInEllipse.bMarker = true;
                    }
                }

                dTimer5 = dTimer5 - System.currentTimeMillis();
            }
            dTimer1 = dTimer1 - System.currentTimeMillis();
            if (dTimer1 < -10) {
                System.out.println("Overall: " + dTimer1);
                System.out.println("Amount of CPX considered: " + loCPXToConsider.size());
                System.out.println("Amount of Ellipses considered: " + loPotentialEllipses.size());
                System.out.println("Find Contours: " + (int) (dTimer2 / dTimer1 * 100) + " %");
                System.out.println("Find Ellipses: " + (int) (dTimer3 / dTimer1 * 100) + " %");
                System.out.println("Find Best Fit: " + (int) (dTimer4 / dTimer1 * 100) + " %");
                System.out.println("Find cloe CPX: " + (int) (dTimer5 / dTimer1 * 100) + " %");
                System.out.println("Ratio: " + dTimer1 / dTimer2);
                System.out.println("--------------------------");
            }
        }
        return loReturn;
    }

    public static List<Circle> Ziegenhein_2019_validate(ImageGrid oEdges, Double dDistance, double dLeadingDistance) throws EmptySetException, IOException {

        //for Debug: ImageInt oBlackBoard
        List<Circle> loReturn = new ArrayList<>();
        List<CPX> allContours = BasicOperations.getAllContours(oEdges);
        oEdges.resetMarkers();
//        List<MatrixEntry> out_AllPoints = new ArrayList<>();
//        for (CPX o : allContours) {
//            out_AllPoints.addAll(convert(o.getPoints()));
//        }        

        for (CPX o : allContours) {
            double dTimer1 = 0.0;
            double dTimer2 = 0.0;
            double dTimer3 = 0.0;
            double dTimer4 = 0.0;
            double dTimer5 = 0.0;
            dTimer1 = System.currentTimeMillis();
            List<CPX> loCPXToConsider = new ArrayList<>();
            List<Circle_Fit> loPotentialEllipses = new ArrayList<>();
            if (o.lo.size() > dLeadingDistance && !o.bMarker) {
                ImagePoint opFocal = o.getFocalPoint();

                dTimer2 = System.currentTimeMillis();
                // Find the potential contours that would fit
                for (CPX osub : allContours) {
                    if (osub == null || osub.lo.size() < 5 || osub.bMarker || o.equals(osub) || o.oStart.getNorm(osub.oStart) > 3 * dDistance) {
                        continue;
                    }
                    List<MatrixEntry> lmeAllEdges = new ArrayList<>();
                    for (CPX oExclude : allContours) {
//                        if (oExclude.equals(osub)) {
//                            continue;
//                        }
                        lmeAllEdges.addAll(convert(oExclude.getPoints()));
                    }
                    if (opFocal.getNorm(osub.getFocalPoint()) < dDistance && RayTracingCheck3(convert(o.getFocalPoint()), convert(osub.getFocalPoint()), lmeAllEdges)) {
                        loCPXToConsider.add(osub);
                    }
                }
                dTimer2 = dTimer2 - System.currentTimeMillis();
//                IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\Sync\\openTIV\\Tests\\ImageProc\\EllipseDetect\\_3\\" + "Debug.png"));
                dTimer3 = System.currentTimeMillis();
                // Find the potential ellipses that would fit
                if (loCPXToConsider.isEmpty()) {
                    List<CPX> loContoursToFit = new ArrayList<>();
                    if (o.isClosedContour() && false) {
                        double dAVG = 0.0;
                        for (int i = 0; i < o.lo.size(); i++) {
                            dAVG = dAVG + o.getCurv(i, 5, 5);
                        }
                        dAVG = dAVG / o.lo.size();
                        CPX onew = new CPX();
                        onew.oStart = o.oStart;
                        for (int isub = 1; isub < o.lo.size(); isub++) {
//                            Random rand = new Random();
//                            onew.lo.add(o.lo.get(rand.nextInt(o.lo.size())));                            
                            if (o.getCurv(isub, 5, 5) > dAVG) {
                                onew.lo.add(o.lo.get(isub));
                            }
                        }
//                        onew.oEnd = o.lo.get(iEnd - 1);
                        loContoursToFit.add(onew);
                    } else {
                        loContoursToFit.add(o);
                    }
                    loPotentialEllipses.add(EllipseFit_Contours(loContoursToFit));
                }
                for (CPX oPot : loCPXToConsider) {
                    List<CPX> loContoursToFit = new ArrayList<>();
                    loContoursToFit.add(o);
                    loContoursToFit.add(oPot);
                    Circle_Fit oCosnider_1 = EllipseFit_Contours(loContoursToFit);
                    loPotentialEllipses.add(oCosnider_1);

                    for (CPX oPot_sub : loCPXToConsider) {
                        if (oPot.equals(oPot_sub)) {
                            continue;
                        }
                        loContoursToFit.add(oPot_sub);
                        Circle_Fit oCosnider_2 = EllipseFit_Contours(loContoursToFit);

                        loPotentialEllipses.add(oCosnider_2);

                    }
                }

                if (loPotentialEllipses.isEmpty()) {
                    continue;
                }

                dTimer3 = dTimer3 - System.currentTimeMillis();

                List<Circle_Fit> Ellipse_PassedTest = new ArrayList<>();
                for (Circle_Fit ofit : loPotentialEllipses) {
                    if (ofit == null) {
                        continue;
                    }
//                    if ((double) ofit.lmeEntriesToFit.size() / (double) ofit.lmeCircle.size() > dPercantagePoints) {
//                        if (CheckDistance(ofit, ofit.lmeEntriesToFit, (int) (0.01 * Math.max(ofit.dDiameterI, ofit.dDiameterJ)), (int) (0.011 * Math.max(ofit.dDiameterI, ofit.dDiameterJ)))) {
                    Ellipse_PassedTest.add(ofit);
//                        }
//                    }
                }

                dTimer4 = System.currentTimeMillis();
                //Find the rigth ellipse of all fitted ellipses
                List<EnumObject> lo = new ArrayList<>();
                for (Circle_Fit oPassedFit : Ellipse_PassedTest) {
                    if (oPassedFit == null || oPassedFit.loContours.isEmpty() || oPassedFit.lmeCircle.isEmpty()) {
                        continue;
                    }
                    lo.add(new EnumObject(1.0 * getPixelsOnCircle(oPassedFit, convert_CPX(oPassedFit.loContours), 1), oPassedFit));
                }
                if (lo.isEmpty()) {
                    continue;
                }

                EnumObject oCircWithMaxCirc = Sorting.getMaxCharacteristic(lo, new Sorting.Characteristic<EnumObject>() {

                    @Override
                    public Double getCharacteristicValue(EnumObject pParameter) {
                        return pParameter.dEnum;
                    }
                });
                dTimer4 = dTimer4 - System.currentTimeMillis();
                dTimer5 = System.currentTimeMillis();
                //Check Contours that are close to the ellipse
                Circle_Fit oBestFit = (Circle_Fit) ((EnumObject) oCircWithMaxCirc.o).o;
                loReturn.add(oBestFit);
                for (CPX oInEllipse : oBestFit.loContours) {
                    oInEllipse.bMarker = true;
                }
                for (CPX oCheckInEllipse : allContours) {
                    if (oCheckInEllipse == null || oCheckInEllipse.lo.size() < 5 || oCheckInEllipse.bMarker || o.oStart.getNorm(oCheckInEllipse.oStart) > 3 * dDistance) {
                        continue;
                    }
                    if (checkIfInside(oBestFit, oCheckInEllipse, 2, oCheckInEllipse.lo.size() / 3)) {
                        oCheckInEllipse.bMarker = true;
                    }
                }
//                return loReturn;
                dTimer5 = dTimer5 - System.currentTimeMillis();
            }
            dTimer1 = dTimer1 - System.currentTimeMillis();
            if (dTimer1 < -10) {
                System.out.println("Overall: " + dTimer1);
                System.out.println("Amount of CPX considered: " + loCPXToConsider.size());
                System.out.println("Amount of Ellipses considered: " + loPotentialEllipses.size());
                System.out.println("Find Contours: " + (int) (dTimer2 / dTimer1 * 100) + " %");
                System.out.println("Find Ellipses: " + (int) (dTimer3 / dTimer1 * 100) + " %");
                System.out.println("Find Best Fit: " + (int) (dTimer4 / dTimer1 * 100) + " %");
                System.out.println("Find cloe CPX: " + (int) (dTimer5 / dTimer1 * 100) + " %");
                System.out.println("Ratio: " + dTimer1 / dTimer2);
                System.out.println("--------------------------");
            }
//            if (o.lo.size() > 5) {
//                oBlackBoard.setPoints(out_AllPoints, 255);
//                oBlackBoard.setPoints(convert(o.lo), 200);
//                oBlackBoard.setPoint(convert(o.getFocalPoint()), 55);
//                List<MatrixEntry> lmeToConsider = new ArrayList<>();
//                List<ImagePoint> loToConsider = new ArrayList<>();
//                lmeToConsider.addAll(convert(o.lo));
//                loToConsider.addAll(o.lo);
//                ImagePoint opFocal = o.getFocalPoint();
//                for (CPX osub : allContours) {
//                    if (osub.equals(o) || osub.lo.size() < 5) {
//                        continue;
//                    }
//
//                    List<MatrixEntry> lmeAllEdges = new ArrayList<>();
//                    for (CPX oExclude : allContours) {
//                        if (oExclude.equals(osub)) {
//                            continue;
//                        }
//                        lmeAllEdges.addAll(convert(oExclude.lo));
//                    }
//
////                    if (opFocal.getNorm(osub.getFocalPoint()) < dDistance && RayTracingCheck2(convert(o.lo), convert(o.getFocalPoint()), convert(osub.lo), lmeAllEdges)) {
//                    if (opFocal.getNorm(osub.getFocalPoint()) < dDistance && RayTracingCheck3(convert(o.getFocalPoint()), convert(osub.getFocalPoint()), lmeAllEdges)) {
//                        lmeToConsider.addAll(convert(osub.lo));
//                        loToConsider.addAll(osub.lo);
//                        oBlackBoard.setPoints(convert(osub.lo), 127);
//                    }
//                }
//                if (lmeToConsider.isEmpty()) {
//                    continue;
//                }
//                Circle ofit = EllipseFit(lmeToConsider);
//                if (ofit == null || ofit.dDiameterI < 5 || ofit.dDiameterJ < 5) {
//                    continue;
//                }
//                if ((double) lmeToConsider.size() / (double) ofit.lmeCircle.size() > dPercantagePoints) {
//                    if (CheckDistance(ofit, lmeToConsider, (int) (0.1 * Math.max(ofit.dDiameterI, ofit.dDiameterJ)), (int) (0.1 * Math.max(ofit.dDiameterI, ofit.dDiameterJ)))) {
//                        loReturn.add(ofit);
//                        for (ImagePoint op : loToConsider) {
//                            op.bMarker = true;
//                        }
//                    }
//                }
//                IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\Sync\\openTIV\\Tests\\ImageProc\\EllipseDetect\\_2\\" + "Debug.png"));
//            }
        }
        return loReturn;
    }

    public static boolean filterEllipse(Settings oSettings, Circle oChek) {

        if (oChek == null) {
            return false;
        }

        if (Double.isNaN(oChek.getFormRatio()) || !Double.isFinite(oChek.getFormRatio())) {
            return false;
        }

        if (Double.isNaN(oChek.getSize()) || !Double.isFinite(oChek.getSize())) {
            return false;
        }

        if (Double.isNaN(oChek.getMajorAxis()) || !Double.isFinite(oChek.getMajorAxis())) {
            return false;
        }

        if (Double.isNaN(oChek.getMinorAxis()) || !Double.isFinite(oChek.getMinorAxis())) {
            return false;
        }

        boolean bFilterRatio_Max = Boolean.valueOf(oSettings.getSettingsValue("RatioFilter_Max").toString());
        Double bFilterRatio_Max_Value = Double.valueOf(oSettings.getSettingsValue("RatioFilter_Max_Value").toString());

        boolean bFilterRatio_Min = Boolean.valueOf(oSettings.getSettingsValue("RatioFilter_Min").toString());
        Double bFilterRatio_Min_Value = Double.valueOf(oSettings.getSettingsValue("RatioFilter_Min_Value").toString());

        boolean bSize_Max = Boolean.valueOf(oSettings.getSettingsValue("Size_Max").toString());
        Double bSize_Max_Value = Double.valueOf(oSettings.getSettingsValue("Size_Max_Value").toString());

        boolean bSize_Min = Boolean.valueOf(oSettings.getSettingsValue("Size_Min").toString());
        Double bSize_Min_Value = Double.valueOf(oSettings.getSettingsValue("Size_Min_Value").toString());

        boolean bMajor_Max = Boolean.valueOf(oSettings.getSettingsValue("Major_Max").toString());
        Double bMajor_Max_Value = Double.valueOf(oSettings.getSettingsValue("Major_Max_Value").toString());

        boolean bMajor_Min = Boolean.valueOf(oSettings.getSettingsValue("Major_Min").toString());
        Double bMajor_Min_Value = Double.valueOf(oSettings.getSettingsValue("Major_Min_Value").toString());

        boolean bMinor_Max = Boolean.valueOf(oSettings.getSettingsValue("Major_Max").toString());
        Double bMinor_Max_Value = Double.valueOf(oSettings.getSettingsValue("Major_Max_Value").toString());

        boolean bMinor_Min = Boolean.valueOf(oSettings.getSettingsValue("Minor_Min").toString());
        Double bMinor_Min_Value = Double.valueOf(oSettings.getSettingsValue("Minor_Min_Value").toString());

        if (bFilterRatio_Max && oChek.getFormRatio() > bFilterRatio_Max_Value) {
            return false;
        }
        if (bFilterRatio_Min && oChek.getFormRatio() < bFilterRatio_Min_Value) {
            return false;
        }

        if (bSize_Max && oChek.getSize() > bSize_Max_Value) {
            return false;
        }
        if (bSize_Min && oChek.getSize() < bSize_Min_Value) {
            return false;
        }

        if (bMajor_Max && oChek.getMajorAxis() > bMajor_Max_Value) {
            return false;
        }
        if (bMajor_Min && oChek.getMajorAxis() < bMajor_Min_Value) {
            return false;
        }

        if (bMinor_Max && oChek.getMinorAxis() > bMinor_Max_Value) {
            return false;
        }
        if (bMinor_Min && oChek.getMinorAxis() < bMinor_Min_Value) {
            return false;
        }

        return true;

    }

    public static boolean checkIfInside(Circle oEllipse, CPX oToCheck, int iExpandsion, int iMinCounter) {
        List<MatrixEntry> lmeToCheck = convert(oToCheck.lo);
        Circle oEllipseToCheck_Expanded = new Circle(oEllipse.meCenter, oEllipse.dDiameterI + iExpandsion, oEllipse.dDiameterJ + iExpandsion, oEllipse.dAngle);
        int iCounter = 0;
        for (MatrixEntry me : lmeToCheck) {
            double dCheck = oEllipseToCheck_Expanded.checkpoint(me);
            if (dCheck <= 1) {
                iCounter++;
            }
            if (iCounter > iMinCounter) {
                return true;
            }
        }
        return false;
    }

    public static List<MatrixEntry> convert_CPX(List<CPX> loInput) {
        List<MatrixEntry> lme = new ArrayList<>();
        for (CPX o : loInput) {
            lme.addAll(convert(o.getPoints()));
        }
        return lme;
    }

    public static List<MatrixEntry> convert(List<ImagePoint> loInput) {
        List<MatrixEntry> lme = new ArrayList<>();
        for (ImagePoint o : loInput) {
            if (!o.bMarker) {
                OrderedPair op = o.getPos();
                lme.add(new MatrixEntry(op.y, op.x));
            }
        }
        return lme;
    }

    public static MatrixEntry convert(ImagePoint o) {
        OrderedPair op = o.getPos();
        return new MatrixEntry(op.y, op.x);
    }

    public static boolean RayTracingCheck2(List<MatrixEntry> lme1, MatrixEntry meRefPoint, List<MatrixEntry> lme2, List<MatrixEntry> lmeReference) {

        for (MatrixEntry me2 : lme2) {
            Line oLine = new Line(meRefPoint, me2);
            for (int i = 3; i < oLine.lmeLine.size() - 3; i++) {
                MatrixEntry meLine = oLine.lmeLine.get(i);
                for (MatrixEntry me_test : lmeReference) {
                    if (meLine.getNorm(me_test) < 1.9) {
                        return false;
                    }
//                    if (meLine.equals(me_test)) {
//                        return false;
//                    }
                }
            }
        }

        return true;
    }

    public static boolean RayTracingCheck3(MatrixEntry meRefPoint, MatrixEntry meCheckPoint, List<MatrixEntry> lmeReference) {

        Line oLine = new Line(meRefPoint, meCheckPoint);
        for (int i = 5; i < oLine.lmeLine.size() - 5; i++) {
            MatrixEntry meLine = oLine.lmeLine.get(i);
            for (MatrixEntry me_test : lmeReference) {
                if (meLine.getNorm(me_test) < 1.9) {
                    return false;
                }
//                    if (meLine.equals(me_test)) {
//                        return false;
//                    }
            }
        }

        return true;
    }

    public static int getPixelsOnCircle(Circle oCirc, List<MatrixEntry> lmeReference, int iMaxDist) {

        int iCheck = 0;
        for (MatrixEntry meRef : lmeReference) {
            try {
                EnumObject oEnum = Sorting.getMinCharacteristic(oCirc.lmeCircle, meRef, new Sorting.Characteristic2<MatrixEntry>() {

                    @Override
                    public Double getCharacteristicValue(MatrixEntry pParameter, MatrixEntry pParameter2) {
                        return pParameter.getNorm(pParameter2);
                    }
                });

                MatrixEntry meShortesDist = (MatrixEntry) oEnum.o;

                if (meRef.getNorm(meShortesDist) < iMaxDist) {
                    iCheck++;
                }

            } catch (EmptySetException ex) {
                Logger.getLogger(EllipseDetection.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return iCheck;

    }

    public static boolean RayTracingCheck(List<MatrixEntry> lme1, List<MatrixEntry> lme2) {
        for (MatrixEntry me1 : lme1) {
            for (MatrixEntry me2 : lme2) {
                Line oLine = new Line(me1, me2);
                for (MatrixEntry meLine : oLine.lmeLine) {
                    for (MatrixEntry me_test : lme1) {
                        if (meLine.equals(me_test)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static Circle_Fit EllipseFit_Contours(List<CPX> loContours) {
        HashSet<MatrixEntry> lme = new HashSet<>();
        for (CPX o : loContours) {
            lme.addAll(convert(o.getPoints()));
        }
        //http://nicky.vanforeest.com/misc/fitEllipse/fitEllipse.html
        /*
         Fitzgibbon, Pilu and Fischer in Fitzgibbon, A.W., Pilu, M., and Fischer R.B., Direct least squares fitting of ellipsees, Proc. of the 13th Internation Conference on Pattern Recognition, pp 253–257, Vienna, 1996
         */

        double[][] S = MatrixGenerator.createQuadraticMatrix(6, 0.0);

        int iCount = 0;
        for (MatrixEntry me : lme) {
            double x = me.j;
            double y = 1.0 * me.i;
            double dV = 1;
            double[] da = {x * x, x * y, y * y, x, y, dV};
//            dATest[iCount] = da;
            iCount++;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    S[i][j] = S[i][j] + da[i] * da[j];
                }
            }
        }

        
        Jama.Matrix Jama_S = new Jama.Matrix(S);
        Jama.Matrix Jama_Sinv;
        try {
            Jama_Sinv = Jama_S.inverse();
        } catch (Exception e) {
            return null;
        }

        double[][] C = MatrixGenerator.createQuadraticMatrix(6, 0.0);
        C[0][2] = 2;
        C[2][0] = 2;
        C[1][1] = -1;

        Jama.Matrix Jama_C = new Jama.Matrix(C);

        //EigenvalueDecomposition oED = new EigenvalueDecomposition(Jama_Sinv.times(Jama_C));
        org.ujmp.core.Matrix dense = org.ujmp.core.DenseMatrix.Factory.zeros(6, 6);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                dense.setAsDouble(Jama_Sinv.times(Jama_C).getArray()[i][j], i, j);
            }

        }

        org.ujmp.core.Matrix[] eigenValueDecomposition = dense.eig();

        double dMax = 0.0;
        int iPosMax = 0;
        double[][] daEV = eigenValueDecomposition[1].toDoubleArray();

        int iRunner = 0;
        for (double d : daEV[0]) {
            if (Math.abs(d) > dMax) {
                dMax = d;
                iPosMax = iRunner;
            }
            iRunner++;
        }

        double[] V = new double[6];

        for (int i = 0; i < 6; i++) {
            V[i] = eigenValueDecomposition[0].toDoubleArray()[i][iPosMax];
        }

        double a = V[0];
        double b = V[1] / 2.0;
        double c = V[2];
        double d = V[3] / 2.0;
        double f = V[4] / 2.0;
        double g = V[5];

        double dnum = b * b - a * c;
        double dX0 = (c * d - b * f) / dnum;
        double dY0 = (a * f - b * d) / dnum;

        double up = 2 * (a * f * f + c * d * d + g * b * b - 2 * b * d * f - a * c * g);
        double down1 = (b * b - a * c) * ((c - a) * Math.sqrt(1 + 4 * b * b / ((a - c) * (a - c))) - (c + a));
        double down2 = (b * b - a * c) * ((a - c) * Math.sqrt(1 + 4 * b * b / ((a - c) * (a - c))) - (c + a));

        double l1 = 2 * Math.sqrt(up / down1);
        double l2 = 2 * Math.sqrt(up / down2);

        double dAngle = 0.5 * Math.atan(2 * b / (a - c));

        Circle_Fit oC = new Circle_Fit(new MatrixEntry((int) dY0, (int) dX0), l2, l1, dAngle, loContours);
        oC.opSubPixelCenter = new OrderedPair(dX0, dY0);

        return oC;
    }
    
    public static Circle EllipseFit(List<MatrixEntry> lme) {
        //http://nicky.vanforeest.com/misc/fitEllipse/fitEllipse.html
        /*
         Fitzgibbon, Pilu and Fischer in Fitzgibbon, A.W., Pilu, M., and Fischer R.B., Direct least squares fitting of ellipsees, Proc. of the 13th Internation Conference on Pattern Recognition, pp 253–257, Vienna, 1996
         */

        double[][] S = MatrixGenerator.createQuadraticMatrix(6, 0.0);

        int iCount = 0;
        for (MatrixEntry me : lme) {
            double x = me.j;
            double y = 1.0 * me.i;
            double dV = 1;
            double[] da = {x * x, x * y, y * y, x, y, dV};
//            dATest[iCount] = da;
            iCount++;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    S[i][j] = S[i][j] + da[i] * da[j];
                }
            }
        }

        Jama.Matrix Jama_S = new Jama.Matrix(S);
        Jama.Matrix Jama_Sinv;
        try {
            Jama_Sinv = Jama_S.inverse();
        } catch (Exception e) {
            return null;
        }

        double[][] C = MatrixGenerator.createQuadraticMatrix(6, 0.0);
        C[0][2] = 2;
        C[2][0] = 2;
        C[1][1] = -1;

        Jama.Matrix Jama_C = new Jama.Matrix(C);

        //EigenvalueDecomposition oED = new EigenvalueDecomposition(Jama_Sinv.times(Jama_C));
        org.ujmp.core.Matrix dense = org.ujmp.core.DenseMatrix.Factory.zeros(6, 6);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                dense.setAsDouble(Jama_Sinv.times(Jama_C).getArray()[i][j], i, j);
            }

        }

        org.ujmp.core.Matrix[] eigenValueDecomposition = dense.eig();

        double dMax = 0.0;
        int iPosMax = 0;
        double[][] daEV = eigenValueDecomposition[1].toDoubleArray();

        int iRunner = 0;
        for (double d : daEV[0]) {
            if (Math.abs(d) > dMax) {
                dMax = d;
                iPosMax = iRunner;
            }
            iRunner++;
        }

        double[] V = new double[6];

        for (int i = 0; i < 6; i++) {
            V[i] = eigenValueDecomposition[0].toDoubleArray()[i][iPosMax];
        }

        double a = V[0];
        double b = V[1] / 2.0;
        double c = V[2];
        double d = V[3] / 2.0;
        double f = V[4] / 2.0;
        double g = V[5];

        double dnum = b * b - a * c;
        double dX0 = (c * d - b * f) / dnum;
        double dY0 = (a * f - b * d) / dnum;

        double up = 2 * (a * f * f + c * d * d + g * b * b - 2 * b * d * f - a * c * g);
        double down1 = (b * b - a * c) * ((c - a) * Math.sqrt(1 + 4 * b * b / ((a - c) * (a - c))) - (c + a));
        double down2 = (b * b - a * c) * ((a - c) * Math.sqrt(1 + 4 * b * b / ((a - c) * (a - c))) - (c + a));

        double l1 = 2 * Math.sqrt(up / down1);
        double l2 = 2 * Math.sqrt(up / down2);

        double dAngle = 0.5 * Math.atan(2 * b / (a - c));

        Circle oC = new Circle(new MatrixEntry((int) dY0, (int) dX0), l2, l1, dAngle);
        oC.opSubPixelCenter = new OrderedPair(dX0, dY0);

        return oC;
    }

    public static class Circle_Fit extends Circle {

        public List<CPX> loContours = new ArrayList<>();

        public Circle_Fit(MatrixEntry meCenter, double dDiameterI, double dDiameterJ, Double dAngle, List<CPX> loContours) {
            super(meCenter, dDiameterI, dDiameterJ, dAngle);
            for (CPX o : loContours) {
                this.loContours.add(o);
            }
        }

    }

}
