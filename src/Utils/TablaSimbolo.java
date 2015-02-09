package Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class TablaSimbolo {

	HashMap<String, TuplaTablaSimbolos> _tabla;
	
	public TablaSimbolo(){
		_tabla = new HashMap<String, TuplaTablaSimbolos>();
	}
	public void addTupla(TuplaTablaSimbolos tts){
		TuplaTablaSimbolos t= tts;
		if(!(_tabla.containsKey((String)tts.getValue("valor"))) )
				_tabla.put((String)tts.getValue("valor"),tts);
	}
	
	public boolean isDeclared(String val, String scope){
		TuplaTablaSimbolos tupla = null;
		if(scope.equals("Main")){
			tupla = getTupla(val);

		}
		else
			tupla = getTupla(val+"_f");
		if(tupla != null && (Boolean) tupla.getValue("alcance") == null)
			return false;
		return tupla != null;
	}
	
	
	public TuplaTablaSimbolos getTupla(String clave){
		return _tabla.get(clave);
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
		TuplaTablaSimbolos[] tabla = new TuplaTablaSimbolos[_tabla.values().size()];
		tabla = _tabla.values().toArray(tabla); 
		for (int i = 0; i<tabla.length; i++){
			if(tabla[i] != null)
				tabla[i].mostrar();
		}
	}
	
	public Collection<TuplaTablaSimbolos> values(){
		return _tabla.values();
	}
	public Set<String> getAllkeys() {

		return _tabla.keySet();
	}
	

	
}
