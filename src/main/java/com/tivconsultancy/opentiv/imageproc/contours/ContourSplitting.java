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
package com.tivconsultancy.opentiv.imageproc.contours;

import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ContourSplitting {

    public static List<CPX> splitByCurvature(int iCurvOrder, int iTangOrder, double dCurvThresh, List<CPX> loInput) {
        List<ImagePoint> loBreakPointsCurv = new ArrayList<>();
        for (CPX o : loInput) {
            if (o.lo.size() < 2 * iCurvOrder) {
                continue;
            }
            for (int i = 0; i < o.lo.size(); i++) {
//                if(Math.abs(o.getCurv(i, iCuvrOrder, iTangOrder)) > 1 || o.islocallyconvex(iConvOrder, i, oHelp) ){
                if (Math.abs(o.getCurv(i, iCurvOrder, iTangOrder)) > dCurvThresh) {
                    loBreakPointsCurv.add(o.lo.get(i));
                }
            }
        }

        for (CPX o : loInput) {
            o.lo.removeAll(loBreakPointsCurv);
        }

        return loInput;
    }

}
