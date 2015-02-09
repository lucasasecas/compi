package al_Main;

import java.util.Vector;

import Utils.LexicalException;
import acciones.Action;


public class Estado {
	
	Vector<Action> _actions;
	int _sigSt;
	
	public Estado(int sig){
		_actions = new Vector<Action>();
		_sigSt = sig;
	}
	
	public void addAction(Action act){
		_actions.add(act);
	}
	
	public int getState(){
		return _sigSt;
	}

	public void performActions() throws LexicalException {
		for(int i =0; i<_actions.size(); i++){
			_actions.get(i).ejecutar();
		}
		
	}
	
	

}
