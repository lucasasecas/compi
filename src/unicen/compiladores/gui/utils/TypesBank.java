package unicen.compiladores.gui.utils;

public class TypesBank {

	public static final int ENTERO_ID = 259;
	public static final String ENTERO_STR = "entero";
	
	public static final int ENTEROL_ID = 260;
	public static final String ENTEROL_STR = "entero_l";
	
	public static final int REGISTRO_ID = 258;
	public static final String  REGISTRO_STR = "registro";
	
	public static final int CADENA_ID = 270;
	public static final String CADENA_STR = "cadena de caracteres";
	
	public static int getID(String sval){
		if(sval == ENTERO_STR){
			return ENTERO_ID;
		} else if(sval == ENTEROL_STR ){
			return ENTEROL_ID;
		}
		return 0;
	}
}
