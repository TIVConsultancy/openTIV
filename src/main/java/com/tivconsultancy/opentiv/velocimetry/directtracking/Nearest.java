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
package com.tivconsultancy.opentiv.velocimetry.directtracking;

import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition2;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.sets.Set2D;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Nearest {

    public static VelocityVec getNearestForComplexObjectsParallel(Trackable otRef, List<? extends Trackable> loSecondFrame, double dStepSize, Set2D oSearchArea, SideCondition2 o) throws EmptySetException, IOException {

        List<OPwithCont> lopDisplacementAndSum = new ArrayList<>();

        List<Trackable> loSecondFrameInRadius = new ArrayList<>();

        for (Trackable ot : loSecondFrame) {
            if(ot != null && o == null) loSecondFrameInRadius.add(ot);
            if(ot != null && o.check(otRef, ot)) loSecondFrameInRadius.add(ot);
        }

        if (loSecondFrameInRadius.isEmpty()) {
            return null;
        }

        Collection<OrderedPair> elems = new LinkedList<>();

        for (int i = (int) (oSearchArea.oIntervalY.dLeftBorder); i <= (int) (oSearchArea.oIntervalY.dRightBorder); i = i + (int) dStepSize) {
            for (int j = (int) (oSearchArea.oIntervalX.dLeftBorder); j <= (int) (oSearchArea.oIntervalX.dRightBorder); j = j + (int) dStepSize) {
                elems.add(new OrderedPair(i, j, 0.0));
            }
        }
        
        //parallel implementation
//        Parallel.For(elems, new Parallel.Operation<OrderedPair>() {
//            @Override
//            public void perform(OrderedPair pParameter) {
//                List<Trackable> loSecondFrameShifted = new ArrayList<>();
//                for (Trackable ot : loSecondFrameInRadius) {
////                    loSecondFrameShifted.add(new BasicTrackable(ot.getPosition().x - (i * dStepSize), ot.getPosition().y - (j * dStepSize)));
//                    loSecondFrameShifted.add(ot.shift(pParameter));
////                    loSecondFrameShifted.add(ot.shift(new OrderdPair((-1.0) * i, (-1.0) * j)));
//                }
//                // lopDisplacementAndSum.add(new OrderdPair(i * dStepSize, j * dStepSize, otRef.getDistance(otRef.getNearest(loSecondFrameShifted))));
//                lopDisplacementAndSum.add(new OrderedPair(pParameter.x, pParameter.y, otRef.getDistance(otRef.getNearest(loSecondFrameShifted))));
//            }
//        });
//        int iCount = 0;
//        long timeStart = Calendar.getInstance().getTimeInMillis();
//        double dTimeShift = 0.0;
        //shifting all Objects in the second frame
//        for (OrderedPair pParameter : elems) {
//            List<Trackable> loSecondFrameShifted = new ArrayList<>();                        
//            for (Trackable ot : loSecondFrameInRadius) {                
//                Trackable oShifted = ot.shift(pParameter);
//                if(oShifted != null) loSecondFrameShifted.add(oShifted);                
//            }            
//            if(loSecondFrameShifted.isEmpty()) continue;            
//            Trackable oNearest = otRef.getNearest(loSecondFrameShifted);            
//            lopDisplacementAndSum.add(new OPwithCont(pParameter.x, pParameter.y, otRef.getDistance(oNearest), oNearest, otRef));            
//            iCount++;
//        }
        //shifting only the reference object
        for (OrderedPair pParameter : elems) {
            Trackable oShiftRef = otRef.shift(pParameter);
            if(oShiftRef == null) continue;
            if(oShiftRef.getPosition().x < 0 || oShiftRef.getPosition().y < 0) continue;
            Trackable oNearest;
            if(loSecondFrameInRadius.size() == 1){
                oNearest = loSecondFrameInRadius.get(0);
            }else{
                oNearest = oShiftRef.getNearest(loSecondFrameInRadius);            
            }
             
            lopDisplacementAndSum.add(new OPwithCont(pParameter.x, pParameter.y, oShiftRef.getDistance(oNearest), oNearest, oShiftRef));            
//            iCount++;
        }
//        System.out.println("++++++++++++");
//        System.out.println( (Calendar.getInstance().getTimeInMillis() - timeStart) /1000.0);
//        System.out.println("------------");
//        System.out.println(dTimeShift/1000.0);
        
        //Writing out the correlation map as image
//      double dMin = Sorting.getMinCharacteristic(lopDisplacementAndSum, new Sorting.Characteristic<OPwithCont>() {
//
//            @Override
//            public Double getCharacteristicValue(OPwithCont pParameter) {
//                return pParameter.dValue;
//            }
//        }).dEnum;
//  
        
//        double dMax = Sorting.getMaxCharacteristic(lopDisplacementAndSum, new Sorting.Characteristic<OPwithCont>() {
//
//            @Override
//            public Double getCharacteristicValue(OPwithCont pParameter) {
//                return pParameter.dValue;
//            }
//        }).dEnum;
        
//        ImageGrid oGrid = new ImageGrid(81, 81);
//        for(OPwithCont oOP : lopDisplacementAndSum){
//            OrderedPair opTemp = new OrderedPair(oOP.x + 40, oOP.y + 40, (oOP.dValue - dMin)/dMax * 255.0);            
//            oGrid.oa[oGrid.getIndex(opTemp)] = new ImagePoint(oGrid.getIndex(opTemp), opTemp.dValue.intValue(), oGrid);
//        }
//        
//        Writer.PaintGreyPNG(oGrid, new File("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Data\\BubbleCase1\\Debug\\Correlation.png"));
        
        Collections.sort(lopDisplacementAndSum, new Comparator<OPwithCont>() {

            @Override
            public int compare(OPwithCont o1, OPwithCont o2) {
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

        List<OPwithCont> lopSimilarValue = new ArrayList<>();
        for (OPwithCont op : lopDisplacementAndSum) {
            if (op != null && op.dValue == lopDisplacementAndSum.get(0).dValue) {
                lopSimilarValue.add(op);
            }
        }

        Collections.sort(lopSimilarValue, new Comparator<OPwithCont>() {

            @Override
            public int compare(OPwithCont o1, OPwithCont o2) {
                if (o1.getNorm(new OrderedPair(0.0, 0.0)) < o2.getNorm(new OrderedPair(0.0, 0.0))) {
                    return -1;
                }

                if (o1.getNorm(new OrderedPair(0.0, 0.0)) > o2.getNorm(new OrderedPair(0.0, 0.0))) {
                    return 1;
                }

                return 0;
            }
        });

        VelocityVec oVelo = new VelocityVec(lopSimilarValue.get(0).x, lopSimilarValue.get(0).y, otRef.getPosition(), lopSimilarValue.get(0).oContent1, otRef);

        return oVelo;
    }
    
    public static class OPwithCont extends OrderedPair{
        public Trackable oContent1;
        public Trackable oContent2;
        
        public OPwithCont(double x, double y, double dValue, Trackable oContent1, Trackable oContent2){
            super(x, y, dValue);
            this.oContent1 = oContent1;
            this.oContent2 = oContent2;
        }
    }
    
}

  
