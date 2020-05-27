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
package com.tivconsultancy.opentiv.velocimetry.primitives;


import com.tivconsultancy.opentiv.helpfunctions.io.Reader;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class BasicTrackable implements Trackable, Serializable {

    private static final long serialVersionUID = -2331823097619209885L;
    
    OrderedPair opPosition;
    boolean bIsTracked = false;
    double dSize;
    
    public BasicTrackable(OrderedPair opPosition, double dSize){
        this.opPosition = opPosition;        
        this.dSize = dSize;
    }

    @Override
    public OrderedPair getPosition() {
        return this.opPosition;
    }

    @Override
    public void setTracked(boolean bTracked) {
        this.bIsTracked = bTracked;                
    }

    @Override
    public Double getSize() {
        return dSize;
    }

    @Override
    public boolean isTracked() {
        return bIsTracked;
    }

    @Override
    public Trackable shift(OrderedPair op) {        
        return new BasicTrackable(opPosition.add2(op, "xy"), dSize);
    }

    @Override
    public Double getDistance(Trackable o) {
        return o.getPosition().getNorm(this.opPosition);
    }

    @Override
    public Trackable getNearest(List<? extends Trackable> lo) {
        List<Trackable> loTrack = new ArrayList<>();
        for(Trackable o : lo){
            loTrack.add(o);
        }
        EnumObject oEnum = null;
        try {
            oEnum = Sorting.getMinCharacteristic(loTrack, this, new Sorting.Characteristic2<Trackable>() {
                
                @Override
                public Double getCharacteristicValue(Trackable pParameter, Trackable pParameter2) {
                    return pParameter.getDistance(pParameter2);
                }        
            });
        } catch (EmptySetException ex) {
            Logger.getLogger(BasicTrackable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (Trackable) oEnum.o;
    }

    @Override
    public Trackable rotate(OrderedPair op, double dAlpha) {
        this.opPosition.rotateAroundReference(op, dAlpha);
        return this;
    }
    
    public static List<BasicTrackable> readFromFile(String sFile, int iColPosx, int iColPosy) throws IOException {
        Reader oRead = new Reader(sFile);
        Double[][] daa = oRead.readBigFile();

        List<BasicTrackable> oTracks = new ArrayList<>();

        for (Double[] da : daa) {
            oTracks.add(new BasicTrackable(new OrderedPair(da[iColPosx], da[iColPosy]), 0.0));
        }

        return oTracks;
    }
    
    public static OrderedPair getNearestInList(List<? extends Trackable> lop, OrderedPair op) {
        double[] daDistance = new double[lop.size()];
        for (int i = 0; i < lop.size(); i++) {
            daDistance[i] = lop.get(i).getPosition().getNorm(op);
        }

        double dMinDist = Double.MAX_VALUE;
        int iIndeOfMinDist = -1;

        for (int j = 0; j < daDistance.length; j++) {
            if (daDistance[j] < dMinDist) {
                dMinDist = daDistance[j];
                iIndeOfMinDist = j;
            }
        }

        return new OrderedPair(iIndeOfMinDist, iIndeOfMinDist, dMinDist);
    }
    
}
