package acciones;

import Utils.LexicalException;
import al_Main.AnalizadorLexico;

public class Action9 extends Action {

	public Action9(AnalizadorLexico al) {
		super(al);
	}

	@Override
	public void ejecutar() throws LexicalException {
		_analizador.rollback(1);
		throw new LexicalException("Error: falta el caracter '='");
	}

}
