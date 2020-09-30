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
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class ImageBoolean implements Serializable{

    private static final long serialVersionUID = -6968251207131438590L;

    public boolean[][] baMarker;
    public String sIdent;
    
    public ImageBoolean() {
        baMarker = new boolean[1][1];
    }
    
    public ImageBoolean(int iLength, int jLength) {
        baMarker = new boolean[iLength][jLength];
    }
    
    public ImageBoolean(boolean[][] bba) {
        baMarker = bba;
    }
    
    public void markPoints(List<MatrixEntry> lme, boolean b){
        for(MatrixEntry me : lme){
            baMarker[me.i][me.j] = b;
        }
    }
    
    public static ImageBoolean valueOf(ImageInt img) {
        ImageBoolean imgReturn = new ImageBoolean(img.baMarker.length, img.baMarker[0].length);
        for (int i = 0; i < img.baMarker.length; i++) {
            for (int j = 0; j < img.baMarker[0].length; j++) {
                imgReturn.baMarker[i][j] = img.iaPixels[i][j] > 127;
            }
        }
        return imgReturn;
    }        
    
}
