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

package com.tivconsultancy.opentiv.datamodels.overtime;

import com.tivconsultancy.opentiv.datamodels.Refreshable;
import com.tivconsultancy.opentiv.logging.TIVLog;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.application.Platform;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public abstract class Database<T extends IndexableResults> {

    private final transient List<Refreshable> ObjectsToCallOnChange = new ArrayList<>();
    
    public abstract void setRes(int iStep, T res);
    public abstract void setRes(int iStep, T res, boolean refresh);
    public abstract T getRes(int iStep);
    public abstract IndexDatabase getIndexedResults();
    
    public void addObjectToRefresh(Refreshable ref) {
        ObjectsToCallOnChange.add(ref);
    }
    
    public void refreshObjects() {
        Platform.runLater(new Runnable() {

            public void run() {
                for (Refreshable r : ObjectsToCallOnChange) {
                    try {
                        r.refresh();
                    } catch (Exception e) {
                        TIVLog.tivLogger.log(Level.WARNING, "Cannot refresh object: " + r.toString(), e);
                    }   
                }
            }
        });
    }
    
}
