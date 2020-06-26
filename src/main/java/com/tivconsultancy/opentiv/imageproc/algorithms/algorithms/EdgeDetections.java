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

import com.tivconsultancy.opentiv.helpfunctions.matrix.Matrix;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixGenerator;
import com.tivconsultancy.opentiv.helpfunctions.matrix.trigonometrics;
import static com.tivconsultancy.opentiv.helpfunctions.operations.Convolution.ConvolutionParallel;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Writer;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class EdgeDetections {
    public static ImageGrid getEdgesImageGrid(String sFile, int EdgeThreshold, int cutyTop, int cutyBottom, int cutxLeft, int cutxRight) throws IOException, EmptySetException {
        ImageGrid o = IMG_Reader.readImageGrey(new File(sFile));
//        Writer.PaintGreyPNG(o, new File("D:\\open_TIV\\BubbleTracking\\N01_600mm\\Debug\\_Input.png"));
        int[][] iaProcess = o.getMatrix();
        iaProcess = NoiseReduction.Gau(iaProcess);
        o = new ImageGrid(getThinEdge(iaProcess, Boolean.FALSE, null, null, 0));        
        o.binarize(EdgeThreshold);
        o = o.getsubY(cutyTop, cutyBottom);
        o = o.getsubX(cutxLeft, cutxRight);        
        o.resetMarkers();
        Morphology.dilatation(o);
        Morphology.dilatation(o);
        Morphology.erosion(o);
        o.resetMarkers();        
        (new Morphology()).markFillN4(o, o.oa[0]);
        (new Morphology()).markFillN4(o, o.oa[o.oa.length-1]);
        Morphology.setmarkedPoints(o, 255, (ImagePoint pParameter) -> !pParameter.bMarker);
        o.resetMarkers();        
        Morphology.markEdgesBinarizeImage(o);
        Morphology.setmarkedPoints(o, 0, (ImagePoint pParameter) -> !pParameter.bMarker);                
        o = Ziegenhein_2018.thinoutEdges(o);        
        return o;
    }
    
    public static ImageInt getEdges(String sFile, int EdgeThreshold, int cutyTop, int cutyBottom, int cutxLeft, int cutxRight) throws IOException, EmptySetException {
        ImageInt o = new ImageInt(IMG_Reader.readImageGrayScale(new File(sFile)));
//        Writer.PaintGreyPNG(o, new File("D:\\open_TIV\\BubbleTracking\\N01_600mm\\Debug\\_Input.png"));
        o.iaPixels = NoiseReduction.Gau(o.iaPixels);
        o.iaPixels = getThinEdge(o.iaPixels, Boolean.FALSE, null, null, 0);        
        BasicIMGOper.threshold(o, EdgeThreshold);        
        o = o.getsubY(cutyTop, cutyBottom);
        o = o.getsubX(cutxLeft, cutxRight);        
        o.resetMarkers();
        Morphology.dilatation(o);
        Morphology.dilatation(o);
        Morphology.erosion(o);
        o.resetMarkers();        
        (new Morphology()).markFillN4(o, 0,0);
        (new Morphology()).markFillN4(o, o.iaPixels.length, o.iaPixels[0].length);
        Morphology.setNotMarkedPoints(o, 255);
        o.resetMarkers();        
        Morphology.markEdgesBinarizeImage(o);
        Morphology.setNotMarkedPoints(o, 0);       
        o = Ziegenhein_2018.thinoutEdges(o);        
        return o;
    }
    
    public static ImageInt getEdges(ImageInt o, int EdgeThreshold) throws IOException, EmptySetException {        
//        Writer.PaintGreyPNG(o, new File("D:\\open_TIV\\BubbleTracking\\N01_600mm\\Debug\\_Input.png"));
        o.iaPixels = NoiseReduction.Gau(o.iaPixels);
        o.iaPixels = getThinEdge(o.iaPixels, Boolean.FALSE, null, null, 0);
        BasicIMGOper.threshold(o, EdgeThreshold);
//        IMG_Writer.PaintGreyPNG(o, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\0p5slpmin\\ContourDetection\\Debug\\Edges.png"));
        o.resetMarkers();
        Morphology.dilatation(o);
        Morphology.dilatation(o);
        Morphology.erosion(o);
        o.resetMarkers();                

        (new Morphology()).markFillN4(o, 0, 0);
        (new Morphology()).markFillN4(o, o.iaPixels.length-1, 0);
        (new Morphology()).markFillN4(o, 0, o.iaPixels[0].length-1);
        (new Morphology()).markFillN4(o, o.iaPixels.length-1, o.iaPixels[0].length-1);        
        Morphology.setNotMarkedPoints(o, 255);
        o.resetMarkers();
        Morphology.markEdgesBinarizeImage(o);
        Morphology.setNotMarkedPoints(o, 0);
        Morphology.setmarkedPoints(o, 255);
        o = Ziegenhein_2018.thinoutEdges(o);
//        IMG_Writer.PaintGreyPNG(o, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\0p5slpmin\\ContourDetection\\Debug\\Edges_After.png"));
        return o;
    }        
    
    public static ImageInt getEdgesTechnobis(ImageInt o, int EdgeThreshold) throws IOException, EmptySetException {        
        IMG_Writer.PaintGreyPNG(o, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\0p5slpmin\\ContourDetection\\Debug\\_Input.png"));
        o.iaPixels = NoiseReduction.Gau(o.iaPixels);
        o.iaPixels = getThinEdge(o.iaPixels, Boolean.FALSE, null, null, 0);
        BasicIMGOper.threshold(o, EdgeThreshold);
        IMG_Writer.PaintGreyPNG(o, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\0p5slpmin\\ContourDetection\\Debug\\Edges_T.png"));
        o.resetMarkers();
//        Morphology.dilatation(o);
//        Morphology.dilatation(o);
//        Morphology.erosion(o);
        o.resetMarkers();

        (new Morphology()).markFillN4(o, 0, 0);
        (new Morphology()).markFillN4(o, o.iaPixels.length-1, 0);
        (new Morphology()).markFillN4(o, 0, o.iaPixels[0].length-1);
        (new Morphology()).markFillN4(o, o.iaPixels.length-1, o.iaPixels[0].length-1);        
        Morphology.setNotMarkedPoints(o, 255);
        IMG_Writer.PaintGreyPNG(o, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\0p5slpmin\\ContourDetection\\Debug\\Edges_MarkFill.png"));
        o.resetMarkers();
        Morphology.markEdgesBinarizeImage(o);
        Morphology.setNotMarkedPoints(o, 0);
        Morphology.setmarkedPoints(o, 255);
        o = Ziegenhein_2018.thinoutEdges(o);
        IMG_Writer.PaintGreyPNG(o, new File("E:\\Work\\openTIV\\SharpBoundaryTracking\\0p5slpmin\\ContourDetection\\Debug\\Edges_After.png"));
        return o;
    }
    
    
//    public static ImageInt getEdgesClosedObjects(ImageInt o, int EdgeThreshold) throws IOException, EmptySetException {        
////        Writer.PaintGreyPNG(o, new File("D:\\open_TIV\\BubbleTracking\\N01_600mm\\Debug\\_Input.png"));
//        o.iaPixels = NoiseReduction.Gau(o.iaPixels);
//        o.iaPixels = getThinEdge(o.iaPixels, Boolean.FALSE, null, null, 0);        
//        BasicIMGOper.threshold(o, EdgeThreshold);        
//        o.resetMarkers();
//        Morphology.dilatation(o);
//        Morphology.dilatation(o);
//        Morphology.erosion(o);
//        o.resetMarkers();                
//        (new Morphology()).markFillN4(o, 0, 0);
//        (new Morphology()).markFillN4(o, o.iaPixels.length-1, 0);
//        (new Morphology()).markFillN4(o, 0, o.iaPixels[0].length-1);
//        (new Morphology()).markFillN4(o, o.iaPixels.length-1, o.iaPixels[0].length-1);        
//        Morphology.setNotMarkedPoints(o, 255);
//        o = BasicIMGOper.invert(o);
//        List<MatrixEntry> lmeBlack = new ArrayList<MatrixEntry>();
//        for(int i = 0; i < o.iaPixels.length; i++){
//            for(int j = 0; j < o.iaPixels[0].length; j++){
//                if(o.iaPixels[i][j] < 127){
//                    lmeBlack.add(new MatrixEntry(i, j));
//                }
//            }
//        }
//        Morphology oMorph = new Morphology();
//        for(MatrixEntry me : lmeBlack){
//            if(o.iaPixels[me.i][me.j] >= 127 || o.baMarker[me.i][me.j]) continue;
//            List<MatrixEntry> lme = oMorph.markFillN4(o, me.i, me.j);
//        }
//        
//        o.resetMarkers();
//        Morphology.markEdgesBinarizeImage(o);
//        Morphology.setNotMarkedPoints(o, 0);
//        Morphology.setmarkedPoints(o, 255);
//        o = Ziegenhein_2018.thinoutEdges(o);        
//        return o;
//    }
    
    public static ImageGrid getEdgesWithNoisReduction1(String sFile, int EdgeThreshold, int cutyTop, int cutyBottom, int cutxLeft, int cutxRight) throws IOException, EmptySetException {
        ImageGrid o = IMG_Reader.readImageGrey(new File(sFile));
        NoiseReduction.simple1(o, 50);
//        NoiseReduction.simple1(o, 50);
//        Writer.PaintGreyPNG(o, new File("E:\\Sync\\openTIV\\Projects\\06-2019_MicroBubbles\\Debug\\_Input.png"));
        int[][] iaProcess = o.getMatrix();
        iaProcess = NoiseReduction.Gau(iaProcess);        
        o = new ImageGrid(getThinEdge(iaProcess, Boolean.FALSE, null, null, 0)); 
        o.binarize(EdgeThreshold);
        o = o.getsubY(cutyTop, cutyBottom);
        o = o.getsubX(cutxLeft, cutxRight);
        
        o.resetMarkers();
        Morphology.dilatation(o);
        Morphology.dilatation(o);
        Morphology.erosion(o);        
        o.resetMarkers();        
        (new Morphology()).markFillN4(o, o.oa[0]);
        (new Morphology()).markFillN4(o, o.oa[o.oa.length-1]);
        (new Morphology()).markFillN4(o, o.oa[o.getIndex(new OrderedPair(o.jLength-1, 0))]);
        (new Morphology()).markFillN4(o, o.oa[o.getIndex(new OrderedPair(0, o.iLength-1))]);
        Morphology.setmarkedPoints(o, 255, (ImagePoint pParameter) -> !pParameter.bMarker);
        o.resetMarkers();        
        Morphology.erosion(o);
//        Morphology.erosion(o);
        o.resetMarkers();     
        Morphology.markEdgesBinarizeImage(o);
        Morphology.setmarkedPoints(o, 0, (ImagePoint pParameter) -> !pParameter.bMarker);                
        o = Ziegenhein_2018.thinoutEdges(o);
//        Writer.PaintGreyPNG(o, new File("E:\\Sync\\openTIV\\Projects\\06-2019_MicroBubbles\\Debug\\_Edges.png"));
        return o;
    }
    
    public static ImageGrid getEdgesWithNoisReduction1(ImageGrid o, int EdgeThreshold) throws IOException, EmptySetException {
        
        int[][] iaProcess = o.getMatrix();
        iaProcess = NoiseReduction.Gau(iaProcess);        
        o = new ImageGrid(getThinEdge(iaProcess, Boolean.FALSE, null, null, 0)); 
        o.binarize(EdgeThreshold);
        
        o.resetMarkers();
        Morphology.dilatation(o);
        Morphology.dilatation(o);
        Morphology.erosion(o);        
        o.resetMarkers();        
        (new Morphology()).markFillN4(o, o.oa[0]);
        (new Morphology()).markFillN4(o, o.oa[o.oa.length-1]);
        (new Morphology()).markFillN4(o, o.oa[o.getIndex(new OrderedPair(o.jLength-1, 0))]);
        (new Morphology()).markFillN4(o, o.oa[o.getIndex(new OrderedPair(0, o.iLength-1))]);
        Morphology.setmarkedPoints(o, 255, (ImagePoint pParameter) -> !pParameter.bMarker);
        o.resetMarkers();        
        Morphology.erosion(o);
//        Morphology.erosion(o);
        o.resetMarkers();     
        Morphology.markEdgesBinarizeImage(o);
        Morphology.setmarkedPoints(o, 0, (ImagePoint pParameter) -> !pParameter.bMarker);                
        o = Ziegenhein_2018.thinoutEdges(o);
//        Writer.PaintGreyPNG(o, new File("E:\\Sync\\openTIV\\Projects\\06-2019_MicroBubbles\\Debug\\_Edges.png"));
        return o;
    }
    
    public static int[][] getThinEdge(final int[][] iaGreyInput, Boolean bPrintSteps, String pwd, String sIdentifier, int iSimpleTreshold) {

        int[][] iaAbbrX = ConvolutionParallel(iaGreyInput, MatrixGenerator.getSobelOperator().get(0));

        int[][] iaAbbrY = ConvolutionParallel(iaGreyInput, MatrixGenerator.getSobelOperator().get(1));

        int[][] iaGreyRichtung = trigonometrics.getDirection(
                iaAbbrX,
                iaAbbrY);                

        int[][] iaGreyAbsolutGrad = Matrix.addPythagoras(iaAbbrX, iaAbbrY);        

        if (iSimpleTreshold != 0) {
            iaGreyAbsolutGrad = Matrix.treshold(iaGreyAbsolutGrad, iSimpleTreshold, 0);
        }

        int[][] iaCanny = IntelligentcannyALgorithm(iaGreyAbsolutGrad, iaGreyRichtung);

        return iaCanny;
    }

    public static int[][] hyst(final int[][] iaGreyInput, Boolean bPrintSteps, String pwd, String sIdentifier, int iSimpleTreshold) throws IOException {

        int[][] iaAbbrX = ConvolutionParallel(iaGreyInput, MatrixGenerator.getSobelOperator().get(0));

        int[][] iaAbbrY = ConvolutionParallel(iaGreyInput, MatrixGenerator.getSobelOperator().get(1));

        int[][] iaGreyRichtung = trigonometrics.getDirection(
                iaAbbrX,
                iaAbbrY);                

        int[][] iaGreyAbsolutGrad = Matrix.addPythagoras(iaAbbrX, iaAbbrY);        

        if (iSimpleTreshold != 0) {
            iaGreyAbsolutGrad = Matrix.treshold(iaGreyAbsolutGrad, iSimpleTreshold, 0);
        }

        int[][] iaCanny = IntelligentcannyALgorithm(iaGreyAbsolutGrad, iaGreyRichtung);

        return iaCanny;
    }

    
    public static int[][] IntelligentcannyALgorithm(int[][] iaAbsoluteGradient, int[][] iaDirectionOfGradient) {

        /*        int[][] iaAbsoluteGradient = Matrix.addPythagoras(iaInputGradientX, iaInputGradientY);
        
         int[][] iaDirectionOfGradient = trigonometrics.getDirection(iaInputGradientX, iaInputGradientY);*/
        //int[][] iaReturn = new int[iaAbsoluteGradient.length][iaAbsoluteGradient[0].length];
        int[][] iaReturn = MatrixGenerator.createMatrix(iaAbsoluteGradient.length, iaAbsoluteGradient[0].length, 0);

        for (int i = 1; i < iaAbsoluteGradient.length - 1; i++) {

            for (int j = 1; j < iaAbsoluteGradient[0].length - 1; j++) {

                if (iaDirectionOfGradient[i][j] == 0) {

                    if ((iaAbsoluteGradient[i][j - 1] < iaAbsoluteGradient[i][j])
                            && (iaAbsoluteGradient[i][j + 1] < iaAbsoluteGradient[i][j])) {

                        iaReturn[i][j] = iaAbsoluteGradient[i][j];

                    } else {

                        if ((iaAbsoluteGradient[i][j - 1] < iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i][j + 1] == iaAbsoluteGradient[i][j])) {

                            int tempj = j;

                            while ((tempj + 1) < iaAbsoluteGradient[0].length && iaDirectionOfGradient[i][tempj + 1] == 0) {

                                if (iaAbsoluteGradient[i][tempj + 1] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[i][tempj + 1] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempj = tempj + 1;
                            }

                        }

                        if ((iaAbsoluteGradient[i][j - 1] == iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i][j + 1] < iaAbsoluteGradient[i][j])) {

                            int tempj = j;

                            while (tempj - 1 > 0 && iaAbsoluteGradient[i][tempj - 1] == 0) {

                                if (iaAbsoluteGradient[i][tempj - 1] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[i][tempj - 1] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempj = tempj - 1;
                            }

                        }

                    }

                }

                if (iaDirectionOfGradient[i][j] == 1) {

                    if ((iaAbsoluteGradient[i + 1][j + 1] < iaAbsoluteGradient[i][j])
                            && (iaAbsoluteGradient[i - 1][j - 1] < iaAbsoluteGradient[i][j])) {

                        iaReturn[i][j] = iaAbsoluteGradient[i][j];

                    } else {

                        if ((iaAbsoluteGradient[i + 1][j + 1] < iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i - 1][j - 1] == iaAbsoluteGradient[i][j])) {

                            int tempj = j;
                            int tempi = i;

                            while ((tempj - 1) > 0 && (tempi - 1) > 0 && iaDirectionOfGradient[tempi - 1][tempj - 1] == 1) {

                                if (iaAbsoluteGradient[tempi - 1][tempj - 1] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[tempi - 1][tempj - 1] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempj = tempj - 1;
                                tempi = tempi - 1;
                            }

                        }

                        if ((iaAbsoluteGradient[i + 1][j + 1] == iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i - 1][j - 1] < iaAbsoluteGradient[i][j])) {

                            int tempj = j;
                            int tempi = i;

                            while ((tempj + 1) < iaAbsoluteGradient[0].length && (tempi + 1) < iaAbsoluteGradient.length && iaDirectionOfGradient[tempi + 1][tempj + 1] == 1) {

                                if (iaAbsoluteGradient[tempi + 1][tempj + 1] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[tempi + 1][tempj + 1] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempj = tempj + 1;
                                tempi = tempi + 1;
                            }

                        }
                    }
                }

                if (iaDirectionOfGradient[i][j] == 2) {

                    if ((iaAbsoluteGradient[i + 1][j] < iaAbsoluteGradient[i][j])
                            && (iaAbsoluteGradient[i - 1][j] < iaAbsoluteGradient[i][j])) {

                        iaReturn[i][j] = iaAbsoluteGradient[i][j];

                    } else {

                        if ((iaAbsoluteGradient[i + 1][j + 1] < iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i - 1][j] == iaAbsoluteGradient[i][j])) {

                            int tempi = i;

                            while ((tempi - 1) > 0 && iaDirectionOfGradient[tempi - 1][j] == 2) {

                                if (iaAbsoluteGradient[tempi - 1][j] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[tempi - 1][j] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempi = tempi - 1;
                            }

                        }

                        if ((iaAbsoluteGradient[i + 1][j] == iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i - 1][j] < iaAbsoluteGradient[i][j])) {

                            int tempi = i;

                            while ((tempi + 1) < iaAbsoluteGradient.length && iaDirectionOfGradient[tempi + 1][j] == 1) {

                                if (iaAbsoluteGradient[tempi + 1][j] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[tempi + 1][j] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempi = tempi + 1;
                            }

                        }
                    }
                }

                if (iaDirectionOfGradient[i][j] == 3) {

                    if ((iaAbsoluteGradient[i + 1][j - 1] < iaAbsoluteGradient[i][j])
                            && (iaAbsoluteGradient[i - 1][j + 1] < iaAbsoluteGradient[i][j])) {

                        iaReturn[i][j] = iaAbsoluteGradient[i][j];

                    } else {

                        if ((iaAbsoluteGradient[i + 1][j - 1] < iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i - 1][j + 1] == iaAbsoluteGradient[i][j])) {

                            int tempj = j;
                            int tempi = i;

                            while ((tempj + 1) < iaAbsoluteGradient[0].length && (tempi - 1) > 0 && iaDirectionOfGradient[tempi - 1][tempj + 1] == 1) {

                                if (iaAbsoluteGradient[tempi - 1][tempj + 1] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[tempi - 1][tempj + 1] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempj = tempj + 1;
                                tempi = tempi - 1;
                            }

                        }

                        if ((iaAbsoluteGradient[i + 1][j - 1] == iaAbsoluteGradient[i][j])
                                && (iaAbsoluteGradient[i - 1][j + 1] < iaAbsoluteGradient[i][j])) {

                            int tempj = j;
                            int tempi = i;

                            while ((tempj - 1) > 0 && (tempi + 1) < iaAbsoluteGradient.length && iaDirectionOfGradient[tempi + 1][tempj - 1] == 1) {

                                if (iaAbsoluteGradient[tempi + 1][tempj - 1] > iaAbsoluteGradient[i][j]) {
                                    break;
                                }

                                if (iaAbsoluteGradient[tempi + 1][tempj - 1] < iaAbsoluteGradient[i][j]) {
                                    iaReturn[i][j] = iaAbsoluteGradient[i][j];
                                    break;
                                }

                                tempj = tempj - 1;
                                tempi = tempi + 1;
                            }

                        }
                    }

                }

            }

        }

        return iaReturn;

    }
    
    public static double sobelOperator(ImageGrid oGrid, ImagePoint oPoint){
        List<ImagePoint> loN = oGrid.getNeighborsN8(oPoint);
        double Gx = 0;
        double Gy = 0;
        if(loN.get(0)!= null) Gx += 2*loN.get(0).iValue;
        if(loN.get(1)!= null) Gx += loN.get(1).iValue;
        if(loN.get(3)!= null) Gx -= loN.get(3).iValue;
        if(loN.get(4)!= null) Gx -= 2*loN.get(4).iValue;
        if(loN.get(5)!= null) Gx -= loN.get(5).iValue;
        if(loN.get(7)!= null) Gx += loN.get(7).iValue;
        
        if(loN.get(1)!= null) Gy -= loN.get(1).iValue;
        if(loN.get(2)!= null) Gy -= 2*loN.get(2).iValue;
        if(loN.get(3)!= null) Gy -= loN.get(3).iValue;
        if(loN.get(5)!= null) Gy += loN.get(5).iValue;
        if(loN.get(6)!= null) Gy += 2*loN.get(6).iValue;
        if(loN.get(7)!= null) Gy += loN.get(7).iValue;
        
        return Math.sqrt(Gx*Gx+Gy*Gy);
        
    }        
    
}
