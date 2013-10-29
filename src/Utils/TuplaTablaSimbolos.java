package Utils;

public class TuplaTablaSimbolos {

	public String _value = new String();
	public int _type = 0;
	public String _use = new String();
	public int _kind = 0;
	public String _scope = new String();
	
	public static enum Scopes
		{
			MAIN,
			FUNCTION
		}
	
	
	public TuplaTablaSimbolos(String value) {
		this._value = _value;
		
	}
	
	public TuplaTablaSimbolos(){
		
	}


	public String getValue() {
		return _value;
	}


	public void setValue(String _value) {
		this._value = _value;
	}


	public int getType() {
		return _type;
	}


	public void setType(int _type) {
		this._type = _type;
	}


	public void mostrar() {
//		String t;
//		switch(_type){
//			case 257:
//				t = new String("Identificador");
//				break;
//			case 263:
//				t = new String("Constante");
//				break;
//			case 270:
//				t = new String("Cadena de caracteres");
//				break;
//			default:
//				t = new String(" ");
//				break;
//		}
//				
//		System.out.println("Valor: "+_value+"; Tipo: "+t+"; Es funcion: "+new String(_isFunction?"si":"no"));
	}

}
