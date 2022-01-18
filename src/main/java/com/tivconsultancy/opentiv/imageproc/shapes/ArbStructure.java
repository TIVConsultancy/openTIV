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
package com.tivconsultancy.opentiv.imageproc.shapes;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ArbStructure implements Shape, Serializable{

    private static final long serialVersionUID = -8299568128551323389L;
    public List<ImagePoint> loPoints = new ArrayList<>();
    
    public ArbStructure(List<?> loInput){
        if(loInput.isEmpty()){
            return;
        }
        if(loInput.get(0) instanceof ImagePoint){
            loPoints = (List<ImagePoint>) loInput;
        }
        if(loInput.get(0) instanceof MatrixEntry){
            for(Object o : loInput){
                MatrixEntry me = (MatrixEntry) o;
                loPoints.add(new ImagePoint(me.j, me.i, 0, null));
            }            
        }        
    }
    
    public List<MatrixEntry> getBoundingBox(){
        double dMaxX = 0;
        double dMinX = Double.MAX_VALUE;
        
        double dMaxY = 0;
        double dMinY = Double.MAX_VALUE;
        
        for(ImagePoint o : loPoints){
            OrderedPair op = o.getPos();
            if(dMaxX < op.getPosX()){
                dMaxX = op.getPosX();
            }
            if(dMinX > op.getPosX()){
                dMinX = op.getPosX();
            }
            if(dMaxY < op.getPosY()){
                dMaxY = op.getPosY();
            }
            if(dMinY > op.getPosY()){
                dMinY = op.getPosY();
            }
        }
        //Coordinates with respect to picture coordinates
        MatrixEntry leftTopCorner = new MatrixEntry((int) dMinY, (int) dMinX);
        MatrixEntry rightBottomCorner = new MatrixEntry((int) dMaxY, (int) dMaxX);
        List<MatrixEntry> lmeBox = new ArrayList<>();
        lmeBox.add(leftTopCorner);
        lmeBox.add(rightBottomCorner);
        return lmeBox;
    }
        

    @Override
    public double getSize() {
        //Circle equivalnt radius
        return Math.sqrt(loPoints.size()/Math.PI);
    }

    @Override
    public double getMinorAxis() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMajorAxis() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getFormRatio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getOrientationAngle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getPixelCount() {
        return loPoints.size();
    }

    @Override
    public List<MatrixEntry> getlmeList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getGreyDerivative() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderedPair getSubPixelCenter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
