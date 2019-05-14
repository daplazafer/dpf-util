/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

import collections.interfaces.BinaryTreeNode;
import file.ObjectIO;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 *
 * @author dpf
 * @param <E>
 */
public class BinaryTree<E> implements BinaryTreeNode<E>, Iterable<BinaryTree<E>>, Comparable<BinaryTreeNode<E>>, Serializable {

    private BinaryTreeNode<E> parent, leftChild, rightChild;
    private E element;

    public BinaryTree(E element) {
        this.element = element;
    }

    public BinaryTree(File inputFile) {
        readFromFile(inputFile);
    }

    public BinaryTree(String inputFile) {
        this(new File(inputFile));
    }

    public void saveToFile(File outputFile) {
        List<BinaryTree<E>> fullTree;
        Queue<BinaryTree<E>> q, qaux; //DEFINICIÓN DE 2 VARIABLES DE TIPO COLA
        BinaryTree<E> aux; //DEFINICIÓN AUX DE TIPO NODOARBOL
        q = new LinkedList<>(); //SE INSTANCIA EL OBJETO COLA
        qaux = new LinkedList<>(); //SE INSTANCIA EL OBJETO COLAAUX
        q.add(this); //SE INSERTA EL NODOARBOL "A" (RAIZ) COMO PRIMER NODO EN LA COLA
        while (!q.isEmpty()) { //MIENTRAS HAYAN ELEMENTOS EN LA COLA...
            qaux.add(aux = q.remove()); /*EL ELEMENTO EXTRAIDO DE LA COLA PRINCIPAL ES ASIGNADO 
             A AUX Y A SU VEZ INSERTADO EN LA COLA AUXILIAR*/
            if (aux != null) {
                if (aux.leftChild != null) //SI EL HIJO IZQUIERDO DEL NODO ACTUAL EXISTE
                    q.add((BinaryTree<E>) aux.leftChild); //SE INSERTA ESE HIJO COMO ELEMENTO SIGUIENTE EN LA COLA
                if (aux.rightChild != null) //SI EL HIJO DERECHO DEL NODO ACTUAL EXISTE
                    q.add((BinaryTree<E>) aux.rightChild); //SE INSERTA ESE HIJO COMO ELEMENTO SIGUIENTE EN LA COLA
            }
        }
        fullTree = new LinkedList<>();
        fullTree.addAll(qaux);
        ObjectIO.writeObject(fullTree, outputFile);
    }

    public void saveToFile(String outputFile) {
        saveToFile(new File(outputFile));
    }

    private void readFromFile(File inputFile) {
        List<BinaryTree<E>> fullTree;
        fullTree = (List<BinaryTree<E>>) ObjectIO.readObject(inputFile);
        if(fullTree.get(0)!=null){
            if(fullTree.get(0).getElement()!=null)
                this.element = fullTree.get(0).getElement();
            if(fullTree.get(0).getParent()!=null)
                this.parent = fullTree.get(0).getParent();
            if(fullTree.get(0).getLeftChild()!=null)
                this.leftChild = fullTree.get(0).getLeftChild();
            if(fullTree.get(0).getRightChild()!=null)
                this.rightChild = fullTree.get(0).getRightChild();
        }
    }

    @Override
    public BinaryTreeNode<E> getParent() {
        return parent;
    }

    @Override
    public BinaryTreeNode<E> getLeftChild() {
        return leftChild;
    }

    @Override
    public void setLeftChild(BinaryTreeNode<E> leftChild) {
        BinaryTree<E> bt = (BinaryTree<E>) leftChild;
        bt.parent = this;
        this.leftChild = bt;
    }

    @Override
    public BinaryTreeNode<E> getRightChild() {
        return rightChild;
    }

    @Override
    public void setRightChild(BinaryTreeNode<E> rightChild) {
        BinaryTree<E> bt = (BinaryTree<E>) rightChild;
        bt.parent = this;
        this.rightChild = bt;
    }

    @Override
    public E getElement() {
        return element;
    }

    @Override
    public boolean isRoot() {
        return (parent == null);
    }

    @Override
    public boolean isLeftChild() {
        return (this.parent.getLeftChild() == this);
    }

    @Override
    public boolean isRightChild() {
        return (this.parent.getRightChild() == this);
    }

    @Override
    public boolean isLeaf() {
        return (this.getLeftChild() == null && this.getRightChild() == null);
    }

    @Override
    public String toString() {
        return element.toString();
    }

    @Override
    public Iterator<BinaryTree<E>> iterator() {
        return new BinaryTreeInOrderIterator(this);
    }

    @Override
    public int compareTo(BinaryTreeNode<E> o) {
        return ((Comparable) element).compareTo((Comparable) o.getElement());
    }

    private class BinaryTreeInOrderIterator implements Iterator<BinaryTree<E>> {

        BinaryTree<E> next;

        BinaryTreeInOrderIterator(BinaryTree startNode) {
            this.next = startNode;
            if (next == null) {
                return;
            }
            while (next.leftChild != null) {
                next = (BinaryTree) next.leftChild;
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public BinaryTree<E> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            BinaryTree r = next;
            if (next.rightChild != null) {
                next = (BinaryTree<E>) next.rightChild;
                while (next.leftChild != null) {
                    next = (BinaryTree<E>) next.leftChild;
                }
                return r;
            } else {
                while (true) {
                    if (next.parent == null) {
                        next = null;
                        return r;
                    }
                    if (((BinaryTree<E>) (next.parent)).leftChild == next) {
                        next = (BinaryTree<E>) next.parent;
                        return r;
                    }
                    next = (BinaryTree<E>) next.parent;
                }
            }
        }

    }

}
