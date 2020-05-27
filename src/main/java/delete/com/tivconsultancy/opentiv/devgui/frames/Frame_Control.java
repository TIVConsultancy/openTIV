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
package delete.com.tivconsultancy.opentiv.devgui.frames;

import delete.com.tivconsultancy.opentiv.devgui.main.StatusBar;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Thomas Ziegenhein
 */
public class Frame_Control extends JInternalFrame {

    protected int iWidth = 300;
    protected int iHeight = 730;

    JTabbedPane MainTab;

    public Panel_PreProc PreProc;
    public Panel_ReadIn ReadIn;
    public Panel_Masking Mask;
    
    Frame_Main MainFrame;
    
    public Frame_Control(Frame_Main MainFrame) {
        super("PIV", false, true);
        this.MainFrame = MainFrame;
        this.setLocation((int) (MainFrame.getContentPane_MainFrame().getWidth() - iWidth - 20), 50);
        setResizable(false);
        setMaximizable(false);
        setIconifiable(true);
        setEnabled(true);
        this.setLayer(1);
        
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                return;
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                cleanup();
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                cleanup();
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                return;
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                return;
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                return;
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                return;
            }
        });
        

        MainTab = new JTabbedPane();
        MainTab.setPreferredSize(new Dimension(iWidth, iHeight));

        ReadIn = new Panel_ReadIn("Default", MainFrame);
        MainTab.addTab("Read", ReadIn);

        PreProc = new Panel_PreProc("Default", MainFrame);
        MainTab.addTab("Pre", PreProc);

        Mask = new Panel_Masking("Default", MainFrame);
        MainTab.addTab("Mask", Mask);

        JLabel jText = new JLabel("Test2");
        jText.setPreferredSize(new Dimension(20, 10));

        StatusBar jText2 = new StatusBar("test");
        jText2.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY),
                                            new EmptyBorder(4, 4, 10, 10)));

        add(MainTab);
        setVisible(false);
        pack();

        jText2.updateStatus(0.999);

    }

    public void cleanup() {        
        ReadIn.cleanup();
        MainFrame.getImageViewFrame().cleanUp();
        PreProc.cleanup();        
        Mask.cleanup();
    }

//    public void recalc(String sIdent) {
//
//        if (sIdent.equals(Data.CONSTANTS.PTRANSF)) {
////            TransFormIMG oTransCalc = new TransFormIMG(null);
////            Data.PIVGUI_Data.tTransform = new Thread(oTransCalc);
////            Data.PIVGUI_Data.tTransform.start();
//            Data.PIVGUI_Data.PIV_Processes.run(null, Data.CONSTANTS.PTRANSF);
//        }     
//        
//        if (sIdent.equals(Data.CONSTANTS.PREPROC) || sIdent.equals(Data.CONSTANTS.PHIST) || sIdent.equals(Data.CONSTANTS.PSMOTH) || sIdent.equals(Data.CONSTANTS.PNR)) {            
//                Data.PIVGUI_Data.PIV_Processes.run(null, Data.CONSTANTS.PREPROC);
//////            int iIndex = Data.PIVGUI_Data.Main.oImageViewer.jListModelLayers.indexOf(Data.CONSTANTS.PREPROC);
//////            Data.PIVGUI_Data.Main.oImageViewer.jListModelLayers.set(iIndex, "Running ....");
////            PreProcIMGLoader oLoader = new PreProcIMGLoader(null);
////            Data.PIVGUI_Data.tPreProcIMGLoader = new Thread(oLoader);
////            Data.PIVGUI_Data.tPreProcIMGLoader.start();
//////            Data.PIVGUI_Data.Main.oImageViewer.jListModelLayers.set(iIndex, Data.CONSTANTS.PREPROC);
//        }
//           
//        if (sIdent.equals(Data.CONSTANTS.PMasking)) {
//            Data.PIVGUI_Data.PIV_Processes.run(null, Data.CONSTANTS.PMasking);
////            MaskLoader oMasking = new MaskLoader(null);
////            Data.PIVGUI_Data.tMasking = new Thread(oMasking);
////            Data.PIVGUI_Data.tMasking.start();
//        }
//
////        if(Data.PIVGUI_Data.PreProc == null){
////            Data.PIVGUI_Data.PreProc = Data.PIVGUI_Data.Main.oImageViewer.getPresentImage();
////            if(Data.PIVGUI_Data.PreProc == null) return;
////        }
////        if(sIdent.equals("Histogram")){
////            Data.PIVGUI_Data.PreProc = opentiv_preproc.OpenTIV_PreProc.Histogram(Data.PIVGUI_Data.PreProc, AllSettingsContainer.oSetPre);
////        }
////        
////        if(sIdent.equals("Smoothing")){
////            Data.PIVGUI_Data.PreProc = opentiv_preproc.OpenTIV_PreProc.smoothing(Data.PIVGUI_Data.PreProc, AllSettingsContainer.oSetPre);
////        }
////        
////        if(sIdent.equals("NR")){
////            Data.PIVGUI_Data.PreProc = opentiv_preproc.OpenTIV_PreProc.reducenoise(Data.PIVGUI_Data.PreProc, AllSettingsContainer.oSetPre);
////        }
//    }
}
