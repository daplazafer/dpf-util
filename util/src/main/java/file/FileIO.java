package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 *
 * @author Daniel Plaza
 */
public class FileIO {

    private RandomAccessFile raf;
    private FileChannel channel;
    private FileLock lock = null;

    public FileIO(File file) {
        try {
            raf = new RandomAccessFile(file, "rw");
            channel = raf.getChannel();
        } catch (FileNotFoundException ex) {
        }
    }

    public FileIO(String file) {
        this(new File(file));
    }

    /**
     * Read the next line of the file moving the pointer (offset) to the next
     * line
     *
     * @return String which contains the line following the pointer
     * @throws EndOfFileException if it has reached the end of the file
     */
    public String readLine() throws EndOfFileException {
        String line = null;
        lock();
        try {
            line = raf.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        unlock();
        if (line == null) {
            throw new EndOfFileException();
        } else {
            return line;
        }
    }

    /**
     * Write text in the file
     *
     * @param text String to be written
     */
    public void write(String text) {
        lock();
        try {
            raf.writeBytes(text.replace("\n", "\r\n"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        unlock();
    }

    /**
     * Similar to write. Write a line and a line separator at the end of this
     *
     * @param line line to be written
     */
    public void writeln(String line) {
        write(line + "\n");
    }

    /**
     * Sets the file pointer offset
     *
     * @param pos new position of the offset
     */
    public void seek(int pos) {
        try {
            raf.seek(pos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Return the current offset in this file
     *
     * @return offset value
     */
    public long offset() {
        try {
            return raf.getFilePointer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns the length of this file
     *
     * @return the length of this file
     */
    public long getLength() {
        try {
            return raf.length();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void lock() {
        try {
            lock = channel.lock();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void unlock() {
        try {
            lock.release();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class EndOfFileException extends Exception {

    }

}
