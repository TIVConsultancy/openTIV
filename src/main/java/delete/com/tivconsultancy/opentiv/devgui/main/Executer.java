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
package delete.com.tivconsultancy.opentiv.devgui.main;

/**
 *
 * @author Thomas Ziegenhein
 */
import delete.com.tivconsultancy.opentiv.devgui.frames.StandardFrame_Main;

public class Executer {
    
//    public static StandardFrame_Main oMainFrame = new StandardFrame_Main();

    public static void main(String[] args) {
        StandardFrame_Main oMainFrame = new StandardFrame_Main();
        oMainFrame.setEnabled(true);
        oMainFrame.setVisible(true);
//        oMainFrame.setEnabled(true);
//        oMainFrame.setVisible(true);
//        oMainFrame.open();
    }
}
