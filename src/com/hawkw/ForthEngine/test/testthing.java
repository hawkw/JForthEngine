package com.hawkw.ForthEngine.test;

import com.hawkw.ForthEngine.Word;

public class testthing {
	public static void main(String[] argz) {
		System.out.println(Word.wordBuilder(0x0F, 0x0562).getHexValue());
		System.out.println(Integer.toHexString(0x0F));
		System.out.println(Integer.toHexString(0x0562));
	}
}
