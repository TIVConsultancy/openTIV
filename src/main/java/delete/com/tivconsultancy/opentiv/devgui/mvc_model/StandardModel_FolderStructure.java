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
package delete.com.tivconsultancy.opentiv.devgui.mvc_model;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class StandardModel_FolderStructure implements Model_FolderStructure {

    public String sSettings = "Settings";
    public String sDebug = "Debug";
    public String sPictures = "Pictures";
    public String sData = "Data";
    
    public String sOut;
    
    public StandardModel_FolderStructure(String sOut){
        this.sOut = sOut;
    }
    
    @Override
    public String getFolder(Enum FolderName){
        if(FolderName.equals(StandardFolders.Settings)) return sSettings;
        if(FolderName.equals(StandardFolders.Debug)) return sDebug;
        if(FolderName.equals(StandardFolders.Pictures)) return sPictures;
        if(FolderName.equals(StandardFolders.Data)) return sData;
        if(FolderName.equals(StandardFolders.Out)) return sOut;
        return null;
    }

    @Override
    public void setOutFolder(String sOut) {
        this.sOut = sOut;
    }

    @Override
    public Enum getEnum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static enum StandardFolders {
        //possible folders        
        Settings, Debug, Pictures, Data, Out;

        public class GUI_Data {

            public GUI_Data() {
            }
        }
    }
    
}
