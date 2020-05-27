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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class MaskingRhombus implements Feature {

    public int iPosX = 0;
    public int iPosY = 0;
    public Double dNorm = 0.0;
    public double dNormPot = 1;
    
    @Override
    public String getName() {
        return "Rhombus";
    }

    @Override
    public String getDescription() {
        return "Masks an area in a shape of a rhombus";
    }

    @Override
    public String getToolDescription() {
        return "Masks an area in a shape of a rhombus";
    }

    @Override
    public String getSettingsText1() {
        return "Position";
    }

    @Override
    public String getSettingsDescriptions1() {
        return "Center of the circle";
    }

    @Override
    public String getValueDescriptions1() {
        return "in the format of i,j , for exaple 145,200";
    }

    @Override
    public String getSettings1() {
        return iPosY + "," + iPosX;
    }

    @Override
    public void setSettings1(Object o) {
        List<String> PosList = Arrays.asList(o.toString().split(","));
        if (PosList.isEmpty() || PosList.size() < 2) {
            return;
        }
        iPosY = Integer.valueOf(PosList.get(0).trim());
        iPosX = Integer.valueOf(PosList.get(1).trim());
//        if(!checkIfInList()){
//            AllSettingsContainer.oSetMask.loShapes.add(new SimpleShapes(iPosX, iPosY, dNorm, dNormPot));            
//        }
    }

    @Override
    public String getSettingsText2() {
        return "Size";
    }

    @Override
    public String getSettingsDescriptions2() {
        return "Size of the desired rhombus";
    }

    @Override
    public String getValueDescriptions2() {
        return "Double, size of the desired rhombus";
    }

    @Override
    public String getSettings2() {
        return dNorm.toString();
    }

    @Override
    public void setSettings2(Object o) {
        dNorm = Double.valueOf(o.toString().trim());
//        if(!checkIfInList()){
//            AllSettingsContainer.oSetMask.loShapes.add(new SimpleShapes(iPosX, iPosY, dNorm, dNormPot));            
//        }
    }

    @Override
    public void removeFeature() {
//        if(AllSettingsContainer.oSetMask.loShapes.isEmpty()) return;
//        int iCount = 0;
//        for(SimpleShapes o : AllSettingsContainer.oSetMask.loShapes){
//            if(o.iPosX == this.iPosX && o.iPosY == iPosY && o.dNorm == this.dNorm && o.dNormPot == this.dNormPot){
//                break;
//            }
//            iCount++;
//        }        
//        AllSettingsContainer.oSetMask.loShapes.remove(iCount);
    }

    @Override
    public Feature clone() {
        return new MaskingRhombus();
    }

    public String toString() {
        return getName();
    }

//    public boolean checkIfInList(){
//        for(SimpleShapes o : AllSettingsContainer.oSetMask.loShapes){
//            if(o.iPosX == this.iPosX && o.iPosY == iPosY && o.dNorm == this.dNorm && o.dNormPot == this.dNormPot){
//                return true;
//            }
//        }
//        return false;
//    }
    public boolean equals(Object o) {
        if (o instanceof Feature) {
            if (this.getName().equals(((Feature) o).getName())) {
                if (((MaskingRhombus) o).iPosX == this.iPosX && ((MaskingRhombus) o).iPosY == iPosY && ((MaskingRhombus) o).dNorm == this.dNorm && ((MaskingRhombus) o).dNormPot == this.dNormPot) {
                    return true;
                }
            }
        }
        return false;
    }

}
