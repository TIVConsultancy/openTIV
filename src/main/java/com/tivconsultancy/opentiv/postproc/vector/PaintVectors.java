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
package com.tivconsultancy.opentiv.postproc.vector;

import com.tivconsultancy.opentiv.helpfunctions.colorspaces.Colorbar;
import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.helpfunctions.matrix.Cast;
import com.tivconsultancy.opentiv.helpfunctions.matrix.Reshape;
import static com.tivconsultancy.opentiv.imageproc.img_io.IMG_Writer.castToByteprimitive;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Thomas Ziegenhein
 */
public class PaintVectors {
    public static BufferedImage paintOnImage(List<? extends VelocityVec> loIn, Colorbar oColorbar, int[][] iaBlackBoard, String sOutput, double dStretchFactor) throws IOException {
        int iHeight = iaBlackBoard.length; 
        int iWidth = iaBlackBoard[0].length;

        byte[] bapixels = Cast.castToByteprimitive(Reshape.reshape(iaBlackBoard));

        //byte[] bapixels = getPixel(iaInput, 1);
        //System.out.println(pixels.length);
        BufferedImage oFrameImage;

        oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
                

        WritableRaster raster = oFrameImage.getRaster();
        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);

        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        
        
        if (oColorbar != null) {
            oFrameImage = Writer.getType_Int_RGB(oFrameImage);
        }

        Graphics2D g = oFrameImage.createGraphics();
        
        for(VelocityVec o : loIn){            
            if(o != null) g = paintOnBlackBoard(o, oColorbar, g, dStretchFactor);
        }
        
        if( sOutput != null) ImageIO.write(oFrameImage, "png", new File(sOutput));       
        return oFrameImage;
    }
    
    public static ImageGrid paintOnImage(List<? extends VelocityVec> loIn, Colorbar oColorbar, ImageGrid oGrid, String sOutput, double iStretchFactor) throws IOException {
        int iWidth = oGrid.jLength;
        int iHeight = oGrid.iLength;

        byte[] bapixels = castToByteprimitive(oGrid.oa);

        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = oFrameImage.getRaster();

        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);
        
        oFrameImage = paintOnImage(loIn, oColorbar, oFrameImage, sOutput, iStretchFactor);
        return new ImageGrid(oFrameImage);        
    }
    
    public static BufferedImage paintOnImage(List<? extends VelocityVec> loIn, Colorbar oColorbar, BufferedImage oFrameImage, String sOutput, double iStretchFactor) throws IOException {        

        if (oColorbar != null) {
            oFrameImage = Writer.getType_Int_RGB(oFrameImage);
        }

        Graphics2D g = oFrameImage.createGraphics();
        
        for(VelocityVec o : loIn){            
            if(o != null) g = paintOnBlackBoard(o, oColorbar, g, iStretchFactor);
        }
        
        if( sOutput != null) ImageIO.write(oFrameImage, "png", new File(sOutput));       
        return oFrameImage;
    }

    public static Graphics2D paintOnBlackBoard(VelocityVec o, Colorbar oColorbar, Graphics2D g, double dStretchFactor) {

        //VectorArrow v1 = new VectorArrow(o.y, o.x, new OrderedPair(o.getVelocityX(), o.getVelocityY()), iStretchFactor);        
        VectorArrow v1 = new VectorArrow(o.mult2(1.0, "Vy"), dStretchFactor);        

        if (oColorbar != null) {
            g.setColor(oColorbar.getColor(o.opUnitTangent.dValue));
        }

        g.drawLine(v1.dmid[1].intValue(), v1.dmid[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());

        if (v1.drighttip != null && !v1.drighttip[1].isNaN() && !v1.drighttip[0].isNaN()) {
            g.drawLine(v1.drighttip[1].intValue(), v1.drighttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
        }
        if (v1.dlefttip != null && !v1.dlefttip[1].isNaN() && !v1.dlefttip[0].isNaN()) {
            g.drawLine(v1.dlefttip[1].intValue(), v1.dlefttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
        }

        return g;
        
    }
}
