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
package com.tivconsultancy.opentiv.helpfunctions.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public abstract class Settings implements Serializable{

    private static final long serialVersionUID = -3299716352828881633L;

    public List<SettingObject> loSettings = new ArrayList<>();
    public List<SettingsCluster> lsClusters = new ArrayList<>();

    public Settings() {
        setDefaultSetttings();
    }

    private void setDefaultSetttings() {
    }

    public List<SettingObject> getAllSettings() {
        return loSettings;
    }

    public void addSettingsObject(SettingObject o) {
        this.loSettings.add(o);
    }

    public final Object getSettingsValue(String sSettingName) {
        Object o = null;
        for (SettingObject oS : loSettings) {
            o = oS.getValue(sSettingName);
            if (o != null) {
                return o;
            }
        }
//        System.out.println("Warning!! No Setting found for: " + sSettingName);
        return o;
    }

    public final SettingObject getSettingsObject(String sSettingName) {
        Object o = null;
        for (SettingObject oS : loSettings) {
            o = oS.getValue(sSettingName);
            if (o != null) {
                return oS;
            }
        }
//        System.out.println("Warning!! No Setting found for: " + sSettingName);
        return null;
    }

    public final List<Object> getALLSettingsValues(String sSettingName) {
        List<Object> lo = new ArrayList<>();
        for (SettingObject oS : loSettings) {
            if (oS == null) {
                continue;
            }
            Object o = oS.getValue(sSettingName);
            if (o != null) {
                lo.add(o);
            }
        }
        if (lo.isEmpty()) {
//            System.out.println("Warning!! No Setting found for: " + sSettingName);
        }
        return lo;
    }

    public final void setSettingsValue(String sSettingName, Object oValue) {
        Object o;
        for (SettingObject oS : loSettings) {
            o = oS.getValue(sSettingName);
            if (o != null) {
                oS.setValue(oValue.toString());
                return;
            }
        }
//        System.out.println("Warning!! No Setting found for: " + sSettingName);
    }

    public final void setSettingsValue(SettingObject oSettig) {
        Object o;
        for (SettingObject oS : loSettings) {
            o = oS.getValue(oSettig.sName);
            if (o != null) {
                oS.setValue(oSettig.sValue.toString());
                return;
            }
        }
//        System.out.println("Warning!! No Setting found for: " + oSettig.sName);
    }

    public List<SettingObject> getOutput() {
        return loSettings;
    }

    public List<String> getForFile() {
        List<SettingObject> ol = getOutput();
        List<String> ls = new ArrayList<>();
        for (SettingObject o : ol) {
            ls.add(o.getForFile());
        }
        return ls;
    }

    public void setFromFile(List<String[]> lsIn) {
        List<SettingObject> settings = new ArrayList<>();
        for (String[] sa : lsIn) {
            settings.add(getSettings(sa));
        }
        setReadIn(settings);
    }

    public SettingObject getSettings(String[] sa) {
        SettingObject o = new SettingObject(sa[0], sa[1].replaceAll("\\s+",""), null, (SettingObject.SettingsType) SettingObject.getSettingsType(sa[3]));
        o.setValue(sa[2].replaceAll("\\s+",""));
        return o;
    }

    public void setReadIn(List<SettingObject> lsIn) {
        for (SettingObject s : lsIn) {
            this.setSettingsValue(s);
        }
    }

    public List<SettingsCluster> getClusters() {
        return lsClusters;
    }
    
    public void removeSettings(String sSettingName){
        List<SettingObject> loRemove = new ArrayList<>();
        for (SettingObject oS : loSettings) {
            if (oS == null) {
                continue;
            }
            Object o = oS.getValue(sSettingName);
            if (o != null) {
                loRemove.add(oS);
            }
        }
        
        loSettings.removeAll(loRemove);
        
    }

    // Unique identifier
    public abstract String getType();

    public abstract void buildClusters();

}
