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
package com.tivconsultancy.opentiv.preprocessor;

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsCluster;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class SettingsPreProc extends Settings implements Serializable{

    private static final long serialVersionUID = -6649328924847991538L;

    public SettingsPreProc() {
        /*
         ____ Abbrevations
         NR : Noise Reduction
         SF : Smoothing Filter
         HG : Histogram
         */
        this.loSettings.add(new SettingObject("NRSimple1", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("NRSimple1Threshold", 50, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("NRType", "Simple1", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("SFGauss", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("SF5x5Gauss", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("SF3x3Box", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("SFType", "Gauss", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("HGType", "Brightness", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("HGBrightness", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("Brightness", 0, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("HGType", "Equalize", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("HGEqualize", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("Equalize", 255, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("HGType", "Contrast", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("HGContrast", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("BlackMin", 0, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("WhiteMax", 255, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("HGType", "BlackStretch", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("HGBlackStretch", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("BlackStretchFactor", 1.0, SettingObject.SettingsType.Double));
        this.loSettings.add(new SettingObject("HGType", "WhiteStretch", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("HGWhiteStretch", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("WhiteStretchFactor", 1.0, SettingObject.SettingsType.Double));

        this.loSettings.add(new SettingObject("BcutyTop", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("cutyTop", 0, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("BcutyBottom", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("cutyBottom", 10, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("BcutxLeft", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("cutxLeft", 0, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("BcutxRight", false, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("cutxRight", 10, SettingObject.SettingsType.Integer));
        buildClusters();

    }

    @Override
    public String getType() {
        return "Image Pre Processing";
    }

    @Override
    public void buildClusters() {
        SettingsCluster NR1 = new SettingsCluster("Noise Reduction", new String[]{"NRSimple1", "NRSimple1Threshold", "NRType"}, this);
        NR1.setDescription("Simple noise reduction that are based on a threshold. \n NRSimple1: Turn on/off \n NRSimple1Threshold: Threshold \n NRType: Type of noise reduction (Available: Simple1) ");
        lsClusters.add(NR1);

        SettingsCluster IMGFilter = new SettingsCluster("Image Filter", new String[]{"SFGauss", "SF5x5Gauss", "SF3x3Box"}, this);
        IMGFilter.setDescription("Standard image filters of different sizes");
        lsClusters.add(IMGFilter);

        SettingsCluster Brightness = new SettingsCluster("Brightness", new String[]{"HGBrightness", "Brightness"}, this);
        Brightness.setDescription("Shifts the brighntess +- to the value");
        lsClusters.add(Brightness);

        SettingsCluster Equalize = new SettingsCluster("Equalize", new String[]{"HGEqualize", "Equalize"}, this);
        Equalize.setDescription("Equalizes the histogram to the given maximum value");
        lsClusters.add(Equalize);

        SettingsCluster Contrast = new SettingsCluster("Contrast", new String[]{"HGContrast", "BlackMin", "WhiteMax"}, this);
        Contrast.setDescription("Increase the contrast by normalizing to the possible black and white values");
        lsClusters.add(Contrast);

        SettingsCluster HGBlackStretch = new SettingsCluster("Black Stretch", new String[]{"HGBlackStretch", "BlackStretchFactor"}, this);
        HGBlackStretch.setDescription("Stretches the histogram towards black values by the given factor");
        lsClusters.add(HGBlackStretch);

        SettingsCluster HGWhiteStretch = new SettingsCluster("White Stretch", new String[]{"HGWhiteStretch", "WhiteStretchFactor"}, this);
        HGWhiteStretch.setDescription("Stretches the histogram towards black values by the given factor");
        lsClusters.add(HGWhiteStretch);

        SettingsCluster CutImage = new SettingsCluster("Cut Image", new String[]{"BcutyTop", "cutyTop", "BcutyBottom", "cutyBottom", "BcutxLeft", "cutxLeft", "BcutxRight", "cutxRight"}, this);
        CutImage.setDescription("Cut image");
        lsClusters.add(CutImage);
    }

}
