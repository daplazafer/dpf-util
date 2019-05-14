/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import time.Chronometer;
import util.ConsoleIO;

/**
 *
 * @author dpf
 */
public class TestFunction {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String function = "(3*W)^4-W*log2((512*W)+1)-pi^W";
        
        Function f = new Function(function,"W");
        ConsoleIO.println(f);
        for(int i=0; i<=10;i++)
            ConsoleIO.println(f.toString(i));
        
        Chronometer c = new Chronometer();      
        ConsoleIO.print("solving\t\tf(x) = 0, x = "+f.solve());
        ConsoleIO.println("\ttime = "+c.lap()+" ms");
        ConsoleIO.print("derivative\tf'(3) = "+f.derivative(3));
        ConsoleIO.println("\t\ttime = "+c.lap()+" ms");
        ConsoleIO.print("integral\tin (2,3) = "+f.integral(2, 3));
        ConsoleIO.println("\t\ttime = "+c.lap()+" ms");
        ConsoleIO.print("integral\tin (3,2) = "+f.integral(3, 2));
        ConsoleIO.println("\t\ttime = "+c.lap()+" ms");
        
        /*
        ---------------------------------------------------------------------
        */
        ConsoleIO.println("\n\n----------------------------------------------\n\n");
        
        String function1 = "sin(X!)/(3*X^4)";
        
        Function f1 = new Function(function1,"X");
        ConsoleIO.println(f1.image(6));
                       
    }
    
}
