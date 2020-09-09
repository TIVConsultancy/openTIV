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
package com.tivconsultancy.opentiv.helpfunctions.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Thomas Ziegenhein
 */
public class StringWorker {

    public static List<String> seperate(Object oContens, String sDeli) {

        List<String> slSeperated = new ArrayList<String>();

        String sContens = oContens.toString();

        Scanner oSampleScanner = new Scanner(sContens).useDelimiter(sDeli);

        while (oSampleScanner.hasNext()) {
            slSeperated.add(oSampleScanner.next());
        }

        return slSeperated;

    }

    public static List<String> replaceString(List<String> lsIn, String sToReplace, String sReplacer) {
        List<String> lsOut = new ArrayList<String>();
        for (String s : lsIn) {
            lsOut.add(s.replace(sToReplace, sReplacer));
        }
        return lsOut;
    }

    public static List<String[]> replaceStringsa(List<String[]> lsIn, String sToReplace, String sReplacer) {
        List<String[]> lsOut = new ArrayList<String[]>();
        for (String[] sa : lsIn) {
            String[] saReplace = new String[sa.length];
            for (int i = 0; i < sa.length; i++) {
                saReplace[i] = sa[i].replace(sToReplace, sReplacer);
            }
            lsOut.add(saReplace);
        }

        return lsOut;
    }

    public static List<Integer> castToInt(List<String> lsInput) {
        List<Integer> liNumbers = new ArrayList<Integer>();

        for (String s : lsInput) {
            liNumbers.add(Integer.valueOf(s));
        }

        return liNumbers;
    }

    public static boolean contains(String sToConsider, List<String> ls) {
        for (String s : ls) {
            if (!sToConsider.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsOr(String sToConsider, List<String> ls) {
        for (String s : ls) {
            if (sToConsider.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsPositive(String sToConsider, List<String> ls) {
        for (String s : ls) {
            if (s.contains(sToConsider)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsPositive(String sToConsider, String[] sa) {
        for (String s : sa) {
            if (s != null && sToConsider != null && sToConsider.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsPositive(String sToConsider, String[] sa, String[] saExcluders) {
        for (String s : sa) {
            if (sToConsider.contains(s)) {
                if (!containsPositive(sToConsider, saExcluders)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> Seperator(Object oContens, String sDeli, List<Integer> liColumn) {

        List<String> slSeperated = new ArrayList<String>();
        List<String> slSeperatedColumn = new ArrayList<String>();

        String sContens = oContens.toString();

        Scanner oSampleScanner = new Scanner(sContens).useDelimiter(sDeli);

        while (oSampleScanner.hasNext()) {
            slSeperated.add(oSampleScanner.next());
        }

        for (Integer i : liColumn) {
            slSeperatedColumn.add(slSeperated.get(i));
        }

        return slSeperatedColumn;

    }

    public static List<String> separate(String sLine, String sDelimiter) {

        List<String> lsTemp = new ArrayList<String>();
        sLine.split(sDelimiter);
        if (sLine != null && !sLine.isEmpty()) {
            Scanner oScanner = new Scanner(sLine);
            oScanner = oScanner.useDelimiter(sDelimiter);

            while (oScanner.hasNext()) {
                lsTemp.add(oScanner.next());
            }
        }

        return lsTemp;

    }

    public static List<String> separate(String sLine, String sDelimiter, int iLimit) {

        Scanner oScanner = new Scanner(sLine);

        oScanner.useDelimiter(sDelimiter);
        List<String> lsTemp = new ArrayList<String>();

        int i = 0;

        while (oScanner.hasNext() && i < iLimit) {
            lsTemp.add(oScanner.next());
            i = i + 1;
        }

        return lsTemp;

    }

    public static List<String> seperateTwoElements(String sLine, String sDelimiter) {

        List<String> sCutToTwo = new ArrayList<String>();

        int iIndexOfDelimiter = sLine.indexOf(sDelimiter);

        sCutToTwo.add(sLine.substring(0, iIndexOfDelimiter));
        if (sLine.length() >= iIndexOfDelimiter + 1) {
            sCutToTwo.add(sLine.substring(iIndexOfDelimiter + 1));
        }

        return sCutToTwo;

    }

    public static String extractorSingle(String Pointer, String delimiter, String sLine) {

        int Pointerlength = Pointer.length();
        int delimiterlength = delimiter.length();
        int index;
        String Extract = new String();

        if (sLine.contains(Pointer)) {
            index = sLine.indexOf(Pointer);
            int i = 1;
            while (!(sLine.substring(index + Pointerlength, index + Pointerlength + i).contains(delimiter))) {
                i++;
                if (index + i >= sLine.length()) {
                    break;
                }
            }
            Extract = (sLine.substring(index + Pointerlength, index + Pointerlength + i - delimiterlength));
            return (Extract);
        } else {
            return (null);
        }
    }

    public static String ReplaceString(String sLine, String Pointer, String delimiter, String sReplacement) {
        if (sLine != null && !sLine.isEmpty() && sLine.contains(Pointer)) {
            String sNewLine = "";

            String sTemporary = extractorSingle(Pointer, delimiter, sLine);

            List<String> slTemporary = cutTwoElements(Pointer, delimiter, sLine);

            for (int i = 0; i < slTemporary.size() - 1; i++) {
                String s = slTemporary.get(i);
                sNewLine = sNewLine + s + sReplacement;
            }

            sNewLine = sNewLine + slTemporary.get(slTemporary.size() - 1);

            return sNewLine;
        }
        return sLine;

    }

    public static int getPositionOfSameString(String s, List<Object> lo) {
        if (s != null && lo != null) {
            for (int i = 0; i < lo.size(); i++) {
                if (s.equals(lo.get(i).toString())) {
                    return i;
                }
            }
        }
        return -1;

    }

    public static List<String> cutTwoElements(String Pointer, String delimiter, String sLine) {

        List<String> slCutToTwo = new ArrayList<String>();

        int index = sLine.indexOf(Pointer);

        slCutToTwo.add(sLine.substring(0, index));
        slCutToTwo.add(sLine.substring(index + delimiter.length()));

        return slCutToTwo;

    }
    
    public static List<String> cutElements2(String sLine, String delimiter) {

        List<String> cutElements = new ArrayList<>();
        
        if(!sLine.contains(delimiter)){
            cutElements.add(sLine);
            return new ArrayList<>();
        }
        String rest = sLine.substring(0, sLine.length());
        int index = rest.indexOf(delimiter);
        while(!rest.isEmpty() && index >=0){
            if(index + 1 >= rest.length()){
                cutElements.add(rest);
                break;
            }
            cutElements.add(rest.substring(0, index));
            rest = rest.substring(index+1, rest.length());
            index = rest.indexOf(delimiter);            
        }
        cutElements.add(rest);
        
        return cutElements;      

    }
    
    public static List<String> cutElements(String delimiter, String sLine) {

        List<String> slCutToTwo = new ArrayList<String>();
        int index1 = sLine.indexOf(delimiter);
        int index2 = sLine.indexOf(delimiter, index1+1);
        slCutToTwo.add(sLine.substring(0, index1));
        if(index2<=0){
            index2 = sLine.length();
            if(index1+1>=index2){
                return slCutToTwo;
            }
            slCutToTwo.add(sLine.substring(index1+1, index2));
            return slCutToTwo;
        }
        slCutToTwo.add(sLine.substring(index1+1, index2));
        while (index2>-1){
            index1 = index2+1;
            try {
            index2 = sLine.indexOf(delimiter, index1+1);
            slCutToTwo.add(sLine.substring(index1, index2));
            }
            catch (IndexOutOfBoundsException e) {
                break;
            }
            
        }
        slCutToTwo.add(sLine.substring(index1, sLine.length()));
//        slCutToTwo.add(sLine.substring(0, index));
//        slCutToTwo.add(sLine.substring(index + delimiter.length()));

        return slCutToTwo;

    }

    public static String[] reshapeC(String[][] oaInput, int iColumn) {
        /**
         * get the column in iColumn M_nm => M_n1
         */
        String[] daExtractedArray = new String[oaInput.length];

        for (int i = 0; i < oaInput.length; i++) {

            //System.out.println(i+ ", " + j+ ", " + iaColumns[j]);
            //System.out.println(daExtractedArray[i][j]);
            daExtractedArray[i] = oaInput[i][iColumn];

        }

        return (daExtractedArray);
    }

    public static String[] getUnique(String[] oaContent) {

        List<String> laUnique = new ArrayList<String>();

        for (int i = 0; i < oaContent.length; i++) {

            if (!(laUnique.contains(oaContent[i]))) {

                laUnique.add(oaContent[i]);

            }

        }

        String[] oaUnique = laUnique.toArray(new String[1]);
        return oaUnique;

    }

    public static String[][] deletRow(String[][] saInput, int iRow) {

        String[][] saDeleteRow = new String[saInput.length - 1][saInput[1].length];

        int j = 0;

        for (int i = 0; i < saInput.length; i++) {

            if (i != iRow) {
                saDeleteRow[j] = saInput[i];
                j++;
            }

        }

        return saDeleteRow;
    }

    public static String[][] Filter(String[][] saInput, int iColumn, String sFilter) {

        List<String[]> lsFilterd = new ArrayList<String[]>();

        for (int i = 0; i < saInput.length; i++) {

            if (saInput[i][iColumn].equals(sFilter)) {
                lsFilterd.add(saInput[i]);
            }

        }

        return lsFilterd.toArray(new String[0][0]);

    }

    public static Double[][] getColumnConvert(String[][] saInput, int[] iaColumn) {

        Double[][] sagetColumn = new Double[saInput.length][iaColumn.length];

        for (int i = 0; i < saInput.length; i++) {

            for (int j = 0; j < iaColumn.length; j++) {

                sagetColumn[i][j] = Double.valueOf(saInput[i][iaColumn[j]]);

            }

        }

        return sagetColumn;

    }

    public static double[][] getColumnConvertPrimi(String[][] saInput) {

        double[][] sagetColumn = new double[saInput.length][saInput[0].length];

        for (int i = 0; i < saInput.length; i++) {

            for (int j = 0; j < saInput[0].length; j++) {

                sagetColumn[i][j] = (double) Double.valueOf(saInput[i][j]);

            }

        }

        return sagetColumn;

    }

    public static Double[][] getColumnConvert(String[][] saInput) {

        Double[][] sagetColumn = new Double[saInput.length][saInput[0].length];

        for (int i = 0; i < saInput.length; i++) {

            for (int j = 0; j < saInput[0].length; j++) {

                try {
                    sagetColumn[i][j] = Double.valueOf(saInput[i][j]);
                } catch (Exception e) {
                    sagetColumn[i][j] = null;
                }

            }

        }

        return sagetColumn;

    }

    public static List<Double[]> getColumnConvert(List<String[]> saInput) {

        List<Double[]> lsda = new ArrayList<Double[]>();

        for (int i = 0; i < saInput.size(); i++) {

            try {
                lsda.add(getColumnConvert(saInput.get(i)));
            } catch (Exception e) {
                lsda.add(null);
            }

        }

        return lsda;

    }

    public static Double[] getColumnConvert(String[] saInput) {

        Double[] sagetColumn = new Double[saInput.length];

        for (int i = 0; i < saInput.length; i++) {

            try {
                sagetColumn[i] = Double.valueOf(saInput[i]);
            } catch (Exception e) {
                sagetColumn[i] = null;
            }
        }
        return sagetColumn;
    }

    public static String clean(String sInput) {

        String sCleaned1 = sInput.replaceAll("\\s", "");
//        sCleaned1 = sCleaned1.replace(".", "");
//        sCleaned1 = sCleaned1.replace(":", "");
        sCleaned1 = sCleaned1.replace("?", "");
        sCleaned1 = sCleaned1.replace("<", "");
        sCleaned1 = sCleaned1.replace(">", "");

        return sCleaned1;
    }

    public static String cleanInteger(String sInput) {

        String sCleaned1 = sInput.replaceAll("\\s", "");
        String sCleaned2 = sCleaned1.replace(".", "");
        String sCleaned3 = sCleaned2.replace(",", "");

        return sCleaned3;
    }

    public static String cleanDouble(String sInput) {

        String sCleaned1 = sInput.replaceAll("\\s", "");
        String sCleaned2 = sCleaned1.replace(",", "");

        return sCleaned2;
    }

    public static List<String> clean(List<String> lsInput) {

        List<String> lsCleaned = new ArrayList<String>();

        for (String s : lsInput) {

            String sCleaned1 = s.replaceAll("\\s", "");
            String sCleaned3 = sCleaned1.replace(".", "");

            lsCleaned.add(sCleaned3);

        }

        return lsCleaned;
    }

    public static List<String> cleanWithPrefix(List<String> lsInput, String sPrefix) {

        List<String> lsCleaned = new ArrayList<String>();

        for (String s : lsInput) {

            String sCleaned1 = s.replaceAll("\\s", "");
            String sCleaned3 = sCleaned1.replace(".", "");

            lsCleaned.add(sPrefix + sCleaned3);

        }

        return lsCleaned;
    }

    public static String LastPathChar(String sInput) {

        String sLastPathChar = "";

        return sLastPathChar;

    }

    public static List<String> getNumbersOfCertainLength(int iLength, int iStart, int iEnd, String sPrefix, String sSuffix) {

        List<String> lsNumbers = new ArrayList<String>();

        for (int i = iStart; i <= iEnd; i++) {

            String sNumber = "";

            for (int j = 0; j < iLength; j++) {

                sNumber = sNumber + "0";

            }

            String sInt = String.valueOf(i);

            char[] caInt = sInt.toCharArray();
            char[] caNumber = sNumber.toCharArray();

            for (int k = 1; k <= sInt.length(); k++) {

                caNumber[caNumber.length - k] = caInt[caInt.length - k];

            }

            sNumber = String.copyValueOf(caNumber);

            sNumber = sPrefix + sNumber + sSuffix;

            lsNumbers.add(sNumber);
        }

        return lsNumbers;

    }

    public static String getNumbersOfCertainLength(int iLength, int iNumber, String sPrefix, String sSuffix) {

        int i = iNumber;

        String sNumber = "";

        for (int j = 0; j < iLength; j++) {

            sNumber = sNumber + "0";

        }

        String sInt = String.valueOf(i);

        char[] caInt = sInt.toCharArray();
        char[] caNumber = sNumber.toCharArray();

        for (int k = 1; k <= sInt.length(); k++) {

            caNumber[caNumber.length - k] = caInt[caInt.length - k];

        }

        sNumber = String.copyValueOf(caNumber);

        sNumber = sPrefix + sNumber + sSuffix;

        return sNumber;

    }

    public static List<String> getNumbersOfCertainLength(int iLength, int iStart, int iEnd, int iLeap, String sPrefix, String sSuffix) {

        List<String> lsNumbers = new ArrayList<String>();

        for (int i = iStart; i <= iEnd; i = i + iLeap) {

            String sNumber = "";

            for (int j = 0; j < iLength; j++) {

                sNumber = sNumber + "0";

            }

            String sInt = String.valueOf(i);

            char[] caInt = sInt.toCharArray();
            char[] caNumber = sNumber.toCharArray();

            for (int k = 1; k <= sInt.length(); k++) {

                caNumber[caNumber.length - k] = caInt[caInt.length - k];

            }

            sNumber = String.copyValueOf(caNumber);

            sNumber = sPrefix + sNumber + sSuffix;

            lsNumbers.add(sNumber);
        }

        return lsNumbers;

    }

    public static List<String> getNumbersOfCertainLength(int iLength, int iStart, int iEnd, int iLeap, int iBurst, String sPrefix, String sSuffix) {

        List<String> lsNumbers = new ArrayList<String>();

        for (int i = iStart; i < iEnd; i = i + iLeap + iBurst) {

            lsNumbers.addAll(
                    getNumbersOfCertainLength(iLength, i, i + iBurst, sPrefix, sSuffix)
            );
        }

        return lsNumbers;

    }
}
