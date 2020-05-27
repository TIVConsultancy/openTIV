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
package com.tivconsultancy.opentiv.physics.vectors;

import com.tivconsultancy.opentiv.helpfunctions.io.Reader;
import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.math.interfaces.GeneralValue;
import com.tivconsultancy.opentiv.math.interfaces.Value;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.primitives.Vector;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.physics.interfaces.Velocity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class VelocityVec extends Vector implements Velocity, Serializable {

    private static final long serialVersionUID = 3694579688340543059L;

    public Trackable VelocityObject1;
    public Trackable VelocityObject2;

    public VelocityVec(Vector o) {
        super(o, new OrderedPair(o.getX(), o.getY()), o.opUnitTangent.dValue);
    }

    public VelocityVec(Double Vx, Double Vy, OrderedPair opPosition) {
        super(opPosition, new OrderedPair(Vx, Vy), new OrderedPair(Vx, Vy).getNorm(new OrderedPair(0, 0)));
    }

    public VelocityVec(Double Vx, Double Vy, OrderedPair opPosition, Trackable VelocityObject1, Trackable VelocityObject2) {
        super(opPosition, new OrderedPair(Vx, Vy), new OrderedPair(Vx, Vy).getNorm(new OrderedPair(0, 0)));
        this.VelocityObject1 = VelocityObject1;
        this.VelocityObject2 = VelocityObject2;
    }

    public void setPosition(OrderedPair op) {
        super.x = op.x;
        super.y = op.y;
    }

    public VelocityVec clone() {
        Vector oClone = super.clone();
        return new VelocityVec(oClone.opUnitTangent.x * oClone.opUnitTangent.dValue, oClone.opUnitTangent.y * oClone.opUnitTangent.dValue, oClone, this.VelocityObject1, this.VelocityObject2);
    }

    public void shiftPosition(Double dShiftX, Double dShiftY) {
        super.x = super.x + dShiftX;
        super.y = super.y + dShiftY;
    }

    @Override
    public void calcVelocityXY(Trackable o1, Trackable o2, double dT) {
        this.x = ((double) o2.getPosition().x - o1.getPosition().x) / dT;
        this.y = ((double) o2.getPosition().y - o1.getPosition().y) / dT;
        this.x = o1.getPosition().x;
        this.y = o1.getPosition().y;
    }

    @Override
    public Double getVelocityX() {
        return this.opUnitTangent.x * this.opUnitTangent.dValue;
    }

    @Override
    public Double getVelocityY() {
        return this.opUnitTangent.y * this.opUnitTangent.dValue;
    }

    @Override
    public Double getVelocityZ() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderedPair getPositionXY() {
        return super.clone();
    }

    @Override
    public Double getSize() {
        return getLength();
    }

    public static void writeToFile(String sFile, List<? extends VelocityVec> loVelos, String sHeader, Value o) {
        List<String> ls = new ArrayList<>();
        if (sHeader == null) {
            ls.add("Posx, Posy, Posz, Vx, Vy");
        } else {
            ls.add(sHeader);
        }

        for (VelocityVec oVelo : loVelos) {
            if (oVelo != null) {
                if (o != null) {
                    ls.add("" + oVelo.x + "," + oVelo.y + "," + "0" + "," + oVelo.getVelocityX() + "," + oVelo.getVelocityY() + "," + o.getValue(oVelo));
                } else {
                    ls.add("" + oVelo.x + "," + oVelo.y + "," + "0" + "," + oVelo.getVelocityX() + "," + oVelo.getVelocityY());
                }

            }
        }

        Writer oWrtite = new Writer(sFile);
        oWrtite.write(ls);
    }

    public static void writeToFile(String sFile, List<? extends VelocityVec> loVelos) {

        try {
            FileOutputStream fileOut = new FileOutputStream(sFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(loVelos);
            out.close();
            fileOut.close();            
        } catch (IOException i) {
            System.err.println("Error Writing Velocity Data: " + i.getMessage());
        }
    }
    
    public static List<? extends VelocityVec> readFromFile(String sFile) {
        
        FileInputStream fisFeatures = null;
        ObjectInputStream oisFeatures = null;
        List<? extends VelocityVec> loReadIn = new ArrayList<>();
        
        try {
            fisFeatures = new FileInputStream(sFile);
            oisFeatures = new ObjectInputStream(fisFeatures);
            loReadIn = (ArrayList) oisFeatures.readObject();
            oisFeatures.close();
            fisFeatures.close();
        } catch (IOException ioe) {
            try {
                oisFeatures.close();
                fisFeatures.close();
            } catch (Exception ex) {
                System.err.println("Error Reading Velocity Data: " + ex.getMessage());
            }
            System.err.println("Error Reading Velocity Data: " + ioe.getMessage());            
        } catch (ClassNotFoundException c) {
            try {
                oisFeatures.close();
                fisFeatures.close();
            } catch (Exception ex) {
                System.err.println("Error Reading Velocity Data: " + ex.getMessage());
            }
            System.err.println("Error Reading Velocity Data, Class not found: " + c.getMessage());   
        }
        
        return loReadIn;
        
    }

    public static List<VelocityVec> readFromFile(String sFile, GeneralValue<String[]> o) throws IOException {
        List<VelocityVec> loOut = new ArrayList<>();

        Reader oRead = new Reader(sFile);
        List<String[]> lsInput = oRead.readBigFileInListStringParsing(1, ",");

        for (String[] s : lsInput) {
            if (o == null) {
                loOut.add(new VelocityVec(Double.valueOf(s[3]), Double.valueOf(s[4]), new OrderedPair(Double.valueOf(s[0]), Double.valueOf(s[1]))));
            } else {
                loOut.add((VelocityVec) o.getValue(s));
            }
        }

        return loOut;
    }

    public VelocityVec add(VelocityVec o) {
        VelocityVec oAdd = new VelocityVec(super.add(o));
        oAdd.VelocityObject1 = this.VelocityObject1;
        oAdd.VelocityObject2 = this.VelocityObject2;
        return oAdd;
    }

    @Override
    public VelocityVec add(OrderedPair o2) {
        VelocityVec oAdd = new VelocityVec(super.add(o2));
        oAdd.VelocityObject1 = this.VelocityObject1;
        oAdd.VelocityObject2 = this.VelocityObject2;
        return oAdd;
    }

//    public static BasicVelocity getAverageAroundPoint(List<BasicVelocity> loVelos, OrderdPair op, double dVicinity) {
//
//        List<BasicVelocity> loVeloNearBy = new ArrayList<>();
//
//        for (BasicVelocity oV : loVelos) {
//            if (OrderdPair.SecondCartesian(oV.opPosition, op) < dVicinity) {
//                loVeloNearBy.add(oV);
//            }
//        }
//
//        Double dVx = 0.0;
//        Double dVy = 0.0;
//
//        int iRunner = 1;
//        for (BasicVelocity oV : loVeloNearBy) {
//            if (oV != null) {
//                dVx = dVx + oV.getVelocityX();
//                dVy = dVy + oV.getVelocityY();
//                iRunner++;
//            }
//        }
//
//        if (iRunner > 1) {
//            return new BasicVelocity(dVx / ((double) iRunner), dVy / ((double) iRunner), op);
//        } else {
//            return null;
//        }
//    }
    @Override
    public Vector getVec() {
        return this;
    }
}
