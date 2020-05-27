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

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Rectangle implements Serializable{

    private static final long serialVersionUID = -8912756112435594630L;
    public MatrixEntry oTopLeft;
    public MatrixEntry oTopRight;
    public MatrixEntry oBottomLeft;
    public MatrixEntry oBottomRight;
    
    public Line LineLeft;
    public Line LineRight;
    public Line LineTop;
    public Line LineBottom;
    
    
    public Rectangle(MatrixEntry oTopLeft, MatrixEntry oBottomRight){
        this.oBottomLeft = new MatrixEntry(oBottomRight.i, oTopLeft.j);
        this.oTopLeft = oTopLeft;
        this.oTopRight = new MatrixEntry( oTopLeft.i, oBottomRight.j);
        this.oBottomRight = oBottomRight;
        
        this.LineLeft = new Line(this.oBottomLeft, this.oTopLeft);
        this.LineTop = new Line(this.oTopLeft, this.oTopRight);
        this.LineRight = new Line(this.oTopRight, this.oBottomRight);
        this.LineBottom = new Line(this.oBottomRight, this.oBottomLeft);
    }
    
    public ImageInt setRectangle(ImageInt oGrid, int iValue){        
        LineLeft.setLine(oGrid, iValue);
        LineTop.setLine(oGrid, iValue);
        LineRight.setLine(oGrid, iValue);
        LineBottom.setLine(oGrid, iValue);
        return oGrid;
    }
    
}
