/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.Serializable;
import util.ConsoleIO;

/**
 *
 * @author dpf
 */
public class ObjectIOTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String objectFile = "objectIO_test.object";
        
        TestClass tcw = new TestClass(3, "hello");
        TestClass tcr;
        
        ObjectIO.writeObject(tcw, objectFile);
            
        tcr = (TestClass) ObjectIO.readObject(objectFile);
            
        ConsoleIO.println(tcr);
  
    }
    
    public static class TestClass implements Serializable{
        
        private int number;
        private String str;

        public TestClass(int number, String str) {
            this.number = number;
            this.str = str;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return number +" "+ str;
        }
        
        
        
    }
    
}
