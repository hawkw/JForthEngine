package com.hawkw.ForthEngine;

public class Word {

	private int myOpcode = 0x00;
	private int myAddress;
	private String myString = null;
	
	protected Word (int opcode) {
		myOpcode = opcode;
	}
	
	protected Word (int opcode, int address) {
		myOpcode = opcode;
		myAddress = address;
	}
	
	protected Word (String string) {
		myString = string;
		myOpcode = 0x14;
	}

	/**
	 * Builds a word for a no-argument command from a one-byte opcode
	 * 
	 * @param i
	 *            the one-byte opcode
	 * @return a word for that one-byte opcode
	 */
	public static Word wordBuilder(int i) {
		return new Word(i);
	}
	
	/**
	 * Builds a word for a one-argument command with a one-byte opcode and a
	 * two-byte RAM address
	 * 
	 * @param currentInstruction
	 *            the one-byte opcode
	 * @param currentAddress
	 *            the two-byte RAM address
	 * @return a word containing the one-byte opcode and the two-byte RAM
	 *         address
	 */
	public static Word wordBuilder (int currentInstruction, int currentAddress) {
		return new Word(currentInstruction, currentAddress);
	}
	
	public static Word wordBuilder(String s) {
		return new Word(s);
	}


	public String getHexValue() {
		String hexValue = "0x";
		if (Integer.toHexString(myOpcode).length() < 2) {
			hexValue = hexValue + "0" + Integer.toHexString(myOpcode).toUpperCase();
		} else {
			hexValue = hexValue + Integer.toHexString(myOpcode).toUpperCase();
		}
		if (myAddress > 0) {
			if (Integer.toHexString(myAddress).length() < 4) {
				hexValue = hexValue + "0" + Integer.toHexString(myAddress).toUpperCase();
			} else {
				hexValue = hexValue + Integer.toHexString(myAddress).toUpperCase();
			}
		}
		int length = hexValue.length();
		for (int i = 0; i < (10 - length); i++) {
			hexValue = hexValue + "0";
		}
		return hexValue;
	}

	public String getString() {
		return this.myString;
	}

	public String toString() {
		if (myString != null) {
			return myString;
		} else {
			return this.getHexValue();
		}
	}

	public String getAddress() {
		String hexValue = "0x";
		if (Integer.toHexString(myAddress).length() < 4) {
			hexValue = hexValue + "0" + Integer.toHexString(myAddress).toUpperCase();
		} else {
			hexValue = hexValue + Integer.toHexString(myAddress).toUpperCase();
		}
		return hexValue;
	}
}
