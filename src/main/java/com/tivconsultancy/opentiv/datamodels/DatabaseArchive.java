/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tivconsultancy.opentiv.datamodels;

import com.tivconsultancy.opentiv.datamodels.IndexableResults;
import com.tivconsultancy.opentiv.datamodels.IndexDatabase;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public class DatabaseArchive<T extends IndexableResults> extends Database<T>{
    
    protected IndexDatabase<T> overTime1DResuls;
    
    public DatabaseArchive(){
        overTime1DResuls = new IndexDatabase<>();
    }

    public IndexDatabase getIndexedResults(){
        return overTime1DResuls;
    }
    
    public void setRes(int iStep, T res){
        overTime1DResuls.replaceOrAdd(iStep, res);
    }
    
    public T getRes(int iStep){
        return overTime1DResuls.get(iStep);
    }
    
}
