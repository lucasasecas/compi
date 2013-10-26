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

Programa: Declaraciones Sentencias
	  | error {guardarError("error sintactico no identificado");} 
	  ;

Declaraciones: Declaraciones Declaracion 
		| Declaracion
		;

Declaracion: DeclaracionVariables 
		| DeclaracionFunciones
		;

DeclaracionVariables: Tipo ListaVariables ';' {agregarRegla("declaracion de variables");} 
			  ;
			  
Tipo: UINT
    ;

ListaVariables: ListaVariables ',' ID
		  | ID
		  ;
		  
DeclaracionFunciones: FUNCTION BEGIN CuerpoFuncion END {agregarRegla("Declaracion de funcion");}
			  | FUNCTION BEGIN error{guardarError("el cuerpo de la funcion esta mal definido");} END
			  | FUNCTION error {guardarError("falta el begin");} END
			  ;
			  
		   
CuerpoFuncion: DeclaracionesFuncion InstanciasFuncion
		 | DeclaracionesFuncion error{guardarError("error en una de las instancias de la funcion");} 
		 | error{guardarError("error en las declaraciones de funcion");} InstanciasFuncion
		 ;

DeclaracionesFuncion: DeclaracionesFuncion DeclaracionVariables
			  | DeclaracionVariables
			  ;
			  
InstanciasFuncion : PuntoEntrada 
                  | InstanciasFuncion PuntoEntrada
                  | InstanciasFuncion Sentencia  
			;
			
PuntoEntrada: ID ':' Sentencia 
		;
		
		    
Sentencias: Sentencias Sentencia 
	    |Sentencia 
	    | Sentencias error{guardarError("Sentencia mal declarada o invalida");} Sentencia
	    ;
	    
Sentencia: Asignacion ';' {agregarRegla("Asignacion");}
	   | Seleccion  {agregarRegla("Sentencia If");}
	   | Iteracion {agregarRegla("Iteracion For");}
	   | SalidaPantalla ';' {agregarRegla("Salida por pantalla Print");}
	   | Return ';' {agregarRegla("Sentencia Return");}
	   | LlamadaFn ';' 
	   
	   ;
	   		    						  		 
Asignacion: AsigIzq ASIGN Expresion {
			pila.push($2.sval);	
						}
	    | AsigIzq ASIGN error{guardarError("se produjo error en la asignacion");}
	    ;

AsigIzq: ID {pila.push($1.sval);}	    
	 ;
	 
Return: RETURN '(' Expresion ')' 
	| RETURN '(' error{guardarError("sentencia return mal definida");} ';'
	;
	    
Expresion: Expresion '+' Termino {
			pila.push('+');
		}
	   | Expresion '-' Termino {
	   		pila.push('-');
   		}
	   | Termino
	   | Expresion error {guardarError("Expresion mal definida");}';'
	   ;

Termino: Termino '*' Factor {
		pila.push('*');
	   }
	 | Termino '/' Factor {
	 	pila.push('/');
	 	}
	 | Factor 
	 | Termino '*' error{guardarError("termino mal definico");} ';'
	 | Termino '/' error{guardarError("termino mal definido");} ';'
	 ;

Factor: ID {$$ = $1;
		pila.push($1.sval);}
	| CTE {$$ = $1;
		 pila.push($1.sval);}
	| LlamadaFn
	;

LlamadaFn: ID '(' ')' {agregarRegla("llamada a funcion");}
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
			pila.push($2.sval);
		}
		;

		
Iteracion: CabeceraIteracion BloqueSentencias {pila.nuevoSalto("BI"); pila.setSaltoPrevio(pila.getLastFlag()); pila.setSaltoPrevio(0);}
	   | CabeceraIteracion error ';'
	   ;
	   
CabeceraIteracion: FOR '(' Asignacion ';' Comparacion2 ')' {pila.nuevoSalto("BF");}
		     | FOR '(' Asignacion ';' error ')'
		     ;
			
Comparacion2: Comp2Izq COMP Expresion {pila.push($2.sval);}
		| Comp2Izq COMP error{guardarError("del lado derecho de la comparacion debe ir una expresion");} ';'
		| Comp2Izq error ';'
		;

Comp2Izq: ID {pila.setFlag(); pila.push($1.sval);}		
;
		 			 	   
SalidaPantalla: PRINT '(' STR ')' {pila.push($3.sval); pila.push($1.sval);}
		  | PRINT '(' error{guardarError("falta cadena de caracteres o se escrio mal");} ')'
		  | PRINT '(' STR error{guardarError("la sentencia print se debe cerrar con el caracter especial ')'");} ';'
		  ;

 %%

public Pila pila;
Vector<Vector<String>> lista;
AnalizadorLexico an; 
Vector<String> errores;
Vector<String> reglas;
TablaSimbolo _tds;
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


public Parser(){
	_tds = new TablaSimbolo();
	an = new AnalizadorLexico(new File("sources/source.txt"), _tds);
	lista = new Vector<Vector<String>>();
	errores = new Vector<String>();
	reglas = new Vector<String>();
	pila = new Pila();
//	yydebug = true;
}

public Parser(String string) {
	super();
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


private void agregarRegla(String s){
	reglas.add(s);
}

private void yyerror(String string) {

}

public void imprimirTablaDeSimbolos() {
	_tds.imprimirTablaSimbol();
	
}
public void imprimirResultados(){
	System.out.println(pila.toString());
	this.imprimirTokens();
	this.imprimirReglas();
	this.imprimirTablaDeSimbolos();
	this.imprimirErroresLexicos();
	this.imprimirErroresSintacticos();
}
public void getErroresLexicos() {
	
	an.printErrors();
}
