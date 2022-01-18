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
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public interface Shape {

    public double getSize();

    public double getMinorAxis();

    public double getMajorAxis();

    public double getFormRatio();

    public double getOrientationAngle();

    public int getPixelCount();

    public List<MatrixEntry> getlmeList();
    
    public double getGreyDerivative();
    
    public OrderedPair getSubPixelCenter();

}
