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
package delete.trash;

import delete.trash.SettingsFeatureObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Settings_FeatureBased {
    public List<SettingsFeatureObject> loSettings = new ArrayList<>();

    public Settings_FeatureBased() {
        setDefaultSetttings();
    }

    private void setDefaultSetttings() {
    }

    public void addSettingsObject(SettingsFeatureObject o) {
        this.loSettings.add(o);
    }

    public final SettingsFeatureObject getSettingsValue(String sSettingName) {        
        for (SettingsFeatureObject oS : loSettings) {            
            if (oS.getName().equals(sSettingName)) {
                return oS;
            }
        }
//        System.out.println("Warning!! No Setting found for: " + sSettingName);
        return null;
    }

    public final List<SettingsFeatureObject> getALLSettingsValues(String sSettingName) {
        List<SettingsFeatureObject> lo = new ArrayList<>();
        for (SettingsFeatureObject oS : loSettings) {
            if (oS.getName().equals(sSettingName)) {
                lo.add(oS);
            }
        }
        if (lo.isEmpty()) {
//            System.out.println("Warning!! No Setting found for: " + sSettingName);
        }
        return lo;
    }
    
    public List<SettingsFeatureObject> getOutput() {
        return loSettings;
    }

    public void setReadIn(List<SettingsFeatureObject> lsIn) {
        for (SettingsFeatureObject s : lsIn) {
             loSettings.add(s);
        }
    }
}
