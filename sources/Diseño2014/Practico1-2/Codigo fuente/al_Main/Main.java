package al_Main;

import java.io.File;

import gc_Assembler.GeneradorAssembler;
import as_Parser.Parser;
import as_Parser.ParserVal;
import Utils.TablaSimbolo;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
		Parser p;
		TablaSimbolo tds = new TablaSimbolo();
//		p = new Parser(args[0], tds);
		AnalizadorLexico al = new AnalizadorLexico(tds);
//		while (!al.eof()){
//			ParserVal yylval = new ParserVal();
//			 int t = al.getNextToken(yylval);
//			 System.out.println(yylval.sval + " -- " + yylval.kind);
//		}
//		tds.imprimirTablaSimbol();
		
		p = new Parser(tds, al);
//			
		p.run("sources/source.txt");
		
		p.getTablaDeSimbolos().imprimirTablaSimbol();
//		GeneradorAssembler ga ;
//		if(!p.hayError()){
//			ga = new GeneradorAssembler(tds, "sources/aux.txt", p.pila.toArray());
//			if(!ga.hayErrores())
//				System.out.println("Codigo assembler creado correctamente");
//		}
//		
//		p.imprimirResultados();

	}

}
