package acciones;

import Utils.LexicalException;
import al_Main.AnalizadorLexico;

public class Action7 extends Action {

	public Action7(AnalizadorLexico al) {
		super(al);
	}

	@Override
	public void ejecutar() throws LexicalException {
		_analizador.initToken();
		throw new LexicalException("caracter invalido o desonocido");
	}

}
