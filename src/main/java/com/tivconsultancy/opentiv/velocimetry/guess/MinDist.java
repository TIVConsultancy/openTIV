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
package com.tivconsultancy.opentiv.velocimetry.guess;

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import com.tivconsultancy.opentiv.velocimetry.primitives.BasicTrackable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class MinDist {

    public static VelocityVec tryinStepsFixedSet(List<? extends Trackable> loFirstFrame, List<? extends Trackable> loSecondFrame, double dStepSize, double dDisplacement) {

        List<OrderedPair> lopDisplacementAndSum = new ArrayList<>();
        int iDisplacementSteps = (int) ((dDisplacement / dStepSize) / 2.0);

        for (int i = -1 * iDisplacementSteps; i <= iDisplacementSteps; i++) {
            for (int j = -1 * iDisplacementSteps; j <= iDisplacementSteps; j++) {
                Double dSumOfDistances = 0.0;
                for (Trackable ot1 : loFirstFrame) {
                    for (Trackable ot2 : loSecondFrame) {
                        dSumOfDistances = dSumOfDistances + (new OrderedPair(ot1.getPosition().x - (ot2.getPosition().x - (i * dStepSize)), ot1.getPosition().y - (ot2.getPosition().y - (j * dStepSize)))).getNorm(null);
                    }
                }
                lopDisplacementAndSum.add(new OrderedPair(i * dStepSize, j * dStepSize, dSumOfDistances));
            }
        }

        Collections.sort(lopDisplacementAndSum, new Comparator<OrderedPair>() {

            @Override
            public int compare(OrderedPair o1, OrderedPair o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }

                if (o1 == null && o2 != null) {
                    return 1;
                }

                if (o1 != null && o2 == null) {
                    return -1;
                }

                if (o1.dValue < o2.dValue) {
                    return -1;
                }

                if (o1.dValue > o2.dValue) {
                    return 1;
                }

                return 0;
            }
        });

        return new VelocityVec(lopDisplacementAndSum.get(0).x, lopDisplacementAndSum.get(0).y, null);

    }

    public static VelocityVec tryinStepsVariableSet(List<? extends Trackable> loFirstFrame, List<? extends Trackable> loSecondFrame, double dStepSize, double dDisplacement, double dRadius, OrderedPair opCenter) {

        List<OrderedPair> lopDisplacementAndSum = new ArrayList<>();
        int iDisplacementSteps = (int) ((dDisplacement / dStepSize) / 2.0);

        List<Trackable> loFirstFrameInRadius = new ArrayList<>();

        for (Trackable ot : loFirstFrame) {
            if (opCenter.getNorm(ot.getPosition()) < dRadius) {
                loFirstFrameInRadius.add(ot);
            }
        }

        for (int i = -1 * iDisplacementSteps; i <= iDisplacementSteps; i++) {
            for (int j = -1 * iDisplacementSteps; j <= iDisplacementSteps; j++) {

                List<Trackable> loSecondFrameInRadius = new ArrayList<>();

                for (Trackable ot : loSecondFrame) {
                    if (opCenter.getNorm(new OrderedPair(ot.getPosition().x - (i * dStepSize), ot.getPosition().y - (j * dStepSize))) < dRadius) {
                        loSecondFrameInRadius.add(ot);
                    }
                }

                Double dSumOfDistances = 0.0;
                for (Trackable ot1 : loFirstFrameInRadius) {
                    dSumOfDistances = dSumOfDistances + BasicTrackable.getNearestInList(loSecondFrameInRadius, ot1.getPosition()).dValue;
                }
                lopDisplacementAndSum.add(new OrderedPair(i * dStepSize, j * dStepSize, dSumOfDistances));
            }
        }

        Collections.sort(lopDisplacementAndSum, new Comparator<OrderedPair>() {

            @Override
            public int compare(OrderedPair o1, OrderedPair o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }

                if (o1 == null && o2 != null) {
                    return 1;
                }

                if (o1 != null && o2 == null) {
                    return -1;
                }

                if (o1.dValue < o2.dValue) {
                    return -1;
                }

                if (o1.dValue > o2.dValue) {
                    return 1;
                }

                return 0;

            }
        }
        );

        List<OrderedPair> lopSimilarValue = new ArrayList<>();
        for (OrderedPair op : lopDisplacementAndSum) {
            if (op.dValue == lopDisplacementAndSum.get(0).dValue) {
                lopSimilarValue.add(op);
            }
        }

        Collections.sort(lopSimilarValue, new Comparator<OrderedPair>() {

            @Override
            public int compare(OrderedPair o1, OrderedPair o2) {
                if (o1.getNorm(null) < o2.getNorm(null)) {
                    return -1;
                }

                if (o1.getNorm(null) > o2.getNorm(null)) {
                    return 1;
                }

                return 0;
            }
        }
        );

        VelocityVec oMinimizeDistance = new VelocityVec(lopSimilarValue.get(0).x, lopSimilarValue.get(0).y, opCenter);
        Trackable a = (loSecondFrame.get((int) BasicTrackable.getNearestInList(loSecondFrame, new OrderedPair(opCenter.x + oMinimizeDistance.getVelocityX(), opCenter.y + oMinimizeDistance.getVelocityY())).x));
        VelocityVec oCorrespondingVelo = new VelocityVec(Double.MAX_VALUE, Double.MAX_VALUE, null);
        oCorrespondingVelo.calcVelocityXY(new BasicTrackable(opCenter, 0.0), a, 1.0);
        return oCorrespondingVelo;

    }

//    public static BasicVelocity tryinStepsVariableSet(List<? extends Trackable> loFirstFrame, List<? extends Trackable> loSecondFrame, double dStepSize, double dDisplacement, double dRadius, OrderedPair opCenter) {
//
//        List<OrderedPair> lopDisplacementAndSum = new ArrayList<>();
//        int iDisplacementSteps = (int) ((dDisplacement / dStepSize) / 2.0);
//
//        List<Trackable> loFirstFrameInRadius = new ArrayList<>();
//
//        for (Trackable ot : loFirstFrame) {
//            if (opCenter.SecondCartesian(ot.getPosition()) < dRadius) {
//                loFirstFrameInRadius.add(ot);
//            }
//        }
//
//        for (int i = -1 * iDisplacementSteps; i <= iDisplacementSteps; i++) {
//            for (int j = -1 * iDisplacementSteps; j <= iDisplacementSteps; j++) {
//
//                List<Trackable> loSecondFrameInRadius = new ArrayList<>();
//
//                for (Trackable ot : loSecondFrame) {
//                    if (opCenter.SecondCartesian(new OrderedPair(ot.getPosition().x - (i * dStepSize), ot.getPosition().y - (j * dStepSize))) < dRadius) {
//                        loSecondFrameInRadius.add(ot);
//                    }
//                }
//                
//                Double dSumOfDistances = 0.0;
//                for (Trackable ot1 : loFirstFrameInRadius) {
//                    dSumOfDistances = dSumOfDistances + BasicTrackable.getNearestInList(loSecondFrameInRadius, ot1.getPosition()).dValue;
//                }
//                lopDisplacementAndSum.add(new OrderedPair(i * dStepSize, j * dStepSize, dSumOfDistances));
//            }
//        }
//
//        Collections.sort(lopDisplacementAndSum, new ValueOrderedPairComparator());
//        
//        List<OrderedPair> lopSimilarValue = new ArrayList<>();
//        for(OrderedPair op : lopDisplacementAndSum){
//            if(op.dValue == lopDisplacementAndSum.get(0).dValue){
//                lopSimilarValue.add(op);
//            }
//        }
//        
//        Collections.sort(lopSimilarValue, new ModulusOrderedPairComparator());
//
//        return new BasicVelocity(lopSimilarValue.get(0).x, lopSimilarValue.get(0).y);
//
//    }
}
