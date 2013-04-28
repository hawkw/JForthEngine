package com.hawkw.ForthEngine;

import java.util.Scanner;
import com.hawkw.Stack.NodeStack;

public class ForthEngine {
	// Our Forth Engine's "memory" consists of an array of
	// MEMORY_MODULE_SIZE-index byte arrays. The default size of a memory module
	// is 1 kB. A location in memory is addressed as ram[x][y]
	//
	// for example,
	// 0x0000 - 0x0400 : module 0
	// 0x1000 - 0x1400 : module 1
	// 0x2000 - 0x2400 : module 2
	// ...ad nauseum

	protected static final int DEFAULT_MEMORY_MODULES = 2;
	protected static final int MEMORY_MODULE_SIZE = 1024;
	protected static int moduleCount;
	private String[][] ram;

	// the Forth Engine consists of three primary variables:
	// the DataStack, to which numbers are pushed
	// the ReturnStack, used for subroutines
	// and the ProgramCounter, which stores the address of the current location
	// in memory
	protected NodeStack<String> DataStack = new NodeStack<String>();
	protected NodeStack<String> ReturnStack = new NodeStack<String>();
	protected ProgramCounter counter = new ProgramCounter(0, 0);
	protected static Scanner scan = new Scanner(System.in);
	protected boolean done = false;

	/**
	 * 0-argument constructor: creates a ForthEngine with the default amount of
	 * 'installed' RAM modules
	 * 
	 * @throws TooMuchRAMRequestedError
	 */
	public ForthEngine() throws TooMuchRAMRequestedError {
		this(DEFAULT_MEMORY_MODULES);
	}

	/**
	 * 1-argument constructor: creates a ForthEngine with a user-requested
	 * number of 'installed' RAM modules
	 * 
	 * @param requestedModules
	 *            the number of RAM modules 'installed' in this ForthEngine
	 * @throws TooMuchRAMRequestedError
	 *             if the number of requested modules is greater than 16 (as the
	 *             RAM-addressing system supports up to 16 RAM modules)
	 */
	public ForthEngine(int requestedModules) throws TooMuchRAMRequestedError {
		if (requestedModules > 16)
			throw new TooMuchRAMRequestedError();
		moduleCount = requestedModules;
		ram = new String[moduleCount][MEMORY_MODULE_SIZE];
	}

	/**
	 * Runs the program currently stored in memory by resetting the counter to
	 * (0,0) and evaling and incrementing for each location in RAM
	 */
	public void run() {
		done = false;
		counter.reset(); // reset the ProgramCounter to (0,0)
		while (!done) {
			if (ForthParser.verboseMode)
				System.out.println("-> evaling "
						+ ram[counter.getModule()][counter.getIndex()]);
			eval(); // eval the current instruction
			counter.increment(); // increment the ProgramCounter to the next
									// position
			if (ForthParser.verboseMode)
				System.out.println("-> ProgramCounter incremented to "
						+ "(" + counter.getModule() + "," + counter.getIndex()
						+ ")");
		}
	}

	/**
	 * Evaluates the command at the program counter.
	 */
	public void eval() {

		String currentInstruction = ram[counter.getModule()][counter.getIndex()];
		String tempA;
		String tempB;
		int tempInt;
		String address;

		switch (currentInstruction.substring(0, 4)) {
		case "0x01": // STORE
			address = currentInstruction.substring(4, 8);
			writeToRAM(DataStack.pop(), parseHex(address));
			break;
		case "0x02": // ADD
			tempInt = Integer.parseInt(DataStack.pop()) + Integer.parseInt(DataStack.pop());
			DataStack.push(Integer.toString(tempInt));
			break;
		case "0x03": // SUBTRACT
			tempInt = Integer.parseInt(DataStack.pop()) - Integer.parseInt(DataStack.pop());
			DataStack.push(Integer.toString(tempInt));
			break;
		case "0x04": // RSTORE
			ReturnStack.push(DataStack.pop());
			break;
		case "0x05": // FETCH
			address = currentInstruction.substring(4, 8);
			DataStack.push(readFromRAM(parseHex(address)));
			break;
		case "0x06": // AND
			DataStack
					.push(Integer.toHexString((Integer.parseInt(
							DataStack.pop(), 16) & Integer.parseInt(
							DataStack.pop(), 16))));
			break;
		case "0x07": // DROP
			DataStack.pop();
			break;
		case "0x08": // DUP
			if (ForthParser.verboseMode)
				System.out.println("-> DUP " + DataStack.peek());
			DataStack.push(DataStack.peek());
			break;
		case "0x09": // OR
			DataStack
					.push(Integer.toHexString((Integer.parseInt(
							DataStack.pop(), 16) | Integer.parseInt(
							DataStack.pop(), 16))));
			break;
		case "0x0A": // OVER
			// FIXME: implement this
			break;
		case "0x0B": // RFETCH
			DataStack.push(ReturnStack.pop());
			break;
		case "0x0C": // SWAP
			tempA = DataStack.pop();
			tempB = DataStack.pop();
			DataStack.push(tempB);
			DataStack.push(tempA);
			break;
		case "0x0D": // XOR
			DataStack
					.push(Integer.toHexString((Integer.parseInt(
							DataStack.pop(), 16) ^ Integer.parseInt(
							DataStack.pop(), 16))));
			break;
		case "0x0E": // IF
			if (Integer.parseInt(DataStack.pop()) == 1) { // if N1 == true;
				address = currentInstruction.substring(3, 7); // take the
																// address from
																// the word
				if (ForthParser.verboseMode)
					System.out.println("-> IF branching to " + address);
				counter.branch(parseHex(address)); // branch to the address
			}
			if (ForthParser.verboseMode)
				System.out.println("-> IF false, not branching");
			break;
		case "0x0F": // CALL
			address = currentInstruction.substring(3, 7); // take the address
															// from the word
			if (ForthParser.verboseMode)
				System.out.println("-> CALL " + address);
			counter.branch(parseHex(address)); // branch to the address
			break;
		case "0x10": // EXIT
			if (ForthParser.verboseMode)
				System.out.println("-> EXIT");
			done = true;
			break;
		case "0x11": // LIT
			if (ForthParser.verboseMode)
				System.out.println("-> LIT" + counter.getNext());
			DataStack.push(counter.getNext());
			break;
		case "0x12": // COUT
			if (ForthParser.verboseMode)
				System.out.println("-> COUT" + DataStack.peek());
			System.out.println(" > " + DataStack.pop());
			break;
		case "0x13": // CIN
			tempA = getCIN();
			if (ForthParser.verboseMode)
				System.out.println("-> CIN " + tempA);
			DataStack.push(tempA);
			break;
		}
			

		if (ForthParser.debugMode) {
			if (!DataStack.empty())
				System.out
						.println("Debug: DataStack = " + DataStack.toString());
			if (!ReturnStack.empty())
				System.out.println("Debug: ReturnStack = "
						+ ReturnStack.toString());
		}
	}

	public void fillRAM(String s) {
		ram[counter.getModule()][counter.getIndex()] = s;
		counter.increment();
	}

	public void push(String instruction) {
		this.DataStack.push(instruction);
	}

	private String readFromRAM(RAMLocation r) {
		return ram[r.getModule()][r.getIndex()];
	}

	private void writeToRAM(String s, RAMLocation r) {
		ram[r.getModule()][r.getIndex()] = s;
	}

	/**
	 * A RAMLocation is essentially an ordered pair that represents a location
	 * within the memory matrix
	 * 
	 * A RAM location should parse to a 16-bit hex value, as in the following
	 * example: (note that the first hex digit corresponds to the RAM module,
	 * and the next three digits correspond to the location within the module)
	 * 0x0000 - 0x0400 : module 0 0x1000 - 0x1400 : module 1 0x2000 - 0x2400 :
	 * module 2 ...ad nauseum
	 * 
	 * @author hawk
	 * 
	 */
	class RAMLocation {
		protected int module, index;

		public RAMLocation(int m, int i) {
			module = m;
			index = i;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getModule() {
			return module;
		}

		public void setModule(int module) {
			this.module = module;
		}
	}

	/**
	 * Parses a two-byte hexadecimal representing the position of a location in
	 * the RAM matrix into a RAMLocation object that represents it as an ordered
	 * pair.
	 * 
	 * @param input
	 *            a two-byte hex representing the location in the RAM matrix
	 * @return a RAMLocation representing the location in the RAM matrix
	 */
	public RAMLocation parseHex(String address) {
		int module = Integer.parseInt(address.substring(0, 2));
		int index = Integer.parseInt(address.substring(3, 4));
		return new RAMLocation(module, index);
	}

	/**
	 * The ProgramCounter is a special instance of RAMLocation that represents
	 * the current location in memory. The programCounter has access to the
	 * increment method, which moves the program counter to the next location in
	 * the RAM matrix.
	 * 
	 * @see RAMLocation
	 * 
	 */
	class ProgramCounter extends RAMLocation {

		public ProgramCounter(int m, int i) {
			super(m, i);
		}

		/**
		 * Increments the PC to the next loc in the memory matrix.
		 */
		public void increment() {
			if (this.index == 1023 && this.module == moduleCount) {
				// if the index of the program counter is at the 1023rd (final)
				// location in the last memory module in the matrix, then it is
				// time to 'wrap around' and go back to index [0][0]
				this.reset();
			} else if (this.index >= MEMORY_MODULE_SIZE) {
				// if the program counter currently points to the last index in
				// the memory module, then it is time to go to the 0th location
				// in the next module
				this.module += 1;
				this.index = 0;
			} else if (this.index < MEMORY_MODULE_SIZE) {
				// if the index that the program counter currently points to is
				// less than the size of the memory module, then we just
				// increment to the next index in the module
				this.index += 1;
			}
		}

		/**
		 * returns the value of the cell after the ProgramCounter
		 * 
		 * @return the value of the next cell in RAM
		 */
		public String getNext() {
			if (this.index == 1023 && this.module == moduleCount) {
				return ram[0][0];
			} else if (this.index >= MEMORY_MODULE_SIZE) {
				return ram[module + 1][0];
			} else {
				return ram[module][index + 1];
			}
		}

		/**
		 * resets the program counter to (0,0)
		 */
		public void reset() {
			this.index = 0;
			this.module = 0;
		}

		/**
		 * sets the program counter equal to the RAMLocation given as a
		 * parameter
		 * 
		 * @param r
		 *            the RAMLocation to branch to
		 */
		public void branch(RAMLocation r) {
			this.module = r.getModule();
			this.index = r.getIndex();
		}
	}

	public static String getCIN() {
		System.out.print(" > ");
		return scan.next();
	}
}
