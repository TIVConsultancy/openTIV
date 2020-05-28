/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tivconsultancy.opentiv.datamodels.overtime;

import com.tivconsultancy.opentiv.datamodels.Archive;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public class DatabaseArchive<T extends DataBaseEntry> extends Database<T>{
    
//    protected IndexDatabase<T> overTime1DResuls;
    protected Archive overTimeRes;
    
    public DatabaseArchive(){
        overTimeRes = new Archive();
//        overTime1DResuls = new IndexDatabase<>();
    }
    
    @Override
    public void setRes(String ident, T res, boolean refresh){
        overTimeRes.put(ident, res);
//        overTime1DResuls.replaceOrAdd(ident, res);
        if(refresh){
            refreshObjects();
        }
    }
    
    @Override
    public void setRes(String ident, T res){
        setRes(ident, res, false);
    }
    
    @Override
    public T getRes(String ident){
        return (T) overTimeRes.get(ident);
//        return overTime1DResuls.get(ident);
    }
    
    @Override
    public IndexDatabase getIndexedResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> getAllKeys() {
        Set<Object> keysObject = overTimeRes.getAllkeys();
        Set<String> keysString = new HashSet<>();
        for(Object o : keysObject){
            if(o != null){
                keysString.add(o.toString());
            }
        }
        return keysString;
    }
    
}
