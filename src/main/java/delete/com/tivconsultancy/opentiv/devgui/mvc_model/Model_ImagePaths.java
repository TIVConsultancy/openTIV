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
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Model_ImagePaths {
 
    public List<ImagePath> getIMGFiles();
    public void addImage(ImagePath o);
    public void setImage(int iPos, ImagePath o);
    public void removeImage(int iPos);
    public void clearImages();
    public String getName();
    public void setName(String name);
    
}
