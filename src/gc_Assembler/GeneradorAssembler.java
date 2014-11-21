package gc_Assembler;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import Utils.TablaSimbolo;
import Utils.TuplaTablaSimbolos;

public class GeneradorAssembler {
	
	Vector<String> intermedio;
	Stack<Integer> labels;
	int auxCounter = 0;
	HashMap<String, String> messages = new HashMap<String, String>();
	private String lastCmp;
	String[] operators ={"HEADER", "MAIN",  ":",  "RETURN", "+", "-", "*", "/", "=", "<", ">", "<=", ">=", "==", "!=", "BF", "BI", "PRINT", "CALL"};
	private boolean onMain = false;
	TablaSimbolo _tds;
	private PrintWriter archivo;
	private NodoArbol raiz;
	private int countLabels;
		
	public GeneradorAssembler(TablaSimbolo tds, String pathArchivo, NodoArbol nodo){
		_tds = tds;
		intermedio = new Vector<String>();
		labels = new Stack<Integer>();
		raiz = nodo;
		try{
			this.archivo = new PrintWriter(pathArchivo);
			this.generarEncabezados();
			String codigo = generateCode();
			System.out.println();
			this.generarVariables();
			archivo.println(codigo);
			archivo.println("label_quit:");
			archivo.println("invoke ExitProcess , 0");
			archivo.println("end start");
		} catch(Exception e){
			System.err.println("Imposible generar codigo assembler");
		}
		
		archivo.close();
	}
	
	private void generarEncabezados() {
		archivo.println(".386");
		archivo.println(".model flat, stdcall");
		archivo.println("option casemap :none");
		archivo.println("include \\masm32\\include\\windows.inc");
		archivo.println("include \\masm32\\include\\kernel32.inc");
		archivo.println("include \\masm32\\include\\user32.inc");
		archivo.println("include \\masm32\\include\\masm32.inc");
		archivo.println("includelib \\masm32\\lib\\kernel32.lib");
		archivo.println("includelib \\masm32\\lib\\user32.lib");
		archivo.println("includelib \\masm32\\lib\\masm32.lib");
		
	}

	private String generateCode(){
		String code = ".code"+'\n'+"start:"+'\n';
		code += "JMP _start"+'\n';
		code += "label_div0Excp:"+'\n';
		code += "invoke MessageBox, NULL, addr _error, addr _error, MB_OK"+'\n';
		code += "JMP label_quit"+'\n';
		code += "_start:"+'\n';
//		this.procLabels();
		this.procMessages();
		code += procesarSentencia(raiz);
//		int i = 0;
//		for( i=0; i<intermedio.size(); i++){
//			if(isLabel(i)){
//				code += "label_"+i+":"+'\n';
//			}
//			String val = intermedio.elementAt(i);
//			if(Arrays.asList(operators).contains(val)){
//				code += generateOperation(val);
//			}
//			else
//				pila.push(val);
//		}
//		if(isLabel(i)){
//			code += "label_"+i+":"+'\n';
//		}
		
		return code;
	}

	private String procesarSentencia(NodoArbol nodo) {
		String codigo = "";
		if(nodo==null || nodo.esHoja()) return "";
		System.out.println(nodo.getValue());
		if(nodo.getValue()=="S"){
			codigo += procesarSentencia(nodo.getHijoDer());
			codigo += procesarSentencia(nodo.getHijoIzq());
			nodo.setHijoIzq(null);;
			nodo.setHijoDer(null);
		}else if(nodo.hijosHoja())
			codigo += generateOperation(nodo)+'\n';
			else{
				codigo += procesarSentencia(nodo.getHijoIzq());
				codigo += procesarSentencia(nodo.getHijoDer());
				codigo += procesarSentencia(nodo);
			}
				
		return codigo;
		
	}

	private void procMessages() {
		String code = "";
		int count = 0;
		for(TuplaTablaSimbolos tupla : _tds.values())
			if((Integer) tupla.getValue("clase") == 270){
				int c = count++;
				String message = (String) tupla.getValue("valor");
				messages.put(message, "_message"+c);
			}
	}
//
	private void generarVariables() {
		
		archivo.println(".data");
		int count  = 0;
		String tipoReg;
		String prefix;
		for(TuplaTablaSimbolos tupla : _tds.values()){
			int kind =(Integer) tupla.getValue("clase");
			if(tupla.getValue("tipo")!=null && kind != 262){
				tipoReg = esEntero((String)tupla.getValue("valor"))?"DW":"DD";
				prefix = ((String)tupla.getValue("valor")).startsWith("@")?"":"_";
				archivo.println(prefix+(String)tupla.getValue("valor")+"	"+tipoReg+"	0");
				continue;
			}
			
			if(kind == 270){
				
				archivo.println(messages.get((String)tupla.getValue("valor"))+" db \""+ (String)tupla.getValue("valor")+"\", 0");
			}
		}
		archivo.println("_error db \"No se puede dividir por cero\", 0");
	}
//
//	private boolean isLabel(int i) {
//		return labels.contains(new Integer(i));
//		
//	}

	private String generateOperation(NodoArbol nodo) {
		String code = "";
		String aux = "";
		String arg1 = nodo.getHijoIzq()==null?"":nodo.getHijoIzq().getValue();
		String arg2 = nodo.getHijoDer()==null?"":nodo.getHijoDer().getValue();
		String reg;
		String reg2;
		String op = nodo.getValue();
		boolean entero;
		switch(op){
		case "+":
			aux = generarAux((String)(_tds.getTupla(arg1).getValue("tipo")));
			reg = esEntero(arg1)?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);

			code += "mov "+reg+", "+arg1+'\n';
			code += "add "+reg+", "+arg2+'\n';
			code += "mov "+aux+", "+reg+'\n';
			nodo.setValue(aux);
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "-":
			aux = generarAux((String)(_tds.getTupla(arg1).getValue("tipo")));
			reg = esEntero(arg1)?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "sub "+reg+", "+arg2+'\n';
			code += "mov "+aux+", "+reg+'\n';
			nodo.setValue(aux);
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "*":
			aux = generarAux((String)(_tds.getTupla(arg1).getValue("tipo")));
			reg = esEntero(arg1)?"ax":"eax";
			reg2 = esEntero(arg1)?"dx":"edx";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "mov "+reg2+", "+arg2+'\n';
			code += "mul "+reg2+'\n';
			code += "mov "+aux+", "+reg+'\n';
			nodo.setValue(aux);
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "/":
			aux = generarAux((String)(_tds.getTupla(arg1).getValue("tipo")));
			entero = esEntero(arg1); 
			reg = entero?"ax":"eax";
			reg2 = entero?"dx":"edx";
			String reg3 = entero?"bx":"ebx";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg2+", 0"+'\n';
			code += "mov "+reg+", "+arg1+'\n';
			code += "mov "+reg3+", "+arg2+'\n';
			code += "cmp "+reg3+", 0"+'\n';
			code +=  "JE label_div0Excp"+'\n';
			code += "div "+reg3+'\n';
			code += "mov "+aux+", "+reg+'\n';
			nodo.setValue(aux);
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case ":=":
			reg = esEntero(arg1)?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg2+'\n';
			code += "mov "+arg1+", "+reg+'\n';
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "<":
			this.lastCmp = op;
			entero = esEntero(arg1); 
			reg = entero?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "cmp "+reg+","+arg2+'\n';
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case ">":
			this.lastCmp = op;
			entero = esEntero(arg1); 
			reg = entero?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "cmp "+reg+","+arg2+'\n';
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case ">=":
			this.lastCmp = op;
			entero = esEntero(arg1); 
			reg = entero?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "cmp "+reg+","+arg2+'\n';
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "<=":
			this.lastCmp = op;
			entero = esEntero(arg1); 
			reg = entero?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "cmp "+reg+","+arg2+'\n';
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "=":
			this.lastCmp = op;
			entero = esEntero(arg1); 
			reg = entero?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "cmp "+reg+","+arg2+'\n';
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "^=":
			this.lastCmp = op;
			entero = esEntero(arg1); 
			reg = entero?"ax":"eax";
			arg1 = procArgument(arg1);
			arg2 = procArgument(arg2);
			code += "mov "+reg+", "+arg1+'\n';
			code += "cmp "+reg+","+arg2+'\n';
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			nodo=null;
			break;
		case "CONDICION":
			int actual = apiloLabel();
			if (this.lastCmp.equals("<"))
				 code += "JGE label_" + actual+'\n';
			if ( this.lastCmp.equals(">") )
				code += "JLE label_" +actual+'\n';
			if ( this.lastCmp.equals("<=") )
				code +=  "JG label_" + actual+'\n';
			if ( this.lastCmp.equals(">=") )
				code +=  "JL label_" + actual+'\n';
			if ( this.lastCmp.equals("^=") )
				code +=  "JE label_" + actual+'\n';
			if ( this.lastCmp.equals("=") )
				code +=  "JNE label_" + actual+'\n';
			nodo.setHijoIzq(null);
			break;
		case "ENTONCES":
			int viejo = desapiloLabel();
			int nuevo = apiloLabel();
			code += "JMP label_" + nuevo+'\n';
			code += "label_"+viejo+":\n";
			nodo.setHijoIzq(null);
			break;
		case "CUERPO":
			code+="label_"+desapiloLabel()+":\n";
			nodo.setHijoIzq(null);
			nodo.setHijoDer(null);
			break;
		case "IMPRIMIR":
			arg1 = procArgument(arg1);
			code += "invoke MessageBox, NULL, addr "+arg1+", addr "+arg1+", MB_OK"+'\n';
			nodo.setHijoDer(null);
			nodo.setHijoIzq(null);
			break;
		default:
			nodo.setHijoDer(null);
			nodo.setHijoIzq(null);
			break;
		}
		
		return code;
		
		
	}

	private int apiloLabel() {
		
		labels.push(new Integer(++countLabels));
		return countLabels;
	}

	private int desapiloLabel() {
		return labels.pop();
	
	}

	private boolean esEntero(String arg1) {
		TuplaTablaSimbolos tupla = _tds.getTupla(arg1);
		return tupla!=null && ((String)tupla.getValue("tipo")).equals("entero");
	}

	private String generarAux(String tipo) {
		String aux = "@aux"+auxCounter++;
		TuplaTablaSimbolos tupla = new TuplaTablaSimbolos(aux);
		tupla.setValue("clase",300);
		if(tipo!=null){
			tupla.setValue("tipo",tipo);
		}
		_tds.addTupla(tupla);
		return aux;
	}

	
	private String procArgument(String arg){
		if(arg.equals("ax")){
			return arg;
		}
		TuplaTablaSimbolos tupla = _tds.getTupla(arg);
		int kind = (Integer) tupla.getValue("clase");
		if(kind == 300 || kind == 262){
			return arg;
		}
		if(kind == 257){
			return "_" + arg;	
		}
		if(kind==270){
			return messages.get(arg);
		}
		return null;
	}
//
//	public boolean hayErrores() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
}
