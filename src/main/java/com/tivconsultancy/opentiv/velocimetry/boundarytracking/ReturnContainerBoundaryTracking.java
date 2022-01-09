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
package com.tivconsultancy.opentiv.velocimetry.boundarytracking;

import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class ReturnContainerBoundaryTracking implements Serializable {

    private static final long serialVersionUID = -4125210442722832757L;

//    public ImageInt contours1 = null;
//    public ImageInt contours2 = null;
    public List<CPXTr> lCPXTr1 = null;
    public List<CPXTr> lCPXTr2 = null;
    public Map<CPXTr, VelocityVec> velocityVectors = new HashMap<>();

    public ReturnContainerBoundaryTracking(Map<CPXTr, VelocityVec> vectors, List<CPXTr> lCPXTr1, List<CPXTr> lCPXTr2) {
//        this.contours1 = contours1;
//        this.contours2 = contours2;
        this.lCPXTr1=lCPXTr1;
        this.lCPXTr2=lCPXTr2;
        this.velocityVectors = vectors;
    }

}
