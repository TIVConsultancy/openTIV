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
package com.tivconsultancy.opentiv.imageproc.primitives;

import com.tivconsultancy.opentiv.helpfunctions.matrix.Matrix;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import static com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader.getGrayScale;
import static com.tivconsultancy.opentiv.imageproc.img_io.IMG_Writer.castToByteprimitive;
import com.tivconsultancy.opentiv.math.interfaces.Additionable;
import com.tivconsultancy.opentiv.math.interfaces.Multipliable;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import com.tivconsultancy.opentiv.physics.interfaces.Prototype;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ImageInt extends ImageBoolean implements Serializable, Additionable<ImageInt>, Multipliable<ImageInt>, Prototype<ImageInt> {

    private static final long serialVersionUID = -3978520008605049954L;

    public int[][] iaPixels;
    public File origin = null;

    public ImageInt(int iLength, int jLength) {
        super(iLength, jLength);
        iaPixels = new int[iLength][jLength];
    }

    public ImageInt(int iLength, int jLength, int iGreyLevel) {
        super(iLength, jLength);
        iaPixels = new int[iLength][jLength];
        for (int[] a : iaPixels) {
            Arrays.fill(a, iGreyLevel);
        }
    }

    public ImageInt(int iLength, int jLength, double randomSeed) {
        super(iLength, jLength);
        iaPixels = new int[iLength][jLength];
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                iaPixels[i][j] = (int) (Math.random() * 255);
            }
        }
    }

    public ImageInt(int[][] iaa) {
        this(iaa, false);
    }

    public ImageInt(int[][] iaa, boolean norm) {
        super(iaa.length, iaa[0].length);
        if (norm) {
            int maxVal = Matrix.getMax(iaa);
            iaPixels = new int[iaa.length][iaa[0].length];
            for (int i = 0; i < iaa.length; i++) {
                for (int j = 0; j < iaa[0].length; j++) {
                    iaPixels[i][j] = (int) ((1.0 * iaa[i][j]) / (1.0 * maxVal) * 255);
                }
            }
        } else {
            iaPixels = new int[iaa.length][iaa[0].length];
            for (int i = 0; i < iaa.length; i++) {
                for (int j = 0; j < iaa[0].length; j++) {
                    iaPixels[i][j] = iaa[i][j];
                }
            }
        }

    }

    public ImageInt(int height, int width, byte[] a) {
        super(height, width);
//        if(iaa.length == 0) return;
        iaPixels = new int[height][width];
        int iCount = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                iaPixels[i][j] = Byte.toUnsignedInt(a[iCount]);
                iCount++;
            }
        }
    }

    public ImageInt(int[][] iaa, File origin) {
        super(iaa.length, iaa[0].length);
        this.origin = origin;
//        if(iaa.length == 0) return;
        iaPixels = new int[iaa.length][iaa[0].length];
        for (int i = 0; i < iaa.length; i++) {
            for (int j = 0; j < iaa[0].length; j++) {
                iaPixels[i][j] = iaa[i][j];
            }
        }
        baMarker = new boolean[iaa.length][iaa[0].length];
    }

    public ImageInt(int[][] iaa, boolean[][] ibb) {
        super(ibb);
        iaPixels = iaa;
    }

    public ImageInt(BufferedImage oBuffIMG) {
        BufferedImage oBufImageGrey = getGrayScale(oBuffIMG);
        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth()];
        iaData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);

        int iLength = oBufImageGrey.getHeight();
        int jLength = oBufImageGrey.getWidth();
//        int[] iaTemp = new int[iLength * jLength * oBuffIMG.getRaster().getNumBands()];
        iaPixels = new int[iLength][jLength];
        baMarker = new boolean[iLength][jLength];

//        iaTemp = oBuffIMG.getData().getPixels(0, 0, oBuffIMG.getWidth(), oBuffIMG.getHeight(), iaTemp);
        int iCount = 0;
        for (int i = 0; i < iLength; i++) {
            for (int j = 0; j < jLength; j++) {
                iaPixels[i][j] = iaData[iCount];
                iCount++;
            }
        }
    }

    public void setImage(BufferedImage oBuffIMG) {
        BufferedImage oBufImageGrey = getGrayScale(oBuffIMG);
        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth()];
        iaData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);

        int iLength = oBufImageGrey.getHeight();
        int jLength = oBufImageGrey.getWidth();
//        int[] iaTemp = new int[iLength * jLength * oBuffIMG.getRaster().getNumBands()];
        iaPixels = new int[iLength][jLength];
        baMarker = new boolean[iLength][jLength];

//        iaTemp = oBuffIMG.getData().getPixels(0, 0, oBuffIMG.getWidth(), oBuffIMG.getHeight(), iaTemp);
        int iCount = 0;
        for (int i = 0; i < iLength; i++) {
            for (int j = 0; j < jLength; j++) {
                iaPixels[i][j] = iaData[iCount];
                iCount++;
            }
        }
    }

    public void setBoolean(boolean[][] ba) {
        for (int i = 0; i < ba.length; i++) {
            for (int j = 0; j < ba[0].length; j++) {
                this.baMarker[i][j] = ba[i][j];
            }
        }
    }

    public boolean checkIfInBound(double i, double j) {
        return i < iaPixels.length && j < iaPixels[0].length;
    }

    public Double forwardDifference(int i, int j, String sCoord, String sDirec) {
        int iRef = iaPixels[i][j];
        int dDiff;
        Double dSign = 1.0;
        if (sCoord.equals("y")) {
            if (sDirec.equals("+")) {
                if (i <= 0) {
                    dDiff = iaPixels[iaPixels.length - 1][j];
                } else {
                    dDiff = iaPixels[i - 1][j];
                }

            } else {
                if (i >= (iaPixels.length - 1)) {
                    dDiff = iaPixels[0][j];
                } else {
                    dDiff = iaPixels[i + 1][j];
                }
                dSign = -1.0;
            }
        } else {
            if (sDirec.equals("+")) {
                if (j >= iaPixels[i].length - 1) {
                    dDiff = iaPixels[i][0];
                } else {
                    dDiff = iaPixels[i][j + 1];
                }

            } else {
                if (j <= 0) {
                    dDiff = iaPixels[i][iaPixels[i].length - 1];
                } else {
                    dDiff = iaPixels[i][j - 1];
                }
                dSign = -1.0;
            }
        }

        return dSign * (dDiff - iRef);
    }

    public Double forwardAverage(int i, int j, String sCoord, String sDirec) {
        int iRef = iaPixels[i][j];
        int iDiff;
        Double dSign = 1.0;
        if (sCoord.equals("y")) {
            if (sDirec.equals("+")) {
                if (i <= 0) {
                    iDiff = iaPixels[iaPixels.length - 1][j];
                } else {
                    iDiff = iaPixels[i - 1][j];
                }

            } else {
                if (i >= (iaPixels.length - 1)) {
                    iDiff = iaPixels[0][j];
                } else {
                    iDiff = iaPixels[i + 1][j];
                }
                dSign = -1.0;
            }
        } else {
            if (sDirec.equals("+")) {
                if (j >= iaPixels[i].length - 1) {
                    iDiff = iaPixels[i][0];
                } else {
                    iDiff = iaPixels[i][j + 1];
                }

            } else {
                if (j <= 0) {
                    iDiff = iaPixels[i][iaPixels[i].length - 1];
                } else {
                    iDiff = iaPixels[i][j - 1];
                }
                dSign = -1.0;
            }
        }

        return (iRef + iDiff) / 2.0;
    }

    public Double centralDifference(int i, int j, String sCoord) {
        return (forwardDifference(i, j, sCoord, "+") - forwardDifference(i, j, sCoord, "-")) / 2.0;
    }

    public Double centralDifferenceSecondDeri(int i, int j, String sCoord) {

        Double dRight;
        Double dLeft;
        if (sCoord.equals("x")) {
            dRight = centralDifference(i, j + 1, sCoord);
            dLeft = centralDifference(i, j - 1, sCoord);

        } else {
            dRight = centralDifference(i + 1, j, sCoord);
            dLeft = centralDifference(i - 1, j, sCoord);
        }

        return (dRight - dLeft) / 2.0;
    }

    public List<MatrixEntry> getNeighborsN8(int i, int j) {
        /*
         Resturn array list with counter clockwise order:
         3--2--1
         4-- --0
         5--6--7
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= iaPixels[i].length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i, j + 1));
        }

        if (j >= iaPixels[i].length - 1 || i <= 0) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i - 1, j + 1));
        }

        if (i <= 0) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i - 1, j));
        }

        if (j <= 0 || i <= 0) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i - 1, j - 1));
        }

        if (j <= 0) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i, j - 1));
        }

        if (j <= 0 || i >= iaPixels.length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i + 1, j - 1));
        }

        if (i >= iaPixels.length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i + 1, j));
        }

        if (i >= iaPixels.length - 1 || j >= iaPixels[i].length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i + 1, j + 1));
        }

        return lo;

    }

    public void setNeighborsN8(int i, int j, int iValue) {
        /*
         Resturn array list with counter clockwise order:
         3--2--1
         4-- --0
         5--6--7
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= iaPixels[i].length - 1) {
        } else {
            iaPixels[i][j + 1] = iValue;
        }

        if (j >= iaPixels[i].length - 1 || i <= 0) {
        } else {
            iaPixels[i - 1][j + 1] = iValue;
        }

        if (i <= 0) {
        } else {
            iaPixels[i - 1][j] = iValue;
        }

        if (j <= 0 || i <= 0) {
        } else {
            iaPixels[i - 1][j - 1] = iValue;
        }

        if (j <= 0) {
        } else {
            iaPixels[i][j - 1] = iValue;
        }

        if (j <= 0 || i >= iaPixels.length - 1) {
        } else {
            iaPixels[i + 1][j - 1] = iValue;
        }

        if (i >= iaPixels.length - 1) {
        } else {
            iaPixels[i + 1][j] = iValue;
        }

        if (i >= iaPixels.length - 1 || j >= iaPixels[i].length - 1) {
        } else {
            iaPixels[i + 1][j + 1] = iValue;
        }
    }

    public void setNeighborsN8(int i, int j, int iValue, SideCondition<MatrixEntry> o) {
        /*
         Resturn array list with counter clockwise order:
         3--2--1
         4-- --0
         5--6--7
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= iaPixels[i].length - 1) {
        } else {
            if (o.check(new MatrixEntry(i, j + 1))) {
                iaPixels[i][j + 1] = iValue;
            }
        }

        if (j >= iaPixels[i].length - 1 || i <= 0) {
        } else {
            if (o.check(new MatrixEntry(i - 1, j + 1))) {
                iaPixels[i - 1][j + 1] = iValue;
            }
        }

        if (i <= 0) {
        } else {
            if (o.check(new MatrixEntry(i - 1, j))) {
                iaPixels[i - 1][j] = iValue;
            }
        }

        if (j <= 0 || i <= 0) {
        } else {
            if (o.check(new MatrixEntry(i - 1, j - 1))) {
                iaPixels[i - 1][j - 1] = iValue;
            }
        }

        if (j <= 0) {
        } else {
            if (o.check(new MatrixEntry(i, j - 1))) {
                iaPixels[i][j - 1] = iValue;
            }
        }

        if (j <= 0 || i >= iaPixels.length - 1) {
        } else {
            if (o.check(new MatrixEntry(i + 1, j - 1))) {
                iaPixels[i + 1][j - 1] = iValue;
            }
        }

        if (i >= iaPixels.length - 1) {
        } else {
            if (o.check(new MatrixEntry(i + 1, j))) {
                iaPixels[i + 1][j] = iValue;
            }
        }

        if (i >= iaPixels.length - 1 || j >= iaPixels[i].length - 1) {
        } else {
            if (o.check(new MatrixEntry(i + 1, j + 1))) {
                iaPixels[i + 1][j + 1] = iValue;
            }
        }
    }

    public void setNeighborsN8(int i, int j, boolean bValue) {
        /*
         Resturn array list with counter clockwise order:
         3--2--1
         4-- --0
         5--6--7
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= baMarker[i].length - 1) {
        } else {
            baMarker[i][j + 1] = bValue;
        }

        if (j >= baMarker[i].length - 1 || i <= 0) {
        } else {
            baMarker[i - 1][j + 1] = bValue;
        }

        if (i <= 0) {
        } else {
            baMarker[i - 1][j] = bValue;
        }

        if (j <= 0 || i <= 0) {
        } else {
            baMarker[i - 1][j - 1] = bValue;
        }

        if (j <= 0) {
        } else {
            baMarker[i][j - 1] = bValue;
        }

        if (j <= 0 || i >= baMarker.length - 1) {
        } else {
            baMarker[i + 1][j - 1] = bValue;
        }

        if (i >= baMarker.length - 1) {
        } else {
            baMarker[i + 1][j] = bValue;
        }

        if (i >= baMarker.length - 1 || j >= baMarker[i].length - 1) {
        } else {
            baMarker[i + 1][j + 1] = bValue;
        }

    }

    public void setNeighborsN8(int i, int j, boolean bValue, SideCondition<MatrixEntry> o) {
        /*
         Resturn array list with counter clockwise order:
         3--2--1
         4-- --0
         5--6--7
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= baMarker[i].length - 1) {
        } else {
            if (o.check(new MatrixEntry(i, j + 1))) {
                baMarker[i][j + 1] = bValue;
            }
        }

        if (j >= baMarker[i].length - 1 || i <= 0) {
        } else {
            if (o.check(new MatrixEntry(i - 1, j + 1))) {
                baMarker[i - 1][j + 1] = bValue;
            }
        }

        if (i <= 0) {
        } else {
            if (o.check(new MatrixEntry(i - 1, j))) {
                baMarker[i - 1][j] = bValue;
            }
        }

        if (j <= 0 || i <= 0) {
        } else {
            if (o.check(new MatrixEntry(i - 1, j - 1))) {
                baMarker[i - 1][j - 1] = bValue;
            }
        }

        if (j <= 0) {
        } else {
            if (o.check(new MatrixEntry(i, j - 1))) {
                baMarker[i][j - 1] = bValue;
            }
        }

        if (j <= 0 || i >= baMarker.length - 1) {
        } else {
            if (o.check(new MatrixEntry(i + 1, j - 1))) {
                baMarker[i + 1][j - 1] = bValue;
            }
        }

        if (i >= baMarker.length - 1) {
        } else {
            if (o.check(new MatrixEntry(i + 1, j))) {
                baMarker[i + 1][j] = bValue;
            }
        }

        if (i >= baMarker.length - 1 || j >= baMarker[i].length - 1) {
        } else {
            if (o.check(new MatrixEntry(i + 1, j + 1))) {
                baMarker[i + 1][j + 1] = bValue;
            }
        }

    }

    public void setNeighborsN4(int i, int j, boolean bValue, SideCondition<MatrixEntry> o) {
        /*
         Resturn array list with counter clockwise order:
         3--2--1
         4-- --0
         5--6--7
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= baMarker[i].length - 1) {
        } else {
            if (o.check(new MatrixEntry(i, j + 1))) {
                baMarker[i][j + 1] = bValue;
            }
        }

        if (i <= 0) {
        } else {
            if (o.check(new MatrixEntry(i - 1, j))) {
                baMarker[i - 1][j] = bValue;
            }
        }

        if (j <= 0) {
        } else {
            if (o.check(new MatrixEntry(i, j - 1))) {
                baMarker[i][j - 1] = bValue;
            }
        }

        if (i >= baMarker.length - 1) {
        } else {
            if (o.check(new MatrixEntry(i + 1, j))) {
                baMarker[i + 1][j] = bValue;
            }
        }

    }

    public void setPoints(List<MatrixEntry> lme, int iValue) {
        setPoints(lme, iValue, 0);
//        for (MatrixEntry me : lme) {
//            if (me.i >= 0 && me.j >= 0 && me.i < iaPixels.length && me.j < iaPixels[0].length) {
//                iaPixels[me.i][me.j] = iValue;
//            }
//        }
    }

    public void setPoints(List<MatrixEntry> lme, int iValue, int iRadius) {
        if (iRadius == 0) {
            for (MatrixEntry me : lme) {
                if (me.i >= 0 && me.j >= 0 && me.i < iaPixels.length && me.j < iaPixels[0].length) {
                    iaPixels[me.i][me.j] = iValue;
                }
            }
        } else {
            for (MatrixEntry meO : lme) {
                if (meO.i >= 0 && meO.j >= 0 && meO.i < iaPixels.length && meO.j < iaPixels[0].length) {
                    List<MatrixEntry> expand = this.getsubArea(meO.i, meO.j, iRadius);
                    for (MatrixEntry me : expand) {
                        iaPixels[me.i][me.j] = iValue;
                    }
                }
            }
        }
    }

    public void setPoints(List<MatrixEntry> lme, int iValue, int iRadius, Operation op) {
        if (iRadius == 0) {
            for (MatrixEntry me : lme) {
                if (me.i >= 0 && me.j >= 0 && me.i < iaPixels.length && me.j < iaPixels[0].length) {
                    iaPixels[me.i][me.j] = iValue;
                }
            }
        } else {
            for (MatrixEntry meO : lme) {
                MatrixEntry meOp = op.perform(meO, this);
                if(meOp == null) continue;
                if (meOp.i >= 0 && meOp.j >= 0 && meOp.i < iaPixels.length && meOp.j < iaPixels[0].length) {
                    List<MatrixEntry> expand = this.getsubArea(meOp.i, meOp.j, iRadius);
                    for (MatrixEntry me : expand) {
                        iaPixels[me.i][me.j] = iValue;
                    }
                }
            }
        }
    }

    public void setPointsIMGP(List<ImagePoint> lme, int iValue) {
        for (ImagePoint me : lme) {
            OrderedPair op = me.getPos();
            if (op.x >= 0 && op.y >= 0 && op.y < iaPixels.length && op.x < iaPixels[0].length) {
                iaPixels[(int) op.y][(int) op.x] = iValue;
            }
        }
    }

    public void setPoints(HashSet<ImagePointInt> lme, int iValue) {
        for (MatrixEntry me : lme) {
            if (me.i >= 0 && me.j >= 0 && me.i < iaPixels.length && me.j < iaPixels[0].length) {
                iaPixels[me.i][me.j] = iValue;
            }
        }
    }

    public void setPointIMGP(ImagePoint me, int iValue) {
        OrderedPair op = me.getPos();
        if (op.x >= 0 && op.y >= 0 && op.y < iaPixels.length && op.x < iaPixels[0].length) {
            iaPixels[(int) op.y][(int) op.x] = iValue;
        }

    }

    public void setPoint(MatrixEntry me, int iValue) {
        if (me.i >= 0 && me.j >= 0 && me.i < iaPixels.length && me.j < iaPixels[0].length) {
            iaPixels[me.i][me.j] = iValue;
        }
    }

    public Integer getPointIMGP(ImagePoint me) {
        OrderedPair op = me.getPos();
        if (op.x >= 0 && op.y >= 0 && op.y < iaPixels.length && op.x < iaPixels[0].length) {
            return iaPixels[(int) op.y][(int) op.x];
        }
        return null;
    }

    public Integer getValue(MatrixEntry me) {
        if (me.j >= 0 && me.j < iaPixels[0].length && me.i >= 0 && me.i < iaPixels.length) {
            return iaPixels[me.i][me.j];
        }
        return null;
    }

    public List<MatrixEntry> getNeighborsN4(int i, int j) {
        /*
         Resturn array list with counter clockwise order:
         ---1---
         2-- --0
         ---3---
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= iaPixels[i].length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i, j + 1));
        }

        if (i <= 0) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i - 1, j));
        }

        if (j <= 0) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i, j - 1));
        }

        if (i >= iaPixels.length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i + 1, j));
        }

        return lo;

    }

    public List<MatrixEntry> getNeighborsN4(int i, int j, SideCondition oSide) {
        /*
         Resturn array list with counter clockwise order:
         ---1---
         2-- --0
         ---3---
        
         Values are null when on boundary
         */

        List<MatrixEntry> lo = new ArrayList<>();
        if (j >= iaPixels[i].length - 1) {
            lo.add(null);
        } else {
            if (oSide.check(new MatrixEntry(i, j + 1))) {
                lo.add(new MatrixEntry(i, j + 1));
            } else {
                lo.add(null);
            }
        }

        if (i <= 0) {
            lo.add(null);
        } else {
            if (oSide.check(new MatrixEntry(i - 1, j))) {
                lo.add(new MatrixEntry(i - 1, j));
            } else {
                lo.add(null);
            }
        }

        if (j <= 0) {
            lo.add(null);
        } else {
            if (oSide.check(new MatrixEntry(i, j - 1))) {
                lo.add(new MatrixEntry(i, j - 1));
            } else {
                lo.add(null);
            }
        }

        if (i >= iaPixels.length - 1) {
            lo.add(null);
        } else {
            if (oSide.check(new MatrixEntry(i + 1, j))) {
                lo.add(new MatrixEntry(i + 1, j));
            } else {
                lo.add(null);
            }
        }

        return lo;
    }

    public ImageInt getsubY(Set1D yInterval) {
        return getsubY((int) yInterval.dLeftBorder, (int) yInterval.dRightBorder);
    }

    public ImageInt getsubY(int iMin, int iMax) {

        int iMinIndex = 0;
        if (iMin > 0 && iMin < iaPixels.length - 1) {
            iMinIndex = iMin;
        }
        int iMaxIndex = iaPixels.length;
        if (iMax < iaPixels.length && iMax > 0) {
            iMaxIndex = iMax;
        }
        int[][] iaNew = new int[iMaxIndex - iMinIndex][iaPixels[0].length];
        for (int i = iMinIndex; i < iMaxIndex; i++) {
            iaNew[i - iMinIndex] = iaPixels[i];
        }

        return new ImageInt(iaNew);

    }

    public ImageInt getsubY2(int iMin, int iMax) {

        int iMinIndex = 0;
        if (iMin > 0 && iMin < iaPixels.length - 1) {
            iMinIndex = iMin;
        }
        int iMaxIndex = iaPixels.length;
        if (iMax < iaPixels.length && iMax > 0) {
            iMaxIndex = iMax;
        }
        int[][] iaNew = new int[iMax - iMinIndex][iaPixels[0].length];
        for (int i = iMinIndex; i < iMaxIndex; i++) {
            iaNew[i - iMinIndex] = iaPixels[i];
        }

        return new ImageInt(iaNew);

    }

    public int getSum() {
        int iSum = 0;
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                iSum = iSum + iaPixels[i][j];
            }
        }
        return iSum;
    }

    public ImageInt getsubX(Set1D xInterval) {
        return getsubX((int) xInterval.dLeftBorder, (int) xInterval.dRightBorder);
    }

    public ImageInt getsubX(int iMin, int iMax) {

        int iMinIndex = 0;
        if (iMin > 0 && iMin < iaPixels[0].length - 1) {
            iMinIndex = iMin;
        }

        int iMaxIndex = iaPixels[0].length;
        if (iMax < iaPixels[0].length && iMax > 0) {
            iMaxIndex = iMax;
        }

        int[][] iaNew = new int[iaPixels.length][iMaxIndex - iMinIndex];
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = iMinIndex; j < iMaxIndex; j++) {
                iaNew[i][j - iMinIndex] = iaPixels[i][j];
            }
        }

        return new ImageInt(iaNew);
    }

    public ImageInt getsubX2(int iMin, int iMax) {

        int iMinIndex = 0;
        if (iMin > 0 && iMin < iaPixels[0].length - 1) {
            iMinIndex = iMin;
        }

        int iMaxIndex = iaPixels[0].length;
        if (iMax < iaPixels[0].length && iMax > 0) {
            iMaxIndex = iMax;
        }

        int[][] iaNew = new int[iaPixels.length][iMax - iMin];
        if ((iMaxIndex - iMinIndex) > (iMax - iMin)) {
            return new ImageInt(iaNew);
        }
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = iMinIndex; j < iMaxIndex; j++) {
                iaNew[i][j - iMinIndex] = iaPixels[i][j];
            }
        }
        return new ImageInt(iaNew);
    }

    public void resetMarkers() {
        for (int i = 0; i < baMarker.length; i++) {
            for (int j = 0; j < baMarker[0].length; j++) {
                baMarker[i][j] = false;
            }
        }
    }

    public List<MatrixEntry> getsubArea(int iCenter, int jCenter, int iRadius) {
        List<MatrixEntry> lmeReturn = new ArrayList<>();
        for (int i = Math.max(iCenter - iRadius, 0); i < Math.min(iCenter + iRadius, iaPixels.length); i++) {
            for (int j = Math.max(jCenter - iRadius, 0); j < Math.min(jCenter + iRadius, iaPixels[0].length); j++) {
                lmeReturn.add(new MatrixEntry(i, j, iaPixels[i][j]));
            }
        }
        return lmeReturn;
    }

    public ImageInt getsubArea2(int i, int j, int iRadius) {
        ImageInt iaReturn = this.getsubX(j - iRadius, j + iRadius);
        iaReturn = iaReturn.getsubY(i - iRadius, i + iRadius);
        return iaReturn;
    }

    public MatrixEntry containsValue(int iValue) {
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                if (iaPixels[i][j] == iValue) {
                    return new MatrixEntry(i, j);
                }
            }
        }
        return null;
    }

    public BufferedImage getBuffImage() {
        byte[] bapixels = castToByteprimitive(this);

        BufferedImage oFrameImage = new BufferedImage(iaPixels[0].length, iaPixels.length, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = oFrameImage.getRaster();

        raster.setDataElements(0, 0, iaPixels[0].length, iaPixels.length, bapixels);

        return oFrameImage;
    }

    public boolean isInside(int i, int j) {
        if (this.iaPixels.length > i && this.iaPixels[0].length > j && i >= 0 && j >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public ImageInt clone() {
        int[][] iaClone = new int[iaPixels.length][iaPixels[0].length];
        boolean[][] baClone = new boolean[iaPixels.length][iaPixels[0].length];
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                iaClone[i][j] = iaPixels[i][j];
                baClone[i][j] = baMarker[i][j];
            }
        }
        return new ImageInt(iaClone);
    }

    public static ImageInt toImageInt(ImageIcon imgIcon) {
        Image img = imgIcon.getImage();
        return toImageInt(img);
    }

    public static ImageInt toImageInt(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();
        return new ImageInt(bi);
    }

    public static ImageInt valueOf(ImageBoolean imgB) {
        ImageInt imgReturn = new ImageInt(imgB.baMarker.length, imgB.baMarker[0].length, 0);
        for (int i = 0; i < imgB.baMarker.length; i++) {
            for (int j = 0; j < imgB.baMarker[0].length; j++) {
                if (imgB.baMarker[i][j]) {
                    imgReturn.iaPixels[i][j] = 255;
                }
            }
        }
        return imgReturn;
    }

    public int[][] getGLAM(int dividerGrey) {
        boolean[][] shape = new boolean[][]{{false, true, false}, {true, true, true}, {false, true, false}};
        return getGLAM(dividerGrey, shape);
    }

    public int[][] getGLAM(MatrixEntry me, int dRadius, int dividerGrey, boolean[][] shape) {
        ImageInt sub = this.getsubArea2(me.i, me.j, dRadius);
        return sub.getGLAM(dividerGrey, shape);
    }

    public int[][] getGLAM(MatrixEntry me, int dRadius, int dividerGrey) {
        ImageInt sub = this.getsubArea2(me.i, me.j, dRadius);
        return sub.getGLAM(dividerGrey);
    }

    public int[] getGLAM2(MatrixEntry me, int dRadius, int dividerGrey) {
        ImageInt sub = this.getsubArea2(me.i, me.j, dRadius);
        return sub.getGLAM2(dividerGrey);
    }

    public int[][] getGLAM(int dividerGrey, boolean[][] shape) {

        int shapeSize = (int) (shape.length / 2.0);

        int[][] reduceMatrix = new int[iaPixels.length][iaPixels[0].length];
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                reduceMatrix[i][j] = (int) (iaPixels[i][j] / dividerGrey);
            }
        }

        int[][] glam = new int[(int) (255 / dividerGrey) + 1][(int) (255 / dividerGrey) + 1];

        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                int ig = reduceMatrix[i][j];
                for (int isub = -shapeSize; isub <= shapeSize; isub++) {
                    for (int jsub = -shapeSize; jsub <= shapeSize; jsub++) {
                        if (this.isInside(i + isub, j + jsub) && shape[isub + shapeSize][jsub + shapeSize]) {
                            if (isub == 0 && jsub == 0) {
                                continue;
                            }
                            glam[ig][reduceMatrix[i + isub][j + jsub]] = glam[ig][reduceMatrix[i + isub][j + jsub]] + 1;
                        }
                    }
                }
            }
        }
        return glam;
    }

    public int[] getGLAM2(int dividerGrey) {

        List<boolean[][]> directions = new ArrayList<>();
        boolean[][] shapeRB = new boolean[][]{{false, false, false}, {false, false, false}, {false, false, true}};
        boolean[][] shapeR = new boolean[][]{{false, false, false}, {false, false, true}, {false, false, false}};
        boolean[][] shapeRT = new boolean[][]{{false, false, true}, {false, false, false}, {false, false, false}};
        boolean[][] shapeT = new boolean[][]{{false, true, false}, {false, false, false}, {false, false, false}};
        boolean[][] shapeLT = new boolean[][]{{true, false, false}, {false, false, false}, {false, false, false}};
        boolean[][] shapeL = new boolean[][]{{false, false, false}, {true, false, false}, {false, false, false}};
        boolean[][] shapeLB = new boolean[][]{{false, false, false}, {false, false, false}, {true, false, false}};
        boolean[][] shapeB = new boolean[][]{{false, false, false}, {false, false, false}, {true, false, false}};

        directions.add(shapeRB);
        directions.add(shapeR);
        directions.add(shapeRT);
        directions.add(shapeT);
        directions.add(shapeLT);
        directions.add(shapeL);
        directions.add(shapeLB);
        directions.add(shapeB);

        List<int[][]> res = new ArrayList<>();
        for (boolean[][] shape : directions) {
            res.add(getGLAM(dividerGrey, shape));
        }

        int[] features = new int[res.size() * res.get(0).length * res.get(0)[0].length];

        int iCounter = 0;
        for (int[][] iaa : res) {
            for (int[] ia : iaa) {
                for (int i : ia) {
                    features[iCounter] = i;
                    iCounter++;
                }
            }
        }

        return features;

    }

    public int[] getIGLAM(int dividerGrey) {

        List<boolean[][]> directions = new ArrayList<>();
        boolean[][] shapeRB = new boolean[][]{{false, false, false}, {false, false, false}, {false, false, true}};
        boolean[][] shapeR = new boolean[][]{{false, false, false}, {false, false, true}, {false, false, false}};
        boolean[][] shapeRT = new boolean[][]{{false, false, true}, {false, false, false}, {false, false, false}};
        boolean[][] shapeT = new boolean[][]{{false, true, false}, {false, false, false}, {false, false, false}};
        boolean[][] shapeLT = new boolean[][]{{true, false, false}, {false, false, false}, {false, false, false}};
        boolean[][] shapeL = new boolean[][]{{false, false, false}, {true, false, false}, {false, false, false}};
        boolean[][] shapeLB = new boolean[][]{{false, false, false}, {false, false, false}, {true, false, false}};
        boolean[][] shapeB = new boolean[][]{{false, false, false}, {false, false, false}, {true, false, false}};

        directions.add(shapeRB);
        directions.add(shapeR);
        directions.add(shapeRT);
        directions.add(shapeT);
        directions.add(shapeLT);
        directions.add(shapeL);
        directions.add(shapeLB);
        directions.add(shapeB);

        List<int[][]> res = new ArrayList<>();
        for (boolean[][] shape : directions) {
            res.add(getGLAM(dividerGrey, shape));
        }

        int[] features = new int[res.size() * res.get(0).length * res.get(0)[0].length];

        int iCounter = 0;
        for (int[][] iaa : res) {
            for (int[] ia : iaa) {
                for (int i : ia) {
                    features[iCounter] = i;
                    iCounter++;
                }
            }
        }

        return features;

    }

    public void normalize() {
        int iMax = 0;
        int iMin = Integer.MAX_VALUE;
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                if (iMax < iaPixels[i][j]) {
                    iMax = iaPixels[i][j];
                }
                if (iMin > iaPixels[i][j]) {
                    iMin = iaPixels[i][j];
                }
            }
        }

        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                iaPixels[i][j] = (int) ((Math.abs(iMin) + iaPixels[i][j]) * (255.0 / (iMax + Math.abs(iMin))));
            }
        }
    }

    public List<MatrixEntry> getPoints(int value) {
        List<MatrixEntry> lme = new ArrayList<>();
        for (int i = 0; i < this.iaPixels.length; i++) {
            for (int j = 0; j < this.iaPixels[0].length; j++) {
                if (this.iaPixels[i][j] == value) {
                    lme.add(new MatrixEntry(i, j));
                }
            }
        }
        return lme;
    }

    public void setField(int[][] iaField, int iStart, int jStart) {
        for (int i = 0; i < iaField.length; i++) {
            for (int j = 0; j < iaField[0].length; j++) {
                this.iaPixels[iStart + i][jStart + j] = iaField[i][j];
            }
        }
    }

    public ImageInt flip() {
        int[][] ianew = new int[iaPixels[0].length][iaPixels.length];
        for (int i = 0; i < iaPixels.length; i++) {
            for (int j = 0; j < iaPixels[0].length; j++) {
                ianew[j][i] = iaPixels[i][j];
            }
        }
        return new ImageInt(ianew);
    }

    public ImageInt rotateImage() {
        int[][] arr = iaPixels;
        int[][] newArray = new int[iaPixels[0].length][iaPixels.length];

        for (int i = 0; i < arr[0].length; i++) {
            for (int j = arr.length - 1; j >= 0; j--) {
                newArray[i][j] = arr[j][i];
            }
        }
        return new ImageInt(newArray);
    }

    public ImageInt scale(int scaledWidth, int scaledHeight) {
        BufferedImage inputImage = this.getBuffImage();
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return new ImageInt(outputImage);
    }

    public void iterate(IterativeFunction fun) {
        for (int i = 0; i < this.iaPixels.length; i++) {
            for (int j = 0; j < this.iaPixels[0].length; j++) {
                fun.perform(i, j);
            }
        }
    }

    public void iterate(IterativeFunction2 fun) {
        for (int i = 0; i < this.iaPixels.length; i++) {
            for (int j = 0; j < this.iaPixels[0].length; j++) {
                fun.perform(i, j, this);
            }
        }
    }

    @Override
    public ImageInt add(ImageInt o2) {
        return this.add(o2, 255);
    }

    public ImageInt add(ImageInt o2, int max) {
        ImageInt o1 = this;
        if (this.iaPixels.length != o2.iaPixels.length || this.iaPixels[0].length != o2.iaPixels[0].length) {
            throw new UnsupportedOperationException("Images must have same size for addition.");
        }
        this.iterate((int i, int j) -> {
            o2.iaPixels[i][j] = Math.min(max, o2.iaPixels[i][j] + o1.iaPixels[i][j]);
        });
        return o2;
    }

    @Override
    public ImageInt add2(ImageInt o2, String sType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ImageInt getPrototype() {
        if(iaPixels != null && this.iaPixels.length>0){
            return new ImageInt(this.iaPixels.length, this.iaPixels[0].length, 0);
        }
        return new ImageInt(1, 1, 0);
    }

    @Override
    public ImageInt mult(Double d) {
        ImageInt multIMG = this.clone();
        ImageInt start = this;
        iterate(new IterativeFunction() {
            @Override
            public void perform(int i, int j) {
                multIMG.iaPixels[i][j] = (int) (start.iaPixels[i][j] * d);
            }
        } );
        return multIMG;
    }

    @Override
    public ImageInt mult2(Double d, String sType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public interface IterativeFunction {

        public void perform(int i, int j);
    }

    public interface IterativeFunction2 {

        public void perform(int i, int j, ImageInt img);
    }

    public interface Operation {
        
        public MatrixEntry perform(MatrixEntry me, ImageInt img);

    }

}
