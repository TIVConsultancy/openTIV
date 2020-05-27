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
public class SettingsCluster implements Serializable{

    private static final long serialVersionUID = 5921152130198783168L;
    
    String sName;
    String[] lsIdent;
    List<SettingObject> lsSettings = new ArrayList<>();
    String sDescription = "Description";
    
    public SettingsCluster(String sName, String[] lsIdent, Settings oSet){
        this.sName = sName;
        this.lsIdent = lsIdent;
        buildCluster(oSet);
    }
    
    private void buildCluster(Settings oSet){
        for(String s : lsIdent){
            lsSettings.add(oSet.getSettingsObject(s));
        }
    }
    
    public String getName(){
        return sName;
    }
    
    public void setDescription(String sDescription){
        this.sDescription = sDescription;
    }
    
    public String getDescription(){
        return this.sDescription;
    }
    
    public List<SettingObject> getSettings(){
        return lsSettings;
    }
    
    @Override
    public String toString(){
        return getName();
    }
}
