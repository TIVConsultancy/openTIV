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
package com.tivconsultancy.opentiv.imageproc.shapes;

import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Rectangle2 implements Serializable{

    private static final long serialVersionUID = 605952500409976565L;
    public ImagePoint oTopLeft;
    public ImagePoint oTopRight;
    public ImagePoint oBottomLeft;
    public ImagePoint oBottomRight;
    
    public Line2 LineLeft;
    public Line2 LineRight;
    public Line2 LineTop;
    public Line2 LineBottom;
    
    
    public Rectangle2(ImagePoint oTopLeft, ImagePoint oBottomRight){
        this.oBottomLeft = new ImagePoint((int) oTopLeft.getPos().x, (int) oBottomRight.getPos().y, 255, oTopLeft.getGrid());
        this.oTopLeft = oTopLeft;
        this.oTopRight = new ImagePoint((int) oBottomRight.getPos().x, (int) oTopLeft.getPos().y, 255, oTopLeft.getGrid());
        this.oBottomRight = oBottomRight;
        
        this.LineLeft = new Line2(this.oBottomLeft, this.oTopLeft);
        this.LineTop = new Line2(this.oTopLeft, this.oTopRight);
        this.LineRight = new Line2(this.oTopRight, this.oBottomRight);
        this.LineBottom = new Line2(this.oBottomRight, this.oBottomLeft);
    }
    
    public ImageGrid setRectangle(ImageGrid oGrid, int iValue){        
        LineLeft.setLine(oGrid, iValue);
        LineTop.setLine(oGrid, iValue);
        LineRight.setLine(oGrid, iValue);
        LineBottom.setLine(oGrid, iValue);
        return oGrid;
    }
    
}
