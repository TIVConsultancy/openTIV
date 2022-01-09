/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tivconsultancy.opentiv.datamodels.overtime;

import com.tivconsultancy.opentiv.math.specials.LookUp;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public class DatabaseRAM<T extends DataBaseEntry> extends Database<T>{
    
    protected LookUp<T> overTime1DResuls;    
    
    public DatabaseRAM(){
        overTime1DResuls = new LookUp<>();
    }
    
    @Override
    public void setRes(String ident, T res, boolean refresh, boolean append){
        overTime1DResuls.setorAdd(ident, res);
        if(refresh){
            refreshObjects();
        }
    }
    
    public void setRes(String ident, T res){
        setRes(ident, res, false, true);
    }
    
    public T getRes(String ident){
        return overTime1DResuls.get(ident);
    }
    
    @Override
    public IndexDatabase getIndexedResults() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Set<String> getAllKeys() {
        return new LinkedHashSet<>(overTime1DResuls.getNames());
    }

//    @Override
//    public T getEntry(int index) {
//        return overTime1DResuls.get(index).o;
//    }
//
//    @Override
//    public int getIndex(T o) {
//        return overTime1DResuls.getIndex(o);
//    }
//
//    @Override
//    public List<T> getEntries() {
//        return overTime1DResuls.getValues();
//    }
//
//    @Override
//    public void setWithIndex(String ident, int index, T o) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}
