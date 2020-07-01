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
public class HGEqualize implements Feature {

    Settings oSet;
    
    public HGEqualize(Settings oSet){
        this.oSet = oSet;
    }
    
    public HGEqualize() {
    }

    @Override
    public String getName() {
        return "Equalize";
    }

    @Override
    public String getDescription() {
        return "Histogram equalization is a method in image processing of contrast adjustment using the image's histogram. ";
    }

    @Override
    public String getToolDescription() {
        return "Histogram equalization is a method in image processing of contrast adjustment using the image's histogram. ";
    }

    @Override
    public String getSettingsText1() {
        return "Max White";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "Set the maximal white value";
    }

    @Override
    public String getValueDescriptions1() {
        return "Integer < 256";
    }

    @Override
    public String getSettings1() {
        return oSet.getSettingsValue("Equalize").toString();
    }

    @Override
    public void setSettings1(Object o) {     
        oSet.setSettingsValue("HGEqualize", true);
        oSet.setSettingsValue("Equalize", Integer.valueOf(o.toString().trim()));
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
        oSet.setSettingsValue("HGEqualize", false);
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
