package unicen.compiladores.gui;

import java.util.Vector;

public class SentenciaManager {

	private Vector<Sentencia> sentencias;
	
	public SentenciaManager(){
		sentencias = new Vector<Sentencia>();
	}
	
	public Vector<Sentencia> getAllSentencias(){
		return this.sentencias;
	}

	public void addSentence(Sentencia sentencia) {
		this.sentencias.add(sentencia);
		
	}
}
