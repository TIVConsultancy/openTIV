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

import delete.com.tivconsultancy.opentiv.devgui.main.ImagePath;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Features;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_FolderStructure;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Save;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Settings;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardEnum;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Data;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Features;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_FolderStructure;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Save;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.StandardModel_Settings;
import delete.com.tivconsultancy.opentiv.devgui.main.Processes_StandardBackbone;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.logging.TIVLog;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Images;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StandardFrame_Main extends javax.swing.JFrame implements Frame_Main {

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu fileOptions;
    javax.swing.JDesktopPane jDesktopPaneMain;
    protected Frame_StartUp jInternalFrameStartUp = null;
    protected Frame_Feature oFeatureFrame = null;
    protected StandardFrame_ImageView oImageViewer = null;
    protected Frame_Control oControl = null;

    protected Model_Images ModelData;
    protected Model_Settings ModelSettings;
    protected Model_Save ModelSave;
    protected Model_FolderStructure ModelFolders;
    protected Model_Features ModelFeatures;

    public List<Object> ModelLayers = new ArrayList<>();
    public Processes_StandardBackbone backbone;
    
    Frame_SessionStart oSessionStart;

    Frame_SettingsView oSettingsFrame;
    
    //PIV
//    public Frame_PIV oPIVFrame = null;
//    public Frame_PIVStart oStartPIV = null;
//    //Contour
//    public Frame_ContourDetect oContourDetectFrame = null;
//    public Frame_ContourDetectStart oStartContourDetect = null;
    public StandardFrame_Main() {
        jInternalFrameStartUp = new Frame_StartUp();
        jInternalFrameStartUp.setVisible(true);
        getContentPane().add(jInternalFrameStartUp);
        jInternalFrameStartUp.setLocation(getContentPane().getWidth() / 2 - jInternalFrameStartUp.getWidth() / 2, 100);
        initModel();
        createImageLayers();
        setLookAndFeel();
        initOuterFrame();
        initComponents();
        initMenuBar();
//        jInternalFrameStartUp = new Frame_StartUp();
//        jInternalFrameStartUp.setVisible(true);
//        getContentPane().add(jInternalFrameStartUp);
//        jInternalFrameStartUp.setLocation(getContentPane().getWidth() / 2 - jInternalFrameStartUp.getWidth() / 2, 100);

        jInternalFrameStartUp.setVisible(false);
//        setLayout();
    }

    public void initModel() {
        ModelData = new StandardModel_Data();
        ModelSettings = new StandardModel_Settings();        
        ModelSave = new StandardModel_Save();
        ModelFolders = new StandardModel_FolderStructure(null);
        ModelFeatures = new StandardModel_Features(ModelSettings);
    }

    private void initComponents() {        
        initControlFrame();
        initFeatureFrame();
        initImageViewer();
        initStartFrames();
        initOptionsFrames();
    }
    
    public void initOptionsFrames(){
        oSettingsFrame = new Frame_SettingsView(this);
        this.getContentPane().add(oSettingsFrame);
    }
    
    private void initControlFrame(){
        oControl = new Frame_Control(this);
        this.getContentPane().add(oControl);
    }

    private void initImageViewer() {
        oImageViewer = new StandardFrame_ImageView(this);
        getContentPane().add(oImageViewer);
        try {
            oImageViewer.setIcon(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(StandardFrame_ImageView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initStartFrames() {
        oSessionStart = new Frame_SessionStart(this);
        getContentPane().add(oSessionStart);        
//        oStartPIV = new Frame_PIVStart();
//        getContentPane().add(oStartPIV);
//        oStartContourDetect = new Frame_ContourDetectStart();
//        getContentPane().add(oStartContourDetect);
    }

    public void open() {
        initComponents();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(StandardFrame_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        jInternalFrameStartUp.setVisible(false);
    }

//    public final void intiPIVInternalFrame() {
//        oPIVFrame = new Frame_PIV();
//        getContentPane().add(oPIVFrame);
////        jDesktopPaneMain.setLayer(jInternalFrame1, javax.swing.JLayeredPane.DEFAULT_LAYER);
//    }
//    
//    public final void initContourDetectInternalFrame() {
//        oContourDetectFrame = new Frame_ContourDetect();
//        getContentPane().add(oContourDetectFrame);
////        jDesktopPaneMain.setLayer(jInternalFrame1, javax.swing.JLayeredPane.DEFAULT_LAYER);
//    }
    public final void initFeatureFrame() {
        oFeatureFrame = new Frame_Feature(this);
        getContentPane().add(oFeatureFrame);
    }

    public JTabbedPane getPreProcPane() {
        JTabbedPane PreProc = new JTabbedPane();
        return PreProc;
    }

    public final void setLookAndFeel() {
        boolean bNimbus = true;
        try {
            if (bNimbus) {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } else {
//                UIManager.setLookAndFeel(new DarculaLaf());
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot set look and feel" + (new Throwable().getStackTrace()[0].getMethodName()) + ex.getLocalizedMessage(), ex);
        }
    }

    public final void initOuterFrame() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(inset, inset,
                  screenSize.width - inset * 2,
                  screenSize.height - inset * 2);

        jDesktopPaneMain = new javax.swing.JDesktopPane();
        jDesktopPaneMain.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        jDesktopPaneMain.setSize(screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(jDesktopPaneMain);

        setVisible(true);

    }

    public void setLayout() {
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGap(0, 400, Short.MAX_VALUE)
//        );
//        layout.setVerticalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGap(0, 279, Short.MAX_VALUE)
//        );
//
//        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
//        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
//        jInternalFrame1Layout.setHorizontalGroup(
//                jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGap(0, 337, Short.MAX_VALUE)
//        );
//        jInternalFrame1Layout.setVerticalGroup(
//                jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGap(0, 181, Short.MAX_VALUE)
//        );
//
//        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPaneMain);
//        jDesktopPaneMain.setLayout(jDesktopPane1Layout);
//        jDesktopPane1Layout.setHorizontalGroup(
//                jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGroup(jDesktopPane1Layout.createSequentialGroup()
//                        .addGap(224, 224, 224)
//                        .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                        .addContainerGap(211, Short.MAX_VALUE))
//        );
//        jDesktopPane1Layout.setVerticalGroup(
//                jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGroup(jDesktopPane1Layout.createSequentialGroup()
//                        .addGap(117, 117, 117)
//                        .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                        .addContainerGap(140, Short.MAX_VALUE))
//        );

    }

    @Override
    public Frame_Feature getFeatureFrame() {
        return oFeatureFrame;
    }

    @Override
    public StandardFrame_ImageView getImageViewFrame() {
        return oImageViewer;
    }

    @Override
    public Model_Images getDataModel() {
        return ModelData;
    }

    @Override
    public Model_Settings getSettingsModel() {
        return ModelSettings;
    }

    @Override
    public void recalc(Enum Ident) {
        File oFile =((ImagePath) this.getImageViewFrame().jListImages.getSelectedValue()).o;
        backbone = new Processes_StandardBackbone(oFile, Ident, this, ModelSettings, ModelData, ModelFolders, ModelSave);
    }

    public final void initMenuBar() {

        // Menu bar
        menuBar = new javax.swing.JMenuBar();
        setJMenuBar(menuBar);

        fileMenu = new javax.swing.JMenu();
        JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        exitMenuItem.addActionListener((event) -> System.exit(0));
        JMenuItem MenueItemSessionStart = new javax.swing.JMenuItem();
        MenueItemSessionStart.addActionListener((event) -> oSessionStart.setVisible(true));
//        JMenuItem MenueItemContourSession = new javax.swing.JMenuItem();
//        MenueItemContourSession.addActionListener((event) -> oStartContourDetect.setVisible(true));

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");
        
        MenueItemSessionStart.setMnemonic('S');
        MenueItemSessionStart.setText("Standard");

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");


        fileMenu.add(exitMenuItem);
        fileMenu.add(MenueItemSessionStart);
        
        fileOptions = new javax.swing.JMenu();
        JMenuItem OptionsSettings = new javax.swing.JMenuItem();
        OptionsSettings.addActionListener((event) -> oSettingsFrame.setVisible(true));
//        JMenuItem MenueItemContourSession = new javax.swing.JMenuItem();
//        MenueItemContourSession.addActionListener((event) -> oStartContourDetect.setVisible(true));

        fileOptions.setMnemonic('s');
        fileOptions.setText("Options");
        
        OptionsSettings.setMnemonic('s');
        OptionsSettings.setText("Settings");


        fileOptions.add(OptionsSettings);  

        menuBar.add(fileMenu);
        menuBar.add(fileOptions);

    }

    public void initFileMenue() {

    }

    @Override
    public Model_Features getFeatureModel() {
        return ModelFeatures;
    }

    private void createImageLayers() {
        ModelLayers.add(StandardEnum.ReadIn);
        ModelLayers.add(StandardEnum.Transform);
        ModelLayers.add(StandardEnum.PreProcessor);
        ModelLayers.add(StandardEnum.Mask);
    }

    @Override
    public List<Object> getImageLayers() {
        return ModelLayers;
    }

    @Override
    public SwingWorker getBackgroundTask() {
        return backbone;
    }

    @Override
    public ImageGrid readPic() {

        Settings oSetPreProc = ModelSettings.getSettings(StandardModel_Settings.StandardSettingNames.PreProc);

        int cutyTop = (int) oSetPreProc.getSettingsValue("cutyTop");
        int cutyBottom = (int) oSetPreProc.getSettingsValue("cutyBottom");
        int cutxLeft = (int) oSetPreProc.getSettingsValue("cutxLeft");
        int cutxRight = (int) oSetPreProc.getSettingsValue("cutxRight");

        return null;
//        ImageGrid o = IMG_Reader.readImageGrey(new File(sFile));
//
//        o = o.getsubY(cutyTop, cutyBottom);
//        o = o.getsubX(cutxLeft, cutxRight);
//        return o;

    }

    @Override
    public Container getContentPane_MainFrame() {
        return getContentPane();
    }

    @Override
    public void cancelAllProcesses() {
        if (backbone != null) {
            backbone.cancel(true);
            backbone = null;
        }

    }

    @Override
    public Model_FolderStructure getFoldersModel() {
        return ModelFolders;
    }

    @Override
    public Model_Save getSaveModel() {
        return ModelSave;
    }

    @Override
    public Frame_Control getControlFrame() {
        return oControl;
    }

}
