package gc_Assembler;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

import Utils.TablaSimbolo;
import Utils.TuplaTablaSimbolos;

public class GeneradorAssembler {
	
	Vector<String> intermedio;
	Stack<String> pila;
	Vector<Integer> labels;
	int auxCounter = 0;
	private String lastCmp;
	String[] operators ={"HEADER", "MAIN",  ":",  "RETURN", "+", "-", "*", "/", "=", "<", ">", "<=", ">=", "==", "!=", "BF", "BI", "PRINT", "CALL"};
	private boolean onMain = false;
	TablaSimbolo _tds;
	private PrintWriter archivo;
	
	public GeneradorAssembler(TablaSimbolo tds, String pathArchivo, String[] codigoIntermedio){
		_tds = tds;
		intermedio = new Vector<String>();
		labels = new Vector<Integer>();
		pila = new Stack<String>();
		intermedio.addAll(Arrays.asList(codigoIntermedio));
		try{
			this.archivo = new PrintWriter(pathArchivo);
			this.generarEncabezados();
			String codigo = generateCode();
			this.generarVariables();
			archivo.println(codigo);
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
		String code = ".code"+'\n'+"start:"+'\n'+"JMP _start"+'\n';
		this.procLabels();
		int i = 0;
		for( i=0; i<intermedio.size(); i++){
			if(isLabel(i)){
				code += "label_"+i+":"+'\n';
			}
			String val = intermedio.elementAt(i);
			if(Arrays.asList(operators).contains(val)){
				code += generateOperation(val);
			}
			else
				pila.push(val);
		}
		if(isLabel(i)){
			code += "label_"+i+":"+'\n';
		}		
		return code;
	}

	private void generarVariables() {
		
		archivo.println(".data");
		archivo.println("_string	DW	\"empty\"");
		for(TuplaTablaSimbolos tupla : _tds.values()){
			int kind = tupla._kind;
			if(kind == 257){
				if(tupla._use != null){
					if(tupla._use.equals("Variable"))
						archivo.println("_"+tupla._value+"	DW	0");
					if(tupla._use.equals("Punto de entrada")){
						archivo.println("$"+tupla._value+"	DW	0");
					}
				}
			}
			if(kind == 300){
				archivo.println(tupla._value+"	DW	0");
			}
		}
	}

	private boolean isLabel(int i) {
		return labels.contains(new Integer(i));
		
	}

	private String generateOperation(String op) {
		String code = "";
		String aux = "";
		String arg1 = "";
		String arg2 = "";
		switch(op){
		case "HEADER":
			arg1 = pila.lastElement();
			code += "func_"+arg1+":"+'\n';
			break;
		case "RETURN":
			arg1 = pila.pop();
			arg2 = pila.lastElement();
			code += "mov $"+arg2+", "+arg1+'\n';
			code += "ret"+'\n';
			break;
		case "+":
			aux = generarAux();
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "add ax, "+arg2+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "-":
			aux = generarAux();
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "sub ax, "+arg2+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "*":
			aux = generarAux();
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "mov dx, "+arg2+'\n';
			code += "mul dx "+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "/":
			aux = generarAux();
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "mov dx, "+arg2+'\n';
			code += "div dx "+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "=":
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg2+'\n';
			code += "mov "+arg1+", ax"+'\n';
			break;
		case "==":
			this.lastCmp = op;
			arg2 = pila.pop();
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case "!=":
			this.lastCmp = op;
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case "<":
			this.lastCmp = op;
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+'\n';
			break;
		case ">":
			this.lastCmp = op;
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case "<=":
			this.lastCmp = op;
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case ">=":
			this.lastCmp = op; 
			arg2 = procArgument(pila.pop());
			arg1 = procArgument(pila.pop());
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case "BF":
			arg1 = pila.pop();
			
			if (this.lastCmp.equals("<"))
				 code += "JGE label_" + arg1+'\n';
			if ( this.lastCmp.equals(">") )
				code += "JLE label_" +arg1+'\n';
			if ( this.lastCmp.equals("<=") )
				code +=  "JG label_" + arg1+'\n';
			if ( this.lastCmp.equals(">=") )
				code +=  "JL label_" + arg1+'\n';
			if ( this.lastCmp.equals("!=") )
				code +=  "JE label_" + arg1+'\n';
			if ( this.lastCmp.equals("==") )
				code +=  "JNE label_" + arg1+'\n';
			break;
		case "BI":
			arg1 = pila.pop();
			code += "jmp label_" + arg1+'\n';
			break;
		case "PRINT":
			arg1 = pila.pop();
			code += "mov _string, \""+arg1+"\""+'\n';
			code += "invoke MessageBox, NULL, addr _string, addr _title, MB_OK"+'\n';
			break;
		case "CALL":
			arg1 = pila.pop();
			code += "call func_"+arg1+'\n';
			pila.push("$"+arg1);
			break;
		case ":":
			arg1 = pila.pop();
			code += "mov $"+arg1+", 0"+'\n';
			code += "ret"+'\n';
			break;
		case "MAIN":
			onMain = true;
			code += "_start:"+'\n';
		}
		return code;
		
		
	}

	private String generarAux() {
		String aux = "@aux"+auxCounter++;
		TuplaTablaSimbolos tupla = new TuplaTablaSimbolos(aux);
		tupla._kind = 300;
		_tds.addTupla(tupla);
		return aux;
	}

	private String getNextOperand() {
		String operand = pila.pop();
		
		return null;
	}

	private int getCountOp(String op) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void procLabels(){
		for(int i = 0; i<intermedio.size(); i++){
			String l = intermedio.get(i);
			if(l.startsWith("#") && l.endsWith("#")){
				String sVal = l.substring(1, l.length() - 1);
				intermedio.set(i, sVal);
				Integer val = new Integer(sVal);
				labels.add(val);
			}
		}
			
	}
	
	private String procArgument(String arg){
		TuplaTablaSimbolos tupla = _tds.getTupla(arg);
		if(arg.startsWith("$"))
			return arg;
		if(tupla._kind == 300){
			return arg;
		}
		if(tupla._kind == 257){
			return onMain? "_" + arg : "_" + arg + "_f";
			
		}
		if(tupla._kind == 263){
			return arg;
		}
		return null;
	}

}
