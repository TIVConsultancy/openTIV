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

import com.tivconsultancy.opentiv.math.interfaces.*;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ImagePoint implements Serializable, Additionable<ImagePoint>, Substractable<ImagePoint>, Normable<ImagePoint>, Multipliable<ImagePoint> {

    private static final long serialVersionUID = 1752513465508047826L;

    public Integer i;
    public Integer iValue;
    public boolean bMarker = false;

    protected ImageGrid oGrid;

    public ImagePoint(Integer i, Integer iValue, ImageGrid oGrid) {
        this.i = i;
        this.iValue = iValue;
        this.oGrid = oGrid;
    }

    public ImagePoint(int x, int y, Integer iValue, ImageGrid oGrid) {        
        this.i = oGrid.getIndex(new OrderedPair(x, y));
        this.iValue = iValue;
        this.oGrid = oGrid;
    }

    public ImageGrid getGrid() {
        return this.oGrid;
    }

    public OrderedPair getPos() {
        return this.getGrid().getPos(this);
    }

    public double getConvolution(int[][] iaConvolution) {
        
        OrderedPair pos = getPos();
        int i = (int) pos.y;
        int j = (int) pos.x;
        
        int iMDown = (int) (i - Math.floor(((double) iaConvolution.length - 1) / 2.0));

        int iMUp = (int) (i + Math.round(((double) iaConvolution.length - 1) / 2.0));

                        // System.out.println("UpdDown");
        // System.out.println(iMDown + "|" + i + "|" + iMUp);

        /*if ((iMUp - iMDown) < (iaConvolution.length-1)){
         System.out.println("boing");
         iMUp = iMUp +1;
         }*/

        /*if (iMDown < 0) {
         iMDown = 0;
         }

         if (iMUp > iaInput.length) {
         iMUp = iaInput.length;
         }*/
        int jMLeft = (int) (j - Math.floor(((double) iaConvolution[0].length - 1) / 2.0));

        int jMRight = (int) (j + Math.round(((double) iaConvolution[0].length - 1) / 2.0));

                        //System.out.println(jMLeft + "|" + j + "|" + jMRight);

        /*if (jMLeft < 0) {
         jMLeft = 0;
         }

         if (jMRight > iaInput[0].length) {
         jMRight = iaInput[0].length;
         }*/
        double dTemp = 0;

        int iIrunner = 0;

        int iMClean = 0;

        for (int iM = iMDown; iM <= iMUp; iM++) {

            if (iM < 0) {
                iMClean = this.oGrid.iLength + iM;
            } else if (iM >= this.oGrid.iLength) {
                iMClean = (iM - this.oGrid.iLength);
            } else {
                iMClean = iM;
            }

            int iJRunner = 0;

            int jMClean = 0;

            for (int jM = jMLeft; jM <= jMRight; jM++) {

                if (jM < 0) {
                    jMClean = this.oGrid.jLength + jM;
                } else if (jM >= this.oGrid.jLength) {
                    jMClean = (jM - this.oGrid.jLength);
                } else {
                    jMClean = jM;
                }

                //System.out.println(daConvolution[iIrunner][iJRunner]);
                dTemp = dTemp + this.oGrid.oa[this.oGrid.getIndex(iMClean, jMClean)].iValue * iaConvolution[iIrunner][iJRunner];

                iJRunner = iJRunner + 1;

            }

            iIrunner = iIrunner + 1;

        }
        
        return dTemp;
        
    }

    @Override
    public ImagePoint add(ImagePoint o2) {
        return (new ImagePoint(this.i, this.iValue + o2.iValue, this.oGrid));
    }

    @Override
    public ImagePoint add2(ImagePoint o2, String sType) {
        throw new UnsupportedOperationException("Type of addition not supported");
    }

    @Override
    public ImagePoint sub(ImagePoint o2) {
        return (new ImagePoint(this.i, this.iValue - o2.iValue, this.oGrid));
    }

    @Override
    public ImagePoint sub2(ImagePoint o2, String sType) {
        throw new UnsupportedOperationException("Type of addition not supported");
    }

    @Override
    public Double getNorm(ImagePoint o2) {
        return this.oGrid.getPos(this).getNorm(o2.oGrid.getPos(o2));
    }

    @Override
    public Double getNorm2(ImagePoint o2, String sNormType) {
        return this.oGrid.getPos(this).getNorm2(o2.oGrid.getPos(o2), sNormType);
    }

    @Override
    public ImagePoint mult(Double d) {
        return new ImagePoint(this.i, (int) (this.iValue * d), this.oGrid);
    }

    @Override
    public ImagePoint mult2(Double d, String sType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return ("Position : " + this.getGrid().getPos(this) + " Value: " + this.iValue);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ImagePoint)) {
            return false;
        }
        ImagePoint op = (ImagePoint) o;
        if (Objects.equals(op.i, this.i)) {
            return (true);
        } else {
            return (false);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.i);
        hash = 67 * hash + Objects.hashCode(this.iValue);
        return hash;
    }

}
