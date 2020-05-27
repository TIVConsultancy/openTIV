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
package com.tivconsultancy.opentiv.imageproc.img_io;

import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Thomas Ziegenhein
 */
public class IMG_Writer {

    public static Image PaintGreyPNG(ImageGrid oGrid, File oOutput) throws IOException {

        int iWidth = oGrid.jLength;
        int iHeight = oGrid.iLength;

        byte[] bapixels = castToByteprimitive(oGrid.oa);

        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = oFrameImage.getRaster();

        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }
    
    public static Image PaintGreyPNG(ImageInt oGrid, File oOutput) throws IOException {

        int iWidth = oGrid.iaPixels[0].length;
        int iHeight = oGrid.iaPixels.length;

        byte[] bapixels = castToByteprimitive(oGrid);

        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = oFrameImage.getRaster();

        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }
    
    public static void PaintGreyPNG(BufferedImage oIMG, File oOutput) throws IOException {

//        int iWidth = oIMG.getWidth();
//        int iHeight = oIMG.getHeight();
//
//        byte[] bapixels = castToByteprimitive(oGrid);
//
//        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
//        WritableRaster raster = oFrameImage.getRaster();
//
//        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        ImageIO.write(oIMG, "png", oOutput);

    }

    public static Image PaintGreyPNG(boolean[][] boa, File oOutput) throws IOException {

        int iWidth = boa[0].length;
        int iHeight = boa.length;

        int[][] iaBlackBoard = new int[boa.length][boa[0].length];

        for (int i = 0; i < iaBlackBoard.length; i++) {
            for (int j = 0; j < iaBlackBoard[0].length; j++) {
                if (boa[i][j]) {
                    iaBlackBoard[i][j] = 255;
                } else {
                    iaBlackBoard[i][j] = 0;
                }
            }
        }

        byte[] bapixels = castToByteprimitive((new ImageGrid(iaBlackBoard)).oa);

        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = oFrameImage.getRaster();

        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }

    public static byte[] castToByteprimitive(ImagePoint[] iaInput) {

        byte[] byReturn = new byte[iaInput.length];

        for (int i = 0; i < byReturn.length; i++) {

            byReturn[i] = (byte) ((int) iaInput[i].iValue);

        }

        return byReturn;
    }
    
    public static byte[] castToByteprimitive(ImageInt iaInput) {

        byte[] byReturn = new byte[iaInput.iaPixels.length * iaInput.iaPixels[0].length];

        int iCount = 0;
        for (int i = 0; i < iaInput.iaPixels.length; i++) {
            for(int j = 0; j< iaInput.iaPixels[0].length; j++){
                byReturn[iCount] = (byte) (iaInput.iaPixels[i][j]);
                iCount++;
            }           
        }

        return byReturn;
    }

}
