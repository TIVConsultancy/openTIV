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
package com.tivconsultancy.opentiv.highlevel.protocols;

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsCluster;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.math.specials.LookUp;
import com.tivconsultancy.opentiv.math.specials.NameObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class Prot_ReadIMGFiles extends Protocol implements Serializable {

    private static final long serialVersionUID = -1394639842255399516L;

    protected File imgFile;
    protected ImageInt imgRead;
    protected String name = "Read In";
    
    protected LookUp<BufferedImage> outPutImages;

    public Prot_ReadIMGFiles(String name) {
        this();
        this.name = name;
    }
    
    public Prot_ReadIMGFiles() {
        super();
        imgRead = new ImageInt(50, 50, 150);
        buildLookUp();
        initSettings();
        buildClusters();
    }


    protected ImageInt readImage() throws IOException {
        imgRead.setImage(IMG_Reader.readImageGrayScale(imgFile));
        return imgRead;
    }

    private void buildLookUp() {
        outPutImages = new LookUp<>();
        outPutImages.add(new NameObject<>(name, imgRead.getBuffImage()));
    }

    @Override
    public NameSpaceProtocolResults1D[] get1DResultsNames() {
        return new NameSpaceProtocolResults1D[0];
    }

    @Override
    public List<String> getIdentForViews() {
        return Arrays.asList(new String[]{name});
    }

    @Override
    public BufferedImage getView(String identFromViewer) {
        return outPutImages.get(identFromViewer);
    }

    @Override
    public Double getOverTimesResult(NameSpaceProtocolResults1D ident) {
        return null;
    }

    @Override
    public void run(Object... input) throws UnableToRunException {
        if (input != null && input.length != 0 && input[0] != null && input[0] instanceof File) {
            imgFile = (File) input[0];
            try {                
                readImage();                
            } catch (IOException ex) {
                throw new UnableToRunException("Cannot read imgage: " + imgFile, ex);
            }
            
        }else{
            throw new UnableToRunException("Input is not a file", new IOException());
        }
        buildLookUp();
    }

    @Override
    public String getType() {
        return name;
    }

    @Override
    public void buildClusters() {
//        SettingsCluster CutImage = new SettingsCluster("Cut Image",
//                                                       new String[]{"BcutyTop", "cutyTop", "BcutyBottom",
//                                                           "cutyBottom", "BcutxLeft", "cutxLeft", "BcutxRight",
//                                                           "cutxRight"}, this);
//        CutImage.setDescription("Cut image");
//        lsClusters.add(CutImage);
    }
    
    /**
     * Cuts the image
     *
     * @param oInput Input image in the openTIV ImageInt format
     * @param oSettings Settings object containing the settings information
     * @return
     */  
    
        
    private void initSettings(){
//        this.loSettings.add(new SettingObject("Cut Top", "BcutyTop", false, SettingObject.SettingsType.Boolean));
//        this.loSettings.add(new SettingObject("Value", "cutyTop", 0, SettingObject.SettingsType.Integer));
//        this.loSettings.add(new SettingObject("Cut Bottom", "BcutyBottom", true, SettingObject.SettingsType.Boolean));
//        this.loSettings.add(new SettingObject("Value","cutyBottom", 600, SettingObject.SettingsType.Integer));
//        this.loSettings.add(new SettingObject("Cut Left", "BcutxLeft", false, SettingObject.SettingsType.Boolean));
//        this.loSettings.add(new SettingObject("Value","cutxLeft", 0, SettingObject.SettingsType.Integer));
//        this.loSettings.add(new SettingObject("Cut Right", "BcutxRight", false, SettingObject.SettingsType.Boolean));
//        this.loSettings.add(new SettingObject("Value","cutxRight", 10, SettingObject.SettingsType.Integer));
    }

    @Override
    public Object[] getResults() {
        return new Object[]{imgRead};
    }

    @Override
    public void setImage(BufferedImage bi) {
        for(String s : getIdentForViews()){
            outPutImages.set(s, bi);
        }
    }

}
