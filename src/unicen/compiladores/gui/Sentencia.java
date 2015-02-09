package unicen.compiladores.gui;

public class Sentencia {
	private int nroLine;
	private String sentencia;
	/**
	 * @param nroLine
	 * @param sentencia
	 */
	public Sentencia(int nroLine, String sentencia) {
		super();
		this.nroLine = nroLine;
		this.sentencia = sentencia;
	}
	public int getNroLine() {
		return nroLine;
	}
	public void setNroLine(int nroLine) {
		this.nroLine = nroLine;
	}
	public String getSentencia() {
		return sentencia;
	}
	public void setSentencia(String sentencia) {
		this.sentencia = sentencia;
	}
	
	

}
