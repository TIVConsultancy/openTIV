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
package com.tivconsultancy.opentiv.postproc.vector;

import static com.tivconsultancy.opentiv.math.primitives.BasicMathLib.SecondCartesian;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import com.tivconsultancy.opentiv.math.primitives.Vector;
import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class VectorArrow implements Serializable{

    private static final long serialVersionUID = 5642230743964655607L;

    public Double[] dmid;
    public Double[] dtip;

    public Double[] drighttip;
    public Double[] dlefttip;
    
    public Color oColor = null;

   public VectorArrow(Vector oVec, double stretch) {
        this.dmid = new Double[2];
        this.dtip = new Double[2];

        OrderedPair vVec = new OrderedPair(oVec.opUnitTangent.x*oVec.opUnitTangent.dValue, oVec.opUnitTangent.y*oVec.opUnitTangent.dValue);
        double dIndexy = oVec.y;
        double dIndexx = oVec.x;
        this.drighttip = new Double[2];
        this.dlefttip = new Double[2];
        this.dmid[0] = dIndexy;
        this.dmid[1] = dIndexx;
        this.dtip[0] = dIndexy + (vVec.y * stretch);
        this.dtip[1] = dIndexx + (vVec.x * stretch);
        
        double dNorm = SecondCartesian(dmid[1], dmid[0], dtip[1], dtip[0]);

        double py = dIndexy + stretch/2.0 * vVec.y;
        double px = dIndexx + stretch/2.0 * vVec.x;
        double betr = vVec.getNorm(new OrderedPair(0, 0));
        double[] normv1 = new double[2];
        normv1[1] = vVec.y / betr;
        normv1[0] = -vVec.x / betr;
        double[] normv2 = new double[2];
        normv2[1] = -vVec.y / betr;
        normv2[0] = vVec.x / betr;
        this.drighttip[0] = py + dNorm / 4.0 * normv1[0];
        this.drighttip[1] = px + dNorm / 4.0 * normv1[1];
        this.dlefttip[0] = py + dNorm / 4.0 * normv2[0];
        this.dlefttip[1] = px + dNorm / 4.0 * normv2[1];
        
        
    }
   
   VectorArrow(Vector oVec, int stretch, Color oColor) {
       this(oVec, stretch);
       this.oColor = oColor;
   }


    
}
