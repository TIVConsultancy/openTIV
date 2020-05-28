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

import com.tivconsultancy.opentiv.math.specials.LookUp;
import com.tivconsultancy.opentiv.math.specials.NameObject;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public class IndexDatabase<T extends IndexableResults> {

    private LookUp<T> resultsOverTime;    

    public IndexDatabase() {
        initDatabase();
    }

    private void initDatabase() {
        resultsOverTime = new LookUp<>();
    }
    
    private void replaceOrAdd(String name, T res) {
        if (resultsOverTime.get(name) == null) {
            resultsOverTime.add(new NameObject<>(name, res));
        } else {
            resultsOverTime.set(name, res);
        }

    }

    public void replaceOrAdd(int i, T res) {
        this.replaceOrAdd(i + "", res);
    }

    private void add(String name, T res) {
        resultsOverTime.add(new NameObject<>(name, res));
    }

    public void add(int i, T res) {
        this.add(i + "", res);
    }

    public T get(String s) {
        return resultsOverTime.get(s);
    }

    public T get(int i) {
        return this.get(i + "");
    }
    
    public T getIndexBased(int i) {
        return resultsOverTime.get(i).o;
    }
    
    public int getEntry(int i) {
        return Integer.valueOf(resultsOverTime.get(i).name);
    }
//    
//    public Double getIndexBased(int i, String name) {
//        if(this.getIndexBased(i) == null || this.getIndexBased(i).getRes(name) == null){
//            return 0.0;
//        }
//        return this.getIndexBased(i).getRes(name);
//    }
//
//    public Double get(int i, String name) {
//        if(this.get(i) == null || this.get(i).getRes(name) == null){
//            return 0.0;
//        }
//        return this.get(i).getRes(name);
//    }

    public int getSize() {
        return resultsOverTime.getSize();
    }
//
//    public void refreshObjects() {
//        Platform.runLater(new Runnable() {
//
//            public void run() {
//                for (Refreshable r : ObjectsToCallOnChange) {
//                    try {
//                        r.refresh();
//                    } catch (Exception e) {
//                        TIVLog.tivLogger.log(Level.SEVERE, "Cannot refresh object: " + r.toString(), e);
//                    }                    
//                }
//            }
//        });
//    }

}
