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
package com.tivconsultancy.opentiv.math.functions;

import com.tivconsultancy.opentiv.math.sets.Set1D;
import java.io.Serializable;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Spline implements Serializable{

    private static final long serialVersionUID = 4972141954276655645L;

    public SplineInterpolator o = new SplineInterpolator();
    public PolynomialSplineFunction oSpline = null;
    public boolean bRot = false;
    public boolean bdydxInfinity = false;
    public boolean bdydxZero = false;
    public boolean bSwitchCoord = false;
    public boolean bInverseX = false;

    public double x1;
    public double x2;
    public double x3;

    public double y1;
    public double y2;
    public double y3;

    public Spline(double[] xPoints, double[] yPoints) {

        if (xPoints.length > 3) {
            throw new ExceptionInInitializerError("Points for spline = 3");
        }
        if (yPoints.length > 3) {
            throw new ExceptionInInitializerError("Points for spline = 3");
        }

        x1 = xPoints[0];
        y1 = yPoints[0];
        x2 = xPoints[1];
        y2 = yPoints[1];
        x3 = xPoints[2];
        y3 = yPoints[2];

        boolean bXMonoton = false;
        boolean bYMonoton = true;

        if ((x1 < x2 && x2 < x3) || (x1 > x2 && x2 > x3)) {
            bXMonoton = true;
        }
        if ((y1 < y2 && y2 < y3) || (y1 > y2 && y2 > y3)) {
            bYMonoton = true;
        }
        if (x1 == x2 && x2 == x3) {
            bdydxInfinity = true;
        }
        if (y1 == y2 && y2 == y3) {
            bdydxZero = true;
        }

        if (bdydxInfinity && bdydxZero) {
            throw new ExceptionInInitializerError("Not a Function");
        }

        if (bdydxInfinity || bdydxZero) {
            return;
        }

        if (!bXMonoton && !bYMonoton) {
            throw new ExceptionInInitializerError("Not a Function");
        }

        if (bXMonoton && (x1 > x2 && x2 > x3)) {
            bInverseX = true;
            x1 = -1.0 * x1;
            x2 = -1.0 * x1;
            x3 = -1.0 * x1;
        }

        if (bYMonoton && (y1 > y2 && y2 > y3)) {
            bInverseX = true;
            bSwitchCoord = true;
            x1 = -1.0 * yPoints[0];
            y1 = -1.0 * yPoints[0];
            x2 = -1.0 * yPoints[1];
            y2 = xPoints[1];
            x3 = xPoints[2];
            y3 = xPoints[2];
        }

        if (bYMonoton && (y1 < y2 && y2 < y3)) {
            bSwitchCoord = true;
            x1 = yPoints[0];
            y1 = yPoints[0];
            x2 = yPoints[1];
            y2 = xPoints[1];
            x3 = xPoints[2];
            y3 = xPoints[2];
        }
        
        oSpline = o.interpolate(new double[]{x1, x2, x3}, new double[]{y1, y2, y3});

    }

    public double getValue(double x) {
        if (this.bInverseX) {
            return -1.0 * oSpline.value(-1.0 * x);
        }
        return oSpline.value(x);
    }

    public double getdydx(double x) {
        if (this.bInverseX) {
            if (bSwitchCoord) {
                return -1.0 / oSpline.derivative().value(-1.0 * x);
            }
            return -1.0 * oSpline.derivative().value(-1.0 * x);
        }
        if (bSwitchCoord) {
            return 1.0 / oSpline.derivative().value(x);
        }
        return oSpline.derivative().value(x);
    }
    
    public Double getdydxx(double x) {        
        double dydx1 = getdydx(x);
        double dydx2 = 0.0;
        double dDist = 0.0;
        Set1D o;
        if (bInverseX) {
            o = new Set1D(-1.0 * this.x1, -1.0 * this.x3);
            double dDistLeft = Math.abs(-1.0 * x - o.dLeftBorder);
            double dDistRight = Math.abs(o.dRightBorder - (-1.0 * x));
            if (dDistLeft < dDistRight) {
                dDist = dDistRight / 100.0;
            } else {
                dDist = -1.0 * dDistLeft / 100.0;
            }

        } else {
            o = new Set1D(this.x1, this.x3);
            double dDistLeft = Math.abs(x - o.dLeftBorder);
            double dDistRight = Math.abs(o.dRightBorder - x);
            if (dDistLeft < dDistRight) {
                dDist = dDistRight / 100.0;
            } else {
                dDist = -1.0 * dDistLeft / 100.0;
            }
        }

        if (dDist != 0.0) {
            dydx2 = oSpline.derivative().value((-1.0 * x) + dDist);
            return (dydx2 - dydx1) / dDist;
        } else {
            return null;
        }

    }

//    public Double getdydxx(double x) {
//        oSpline.derivative()
//        double dydx1 = getdydx(x);
//        double dydx2 = 0.0;
//        double dDist = 0.0;
//        Set1D o;
//        if (bInverseX) {
//            o = new Set1D(-1.0 * this.x1, -1.0 * this.x3);
//            double dDistLeft = Math.abs(-1.0 * x - o.dLeftBorder);
//            double dDistRight = Math.abs(o.dRightBorder - (-1.0 * x));
//            if (dDistLeft < dDistRight) {
//                dDist = dDistRight / 100.0;
//            } else {
//                dDist = -1.0 * dDistLeft / 100.0;
//            }
//
//        } else {
//            o = new Set1D(this.x1, this.x3);
//            double dDistLeft = Math.abs(x - o.dLeftBorder);
//            double dDistRight = Math.abs(o.dRightBorder - x);
//            if (dDistLeft < dDistRight) {
//                dDist = dDistRight / 100.0;
//            } else {
//                dDist = -1.0 * dDistLeft / 100.0;
//            }
//        }
//
//        if (dDist != 0.0) {
//            dydx2 = oSpline.derivative().value((-1.0 * x) + dDist);
//            return (dydx2 - dydx1) / dDist;
//        } else {
//            return null;
//        }
//
//    }

}
