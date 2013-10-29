package gc_Assembler;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

public class GeneradorAssembler {
	
	Vector<String> intermedio;
	Stack<String> pila;
	Vector<Integer> labels;
	int auxCounter = 0;
	private String lastCmp;
	String[] operators ={"HEADER", ":",  "RETURN", "+", "-", "*", "/", "=", "<", ">", "<=", ">=", "==", "!=", "BF", "BI", "PRINT", "CALL"};
	
	public GeneradorAssembler(){
		intermedio = new Vector<String>();
		labels = new Vector<Integer>();
		pila = new Stack<String>();
	}
	
	public String generateCode(String[] codigoIntermedio){
		intermedio.addAll(Arrays.asList(codigoIntermedio));
		String code = new String();
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
			aux = "@aux"+auxCounter++;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "add ax, "+arg2+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "-":
			aux = "@aux"+auxCounter++;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "sub ax, "+arg2+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "*":
			aux = "@aux"+auxCounter++;
			arg2 = this.getNextOperand();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "mov dx, "+arg2+'\n';
			code += "mul dx "+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "/":
			aux = "@aux"+auxCounter++;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "mov dx, "+arg2+'\n';
			code += "div dx "+'\n';
			code += "mov "+aux+", ax"+'\n';
			pila.push(aux);
			break;
		case "=":
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg2+'\n';
			code += "mov "+arg1+", ax"+'\n';
			break;
		case "==":
			this.lastCmp = op;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case "!=":
			this.lastCmp = op;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case "<":
			this.lastCmp = op;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+'\n';
			break;
		case ">":
			this.lastCmp = op;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case "<=":
			this.lastCmp = op;
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
			code += "mov ax, "+arg1+'\n';
			code += "cmp ax,"+arg2+", ax"+'\n';
			break;
		case ">=":
			this.lastCmp = op; 
			arg2 = pila.pop();
			arg2 = arg1.startsWith("@")?arg2: "_"+arg2;
			arg1 = pila.pop();
			arg1 = arg2.startsWith("@")?arg1: "_"+arg1;
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
			code += "call func_"+arg1;
			pila.push("$"+arg1);
			break;
		case ":":
			arg1 = pila.pop();
			code += "mov $"+arg1+", 0"+'\n';
			code += "ret"+'\n';
			break;
		}
		return code;
		
		
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

}
