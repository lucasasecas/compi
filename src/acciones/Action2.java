package acciones;

import al_Main.AnalizadorLexico;

public class Action2 extends Action {

	public Action2(AnalizadorLexico al) {
		super(al);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void ejecutar() {
		_analizador.addCharToToken();
	}

}
