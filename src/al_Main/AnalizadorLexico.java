package al_Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import unicen.compiladores.gui.ErrorManager;
import unicen.compiladores.gui.ParserError;
import acciones.Action;
import acciones.Action1;
import acciones.Action10;
import acciones.Action11;
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
import Utils.TuplaTablaSimbolos;


public class AnalizadorLexico {
	
	private static final int FINAL = -1;
	
	private int ID 		= 257;
	private int REGISTRO= 258;
	private int ENTERO 	= 259;
	private int ENTEROL	= 260;
	private int ASIGN 	= 261;
	private int CTE 	= 262;
	private int SI		= 263;
	private int SINO	= 264;
	private int COMP	= 265;
	private int ENTONCES= 266;
	private int MIENTRAS= 267;
	private int ITERATE = 268;
	private int PRINT	= 269;
	private int STR		= 270;


	private int EOF     = 0;
	
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

	private ErrorManager errorManager;
	
	public AnalizadorLexico(TablaSimbolo tds, ErrorManager errorManager){
		this.errorManager = errorManager;
		_tpr = new HashMap<String, Integer>();
		_tpr.put("entero", ENTERO);
		_tpr.put("entero_l", ENTEROL);
		_tpr.put("si", SI);
		_tpr.put("entonces", ENTONCES);
		_tpr.put("sino", SINO);
		_tpr.put("mientras", MIENTRAS);
		_tpr.put("imprimir", PRINT);
		_tpr.put("registro", REGISTRO);
		_tpr.put("iterar", ITERATE);
		_tds = tds;
		
		_patterns = new Pattern[22];
		_patterns[0] = Pattern.compile("[a-zA-Z]"); 
		_patterns[1] = Pattern.compile("\\d");
		_patterns[2] = Pattern.compile("\\+");
		_patterns[3] = Pattern.compile("-");
		_patterns[4] = Pattern.compile("\\*");
		_patterns[5] = Pattern.compile("/");
		_patterns[6] = Pattern.compile("<");
		_patterns[7] = Pattern.compile(">");
		_patterns[8] = Pattern.compile("\\:");
		_patterns[9] = Pattern.compile("=");
		_patterns[10] = Pattern.compile("\\^");
		_patterns[11] = Pattern.compile("\\(");
		_patterns[12] = Pattern.compile("\\)");
		_patterns[13] = Pattern.compile("\\{");
		_patterns[14] = Pattern.compile("\\}");
		_patterns[15] = Pattern.compile("\\,");
		_patterns[16] = Pattern.compile("\\;");
		_patterns[17] = Pattern.compile("\\'");
		_patterns[18] = Pattern.compile("\\.");
		_patterns[19] = Pattern.compile("\\r|\\n");
		_patterns[20] = Pattern.compile("\\t|\\s");
		_patterns[21] = Pattern.compile("\\$|\\&|_");
		
		
		
		
		_errors = new Vector<String>();
		initMatrix();
		initMatrixTypes();
	}
	
	/**
	 * Retorna la columna que representa el caracter leido	
	 * @param c caracter leido
	 * @return columna de la tabla
	 */
	private int getColumn(int c){
		Matcher m;
		if(c == 0)
			return _patterns.length+1;
		String sC = "" + (char) c;
		for(int i=0; i<_patterns.length; i++){
			m = _patterns[i].matcher(sC);
			if(m.find())
				return i;
		}
		return _patterns.length;
	}
	
	public void initMatrix(){
		Action ac1 = new Action1(this);
		Action ac2 = new Action2(this);
		Action ac3 = new Action3(this);
		Action ac4 = new Action4(this);

		Action ac6 = new Action6(this);
		Action ac7 = new Action7(this);
		Action ac8 = new Action8(this);
		Action ac9 = new Action9(this);
		Action ac10 = new Action10(this);
		Action ac11 = new Action11(this);
 		
		Estado[][] matriz = new Estado[13][24];
		Action5 ac5_1 = new Action5(this, "Caracter invalido");
		matriz[0][0] = new Estado(1);	matriz[0][0].addAction(ac1);	matriz[0][0].addAction(ac2);
 		matriz[0][1] = new Estado(2); 	matriz[0][1].addAction(ac1);	matriz[0][1].addAction(ac2);
 		matriz[0][2] = new Estado(-1);	matriz[0][2].addAction(ac1);	matriz[0][2].addAction(ac2);
 		matriz[0][3] = new Estado(-1);	matriz[0][3].addAction(ac1);	matriz[0][3].addAction(ac2);
 		matriz[0][4] = new Estado(3); 	matriz[0][4].addAction(ac1);	matriz[0][4].addAction(ac2);
 		matriz[0][5] = new Estado(-1);	matriz[0][5].addAction(ac1);	matriz[0][5].addAction(ac2);
 		matriz[0][6] = new Estado(5); 	matriz[0][6].addAction(ac1);	matriz[0][6].addAction(ac2);
 		matriz[0][7] = new Estado(7); 	matriz[0][7].addAction(ac1);	matriz[0][7].addAction(ac2);
 		matriz[0][8] = new Estado(9); 	matriz[0][8].addAction(ac1);	matriz[0][8].addAction(ac2);
 		matriz[0][9] = new Estado(-1);	matriz[0][9].addAction(ac1);	matriz[0][9].addAction(ac2);
 		matriz[0][10] = new Estado(10); matriz[0][10].addAction(ac1);	matriz[0][10].addAction(ac2);
 		matriz[0][11] = new Estado(-1); matriz[0][11].addAction(ac1);	matriz[0][11].addAction(ac2);
 		matriz[0][12] = new Estado(-1); matriz[0][12].addAction(ac1);	matriz[0][12].addAction(ac2);
 		matriz[0][13] = new Estado(-1); matriz[0][13].addAction(ac1);	matriz[0][13].addAction(ac2);
 		matriz[0][14] = new Estado(-1); matriz[0][14].addAction(ac1);	matriz[0][14].addAction(ac2);
 		matriz[0][15] = new Estado(-1); matriz[0][15].addAction(ac1);	matriz[0][15].addAction(ac2);
 		matriz[0][16] = new Estado(-1); matriz[0][16].addAction(ac1);	matriz[0][16].addAction(ac2);
 		matriz[0][17] = new Estado(11); matriz[0][17].addAction(ac1);
 		matriz[0][18] = new Estado(-1); matriz[0][18].addAction(ac1);	matriz[0][18].addAction(ac2);
 		matriz[0][19] = new Estado(0);  //matriz[0][18].addAction(ac8);
 		matriz[0][20] = new Estado(0);  //matriz[0][19].addAction(ac8);
 		matriz[0][21] = new Estado(-1); matriz[0][21].addAction(ac3);
 		matriz[0][22] = new Estado(0);  matriz[0][22].addAction(ac11);
 		matriz[0][23] = new Estado(-1); matriz[0][23].addAction(ac1);
		
		matriz[1][0] = new Estado(1);   matriz[1][0].addAction(ac2); 
 		matriz[1][1] = new Estado(1);   matriz[1][1].addAction(ac2); 
 		matriz[1][2] = new Estado(-1);  matriz[1][2].addAction(ac3);
 		matriz[1][3] = new Estado(-1);  matriz[1][3].addAction(ac3);
 		matriz[1][4] = new Estado(-1);   matriz[1][4].addAction(ac3);  
 		matriz[1][5] = new Estado(-1);  matriz[1][5].addAction(ac3);  
 		matriz[1][6] = new Estado(-1);   matriz[1][6].addAction(ac3);  
 		matriz[1][7] = new Estado(-1);   matriz[1][7].addAction(ac3);  
 		matriz[1][8] = new Estado(-1);   matriz[1][8].addAction(ac3);  
 		matriz[1][9] = new Estado(-1);  matriz[1][9].addAction(ac3);  
 		matriz[1][10] = new Estado(-1); matriz[1][10].addAction(ac3); 
 		matriz[1][11] = new Estado(-1); matriz[1][11].addAction(ac3); 
 		matriz[1][12] = new Estado(-1); matriz[1][12].addAction(ac3); 
 		matriz[1][13] = new Estado(-1); matriz[1][13].addAction(ac3); 
 		matriz[1][14] = new Estado(-1); matriz[1][14].addAction(ac3); 
 		matriz[1][15] = new Estado(-1); matriz[1][15].addAction(ac3); 
 		matriz[1][16] = new Estado(-1); matriz[1][16].addAction(ac3); 
 		matriz[1][17] = new Estado(-1); matriz[1][17].addAction(ac3); 
 		matriz[1][18] = new Estado(-1);  matriz[1][18].addAction(ac3); 
 		matriz[1][19] = new Estado(-1);  matriz[1][19].addAction(ac3); 
 		matriz[1][20] = new Estado(-1);  matriz[1][20].addAction(ac3);
 		matriz[1][21] = new Estado(1); matriz[1][21].addAction(ac2);
 		matriz[1][22] = new Estado(-1); matriz[1][22].addAction(ac3);
 		matriz[1][23] = new Estado(-1); matriz[1][23].addAction(ac3);
 		
 		matriz[2][0] = new Estado(-1);    matriz[2][0].addAction(ac4);   
 		matriz[2][1] = new Estado(2);    matriz[2][1].addAction(ac2);   
 		matriz[2][2] = new Estado(-1);   matriz[2][2].addAction(ac4);   
 		matriz[2][3] = new Estado(-1);   matriz[2][3].addAction(ac4);   
 		matriz[2][4] = new Estado(-1);    matriz[2][4].addAction(ac4);  
 		matriz[2][5] = new Estado(-1);   matriz[2][5].addAction(ac4);   
 		matriz[2][6] = new Estado(-1);    matriz[2][6].addAction(ac4);  
 		matriz[2][7] = new Estado(-1);    matriz[2][7].addAction(ac4);  
 		matriz[2][8] = new Estado(-1);    matriz[2][8].addAction(ac4);  
 		matriz[2][9] = new Estado(-1);   matriz[2][9].addAction(ac4);   
 		matriz[2][10] = new Estado(-1); matriz[2][10].addAction(ac4);  
 		matriz[2][11] = new Estado(-1); matriz[2][11].addAction(ac4);  
 		matriz[2][12] = new Estado(-1); matriz[2][12].addAction(ac4);  
 		matriz[2][13] = new Estado(-1); matriz[2][13].addAction(ac4);  
 		matriz[2][14] = new Estado(-1); matriz[2][14].addAction(ac4);  
 		matriz[2][15] = new Estado(-1); matriz[2][15].addAction(ac4);  
 		matriz[2][16] = new Estado(-1); matriz[2][16].addAction(ac4);  
 		matriz[2][17] = new Estado(-1); matriz[2][17].addAction(ac4);  
 		matriz[2][18] = new Estado(-1);  matriz[2][18].addAction(ac4);
 		matriz[2][19] = new Estado(-1);  matriz[2][19].addAction(ac4); 
 		matriz[2][20] = new Estado(-1);  matriz[2][20].addAction(ac4); 
 		matriz[2][21] = new Estado(-1); matriz[2][21].addAction(ac4);   
 		matriz[2][22] = new Estado(-1); matriz[2][22].addAction(ac4);  
 		matriz[2][23] = new Estado(-1); matriz[2][23].addAction(ac4);  
 		
 		matriz[3][0] = new Estado(-1); 	matriz[3][0].addAction(ac8); 
 		matriz[3][1] = new Estado(-1); 	matriz[3][1].addAction(ac8);
 		matriz[3][2] = new Estado(-1);	matriz[3][2].addAction(ac8);
 		matriz[3][3] = new Estado(-1);	matriz[3][3].addAction(ac8);
 		matriz[3][4] = new Estado(4); 	
 		matriz[3][5] = new Estado(-1);	matriz[3][5].addAction(ac8);
 		matriz[3][6] = new Estado(-1); 	matriz[3][6].addAction(ac8);
 		matriz[3][7] = new Estado(-1); 	matriz[3][7].addAction(ac8);
 		matriz[3][8] = new Estado(-1); 	matriz[3][8].addAction(ac8);
 		matriz[3][9] = new Estado(-1); 	matriz[3][9].addAction(ac8);
 		matriz[3][10] = new Estado(-1);	matriz[3][10].addAction(ac8);
 		matriz[3][11] = new Estado(-1);	matriz[3][11].addAction(ac8);
 		matriz[3][12] = new Estado(-1);	matriz[3][12].addAction(ac8);
 		matriz[3][13] = new Estado(-1);	matriz[3][13].addAction(ac8);
 		matriz[3][14] = new Estado(-1);	matriz[3][14].addAction(ac8);
 		matriz[3][15] = new Estado(-1);	matriz[3][15].addAction(ac8);
 		matriz[3][16] = new Estado(-1); matriz[3][16].addAction(ac8);
 		matriz[3][17] = new Estado(-1);	matriz[3][17].addAction(ac8);
 		matriz[3][18] = new Estado(-1);	matriz[3][18].addAction(ac8);
 		matriz[3][19] = new Estado(-1);	matriz[3][19].addAction(ac8);
 		matriz[3][20] = new Estado(-1);	matriz[3][20].addAction(ac8);
 		matriz[3][21] = new Estado(-1);	matriz[3][21].addAction(ac8);
 		matriz[3][22] = new Estado(-1);	matriz[3][22].addAction(ac8);
 		matriz[3][23] = new Estado(-1);	matriz[3][23].addAction(ac8);
 		
 		matriz[4][0] = new Estado(4); 
 		matriz[4][1] = new Estado(4); 
 		matriz[4][2] = new Estado(4);
 		matriz[4][3] = new Estado(4);
 		matriz[4][4] = new Estado(4); 
 		matriz[4][5] = new Estado(4);
 		matriz[4][6] = new Estado(4); 
 		matriz[4][7] = new Estado(4); 
 		matriz[4][8] = new Estado(4); 
 		matriz[4][9] = new Estado(4);
 		matriz[4][10] = new Estado(4);
 		matriz[4][11] = new Estado(4);
 		matriz[4][12] = new Estado(4);
 		matriz[4][13] = new Estado(4);
 		matriz[4][14] = new Estado(4);
 		matriz[4][15] = new Estado(4);
 		matriz[4][16] = new Estado(4);
 		matriz[4][17] = new Estado(4);
 		matriz[4][18] = new Estado(4);
 		matriz[4][19] = new Estado(0);
 		matriz[4][20] = new Estado(4); 
 		matriz[4][21] = new Estado(4);
 		matriz[4][22] = new Estado(4);
 		matriz[4][23] = new Estado(0);
 		
 		matriz[5][0] = new  Estado(-1);  matriz[5][0].addAction(ac8);
 		matriz[5][1] = new  Estado(-1);  matriz[5][1].addAction(ac8);
 		matriz[5][2] = new  Estado(-1);  matriz[5][2].addAction(ac8);
 		matriz[5][3] = new  Estado(-1);  matriz[5][3].addAction(ac8);
 		matriz[5][4] = new  Estado(-1);  matriz[5][4].addAction(ac8);
 		matriz[5][5] = new  Estado(-1);  matriz[5][5].addAction(ac8);
 		matriz[5][6] = new  Estado(-1);  matriz[5][6].addAction(ac8);
 		matriz[5][7] = new  Estado(-1);  matriz[5][7].addAction(ac8);
 		matriz[5][8] = new  Estado(-1);  matriz[5][8].addAction(ac8);
 		matriz[5][9] = new  Estado(6);   matriz[5][9].addAction(ac2);
 		matriz[5][10] = new Estado(-1);  matriz[5][10].addAction(ac8);
 		matriz[5][11] = new Estado(-1); matriz[5][11].addAction(ac8);
 		matriz[5][12] = new Estado(-1); matriz[5][12].addAction(ac8);
 		matriz[5][13] = new Estado(-1); matriz[5][13].addAction(ac8);
 		matriz[5][14] = new Estado(-1); matriz[5][14].addAction(ac8);
 		matriz[5][15] = new Estado(-1); matriz[5][15].addAction(ac8);
 		matriz[5][16] = new Estado(-1); matriz[5][16].addAction(ac8);
 		matriz[5][17] = new Estado(-1); matriz[5][17].addAction(ac8);
 		matriz[5][18] = new Estado(-1); matriz[5][18].addAction(ac8);
 		matriz[5][19] = new Estado(-1); matriz[5][19].addAction(ac8);
 		matriz[5][20] = new Estado(-1); matriz[5][20].addAction(ac8);
 		matriz[5][21] = new Estado(-1); matriz[5][21].addAction(ac8);
 		matriz[5][22] = new Estado(-1); matriz[5][22].addAction(ac8);
 		matriz[5][23] = new Estado(-1); matriz[5][23].addAction(ac8);
 		
 		matriz[6][0] =  new Estado(-1);   matriz[6][0].addAction(ac8); 
 		matriz[6][1] =  new Estado(-1);   matriz[6][1].addAction(ac8); 
 		matriz[6][2] =  new Estado(-1);   matriz[6][2].addAction(ac8); 
 		matriz[6][3] =  new Estado(-1);   matriz[6][3].addAction(ac8); 
 		matriz[6][4] =  new Estado(-1);   matriz[6][4].addAction(ac8); 
 		matriz[6][5] =  new Estado(-1);   matriz[6][5].addAction(ac8); 
 		matriz[6][6] =  new Estado(-1);   matriz[6][6].addAction(ac8); 
 		matriz[6][7] =  new Estado(-1);   matriz[6][7].addAction(ac8); 
 		matriz[6][8] =  new Estado(-1);   matriz[6][8].addAction(ac8); 
 		matriz[6][9] =  new Estado(-1);   matriz[6][9].addAction(ac8); 
 		matriz[6][10] = new Estado(-1);   matriz[6][10].addAction(ac8);
 		matriz[6][11] = new Estado(-1);  matriz[6][11].addAction(ac8); 
 		matriz[6][12] = new Estado(-1);  matriz[6][12].addAction(ac8); 
 		matriz[6][13] = new Estado(-1);  matriz[6][13].addAction(ac8); 
 		matriz[6][14] = new Estado(-1);  matriz[6][14].addAction(ac8); 
 		matriz[6][15] = new Estado(-1);  matriz[6][15].addAction(ac8); 
 		matriz[6][16] = new Estado(-1);  matriz[6][16].addAction(ac8); 
 		matriz[6][17] = new Estado(-1);  matriz[6][17].addAction(ac8); 
 		matriz[6][18] = new Estado(-1);  matriz[6][18].addAction(ac8); 
 		matriz[6][19] = new Estado(-1);  matriz[6][19].addAction(ac8); 
 		matriz[6][20] = new Estado(-1);  matriz[6][20].addAction(ac8); 
 		matriz[6][21] = new Estado(-1);  matriz[6][21].addAction(ac8); 
 		matriz[6][22] = new Estado(-1);  matriz[6][22].addAction(ac8); 
 		matriz[6][23] = new Estado(-1);  matriz[6][23].addAction(ac8); 
 		
 		matriz[7][0] = new Estado(-1);   matriz[7][0].addAction(ac8); 
 		matriz[7][1] = new Estado(-1);   matriz[7][1].addAction(ac8); 
 		matriz[7][2] = new Estado(-1);  matriz[7][2].addAction(ac8); 
 		matriz[7][3] = new Estado(-1);  matriz[7][3].addAction(ac8); 
 		matriz[7][4] = new Estado(-1);   matriz[7][4].addAction(ac8); 
 		matriz[7][5] = new Estado(-1);  matriz[7][5].addAction(ac8); 
 		matriz[7][6] = new Estado(-1);   matriz[7][6].addAction(ac8); 
 		matriz[7][7] = new Estado(-1);   matriz[7][7].addAction(ac8); 
 		matriz[7][8] = new Estado(-1);   matriz[7][8].addAction(ac8); 
 		matriz[7][9] = new Estado(8);  matriz[7][9].addAction(ac2); 
 		matriz[7][10] = new Estado(-1); matriz[7][10].addAction(ac8);
 		matriz[7][11] = new Estado(-1); matriz[7][11].addAction(ac8);
 		matriz[7][12] = new Estado(-1); matriz[7][12].addAction(ac8);
 		matriz[7][13] = new Estado(-1); matriz[7][13].addAction(ac8);
 		matriz[7][14] = new Estado(-1); matriz[7][14].addAction(ac8);
 		matriz[7][15] = new Estado(-1); matriz[7][15].addAction(ac8);
 		matriz[7][16] = new Estado(-1); matriz[7][16].addAction(ac8);
 		matriz[7][17] = new Estado(-1); matriz[7][17].addAction(ac8);
 		matriz[7][18] = new Estado(-1);  matriz[7][18].addAction(ac8);
 		matriz[7][19] = new Estado(-1);  matriz[7][19].addAction(ac8);
 		matriz[7][20] = new Estado(-1);  matriz[7][20].addAction(ac8);
 		matriz[7][21] = new Estado(-1); matriz[7][21].addAction(ac8);
 		matriz[7][22] = new Estado(-1); matriz[7][22].addAction(ac8);
 		matriz[7][23] = new Estado(-1); matriz[7][23].addAction(ac8);
 		
 		matriz[8][0] =  new Estado(-1);  matriz[8][0].addAction(ac8);
 		matriz[8][1] =  new Estado(-1);  matriz[8][1].addAction(ac8);
 		matriz[8][2] =  new Estado(-1);  matriz[8][2].addAction(ac8);
 		matriz[8][3] =  new Estado(-1);  matriz[8][3].addAction(ac8);
 		matriz[8][4] =  new Estado(-1);  matriz[8][4].addAction(ac8);
 		matriz[8][5] =  new Estado(-1);  matriz[8][5].addAction(ac8);
 		matriz[8][6] =  new Estado(-1);  matriz[8][6].addAction(ac8);
 		matriz[8][7] =  new Estado(-1);  matriz[8][7].addAction(ac8);
 		matriz[8][8] =  new Estado(-1);  matriz[8][8].addAction(ac8);
 		matriz[8][9] =  new Estado(-1);  matriz[8][9].addAction(ac8);
 		matriz[8][10] = new Estado(-1);  matriz[8][10].addAction(ac8);
 		matriz[8][11] = new Estado(-1);  matriz[8][11].addAction(ac8);
 		matriz[8][12] = new Estado(-1);  matriz[8][12].addAction(ac8);
 		matriz[8][13] = new Estado(-1);  matriz[8][13].addAction(ac8);
 		matriz[8][14] = new Estado(-1);  matriz[8][14].addAction(ac8);
 		matriz[8][15] = new Estado(-1);  matriz[8][15].addAction(ac8);
 		matriz[8][16] = new Estado(-1);  matriz[8][16].addAction(ac8);
 		matriz[8][17] = new Estado(-1);  matriz[8][17].addAction(ac8);
 		matriz[8][18] = new Estado(-1);  matriz[8][18].addAction(ac8);
 		matriz[8][19] = new Estado(-1);  matriz[8][19].addAction(ac8);
 		matriz[8][20] = new Estado(-1);  matriz[8][20].addAction(ac8);
 		matriz[8][21] = new Estado(-1);  matriz[8][21].addAction(ac8);
 		matriz[8][22] = new Estado(-1);  matriz[8][22].addAction(ac8);
 		matriz[8][23] = new Estado(-1);  matriz[8][23].addAction(ac8);
 		
 		Action5 ac5_9 = new Action5(this, "luego del caracter ':' se espera el caracter '='");
 		matriz[9][0] = new Estado(-1);   matriz[9][0].addAction(ac5_9); 
 		matriz[9][1] = new Estado(-1);   matriz[9][1].addAction(ac5_9); 
 		matriz[9][2] = new Estado(-1);  matriz[9][2].addAction(ac5_9);
 		matriz[9][3] = new Estado(-1);  matriz[9][3].addAction(ac5_9);
 		matriz[9][4] = new Estado(-1);   matriz[9][4].addAction(ac5_9);
 		matriz[9][5] = new Estado(-1);  matriz[9][5].addAction(ac5_9);
 		matriz[9][6] = new Estado(-1);   matriz[9][6].addAction(ac5_9);
 		matriz[9][7] = new Estado(-1);   matriz[9][7].addAction(ac5_9);
 		matriz[9][8] = new Estado(-1);   matriz[9][8].addAction(ac5_9);
 		matriz[9][9] = new Estado(-1);  matriz[9][9].addAction(ac2);
 		matriz[9][10] = new Estado(-1); matriz[9][10].addAction(ac5_9);
 		matriz[9][11] = new Estado(-1); matriz[9][11].addAction(ac5_9);
 		matriz[9][12] = new Estado(-1); matriz[9][12].addAction(ac5_9);
 		matriz[9][13] = new Estado(-1); matriz[9][13].addAction(ac5_9);
 		matriz[9][14] = new Estado(-1); matriz[9][14].addAction(ac5_9);
 		matriz[9][15] = new Estado(-1); matriz[9][15].addAction(ac5_9);
 		matriz[9][16] = new Estado(-1); matriz[9][16].addAction(ac5_9);
 		matriz[9][17] = new Estado(-1); matriz[9][17].addAction(ac5_9);
 		matriz[9][18] = new Estado(-1);  matriz[9][18].addAction(ac5_9);
 		matriz[9][19] = new Estado(-1);  matriz[9][19].addAction(ac5_9);
 		matriz[9][20] = new Estado(-1);  matriz[9][20].addAction(ac5_9);
 		matriz[9][21] = new Estado(-1); matriz[9][21].addAction(ac5_9);
 		matriz[9][22] = new Estado(-1); matriz[9][22].addAction(ac5_9);
 		matriz[9][23] = new Estado(-1); matriz[9][23].addAction(ac5_9);
 		
 		Action5 ac5_10 = new Action5(this, "luego del caracter '^' se espera el caracter '='");
 		matriz[10][0] = new Estado(-1);  matriz[10][0].addAction(ac5_10); 
 		matriz[10][1] = new Estado(-1);  matriz[10][1].addAction(ac5_10); 
 		matriz[10][2] = new Estado(-1);  matriz[10][2].addAction(ac5_10); 
 		matriz[10][3] = new Estado(-1);  matriz[10][3].addAction(ac5_10); 
 		matriz[10][4] = new Estado(-1);  matriz[10][4].addAction(ac5_10); 
 		matriz[10][5] = new Estado(-1);  matriz[10][5].addAction(ac5_10); 
 		matriz[10][6] = new Estado(-1);  matriz[10][6].addAction(ac5_10); 
 		matriz[10][7] = new Estado(-1);  matriz[10][7].addAction(ac5_10); 
 		matriz[10][8] = new Estado(-1);  matriz[10][8].addAction(ac5_10); 
 		matriz[10][9] = new Estado(-1);  matriz[10][9].addAction(ac2);   
 		matriz[10][10] = new Estado(-1); matriz[10][10].addAction(ac5_10);
 		matriz[10][11] = new Estado(-1); matriz[10][11].addAction(ac5_10);
 		matriz[10][12] = new Estado(-1); matriz[10][12].addAction(ac5_10);
 		matriz[10][13] = new Estado(-1); matriz[10][13].addAction(ac5_10);
 		matriz[10][14] = new Estado(-1); matriz[10][14].addAction(ac5_10);
 		matriz[10][15] = new Estado(-1); matriz[10][15].addAction(ac5_10);
 		matriz[10][16] = new Estado(-1); matriz[10][16].addAction(ac5_10);
 		matriz[10][17] = new Estado(-1); matriz[10][17].addAction(ac5_10);
 		matriz[10][18] = new Estado(-1); matriz[10][18].addAction(ac5_10);
 		matriz[10][19] = new Estado(-1); matriz[10][19].addAction(ac5_10);
 		matriz[10][20] = new Estado(-1); matriz[10][20].addAction(ac5_10);
 		matriz[10][21] = new Estado(-1); matriz[10][21].addAction(ac5_10);
 		matriz[10][22] = new Estado(-1); matriz[10][22].addAction(ac5_10);
 		matriz[10][23] = new Estado(-1); matriz[10][23].addAction(ac5_10);
 		
 		Action5 ac5_11 = new Action5(this, "La cadena de caracteres debe finalizar con el caracter \"'\"");
 		matriz[11][0] = new Estado(11);    matriz[11][0].addAction(ac2);
 		matriz[11][1] = new Estado(11);    matriz[11][1].addAction(ac2); 
 		matriz[11][2] = new Estado(11);    matriz[11][2].addAction(ac2);
 		matriz[11][3] = new Estado(11);    matriz[11][3].addAction(ac2);
 		matriz[11][4] = new Estado(11);    matriz[11][4].addAction(ac2);
 		matriz[11][5] = new Estado(11);    matriz[11][5].addAction(ac2);
 		matriz[11][6] = new Estado(11);    matriz[11][6].addAction(ac2);
 		matriz[11][7] = new Estado(11);    matriz[11][7].addAction(ac2);
 		matriz[11][8] = new Estado(11);    matriz[11][8].addAction(ac2);
 		matriz[11][9] = new Estado(11);    matriz[11][9].addAction(ac2);
 		matriz[11][10] = new Estado(11);  matriz[11][10].addAction(ac2);
 		matriz[11][11] = new Estado(11);  matriz[11][11].addAction(ac2);
 		matriz[11][12] = new Estado(11);  matriz[11][12].addAction(ac2);
 		matriz[11][13] = new Estado(11);  matriz[11][13].addAction(ac2);
 		matriz[11][14] = new Estado(11);  matriz[11][14].addAction(ac2);
 		matriz[11][15] = new Estado(11);  matriz[11][15].addAction(ac2);
 		matriz[11][16] = new Estado(11);  matriz[11][16].addAction(ac2);
 		matriz[11][17] = new Estado(-1);  matriz[11][17].addAction(ac10);
 		matriz[11][18] = new Estado(11);  matriz[11][18].addAction(ac2);
 		matriz[11][19] = new Estado(12);  
 		matriz[11][20] = new Estado(11);  matriz[11][20].addAction(ac2);
 		matriz[11][21] = new Estado(11);  matriz[11][21].addAction(ac2);
 		matriz[11][22] = new Estado(11);  matriz[11][22].addAction(ac2);
 		matriz[11][23] = new Estado(11);  matriz[11][23].addAction(ac5_11);
 		
 		//TODO debe saltar error si no hay un signo mas
 		Action5 ac5_12 = new Action5(this, "Falta el caracter '+' para generar caracteres multilinea");
 		matriz[12][0] = new Estado(-1);     matriz[12][0].addAction(ac5_12);
 		matriz[12][1] = new Estado(-1);     matriz[12][1].addAction(ac5_12);
 		matriz[12][2] = new Estado(11);     
 		matriz[12][3] = new Estado(-1);     matriz[12][3].addAction(ac5_12);
 		matriz[12][4] = new Estado(-1);     matriz[12][4].addAction(ac5_12);
 		matriz[12][5] = new Estado(-1);     matriz[12][5].addAction(ac5_12);
 		matriz[12][6] = new Estado(-1);     matriz[12][6].addAction(ac5_12);
 		matriz[12][7] = new Estado(-1);     matriz[12][7].addAction(ac5_12);
 		matriz[12][8] = new Estado(-1);     matriz[12][8].addAction(ac5_12);
 		matriz[12][9] = new Estado(-1);     matriz[12][9].addAction(ac5_12);
 		matriz[12][10] = new Estado(-1);   matriz[12][10].addAction(ac5_12);
 		matriz[12][11] = new Estado(-1);   matriz[12][11].addAction(ac5_12);
 		matriz[12][12] = new Estado(-1);   matriz[12][12].addAction(ac5_12);
 		matriz[12][13] = new Estado(-1);   matriz[12][13].addAction(ac5_12);
 		matriz[12][14] = new Estado(-1);   matriz[12][14].addAction(ac5_12);
 		matriz[12][15] = new Estado(-1);   matriz[12][15].addAction(ac5_12);
 		matriz[12][16] = new Estado(-1);   matriz[12][16].addAction(ac5_12);
 		matriz[12][17] = new Estado(-1);   matriz[12][17].addAction(ac5_12);
 		matriz[12][18] = new Estado(-1);   matriz[12][18].addAction(ac5_12);
 		matriz[12][19] = new Estado(12);
 		matriz[12][20] = new Estado(12); 
 		matriz[12][21] = new Estado(-1);   matriz[12][21].addAction(ac5_12);
 		matriz[12][22] = new Estado(-1);   matriz[12][22].addAction(ac5_12);
 		matriz[12][23] = new Estado(-1);   matriz[12][23].addAction(ac5_12);


 		this.setMatriz(matriz);
 		
	}
	
	private void initMatrixTypes(){
	
		_types = new int[13][24];
		for(int i = 0; i<13; i++)
			for(int j=0; j<23; j++)
				_types[i][j] = -1;
		
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
		_types[0][10] = COMP;
		_types[0][11] = '(';
		_types[0][12] = ')';
		_types[0][13] = '{';
		_types[0][14] = '}';
		_types[0][15] = ',';
		_types[0][16] = ';';
		_types[0][17] = STR;
		_types[0][18] = '.';
		_types[0][23] = EOF;
		_types[3][8] = COMP;
		
	}
	
	
	/**
	 * Retorna el siguiente estado del grafo y realiza las acciones semanticas
	 * correspondientes
	 * @param state estado anterior
	 * @return nuevo estado
	 * @throws LexicalException
	 */
	private int transition(int state) throws LexicalException {
		int colum = this.getColumn((char) _actualChar);
		Estado st =_matrizTransicion[state][colum];
		
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
			int col = this.getColumn(_actualChar);
			int[] d = _types[preState];
			int newKind = _types[preState][col];
			if(newKind != -1)
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
				System.err.println(e.toString());
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
	

	public void addErrorMessage(String message, boolean war) {
		ParserError error = new ParserError(message, ParserError.TYPE_LEXICO, _token.row, war);
		errorManager.addError(error);
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
		tupla.setValue("clase", _token.kind);
		_tds.addTupla(tupla);
		
	}

	public boolean hayError() {
		return _errors.size() > 0;
	}

	public void setReaderOrigin(String source) {
		try {
			this._reader = new BufferedCharStream(new File(source));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setTypeOfToken(String tipo) {
		TuplaTablaSimbolos tupla = _tds.getTupla(_token.sval);
		tupla.setValue("tipo", tipo);
		
	}

	public void errorCaracterInvalido(boolean b) {
		this.initToken();
		this.addErrorMessage("el caracter '"+(char)_actualChar+"' es un caracter invalido", false);
		
	}

}
