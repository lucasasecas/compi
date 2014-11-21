package unicen.compiladores.gui.utils;

import java.util.HashMap;

public class TokensDictionary {

	HashMap<Integer, String> mapa = new HashMap<Integer,String>();
	
	public TokensDictionary(){
		mapa.put(new Integer(257), "Identificador");
		mapa.put(new Integer(258), "Registro");
		mapa.put(new Integer(259), "entero");
		mapa.put(new Integer(260), "enter_l");
		mapa.put(new Integer(261), "asignacion");
		mapa.put(new Integer(262), "constante");
		mapa.put(new Integer(263), "si");
		mapa.put(new Integer(264), "sino");
		mapa.put(new Integer(265), "comparador");
		mapa.put(new Integer(266), "entonces");
		mapa.put(new Integer(267), "mientras");
		mapa.put(new Integer(268), "iterar");
		mapa.put(new Integer(269), "imprimir");
		mapa.put(new Integer(270), "cadena de caracteres");
		mapa.put(new Integer(0), "fin de linea");
		

	}
	public  String getToken(int id){
		return mapa.get(new Integer(id));
	}
	public String getToken(String value) {
		Integer integer = Integer.parseInt(value);
		return mapa.get(integer);
	}
}
