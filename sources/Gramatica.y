%{
package as_Parser;
import java.io.*;
import al_Main.AnalizadorLexico;
import java.util.Vector;
import Utils.TablaSimbolo;
import Utils.TuplaTablaSimbolos;
%}

 /*YACC Declarations*/ 
%token ID
%token UINT
%token FUNCTION
%token BEGIN
%token END
%token RETURN
%token CTE
%token ASIGN IF ELSE COMP THEN FOR STR PRINT 
%right ':'
%left ASIGN
%left '+' '-' 
%left '/' '*'

%nonassoc IT
%nonassoc ELSE

%%

Programa: Declaraciones Sentencias /*
/*	  | Declaraciones error { errores.add("Error sintactico: No se declararon sentencias a este programa");} 
	  | Sentencias {errores.add("Error sintactico: no se declararon variables ni finciones para este programa");}*/
	  
	  ;

Declaraciones: Declaraciones Declaracion 
		| Declaracion
		;

Declaracion: DeclaracionVariables 
		| DeclaracionFunciones
		;

DeclaracionVariables: Tipo ListaVariables ';' {agregarRegla("declaracion de variables");} 
			  | Tipo error {guardarError("declaracion de variables incorrecta"); } ';' 
			 ;
			  
Tipo: UINT
    ;

ListaVariables: ListaVariables ',' ID
		  | ID
		  ;
		  
DeclaracionFunciones: FUNCTION BEGIN CuerpoFuncion END {agregarRegla("Declaracion de funcion");}
			  | FUNCTION BEGIN error{guardarError("cuerpo de funcion mal declarado");} END	
			  ;
			  
		   
CuerpoFuncion: DeclaracionesFuncion InstanciasFuncion
		 ;

DeclaracionesFuncion: DeclaracionesFuncion DeclaracionVariables
			  | DeclaracionVariables
			  ;
			  
InstanciasFuncion: InstanciasFuncion InstanciaFuncion
			| InstanciaFuncion
			;
			
InstanciaFuncion: CabeceraEntrada Sentencias
		    ;
		    
CabeceraEntrada: ID ':' 
		   ;    
		    
Sentencias: Sentencias Sentencia
	    | Sentencia
	    | error {guardarError("sentencia mal declarada");}';'
	    ;
	    
Sentencia: Asignacion ';' {agregarRegla("Asignacion");}
	   | Seleccion  {agregarRegla("Sentencia If");}
	   | Iteracion {agregarRegla("Iteracion For");}
	   | SalidaPantalla ';' {agregarRegla("Salida por pantalla Print");}
	   | Return ';' {agregarRegla("Sentencia Return");}
	   | Asignacion error {guardarError("las sentencias tipo asugnacion deben terminar con el caracter ';'");} ';'
	   ;
	   		    						  		 
Asignacion: ID ASIGN Expresion 
	    ;

Return: RETURN '(' ')' 
	| RETURN error {guardarError("no se encontro el caracter ')' ");} ';'
	;
	    
Expresion: Expresion '+' Termino
	   | Expresion '-' Termino
	   | Termino
	   | Expresion '+' error {guardarError("termino mal definio");} ';'
	   | Expresion '-' error {guardarError("termino mal definio");} ';'
	   ;

Termino: Termino '*' Factor
	 | Termino '/' Factor
	 | Factor 
	 ;

Factor: ID
	| CTE
	| LlamadaFn
	;

LlamadaFn: ID '(' ')' {agregarRegla("llamada a funcion");}
	   ;	 

Seleccion: IF '(' Comparacion ')' THEN BloqueThen ELSE BloqueElse
	   | IF '(' Comparacion ')' THEN BloqueThen %prec IT 
	   ;

BloqueThen: BloqueSentencias
	    ;
    	
BloqueElse: BloqueSentencias
	    ;
	    
BloqueSentencias: Sentencia
	   	    | BEGIN Sentencias END
		   ;


	    
Comparacion: Expresion COMP Expresion 
		;

		
Iteracion: CabeceraIteracion BloqueSentencias
	   ;
	   
CabeceraIteracion: FOR '(' Asignacion ';' Comparacion2 ')'
                 ;
			
Comparacion2: ID COMP Expresion
		;
		 			 	   
SalidaPantalla: PRINT '(' STR ')' 
		  | PRINT '(' STR error {guardarError("falta el caracter ')'");} ';'
		  | PRINT error{guardarError("falta el caracter '('");} ';'
		  ;

 %%

Vector<String> lista;
AnalizadorLexico an; 
Vector<String> errores;
Vector<String> reglas;
TablaSimbolo _tds;
private int yylex(){
 int t = an.getNextToken(yylval);
String a = new String(yyname[t]);
 if(t != -1)
	 lista.add(yyname[t]);
 return t;
}


public Parser(){
	_tds = new TablaSimbolo();
	an = new AnalizadorLexico(new File("sources/source.txt"), _tds);
	lista = new Vector<String>();
	errores = new Vector<String>();
	reglas = new Vector<String>();
//	yydebug = true;
}

public void run(){
	yyparse();
}

public Vector<String> getErroresSintacticos(){
	return errores;
}

public Vector<String> getReglas(){
	return reglas;
}

private void guardarError(String error){
	errores.add("Error sintactico (Linea "+yylval.getRow()+"): "+error);
}

private void agregarRegla(String s){
	reglas.add(s);
}

private void yyerror(String string) {
}

public void imprimirTablaDeSimbolos() {
	_tds.imprimirTablaSimbol();
	
}

public void getErroresLexicos() {
	
	an.printErrors();
}

