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
package delete.com.tivconsultancy.opentiv.devgui.datacontainer;

import delete.com.tivconsultancy.opentiv.devgui.main.Controller;
import delete.com.tivconsultancy.opentiv.devgui.frames.StandardFrame_Main;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Settings;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;

/**
 *
 * @author Thomas Ziegenhein
 */
public final class GUI_Data {

    //Working Directory
//    public static String sOUT = "";
    
    //Features
//    public static FeatureContainer oFeatures = new FeatureContainer();
//    //Frames
//    public static StandardFrame_Main Main = new StandardFrame_Main();
//    //Controler
//    public static Controller oController = new Controller();
//    //Model
//    public static StandardModel_Settings oModel = new StandardModel_Settings();
//
//    //Images to show
//    //General
//    public static ImageInt Input;
//    public static ImageInt TransEnd;
//    public static ImageInt PreProcEnd;
//    public static ImageInt MaskingEnd;
//    //PIV
//    public static ImageInt PIVGrid;
//    public static ImageInt PIVVectors;
//    //ContourDetecting
//    public static ImageInt CD_Edges;
//    public static ImageInt CD_ShapeFit;

    //Backgroudn Processes
//    public static Thread tPreProcIMGLoader;
//    public static Thread tTransform;
//    public static Thread tPaintHist;
//    public static Thread tMasking;
//
//    public static Thread ImageProcessing;

    //Process Handlers
    //PIV
//    public static ProcessHandler_PIV PIV_Processes = null;
//    public static ProcessAllPictures_PIV PIV_AllPics_Process = null;
//    //ContourDetect
//    public static ProcessHandler_ContourDetect CD_Processes = null;        
//    public static ProcessAllPictures_CD CD_AllPics_Process = null;

    //Folder Structure
//    public static String sSettings = "Settings";
//    public static String sDebug = "Debug";
//    public static String sPictures = "Pictures";
//    public static String sData = "Data";
//    
    //Data
//    public static InterrGrid oPIVGrid;
//    public static List<Circle> loEllipseFit;
    
    //Other
//    public static int iGreyValueVec = 0;
    
//    public static void allocate(int iLength, int jLength){
////        Input = new ImageGrid(iLength, jLength);
//        TransEnd = new ImageGrid(iLength, jLength);
//        PreProcEnd = new ImageGrid(iLength, jLength);
//        MaskingEnd = new ImageGrid(iLength, jLength);
//        PIVGrid = new ImageGrid(iLength, jLength);
//        PIVVectors = new ImageGrid(iLength, jLength);
//    }
    
//    public static void saveState(List<Feature> loAllFeatures) {
//
//        if(sOUT == null || sOUT.isEmpty()) return;
//        
////        List<Feature> loAllFeatures = new ArrayList<>();
//
//        //PIV        
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.ReadIn.oReadIn.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.ReadIn.oTransform.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.PreProc.oHistPanel.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.PreProc.oSmoothing.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.PreProc.oNR.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.Mask.oSimpleMasking.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.PIVPre.oPIVPreProc.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.PIVProc.oPIV.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.PIVProc.oDispAndVali.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oPIVFrame.PIVProc.oProcess.getAllFeatures());
//////        
//////        //Contour
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.ReadIn.oReadIn.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.ReadIn.oTransform.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.PreProc.oHistPanel.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.PreProc.oSmoothing.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.PreProc.oNR.getAllFeatures());
//////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.Mask.oSimpleMasking.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.EdgeDet.oEdgeDetector.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.EdgeDet.oEdgeOperation.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.EllipseFit.oEllipseFitting.getAllFeatures());
////        loAllFeatures.addAll(GUI_Data.Main.oContourDetectFrame.EllipseFit.oEllipseFilter.getAllFeatures());
//        
//        
//        Set<Feature> loAllFeaturesHash = new LinkedHashSet<>(loAllFeatures);
//        loAllFeatures.clear();
//        loAllFeatures.addAll(loAllFeaturesHash);        
//
//        try {
//            FileOutputStream fos = new FileOutputStream(GUI_Data.sOUT + java.io.File.separator + GUI_Data.sSettings + java.io.File.separator + "Features.gtt");
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(loAllFeatures);
//            oos.close();
//            fos.close();
//        } catch (Exception ioe) {
//            ioe.printStackTrace();
//        }
//
//        List<SettingObject> loAllSettings = new ArrayList<>();
//        for(Settings oSet : oController.getAllSettings()){
//            loAllSettings.addAll(oSet.getOutput());
//        }
//        if(oController.getSettings(Controller.SettingNames.EdgeDetect) != null){
//            oController.getSettings(Controller.SettingNames.EdgeDetect).setOrder(GUI_Data.Main.oContourDetectFrame.EdgeDet.oEdgeOperation.getAllFeatures());
//        }
//        
//        
//        try {
//            FileOutputStream fos = new FileOutputStream(GUI_Data.sOUT + java.io.File.separator + GUI_Data.sSettings + java.io.File.separator + "Settings.gtt");
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(loAllSettings);
//            oos.close();
//            fos.close();
//        } catch (Exception ioe) {
//            ioe.printStackTrace();
//        }
//
//    }

}
