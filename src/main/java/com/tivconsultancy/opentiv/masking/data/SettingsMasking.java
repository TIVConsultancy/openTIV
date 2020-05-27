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
package com.tivconsultancy.opentiv.masking.data;

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsCluster;
import com.tivconsultancy.opentiv.masking.help.SimpleShapes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class SettingsMasking extends Settings implements Serializable {

    private static final long serialVersionUID = 5026376913137739541L;
    
    public List<SimpleShapes> loShapes = new ArrayList<>();
    
    public SettingsMasking(){
        this.loSettings.add(new SettingObject("Ziegenhein2018", false, SettingObject.SettingsType.Boolean));
        buildClusters();
    }        

    @Override
    public String getType() {
        return "Image Masking";
    }

    @Override
    public void buildClusters() {
        SettingsCluster Ziegenhein2018 = new SettingsCluster("Ziegenhein2018", new String[]{"Ziegenhein2018"} , this);
        Ziegenhein2018.setDescription("Masking distinct objects in the frame");
        lsClusters.add(Ziegenhein2018);
    }
    
}
