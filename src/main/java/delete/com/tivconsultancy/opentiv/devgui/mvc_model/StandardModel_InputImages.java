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

import delete.com.tivconsultancy.opentiv.devgui.main.ImagePath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class StandardModel_InputImages implements Model_ImagePaths{

    private List<ImagePath> images;
    private String name = "";
    
    public StandardModel_InputImages(String name){
        this.name = name;
        images = new ArrayList<>();
    }
    
    @Override
    public List<ImagePath> getIMGFiles() {
        return images;
    }

    @Override
    public void addImage(ImagePath o) {
        images.add(o);
    }

    @Override
    public void setImage(int iPos, ImagePath o) {
        images.set(iPos, o);
    }

    @Override
    public void removeImage(int iPos) {
        images.remove(iPos);
    }

    @Override
    public void clearImages() {
        images.clear();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
