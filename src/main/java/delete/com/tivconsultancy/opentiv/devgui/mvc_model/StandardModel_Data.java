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

import com.tivconsultancy.opentiv.logging.TIVLog;
import delete.com.tivconsultancy.opentiv.devgui.main.ImagePath;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class StandardModel_Data implements Model_Images {

    List<ImageInt> loImages = new ArrayList<>();

    @Override
    public ImageInt getImage(Enum DataName) {
        for (ImageInt o : loImages) {
            if (o.sIdent.equals(DataName.name())) {
                return o;
            }
        }
        return null;

    }

    @Override
    public void addImage(ImageInt o, Enum DataName) {
        o.sIdent = DataName.name();
        loImages.add(o);
    }
    
    @Override
    public void replaceImage(ImageInt oNew, Enum DataName){
        ImageInt oOld = getImage(DataName);
        if(oOld == null){
//            TIVLog.Log.warn((new Throwable()).getStackTrace()[0].getMethodName() + "Tried to replace a non existing image, Image added");
            this.addImage(oNew, DataName);
        }
        if(oOld!=null && oNew != null){
            int iPos = loImages.indexOf(oOld);
            if(iPos>=0){
                setImage(iPos, oNew, DataName);
            }            
        }        
    }

    @Override
    public void clearImages() {
        loImages.clear();
    }

    @Override
    public void setImage(int iPos, ImageInt oNew, Enum DataName) {
        oNew.sIdent = DataName.name();
        loImages.set(iPos, oNew);
    }
        
//    
//    public static enum StandardDataNames {
//        //Names of the possible Data
//        //Images in ImageInt format
//        ReadIn, Transformation, PreProccessor, Mask
//        //Other
//    }

    @Override
    public List<ImagePath> getIMGFiles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
