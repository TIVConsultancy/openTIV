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

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Morphology;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePointInt;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class PointCloud2 implements Shape, Serializable {

    private static final long serialVersionUID = 6742209117330123769L;

    protected HashSet<ImagePointInt> lop;

    public PointCloud2(HashSet<ImagePointInt> lop) {
        this.lop = lop;
    }
    
    public PointCloud2(List<MatrixEntry> lme) {
        lop = new HashSet<>();
        for(MatrixEntry me : lme){
            lop.add(new ImagePointInt(me));
        }
    }

    public HashSet<ImagePointInt> getPoints() {
        return lop;
    }

    public ImageInt paintOnIMG(ImageInt img, double dMaxDist) {
        for (ImagePointInt oOuter : lop) {
            MatrixEntry meOuter = (MatrixEntry) oOuter;
            int iCounter = 0;
            for (MatrixEntry me : img.getNeighborsN8(meOuter.i, meOuter.j)) {
                if (me == null) {
                    continue;
                }
                if (img.iaPixels[me.i][me.j] > 127) {
                    iCounter++;
                }
            }
            if (iCounter > 7) {
                continue;
            }
            for (ImagePointInt oInner : lop) {
                MatrixEntry meInner = (MatrixEntry) oInner;
                if (meOuter.getNorm(meInner) > dMaxDist) {
                    continue;
                }
                (new Line3(meOuter, meInner)).setLine(img, 255);
            }
        }
        return img;
    }
    
    public HashSet<MatrixEntry> fillOnIMG(double dMaxDist) {
        HashSet<MatrixEntry> lmeFill = new HashSet<>();
        for (ImagePointInt oOuter : lop) {
            MatrixEntry meOuter = (MatrixEntry) oOuter;            
            for (ImagePointInt oInner : lop) {
                MatrixEntry meInner = (MatrixEntry) oInner;
//                if (meOuter.getNorm(meInner) > dMaxDist) {
//                    continue;
//                }
                lmeFill.addAll(Line.BresenhamAlgorithm(meOuter, meInner));
            }
        }
        return lmeFill;
    }

    public ImageInt paintOnIMG2(ImageInt img) {
        ImageInt iablanck = new ImageInt(img.iaPixels.length, img.iaPixels[0].length);
        for (ImagePointInt o : lop) {
            iablanck.setPoint(o, 255);
        }
        Morphology.dilatation(iablanck);
        Morphology.dilatation(iablanck);
        Morphology.erosion(iablanck);
        Morphology.erosion(iablanck);

        for (int i = 0; i < iablanck.iaPixels.length; i++) {
            for (int j = 0; j < iablanck.iaPixels[0].length; j++) {
                if (iablanck.iaPixels[i][j] > 127) {
                    img.iaPixels[i][j] = 255;
                }
            }
        }

        return img;
    }

    public static List<PointCloud2> getPointClouds(HashSet<ImagePointInt> lop, double maxDist) {
        List<PointCloud2> returnList = new ArrayList<>();

        //the visited points will be marked in the following algorithm
        for (ImagePointInt op : lop) {
            op.bMarker = false;
        }
        for (ImagePointInt op : lop) {
            if (op.bMarker) {
                continue;
            }
            //Initialize the while loop
            HashSet<ImagePointInt> neighborhood = new HashSet<>();
            neighborhood.add(op);
            op.bMarker = true;
            HashSet<ImagePointInt> newNeighbors = getOtherNeighbors(lop, neighborhood, maxDist);
            neighborhood.addAll(newNeighbors);

            while (!newNeighbors.isEmpty()) {
                newNeighbors = getOtherNeighbors(lop, neighborhood, maxDist);
                neighborhood.addAll(newNeighbors);
            }
            returnList.add(new PointCloud2(neighborhood));

        }
        return returnList;
    }

    public static PointCloud2 getMaxPointCloud(HashSet<ImagePointInt> lop, ImageInt img, double maxDist) throws EmptySetException {
        List<PointCloud2> clouds = getPointClouds2(lop, img, maxDist);
        EnumObject o = Sorting.getMaxCharacteristic(clouds, new Sorting.Characteristic<PointCloud2>() {
                                                @Override
                                                public Double getCharacteristicValue(PointCloud2 pParameter) {
                                                    return 1.0 * pParameter.lop.size();
                                                }
                                            });

        return (PointCloud2) o.o;
    }

    public static List<PointCloud2> getPointClouds2(HashSet<ImagePointInt> lop, ImageInt img, double maxDist) {
        boolean[][] baArray = new boolean[img.iaPixels.length][img.iaPixels[0].length];
        List<PointCloud2> returnList = new ArrayList<>();
        int iRadius = (int) Math.ceil(maxDist);

        for (ImagePointInt op : lop) {
            if (baArray[op.i][op.j]) {
                continue;
            }
            //Initialize the while loop
            HashSet<ImagePointInt> neighborhood = new HashSet<>();
            neighborhood.add(op);
            baArray[op.i][op.j] = true;
            HashSet<ImagePointInt> newNeighbors = getOtherNeighbors2(img, baArray, neighborhood, iRadius);
            neighborhood.addAll(newNeighbors);

            while (!newNeighbors.isEmpty()) {
//                System.out.println(newNeighbors.size());
//                System.out.println(neighborhood.size());
                newNeighbors = getOtherNeighbors2(img, baArray, neighborhood, iRadius);
                neighborhood.addAll(newNeighbors);
            }
            returnList.add(new PointCloud2(neighborhood));

        }
        return returnList;
    }

    public static PointCloud2 getPointCloudToNeighboorhood(HashSet<ImagePointInt> lop, ImageInt img, double maxDist) {
        boolean[][] baArray = new boolean[img.iaPixels.length][img.iaPixels[0].length];
        int iRadius = (int) Math.ceil(maxDist);
        HashSet<ImagePointInt> seed = new HashSet<>();

        for (ImagePointInt op : lop) {
            baArray[op.i][op.j] = true;
            seed.add(op);
        }
            //Initialize the while loop
            HashSet<ImagePointInt> newNeighbors = getOtherNeighbors2(img, baArray, seed, iRadius);
            seed.addAll(newNeighbors);

            while (!newNeighbors.isEmpty()) {
                newNeighbors = getOtherNeighbors2(img, baArray, seed, iRadius);
                seed.addAll(newNeighbors);
            }
        
        return new PointCloud2(seed);
    }

    public List<MatrixEntry> getMaxDistandPair() {
        double dNormMax = 0.0;
        MatrixEntry me1 = null;
        MatrixEntry me2 = null;

        for (ImagePointInt outer : this.lop) {
            for (ImagePointInt inner : this.lop) {
                double dNorm = outer.getNorm(inner);
                if (dNormMax < dNorm) {
                    dNormMax = dNorm;
                    me1 = outer;
                    me2 = inner;
                }
            }
        }

        List<MatrixEntry> lme = new ArrayList<>();
        lme.add(me1);
        lme.add(me2);
        return lme;
    }

//    public static HashSet<ImagePointInt> getOtherNeighbors3(ImageInt img, boolean[][] baArray, HashSet<ImagePointInt> neighbors, double maxdist) {
//        HashSet<ImagePointInt> newNeighbors = new HashSet<>();
//        for (ImagePointInt neighbor : neighbors) {
////            if(baArray[neighbor.i][neighbor.j]) continue;
//            List<MatrixEntry> lmeN8s = new ArrayList<>();
//            lmeN8s.addAll(img.getNeighborsN8(neighbor.i, neighbor.j));
//            for (int i = 0; i < maxdist; i++) {
//                List<MatrixEntry> lmeNewTemp = new ArrayList<>();
//                for (MatrixEntry me : lmeN8s) {
//                    if (me == null || baArray[me.i][me.j]) {
//                        continue;
//                    }
//                    lmeNewTemp.addAll(img.getNeighborsN8(me.i, me.j));
//                }
//                lmeN8s.addAll(lmeNewTemp);
//            }
//
//            for (MatrixEntry me : lmeN8s) {
//                if (me == null || baArray[me.i][me.j]) {
//                    continue;
//                }
//                if (img.iaPixels[me.i][me.j] > 127) {
//                    newNeighbors.add(new ImagePointInt(me));
//                    baArray[me.i][me.j] = true;
//                }
//            }
//        }
//        return newNeighbors;
//    }
    public static HashSet<ImagePointInt> getOtherNeighbors2(ImageInt img, boolean[][] baArray, HashSet<ImagePointInt> neighbors, int iRadius) {
        HashSet<ImagePointInt> newNeighbors = new HashSet<>();
        for (ImagePointInt neighbor : neighbors) {
            int iCenter = neighbor.i;
            int jCenter = neighbor.j;
            for (int i = Math.max(iCenter - iRadius, 0); i < Math.min(iCenter + iRadius, img.iaPixels.length); i++) {
                for (int j = Math.max(jCenter - iRadius, 0); j < Math.min(jCenter + iRadius, img.iaPixels[0].length); j++) {
                    if (!baArray[i][j] && img.iaPixels[i][j] > 127) {
                        newNeighbors.add(new ImagePointInt(i, j));
                        baArray[i][j] = true;
                    }
                }
            }
        }
        return newNeighbors;
    }

    public static HashSet<ImagePointInt> getOtherNeighbors(HashSet<ImagePointInt> allPoints, HashSet<ImagePointInt> neighbors, double maxdist) {
        HashSet<ImagePointInt> newNeighbors = new HashSet<>();
        for (ImagePointInt neighbor : neighbors) {
            for (ImagePointInt all : allPoints) {
                if (all.bMarker || neighbor.equals(all)) {
                    continue;
                }
                if (neighbor.getNorm(all) <= maxdist) {
                    all.bMarker = true;
                    newNeighbors.add(all);
                }
            }
        }
        return newNeighbors;
    }
    
    public List<MatrixEntry> getBoundingBox() {
        double dMaxX = 0;
        double dMinX = Double.MAX_VALUE;

        double dMaxY = 0;
        double dMinY = Double.MAX_VALUE;

        for (MatrixEntry op : lop) {
            if (dMaxX < op.j) {
                dMaxX = op.j;
            }
            if (dMinX > op.j) {
                dMinX = op.j;
            }
            if (dMaxY < op.i) {
                dMaxY = op.i;
            }
            if (dMinY > op.i) {
                dMinY = op.i;
            }
        }
        //Coordinates with respect to picture coordinates
        dMinX = Math.max(0, dMinX - 1);
        dMinY = Math.max(0, dMinY - 1);
        dMaxX = Math.max(0, dMaxX + 1);
        dMaxY = Math.max(0, dMaxY + 1);
        MatrixEntry leftTopCorner = new MatrixEntry((int) dMinY, (int) dMinX);
        MatrixEntry rightBottomCorner = new MatrixEntry((int) dMaxY, (int) dMaxX);
        List<MatrixEntry> lmeBox = new ArrayList<>();
        lmeBox.add(leftTopCorner);
        lmeBox.add(rightBottomCorner);
        return lmeBox;
    }

    @Override
    public double getSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMinorAxis() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMajorAxis() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getFormRatio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getOrientationAngle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getPixelCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MatrixEntry> getlmeList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getGreyDerivative() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderedPair getSubPixelCenter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
