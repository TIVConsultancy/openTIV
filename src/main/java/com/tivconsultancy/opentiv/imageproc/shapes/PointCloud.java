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
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class PointCloud implements Shape, Serializable {

    private static final long serialVersionUID = 913609036006428960L;

    protected HashSet<ImagePoint> lop;

    public PointCloud(HashSet<ImagePoint> lop) {
        this.lop = lop;
    }

    public HashSet<ImagePoint> getPoints(){
        return lop;
    }
    
    public ImageInt paintOnIMG(ImageInt img) {
        for (ImagePoint oOuter : lop) {
            OrderedPair op = oOuter.getPos();
            int i = (int) op.getPosY();
            int j = (int) op.getPosX();
            MatrixEntry meOuter = new MatrixEntry(i, j);
            int iCounter = 0;
            for (MatrixEntry me : img.getNeighborsN8(i, j)) {
                if (img.iaPixels[me.i][me.j] > 127) {
                    iCounter++;
                }
            }
            if (iCounter > 7) {
                continue;
            }
            for (ImagePoint oInner : lop) {
                OrderedPair opInner = oInner.getPos();
                int iInner = (int) opInner.getPosY();
                int jInner = (int) opInner.getPosX();
                MatrixEntry meInner = new MatrixEntry(iInner, jInner);
                (new Line3(meOuter, meInner)).setLine(img, 255);
            }
        }
        return img;
    }

    public static List<PointCloud> getPointClouds(HashSet<ImagePoint> lop, double maxDist) {
        List<PointCloud> returnList = new ArrayList<>();

        //the visited points will be marked in the following algorithm
        for (ImagePoint op : lop) {
            op.bMarker = false;
        }
        for (ImagePoint op : lop) {
            if (op.bMarker) {
                continue;
            }
            //Initialize the while loop
            HashSet<ImagePoint> neighborhood = new HashSet<>();
            neighborhood.add(op);
            op.bMarker = true;
            HashSet<ImagePoint> newNeighbors = getOtherNeighbors(lop, neighborhood, maxDist);
            neighborhood.addAll(newNeighbors);

            while (!newNeighbors.isEmpty()) {
                newNeighbors = getOtherNeighbors(lop, neighborhood, maxDist);
                neighborhood.addAll(newNeighbors);
            }
            returnList.add(new PointCloud(neighborhood));

        }
        return returnList;
    }

    public static HashSet<ImagePoint> getOtherNeighbors(HashSet<ImagePoint> allPoints, HashSet<ImagePoint> neighbors, double maxdist) {
        HashSet<ImagePoint> newNeighbors = new HashSet<>();
        for (ImagePoint neighbor : neighbors) {
            for (ImagePoint all : allPoints) {
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
