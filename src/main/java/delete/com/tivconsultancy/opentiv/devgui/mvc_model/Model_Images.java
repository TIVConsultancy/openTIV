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
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Model_Images {
    
    public ImageInt getImage(Enum DataName);
    public List<ImagePath> getIMGFiles();
    public void addImage(ImageInt o, Enum DataName);
    public void replaceImage(ImageInt oNew, Enum DataName);
    public void setImage(int iPos, ImageInt oNew, Enum DataName);
    public void clearImages();
    
    public static enum DataNames {
        //Names of the possible Data
        //Images in ImageInt format
        Input, TransEnd, PreProcEnd, MaskingEnd, PIVGrid, PIVVectors, CD_Edges, CD_ShapeFit
        //Other
    }
    
}
