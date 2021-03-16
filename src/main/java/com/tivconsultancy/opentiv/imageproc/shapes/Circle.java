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
package com.tivconsultancy.opentiv.imageproc.shapes;

import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Morphology;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class Circle implements Shape, Serializable {

    private static final long serialVersionUID = 8093116653258390116L;

    public MatrixEntry meCenter;
    public OrderedPair opSubPixelCenter;
    public double dDiameterI;
    public double dDiameterJ;
    public double dCircStep = 50;
    public List<MatrixEntry> lmeCircle = new ArrayList<MatrixEntry>();
    //Preventing heap space errors
    //-----------------------
    int iMaxLength = 10000;
    int iMax_Lengthi = 1000;
    int iMax_Lengthj = 1000;
    //-----------------------
    public Double dAngle = null;
    public Double dAvergeGreyDerivative=null;

    public Circle(MatrixEntry meCenter, double dDiameter) {
        this.meCenter = meCenter;
        this.dDiameterI = dDiameter;
        this.dDiameterJ = dDiameter;
        dCircStep = (Math.PI * dDiameter); //(int) Math.ceil(4.0/dDiameter);
        this.dAngle = 0.0;
        this.dAvergeGreyDerivative=0.0;
        paint();
    }

//    public Circle(OrderdPair opCenter, double dDiameter) {
//        this.opSubPixelCenter = opCenter;
//        this.meCenter = new MatrixEntry(opCenter);
//        this.dDiameterI = dDiameter;
//        this.dDiameterJ = dDiameter;
//        dCircStep = (Math.PI * dDiameter); //(int) Math.ceil(4.0/dDiameter);
//        paint();
//    }
    public Circle(MatrixEntry meCenter, double dDiameterI, double dDiameterJ) {
        this.meCenter = meCenter;
        this.dDiameterI = dDiameterI;
        this.dDiameterJ = dDiameterJ;
        this.dAngle = 0.0;
        this.dAvergeGreyDerivative=0.0;
        dCircStep = (Math.PI * Math.max(dDiameterJ, dDiameterI)); //(int) Math.ceil(4.0/dDiameter);
        paint();
    }

    public Circle(MatrixEntry meCenter, double dDiameterI, double dDiameterJ, Double dAngle) {
        this.meCenter = meCenter;
        this.dDiameterI = dDiameterI;
        this.dDiameterJ = dDiameterJ;
        dCircStep = (Math.PI * Math.max(dDiameterJ, dDiameterI)); //(int) Math.ceil(4.0/dDiameter);
        this.dAngle = dAngle;
        this.dAvergeGreyDerivative=0.0;
        paint();
    }

    public Double getCircumference() {
        //Ramajujan approx
        double a = (Math.max(this.dDiameterI, this.dDiameterJ)) / 2.0;
        double b = (Math.min(this.dDiameterI, this.dDiameterJ)) / 2.0;
        double h = (a - b) * (a - b) / ((a + b) * (a + b));
        return Math.PI * (a + b) * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));
    }

    public final void paint() {
        if (meCenter.j + (int) dDiameterJ <= 0 || meCenter.i + (int) dDiameterI <= 0) {
            return;
        }
        if (dDiameterJ > this.iMax_Lengthj || dDiameterI > this.iMax_Lengthi) {
            return;
        }
        BufferedImage oBuff = new BufferedImage(meCenter.j + (int) dDiameterJ, meCenter.i + (int) dDiameterI, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = (Graphics2D) oBuff.getGraphics();
        g.setColor(Color.WHITE);
        g.rotate(this.dAngle, meCenter.j, meCenter.i);
        g.drawOval(meCenter.j - (int) (dDiameterJ / 2.0), meCenter.i - (int) (dDiameterI / 2.0), (int) dDiameterJ, (int) dDiameterI);
        ImageInt oImage = new ImageInt(oBuff);
        for (int i = 0; i < oImage.iaPixels.length; i++) {
            for (int j = 0; j < oImage.iaPixels[0].length; j++) {
                if (oImage.iaPixels[i][j] > 127) {
//                    OrderedPair oRot = new OrderedPair((double) j - meCenter.j, (double) i - meCenter.i);
//                    oRot.rotateAroundZero(this.dAngle);
//                    this.lmeCircle.add(new MatrixEntry(oRot.y + meCenter.i, oRot.x + meCenter.j));
                    this.lmeCircle.add(new MatrixEntry(i, j));
                }
            }
        }
    }

    public double checkpoint(MatrixEntry me) {
        double h = 0;
        double k = 0;
        double a = this.dDiameterJ / 2.0;
        double b = this.dDiameterI / 2.0;

        OrderedPair opCheck = new OrderedPair(me.j, me.i);

        OrderedPair OrderedPair_Transformed_1 = new OrderedPair(opCheck.x - this.meCenter.j, opCheck.y - this.meCenter.i);
        OrderedPair_Transformed_1.rotateAroundZero(-this.dAngle);
        double x = OrderedPair_Transformed_1.x;
        double y = OrderedPair_Transformed_1.y;
//       MatrixEntry meCheck = new MatrixEntry((int) y+this.meCenter.i, (int) x+this.meCenter.j);
//       if(this.lmeCircle.contains(meCheck)) return 1.0;

        double p = (Math.pow((x - h), 2) / Math.pow(a, 2))
                + (Math.pow((y - k), 2) / Math.pow(b, 2));

        return p;
    }

//   public static int[][][] getHoughTransformation(int[][] iaEdges, int iMinMajor, int iMaxMajor, int iMinMinor, int iMaxMinor){
//       int[][][] iaVote = new int[iaEdges.length][iaEdges[0].length][iMaxRadius - iMinRadius + 1];
//   }
    public static int[][][] getHoughTransformation_Cirlces(ImageInt iaEdges, int iMinRadius, int iMaxRadius) {
        int[][][] iaVote = new int[iMaxRadius][iaEdges.iaPixels.length][iaEdges.iaPixels[0].length];
//       int[][] iaVote = new int[iaEdges.iaPixels.length][iaEdges.iaPixels[0].length];
        for (int i = 0; i < iaEdges.iaPixels.length; i++) {
            for (int j = 0; j < iaEdges.iaPixels[0].length; j++) {
                if (iaEdges.iaPixels[i][j] > 127) {
                    for (int r = iMinRadius; r < iMaxRadius; r++) {
                        for (int t = 0; t < 360; t++) {
                            int a = (int) (j - r * Math.cos(t * Math.PI / 180));
                            int b = (int) (i - r * Math.sin(t * Math.PI / 180));
                            if (a < 0 || a > iaVote[0].length - 1 || b < 0 || b > iaVote[0][0].length - 1) {
                                continue;
                            }
                            iaVote[r][a][b]++;
                        }
                    }
                }
            }
        }
        return iaVote;
    }

    public static int[][][] getHoughTransformation_Cirlces_TestForEllipses(ImageInt iaEdges, int iMinRadius, int iMaxRadius, int iTolerance) {
        int[][][] iaVote = new int[(int) (iMaxRadius * 1.5 + 1)][iaEdges.iaPixels.length][iaEdges.iaPixels[0].length];
//       int[][] iaVote = new int[iaEdges.iaPixels.length][iaEdges.iaPixels[0].length];
        for (int i = 0; i < iaEdges.iaPixels.length; i++) {
            for (int j = 0; j < iaEdges.iaPixels[0].length; j++) {

                int iLeft = i - iMaxRadius < 0 ? 0 : i - iMaxRadius;
                int iRight = i + iMaxRadius > iaEdges.iaPixels.length ? iaEdges.iaPixels.length : i + iMaxRadius;

                int jLeft = j - iMaxRadius < 0 ? 0 : j - iMaxRadius;
                int jRight = j + iMaxRadius > iaEdges.iaPixels[0].length ? iaEdges.iaPixels[0].length : j + iMaxRadius;

                for (int t = iLeft; t < iRight; t++) {
                    for (int z = jLeft; z < jRight; z++) {

                        int tLeftSub = t - iTolerance < 0 ? 0 : t - iTolerance;
                        int tRightSub = t + iTolerance > iaEdges.iaPixels.length ? iaEdges.iaPixels.length : t + iTolerance;

                        int zLeftSub = z - iTolerance < 0 ? 0 : z - iTolerance;
                        int zRightSub = z + iTolerance > iaEdges.iaPixels[0].length ? iaEdges.iaPixels[0].length : z + iTolerance;

                        for (int tsub = tLeftSub; tsub < tRightSub; tsub++) {
                            for (int zsub = zLeftSub; zsub < zRightSub; zsub++) {
                                if (iaEdges.iaPixels[tsub][zsub] > 127) {
                                    int iNorm = (int) Math.round(Math.sqrt(((i - t) * (i - t)) + ((j - z) * (j - z))));
                                    if (iNorm < iMinRadius) {
                                        continue;
                                    }
                                    iaVote[iNorm][i][j] = iaVote[iNorm][i][j] + 1;
//                           iaVote[i][j] = iaVote[i][j] + 1; 
                                }

                            }

                        }
                    }
                }
            }
        }
        return iaVote;
    }

    public final void paint2() {

        if (dDiameterI == dDiameterJ) {
            if (dDiameterI <= 4 && ((dDiameterI % 2) == 0)) {
                for (int i = (int) Math.floor(-1.0 * dDiameterI / 2.0); i <= Math.ceil(dDiameterI / 2.0); i++) {
                    for (int j = (int) Math.floor(-1.0 * dDiameterI / 2.0); j <= Math.ceil(dDiameterI / 2.0); j++) {
                        if (!(Math.abs(i) == dDiameterI / 2.0 && Math.abs(j) == dDiameterI / 2.0) && (Math.abs(i) == dDiameterI / 2.0 || Math.abs(j) == dDiameterI / 2.0 || (Math.abs(i - 1) == dDiameterI / 2.0 - 1 && Math.abs(j - 1) == dDiameterI / 2.0 - 1))) {
                            if (!(i == 0 && j == 0)) {
                                lmeCircle.add(new MatrixEntry(meCenter.i + i, meCenter.j + j));
                            }
                        }
                    }
                }
                return;
            }
        }

        double dStep = 2 * Math.PI / dCircStep;

        MatrixEntry mePrevStep = new MatrixEntry();
        double dTheta = 0;
        mePrevStep.j = (int) (dDiameterJ / 2 * Math.cos(dTheta));
        mePrevStep.i = (int) (dDiameterI / 2 * Math.sin(dTheta));
        int iCount = 0;

        if (this.dAngle != null) {
            OrderedPair oRot = new OrderedPair((double) mePrevStep.j, (double) mePrevStep.i);
            oRot.rotateAroundZero(this.dAngle);
            mePrevStep = new MatrixEntry((int) oRot.y, (int) oRot.x);
        }

        mePrevStep.j = mePrevStep.j + meCenter.j;
        mePrevStep.i = mePrevStep.i + meCenter.i;

        for (dTheta = dStep; dTheta < 2 * Math.PI; dTheta += dStep) {
            MatrixEntry meStep = new MatrixEntry();
            meStep.j = (int) (dDiameterJ / 2 * Math.cos(dTheta));
            meStep.i = (int) (dDiameterI / 2 * Math.sin(dTheta));
            if (this.dAngle != null) {
                OrderedPair oRot = new OrderedPair((double) meStep.j, (double) meStep.i);
                oRot.rotateAroundZero(this.dAngle);
                meStep = new MatrixEntry((int) oRot.y, (int) oRot.x);
            }

            meStep.j = meStep.j + meCenter.j;
            meStep.i = meStep.i + meCenter.i;

            lmeCircle.add(meStep);
            Line oCircLine = new Line(mePrevStep, meStep);
            for (MatrixEntry op : oCircLine.lmeLine) {
                lmeCircle.add(op);
            }

            lmeCircle.addAll(oCircLine.lmeLine);
            mePrevStep = meStep;
            iCount++;
            if (iCount > iMaxLength) {
                break;
            }
        }

        //MatrixEntry.doUniqueME(lmeCircle);
    }

    @Override
    public double getSize() {
        return (dDiameterI + dDiameterJ) / 2.0;
    }

    @Override
    public double getMinorAxis() {
        return Math.min(dDiameterI, dDiameterJ);
    }

    @Override
    public double getMajorAxis() {
        return Math.max(dDiameterI, dDiameterJ);
    }

    @Override
    public double getFormRatio() {
        double dMajor = getMajorAxis();
        double dMinor = getMinorAxis();
        if (dMajor == 0) {
            return Double.MAX_VALUE;
        }
        return dMinor / dMajor;
    }

    @Override
    public int getPixelCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getOrientationAngle() {
        return dAngle;
    }

    public String getOutputString() {
        // x,y,major axis, minor axis, orientation angle
        return this.meCenter.j + "," + this.meCenter.i + "," + "0.0" + "," + this.getMajorAxis() + "," + this.getMinorAxis() + "," + this.getOrientationAngle();
    }
    
    public List<MatrixEntry> getAreaCircle(){
        this.paint();
        double maxLenght = Math.max(this.dDiameterI, this.dDiameterJ);
        ImageInt img = new ImageInt((int) maxLenght+5, (int) maxLenght+5, 0);
        img.setPoints(lmeCircle, 255);
        List<MatrixEntry> lmeFill = (new Morphology()).markFillN4(img, 0, 0);
        img.setPoints(lmeFill, 255);
        List<MatrixEntry> lmeArea = new ArrayList<>();
        for(MatrixEntry me : this.lmeCircle){
            lmeArea.add(me);
        }
        img.iterate(new ImageInt.IterativeFunction() {
            @Override
            public void perform(int i, int j) {
                if(img.iaPixels[i][j] == 0){
                    lmeArea.add(new MatrixEntry(i, j));
                }
            }
        });
        return lmeArea;
    }
    
    public static void writeOut(List<Circle> lC,String sOutput){
        List<String> lsOut = new ArrayList<>();
        for (Circle circle : lC) {
            lsOut.add(circle.getOutputString());
        }
        lsOut.add(0,"x,y,z,major axis, minor axis, orientation angle");
        new Writer(sOutput).writebigString(lsOut);
    }

}
