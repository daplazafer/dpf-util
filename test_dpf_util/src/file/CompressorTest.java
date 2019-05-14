/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import file.FileCompressor.Dictionary;
import java.io.File;
import time.Chronometer;
import util.ConsoleIO;

/**
 *
 * @author dpf
 */
public class CompressorTest {

    private static final String FILE = "in.txt";
    private static final String FILE_COMP = "out_c.comp";
    private static final String FILE_DECOMP = "out_d.txt";
    private static final String FILE_DIC = "dictionary.d";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        long tgenerate, tcompress, tdecompress;
        Chronometer c = new Chronometer(false);

        // Genera el diccionario
        c.start();
        Dictionary dictionary = FileCompressor.generateDictionary(FILE);
        tgenerate = c.stop();

        dictionary.printDictionary();// imprime el diccionario


        // Comprime el fichero
        c.start();
        FileCompressor.compressFile(FILE, FILE_COMP, dictionary);
        tcompress = c.stop();


        // Descomprime el fichero
        c.start();
        FileCompressor.decompressFile(FILE_COMP, FILE_DECOMP, dictionary);
        tdecompress = c.stop();

        // Escribe los tamaños de los ficheros, el % de compresión y los tiempos
        // de ejecución
        File source = new File(FILE), comp = new File(FILE_COMP), decomp = new File(FILE_DECOMP);
        int t_o = (int) source.length(), t_c = (int) comp.length(), t_d = (int) decomp.length();
        ConsoleIO.printf("\nCompression process of  \"" + FILE + "\" completed:\n\t- File source:\t\t" + t_o
                        + "\tbytes\n\t- File compressed:\t" + t_c + "\tbytes\n\t- File decompressed:\t" + t_d
                        + "\tbytes" + "\n\nCompression factor %.2f%s\n", (float) (100.0 - t_c * 100.0 / t_o), "%");
        // Compara el fichero orignal con el descomprimido
        if (file.FileUtil.compareFiles(source, decomp))
            ConsoleIO.println("Files are equal");
        else
            ConsoleIO.println("ERROR Files are different");

        ConsoleIO.println("\nExecution time:\n\t- Generate dictionary:\t\t" + tgenerate
                        + "\tms\n\t- Compress file:\t\t" + tcompress + "\tms\n\t- Decompress file:\t\t" + tdecompress
                        + "\tms\n\t- TOTAL:\t\t\t" + (tgenerate + tcompress + tdecompress) + "\tms");
        
        ConsoleIO.println("-----------------------------------------------------");
        
        ConsoleIO.println("Writting the ditionary to a file...");
        dictionary.saveToFile(new File(FILE_DIC));
        ConsoleIO.println("Reading the ditionary from file...");
        Dictionary dr = Dictionary.readFromFile(new File(FILE_DIC));
        ConsoleIO.println("Decompressing the file with the dictionary...");
        FileCompressor.decompressFile(FILE_COMP, FILE_DECOMP, dr);
        
        if (file.FileUtil.compareFiles(source, decomp))
                ConsoleIO.println("Files are equal");
        else
                ConsoleIO.println("ERROR Files are different");

    }
    
}
