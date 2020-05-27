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
package com.tivconsultancy.opentiv.postproc.vector;


import com.tivconsultancy.opentiv.helpfunctions.colorspaces.Colorbar;
import com.tivconsultancy.opentiv.math.interfaces.GeneralValue;
import com.tivconsultancy.opentiv.math.primitives.Vector;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.batik.dom.GenericDOMImplementation;
//import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;


/**
 *
 * @author Thomas Ziegenhein
 */
public class SVG {    

    public static SVGGraphics2D getGraphics() {
        DOMImplementation domImpl
                = GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        return new SVGGraphics2D(document);
    }
    
    public static void writeOut(String sFile, SVGGraphics2D g2d) throws UnsupportedEncodingException, FileNotFoundException, SVGGraphics2DIOException, IOException{
        boolean useCSS = true;
        Writer out = new OutputStreamWriter(new FileOutputStream(sFile), "UTF-8");
        g2d.stream(out, useCSS);
        out.close();
    }
    
    public static SVGGraphics2D paintVectors(List<? extends Vector> loVelo, Colorbar oColorbar, int iStretch) throws IOException {
        
        SVGGraphics2D g = getGraphics();                        
        

        for (int i = 0; i < loVelo.size(); i++) {
            VectorArrow v1 = new VectorArrow(loVelo.get(i), iStretch);

            if (oColorbar != null) {                
                g.setColor(oColorbar.getColor(loVelo.get(i).opUnitTangent.dValue));
            }
            
            g.drawLine(v1.dmid[1].intValue(), v1.dmid[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());

            if (v1.drighttip != null && !v1.drighttip[1].isNaN() && !v1.drighttip[0].isNaN()) {
                g.drawLine(v1.drighttip[1].intValue(), v1.drighttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
            }
            if (v1.dlefttip != null && !v1.dlefttip[1].isNaN() && !v1.dlefttip[0].isNaN()) {
                g.drawLine(v1.dlefttip[1].intValue(), v1.dlefttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
            }
        }
        return g;
    }
    
    public static SVGGraphics2D paintVectors(String sOutput, List<? extends Vector> loVelo, Colorbar oColorbar, int iStretch) throws IOException {
        
        SVGGraphics2D g = paintVectors(loVelo, oColorbar, iStretch);
        writeOut(sOutput, g);
        return g;
    }
    
    public static SVGGraphics2D paintVectors(String sOutput, List<? extends Vector> loVelo, Colorbar oColorbar, int iStretch, GeneralValue o) throws IOException {
        
        SVGGraphics2D g = getGraphics();                        
        

        for (int i = 0; i < loVelo.size(); i++) {
            VectorArrow v1 = new VectorArrow(loVelo.get(i), iStretch);

            if (oColorbar != null) {                
                g.setColor(oColorbar.getColor(loVelo.get(i).opUnitTangent.dValue));
            }
            
            g.drawLine(v1.dmid[1].intValue(), v1.dmid[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());

            if (v1.drighttip != null && !v1.drighttip[1].isNaN() && !v1.drighttip[0].isNaN()) {
                g.drawLine(v1.drighttip[1].intValue(), v1.drighttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
            }
            if (v1.dlefttip != null && !v1.dlefttip[1].isNaN() && !v1.dlefttip[0].isNaN()) {
                g.drawLine(v1.dlefttip[1].intValue(), v1.dlefttip[0].intValue(), v1.dtip[1].intValue(), v1.dtip[0].intValue());
            }
            
            int iStartTexti = (int) loVelo.get(i).y;
            int iStartTextj = (int) loVelo.get(i).x;
            
            if(loVelo.get(i).getY() < 0){
                g.drawString(o.getValue(loVelo.get(i)).toString(), iStartTextj, iStartTexti + 15);
            }else{
                g.drawString(o.getValue(loVelo.get(i)).toString(), iStartTextj, iStartTexti - 10);
            }
            
            
        }        
        g.drawLine(0, 0, 0, 0);
        g.drawLine(1280, 0, 1280, 0);
        g.drawLine(1280, 1024, 1270, 1014);
        g.drawLine(0, 1024, 0, 1024);

        writeOut(sOutput, g);
        return g;
    }

}
