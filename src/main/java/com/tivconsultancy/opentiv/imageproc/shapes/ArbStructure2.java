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

import com.tivconsultancy.opentiv.helpfunctions.matrix.Entries;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.N8;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageBoolean;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.math.interfaces.Normable;
import com.tivconsultancy.opentiv.math.primitives.ObjectPair;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.primitives.Vector;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import com.tivconsultancy.opentiv.math.sets.Set2D;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ArbStructure2 implements Shape, Serializable, Normable<ArbStructure2> {

    private static final long serialVersionUID = -6387066610382197860L;

    public List<MatrixEntry> loPoints = new ArrayList<>();
    public List<MatrixEntry> loBorderPoints = new ArrayList<>();
    public List<MatrixEntry> loMajor = new ArrayList<>();
    public List<MatrixEntry> loMinor = new ArrayList<>();
    public Double dAvergeGreyDerivative = null;

    public ArbStructure2(List<?> loInput) {
        if (loInput == null || loInput.isEmpty()) {
            return;
        }
        if (loInput.get(0) instanceof MatrixEntry) {
            for (Object o : loInput) {
                loPoints.add((MatrixEntry) o);
            }
        }
    }

    public ArbStructure2 clone() {
        return new ArbStructure2(loPoints);
    }

    public OrderedPair getPosition() {
        double dX = 0.0;
        double dY = 0.0;
        double dCounter = 0.0;
        for (MatrixEntry me : loPoints) {
            dX = dX + me.j;
            dY = dY + me.i;
            dCounter++;
        }
        return new OrderedPair(dX / dCounter, dY / dCounter);
    }

    public List<MatrixEntry> getBoundingBox() {
        double dMaxX = 0;
        double dMinX = Double.MAX_VALUE;

        double dMaxY = 0;
        double dMinY = Double.MAX_VALUE;

        for (MatrixEntry op : loPoints) {
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

    public Set2D getBoundBoxSet() {
        List<MatrixEntry> lmeBoundBox = getBoundingBox();
        Set1D x = new Set1D(lmeBoundBox.get(0).j, lmeBoundBox.get(1).j);
        Set1D y = new Set1D(lmeBoundBox.get(0).i, lmeBoundBox.get(1).i);
        return new Set2D(x, y);
    }

    public boolean containsPoint(List<MatrixEntry> lme, int iMax, int jMax) {
        ArbStructure2 temp = new ArbStructure2(lme);
        if (!temp.getBoundBoxSet().overlap(this.getBoundBoxSet())) {
            return false;
        }
        ImageBoolean set = new ImageBoolean(iMax, jMax);
        set.markPoints(this.loPoints, true);
        for (MatrixEntry me : lme) {
            if (set.getBool(me)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPoint(MatrixEntry me) {
        if (this.getBoundBoxSet().isInside(me, new Set2D.Characteristic() {
            @Override
            public OrderedPair getCharacteristicValue(Object pParameter) {
                MatrixEntry me = (MatrixEntry) pParameter;
                return new OrderedPair(me.j, me.i);
            }
        })) {
            for (MatrixEntry meIn : loPoints) {
                if (meIn.equalsMatrixEntry(meIn)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<MatrixEntry> getBorderPoints(ImageInt iMask) {
        List<MatrixEntry> lmeBorder = new ArrayList<>();
        for (MatrixEntry me : loPoints) {
            N8 oN8 = new N8(iMask, me.i, me.j);
            if (oN8.isBorder()) {
                lmeBorder.add(new MatrixEntry(me.i, me.j));
            }
        }
        return lmeBorder;
    }

    public void drawOnImg(ImageInt iImg) {
        for (int j = 0; j < loPoints.size() - 1; j++) {
            Line oL = new Line(loPoints.get(j), loPoints.get(j + 1));
            oL.setLine(iImg, 255);
        }
        Line oL = new Line(loPoints.get(0), loPoints.get(loPoints.size() - 1));
        oL.setLine(iImg, 255);
    }

    /*
    * Brut force, improve!
     */
    public EnumObject getDistance(ArbStructure2 arb) {
        EnumObject minDist = new EnumObject(Double.MAX_VALUE, null);
        for (MatrixEntry meOutside : this.loPoints) {
            for (MatrixEntry meInside : arb.loPoints) {
                double dist = meOutside.SecondCartesian(meInside);
                if (dist < minDist.dEnum) {
                    minDist = new EnumObject(dist, new ObjectPair(meOutside, meInside));
                }
            }
        }
        return minDist;
    }

    public void getMajorMinor(List<MatrixEntry> lme) {
        List<MatrixEntry> MajorMinor = Entries.getMajorMinorAxis(lme);
        this.loMajor = MajorMinor.subList(0, 2);
        this.loMinor = MajorMinor.subList(2, 4);
    }

    public double RotateToVolume() {

        int iCountRight = 0;
        int iCountLeft = 0;
        double dIntegrandRight = 0.0;
        double dIntegrandLeft = 0.0;

        Double dAngle = 0.0;

        if ((this.loMajor.get(0).j - this.loMajor.get(1).j) != 0) {
            double dm = ((double) (this.loMajor.get(0).i - this.loMajor.get(1).i)) / ((double) (this.loMajor.get(0).j - this.loMajor.get(1).j));
            dAngle = Math.atan(Math.abs(dm));
        }
        OrderedPair opPosition = getPosition();
        MatrixEntry mePosition = new MatrixEntry(opPosition);
        for (MatrixEntry me : this.loPoints) {

            int iPosX = me.j - mePosition.j;
            int iPosY = mePosition.i - me.i;

            double dPosXRot = Math.cos(dAngle) * iPosX - Math.sin(dAngle) * iPosY;

            if (dPosXRot > 0) {
                dIntegrandRight = dIntegrandRight + dPosXRot;
                iCountRight = iCountRight + 1;
            }

            if (dPosXRot < 0) {
                dIntegrandLeft = dIntegrandLeft + dPosXRot;
                iCountLeft = iCountLeft + 1;
            }

            if (dPosXRot == 0) {
                dIntegrandRight = dIntegrandRight + 0.25;
                dIntegrandLeft = dIntegrandLeft - 0.25;
                iCountRight = iCountRight + 1;
                iCountLeft = iCountLeft + 1;
            }

        }
//
        double RotationcentroidRight = dIntegrandRight / ((double) iCountRight);
        double RotationcentroidLeft = (-1.0) * dIntegrandLeft / ((double) iCountLeft);

        double dRotationVolume = this.loPoints.size() * 1.0 / 2.0 * Math.PI * (RotationcentroidRight + RotationcentroidLeft);

        return dRotationVolume;
    }

    @Override
    public double getSize() {
//        return Math.sqrt(loPoints.size() / Math.PI);
        return RotateToVolume();
    }

    @Override
    public double getMinorAxis() {
        if (this.loMinor.isEmpty()) {
            return 0.0;
        }
        return this.loMinor.get(0).getNorm(this.loMinor.get(1));
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMajorAxis() {
        if (this.loMajor.isEmpty()) {
            return 0.0;
        }
        return this.loMajor.get(0).getNorm(this.loMajor.get(1));
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getFormRatio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getOrientationAngle() {

        if (this.loMinor.get(0).i > this.loMinor.get(1).i) {
            Vector oVec = new Vector(new OrderedPair(this.loMinor.get(1).j, this.loMinor.get(1).i), new OrderedPair(this.loMinor.get(0).j, this.loMinor.get(0).i));
            if (oVec.opUnitTangent.y == -1.0) {
                return 0.0;
            }
            if (this.loMinor.get(0).j > this.loMinor.get(1).j) {
                return -Math.acos(oVec.opUnitTangent.y);
            } else {
                return Math.acos(oVec.opUnitTangent.y);
            }
        } else {
            Vector oVec = new Vector(new OrderedPair(this.loMinor.get(0).j, this.loMinor.get(0).i), new OrderedPair(this.loMinor.get(1).j, this.loMinor.get(1).i));
            if (oVec.opUnitTangent.y == -1.0) {
                return 0.0;
            }
            if (this.loMinor.get(0).j < this.loMinor.get(1).j) {
                return -Math.acos(oVec.opUnitTangent.y);
            } else {
                return Math.acos(oVec.opUnitTangent.y);
            }
        }

    }

    @Override
    public int getPixelCount() {
        return loPoints.size();
    }

    @Override
    public Double getNorm(ArbStructure2 o2) {
        return this.getDistance(o2).dEnum;
    }

    @Override
    public Double getNorm2(ArbStructure2 o2, String sNormType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MatrixEntry> getlmeList() {
        return loBorderPoints;
    }

    @Override
    public double getGreyDerivative() {
        return dAvergeGreyDerivative;
    }

    @Override
    public OrderedPair getSubPixelCenter() {
        if (this.loPoints.size() > 0) {
            double dXCenter = 0.0;
            double dYCenter = 0.0;
            for (MatrixEntry me : loPoints) {
                dXCenter += me.j;
                dYCenter += me.i;
            }
            return new OrderedPair(dXCenter / (double) this.loPoints.size(), dYCenter / (double) this.loPoints.size());
        } else {
            return new OrderedPair();
        }
    }

}
