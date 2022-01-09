/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tivconsultancy.opentiv.datamodels.overtime;

import com.tivconsultancy.opentiv.datamodels.TempOnDisk;
import com.tivconsultancy.opentiv.logging.TIVLog;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public class DatabaseDisk<T extends DataBaseEntry> extends Database<T>{
    
//    protected IndexDatabase<T> overTime1DResuls;
    protected TempOnDisk overTimeRes;
    
    public DatabaseDisk(){
        this(null);
    }
    
    public DatabaseDisk(File whereToSave){
        overTimeRes = new TempOnDisk(whereToSave);
//        overTime1DResuls = new IndexDatabase<>();
    }
    
    @Override
    public void setRes(String ident, T res, boolean refresh, boolean append){
        try {
            overTimeRes.put(ident, res, append);
        } catch (IOException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "cannot set data to database (Disk) : " + ident, ex);
        }
        if(refresh){
            refreshObjects();
        }
    }
    
    @Override
    public void setRes(String ident, T res){
        setRes(ident, res, false, true);
    }
    
    @Override
    public T getRes(String ident){        
        try {
            return (T) overTimeRes.get(ident);
//        return overTime1DResuls.get(iStep);
        } catch (IOException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot read database from disk: " + ident, ex);
        } catch (ClassNotFoundException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot deserialize object: " + ident, ex);
        }
        return null;
    }

    @Override
    public IndexDatabase getIndexedResults() {
        throw new UnsupportedOperationException("Not supported, use DatabaseRAM instead");
    }
    
    @Override
    public Set<String> getAllKeys() {
        return new LinkedHashSet<>(overTimeRes.getAllFileNamesInFolder());
    }
    
}
