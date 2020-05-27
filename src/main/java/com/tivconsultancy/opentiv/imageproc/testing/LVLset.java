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
package com.tivconsultancy.opentiv.imageproc.testing;

import com.tivconsultancy.opentiv.helpfunctions.io.Writer;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.LevelSet;
import com.tivconsultancy.opentiv.imageproc.algorithms.algorithms.LevelSet.ImageGridDouble;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class LVLset {
    public static void testFarstMarching() throws IOException{
        ImageGrid o = IMG_Reader.readImageGrey(new File("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Tests\\LevelSet\\FastMarching\\Test1.jpg"));
        o.binarize(127);
        ImageGridDouble lvlGrid =  LevelSet.fastMarching(o);
        lvlGrid = LevelSet.signLevelSet(lvlGrid);
        List<OrderedPair> lopOut = new ArrayList<>();
        for(ImagePoint op : lvlGrid.oa){
            lopOut.add(lvlGrid.getPosDouble(op));
        }
        Writer.WriteWithValue("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Tests\\LevelSet\\FastMarching\\Out.csv", lopOut, true, null);
        lopOut.clear();
        
        ImageGridDouble oGridCurv = LevelSet.getCurvatureClassic(lvlGrid);
        
        for(ImagePoint op : oGridCurv.oa){
            lopOut.add(lvlGrid.getPosDouble(op));
        }
        Writer.WriteWithValue("E:\\Sync\\openTIV\\ClosedTIV\\BoundaryTracking\\Tests\\LevelSet\\FastMarching\\Curv.csv", lopOut, true, null);
        
    }
}
