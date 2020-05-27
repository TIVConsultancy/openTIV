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

import com.tivconsultancy.opentiv.physics.interfaces.Velocity;
import com.tivconsultancy.opentiv.velocimetry.primitives.TrackAblePair;


/**
 *
 * @author Thomas Ziegenhein
 */
public class TwoTrackablePairCriteria {
    
    public static Double BernahrdAndThompson(TrackAblePair oPair1, TrackAblePair oPair2){
        return (oPair1.vDisplacement.sub(oPair2.vDisplacement).opUnitTangent.dValue);        
    }
    
    public static Double Z0(TrackAblePair oPair1, TrackAblePair oPair2, Velocity oGuess, Double C1, Double C2){
        
        Double dRefLength = oGuess.getSize(); // Vector.modulus(oGuess.getVelocityX(), oGuess.getVelocityY());
        
         Double dRefAngle = oGuess.getVec().cosAngle(oPair1.vDisplacement);                
        
        Double dLength1 = oPair1.vDisplacement.getLength();
        Double dLength2 = oPair2.vDisplacement.getLength();
        
        Double dAngle1And2 = oPair1.vDisplacement.cosAngle(oPair2.vDisplacement);
        
        Double dCartesianDistance = Math.sqrt(dLength1*dLength1+dLength2*dLength2 - 2*(dLength1*dLength2*dAngle1And2));
        
        Double dRefCriteria = Math.sqrt((dLength1-dRefLength)*(dLength1-dRefLength) + (dLength2-dRefLength)*(dLength2-dRefLength))*((1+dRefAngle)/2.0)*((1+dRefAngle)/2.0);
        
        Double dWeightCartesianDistanceRef = Math.sqrt(dRefLength*dRefLength+dRefLength*dRefLength - 2*(dRefLength*dRefLength*dAngle1And2));
        
        Double dCriteria = (C1 * dCartesianDistance + C2 * dRefCriteria)-(dWeightCartesianDistanceRef *((1+dRefAngle)/2.0)*((1+dRefAngle)/2.0));
        
        return dCriteria;
    }
    
    public static Double Z1(TrackAblePair oPair1, TrackAblePair oPair2, Velocity oGuess, Double C1, Double C2){
        
        Double dRefLength = oGuess.getSize();
        Double dRefAngle = oGuess.getVec().cosAngle(oPair1.vDisplacement);
        
        Double dLength1 = oPair1.vDisplacement.getLength();
        Double dLength2 = oPair2.vDisplacement.getLength();
        
        Double dAngle1And2 = oPair1.vDisplacement.cosAngle(oPair2.vDisplacement);
        
        Double dCartesianDistance = Math.sqrt(dLength1*dLength1+dLength2*dLength2 - 2*(dLength1*dLength2*dAngle1And2));
        
        Double dRefCriteria = Math.sqrt((dLength1-dRefLength)*(dLength1-dRefLength) + (dLength2-dRefLength)*(dLength2-dRefLength));
        
        Double dWeightCartesianDistanceRef = Math.sqrt(dRefLength*dRefLength+dRefLength*dRefLength - 2*(dRefLength*dRefLength*dAngle1And2));
        
        Double dCriteria = (C1 * dCartesianDistance + C2 * dRefCriteria);
        
        return dCriteria;
    }
    
    public static Double Z2(TrackAblePair oPair1, TrackAblePair oPair2, Velocity oGuess, Double C1, Double C2){
        
        Double d1 = (oPair1.vDisplacement.sub(oPair2.vDisplacement)).getLength();
        Double d2 = (oPair1.vDisplacement.sub(oGuess.getVec())).getLength();
        Double d3 = (oPair2.vDisplacement.sub(oGuess.getVec())).getLength();
        
//        return C1*d1+(C2/2.0*d2)+(C2/2.0*d3);
        return d2;
        
    }
    
    
}
