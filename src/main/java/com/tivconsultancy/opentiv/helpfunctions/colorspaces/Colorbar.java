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

import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Colorbar {

    public Color getColor(Double Value);

    public Double getMappingStartValue();

    public Double getMappingEndValue();

    public List<ColorStartEndWithFraction> getInterpolColors();

    public void printTestbar(String sOut, int iSteps);

    public class StartEndLinearColorBar implements Colorbar {

        Double dMappingStart;
        Double dMappingEnd;
        List<ColorStartEndWithFraction> loColors = new ArrayList<ColorStartEndWithFraction>();
        List<Color> loInputColors = new ArrayList<Color>();
        ColorSpace oColorSpace;
        ColorOperation<Double> oOperatrion = new ColorOperation<Double>() {
            @Override
            public Double operate(Double pParameter) {
                return pParameter;
            }
        };

        ColorStepsGenerator<Object> oColGen = new ColorStepsGenerator<Object>() {

            @Override
            public List<ColorStartEndWithFraction> generate(List<Color> loColors, ColorSpace oColorSpace) {
                List<ColorStartEndWithFraction> loCols = new ArrayList<ColorStartEndWithFraction>();
                double dFractionStep = 1.0 / (loColors.size() - 1);
                int iCount = 0;
                for (double d = 0.0; d <= 1.0 - dFractionStep; d = d + dFractionStep) {
                    Color oStart = loColors.get(iCount);
                    Color oEnd = loColors.get(iCount + 1);
                    loCols.add(new ColorStartEndWithFraction(d, d + dFractionStep, oColorSpace.fromRGB(oStart.getColorComponents(null)), oColorSpace.fromRGB(oEnd.getColorComponents(null))));
                    iCount++;
                    if (iCount >= loColors.size()) {
                        break;
                    }
                }
                return loCols;
            }
        };

        public StartEndLinearColorBar(Double dMappingStartValue, Double dMappingEndValue, Color oStart, Color oEnd, ColorSpace oColorSpace, ColorOperation oOperation) {
            this.dMappingStart = dMappingStartValue;
            this.dMappingEnd = dMappingEndValue;
            this.oColorSpace = oColorSpace;
            loInputColors.add(oStart);
            loInputColors.add(oEnd);
            loColors.add(new ColorStartEndWithFraction(0.0, 1.0, oColorSpace.fromRGB(oStart.getColorComponents(null)), oColorSpace.fromRGB(oEnd.getColorComponents(null))));
            if (oOperation != null) {
                this.oOperatrion = oOperation;
            }
        }

        public StartEndLinearColorBar(Double dMappingStartValue, Double dMappingEndValue, List<Color> loColors, ColorSpace oColorSpace, ColorOperation oOperation) {
            this.dMappingStart = dMappingStartValue;
            this.dMappingEnd = dMappingEndValue;
            this.oColorSpace = oColorSpace;
            loInputColors = loColors;
            this.loColors = oColGen.generate(loColors, oColorSpace);
            if (oOperation != null) {
                this.oOperatrion = oOperation;
            }
        }
        
        @Override
        public Color getColor(Double dValueInput) {
            double dValue = this.oOperatrion.operate(dValueInput);
            double dMappingStartValue = this.oOperatrion.operate(dMappingStart);
            double dMappingEndValue = this.oOperatrion.operate(dMappingEnd);

            float[] fColor = new float[oColorSpace.getNumComponents()];
            double dFraction = (dValue - dMappingStartValue) / (dMappingEndValue - dMappingStartValue);
            for (ColorStartEndWithFraction o : loColors) {
                if (o.oStartEnd.isInside(dFraction)) {
                    fColor = o.interpolate(dFraction);
                    break;
                }else{
                    fColor = o.interpolate(1.0);
                }
            }

            fColor = oColorSpace.toRGB(fColor);

            java.awt.Color.RGBtoHSB((int) (fColor[0] * 255), (int) (fColor[1] * 255), (int) (fColor[2] * 255), fColor);

            return Color.getHSBColor(fColor[0], fColor[1], fColor[2]);
        }
        
        @Override
        public Double getMappingStartValue() {
            return dMappingStart;
        }

        @Override
        public Double getMappingEndValue() {
            return dMappingEnd;
        }

        @Override
        public List<ColorStartEndWithFraction> getInterpolColors() {
            return loColors;
        }

        @Override
        public void printTestbar(String sOut, int iSteps) {
            Colorbar oColbar = new StartEndLinearColorBar(0.0, (double) iSteps, loInputColors, oColorSpace, oOperatrion);

            int iHeight = 10;
            int[][] iaPixelsR = new int[iHeight][iSteps];
            int[][] iaPixelsG = new int[iHeight][iSteps];
            int[][] iaPixelsB = new int[iHeight][iSteps];
            for (int i = 0; i < iHeight; i++) {
                for (int j = 0; j < iSteps; j++) {
                    Color oColor = oColbar.getColor((double) j);
                    iaPixelsR[i][j] = oColor.getRed();
                    iaPixelsG[i][j] = oColor.getGreen();
                    iaPixelsB[i][j] = oColor.getBlue();
                }
            }
            try {
                Writer.PaintColorPNG(iaPixelsR, iaPixelsG, iaPixelsB, new File(sOut));
            } catch (IOException ex) {
                Logger.getLogger(Colorbar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public static List<Color> getColdToWarm() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(0.2298f, 0.298711f, 0.753689f));
            loCol.add(new Color(0.303868f, 0.509422f, 0.917388f));
            loCol.add(new Color(0.466667f, 0.604562f, 0.968154f));
            loCol.add(new Color(0.552956f, 0.68893f, 0.995377f));
            loCol.add(new Color(0.63917f, 0.759594f, 0.998154f));
            loCol.add(new Color(0.722194f, 0.813947f, 0.976577f));
            loCol.add(new Color(0.798688f, 0.84979f, 0.931685f));
            loCol.add(new Color(0.8654f, 0.865415f, 0.8654f));
            loCol.add(new Color(0.924132f, 0.82739f, 0.774502f));
            loCol.add(new Color(0.958846f, 0.769772f, 0.678004f));
            loCol.add(new Color(0.969955f, 0.69427f, 0.57937f));
            loCol.add(new Color(0.958007f, 0.602838f, 0.481773f));
            loCol.add(new Color(0.923949f, 0.497307f, 0.387976f));
            loCol.add(new Color(0.869184f, 0.378317f, 0.300267f));
            loCol.add(new Color(0.795636f, 0.241291f, 0.220523f));
            loCol.add(new Color(0.705669f, 0.0155489f, 0.15024f));
            return loCol;
        }
        
        public static List<Color> getRedToOrange() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(255, 0, 0));
            loCol.add(new Color(255, 50, 0));
            loCol.add(new Color(255, 100, 0));
            loCol.add(new Color(255, 150, 0));
            loCol.add(new Color(255, 200, 0));
            
            return loCol;
        }

        public static List<Color> getColdRainbow() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(43, 140, 190));
            loCol.add(new Color(222, 45, 38));
            return loCol;
        }

        public static List<Color> getColdCutRainbow() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(136, 86, 167));
            loCol.add(new Color(217, 95, 14));
            return loCol;
        }
        
        public static List<Color> getWarmToColdRainbow() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(0, 105, 234));
            loCol.add(new Color(170, 64, 64));
            return loCol;
        } 
        
        public static List<Color> getColdToWarmRainbow() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(43, 140, 190));
            loCol.add(new Color(225, 71, 7));
            return loCol;
        } 
        
        public static List<Color> getColdToWarmRainbow2() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(43, 140, 190));
            loCol.add(Color.GREEN);
            loCol.add(Color.RED);   
            loCol.add(Color.WHITE);
            return loCol;
        }
        
        public static List<Color> getJet() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(43, 140, 190));            
            loCol.add(Color.BLUE);
            loCol.add(Color.CYAN);                     
            loCol.add(Color.WHITE);
            loCol.add(Color.YELLOW);
            loCol.add(Color.RED);
            
            return loCol;
        }
        
        public static List<Color> getBrown() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(198, 114, 67));
            loCol.add(new Color(0, 0, 0));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        }
        
        public static List<Color> getLightBrown() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(156, 79, 32));
            loCol.add(new Color(0, 0, 0));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        }
        
        public static List<Color> getveryLightBrown() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(218, 164, 109));
            loCol.add(new Color(0, 0, 0));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        }
        
        public static List<Color> getLightBlue() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(115, 136, 160));
            loCol.add(new Color(0, 0, 0));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        }
        
        public static List<Color> getdarkGreen() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(118, 102, 33));
            loCol.add(new Color(0, 0, 0));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        }
        
        public static List<Color> getPink() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(254, 119, 125));
            loCol.add(new Color(255, 255, 255));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        }
        
        public static List<Color> getGrey() {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(255, 255, 255));
            loCol.add(new Color(0, 0, 0));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        } 
        
        public static List<Color> getCustom(int R, int G, int B, int R2, int G2, int B2) {
            List<Color> loCol = new ArrayList<Color>();
            loCol.add(new Color(R, G, B));
            loCol.add(new Color(R2, G2, B2));
            //loCol.add(new Color(30, 166, 176));           
            return loCol;
        } 

        public static interface ColorOperation<Double> {

            public Double operate(Double pParameter);
        };

        public static interface ColorStepsGenerator<Object> {

            public List<ColorStartEndWithFraction> generate(List<Color> loColors, ColorSpace oColorSpace);
        };

    }

}
