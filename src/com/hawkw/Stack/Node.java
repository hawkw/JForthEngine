package com.hawkw.Stack;
public class Node<E> {
	private E element;			// the element stored by this node
	private Node<E> nextNode;	// the next node in the linked list
	
	/**
	 * 0-param constructor: creates a node with null references
	 * for both instance variables
	 */
	public Node () {
		this(null,null);
	}
	
	/**
	 * 2-param constructor: creates a new node with a reference
	 * to the contained element and a reference to the next node.
	 * @param element the element E to be stored by this node
	 * @param nextNode the next node
	 */
	public Node (E element, Node<E> nextNode) {
		this.element = element;
		this.nextNode = nextNode;
	}
	
	/**
	 * accessor for element
	 * @return element this node's element
	 */
	public E getElement () {
		return element;
	}
	
	/**
	 * accessor for nextNode
	 * @return nextNode this node's nextNode
	 */
	public Node<E> getNext () {
		return nextNode;
	}
	
	/**
	 * mutator for element
	 * @param element this node's element
	 */
	public void setElement (E element) {
		this.element = element;
	}
	
	/**
	 * mutator for nextNode
	 * @param nextNode this node's nextNode
	 */
	public void setNext (Node<E> nextNode) {
		this.nextNode = nextNode;
	}
	
}