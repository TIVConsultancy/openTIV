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
package com.tivconsultancy.opentiv.velocimetry.helpfunctions;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import java.io.Serializable;


/**
 *
 * @author Thomas Ziegenhein
 */
public class MatrixEntryWithDouble extends MatrixEntry implements Serializable {
    
    private static final long serialVersionUID = 112480151L;
    
    public double x = 0;
    public double y = 0;
    public MatrixEntryWithDouble(){
        super(0,0);
    }
    
    public MatrixEntryWithDouble(MatrixEntry me){
        super(me.i,me.j);
    }
            
    public MatrixEntryWithDouble(int i, int j){
        super(i, j);
        this.x = j;
        this.y = i;
    }
    
    public MatrixEntryWithDouble(double x, double y){
        super((int) x, (int) y);
        this.x = x;
        this.y = y;
    }
}
