/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.velocimetry.directtracking;

import com.tivconsultancy.opentiv.math.exceptions.EmptySetException;
import com.tivconsultancy.opentiv.math.interfaces.SideCondition2;
import com.tivconsultancy.opentiv.math.sets.Set2D;
import com.tivconsultancy.opentiv.physics.interfaces.Trackable;
import com.tivconsultancy.opentiv.physics.vectors.VelocityVec;
import com.tivconsultancy.opentiv.velocimetry.iterativemethods.RelaxationMethods;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Cityk
 */
public class RelaxationTracking {
    
    public static VelocityVec getBernardThompson(Trackable otRef, List<? extends Trackable> loFirstFrame, List<? extends Trackable> loSecondFrame, double Rs, double Rc, double maxVelo) throws EmptySetException, IOException {
        Trackable bestMatch = RelaxationMethods.BarnardandThompson(otRef, loSecondFrame, loSecondFrame, Rs, Rc);
        if(bestMatch == null) return null;
        return new VelocityVec(bestMatch.getPosition().x - otRef.getPosition().x, bestMatch.getPosition().y - otRef.getPosition().y, otRef.getPosition());
    }
    
}
