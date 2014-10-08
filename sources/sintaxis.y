%{
	package as_Parser;
	import java.util.Vector;

	import java.util.List;
	import unicen.compiladores.gui.ErrorManager;
	import unicen.compiladores.gui.ParserError;
	import unicen.compiladores.gui.Sentencia;
	import unicen.compiladores.gui.SentenciaManager;
	import Utils.TablaSimbolo;
	import al_Main.AnalizadorLexico;
	import Utils.TuplaTablaSimbolos;
%}

%token ID
%token REGISTRO
%token ENTERO
%token ENTEROL
%token ASIGN
%token CTE
%token SI
%token SINO
%token COMP
%token ENTONCES
%token MIENTRAS
%token ITERAR
%token IMPRIMIR
%token STR

%right ':'
%right ASIGN
%left '+' '-' 
%left '/' '*'

%nonassoc IT
%nonassoc SINO

%%

Programa: Declaraciones Sentencias
	|	Declaraciones error {yyerror("Se han encontrado errores en las sentencias");} ';'
	;

Declaraciones:  Declaraciones Declaracion 
	|	Declaracion
	;
			 
 Declaracion:	Declaracion_reg {addUse("registro", $1.list);}
	|	Declaracion_var {addUse("variable", $1.list);}
	;
			
Declaracion_reg:	REGISTRO Cuerpo_reg Lista_var_reg ';' {$$ = $3;}
	|	REGISTRO Cuerpo_reg error {yyerror("se ha encontrado un error en la declaracion de variables del registro");}';'
	;

Lista_var_reg: Lista_var_reg ',' ID {$$.list.addAll($1.list); $$.list.add($3);}
	| ID {$$.list.add($1);}
	;

Cuerpo_reg:		'{' Declaraciones_reg '}' {$$ = $2; addUse("variable interna registro", $2.list);} 
	|	'{' Declaraciones_reg error {yyerror("no de encontro el caracter '}'"); } ';'
	;
		  
Declaraciones_reg: 	Declaraciones_reg Declaracion_unit {$$.list.addAll($1.list);$$.list.add($2);}
				 |	Declaracion_unit {$$.list.add($1);}
				 ;

Declaracion_unit:	Tipo ID ';' {setType($1.sval, $2.sval); $$ = $2;}
				;
 
Tipo:	ENTERO {$$ = $1;}
	|	ENTEROL {$$ = $1;}
	;

Lista_var: 	Lista_var ',' ID {$$.list.addAll($1.list); $$.list.add($3);}
		 |	ID {$$.list.add($1);}
		 ;

Declaracion_var: Tipo Lista_var ';' {setType($1.sval, $2.list);$$ = $2;}
	;
	
Sentencias: Sentencias Sentencia
	|	Sentencia
	;
	
Sentencia: Asignacion {nuevaSentencia($1,"Asignacion");}
	|	Seleccion {nuevaSentencia($1,"Sentencia 'si'");}
	|	Iteracion {nuevaSentencia($1,"sentencia 'mientras'");}
	|	Salida {nuevaSentencia($1,"Sentencia 'imprimir'");}
	;
Asignacion : AsignacionIzq ASIGN Expresion ';' {$$ = $2;}
	| AsignacionIzq ASIGN Expresion error {yyerror("Sentencia debe finalizar con caracter ';'");} ';'
	| AsignacionIzq error {yyerror("Se esperaba el simbolo de asignacion ':='");} ';'
	;
AsignacionIzq: ID
	;
Expresion: Expresion '+' Termino;
	|	Expresion '-' Termino
	|	Termino
	|	Expresion '+' error{yyerror("Termino incorrecto");} ';'
	|	Expresion '-' error{yyerror("Termino incorrecto");} ';'
	;
Termino: Termino '*' Factor
	|	Termino '/' Factor
	|	Factor
	| 	Termino '*' error{yyerror("Operando invalido");} ';'
	|	Termino '/' error{yyerror("Operando invalido");} ';'
	;
Factor: ID
	|	CTE {verificarRango($1.sval);}
	|	CampoReg
	|	CTENeg
	;
	
CTENeg: '-' CTE {setToNegative($2);}
	;

CampoReg: ID '.' ID
	| ID '.' error{yyerror("Campo de registro invalido");} ';'
	;

Seleccion: CabeceraSi BloqueEntonces SINO BloqueSino {$$ = $1;}
	|	CabeceraSi BloqueEntonces %prec IT {$$ = $1;}
	;
CabeceraSi: SI '(' Comparacion ')' ENTONCES; {$$ = $1;}
	| SI '(' Comparacion ')' error {yyerror("Falta la palabra reservada 'entonces'");} ';'
	| SI '(' Comparacion error {yyerror("No se encontro el caracter ')'"); } ';'
	| SI '(' error {yyerror("error en la comparacion')'");} ';'
	| SI error {yyerror("no se encontro el caracter '('");} ';'
	;
Comparacion: Expresion COMP Expresion 
	;
BloqueEntonces: Sentencia | BloqueSentencias
	;
BloqueSentencias: '{' Sentencias '}'
	;
BloqueSino: BloqueSentencias
	| Sentencia
	;
	
Iteracion: CabeceraIteracion BloqueIteracion {$$ = $1;}
	; 
CabeceraIteracion: MIENTRAS '(' Comparacion ')' ITERAR {$$ = $1;}
	| MIENTRAS '(' Comparacion ')' error {yyerror("Falta la palabra reservada iterar");} ';'
	| MIENTRAS '(' Comparacion error {yyerror("Falta el caracter ')'");} ';'
	| MIENTRAS '(' error {yyerror("Error en la comparacion");} ';'
	| MIENTRAS error{yyerror("La sentencia 'mientras' debe ser seguida del caracter '('");} ';'
	;
BloqueIteracion: Sentencia 
	|	BloqueSentencias
	;

Salida: IMPRIMIR '(' STR ')' ';' {$$ = $1;}
	| IMPRIMIR '(' STR ')' error{yyerror("la sentencia debe finalizar con el caracter ';'");}
	| IMPRIMIR '(' STR error {yyerror("Falta el caracter ')'");} ';'
	| IMPRIMIR '(' error {yyerror("Se esperaba una cadena de caracteres");} ';'
	| IMPRIMIR error {yyerror("La sentencia 'imprimir' debe ser seguida del caracter '('");} ';'
	;
%%

TablaSimbolo tds;
AnalizadorLexico al;
Vector<ParserVal> tokens = new Vector<ParserVal>();
ErrorManager errorManager;
SentenciaManager sentenciasManager;
public static double MAX = Math.pow(2, 15)-1; 

private int yylex(){
	 yylval = new ParserVal();
	 int t = al.getNextToken(yylval);
	 System.out.println(yylval.kind+"--"+yylval.sval);
	 tokens.add(yylval.Clone());
	 return t;
	}

public Parser(TablaSimbolo tds, AnalizadorLexico al, ErrorManager errorManager, SentenciaManager sentenciasManager){
    yydebug = true;
	this.tds = tds;
	this.al = al;
	this.errorManager = errorManager;
	this.sentenciasManager = sentenciasManager;
	}
	
private void yyerror(String string) {
	ParserError error = new ParserError(string, ParserError.TYPE_SINTACTICO, yylval.row, false);
	errorManager.addError(error);
}

public TablaSimbolo getTablaDeSimbolos() {
	return tds;
}

private void setToNegative(ParserVal val_peek) {
	TuplaTablaSimbolos tupla = tds.getTupla(val_peek.sval);
	tupla.setValue("negativo", true);
	
}

public void nuevaSentencia(ParserVal lval,String sentencia){
	this.sentenciasManager.addSentence(
		new Sentencia(
			lval.row,
			sentencia
		)
	);
}

public void run(String fileName)
{
	al.setReaderOrigin(fileName);
    yyparse();
}

public Vector<ParserVal> getTokens() {
	// TODO Auto-generated method stub
	return tokens;
}

private void setType(String sval, List<ParserVal> list) {
		for(int i=0; i<list.size(); i++){
			setType(sval, list.get(i).sval);
		}
}

private void setType(String tipo, String var){
	tds.getTupla(var).setValue("tipo", tipo);
}

private void verificarRango(String a){
}

private void addUse(String use, List<ParserVal> ids){
	for(int i=0; i<ids.size(); i++){
		addUse(use, ids.get(i).sval);
	}
}

private void addUse(String use, String id){
	tds.getTupla(id).setValue("uso", use);
}

