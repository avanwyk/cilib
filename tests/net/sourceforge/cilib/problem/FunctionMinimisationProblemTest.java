/*
 * FunctionMinimisationProblemTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 4:45 PM
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

package net.sourceforge.cilib.problem;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.functions.continuous.Ackley;
import net.sourceforge.cilib.functions.continuous.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 *
 * @author Edwin Peer
 */
public class FunctionMinimisationProblemTest {

    private static Random random;
    private static double[] x;
    private static Function function;
    private static FunctionMinimisationProblem problem;
    
    public FunctionMinimisationProblemTest() {
      
    }
    
    /*public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FunctionMinimisationProblemTest.class);
        
        return suite;
    }*/
    
    @BeforeClass
    public static void setUp() {
        function = new Spherical();
        function.setDomain("R^5");
        problem = new FunctionMinimisationProblem();
        problem.setFunction(function);
        random = new Random();
        x = new double[function.getDimension()];
        for (int i = 0; i < function.getDimension(); ++i) {
            x[i] = random.nextDouble();
        }
    }
       
    
    
    /** Test of getFunction method, of class za.ac.up.cs.ailib.Functions.FunctionMinimisationProblem. */
    @Test
    public void testGetFunction() {
        assertSame(function, problem.getFunction());
    }
    
    /** Test of setFunction method, of class za.ac.up.cs.ailib.Functions.FunctionMinimisationProblem. */
    @Test
    public void testSetFunction() {
        Function f = new Ackley();
        problem.setFunction(f);
        assertSame(f, problem.getFunction());
    }
    
    
    
}
