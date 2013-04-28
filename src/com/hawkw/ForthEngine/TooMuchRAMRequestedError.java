package com.hawkw.ForthEngine;

public class TooMuchRAMRequestedError extends Exception {
	public TooMuchRAMRequestedError () {
		super("The ForthEngine RAM addressing system supports up to 16 'installed' memory modules. Please request fewer modules.");
	}
}
