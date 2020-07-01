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
public class PreProc_Box3x3 implements Feature{

    Settings oSet;
    
    public PreProc_Box3x3(Settings oSet){
        this.oSet = oSet;
    }
    
    @Override
    public String getName() {
        return "Box 3x3 Filter";
    }

    @Override
    public String getDescription() {
        return "Box smothing";
    }

    @Override
    public String getToolDescription() {
        return "Box smothing";
    }

   @Override
    public String getSettingsText1() {
        return null;
    }

    @Override
    public String getSettingsDescriptions1() {
        return null;
    }

    @Override
    public String getValueDescriptions1() {
        return null;
    }

    @Override
    public String getSettings1() {
        return null;
    }

    @Override
    public void setSettings1(Object o) {        
        oSet.setSettingsValue("SF3x3Box", true);        
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
        oSet.setSettingsValue("SF3x3Box", false);
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
