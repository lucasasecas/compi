package Utils;

import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class TuplaTablaSimbolos {
	
	LinkedHashMap<String, Object> tupla;
	
	public static enum Scopes
		{
			MAIN,
			FUNCTION
		}
	
	
	public TuplaTablaSimbolos(String value) {
		this.tupla = new LinkedHashMap<String, Object>();
		this.tupla.put("valor", value);
		
	}
	
	public TuplaTablaSimbolos(){
		this.tupla = new LinkedHashMap<String, Object>();
	}

	public Object getValue(String key) {
		return tupla.get(key);
	}

	public void setValue(String key, Object value) {
		this.tupla.put(key,
				value);
	}

	
	public void mostrar() {
		for(String key : tupla.keySet()){
			System.out.print(key+": "+tupla.get(key).toString()+"	");
		}
		System.out.println('\n');
				
	}

	public Collection<Object> getAllValues() {
		// TODO Auto-generated method stub
		return tupla.values();
	}

	public Set<String> getAllKeys() {
		// TODO Auto-generated method stub
		return tupla.keySet();
	}
	

}
