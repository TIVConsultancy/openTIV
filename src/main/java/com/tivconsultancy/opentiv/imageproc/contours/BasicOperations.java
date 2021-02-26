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
package com.tivconsultancy.opentiv.imageproc.contours;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.BasicIMGOper;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Morphology;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageBoolean;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt.IterativeFunction;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.imageproc.shapes.ArbStructure;
import com.tivconsultancy.opentiv.imageproc.shapes.ArbStructure2;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
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
public class BasicOperations {

    public static List<CPX> getAllContours(ImageInt oEdges) {
        try {
            return getAllContours(new ImageGrid(oEdges.iaPixels));
        } catch (EmptySetException ex) {
            Logger.getLogger(BasicOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static List<ArbStructure> getAllStructures(ImageGrid oEdges) throws EmptySetException {

        List<ArbStructure> loStructures = new ArrayList<>();
//        HashSet<ImagePoint> loStarts = Algorithms.Ziegenhein_2018.getStart(oEdges);
//        ImageInt oBlackBoard = new ImageInt(new int[oEdges.iLength][oEdges.jLength]);
        oEdges.resetMarkers();

        for (ImagePoint op : oEdges.oa) {
            if (op.iValue < 127 && !op.bMarker) {
                ArbStructure oStruc = new ArbStructure(new Morphology().markFillN8(oEdges, op));
                oEdges.setPoint(oStruc.loPoints, new ImageGrid.setIMGPoint() {

                    @Override
                    public void setPoint(ImageGrid oGrid, ImagePoint op) {
                        oGrid.oa[op.i].bMarker = true;
                    }
                }, oEdges);
//                oBlackBoard.setPointsIMGP(oStruc.loPoints, 255);
//                IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\Sync\\TIVConsultancy\\UseCases\\Crystallography2_Technobis\\TIVConsultancy\\_strucTestOut.png"));
                loStructures.add(oStruc);
            }

        }

        return loStructures;
    }

    public static List<ArbStructure2> getAllStructures(ImageInt oStructures) {

        ImageInt objectMarker = BasicIMGOper.invert(oStructures.clone());
        List<ArbStructure2> loStructures = new ArrayList<>();
        oStructures.resetMarkers();
        IterativeFunction fun = new ImageInt.IterativeFunction() {
            @Override
            public void perform(int i, int j) {
                if (!objectMarker.baMarker[i][j] && objectMarker.iaPixels[i][j] < 127) {
                    ArbStructure2 oStruc = new ArbStructure2(new Morphology().markFillN8(objectMarker, i, j));
                    loStructures.add(oStruc);
                }

            }
        };
        objectMarker.iterate(fun);

        return loStructures;

    }

    public static List<CPX> getAllContours(ImageGrid oEdges) throws EmptySetException {

        List<CPX> loOpen = new ArrayList<>();
        List<CPX> loClosed = new ArrayList<>();
        HashSet<ImagePoint> loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        for (ImagePoint o : loStarts) {
            loOpen.add(new CPX(o));
        }

        ImageGrid oBlackBoard = new ImageGrid(oEdges.iLength, oEdges.jLength);
        for (CPX o : loOpen) {
            oBlackBoard.setPoint(o.lo, 127);
            oEdges.setPoint(o.lo, 0);
            oEdges.setPoint(o.oStart, 0);
        }

        oEdges.resetMarkers();
        HashSet loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);
        loJoints = Ziegenhein_2018.getJoints(oEdges);
        oEdges.setPoint(loJoints, 0);

        loStarts = Ziegenhein_2018.getStart(oEdges);
        oEdges.resetMarkers();
        int iCount = 0;
        for (ImagePoint o : loStarts) {
            CPX oTemp = new CPX(o);
            loClosed.add(oTemp);
            oEdges.setPoint(oTemp.lo, 0); // 255 + 0 * iCount * 50);
            oEdges.setPoint(oTemp.oStart, 0);
            oBlackBoard.setPoint(oTemp.lo, 255);
        }

        for (ImagePoint o : oEdges.oa) {
            if (o.iValue > 127 && !o.bMarker) {
                CPX oTemp = new CPX(o);
                loClosed.add(oTemp);
                oEdges.setPoint(oTemp.lo, 0); // 255 + 0 * iCount * 50);
                oEdges.setPoint(oTemp.oStart, 0);
                oBlackBoard.setPoint(oTemp.lo, 255);
                iCount++;
            }
        }

//        try {
//            IMG_Writer.PaintGreyPNG(oEdges, new File("E:\\TIVTechnologies\\ContourDetectTest\\Test.png"));
//            IMG_Writer.PaintGreyPNG(oBlackBoard, new File("E:\\TIVTechnologies\\ContourDetectTest\\BlackBoard.png"));
//        } catch (IOException ex) {
//            Logger.getLogger(BasicOperations.class.getName()).log(Level.SEVERE, null, ex);
//        }
        List<CPX> loReturn = new ArrayList<>();
        loReturn.addAll(loOpen);
        loReturn.addAll(loClosed);
        return loReturn;
    }

    public static void markOuterEnds(List<MatrixEntry> lsip, ImageGrid oGrid, int iValue) {
        MatrixEntry me = new MatrixEntry();
        me.i = 0;
        me.j = 0;
        for (MatrixEntry ip : lsip) {
            me.i += ip.i;
            me.j += ip.j;
        }

        me.i = me.i / lsip.size();
        me.j = me.j / lsip.size();
        double dMaxDist = 0.0;
        ImagePoint oip = new ImagePoint(0, 0, oGrid
        );
        for (MatrixEntry ip : lsip) {
            double dDist = me.getNorm(ip);
            if (dDist > dMaxDist) {
                dMaxDist = dDist;
                oip = new ImagePoint(ip.j, ip.i, 127, oGrid);
            }
        }
//        cpx.oStart=oip;
        oGrid.setPoint(oip, iValue);
        me = oip.getME();
        dMaxDist = 0.0;
        ImagePoint oip2 = new ImagePoint(0, 0, oGrid);
        for (MatrixEntry ip : lsip) {
            double dDist = me.getNorm(ip);
            if (dDist > dMaxDist) {
                dMaxDist = dDist;
                oip2 = new ImagePoint(ip.j, ip.i, 127, oGrid);
            }
        }

        oGrid.setPoint(oip2, iValue);
    }

    public static void markOuterEnds2(List<MatrixEntry> lsip, ImageGrid oGrid, int iValue, ImageGrid oGrid2) {
        MatrixEntry me = new MatrixEntry();
        me.i = 0;
        me.j = 0;
        for (MatrixEntry ip : lsip) {
            me.i += ip.i;
            me.j += ip.j;
        }

        me.i = me.i / lsip.size();
        me.j = me.j / lsip.size();
        double dMeanDist = 0.0;

        for (MatrixEntry ip : lsip) {
            dMeanDist += me.getNorm(ip);
        }
        dMeanDist = dMeanDist / lsip.size();
        for (MatrixEntry ip : lsip) {
            ImagePoint mip = new ImagePoint(ip.j, ip.i, 0, oGrid);
            if (oGrid.oa[mip.i].iValue == iValue && (ip.getNorm(me) < dMeanDist || oGrid2.oa[mip.i].iValue == 255 )) {
                //|| new N8(oGrid, mip).getBP() > 2
                oGrid.setPoint(mip, 0);
            }
        }

    }

}
