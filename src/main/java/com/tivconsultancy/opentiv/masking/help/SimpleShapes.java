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
package com.tivconsultancy.opentiv.masking.help;

import com.tivconsultancy.opentiv.helpfunctions.hpc.Stopwatch;
import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class SimpleShapes implements Serializable{

    private static final long serialVersionUID = 3530343965411910131L;

    public int iPosX = 0;
    public int iPosY = 0;
    public double dNorm;
    public double dNormPot;

    public SimpleShapes(int iPosX, int iPosY, double dNormMax, double dNormPot) {
        this.iPosX = iPosX;
        this.iPosY = iPosY;
        this.dNorm = dNormMax;
        this.dNormPot = dNormPot;
    }

//    public void setOnGrid(ImageGrid oInput) {        
//        ImagePoint Center = new ImagePoint(iPosX, iPosY, 255, oInput);
//        OrderedPair opCenter = Center.getPos();
////        ImageGrid oReturn = new ImageGrid(oInput.getMatrix());        
//        for (int i = 0; i < oInput.oa.length; i++) {
//            ImagePoint o = oInput.oa[i];
//            OrderedPair op = o.getPos();
//            if ((op.x - opCenter.x) < dNorm && (op.y - opCenter.y) < dNorm) {
//                if ((o.getNorm2(Center, String.valueOf(dNormPot))) < dNorm) {
//                    oInput.oa[i].bMarker = true;
//                    oInput.oa[i].iValue = 0;
//                }
//            }
//        }      
    public void setOnGrid(ImageInt oInput) {
        MatrixEntry Center = new MatrixEntry(iPosY, iPosX);
        Stopwatch.addTimmer("getArea");
        List<MatrixEntry> oArea = oInput.getsubArea(iPosY, iPosX, (int) dNorm);
        Stopwatch.stop("getArea");

        if (dNormPot >= 10) {
            for (MatrixEntry me : oArea) {
                oInput.iaPixels[me.i][me.j] = 0;
                oInput.baMarker[me.i][me.j] = true;
            }
        } else {
            for (MatrixEntry me : oArea) {
                if ((me.getNorm2(Center, String.valueOf(dNormPot))) < dNorm) {
                    oInput.iaPixels[me.i][me.j] = 0;
                    oInput.baMarker[me.i][me.j] = true;
                }

            }
        }
    }

}
