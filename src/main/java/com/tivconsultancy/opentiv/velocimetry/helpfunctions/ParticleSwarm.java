/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.velocimetry.helpfunctions;

import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.velocimetry.primitives.BasicTrackable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ParticleSwarm implements Trackable {

    public List<BasicTrackable> loContent = new ArrayList<>();
    boolean bTracked = false;

    public ParticleSwarm(List<BasicTrackable> loInput, BasicTrackable oRef, Double dRadius) {        
        for (BasicTrackable o : loInput) {
            if (dRadius != null && oRef != null && o.getDistance(oRef) <= dRadius) {
                loContent.add(o);
            }else if (dRadius == null || oRef == null) {
                loContent.add(o);
            }
        }
    }

    @Override
    public OrderedPair getPosition() {
        double dX = 0.0;
        double dY = 0.0;
        int iCount = 0;
        for (BasicTrackable o : loContent) {
            dX = dX + o.getPosition().x;
            dY = dY + o.getPosition().y;
            iCount++;
        }
        if (iCount == 0) {
            return null;
        }
        return new OrderedPair(dX / iCount, dY / iCount, null);
    }

    @Override
    public void setTracked(boolean bTracked) {
        this.bTracked = bTracked;
    }

    @Override
    public Double getSize() {
        double dSize = 0.0;
        int iCount = 0;
        for (BasicTrackable o : loContent) {
            dSize = dSize + o.getSize();            
            iCount++;
        }
        if (iCount == 0) {
            return null;
        }
        return dSize/iCount;
    }

    @Override
    public boolean isTracked() {
        return bTracked;
    }

    @Override
    public Trackable shift(OrderedPair op) {
        List<BasicTrackable> loShift = new ArrayList<>();
        for(BasicTrackable o : loContent){
            loShift.add((BasicTrackable) o.shift(op));
        }
        return new ParticleSwarm(loShift, null, null);
    }

    @Override
    public Double getDistance(Trackable o) {
        if(o instanceof ParticleSwarm) return this.getDistanceSwarm((ParticleSwarm) o);
        double dDist = 0.0;
        int iCount = 0;
        for (BasicTrackable oCont : loContent) {
            dDist = dDist + o.getDistance(oCont);
            iCount++;
        }
        if(iCount == 0) return null;
        return dDist;
//        List<Trackable> ld = new ArrayList<>();
//        for(BasicTrackable oCont : loContent){
//            ld.add(oCont);
//        }
//        EnumObject oEnum = null;
//        try {
//            oEnum = Sorting.getMinCharacteristic(ld, o, new Sorting.Characteristic2<Trackable>() {
//                
//                @Override
//                public Double getCharacteristicValue(Trackable pParameter, Trackable pParameter2) {
//                    return pParameter.getDistance(pParameter2);
//                }
//            }
//            );
//        } catch (EmptySetException ex) {
//            Logger.getLogger(ParticleSwarm.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if(oEnum == null) return null;
//        return oEnum.dEnum;
//        
    }
    
    public Double getDistanceSwarm(ParticleSwarm o) {

        List<Trackable> loNearestTracks = new ArrayList<>();
        if(loContent.isEmpty() || o.loContent.isEmpty()) return null;
        for(BasicTrackable oCont : loContent){
            loNearestTracks.add(oCont.getNearest(o.loContent));
        }
        
        double dDist = 0.0;
        int iCount = 0;
        
        for(int i = 0; i < loNearestTracks.size(); i++){
            if(loNearestTracks.get(i) == null) continue;
            dDist = dDist + loContent.get(i).getDistance(loNearestTracks.get(i));
            iCount++;
        }
        
        if(iCount == 0) return  null;
        return dDist;

    }

    @Override
    public Trackable getNearest(List<? extends Trackable> lo) {
        List<Trackable> ld = new ArrayList<>();
        for(Trackable o : lo){
            ld.add(o);
        }
        EnumObject oEnum = null;
        try {
            oEnum = Sorting.getMinCharacteristic(ld, this, new Sorting.Characteristic2<Trackable>() {
                
                @Override
                public Double getCharacteristicValue(Trackable pParameter,Trackable pParameter2) {
                    return pParameter.getDistance(pParameter2);
                }
            });
        } catch (EmptySetException ex) {
            Logger.getLogger(ParticleSwarm.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(oEnum == null){
            return null;
        }
        return (Trackable) oEnum.o;
    }

    @Override
    public Trackable rotate(OrderedPair op, double dAlpha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
