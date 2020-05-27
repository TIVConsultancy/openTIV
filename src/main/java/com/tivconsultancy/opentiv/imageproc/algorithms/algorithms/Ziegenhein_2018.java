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

import static com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Gao_2018.checkIfEndCNCP;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.imageproc.shapes.Line2;
import com.tivconsultancy.opentiv.imageproc.shapes.Splines;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Ziegenhein_2018 {

    public static ImageGrid preProcessorStep1(ImageGrid oGrid) throws EmptySetException {
        Ziegenhein_2018.CNCPNetwork.markCNCPNetwork(oGrid, new Ziegenhein_2018.sortOutRule() {

                                                @Override
                                                public List<CNCP> remove(Ziegenhein_2018.CNCPNetwork o) {
                                                    List<CNCP> loRemove = new ArrayList<>();
                                                    for (CNCP oN : o.lo) {
                                                        if (oN.lo.isEmpty() || oN.lo.size() <= 5) {
                                                            loRemove.add(oN);
                                                        }
                                                    }
                                                    return loRemove;
                                                }
                                            }, 0);
        return oGrid;
    }

    public static ImageGrid preProcessorStep2(ImageGrid oClean) throws EmptySetException {
        //remove all open curves which have are below a certain size or are closer to the boundaries than a certain size
        oClean = Ziegenhein_2018.CNCPNetwork.markOpenCNCP(oClean, new Ziegenhein_2018.sortOutRule3<CNCP>() {

                                                      @Override
                                                      public boolean remove(CNCP o, HashSet<CNCP> lo) {
                                                          boolean bProblem = o.lo.size() <= 5;
                                                          bProblem = bProblem || (o.oStart.getPos().x <= 4 || o.oStart.getPos().y <= 4);
                                                          bProblem = bProblem || (o.oStart.getPos().x >= (o.oStart.getGrid().jLength - 4) || o.oStart.getPos().y >= (o.oStart.getGrid().iLength - 4));
                                                          return bProblem;
                                                      }
                                                  }, 0);
        return oClean;
    }

    public static ImageGrid removeOpenShortCNCP(ImageGrid oGrid, int iMinLength) throws EmptySetException {
        //remove all open curves which have are below a certain size or are closer to the boundaries than a certain size
        oGrid = Ziegenhein_2018.CNCPNetwork.markOpenCNCP(oGrid, new Ziegenhein_2018.sortOutRule3<CNCP>() {

                                                     @Override
                                                     public boolean remove(CNCP o, HashSet<CNCP> lo) {
                                                         boolean bProblem = o.lo.size() <= iMinLength;
                                                         bProblem = bProblem || (o.oStart.getPos().x <= 4 || o.oStart.getPos().y <= 4);
                                                         bProblem = bProblem || (o.oStart.getPos().x >= (o.oStart.getGrid().jLength - 4) || o.oStart.getPos().y >= (o.oStart.getGrid().iLength - 4));
                                                         return bProblem;
                                                     }
                                                 }, 0);
        return oGrid;
    }

    public static ImageGrid removelonelyCNCP(ImageGrid oGrid, int iMinLength, int iMaxDist) throws EmptySetException {
        //remove all open curves which have are below a certain size or are closer to the boundaries than a certain size
        oGrid = Ziegenhein_2018.CNCPNetwork.markOpenCNCP(oGrid, new Ziegenhein_2018.sortOutRule3<CNCP>() {

                                                     @Override
                                                     public boolean remove(CNCP o, HashSet<CNCP> lo) {
                                                         EnumObject oEnum = null;
                                                         try {
                                                             oEnum = o.getclosest(lo);
                                                         } catch (EmptySetException ex) {
                                                             Logger.getLogger(Ziegenhein_2018.class.getName()).log(Level.SEVERE, null, ex);
                                                         }

                                                         return (oEnum == null || oEnum.o == null || (o.lo.size() < iMinLength && oEnum.dEnum > iMaxDist));
                                                     }
                                                 }, 0);
        return oGrid;
    }

    public static ImageGrid oTreatJoints(ImageGrid oGrid) throws EmptySetException {
        //get The somoothest Pair of Joints, all Joints are from now on cleared
        int iMaxLoop = 3;
        while (!Ziegenhein_2018.getJoints(oGrid).isEmpty() && iMaxLoop > 1) {

            oGrid = Ziegenhein_2018.CNCPNetwork.markCNCPNetwork(oGrid, new Ziegenhein_2018.sortOutRule() {

                                                            @Override
                                                            public List<Ziegenhein_2018.CNCP> remove(Ziegenhein_2018.CNCPNetwork o) {
                                                                List<Ziegenhein_2018.CNCP> loRemove = new ArrayList<>();
                                                                loRemove.addAll(o.lo);
                                                                try {
                                                                    List<CNCP> loPair = o.getSmoothestPair();
                                                                    loRemove.removeAll(loPair);
                                                                } catch (EmptySetException ex) {
                                                                    Logger.getLogger(Ziegenhein_2018.class.getName()).log(Level.SEVERE, null, ex);
                                                                }

                                                                for (CNCP oCNCP : o.lo) {
                                                                    if (oCNCP.lo.size() < 5) {
                                                                        loRemove.add(oCNCP);
                                                                    }
                                                                }

                                                                return loRemove;
                                                            }
                                                        }, 0);
            oGrid.resetMarkers();
            iMaxLoop--;
        }
        return oGrid;
    }

    public static ImageGrid closeOpenContour(ImageGrid oGrid, int iDist) throws EmptySetException {
        //close all countours which have an end point closer than 5 pixels
        oGrid = Ziegenhein_2018.CNCPNetwork.OpenCNCP(oGrid, new Ziegenhein_2018.RuleWithAction<CNCP>() {

                                                 @Override
                                                 public boolean isValid(CNCP o, HashSet<CNCP> lo) {
                                                     if (o != null && o.oStart != null && o.oEnd != null) {
                                                         return (o.oStart.getPos().getNorm(o.oEnd.getPos()) <= iDist);

                                                     }
                                                     return false;
                                                 }

                                                 @Override
                                                 public void operate(CNCP o) {
                                                     if (o.oStart == null || o.oEnd == null) {
                                                         return;
                                                     }
                                                     if (o.lo.size() < 5) {
                                                         return;
                                                     }
                                                     o.closeEndPointsSmooth(2, 255, null);
                                                 }
                                             });
        return oGrid;
    }
    
    public static ImageGrid closeOpenContourLinear(ImageGrid oGrid, int iDist) throws EmptySetException {
        //close all countours which have an end point closer than 5 pixels
        oGrid = Ziegenhein_2018.CNCPNetwork.OpenCNCP(oGrid, new Ziegenhein_2018.RuleWithAction<CNCP>() {

                                                 @Override
                                                 public boolean isValid(CNCP o, HashSet<CNCP> lo) {
                                                     if (o != null && o.oStart != null && o.oEnd != null) {
                                                         return (o.oStart.getPos().getNorm(o.oEnd.getPos()) <= iDist);

                                                     }
                                                     return false;
                                                 }

                                                 @Override
                                                 public void operate(CNCP o) {
                                                     if (o.oStart == null || o.oEnd == null) {
                                                         return;
                                                     }
                                                     if (o.lo.size() < 5) {
                                                         return;
                                                     }
                                                     o.closeEndPointsLinear(255, null);
                                                 }
                                             });
        return oGrid;
    }

    public static ImageGrid connectContours(ImageGrid oGrid, int iDist) throws EmptySetException {

        oGrid = Ziegenhein_2018.CNCPNetwork.OpenCNCP(oGrid, new Ziegenhein_2018.RuleWithDoubleAction<CNCP>() {

                                                 @Override
                                                 public List<CNCP> isValid(CNCP o, HashSet<CNCP> lo) {

                                                     List<CNCP> loReturn = new ArrayList<>();
                                                     try {
                                                         for (EnumObject oEnum : o.getclosest(lo, iDist)) {
                                                             if (oEnum.dEnum > 1.5 && oEnum.o != null) {
                                                                 loReturn.add((CNCP) oEnum.o);
                                                             }
                                                         }
                                                     } catch (EmptySetException ex) {
                                                         Logger.getLogger(Ziegenhein_2018.class.getName()).log(Level.SEVERE, null, ex);
                                                     }

                                                     return loReturn;

                                                 }

                                                 @Override
                                                 public void operate(CNCP o1, List<CNCP> lo2, ImageGrid oGrid) {
                                                     for (CNCP o2 : lo2) {
                                                         if (o1.oStart == null || o1.oEnd == null || o2.oStart == null || o2.oEnd == null) {
                                                             return;
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
                                                             Logger.getLogger(Ziegenhein_2018.class.getName()).log(Level.SEVERE, null, ex);
                                                         }
                                                         if (oHelp != null) {
                                                             oHelp = (EnumObject) oHelp.o;
                                                             Line2 oLine = new Line2((ImagePoint) ((Object[]) oHelp.o)[0], (ImagePoint) ((Object[]) oHelp.o)[1]);
                                                             oLine.setLine(oGrid, 255);
                                                         }
                                                     }
                                                 }
                                             });
        return oGrid;
    }

    public static ImageGrid connectContours2(ImageGrid oGrid, int iDist) throws EmptySetException {

        oGrid = Ziegenhein_2018.CNCPNetwork.OpenCNCP(oGrid, new Ziegenhein_2018.RuleWithDoubleAction<CNCP>() {

                                                 @Override
                                                 public List<CNCP> isValid(CNCP o, HashSet<CNCP> lo) {

                                                     List<CNCP> loReturn = new ArrayList<>();
                                                     try {
                                                         for (EnumObject oEnum : o.getclosest(lo, iDist)) {
                                                             if (oEnum.dEnum > 1.5 && oEnum.o != null) {
                                                                 loReturn.add((CNCP) oEnum.o);
                                                             }
                                                         }
                                                     } catch (EmptySetException ex) {
                                                         Logger.getLogger(Ziegenhein_2018.class.getName()).log(Level.SEVERE, null, ex);
                                                     }

                                                     return loReturn;

                                                 }

                                                 @Override
                                                 public void operate(CNCP o1, List<CNCP> lo2, ImageGrid oGrid) {
                                                     for (CNCP o2 : lo2) {
                                                         if (o1.oStart == null || o1.oEnd == null || o2.oStart == null || o2.oEnd == null) {
                                                             return;
                                                         }

                                                         if (o1.oStart.getNorm(o2.oEnd) < iDist) {
                                                             Line2 oLine = new Line2(o1.oStart, o2.oEnd);
                                                             oLine.setLine(oGrid, 255);
                                                         }

                                                         if (o1.oStart.getNorm(o2.oStart) < iDist) {
                                                             Line2 oLine = new Line2(o1.oStart, o2.oStart);
                                                             oLine.setLine(oGrid, 255);
                                                         }

                                                         if (o1.oEnd.getNorm(o2.oEnd) < iDist) {
                                                             Line2 oLine = new Line2(o1.oStart, o2.oEnd);
                                                             oLine.setLine(oGrid, 255);
                                                         }

                                                         if (o1.oEnd.getNorm(o2.oStart) < iDist) {
                                                             Line2 oLine = new Line2(o1.oStart, o2.oStart);
                                                             oLine.setLine(oGrid, 255);
                                                         }
                                                     }
                                                 }
                                             });
        return oGrid;
    }

    public static ImageGrid remove(ImageGrid oEdges) throws EmptySetException {

        /*
         Edges have a value > 1, non-edges = 0
         */
        ArrayList<CNCP> loCNCP = new ArrayList<>();
        ImagePoint P = findStartingPoint(oEdges);
        loCNCP.add(new CNCP(P));
        while (P != null) {
            //mark present point
            oEdges.oa[P.i].bMarker = true;
            //check if present point is a joint
            if (checkIfJointPoint(P)) {
                //if so, go to the next starting point
                P = findStartingPoint(oEdges);
                //if no other starting points are found, the while loop will end
                if (P == null) {
                    break;
                }
                //start a new potential not closed contour
                setCNCP(oEdges, loCNCP.get(loCNCP.size() - 1), 0);
                loCNCP.add(new CNCP(P));
            } else {
                //if the present point is not a joint, go to the next one
                OrderedPair opNewPoint = getNextPoint(P);
                //check if there is a next point on the contour
                if (!checkIfNextPointIsValid(P)) {
                    //if there is no next point on the contour search for another starting point
                    P = findStartingPoint(oEdges);
                    //end the loop when no other starting point is found
                    if (P == null) {
                        break;
                    }
                    //start a new not-closed contour
                    setCNCP(oEdges, loCNCP.get(loCNCP.size() - 1), 0);
                    loCNCP.add(new CNCP(P));
                } else {
                    //if there is a next point set it as the present point
                    P = oEdges.oa[P.getGrid().getIndex(opNewPoint)];
                    //end the while loop when the next point is out of the image
                    if (P == null) {
                        break;
                    }
                    //add point to the present contour
                    loCNCP.get(loCNCP.size() - 1).addPoint(P);
                }

            }
        }
        return oEdges;
    }

    public static OrderedPair getNextPoint(ImagePoint pRef) throws EmptySetException {

        List<ImagePoint> lo = pRef.getGrid().getNeighborsN8(pRef);
        List<EnumObject> loHelp = new ArrayList<>();
        for (ImagePoint p : lo) {
            if (p != null && p.iValue > 0 && !p.bMarker) {
                loHelp.add(new EnumObject(pRef.getNorm(p), p));
            }
        }

        if (loHelp.isEmpty()) {
            return null;
        }

        return ((ImagePoint) ((EnumObject) Sorting.getMinCharacteristic(loHelp, new Sorting.Characteristic<EnumObject>() {

                                                                    @Override
                                                                    public Double getCharacteristicValue(EnumObject pParameter) {
                                                                        return pParameter.dEnum;
                                                                    }
                                                                }).o).o).getPos();

//        for (ImagePoint p : lo) {
//            if (p != null && p.iValue > 0 && !p.bMarker) {
//                return pRef.getGrid().getPos(p);
//            }
//        }
//        return null;
    }

    public static OrderedPair getNextPointSpecial2(ImagePoint pRef) {

        List<ImagePoint> lo = pRef.getGrid().getNeighborsN8(pRef);

        for (ImagePoint p : lo) {
            if (p != null && p.iValue > 0 && !p.bMarker) {
                if (checkIfJointPoint(p)) {
                    return pRef.getGrid().getPos(p);
                }
            }
        }

        for (ImagePoint p : lo) {
            if (p != null && p.iValue > 0 && !p.bMarker) {
                return pRef.getGrid().getPos(p);
            }
        }

        return null;
    }

    public static OrderedPair getNextPointWOJoints(ImagePoint pRef) {

        List<ImagePoint> lo = pRef.getGrid().getNeighborsN8(pRef);

        for (ImagePoint p : lo) {
            if (p != null && checkIfJointPoint(p)) {
                for (int i = lo.size() - 1; i >= 0; i--) {
                    p = lo.get(i);
                    if (p != null && p.iValue > 0 && !p.bMarker && !checkIfJointPoint(p)) {
                        return pRef.getGrid().getPos(p);
                    }
                }
            }
            if (p != null && p.iValue > 0 && !p.bMarker && !checkIfJointPoint(p)) {
                return pRef.getGrid().getPos(p);
            }
        }

        return null;

    }

    public static OrderedPair getNextPoint(ImagePoint pRef, ImagePoint pExclude) {
        List<ImagePoint> lo = pRef.getGrid().getNeighborsN8(pRef);

        for (ImagePoint p : lo) {
            if (p != null && p.equals(pExclude)) {
                for (int i = lo.size() - 1; i >= 0; i--) {
                    p = lo.get(i);
                    if (p != null && p.iValue > 0 && !p.bMarker && !checkIfJointPoint(p)) {
                        return pRef.getGrid().getPos(p);
                    }
                }
            }
            if (p != null && p.iValue > 0 && !p.bMarker && !p.equals(pExclude)) {
                return pRef.getGrid().getPos(p);
            }
        }
        return null;
    }

    public static OrderedPair getNextPointSpecial(ImagePoint pRef, ImagePoint Joint) {
        List<ImagePoint> lo = pRef.getGrid().getNeighborsN8(pRef);

        outer:
        for (ImagePoint p : lo) {
            if (p != null && p.iValue > 0 && !p.bMarker && !p.equals(Joint)) {
                List<ImagePoint> loN8Ofp = p.getGrid().getNeighborsN8(p);
                for (ImagePoint oN8Ofp : loN8Ofp) {
                    if (oN8Ofp != null && oN8Ofp.equals(Joint)) {
                        continue outer;
                    }
                }
                return pRef.getGrid().getPos(p);
            }
        }
        return null;
    }

    public static boolean checkIfNextPointIsValid(ImagePoint P) {
        if (P.getGrid().oa[P.i].iValue == 0) {
            return false;
        }
        return !P.getGrid().oa[P.i].bMarker;
    }

    public static CNCP getCNCPToStartingPoint(ImagePoint oStartingPoint) throws EmptySetException {
        CNCP o = new CNCP(oStartingPoint);
        OrderedPair opNewPoint = getNextPoint(oStartingPoint);
        if (opNewPoint == null) {
            return o;
        }
        ImagePoint P = oStartingPoint.getGrid().oa[oStartingPoint.getGrid().getIndex(opNewPoint)];
        while (P != null) {
            if (checkIfNextPointIsValid(P) && (checkIfEnd(P) || checkIfJointPoint(P))) {
                P.bMarker = true;
                o.lo.add(P);
                o.closeCNCP(P);
                break;
            } else if (checkIfNextPointIsValid(P)) {
                o.lo.add(P);
                P.bMarker = true;
                if (o.lo.size() <= 2) {
                    opNewPoint = getNextPointSpecial(P, oStartingPoint);
                } else {
                    opNewPoint = getNextPoint(P);
                }

                if (opNewPoint == null) {
                    o.closeCNCP(P);
                    break;
                }
                P = oStartingPoint.getGrid().oa[oStartingPoint.getGrid().getIndex(opNewPoint)];
            } else {
                o.closeCNCP(P);
                break;
            }
        }
        return o;
    }

    public static CNCPNetwork getCNCPsToJoint(ImagePoint Joint) throws EmptySetException {
        CNCPNetwork o = new CNCPNetwork();
        OrderedPair opNewPoint = getNextPoint(Joint);
        if (opNewPoint == null) {
            Joint.bMarker = true;
            return null;
        }
        o.lo.add(new CNCP(Joint));
        ImagePoint P = Joint.getGrid().oa[Joint.getGrid().getIndex(opNewPoint)];
        while (P != null) {
            if (checkIfNextPointIsValid(P) && checkIfEnd(P)) {
                P.bMarker = true;
                o.lo.get(o.lo.size() - 1).addPoint(P);
                o.lo.get(o.lo.size() - 1).closeCNCP(P);
                opNewPoint = getNextPoint(Joint);
                if (opNewPoint == null) {
                    break;
                }
                o.lo.add(new CNCP(Joint));
                P = Joint.getGrid().oa[Joint.getGrid().getIndex(opNewPoint)];
            } else if (checkIfNextPointIsValid(P) && checkIfJointPoint(P)) {
                o.lo.get(o.lo.size() - 1).addPoint(P);
                o.lo.get(o.lo.size() - 1).closeCNCP(P);
                opNewPoint = getNextPointWOJoints(Joint);
                if (opNewPoint == null) {
                    break;
                }
                o.lo.add(new CNCP(Joint));
                P = Joint.getGrid().oa[Joint.getGrid().getIndex(opNewPoint)];
            } else if (checkIfNextPointIsValid(P)) {
                o.lo.get(o.lo.size() - 1).addPoint(P);
                P.bMarker = true;
                if (o.lo.get(o.lo.size() - 1).lo.size() <= 2) {
                    opNewPoint = getNextPointSpecial(P, Joint);
                } else {
                    opNewPoint = getNextPoint(P);
                }

                if (opNewPoint == null) {
                    break;
                }
                P = Joint.getGrid().oa[Joint.getGrid().getIndex(opNewPoint)];
            } else {
                o.lo.get(o.lo.size() - 1).closeCNCP(P);
                opNewPoint = getNextPoint(P, Joint);
                if (opNewPoint == null) {
                    break;
                }
                o.lo.add(new CNCP(Joint));
                P = Joint.getGrid().oa[Joint.getGrid().getIndex(opNewPoint)];
            }
        }
        return o;
    }

    public static ImageGrid setCNCP(ImageGrid oEdges, CNCP o, int iValue) {
        o.checkIfNCP();
        if (true) {
            oEdges.setPoint(o.oStart, iValue);
            oEdges.setPoint(o.lo, iValue);
        }
        return oEdges;
    }

    public static ImageGrid thinoutEdges(ImageGrid oGrid) {
        for (ImagePoint o : oGrid.oa) {
            if (o.iValue > 0) {
                N8 oN8 = new N8(o.getGrid(), o);

                if ((oN8.getBP() == 0)
                        || (oN8.getBP() == 2 && oN8.getC2WE() == 1)
                        || (oN8.getBP() == 2 && oN8.getC2P() == 1)
                        || (oN8.getBP() == 3 && oN8.getC3P() == 1)
                        || (oN8.getBP() == 3 && oN8.getC2WE() == 1 && oN8.getC2P() == 1)
                        || (oN8.getBP() == 4 && oN8.getC4P() == 1)
                        || (oN8.getBP() == 4 && oN8.getC2WE() == 1 && oN8.getC2P() == 2)
                        || (oN8.getBP() == 5 && oN8.getC5P() == 1)) {
                    oGrid.oa[o.i].iValue = 0;
                }
            }
        }
        return oGrid;
    }

    public static ImageInt thinoutEdges(ImageInt oGrid) {
        for (int i = 0; i < oGrid.iaPixels.length; i++) {
            for (int j = 0; j < oGrid.iaPixels[0].length; j++) {
                if (oGrid.iaPixels[i][j] > 0) {
                    N8 oN8 = new N8(oGrid, i, j);

                    if ((oN8.getBP() == 0)
                            || (oN8.getBP() == 2 && oN8.getC2WE() == 1)
                            || (oN8.getBP() == 2 && oN8.getC2P() == 1)
                            || (oN8.getBP() == 3 && oN8.getC3P() == 1)
                            || (oN8.getBP() == 3 && oN8.getC2WE() == 1 && oN8.getC2P() == 1)
                            || (oN8.getBP() == 4 && oN8.getC4P() == 1)
                            || (oN8.getBP() == 4 && oN8.getC2WE() == 1 && oN8.getC2P() == 2)
                            || (oN8.getBP() == 5 && oN8.getC5P() == 1)) {
                        oGrid.iaPixels[i][j] = 0;
                    }
                }
            }
        }
        return oGrid;
    }

    public static ImageGrid markEndStart(ImageGrid oGrid, int iValue) {
        for (ImagePoint o : oGrid.oa) {
            if (!o.bMarker && o.iValue > 0 && checkIfStart(o)) {
                o.iValue = iValue;
            }

            if (!o.bMarker && o.iValue > 0 && checkIfEnd(o)) {
                o.iValue = iValue;
            }
        }
        return oGrid;
    }

    public static ImagePoint findStartingPoint(ImageGrid oEdges) {
        for (ImagePoint o : oEdges.oa) {
            if (!o.bMarker && o.iValue > 0 && checkIfStart(o)) {
                o.bMarker = true;
                return o;
            }
        }
        return null;
    }

    public static ImagePoint findJointPoint(ImageGrid oEdges) {
        for (ImagePoint o : oEdges.oa) {
            if (!o.bMarker && o.iValue > 0 && checkIfJointPoint(o)) {
                o.bMarker = true;
                return o;
            }
        }
        return null;
    }

    public static boolean checkIfStart(ImagePoint o) {
        N8 oN8 = new N8(o.getGrid(), o);
        if (oN8.getBP() == 1
                || (oN8.getBP() == 2 && oN8.getC2P() == 1)) {
            return true;
        }
        return false;
    }

    public static boolean checkIfEnd(ImagePoint o) {
        N8 oN8 = new N8(o.getGrid(), o);
        if (oN8.getBP() == 1
                || (oN8.getBP() == 2 && oN8.getC2P() == 1)
                || (oN8.getBP() == 3 && oN8.getC3P() == 1)) {
            return true;
        }
        return false;
    }

    public static boolean checkIfJointPoint(ImagePoint o) {
        N8 oN8 = new N8(o.getGrid(), o);
        int BP = oN8.getBP();
        if (BP < 3) {
            return false;
        }
        int C2P = oN8.getC2P();
        int C3P = oN8.getC3P();
        int C3PC = oN8.getC3PC();
        int C4P = oN8.getC4P();
        if (BP == 3 && C2P == 0 && C3P == 0) {
            return true;
        }
        if (BP == 4 && C2P <= 1 && (C3P <= 1 && C3PC == 0) && C4P == 0) {
            return C3P != 1;
        }
        return false;
    }

    public static HashSet<ImagePoint> getJoints(ImageGrid Edges) {
        HashSet<ImagePoint> loJoints = new HashSet<>();
        for (ImagePoint oP : Edges.oa) {
            if (oP.iValue > 0 && Ziegenhein_2018.checkIfJointPoint(oP)) {
                loJoints.add(oP);
            }
        }
        return loJoints;
    }

    public static HashSet<ImagePoint> getStart(ImageGrid Edges) {
        HashSet<ImagePoint> loJoints = new HashSet<>();
        for (ImagePoint oP : Edges.oa) {
            if (oP.iValue > 0 && Ziegenhein_2018.checkIfStart(oP)) {
                loJoints.add(oP);
            }
        }
        return loJoints;

    }

    public static class CNCP implements Serializable {

        private static final long serialVersionUID = 936753173613431007L;

        public ArrayList<ImagePoint> lo = new ArrayList<>();
        public ImagePoint oStart = null;
        public ImagePoint oEnd = null;
        boolean NCP = false;

        public CNCP(ImagePoint oStart) {
            this.oStart = oStart;
        }

        public void addPoint(ImagePoint o) {
            lo.add(o);
        }

        public void closeCNCP(ImagePoint oEnd) {
            this.oEnd = oEnd;
        }

        public List<ImagePoint> getPoints() {
            List<ImagePoint> loPoints = new ArrayList<>();
            loPoints.add(oStart);
            loPoints.addAll(lo);
            return loPoints;
        }

        public boolean isClosedContour() {
            if (oEnd == null || oStart == null) {
                return false;
            }
            return oEnd.equals(oStart);
        }

        public static ImageGrid markClosedContour(ImageGrid oGrid, int iValue) throws EmptySetException {
            for (ImagePoint o : oGrid.oa) {
                if (o != null && o.iValue > 0 && !(checkIfEnd(o) || checkIfStart(o))) {
                    CNCP oCNCP = new CNCP(o);
                    o.bMarker = true;
                    oCNCP.addPoint(o);
                    OrderedPair P = getNextPoint(o);
                    while (P != null) {
                        if (checkIfEnd(oGrid.oa[oGrid.getIndex(P)]) || checkIfStart(oGrid.oa[oGrid.getIndex(P)])) {
                            break;
                        }
                        double dDist = oGrid.oa[oGrid.getIndex(P)].getNorm(oCNCP.lo.get(0));
                        if (oCNCP.lo.size() > 3 && dDist < 2) {
                            oCNCP.lo.add(oGrid.oa[oGrid.getIndex(P)]);
                            oGrid.setPoint(oCNCP.lo, iValue);
                            Data.loCNCPs.add(oCNCP);
                            break;
                        }
                        oGrid.oa[oGrid.getIndex(P)].bMarker = true;
                        oCNCP.lo.add(oGrid.oa[oGrid.getIndex(P)]);
                        P = getNextPoint(oGrid.oa[oGrid.getIndex(P)]);

                    }
                }
            }
            return oGrid;
        }

        public static ImageGrid setOnGrid(ImageGrid oGrid, Collection<CNCP> oCNCPs, int iValue) {
            List<ImagePoint> lo = new ArrayList<>();
            for (CNCP p : oCNCPs) {
                lo.addAll(p.lo);
            }
            oGrid.setPoint(lo, iValue);
            return oGrid;
        }

        public static ImageGrid connectCNCP(CNCP o1, CNCP o2, ImagePoint pConnect1, ImagePoint pConnect2, ImageGrid oGrid, int iValueOnGrid) {

            int iStart = o1.lo.indexOf(pConnect1);
            int iEnd = o2.lo.indexOf(pConnect2);

            if (iStart < 0 || iEnd < 0) {
                return oGrid;
            }

            Double dX = pConnect1.getPos().x - pConnect2.getPos().x;
            Double dY = pConnect1.getPos().y - pConnect2.getPos().y;

            if (Math.abs(dX) < 2 || Math.abs(dY) < 2) {
                Line2 oLine = new Line2(pConnect1, pConnect2);
                oLine.setLine(oGrid, 255);
                return oGrid;
            }

            //Directional of Start
            OrderedPair opAbbr1 = o1.getDerivationOutwardsEndPoints(pConnect1, 2);
            double dX1 = opAbbr1.x;
            double dY1 = opAbbr1.y;

            //Directional of End
            OrderedPair opAbbr2 = o2.getDerivationOutwardsEndPoints(pConnect2, 2);
            double dX2 = opAbbr2.x;
            double dY2 = opAbbr2.y;

            Double dCutY = (pConnect1.getPos().y + pConnect2.getPos().y) / 2.0;
            Double dCutX = (pConnect1.getPos().x + pConnect2.getPos().x) / 2.0;
            boolean bRotate = false;
            double[] x = new double[3];
            double[] y = new double[3];

            if (Math.abs(dX) > Math.abs(dY)) {

                if (Math.abs(dX1) > Math.abs(dX2)) {
                    Double dDistX = Math.abs(pConnect1.getPos().x - dCutX);
                    dCutY = dCutY + dY1 * dDistX;

                } else {
                    Double dDistX = Math.abs(pConnect2.getPos().x - dCutX);
                    dCutY = dCutY + dY2 * dDistX;

                }

                if (pConnect2.getPos().x > pConnect1.getPos().x) {
                    x = new double[]{pConnect1.getPos().x, dCutX, pConnect2.getPos().x};
                    y = new double[]{pConnect1.getPos().y, dCutY, pConnect2.getPos().y};
                } else {
                    x = new double[]{pConnect2.getPos().x, dCutX, pConnect1.getPos().x};
                    y = new double[]{pConnect2.getPos().y, dCutY, pConnect1.getPos().y};
                }

            } else {
                bRotate = true;
                if (Math.abs(dY1) > Math.abs(dY2)) {
                    Double dDistY = Math.abs(pConnect1.getPos().y - dCutY);
                    dCutX = dCutX + dX1 * dDistY;
                } else {
                    Double dDistY = Math.abs(pConnect2.getPos().y - dCutY);
                    dCutX = dCutX + dX2 * dDistY;
                }

                if (pConnect2.getPos().y > pConnect1.getPos().y) {
                    y = new double[]{pConnect1.getPos().x, dCutX, pConnect2.getPos().x};
                    x = new double[]{pConnect1.getPos().y, dCutY, pConnect2.getPos().y};
                } else {
                    y = new double[]{pConnect2.getPos().x, dCutX, pConnect1.getPos().x};
                    x = new double[]{pConnect2.getPos().y, dCutY, pConnect1.getPos().y};
                }

            }

            SplineInterpolator o = new SplineInterpolator();
            PolynomialSplineFunction oSpline = o.interpolate(x, y);

            List<ImagePoint> loSplinePoints = new ArrayList<>();

            if (!bRotate) {
                if (pConnect2.getPos().x < pConnect1.getPos().x) {
                    for (int iXIter = (int) pConnect2.getPos().x; iXIter <= pConnect1.getPos().x; iXIter++) {
                        int dYSplien = (int) oSpline.value(iXIter);
                        loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                    }
                } else {
                    for (int iXIter = (int) pConnect1.getPos().x; iXIter <= pConnect2.getPos().x; iXIter++) {
                        int dYSplien = (int) oSpline.value(iXIter);
                        loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                    }
                }
            } else {
                if (pConnect2.getPos().y < pConnect1.getPos().y) {
                    for (int iYIter = (int) pConnect2.getPos().y; iYIter <= pConnect1.getPos().y; iYIter++) {
                        int dXSplien = (int) oSpline.value(iYIter);
                        loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                    }
                } else {
                    for (int iYIter = (int) pConnect1.getPos().y; iYIter <= pConnect2.getPos().y; iYIter++) {
                        int dXSplien = (int) oSpline.value(iYIter);
                        loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                    }
                }
            }

            HashSet<ImagePoint> loPointsToSet = new HashSet<>();

            for (int iST = 0; iST < loSplinePoints.size() - 1; iST++) {
                if (loSplinePoints.get(iST).getNorm(loSplinePoints.get(iST + 1)) > 1.5) {
                    Line2 oLine = new Line2(loSplinePoints.get(iST), loSplinePoints.get(iST + 1));
                    loPointsToSet.addAll(oLine.lmeLine);
                } else {
                    loPointsToSet.add(loSplinePoints.get(iST));
                }
            }

            oGrid.setPoint(loPointsToSet, iValueOnGrid);
            return oGrid;
        }

        public OrderedPair getDerivationOutwardsEndPoints(ImagePoint oRef, int iLength) {

            if (oRef.equals(this.oStart)) {
                //Directional of Start
                double dX1 = 0;
                double dY1 = 0;
                int iCount = 0;
                for (int i = Math.min(lo.size() - iLength, iLength); i >= 1; i--) {
                    dX1 = dX1 + (lo.get(i - 1).getPos().x - lo.get(i).getPos().x); // / (lo.get(i - 1).getPos().getNorm( lo.get(i).getPos())) ;
                    dY1 = dY1 + (lo.get(i - 1).getPos().y - lo.get(i).getPos().y); // / (lo.get(i - 1).getPos().getNorm( lo.get(i).getPos()));
                    iCount++;
                }
                dX1 = dX1 + (oStart.getPos().x - lo.get(0).getPos().x);
                dY1 = dY1 + (oStart.getPos().y - lo.get(0).getPos().y);
                iCount++;
                dX1 = dX1 / (1.0 * (iCount));
                dY1 = dY1 / (1.0 * (iCount));
                return new OrderedPair(dX1, dY1);
                //Directional of End
            } else if (oRef.equals(this.oEnd)) {
                int iLeft = Math.max(0, lo.size() - iLength);
                int iRight = this.lo.size() - 1;
                int iCount = 0;
                double dX = 0;
                double dY = 0;
                for (int i = iLeft; i < iRight; i++) {
                    dX = dX + (lo.get(i + 1).getPos().x - lo.get(i).getPos().x);
                    dY = dY + (lo.get(i + 1).getPos().y - lo.get(i).getPos().y);
                    iCount++;
                }
                dX = dX / (1.0 * (iCount));
                dY = dY / (1.0 * (iCount));
                return new OrderedPair(dX, dY);
            } else {
                throw new UnsupportedOperationException("Input is not end or start point");
            }
        }

        public EnumObject getclosest(HashSet<CNCP> lo) throws EmptySetException {

            EnumObject oHelp = Sorting.getMinCharacteristic(lo, this, new Sorting.Characteristic2<CNCP>() {

                                                        @Override
                                                        public Double getCharacteristicValue(CNCP pParameter, CNCP pParameter2) {
                                                            if (pParameter.oEnd == null || pParameter.oStart == null || pParameter2.oEnd == null || pParameter2.oStart == null) {
                                                                return Double.MAX_VALUE;
                                                            }
                                                            if (pParameter.equals(pParameter2)) {
                                                                return Double.MAX_VALUE;
                                                            }
                                                            Double d1 = pParameter.oEnd.getNorm(pParameter2.oStart);
                                                            Double d2 = pParameter.oEnd.getNorm(pParameter2.oEnd);
                                                            Double d3 = pParameter.oStart.getNorm(pParameter2.oStart);
                                                            Double d4 = pParameter.oStart.getNorm(pParameter2.oEnd);
                                                            return Math.abs(Math.min(d1, Math.min(d2, Math.min(d3, d4))));
                                                        }
                                                    });

            return oHelp;

        }

        public List<EnumObject> getclosest(HashSet<CNCP> lo, double dNorm) throws EmptySetException {

            if (this.oEnd == null || this.oStart == null) {
                return new ArrayList<>();
            }
            ArrayList<EnumObject> loHelp = new ArrayList<>();
            for (CNCP o : lo) {
                if (o == null || o.oStart == null || o.oEnd == null) {
                    continue;
                }
                Double d1 = this.oEnd.getNorm(o.oStart);
                Double d2 = this.oEnd.getNorm(o.oEnd);
                Double d3 = this.oStart.getNorm(o.oStart);
                Double d4 = this.oStart.getNorm(o.oEnd);
                loHelp.add(new EnumObject(Math.abs(Math.min(d1, Math.min(d2, Math.min(d3, d4)))), o));
            }

            Collections.sort(loHelp, new Comparator<EnumObject>() {
                         @Override
                         public int compare(EnumObject o1, EnumObject o2) {
                             return ((int) Math.signum(o1.dEnum - o2.dEnum));
                         }
                     });

            List<EnumObject> loReturn = new ArrayList<>();

            for (EnumObject o : loHelp) {
                if (o.dEnum <= dNorm) {
                    loReturn.add(o);
                }
            }

            return loReturn;

        }

        public void checkIfNCP() {
            if (lo.size() < (new Gao_2018.parameters()).len) {
                NCP = true;
                return;
            }
            for (ImagePoint o : lo) {
                if (checkIfEndCNCP(o)) {
                    NCP = true;
                    break;
                }
            }
        }

        public OrderedPair getUnitTangent(ImagePoint o, int iLength) {
            double dX = 0;
            double dY = 0;
            int iCount = 0;
            for (int i = Math.max(this.lo.indexOf(o) - iLength, 0); i < Math.min(this.lo.indexOf(o) + iLength, lo.size() - 1); i++) {
                dX = dX + (lo.get(i).getPos().x - lo.get(i + 1).getPos().x) / lo.get(i).getPos().getNorm(lo.get(i + 1).getPos());
                dY = dY + (lo.get(i).getPos().y - lo.get(i + 1).getPos().y) / lo.get(i).getPos().getNorm(lo.get(i + 1).getPos());
                iCount++;
            }
            if (iCount == 0) {
                return null;
            }
            dX = dX / (1.0 * iCount);
            dY = dY / (1.0 * iCount);
            return new OrderedPair(dX, dY, dY / dX);
        }

        public Double getSlope(ImagePoint o, int iLength) {
            double dX = 0;
            double dY = 0;
            int iCount = 0;
            for (int i = Math.max(this.lo.indexOf(o) - iLength, 0); i < Math.min(this.lo.indexOf(o) + iLength, lo.size() - 1); i++) {
                dX = dX + (lo.get(i).getPos().x - lo.get(i + 1).getPos().x);
                dY = dY + (lo.get(i).getPos().y - lo.get(i + 1).getPos().y);
                iCount++;
            }
            if (iCount == 0) {
                return null;
            }
            dX = dX / (1.0 * iCount);
            dY = dY / (1.0 * iCount);
            return dY / dX;
        }

        public void closeEndPointsLinear(int iValueOnGrid, ImageGrid oGrid) {

            if (oGrid == null) {
                oGrid = oStart.getGrid();
            }

            Line2 oLine = new Line2(oStart, oEnd);
            oLine.setLine(oGrid, iValueOnGrid);

        }

        public void closeEndPointsSmooth(int iLength, int iValueOnGrid, ImageGrid oGrid) {

            if (oGrid == null) {
                oGrid = oStart.getGrid();
            }

            Double distX = oStart.getPos().x - oEnd.getPos().x;
            Double distY = oStart.getPos().y - oEnd.getPos().y;

            if (Math.abs(distX) < 4 || Math.abs(distY) < 4) {
                Line2 oLine = new Line2(oStart, oEnd);
                oLine.setLine(oGrid, iValueOnGrid);
                return;
            }

            //Directional of Start
            double dX1 = getDerivationOutwardsEndPoints(oStart, iLength).x;
            double dY1 = getDerivationOutwardsEndPoints(oStart, iLength).y;

//            double dX1 = 0;
//            double dY1 = 0;
//            dX1 = dX1 + (oStart.getPos().x - lo.get(0).getPos().x);
//            dY1 = dY1 + (oStart.getPos().y - lo.get(0).getPos().y);
//            int i = 0;
//            for (; i < Math.min(iLength, lo.size() - 1); i++) {
//                dX1 = dX1 + (lo.get(i).getPos().x - lo.get(i + 1).getPos().x);
//                dY1 = dY1 + (lo.get(i).getPos().y - lo.get(i + 1).getPos().y);
//            }
//            dX1 = dX1 / (1.0 * (i + 1));
//            dY1 = dY1 / (1.0 * (i + 1));
            //Directional of End
            double dX2 = getDerivationOutwardsEndPoints(oEnd, iLength).x;
            double dY2 = getDerivationOutwardsEndPoints(oEnd, iLength).y;
//            double dX2 = 0;
//            double dY2 = 0;
//            dX2 = dX2 + (oEnd.getPos().x - lo.get(lo.size() - 1).getPos().x);
//            dY2 = dY2 + (oEnd.getPos().y - lo.get(lo.size() - 1).getPos().y);
//            i = lo.size() - 2;
//            int iCount = 1;
//            for (; i > Math.max(lo.size() - iLength, 0); i--) {
//                dX2 = dX2 + (lo.get(i).getPos().x - lo.get(i - 1).getPos().x);
//                dY2 = dY2 + (lo.get(i).getPos().y - lo.get(i - 1).getPos().y);
//                iCount++;
//            }
//            dX2 = dX2 / (1.0 * (iCount));
//            dY2 = dY2 / (1.0 * (iCount));

            System.out.println(oStart.getPos());
            Double dCutY = (oStart.getPos().y + oEnd.getPos().y) / 2.0;
            Double dCutX = (oStart.getPos().x + oEnd.getPos().x) / 2.0;
            boolean bRotate = false;
            double[] x = new double[3];
            double[] y = new double[3];

            if (Math.abs(distX) > Math.abs(distY)) {

                if (Math.abs(dY1) > Math.abs(dY2)) {
                    Double dDistX = Math.abs(oStart.getPos().x - dCutX);
                    dCutY = dCutY + dY1 * dDistX / 2.0;

                } else {
                    Double dDistX = Math.abs(oEnd.getPos().x - dCutX);
                    dCutY = dCutY + dY2 * dDistX / 2.0;

                }

                if (oEnd.getPos().x > oStart.getPos().x) {
                    x = new double[]{oStart.getPos().x, dCutX, oEnd.getPos().x};
                    y = new double[]{oStart.getPos().y, dCutY, oEnd.getPos().y};
                } else {
                    x = new double[]{oEnd.getPos().x, dCutX, oStart.getPos().x};
                    y = new double[]{oEnd.getPos().y, dCutY, oStart.getPos().y};
                }

            } else {
                bRotate = true;
                if (Math.abs(dX1) > Math.abs(dX2)) {
                    Double dDistY = Math.abs(oStart.getPos().y - dCutY);
                    dCutX = dCutX + dX1 * dDistY / 2.0;
                } else {
                    Double dDistY = Math.abs(oEnd.getPos().y - dCutY);
                    dCutX = dCutX + dX2 * dDistY / 2.0;
                }

                if (oEnd.getPos().y > oStart.getPos().y) {
                    y = new double[]{oStart.getPos().x, dCutX, oEnd.getPos().x};
                    x = new double[]{oStart.getPos().y, dCutY, oEnd.getPos().y};
                } else {
                    y = new double[]{oEnd.getPos().x, dCutX, oStart.getPos().x};
                    x = new double[]{oEnd.getPos().y, dCutY, oStart.getPos().y};
                }

            }

            SplineInterpolator o = new SplineInterpolator();
            PolynomialSplineFunction oSpline = o.interpolate(x, y);

            List<ImagePoint> loSplinePoints = new ArrayList<>();

            if (!bRotate) {
                if (oEnd.getPos().x < oStart.getPos().x) {
                    for (int iXIter = (int) oEnd.getPos().x; iXIter <= oStart.getPos().x; iXIter++) {
                        int dYSplien = (int) oSpline.value(iXIter);
                        loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                    }
                } else {
                    for (int iXIter = (int) oStart.getPos().x; iXIter <= oEnd.getPos().x; iXIter++) {
                        int dYSplien = (int) oSpline.value(iXIter);
                        loSplinePoints.add(new ImagePoint(iXIter, dYSplien, 255, oGrid));
                    }
                }
            } else {
                if (oEnd.getPos().y < oStart.getPos().y) {
                    for (int iYIter = (int) oEnd.getPos().y; iYIter <= oStart.getPos().y; iYIter++) {
                        int dXSplien = (int) oSpline.value(iYIter);
                        loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                    }
                } else {
                    for (int iYIter = (int) oStart.getPos().y; iYIter <= oEnd.getPos().y; iYIter++) {
                        int dXSplien = (int) oSpline.value(iYIter);
                        loSplinePoints.add(new ImagePoint(dXSplien, iYIter, 255, oGrid));
                    }
                }
            }

            HashSet<ImagePoint> loPointsToSet = new HashSet<>();

            for (int iST = 0; iST < loSplinePoints.size() - 1; iST++) {
                if (loSplinePoints.get(iST).getNorm(loSplinePoints.get(iST + 1)) > 1.5) {
                    Line2 oLine = new Line2(loSplinePoints.get(iST), loSplinePoints.get(iST + 1));
                    loPointsToSet.addAll(oLine.lmeLine);
                } else {
                    loPointsToSet.add(loSplinePoints.get(iST));
                }
            }

            oGrid.setPoint(loPointsToSet, iValueOnGrid);

        }

        public static Double getCurv(CNCP oBubble, int i, int iCurvOrder, int iTangentOrder) {
            Double dCurv = 0.0;
            int iCount = 0;
            for (int j = Math.max(i - iCurvOrder, 0); j < Math.min(i + iCurvOrder - 1, oBubble.getPoints().size() - 1); j++) {
                ImagePoint o = oBubble.getPoints().get(j);
                ImagePoint oNext = oBubble.getPoints().get(j + 1);
                Double dydx = oBubble.getSlope(o, iTangentOrder);
                Double dydx2 = oBubble.getSlope(oNext, iTangentOrder);
                dCurv = dCurv + ((dydx2 - dydx) / oNext.getNorm(o)) / Math.pow(1 + Math.pow(dydx, 2), 3.0 / 2.0);
                iCount++;
            }
            dCurv = dCurv / (1.0 * iCount);
            return dCurv;
        }

        public static Double getCurvSpline(CNCP oBubble, int i, int iCurvOrder) {
            List<ImagePoint> loPoints = new ArrayList<>();
            for (int j = Math.max(i - iCurvOrder, 0); j < Math.min(i + iCurvOrder, oBubble.getPoints().size() - 1); j++) {
                ImagePoint o = oBubble.getPoints().get(j);
                loPoints.add(o);
            }
            Boolean bRot = Splines.checkRotation(loPoints);
            if (bRot == null) {
                return Double.NaN;
            }
            PolynomialSplineFunction oSpline = Splines.getSpline(loPoints);
            UnivariateFunction oDer = oSpline.derivative();
            Double dCurv;
            Double dydx1;
            Double dydx2;
            Double dydxx;
            if (!bRot) {
                dydx1 = oDer.value(oBubble.getPoints().get(i).getPos().x);
                if (oSpline.isValidPoint(oBubble.getPoints().get(i).getPos().x + 0.1)) {
                    dydx2 = oDer.value(oBubble.getPoints().get(i).getPos().x + 0.1);
                } else {
                    dydx2 = oDer.value(oBubble.getPoints().get(i).getPos().x - 0.1);
                }
                dydxx = (dydx2 - dydx1) / 0.1;
            } else {
                dydx1 = 1.0 / oDer.value(-1.0 * oBubble.getPoints().get(i).getPos().y);
                if (oSpline.isValidPoint(-1.0 * (oBubble.getPoints().get(i).getPos().y + 0.1))) {
                    dydx2 = 1.0 / oDer.value(-1.0 * (oBubble.getPoints().get(i).getPos().y + 0.1));
                } else {
                    dydx2 = 1.0 / oDer.value(-1.0 * (oBubble.getPoints().get(i).getPos().y - 0.1));
                }

                dydxx = (dydx2 - dydx1) / 0.1;
            }

            dCurv = dydxx / Math.pow(1 + Math.pow(dydx1, 2), 3.0 / 2.0);
            return dCurv;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof CNCP)) {
                return false;
            }

            boolean bStart = false;
            boolean bEnd = false;
            boolean blo = true;
            CNCP oComp = (CNCP) obj;

            if (this.oStart != null && oComp.oStart != null) {
                if (this.oStart.equals(((CNCP) obj).oStart)) {
                    bStart = true;
                }
            } else {
                if (this.oStart == null && oComp.oStart == null) {
                    bStart = true;
                }
            }

            if (this.oEnd != null && oComp.oEnd != null) {
                if (this.oEnd.equals(((CNCP) obj).oEnd)) {
                    bEnd = true;
                }
            } else {
                if (this.oEnd == null && oComp.oEnd == null) {
                    bEnd = true;
                }
            }

            if (this.lo != null && oComp.lo != null) {
                if (this.lo.size() == oComp.lo.size()) {
                    for (ImagePoint oOuter : this.lo) {
                        blo = false;
                        for (ImagePoint oInner : oComp.lo) {
                            if (!oOuter.equals(oInner)) {
                                blo = true;
                            }
                        }
                    }
                } else {
                    blo = false;
                }
            } else {
                if (!(this.lo == null && oComp.lo == null)) {
                    blo = false;
                }
            }

            return bStart && bEnd && blo;

        }

        public static Double getCurvSpline2(CNCP oBubble, int i, int iCurvOrder) {
            List<ImagePoint> loPoints = new ArrayList<>();
            int iLeft = Math.max(i - iCurvOrder, 0);
            int iRight = Math.min(i + iCurvOrder, oBubble.getPoints().size() - 1);
            int iMid = (int) ((iRight + iLeft) / 2.0);
            loPoints.add(oBubble.getPoints().get(iLeft));
            loPoints.add(oBubble.getPoints().get(iMid));
            loPoints.add(oBubble.getPoints().get(iRight));

            Boolean bRot = Splines.checkRotation(loPoints);
            if (bRot == null) {
                return Double.NaN;
            }
            PolynomialSplineFunction oSpline = Splines.getSpline(loPoints);
            UnivariateFunction oDer = oSpline.derivative();
            Double dCurv;
            Double dydx1;
            Double dydx2;
            Double dydxx;

            if (!bRot) {
                dydx1 = oDer.value(oBubble.getPoints().get(i).getPos().x);
                if (oSpline.isValidPoint(oBubble.getPoints().get(i).getPos().x + 0.1)) {
                    dydx2 = oDer.value(oBubble.getPoints().get(i).getPos().x + 0.1);
                } else {
                    dydx2 = oDer.value(oBubble.getPoints().get(i).getPos().x - 0.1);
                }
                dydxx = (dydx2 - dydx1) / 0.1;
            } else {
                dydx1 = 1.0 / oDer.value(-1.0 * oBubble.getPoints().get(i).getPos().y);
                if (oSpline.isValidPoint(-1.0 * (oBubble.getPoints().get(i).getPos().y + 0.1))) {
                    dydx2 = 1.0 / oDer.value(-1.0 * (oBubble.getPoints().get(i).getPos().y + 0.1));
                } else {
                    dydx2 = 1.0 / oDer.value(-1.0 * (oBubble.getPoints().get(i).getPos().y - 0.1));
                }

                dydxx = (dydx2 - dydx1) / 0.1;
            }

            dCurv = dydxx / Math.pow(1 + Math.pow(dydx1, 2), 3.0 / 2.0);
            return dCurv;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(this.lo);
            hash = 97 * hash + Objects.hashCode(this.oStart);
            hash = 97 * hash + Objects.hashCode(this.oEnd);
            return hash;
        }

        public static List<ImagePoint> fillArea(List<ImagePoint> loClosedCurve) {
            List<ImagePoint> loStack = new ArrayList<>();
            ImageGrid oGrid = new ImageGrid(loClosedCurve.get(0).getGrid().iLength, loClosedCurve.get(0).getGrid().jLength);
            for (int i = 0; i < oGrid.oa.length; i++) {
                oGrid.oa[i] = new ImagePoint(i, null, oGrid);
            }
            oGrid.setPoint(loClosedCurve, 255);
            for (ImagePoint o : loClosedCurve) {
                oGrid.oa[o.i].bMarker = true;
            }
            SideCondition<ImagePoint> oSide = (ImagePoint pParameter) -> pParameter != null && !pParameter.bMarker;

            ImagePoint oFocal = getFocalPoint(loClosedCurve);
            oGrid.oa[oFocal.i].bMarker = true;
            fillStack(loStack, oGrid.getNeighborsN4(oFocal, oSide), oSide);
            while (!loStack.isEmpty()) {
                List<ImagePoint> loNewStack = new ArrayList<>();
                for (ImagePoint oSet : loStack) {
                    oGrid.oa[oSet.i].bMarker = true;
                    loNewStack.addAll(oGrid.getNeighborsN4(oSet, oSide));
                }
                loStack.clear();
                fillStack(loStack, loNewStack, oSide);
            }
            List<ImagePoint> loReurn = new ArrayList<>();
            for (ImagePoint o : oGrid.oa) {
                if (o.iValue == null && o.bMarker) {
                    loReurn.add(o);
                }
            }
            return loReurn;
        }

        public static List<ImagePoint> fillStack(List<ImagePoint> loStack, Collection<ImagePoint> loFill, SideCondition O) {
            for (ImagePoint oFill : loFill) {
                if (oFill != null && O.check(oFill)) {
                    loStack.add(oFill);
                }
            }
            return loStack;
        }

        public static ImagePoint getFocalPoint(List<ImagePoint> lo) {
            double dX = 0.0;
            double dY = 0.0;
            int iCount = 0;
            for (ImagePoint o : lo) {
                dX = dX + o.getPos().x;
                dY = dY + o.getPos().y;
                iCount++;
            }
            if (iCount > 0) {
                return new ImagePoint((int) (dX / iCount), (int) (dY / iCount), 255, lo.get(0).getGrid());
            } else {
                return null;
            }

        }

        public ImagePoint getFocalPoint() {
            double dX = 0.0;
            double dY = 0.0;
            int iCount = 0;
            for (ImagePoint o : this.lo) {
                dX = dX + o.getPos().x;
                dY = dY + o.getPos().y;
                iCount++;
            }
            if (iCount > 0) {
                return new ImagePoint((int) (dX / iCount), (int) (dY / iCount), 255, this.oStart.getGrid());
            } else {
                return null;
            }

        }

    }

    public static class CNCPNetwork {

        public List<CNCP> lo = new ArrayList<>();

        public CNCP getShortest() {
            EnumObject oe = new EnumObject(1.0 * lo.get(0).lo.size(), lo.get(0));
            for (CNCP o : lo) {
                if (o.lo.size() < oe.dEnum) {
                    oe = new EnumObject(1.0 * o.lo.size(), o);
                }
            }
            return (CNCP) oe.o;
        }

        public CNCP getOnePoint() {
            for (CNCP o : lo) {
                if (o.lo.size() == 2) {
                    return o;
                }
            }
            return null;
        }

        public static ImageGrid markCNCPNetwork(ImageGrid oEdges, sortOutRule O, int iValue) throws EmptySetException {
            List<CNCPNetwork> loNet = new ArrayList<>();
            for (ImagePoint P : oEdges.oa) {
                if (P.iValue > 0 && checkIfJointPoint(P)) {
                    CNCPNetwork oNet = getCNCPsToJoint(P);
                    if (oNet != null) {
                        loNet.add(oNet);
                    }
                }
            }

            for (CNCPNetwork o : loNet) {
                List<CNCP> loRemove = O.remove(o);
                for (CNCP oRemove : loRemove) {
                    if (oRemove != null) {
                        setCNCP(oEdges, oRemove, iValue);
                    }
                }
            }

            return oEdges;
        }

        public static ImageGrid markOpenCNCP(ImageGrid oEdges, sortOutRule3 O, int iValue) throws EmptySetException {
            HashSet<CNCP> loOpenCNCP = new HashSet<>();
            for (ImagePoint P : oEdges.oa) {
                if (P.iValue > 0 && checkIfStart(P)) {
                    CNCP oOpen = getCNCPToStartingPoint(P);
                    if (oOpen != null) {
                        loOpenCNCP.add(oOpen);
                    }
                }
            }

            for (CNCP o : loOpenCNCP) {
                if (O.remove(o, loOpenCNCP)) {
                    setCNCP(oEdges, o, iValue);
                }
            }

            return oEdges;
        }

        public static ImageGrid OpenCNCP(ImageGrid oEdges, RuleWithAction O) throws EmptySetException {
            HashSet<CNCP> loOpenCNCP = new HashSet<>();
            for (ImagePoint P : oEdges.oa) {
                if (P.iValue > 0 && checkIfStart(P)) {
                    CNCP oOpen = getCNCPToStartingPoint(P);
                    if (oOpen != null) {
                        loOpenCNCP.add(oOpen);
                    }
                }
            }

            for (CNCP o : loOpenCNCP) {
                if (O.isValid(o, loOpenCNCP)) {
                    O.operate(o);
                }
            }
            return oEdges;
        }

        public static ImageGrid OpenCNCP(ImageGrid oEdges, RuleWithDoubleAction O) throws EmptySetException {
            List<CNCP> loOpenCNCP = new ArrayList<>();
            for (ImagePoint P : oEdges.oa) {
                if (P.iValue > 0 && checkIfStart(P)) {
                    CNCP oOpen = getCNCPToStartingPoint(P);
                    if (oOpen != null) {
                        loOpenCNCP.add(oOpen);
                    }
                }
            }

            for (CNCP o : loOpenCNCP) {
                HashSet<CNCP> hash = new HashSet<>(loOpenCNCP);
                ArrayList<CNCP> loPartner = (ArrayList<CNCP>) O.isValid(o, hash);
                if (loPartner != null && !loPartner.isEmpty()) {
                    O.operate(o, loPartner, oEdges);
                }
            }
            return oEdges;
        }

        public List<CNCP> getSmoothestPair() throws EmptySetException {
            if (lo.size() <= 1) {
                return lo;
            }

            List<CNCPPair> loPairs = new ArrayList<>();
            CNCP oLOngest = this.getLongest();
            for (CNCP o : lo) {
                if (o.isClosedContour()) {
                    List<CNCP> loTemp = new ArrayList<>();
                    loTemp.add(o);
                    return loTemp;
                }
                if (!oLOngest.equals(o)) {
                    loPairs.add(new CNCPPair(oLOngest, o));
                }
            }

            List<EnumObject> loHelpObjects = new ArrayList<>();
            for (CNCPPair oPair : loPairs) {
                loHelpObjects.add(new EnumObject(oPair.getSmoothness(2), oPair));
            }

            EnumObject oMin = Sorting.getMinCharacteristic(loHelpObjects, (Sorting.Characteristic<EnumObject>) (EnumObject pParameter) -> pParameter.dEnum);

            return ((CNCPPair) ((EnumObject) (oMin.o)).o).getList();
        }

        public CNCP getLongest() throws EmptySetException {
            List<EnumObject> loHelpObjects = new ArrayList<>();
            for (CNCP o : lo) {
                loHelpObjects.add(new EnumObject(1.0 * o.lo.size(), o));
            }
            EnumObject oMin = Sorting.getMaxCharacteristic(loHelpObjects, (Sorting.Characteristic<EnumObject>) (EnumObject pParameter) -> pParameter.dEnum);
            return (CNCP) ((EnumObject) oMin.o).o;
        }

    }

    public static class CNCPPair {

        CNCP o1;
        CNCP o2;

        public CNCPPair(CNCP o1, CNCP o2) {
            this.o1 = o1;
            this.o2 = o2;
        }

        public Double getSmoothness(int iLength) throws EmptySetException {
            if (o1.lo.isEmpty() || o2.lo.isEmpty()) {
                throw new EmptySetException("");
            }
            if (iLength <= 0) {
                throw new UnsupportedOperationException("");
            }
            //Directional of o1 to joint
            double dX1 = 0;
            double dY1 = 0;
            dX1 = dX1 + (o1.oStart.getPos().x - o1.lo.get(0).getPos().x);
            dY1 = dY1 + (o1.oStart.getPos().y - o1.lo.get(0).getPos().y);
            int i = 0;
            for (; i < Math.min(iLength, o1.lo.size() - 1); i++) {
                dX1 = dX1 + (o1.lo.get(i).getPos().x - o1.lo.get(i + 1).getPos().x);
                dY1 = dY1 + (o1.lo.get(i).getPos().y - o1.lo.get(i + 1).getPos().y);
            }
            dX1 = dX1 / (1.0 * (i + 1));
            dY1 = dY1 / (1.0 * (i + 1));

            //Directional of o2 to joint
            double dX2 = 0;
            double dY2 = 0;
            dX2 = dX2 + (o2.oStart.getPos().x - o2.lo.get(0).getPos().x);
            dY2 = dY2 + (o2.oStart.getPos().y - o2.lo.get(0).getPos().y);
            i = 0;
            for (; i < Math.min(iLength, o2.lo.size() - 1); i++) {
                dX2 = dX2 + (o2.lo.get(i).getPos().x - o2.lo.get(i + 1).getPos().x);
                dY2 = dY2 + (o2.lo.get(i).getPos().y - o2.lo.get(i + 1).getPos().y);
            }
            dX2 = dX2 / (1.0 * (i + 1));
            dY2 = dY2 / (1.0 * (i + 1));

            return (Math.abs(dX1 + dX2) + Math.abs(dY1 + dY2));

        }

        public ArrayList<CNCP> getList() {
            ArrayList<CNCP> lo = new ArrayList();
            lo.add(o1);
            lo.add(o2);
            return lo;
        }
    }

    public static interface sortOutRule {

        public List<CNCP> remove(CNCPNetwork o);
    }

    public static interface sortOutRule2<T> {

        public boolean remove(T o);
    }

    public static interface sortOutRule3<T> {

        public boolean remove(T o, HashSet<T> lo);
    }

    public static interface RuleWithAction<T> {

        public boolean isValid(T o, HashSet<T> lo);

        public void operate(T o);
    }

    public static interface RuleWithDoubleAction<T> {

        public List<T> isValid(T o, HashSet<T> lo);

        public void operate(T o1, List<T> o2, ImageGrid oGrid);
    }

}
