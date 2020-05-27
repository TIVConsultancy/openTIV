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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Parallel implements Serializable{

    private static final int NUM_CORES_Max = Runtime.getRuntime().availableProcessors();
    private static final long serialVersionUID = 7418688585504351922L;
    private int NUM_CORES = 2;

    private static final ExecutorService forPool_Max = Executors.newFixedThreadPool(NUM_CORES_Max * 2, (Runnable r) -> new Thread(r));
    private final ExecutorService forPool;
    

    public Parallel(int NUM_CORES) {
        this.NUM_CORES = NUM_CORES;
        forPool = Executors.newFixedThreadPool(this.NUM_CORES * 2, (Runnable r) -> new Thread(r));
    }

    public static <T> void For(final Iterable<T> elements, final Operation<T> operation) {
        try {
            List<Future<Void>> lfutures = forPool_Max.invokeAll(createCallables(elements, operation));
//            for(Future ofuture : lfutures){
//                ofuture.get();
//            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Parallel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public <T> void For3(final Iterable<T> elements, final Operation<T> operation) {
        try {
            List<Future<Void>> lfutures = forPool.invokeAll(createCallables(elements, operation));
//            for(Future ofuture : lfutures){
//                ofuture.get();
//            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Parallel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static <T> void For2(final Iterable<T> elements, final Operation<T> operation) {
        try {
            forPool_Max.invokeAll(createList(elements, operation));
        } catch (InterruptedException ex) {
            Logger.getLogger(Parallel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static <T> Collection<Callable<Void>> createCallables(final Iterable<T> elements, final Operation<T> operation) {
        List<Callable<Void>> callables = new LinkedList<Callable<Void>>();
        for (final T elem : elements) {
            callables.add(new Callable<Void>() {
                @Override
                public Void call() {
                    operation.perform(elem);
                    return null;
                }
            });
        }

        return callables;
    }

    public static <T> List<Callable<Void>> createList(final Iterable<T> elements, final Operation<T> operation) {
        List<Callable<Void>> callables = new ArrayList<Callable<Void>>();
        for (final T elem : elements) {
            callables.add(new Callable<Void>() {
                @Override
                public Void call() {
                    operation.perform(elem);
                    return null;
                }
            });
        }

        return callables;
    }

    public static interface Operation<T> {

        public void perform(T pParameter);
    }
}
