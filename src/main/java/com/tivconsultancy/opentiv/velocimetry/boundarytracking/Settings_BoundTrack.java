/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.velocimetry.boundarytracking;

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;


/**
 *
 * @author Thomas Ziegenhein
 */
public class Settings_BoundTrack extends Settings {

    public Settings_BoundTrack() {
        super();
        setDefaultSetttings();
    }

    private void setDefaultSetttings() {

        this.loSettings.add(new SettingObject("sPWDIn", "sPWDIn", "", SettingObject.SettingsType.String));  // = "E:\\Work\\openTIV\\BubbleTracking\\N025\\600mm"; // "E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Data\\BubbleCase1";
        this.loSettings.add(new SettingObject("sPWDOut", "sPWDOut", "", SettingObject.SettingsType.String)); // = "E:\\Work\\openTIV\\BubbleTracking\\N025\\600mm"; // "E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Data\\BubbleCase1";
        this.loSettings.add(new SettingObject("sDebugFolder", "sDebugFolder", "Debug", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("sOutputFolder", "sOutputFolder", "Data", SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("EdgeThreshold", "EdgeThreshold", 100, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("cutyTop", "cutyTop", 0, SettingObject.SettingsType.Integer)); //240 ; //220; //110; //30
        this.loSettings.add(new SettingObject("cutyBottom", "cutyBottom", 800, SettingObject.SettingsType.Integer)); //380 ; //300;// 250; //300
        this.loSettings.add(new SettingObject("cutxLeft", "cutxLeft", 50, SettingObject.SettingsType.Integer)); //200 ; //755; //100; //30
        this.loSettings.add(new SettingObject("cutxRight", "cutxRight", 1220, SettingObject.SettingsType.Integer)); //410 ; //840; //280; //300

        //Curv processing
        this.loSettings.add(new SettingObject("iCurvOrder", "iCurvOrder", 5, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("iTangOrder", "iTangOrder", 10, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("dCurvThresh", "dCurvThresh", 0.075, SettingObject.SettingsType.Double));

    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void buildClusters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
