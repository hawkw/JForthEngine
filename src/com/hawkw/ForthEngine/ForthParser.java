package com.hawkw.ForthEngine;

public class ForthParser {
	// addr: 0x0000 - 0x0400 : module 0
	//  	 0x1000 - 0x1400 : module 1
	//		 0x2000 - 0x2400 : module 2
	//		 ...ad nauseum

		public byte opcodeToByte (String s) {
			switch (s) {
				case "NOP":
					return 0x00;
				case "!":
					return 0x01;
				case "+":
					return 0x02;
				case "-":
					return 0x03;
				case ">R":
					return 0x04;
				case "@":
					return 0x05;
				case "AND":
					return 0x06;
				case "DROP":
					return 0x07;
				case "DUP":
					return 0x08;
				case "OR":
					return 0x09;
				case "OVER":
					return 0x0A;
				case "R>":
					return 0x0B;
				case "SWAP":
					return 0x0C;
				case "XOR":
					return 0x0D;
				case "IF":
					return 0x0E;
				case "CALL":
					return 0x0F;
				case "EXIT": 
					return 0x10;
				case "LIT":
					return 0x11;
				case "CHAR":
					return 0x12; //TODO: NYI
			}
			return 0x00000000;
		}
		
		public short RAMLocationToAddr (ForthEngine.RAMLocation r) {
			String addr = "" + Integer.toHexString(r.getModule()) + Integer.toHexString(r.getIndex());
			return Short.parseShort(addr);
		}
		
		public int cellBuilder (byte opcode, short addr) {
			String cell = "" + opcode + addr;
			return Integer.parseInt(cell);
		}
}
