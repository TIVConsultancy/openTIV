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
package com.tivconsultancy.opentiv.datamodels;

import com.tivconsultancy.opentiv.logging.TIVLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class TempOnDisk {

    private int intRand = (int) (Math.random() * 10000000);
    private File baseFolder = null;

    public TempOnDisk(File whereToSave) {
        if (whereToSave == null) {
            try {
                cleanup();
                baseFolder = new File(File.createTempFile("tivcon-temp-file", "tmp").getParent() + File.separator + "tivcon-" + intRand);
                baseFolder.mkdir();
            } catch (IOException ex) {
                TIVLog.tivLogger.log(Level.SEVERE, null, ex);
            }
        }else{
            baseFolder = whereToSave;
        }
    }

    public void put(String ident, Serializable object, boolean append) throws FileNotFoundException, IOException {
        FileOutputStream fout = new FileOutputStream(new File(baseFolder, ident), append);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(object);
    }
    
    public List<String> getAllFileNamesInFolder(){
        List<String> names = new ArrayList<>();
        for(File f : baseFolder.listFiles()){
            names.add(f.getName());
        }
        return names;
    }

    public Serializable get(String ident) throws FileNotFoundException, IOException, ClassNotFoundException {
        File inFile = new File(baseFolder, ident);
        if(!inFile.exists()) return null;
        FileInputStream streamIn = new FileInputStream(inFile);
        ObjectInputStream ois = new ObjectInputStream(streamIn);
        return (Serializable) ois.readObject();
    }

    private void cleanup() throws IOException {
        File newTemp = File.createTempFile("tivcon-temp-file", "tmp");
        File parent = newTemp.getParentFile();
        for (File f : parent.listFiles()) {
            if (f.toString().contains("tivcon-")) {
                FileTime creationTime = (FileTime) Files.readAttributes(f.toPath(), BasicFileAttributes.class).creationTime();
                if (System.currentTimeMillis() - creationTime.toMillis() > 1.728e+8) {
                    TIVLog.tivLogger.log(Level.INFO, "Temp file cleanup, the following file/folder will be deleted: " + f.getName(), new Throwable());
                    if (f.isDirectory()) {
                        FileUtils.deleteDirectory(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
    }

}
