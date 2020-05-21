/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.highlevel.protocols;

import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsCluster;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Writer;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.masking.main.OpenTIV_Masking;
import com.tivconsultancy.opentiv.math.specials.LookUp;
import com.tivconsultancy.opentiv.math.specials.NameObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class Prot_ObjectMasking extends Protocol {

    ImageInt masking1;
    ImageInt masking2;
    ImageInt totMask;
    
    private String name = "Masking";

    public Prot_ObjectMasking() {
        masking1 = new ImageInt(50, 50, 0);
        masking2 = new ImageInt(50, 50, 0);
        totMask = new ImageInt(50, 50, 0);
        buildLookUp();
        initSettins();
        buildClusters();
    }

    private void buildLookUp() {
        outPutImages = new LookUp<>();
        outPutImages.add(new NameObject<>(name, totMask));
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
        return outPutImages.get(identFromViewer).getBuffImage();
    }

    @Override
    public Double getOverTimesResult(NameSpaceProtocolResults1D ident) {
        return null;
    }

    @Override
    public void run(Object... input) throws UnableToRunException {
        if (input != null && input.length >= 2 && input[0] != null && input[1] != null && input[0] instanceof ImageInt && input[1] instanceof ImageInt) {
            ImageInt mask1 = OpenTIV_Masking.performMasking((ImageInt) input[0], this);
            ImageInt mask2 = OpenTIV_Masking.performMasking((ImageInt) input[1], this);
            
            mask1 = mask1 == null ? new ImageInt(((ImageInt) input[0]).iaPixels.length, ((ImageInt) input[0]).iaPixels[0].length, 255) : mask1;
            mask2 = mask2 == null ? new ImageInt(((ImageInt) input[0]).iaPixels.length, ((ImageInt) input[0]).iaPixels[0].length, 255) : mask2;
            
            masking1.setImage(mask1.getBuffImage());
            masking1.setBoolean(mask1.baMarker);
            masking2.setImage(mask2.getBuffImage());
            masking2.setBoolean(mask2.baMarker);
            
            totMask.setImage(new ImageInt(((ImageInt) input[0]).iaPixels.length, ((ImageInt) input[0]).iaPixels[0].length, 255).getBuffImage());
            
            for (int i = 0; i < Math.min(masking1.iaPixels.length, masking2.iaPixels.length); i++) {
                for (int j = 0; j < Math.min(masking1.iaPixels[0].length, masking2.iaPixels[0].length); j++) {
                    if (masking1.baMarker[i][j] || masking2.baMarker[i][j]) {
                        totMask.baMarker[i][j] = true;
                        if (masking1.baMarker[i][j]) {
                            totMask.iaPixels[i][j] = 0;
                        }
                        if (masking2.baMarker[i][j]) {
                            totMask.iaPixels[i][j] = 127;
                        }
                    }
                }
            }
        } else {
            throw new UnableToRunException("Wrong input", new Exception());
        }
        buildLookUp();
    }

    @Override
    public Object[] getResults() {
        return new Object[]{totMask};
    }

    @Override
    public String getType() {
        return name;
    }

    private void initSettins() {
        this.loSettings.add(new SettingObject("Ziegenhein2018", "Ziegenhein2018", false, SettingObject.SettingsType.Boolean));
    }

    @Override
    public void buildClusters() {
        SettingsCluster IMGFilter = new SettingsCluster("Object Masking",
                                                        new String[]{"Ziegenhein2018"}, this);
        IMGFilter.setDescription("Masks objects in pictures based on edge detecting");
        lsClusters.add(IMGFilter);
    }

}
