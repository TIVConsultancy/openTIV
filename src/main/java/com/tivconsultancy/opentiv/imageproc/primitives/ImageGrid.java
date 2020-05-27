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

import static com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader.getGrayScale;
import static com.tivconsultancy.opentiv.imageproc.img_io.IMG_Writer.castToByteprimitive;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition;
import com.tivconsultancy.opentiv.math.interfaces.Value;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.sets.Set2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ImageGrid implements Serializable{

    private static final long serialVersionUID = 98231744434220894L;

    public int iLength;
    public int jLength;
    public ImagePoint[] oa;

    public ImageGrid(int iLength, int jLength) {
        this.iLength = iLength;
        this.jLength = jLength;
        this.oa = new ImagePoint[iLength * jLength];
        for (int i = 0; i < this.oa.length; i++) {
            oa[i] = new ImagePoint(i, 0, this);
        }
    }
    
     public ImageGrid(int iLength, int jLength, int iGreyLevel ) {
        this.iLength = iLength;
        this.jLength = jLength;
        this.oa = new ImagePoint[iLength * jLength];
        for (int i = 0; i < this.oa.length; i++) {
            oa[i] = new ImagePoint(i, iGreyLevel, this);
        }
    }

    public ImageGrid(int[][] iaa) {
        this.iLength = iaa.length;
        this.jLength = iaa[0].length;
        this.oa = new ImagePoint[iLength * jLength];
        int iCount = 0;
        for (int[] ia : iaa) {
            for (int i : ia) {
                oa[iCount] = new ImagePoint(iCount, i, this);
                iCount++;
            }
        }
    }

    public ImageGrid(int iLength, int jLength, int[] ia) {
        if (iLength * jLength != ia.length) {
            throw new UnsupportedOperationException("");
        }
        this.iLength = iLength;
        this.jLength = jLength;
        this.oa = new ImagePoint[iLength * jLength];        
        for (int i = 0; i < iLength * jLength; i++) {
            oa[i] = new ImagePoint(i, ia[i], this);
        }
    }

    public ImageGrid(BufferedImage oBuffIMG) {        
        BufferedImage oBufImageGrey = getGrayScale(oBuffIMG);
        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth()];
        iaData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);    
        iLength = oBufImageGrey.getHeight();
        jLength = oBufImageGrey.getWidth();
        if (iLength * jLength != iaData.length) {
            throw new UnsupportedOperationException("");
        }
        this.oa = new ImagePoint[iLength * jLength];
        for (int i = 0; i < iLength * jLength; i++) {
            oa[i] = new ImagePoint(i, iaData[i], this);
        }
    }

    public OrderedPair getPos(ImagePoint o) {
        if (o == null || o.i == null) {
            return null;
        }
        Integer Indexi = ((int) o.i / this.jLength);
        Integer Indexj = o.i - Indexi * this.jLength;
        OrderedPair opPos;
        if (o.iValue != null) {
            opPos = new OrderedPair(Indexj, Indexi, new Double(o.iValue));
        } else {
            opPos = new OrderedPair(Indexj, Indexi, null);
        }

        return opPos;
    }

    public boolean checkIfInBound(OrderedPair op) {
        return op.x < jLength && op.y < iLength;
    }

    public int getIndex(OrderedPair op) {
        //there is no check if i,j is in bounds for speed up issues
        return (int) ((op.y) * jLength + op.x);
    }
    
    public int getIndex(int i, int j) {
        //there is no check if i,j is in bounds for speed up issues
        return i * jLength + j;
    }

    public boolean checkIfOnBoundary(ImagePoint o) {
        OrderedPair oRef = this.getPos(o);
        if ((oRef.x + 1) == this.jLength) {
            return true;
        }
        if (oRef.x == 0) {
            return true;
        }
        if ((oRef.y + 1) == this.iLength) {
            return true;
        }
        return oRef.y == 0;
    }

    public int[][] getMatrix() {
        int[][] ia = new int[iLength][jLength];
        for (ImagePoint o : oa) {
            ia[(int) o.getPos().y][(int) o.getPos().x] = o.iValue;
        }
        return ia;
    }
    
    public int[] getData() {
        int[] ia = new int[iLength*jLength];
        for (ImagePoint o : oa) {
            ia[o.i] = o.iValue;
        }
        return ia;
    }

    public Double forwardDifference(ImagePoint oRef, String sCoord, String sDirec, Value o) {
        ImagePoint dDiff;
        Double dSign = 1.0;
        if (sCoord.equals("y")) {
            if (sDirec.equals("+")) {
                dDiff = oa[oRef.i - this.jLength];
            } else {
                dDiff = oa[oRef.i + this.jLength];
                dSign = -1.0;
            }
        } else {
            if (sDirec.equals("+")) {
                dDiff = oa[oRef.i + 1];
            } else {
                dDiff = oa[oRef.i - 1];
                dSign = -1.0;
            }
        }

        Double d1 = o.getValue(oRef);
        Double d2 = o.getValue(dDiff);

        return dSign * (d2 - d1);
    }

    public Double forwardAverage(ImagePoint oRef, String sCoord, String sDirec, Value o) {
        ImagePoint dAvg;
        if (sCoord.equals("y")) {
            if (sDirec.equals("+")) {
                dAvg = oa[oRef.i - this.jLength];
            } else {
                dAvg = oa[oRef.i + this.jLength];
            }
        } else {
            if (sDirec.equals("+")) {
                dAvg = oa[oRef.i + 1];
            } else {
                dAvg = oa[oRef.i - 1];
            }
        }

        Double d1 = o.getValue(oRef);
        Double d2 = o.getValue(dAvg);

        return (d2 + d1) / 2.0;
    }

    public Double centralDifference(ImagePoint oRef, String sCoord, Value o) {
        ImagePoint dDiffRight;
        ImagePoint dDiffLeft;
        Double dSign = 1.0;
        if (sCoord.equals("y")) {
            dDiffRight = oa[oRef.i - this.jLength];
            dDiffLeft = oa[oRef.i + this.jLength];

        } else {
            dDiffRight = oa[oRef.i + 1];
            dDiffLeft = oa[oRef.i - 1];
        }

        Double dRight = o.getValue(dDiffRight);
        Double dLeft = o.getValue(dDiffLeft);

        return (dRight - dLeft) / 2.0;
    }

    public Double centralDifferenceSecondDeri(ImagePoint oRef, String sCoord, Value o) {

        List<ImagePoint> loN4 = this.getNeighborsN4(oRef);

        Double dRight;
        Double dLeft;
        if (sCoord.equals("x")) {
            dRight = centralDifference(loN4.get(0), sCoord, o);
            dLeft = centralDifference(loN4.get(2), sCoord, o);

        } else {
            dRight = centralDifference(loN4.get(1), sCoord, o);
            dLeft = centralDifference(loN4.get(3), sCoord, o);
        }

        return (dRight - dLeft) / 2.0;
    }

    public Double centralDifferenceMixed(ImagePoint oRef, Value o) {

        List<ImagePoint> loN4 = this.getNeighborsN4(oRef);

        Double dRight;
        Double dLeft;
        dRight = centralDifference(loN4.get(1), "x", o);
        dLeft = centralDifference(loN4.get(3), "x", o);

        return (dRight - dLeft) / 2.0;
    }

    public ArrayList<ImagePoint> getNeighborsN8(ImagePoint o) {
        /*
         Resturn array list with counter clockwise order:
         3--2--1
         4-- --0
         5--6--7
        
         Values are null when on boundary
         */

        OrderedPair oRef = this.getPos(o);
        ArrayList<ImagePoint> lo = new ArrayList<>();
        //0
        if ((oRef.x + 1) == this.jLength) {
            lo.add(null);
        } else {
            lo.add(oa[o.i + 1]);
        }
        //1
        if (oRef.y == 0 || ((oRef.x + 1) == this.jLength)) {
            lo.add(null);
        } else {
            lo.add(oa[o.i - this.jLength + 1]);
        }
        //2
        if (oRef.y == 0) {
            lo.add(null);
        } else {
            lo.add(oa[o.i - this.jLength]);
        }
        //3
        if (oRef.y == 0 || oRef.x == 0) {
            lo.add(null);
        } else {
            lo.add(oa[o.i - this.jLength - 1]);
        }
        //4
        if (oRef.x == 0) {
            lo.add(null);
        } else {
            lo.add(oa[o.i - 1]);
        }
        //5
        if (oRef.x == 0 || ((oRef.y + 1) == this.iLength)) {
            lo.add(null);
        } else {
            lo.add(oa[o.i + this.jLength - 1]);
        }
        //6
        if (((oRef.y + 1) == this.iLength)) {
            lo.add(null);
        } else {
            lo.add(oa[o.i + this.jLength]);
        }
        //7
        if (((oRef.x + 1) == this.jLength) || ((oRef.y + 1) == this.iLength)) {
            lo.add(null);
        } else {
            lo.add(oa[o.i + this.jLength + 1]);
        }
        return lo;
    }

    public ArrayList<ImagePoint> getNeighborsN4(ImagePoint o) {
        /*
         Resturn array list with counter clockwise order:
         ---1---
         2-- --0
         ---3---
        
         Values are null when on boundary
         */

        OrderedPair oRef = this.getPos(o);
        ArrayList<ImagePoint> lo = new ArrayList<>();
        //0
        if ((oRef.x + 1) == this.jLength) {
            lo.add(null);
        } else {
            lo.add(oa[o.i + 1]);
        }
        //1
        if (oRef.y == 0) {
            lo.add(null);
        } else {
            lo.add(oa[o.i - this.jLength]);
        }
        //2
        if (oRef.x == 0) {
            lo.add(null);
        } else {
            lo.add(oa[o.i - 1]);
        }
        //3
        if (((oRef.y + 1) == this.iLength)) {
            lo.add(null);
        } else {
            lo.add(oa[o.i + this.jLength]);
        }
        return lo;
    }

    public ArrayList<ImagePoint> getNeighborsN4(ImagePoint o, SideCondition oSide) {
        /*
         Resturn array list with counter clockwise order:
         ---1---
         2-- --0
         ---3---
        
         Values are null when on boundary
         */

        OrderedPair oRef = this.getPos(o);
        ArrayList<ImagePoint> lo = new ArrayList<>();
        //0
        if ((oRef.x + 1) == this.jLength) {
            lo.add(null);
        } else {
            if (oSide.check(oa[o.i + 1])) {
                lo.add(oa[o.i + 1]);
            } else {
                lo.add(null);
            }
        }
        //1
        if (oRef.y == 0) {
            lo.add(null);
        } else {
            if (oSide.check(oa[o.i - this.jLength])) {
                lo.add(oa[o.i - this.jLength]);
            }

        }
        //2
        if (oRef.x == 0) {
            lo.add(null);
        } else {
            if (oSide.check(oa[o.i - 1])) {
                lo.add(oa[o.i - 1]);
            } else {
                lo.add(null);
            }
        }
        //3
        if (((oRef.y + 1) == this.iLength)) {
            lo.add(null);
        } else {
            if (oSide.check(oa[o.i + this.jLength])) {
                lo.add(oa[o.i + this.jLength]);
            } else {
                lo.add(null);
            }
        }
        return lo;
    }

    public void setPoint(Collection<ImagePoint> lo, int iValue) {
        for (ImagePoint o : lo) {
            if (o != null) {
                setPoint(o, iValue);
            }
        }
    }

    public void setPoint(Collection<ImagePoint> lo, setIMGPoint oSetFun, ImageGrid oGrid) {
        for (ImagePoint o : lo) {
            if (o != null) {
                oSetFun.setPoint(oGrid, o);
            }
        }
    }

    public static void setPoint(ImageGrid oGrid, Set2D oSubArea, setIMGPoint o) {
        for (int i = (int) oSubArea.oIntervalY.dLeftBorder; i <= oSubArea.oIntervalY.dRightBorder; i++) {
            for (int j = (int) oSubArea.oIntervalX.dLeftBorder; j <= oSubArea.oIntervalX.dRightBorder; j++) {
                if (oGrid.isInside(new OrderedPair(j, i))) {
                    o.setPoint(oGrid, oGrid.oa[oGrid.getIndex(new OrderedPair(j, i))]);
                }
            }
        }

    }

    public void addPoint(Collection<ImagePoint> lo, int iValue) {
        for (ImagePoint o : lo) {
            if (o != null) {
                addPoint(o, iValue);
            }
        }
    }

    public void setPoint(ImagePoint o, int iValue) {
        if (o.i != null && o.i < this.oa.length) {
            if (this.oa[o.i] != null) {
                this.oa[o.i].iValue = iValue;
            } else {
                this.oa[o.i] = new ImagePoint(o.i, iValue, this);
            }
        }
    }
    
    public void setData(int[] iaData) {
        for(int i = 0; i < Math.min(iaData.length, oa.length); i++){
            oa[i].iValue = iaData[i];
        }
    }

    public void addPoint(ImagePoint o, int iValue) {
        if (o.i != null && o.i < this.oa.length) {
            if (this.oa[o.i] != null) {
                int inewValue = this.oa[o.i].iValue + iValue < 255 ? this.oa[o.i].iValue + iValue : 255;
                inewValue = inewValue < 0 ? 0 : inewValue;
                this.oa[o.i].iValue = inewValue;
            } else {
                this.oa[o.i] = new ImagePoint(o.i, iValue, this);
            }
        }
    }

    public void binarize(int iThreshValue) {
        for (ImagePoint o : this.oa) {
            if (o.iValue < iThreshValue) {
                o.iValue = 0;
            } else {
                o.iValue = 255;
            }
        }
    }

    public void resetMarkers() {
        for (ImagePoint o : oa) {
            o.bMarker = false;
        }
    }

    public ImageGrid getsubY(int iMin, int iMax) {
        int iMinIndex = 0;
        if (iMin > 0 && iMin <= iLength) {
            iMinIndex = this.getIndex(new OrderedPair(0, iMin));
        }else{
            iMin = 0;
        }
        int iMaxIndex = oa.length - 1;
        if (iMax <= iLength && iMax > 0) {
            iMaxIndex = this.getIndex(new OrderedPair(jLength - 1, iMax - 1));
        }else{
            iMax = iLength;
        }
        int[] iaNew = new int[iMaxIndex - iMinIndex + 1];
        for (int i = iMinIndex; i <= iMaxIndex; i++) {
            iaNew[i - iMinIndex] = oa[i].iValue;
        }
        
        return new ImageGrid((iMax - iMin), jLength, iaNew);

    }

    public ImageGrid getsubX(int iMin, int iMax) {
        if (!(iMin >= 0 && iMin <= jLength)) {
            throw new UnsupportedOperationException("Invalid input");
        }
        int jlengthNew = iMax - iMin;
        int[] iaNew = new int[iLength * jlengthNew];
        int iCount = 0;
        for (ImagePoint o : this.oa) {
            if (o.getPos().x < iMax && o.getPos().x >= iMin) {
                iaNew[iCount] = o.iValue;
                iCount++;
            }
        }
        return new ImageGrid(iLength, jlengthNew, iaNew);
    }
    
    public ImagePoint[][] getsubArea(ImagePoint oCenter, int iRadius) {
        OrderedPair opCenter = oCenter.getPos();
        int iCenter = (int) opCenter.y;
        int jCenter = (int) opCenter.x;
        
        int iStart = Math.max(0, iCenter-iRadius);
        int iEnd = Math.min(iLength, iCenter + iRadius);
        int jStart = Math.max(0, jCenter -iRadius);
        int jEnd = Math.min(jLength, jCenter +iRadius);
        
        ImagePoint[][] oReturn = new ImagePoint[iEnd - iStart][jEnd - jStart];
        
        for(int i = iStart ; i < iEnd; i++){
            for(int j = jStart; j < jEnd; j++){
                oReturn[i-iStart][j-jStart] = this.oa[this.getIndex(i, j)];
            }            
        }
        return oReturn;
    }

    public boolean[][] getMarkedPoints() {
        boolean[][] bMarkers = new boolean[this.iLength][this.jLength];
        for (ImagePoint oPoint : oa) {
            OrderedPair opPos = oPoint.getPos();
            bMarkers[(int) opPos.y][(int) opPos.x] = oPoint.bMarker;
        }
        return bMarkers;
    }
    
    public int[][] getMarkedPointsAsInt() {
        int[][] iaMarkers = new int[this.iLength][this.jLength];
        for (ImagePoint oPoint : oa) {
            OrderedPair opPos = oPoint.getPos();
            iaMarkers[(int) opPos.y][(int) opPos.x] = oPoint.bMarker ? 0 : 255;
        }
        return iaMarkers;
    }

    public ImagePoint containsValue(int iValue) {
        for (ImagePoint o : oa) {
            if (o.iValue == iValue && !o.bMarker) {
                return o;
            }
        }
        return null;
    }

    public boolean isInside(OrderedPair op) {
        return op.x >= 0 && op.x < jLength && op.y >= 0 && op.y < iLength;
    }

    public BufferedImage getBuffImage() {
        int iWidth = jLength;
        int iHeight = iLength;

        byte[] bapixels = castToByteprimitive(oa);

        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = oFrameImage.getRaster();

        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        return oFrameImage;
    }

    @Override
    public ImageGrid clone() {
        return new ImageGrid(this.getMatrix());
    }
//    
//    public void clone(ImageGrid oGrid) {
//        oGrid.setData(this.getData());     
//    }

    public interface setIMGPoint {
        public void setPoint(ImageGrid oGrid, ImagePoint op);
    }

}
