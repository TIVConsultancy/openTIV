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

import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018.CNCP;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.interfaces.Value;
import com.tivconsultancy.opentiv.math.primitives.BasicMathLib;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class LevelSet {

    public static ImageGridDouble fastMarching(ImageGrid oEdges) {
        ImagePointDouble[] opFastMarching = new ImagePointDouble[oEdges.oa.length];
        ImageGridDouble oFM = new ImageGridDouble(oEdges.iLength, oEdges.jLength);
        oFM.oa = opFastMarching;
        for (int i = 0; i < oEdges.oa.length; i++) {
            ImagePoint op = oEdges.oa[i];
            if (op.iValue > 0) {
                oFM.oa[i] = new ImagePointDouble(i, 0.0, oFM);
            } else {
                oFM.oa[i] = new ImagePointDouble(i, null, oFM);
            }
        }

        List<EnumObject> loSet = new ArrayList<>();
        boolean bRun = true;

        int iCountRun = 0;
        while (bRun) {
            bRun = false;
            for (int i = 0; i < oFM.oa.length; i++) {
                ImagePointDouble op = (ImagePointDouble) oFM.oa[i];
                List<ImagePointDouble> loN4 = oFM.getNeighborsN4Double(op);
                //Points with a distance are not considered
                if (op.dValue != null) {
                    continue;
                }
                if (loN4.get(0) == null || loN4.get(1) == null || loN4.get(2) == null || loN4.get(3) == null) {
                    continue;
                }
                int iCountValid = 0;
                if (loN4.get(0).dValue != null) {
                    iCountValid++;
                }
                if (loN4.get(1).dValue != null) {
                    iCountValid++;
                }
                if (loN4.get(2).dValue != null) {
                    iCountValid++;
                }
                if (loN4.get(3).dValue != null) {
                    iCountValid++;
                }

                //Points that are non-boundary points are not considered
                if (iCountValid < 1) {
                    continue;
                }

                bRun = true;

                Double Dxplus;
                Double Dxminus;
                Double Dyplus;
                Double Dyminus;

                if (loN4.get(0).dValue != null) {
                    Dxplus = (double) loN4.get(0).dValue;
                } else {
                    Dxplus = Double.NEGATIVE_INFINITY;
                }

                if (loN4.get(2).dValue != null) {
                    Dxminus = (double) loN4.get(2).dValue;
                } else {
                    Dxminus = Double.NEGATIVE_INFINITY;
                }

                if (loN4.get(1).dValue != null) {
                    Dyplus = (double) loN4.get(1).dValue;
                } else {
                    Dyplus = Double.NEGATIVE_INFINITY;
                }

                if (loN4.get(3).dValue != null) {
                    Dyminus = (double) loN4.get(3).dValue;
                } else {
                    Dyminus = Double.NEGATIVE_INFINITY;
                }

                if (Double.isInfinite(Dxplus) && Double.isInfinite(Dxminus)) {
                    Double b = -1.0 * Math.max(Dyplus, Math.max(Dyminus, 0));
                    Double p = 2.0 * b;
                    Double q = (Math.pow(b, 2) - 1);
                    Double d = BasicMathLib.pqformulat(p, q).x;
                    loSet.add(new EnumObject((double) i, d));
                    continue;
                }
                if (Double.isInfinite(Dyplus) && Double.isInfinite(Dyminus)) {
                    Double a = -1.0 * Math.max(Dxplus, Math.max(Dxminus, 0));
                    Double p = 2.0 * a;
                    Double q = (Math.pow(a, 2) - 1);
                    Double d = BasicMathLib.pqformulat(p, q).x;
                    loSet.add(new EnumObject((double) i, d));
                    continue;
                }

                if (!Double.isInfinite(Dxplus) && !Double.isInfinite(Dxminus)) {
                    if (Double.isInfinite(Dyplus) || Double.isInfinite(Dyminus)) {
                        Double a = -1.0 * Dxplus;
                        Double b = -1.0 * Math.max(Dyplus, Math.max(Dyminus, 0));
                        Double p = a + b;
                        Double q = (Math.pow(a, 2) + Math.pow(b, 2) - 1) / 2.0;
                        if (BasicMathLib.pqformulat(p, q) == null) {
                            a = -1.0 * Dxminus;
                            b = -1.0 * Math.max(Dyplus, Math.max(Dyminus, 0));
                            p = a + b;
                            q = (Math.pow(a, 2) + Math.pow(b, 2) - 1) / 2.0;
                        }
                        Double d = BasicMathLib.pqformulat(p, q).x;
                        loSet.add(new EnumObject((double) i, d));
                    } else {
                        Double d = (Dxplus + Dxminus + Dyminus + Dyplus) / 4.0;
                        loSet.add(new EnumObject((double) i, d));
                    }
                    continue;
                }

                if (!Double.isInfinite(Dyplus) && !Double.isInfinite(Dyminus)) {
                    if (Double.isInfinite(Dxplus) || Double.isInfinite(Dxminus)) {
                        Double a = -1.0 * Math.max(Dxplus, Math.max(Dxminus, 0));
                        Double b = -1.0 * Dyplus;

                        Double p = a + b;
                        Double q = (Math.pow(a, 2) + Math.pow(b, 2) - 1) / 2.0;
                        if (BasicMathLib.pqformulat(p, q) == null) {
                            a = -1.0 * Math.max(Dxplus, Math.max(Dxminus, 0));
                            b = -1.0 * Dyminus;

                            p = a + b;
                            q = (Math.pow(a, 2) + Math.pow(b, 2) - 1) / 2.0;
                        }
                        Double d = BasicMathLib.pqformulat(p, q).x;
                        loSet.add(new EnumObject((double) i, d));
                    } else {
                        Double d = (Dxplus + Dxminus + Dyminus + Dyplus) / 4.0;
                        loSet.add(new EnumObject((double) i, d));
                    }
                    continue;
                }

                Double a = -1.0 * Math.max(Dxplus, Math.max(Dxminus, 0));
                Double b = -1.0 * Math.max(Dyplus, Math.max(Dyminus, 0));

                Double p = a + b;
                Double q = (Math.pow(a, 2) + Math.pow(b, 2) - 1) / 2.0;
                if (BasicMathLib.pqformulat(p, q) == null) {
                    System.out.println(a);
                    System.out.println(b);
                    System.out.println(p);
                    System.out.println(q);
                }

                Double d = BasicMathLib.pqformulat(p, q).x;
                loSet.add(new EnumObject((double) i, d));
            }

            for (EnumObject oSet : loSet) {
                ((ImagePointDouble) oFM.oa[(int) oSet.dEnum.doubleValue()]).dValue = (Double) oSet.o;
            }
//            if (iCountRun > 4) bRun = false;        
            iCountRun++;
        }        
        
        return oFM;

    }
    
    public static ImageGridDouble signLevelSet(ImageGridDouble lvlSet){
        List<ImagePoint> loNegative = new ArrayList<>();
        List<ImagePoint> loPointsOnCurve = new ArrayList<>();
        for(ImagePoint o : lvlSet.oa){
            if( o != null && ((ImagePointDouble) o).dValue != null && ((ImagePointDouble) o).dValue == 0){
                loPointsOnCurve.add(o);
            }
        }
        loNegative = CNCP.fillArea(loPointsOnCurve);
        for(ImagePoint o : loNegative){
            ((ImagePointDouble) lvlSet.oa[o.i]).dValue = -1.0 * ((ImagePointDouble) lvlSet.oa[o.i]).dValue; 
        }
        
        return lvlSet;
    }

    public static ImageGridDouble getCurvature(ImageGrid olvlSet) {
        ImageGridDouble oCurv = new ImageGridDouble(olvlSet.iLength, olvlSet.jLength);
        Value oValue = (Value) (Object pParameter) -> ((ImagePointDouble) pParameter).dValue;
        for (int i = 0; i< oCurv.oa.length; i++) {        
            oCurv.oa[i] = new ImagePointDouble( i, Double.NaN, olvlSet);            
        }

        for (ImagePoint o : olvlSet.oa) {
            if (o != null && ((ImagePointDouble) o).dValue != null && ((ImagePointDouble) o).dValue == 0) {
                
                System.out.println(o);

                List<ImagePoint> loN4 = olvlSet.getNeighborsN4(o);

                Double dFWDxP = olvlSet.forwardDifference(o, "x", "+", oValue);
                Double dFWDxM = olvlSet.forwardDifference(o, "x", "-", oValue);
                Double dFWDyP = olvlSet.forwardDifference(o, "y", "+", oValue);
                Double dFWDyM = olvlSet.forwardDifference(o, "y", "-", oValue);                

                Double dCDxP;
                //CentralDifference x+ of averaged value
                {
                    Double dAvgxPlRight = olvlSet.forwardAverage(loN4.get(0), "x", "+", oValue);
                    Double dAvgxPlLeft = olvlSet.forwardAverage(loN4.get(2), "x", "+", oValue);
                    dCDxP = (dAvgxPlRight - dAvgxPlLeft)/2.0;
                }
                
                Double dCDxM;
                //CentralDifference x- of averaged value
                {
                    Double dAvgxPlRight = olvlSet.forwardAverage(loN4.get(0), "x", "-", oValue);
                    Double dAvgxPlLeft = olvlSet.forwardAverage(loN4.get(2), "x", "-", oValue);
                    dCDxM = (dAvgxPlRight - dAvgxPlLeft)/2.0;
                }
                
                Double dCDyP;
                //CentralDifference y+ of averaged value
                {
                    Double dAvgyPlRight = olvlSet.forwardAverage(loN4.get(1), "y", "+", oValue);
                    Double dAvgyPlLeft = olvlSet.forwardAverage(loN4.get(3), "y", "+", oValue);
                    dCDyP = (dAvgyPlRight - dAvgyPlLeft)/2.0;
                }
                
                Double dCDyM;
                //CentralDifference y- of averaged value
                {
                    Double dAvgyPlRight = olvlSet.forwardAverage(loN4.get(1), "y", "-", oValue);
                    Double dAvgyPlLeft = olvlSet.forwardAverage(loN4.get(3), "y", "-", oValue);
                    dCDyM = (dAvgyPlRight - dAvgyPlLeft)/2.0;
                }
                
                Double dNxP = dFWDxP / Math.sqrt( Math.pow(dFWDxP,2) + Math.pow(dCDyP,2) ) ;
                Double dNxM = dFWDxM / Math.sqrt( Math.pow(dFWDxM,2) + Math.pow(dCDyM,2) ) ;
                
                Double dNyP = dFWDyP / Math.sqrt( Math.pow(dFWDyP,2) + Math.pow(dCDxP,2) ) ;
                Double dNyM = dFWDyM / Math.sqrt( Math.pow(dFWDyM,2) + Math.pow(dCDxM,2) ) ;
                
                ((ImagePointDouble) oCurv.oa[o.i]).dValue = (dNxP - dNxM) + (dNyM - dNyP);
            }
        }
        
        return oCurv;

    }
    
    public static ImageGridDouble getCurvatureClassic(ImageGrid olvlSet) {
        ImageGridDouble oCurv = new ImageGridDouble(olvlSet.iLength, olvlSet.jLength);
        Value oValue = (Value) (Object pParameter) -> ((ImagePointDouble) pParameter).dValue;
        for (int i = 0; i< oCurv.oa.length; i++) {        
            oCurv.oa[i] = new ImagePointDouble( i, Double.NaN, olvlSet);            
        }

        for (ImagePoint o : olvlSet.oa) {
            if (o != null && ((ImagePointDouble) o).dValue != null && ((ImagePointDouble) o).dValue == 0) {
                Double dx = olvlSet.centralDifference(o, "x", oValue);
                Double dy = olvlSet.centralDifference(o, "y", oValue);
                Double dxy = olvlSet.centralDifferenceMixed(o, oValue);
                Double dxx = olvlSet.centralDifferenceSecondDeri(o, "x", oValue);
                Double dyy = olvlSet.centralDifferenceSecondDeri(o, "y", oValue);
                
                Double dCurv = ( dxx*Math.pow(dy,2) - 2*dy*dx*dxy + dyy*Math.pow(dx, 2) ) /Math.pow(Math.pow(dx,2) + Math.pow(dy,2), 3.0/2.0);
                
                ((ImagePointDouble) oCurv.oa[o.i]).dValue = ( dxx*Math.pow(dy,2) - 2*dy*dx*dxy + dyy*Math.pow(dx, 2) ) /Math.pow(Math.pow(dx,2) + Math.pow(dy,2), 3.0/2.0);
            }
        }
        return oCurv;
    }

    public static class ImageGridDouble extends ImageGrid {

        public ImageGridDouble(int iLength, int jLength) {
            super(iLength, jLength);
        }

        public OrderedPair getPosDouble(ImagePoint o) {
            if (o == null || o.i == null) {
                return null;
            }
            Integer Indexi = ((int) o.i / this.jLength);
            Integer Indexj = o.i - Indexi * this.jLength;
            OrderedPair opPos;
            if (((ImagePointDouble) o).dValue != null) {
                opPos = new OrderedPair(Indexj, Indexi, ((ImagePointDouble) o).dValue);
            } else {
                opPos = new OrderedPair(Indexj, Indexi, -1.0);
            }

            return opPos;
        }

        public ArrayList<ImagePointDouble> getNeighborsN4Double(ImagePointDouble o) {
            /*
             Resturn array list with counter clockwise order:
             ---1---
             2-- --0
             ---3---
        
             Values are null when on boundary
             */

            OrderedPair oRef = this.getPos(o);
            ArrayList<ImagePointDouble> lo = new ArrayList<>();
            //0
            if ((oRef.x + 1) == this.jLength) {
                lo.add(null);
            } else {
                lo.add((ImagePointDouble) oa[o.i + 1]);
            }
            //1
            if (oRef.y == 0) {
                lo.add(null);
            } else {
                lo.add((ImagePointDouble) oa[o.i - this.jLength]);
            }
            //2
            if (oRef.x == 0) {
                lo.add(null);
            } else {
                lo.add((ImagePointDouble) oa[o.i - 1]);
            }
            //3
            if (((oRef.y + 1) == this.iLength)) {
                lo.add(null);
            } else {
                lo.add((ImagePointDouble) oa[o.i + this.jLength]);
            }
            return lo;
        }

    }

    public static class ImagePointDouble extends ImagePoint {

        Double dValue;

        public ImagePointDouble(int i, Double dValue, ImageGrid oGrid) {
            super(i, -1, oGrid);
            this.dValue = dValue;
        }

    }

}
