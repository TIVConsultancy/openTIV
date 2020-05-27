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

import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Gao_2018 {

    public static ImageGrid removeNCP(ImageGrid oEdges) {
        /*
         Edges have a value > 1, non-edges = 0
         */
        ArrayList<CNCP> loCNCP = new ArrayList<>();
        ImagePoint P = step1(oEdges);
        loCNCP.add(new CNCP(P));
        System.out.println(P.getGrid().getPos(P));
        while (P != null) {
            // Step 2
            if (checkIfEndCNCP(P)) {
                P = step1(oEdges);
                if (P == null) {
                    break;
                }
                loCNCP.add(new CNCP(P));
            } else {
                List<ImagePoint> lo = P.getGrid().getNeighborsN8(P);
                OrderedPair op = P.getGrid().getPos(P);
                int iNextX = 0;
                if ((lo.get(0).iValue > 0 && !lo.get(0).bMarker)
                        || (lo.get(1).iValue > 0 && !lo.get(1).bMarker)
                        || (lo.get(7).iValue > 0 && !lo.get(7).bMarker)) {
                    iNextX = iNextX + 1;
                } else if ((lo.get(3).iValue > 0 && !lo.get(3).bMarker)
                        || (lo.get(4).iValue > 0 && !lo.get(4).bMarker)
                        || (lo.get(5).iValue > 0 && !lo.get(5).bMarker)) {
                    iNextX = iNextX - 1;
                }

                int iNextY = 0;
                if ((lo.get(5).iValue > 0 && !lo.get(5).bMarker)
                        || (lo.get(6).iValue > 0 && !lo.get(6).bMarker)
                        || (lo.get(7).iValue > 0 && !lo.get(7).bMarker)) {
                    iNextY = iNextY + 1;
                } else if ((lo.get(1).iValue > 0 && !lo.get(1).bMarker)
                        || (lo.get(2).iValue > 0 && !lo.get(2).bMarker)
                        || (lo.get(3).iValue > 0 && !lo.get(3).bMarker)) {
                    iNextY = iNextY - 1;
                }

                OrderedPair opNewPoint = new OrderedPair((int) op.x + iNextX, (int) op.y + iNextY);
                if ((iNextX == 0 && iNextY == 0) || oEdges.oa[P.getGrid().getIndex(opNewPoint)].iValue == 0 || oEdges.oa[P.getGrid().getIndex(opNewPoint)].bMarker) {
                    P = step1(oEdges);
                    if (P == null) {
                        break;
                    }
                    loCNCP.add(new CNCP(P));
                } else {
                    oEdges.oa[P.getGrid().getIndex(opNewPoint)].bMarker = true;
                    P = oEdges.oa[P.getGrid().getIndex(opNewPoint)];
                    if (P == null) {
                        break;
                    }
                    loCNCP.get(loCNCP.size() - 1).addPoint(P);
                }

                System.out.println(P.getGrid().getPos(P));

            }
        }

        for (CNCP o : loCNCP) {
            o.checkIfNCP();
            if (o.NCP) {
                oEdges.setPoint(o.lo, 0);
            }
        }

        return oEdges;

    }

    public static ImageGrid thinEdges(ImageGrid o) {
        for (ImagePoint p : o.oa) {
            if (p.iValue > 0) {
                List<ImagePoint> lo = p.getGrid().getNeighborsN8(p);
                int BP = getBP(lo);
                int C2P = getC2P(lo);
                if (BP == 4 && C2P == 2) {
                    o.oa[p.i].iValue = 0;
                }
            }
        }
        return o;
    }

    public static ImagePoint step1(ImageGrid oEdges) {
        for (ImagePoint o : oEdges.oa) {
            if (!o.bMarker && o.iValue > 0 && checkIfStartCNCP(o)) {
                o.bMarker = true;
                return o;
            }
        }
        return null;
    }

    public static int getBP(List<ImagePoint> lo) {
        int iBP = 0;
        for (ImagePoint o : lo) {
            if(o != null) iBP = iBP + ((o.iValue > 0) ? 1 : 0);
        }
        return iBP;
    }

    public static int getC2P(List<ImagePoint> lo) {
        int C2P = 0;
        for (int i = 0; i <= 7; i++) {
            C2P = C2P + check_pattern_11(lo, i);
        }
        return C2P;
    }

    public static int getC3P(List<ImagePoint> lo) {
        int C3P = 0;
        for (int i = 0; i <= 7; i++) {
            C3P = C3P + check_pattern_111(lo, i);
        }
        return C3P;
    }

    public static int check_pattern_11(List<ImagePoint> lo, int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;

        if (lo.get(i).iValue == 0 && lo.get(i1).iValue > 0 && lo.get(i2).iValue > 0 && lo.get(i3).iValue == 0) {
            return 1;
        }
        return 0;

    }

    public static int check_pattern_111(List<ImagePoint> lo, int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;
        int i4 = i + 4 > 7 ? i + 3 - 7 : i + 4;

        if (lo.get(i).iValue == 0 && lo.get(i1).iValue > 0 && lo.get(i2).iValue > 0 && lo.get(i3).iValue > 0 && lo.get(i4).iValue == 0) {
            return 1;
        }
        return 0;

    }

    public static boolean checkIfStartCNCP(ImagePoint oa) {

        List<ImagePoint> lo = oa.getGrid().getNeighborsN8(oa);
        int BP = getBP(lo);
        int C2P = getC2P(lo);
        int C3P = getC3P(lo);

        return BP == 1 || (BP == 2 && C2P == 1); // BP <= 1 || (BP == 2 && C2P == 1) || (BP == 3 && C3P == 1);
    }

    public static boolean checkIfEndCNCP(ImagePoint oa) {

        List<ImagePoint> lo = oa.getGrid().getNeighborsN8(oa);
        int BP = getBP(lo);
        int C2P = getC2P(lo);

        return BP == 0 || (BP == 2 && C2P == 1) || BP == 4 && C2P != 2 || BP >= 5;
    }

    public static boolean checkIfNCPLastPoint(ImagePoint oa) {
        List<ImagePoint> lo = oa.getGrid().getNeighborsN8(oa);
        int BP = getBP(lo);
        int C2P = getC2P(lo);
        int C3P = getC3P(lo);

        return (BP == 2 && C2P != 1) || (BP == 3 && C3P != 1) || BP >= 4;
    }

    static class CNCP {

        HashSet<ImagePoint> lo = new HashSet<>();
        boolean NCP = false;

        public CNCP(ImagePoint oStart) {
            lo.add(oStart);
        }

        public void addPoint(ImagePoint o) {
            lo.add(o);
        }

        public void checkIfNCP() {
            if (lo.size() < (new parameters()).len) {
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

    }

    static class parameters {

        public int len = 100;
    }

}
