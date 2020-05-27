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
package com.tivconsultancy.opentiv.helpfunctions.hpc;

import java.io.Serializable;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StopUnit implements Serializable{

    private static final long serialVersionUID = 395147403072810306L;

    long fStartTime = 0;
    long fEndTime = 0;
    String sName;    
    
    public StopUnit(String sName) {
        this.sName = sName;
        fStartTime = System.currentTimeMillis();
    }

    public long end() {
        if (fEndTime == 0) {
            fEndTime = System.currentTimeMillis();
        }
        return (fEndTime - fStartTime);
    }

    @Override
    public String toString() {
        if (fEndTime > 0) {
            return (fEndTime - fStartTime) + " [ms]";
        } else {
            return "StopUnit " + sName + " running for: " + fStartTime;
        }
    }
    
//    public String toString() {
//        if (fEndTime > 0) {
//            return sName + ": " + (fEndTime - fStartTime) + " [ms]";
//        } else {
//            return "StopUnit " + sName + " running for: " + fStartTime;
//        }
//    }
}
