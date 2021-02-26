/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.velocimetry.boundarytracking;


import com.tivconsultancy.opentiv.imageproc.contours.CPX;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.algorithms.Sorting;
import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.sets.Set1D;
import com.tivconsultancy.opentiv.math.sets.Set2D;
import com.tivconsultancy.opentiv.math.specials.EnumObject;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class CPXTr extends CPX implements Trackable, Serializable {

    private static final long serialVersionUID = 1280040523443199146L;

    CPXTr oParent = null;
    boolean bTracked = false;

    public CPXTr() {

    }

    public CPXTr(ImagePoint oStart) throws EmptySetException {
        super(oStart);
    }
    
    public CPXTr(CPX cpx) {
        this.lo=cpx.lo;
        this.oStart=cpx.oStart;
    }
    
    @Override
    public boolean isClosedContour(){
        if(oStart == null || oEnd == null) return false;
        return (oStart.getPos().getNorm(oEnd.getPos()) < 1.6);
    }
    
    public OrderedPair getPosition2() {
        if (this.lo.isEmpty()) {
            return null;
        }
        return lo.get(lo.size()/2).getPos();
    }
    
    @Override
    public OrderedPair getPosition() {
        OrderedPair oPos = new OrderedPair();
        int iCount = 0;
        for (ImagePoint o : lo) {
            OrderedPair oPosTemp = o.getPos();
            oPos = oPos.add(oPosTemp);
            iCount++;
        }
        if (iCount == 0) {
            return null;
        }
        oPos = oPos.mult(1.0 / (double) iCount);
        return oPos;
    }

    @Override
    public void setTracked(boolean bTracked) {
        bTracked = true;
    }

    @Override
    public Double getSize() {
        return (double) lo.size();
    }

    @Override
    public boolean isTracked() {
        return bTracked;
    }

    @Override
    public CPXTr shift(OrderedPair op) {
        CPXTr oShifted = new CPXTr();
        OrderedPair oShiftStart = this.oStart.getPos().add(op);       
        Set1D IntervalX = new Set1D(0, this.oStart.getGrid().jLength);
        Set1D IntervalY = new Set1D(0, this.oStart.getGrid().iLength);
        Set2D Boundary = new Set2D(IntervalX, IntervalY);
        for (ImagePoint o : this.lo) {
            OrderedPair oNewPos = o.getPos().add(op);
            if (Boundary.isInside(oNewPos, (Set2D.Characteristic<OrderedPair>) (OrderedPair pParameter) -> pParameter)) {
                oShifted.addPoint(new ImagePoint((int) oNewPos.x, (int) oNewPos.y, 255, oStart.getGrid()));
            }
        }
        if(oShifted.lo.isEmpty()) return null;
        if(oStart.getGrid().getIndex(oShiftStart) > 0 && oStart.getGrid().getIndex(oShiftStart) < oStart.getGrid().oa.length) oShifted.oStart = oStart.getGrid().oa[oStart.getGrid().getIndex(oShiftStart)];
        else oShifted.oStart = oShifted.lo.get(0);
        oShifted.oEnd = oShifted.lo.get(oShifted.lo.size()-1);
        return oShifted;
    }

    @Override
    public Double getDistance(Trackable o) {
        return this.getNorm((CPXTr) o);
    }

    @Override
    public Trackable getNearest(List<? extends Trackable> lo) {
        List<CPXTr> loTr = (List<CPXTr>) lo;
        CPXTr oNearest = null;
        try {
            EnumObject oNearestEnum = Sorting.getMinCharacteristic(loTr, this, new Sorting.Characteristic2<CPXTr>() {

                @Override
                public Double getCharacteristicValue(CPXTr pParameter, CPXTr pParameter2) {
                    return pParameter.getDistance(pParameter2);
                }
            });
            
            oNearest = (CPXTr) oNearestEnum.o;
                    
        } catch (EmptySetException ex) {
            Logger.getLogger(CPXTr.class.getName()).log(Level.SEVERE, null, ex);
        }

        return oNearest;        
    }           

    @Override
    public Trackable rotate(OrderedPair op, double dAlpha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
