package Utils;

import java.util.Collection;
import java.util.HashMap;

public class TablaSimbolo {

	HashMap<String, TuplaTablaSimbolos> _tabla;
	
	public TablaSimbolo(){
		_tabla = new HashMap<String, TuplaTablaSimbolos>();
	}
	public void addTupla(TuplaTablaSimbolos tts){
		if(!(_tabla.containsKey(tts.get_value())) )
				_tabla.put(tts.get_value(),tts);
	}
	
	public boolean idDeclared(String val, String scope){
		TuplaTablaSimbolos tupla = null;
		if(scope.equals("Main")){
			tupla = getTupla(val);
			if(tupla._scope == null)
				return false;
		}
		else
			tupla = getTupla(val+"_f");
		return tupla != null;
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
	
	public void setTupla(TuplaTablaSimbolos tupla) {
		TuplaTablaSimbolos nTupla = tupla.clone();
		this.delTupla(tupla._value);
		this.addTupla(nTupla);
	}
	
	public TuplaTablaSimbolos changeScope(String val){
		TuplaTablaSimbolos tupla = this.getTupla(val);
		if(tupla != null){
			if(tupla._scope != null && tupla._scope.equals("Main"))
				tupla = tupla.clone();
			else
				this.delTupla(val);
		}
		else tupla = new TuplaTablaSimbolos();
		tupla._value = val + "_f";
		tupla._scope = "funcion";
		this.addTupla(tupla);
		return tupla;
			
	}
	
}
