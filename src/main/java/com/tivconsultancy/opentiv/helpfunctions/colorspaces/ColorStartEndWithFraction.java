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

import com.tivconsultancy.opentiv.math.sets.Set1D;
import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ColorStartEndWithFraction implements Serializable {

    private static final long serialVersionUID = 2714203826504243215L;

    public Set1D oStartEnd;
    public float[] fColorStart;
    public float[] fColorEnd;

    public ColorStartEndWithFraction(Double dStartFraction, Double dEndFraction, float[] fColorStart, float[] fColorEnd) {
        this.oStartEnd = new Set1D(dStartFraction, dEndFraction);
        this.fColorStart = fColorStart;
        this.fColorEnd = fColorEnd;
    }

    public float[] interpolate(Double dFraction) {
        float[] fColor = new float[fColorStart.length];
        for (int i = 0; i < fColorStart.length; i++) {
            fColor[i] = (float) ((dFraction - oStartEnd.dLeftBorder) / (oStartEnd.dRightBorder - oStartEnd.dLeftBorder) * (fColorEnd[i] - fColorStart[i])) + fColorStart[i];
        }
        return fColor;
    }

}
