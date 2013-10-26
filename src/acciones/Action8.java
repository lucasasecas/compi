package acciones;

import al_Main.AnalizadorLexico;

public class Action8 extends Action {

	public Action8(AnalizadorLexico al) {
		super(al);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void ejecutar() {
		_analizador.rollback(1);

	}

}
