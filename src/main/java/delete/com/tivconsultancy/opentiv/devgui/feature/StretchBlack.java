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


import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StretchBlack implements Feature{

    Settings oSet;
    
    public StretchBlack(Settings oSet){
        this.oSet = oSet;
    }
    
    @Override
    public String getName() {
        return "StretchBlack";
    }

    @Override
    public String getDescription() {
        return "Stretches the left side of the histogram by a Factor";
    }

    @Override
    public String getToolDescription() {
        return "Stretches the left side of the histogram by a Factor";
    }

    @Override
    public String getSettingsText1() {
        return "Factor";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "Factor by which the left side of the histogram will be stretched";
    }

    @Override
    public String getValueDescriptions1() {
        return "Usually a value around 1 is common";
    }

    @Override
    public String getSettings1() {
        return oSet.getSettingsValue("BlackStretchFactor").toString(); 
    }

    @Override
    public void setSettings1(Object o) {
        oSet.setSettingsValue("HGBlackStretch", true);
        oSet.setSettingsValue("BlackStretchFactor", Double.valueOf(o.toString().trim()));
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
    
    @Override
    public String toString(){
        return getName();
    }
    
    public Feature clone(){
        return this;
    }

    @Override
    public void removeFeature() {
        oSet.setSettingsValue("HGBlackStretch", false);
    }
    
    public boolean equals(Object o) {
        if (o instanceof Feature){
            if(this.getName().equals( ((Feature) o).getName())){
                return true;
            }
        }
        return false;
    }
    
}
