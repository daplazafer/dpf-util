/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections.interfaces;

/**
 *
 * @author dpf
 * @param <E>
 */
public interface BinaryTreeNode<E> {

    public BinaryTreeNode<E> getParent();

    public BinaryTreeNode<E> getLeftChild();

    public void setLeftChild(BinaryTreeNode<E> leftChild);

    public BinaryTreeNode<E> getRightChild();

    public void setRightChild(BinaryTreeNode<E> rightChild);

    public E getElement();

    public boolean isRoot();

    public boolean isLeftChild();

    public boolean isRightChild();

    public boolean isLeaf();

}
