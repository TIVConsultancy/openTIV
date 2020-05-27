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
package com.tivconsultancy.opentiv.helpfunctions.colorspaces;

import java.awt.color.ColorSpace;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ColorspaceMSH extends ColorSpace implements Serializable{

    private static final long serialVersionUID = -7627389647698311097L;

    /*
    https://github.com/kennethmoreland-com/kennethmoreland-com.github.io/blob/master/color-advice/smooth-cool-warm/smooth-cool-warm.ipynb
    */
    
    public ColorspaceMSH(){
        super(TYPE_3CLR, 3);
    }
    
    @Override
    public float[] toRGB(float[] colorvalue) {
        float[] fLAB = new float[3];
        fLAB[0] = colorvalue[0] * (float) Math.cos(colorvalue[1]);
        fLAB[2] = colorvalue[0] * (float) Math.sin(colorvalue[1]) * (float) Math.cos(colorvalue[2]);
        fLAB[2] = colorvalue[0] * (float) Math.sin(colorvalue[1]) * (float) Math.sin(colorvalue[2]);
        return (new ColorSpaceCIEELab()).toRGB(fLAB);
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
        float[] fColor = new float[3];
        float[] fLAB = (new ColorSpaceCIEELab()).fromRGB(rgbvalue);
        fColor[0] = (float) Math.sqrt(fLAB[0]*fLAB[0] + fLAB[1]*fLAB[1] + fLAB[2]*fLAB[2]);
        fColor[1] = (float) Math.acos(fLAB[0]/fColor[0]);
        fColor[2] = (float) Math.atan2(fLAB[2], fLAB[1]);
        return fColor;
    }

    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
