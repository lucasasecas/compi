import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import Utils.TablaSimbolo;
import al_Main.AnalizadorLexico;
import as_Parser.ParserVal;


public class ALTest {

	@Test
	public void testIDLess12Characters() throws IOException {
		File file = File.createTempFile("temp", ".txt", new File("C:/"));
		file.
		AnalizadorLexico al = new AnalizadorLexico(file, new TablaSimbolo());
		ParserVal val = new ParserVal(); 
		
		int i = al.getNextToken(val);
		
		assertEquals(val.sval.length(), 12);
	}

}
