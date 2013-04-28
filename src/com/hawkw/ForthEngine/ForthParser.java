package com.hawkw.ForthEngine;

import java.io.*;
import java.util.Scanner;

import com.hawkw.Stack.NodeStack;

public class ForthParser {

	public static boolean debugMode = false;
	public static boolean verboseMode = false;
	public static Scanner stream;

	/**
	 * Gets the opcode for a FORTH command from the OpcodeLibrary
	 * 
	 * @param command
	 *            a String representing a FORTH command
	 * @return the opcode for that command
	 */
	public static String getOpcode(String command) {
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
			String opcode() {
				return "0x00";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		STORE {

			@Override
			String opcode() {
				return "0x01";
			}

			@Override
			int requiredArgs() {

				return 1;
			}
		},
		ADD {

			@Override
			String opcode() {
				return "0x02";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		SUB {

			@Override
			String opcode() {
				return "0x03";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		RSTORE {

			@Override
			String opcode() {
				return "0x04";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		FETCH {

			@Override
			String opcode() {
				return "0x05";
			}

			@Override
			int requiredArgs() {

				return 1;
			}
		},
		AND {

			@Override
			String opcode() {
				return "0x06";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		DROP {

			@Override
			String opcode() {
				return "0x07";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		DUP {

			@Override
			String opcode() {
				return "0x08";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		OR {

			@Override
			String opcode() {
				return "0x09";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		OVER {

			@Override
			String opcode() {
				return "0x0A";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		RFETCH {

			@Override
			String opcode() {
				return "0x0B";
			}

			@Override
			int requiredArgs() {

				return 0;
			}

		},
		SWAP {

			@Override
			String opcode() {
				return "0x0C";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		XOR {

			@Override
			String opcode() {
				return "0x0D";
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		IF {

			@Override
			String opcode() {
				return "0x0E";
			}

			@Override
			int requiredArgs() {
				return 1;
			}
		},
		CALL {

			@Override
			String opcode() {
				return "0x0F";
			}

			@Override
			int requiredArgs() {
				return 1;
			}
		},
		EXIT {

			@Override
			String opcode() {
				return "0x10";
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		LIT {

			@Override
			String opcode() {
				return "0x11";
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		COUT {

			@Override
			String opcode() {
				return "0x12";
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		CIN {

			@Override
			String opcode() {
				return "0x13";
			}

			@Override
			int requiredArgs() {
				return 0;
			}

		};
		abstract String opcode();

		abstract int requiredArgs();
	}

	/**
	 * Builds a word for a no-argument command from a one-byte opcode
	 * 
	 * @param currentInstruction
	 *            the one-byte opcode
	 * @return a word for that one-byte opcode
	 */
	public static String wordBuilder(String currentInstruction) {
		return currentInstruction + "000000";
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
	public static String wordBuilder(String currentInstruction,
			String currentAddress) {
		return currentInstruction + currentAddress + "00";
	}

	public static void main(String[] argv) {

		ForthEngine engine = null;
		File target = null;

		NodeStack<String> filterStack = new NodeStack<String>();
		NodeStack<String> instructionCompileStack = new NodeStack<String>();

		boolean engineRunning = false;

		// really gross and ugly way of handling input
		if (argv.length == 0) {
			System.out
					.println("Please enter the name of the file to be interpreted.");

		} else {
			try {
				for (String s : argv) {
					if (s.contains("--mem")) {
						engine = new ForthEngine(Integer.parseInt(s.substring(
								4, 6)));
						engineRunning = true;
					} else if (s.equals("--debug") || s.equals("-d"))  {
						debugMode = true;
						System.out.println("Debug mode set.");
					} else if (s.equals("--verbose") || s.equals("-v")) {
						verboseMode = true;
						System.out.println("Verbose mode set.");
						System.out
								.println("Warning: Verbose mode is really verbose.");
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
					if (verboseMode)
						System.out.println("-> pushed \""
								+ filterStack.peek() + "\" to filterStack");
				}

				while (!filterStack.empty()) {
					// if we find a comment-closing character...
					if (filterStack.peek().contains(")")) {
						// ...pop until we find a comment character
						while (!filterStack.empty()
								&& !filterStack.peek().equals("(")) {
							filterStack.pop();
						}
						if (!filterStack.empty())
							filterStack.pop();
						// if we don't find a comment opening character...
					} else {
						// ...everything is fine and good!
						instructionCompileStack.push(filterStack.pop());
						if (verboseMode)
							System.out.println("-> pushed \""
									+ instructionCompileStack.peek()
									+ "\" to instructionCompileStack");
					}
				}

				String currentInstruction = "0x00";
				String currentAddress = "0x0000";
				
				
				if (debugMode && !verboseMode) {
					System.out.print("Debug: Loading instructions into memory...");
				} else if (verboseMode) {
					System.out.println("Debug: Loading instructions into memory...");
				}
			
				while (!instructionCompileStack.empty()) {
					if (getReqArgs(instructionCompileStack.peek()) == 1) {
						currentInstruction = getOpcode(instructionCompileStack.pop());
						currentAddress = instructionCompileStack.pop();
						if (verboseMode)
							System.out.println("-> fillRAM "
									+ wordBuilder(currentInstruction, currentAddress));
						engine.fillRAM(wordBuilder(currentInstruction, currentAddress));
					} else if (getReqArgs(instructionCompileStack.peek()) == 0) {
						currentInstruction = getOpcode(instructionCompileStack
								.pop());
						if (verboseMode)
							System.out.println("-> fillRAM "
									+ wordBuilder(currentInstruction));
						engine.fillRAM(wordBuilder(currentInstruction));
					}
				}
				
				if (debugMode && !verboseMode) {
					System.out.print("done!\n");
				} else if (verboseMode) {
					System.out.println("Debug: Loading instructions into memory: done!");
				}
				
				// actually do the thing
				if (debugMode)
					System.out.println("Debug: running!");
				engine.run();
			}

			catch (NumberFormatException e) {
				System.err.println("Caught a NumberFormatException.");
				if (debugMode)
					e.printStackTrace(System.err);
			} catch (TooMuchRAMRequestedError e) {
				System.err
						.println("The ForthEngine RAM addressing system supports up to 16 'installed' memory modules. Please request fewer modules.");
				if (debugMode)
					e.printStackTrace(System.err);
			} catch (FileNotFoundException e) {
				System.err
						.println("File not found. Please enter the name of the file to be interpreted.");
				if (debugMode)
					e.printStackTrace(System.err);
			}
		}
	}
}
