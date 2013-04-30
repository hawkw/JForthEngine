

import static org.junit.Assert.*;

import org.junit.Test;

import com.hawkw.ForthEngine.ForthParser;
import com.hawkw.ForthEngine.Word;

public class ForthParserTest {

	@Test
	public void opcodeDispatchTest() {
		org.junit.Assert.assertTrue(ForthParser.getOpcode("NOP") == 0x00);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("STORE") == 0x01);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("ADD") == 0x02);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("SUB") == 0x03);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("RSTORE") == 0x04);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("FETCH") == 0x05);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("AND") == 0x06);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("DROP") == 0x07);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("DUP") == 0x08);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("OR") == 0x09);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("OVER") == 0x0A);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("RFETCH") == 0x0B);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("SWAP") == 0x0C);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("XOR") == 0x0D);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("IF") == 0x0E);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("CALL") == 0x0F);
		org.junit.Assert.assertTrue(ForthParser.getOpcode("EXIT") == 0x10);
	}
	
	@Test
	public void wordBuilderTest(){
		org.junit.Assert.assertTrue(Word.wordBuilder(0x0F).getHexValue(), Word.wordBuilder(0x0F).getHexValue().contains("0F"));
		org.junit.Assert.assertTrue(Word.wordBuilder(0x0F, 0x0562).getHexValue(), Word.wordBuilder(0x0F, 0x0562).getHexValue().equals("0x0F0562"));
		org.junit.Assert.assertTrue(Word.wordBuilder(0x0F, 0x0562).getHexValue(), Word.wordBuilder(0x12, 0xF562).getHexValue().equals("0x12F562"));
	}

}
