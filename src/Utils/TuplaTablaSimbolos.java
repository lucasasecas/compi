package Utils;

public class TuplaTablaSimbolos {

	public String _value;
	public int _type;
	public boolean _isFunction;
	
	
	
	public TuplaTablaSimbolos(String _value, int _type, boolean _isFunction) {
		this._value = _value;
		this._type = _type;
		this._isFunction = _isFunction;
	}
	
	public TuplaTablaSimbolos(){
		_type = 0;
		_isFunction = false;
		_value = new String();
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


	public boolean isFunction() {
		return _isFunction;
	}


	public void set_isFunction(boolean _isFunction) {
		this._isFunction = _isFunction;
	}


	public void mostrar() {
		String t;
		switch(_type){
			case 257:
				t = new String("Identificador");
				break;
			case 263:
				t = new String("Constante");
				break;
			case 270:
				t = new String("Cadena de caracteres");
				break;
			default:
				t = new String(" ");
				break;
		}
				
		System.out.println("Valor: "+_value+"; Tipo: "+t+"; Es funcion: "+new String(_isFunction?"si":"no"));
	}

}
