package com.hawkw.ForthEngine;

import java.io.*;
import java.util.Scanner;

import com.hawkw.Queue.EmptyQueueException;
import com.hawkw.Queue.NodeQueue;

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
	public static int getOpcode(String command) {
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
			int opcode() {
				return 0x00;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		STORE {

			@Override
			int opcode() {
				return 0x01;
			}

			@Override
			int requiredArgs() {

				return 1;
			}
		},
		ADD {

			@Override
			int opcode() {
				return 0x02;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		SUB {

			@Override
			int opcode() {
				return 0x03;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		RSTORE {

			@Override
			int opcode() {
				return 0x04;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		FETCH {

			@Override
			int opcode() {
				return 0x05;
			}

			@Override
			int requiredArgs() {

				return 1;
			}
		},
		AND {

			@Override
			int opcode() {
				return 0x06;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		DROP {

			@Override
			int opcode() {
				return 0x07;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		DUP {

			@Override
			int opcode() {
				return 0x08;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		OR {

			@Override
			int opcode() {
				return 0x09;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		OVER {

			@Override
			int opcode() {
				return 0x0A;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		RFETCH {

			@Override
			int opcode() {
				return 0x0B;
			}

			@Override
			int requiredArgs() {

				return 0;
			}

		},
		SWAP {

			@Override
			int opcode() {
				return 0x0C;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		XOR {

			@Override
			int opcode() {
				return 0x0D;
			}

			@Override
			int requiredArgs() {

				return 0;
			}
		},
		IF {

			@Override
			int opcode() {
				return 0x0E;
			}

			@Override
			int requiredArgs() {
				return 1;
			}
		},
		CALL {

			@Override
			int opcode() {
				return 0x0F;
			}

			@Override
			int requiredArgs() {
				return 1;
			}
		},
		EXIT {

			@Override
			int opcode() {
				return 0x10;
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		LIT {

			@Override
			int opcode() {
				return 0x11;
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		COUT {

			@Override
			int opcode() {
				return 0x12;
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		CIN {

			@Override
			int opcode() {
				return 0x13;
			}

			@Override
			int requiredArgs() {
				return 0;
			}
		},
		S {

			@Override
			int opcode() {
				return 0x14;
			}

			@Override
			int requiredArgs() {
				// TODO Auto-generated method stub
				return 2;
			}

		};
		abstract int opcode();

		abstract int requiredArgs();
	}

	public static void main(String[] argv) {

		ForthEngine engine = null;
		File target = null;

		NodeQueue<String> filterQueue = new NodeQueue<String>();
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
					} else if (s.equals("--debug") || s.equals("-d")) {
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
				String token;

				// enqueue instructions from the file stream into the
				// filterStack
				// in order to filter out the comments
				if (debugMode && !verboseMode)
					System.out
							.print("Debug: reading instructions onto filterQueue...");

				while (stream.hasNext()) {
					token = stream.next();
					if (verboseMode)
						System.out.println("-> enqueueing \"" + token
								+ "\" to filterQueue");
					filterQueue.enqueue(token);

				}

				if (debugMode && !verboseMode)
					System.out.print("done!\n");

				StringBuilder s;
				String tempString;
				int currentInstruction = 0x00;
				int currentAddress = 0x0000;

				if (debugMode && !verboseMode)
					System.out
							.print("Debug: enqueueing instructions to compileStack...");

				while (!filterQueue.empty()) {
					// if we find a comment-closing character...
					if (filterQueue.front().contains("(")) {
						// ...pop until we find a comment character
						while (!filterQueue.empty()
								&& !filterQueue.front().contains(")")) {
							filterQueue.dequeue();
						}
						if (!filterQueue.empty())
							filterQueue.dequeue();
						// if we don't find a comment opening character...
					} else if (filterQueue.front().contains("S\"")) { // ...but
																		// we
																		// do
																		// find
																		// a
																		// string...
						s = new StringBuilder();
						filterQueue.dequeue();
						do {
							s.append(filterQueue.dequeue() + " ");
						} while (!filterQueue.empty()
								&& !filterQueue.front().contains("\""));
						filterQueue.dequeue();
						currentAddress = Integer.parseInt(
								filterQueue.dequeue(), 16);
						if (verboseMode)
							System.out.println("-> fillRAM "
									+ Word.wordBuilder(0x14, currentAddress));
						engine.fillRAM(Word.wordBuilder(0x14, currentAddress));
						engine.writeToRAM(Word.wordBuilder(s.toString()), engine.parseHex(Integer.toHexString(currentAddress)));

					} else {
						// ...everything is fine and good!
						if (getReqArgs(filterQueue.front()) == 1) {
							currentInstruction = getOpcode(filterQueue
									.dequeue());
							currentAddress = Integer.parseInt(
									filterQueue.dequeue(), 16);
							if (verboseMode)
								System.out.println("-> fillRAM "
										+ Word.wordBuilder(currentInstruction,
												currentAddress));
							engine.fillRAM(Word.wordBuilder(currentInstruction,
									currentAddress));
						} else if (getReqArgs(filterQueue.front()) == 0) {
							currentInstruction = getOpcode(filterQueue
									.dequeue());
							if (verboseMode) {
								System.out.println("-> fillRAM "
										+ Word.wordBuilder(currentInstruction));
							}
							engine.fillRAM(Word.wordBuilder(currentInstruction));
						}
					}
				}
				if (debugMode && !verboseMode)
					System.out.print("done!\n");

				filterQueue = null;
				if (debugMode && !verboseMode) {
					System.out
							.print("Debug: Loading instructions into memory...");
				} else if (verboseMode) {
					System.out
							.println("Debug: Loading instructions into memory...");
				}

				if (debugMode && !verboseMode) {
					System.out.print("done!\n");
				} else if (verboseMode) {
					System.out
							.println("Debug: Loading instructions into memory: done!");
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
			} catch (EmptyQueueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
