/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.imageproc.helpfun;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.math.sets.Set1D;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class ImageIntSub extends ImageInt {

    private static final long serialVersionUID = -245782501114957116L;

    protected Set1D cutX;
    protected Set1D cutY;

    public ImageIntSub(int[][] ia, Set1D cutX, Set1D cutY) {
        super(ia);
        this.cutX = cutX;
        this.cutY = cutY;
    }
    
    public Set1D getCutX(){
        return cutX;
    }
    
    public Set1D getCutY(){
        return cutY;
    }

    public void setOnImage(ImageInt img) {
        if(cutX.dLeftBorder >= img.iaPixels[0].length || cutY.dLeftBorder >=img.iaPixels.length){
            return;
        }
        int startX = (int) Math.max(cutX.dLeftBorder, 0);
        int endX = (int) Math.min(cutX.dRightBorder, img.iaPixels[0].length);
        int startY = (int) Math.max(cutY.dLeftBorder, 0);
        int endY = (int) Math.min(cutY.dRightBorder, img.iaPixels.length);
        int runY = 0;
        int runX = 0;
        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                img.setPoint(new MatrixEntry(i, j), this.iaPixels[runY][runX]);
                runX++;
            }
            runY++;
        }
    }
    
    public ImageInt setOnImage(ImageInt img, ImageInt toSet) {
        if(cutX.dLeftBorder >= img.iaPixels[0].length || cutY.dLeftBorder >=img.iaPixels.length){
            return img;
        }
        int startX = (int) Math.max(cutX.dLeftBorder, 0);
        int endX = (int) Math.min(cutX.dRightBorder, img.iaPixels[0].length);
        int startY = (int) Math.max(cutY.dLeftBorder, 0);
        int endY = (int) Math.min(cutY.dRightBorder, img.iaPixels.length);
        int runY = 0;        
        for (int i = startY; i < endY; i++) {
            int runX = 0;
            for (int j = startX; j < endX; j++) {
                img.setPoint(new MatrixEntry(i, j), toSet.iaPixels[runY][runX]);
                runX++;
            }
            runY++;
        }
        return img;
    }

}
