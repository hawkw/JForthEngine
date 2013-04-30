package com.hawkw.Queue;
public class Node<E> {
	// Instance variables:
	private E element;
	private Node<E> next;

	/**
	 * 0-param constructor Creates a node with null references to its element
	 * and next node.
	 */
	public Node() {
		this(null, null);
	}

	/**
	 * 2-parameter constructor Creates a node with the given element and next
	 * node.
	 * 
	 * @param element
	 *            the element for this node
	 * @param next
	 *            the next node in the queue
	 */
	public Node(E element, Node<E> next) {
		this.element = element;
		this.next = next;
	}

	/**
	 * returns the element of this node
	 * 
	 * @return the element of this node
	 */
	public E getElement() {
		return element;
	}

	/**
	 * returns the next node in the queue
	 * 
	 * @return the next node in the queue
	 */
	public Node<E> getNext() {
		return next;
	}

	/**
	 * sets the element of this node
	 * 
	 * @param element
	 *            the new element for this node
	 */
	public void setElement(E element) {
		this.element = element;
	}

	/**
	 * sets the next node of this node
	 * 
	 * @param next
	 *            the new next node
	 */
	public void setNext(Node<E> next) {
		this.next = next;
	}
}
