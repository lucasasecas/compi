package acciones;

import al_Main.AnalizadorLexico;

public class Action1 extends Action {

	public Action1(AnalizadorLexico al) {
		super(al);
	}

	@Override
	public void ejecutar() {
		_analizador.initToken();
	}

	
}
