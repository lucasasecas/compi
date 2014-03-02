package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Pila {
	private Stack<String> pila;
	List<Stack> auxiliares;
	int tope;
	Stack<Integer> flags;
	
	public Pila(){
		pila = new Stack();
		auxiliares = new ArrayList<Stack>();
		tope = -1;
		flags = new Stack<Integer>();
	}
	
	public void push(char c){
		pila.push(c+"");
		
	}
	
	public void push(String c){
		pila.push(c);
	}
	
//	public void commit(){
//		if(auxiliares.size() > 1)
//			this.addAll(auxiliares.get(auxiliares.size()-2),auxiliares.get(auxiliares.size()-1));
//		else
//			this.addAll(pila, auxiliares.get(tope));
//		auxiliares.remove(tope);
//		tope--;	
//	}
//	
//	private void addAll(Stack stack, Stack stack2) {
//		int count = stack.size();
//		for(int i= 0; i<stack2.size(); i++){
//			String content = (String) stack2.get(i).toString(); 
//			if( content.startsWith("#") && content.endsWith("#") && content.length() > 1 ){
//				String sVal = content.substring(1, content.length() -1 );
//				int val = Integer.valueOf(sVal).intValue();
//				val += count;
//				stack2.set(i, "#"+val+"#");
//			}
//		}
//		stack.addAll(stack2);
//		
//	}

//	public void fail(){
//		auxiliares.remove(tope);
//		tope--;	
//	}

	public void setSaltoPrevio(int s) {
		
		int dec = pila.size()-1;
		int index = pila.lastIndexOf("#");
		if(dec >= 0){
			int val = pila.size()+s;
			pila.set(index, "#"+val+"#" );
		}
			
	}

	public void nuevoSalto(String string) {
		this.push('#');
		this.push(string);
	}
	
	@Override
	public String toString(){
		return pila.toString();
	}
	
	public void setFlag(){
		Integer flag = new Integer(pila.size());
		flags.push(flag);	
	}

	public int getLastFlag(){
		Integer f = flags.pop();
		return f.intValue() - pila.size();
	}
	
	private int size() {
		return pila.size();
	}

	public String[] toArray() {
		String[] a = new String[pila.size()];
		a = pila.toArray(a);
		return a;
		
	}
	
	
}
