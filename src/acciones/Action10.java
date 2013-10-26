package acciones;

import al_Main.AnalizadorLexico;
import Utils.LexicalException;

public class Action10 extends Action {

	public Action10(AnalizadorLexico al) {
		super(al);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void ejecutar() throws LexicalException {
		_analizador.addTokenToTDS();
	}

}
