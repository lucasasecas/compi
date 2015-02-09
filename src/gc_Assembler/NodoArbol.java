package gc_Assembler;

public class NodoArbol {
	private NodoArbol hijoDer;
	private NodoArbol hijoIzq;
	private String value;
	/**
	 * @param hijoDer
	 * @param hijoIzq
	 * @param value
	 */
	public NodoArbol(NodoArbol hijoDer, NodoArbol hijoIzq, String value) {
		super();
		this.hijoDer = hijoDer;
		this.hijoIzq = hijoIzq;
		this.value = value;
	}
	/**
	 * @param value
	 */
	public NodoArbol(String value) {
		super();
		this.value = value;
	}
	public NodoArbol getHijoDer() {
		return hijoDer;
	}
	public void setHijoDer(NodoArbol hijoDer) {
		this.hijoDer = hijoDer;
	}
	public NodoArbol getHijoIzq() {
		return hijoIzq;
	}
	public void setHijoIzq(NodoArbol hijoIzq) {
		this.hijoIzq = hijoIzq;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		String result = "Val: "+value+'\n';
		result += (hijoIzq!=null?"hijo 1: " + hijoIzq.toString():"null") + '\n';
		result += (hijoDer!=null?"hijo 2: " + hijoDer.toString():"null") + '\n';
		return result;
		
	}
	public void setLastHijoDer(NodoArbol nodo) {
		if(hijoDer==null)
			hijoDer = nodo;
		else
			hijoDer.setLastHijoDer(nodo);
		
		
	}
	
	public NodoArbol clone(){
		NodoArbol aux = new NodoArbol(this.value);
		aux.setHijoIzq(this.hijoIzq==null?null:this.hijoIzq.clone());
		aux.setHijoDer(this.hijoDer==null?null:this.hijoDer.clone());
		return aux;
	}
	public boolean hijosHoja() {
		
		if((hijoIzq != null && hijoIzq.esHoja() && hijoDer== null)
				|| (hijoDer != null && hijoDer.esHoja() && hijoIzq== null)
				|| (hijoIzq != null && hijoIzq.esHoja() && hijoDer != null && hijoDer.esHoja()))
			return true;
		return false;
	}
	public boolean esHoja() {
		return hijoDer == null && hijoIzq == null;
	}
	public int indexOf(NodoArbol containded) {
		if (containded == this.hijoIzq)
			return 0;
		return 1;
	}
	
	
	
	
	

}
