package com.hawkw.Queue;

/**
 * ArrayQueue.java implements the queue ADT using an array
 * 
 * @author Hawk Weisman
 * @pledge
 * 
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class ArrayQueue<E> implements Queue<E> {

	private E[] queue;
	private static final int DEFAULT_CAPACITY = 200; // the default capacity of
														// the array
	private int capacity; // the actual capacity of the array
	private int f; // the index of the front element
	private int r; // the index of the rear element

	/**
	 * 0-parameter constructor creates a queue with the default capacity
	 */
	public ArrayQueue() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 1-parameter constructor creates a queue with the given capacity
	 * 
	 * @param capacity
	 *            the capacity of the queue to be created
	 */
	public ArrayQueue(int capacity) {
		this.capacity = capacity;
		queue = (E[]) new Object[this.capacity];
	}

	/**
	 * returns the size of the queue
	 * 
	 * @return the size of the queue
	 */
	@Override
	public int size() {
		return (capacity - f + r) % capacity;
	}

	/**
	 * returns true if the queue is empty, false otherwise
	 * 
	 * @return true if the queue is empty, false otherwise
	 */
	@Override
	public boolean empty() {
		return (f == r);
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
			throw new EmptyQueueException("Can't return front, queue is empty.");
		return queue[f];
	}

	/**
	 * adds the given element to the rear of the queue
	 * 
	 * @param element
	 *            the element to be added to the queue
	 * @throws FullQueueException
	 *             if the queue is full
	 */
	@Override
	public void enqueue(E element) throws FullQueueException {
		if (size() == capacity - 1)
			throw new FullQueueException("Cannot enqueue, queue is full.");
		queue[r] = element;
		r = (r + 1) % capacity;
	}

	/**
	 * dequeues (removes & returns) the front element of the queue
	 * 
	 * @return the front element of the queue
	 * @throws EmptyQueueException
	 *             if the queue is empty
	 */
	@Override
	public E dequeue() throws EmptyQueueException {
		if (empty())
			throw new EmptyQueueException("Cannot dequeue, nothing to dequeue.");
		E temp = queue[f];
		queue[f] = null;
		f = (f + 1) % capacity;
		return temp;
	}
}
