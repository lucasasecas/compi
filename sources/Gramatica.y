%{
package as_Parser;
import java.util.Stack;
import java.io.*;
import al_Main.AnalizadorLexico;
import java.util.Vector;
import Utils.TablaSimbolo;
import Utils.TuplaTablaSimbolos;
import Utils.Pila;
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
%right ASIGN
%left '+' '-' 
%left '/' '*'

%nonassoc IT
%nonassoc ELSE

%%

Programa: Declaraciones {
					alcance = "Main";
					push("MAIN");
					} Sentencias
	  | error {guardarError("Error en delcaracion");} Sentencias
	  ;

Declaraciones: DeclaracionesVariables DeclaracionesFunciones
		 | DeclaracionesVariables
		 
		 | DeclaracionesFunciones DeclaracionesVariables error {guardarError("Las declaraciones de variables estan despues de las declaraciones de funciones");} 
		 | DeclaracionesFunciones error{guardarError("No se encontro declaraciones de variables ");}	
		;

DeclaracionesVariables: DeclaracionesVariables DeclaracionVariables {
			for(ParserVal a : $2.list){
				if(_tds.isDeclared(a.sval, "Main"))
					guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
				tupla.set_scope("Main");
			}
		}
		| DeclaracionVariables {
			for(ParserVal a : $1.list){
				if(_tds.isDeclared(a.sval, "Main"))
					guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
				tupla.set_scope("Main");
			}
		}
		;

DeclaracionVariables: Tipo ListaVariables ';' {
				for(ParserVal a : $2.list){
	  				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
	  				tupla.set_type($1.sval);
	  				tupla.set_use("Variable");
				}
				$$ = $2;				  
				agregarRegla("declaracion de variables");} 	  
			  ;
			  
Tipo: UINT {$$ = $1;}
    ;

ListaVariables: ListaVariables ',' ID {$1.addToList($3); $$ = $1;} 
		  | ID {$$.addToList($1);}
		  ;
DeclaracionesFunciones: DeclaracionesFunciones DeclaracionFunciones
			    | DeclaracionFunciones
			    ;
			    
DeclaracionFunciones: FUNCTION BEGIN CuerpoFuncion END {setPEFlag(); agregarRegla("Declaracion de funcion");alcance = "Main";}
			  | FUNCTION BEGIN error{guardarError("el cuerpo de la funcion esta mal definido");} END
			  | FUNCTION error {guardarError("falta el begin");} END
			  ;
			  
		   
CuerpoFuncion: DeclaracionesFuncion InstanciasFuncion
		 | DeclaracionesFuncion error{guardarError("error en una de las instancias de la funcion");} 
		 | error{guardarError("error en las declaraciones de funcion");} InstanciasFuncion
		 ;

DeclaracionesFuncion:DeclaracionesFuncion DeclaracionVariables {
				for(ParserVal a : $2.list){
					if(_tds.isDeclared(a.sval, "funcion"))
						guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
					_tds.changeScope(a.sval);
				}
			  }
			  | DeclaracionVariables {	
				for(ParserVal a : $1.list){
					if(_tds.isDeclared(a.sval, "funcion"))
						guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
					_tds.changeScope(a.sval);
				}
			  }
			  ;

InstanciasFuncion : InstanciasFuncion InstanciaFuncion 
			| InstanciaFuncion 
			;
                  
InstanciaFuncion: PuntoEntrada 
                | Sentencia  
			;
			
PuntoEntrada: CabeceraPE Sentencia
		;

CabeceraPE: ID ':'  {
			setPEFlag();
			push($1.sval);
			push("HEADER");
			TuplaTablaSimbolos tupla = _tds.getTupla($1.sval);
			if(_tds.isDeclared($1.sval, "Main"))
					guardarErrorSem("El identificador "+$1.sval+" ya ha sido declarado");
			tupla.set_use("Punto de entrada");
			tupla._scope = "Main";
		}
	    ;

Sentencias: Sentencias Sentencia 
	    |Sentencia 
	    | Sentencias error{guardarError("Sentencia mal declarada o invalida");} Sentencia
	    ;
	    
Sentencia: Asignacion ';' {agregarRegla("Asignacion");}
	   | Seleccion  {agregarRegla("Sentencia If");}
	   | Iteracion {agregarRegla("Iteracion For");}
	   | SalidaPantalla ';' {agregarRegla("Salida por pantalla Print");}
	   | Return ';'
	   | LlamadaFn ';'  
	   ;
	   		    						  		 
Asignacion: AsigIzq ASIGN Expresion {
				push($2.sval);	
				$$ = $1;
			}
	    | AsigIzq ASIGN error{guardarError("se produjo error en la asignacion");}
	    ;

AsigIzq: ID {push($1.sval);
		verificarDeclaracion($1.sval);
		$$ = $1;
	}	    
	 ;
	 
Return: RETURN '(' Expresion ')' {push("RETURN");}
	| RETURN '(' error{guardarError("sentencia return mal definida");} ';'
	;
	    
Expresion: Expresion '+' Termino {
			push('+');
		}
	   | Expresion '-' Termino {
	   		push('-');
   		}
	   | Termino
	   | Expresion error {guardarError("Expresion mal definida");}';'
	   ;

Termino: Termino '*' Factor {
		push('*');
	   }
	 | Termino '/' Factor {
	 	push('/');
	 	}
	 | Factor 
	 | Termino '*' error{guardarError("termino mal definico");} ';'
	 | Termino '/' error{guardarError("termino mal definido");} ';'
	 ;

Factor: ID {
		$$ = $1;
		push($1.sval);
		verificarDeclaracion($1.sval);
	}
	| CTE {
		$$ = $1;
		push($1.sval);}
	| LlamadaFn 
	;

LlamadaFn: ID '(' ')' {
		agregarRegla("llamada a funcion");
		push($1.sval);
		push("CALL");
		}
	   | ID '(' error{guardarError("falta el caracter de cierre ')'. Recuerde que las llamadas a funcion no lleva argumentos");} ';'
	   ;	 

Seleccion: CabeceraIf BloqueThen ELSE
	    {
	    	pila.setSaltoPrevio(2);
	    	pila.nuevoSalto("BI");
	    }
	     BloqueElse {pila.setSaltoPrevio(0);}
	   | CabeceraIf BloqueThen {pila.setSaltoPrevio(0);} %prec IT 
	   ;

CabeceraIf: IF '(' Comparacion ')' THEN 
	    {
	    	pila.nuevoSalto("BF");
	    }	
	    | IF '(' error{guardarError("se produjo un error en la comparacion");} ')' THEN
	    | IF '(' Comparacion ')' error{guardarError("no se encontro la palabra reservada \"then\"");} ';'
	    ;

BloqueThen: BloqueSentencias
	    ;
    	
BloqueElse: BloqueSentencias
	    ;
	    
BloqueSentencias: Sentencia 
	   	    | BEGIN Sentencias END
	   	    | BEGIN error{guardarError("error sintactico dentro del bloque de codigo");} END
	   	    | BEGIN Sentencias error{guardarError("no se encontro el cierre de bloque de codigo \"end\"");} ';' 
		   ;


	    
Comparacion: Expresion COMP Expresion {
			push($2.sval);
		}
		;

		
Iteracion: CabeceraIteracion BloqueSentencias {
			push(varFor);
			push(varFor);
			push("1");
			push("+");
			push("=");
			pila.nuevoSalto("BI");
			pila.setSaltoPrevio(pila.getLastFlag());
			pila.setSaltoPrevio(0);
		}
	   | CabeceraIteracion error ';'
	   ;
	   
CabeceraIteracion: FOR '(' Asignacion ';' Comparacion2 ')' {pila.nuevoSalto("BF"); varFor = $3.sval;}
		     | FOR '(' Asignacion ';' error ')'
		     ;
			
Comparacion2: Comp2Izq COMP Expresion {$$ = $1; push($2.sval);}
		| Comp2Izq COMP error{guardarError("del lado derecho de la comparacion debe ir una expresion");} ';'
		| Comp2Izq error ';'
		;

Comp2Izq: ID {$$ = $1; pila.setFlag(); push($1.sval);
			verificarDeclaracion($1.sval);
		}		
;
		 			 	   
SalidaPantalla: PRINT '(' STR ')' {push($3.sval); push("PRINT");}
		  | PRINT '(' error{guardarError("falta cadena de caracteres o se escrio mal");} ')'
		  | PRINT '(' STR error{guardarError("la sentencia print se debe cerrar con el caracter especial ')'");} ';'
		  ;

 %%

public Pila pila;
Vector<Vector<String>> lista;
AnalizadorLexico an; 
Vector<String> errores;
Vector<String> erroresSem;
Vector<String> intermedio;
Vector<String> reglas;
TablaSimbolo _tds;
String alcance = "funcion";
boolean pEFlag = false;
String varFor = "";

private int yylex(){
 yylval = new ParserVal();
 int t = an.getNextToken(yylval);
 Vector<String> pair = new Vector<String>();
 pair.add(yyname[yylval.kind]);
 pair.add(yylval.sval);
 if(t != -1)
	 lista.add(pair);
 return t;
}


public Parser(TablaSimbolo tds){
	_tds = tds;
	an = new AnalizadorLexico(new File("sources/source.txt"), _tds);
	lista = new Vector<Vector<String>>();
	errores = new Vector<String>();
	reglas = new Vector<String>();
	intermedio = new Vector<String>();
	erroresSem = new Vector<String>();
	pila = new Pila();
//	yydebug = true;
}

public Parser(String string, TablaSimbolo tds) {
	this(tds);
	an = new AnalizadorLexico(new File(string), _tds);
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

public void imprimirReglas(){
	
	System.out.println("LISTA REGLAS ENCONTRADAS");
	for(int i = 0; i<reglas.size();i++)
		System.out.println(reglas.get(i));
	System.out.println();
	System.out.println();
}

private void guardarError(String error){
	errores.add("Error sintactico (Linea "+yylval.row+"): "+error);
	
}

private void guardarErrorSem(String error){
	erroresSem.add("Error sintactico (Linea "+yylval.row+"): "+error);
	
}


private void imprimirTokens(){
	System.out.println("LISTA DE TOKENS ");
	for(int i = 0; i<lista.size();i++)
	{
		System.out.println(lista.get(i).get(0)+ " --> " +lista.get(i).get(1));
	}
		
	System.out.println();
	System.out.println();
}

public void imprimirErroresLexicos(){
	System.out.println("ERRORES LEXICOS");
	an.printErrors();
	System.out.println();
	System.out.println();
}

public void imprimirErroresSintacticos(){
	System.out.println("ERRORES SINTACTICOS");
	for(int i = 0; i<errores.size();i++)
		System.out.println(errores.get(i));
	System.out.println();
	System.out.println();
}

public void imprimirErroresSemanticos(){
	System.out.println("ERRORES SEMANTICOS");
	for(int i = 0; i<errores.size();i++)
		System.out.println(erroresSem.get(i));
	System.out.println();
	System.out.println();
}


public void setPEFlag(){
	if(pEFlag)
		push(":");
	else
		pEFlag = true;
}

private void agregarRegla(String s){
	reglas.add(s);
}

private void yyerror(String string) {

}

public void imprimirTablaDeSimbolos() {
	_tds.imprimirTablaSimbol();
	
}
public void imprimirResultados(){
	
	this.imprimirTokens();
	this.imprimirCodigoIntermedio();
	this.imprimirTablaDeSimbolos();
	this.imprimirErroresLexicos();
	this.imprimirErroresSintacticos();
	this.imprimirErroresSemanticos();
}
public void getErroresLexicos() {
	
	an.printErrors();
}

public boolean hayError(){
	return an.hayError() ||  errores.size() > 0;
}

public void verificarDeclaracion(String val){
	if(alcance.equals("funcion")){	
		if(!_tds.isDeclared(val, "funcion") && !_tds.isDeclared(val, "Main"))
			guardarErrorSem("La variable '"+val+"' no ha sido declarada");
	}
	else
		if(!_tds.isDeclared(val, "Main"))
			guardarErrorSem("La variable '"+val+"' no ha sido declarada");
		
	
}

public void push(String a ){
	pila.push(a);
	intermedio.add(new String("Linea "+yylval.row+": "+a));
}

public void push(char a ){
	pila.push(a);
	intermedio.add(new String("Linea "+yylval.row+": "+a));
}
