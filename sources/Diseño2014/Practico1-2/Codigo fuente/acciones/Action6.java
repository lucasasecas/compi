package acciones;

import Utils.LexicalException;
import al_Main.AnalizadorLexico;

public class Action6 extends Action{

	public Action6(AnalizadorLexico al) {
		super(al);
	}

	@Override
	public void ejecutar() throws LexicalException{
		_analizador.rollback(1);
		throw new LexicalException("no se cerro correctamente el comentario");
		
	}

}
