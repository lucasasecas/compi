package acciones;

import Utils.LexicalException;
import al_Main.AnalizadorLexico;

public abstract class Action {
	
	AnalizadorLexico _analizador;
	
	public Action(AnalizadorLexico al){
		_analizador = al;
	}
	
	public abstract void ejecutar() throws LexicalException;

}
