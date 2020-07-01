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
public class CutX implements Feature{

    Settings oSet;
    
    public CutX(Settings oSet){
        this.oSet = oSet;
    }
    
    @Override
    public String getName() {
        return "Cut X";
    }

    @Override
    public String getDescription() {
        return "cuts the image in the x-cooridnate";
    }

    @Override
    public String getToolDescription() {
        return "cuts the image in the x-cooridnate";
    }

    @Override
    public String getSettingsText1() {
        return "Left Border";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "defines the cut at the left of the image, (0,0) is at the top left corner";
    }

    @Override
    public String getValueDescriptions1() {
        return "Integer, >0 and smaller than the image width";
    }

    @Override
    public String getSettings1() {
        return oSet.getSettingsValue("cutxLeft").toString();
    }

    @Override
    public void setSettings1(Object o) {
        oSet.setSettingsValue("BcutxLeft", true);
        oSet.setSettingsValue("cutxLeft", Integer.valueOf(o.toString().trim()));
    }

    @Override
    public String getSettingsText2() {
        return "Right Border";
    }

    @Override
    public String getSettingsDescriptions2() {
        return "defines the cut at the bottom of the image (0,0) is at the top left corner";
    }

    @Override
    public String getValueDescriptions2() {
        return "Integer, >0 and smaller than image height";
    }

    @Override
    public String getSettings2() {       
        return oSet.getSettingsValue("cutxRight").toString();
    }

    @Override
    public void setSettings2(Object o) {
        oSet.setSettingsValue("BcutxRight", true);
        oSet.setSettingsValue("cutxRight", Integer.valueOf(o.toString().trim()));
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
        oSet.setSettingsValue("BcutxLeft", false);
        oSet.setSettingsValue("BcutxRight", false);
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
