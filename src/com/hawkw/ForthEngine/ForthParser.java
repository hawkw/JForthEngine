package com.hawkw.ForthEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import com.hawkw.Stack.NodeStack;

public class ForthParser {

	/**
	 * Gets the opcode for a FORTH command from the OpcodeLibrary
	 * 
	 * @param command
	 *            a String representing a FORTH command
	 * @return the opcode for that command
	 */
	public static byte getOpcode(String command) {
		final OpcodeLibrary d = OpcodeLibrary.valueOf(command);
		return d.opcode();
	}

	/**
	 * Gets the required number of arguments for a FORTH command from the
	 * OpcodeLibrary
	 * 
	 * @param command
	 *            a String representing a FORTH command
	 * @return the number of required arguments for that command
	 */
	public static int getReqArgs(String command) {
		final OpcodeLibrary d = OpcodeLibrary.valueOf(command);
		return d.requiredArgs();
	}

	/**
	 * Stores the opcodes and required arguments for the FORTH instruction set
	 * 
	 * @author hawk
	 * 
	 */
	private enum OpcodeLibrary {
		NOP {

			@Override
			byte opcode() {
				return 0x00;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		STORE {

			@Override
			byte opcode() {
				return 0x01;
			}

			@Override
			int requiredArgs() {
				
				return 1;
			}
		},
		ADD {

			@Override
			byte opcode() {
				return 0x02;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		SUB {

			@Override
			byte opcode() {
				return 0x03;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		RSTORE {

			@Override
			byte opcode() {
				return 0x04;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		FETCH {

			@Override
			byte opcode() {
				return 0x05;
			}

			@Override
			int requiredArgs() {
				
				return 1;
			}
		},
		AND {

			@Override
			byte opcode() {
				return 0x06;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		DROP {

			@Override
			byte opcode() {
				return 0x07;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		DUP {

			@Override
			byte opcode() {
				return 0x08;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		OR {

			@Override
			byte opcode() {
				return 0x09;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		OVER {

			@Override
			byte opcode() {
				return 0x0A;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		RFETCH {

			@Override
			byte opcode() {
				return 0x0B;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}

		},
		SWAP {

			@Override
			byte opcode() {
				return 0x0C;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		XOR {

			@Override
			byte opcode() {
				return 0x0D;
			}

			@Override
			int requiredArgs() {
				
				return 0;
			}
		},
		IF {

			@Override
			byte opcode() {
				return 0x0E;
			}

			@Override
			int requiredArgs() {
				return 1;
			}
		},
		CALL {

			@Override
			byte opcode() {
				return 0x0F;
			}

			@Override
			int requiredArgs() {
				return 1;
			}
		},
		EXIT {

			@Override
			byte opcode() {
				return 0x10;
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		LIT {

			@Override
			byte opcode() {
				return 0x11;
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		CHAR {

			@Override
			byte opcode() {
				return 0x12;
			}

			@Override
			int requiredArgs() {
				return 1;
			}
		},
		COUT {

			@Override
			byte opcode() {
				return 0x13;
			}

			@Override
			int requiredArgs() {
				return 0;
			}

		};
		abstract byte opcode();

		abstract int requiredArgs();
	}

	/**
	 * Builds a word for a no-argument command from a one-byte opcode
	 * 
	 * @param opcode
	 *            the one-byte opcode
	 * @return a word for that one-byte opcode
	 */
	public static int wordBuilder(byte opcode) {
		return Integer.parseInt(opcode + "000000");
	}

	/**
	 * Builds a word for a one-argument command with a one-byte opcode and a
	 * two-byte RAM address
	 * 
	 * @param opcode
	 *            the one-byte opcode
	 * @param address
	 *            the two-byte RAM address
	 * @return a word containing the one-byte opcode and the two-byte RAM
	 *         address
	 */
	public static int wordBuilder(byte opcode, short address) {
		return Integer.parseInt(opcode + address + "00");
	}

	public static void main(String[] argv) {

		ForthEngine engine = null;
		File target = null;
		Scanner stream;

		NodeStack<String> filterStack = new NodeStack<String>();
		NodeStack<String> instructionCompileStack = new NodeStack<String>();

		boolean engineRunning = false;
		boolean debugMode = false;

		// really gross and ugly way of handling input
		if (argv.length == 0) {
			System.out
					.println("Please enter the name of the file to be interpreted.");

		} else {
			try {
				for (String s : argv) {
					if (s.substring(0, 4) == "--mem") {
						engine = new ForthEngine(Integer.parseInt(s.substring(
								4, 6)));
						engineRunning = true;
					} else if (s.substring(0, 6) == "--debug") {
						debugMode = true;
					} else {
						target = new File(s);
					}

					if (!engineRunning)
						engine = new ForthEngine();
				}

				stream = new Scanner(target);

				// Push instructions from the file stream into the filterStack
				// in order to filter out the comments
				while (stream.hasNext()) {

					filterStack.push(stream.next());

					// if we find a comment-opening character...
					if (filterStack.peek() == ")") {
						// ...pop until we find a comment closing character
						while (filterStack.peek() != "(")
							filterStack.pop();
						filterStack.pop();
						// if we don't find a comment opening character...
					} else {
						// ...everything is fine and good!
						instructionCompileStack.push(filterStack.pop());
					}
				}

				byte currentInstruction = 0x00;
				short currentAddress = 0x0000;

				while (!instructionCompileStack.empty()) {
					if (getReqArgs(instructionCompileStack.peek()) == 1) {
						currentInstruction = Byte
								.parseByte(instructionCompileStack.pop());
						currentAddress = Byte.parseByte(instructionCompileStack
								.pop());
						engine.fillRAM(wordBuilder(currentInstruction,
								currentAddress));
					} else if (getReqArgs(instructionCompileStack.peek()) == 0) {
						currentInstruction = Byte
								.parseByte(instructionCompileStack.pop());
						engine.fillRAM(wordBuilder(currentInstruction));
					}
				}

				// actually do the thing
				engine.run();

			}

			catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TooMuchRAMRequestedError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
