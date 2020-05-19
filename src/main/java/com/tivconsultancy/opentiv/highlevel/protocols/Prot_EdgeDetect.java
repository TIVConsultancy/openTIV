/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.highlevel.protocols;

import com.tivconsultancy.opentiv.helpfunctions.settings.FactorySettingsCluster;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingObject;
import com.tivconsultancy.opentiv.helpfunctions.settings.SettingsCluster;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.BasicIMGOper;
import static com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.EdgeDetections.getThinEdge;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.NoiseReduction;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.Ziegenhein_2018;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.math.specials.LookUp;
import com.tivconsultancy.opentiv.math.specials.NameObject;
import com.tivconsultancy.opentiv.preprocessor.OpenTIV_PreProc;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class Prot_EdgeDetect extends Protocol {

    ImageInt imgEdges;
    private String name = "Edge Detect";

    public Prot_EdgeDetect(String name) {
        this();
        this.name = name;
    }

    public Prot_EdgeDetect() {
        super();
        imgEdges = new ImageInt(50, 50, 0.0);
        buildLookUp();
        initSettings();
        buildClusters();
    }

    private void buildLookUp() {
        outPutImages = new LookUp<>();
        outPutImages.add(new NameObject<>(name, imgEdges));
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
    public ImageInt getView(String identFromViewer) {
        return outPutImages.get(identFromViewer);
    }

    @Override
    public Double getOverTimesResult(NameSpaceProtocolResults1D ident) {
        return null;
    }

    @Override
    public void run(Object... input) throws UnableToRunException {
        if (input != null && input.length != 0 && input[0] != null && input[0] instanceof ImageInt) {
            
            ImageInt imgInput = ((ImageInt) input[0]).clone();
            
            int iHystLow = Integer.valueOf(getSettingsValue("HysteresisThreshold").toString());
            int iHystHigh = Integer.valueOf(getSettingsValue("HysteresisThresholdHigh").toString());

            imgInput.iaPixels = NoiseReduction.Gau(imgInput.iaPixels);
            imgInput.iaPixels = getThinEdge(imgInput.iaPixels, Boolean.FALSE, null, null, 0);
            if (iHystHigh < 0) {
                BasicIMGOper.threshold(imgInput, iHystLow);
            } else {
                imgInput = BasicIMGOper.hysteresis(imgInput, iHystLow, iHystHigh);
            }

            imgInput = Ziegenhein_2018.thinoutEdges(imgInput);
            imgEdges.setImage(imgInput.getBuffImage());
        } else {
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
        lsClusters.clear();
        lsClusters.add(FactorySettingsCluster.
                getStandardCluster("Advanced Canny Edge Detector",
                                   new String[]{ "HysteresisThreshold", "HysteresisThresholdHigh"},
                                   "Threshold that defines a strong or "
                                   + "weak edge. "
                                   + "Weak edges that are not connected "
                                   + "to a strong edge "
                                   + "will be deleted",
                                   this));
    }

    /**
     * Cuts the image
     *
     * @param oInput Input image in the openTIV ImageInt format
     * @param oSettings Settings object containing the settings information
     * @return
     */
    private void initSettings() {
//        this.loSettings.add(new SettingObject("Activated", "SimpleEdges", true, SettingObject.SettingsType.Boolean));
        this.loSettings.add(new SettingObject("Threshold Low", "HysteresisThreshold", 127, SettingObject.SettingsType.Integer));
        this.loSettings.add(new SettingObject("Threshold High", "HysteresisThresholdHigh", -1, SettingObject.SettingsType.Integer));
    }

    @Override
    public Object[] getResults() {
        return new Object[]{imgEdges};
    }

}
