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
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;



/**
 *
 * @author Thomas Ziegenhein
 */
public class Process_Sequence implements Feature {
    
    Settings oSet;
    
    public Process_Sequence(Settings oSet){
        this.oSet = oSet;
    }
    
    @Override
    public String getName() {
        return "Image Squence";
    }

    @Override
    public String getDescription() {
        return "Processes images that are taken in squences. For example: 3 Images recorded with 250 fps -> pause 1 second -> 3 Images recorded with 250 fps -> pause 1 second -> ... ";
    }

    @Override
    public String getToolDescription() {
        return "Processes images that are taken in squences. For example: 3 Images recorded with 250 fps -> pause 1 second -> 3 Images recorded with 250 fps -> pause 1 second -> ...";
    }

    @Override
    public String getSettingsText1() {
        return "Squence length";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "Length of the image sequence taken consecutively";
    }

    @Override
    public String getValueDescriptions1() {
        return "Integer";
    }

    @Override
    public String getSettings1() {
        return oSet.getSettingsValue("Sequence").toString();
    }

    @Override
    public void setSettings1(Object o) {
        oSet.setSettingsValue("Sequence", Integer.valueOf(o.toString().trim()));
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
        oSet.setSettingsValue("Sequence", 1);
    }
    
    public boolean equals(Object o) {
        if (o instanceof Process_Sequence){
            return true;
        }
        return false;
    }
}