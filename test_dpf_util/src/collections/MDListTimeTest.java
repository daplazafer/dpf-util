/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import time.Chronometer;
import util.ConsoleIO;
import util.Util;

/**
 *
 * @author dpf
 */
public class MDListTimeTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List al;
        List gl;
        List dml1;

        int min = 11;
        int max = 21;

        int size;
        for (int i = min; i <= max; i++) {
            size = (int) Math.pow(2, i);

            al = new ArrayList();
            gl = new GlueList();
            dml1 = new ModularList();

            ConsoleIO.println("Initial size: " + size);
            ConsoleIO.println("TEST\t\t\tDMList/ms\tGlueList/ms\tArrayList/ms");
            compareLists(size, dml1, gl, al);
            ConsoleIO.println();
            ConsoleIO.println("Equals: " + listsEquals(al, dml1, gl));
            ConsoleIO.println();
            ConsoleIO.println("-----------------------------------------------------------------------");
        }

    }

    private static String generateRandomObject() {
        return UUID.randomUUID().toString();
    }

    private static void fillLists(int listSize, List<String>... lists) {
        String o;
        for (int i = 0; i < listSize; i++) {
            o = generateRandomObject();
            for (List<String> l : lists) {
                l.add(o);
            }
        }
    }

    private static void compareLists(int listSize, List<String>... lists) {
        fillLists(listSize, lists);
        long totals[] = new long[lists.length];
        long time;
        int[] tests;

        tests = get_random_tests(lists[0].size(),lists[0].size()/8);
        ConsoleIO.print("getI(" + tests.length + ")\t\t");
        for (int i = 0; i < lists.length; i++) {
            time = testGetIndex(lists[i], tests);
            ConsoleIO.print(time + "\t\t");
            totals[i] += time;
        }
        ConsoleIO.println();

        /*
         ConsoleIO.print("findO("+tests.length+")\t\t");
         for(int i = 0; i< lists.length ; i++){
         time = testFindO(lists[i],tests);
         ConsoleIO.print(time+"\t\t");
         totals[i]+=time;
         }
         ConsoleIO.println();
         */
        
        tests = get_random_tests(lists[0].size(),lists[0].size()/8);
        ConsoleIO.print("addEnd(" + tests.length + ")\t\t");
        for (int i = 0; i < lists.length; i++) {
            time = testAddEnd(lists[i], tests);
            ConsoleIO.print(time + "\t\t");
            totals[i] += time;
        }
        ConsoleIO.println();

        tests = get_random_tests(lists[0].size(),listSize/8);
        ConsoleIO.print("addI(" + tests.length + ")\t\t");
        for (int i = 0; i < lists.length; i++) {
            time = testAddIndex(lists[i], tests);
            ConsoleIO.print(time + "\t\t");
            totals[i] += time;
        }
        ConsoleIO.println();

        tests = get_random_tests_remove(lists[0].size()-lists[0].size()/8,listSize/8);
        ConsoleIO.print("removeI(" + tests.length + ")\t\t");
        for (int i = 0; i < lists.length; i++) {
            time = testRemove(lists[i], tests);
            ConsoleIO.print(time + "\t\t");
            totals[i] += time;
        }
        ConsoleIO.println();

        ConsoleIO.print("TOTAL(" + tests.length * 4 + ")\t\t");
        for (long total : totals) {
            ConsoleIO.print(total + "\t\t");
        }
        ConsoleIO.println();

        ConsoleIO.print("%\t\t\t");
        ConsoleIO.print("-\t\t");
        for (int i = 1; i < totals.length; i++) {
            ConsoleIO.printf("%.2f\t\t", ((double) totals[i]) * 100 / ((double) totals[0]));
        }
        ConsoleIO.println();

    }

    private static long testAddEnd(List<String> l, int[] index) {
        Chronometer c = new Chronometer();
        for (int i : index) {
            l.add("" + i);
        }
        return c.stop();
    }

    private static long testAddIndex(List<String> l, int[] index) {
        Chronometer c = new Chronometer();
        for (int i : index) {
            l.add(i, "" + i);
        }
        return c.stop();
    }

    private static long testGetIndex(List l, int[] index) {
        Chronometer c = new Chronometer();
        for (int i : index) {
            l.get(i);
        }
        return c.stop();
    }

    private static long testFindO(List<String> l, int[] index) {
        Chronometer c = new Chronometer();
        for (int i : index) {
            l.indexOf("" + i);
        }
        return c.stop();
    }

    private static long testRemove(List l, int[] index) {
        Chronometer c = new Chronometer();
        for (int i : index) {
            l.remove(i);
        }
        return c.stop();
    }

    private static int[] get_random_tests(int listSize, int tests_count) {
        int tests[] = new int[tests_count];
        for (int j = 0; j < tests.length; j++) {
            tests[j] = Util.randomInt(0, listSize-1);
        }
        return tests;
    }
    
    private static int[] get_random_tests_remove(int listSize, int tests_count) {
        int tests[] = new int[tests_count];
        for (int j = 0; j < tests.length; j++) {
            tests[j] = Util.randomInt(0, listSize-1-j);
        }
        return tests;
    }
    
    private static boolean listsEquals(List... lists) {
        int size = lists[0].size();
        for (List l : lists) {
            if (l.size() != size) {
                return false;
            }
        }
        Object o;
        for (int i = 0; i < size; i++) {
            o = lists[0].get(i);
            for (List l : lists) {
                if (!l.get(i).equals(o)) {
                    return false;
                }
            }
        }
        return true;
    }

}
