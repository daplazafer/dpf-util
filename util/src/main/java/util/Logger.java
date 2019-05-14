package util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Daniel Plaza
 */
public abstract class Logger {
    
    private final static String SEPARATOR = " : ";
    private final static SimpleDateFormat DATEFORMAT = Util.DEFAULTDATEFORMAT;
    
    private PrintStream ps = null;
    
    /**
     * Prints through console
     */
    protected Logger() {
        ps = System.out;
    }

    protected Logger(File logFile) {
        if (logFile != null) {
            try {
                ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(logFile, true)), true);
            } catch (FileNotFoundException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    protected void log(String line) {
        if (ps != null)
            ps.println(DATEFORMAT.format(new Date()) +SEPARATOR+ line);
    }

}
