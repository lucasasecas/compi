%{
	package as_Parser;
	import java.util.Vector;

	import gc_Assembler.NodoArbol;
	import java.util.List;
	import unicen.compiladores.gui.ErrorManager;
	import unicen.compiladores.gui.ParserError;
	import unicen.compiladores.gui.Sentencia;
	import unicen.compiladores.gui.SentenciaManager;
	import Utils.TablaSimbolo;
	import al_Main.AnalizadorLexico;
	import Utils.TuplaTablaSimbolos;
	import unicen.compiladores.gui.utils.TokensDictionary;
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

Programa: Declaraciones Sentencias {
			ParserVal a= $2;
			raiz=a.sref;
		}
	|Declaraciones {yyerror("No se encontraron declaraciones");}
	;

Declaraciones:  Declaraciones Declaracion 
	|	Declaracion
	;
			 
 Declaracion:	Declaracion_reg {nuevaSentencia($1, "Declaracion de registro");}
	|	Declaracion_var {nuevaSentencia($1, "Declaracion de variable");}
	;
			
Declaracion_reg:	REGISTRO Cuerpo_reg Lista_var_reg ';' {$$ = $3; setRecordInnerVars($2.list, $3.list);} 
	|	REGISTRO error {yyerror("Declaracion de variables internas al registro invalida");} Lista_var_reg ';' {$$ = $3;}
	;

Lista_var_reg: Lista_var_reg ',' ID {$$.list.addAll($1.list); $$.list.add($3);}
	| ID {$$.list.add($1);}
	;

Cuerpo_reg:		'{' Declaraciones_reg '}' {$$ = $2;}
	;
		  
Declaraciones_reg: 	Declaraciones_reg Declaracion_unit {$$=$1;$$.list.addAll($1.list);$$.list.add($2);}
				 |	Declaracion_unit {$$=$1; $$.list.add($1);}
				 ;

Declaracion_unit:	Tipo ID ';' {$$ = $2; $$.type = $1.sval;nuevaSentencia($$, "declaracion de variable de registro");}
				;
 
Tipo:	ENTERO {$$ = $1;}
	|	ENTEROL {$$ = $1;}
	;

Lista_var: 	Lista_var ',' ID {$$.list.addAll($1.list); $$.list.add($3);}
		 |	ID {$$.list.add($1);}
		 ;

Declaracion_var: Tipo Lista_var ';' {$$ = $1; $2.type=$1.sval; setGlobalVars($2);}
	|	ID {yyerror("Tipo de datos desconocido");} Lista_var ';'
	;
	
Sentencias: Sentencias Sentencia {
				NodoArbol nodo = crearNodo("S", $2.sref,$1.sref);
				$$.row = $1.row;
				$$.sref = nodo;
			}
		|	Sentencia{
				NodoArbol nodo = crearNodo("S", $1.sref, null);
				$$.row = $1.row;
				$$.sref = nodo;
			}
	;
	
Sentencia: Asignacion {nuevaSentencia($1,"Asignacion");
			$$ = $1;}
	|	Seleccion {nuevaSentencia($1,"Sentencia 'si'"); $$ = $1;}
	|	Iteracion {nuevaSentencia($1,"sentencia 'mientras'"); $$ = $1;}
	|	Salida {nuevaSentencia($1,"Sentencia 'imprimir'");$$=$1;}
	|   error {yyerror("Sentencia mal declarada");} 
	;
Asignacion : AsignacionIzq ASIGN Expresion ';' {
			NodoArbol nodo = crearNodo(":=", $1.sref, $3.sref);
			$$.sval= ":=";
			$$.row = $1.row;
			$$.sref = nodo;
			
		}
	| AsignacionIzq ASIGN Expresion error {yyerror("Sentencia debe finalizar con caracter ';'");} ';'
	;
AsignacionIzq: ID {
			$$.sval= $1.sval;
			$$.row = $1.row;
			$$.sref = crearHoja($$.sval);
	}
	| CampoReg;
	;
Expresion: Expresion '+' Termino{
			checkTypes($1, $3);
			NodoArbol nodo = crearNodo("+", $1.sref, $3.sref);
			$$.sval= "+";
			$$.row = $1.row;
			$$.sref = nodo;
			$$.type=$1.type;
		}
	|	Expresion '-' Termino{
			checkTypes($1, $3);
			NodoArbol nodo = crearNodo("-", $1.sref, $3.sref);
			$$.sval= "-";
			$$.row = $1.row;
			$$.sref = nodo;
			$$.type=$1.type;
		}
	|	Termino{
			$$.sval=$1.sval;
			$$.row = $1.row;
			$$.sref =  $1.sref;
			$$.type=$1.type;
		}
	;
	
Termino: Termino '*' Factor{
			checkTypes($1, $3);
			NodoArbol nodo = crearNodo("*", $1.sref, $3.sref);
			$$.sval= "*";
			$$.row = $1.row;
			$$.sref = nodo;
			$$.type=$1.type;
		}
	|	Termino '/' Factor{
			checkTypes($1, $3);
			NodoArbol nodo = crearNodo("/", $1.sref, $3.sref);
			$$.sval= "/";
			$$.row = $1.row;
			$$.sref = nodo;
			$$.type=$1.type;
		}
	|	Factor {
			$$.sval=$1.sval;
			$$.row = $1.row;
			$$.sref =  $1.sref;
			$$.type = $1.type;
	}
	;
Factor: ID {
				TuplaTablaSimbolos tupla = tds.getTupla(val_peek(0).sval);
				if (tupla==null || tupla.getValue("uso") == null){
					yyerror("La variable "+val_peek(0).sval+"' no ha sido declarada");
					if(tupla.getValue("uso") == null)
						tds.delTupla(val_peek(0).sval);
				}
				else if(tupla.getValue("uso") == "registro")
					yyerror("Solo se permite llamados a elementos de registro");
				
				$$.sval=$1.sval;
				$$.row = $1.row;
				$$.sref =  crearHoja($1.sval);
				$$.type = (String)(tds.getTupla($$.sval).getValue("tipo"));
		    }
	|	CTE {
				verificarRango($1.sval, $1.row);
				$$.sval=$1.sval;
				$$.row = $1.row;
				$$.sref =  crearHoja($1.sval);
				$$.type = (String)(tds.getTupla($$.sval).getValue("tipo"));
				}
	|	CampoReg {$$.sval = $1.sval; $$.row=$1.row;
					$$.type = (String)(tds.getTupla($$.sval).getValue("tipo"));
					}
	|	CTENeg {
					$$.sval = $1.sval;
					$$.row=$1.row;
					$$.type = (String)(tds.getTupla($$.sval).getValue("tipo"));
					$$.sref = $1.sref;
					}
	;
	
CTENeg: '-' CTE {
					setToNegative($2);
					$$.sval="-"+$2.sval;
					$$.sref = crearHoja($$.sval);
					$$.row = $2.row;
				}
	;

CampoReg: ID '.' ID {
						
						TuplaTablaSimbolos tuplaReg = tds.getTupla($1.sval);
						TuplaTablaSimbolos tuplaInterna = tds.getTupla($1.sval+"@"+$3.sval);
						if(tuplaReg==null || tuplaReg.getValue("uso")==null){
							yyerror("El registro '"+$1.sval+"' no existe");
							if(tuplaReg.getValue("uso")==null)
								tds.delTupla($1.sval);
						}
						else if(tuplaInterna == null){
							yyerror("El registro "+$1.sval+" no posee el campo interno "+$3.sval);
						}else{
							$$.sval = $1.sval+"@"+$3.sval;
							$$.row = $1.row;
						}
					}
	;

Seleccion: CabeceraSi BloqueEntonces SINO BloqueSino {
				NodoArbol nodoCpo = crearNodo("CUERPO", $2.sref, $4.sref);
				$$.row = $1.row;
				$$.sval = $1.sval;
				$$.sref = $1.sref;
				$$.sref.setHijoDer(nodoCpo);
			}
	|	CabeceraSi  SINO BloqueSino {yyerror("Falta sentencias luego del si");}
	|	CabeceraSi BloqueEntonces %prec IT {
			NodoArbol nodoCpo = crearNodo("CUERPO", $2.sref, null);
			$$.row = $1.row;
			$$.sval = $1.sval;
			$$.sref = $1.sref;
			$$.sref.setHijoDer(nodoCpo);
		}
	;
CabeceraSi: SI '(' Comparacion ')' ENTONCES {
				NodoArbol nodoCond = crearNodo("CONDICION", $3.sref, null);
				NodoArbol nodoSi = crearNodo("SI", nodoCond, null);
				$$ = $1;
				$$.sref = nodoSi;
			}
	| SI '(' error{yyerror("Error en la comparacion");} ')' ENTONCES
	| SI Comparacion ')' ENTONCES {yyerror("Falta caracter '('");}
	| SI '(' Comparacion ENTONCES {yyerror("No se encontro el caracter ')'"); } 
	| SI '(' Comparacion ')' {yyerror("Falta la palabra reservada 'entonces'");}
	;

Comparacion: Expresion COMP Expresion{
				NodoArbol nodo = crearNodo($2.sval, $1.sref, $3.sref);
				$$.row = $1.row;
				$$.sval = $2.sval;
				$$.sref = nodo;				
			} 
	;

BloqueEntonces: Sentencia {
					ParserVal a = $1;
					$$ = $1;
					$$.sref = crearNodo("ENTONCES", $1.sref, null);
					}
	| 
				BloqueSentencias {
					$$ = $1;
					ParserVal a = $1;
					$$.sref = crearNodo("ENTONCES", $1.sref, null);
				}
	;

BloqueSentencias: '{' Sentencias  '}' {$$ = $2;}
	;

BloqueSino: BloqueSentencias {
				$$ = $1;
				$$.sref = crearNodo("SINO", $1.sref, null);
			}
	| Sentencia {
		$$ = $1;
		$$.sref = crearNodo("SINO", $1.sref, null);
	}
	;
	
Iteracion: CabeceraIteracion BloqueIteracion {
				NodoArbol nodoCpo = crearNodo("CUERPO", $2.sref, null);
				$$.row = $1.row;
				$$.sval = $1.sval;
				$$.sref = $1.sref;
				$$.sref.setHijoDer(nodoCpo);
			}
	; 
CabeceraIteracion: MIENTRAS '(' Comparacion ')' ITERAR {
						NodoArbol nodoCond = crearNodo("CONDICION", $3.sref, null);
						NodoArbol nodoIt = crearNodo("ITERACION", nodoCond, null);
						$$ = $1;
						$$.sref = nodoIt;
					}
	| MIENTRAS  Comparacion ')' ITERAR {yyerror("Falta caracter '('");}
	| MIENTRAS '(' error{yyerror("Error en la comparacion");}  ')' ITERAR
	| MIENTRAS '(' Comparacion ITERAR {yyerror("No se encontro el caracter ')'"); }
	| MIENTRAS '(' Comparacion ')' {yyerror("Falta la palabra reservada 'iterar'");} ';'
	;

BloqueIteracion: Sentencia {$$ = $1;}
	|	BloqueSentencias{$$ = $1;}
	;

Salida: IMPRIMIR '(' STR ')' ';' {
			$$ = $1;
			NodoArbol hoja = crearHoja($3.sval);
			$$.sref = crearNodo("IMPRIMIR", hoja, null);
		}
	| IMPRIMIR '(' error{yyerror("Cadena de caracteres mal definida");} ')' ';'
	| IMPRIMIR '(' STR ')' error{yyerror("la sentencia debe finalizar con el caracter ';'");}
	;

	%%
Vector<NodoArbol> sentencias;
TablaSimbolo tds;
AnalizadorLexico al;
Vector<ParserVal> tokens = new Vector<ParserVal>();
ErrorManager errorManager;
SentenciaManager sentenciasManager;
public static double MAX = Math.pow(2, 15)-1; 
NodoArbol raiz;
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
	this.sentencias = new Vector<NodoArbol>();
	}
	
private void yyerror(String string) {
	ParserError error = new ParserError(string, ParserError.TYPE_SINTACTICO, yylval.row, false);
	errorManager.addError(error);
}

public TablaSimbolo getTablaDeSimbolos() {
	return tds;
}

private void setToNegative(ParserVal val_peek) {
	TuplaTablaSimbolos tupla = tds.getTupla(val_peek.sval).clone();
	Double neg = Double.valueOf((String) tupla.getValue("valor"))*-1;
	int val = neg.intValue();
	tupla.setValue("valor", val+"");
	tupla.setValue("negativo", true);
	tds.addTupla(tupla);
}


public void nuevaSentencia(ParserVal lval,String sentencia){
	this.sentenciasManager.addSentence(
		new Sentencia(
			lval.row,
			sentencia
		)
	);
}

public NodoArbol run(String fileName)
{
	al.setReaderOrigin(fileName);
    yyparse();
	return raiz;
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

private void verificarRango(String a, int nro){
	double max = Math.pow(2, 31)-1;
	double val = Double.valueOf(a);
	if(val>max){
			errorManager.addError(new ParserError("Constante fuera de rango", ParserError.TYPE_LEXICO,nro ,false));
			tds.delTupla(a);
	}
	
}

private void addUse(String use, List<ParserVal> ids){
	for(int i=0; i<ids.size(); i++){
		addUse(use, ids.get(i).sval);
	}
}

private void addUse(String use, String id){
	tds.getTupla(id).setValue("uso", use);
}
private void setGlobalVars(ParserVal val_peek) {
	for(ParserVal val : val_peek.list){
		TuplaTablaSimbolos tupla = tds.getTupla(val.sval);
		if(tupla==null){
			tupla = new TuplaTablaSimbolos();
			tupla.setValue("valor", val.sval);
			tupla.setValue("clase", val.kind);
			tds.addTupla(tupla);
		}
		if(tupla.getValue("uso") != null){
			this.errorManager.addError(new ParserError("La variable "+val.sval+" ya fue definida",
					ParserError.TYPE_SINTACTICO, val.row, false));
			return;
		}
		else{
			tupla.setValue("uso", "variable");
			tupla.setValue("tipo",val_peek.type);
		}
	}
	
}

private boolean existsID(String id){
	TuplaTablaSimbolos tupla = tds.getTupla(id);
	if (tupla==null)
		return false;
	return true;
}

private void setRecordInnerVars(List<ParserVal> innerVars, List<ParserVal> regVars) {
	for(ParserVal val : innerVars){
		TuplaTablaSimbolos tupla = tds.getTupla(val.sval);
//		chequeo que no este ya definida si lo esta creo una nueva
		if(tupla.getValue("uso") != null){
			tupla = new TuplaTablaSimbolos();
			tupla.setValue("row", val.row);
			tupla.setValue("clase", val.kind);
			tupla.setValue("valor", val.sval);
			
		}else{
			tds.delTupla(val.sval);
		}
		tupla.setValue("uso", "variable interna registro");
		tupla.setValue("tipo",val.type);
		
		/**
		 * Manejo de nameling
		 */
		for(ParserVal rVal : regVars){
			TuplaTablaSimbolos tuplaReg = tds.getTupla(rVal.sval);
			if(tuplaReg.getValue("uso") != null){
				this.errorManager.addError(new ParserError("No se puede utilizar el identificador "+rVal.sval+" ya fue definida dentro del ambito global",
					ParserError.TYPE_SINTACTICO, rVal.row, false));
				continue;
			}
			this.addUse("registro", rVal.sval);
			if(tds.getTupla(rVal.sval + "@" + tupla.getValue("valor")) != null){
				this.errorManager.addError(new ParserError("La variable "+val.sval+" ya fue definida dentro del registro",
					ParserError.TYPE_SINTACTICO, val.row, false));
				return;
			}
			TuplaTablaSimbolos nueva = tupla.clone();
			nueva.setValue("valor", rVal.sval + "@" + tupla.getValue("valor"));
			tds.addTupla(nueva);;
		}
	
	}	
}

private NodoArbol crearHoja(String val){
	TuplaTablaSimbolos tupla = tds.getTupla(val);
	if(tupla==null)
		return null;
	NodoArbol nodo = new NodoArbol((String) tupla.getValue("valor"));
	return nodo;
}

private NodoArbol crearNodo(String val, NodoArbol izq, NodoArbol der) {
	return new NodoArbol(der, izq, val);
}

private void checkTypes(ParserVal val1, ParserVal val2) {
	if(val1.type != val2.type){
		errorManager.addError(new ParserError("No se permiten operaciones o asignaciones "
				+ "entre elementos de distinto tipo", ParserError.TYPE_SINTACTICO, val1.row, false));
	}
	
}