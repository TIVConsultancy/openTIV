/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.velocimetry.boundarytracking;

import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import java.io.IOException;

/**
 *
 * @author Thomas Ziegenhein
 */
public class OpenTIV_BoundaryTracking {

    /**
     * @param args the command line arguments
     */    

    public static void main(String[] args) throws IOException, EmptySetException {        
        BoundTrackZiegenhein_2018.perform();
        System.exit(0);
    }

    

}
