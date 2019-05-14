/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package time;

import collections.Sort;
import time.ParallelTask.Task;
import util.ConsoleIO;
import util.Util;

/**
 *
 * @author dpf
 */
public class ParallelTaskTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 4096;
        Integer[][] m1 = new Integer[n][n];
        Integer[][] m2 = new Integer[n][n];
        Integer[][] m3 = new Integer[n][n];
        Integer[][] m4 = new Integer[n][n];
        generateRandomMatrix(n, m1, m2);
        
        Chronometer c = new Chronometer(false);
        
        //Secuencial
        c.start();
        for(int i = 0 ; i<m1.length ; i++)
            for(int j = 0 ; j<m1[0].length ; j++){
                m3[i][j] = m1[i][j] + m2[i][j];
            }
        ConsoleIO.println("Secuencial\t"+c.stop()+" ms");
        
        //Parallel
        c.start();
        int threads = 4;
        int section = n/threads;
        ParallelTask pt;
        pt = new ParallelTask((Task) () -> {
            for(int i = 0 ; i< 1*section ; i++)
                for(int j = 0; j< n ; j++){
                    m4[i][j] = m1[i][j] + m2[i][j];
                }
        },(Task) () -> {
            for(int i = 1*section ; i< 2*section ; i++)
                for(int j = 0; j< n ; j++){
                    m4[i][j] = m1[i][j] + m2[i][j];
                }
        },(Task) () -> {
            for(int i = 2*section ; i< 3*section ; i++)
                for(int j = 0; j< n ; j++){
                    m4[i][j] = m1[i][j] + m2[i][j];
                }   
        },(Task) () -> {
            for(int i = 3*section ; i< n ; i++)
                for(int j = 0; j< n ; j++){
                    m4[i][j] = m1[i][j] + m2[i][j];
                }
        });
        pt.startTasks(true);
        ConsoleIO.println("Parallel\t"+c.stop()+" ms");
        
        ConsoleIO.println(compareMatrix(m3, m4));
        
    }
    
    private static void generateRandomMatrix(int n, Integer[][]... m){
        int r;
        for(int i = 0; i< n ; i++)
            for(int j = 0; j< n ; j++){
                r = Util.randomInt(-999,999);
                for(Integer[][] matrix : m){
                    matrix[i][j] = r;
                }
            }
    }
    
    private static boolean compareMatrix(Integer[][] m1, Integer[][] m2){
        for(int i = 0; i< m1.length ; i++)
            for(int j = 0 ; j<m1[0].length; j++){
                if(m1[i][j].compareTo(m2[i][j]) != 0){
                    return false;
                }
            }
        return true;
    }
    
}
