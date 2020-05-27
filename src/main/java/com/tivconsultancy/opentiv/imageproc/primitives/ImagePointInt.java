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
package com.tivconsultancy.opentiv.imageproc.primitives;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class ImagePointInt extends MatrixEntry implements Serializable{

    private static final long serialVersionUID = -6921653534671180391L;

    public boolean bMarker = false;

    public ImagePointInt(int i, int j) {
        super(i, j);
    }

    public ImagePointInt(MatrixEntry me) {
        super(me.i, me.j);
    }

    public ImagePointInt(ImagePoint op) {
        super(op.getPos().y, op.getPos().x);
    }

    @Override
    public boolean equals(Object o) {
        if(this == null || o == null){
            return false;
        }
        if (!(o instanceof ImagePointInt)) {
            return false;
        }
        if (((ImagePointInt) o).i == this.i
                && ((ImagePointInt) o).j == this.j) {
            return true;
        }
        return false;
    }

}
