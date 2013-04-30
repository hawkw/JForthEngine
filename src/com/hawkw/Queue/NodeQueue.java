package com.hawkw.Queue;
/**
 * NodeQueue.java Implements the Queue ADT using a linked list. Contains code
 * taken from Goodrich & Tomassia
 * 
 * @author Hawk Weisman c
 */

public class NodeQueue<E> implements Queue<E> {

	private int size; // the size of the queue
	private Node<E> head; // the head node of the queue
	private Node<E> tail; // the tail node of the queue

	/**
	 * Adds an element to the rear of the queue
	 * 
	 * @param element
	 *            the element to be enqueued
	 * @author Goodrich & Tomassia
	 */
	@Override
	public void enqueue(E element) {
		Node<E> node = new Node<E>();
		node.setElement(element);
		node.setNext(null); // node will be new tail node
		if (size == 0)
			head = node; // special case of a previously empty queue
		else
			tail.setNext(node); // add node at the tail of the list
		tail = node; // update the reference to the tail node
		size++;
	}

	/**
	 * Removes an element from the front of the queue
	 * 
	 * @return the element at the head of the queue
	 * @throws EmptyQueueException
	 *             if the queue is empty
	 * @author Goodrich & Tomassia
	 */
	@Override
	public E dequeue() throws EmptyQueueException {
		if (empty())
			throw new EmptyQueueException("Queue is empty.");
		E tmp = head.getElement();
		head = head.getNext();
		size--;
		if (size == 0)
			tail = null; // the queue is now empty
		return tmp;
	}

	/**
	 * returns the number of elements in this queue
	 * 
	 * @return the number of elements in this queue
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * returns true if the queue is empty, false otherwise
	 * 
	 * @return true if the queue is empty, false otherwise
	 */
	@Override
	public boolean empty() {
		return (size == 0);
	}

	/**
	 * returns the front element of the queue
	 * 
	 * @return the front element of the queue
	 * @throws EmptyQueueException
	 *             if the queue is empty
	 */
	@Override
	public E front() throws EmptyQueueException {
		if (empty())
			throw new EmptyQueueException("Queue is empty."); // whoops
		return head.getElement();
	}
}
