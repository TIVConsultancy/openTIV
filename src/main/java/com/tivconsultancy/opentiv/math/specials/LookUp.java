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
package com.tivconsultancy.opentiv.math.specials;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 * @param <T>
 */
public class LookUp<T> implements Serializable {

    private static final long serialVersionUID = 11234151L;
    
    private List<NameObject<T>> lo;

    public LookUp(List<String> names, List<T> content) {
        for (int i = 0; i < Math.min(names.size(), content.size()); i++) {
            lo.add(new NameObject(names.get(i), content.get(i)));
        }
    }

    public LookUp() {
        lo = new ArrayList<>();
    }

    public int getIndex(NameObject input) {
        if (input == null) {
            return -1;
        }
        for (NameObject o : lo) {
            if (o == null || o.name == null) {
                continue;
            }
            if (o.name.equals(input.name)) {
                return lo.indexOf(o);
            }
        }
        return -1;
    }
    
    public int getIndex(T input) {
        if (input == null) {
            return -1;
        }
        for (NameObject no : lo) {
            if (no == null || no.o == null) {
                continue;
            }
            if (no.o.equals(input)) {
                return lo.indexOf(no);
            }
        }
        return -1;
    }
    
    public NameObject<T> get(int index) {
        return lo.get(index);
    }

    public T get(String input) {
        if (input == null) {
            return null;
        }
        for (NameObject o : lo) {
            if (o == null || o.name == null) {
                continue;
            }
            if (o.name.equals(input)) {
                return (T) o.o;
            }
        }
        return null;
    }
    
    public NameObject getEntry(String input) {
        if (input == null) {
            return null;
        }
        for (NameObject o : lo) {
            if (o == null || o.name == null) {
                continue;
            }
            if (o.name.equals(input)) {
                return o;
            }
        }
        return null;
    }

    public NameObject get(T input) {
        if (input == null) {
            return null;
        }
        for (NameObject o : lo) {
            if (o == null || o.name == null) {
                continue;
            }
            if (o.o.equals(input)) {
                return o;
            }
        }
        return null;
    }

    public void add(NameObject<T> o) {
        lo.add(o);
    }
    
    public void add(NameObject<T> o, int index) {
        if(index < lo.size()){
            lo.add(index, o);
        }else{
            add(o);
        }        
    }
    
    public boolean addDuplFree(NameObject<T> o) {
        if(LookUp.this.get(o.name) == null){
            lo.add(o);
            return true;
        }        
        return false;
    }

    public List<String> getNames() {
        List<String> ls = new ArrayList<>();
        for (NameObject o : lo) {
            ls.add(o.name);
        }
        return ls;
    }
    
    public List<T> getValues() {
        List<T> ls = new ArrayList<>();
        for (NameObject o : lo) {
            ls.add((T) o.o);
        }
        return ls;
    }
    
    public void remove(String name){
        NameObject o = getEntry(name);
        if(o != null){
            lo.remove(o);
        }else{
            throw new TypeNotPresentException(name, new Throwable(name + " - Not found in table"));
        }
    }
    
    public boolean set(String name ,T toSet){
        if(getEntry(name) == null){
            return false;
        }
        getEntry(name).o = toSet;
        return true;
    }
    
    public void setorAdd(String name ,T toSet){
        if(!set(name, toSet)){
            add(new NameObject<>(name, toSet));
        }
    }
    
    public int getSize(){
        return lo.size();
    }
    
    public List<NameObject<T>> getContent(){
        return lo;
    }

}
