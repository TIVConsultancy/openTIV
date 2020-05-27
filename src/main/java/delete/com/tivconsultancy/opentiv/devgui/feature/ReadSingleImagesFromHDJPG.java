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
public class ReadSingleImagesFromHDJPG implements Feature{

    Settings oSet;
    
    public ReadSingleImagesFromHDJPG(Settings oSet){
        this.oSet = oSet;
    }
    
    public String sSettings1;
    
    @Override
    public String getName() {
        return "Single Images from HDD JPEG";
    }

    @Override
    public String getDescription() {
        return "Reads all jpeg  files in the specified folder";
    }

    @Override
    public String getToolDescription() {
        return "Reads all jpeg  files in the specified folder";
    }

    @Override
    public String getSettingsText1() {
        return " File Folder";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "The position where the files are located (for example C:\\myPictures or /home/myPictures";
    }

    @Override
    public String getValueDescriptions1() {
        return "C:\\myPictures or /home/myPictures";
    }

    @Override
    public String getSettings1() {
        if(sSettings1 == null){
            return "C:\\";
        }else{
            return sSettings1;
        }
        
    }

    @Override
    public void setSettings1(Object o) {
        this.sSettings1 = o.toString().trim();
        oSet.setSettingsValue("ReadInFolder", (o.toString().trim()));
        oSet.setSettingsValue("ReadInFileType", ".jpg");
        
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
        return new ReadSingleImagesFromHDJPG(oSet);
    }

    @Override
    public void removeFeature() {        
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
