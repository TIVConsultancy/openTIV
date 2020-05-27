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
package com.tivconsultancy.opentiv.imageproc.testing;

import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EllipseDetection;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Writer;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.imageproc.shapes.Circle;
import com.tivconsultancy.opentiv.imageproc.shapes.Line2;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.io.File;
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
public class TestPackage {
    
    public static void testEllipseDetect(String sFolder, String sFileName) throws IOException, EmptySetException{
        ImageGrid oEdges = new ImageGrid(IMG_Reader.readImageGrayScale(new File(sFolder + File.separator + sFileName)));
        ImageInt oBlackBoard = new ImageInt(IMG_Reader.readImageGrayScale(new File(sFolder + File.separator + sFileName)));
//        ImageInt oBlackBoard2 = new ImageInt(IMG_Reader.readImageGrayScale(new File(sFolder + File.separator + sFileName)));
//        List<Circle> lo = Algorithms.EllipseDetection.Xi_2002(oEdges, 40, 20);
        List<Circle> lo = EllipseDetection.Ziegenhein_2019_validate(oEdges, 50.0, 30.0);
        for(Circle o : lo){
             oBlackBoard.setPoints(o.lmeCircle, 127);
        }
        IMG_Writer.PaintGreyPNG(oBlackBoard, new File(sFolder + File.separator + "Output.png"));
    }

    public static boolean testGetN8Neighbors() {
        ImageGrid oGrid = new ImageGrid(3, 6);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                int iIndex = (i) * 6 + j;
                oGrid.oa[iIndex] = new ImagePoint(iIndex, iIndex, oGrid);
            }
        }
        ArrayList<ImagePoint> lo1 = oGrid.getNeighborsN8(oGrid.oa[0]);
        ArrayList<ImagePoint> lo2 = oGrid.getNeighborsN8(oGrid.oa[5]);
        ArrayList<ImagePoint> lo3 = oGrid.getNeighborsN8(oGrid.oa[12]);
        ArrayList<ImagePoint> lo4 = oGrid.getNeighborsN8(oGrid.oa[17]);
        ArrayList<ImagePoint> lo = oGrid.getNeighborsN8(oGrid.oa[8]);
        Integer checkSum = 0;
        for (ImagePoint o : lo) {
            checkSum = (int) (checkSum + o.iValue);
        }
        if (checkSum == 64) {
            return true;
        }
        return false;

//        System.out.println(oGrid.getPos(oGrid.oa[8]));
//        System.out.println("----------------------");
//        for(ImagePoint o : lo){
//            System.out.println(oGrid.getPos(o));
//        }
    }

    public static void getAllJoints() throws IOException, EmptySetException {
        ImageGrid oIMGGrid = IMG_Reader.readImageGrey(new File("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Tests\\Contourdetecting\\ContourRemoval\\closing1.jpg"));
        oIMGGrid.binarize(127);
        oIMGGrid = Ziegenhein_2018.thinoutEdges(oIMGGrid);                
        
        oIMGGrid = Ziegenhein_2018.CNCPNetwork.OpenCNCP(oIMGGrid, new Ziegenhein_2018.RuleWithDoubleAction<Ziegenhein_2018.CNCP>() {

            @Override
            public List<Ziegenhein_2018.CNCP> isValid(Ziegenhein_2018.CNCP o, HashSet<Ziegenhein_2018.CNCP> lo) {

                List<Ziegenhein_2018.CNCP> loReturn = new ArrayList<>();
                try {
                    for (EnumObject oEnum : o.getclosest(lo, 20)) {
                        if (oEnum.dEnum > 1.5 && oEnum.o != null) {
                            loReturn.add((Ziegenhein_2018.CNCP) oEnum.o);
                        }
                    }
                } catch (EmptySetException ex) {
                    Logger.getLogger(TestPackage.class.getName()).log(Level.SEVERE, null, ex);
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
                        Logger.getLogger(TestPackage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (oHelp != null && oHelp.o != null) {
                        oHelp = (EnumObject) oHelp.o;
                        ImagePoint oConnect1 = (ImagePoint) ((Object[]) oHelp.o)[0];
                        ImagePoint oConnect2 = (ImagePoint) ((Object[]) oHelp.o)[1];

                        OrderedPair opDeri1 = o1.getDerivationOutwardsEndPoints(oConnect1, 5);
                        OrderedPair opDeri2 = o2.getDerivationOutwardsEndPoints(oConnect2, 5);
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

                        System.out.println("Ping");
                        
                        if (dSmoothness <= 10.5 && (Math.min(dDirecDist1, dDirecDist2) * dDist) < 100) {
                            Line2 oLine = new Line2(oConnect1, oConnect2);
                            oLine.setLine(oGrid, 127);
                        }
//                        CNCP.connectCNCP(o1, o2, oConnect1, oConnect2, oGrid, 127);

                    }
                }
            }
        });
        
        
        IMG_Writer.PaintGreyPNG(oIMGGrid, new File("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Tests\\Contourdetecting\\ContourRemoval\\Joints.png"));
        
//        oIMGGrid = Ziegenhein_2018.CNCPNetwork.markOpenCNCP(oIMGGrid, new Ziegenhein_2018.sortOutRule3<Ziegenhein_2018.CNCP>() {
//
//            @Override
//            public boolean remove(Ziegenhein_2018.CNCP o, HashSet<Ziegenhein_2018.CNCP> lo) {
//                if(o.oStart == null || o.oEnd == null) return false;
////                boolean bProblem = o.lo.size()>10;
////                bProblem = bProblem && (o.oStart.getPos().x <= 2 && o.oStart.getPos().y <= 2 );
//                boolean bProblem =  (o.oStart.getPos().getNorm(o.oEnd.getPos()) <= 5);
//                return bProblem;
//            }
//        }, 127);
//        
//        oIMGGrid.resetMarkers();
//        
//        Ziegenhein_2018.markEndStart(oIMGGrid, 50);
//        
//        Writer.PaintGreyPNG(oIMGGrid, new File("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Tests\\Contourdetecting\\ContourRemoval\\Contours.png"));
        
    }

}
