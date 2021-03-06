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
package delete.com.tivconsultancy.opentiv.devgui.feature;

import delete.com.tivconsultancy.opentiv.devgui.mvc_model.SettingsProcess;
import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;



/**
 *
 * @author Thomas Ziegenhein
 */
public class Process_Parallel implements Feature {
    
    Settings oSet;
    
    public Process_Parallel(Settings oSet){
        this.oSet = oSet;
    }
    
    @Override
    public String getName() {
        return "Parallel";
    }

    @Override
    public String getDescription() {
        return "Parallel processing";
    }

    @Override
    public String getToolDescription() {
        return "Parallel processing";
    }

    @Override
    public String getSettingsText1() {
        return "Number of Cores";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "Number of Cores (Threads = Cores x 2)";
    }

    @Override
    public String getValueDescriptions1() {
        return "Integer";
    }

    @Override
    public String getSettings1() {
        return oSet.getSettingsValue("Cores").toString();
    }

    @Override
    public void setSettings1(Object o) {        
        oSet.setSettingsValue("Cores", Integer.valueOf(o.toString().trim()));
    }

    @Override
    public String getSettingsText2() {
        return null;
    }

    @Override
    public String getSettingsDescriptions2() {
        return null;
    }

    @Override
    public String getValueDescriptions2() {
        return null;
    }

    @Override
    public String getSettings2() {
        return null;
    }

    @Override
    public void setSettings2(Object o) {
    }
    
    public String toString(){
        return getName();
    }

    @Override
    public Feature clone() {
        return this;
    }

    @Override
    public void removeFeature() {
        oSet.setSettingsValue("Cores", 1);
    }
    
    public boolean equals(Object o) {
        if (o instanceof Process_Parallel){
            return true;
        }
        return false;
    }
}