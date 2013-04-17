package com.hawkw.ForthEngine;

import com.hawkw.Stack.NodeStack;

public class ForthEngine {
	// Our Forth Engine's "memory" consists of an array of
	// MEMORY_MODULE_SIZE-index byte arrays. The default size of a memory module
	// is 1 kB. A location in memory is addressed as ram[x][y]
	protected static final int DEFAULT_MEMORY_MODULES = 2;
	protected static final int MEMORY_MODULE_SIZE = 1024;
	protected static int moduleCount;
	private int[][] ram;

	private NodeStack<int> DataStack;
	private NodeStack<int> ReturnStack;

	public ForthEngine() {
		this(DEFAULT_MEMORY_MODULES);
	}

	public ForthEngine(int m) {
		moduleCount = m;
		ram = new int[moduleCount][MEMORY_MODULE_SIZE];
	}

	public void eval() {
		// TODO: write eval()
	}

	/**
	 * A RAMLocation is essentially an ordered pair that represents a location
	 * within the memory matrix
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
	 * Parses an integer representing the sequential position of a location in
	 * the RAM matrix into a RAMLocation object that represents it as an ordered
	 * pair.
	 * 
	 * @param input
	 *            an integer representing the sequential location in the RAM
	 *            matrix
	 * @return a RAMLocation representing the (input)th location in the RAM
	 *         matrix
	 */
	private RAMLocation parseRAMLocation(int input) {
		if (input >= MEMORY_MODULE_SIZE) {
			return new RAMLocation(0, input);
		} else {
			return new RAMLocation((input % MEMORY_MODULE_SIZE),
					(input - MEMORY_MODULE_SIZE));
		}
	}

	private int RAMLocationToHex (RAMLocation r) {
		//TODO: write this
		return 0;
		
	}
	/**
	 * The ProgramCounter is a special instance of RAMLocation that represents
	 * the current location in memory. The programCounter has access to the
	 * increment method, which moves the program counter to the next location in
	 * the RAM matrix.
	 * 
	 * @see RAMLocation
	 * @author hawk
	 * 
	 */
	static class ProgramCounter extends RAMLocation {

		public ProgramCounter(int m, int i) {
			super(m, i);
			// TODO Auto-generated constructor stub
		}

		/**
		 * 
		 */
		public void increment() {
			if (this.index == 255 && this.module == moduleCount) {
				// if the index of the program counter is at the 1023rd (final)
				// location in the last memory module in the matrix, then it is
				// time to 'wrap around' and go back to index [0][0]
				this.index = 0;
				this.module = 0;
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
				this.index += 0;
			}
		}
	}
}
