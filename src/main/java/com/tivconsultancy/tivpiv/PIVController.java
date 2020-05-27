/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.tivpiv;

import com.tivconsultancy.opentiv.helpfunctions.io.Reader;
import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.highlevel.methods.Method;
import com.tivconsultancy.opentiv.highlevel.protocols.NameSpaceProtocolResults1D;
import com.tivconsultancy.opentiv.highlevel.protocols.Protocol;
import com.tivconsultancy.opentiv.datamodels.Results1DPlotAble;
import com.tivconsultancy.tivGUI.MainFrame;
import com.tivconsultancy.tivGUI.StaticReferences;
import com.tivconsultancy.tivGUI.controller.BasicController;
import com.tivconsultancy.tivGUI.startup.Database;
import com.tivconsultancy.tivGUI.startup.StartUpSubControllerLog;
import com.tivconsultancy.tivGUI.startup.StartUpSubControllerPlots;
import com.tivconsultancy.tivGUI.startup.StartUpSubControllerViews;
import com.tivconsultancy.tivpiv.data.DataPIV;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.embed.swing.SwingFXUtils;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class PIVController extends BasicController {

    protected File mainFolder;
    protected List<File> ReadInFile;
    protected DataPIV database1Step;    

    public PIVController() {
        currentMethod = new PIVMethod();
        ReadInFile = new ArrayList<>();
        initDatabase();
        initSubControllers();
        createHints(currentMethod);
    }

    private void initSubControllers() {
        subViews = new StartUpSubControllerViews(this);
        subPlots = new StartUpSubControllerPlots();
        subMenu = new tivPIVSubControllerMenu();
        subLog = new StartUpSubControllerLog();
    }

    @Override
    public void startNewSession(File inputFolder) {
        startNewMethod(new PIVMethod());
        if (inputFolder != null && inputFolder.exists()) {
            mainFolder = inputFolder;
            ReadInFile = new ArrayList<>();
            for (File f : mainFolder.listFiles()) {
                if (f.isDirectory()) {
                    continue;
                }
                ReadInFile.add(f);
            }
        }
        mainFrame.startNewSession();
    }

    @Override
    public List<File> getInputFiles(String name) {
        return ReadInFile;
    }

    @Override
    public void setSelectedFile(File f) {
        this.selectedFile = f;
        File nextFile = f;
        if (getNextPic() != null) {
            nextFile = getNextPic();
        }
        this.getCurrentMethod().setFiles(new File[]{f, nextFile});
        try {
            getCurrentMethod().readInFileForView(f);
        } catch (Exception ex) {
            PIVStaticReferences.getlog().log(Level.SEVERE, null, ex);
        }
        subViews.update();
    }

    private File getNextPic() {
        int index = getSelecedIndex();
        if (index >= 0 && (ReadInFile.size() - 1) > index) {
            return ReadInFile.get(index + 1);
        }
        return null;
    }

    private int getSelecedIndex() {
        for (File f : ReadInFile) {
            if (f.getName().equals(selectedFile.getName())) {
                return ReadInFile.indexOf(f);
            }
        }
        return -1;
    }

    @Override
    public void startNewMethod(Method newMethod) {
        currentMethod = newMethod;
        initDatabase();
    }

    @Override
    public void importSettings(File loadFile) {
        try {
            Reader oRead = new Reader(loadFile);
            oRead.setSeperator(";");
            List<String[]> ls = oRead.readFileStringa2();
            for (Protocol p : currentMethod.getProtocols()) {
                p.setFromFile(ls);
            }
        } catch (IOException ioe) {
            StaticReferences.getlog().log(Level.SEVERE, "Cannot load settings file", ioe);
        }
        mainFrame.startNewSettings();
    }

    @Override
    public void exportSettings(File saveFile) {
        List<String> allSettings = new ArrayList<>();
        for (Protocol p : currentMethod.getProtocols()) {
            allSettings.addAll(p.getForFile());
        }

        try {
            Writer oWrite = new Writer(saveFile);
            oWrite.write(allSettings);
        } catch (Exception ioe) {
            StaticReferences.getlog().log(Level.SEVERE, "Cannot save settings", ioe);
        }
    }

    /**
     * Initialization of our two databases, one for our 1 Dimensional results
     * (used to plot graphs) The other one used for internal usage in the PIV
     * algorithms, only valid for one time step
     *
     */
    private void initDatabase() {
        data = new Database<>();        
        database1Step = new DataPIV();
        try {
            data.getIndexedResults().addObjectToRefresh(mainFrame.getPlotArea());
        } catch (Exception e) {
            StaticReferences.getlog().log(Level.SEVERE, "Cannot connect Plot Area to database", e);
        }
    }

    /**
     * Gets the current database for internal use for the current time step
     *
     * @return
     */
    public DataPIV getDataPIV() {
        return database1Step;
    }

    @Override
    public void loadSession(File f) {
        startNewSession(f);
        File settingsFile = null;
        for (File af : f.listFiles()) {
            if (af.getName().contains(".tiv")) {
                settingsFile = af;
                break;
            }
        }
        if(settingsFile == null){
            StaticReferences.getlog().log(Level.SEVERE, "Cannot load Settings file: Not existing .tiv file", new Throwable());
            return;
        }
        try {
            importSettings(settingsFile);
        } catch (Exception ex) {
            StaticReferences.getlog().log(Level.SEVERE, "Cannot load Settings file: " + new File(f, "Settings.tiv").toString() + ex.getMessage(), ex);
        }

    }

    @Override
    public void runCurrentStep() {
        int currentStep = getSelecedIndex();
        new Thread() {
            @Override
            public void run() {
                try {
                    startNewTimeStep();
                    getCurrentMethod().run();
                    data.setRes(currentStep, database1Step);
                    subViews.update();
                } catch (Exception ex) {
                    StaticReferences.getlog().log(Level.SEVERE, "Unable to run : " + ex.getMessage(), ex);
                }
            }
        }.start();
    }

    @Override
    public void run() {
        new Thread() {
            @Override
            public void run() {
                timeline:
                for (int i = 0; i < 10; i++) {
                    startNewTimeStep();
                    try {
                        getCurrentMethod().run();
                        data.setRes(i, database1Step);
                        subViews.update();
                    } catch (Exception ex) {
                        StaticReferences.getlog().log(Level.SEVERE, "Unable to run : " + ex.getMessage(), ex);
                    }
                }
            }
        }.start();
    }

//    protected void createHints(Method newMethod) {
//        hints = new Settings() {
//            @Override
//            public String getType() {
//                return "hints";
//            }
//
//            @Override
//            public void buildClusters() {
//            }
//        };
//        for (Protocol p : newMethod.getProtocols()) {
//            for (SettingsCluster c : p.lsClusters) {
//                for (SettingObject o : c.getSettings()) {
//                    if (p instanceof Prot_PIVInterrAreas && o.getName().contains("PIV_GridType")) {
//                        hints.addSettingsObject(new SettingObject("Grid Type", "PIV_GridType", "Standard", SettingObject.SettingsType.String));
//                        hints.addSettingsObject(new SettingObject("Grid Type", "PIV_GridType", "50Overlap", SettingObject.SettingsType.String));
//                        hints.addSettingsObject(new SettingObject("Grid Type", "PIV_GridType", "PIV_Ziegenhein2018", SettingObject.SettingsType.String));
//                    } else {
//                        if (o.ident.equals(SettingObject.SettingsType.Boolean)) {
//                            hints.addSettingsObject(o);
//                            hints.addSettingsObject(new SettingObject(o.getName(), !((Boolean) o.sValue), SettingObject.SettingsType.Boolean));
//                        } else {
//                            hints.addSettingsObject(o);
//                        }
//                    }
//
//                }
//            }
//        }
//    }

    @Override
    public Results1DPlotAble get1DResults() {
        return database1Step.results1D;
    }

    @Override
    public void startNewTimeStep() {
        database1Step = new DataPIV();
        for (Protocol pro : getCurrentMethod().getProtocols()) {
            for (NameSpaceProtocolResults1D e : pro.get1DResultsNames()) {
                get1DResults().addResult(e.toString(), Double.NaN);
            }
            if (MainFrame.loadGif != null) {
                pro.setImage(SwingFXUtils.fromFXImage(MainFrame.loadGif, null));
            }

        }
    }
}
