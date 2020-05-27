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
package delete.com.tivconsultancy.opentiv.devgui.main;

import delete.com.tivconsultancy.opentiv.devgui.frames.Frame_Main;
import delete.com.tivconsultancy.opentiv.devgui.frames.Processes;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Features;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_FolderStructure;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Save;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Settings;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardEnum;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Settings.StandardSettingNames;

import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.masking.data.SettingsMasking;
import com.tivconsultancy.opentiv.preprocessor.SettingsPreProc;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingWorker;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Images;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class Processes_StandardBackbone extends SwingWorker<Object, Object> {

    File oFile;
    Enum Ident;
    Model_Settings ModelSettings;
    Model_Images ModelData;
    Model_Features ModelFeatures;
    Model_FolderStructure ModelFolderStruc;
    Model_Save ModelSave;
    Frame_Main MainFrame;

    public Processes_StandardBackbone(File oFile, Enum sIdent, Frame_Main MainFrame, Model_Settings ModelSettings, Model_Images ModelData, Model_FolderStructure ModelFolderStruc, Model_Save ModelSave) {
        this.oFile = oFile;
        this.Ident = sIdent;
        this.MainFrame = MainFrame;
        this.ModelSettings = ModelSettings;
        this.ModelData = ModelData;
        this.ModelFolderStruc = ModelFolderStruc;
        this.ModelSave = ModelSave;
        this.execute();
    }

    @Override
    protected Object doInBackground() throws Exception {
        boolean bRundTransform = false;
        boolean bRunPreproc = false;
        boolean bRundMasking = false;
        ImageInt oReadIn = null;

        if (oFile != null) {
            oReadIn = new ImageInt(IMG_Reader.readImageGrayScale(oFile));
            if (ModelData.getImage(StandardEnum.ReadIn) != null) {
                ModelData.replaceImage(oReadIn.clone(), StandardEnum.ReadIn);
            } else {
                ModelData.addImage(oReadIn.clone(), StandardEnum.ReadIn);
            }
        }

        if (Ident == null) {
            return "";
        }

        if (Ident.equals(StandardEnum.ReadIn) || Ident.equals(StandardEnum.Transform)) {
            bRundTransform = true;
            bRunPreproc = true;
            bRundMasking = true;
            ModelData.replaceImage(oReadIn.clone(), StandardEnum.Transform);
            ModelData.replaceImage(oReadIn.clone(), StandardEnum.PreProcessor);
            ModelData.replaceImage(oReadIn.clone(), StandardEnum.Mask);
        }

        if (Ident.equals(StandardEnum.PreProcessor) || Ident.equals(StandardEnum.Hist) || Ident.equals(StandardEnum.Smoothing) || Ident.equals(StandardEnum.NR)) {
            if (ModelData.getImage(StandardEnum.Transform) == null) {
                bRundTransform = true;
            }
            bRunPreproc = true;
            bRundMasking = true;

            ModelData.replaceImage(oReadIn.clone(), StandardEnum.PreProcessor);
            ModelData.replaceImage(oReadIn.clone(), StandardEnum.Mask);
        }

        if (Ident.equals(StandardEnum.Mask)) {
            if (ModelData.getImage(StandardEnum.Transform) == null) {
                bRundTransform = true;
            }
            if (ModelData.getImage(StandardEnum.PreProcessor) == null) {
                bRunPreproc = true;
            }
            bRundMasking = true;
            ModelData.replaceImage(oReadIn.clone(), StandardEnum.Mask);
        }

        if (bRundTransform) {

            if (isCancelled()) {
                return "";
            }
            ModelData.replaceImage(Processes.Transformation(oFile, oReadIn, (SettingsPreProc) ModelSettings.getSettings(StandardSettingNames.PreProc)), StandardEnum.Transform);
            TimeUnit.MILLISECONDS.sleep(50);
        }

        if (bRunPreproc) {

            if (isCancelled()) {
                return "";
            }
            if (!MainFrame.getControlFrame().PreProc.oNR.jLM.isEmpty()
                    || !MainFrame.getControlFrame().PreProc.oHistPanel.jLM.isEmpty()
                    || !MainFrame.getControlFrame().PreProc.oSmoothing.jLM.isEmpty()) {
                MainFrame.getDataModel().replaceImage(
                        Processes.PreProcessor(MainFrame.getDataModel().getImage(
                                        StandardEnum.Transform), (SettingsPreProc) MainFrame.getSettingsModel().getSettings(StandardSettingNames.PreProc)), StandardEnum.PreProcessor);
            }
            TimeUnit.MILLISECONDS.sleep(50);

//            Processes.updateHist(Data.GUI_Data.Main.oPIVFrame.PreProc.oHist, Data.GUI_Data.PreProcEnd, null);
        }
        if (bRundMasking) {
            if (isCancelled()) {
                return "";
            }
            if (!MainFrame.getControlFrame().Mask.oSimpleMasking.jLM.isEmpty()) {
                if (MainFrame.getDataModel().getImage(StandardEnum.PreProcessor) == null) {
                    MainFrame.getDataModel().addImage(MainFrame.getDataModel().getImage(StandardEnum.Transform).clone(), StandardEnum.PreProcessor);
                }

                Integer iLeap = 1;

                iLeap = Integer.valueOf((String) MainFrame.getSettingsModel().getSettings(StandardSettingNames.Process).getSettingsValue("Leap"));

                ImageInt PreProc = MainFrame.getDataModel().getImage(StandardEnum.PreProcessor);
                SettingsMasking oSetMask = (SettingsMasking) MainFrame.getSettingsModel().getSettings(StandardEnum.Mask);
                ImageInt Mask = Processes.masking(PreProc, MainFrame.getImageViewFrame().jListImages.getSelectedIndex() + iLeap, oSetMask);
                MainFrame.getDataModel().replaceImage(Mask, StandardEnum.Mask);
            }
            TimeUnit.MILLISECONDS.sleep(50);

//                publish(new String[]{"4", "start"});
//                if (!Data.PIVGUI_Data.Main.oPIVFrame.PIVPre.oPIVPreProc.jLM.isEmpty()) {
//                    Data.PIVGUI_Data.oPIVGrid = Processes.getGrid(Data.PIVGUI_Data.PreProcEnd);
//                    PIVGUI_Data.PIVGrid = Processes.checkPIVMasking(Data.PIVGUI_Data.oPIVGrid, Data.PIVGUI_Data.PreProcEnd, Data.PIVGUI_Data.MaskingEnd); 
//                }
//                publish(new String[]{"4", "end"});
//                TimeUnit.MILLISECONDS.sleep(10);
//                publish(new String[]{"5", "start"});
//                Processes.processPIV(Data.PIVGUI_Data.PIVGrid, Data.PIVGUI_Data.MaskingEnd);
//                publish(new String[]{"5", "end"});
//                TimeUnit.MILLISECONDS.sleep(10);
        }
                
        MainFrame.getImageViewFrame().updateFromRecalc(Ident);
        MainFrame.getImageViewFrame().updateCurrentView();
        return null;        
    }

}
