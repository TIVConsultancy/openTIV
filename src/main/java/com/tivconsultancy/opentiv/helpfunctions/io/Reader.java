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
package com.tivconsultancy.opentiv.helpfunctions.io;


import com.sun.javafx.iio.common.ImageTools;
import com.tivconsultancy.opentiv.helpfunctions.matrix.Matrix;
import com.tivconsultancy.opentiv.helpfunctions.strings.StringWorker;
import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Reader {

    public Boolean Error = false;
    protected String sFile;
    protected File fFile;
    public String sSeperator;
    private int iFileExist = 0;
    public BufferedReader oReaderObject;
    protected String sFirstLine;
    protected List<String> lsFileContens = new ArrayList<String>();
    protected List<String[]> lsFileContensString = new ArrayList<String[]>();
    protected Double[][] adFileContens;
    public int iStartheader = 1;
    public String sDecimalSeparator = ".";

    public Reader(String sFile) {
        this.sFile = sFile;
        this.sSeperator = ",";
        this.fFile = new File(this.sFile);
        if (fFile.exists()) {
            this.iFileExist = 1;
        } else {
            System.out.println("File not found:  " + sFile);
            Error = true;
        }

    }

    public Reader(File fFile) {
        this.fFile = fFile;
        this.sSeperator = ",";
        if (fFile.exists()) {
            this.iFileExist = 1;
            this.sFile = this.fFile.getAbsolutePath();
        } else {
            System.out.println("File not found:  " + fFile);
            Error = true;
        }
    }

    public void close() throws IOException {
        if (this.oReaderObject != null) {
            this.oReaderObject.close();
        }
    }

    public void setSeperator(String sSeperator) {
        this.sSeperator = sSeperator;
    }

    public void setFile(File fFile) {
        this.fFile = fFile;
        if (fFile.exists()) {
            this.iFileExist = 1;
        } else {
            System.out.println("File not found:  " + fFile);
            Error = true;
        }
    }

    public void readToCommandLine() throws IOException {

        int iBufferCheck = 0;

        if (this.iFileExist == 1) {
            try {
                oReaderObject = new BufferedReader(new FileReader(this.fFile));
                iBufferCheck = 1;
            } catch (FileNotFoundException ex) {
                System.out.println("can not read from Data");
                Error = true;
            }
        } else {
            System.out.println("File not exist");
            Error = true;
        }

        if (iBufferCheck == 1) {
            for (String line; (line = oReaderObject.readLine()) != null;) {
            }
        }

    }

    public String readFirstLine() throws IOException {
        if (getBuffer() == 1) {
            for (int k = 0; k < iStartheader - 1; k++) {

                oReaderObject.readLine();

            }
            this.sFirstLine = oReaderObject.readLine();
            if (sFirstLine == null) {
                System.out.println("Warning: File empty");
            }
        }

        return (this.sFirstLine);

    }

    private String readFirstLine(int iBuffercheck) throws IOException {

        if (iBuffercheck == 1) {

            for (int k = 0; k < iStartheader - 1; k++) {

                oReaderObject.readLine();

            }

            this.sFirstLine = oReaderObject.readLine();
            if (sFirstLine == null) {
                System.out.println("Warning: File empty");
            }
        }

        return (this.sFirstLine);

    }

    public List<String> readFile() throws IOException {

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);
            this.lsFileContens.add(this.sFirstLine);

            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                this.lsFileContens.add(line);

            }
        }
        return (this.lsFileContens);

    }

    public List<String> readFileBlank() throws IOException {

        if (getBuffer() == 1) {

            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                this.lsFileContens.add(line);

            }
        }
        return (this.lsFileContens);

    }

    public List<String> readFileLine(int iStart) throws IOException {

        if (getBuffer() == 1) {

            //this.sFirstLine = readFirstLine(1);
            //this.lsFileContens.add(this.sFirstLine);
            int iCounter = 0;
            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                if (iCounter >= iStart) {
                    this.lsFileContens.add(line);
                } else {
                    iCounter++;
                }

            }
        }
        this.oReaderObject.close();
        return (this.lsFileContens);

    }

    public List<String[]> readFileStringa() throws IOException {

        if (getBuffer() == 1) {

            //this.sFirstLine = readFirstLine(1);
            //this.lsFileContens.add(this.sFirstLine);
            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                Scanner oScanner = new Scanner(line);

                oScanner.useDelimiter(this.sSeperator);

                List<String> lsTemp = new ArrayList<String>();

                while (oScanner.hasNext()) {
                    lsTemp.add(oScanner.next());

                }

                this.lsFileContensString.add(lsTemp.toArray(new String[2]));
                //this.lsFileContens.add(line);

            }
        }
        this.oReaderObject.close();
        return (this.lsFileContensString);

    }
    
    public List<String[]> readFileStringa2() throws IOException {

        if (getBuffer() == 1) {

            //this.sFirstLine = readFirstLine(1);
            //this.lsFileContens.add(this.sFirstLine);
            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                List<String> lsTemp = StringWorker.cutElements(this.sSeperator, line);

                this.lsFileContensString.add(lsTemp.toArray(new String[2]));
                //this.lsFileContens.add(line);

            }
        }
        this.oReaderObject.close();
        return (this.lsFileContensString);

    }

    public List<Double[]> readBigFiletoDouble(int iSeperationColumn, int iXCOl, int iYCol, int iValueCol) throws IOException {
        List<Double[]> sContens = new ArrayList<Double[]>();

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);
//            sContens.add(this.sFirstLine);
//            String[] sTemp = new String[4];
            for (String line; (line = this.oReaderObject.readLine()) != null;) {
//                Scanner oScanner = new Scanner(line);
                List<String> lsTemp = StringWorker.cutElements(this.sSeperator, line);
//                oScanner.useDelimiter(this.sSeperator);
//                int icounter = 0;
//                while (oScanner.hasNext()) {
//                    lsTemp.add(oScanner.next());
//                    String s = oScanner.next();
//                    if (icounter == iXCOl || icounter == iYCol || icounter == iSeperationColumn || icounter == iValueCol) {
//                        dCont[icounter] = Double.valueOf(s);
//                    }
//                    icounter++;
//                }
                String[] sTemp = lsTemp.toArray(new String[1]);
//                Double[] dCont = {Double.valueOf(sTemp[iXCOl]), Double.valueOf(sTemp[iYCol]), Double.valueOf(sTemp[iValueCol]), Double.valueOf(sTemp[iSeperationColumn])};
                Double dX = Double.valueOf(lsTemp.get(iXCOl));
                Double dY = Double.valueOf(lsTemp.get(iYCol));
                Double dValue = Double.valueOf(lsTemp.get(iValueCol));
                Double dSp = Double.valueOf(lsTemp.get(iSeperationColumn));
                if (dX != null && !dX.isInfinite() && !dX.isNaN()
                        && dY != null && !dY.isInfinite() && !dY.isNaN()
                        && dValue != null && !dValue.isInfinite() && !dValue.isNaN()
                        && dSp != null && !dSp.isInfinite() && !dSp.isNaN()) {
                    Double[] dCont = {dX, dY, dValue, dSp};
                    sContens.add(dCont);
                }
            }
        }
        return sContens;
    }        

    public List<String[]> readBigFilelowMem() throws IOException {
        List<String[]> sContens = new ArrayList<String[]>();

        if (getBuffer() == 1) {

//            this.sFirstLine = readFirstLine(1);
//            sContens.add(this.sFirstLine);
            for (String line; (line = this.oReaderObject.readLine()) != null;) {                
                List<String> lsTemp = StringWorker.cutElements(this.sSeperator, line);
                String[] sTemp = lsTemp.toArray(new String[1]);
                sContens.add(sTemp);
            }
        }
        return sContens;
    }

    public String[] readLine(int iLine) throws IOException {

        if (getBuffer() == 1) {

            //this.sFirstLine = readFirstLine(1);
            //this.lsFileContens.add(this.sFirstLine);
            int iCounter = 0;
            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                Scanner oScanner = new Scanner(line);

                oScanner.useDelimiter(this.sSeperator);

                List<String> lsTemp = new ArrayList<String>();

                while (oScanner.hasNext()) {
                    lsTemp.add(oScanner.next());

                }

                if (iCounter == iLine) {
                    this.oReaderObject.close();
                    return lsTemp.toArray(new String[1]);
                }
                iCounter++;

            }
        }
        this.oReaderObject.close();
        return null;

    }

    public List<String[]> readFileStringaNoDelimitersNoEmpty() throws IOException {

        if (getBuffer() == 1) {

            //this.sFirstLine = readFirstLine(1);
            //this.lsFileContens.add(this.sFirstLine);
            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                Scanner oScanner = new Scanner(line);

                oScanner.useDelimiter(this.sSeperator);

                List<String> lsTemp = new ArrayList<String>();

                while (oScanner.hasNext()) {
                    String sT = oScanner.next();
                    if (!sT.equals(this.sSeperator) && !sT.isEmpty()) {
                        lsTemp.add(sT);
                    }

                }

                this.lsFileContensString.add(lsTemp.toArray(new String[2]));
                //this.lsFileContens.add(line);

            }
        }
        this.oReaderObject.close();
        return (this.lsFileContensString);

    }

    public List<String[]> readFileStringaWhiteSpace() throws IOException {

        if (getBuffer() == 1) {

            //this.sFirstLine = readFirstLine(1);
            //this.lsFileContens.add(this.sFirstLine);
            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                this.lsFileContensString.add(line.split("\\s+"));
                //this.lsFileContens.add(line);

            }
        }
        this.oReaderObject.close();
        return (this.lsFileContensString);

    }

    public List<String[]> readFileStringa(int iColCount) throws IOException {

        if (getBuffer() == 1) {

            int iColNumber = iColCount;

            //this.sFirstLine = readFirstLine(1);
            //this.lsFileContens.add(this.sFirstLine);
            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                Scanner oScanner = new Scanner(line);

                oScanner.useDelimiter(this.sSeperator);

                String[] asTemp = new String[iColNumber];

                for (int j = 0; j < iColNumber; j++) {

                    if (oScanner.hasNext()) {
                        asTemp[j] = oScanner.next();

                    }

                }

                this.lsFileContensString.add(asTemp);
                //this.lsFileContens.add(line);

            }
        }
        return (this.lsFileContensString);

    }

    public String readFileInOneString() throws IOException {

        String sContens = "";

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);
            sContens = sContens + this.sFirstLine;

            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                sContens = sContens + line;

            }

        }
        return (sContens);

    }

    public String getRow(int iRow) throws IOException {

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);
            int iCounter = 0;

            if (iCounter == iRow) {
                return this.sFirstLine;
            }

            for (String line; (line = this.oReaderObject.readLine()) != null;) {
                iCounter++;
                if (iCounter == iRow) {
                    return line;
                }

            }

        }

        return null;
    }

    public int getRowCount() throws IOException {

        int iCounter = 1;

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);
            for (String line; (line = this.oReaderObject.readLine()) != null;) {
                iCounter++;
            }

        }

        return iCounter;
    }

    public List<String> readBigFileInListString() throws IOException {

        List<String> sContens = new ArrayList<String>();

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);
            sContens.add(this.sFirstLine);

            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                sContens.add(line);
            }

        }
        return (sContens);

    }

    public List<String[]> readBigFileInListStringSeperate() throws IOException {

        List<String[]> sContens = new ArrayList<String[]>();

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);

            sContens.add(StringWorker.separate(this.sFirstLine, sSeperator).toArray(new String[2]));

            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                sContens.add(StringWorker.separate(line, sSeperator).toArray(new String[2]));
            }

        }
        return (sContens);

    }

    public List<String[]> readBigFileInListStringSeperate(int iHeaderLine) throws IOException {

        List<String[]> sContens = new ArrayList<String[]>();

        if (getBuffer() == 1) {
            this.sFirstLine = readFirstLine(1);

            if (iHeaderLine == 0) {
                sContens.add(StringWorker.separate(this.sFirstLine, sSeperator).toArray(new String[2]));
            } else {
                int iCounter = 1;
                for (String line; (line = this.oReaderObject.readLine()) != null; iCounter++) {
                    if (iHeaderLine == iCounter) {
                        sContens.add(StringWorker.separate(line, sSeperator).toArray(new String[2]));
                        break;
                    }
                }
            }

            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                sContens.add(StringWorker.separate(line, sSeperator).toArray(new String[2]));

            }

        }
        return (sContens);

    }

    public List<String[]> readBigFileInListStringParsing(int iHeaderLine, String sDelims) throws IOException {

        List<String[]> sContens = new ArrayList<String[]>();

        if (getBuffer() == 1) {

            this.sFirstLine = readFirstLine(1);

            if (iHeaderLine == 0) {
                sContens.add(StringWorker.separate(this.sFirstLine, sSeperator).toArray(new String[2]));
            } else {
                int iCounter = 1;
                for (String line; (line = this.oReaderObject.readLine()) != null; iCounter++) {
                    if (iHeaderLine == iCounter) {
                        int i = 0;

                        while (line.subSequence(i, i + 1).equals(" ")) {
                            i++;
                        }

                        sContens.add(line.substring(i).split(sDelims));
                        break;
                    }
                }
            }

            for (String line; (line = this.oReaderObject.readLine()) != null;) {

                int i = 0;

                while (line.subSequence(i, i + 1).equals(" ")) {
                    i++;
                }

                sContens.add(line.substring(i).split(sDelims));
            }

        }
        return (sContens);

    }

    public double[][] readBigFilePrimi() throws IOException {

        List<String> lsContent = readBigFileInListString();

        List<String[]> lsaContent = new ArrayList<String[]>();

        for (String ls : lsContent) {

            lsaContent.add(StringWorker.seperate(ls, this.sSeperator).toArray(new String[3]));

        }

        String[][] saContent = lsaContent.toArray(new String[3][3]);

        return StringWorker.getColumnConvertPrimi(saContent);

    }

    public double[][] readBigFilePrimi(int iStart, List<Integer> liColumn) throws IOException {

        List<String> lsContent = readBigFileInListString();

        lsContent = new ArrayList<String>(lsContent.subList(iStart, lsContent.size()));

        List<String[]> lsaContent = new ArrayList<String[]>();

        for (String ls : lsContent) {

            lsaContent.add(StringWorker.Seperator(ls, this.sSeperator, liColumn).toArray(new String[3]));

        }

        String[][] saContent = lsaContent.toArray(new String[3][3]);

        return StringWorker.getColumnConvertPrimi(saContent);

    }

    public Double[][] readBigFile(int iStart) throws IOException {

        List<String> lsContent = readBigFileInListString();

        if (lsContent.size() < iStart) {
            return null;
        }

        lsContent = new ArrayList<String>(lsContent.subList(iStart, lsContent.size()));

        List<String[]> lsaContent = new ArrayList<String[]>();

        for (String ls : lsContent) {

            lsaContent.add(StringWorker.seperate(ls, this.sSeperator).toArray(new String[2]));

        }

        String[][] saContent = lsaContent.toArray(new String[2][2]);
//        this.close();

        return StringWorker.getColumnConvert(saContent);

    }

    public Double[][] readBigFile(int iStart, List<Integer> liColumns) throws IOException {
        
        throw new UnsupportedOperationException("Revise");

//        Double iMaxCol = Organize.getMax(liColumns);
//
//        List<String> lsContent = readBigFileInListString();
//
//        lsContent = new ArrayList<String>(lsContent.subList(iStart, lsContent.size()));
//
//        List<String[]> lsaContent = new ArrayList<String[]>();
//
//        for (String ls : lsContent) {
//
//            String[] sa = StringWorker.seperate(ls, this.sSeperator).toArray(new String[3]);
//            String[] saOnlyCol = new String[liColumns.size()];
//
//            if (sa.length < iMaxCol) {
//                return null;
//            }
//
//            int iCount = 0;
//            for (Integer i : liColumns) {
//                saOnlyCol[iCount] = sa[i];
//                iCount++;
//            }
//
//            lsaContent.add(saOnlyCol);
//
//        }
//
//        String[][] saContent = lsaContent.toArray(new String[3][3]);
//
//        return StringWorker.getColumnConvert(saContent);

    }

    public double[][] readBigFileprimi(int iStart) throws IOException {

        List<String> lsContent = readBigFileInListString();

        lsContent = new ArrayList<String>(lsContent.subList(iStart, lsContent.size()));

        List<String[]> lsaContent = new ArrayList<String[]>();

        for (String ls : lsContent) {

            lsaContent.add(StringWorker.seperate(ls, this.sSeperator).toArray(new String[3]));

        }

        String[][] saContent = lsaContent.toArray(new String[3][3]);

        return StringWorker.getColumnConvertPrimi(saContent);

    }

    public Double[][] readBigFile() throws IOException {

        List<String> lsContent = readBigFileInListString();

        List<String[]> lsaContent = new ArrayList<String[]>();

        for (String ls : lsContent) {

            lsaContent.add(StringWorker.seperate(ls, this.sSeperator).toArray(new String[3]));

        }

        String[][] saContent = lsaContent.toArray(new String[3][3]);

        return StringWorker.getColumnConvert(saContent);

    }

    public Double[][] getNumber() throws IOException {

        readFile();
        adFileContens = new Double[this.lsFileContens.size() - 1][getColumnNumber()];
        for (int i = 0; i < ((int) this.lsFileContens.size()) - 1; i++) {
            Scanner oScanner = new Scanner(this.lsFileContens.get(i + 1));
            oScanner.useDelimiter(this.sSeperator);

            for (int j = 0; j < getColumnNumber(); j++) {

                if (oScanner.hasNext()) {
                    adFileContens[i][j] = Double.valueOf(oScanner.next());

                }
            }

        }

        return (this.adFileContens);

    }

    public Double[][] getNumber(int iTextRow) throws IOException {

        //this.iStartheader = iTextRow;
        readFile();
        this.adFileContens = new Double[this.lsFileContens.size() - iTextRow][getColumnNumber()];
        for (int i = 0; i < ((int) this.lsFileContens.size()) - iTextRow; i++) {
            Scanner oScanner = new Scanner(this.lsFileContens.get(i + iTextRow));

            oScanner.useDelimiter(this.sSeperator);

            for (int j = 0; j < getColumnNumber(); j++) {

                if (oScanner.hasNext()) {
                    adFileContens[i][j] = Double.valueOf(oScanner.next());

                }
            }

            //iTextRow=iTextRow+1;
        }

        return (this.adFileContens);

    }

    public Double[][] getNumber(int iTextRow, int iHeader) throws IOException {

        this.iStartheader = iHeader;
        readFile();
        this.adFileContens = new Double[this.lsFileContens.size() - iTextRow][getColumnNumber()];
        for (int i = 0; i < ((int) this.lsFileContens.size()) - iTextRow; i++) {
            Scanner oScanner = new Scanner(this.lsFileContens.get(i + iTextRow));
            oScanner.useDelimiter(this.sSeperator);

            for (int j = 0; j < getColumnNumber(); j++) {

                if (oScanner.hasNext()) {
                    String s = oScanner.next();
                    if (s.contains("null")) {
                        adFileContens[i][j] = null;
                    } else {
                        adFileContens[i][j] = Double.valueOf(s);
                    }

                }

            }

            //iTextRow=iTextRow+1;
        }

        return (this.adFileContens);

    }

    public Double[][] getNumberParsing(int iTextRow, int iHeader, String sDelims) throws IOException {

        this.iStartheader = iHeader;
        readFile();
        int iColNumber = (lsFileContens.get(iTextRow + iHeader).split(sDelims)).length;
        if (lsFileContens.get(iTextRow + iHeader).split(sDelims)[0].isEmpty()) {
            iColNumber--;
        }
        this.adFileContens = new Double[this.lsFileContens.size() - (iTextRow + iHeader)][iColNumber];
        for (int i = iTextRow + iHeader; i < ((int) this.lsFileContens.size()); i++) {

            String[] sa = lsFileContens.get(i).split(sDelims);
            int iShift = 0;
            if (sa[0].isEmpty()) {
                iShift = 1;
            }

            for (int j = 0; j < iColNumber; j++) {

                this.adFileContens[i - iTextRow - iHeader][j] = Double.valueOf(sa[j + iShift]);

            }

            //iTextRow=iTextRow+1;
        }

        return (this.adFileContens);

    }

    public double[][] getNumberPrim(int iTextRow, int iHeader) throws IOException {

        this.iStartheader = iHeader;
        readFile();
        double[][] daFileContens = new double[this.lsFileContens.size() - iTextRow][getColumnNumber()];
        for (int i = 0; i < ((int) this.lsFileContens.size()) - iTextRow; i++) {
            Scanner oScanner = new Scanner(this.lsFileContens.get(i + iTextRow));
            //System.out.println(this.lsFileContens.get(i));
            oScanner.useDelimiter(this.sSeperator);

            for (int j = 0; j < getColumnNumber(); j++) {

                if (oScanner.hasNext()) {
                    daFileContens[i][j] = (double) Double.valueOf(oScanner.next());
                }

            }

            //iTextRow=iTextRow+1;
        }

        return (daFileContens);

    }

    public static List<Double[][]> getNumber(String[] saFilePaths, int iTextRow, int iHeader) throws IOException {

        List<Double[][]> ldaReadNumbers = new ArrayList<Double[][]>(saFilePaths.length);

        for (int i = 0; i < saFilePaths.length; i++) {

            Reader oTempReader = new Reader(saFilePaths[i]);

            ldaReadNumbers.add(oTempReader.getNumber(iTextRow, iHeader));

        }

        return ldaReadNumbers;

    }

    public static List<Double[][]> getNumber(String[] saFilePaths, int iTextRow, int iHeader, int[] iaColumns) throws IOException {
        
        throw new UnsupportedOperationException("Revise");

//        List<Double[][]> ldaReadNumbers = new ArrayList<Double[][]>(saFilePaths.length);
//
//        for (int i = 0; i < saFilePaths.length; i++) {
//
//            Reader oTempReader = new Reader(saFilePaths[i]);
//
//            Double[][] datemp = oTempReader.getNumber(iTextRow, iHeader);
//
//            ldaReadNumbers.add(Matrix.reshape(datemp, iaColumns));
//
//        }
//
//        return ldaReadNumbers;

    }

    public static Double[][] getNumber(String sFilePath, int iTextRow, int iHeader, int[] iaColumns) throws IOException {
        
        throw new UnsupportedOperationException("Revise");

//        Double[][] daReadNumbers;
//
//        Reader oTempReader = new Reader(sFilePath);
//
//        Double[][] datemp = oTempReader.getNumber(iTextRow, iHeader);
//
//        daReadNumbers = Matrix.reshape(datemp, iaColumns);
//
//        return daReadNumbers;

    }

    public Double[] getColumn(int icolumn) throws IOException {

        readFile();
        Double[] dacolumn = new Double[this.lsFileContens.size()];
        for (int i = 0; i < ((int) this.lsFileContens.size()); i++) {
            Scanner oScanner = new Scanner(this.lsFileContens.get(i));
            oScanner.useDelimiter(this.sSeperator);

            for (int j = 0; j < getColumnNumber(); j++) {

                if (oScanner.hasNext()) {

                    if (icolumn == j) {

                        dacolumn[i] = Double.valueOf(oScanner.next());
                    } else {
                        oScanner.next();
                    }

                }

            }

        }

        return (dacolumn);

    }

    public int getColumnNumber() throws IOException {

        int iColNumber = 0;

        Scanner oScanner = new Scanner(readFirstLine());
        oScanner.useDelimiter(this.sSeperator);

        while (oScanner.hasNext()) {
            iColNumber = iColNumber + 1;
            oScanner.next();
        }

        return (iColNumber);
    }

    public int getBuffer() {

        int iBufferCheck = 0;

        if (this.iFileExist == 1) {
            try {
                this.oReaderObject = new BufferedReader(new FileReader(this.fFile));
                iBufferCheck = 1;
            } catch (FileNotFoundException ex) {
                System.out.println("cannot read from Data:" + ex);
            }
        } else {
            System.out.println("File not exist");
        }
        return (iBufferCheck);

    }

    public int[][] readImageGrey() throws IOException {
        
      //  throw new UnsupportedOperationException("Revise");

        BufferedImage oBufImageGrey = ImageIO.read(fFile);
//
        oBufImageGrey =IMG_Reader.getGrayScale(oBufImageGrey);
//
////        Image oImae = ImageIO.read(fFile);
//        //BufferedImage oBufImagegrey = new BufferedImage(oBufImage.getWidth(), oBufImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
//        //oBufImagegrey = ImageIO.read(fFile);
        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth() * 2];
//
        iaData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);
//
//        //oBufImageGrey.getRGB(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData, 0, 20);
        int[][] iaReturn = Matrix.reshape(iaData, oBufImageGrey.getHeight(), oBufImageGrey.getWidth());
//
//        /*
//         byte[] pixels = ((DataBufferByte) oBufImage.getRaster().getDataBuffer()).getData();
//
//         List<int[][]> lsaRGBAlpha = new ArrayList<int[][]>();
//
//         int iRunner = 0;
//
//
//         RGBAlpha:
//         for (int t = 0; t < 4; t++) {
//
//         int[][] iatReadIn = new int[oBufImage.getHeight()][oBufImage.getWidth()];
//
//         for (int i = 0; i < oBufImage.getHeight(); i++) {
//
//         for (int j = 0; j < oBufImage.getWidth(); j++) {
//
//         iatReadIn[i][j] = pixels[iRunner];
//
//         iRunner = iRunner + 1;
//
//         if (iRunner > pixels.length) {
//         break RGBAlpha;
//         }
//
//         }
//
//         }
//            
//         lsaRGBAlpha.add(iatReadIn);
//
//         }*/
        this.close();
//
        return iaReturn;
    }

    public List<int[][]> readImage() throws IOException {
        throw new UnsupportedOperationException("Revise");

//        BufferedImage oBufImageGrey = ImageIO.read(fFile);
//
//        Image oImae = ImageIO.read(fFile);
//
//        //BufferedImage oBufImagegrey = new BufferedImage(oBufImage.getWidth(), oBufImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
//        //oBufImagegrey = ImageIO.read(fFile);
//        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth() * oBufImageGrey.getData().getNumDataElements()];
//
//        iaData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);
//
//        //oBufImageGrey.getRGB(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData, 0, 20);
//        return Matrix.reshape(iaData, oBufImageGrey.getHeight(), oBufImageGrey.getWidth(), oBufImageGrey.getData().getNumDataElements());

    }

    public List<int[][]> readImageBGRAlpha() throws IOException {

        BufferedImage oBufImage = ImageIO.read(fFile);

        byte[] pixels = ((DataBufferByte) oBufImage.getRaster().getDataBuffer()).getData();

        List<int[][]> lsaRGBAlpha = new ArrayList<int[][]>();

        int iRunner = 0;

        RGBAlpha:
        for (int t = 0; t < 4; t++) {

            int[][] iatReadIn = new int[oBufImage.getHeight()][oBufImage.getWidth()];

            for (int i = 0; i < oBufImage.getHeight(); i++) {

                for (int j = 0; j < oBufImage.getWidth(); j++) {

                    iatReadIn[i][j] = pixels[iRunner];

                    iRunner = iRunner + 1;

                    if (iRunner > pixels.length) {
                        break RGBAlpha;
                    }

                }

            }

            lsaRGBAlpha.add(iatReadIn);

        }

        return lsaRGBAlpha;
    }
    
//    public int[][] readRGB() throws IOException {
//
//        BufferedImage oBufImageGrey = ImageIO.read(fFile);
//
//        oBufImageGrey = ImageTools.getRGB(oBufImageGrey);
//
////        Image oImae = ImageIO.read(fFile);
//        //BufferedImage oBufImagegrey = new BufferedImage(oBufImage.getWidth(), oBufImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
//        //oBufImagegrey = ImageIO.read(fFile);
//        int[] iaData = new int[oBufImageGrey.getHeight() * oBufImageGrey.getWidth() * 2];
//        long[] laData = new long[oBufImageGrey.getHeight() * oBufImageGrey.getWidth() * 2];
//
//        laData = oBufImageGrey.getData().getPixels(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData);
//
//        //oBufImageGrey.getRGB(0, 0, oBufImageGrey.getWidth(), oBufImageGrey.getHeight(), iaData, 0, 20);
//        int[][] iaReturn = Matrix.reshape(iaData, oBufImageGrey.getHeight(), oBufImageGrey.getWidth());
//
//        
//        this.close();
//
//        return iaReturn;
//    }

    public List<int[][]> readImageBGR() throws IOException {

        BufferedImage oBufImage = ImageIO.read(fFile);

        byte[] pixels = ((DataBufferByte) oBufImage.getRaster().getDataBuffer()).getData();

        List<int[][]> lsaRGBAlpha = new ArrayList<int[][]>();

        int iRunner = 0;

        RGB:
        for (int t = 0; t < 3; t++) {

            int[][] iatReadIn = new int[oBufImage.getHeight()][oBufImage.getWidth()];

            for (int i = 0; i < oBufImage.getHeight(); i++) {

                for (int j = 0; j < oBufImage.getWidth(); j++) {

                    iatReadIn[i][j] = pixels[iRunner];

                    iRunner = iRunner + 1;

                    if (iRunner > pixels.length) {
                        break RGB;
                    }

                }

            }

            lsaRGBAlpha.add(iatReadIn);

        }

        return lsaRGBAlpha;
    }

    public byte[] readBytes() throws FileNotFoundException, IOException {
        RandomAccessFile f = new RandomAccessFile(fFile, "r");
        byte[] b = new byte[(int) f.length()];
        f.readFully(b);
        return b;
    }

    public List<String> getHeader(int iTextStart, int iHeaderStart) throws IOException {

        this.iStartheader = iHeaderStart;

        List<String> lsaTemp = readFileBlank();
        List<String> lsaHeader = new ArrayList<String>();
        lsaHeader.addAll(lsaTemp.subList(0, iHeaderStart + iTextStart));

        return lsaHeader;

    }

    public static int findStartPoint(String sPwd, List<String> lsPossibleFiles) {

        int iStartPoint = 0;

        while (iStartPoint < lsPossibleFiles.size() - 1 && !(new File(sPwd + lsPossibleFiles.get(iStartPoint))).exists()) {
            iStartPoint++;
        }

        return iStartPoint;

    }

    public static int findEndPoint(String sPwd, List<String> lsPossibleFiles, int iStartPoint) {

        while (iStartPoint < lsPossibleFiles.size() - 501 && (new File(sPwd + lsPossibleFiles.get(iStartPoint + 500))).exists()) {
            iStartPoint = iStartPoint + 500;
        }

        while (iStartPoint < lsPossibleFiles.size() - 1 && (new File(sPwd + lsPossibleFiles.get(iStartPoint + 1))).exists()) {
            iStartPoint = iStartPoint + 1;
        }

        return iStartPoint;

    }
}
