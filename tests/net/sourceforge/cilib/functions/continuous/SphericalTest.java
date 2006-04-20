/*
 * SphericalTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 2:49 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 *   
 */

package net.sourceforge.cilib.functions.continuous;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.Spherical;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;


/**
 *
 * @author Edwin Peer
 */
public class SphericalTest {
    
    public SphericalTest() {

    }
    
    /*public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(SphericalTest.class);
        
        return suite;
    }*/
    
    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Spherical. */
    @Test
    public void testEvaluate() {
    	ContinuousFunction function = new Spherical();
        function.setDomain("R(-100, 100)^3");
        //double[] x = {1, 2, 3};
        Vector x = new MixedVector();
        x.append(new Real(1.0));
        x.append(new Real(2.0));
        x.append(new Real(3.0));
        
        assertEquals(14.0, function.evaluate(x), 0.0);
    }
    
    public void testGradient() {
    	// add gradient test
    }
    
}
