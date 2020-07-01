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

import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Feature extends Serializable {    
    public String getName();
    public String getDescription();
    public String getToolDescription();
    public String getSettingsText1();
    public String getSettingsDescriptions1();
    public String getValueDescriptions1();
    public String getSettings1();
    public void setSettings1(Object o);
    public String getSettingsText2();    
    public String getSettingsDescriptions2();
    public String getValueDescriptions2();
    public String getSettings2();
    public void setSettings2(Object o);
    public void removeFeature();
    public String toString();
    public Feature clone();
    public boolean equals(Object o);
}
