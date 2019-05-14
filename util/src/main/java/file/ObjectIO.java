/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author dpf
 */
public enum ObjectIO {

    ;

    public static void writeObject(Object o, File outputFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
    
    public static void writeObject(Object o, String outputFile) {
        writeObject(o, new File(outputFile));
    }

    public static Object readObject(File inputFile) {
        FileInputStream fis = null;
        Serializable s = null;
        try {
            fis = new FileInputStream(inputFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            s = (Serializable) ois.readObject();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.err);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace(System.err);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return s;
    }
    
    public static Object readObject(String inputFile) {
        return readObject(new File(inputFile));
    }

}
