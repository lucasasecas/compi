package acciones;

import al_Main.AnalizadorLexico;
import Utils.LexicalException;

public class Action11 extends Action {

	public Action11(AnalizadorLexico al) {
		super(al);
	}

	@Override
	public void ejecutar() throws LexicalException {
		_analizador.errorCaracterInvalido(false);
	}

}
