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

import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.masking.data.SettingsMasking;
import com.tivconsultancy.opentiv.preprocessor.SettingsPreProc;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class StandardModel_Settings implements Model_Settings {

    List<String> lsImagePaths = new ArrayList<>();
    SettingsReadIn oSetReadIn = new SettingsReadIn();
    SettingsPreProc oSettingsPreProc = new SettingsPreProc();
    SettingsMasking oSettingsMasking = new SettingsMasking();
    SettingsProcess oSettingsProcess = new SettingsProcess();

    public StandardModel_Settings() {
    }

//    public void addSettings(Settings oSet) {
//        loSettings.add(oSet);
//    }

    @Override
    public List<Settings> getAllSettings() {
        List<Settings> loSettings = new ArrayList<>();
        loSettings.add(oSetReadIn);
        loSettings.add(oSettingsMasking);
        loSettings.add(oSettingsPreProc);
        loSettings.add(oSettingsProcess);
        return loSettings;
    }

    @Override
    public Settings getSettings(Enum SettingName) {
        if (SettingName.equals(StandardSettingNames.ReadIn)) {
            return oSetReadIn;
        }
        if (SettingName.equals(StandardSettingNames.PreProc)) {
            return oSettingsPreProc;
        }
        if (SettingName.equals(StandardSettingNames.Process)) {
            return oSettingsProcess;
        }
        if (SettingName.equals(StandardSettingNames.Mask)) {
            return oSettingsMasking;
        }
        return null;
    }

    public static enum StandardSettingNames {

        //Names of the possible Settings        

        ReadIn, PreProc, Mask, Process
    }

}
