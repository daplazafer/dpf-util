/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import collections.BinaryTree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import util.ConsoleIO;

/**
 *
 * @author dpf
 */
public class FileCompressor {

    final static char CHAR_ZERO = '0';
    final static char CHAR_ONE = '1';

    private FileCompressor() {
    }

    /**
     * Genera el arbol de Huffman a partir de una lista de caracteres con su
     * frecuencia
     *
     * @param C_f Lista de caracteres con su frecuencia asociada
     * @return arbol de Huffman
     */
    private static BinaryTree<CharFreq> huffman(List<CharFreq> C_f) {

        // Variables
        Queue<BinaryTree<CharFreq>> Q = new PriorityQueue<>();
        BinaryTree<CharFreq> x, y, z;

        // Rellena la cola de prioridad
        C_f.stream().forEach((l) -> {
            Q.add(new BinaryTree<>(l));
        });

        // Genera el arbol de Huffman
        // Mientras la cola de prioridad tenga tamaño>1
        while (Q.size() > 1) {
            // Coge los dos primeros nodos de la cola
            x = Q.poll();
            y = Q.poll();
            // Crea un nuevo nodo con caracter null y freq la suma de los dos
            // anteriores
            z = new BinaryTree<>(new CharFreq(null, x.getElement().getFreq() + y.getElement().getFreq()));
            // Hace que los nodos sean sus hijos
            z.setLeftChild(x);
            z.setRightChild(y);
            // Añade el nodo a la cola de prioridad
            Q.add(z);
        }
        // Devuelve el último elemento de la cola, que es el arbol completo
        return Q.poll();
    }

    /**
     * Genera el diccionario de Huffman a partir de un fichero de texto pasado
     * como párametro.
     *
     * @param file Fichero a partir del cual se genera el diccionario.
     * @return El objeto de clase diccionario.
     */
    public static Dictionary generateDictionary(String file) {

        // Lee el archivo y genera la lista de letras con su frecuencia
        List<CharFreq> char_freq = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            boolean contains;
            int c;
            char ch;
            while ((c = br.read()) != -1) {
                ch = (char) c;
                contains = false;
                for (CharFreq l : char_freq) { // Si la lista contiene el caracter
                    if (l.getCharacter() == ch) {
                        // Suma 1 a su frecuencia de aparicion
                        l.increaseFreq();
                        contains = true;
                        break;
                    }
                }
                // Si no, lo añade a la lista con frecuencia 1
                if (!contains) {
                    char_freq.add(new CharFreq(ch, 1));
                }
            }
        } catch (IOException e) {
            throw new FileNotFoundException(file);
        }

        // Genera el arbol de Huffman
        BinaryTree<CharFreq> tree = huffman(char_freq);

        // Genera el diccionario
        Dictionary dictionary = new Dictionary();
        // Recorre el arbol
        Iterator<BinaryTree<CharFreq>> it = tree.iterator();
        String s;
        char c;
        while (it.hasNext()) {
            tree = it.next();
            // Si el nodo tiene un caracter asignado
            if (tree.getElement().getCharacter() != null) {
                c = tree.getElement().getCharacter();
                // Obtiene su codificacion
                s = getCodification(tree);
                // Guarda el caracter y la codificacion en el diccionario
                dictionary.newEntry(c, s);
            }
        }
        return dictionary;
    }

    private static String getCodification(BinaryTree<CharFreq> n) {
        if (n.isRoot()) {
            return "";
        } else if (n.isLeftChild()) {
            return getCodification((BinaryTree<CharFreq>) n.getParent()) + CHAR_ZERO;
        } else {
            return getCodification((BinaryTree<CharFreq>) n.getParent()) + CHAR_ONE;
        }
    }

    /**
     * Comprime el fichero a partir de un diccionario. Para el correcto
     * funcionamiento el diccionario debe haberse creado a partir del fichero
     * original.
     *
     * @param source Fichero que se desea comprimir
     * @param comp Ruta donde se guardará el fichero comprimido.
     * @param dictionary Diccionario para comprimir el fichero.
     */
    public static void compressFile(String source, String comp, Dictionary dictionary) {

        // Genera el BitSet
        BitSet bits = new BitSet();

        // Va leyendo el archivo original y rellenando el bitset
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source)))) {
            int leido, bi = 0;
            String cod;
            while ((leido = br.read()) != -1) {
                cod = dictionary.findCode((char) leido);
                for (int ci = 0; ci < cod.length(); ci++) {
                    if (cod.charAt(ci) == CHAR_ZERO) {
                        bits.set(bi, false);
                    } else {
                        bits.set(bi, true);
                    }
                    bi++;
                }
            }
            // Se marca el final del fichero con un 1 para no perder datos
            bits.set(bi, true);
        } catch (IOException e) {
            throw new FileNotFoundException(source);
        }
        // Escribe en el archivo destino
        // Escribe el BitSet como un array de bytes para evitar la cabecera del
        // objeto BitSet de java
        try (FileOutputStream fos = new FileOutputStream(comp)) {
            fos.write(bits.toByteArray());
            fos.close();
        } catch (IOException e) {
            throw new WrittingFileException(comp);
        }
    }

    /**
     * Descomprime un fichero previamente comprimido. Para el correcto
     * funcionamiento se debe haber utilizado el mismo diccionario para
     * comprimirlo.
     *
     * @param comp Fichero a descomprimir.
     * @param decomp Ruta donde se guardará el fichero descomprimido.
     * @param dictionary Diccionario para descomprimir el fichero.
     */
    public static void decompressFile(String comp, String decomp, Dictionary dictionary) {
        if (!(new File(comp)).exists()) {
            throw new FileNotFoundException(comp);
        }
        // Genera un BitSet con el binario
        BitSet bits;
        try (FileInputStream fis = new FileInputStream(comp)) {
            // Lee el BitSet del archivo comprimido
            // Lee como array de bytes
            byte[] bytes = new byte[(int) (new File(comp)).length()];
            fis.read(bytes);
            // Convierte a BitSet el array
            bits = BitSet.valueOf(bytes);
            fis.close();
        } catch (IOException e) {
            throw new ReadingFileException(comp);
        }

        // Se recorren los bits leidos
        // Se hace hasta lenght()-1 para no leer el ultimo bit que marca el
        // final del fichero
        String buffer = "";
        StringBuilder decompText = new StringBuilder();
        for (int i = 0; i < bits.length() - 1; i++) {
            // Por cada bit escribe 1 ó 0 en un buffer
            if (bits.get(i)) {
                buffer += CHAR_ONE;
            } else {
                buffer += CHAR_ZERO;
            }
            // Si el diccionario tiene una codificacion igual al buffer
            if (dictionary.containsCode(buffer)) {
                // Añade al texto el caracter correspondiente
                decompText.append(dictionary.findCharacter(buffer));
                // Vacia el buffer
                buffer = "";
            }
        }
        // Escribe el texto descomprimido en el fichero destino
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(decomp))) {
            bw.write(decompText.toString());
            bw.close();
        } catch (IOException e) {
            throw new WrittingFileException(decomp);
        }
    }

    private static class CharFreq implements Comparable<CharFreq> {

        private final Character character;
        private int freq;

        public CharFreq(Character character, int freq) {
            this.character = character;
            this.freq = freq;
        }

        public Character getCharacter() {
            return character;
        }

        public int getFreq() {
            return freq;
        }

        public void increaseFreq() {
            freq++;
        }

        @Override
        public int compareTo(CharFreq o) {
            if (this.getFreq() > o.getFreq()) {
                return 1;
            } else if (this.getFreq() < o.getFreq()) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    public static class Dictionary implements Serializable {

        private HashMap<Character, String> dic_W = new HashMap<>();
        private HashMap<String, Character> dic_R = new HashMap<>();

        private Dictionary() {
            dic_W = new HashMap<>();
            dic_R = new HashMap<>();
        }

        private void newEntry(Character t1, String t2) {
            dic_W.put(t1, t2);
            dic_R.put(t2, t1);
        }

        public Character findCharacter(String t) {
            return dic_R.get(t);
        }

        public String findCode(Character t) {
            return dic_W.get(t);
        }

        public boolean containsChar(Character t) {
            return dic_W.containsKey(t);
        }

        public boolean containsCode(String t) {
            return dic_R.containsKey(t);
        }

        public void saveToFile(File file) {
            ObjectIO.writeObject(this, file);
        }

        public static Dictionary readFromFile(File file) {
            return (Dictionary) ObjectIO.readObject(file);
        }

        public void printDictionary() {
            Iterator<Entry<Character, String>> it = dic_W.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ConsoleIO.println("'" + pair.getKey() + "'\t\t" + pair.getValue());
            }
        }
    }

    private static class FileNotFoundException extends RuntimeException {
        public FileNotFoundException(String file) {
            super("Requested file \"" + file + "\" doen not exist");
        }
    }

    private static class WrittingFileException extends RuntimeException {
        public WrittingFileException(String file) {
            super("Cannot write in the output file \"" + file + "\"");
        }
    }

    private static class ReadingFileException extends RuntimeException {
        public ReadingFileException(String file) {
            super("Cannot read the input file \"" + file + "\"");
        }
    }

}
