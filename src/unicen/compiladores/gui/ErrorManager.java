package unicen.compiladores.gui;

import java.util.Vector;

public class ErrorManager {
	
	Vector<ParserError> errores;
	
	public ErrorManager(){
		this.errores = new Vector<ParserError>();
	}
	
	public void addError(ParserError error){
		errores.add(error);
	}
	
	public Vector<ParserError> getAllErrors(){
		return errores;
	}

	public boolean isEmpty() {
		return errores.size()==0;
	}

}
