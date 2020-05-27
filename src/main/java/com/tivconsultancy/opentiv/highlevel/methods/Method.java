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
package com.tivconsultancy.opentiv.highlevel.methods;

import delete.com.tivconsultancy.opentiv.devgui.main.ImagePath;
import com.tivconsultancy.opentiv.highlevel.protocols.NameSpaceProtocolResults1D;
import com.tivconsultancy.opentiv.highlevel.protocols.Protocol;
import com.tivconsultancy.opentiv.datamodels.Result1D;
import com.tivconsultancy.opentiv.math.specials.LookUp;
import java.io.File;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Method {
    public List<ImagePath> getInputImages();
    public List<Protocol> getProtocols();
    public Protocol getProtocol(String ident);    
    
    public void readInFileForView(File f) throws Exception;
    public void setFiles(File[] f);
    public void run() throws Exception;
}
