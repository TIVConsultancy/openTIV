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
package delete.com.tivconsultancy.opentiv.devgui.mvc_model;

import delete.com.tivconsultancy.opentiv.devgui.feature.Brightness;
import delete.com.tivconsultancy.opentiv.devgui.feature.Contrast;
import delete.com.tivconsultancy.opentiv.devgui.feature.CutX;
import delete.com.tivconsultancy.opentiv.devgui.feature.CutY;
import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import delete.com.tivconsultancy.opentiv.devgui.feature.HGEqualize;
import delete.com.tivconsultancy.opentiv.devgui.feature.MaskingZiegenhein2018;
import delete.com.tivconsultancy.opentiv.devgui.feature.NRSimple1;
import delete.com.tivconsultancy.opentiv.devgui.feature.PreProc_Box3x3;
import delete.com.tivconsultancy.opentiv.devgui.feature.PreProc_Gauss;
import delete.com.tivconsultancy.opentiv.devgui.feature.PreProc_Gauss5x5;
import delete.com.tivconsultancy.opentiv.devgui.feature.Process_ImageStep;
import delete.com.tivconsultancy.opentiv.devgui.feature.Process_Parallel;
import delete.com.tivconsultancy.opentiv.devgui.feature.Process_Sequence;
import delete.com.tivconsultancy.opentiv.devgui.feature.ReadSingleImagesFromHDJPG;
import delete.com.tivconsultancy.opentiv.devgui.feature.ReadSingleImagesFromHDPNG;
import delete.com.tivconsultancy.opentiv.devgui.feature.StretchBlack;
import delete.com.tivconsultancy.opentiv.devgui.feature.StretchWhite;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StandardModel_Features implements Model_Features {

    public static List<Feature> loReadInFeatures = new ArrayList<>();
    public static List<Feature> loTransform = new ArrayList<>();
    public static List<Feature> loHistFeatures = new ArrayList<>();
    public static List<Feature> loSmoothing = new ArrayList<>();
    public static List<Feature> loNR = new ArrayList<>();
    public static List<Feature> loSimpleMasking = new ArrayList<>();
    
    public static List<Feature> loProcess = new ArrayList<>();

    protected Model_Settings oModel_Set;

    public StandardModel_Features(Model_Settings oModel_Set) {
        this.oModel_Set = oModel_Set;
        init();
    }

    private void init() {
        loReadInFeatures.add(new ReadSingleImagesFromHDJPG(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.ReadIn)));
        loReadInFeatures.add(new ReadSingleImagesFromHDPNG(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.ReadIn)));

        loTransform.add(new CutX(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loTransform.add(new CutY(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));

        loHistFeatures.add(new Brightness(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loHistFeatures.add(new Contrast(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loHistFeatures.add(new HGEqualize(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loHistFeatures.add(new StretchWhite(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loHistFeatures.add(new StretchBlack(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));

        loSmoothing.add(new PreProc_Gauss(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loSmoothing.add(new PreProc_Gauss5x5(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loSmoothing.add(new PreProc_Box3x3(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));

        loNR.add(new NRSimple1(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));

//        loSimpleMasking.add(new MaskingCircle());
//        loSimpleMasking.add(new MaskingRhombus());
//        loSimpleMasking.add(new MaskingSquare());
        loSimpleMasking.add(new MaskingZiegenhein2018(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.Mask)));
        
        loProcess.add(new Process_ImageStep(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loProcess.add(new Process_Parallel(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
        loProcess.add(new Process_Sequence(oModel_Set.getSettings(StandardModel_Settings.StandardSettingNames.PreProc)));
    }

    @Override
    public List<Feature> getFeatures(Enum featureName) {
        if (featureName.equals(StandardEnum.ReadIn)) {
            return loReadInFeatures;
        }
        if (featureName.equals(StandardEnum.Transform)) {
            return loTransform;
        }
        if (featureName.equals(StandardEnum.Hist)) {
            return loHistFeatures;
        }
        if (featureName.equals(StandardEnum.Smoothing)) {
            return loSmoothing;
        }
        if (featureName.equals(StandardEnum.NR)) {
            return loNR;
        }
        if (featureName.equals(StandardEnum.Mask)) {
            return loSimpleMasking;
        }
        return null;
    }

    @Override
    public List<Feature> getAllFeatures() {
        List<Feature> AllFeatures = new ArrayList<>();
        AllFeatures.addAll(loHistFeatures);
        AllFeatures.addAll(loNR);
        AllFeatures.addAll(loReadInFeatures);
        AllFeatures.addAll(loSimpleMasking);
        AllFeatures.addAll(loSmoothing);
        AllFeatures.addAll(loTransform);
        return AllFeatures;
    }    

}
