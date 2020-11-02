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
package com.tivconsultancy.opentiv.imageproc.algorithms.areaextraction;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Morphology;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.shapes.ArbStructure2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class PreMarked {
    public static List<ArbStructure2> getAreasBlackOnWhite(ImageInt Input, boolean useN4){        
        Input.resetMarkers();
        List<ArbStructure2> lsStructures = new ArrayList<>();
        MatrixEntry me_Fill = getNextBlackPoint(Input, new MatrixEntry(0, 0));
        while(me_Fill != null){
            ArbStructure2 Structure;
            if(useN4){
                Structure = new ArbStructure2((new Morphology()).markFillN4(Input, me_Fill.i, me_Fill.j));
            }else{
                Structure = new ArbStructure2((new Morphology()).markFillN8(Input, me_Fill.i, me_Fill.j));
            }
            
            if(Structure != null){
                if(!Structure.loPoints.isEmpty()){
                    lsStructures.add(Structure);
                }                
            }
            me_Fill = getNextBlackPoint(Input, me_Fill);
        }
        return lsStructures;
    }
    
    public static List<ArbStructure2> getAreasBlackOnWhite(ImageInt Input){        
        return getAreasBlackOnWhite(Input, true);
    }
    
    public static MatrixEntry getNextBlackPoint(ImageInt Input, MatrixEntry meStart){
        for(int i = 0 ; i < Input.iaPixels.length; i++){
            for(int j = 0 ; j < Input.iaPixels[i].length; j++){
                if(Input.iaPixels[i][j] == 0 && !Input.baMarker[i][j]){
                    return new MatrixEntry(i, j);
                }
            }
        }
        return null;
    }
        
}
