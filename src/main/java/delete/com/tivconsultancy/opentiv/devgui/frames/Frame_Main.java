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
package delete.com.tivconsultancy.opentiv.devgui.frames;

import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Features;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_FolderStructure;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Save;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Settings;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import java.awt.Container;
import java.util.List;
import javax.swing.SwingWorker;
import delete.com.tivconsultancy.opentiv.devgui.mvc_model.Model_Images;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Frame_Main {
    public Frame_Feature getFeatureFrame();    
    public StandardFrame_ImageView getImageViewFrame();    
    public Model_Images getDataModel();    
    public Model_Settings getSettingsModel();
    public Model_Features getFeatureModel();
    public Model_FolderStructure getFoldersModel();
    public Model_Save getSaveModel();
    public void recalc(Enum Idend);    
    public List<? extends Object> getImageLayers();
    public SwingWorker getBackgroundTask();
    public ImageGrid readPic();
    public Container getContentPane_MainFrame();
    public void cancelAllProcesses();
    public Frame_Control getControlFrame();
    
}
