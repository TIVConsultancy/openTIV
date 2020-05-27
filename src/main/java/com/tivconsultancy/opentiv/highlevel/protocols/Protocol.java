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
package com.tivconsultancy.opentiv.highlevel.protocols;

import com.tivconsultancy.opentiv.helpfunctions.settings.Hints;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.Settings;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsCluster;
import com.tivconsultancy.opentiv.math.specials.LookUp;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 * @param <T>
 */
public abstract class Protocol extends Settings implements Hints, Serializable {

    private static final long serialVersionUID = -6114709214102901996L;    
    
    public Protocol() {
    }

    public List<SettingObject> getSettings() {
        return getAllSettings();
    }

    public abstract NameSpaceProtocolResults1D[] get1DResultsNames();

    public abstract List<String> getIdentForViews();

    public abstract BufferedImage getView(String identFromViewer);
    
    public abstract void setImage(BufferedImage bi);

    public abstract Double getOverTimesResult(NameSpaceProtocolResults1D ident);

    public abstract void run(Object... input) throws UnableToRunException;

    public abstract Object[] getResults();

    public List<SettingObject> getHints() {
        List<SettingObject> ls = new ArrayList<>();
        for (SettingsCluster c : getClusters()) {
            for (SettingObject o : c.getSettings()) {
                if (o.ident.equals(SettingObject.SettingsType.Boolean)) {
                    ls.add(o);
                    ls.add(new SettingObject(o.getName(), !((Boolean) o.sValue), SettingObject.SettingsType.Boolean));
                } else {
                    ls.add(o);
                }
            }
        }
        return ls;
    }

}
