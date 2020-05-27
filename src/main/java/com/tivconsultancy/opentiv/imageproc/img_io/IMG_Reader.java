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
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Thomas Ziegenhein
 */
public class IMG_Reader {

    public static BufferedImage getGrayScale(BufferedImage inputImage) {
//        if (inputImage.getColorModel().getColorSpace().getType() != ColorSpace.TYPE_GRAY) {
            BufferedImage img = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = img.getGraphics();
            g.drawImage(inputImage, 0, 0, null);
            g.dispose();
            return img;
//        }
//        return inputImage;
    }

    public static BufferedImage readImageGrayScale(File fFile) throws IOException {        
        BufferedImage oBufImageGrey = ImageIO.read(fFile);               
        oBufImageGrey = getGrayScale(oBufImageGrey);       
        return oBufImageGrey;
    }
    
    public static ImageGrid readImageGrey(File fFile) throws IOException {        
        BufferedImage oBufImageGrey = ImageIO.read(fFile);               
        oBufImageGrey = getGrayScale(oBufImageGrey);       
        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth()];        
        iaData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);
        return new ImageGrid(oBufImageGrey.getHeight(), oBufImageGrey.getWidth(), iaData);
    }
    
    public static ImageGrid readImageGrey(File fFile, ImageGrid o) throws IOException {        
        BufferedImage oBufImageGrey = ImageIO.read(fFile);               
        oBufImageGrey = getGrayScale(oBufImageGrey);       
        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth()];        
        iaData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);
        o.setData(iaData);
        return o;
    }

}
