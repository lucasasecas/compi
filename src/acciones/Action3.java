package acciones;

import Utils.TuplaTablaSimbolos;
import al_Main.AnalizadorLexico;

public class Action3 extends Action {

	public Action3(AnalizadorLexico al) {
		super(al);	
	}

	@Override
	public void ejecutar() {
		_analizador.rollback(1);
		String id = _analizador.getActualToken().sval;
		
		if(_analizador._tpr.containsKey(id)){
			_analizador.setKindToken(_analizador._tpr.get(id));
		}
		else{
			if(id.length() > 15){
				id = id.substring(0, 15);
				_analizador.addErrorMessage("Warning: el id "+_analizador.getActualToken()+" excedio el limite de longitud permitido");
				_analizador.truncateLexeme(id);
			}
			_analizador.addTokenToTDS();
		}

	}

}
