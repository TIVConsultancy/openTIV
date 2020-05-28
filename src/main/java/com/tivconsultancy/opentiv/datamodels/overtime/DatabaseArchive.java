/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tivconsultancy.opentiv.datamodels.overtime;

import com.tivconsultancy.opentiv.datamodels.Archive;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public class DatabaseArchive<T extends IndexableResults> extends Database<T>{
    
//    protected IndexDatabase<T> overTime1DResuls;
    protected Archive overTimeRes;
    
    public DatabaseArchive(){
        overTimeRes = new Archive();
//        overTime1DResuls = new IndexDatabase<>();
    }
    
    @Override
    public void setRes(int iStep, T res, boolean refresh){
        overTimeRes.put(iStep, res);
//        overTime1DResuls.replaceOrAdd(iStep, res);
        if(refresh){
            refreshObjects();
        }
    }
    
    @Override
    public void setRes(int iStep, T res){
        setRes(iStep, res, false);
    }
    
    @Override
    public T getRes(int iStep){
        return (T) overTimeRes.get(iStep);
//        return overTime1DResuls.get(iStep);
    }

    @Override
    public IndexDatabase getIndexedResults() {
        throw new UnsupportedOperationException("Not supported, use DatabaseRAM instead");
    }
    
}
