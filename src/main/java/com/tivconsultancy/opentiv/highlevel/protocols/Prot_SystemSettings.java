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
package com.tivconsultancy.opentiv.highlevel.protocols;

import com.tivconsultancy.opentiv.helpfunctions.settings.FactorySettingsCluster;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import static com.tivconsultancy.opentiv.highlevel.protocols.Prot_SystemSettings.systemSettings.HDD;
import static com.tivconsultancy.opentiv.highlevel.protocols.Prot_SystemSettings.systemSettings.HEAP;
import static com.tivconsultancy.opentiv.highlevel.protocols.Prot_SystemSettings.systemSettings.RAM;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class Prot_SystemSettings extends Protocol implements Serializable {

    private String name = "System";

    public Prot_SystemSettings(String name) {
        this();
        this.name = name;
    }

    public Prot_SystemSettings() {
        super();
        initSettings();
        buildClusters();
    }

    @Override
    public NameSpaceProtocolResults1D[] get1DResultsNames() {
        return new NameSpaceProtocolResults1D[0];
    }

    @Override
    public List<String> getIdentForViews() {
        return new ArrayList<>();
    }

    @Override
    public BufferedImage getView(String identFromViewer) {
        return null;
    }

    @Override
    public Double getOverTimesResult(NameSpaceProtocolResults1D ident) {
        return null;
    }

    @Override
    public void run(Object... input) throws UnableToRunException {
    }

    @Override
    public String getType() {
        return name;
    }

    private void initSettings() {
//        this.loSettings.add(new SettingObject("Activated", "SimpleEdges", true, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("Store Temp Data", "tivGUI_dataStore", true, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("Draw Final Result", "tivGUI_dataDraw", true, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("Storeage option", "tivGUI_Storeage", HEAP.toString(), SettingObject.SettingsType.String));
    }

    @Override
    public void buildClusters() {
        lsClusters.clear();
        lsClusters.add(FactorySettingsCluster.
                getStandardCluster("Data",
                                   new String[]{"tivGUI_dataStore","tivGUI_dataDraw"},
                                   "Temporary Data Storeage ", this));
    }

    @Override
    public Object[] getResults() {
        return null;
    }

    @Override
    public void setImage(BufferedImage bi) {
    }
    
    public List<SettingObject> getHints() {
        List<SettingObject> ls = super.getHints();
        ls.add(new SettingObject("Colorbar", "tivGUI_Storeage", HEAP.toString(), SettingObject.SettingsType.String));
        ls.add(new SettingObject("Colorbar", "tivGUI_Storeage", RAM.toString(), SettingObject.SettingsType.String));
        ls.add(new SettingObject("Colorbar", "tivGUI_Storeage", HDD.toString(), SettingObject.SettingsType.String));
        return ls;
    }
    
    public enum systemSettings{
        HEAP, RAM, HDD
    }

}
