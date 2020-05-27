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
package com.tivconsultancy.opentiv.helpfunctions.settings;

import com.tivconsultancy.opentiv.helpfunctions.strings.StringWorker;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class SettingObject implements Serializable {

    private static final long serialVersionUID = -4390760382951116997L;

    protected String sName = "";
    public String viewName = null;
    public Object sValue = "";
    public SettingsType ident;
    public List<String> explanations;

    public SettingObject(String sName, Object sValue, SettingsType ident) {
        this.sName = sName;
        this.sValue = sValue;
        this.ident = ident;
    }

    public SettingObject(String viewName, String sName, Object sValue, SettingsType ident) {
        this.sName = sName;
        this.sValue = sValue;
        this.ident = ident;
        this.viewName = viewName;
    }

    public Object getValue(String sName) {
        if (sName.equals(this.sName)) {
            return sValue;
        }
        return null;
    }

    public String getName() {
        return this.sName;
    }

    public String getViewName() {
        if (viewName == null) {
            return getName();
        }
        return this.viewName;
    }

    public void setValue(Object sValue) {
        if (sValue == null || ident == null) {
            System.out.println(this.toString());
            return;
        }
        if (sValue instanceof String) {
            String s = sValue.toString();
            if (ident.equals(SettingsType.String)) {
                this.sValue = s;
            }
            if (ident.equals(SettingsType.Double)) {
                this.sValue = Double.valueOf((String) s);
            }
            if (ident.equals(SettingsType.Integer)) {
                this.sValue = Double.valueOf((String) s).intValue();
            }
            if (ident.equals(SettingsType.Boolean)) {
                this.sValue = Boolean.valueOf((String) s);
            }
//            this.sValue = sValue;
        }

    }

    public String toString() {
        return sName + "," + sValue.toString();
    }

    public String getForFile() {
        return viewName + ";" + sName + ";" + sValue.toString() + ";" + ident.toString();
    }

    public String getValueAsString() {
        return sValue.toString();
    }

    public static Enum getSettingsType(String s) {
        for (Enum e : SettingsType.values()) {
            if (s.equals(e.toString())) {
                return e;
            }
        }
        return null;
    }

    public enum SettingsType {

        String, Double, Integer, Boolean, Object
    }

}
