package com.hawkw.Queue;
@SuppressWarnings("serial")
public class EmptyQueueException extends Exception {

	public EmptyQueueException() {
	}

	public EmptyQueueException(String message) {
		super(message);
	}

}
