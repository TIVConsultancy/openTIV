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

import com.tivconsultancy.opentiv.math.primitives.OrderedPair;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author ziegen60
 */
public class Writer {

    protected String sFile;
    protected File fFile;
    public String sSeperator;
    public String sError = "";
    public boolean bError = false;
    public String sHeader = null;

    public Writer(String sFile) {
        this.sFile = sFile;
        this.sSeperator = ",";
        this.fFile = new File(this.sFile);
        if (fFile.exists()) {
            if (fFile.setLastModified(System.currentTimeMillis())) {
                System.out.println("touched " + this.sFile);
            } else {
                System.out.println("touch failed on " + this.sFile);
            }

        } else {
            try {
                fFile.createNewFile();
                System.out.println("create new file " + this.sFile);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    
    public Writer(String sFile,boolean Message) {
        this.sFile = sFile;
        this.sSeperator = ",";
        this.fFile = new File(this.sFile);
        if (fFile.exists()) {
            if (fFile.setLastModified(System.currentTimeMillis())) {
                if (Message)
                System.out.println("touched " + this.sFile);
            } else {
                if (Message)
                System.out.println("touch failed on " + this.sFile);
            }

        } else {
            try {
                fFile.createNewFile();
                if (Message)
                System.out.println("create new file " + this.sFile);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public Writer(File fFile) {
        this.fFile = fFile;
        this.sSeperator = ",";
        this.sFile = this.fFile.getAbsolutePath();
    }

    public void setSeperator(String sSeperator) {
        this.sSeperator = sSeperator;
    }

    public String write(List<?> lsInput) {
        PrintWriter pw = createWriter();

        for (Object s : lsInput) {

            pw.print(s.toString());
            pw.print("\n");
        }

        closeWriter(pw);

        return (this.sFile);

    }

    public String writeWithHeader(List<?> lsInput, String sHeader) {
        PrintWriter pw = createWriter();

        pw.print(sHeader);
        pw.print("\n");

        for (Object s : lsInput) {

            pw.print(s.toString());
            pw.print("\n");
        }

        closeWriter(pw);

        return (this.sFile);
    }

    public String writeWithHeader(List<?> lsInput, String sHeader, StringFromObject o) {
        PrintWriter pw = createWriter();

        pw.print(sHeader);
        pw.print("\n");

        for (Object s : lsInput) {
            pw.print(o.getValue(s));
            pw.print("\n");
        }

        closeWriter(pw);

        return (this.sFile);
    }

    public String write(List<?> lsInput, String sStringForNull) {
        PrintWriter pw = createWriter();

        for (Object s : lsInput) {

            if (s != null) {
                pw.print(s.toString());
                pw.print("\n");
            } else {
                pw.print(sStringForNull);
                pw.print("\n");
            }
        }

        closeWriter(pw);

        return (this.sFile);

    }

    public String writels(List<String[]> lsInput) {
        PrintWriter pw = createWriter();
        if (!bError) {
            for (String[] sa : lsInput) {
                for (int i = 0; i < sa.length - 1; i++) {
                    String s = sa[i];
                    if (s != null) {
                        pw.print(s);
                    }
                }
                pw.print(sa[sa.length - 1]);
                pw.print("\n");
            }
            closeWriter(pw);
        }
        return (this.sFile);
    }

    public String writelsWithDeli(List<String[]> lsInput) {
        PrintWriter pw = createWriter();
        if (!bError) {
            for (String[] sa : lsInput) {
                for (int i = 0; i < sa.length - 1; i++) {
                    String s = sa[i];
                    if (s != null) {
                        pw.print(s);
                        pw.print(this.sSeperator);
                    }
                }
                pw.print(sa[sa.length - 1]);
                pw.print("\n");
            }
            closeWriter(pw);
        }
        return (this.sFile);
    }

    public String writelsWithDeli(List<String[]> lsInput, String sHeader) {
        PrintWriter pw = createWriter();
        if (!bError) {
            pw.print(sHeader);
            pw.print("\n");
            for (String[] sa : lsInput) {
                for (int i = 0; i < sa.length - 1; i++) {
                    String s = sa[i];
                    if (s != null) {
                        pw.print(s);
                        pw.print(this.sSeperator);
                    }
                }
                pw.print(sa[sa.length - 1]);
                pw.print("\n");
            }
            closeWriter(pw);
        }
        return (this.sFile);
    }

    public String writels(List<String[]> lsInput, String sSeperator) {
        PrintWriter pw = createWriter();
        if (!bError) {
            for (String[] sa : lsInput) {
                for (int i = 0; i < sa.length - 1; i++) {
                    String s = sa[i];
                    if (s != null) {
                        pw.print(s);
                        pw.print(sSeperator);
                    }
                }
                pw.print(sa[sa.length-1]);
                pw.print("\n");
            }
            closeWriter(pw);
        }
        return (this.sFile);
    }

    public String writelda(List<Double[]> lsInput) {
        PrintWriter pw = createWriter();
        if (this.sHeader != null) {
            pw.print(this.sHeader);
            pw.print("\n");
        }
        if (!bError) {
            for (Double[] da : lsInput) {
                for (int i = 0; i < da.length; i++) {
                    Double d = da[i];
                    if (d != null) {
                        pw.print((double) d);
                    } else {
                        pw.print("null");
                    }
                    if (i != da.length - 1) {
                        pw.print(this.sSeperator);
                    }
                }
                pw.print("\n");
            }
            closeWriter(pw);
        }
        return (this.sFile);
    }

    public String writelda(List<Double[]> lsInput, List<String> lsAppend, boolean bKeepAppend) {
        PrintWriter pw = createWriter();
        if (this.sHeader != null) {
            pw.print(this.sHeader);
            pw.print("\n");
        }
        if (!bError) {
            for (int t = 0; t < lsInput.size(); t++) {
                if (lsInput.get(t) != null) {
                    Double[] da = lsInput.get(t);
                    if (lsAppend.size() > t) {
                        for (int i = 0; i < da.length; i++) {
                            Double d = da[i];
                            if (d != null) {
                                pw.print((double) d);
                            } else {
                                pw.print("null");
                            }
                            if (i != da.length - 1) {
                                pw.print(this.sSeperator);
                            }
                        }
                        pw.print(lsAppend.get(t));
                    } else {
                        if (bKeepAppend) {
                            for (int i = 0; i < da.length; i++) {
                                Double d = da[i];
                                if (d != null) {
                                    pw.print((double) d);
                                } else {
                                    pw.print("null");
                                }
                                if (i != da.length - 1) {
                                    pw.print(this.sSeperator);
                                }
                            }
                            pw.print(lsAppend.get(lsAppend.size() - 1));
                        }
                    }
                    pw.print("\n");
                }
            }
            closeWriter(pw);
        }
        return (this.sFile);
    }

    public String writeld(List<Double> lsInput, List<String> lsAppend, boolean bKeepAppend) {
        PrintWriter pw = createWriter();
        if (this.sHeader != null) {
            pw.print(this.sHeader);
            pw.print("\n");
        }
        if (!bError) {
            for (int t = 0; t < lsInput.size(); t++) {
                Double d = lsInput.get(t);
                if (lsAppend.size() > t) {
                    if (d != null) {
                        pw.print((double) d);
                    } else {
                        pw.print("null");
                    }
                    pw.print(lsAppend.get(t));
                } else {
                    if (bKeepAppend) {
                        if (d != null) {
                            pw.print((double) d);
                        } else {
                            pw.print("null");
                        }
                        pw.print(lsAppend.get(lsAppend.size() - 1));
                    }
                }
                pw.print("\n");
            }
            closeWriter(pw);
        }
        return (this.sFile);
    }
//
//    public String writelme(List<MatrixEntry> lsInput) {
//        PrintWriter pw = createWriter();
//        if (!bError) {
//            for (MatrixEntry me : lsInput) {
//                if (me != null) {
//                    pw.print(me.i);
//                    pw.print(this.sSeperator);
//                    pw.print(me.j);
//                    pw.print("\n");
//                } else {
//                    pw.print("null");
//                    pw.print(this.sSeperator);
//                    pw.print("null");
//                    pw.print("\n");
//                }
//            }
//            closeWriter(pw);
//        }
//        return (this.sFile);
//    }

    public String write(Double[][] Input) {
        PrintWriter pw = createWriter();

        if (this.sHeader != null) {
            pw.print(this.sHeader);
        }

        for (int i = 0; i < Input.length; i++) {

            for (int j = 0; j < Input[i].length; j++) {

                if (j != 0) {
                    pw.print(this.sSeperator);
                }

                pw.print(Input[i][j]);

            }

            pw.print("\n");

        }

        closeWriter(pw);

        return (this.sFile);

    }

    public String write(Double[] Input) {
        PrintWriter pw = createWriter();

        for (int i = 0; i < Input.length; i++) {

            pw.print(Input[i]);

            pw.print("\n");
        }

        closeWriter(pw);

        return (this.sFile);

    }

    public void write(double[][] Input) {

        PrintWriter pw = createWriter();

        if (Input != null && pw != null) {

            for (int i = 0; i < Input.length; i++) {

                for (int j = 0; j < Input[i].length; j++) {

                    if (j != 0) {
                        pw.print(this.sSeperator);
                    }

                    pw.print(Input[i][j]);

                }

                pw.print("\n");

            }

            closeWriter(pw);

        } else {
            bError = true;
            if (Input == null) {
                sError = "This is writer: Null Input file " + this.sFile + " not written";
            } else {
                sError = "This is writer: Input file " + this.sFile + " not found/corrupt";
            }

        }

    }

    public void write(int[][] Input) {
        PrintWriter pw = createWriter();

        for (int i = 0; i < Input.length; i++) {

            for (int j = 0; j < Input[i].length; j++) {

                if (j != 0) {
                    pw.print(this.sSeperator);
                }

                pw.print(Input[i][j]);

            }

            pw.print("\n");

        }

        closeWriter(pw);

    }

    public void write(int[] Input) {
        PrintWriter pw = createWriter();

        for (int j = 0; j < Input.length; j++) {

            if (j != 0) {
                pw.print(this.sSeperator);
            }

            pw.print(Input[j]);

        }

//        pw.print("\n");
        closeWriter(pw);

    }

    public void write(int[][] Input, int iLine) {
        PrintWriter pw = createWriter();

        int i = iLine;

        for (int j = 0; j < Input[i].length; j++) {

            if (j != 0) {
                pw.print(this.sSeperator);
            }

            pw.print(Input[i][j]);

        }

        pw.print("\n");

        closeWriter(pw);

    }

    public void writeTranspose(int[][] Input, int iLine) {
        PrintWriter pw = createWriter();

        int i = iLine;

        for (int j = 0; j < Input[i].length; j++) {

//                if (j != 0) {
//                    pw.print(this.sSeperator);
//                }
            pw.print(Input[i][j]);
            pw.print("\n");

        }

        closeWriter(pw);

    }

    public void write(String[][] Input) {
        PrintWriter pw = createWriter();

        for (int i = 0; i < Input.length; i++) {

            for (int j = 0; j < Input[i].length; j++) {

                if (j != 0) {
                    pw.print(this.sSeperator);
                }

                pw.print(Input[i][j]);

            }

            pw.print("\n");

        }

        closeWriter(pw);

    }

    public void write(String[] Input) {
        PrintWriter pw = createWriter();

        for (int i = 0; i < Input.length; i++) {

            pw.print(Input[i]);
            pw.print("\n");

        }

        pw.print("\n");

        closeWriter(pw);

    }

    public void writebigLists(List<String[]> lsIn) {
        PrintWriter pw = createWriter();

        for (String[] Input : lsIn) {
            for (int i = 0; i < Input.length; i++) {

                pw.print(Input[i]);

            }
            pw.print("\n");
        }

        closeWriter(pw);

    }

    public void writebigString(List<String> lsIn) {
        PrintWriter pw = createWriter();

        for (String Input : lsIn) {

            pw.print(Input);
            pw.print("\n");

        }

        closeWriter(pw);

    }

    public void write(Double dInput, String sHeader) {
        PrintWriter pw = createWriter();

        pw.println(sHeader);

        pw.print(dInput);
        closeWriter(pw);
    }

    public void write(Double[][] Input, List<String> lsaHeader) {
        PrintWriter pw = createWriter();

        for (String sHeader : lsaHeader) {

            pw.println(sHeader);

        }

        for (int i = 0; i < Input.length; i++) {

            for (int j = 0; j < Input[i].length; j++) {

                if (j != 0) {
                    pw.print(this.sSeperator);
                }

                pw.print(Input[i][j]);
            }
            pw.print("\n");
        }

        closeWriter(pw);
    }

    public void write(Double[][] Input, List<String> lsaHeader, List<Integer> lioNumbers) {
        PrintWriter pw = createWriter();

        for (String sHeader : lsaHeader) {

            pw.println(sHeader);

        }

        for (int i = 0; i < Input.length; i++) {

            for (int j = 0; j < Input[i].length; j++) {

                if (j != 0) {
                    pw.print(this.sSeperator);
                }
                pw.print(Input[i][j]);
            }
            pw.print(this.sSeperator);
            if (lioNumbers.size() > i) {
                pw.print(lioNumbers.get(i));
            } else {
                pw.print(-1);
            }

            pw.print("\n");
        }

        closeWriter(pw);
    }

    public void write(Double[][] Input, String sHeader) {
        PrintWriter pw = createWriter();

        pw.println(sHeader);

        for (int i = 0; i < Input.length; i++) {

            for (int j = 0; j < Input[i].length; j++) {

                if (j != 0) {
                    pw.print(this.sSeperator);
                }

                pw.print(Input[i][j]);

            }

            pw.print("\n");

        }

        closeWriter(pw);

    }

//    public String writeGnuPlot2D(VertexGrid2DCartesian oGrid, List<Double> ldaValues, String sHeader) {
//        List<String> lsOut = new ArrayList<String>();
//        lsOut.add(sHeader);
//        String sOut;
//        if (!ldaValues.isEmpty()) {
//            String sLine = String.valueOf(oGrid.ldaVertexes.get(0)[0]) + " " + String.valueOf(oGrid.ldaVertexes.get(0)[1])
//                    + " " + ldaValues.get(0);
//            lsOut.add(sLine);
//            for (int i = 1; i < oGrid.ldaVertexes.size(); i++) {
//                int iRetal = Double.compare(oGrid.ldaVertexes.get(i - 1)[1], oGrid.ldaVertexes.get(i)[1]);
//                if (iRetal != 0) {
//                    lsOut.add("");
//                }
//                sLine = String.valueOf(oGrid.ldaVertexes.get(i)[0]) + " " + String.valueOf(oGrid.ldaVertexes.get(i)[1])
//                        + " " + ldaValues.get(i);
//                lsOut.add(sLine);
//            }
//            Writer oWGrid = new Writer(sFile);
//            sOut = oWGrid.write(lsOut);
//        } else {
//            sOut = "Warning ! Empty averaging ";
//        }
//
//        return sOut;
//
//    }
    public void write(String sHeader) {
        PrintWriter pw = createWriter();

        pw.println(sHeader);

        closeWriter(pw);

    }

    private PrintWriter createWriter() {

        PrintWriter pw = null;

        try {
            FileWriter fw = new FileWriter(this.fFile);
            BufferedWriter bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            bError = false;
        } catch (IOException e) {
            System.out.println("Error: File not readable" + this.fFile);
            sError = "Error: File not readable: " + this.fFile;
            bError = true;
        }

        return (pw);

    }

    private void closeWriter(PrintWriter pw) {

        if (pw != null) {
            pw.flush();
            pw.close();
        }
    }

    public static Image PaintGreyPNG(byte[] bapixels, int iWidth, int iHeight, File oOutput) throws IOException {

        //byte[] bapixels = getPixel(iaInput, 1);
        //System.out.println(pixels.length);
        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = oFrameImage.getRaster();
        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);
        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }

//    public static Image PaintGreyPNG(int[][] iapixels, File oOutput) throws IOException {
//
//        int iWidth = iapixels[0].length;
//        int iHeight = iapixels.length;
//
//        byte[] bapixels = Matrix.castToByteprimitive(Matrix.reshape(iapixels));
//
//        //byte[] bapixels = getPixel(iaInput, 1);
//        //System.out.println(pixels.length);
//        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
//        WritableRaster raster = oFrameImage.getRaster();
//        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);
//
//        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);
//
//        ImageIO.write(oFrameImage, "png", oOutput);
//
//        return oFrameImage;
//    }
    public static void PaintGreyAsMatrix(int[][] iapixels, File oOutput) throws IOException {

        List<String> lsOut = new ArrayList<String>();

        for (int i = 0; i < iapixels.length; i++) {
            String sOut = "" + iapixels[i][0];
            for (int j = 1; j < iapixels.length; j++) {
                sOut = sOut + "," + iapixels[i][j];
            }
            lsOut.add(sOut);
        }

        Writer oWrite = new Writer(oOutput);
        oWrite.write(lsOut);
    }

    public static void WriteMatrix(double[][] iapixels, File oOutput) throws IOException {

        List<String> lsOut = new ArrayList<String>();

        for (int i = 0; i < iapixels.length; i++) {
            String sOut = "" + iapixels[i][0];
            for (int j = 1; j < iapixels[0].length; j++) {
                sOut = sOut + "," + iapixels[i][j];
            }
            lsOut.add(sOut);
        }

        Writer oWrite = new Writer(oOutput);
        oWrite.write(lsOut);
    }
    
        public static void WriteVector(Double[] iapixels, File oOutput) throws IOException {

        List<String> lsOut = new ArrayList<String>();

        for (int i = 0; i < iapixels.length; i++) {
            String sOut = "" + iapixels[i];
            lsOut.add(sOut);
        }

        Writer oWrite = new Writer(oOutput);
        oWrite.write(lsOut);
    }
    
    public static void WriteMatrix(int[][] iapixels, File oOutput) throws IOException {

        List<String> lsOut = new ArrayList<String>();

        for (int i = 0; i < iapixels.length; i++) {
            String sOut = "" + iapixels[i][0];
            for (int j = 1; j < iapixels[0].length; j++) {
                sOut = sOut + "," + iapixels[i][j];
            }
            lsOut.add(sOut);
        }

        Writer oWrite = new Writer(oOutput);
        oWrite.write(lsOut);
    }

    public static BufferedImage getType_Int_RGB(BufferedImage oImage) {
        int width = oImage.getWidth();
        int height = oImage.getHeight();

        BufferedImage newImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(oImage, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage getType_Int_Grey(BufferedImage oImage) {
        int width = oImage.getWidth();
        int height = oImage.getHeight();

        BufferedImage newImage = new BufferedImage(
                width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = newImage.getGraphics();
        g.drawImage(oImage, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static Image PaintColorPNG(int[][] iapixelsR, int[][] iapixelsG, int[][] iapixelsB, File oOutput) throws IOException {

        int iWidth = iapixelsR[0].length;
        int iHeight = iapixelsR.length;

        //byte[] bapixels = getPixel(iaInput, 1);
        //System.out.println(pixels.length);
        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < iHeight; i++) {
            for (int j = 0; j < iWidth; j++) {
                oFrameImage.setRGB(j, i, (iapixelsR[i][j] << 16) | (iapixelsG[i][j] << 8) | iapixelsB[i][j]);
            }
        }

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }

    public static Image PaintColorPNG(int[][] iapixelsRGB, File oOutput) throws IOException {

        int iWidth = iapixelsRGB[0].length;
        int iHeight = iapixelsRGB.length;

        //byte[] bapixels = getPixel(iaInput, 1);
        //System.out.println(pixels.length);
        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < iHeight; i++) {
            for (int j = 0; j < iWidth; j++) {
                oFrameImage.setRGB(j, i, iapixelsRGB[i][j]);
            }
        }

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }

//    public static Image PaintGreyPNGWithColorVectors(int[][] iapixels, File oOutput, List<MatrixEntry> loArrowRoots, List<MatrixEntry> loVelo, Colorbar oColorbar, int iStretch) throws IOException {
//
//        int iWidth = iapixels[0].length;
//        int iHeight = iapixels.length;
//
//        byte[] bapixels = Matrix.castToByteprimitive(Matrix.reshape(iapixels));
//
//        //byte[] bapixels = getPixel(iaInput, 1);
//        //System.out.println(pixels.length);
//        BufferedImage oFrameImage;
//
//        oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
//
//        WritableRaster raster = oFrameImage.getRaster();
//        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);
//
//        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);
//
//        if (oColorbar != null) {
//            oFrameImage = getType_Int_RGB(oFrameImage);
//        }
//
//        Graphics2D g = oFrameImage.createGraphics();
//
//        for (int i = 0; i < loArrowRoots.size(); i++) {
//            VectorArrow v1 = new VectorArrow(loArrowRoots.get(i).y, loArrowRoots.get(i).x, loVelo.get(i), iStretch);
//
//            if (oColorbar != null) {
//                g.setColor(oColorbar.getColor(loVelo.get(i).SecondCartesian()));
//            }
//
//            g.drawLine(v1.dmid[1].intValue(), v1.dmid[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
//
//            if (v1.drighttip != null && !v1.drighttip[1].isNaN() && !v1.drighttip[0].isNaN()) {
//                g.drawLine(v1.drighttip[1].intValue(), v1.drighttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
//            }
//            if (v1.dlefttip != null && !v1.dlefttip[1].isNaN() && !v1.dlefttip[0].isNaN()) {
//                g.drawLine(v1.dlefttip[1].intValue(), v1.dlefttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
//            }
//        }
//
//        ImageIO.write(oFrameImage, "png", oOutput);
//
//        return oFrameImage;
//    }
//    public static void writeGreyPNG(int[][] iapixels, File oOutput) throws IOException {
//
//        int iWidth = iapixels[0].length;
//        int iHeight = iapixels.length;
//
//        byte[] bapixels = Matrix.castToByteprimitive(Matrix.reshape(iapixels));
//        String[] saPixel = new String[bapixels.length];
//        for (int i = 0; i < bapixels.length; i++) {
//            saPixel[i] = String.valueOf(bapixels[i]);
//
//        }
//
//        Writer oWrite = new Writer(oOutput);
//        oWrite.write(saPixel);
//
////        //byte[] bapixels = getPixel(iaInput, 1);
////        //System.out.println(pixels.length);
////        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
////        WritableRaster raster = oFrameImage.getRaster();
////        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);
////
////        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);
////
////        ImageIO.write(oFrameImage, "png", oOutput);
////        return oFrameImage;
//    }
    public static Image WriteGreyPNG(BufferedImage oFrameImage, File oOutput) throws IOException {

        int iWidth = oFrameImage.getWidth();
        int iHeight = oFrameImage.getHeight();

        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);
        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }

    public static Image PaintABGRPNG(byte[] bapixels, int iWidth, int iHeight, File oOutput) throws IOException {

        //byte[] bapixels = getPixel(iaInput, 1);
        //System.out.println(pixels.length);
        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster raster = oFrameImage.getRaster();
        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);
        raster.setDataElements(0, 0, iWidth, iHeight, bapixels);

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }

    public static Image PaintABGRPNG(int[] iapixels, int iWidth, int iHeight, File oOutput) throws IOException {

        //byte[] bapixels = getPixel(iaInput, 1);
        //System.out.println(pixels.length);
        BufferedImage oFrameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_BGR);
        WritableRaster raster = oFrameImage.getRaster();
        //raster.setPixels(0,0,iaInput[0].length, iaInput.length,);
        raster.setDataElements(0, 0, iWidth, iHeight, iapixels);

        ImageIO.write(oFrameImage, "png", oOutput);

        return oFrameImage;
    }

    public static byte[] getPixel(int[][] iaInput, int iMultiplicator) {

        byte[] iaReturn = new byte[iaInput.length * iaInput[0].length];

        int run = 0;

        for (int i = 0; i < iaInput.length; i++) {

            for (int j = 0; j < iaInput[0].length; j++) {

                if (iaInput[i][j] * iMultiplicator <= 255 && iaInput[i][j] * iMultiplicator >= 0) {
                    iaReturn[run] = (byte) (iaInput[i][j] * iMultiplicator);
                } else if (iaInput[i][j] * iMultiplicator < 0) {
                    iaReturn[run] = 0;
                } else {
                    iaReturn[run] = (byte) 255;
                }

                run = run + 1;

            }

        }

        return iaReturn;

    }

    public static byte[] getPixel(List<int[][]> liaInput, int iMultiplicator) {

        int[][] iatBsp = liaInput.get(0);

        byte[] iaReturn = new byte[iatBsp.length * iatBsp[0].length * liaInput.size()];

        int run = 0;

        for (int t = 0; t < liaInput.size(); t++) {

            int[][] iaInput = liaInput.get(t);

            for (int i = 0; i < iaInput.length; i++) {

                for (int j = 0; j < iaInput[0].length; j++) {

                    if (iaInput[i][j] * iMultiplicator <= 255 && iaInput[i][j] * iMultiplicator >= 0) {
                        iaReturn[run] = (byte) (iaInput[i][j] * iMultiplicator);
                    } else if (iaInput[i][j] * iMultiplicator < 0) {
                        iaReturn[run] = 0;
                    } else {
                        iaReturn[run] = (byte) 255;
                    }

                    run = run + 1;

                }

            }
        }

        return iaReturn;

    }

    public static void visualizeMesh(double[][] da, int iPointsPerCell, String sFile) {

//        int Ni = da.length;
//        int Nj = da[0].length;
//
//        List<OrderedPair>[][] daLoPo = new ArrayList[Ni][Nj];
//
//        double dXYStart = -0.5;
//        double dDXDY = (1.0 - (1.0 / Math.sqrt((double) iPointsPerCell))) / Math.sqrt((double) iPointsPerCell);
//        int iCount = 0;
//        for (int i = 0; i < Ni; i++) {
//            for (int j = 0; j < Nj; j++) {
//                daLoPo[i][j] = new ArrayList<OrderedPair>();
//                double dValue = da[i][j];
//                for (double x = dXYStart + dDXDY; x <= (-1.0 * dXYStart) - dDXDY; x = x + dDXDY) {
//                    for (double y = dXYStart + dDXDY; y <= (-1.0 * dXYStart) - dDXDY; y = y + dDXDY) {
//                        daLoPo[i][j].add(new OrderedPair(x + j, y + i, dValue));
//                    }
//                }
//            }
//        }
//        List<String> lsString = new ArrayList<String>();
//        lsString.add("x,y,z,Value");
//        for (int k = 0; k < Ni; k++) {
//            for (int t = 0; t < Nj; t++) {
//                List<OrderedPair> lop = daLoPo[k][t];
//                for (OrderedPair op : lop) {
//                    lsString.add(op.toString2());
//                }
//            }
//        }
//
//        Writer oWrite = new Writer(sFile);
//        oWrite.write(lsString);
        throw new UnsupportedOperationException("Implement");
    }

    public static void visualizeMesh(double[][] da, int iPointsPerCell, String sFile, double dRotAngle) {

//        int Ni = da.length;
//        int Nj = da[0].length;
//
//        List<OrderedPair>[][] daLoPo = new ArrayList[Ni][Nj];
//
//        double dXYStart = -0.5;
//        double dDXDY = (1.0 - (1.0 / Math.sqrt((double) iPointsPerCell))) / Math.sqrt((double) iPointsPerCell);
//        int iCount = 0;
//        int iStarti = (int) (1.0 * Ni / 2.0);
//        int iStartj = (int) (1.0 * Nj / 2.0);
//        for (int i = -1 * iStarti; i <= iStarti; i++) {
//            for (int j = -1 * iStartj; j <= iStartj; j++) {
//                daLoPo[i + iStarti][j + iStartj] = new ArrayList<OrderedPair>();
//                double dValue = da[i + iStarti][j + iStartj];
//                for (double x = dXYStart + dDXDY; x <= (-1.0 * dXYStart) - dDXDY; x = x + dDXDY) {
//                    for (double y = dXYStart + dDXDY; y <= (-1.0 * dXYStart) - dDXDY; y = y + dDXDY) {
//                        daLoPo[i + iStarti][j + iStartj].add(new OrderedPair(x + j, y + i, dValue));
//                    }
//                }
//            }
//        }
//        List<String> lsString = new ArrayList<String>();
//        lsString.add("x,y,z,Value");
//        for (int k = 0; k < Ni; k++) {
//            for (int t = 0; t < Nj; t++) {
//                List<OrderedPair> lop = daLoPo[k][t];
//                for (OrderedPair op : lop) {
//                    op.rotateAroundZero(dRotAngle);
//                    lsString.add(op.toString2());
//                }
//            }
//        }
//
//        Writer oWrite = new Writer(sFile);
//        oWrite.write(lsString);
        throw new UnsupportedOperationException("Need to be implemented");

    }

    public static void WriteWithValue(String sFile, List<? extends OrderedPair> lop, boolean bHeader, String[] sHeader) {
        List<String[]> lsSave = new ArrayList<String[]>();

        if (bHeader) {

            if (sHeader == null) {
                String[] saTemp = new String[2];
                saTemp[0] = "Position x, ";
                saTemp[1] = "Position y, Value";
                lsSave.add(saTemp);
            } else {
                lsSave.add(sHeader);
            }
        }

        for (OrderedPair op : lop) {
            if (op != null) {
                String[] saTemp = new String[3];
                saTemp[0] = String.valueOf(op.x) + ",";
                saTemp[1] = String.valueOf(op.y) + ",";
                saTemp[2] = String.valueOf(op.dValue);
                lsSave.add(saTemp);
            }
        }

        Writer oWriter = new Writer(sFile);

        oWriter.writels(lsSave);
    }

    public static void WriteWithoutValue(String sFile, List<? extends OrderedPair> lop, boolean bHeader, String[] Header) {
        List<String[]> lsSave = new ArrayList<String[]>();

        if (bHeader) {
            if (Header == null) {
                String[] saTemp = new String[2];
                saTemp[0] = "Position x, ";
                saTemp[1] = "Position y";
                lsSave.add(saTemp);
            } else {
                lsSave.add(Header);
            }
        }

        for (OrderedPair op : lop) {
            if (op != null) {
                String[] saTemp = new String[2];
                saTemp[0] = String.valueOf(op.x) + ",";
                saTemp[1] = String.valueOf(op.y);
                lsSave.add(saTemp);
            }
        }

        Writer oWriter = new Writer(sFile);

        oWriter.writels(lsSave);
    }

    public static void writeObjectToFile(String sFile, Object o) {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;

        try {

            fout = new FileOutputStream(sFile);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(o);

            System.out.println("Done");

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static Object deSerialization(String file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }

    public interface StringFromObject<T> {
        public String getValue(T pParameter);
    }

}
