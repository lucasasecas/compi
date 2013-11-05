package al_Main;

import gc_Assembler.GeneradorAssembler;

import java.io.File;
import java.util.Vector;


import acciones.*;
import as_Parser.Parser;

import Utils.TablaSimbolo;
import Utils.Token;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
		Parser p;
		TablaSimbolo tds = new TablaSimbolo();
		if(args.length > 0)
			p = new Parser(args[0], tds);
		else
			p = new Parser(tds); 
		
		p.run();
		GeneradorAssembler ga = new GeneradorAssembler(tds, args[1], p.pila.toArray());
		p.imprimirResultados();

	}

}
