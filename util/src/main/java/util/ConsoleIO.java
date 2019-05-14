
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 *
 * @author Daniel Plaza
 */
public enum ConsoleIO {;
    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private static final PrintStream PS = System.out;
    private static final PrintStream PERR = System.err;
    
    /**
     * Reads the next input line of the console
     * @param message message before reading the line
     * @return String with the input text
     */
    public static String readLine(Object message){
        print(message);
        try {
            return READER.readLine();
        } catch (IOException ex) {
            return null;
        } 
    }
    
    /**
     * Reads the next input line of the console
     * @return String with the input text
     */
    public static String readLine(){
        return readLine("");
    }
    
    /**
     * Prints some text through console
     * @param line text to be printed
     */
    public static void print(Object line){
        PS.print(line.toString());
    }
    
    /**
     * Prints some text and terminates the line
     * @param line text line to be printed
     */
    public static void println(Object line){
        PS.println(line.toString());
    }
    
    /**
     * Terminates the current line
     */
    public static void println(){
        PS.println();
    }
    
    /**
     * Prints a formated line
     * @param format format for the text line
     * @param args arguments of the line
     */
    public static void printf(String format, Object... args){
        PS.printf(format, args);
    }
    
    /**
     * Prints an error
     * @param line error line text
     */
    public static void printerr(Object line){
        PERR.println(line);        
    }
    
}
