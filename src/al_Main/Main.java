package al_Main;

import gc_Assembler.GeneradorAssembler;

import java.io.File;
import java.util.Vector;


import acciones.*;
import as_Parser.Parser;

import Utils.Token;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Parser p;

		if(args.length > 0)
			p = new Parser(args[0]);
		else
			p = new Parser(); 
		p.run();
		GeneradorAssembler ga = new GeneradorAssembler();
		System.out.println(ga.generateCode(p.pila.toArray()));
		p.imprimirResultados();

	}

}
