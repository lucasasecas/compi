package al_Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import acciones.Action;
import acciones.Action1;
import acciones.Action10;
import acciones.Action2;
import acciones.Action3;
import acciones.Action4;
import acciones.Action5;
import acciones.Action6;
import acciones.Action7;
import acciones.Action8;
import acciones.Action9;
import as_Parser.ParserVal;

import Utils.BufferedCharStream;
import Utils.LexicalException;
import Utils.TablaSimbolo;
import Utils.Token;
import Utils.TuplaTablaSimbolos;


public class AnalizadorLexico {
	
	private static final int FINAL = -1;
	
	private int ID 		= 257;
	private int UINT 	= 258;
	private int FUNCTION=259;
	private int BEGIN	=260;
	private int END		=261;
	private int RETURN	=262;
	private int CTE 	= 263;
	private int ASIGN 	= 264;
	private int IF		=265;
	private int ELSE	=266;
	private int COMP	=267;
	private int THEN	=268;
	private int FOR		=269;
	private int STR		=270;
	private int PRINT	=271;
	
	Estado[][] _matrizTransicion;
	Pattern[] _patterns;
	HashMap<String, String> _palabrasReservadas;
	BufferedCharStream _reader;
	int _actualChar = -1;
	ParserVal _token;
	boolean _fgError = false;
	Vector<String> _errors = new Vector<String>();
	int _state = 0;
	public int[][] _types;
	
	public HashMap<String,Integer> _tpr;
	private TablaSimbolo _tds;
	
	public AnalizadorLexico(File fuente, TablaSimbolo tds){
		_tpr = new HashMap<String, Integer>();
		_tpr.put("uint", UINT);
		_tpr.put("function", FUNCTION);
		_tpr.put("begin", BEGIN);
		_tpr.put("end", END);
		_tpr.put("return", RETURN);
		_tpr.put("if", IF);
		_tpr.put("then", THEN);
		_tpr.put("else", ELSE);
		_tpr.put("for", FOR);
		_tpr.put("print", PRINT);
		
		_tds = tds;
		
		_patterns = new Pattern[19];
		_patterns[0] = Pattern.compile("[a-zA-Z]"); 
		_patterns[1] = Pattern.compile("\\d");
		_patterns[2] = Pattern.compile("\\+");
		_patterns[3] = Pattern.compile("-");
		_patterns[4] = Pattern.compile("\\*");
		_patterns[5] = Pattern.compile("/");
		_patterns[6] = Pattern.compile("<");
		_patterns[7] = Pattern.compile(">");
		_patterns[8] = Pattern.compile("=");
		_patterns[9] = Pattern.compile("!");
		_patterns[10] = Pattern.compile("\\(");
		_patterns[11] = Pattern.compile("\\)");
		_patterns[12] = Pattern.compile("\\,");
		_patterns[13] = Pattern.compile("\\;");
		_patterns[14] = Pattern.compile("\\:");
		_patterns[15] = Pattern.compile("\\'");
		_patterns[16] = Pattern.compile("\\s");
		_patterns[17] = Pattern.compile("\\r| \\n");
		_patterns[18] = Pattern.compile("\\t");
		_errors = new Vector<String>();
		initMatrix();
		initMatrixTypes();
		try {
			_reader = new BufferedCharStream(fuente);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int getColumn(int c){
		Matcher m;
		if(c == 0)
			return 20;
		String sC = "" + (char) c;
		for(int i=0; i<_patterns.length; i++){
			m = _patterns[i].matcher(sC);
			if(m.find())
				return i;
		}
		return 19;
	}
	
	public void initMatrix(){
		Action ac1 = new Action1(this);
		Action ac2 = new Action2(this);
		Action ac3 = new Action3(this);
		Action ac4 = new Action4(this);
		Action ac5 = new Action5(this);
		Action ac6 = new Action6(this);
		Action ac7 = new Action7(this);
		Action ac8 = new Action8(this);
		Action ac9 = new Action9(this);
		Action ac10 = new Action10(this);
 		
		Estado[][] matriz = new Estado[10][21];
		
		matriz[0][0] = new Estado(1); matriz[0][0].addAction(ac1); matriz[0][0].addAction(ac2);
 		matriz[0][1] = new Estado(2); matriz[0][1].addAction(ac1); matriz[0][1].addAction(ac2);
 		matriz[0][2] = new Estado(-1); matriz[0][2].addAction(ac1); matriz[0][2].addAction(ac2);
 		matriz[0][3] = new Estado(-1); matriz[0][3].addAction(ac1); matriz[0][3].addAction(ac2);
 		matriz[0][4] = new Estado(-1); matriz[0][4].addAction(ac1); matriz[0][4].addAction(ac2);
 		matriz[0][5] = new Estado(-1); matriz[0][5].addAction(ac1); matriz[0][5].addAction(ac2);
 		matriz[0][6] = new Estado(3); matriz[0][6].addAction(ac1); matriz[0][6].addAction(ac2);
 		matriz[0][7] = new Estado(3); matriz[0][7].addAction(ac1); matriz[0][7].addAction(ac2); 
 		matriz[0][8] = new Estado(3); matriz[0][8].addAction(ac1); matriz[0][8].addAction(ac2);
 		matriz[0][9] = new Estado(5); matriz[0][9].addAction(ac1); matriz[0][9].addAction(ac2);
 		matriz[0][10] = new Estado(6); matriz[0][10].addAction(ac1); matriz[0][10].addAction(ac2);
 		matriz[0][11] = new Estado(-1); matriz[0][11].addAction(ac1); matriz[0][11].addAction(ac2);
 		matriz[0][12] = new Estado(-1); matriz[0][12].addAction(ac1); matriz[0][12].addAction(ac2);
 		matriz[0][13] = new Estado(-1); matriz[0][13].addAction(ac1); matriz[0][13].addAction(ac2);
 		matriz[0][14] = new Estado(-1); matriz[0][14].addAction(ac1); matriz[0][14].addAction(ac2);
 		matriz[0][15] = new Estado(9); matriz[0][15].addAction(ac1);
 		matriz[0][16] = new Estado(0); 
 		matriz[0][17] = new Estado(0);
 		matriz[0][18] = new Estado(0);
 		matriz[0][19] = new Estado(-1); matriz[0][19].addAction(ac7); 
 		matriz[0][20] = new Estado(-1);
 		
 		matriz[1][0] = new Estado(1); matriz[1][0].addAction(ac2);
 		matriz[1][1] = new Estado(1); matriz[1][1].addAction(ac2);
 		matriz[1][2] = new Estado(-1); matriz[1][2].addAction(ac3);
 		matriz[1][3] = new Estado(-1); matriz[1][3].addAction(ac3);
 		matriz[1][4] = new Estado(-1); matriz[1][4].addAction(ac3);
 		matriz[1][5] = new Estado(-1); matriz[1][5].addAction(ac3);
 		matriz[1][6] = new Estado(-1); 
 		matriz[1][6].addAction(ac3);
 		matriz[1][7] = new Estado(-1); matriz[1][7].addAction(ac3);
 		matriz[1][8] = new Estado(-1); matriz[1][8].addAction(ac3);
 		matriz[1][9] = new Estado(-1); matriz[1][9].addAction(ac3);
 		matriz[1][10] = new Estado(-1); matriz[1][10].addAction(ac3);
 		matriz[1][11] = new Estado(-1); matriz[1][11].addAction(ac3);
 		matriz[1][12] = new Estado(-1); matriz[1][12].addAction(ac3);
 		matriz[1][13] = new Estado(-1); matriz[1][13].addAction(ac3);
 		matriz[1][14] = new Estado(-1); matriz[1][14].addAction(ac3);
 		matriz[1][15] = new Estado(-1);  matriz[1][15].addAction(ac3);
 		matriz[1][16] = new Estado(-1); matriz[1][16].addAction(ac3);
 		matriz[1][17] = new Estado(-1); matriz[1][17].addAction(ac3);
 		matriz[1][18] = new Estado(-1); matriz[1][18].addAction(ac3);
 		matriz[1][19] = new Estado(-1); matriz[1][19].addAction(ac3); 
 		matriz[1][20] = new Estado(-1); matriz[1][20].addAction(ac3);
 		
 		matriz[2][0] = new Estado(-1); matriz[2][0].addAction(ac4);
 		matriz[2][1] = new Estado(2); matriz[2][1].addAction(ac2);
 		matriz[2][2] = new Estado(-1); matriz[2][2].addAction(ac4);
 		matriz[2][3] = new Estado(-1); matriz[2][3].addAction(ac4);
 		matriz[2][4] = new Estado(-1); matriz[2][4].addAction(ac4);
 		matriz[2][5] = new Estado(-1); matriz[2][5].addAction(ac4);
 		matriz[2][6] = new Estado(-1); matriz[2][6].addAction(ac4);
 		matriz[2][7] = new Estado(-1); matriz[2][7].addAction(ac4);
 		matriz[2][8] = new Estado(-1); matriz[2][8].addAction(ac4);
 		matriz[2][9] = new Estado(-1); matriz[2][9].addAction(ac4);
 		matriz[2][10] = new Estado(-1); matriz[2][10].addAction(ac4);
 		matriz[2][11] = new Estado(-1); matriz[2][11].addAction(ac4);
 		matriz[2][12] = new Estado(-1); matriz[2][12].addAction(ac4);
 		matriz[2][13] = new Estado(-1); matriz[2][13].addAction(ac4);
 		matriz[2][14] = new Estado(-1); matriz[2][14].addAction(ac4);
 		matriz[2][15] = new Estado(-1); matriz[2][15].addAction(ac4);
 		matriz[2][16] = new Estado(-1);  matriz[2][16].addAction(ac4);
 		matriz[2][17] = new Estado(-1); matriz[2][17].addAction(ac4);
 		matriz[2][18] = new Estado(-1); matriz[2][18].addAction(ac4);
 		matriz[2][19] = new Estado(-1); matriz[2][19].addAction(ac4); 
 		matriz[2][20] = new Estado(-1); matriz[2][20].addAction(ac4);
 		
 		matriz[3][0] = new Estado(-1); matriz[3][0].addAction(ac8);
 		matriz[3][1] = new Estado(-1); matriz[3][1].addAction(ac8);
 		matriz[3][2] = new Estado(-1); matriz[3][2].addAction(ac8);
 		matriz[3][3] = new Estado(-1); matriz[3][3].addAction(ac8);
 		matriz[3][4] = new Estado(-1); matriz[3][4].addAction(ac8);
 		matriz[3][5] = new Estado(-1); matriz[3][5].addAction(ac8);
 		matriz[3][6] = new Estado(-1); matriz[3][6].addAction(ac8);
 		matriz[3][7] = new Estado(-1); matriz[3][7].addAction(ac8);
 		matriz[3][8] = new Estado(4); matriz[3][8].addAction(ac2);
 		matriz[3][9] = new Estado(-1); matriz[3][9].addAction(ac8);
 		matriz[3][10] = new Estado(-1); matriz[3][10].addAction(ac8);
 		matriz[3][11] = new Estado(-1); matriz[3][11].addAction(ac8);
 		matriz[3][12] = new Estado(-1); matriz[3][12].addAction(ac8);
 		matriz[3][13] = new Estado(-1); matriz[3][13].addAction(ac8);
 		matriz[3][14] = new Estado(-1); matriz[3][14].addAction(ac8);
 		matriz[3][15] = new Estado(-1); matriz[3][15].addAction(ac8); 
 		matriz[3][16] = new Estado(-1); matriz[3][16].addAction(ac8);
 		matriz[3][17] = new Estado(-1); matriz[3][17].addAction(ac8);
 		matriz[3][18] = new Estado(-1); matriz[3][18].addAction(ac8);
 		matriz[3][19] = new Estado(-1); matriz[3][19].addAction(ac8); 
 		matriz[3][20] = new Estado(-1); matriz[3][20].addAction(ac8);
 		
 		matriz[4][0] = new Estado(-1); matriz[4][0].addAction(ac8);
 		matriz[4][1] = new Estado(-1); matriz[4][1].addAction(ac8);
 		matriz[4][2] = new Estado(-1); matriz[4][2].addAction(ac8);
 		matriz[4][3] = new Estado(-1); matriz[4][3].addAction(ac8);
 		matriz[4][4] = new Estado(-1); matriz[4][4].addAction(ac8);
 		matriz[4][5] = new Estado(1);  matriz[4][5].addAction(ac8);
 		matriz[4][6] = new Estado(-1); matriz[4][6].addAction(ac8);
 		matriz[4][7] = new Estado(-1); matriz[4][7].addAction(ac8);
 		matriz[4][8] = new Estado(-1); matriz[4][8].addAction(ac8);
 		matriz[4][9] = new Estado(-1); matriz[4][9].addAction(ac8);
 		matriz[4][10] = new Estado(-1); matriz[4][10].addAction(ac8);
 		matriz[4][11] = new Estado(-1); matriz[4][11].addAction(ac8);
 		matriz[4][12] = new Estado(-1); matriz[4][12].addAction(ac8);
 		matriz[4][13] = new Estado(-1); matriz[4][13].addAction(ac8);
 		matriz[4][14] = new Estado(-1); matriz[4][14].addAction(ac8);
 		matriz[4][15] = new Estado(-1); matriz[4][15].addAction(ac8);
 		matriz[4][16] = new Estado(-1); matriz[4][16].addAction(ac8);
 		matriz[4][17] = new Estado(-1); matriz[4][17].addAction(ac8);
 		matriz[4][18] = new Estado(-1); matriz[4][18].addAction(ac8);
 		matriz[4][19] = new Estado(-1); matriz[4][19].addAction(ac8);
 		matriz[4][20] = new Estado(-1); 
 		
 		matriz[5][0] = new Estado(-1); matriz[5][0].addAction(ac9);
 		matriz[5][1] = new Estado(-1); matriz[5][1].addAction(ac9);
 		matriz[5][2] = new Estado(-1); matriz[5][2].addAction(ac9);
 		matriz[5][3] = new Estado(-1); matriz[5][3].addAction(ac9);
 		matriz[5][4] = new Estado(-1); matriz[5][4].addAction(ac9);
 		matriz[5][5] = new Estado(-1); matriz[5][5].addAction(ac9);
 		matriz[5][6] = new Estado(-1); matriz[5][6].addAction(ac9);
 		matriz[5][7] = new Estado(-1); matriz[5][7].addAction(ac9);
 		matriz[5][8] = new Estado(4); matriz[5][8].addAction(ac2);
 		matriz[5][9] = new Estado(-1); matriz[5][9].addAction(ac9);
 		matriz[5][10] = new Estado(-1); matriz[5][10].addAction(ac9);
 		matriz[5][11] = new Estado(-1); matriz[5][11].addAction(ac9);
 		matriz[5][12] = new Estado(-1); matriz[5][12].addAction(ac9);
 		matriz[5][13] = new Estado(-1); matriz[5][13].addAction(ac9);
 		matriz[5][14] = new Estado(-1); matriz[5][14].addAction(ac9);
 		matriz[5][15] = new Estado(-1); matriz[5][15].addAction(ac9); 
 		matriz[5][16] = new Estado(-1); matriz[5][16].addAction(ac9);
 		matriz[5][17] = new Estado(-1); matriz[5][17].addAction(ac9);
 		matriz[5][18] = new Estado(-1); matriz[5][18].addAction(ac9);
 		matriz[5][19] = new Estado(-1); matriz[5][18].addAction(ac9); 
 		matriz[5][20] = new Estado(-1); matriz[5][20].addAction(ac9);
 		
 		matriz[6][0] = new Estado(-1); matriz[6][0].addAction(ac8);
 		matriz[6][1] = new Estado(-1); matriz[6][1].addAction(ac8);
 		matriz[6][2] = new Estado(-1); matriz[6][2].addAction(ac8);
 		matriz[6][3] = new Estado(-1); matriz[6][3].addAction(ac8);
 		matriz[6][4] = new Estado(7); 
 		matriz[6][5] = new Estado(-1); matriz[6][5].addAction(ac8);
 		matriz[6][6] = new Estado(-1); matriz[6][6].addAction(ac8);
 		matriz[6][7] = new Estado(-1); matriz[6][7].addAction(ac8);
 		matriz[6][8] = new Estado(-1); matriz[6][8].addAction(ac8);
 		matriz[6][9] = new Estado(-1); matriz[6][9].addAction(ac8);
 		matriz[6][10] = new Estado(-1); matriz[6][10].addAction(ac8);
 		matriz[6][11] = new Estado(-1); matriz[6][11].addAction(ac8);
 		matriz[6][12] = new Estado(-1); matriz[6][12].addAction(ac8);
 		matriz[6][13] = new Estado(-1); matriz[6][13].addAction(ac8);
 		matriz[6][14] = new Estado(-1); matriz[6][14].addAction(ac8);
 		matriz[6][15] = new Estado(-1); matriz[6][15].addAction(ac8);
 		matriz[6][16] = new Estado(-1); matriz[6][16].addAction(ac8);
 		matriz[6][17] = new Estado(-1); matriz[6][17].addAction(ac8);
 		matriz[6][18] = new Estado(-1); matriz[6][18].addAction(ac8);
 		matriz[6][19] = new Estado(-1); matriz[6][19].addAction(ac8);
 		matriz[6][20] = new Estado(-1); matriz[6][20].addAction(ac8);
 		
 		matriz[7][0] = new Estado(7); 
 		matriz[7][1] = new Estado(7); 
 		matriz[7][2] = new Estado(7); 
 		matriz[7][3] = new Estado(7); 
 		matriz[7][4] = new Estado(8); 
 		matriz[7][5] = new Estado(7); 
 		matriz[7][6] = new Estado(7); 
 		matriz[7][7] = new Estado(7); 
 		matriz[7][8] = new Estado(7); 
 		matriz[7][9] = new Estado(7); 
 		matriz[7][10] = new Estado(7); 
 		matriz[7][11] = new Estado(7); 
 		matriz[7][12] = new Estado(7); 
 		matriz[7][13] = new Estado(7); 
 		matriz[7][14] = new Estado(7); 
 		matriz[7][15] = new Estado(7);  
 		matriz[7][16] = new Estado(7); 
 		matriz[7][17] = new Estado(7); 
 		matriz[7][18] = new Estado(7); 
 		matriz[7][19] = new Estado(7);  
 		matriz[7][20] = new Estado(-1); matriz[7][20].addAction(ac6);
 		
 		matriz[8][0] = new Estado(7); 
 		matriz[8][1] = new Estado(7); 
 		matriz[8][2] = new Estado(7); 
 		matriz[8][3] = new Estado(7); 
 		matriz[8][4] = new Estado(8);
 		matriz[8][5] = new Estado(7);
 		matriz[8][6] = new Estado(7);
 		matriz[8][7] = new Estado(7);
 		matriz[8][8] = new Estado(7);
 		matriz[8][9] = new Estado(7);
 		matriz[8][10] = new Estado(7);
 		matriz[8][11] = new Estado(0);
 		matriz[8][12] = new Estado(7);
 		matriz[8][13] = new Estado(7);
 		matriz[8][14] = new Estado(7);
 		matriz[8][15] = new Estado(7); 
 		matriz[8][16] = new Estado(7);
 		matriz[8][17] = new Estado(7);
 		matriz[8][18] = new Estado(7);
 		matriz[8][19] = new Estado(7); 
 		matriz[8][20] = new Estado(-1); matriz[8][20].addAction(ac6);
 		
 		matriz[9][0] = new Estado(9); matriz[9][0].addAction(ac2);
 		matriz[9][1] = new Estado(9); matriz[9][1].addAction(ac2);
 		matriz[9][2] = new Estado(9); matriz[9][2].addAction(ac2);
 		matriz[9][3] = new Estado(9); matriz[9][3].addAction(ac2);
 		matriz[9][4] = new Estado(9); matriz[9][4].addAction(ac2);
 		matriz[9][5] = new Estado(9); matriz[9][5].addAction(ac2);
 		matriz[9][6] = new Estado(9); matriz[9][6].addAction(ac2);
 		matriz[9][7] = new Estado(9); matriz[9][7].addAction(ac2);
 		matriz[9][8] = new Estado(9); matriz[9][8].addAction(ac2);
 		matriz[9][9] = new Estado(9); matriz[9][9].addAction(ac2);
 		matriz[9][10] = new Estado(9); matriz[9][10].addAction(ac2);
 		matriz[9][11] = new Estado(9); matriz[9][11].addAction(ac2);
 		matriz[9][12] = new Estado(9); matriz[9][12].addAction(ac2);
 		matriz[9][13] = new Estado(9); matriz[9][13].addAction(ac2);
 		matriz[9][14] = new Estado(9); matriz[9][14].addAction(ac2);
 		matriz[9][15] = new Estado(-1); matriz[9][15].addAction(ac10);
 		matriz[9][16] = new Estado(9); matriz[9][16].addAction(ac2);
 		matriz[9][17] = new Estado(-1); matriz[9][17].addAction(ac5);
 		matriz[9][18] = new Estado(9); matriz[9][18].addAction(ac2);
 		matriz[9][19] = new Estado(9); matriz[9][19].addAction(ac2); 
 		matriz[9][20] = new Estado(-1); matriz[9][20].addAction(ac5);
 				
 		this.setMatriz(matriz);
	}
	
	private void initMatrixTypes(){
	
		_types = new int[10][21];
		for(int i = 0; i<3; i++)
			for(int j=0; j<20; j++)
				_types[i][j] = 0;
		
		_types[0][0] = ID;
		_types[0][1] = CTE ;
		_types[0][2] = '+';
		_types[0][3] = '-';
		_types[0][4] = '*';
		_types[0][5] = '/';
		_types[0][6] = COMP;
		_types[0][7] = COMP;
		_types[0][8] = ASIGN;
		_types[0][9] = COMP;
		_types[0][10] = '(';
		_types[0][11] = ')';
		_types[0][12] = ',';
		_types[0][13] = ';';
		_types[0][14] = ':';
		_types[0][15] = STR;
				
		_types[3][8] = COMP;
		
	}
	
	
	private int transition(int state) throws LexicalException {
		int column = this.getColumn((char) _actualChar);
		Estado st =_matrizTransicion[state][column];
		
		st.performActions();
		return st.getState();
	}
		
	public void getToken() throws LexicalException {
		int state = 0; 
		int preState = state;
		_token.kind = 0;
		while(state != FINAL ){	
			_actualChar = _reader.getNextChar();
			char a = (char) _actualChar;
			preState = state;
			state = transition(state);
			int newKind = _types[preState][this.getColumn(_actualChar)];
			if(newKind != 0)
				_token.kind = newKind;
		}
		
	}

	public int getNextToken(ParserVal lval){
		while(true){
			try
			{ 
				_token = lval;
				if(eof())
					_token.kind=0;
				else
					getToken();
				lval = _token;
				return _token.kind;
			} catch (Exception e){
				addErrorMessage(e.getMessage());
			}
		}
		
	}

	
	public void addCharToToken() {
		_token.sval += (char) _actualChar;
	}


	public void initToken() {
		
		_token.row = _reader.getRow() + 1;
		_token.sval = new String();
		
	}


	public void rollback(int i) {
		_reader.retract(i);		
	}


	public ParserVal getActualToken() {
		return _token;
	}
	

	public void addErrorMessage(String message) {
		_errors.add(new String("Error (Linea "+_token.row+"): "+message));
	}

	public void setMatriz(Estado[][] states) {
		_matrizTransicion = states;
	}

	public boolean eof() {
		int aux = _reader.getNextChar();
		if(aux == 0)
			return true;
		_reader.retract(1);
		return false;
	}

	public void truncateLexeme(String id) {
		_token.sval = id;
	}

	public void printErrors() {
		for(int i =0; i<_errors.size(); i++)
			System.err.println(_errors.get(i));
		
	}

	public int yylex(ParserVal yylval) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setKindToken(int palabraReservada) {
		_token.kind = palabraReservada;
		
	}

	public void addTokenToTDS() {
		TuplaTablaSimbolos tupla = new TuplaTablaSimbolos(new String(_token.sval));
		tupla._kind = _token.kind;
		_tds.addTupla(tupla);
		
	}

}
