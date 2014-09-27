package acciones;

import Utils.LexicalException;
import al_Main.AnalizadorLexico;

public class Action5 extends Action {

	String mensaje;
	public Action5(AnalizadorLexico al, String msg) {
		super(al);
		mensaje = msg;
	}

	@Override
	public void ejecutar() throws LexicalException {
		_analizador.rollback(1);
		throw new LexicalException(mensaje);
	}

}
