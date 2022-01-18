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
package com.tivconsultancy.opentiv.imageproc.algorithms.algorithms;

import com.tivconsultancy.opentiv.helpfunctions.matrix.MatrixEntry;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageGrid;
import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.imageproc.primitives.ImagePoint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Thomas Ziegenhein
 */
public class N8 implements Serializable {

    private static final long serialVersionUID = -3662377633796688271L;

    ArrayList<Integer> lo = new ArrayList<>();

    public N8() {
    }

    public N8(int iSize) {
        for (int i = 0; i < iSize; i++) {
            lo.add(0);
        }
    }

    public N8(ImageGrid o, ImagePoint pRef) {
        List<ImagePoint> loRef = o.getNeighborsN8(pRef);
        for (ImagePoint p : loRef) {
            if (p != null && p.iValue != null && p.iValue > 0) {
                this.lo.add(1);
            } else {
                this.lo.add(0);
            }
        }
    }

    public N8(ImageInt o, int i, int j) {
        List<MatrixEntry> loRef = o.getNeighborsN8(i, j);
        for (MatrixEntry p : loRef) {
            if (p != null && o.iaPixels[p.i][p.j] > 0) {
                this.lo.add(1);
            } else {
                this.lo.add(0);
            }
        }
    }

//    public N8(ImageGrid oGrid, ImagePoint o){
//        /*
//        Resturn array list with counter clockwise order:
//        3--2--1
//        4-- --0
//        5--6--7
//        
//        Values are null when on boundary
//        */        
//       this.lo = oGrid.getNeighborsN8(o);          
//    }
    public static List<N8> createB1() {
        List<N8> lo = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            N8 o = new N8();
            for (int j = 0; j < 8; j++) {
                if (i == j) {
                    o.lo.add(1);
                } else {
                    o.lo.add(0);
                }

            }
            lo.add(o);
        }
        return lo;
    }

    public static List<N8> createB2() {
        List<N8> lo = new ArrayList<>();
        List<N8> loB1 = createB1();
        for (int i = 0; i < 8; i++) {
            for (N8 oB1 : loB1) {
                N8 o = new N8(8);
                for (int j = 0; j < 8; j++) {
                    if (i == j) {
                        o.lo.set(j, 1);
                    } else {
                        o.lo.set(j, oB1.lo.get(j));
                    }
                }
                lo.add(o);
            }
        }
        return lo;
    }

    public static List<N8> createB3() {
        List<N8> lo = new ArrayList<>();
        List<N8> loB2 = returnUnique2(createB2());
        for (int i = 0; i < 8; i++) {
            for (N8 oB1 : loB2) {
                N8 o = new N8(8);
                for (int j = 0; j < 8; j++) {
                    if (i == j) {
                        o.lo.set(j, 1);
                    } else {
                        o.lo.set(j, oB1.lo.get(j));
                    }
                }
                lo.add(o);
            }
        }
        return lo;
    }

    public static List<N8> createB4() {
        List<N8> lo = new ArrayList<>();
        List<N8> loB2 = returnUnique2(createB3());
        for (int i = 0; i < 8; i++) {
            for (N8 oB1 : loB2) {
                N8 o = new N8(8);
                for (int j = 0; j < 8; j++) {
                    if (i == j) {
                        o.lo.set(j, 1);
                    } else {
                        o.lo.set(j, oB1.lo.get(j));
                    }
                }
                lo.add(o);
            }
        }
        return lo;
    }

    public static List<N8> createB5() {
        List<N8> lo = new ArrayList<>();
        List<N8> loB2 = returnUnique2(createB4());
        for (int i = 0; i < 8; i++) {
            for (N8 oB1 : loB2) {
                N8 o = new N8(8);
                for (int j = 0; j < 8; j++) {
                    if (i == j) {
                        o.lo.set(j, 1);
                    } else {
                        o.lo.set(j, oB1.lo.get(j));
                    }
                }
                lo.add(o);
            }
        }
        return lo;
    }

    public static List<N8> createB6() {
        List<N8> lo = new ArrayList<>();
        List<N8> loB2 = returnUnique2(createB5());
        for (int i = 0; i < 8; i++) {
            for (N8 oB1 : loB2) {
                N8 o = new N8(8);
                for (int j = 0; j < 8; j++) {
                    if (i == j) {
                        o.lo.set(j, 1);
                    } else {
                        o.lo.set(j, oB1.lo.get(j));
                    }
                }
                lo.add(o);
            }
        }
        return lo;
    }

    public static List<N8> createB7() {
        List<N8> lo = new ArrayList<>();
        List<N8> loB2 = returnUnique2(createB6());
        for (int i = 0; i < 8; i++) {
            for (N8 oB1 : loB2) {
                N8 o = new N8(8);
                for (int j = 0; j < 8; j++) {
                    if (i == j) {
                        o.lo.set(j, 1);
                    } else {
                        o.lo.set(j, oB1.lo.get(j));
                    }
                }
                lo.add(o);
            }
        }
        return lo;
    }

    public static void write(List<N8> lo) {
        for (N8 o : lo) {
            System.out.println(o.toString());
            System.out.println("----------------");
        }
    }

    public static List<N8> returnUnique(List<N8> lo) {
        System.out.println(lo.size());
        ArrayList<N8> loUniq = new ArrayList<>();
        loUniq.add(lo.get(0));
        lo.remove(0);
        while (!lo.isEmpty()) {
            List<N8> loRemove = new ArrayList<>();
            List<N8> loAdd = new ArrayList<>();
            outer:
            for (N8 o : lo) {
                boolean beq = false;
                for (N8 oU : loUniq) {
                    if (oU.equals2(o)) {
                        beq = true;
                        break;
                    }
                }
                if (!beq) {
                    loAdd.add(o);
                    loRemove.add(o);
                    break;
                }
                loRemove.add(o);
            }
            loUniq.addAll(loAdd);
            lo.removeAll(loRemove);
        }
        return loUniq;
    }

    public static List<N8> returnUnique2(List<N8> lo) {
        ArrayList<N8> loUniq = new ArrayList<>();
        loUniq.add(lo.get(0));
        lo.remove(0);
        while (!lo.isEmpty()) {
            List<N8> loRemove = new ArrayList<>();
            List<N8> loAdd = new ArrayList<>();
            outer:
            for (N8 o : lo) {
                boolean beq = false;
                for (N8 oU : loUniq) {
                    if (oU.equals3(o)) {
                        beq = true;
                        break;
                    }
                }
                if (!beq) {
                    loAdd.add(o);
                    loRemove.add(o);
                    break;
                }
                loRemove.add(o);
            }
            loUniq.addAll(loAdd);
            lo.removeAll(loRemove);
        }
        return loUniq;
    }

    public int getC2P() {
        int C2P = 0;
        for (int i = 0; i <= 7; i++) {
            C2P = C2P + check_pattern_0110(i);
        }
        return C2P;
    }

    public int getC2E() {
        int C2P = 0;
        for (int i = 0; i <= 7; i++) {
            if (i % 2 != 0) {
                C2P = C2P + check_pattern_01010(i);
            }
        }
        return C2P;
    }

    public int getC2WE() {
        int C2P = 0;
        for (int i = 0; i <= 7; i++) {
            if (i % 2 == 0) {
                C2P = C2P + check_pattern_W101(i);
            }
        }
        return C2P;
    }

    public int getC3P() {
        int C3P = 0;
        for (int i = 0; i <= 7; i++) {
            C3P = C3P + check_pattern_01110(i);
        }
        return C3P;
    }

    public int getC3PC() {
        int C3PC = 0;
        for (int i = 0; i <= 7; i++) {
            C3PC = C3PC + check_pattern_111C(i);
        }
        return C3PC;
    }

    public int getC4P() {
        int C4P = 0;
        for (int i = 0; i <= 7; i++) {
            C4P = C4P + check_pattern_011110(i);
        }
        return C4P;
    }

    public int getC5P() {
        int C5P = 0;
        for (int i = 0; i <= 7; i++) {
            C5P = C5P + check_pattern_0111110(i);
        }
        return C5P;
    }

    public int getBP() {
        int iBP = 0;
        for (Integer o : lo) {
            iBP = iBP + ((o > 0) ? 1 : 0);
        }
        return iBP;
    }

    public int check_pattern_0110(int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;

        if (lo.get(i) == 0 && lo.get(i1) > 0 && lo.get(i2) > 0 && lo.get(i3) == 0) {
            return 1;
        }
        return 0;
    }

    public int check_pattern_01010(int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;
        int i4 = i + 4 > 7 ? i + 3 - 7 : i + 4;

        if (lo.get(i) == 0 && lo.get(i1) > 0 && lo.get(i2) == 0 && lo.get(i3) >= 1 && lo.get(i4) == 0) {
            return 1;
        }
        return 0;
    }

    public int check_pattern_W101(int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;

        if (lo.get(i) > 0 && lo.get(i1) == 0 && lo.get(i2) > 0) {
            return 1;
        }
        return 0;
    }

    public int check_pattern_01110(int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;
        int i4 = i + 4 > 7 ? i + 3 - 7 : i + 4;

        if (lo.get(i) == 0 && lo.get(i1) > 0 && lo.get(i2) > 0 && lo.get(i3) > 0 && lo.get(i4) == 0) {
            return 1;
        }
        return 0;
    }

    public int check_pattern_111C(int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        if (i == 0 || i == 2 || i == 4 || i == 6) {
            return 0;
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;
        int i4 = i + 4 > 7 ? i + 3 - 7 : i + 4;

        if (lo.get(i) == 0 && lo.get(i1) > 0 && lo.get(i2) > 0 && lo.get(i3) > 0 && lo.get(i4) == 0) {
            return 1;
        }
        return 0;

    }

    public int check_pattern_011110(int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;
        int i4 = i + 4 > 7 ? i + 3 - 7 : i + 4;
        int i5 = i + 5 > 7 ? i + 4 - 7 : i + 5;

        if (lo.get(i) == 0 && lo.get(i1) > 0 && lo.get(i2) > 0 && lo.get(i3) > 0 && lo.get(i4) > 0 && lo.get(i5) == 0) {
            return 1;
        }
        return 0;

    }

    public int check_pattern_0111110(int i) {
        if (i > 7) {
            throw new UnsupportedOperationException("");
        }
        int i1 = i + 1 > 7 ? i - 7 : i + 1;
        int i2 = i + 2 > 7 ? i + 1 - 7 : i + 2;
        int i3 = i + 3 > 7 ? i + 2 - 7 : i + 3;
        int i4 = i + 4 > 7 ? i + 3 - 7 : i + 4;
        int i5 = i + 5 > 7 ? i + 4 - 7 : i + 5;
        int i6 = i + 6 > 7 ? i + 5 - 7 : i + 6;

        if (lo.get(i) == 0 && lo.get(i1) > 0 && lo.get(i2) > 0 && lo.get(i3) > 0 && lo.get(i4) > 0 && lo.get(i5) > 0 && lo.get(i6) == 0) {
            return 1;
        }
        return 0;

    }

    public static List<N8> getN8WithSimilarity(List<N8> lo, Similarity O) {
        List<N8> loSim = new ArrayList<>();
        for (N8 o : lo) {
            if (O.isSimilar(o)) {
                loSim.add(o);
            }
        }
        return loSim;
    }

    @Override
    public String toString() {
        String s = "";
        s = s + lo.get(3) + "  " + lo.get(2) + "  " + lo.get(1) + "\n";
        s = s + lo.get(4) + "  " + "ß" + "  " + lo.get(0) + "\n";
        s = s + lo.get(5) + "  " + lo.get(6) + "  " + lo.get(7);
        return s;
    }

    public boolean equals2(Object o) {
        N8 oIn = (N8) o;

        //Horizontal Symmetry
        if (Objects.equals(lo.get(4), oIn.lo.get(4))
                && Objects.equals(lo.get(0), oIn.lo.get(0))
                && Objects.equals(lo.get(3), oIn.lo.get(5)) && Objects.equals(lo.get(5), oIn.lo.get(3))
                && Objects.equals(lo.get(2), oIn.lo.get(6)) && Objects.equals(lo.get(6), oIn.lo.get(2))
                && Objects.equals(lo.get(1), oIn.lo.get(7)) && Objects.equals(lo.get(7), oIn.lo.get(1))) {
            return true;
        }

        //Vertical Symmetry
        if (Objects.equals(lo.get(2), oIn.lo.get(2))
                && Objects.equals(lo.get(6), oIn.lo.get(6))
                && Objects.equals(lo.get(1), oIn.lo.get(3)) && Objects.equals(lo.get(3), oIn.lo.get(1))
                && Objects.equals(lo.get(0), oIn.lo.get(4)) && Objects.equals(lo.get(4), oIn.lo.get(0))
                && Objects.equals(lo.get(7), oIn.lo.get(5)) && Objects.equals(lo.get(5), oIn.lo.get(7))) {
            return true;
        }

        //Diagonal Symmetry 1
        if (Objects.equals(lo.get(3), oIn.lo.get(3))
                && Objects.equals(lo.get(7), oIn.lo.get(7))
                && Objects.equals(lo.get(2), oIn.lo.get(4)) && Objects.equals(lo.get(4), oIn.lo.get(2))
                && Objects.equals(lo.get(1), oIn.lo.get(5)) && Objects.equals(lo.get(5), oIn.lo.get(1))
                && Objects.equals(lo.get(0), oIn.lo.get(6)) && Objects.equals(lo.get(6), oIn.lo.get(0))) {
            return true;
        }

        //Diagonal Symmetry 2
        if (Objects.equals(lo.get(1), oIn.lo.get(1))
                && Objects.equals(lo.get(5), oIn.lo.get(5))
                && Objects.equals(lo.get(2), oIn.lo.get(0)) && Objects.equals(lo.get(0), oIn.lo.get(2))
                && Objects.equals(lo.get(3), oIn.lo.get(7)) && Objects.equals(lo.get(7), oIn.lo.get(3))
                && Objects.equals(lo.get(4), oIn.lo.get(6)) && Objects.equals(lo.get(6), oIn.lo.get(4))) {
            return true;
        }

        //Rot 90°
        if (Objects.equals(lo.get(0), oIn.lo.get(6))
                && Objects.equals(lo.get(1), oIn.lo.get(7))
                && Objects.equals(lo.get(2), oIn.lo.get(0))
                && Objects.equals(lo.get(3), oIn.lo.get(1))
                && Objects.equals(lo.get(4), oIn.lo.get(2))
                && Objects.equals(lo.get(5), oIn.lo.get(3))
                && Objects.equals(lo.get(6), oIn.lo.get(4))
                && Objects.equals(lo.get(7), oIn.lo.get(5))) {
            return true;
        }

        //Rot 180°
        if (Objects.equals(lo.get(0), oIn.lo.get(4))
                && Objects.equals(lo.get(1), oIn.lo.get(5))
                && Objects.equals(lo.get(2), oIn.lo.get(6))
                && Objects.equals(lo.get(3), oIn.lo.get(7))
                && Objects.equals(lo.get(4), oIn.lo.get(0))
                && Objects.equals(lo.get(5), oIn.lo.get(1))
                && Objects.equals(lo.get(6), oIn.lo.get(2))
                && Objects.equals(lo.get(7), oIn.lo.get(3))) {
            return true;
        }

        // Rot 270°
        if (Objects.equals(lo.get(0), oIn.lo.get(2))
                && Objects.equals(lo.get(1), oIn.lo.get(3))
                && Objects.equals(lo.get(2), oIn.lo.get(4))
                && Objects.equals(lo.get(3), oIn.lo.get(5))
                && Objects.equals(lo.get(4), oIn.lo.get(6))
                && Objects.equals(lo.get(5), oIn.lo.get(7))
                && Objects.equals(lo.get(6), oIn.lo.get(0))
                && Objects.equals(lo.get(7), oIn.lo.get(1))) {
            return true;
        }

        //Equals
        if (Objects.equals(lo.get(0), oIn.lo.get(0))
                && Objects.equals(lo.get(1), oIn.lo.get(1))
                && Objects.equals(lo.get(2), oIn.lo.get(2))
                && Objects.equals(lo.get(3), oIn.lo.get(3))
                && Objects.equals(lo.get(4), oIn.lo.get(4))
                && Objects.equals(lo.get(5), oIn.lo.get(5))
                && Objects.equals(lo.get(6), oIn.lo.get(6))
                && Objects.equals(lo.get(7), oIn.lo.get(7))) {
            return true;
        }

        return false;
    }

    public boolean hasIn(int value){
        for(Integer i : lo){
            if(i == value){
                return true;
            }
        }
        return false;
    }
    
        public boolean isNeigh(int value){
            if(lo.get(value) == 1){
                return true;
            }

        return false;
    }

    public boolean equals3(Object o) {
        N8 oIn = (N8) o;

        //Equals
        if (Objects.equals(lo.get(0), oIn.lo.get(0))
                && Objects.equals(lo.get(1), oIn.lo.get(1))
                && Objects.equals(lo.get(2), oIn.lo.get(2))
                && Objects.equals(lo.get(3), oIn.lo.get(3))
                && Objects.equals(lo.get(4), oIn.lo.get(4))
                && Objects.equals(lo.get(5), oIn.lo.get(5))
                && Objects.equals(lo.get(6), oIn.lo.get(6))
                && Objects.equals(lo.get(7), oIn.lo.get(7))) {
            return true;
        }

        return false;
    }

    public static interface Similarity {

        public boolean isSimilar(N8 oCompare);
    }
    
    public  boolean isBorder(){
        for (Integer integer : this.lo) {
            if (integer==0){
                return true;
            }
        }
        return false;
    }

//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 29 * hash + Objects.hashCode(this.lo);
//        return hash;
//    }
}
