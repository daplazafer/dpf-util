/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 *
 * @author Daniel Plaza
 */
public enum Util {

    ;

    public static final SimpleDateFormat DEFAULTDATEFORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat TIME12HFORMAT = new SimpleDateFormat("hh:mm");
    public static final SimpleDateFormat TIME24HFORMAT = new SimpleDateFormat("HH:mm");

    /**
     * It opens a URL in the user's default browser.
     *
     * @param url link to the web page
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     */
    public static void openWebpage(String url) throws MalformedURLException, URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URL(url).toURI());
    }

    /**
     * @return Screen width
     */
    public static double getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }

    /**
     * @return Screen height
     */
    public static double getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }

    /**
     * Copy to clipboard
     *
     * @param text String to be copied to the clipboard
     */
    public static void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    public static int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot exceed max.");
        }
        Random r = new Random();
        long range = (long) max - (long) min + 1;
        long fraction = (long) (range * r.nextDouble());
        int randomNumber = (int) (fraction + min);
        return randomNumber;
    }
    
    public static double random(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot exceed max.");
        }
        Random r = new Random();
        double range =  max -  min + 1.0;
        double fraction = (range * r.nextDouble());
        return fraction + min;
    }
    
    public static double random(){
        Random r = new Random();
        return r.nextDouble();
    }
    
    public static void throwException(Exception e){
        throw new RuntimeException(e.getMessage());
    }
}
