package unicen.compiladores.gui;

public class ParserError {

	public static final int TYPE_SINTACTICO = 0;
	public static final int TYPE_LEXICO = 1;
	public static final int TYPE_SEMANTICO = 2;
	


	private String mensaje;
	private int type;
	private int nroLine;
	private boolean isWarning = false;
	
	public ParserError(){
		this.mensaje = "no hay mensaje";
		this.type = ParserError.TYPE_SINTACTICO;
		this.nroLine = 0;
	}
	
	public ParserError(String mensaje, int tipo, int nro, boolean warn){
		this.mensaje = mensaje;
		this.type = tipo;
		this.nroLine = nro;
		this.isWarning = warn;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNroLine() {
		return nroLine;
	}

	public void setNroLine(int nroLine) {
		this.nroLine = nroLine;
	}
	
	public boolean isWarning(){
		return isWarning;
	}
	
	
	
}
