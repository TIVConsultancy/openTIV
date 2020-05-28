/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.highlevel.controller;

import com.tivconsultancy.opentiv.datamodels.overtime.IndexDatabase;
import com.tivconsultancy.opentiv.highlevel.methods.Method;
import com.tivconsultancy.opentiv.datamodels.Results1DPlotAble;
import com.tivconsultancy.opentiv.datamodels.overtime.Database;
import com.tivconsultancy.opentiv.datamodels.overtime.DatabaseWithIndex;
import java.io.File;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Controller {               
    
    public void startNewMethod(Method newMethod);
    public void startNewIndexStep();
    public void setSelectedFile(File f);
    
    public Database getPlotAbleOverTimeResults();
    public Database getDataBase();
    public List<String> getHints(String name);
    public List<File> getInputFiles(String name);
    public Method getCurrentMethod();
    public File getCurrentFileSelected();
    public Results1DPlotAble get1DResults();
    
    public void importSettings(File f);
    public void exportSettings(File f);
    
    public void loadSession(File f);
    public void startNewSession(File f);

    public void runCurrentStep();
    public void run();
    
    
}
