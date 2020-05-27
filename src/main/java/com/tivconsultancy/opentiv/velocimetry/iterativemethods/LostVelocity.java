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

import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.velocimetry.primitives.TrackAblePair;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class LostVelocity extends TrackAblePair implements Serializable{

    private static final long serialVersionUID = -3243367477422368369L;

    public LostVelocity(Trackable oVFrame1, Trackable oVFrame2) {
        super(oVFrame1, oVFrame2);
    }
    
    public LostVelocity(Trackable oVFrame1) {
        super(oVFrame1, null);
    }
    
    @Override
    public void updateCoeffBernahrdAndThompson(List<TrackAblePair> loLikelyPairs, double A, double B){
            
    }
    
    @Override
    public void normalizeCoeffBarnarddAndThompson(List<TrackAblePair> loAllPairs, LostVelocity oLostVelo) {
        Double dSummation = 0.0;
        for(TrackAblePair ovp : loAllPairs){
            if(ovp.oVFrame1.equals(super.oVFrame1)){
                dSummation = dSummation + ovp.dCoeffNew;
            }
        }        
        this.dCoeffNew = 1.0-dSummation;            
    }
    
}
