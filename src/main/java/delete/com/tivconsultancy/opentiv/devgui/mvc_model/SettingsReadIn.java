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

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsCluster;


/**
 *
 * @author Thomas Ziegenhein
 */
public class SettingsReadIn extends Settings {

    public SettingsReadIn() {
        this.loSettings.add(new SettingObject("ReadInFolder", " ",SettingObject.SettingsType.String));
        this.loSettings.add(new SettingObject("ReadInFileType", " ",SettingObject.SettingsType.String));
        buildClusters();
    }

    @Override
    public String getType() {
        return "Image Read In";
    }

    @Override
    public void buildClusters() {
        SettingsCluster ReadIn = new SettingsCluster("Read in folder", new String[]{"ReadInFolder"}, this);
        ReadIn.setDescription("Folder from which data will be read in");
        lsClusters.add(ReadIn);
        
        SettingsCluster ReadInFileType = new SettingsCluster("File Type", new String[]{"ReadInFileType"}, this);
        ReadInFileType.setDescription("The file type which is searched in the read in folder");
        lsClusters.add(ReadInFileType);
    }

}
