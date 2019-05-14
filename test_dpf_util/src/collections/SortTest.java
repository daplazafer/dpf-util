/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

import java.util.Arrays;
import time.Chronometer;
import util.ConsoleIO;
import util.Util;

/**
 *
 * @author dpf
 */
public class SortTest {

    public static void main(String[] args) {
        int n = 1000000;
        Integer[] v1 = new Integer[n];
        Integer[] v2 = new Integer[n];
        Integer[] v3 = new Integer[n];
        Integer[] v4 = new Integer[n];
        generateRandomVector(n, v1, v2, v3, v4);

        Chronometer c = new Chronometer(false);
        
/*
        c.start();
        Sort.mergeSort(v1);
        ConsoleIO.println("MergeSort\t"+c.stop()+" ms");
*/
        c.start();
        Sort.quickSort(v2);
        ConsoleIO.println("QuickSort\t"+c.stop()+" ms");

        c.start();
        Arrays.sort(v3);
        ConsoleIO.println("ArraysSort\t"+c.stop()+" ms");
        
        c.start();
        Sort.quickSortParallel(v4);
        ConsoleIO.println("ParQuickSort\t"+c.stop()+" ms");
        
        ConsoleIO.println(compareVectors(v3,v4));
        
        
    }
    
    private static void print(Integer[]... vector){
        for(Integer[] v: vector){
            for(Integer o : v){
                ConsoleIO.print(o+" ");
            }
            ConsoleIO.println();
        }
    }
    
    private static void generateRandomVector(int n, Integer[]... vectors){
        int r;
        for(int i = 0; i< n ; i++){
            r = Util.randomInt(-99999999,99999999);
            for(Integer[] v : vectors){
                v[i] = r;
            }
        }
    }
    
    private static boolean compareVectors(Integer[] v1, Integer[] v2){
        if(v1.length != v2.length){
            return false;
        }
        for(int i = 0 ; i<v1.length;i++){
            if(v1[i].compareTo(v2[i]) != 0){
                return false;
            }
        }
        return true;
    }
    
}
