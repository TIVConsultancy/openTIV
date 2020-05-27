/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package com.tivconsultancy.tivpiv.data;

import com.tivconsultancy.opentiv.datamodels.IndexableResults;
import com.tivconsultancy.opentiv.datamodels.Result1D;
import com.tivconsultancy.tivpiv.helpfunctions.InterrGrid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class DataPIV implements IndexableResults, Serializable {        

    private static final long serialVersionUID = 7041378570022471115L;
    
    // 1D Data
    public Result1D results1D;
    
    //Data
    public int[][] iaReadInFirst;
    public int[][] iaReadInSecond;
    public double[][] iaGreyIntensity1;
    public double[][] iaGreyIntensity2;
    public boolean[][] baMask;
    public InterrGrid oGrid;
    
    //Settings
    public List<String> Pictures = new ArrayList<>();
    public String sPWDIn = "";
    public String sPWDOut = "";
    public final String sDebugFolder = "Debug";
    public final String sOutputFolder = "Data";
    public final String sPicFolder = "Pictures"; 
//    public int cutyTop = -1;
//    public int cutyBottom = -1;
//    public int cutxLeft = 50;
//    public int cutxRight = 1220;

    //PreProc
    //Sharpening
    // outsorced in protocols
//    public boolean bsharpen = true;
//    public int iHistCut = 50;
//    public int iThresholdSharp = 70;
//    public boolean bBinarize = false;
//    public int imax = 255;
//    //Increasing Contrast
//    public boolean bhighcont = false;

    //Masking
    // outsorced in protocols
//    public boolean bMask = true;
//    public boolean bFixedMask = true;
//    public int iEdgeThreshold = 70;
//    public int iSmallestStructure = 300;

    //PIV
    public int PIV_columns;
    public int PIV_rows;
    public int PIV_WindowSize = 32;
    public String sGridType = "Standard";
    public boolean Hart1998 = false;
    public double Hart1998Divider = 2.0;
    public String sSubPixelType = "Gaussian";
    //Multipass
    public boolean bMultipass = false;
    public boolean bMultipass_BiLin = true;
    public int iMultipassCount = 3;
    //Refine
    public boolean bRefine = false;

    //PostPIV
    //Vector Validation
    public boolean bValidate = true;
    public String sValidationType = "MedianComp";
    public int iStampSize = 2;
    public double dValidationThreshold = 5;
    public boolean bInterpolation = false;
    //Display
    public double dStretch  = 7;
    public boolean AutoStretch = true;
    public double AutoStretchFactor = 2;
    
    
    public DataPIV(){     
        results1D = new Result1D();
    }

    @Override
    public Double getRes(String name) {
        return results1D.getRes(name);
    }

    @Override
    public String getName(Double value) {
        return results1D.getName(value);
    }

    @Override
    public boolean setResult(String name, Double d) {
        return results1D.setResult(name, d);
    }
    
    @Override
    public void addResult(String name, Double d){
        results1D.addResult(name, d);
    }
    
}
