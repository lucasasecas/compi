package al_Main;

import gc_Assembler.GeneradorAssembler;

import as_Parser.Parser;

import Utils.TablaSimbolo;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
		Parser p;
		TablaSimbolo tds = new TablaSimbolo();
//		p = new Parser(args[0], tds);
		p = new Parser("sources/source.txt", tds);
			
		p.run();
		GeneradorAssembler ga ;
		if(!p.hayError()){
			ga = new GeneradorAssembler(tds, "sources/aux.txt", p.pila.toArray());
			if(!ga.hayErrores())
				System.out.println("Codigo assembler creado correctamente");
		}
		
		p.imprimirResultados();

	}

}
