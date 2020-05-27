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
package com.tivconsultancy.opentiv.postproc.pixelplot;

import com.tivconsultancy.opentiv.imageproc.shapes.Line;
import com.tivconsultancy.opentiv.math.interfaces.Position;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class PixelDiagram implements Serializable {

    private static final long serialVersionUID = -7190012393493762611L;

    public int iStartX = 0;
    public int iStartY = 0;

    public double iScalingFactor = 1;
    public List<Integer> loLineColors;

    public int iSpaceLeft = 30;
    public int iSpaceRight = 15;

    public int iSpaceTop = 10;
    public int iSpaceBottom = 30;

    protected int iSpaceBetweenTicsY = 25;
    public int iTicsSizeY = 10;

    protected double iSpaceBetweenTicsX = 50;
    public int iTicsSizeX = 10;

    protected double dSizeX = 0;
    protected double dSizeY = 0;

    protected double dStretchTicsX = 1.0 / 2.0;
    protected double dStretchTicsY = 2.0;

    public String sLabelY = "X [m]";
    public String sLabelX = "Y [s]";

    public Font oLabelFontX = new Font("TimesRoman", Font.PLAIN, 16);
    public Font oLabelFontY = new Font("TimesRoman", Font.PLAIN, 16);
    protected int iLabelXPosY = -1;
    protected int iLabelXPosX = -1;
    protected int iLabelYPosY = -1;
    protected int iLabelYPosX = -1;

    public List<Line> loLines = new ArrayList<>();

    protected BufferedImage oIMG;

    public PixelDiagram(double iSizeX, double iSizeY) {
        this.dSizeX = iSizeX;
        this.dSizeY = iSizeY;
        drawCanvas();
    }

    public void setLables(String sLabelX, String sLabelY) {
        this.sLabelX = sLabelX;
        this.sLabelY = sLabelY;
    }

    public void setPositionLabls(int iLabelXPosX, int iLabelXPosY, int iLabelYPosX, int iLabelYPosY) {
        this.iLabelXPosY = iLabelXPosY;
        this.iLabelXPosX = iLabelXPosX;
        this.iLabelYPosY = iLabelXPosY;
        this.iLabelYPosX = iLabelYPosX;
    }

    protected void setSize(double dSizeX, double dSizeY) {
        this.dSizeX = dSizeX;
        this.dSizeY = dSizeY;
    }

    public void setStretchFactors(double dStretchTicsX, double dStretchTicsY) {
        this.dStretchTicsX = dStretchTicsX;
        this.dStretchTicsY = dStretchTicsY;
    }

    public BufferedImage getImage() {
        return oIMG;
    }

    public void setCanvasSize(List<List<? extends Position>> lloData) {
        double iMaxX = 0;
        double iMinX = 0;
        double iMaxY = 0;
        double iMinY = 0;

        for (List<? extends Position> lo : lloData) {
            for (Position op : lo) {
                if (iMaxX < op.getPosX()) {
                    iMaxX = op.getPosX();
                }
                if (iMinX > op.getPosX()) {
                    iMinX = op.getPosX();
                }
                if (iMaxY < op.getPosY()) {
                    iMaxY = op.getPosY();
                }
                if (iMinY > op.getPosY()) {
                    iMinY = op.getPosY();
                }
            }
        }

        this.dSizeX = iMaxX - Math.max(iMinX, iStartX);
        this.dSizeY = iMaxY - Math.max(iMinY, iStartY);
        drawCanvas();
    }

    protected void drawCanvas() {

        int iTotalSizeX = (int) Math.ceil(dSizeX + iSpaceLeft + iSpaceRight);
        int iTotalSizeY = (int) Math.ceil(dSizeY + iSpaceTop + iSpaceBottom);

        oIMG = new BufferedImage(iTotalSizeX, iTotalSizeY, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * draw the axis on the standard BufferedImage
     */
    public void drawAxis() {

        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        Graphics2D oArea = oIMG.createGraphics();
        oArea.drawLine(iSpaceLeft, iTotalSizeY - iSpaceBottom, iTotalSizeX - iSpaceRight, iTotalSizeY - iSpaceBottom);
        oArea.drawLine(iSpaceLeft, iTotalSizeY - iSpaceBottom, iSpaceLeft, (int) Math.ceil(iSpaceTop));

    }

    /**
     *
     */
    public void drawtics() {

        drawXtics();
        drawYtics();
    }

    public void drawXtics() {

        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        FontMetrics oFontMetrics = oArea.getFontMetrics();
        int iFontHeight = oFontMetrics.getAscent() + oFontMetrics.getDescent();
        //X                
        int iPosTicX = iSpaceLeft;
        int iTicLabelX = iStartX;
        while (iPosTicX <= (iTotalSizeX - iSpaceRight)) {
            oArea.drawLine(iPosTicX, iTotalSizeY - (iSpaceBottom + iTicsSizeX / 2), iPosTicX, iTotalSizeY - (iSpaceBottom - iTicsSizeX / 2));
            oArea.drawString(String.valueOf(iTicLabelX), iPosTicX, iTotalSizeY - (iSpaceBottom - iTicsSizeX / 2 - iFontHeight));
            iPosTicX = iPosTicX + (int) iSpaceBetweenTicsX;
            iTicLabelX = iTicLabelX + (int) (iSpaceBetweenTicsX * 1.0 / dStretchTicsX);
        }
    }

    public void drawYtics() {

        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        FontMetrics oFontMetrics = oArea.getFontMetrics();
        int iFontHeight = oFontMetrics.getAscent() + oFontMetrics.getDescent();
        //Y        
        int iPosTicY = iTotalSizeY - iSpaceBottom;
        int iTicLabelY = iStartY;
        while (iPosTicY >= (iSpaceTop)) {
            oArea.drawLine(iSpaceLeft - iTicsSizeX / 2, iPosTicY, iSpaceLeft + iTicsSizeX / 2, iPosTicY);
            int iWidth = oFontMetrics.stringWidth(String.valueOf(iTicLabelY));
            oArea.drawString(String.valueOf(iTicLabelY), iSpaceLeft - iTicsSizeX / 2 - iWidth, iPosTicY);
            iPosTicY = iPosTicY - iSpaceBetweenTicsY;
            iTicLabelY = iTicLabelY + (int) (iSpaceBetweenTicsY * 1.0 / dStretchTicsY);
        }
    }

    /**
     * draw labels
     */
    public void drawLabels() {

        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        FontMetrics oFontMetrics;

        //X
        oArea.setFont(oLabelFontX);
        oFontMetrics = oArea.getFontMetrics();

        if (iLabelXPosX < 0) {
            iLabelXPosX = iSpaceLeft + ((iTotalSizeX - iSpaceRight - iSpaceLeft) / 2 - oFontMetrics.stringWidth(sLabelX) / 2);
        }
        if (iLabelXPosY < 0) {
            iLabelXPosY = iTotalSizeY + (oFontMetrics.getAscent() - oFontMetrics.getDescent());
        }

        oArea.drawString(sLabelX, iLabelXPosX, iLabelXPosY);
        //Y
        oArea.setFont(oLabelFontY);
        oFontMetrics = oArea.getFontMetrics();
        if (iLabelYPosY == -1) {
            iLabelYPosY = iSpaceTop + (getPlotAreaHeight()) / 2 - oFontMetrics.stringWidth(sLabelY) / 2;
        }
        if (iLabelYPosX == -1) {
            iLabelYPosX = oFontMetrics.getDescent();
        }

        drawRotateString(oArea, iLabelYPosX, iLabelYPosY, 90, sLabelY);

//        AffineTransform orig = oArea.getTransform();
//        oArea.rotate(Math.PI / 2);
//        oArea.drawString(sLabelY, iLabelYPosX, iLabelYPosY);
//        oArea.setTransform(orig);
    }

    public static void drawRotateString(Graphics2D g2d, double x, double y, int angle, String text) {
        g2d.translate((float) x, (float) y);
        g2d.rotate(Math.toRadians(angle));
        g2d.drawString(text, 0, 0);
        g2d.rotate(-Math.toRadians(angle));
        g2d.translate(-(float) x, -(float) y);
    }

    public void setLabelPosInsideTop() {
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();
        FontMetrics oFontMetrics;
        Graphics2D oArea = oIMG.createGraphics();

        oArea.setFont(oLabelFontX);
        oFontMetrics = oArea.getFontMetrics();
        iLabelXPosX = (int) (iTotalSizeX - iSpaceRight - oFontMetrics.stringWidth(sLabelX));
        iLabelXPosY = iTotalSizeY - (iSpaceBottom + iTicsSizeX);

        oArea.setFont(oLabelFontY);
        oFontMetrics = oArea.getFontMetrics();
        iLabelYPosY = iSpaceTop + iTicsSizeX;
        iLabelYPosX = iSpaceLeft + iTicsSizeY;

    }

    public void drawSpecialLines() {

        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        oArea.setColor(Color.red);
        for (Line oLine : loLines) {
            oArea.drawLine(oLine.meStart.j - iStartX + iSpaceLeft, iTotalSizeY - (oLine.meStart.i - iStartY + iSpaceBottom), oLine.meEnd.j - iStartX + iSpaceLeft, iTotalSizeY - (oLine.meEnd.i - iStartY + iSpaceBottom));
        }
    }

    public void drawPoints(List<? extends Position> lop, Color c) {
        drawPoints(lop, c, 7);
    }

    public void drawPoints(List<? extends Position> lop, Color c, int iSize) {

        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        oArea.setColor(c);
        int iCount = 0;
        for (int i = 0; i < lop.size(); i++) {
            double x = lop.get(i).getPosX();
            double y = lop.get(i).getPosY();
            if (iStartX > x || iStartY > y) {
                continue;
            }
            oArea.fillOval((int) ((x - iStartX) + iSpaceLeft) - (int) (iSize / 2.0), (int) (iTotalSizeY - (y - iStartY + iSpaceBottom)) - (int) (iSize / 2.0), iSize, iSize);
            iCount++;
        }
    }

    public void drawLinePoints(List<? extends Position> lop, Color c, int iSize) {

        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        oArea.setColor(c);
        int iCount = 0;
        for (int i = 0; i < lop.size(); i++) {
            double x = lop.get(i).getPosX();
            double y = lop.get(i).getPosY();
            if (iStartX > x || iStartY > y) {
                continue;
            }
            oArea.fillOval((int) ((x - iStartX) + iSpaceLeft) - (int) (iSize / 2.0), (int) (iTotalSizeY - (y - iStartY + iSpaceBottom)) - (int) (iSize / 2.0), iSize, iSize);
            if (i >= 1) {
                double xm1 = lop.get(i - 1).getPosX();
                double ym1 = lop.get(i - 1).getPosY();
                oArea.drawLine((int) ((xm1 - iStartX) + iSpaceLeft), (int) (iTotalSizeY - (ym1 - iStartY + iSpaceBottom)),
                                  (int) ((x - iStartX) + iSpaceLeft), (int) (iTotalSizeY - (y - iStartY + iSpaceBottom)));
            }
            iCount++;
        }
    }

    public void drawLegend(List<String> legend, List<Color> colors) {

        Graphics2D oArea = oIMG.createGraphics();
        FontMetrics oFontMetrics = oArea.getFontMetrics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        int iLengthLine = 10;

        int iHeight = oFontMetrics.getAscent();
        int iDistance = 2;

        int iMaxWidth = 0;
        for (String s : legend) {
            int fontWidth = oFontMetrics.stringWidth(s);
            if (iMaxWidth < fontWidth) {
                iMaxWidth = fontWidth;
            }
        }

        for (int i = 0; i < legend.size(); i++) {
            oArea.setColor(colors.get(i));
            oArea.drawString(legend.get(i), iTotalSizeX - iMaxWidth - iDistance - iLengthLine, (i + 1) * (iHeight) + iDistance);
        }

    }

    public void drawbars(List<? extends Position> lop, Color c) {

        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        oArea.setColor(c);
        int iCount = 0;
        for (int i = 0; i < lop.size(); i++) {
            double x = lop.get(i).getPosX();
            double y = lop.get(i).getPosY();
            if (iStartX > x || iStartY > y) {
                continue;
            }
            oArea.drawLine(
                    (int) ((x - iStartX) + iSpaceLeft),
                    (int) (iTotalSizeY - (0.0 - iStartY + iSpaceBottom)),
                    (int) ((x - iStartX) + iSpaceLeft),
                    (int) (iTotalSizeY - (y - iStartY + iSpaceBottom))
            );
//            oArea.drawOval((int) ((x - iStartX) + iSpaceLeft), (int) (iTotalSizeY - (y - iStartY + iSpaceBottom)), 5, 5);
            iCount++;
        }
    }

    public void drawbar(double x, double y, Color c) {
        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();
        oArea.setColor(c);
        oArea.drawLine(
                (int) ((x - iStartX) + iSpaceLeft),
                (int) (iTotalSizeY - (0.0 - iStartY + iSpaceBottom)),
                (int) ((x - iStartX) + iSpaceLeft),
                (int) (iTotalSizeY - (y - iStartY + iSpaceBottom))
        );
    }

    public void drawbar(double x, Color c) {
        Graphics2D oArea = oIMG.createGraphics();
        int iTotalSizeX = oIMG.getWidth();
        int iTotalSizeY = oIMG.getHeight();

        oArea.setColor(c);

        oArea.drawLine(
                (int) ((x - iStartX) + iSpaceLeft),
                (int) (iTotalSizeY - (0.0 - iStartY + iSpaceBottom)),
                (int) ((x - iStartX) + iSpaceLeft),
                (int) (iSpaceTop)
        );
    }

    public int getPlotAreaHeight() {
        int iTotalSizeY = oIMG.getHeight();

        return iTotalSizeY - iSpaceTop - iSpaceBottom;
    }

    public int getPlotAreaWidth() {
        int iTotalSizeX = oIMG.getWidth();

        return iTotalSizeX - iSpaceLeft - iSpaceRight;
    }

}
