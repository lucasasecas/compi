package Utils;

public class TuplaTablaSimbolos {

	public String _value;
	public String _type;
	public String _use;
	public int _kind = 0;
	public String _scope;
	
	public static enum Scopes
		{
			MAIN,
			FUNCTION
		}
	
	
	public TuplaTablaSimbolos(String value) {
		this._value = value;
		
	}
	
	public TuplaTablaSimbolos(){
		
	}

	public String get_value() {
		return _value;
	}

	public void set_value(String value) {
		this._value = value;
	}

	public String get_type() {
		return _type;
	}

	public void set_type(String type) {
		this._type = type;
	}

	public String get_use() {
		return _use;
	}

	public void set_use(String use) {
		this._use = use;
	}

	public int get_kind() {
		return _kind;
	}

	public void set_kind(int _kind) {
		this._kind = _kind;
	}

	public String get_scope() {
		return _scope;
	}

	public void set_scope(String scope) {
		this._scope = scope;
	}

	public void mostrar() {
		String t;
		switch(_kind){
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
				
		System.out.println("Valor: "+_value+"; Tipo: "+t+"; Alcance: "+ _scope);
	}
	
	public TuplaTablaSimbolos clone(){
		TuplaTablaSimbolos nuevaTupla = new TuplaTablaSimbolos();
		nuevaTupla.set_kind(_kind);
		nuevaTupla.set_scope(_scope);
		nuevaTupla.set_type(_type);
		nuevaTupla.set_use(_use);
		nuevaTupla.set_value(_value);
		return nuevaTupla;
	
	}

}
