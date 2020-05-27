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

import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import java.util.List;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Splines {

    public static org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction getSpline(List<ImagePoint> loPoints) {

        boolean bX = true;
        boolean bY = true;

        for (int i = 1; i < loPoints.size(); i++) {
            if ((loPoints.get(i - 1).getPos().x - loPoints.get(i).getPos().x) >= 0) {
                bX = false;
            }
            if ((loPoints.get(i - 1).getPos().y - loPoints.get(i).getPos().y) <= 0) {
                bY = false;
            }
        }

        if (!bX && !bY) {
            return null;
        }

        double[] xPoints = new double[loPoints.size()];
        double[] yPoints = new double[loPoints.size()];
        if (bX) {
            for (int i = 0; i < loPoints.size(); i++) {
                xPoints[i] = loPoints.get(i).getPos().x;
                yPoints[i] = -1.0*loPoints.get(i).getPos().y;
            }
        } else {
            for (int i = 0; i < loPoints.size(); i++) {
                xPoints[i] = -1.0*loPoints.get(i).getPos().y;
                yPoints[i] = loPoints.get(i).getPos().x;
            }
        }

        SplineInterpolator o = new SplineInterpolator();
        PolynomialSplineFunction oSpline = o.interpolate(xPoints, yPoints);

        return oSpline;

    }
    
    public static Boolean checkRotation(List<ImagePoint> loPoints){
        boolean bX = true;
        boolean bY = true;

        for (int i = 1; i < loPoints.size(); i++) {
            if ((loPoints.get(i - 1).getPos().x - loPoints.get(i).getPos().x) >= 0) {
                bX = false;
            }
            if ((loPoints.get(i - 1).getPos().y - loPoints.get(i).getPos().y) <= 0) {
                bY = false;
            }
        }

        if (!bX && !bY) {
            return null;
        }
        
        return !bX;
        
    }

}
