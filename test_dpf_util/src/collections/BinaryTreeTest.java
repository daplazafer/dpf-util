/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

import java.util.Iterator;
import util.ConsoleIO;

/**
 *
 * @author dpf
 */
public class BinaryTreeTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        

         ConsoleIO.println("            8");
         ConsoleIO.println("          /   \\");
         ConsoleIO.println("         5     4");
         ConsoleIO.println("        / \\     \\");
         ConsoleIO.println("       9   7     11");
         ConsoleIO.println("          / \\    /");
         ConsoleIO.println("         1  12  3");
         ConsoleIO.println("            /");
         ConsoleIO.println("           2\n");

        
        // Nodes
        BinaryTree<Integer> n1 = new BinaryTree(1);
        BinaryTree<Integer> n2 = new BinaryTree(2);
        BinaryTree<Integer> n3 = new BinaryTree(3);
        BinaryTree<Integer> n4 = new BinaryTree(4);
        BinaryTree<Integer> n5 = new BinaryTree(5);
        BinaryTree<Integer> n7 = new BinaryTree(7);
        BinaryTree<Integer> n8_root = new BinaryTree(8);
        BinaryTree<Integer> n9 = new BinaryTree(9);
        BinaryTree<Integer> n11 = new BinaryTree(11);
        BinaryTree<Integer> n12 = new BinaryTree(12);
        
        // Build the tree
        n8_root.setLeftChild(n5);
        n8_root.setRightChild(n4);
        n5.setLeftChild(n9);
        n5.setRightChild(n7);
        n4.setRightChild(n11);
        n7.setLeftChild(n1);
        n7.setRightChild(n12);
        n11.setLeftChild(n3);
        n12.setLeftChild(n2);

        // Print inorder
        printInOrder(n8_root);
        
        ConsoleIO.println("-----------------------------------");
        
        String file = "test_binaryTree";
        
        n8_root.saveToFile(file);
        BinaryTree<Integer> tree = new BinaryTree<>(file);
        
        // Print inorder
        printInOrder(tree);
        
    }
    
    private static void printInOrder(BinaryTree<Integer> node){
        ConsoleIO.print("InOrder: ");
        Iterator<BinaryTree<Integer>> i = node.iterator();
        while (i.hasNext()) {
            ConsoleIO.print(i.next() + " ");
        }
        ConsoleIO.println();
    }

}
