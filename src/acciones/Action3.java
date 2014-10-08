package acciones;

import al_Main.AnalizadorLexico;
/**
 * analiza identificadores
 * <p>
 * Si excede el limite de caracteres o es palabra reservada, ejecuta accion
 * @author Lucas
 *
 */
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
			if(id.length() > 12){
				id = id.substring(0, 12);
				_analizador.addErrorMessage("el id "+_analizador.getActualToken().sval+" excedio el limite de longitud permitido", true);
				_analizador.truncateLexeme(id);
			}
			_analizador.addTokenToTDS();
		}

	}

}
