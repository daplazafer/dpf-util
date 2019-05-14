package file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel Plaza
 */
public abstract class FileUtil {

    /**
     * Copy a file to a destination
     *
     * @param source File to be copied
     * @param dest destination path
     * @throws IOException
     */
    public static void copyFile(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            if (inputChannel != null) {
                inputChannel.close();
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        }
    }

    /**
     * Copy a file to a destination
     *
     * @param source File to be copied
     * @param dest destination path
     * @throws IOException
     */
    public static void copyFile(String source, String dest) throws IOException {
        copyFile(new File(source), new File(dest));
    }

    /**
     * Find out into a directory in order to get all the files with the string
     * passed as parameter.
     *
     * @param path path to search in
     * @param ext extension of the desired files. Ex: ".jpg"
     * @return an array list of filenames
     */
    public static List<File> findFiles(String path, String ext) {
        List<File> fileList = new ArrayList<>();
        File f = new File(path);
        File[] allFiles = f.listFiles();
        for (File file : allFiles) {
            if (file.isFile() && file.toString().contains(ext)) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    
    public static List<File> findDirectories(String path){
        List<File> dirList = new ArrayList<>();
        File f = new File(path);
        File[] allFiles = f.listFiles();
        for (File file : allFiles) {
            if (file.isDirectory()) {
                dirList.add(file);
            }
        }
        return dirList;
    }

    /**
     * Set the default output to a file rather than console
     *
     * @param file text file
     * @throws FileNotFoundException
     */
    public static void setOutFile(File file) throws FileNotFoundException {
        PrintStream ps = new PrintStream(file);
        System.setOut(ps);
    }

    /**
     * Set the default output to a file rather than console
     *
     * @param file text file
     * @throws FileNotFoundException
     */
    public static void setOutFile(String file) throws FileNotFoundException {
        setOutFile(new File(file));
    }

    /**
     * Compare two files
     *
     * @param a first file
     * @param b the other file
     * @return true if the files are equals
     */
    public static final boolean compareFiles(File a, File b) {
        int _size = 1024;
        try {
            final Path filea = Paths.get("", a.getName());
            final Path fileb = Paths.get("", b.getName());
            if (Files.size(filea) != Files.size(fileb)) {
                return false;
            }

            final long size = Files.size(filea);
            final int mapspan = _size * _size * 4;

            try (FileChannel chana = (FileChannel) Files.newByteChannel(filea);
                    FileChannel chanb = (FileChannel) Files.newByteChannel(fileb)) {

                for (long position = 0; position < size; position += mapspan) {
                    MappedByteBuffer mba = mapChannel(chana, position, size, mapspan);
                    MappedByteBuffer mbb = mapChannel(chanb, position, size, mapspan);

                    if (mba.compareTo(mbb) != 0) {
                        return false;
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return true;
    }

    private static MappedByteBuffer mapChannel(FileChannel channel, long position, long size, int mapspan) throws IOException {
        final long end = Math.min(size, position + mapspan);
        final long maplen = (int) (end - position);
        return channel.map(MapMode.READ_ONLY, position, maplen);
    }

    /**
     * Compare two files
     *
     * @param a first file
     * @param b the other file
     * @return true if the files are equals
     */
    public static final boolean compareFiles(String a, String b) {
        return compareFiles(new File(a), new File(b));
    }

    /**
     *
     * @param filename
     * @return lines count
     * @throws IOException
     */
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
}
