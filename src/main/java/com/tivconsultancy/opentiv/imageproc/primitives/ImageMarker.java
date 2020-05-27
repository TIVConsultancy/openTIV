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

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class ImageMarker implements Serializable{

    private static final long serialVersionUID = 4076072844216720580L;
    public boolean[][] ba;
    
    public ImageMarker(boolean[][] ba){
        this.ba = ba;
    }
    
    public boolean get(MatrixEntry me){
        return ba[me.i][me.j];
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
        if (j >= ba[i].length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i, j + 1));
        }

        if (j >= ba[i].length - 1 || i <= 0) {
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

        if (j <= 0 || i >= ba.length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i + 1, j - 1));
        }

        if (i >= ba.length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i + 1, j));
        }

        if (i >= ba.length - 1 || j >= ba[i].length - 1) {
            lo.add(null);
        } else {
            lo.add(new MatrixEntry(i + 1, j + 1));
        }

        return lo;

    }
    
}
