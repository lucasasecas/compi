package acciones;

import Utils.LexicalException;
import al_Main.AnalizadorLexico;

/**
 * analizador de constante de rango
 * @author Lucas
 *
 */
public class Action4 extends Action {

	public static double MAX = Math.pow(2, 15)-1;
	public static double MIN = Math.pow(2, 15);
	public static double MIN_L = Math.pow(2, 31);
	public static double MAX_L = Math.pow(2, 31)-1;
	public Action4(AnalizadorLexico al) {
		super(al);
	}

	@Override
	public void ejecutar() throws LexicalException  {
		_analizador.rollback(1);
		String lex = _analizador.getActualToken().sval;
		long uint = Long.valueOf(lex).longValue();
		if(uint  > MIN){
			if(uint >MIN_L)
				_analizador.addErrorMessage("constante fuera de rango", false);
			_analizador.addTokenToTDS();
		}
		else{
			
			_analizador.addTokenToTDS();
			
		}
		_analizador.setInUseFalse();
		
	}

}
