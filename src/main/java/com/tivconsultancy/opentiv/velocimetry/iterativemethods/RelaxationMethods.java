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

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.primitives.Vector;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.physics.interfaces.Velocity;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import com.tivconsultancy.opentiv.velocimetry.guess.MinDist;
import com.tivconsultancy.opentiv.velocimetry.primitives.BasicTrackable;
import com.tivconsultancy.opentiv.velocimetry.primitives.TrackAblePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class RelaxationMethods {

    public static Trackable BarnardandThompson(Trackable oVReference, List<? extends Trackable> loVeloFrame1, List<? extends Trackable> loVeloFrame2, double dRs, double dRc) {
        double A = 0.3;
        double B = 3.0;
        // generate the possible Velocity pair matrix
        List<List<TrackAblePair>> lloVelocityPairMatrix = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < dRs) {
                List<TrackAblePair> loVelocityPairs = new ArrayList<>();
                for (Trackable oV2 : loVeloFrame2) {
                    if (oV1.getPosition().getNorm(oV2.getPosition()) < dRs) {
                        if (oV1 != null && oV2 != null) {
                            loVelocityPairs.add(new TrackAblePair(oV1, oV2));
                        }
                    }
                }
                //it is important to set the lost velocity at the last position
                loVelocityPairs.add(new LostVelocity(oV1));
                lloVelocityPairMatrix.add(loVelocityPairs);
            }
        }

        // Initialize
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                ovp.initializeCoeffBernahrdAndThompson(lovp.size() - 1);
            }
        }

        //iterating
        for (int i = 0; i < 20; i++) {
            //update Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsBarnardAndThompson(lloVelocityPairMatrix, dRs, dRc), A, B);
                }
            }

            //normalize Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.normalizeCoeffBarnarddAndThompson(lovp, (LostVelocity) lovp.get(lovp.size() - 1));
                }
            }

            //Reset memory
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.resetCoeff();
                }
            }
        }

        List<TrackAblePair> loReferencePairs = new ArrayList<>();
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                if (oVReference.equals(ovp.oVFrame1)) {
                    loReferencePairs.add(ovp);
                }
            }
        }

        Collections.sort(loReferencePairs, new SortTrackablePair());
        Trackable oBestMatch = null;

        for (int i = loReferencePairs.size() - 1; i >= 0; i--) {
            if (loReferencePairs.get(i).oVFrame2 == null) {
                break;
            }
            if (!loReferencePairs.get(i).oVFrame2.isTracked()&& loReferencePairs.get(i).dCoeffNew>0.5) {
                oBestMatch = loReferencePairs.get(i).oVFrame2;
                break;
            }
        }

        if (oBestMatch != null) {
            oBestMatch.setTracked(true);
        }

        return oBestMatch;
//        return loReferencePairs.get(loReferencePairs.size()-1).oVFrame2;

    }
    
    public static Trackable BarnardandThompsonAdapted(Trackable oVReference, List<? extends Trackable> loVeloFrame1, List<? extends Trackable> loVeloFrame2, double dRs, double dRc, double dMaxVelo) {
        double A = 0.3;
        double B = 3.0;
        // generate the possible Velocity pair matrix
        List<List<TrackAblePair>> lloVelocityPairMatrix = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < dRs) {
                List<TrackAblePair> loVelocityPairs = new ArrayList<>();
                for (Trackable oV2 : loVeloFrame2) {
                    if (oV1.getPosition().getNorm(oV2.getPosition()) < dRs) {
                        if (oV1 != null && oV2 != null) {
                            loVelocityPairs.add(new TrackAblePair(oV1, oV2));
                        }
                    }
                }
                //it is important to set the lost velocity at the last position
                loVelocityPairs.add(new LostVelocity(oV1));
                lloVelocityPairMatrix.add(loVelocityPairs);
            }
        }

        // Initialize
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                ovp.initializeCoeffBernahrdAndThompson(lovp.size() - 1);
            }
        }

        //iterating
        for (int i = 0; i < 20; i++) {
            //update Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsBarnardAndThompsonAdjusted(lloVelocityPairMatrix, dRs, dRc, dMaxVelo), A, B);
                }
            }

            //normalize Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.normalizeCoeffBarnarddAndThompson(lovp, (LostVelocity) lovp.get(lovp.size() - 1));
                }
            }

            //Reset memory
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.resetCoeff();
                }
            }
        }

        List<TrackAblePair> loReferencePairs = new ArrayList<>();
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                if (oVReference.equals(ovp.oVFrame1)) {
                    loReferencePairs.add(ovp);
                }
            }
        }

        Collections.sort(loReferencePairs, new SortTrackablePair());
        Trackable oBestMatch = null;

        for (int i = loReferencePairs.size() - 1; i >= 0; i--) {
            if (loReferencePairs.get(i).oVFrame2 == null) {
                break;
            }
            if (!loReferencePairs.get(i).oVFrame2.isTracked()&& loReferencePairs.get(i).dCoeffNew>0.5) {
                oBestMatch = loReferencePairs.get(i).oVFrame2;
                break;
            }
        }

        if (oBestMatch != null) {
            oBestMatch.setTracked(true);
        }

        return oBestMatch;
//        return loReferencePairs.get(loReferencePairs.size()-1).oVFrame2;

    }

    public static Trackable BarnardandThompsonAdaptedWithInternAndExternGuess(Trackable oVReference, List<? extends Trackable> loVeloFrame1, List<? extends Trackable> loVeloFrame2, double dRsSearchRadius, double dRc, double dStepSize, Velocity oGuess) {
        //Guess
        List<Trackable> loPosInDoubleRadiusFrame1 = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < 2*dRsSearchRadius) {
                loPosInDoubleRadiusFrame1.add(oV1);
            }
        }

        List<Trackable> loPosInDoubleRadiusFrame2 = new ArrayList<>();

        for (Trackable oV2 : loVeloFrame2) {
            if (oVReference.getPosition().getNorm(oV2.getPosition()) < 2*dRsSearchRadius) {
                loPosInDoubleRadiusFrame2.add(oV2);
            }
        }
        
        
        Velocity oMinimizeDistance = MinDist.tryinStepsVariableSet(loPosInDoubleRadiusFrame1, loPosInDoubleRadiusFrame2, dStepSize, dRsSearchRadius, dRsSearchRadius, oVReference.getPosition());
        if(oMinimizeDistance.getVec().getLength()<dRsSearchRadius){
            if( ((Vector) oMinimizeDistance.getVec().sub(oGuess.getVec())).getLength() < dRc){
             return (loVeloFrame2.get((int) BasicTrackable.getNearestInList(loVeloFrame2, new OrderedPair(oVReference.getPosition().x+oMinimizeDistance.getVelocityX(), oVReference.getPosition().y+oMinimizeDistance.getVelocityY())).x));   
            }            
        }        
        
        List<Trackable> loPosInRadiusFrame1 = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < dRsSearchRadius) {
                loPosInRadiusFrame1.add(oV1);
            }
        }

        List<Trackable> loPosInRadiusFrame2 = new ArrayList<>();

        for (Trackable oV2 : loVeloFrame2) {
            if (oVReference.getPosition().getNorm(oV2.getPosition()) < dRsSearchRadius) {
                loPosInRadiusFrame2.add(oV2);
            }
        }


        Double dRs = dRsSearchRadius; //(2 * Vector.modulus(oGuess.getVelocityX(), oGuess.getVelocityY())) < 0.1 * dRsSearchRadius ? dRsSearchRadius : (2 * Vector.modulus(oGuess.getVelocityX(), oGuess.getVelocityY()));

        double A = 0.3;
        double B = 3.0;

        Double C1 = 1.0;
        Double C2 = 1.0;
        // generate the possible Velocity pair matrix
        List<List<TrackAblePair>> lloVelocityPairMatrix = new ArrayList<>();
        for (Trackable oV1 : dRs < dRsSearchRadius ? loPosInRadiusFrame1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < dRs) {
                List<TrackAblePair> loVelocityPairs = new ArrayList<>();
                for (Trackable oV2 : dRs < dRsSearchRadius ? loPosInRadiusFrame2 : loVeloFrame2) {
                    if (oV1 != null && oV2 != null) {
                        if (oV1.getPosition().getNorm(oV2.getPosition()) < dRs) {
                            loVelocityPairs.add(new TrackAblePair(oV1, oV2));
                        }
                    }
                }
                //it is important to set the lost velocity at the last position
                loVelocityPairs.add(new LostVelocity(oV1));
                lloVelocityPairMatrix.add(loVelocityPairs);
            }
        }

        // Initialize
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                ovp.initializeCoeffBernahrdAndThompson(lovp.size() - 1);
            }
        }

        //iterating
        for (int i = 0; i < 20; i++) {
            //update Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    if (oGuess.getVelocityX() == 0 && oGuess.getVelocityY() == 0) {
                        ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsBarnardAndThompson(lloVelocityPairMatrix, dRs, dRc), A, B);
                    } else {
                        ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsZ(lloVelocityPairMatrix, dRs, dRc, oGuess, C1, C2), A, B);
                    }

                }
            }

            //normalize Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.normalizeCoeffBarnarddAndThompson(lovp, (LostVelocity) lovp.get(lovp.size() - 1));
                }
            }

            //Reset memory
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.resetCoeff();
                }
            }
        }

        List<TrackAblePair> loReferencePairs = new ArrayList<>();
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                if (oVReference.equals(ovp.oVFrame1)) {
                    loReferencePairs.add(ovp);
                }
            }
        }

        Collections.sort(loReferencePairs, new SortTrackablePair());
        Trackable oBestMatch = null;

        for (int i = loReferencePairs.size() - 1; i >= 0; i--) {
            if (loReferencePairs.get(i).oVFrame2 == null) {
                break;
            }
            if (!loReferencePairs.get(i).oVFrame2.isTracked() && loReferencePairs.get(i).dCoeffNew>0.5) {
                oBestMatch = loReferencePairs.get(i).oVFrame2;
                break;
            }
        }

        if (oBestMatch != null) {
            oBestMatch.setTracked(true);
        }

        return oBestMatch;
//        return loReferencePairs.get(loReferencePairs.size()-1).oVFrame2;

    }
    
    public static Trackable BarnardandThompsonAdaptedWithGuess(Trackable oVReference, List<? extends Trackable> loVeloFrame1, List<? extends Trackable> loVeloFrame2, double dRsSearchRadius, double dRc, double dStepSize) {
        //Guess
        List<Trackable> loPosInDoubleRadiusFrame1 = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < 2*dRsSearchRadius) {
                loPosInDoubleRadiusFrame1.add(oV1);
            }
        }

        List<Trackable> loPosInDoubleRadiusFrame2 = new ArrayList<>();

        for (Trackable oV2 : loVeloFrame2) {
            if (oVReference.getPosition().getNorm(oV2.getPosition()) < 2*dRsSearchRadius) {
                loPosInDoubleRadiusFrame2.add(oV2);
            }
        }
        
        
        Velocity oGuess = MinDist.tryinStepsVariableSet(loPosInDoubleRadiusFrame1, loPosInDoubleRadiusFrame2, dStepSize, dRsSearchRadius, dRsSearchRadius, oVReference.getPosition());
        if(new OrderedPair(oGuess.getVelocityX(), oGuess.getVelocityY()).getNorm(null)<dRsSearchRadius){
            return (loVeloFrame2.get((int) BasicTrackable.getNearestInList(loVeloFrame2, new OrderedPair(oVReference.getPosition().x+oGuess.getVelocityX(), oVReference.getPosition().y+oGuess.getVelocityY())).x));
        }        
        System.out.println(oGuess);
        
        List<Trackable> loPosInRadiusFrame1 = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < dRsSearchRadius) {
                loPosInRadiusFrame1.add(oV1);
            }
        }

        List<Trackable> loPosInRadiusFrame2 = new ArrayList<>();

        for (Trackable oV2 : loVeloFrame2) {
            if (oVReference.getPosition().getNorm(oV2.getPosition()) < dRsSearchRadius) {
                loPosInRadiusFrame2.add(oV2);
            }
        }


        Double dRs = dRsSearchRadius; //(2 * Vector.modulus(oGuess.getVelocityX(), oGuess.getVelocityY())) < 0.1 * dRsSearchRadius ? dRsSearchRadius : (2 * Vector.modulus(oGuess.getVelocityX(), oGuess.getVelocityY()));

        double A = 0.3;
        double B = 3.0;

        Double C1 = 1.0;
        Double C2 = 1.0;
        // generate the possible Velocity pair matrix
        List<List<TrackAblePair>> lloVelocityPairMatrix = new ArrayList<>();
        for (Trackable oV1 : dRs < dRsSearchRadius ? loPosInRadiusFrame1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < dRs) {
                List<TrackAblePair> loVelocityPairs = new ArrayList<>();
                for (Trackable oV2 : dRs < dRsSearchRadius ? loPosInRadiusFrame2 : loVeloFrame2) {
                    if (oV1 != null && oV2 != null) {
                        if (oV1.getPosition().getNorm(oV2.getPosition()) < dRs) {
                            loVelocityPairs.add(new TrackAblePair(oV1, oV2));
                        }
                    }
                }
                //it is important to set the lost velocity at the last position
                loVelocityPairs.add(new LostVelocity(oV1));
                lloVelocityPairMatrix.add(loVelocityPairs);
            }
        }

        // Initialize
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                ovp.initializeCoeffBernahrdAndThompson(lovp.size() - 1);
            }
        }

        //iterating
        for (int i = 0; i < 20; i++) {
            //update Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    if (oGuess.getVelocityX() == 0 && oGuess.getVelocityY() == 0) {
                        ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsBarnardAndThompson(lloVelocityPairMatrix, dRs, dRc), A, B);
                    } else {
                        ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsZ(lloVelocityPairMatrix, dRs, dRc, oGuess, C1, C2), A, B);
                    }

                }
            }

            //normalize Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.normalizeCoeffBarnarddAndThompson(lovp, (LostVelocity) lovp.get(lovp.size() - 1));
                }
            }

            //Reset memory
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.resetCoeff();
                }
            }
        }

        List<TrackAblePair> loReferencePairs = new ArrayList<>();
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                if (oVReference.equals(ovp.oVFrame1)) {
                    loReferencePairs.add(ovp);
                }
            }
        }

        Collections.sort(loReferencePairs, new SortTrackablePair());
        Trackable oBestMatch = null;

        for (int i = loReferencePairs.size() - 1; i >= 0; i--) {
            if (loReferencePairs.get(i).oVFrame2 == null) {
                break;
            }
            if (!loReferencePairs.get(i).oVFrame2.isTracked() && loReferencePairs.get(i).dCoeffNew>0.5) {
                oBestMatch = loReferencePairs.get(i).oVFrame2;
                break;
            }
        }

        if (oBestMatch != null) {
            oBestMatch.setTracked(true);
        }

        return oBestMatch;
//        return loReferencePairs.get(loReferencePairs.size()-1).oVFrame2;

    }

    public static Trackable BarnardandThompsonAdaptedWithGuess(Trackable oVReference, List<? extends Trackable> loVeloFrame1, List<? extends Trackable> loVeloFrame2, double dRsSearchRadius, double dRc, double dStepSize, Velocity oGuess) {
        //Guess
//        Double dRs = (2 * Vector.modulus(oGuess.getVelocityX(), oGuess.getVelocityY())) < 0.1 * dRsSearchRadius ? dRsSearchRadius : (2 * Vector.modulus(oGuess.getVelocityX(), oGuess.getVelocityY()));
        Double dRs = dRsSearchRadius;
        double A = 0.3;
        double B = 3.0;

        Double C1 = 1.0;
        Double C2 = 0.25;
        // generate the possible Velocity pair matrix
        List<List<TrackAblePair>> lloVelocityPairMatrix = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < dRs) {
                List<TrackAblePair> loVelocityPairs = new ArrayList<>();
                for (Trackable oV2 : loVeloFrame2) {
                    if (oV1 != null && oV2 != null) {
                        if (oV1.getPosition().getNorm(oV2.getPosition()) < dRs) {
                            loVelocityPairs.add(new TrackAblePair(oV1, oV2));
                        }
                    }
                }
                //it is important to set the lost velocity at the last position
                loVelocityPairs.add(new LostVelocity(oV1));
                lloVelocityPairMatrix.add(loVelocityPairs);
            }
        }

        // Initialize
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                ovp.initializeCoeffBernahrdAndThompson(lovp.size() - 1);
            }
        }

        //iterating
        for (int i = 0; i < 20; i++) {
            //update Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    if (oGuess.getVelocityX() == 0 && oGuess.getVelocityY() == 0) {
                        ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsBarnardAndThompson(lloVelocityPairMatrix, dRs, dRc), A, B);
                    } else {
                        ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsZ(lloVelocityPairMatrix, dRs, dRc, oGuess, C1, C2), A, B);
//                        ovp.updateCoeffBernahrdAndThompson(ovp.getPossiblePairsBarnardAndThompsonAdjusted(lloVelocityPairMatrix, dRs, dRc), A, B);
                    }

                }
            }

            //normalize Coefficients
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.normalizeCoeffBarnarddAndThompson(lovp, (LostVelocity) lovp.get(lovp.size() - 1));
                }
            }

            //Reset memory
            for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
                for (TrackAblePair ovp : lovp) {
                    ovp.resetCoeff();
                }
            }
        }

        List<TrackAblePair> loReferencePairs = new ArrayList<>();
        for (List<TrackAblePair> lovp : lloVelocityPairMatrix) {
            for (TrackAblePair ovp : lovp) {
                if (oVReference.equals(ovp.oVFrame1)) {
                    loReferencePairs.add(ovp);
                }
            }
        }

        Collections.sort(loReferencePairs, new SortTrackablePair());
        Trackable oBestMatch = null;

        int i;
        for (i = loReferencePairs.size() - 1; i >= 0; i--) {
            if (loReferencePairs.get(i).oVFrame2 == null) {
                break;
            }

            if (!loReferencePairs.get(i).oVFrame2.isTracked()) {
                oBestMatch = loReferencePairs.get(i).oVFrame2;
                break;
            }else{
                break;
            }

        }

        if (oBestMatch != null) {
            oBestMatch.setTracked(true);
        }

        return oBestMatch;
//        return loReferencePairs.get(loReferencePairs.size()-1).oVFrame2;

    }

    public static Velocity Guess(Trackable oVReference, List<? extends Trackable> loVeloFrame1, List<? extends Trackable> loVeloFrame2, double dRsSearchRadius, double dRc, double dStepSize) {
        //Guess
        List<Trackable> loPosInDoubleRadiusFrame1 = new ArrayList<>();
        for (Trackable oV1 : loVeloFrame1) {
            if (oVReference.getPosition().getNorm(oV1.getPosition()) < 2*dRsSearchRadius) {
                loPosInDoubleRadiusFrame1.add(oV1);
            }
        }

        List<Trackable> loPosInDoubleRadiusFrame2 = new ArrayList<>();

        for (Trackable oV2 : loVeloFrame2) {
            if (oVReference.getPosition().getNorm(oV2.getPosition()) < 2*dRsSearchRadius) {
                loPosInDoubleRadiusFrame2.add(oV2);
            }
        }
        
        
        Velocity oGuess = MinDist.tryinStepsVariableSet(loPosInDoubleRadiusFrame1, loPosInDoubleRadiusFrame2, dStepSize, dRsSearchRadius, dRsSearchRadius, oVReference.getPosition());
        System.out.println(oGuess);

        if((new OrderedPair(oGuess.getVelocityX(), oGuess.getVelocityY())).getNorm(null)<dRsSearchRadius){
            return new VelocityVec(oGuess.getVec());
        }else{
            return null;
        }
        

    }

}
