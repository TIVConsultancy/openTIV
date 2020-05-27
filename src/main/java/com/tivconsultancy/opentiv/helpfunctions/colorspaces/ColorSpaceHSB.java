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

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ColorSpaceHSB extends ColorSpace implements Serializable {

    private static final long serialVersionUID = 4458369078784888130L;
    
    public ColorSpaceHSB(){
        super(ColorSpace.TYPE_HSV, 3);
    }

    @Override
    public float[] toRGB(float[] colorvalue) {
        float[] oRGBCol = new float[3];
        Color oRGBColor = Color.getHSBColor(colorvalue[0], colorvalue[1], colorvalue[2]);
        oRGBCol[0] = (float) (oRGBColor.getRed()/255.0);
        oRGBCol[1] = (float) (oRGBColor.getGreen()/255.0);
        oRGBCol[2] = (float) (oRGBColor.getBlue()/255.0);
        
        return oRGBCol;
    }
    
    public Color toColor(float[] colorvalue) {
        float[] oRGBCol = new float[3];
        Color oRGBColor = Color.getHSBColor(colorvalue[0], colorvalue[1], colorvalue[2]);
        oRGBCol[0] = (float) (oRGBColor.getRed()/255.0);
        oRGBCol[1] = (float) (oRGBColor.getGreen()/255.0);
        oRGBCol[2] = (float) (oRGBColor.getBlue()/255.0);
        
        return new Color(oRGBCol[0], oRGBCol[1], oRGBCol[2]);
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
        return (java.awt.Color.RGBtoHSB( (int) (rgbvalue[0] * 255), (int) (rgbvalue[1] * 255), (int) (rgbvalue[2] * 255), null));
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
