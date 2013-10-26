package acciones;

import Utils.LexicalException;
import al_Main.AnalizadorLexico;

public class Action4 extends Action {

	public Action4(AnalizadorLexico al) {
		super(al);
	}

	@Override
	public void ejecutar() throws LexicalException  {
		_analizador.rollback(1);
		String lex = _analizador.getActualToken().sval;
		int uint = Double.valueOf(lex).intValue();
		double d = Math.pow(2, 16)-1;
		if(uint < 0 || uint > d){
			throw new LexicalException("constante "+lex+" fuera de rango");
		}
		else{
			_analizador.addTokenToTDS();
		}
		
	}

}
