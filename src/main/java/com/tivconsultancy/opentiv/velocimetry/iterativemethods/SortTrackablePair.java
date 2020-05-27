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
package com.tivconsultancy.opentiv.velocimetry.iterativemethods;

import com.tivconsultancy.opentiv.velocimetry.primitives.TrackAblePair;
import java.util.Comparator;

/**
 *
 * @author Thomas Ziegenhein
 */
public class SortTrackablePair implements Comparator<TrackAblePair> {

    @Override
    public int compare(TrackAblePair o1, TrackAblePair o2) {
        
        if(o1.dCoeffNew < o2.dCoeffNew){
            return -1;
        }
        
        if(o1.dCoeffNew > o2.dCoeffNew){
            return 1;
        }
        
        return 0;
    }
    
}
