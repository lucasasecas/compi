package Utils;

import java.util.Collection;
import java.util.HashMap;

public class TablaSimbolo {

	HashMap<String, TuplaTablaSimbolos> _tabla;
	
	public TablaSimbolo(){
		_tabla = new HashMap<String, TuplaTablaSimbolos>();
	}
	public void addTupla(TuplaTablaSimbolos tts){
		_tabla.put(tts.getValue(), tts);
	}
	
	public TuplaTablaSimbolos getTupla(String clave){
		return (TuplaTablaSimbolos) _tabla.get(clave);
	}
	
	public void delTupla(String clave){
		this._tabla.remove(clave);
	}
	
	public boolean pertenece (String ttds){
		return _tabla.containsKey(ttds);
	}
	
	public void imprimirTablaSimbol(){
		if(!this._tabla.isEmpty()){
			System.out.println("");
			System.out.println("**TABLA DE SIMBOLOS**");			
		}
		for (TuplaTablaSimbolos e : _tabla.values()){
			e.mostrar();
		}
	}
	
	public Collection<TuplaTablaSimbolos> values(){
		return _tabla.values();
	}
	
}
