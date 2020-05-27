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

import delete.com.tivconsultancy.opentiv.devgui.feature.Feature;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;



/**
 *
 * @author Thomas Ziegenhein
 */
public class Process_ImageStep implements Feature {
    
    Settings oSet;
    
    public Process_ImageStep(Settings oSet){
        this.oSet = oSet;
    }
    
    @Override
    public String getName() {
        return "Image Leap";
    }

    @Override
    public String getDescription() {
        return "Leap between first and second image; e.g. standard value is 1, which meas first image is picture 1 and second image is picture 2. A value of 2 would be first image is picture 1 and second image is picture 3. ";
    }

    @Override
    public String getToolDescription() {
        return "Leap between first and second image";
    }

    @Override
    public String getSettingsText1() {
        return "Leap";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "Leap from first to second image for PIV";
    }

    @Override
    public String getValueDescriptions1() {
        return "Integer > 0";
    }

    @Override
    public String getSettings1() {
        return oSet.getSettingsValue("Leap").toString();
    }

    @Override
    public void setSettings1(Object o) {
        oSet.setSettingsValue("Leap", Integer.valueOf(o.toString().trim()));
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
        oSet.setSettingsValue("Leap", 1);
    }
    
    public boolean equals(Object o) {
        if (o instanceof Process_ImageStep){
            return true;
        }
        return false;
    }
}