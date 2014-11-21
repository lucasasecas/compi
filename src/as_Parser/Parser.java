//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "sintaxis.y"
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
//#line 31 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short REGISTRO=258;
public final static short ENTERO=259;
public final static short ENTEROL=260;
public final static short ASIGN=261;
public final static short CTE=262;
public final static short SI=263;
public final static short SINO=264;
public final static short COMP=265;
public final static short ENTONCES=266;
public final static short MIENTRAS=267;
public final static short ITERAR=268;
public final static short IMPRIMIR=269;
public final static short STR=270;
public final static short IT=271;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    3,    3,    4,    8,    4,    7,
    7,    6,    9,    9,   10,   11,   11,   12,   12,    5,
   13,    5,    2,    2,   14,   14,   14,   14,   14,   15,
   21,   15,   19,   19,   20,   20,   20,   23,   23,   23,
   24,   24,   24,   24,   25,   22,   16,   16,   16,   26,
   30,   26,   26,   26,   26,   29,   27,   27,   31,   28,
   28,   17,   32,   32,   34,   32,   32,   35,   32,   33,
   33,   18,   36,   18,   18,
};
final static short yylen[] = {                            2,
    2,    1,    2,    1,    1,    1,    4,    0,    5,    3,
    1,    3,    2,    1,    3,    1,    1,    3,    1,    3,
    0,    4,    2,    1,    1,    1,    1,    1,    1,    4,
    0,    6,    1,    1,    3,    3,    1,    3,    3,    1,
    1,    1,    1,    1,    2,    3,    4,    3,    2,    5,
    0,    6,    4,    4,    4,    3,    1,    1,    3,    1,
    1,    2,    5,    4,    0,    6,    4,    0,    6,    1,
    1,    5,    0,    6,    5,
};
final static short yydefred[] = {                         0,
   21,    0,   16,   17,    0,    0,    4,    5,    6,    0,
    0,    8,    0,    0,   29,    0,    0,    0,    0,    0,
    3,   24,   25,   26,   27,   28,    0,   34,    0,    0,
   19,    0,    0,    0,    0,   14,    0,   11,    0,    0,
    0,   42,    0,    0,    0,   43,    0,   40,   44,    0,
    0,    0,    0,    0,   23,    0,    0,    0,   57,    0,
   58,   70,   71,   62,   20,    0,   22,    0,   12,   13,
    0,    7,    0,   46,   45,   51,    0,    0,    0,    0,
    0,    0,    0,   65,    0,    0,   73,    0,    0,   61,
   48,   60,    0,    0,   18,    9,   15,   10,    0,   54,
    0,    0,    0,    0,   39,   38,   53,    0,   67,    0,
   64,    0,    0,   31,   30,   59,   47,    0,   50,    0,
   63,    0,    0,   75,   72,    0,   52,   66,   69,   74,
   32,
};
final static short yydgoto[] = {                          5,
    6,   20,    7,    8,    9,   14,   39,   34,   35,   36,
   10,   32,   11,   22,   23,   24,   25,   26,   27,   45,
  126,   46,   47,   48,   49,   29,   60,   91,   50,   99,
   92,   30,   64,  108,  122,  112,
};
final static short yysindex[] = {                      -158,
    0, -101,    0,    0,    0, -171,    0,    0,    0, -245,
 -245,    0, -220, -236,    0,   31,  -10,   -7,   30, -196,
    0,    0,    0,    0,    0,    0, -210,    0, -113,  -87,
    0,   19,   20, -236, -111,    0, -200,    0,   24, -192,
   31,    0, -180,    3,  -11,    0,   34,    0,    0,   54,
    5,   56, -211,   31,    0,    9,  -87, -196,    0, -155,
    0,    0,    0,    0,    0, -145,    0,   25,    0,    0,
   55,    0, -144,    0,    0,    0,  -41,    9,    9,    9,
    9,    9, -151,    0,  -23, -150,    0,   79,   13,    0,
    0,    0,  -84,  -87,    0,    0,    0,    0,   80,    0,
 -147,   48,   34,   34,    0,    0,    0,   81,    0, -143,
    0,   82,  -54,    0,    0,    0,    0, -142,    0, -141,
    0,   69,   70,    0,    0,   71,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  131,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -167,    0,    0,    0,  132,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -39,    0,    0,    0,    0,    0,  -34,    0,    0,    0,
    0,    0,    0, -128,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -104,  -25,  -28,  -17,    0,    0,    0,    0,    0,   75,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,   77,  130,    0,    0,    0,  103,    0,    0,  104,
   14,  127,    0,   23,    0,    0,    0,    0,    0,  -12,
    0,   17,   26,   22,    0,    0,    0,   46,   11,    0,
   78,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=271;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        101,
   49,   41,   41,   41,  125,   41,   37,   41,   37,   58,
   37,   31,   35,   69,   35,   56,   35,  110,   55,   41,
   38,   13,   28,   36,   37,   36,   37,   36,   52,   44,
   35,   79,   51,   80,   43,   58,   28,   43,    3,    4,
  116,   36,   55,   89,   87,   28,   28,   43,   37,   43,
   56,   59,   62,   43,   77,   79,   71,   80,   88,   15,
   54,   85,   66,   66,   74,  102,   17,   73,   73,   53,
   18,  115,   19,   28,   28,   82,   40,   65,   67,   90,
   81,   75,   72,   96,   15,   16,    2,    3,    4,   21,
   79,   17,   80,   33,   83,   18,   86,   19,    1,    2,
    3,    4,  105,  106,  103,  104,   61,   63,   94,   28,
   28,   95,   98,   97,  107,   55,   90,  111,  119,  113,
  118,  120,  123,  127,  121,   49,  128,  129,  130,  131,
    2,    1,   33,   68,   93,   21,   68,   33,   70,  117,
    0,    0,   15,   54,    0,    0,    0,    3,    4,   17,
   57,   55,   55,   18,   12,   19,    0,    0,   55,   55,
    0,    0,   55,    0,   55,    0,    0,    0,   15,   54,
    0,   15,   54,    0,    0,   17,    0,    0,   17,   18,
    0,   19,   18,    0,   19,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  124,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   41,    0,    0,    0,
    0,   37,    0,    0,  100,   41,   41,   35,   41,    0,
   37,   37,    0,   37,    0,    0,   35,   35,   36,   35,
   56,    0,   56,    0,  109,    0,   41,   36,   36,   41,
   36,   42,    0,   78,   42,    0,   49,   49,   76,   41,
   84,   41,    0,   49,   42,   41,   42,   49,  114,   49,
   42,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   42,   43,   59,   45,   41,   47,   43,  123,
   45,  257,   41,  125,   43,   41,   45,   41,  123,   59,
  257,  123,    6,   41,   59,   43,   13,   45,   18,   40,
   59,   43,   40,   45,   45,  123,   20,   45,  259,  260,
  125,   59,   20,   56,  256,   29,   30,   45,   35,   45,
  261,   29,   30,   45,   44,   43,  257,   45,  270,  256,
  257,   51,   44,   44,  257,   78,  263,   44,   44,   40,
  267,   59,  269,   57,   58,   42,   46,   59,   59,   57,
   47,  262,   59,   59,  256,  257,  258,  259,  260,  257,
   43,  263,   45,  261,   41,  267,   41,  269,  257,  258,
  259,  260,   81,   82,   79,   80,   29,   30,  264,   93,
   94,  257,  257,   59,  266,   93,   94,  268,  266,   41,
   41,   41,   41,  266,  268,  125,  268,   59,   59,   59,
    0,    0,  261,   59,   58,    6,   34,   11,   35,   94,
   -1,   -1,  256,  257,   -1,   -1,   -1,  259,  260,  263,
  264,  256,  257,  267,  256,  269,   -1,   -1,  263,  264,
   -1,   -1,  267,   -1,  269,   -1,   -1,   -1,  256,  257,
   -1,  256,  257,   -1,   -1,  263,   -1,   -1,  263,  267,
   -1,  269,  267,   -1,  269,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,
   -1,  256,   -1,   -1,  266,  265,  266,  256,  268,   -1,
  265,  266,   -1,  268,   -1,   -1,  265,  266,  256,  268,
  266,   -1,  268,   -1,  268,   -1,  257,  265,  266,  257,
  268,  262,   -1,  265,  262,   -1,  256,  257,  256,  257,
  256,  257,   -1,  263,  262,  257,  262,  267,  256,  269,
  262,
};
}
final static short YYFINAL=5;
final static short YYMAXTOKEN=271;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","REGISTRO","ENTERO","ENTEROL","ASIGN",
"CTE","SI","SINO","COMP","ENTONCES","MIENTRAS","ITERAR","IMPRIMIR","STR","IT",
};
final static String yyrule[] = {
"$accept : Programa",
"Programa : Declaraciones Sentencias",
"Programa : Declaraciones",
"Declaraciones : Declaraciones Declaracion",
"Declaraciones : Declaracion",
"Declaracion : Declaracion_reg",
"Declaracion : Declaracion_var",
"Declaracion_reg : REGISTRO Cuerpo_reg Lista_var_reg ';'",
"$$1 :",
"Declaracion_reg : REGISTRO error $$1 Lista_var_reg ';'",
"Lista_var_reg : Lista_var_reg ',' ID",
"Lista_var_reg : ID",
"Cuerpo_reg : '{' Declaraciones_reg '}'",
"Declaraciones_reg : Declaraciones_reg Declaracion_unit",
"Declaraciones_reg : Declaracion_unit",
"Declaracion_unit : Tipo ID ';'",
"Tipo : ENTERO",
"Tipo : ENTEROL",
"Lista_var : Lista_var ',' ID",
"Lista_var : ID",
"Declaracion_var : Tipo Lista_var ';'",
"$$2 :",
"Declaracion_var : ID $$2 Lista_var ';'",
"Sentencias : Sentencias Sentencia",
"Sentencias : Sentencia",
"Sentencia : Asignacion",
"Sentencia : Seleccion",
"Sentencia : Iteracion",
"Sentencia : Salida",
"Sentencia : error",
"Asignacion : AsignacionIzq ASIGN Expresion ';'",
"$$3 :",
"Asignacion : AsignacionIzq ASIGN Expresion error $$3 ';'",
"AsignacionIzq : ID",
"AsignacionIzq : CampoReg",
"Expresion : Expresion '+' Termino",
"Expresion : Expresion '-' Termino",
"Expresion : Termino",
"Termino : Termino '*' Factor",
"Termino : Termino '/' Factor",
"Termino : Factor",
"Factor : ID",
"Factor : CTE",
"Factor : CampoReg",
"Factor : CTENeg",
"CTENeg : '-' CTE",
"CampoReg : ID '.' ID",
"Seleccion : CabeceraSi BloqueEntonces SINO BloqueSino",
"Seleccion : CabeceraSi SINO BloqueSino",
"Seleccion : CabeceraSi BloqueEntonces",
"CabeceraSi : SI '(' Comparacion ')' ENTONCES",
"$$4 :",
"CabeceraSi : SI '(' error $$4 ')' ENTONCES",
"CabeceraSi : SI Comparacion ')' ENTONCES",
"CabeceraSi : SI '(' Comparacion ENTONCES",
"CabeceraSi : SI '(' Comparacion ')'",
"Comparacion : Expresion COMP Expresion",
"BloqueEntonces : Sentencia",
"BloqueEntonces : BloqueSentencias",
"BloqueSentencias : '{' Sentencias '}'",
"BloqueSino : BloqueSentencias",
"BloqueSino : Sentencia",
"Iteracion : CabeceraIteracion BloqueIteracion",
"CabeceraIteracion : MIENTRAS '(' Comparacion ')' ITERAR",
"CabeceraIteracion : MIENTRAS Comparacion ')' ITERAR",
"$$5 :",
"CabeceraIteracion : MIENTRAS '(' error $$5 ')' ITERAR",
"CabeceraIteracion : MIENTRAS '(' Comparacion ITERAR",
"$$6 :",
"CabeceraIteracion : MIENTRAS '(' Comparacion ')' $$6 ';'",
"BloqueIteracion : Sentencia",
"BloqueIteracion : BloqueSentencias",
"Salida : IMPRIMIR '(' STR ')' ';'",
"$$7 :",
"Salida : IMPRIMIR '(' error $$7 ')' ';'",
"Salida : IMPRIMIR '(' STR ')' error",
};

//#line 325 "sintaxis.y"
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
	if(!val1.type.equals(val2.type)){
		errorManager.addError(new ParserError("No se permiten operaciones o asignaciones "
				+ "entre elementos de distinto tipo", ParserError.TYPE_SINTACTICO, val1.row, false));
	}
	
}
//#line 549 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 43 "sintaxis.y"
{
			ParserVal a= val_peek(0);
			raiz=a.sref;
		}
break;
case 2:
//#line 47 "sintaxis.y"
{yyerror("No se encontraron declaraciones");}
break;
case 5:
//#line 54 "sintaxis.y"
{nuevaSentencia(val_peek(0), "Declaracion de registro");}
break;
case 6:
//#line 55 "sintaxis.y"
{nuevaSentencia(val_peek(0), "Declaracion de variable");}
break;
case 7:
//#line 58 "sintaxis.y"
{yyval = val_peek(1); setRecordInnerVars(val_peek(2).list, val_peek(1).list);}
break;
case 8:
//#line 59 "sintaxis.y"
{yyerror("Declaracion de variables internas al registro invalida");}
break;
case 9:
//#line 59 "sintaxis.y"
{yyval = val_peek(2);}
break;
case 10:
//#line 62 "sintaxis.y"
{yyval.list.addAll(val_peek(2).list); yyval.list.add(val_peek(0));}
break;
case 11:
//#line 63 "sintaxis.y"
{yyval.list.add(val_peek(0));}
break;
case 12:
//#line 66 "sintaxis.y"
{yyval = val_peek(1);}
break;
case 13:
//#line 69 "sintaxis.y"
{yyval=val_peek(1);yyval.list.addAll(val_peek(1).list);yyval.list.add(val_peek(0));}
break;
case 14:
//#line 70 "sintaxis.y"
{yyval=val_peek(0); yyval.list.add(val_peek(0));}
break;
case 15:
//#line 73 "sintaxis.y"
{yyval = val_peek(1); yyval.type = val_peek(2).sval;nuevaSentencia(yyval, "declaracion de variable de registro");}
break;
case 16:
//#line 76 "sintaxis.y"
{yyval = val_peek(0);}
break;
case 17:
//#line 77 "sintaxis.y"
{yyval = val_peek(0);}
break;
case 18:
//#line 80 "sintaxis.y"
{yyval.list.addAll(val_peek(2).list); yyval.list.add(val_peek(0));}
break;
case 19:
//#line 81 "sintaxis.y"
{yyval.list.add(val_peek(0));}
break;
case 20:
//#line 84 "sintaxis.y"
{yyval = val_peek(2); val_peek(1).type=val_peek(2).sval; setGlobalVars(val_peek(1));}
break;
case 21:
//#line 85 "sintaxis.y"
{yyerror("Tipo de datos desconocido");}
break;
case 23:
//#line 88 "sintaxis.y"
{
				NodoArbol nodo = crearNodo("S", val_peek(0).sref,val_peek(1).sref);
				yyval.row = val_peek(1).row;
				yyval.sref = nodo;
			}
break;
case 24:
//#line 93 "sintaxis.y"
{
				NodoArbol nodo = crearNodo("S", val_peek(0).sref, null);
				yyval.row = val_peek(0).row;
				yyval.sref = nodo;
			}
break;
case 25:
//#line 100 "sintaxis.y"
{nuevaSentencia(val_peek(0),"Asignacion");
			yyval = val_peek(0);}
break;
case 26:
//#line 102 "sintaxis.y"
{nuevaSentencia(val_peek(0),"Sentencia 'si'");
ParserVal a = val_peek(0); 
yyval = a;

}
break;
case 27:
//#line 103 "sintaxis.y"
{nuevaSentencia(val_peek(0),"sentencia 'mientras'"); yyval = val_peek(0);}
break;
case 28:
//#line 104 "sintaxis.y"
{nuevaSentencia(val_peek(0),"Sentencia 'imprimir'");yyval=val_peek(0);}
break;
case 29:
//#line 105 "sintaxis.y"
{yyerror("Sentencia mal declarada");}
break;
case 30:
//#line 107 "sintaxis.y"
{
			NodoArbol nodo = crearNodo(":=", val_peek(3).sref, val_peek(1).sref);
			yyval.sval= ":=";
			yyval.row = val_peek(3).row;
			yyval.sref = nodo;
			
		}
break;
case 31:
//#line 114 "sintaxis.y"
{yyerror("Sentencia debe finalizar con caracter ';'");}
break;
case 33:
//#line 116 "sintaxis.y"
{
			yyval.sval= val_peek(0).sval;
			yyval.row = val_peek(0).row;
			yyval.sref = crearHoja(yyval.sval);
	}
break;
case 35:
//#line 123 "sintaxis.y"
{
			checkTypes(val_peek(2), val_peek(0));
			NodoArbol nodo = crearNodo("+", val_peek(2).sref, val_peek(0).sref);
			yyval.sval= "+";
			yyval.row = val_peek(2).row;
			yyval.sref = nodo;
			yyval.type=val_peek(2).type;
		}
break;
case 36:
//#line 131 "sintaxis.y"
{
			checkTypes(val_peek(2), val_peek(0));
			NodoArbol nodo = crearNodo("-", val_peek(2).sref, val_peek(0).sref);
			yyval.sval= "-";
			yyval.row = val_peek(2).row;
			yyval.sref = nodo;
			yyval.type=val_peek(2).type;
		}
break;
case 37:
//#line 139 "sintaxis.y"
{
			yyval.sval=val_peek(0).sval;
			yyval.row = val_peek(0).row;
			yyval.sref =  val_peek(0).sref;
			yyval.type=val_peek(0).type;
		}
break;
case 38:
//#line 147 "sintaxis.y"
{
			checkTypes(val_peek(2), val_peek(0));
			NodoArbol nodo = crearNodo("*", val_peek(2).sref, val_peek(0).sref);
			yyval.sval= "*";
			yyval.row = val_peek(2).row;
			yyval.sref = nodo;
			yyval.type=val_peek(2).type;
		}
break;
case 39:
//#line 155 "sintaxis.y"
{
			checkTypes(val_peek(2), val_peek(0));
			NodoArbol nodo = crearNodo("/", val_peek(2).sref, val_peek(0).sref);
			yyval.sval= "/";
			yyval.row = val_peek(2).row;
			yyval.sref = nodo;
			yyval.type=val_peek(2).type;
		}
break;
case 40:
//#line 163 "sintaxis.y"
{
			yyval.sval=val_peek(0).sval;
			yyval.row = val_peek(0).row;
			yyval.sref =  val_peek(0).sref;
			yyval.type = val_peek(0).type;
	}
break;
case 41:
//#line 170 "sintaxis.y"
{
				TuplaTablaSimbolos tupla = tds.getTupla(val_peek(0).sval);
				if (tupla==null || tupla.getValue("uso") == null){
					yyerror("La variable "+val_peek(0).sval+"' no ha sido declarada");
					if(tupla.getValue("uso") == null)
						tds.delTupla(val_peek(0).sval);
				}
				else if(tupla.getValue("uso") == "registro")
					yyerror("Solo se permite llamados a elementos de registro");
				
				yyval.sval=val_peek(0).sval;
				yyval.row = val_peek(0).row;
				yyval.sref =  crearHoja(val_peek(0).sval);
				yyval.type = (String)(tds.getTupla(yyval.sval).getValue("tipo"));
		    }
break;
case 42:
//#line 185 "sintaxis.y"
{
				verificarRango(val_peek(0).sval, val_peek(0).row);
				yyval.sval=val_peek(0).sval;
				yyval.row = val_peek(0).row;
				yyval.sref =  crearHoja(val_peek(0).sval);
				yyval.type = (String)(tds.getTupla(yyval.sval).getValue("tipo"));
				}
break;
case 43:
//#line 192 "sintaxis.y"
{yyval.sval = val_peek(0).sval; yyval.row=val_peek(0).row;
					yyval.type = (String)(tds.getTupla(yyval.sval).getValue("tipo"));
					}
break;
case 44:
//#line 195 "sintaxis.y"
{
					yyval.sval = val_peek(0).sval;
					yyval.row=val_peek(0).row;
					yyval.type = (String)(tds.getTupla(yyval.sval).getValue("tipo"));
					yyval.sref = val_peek(0).sref;
					}
break;
case 45:
//#line 203 "sintaxis.y"
{
					setToNegative(val_peek(0));
					yyval.sval="-"+val_peek(0).sval;
					yyval.sref = crearHoja(yyval.sval);
					yyval.row = val_peek(0).row;
				}
break;
case 46:
//#line 211 "sintaxis.y"
{
						
						TuplaTablaSimbolos tuplaReg = tds.getTupla(val_peek(2).sval);
						TuplaTablaSimbolos tuplaInterna = tds.getTupla(val_peek(2).sval+"@"+val_peek(0).sval);
						if(tuplaReg==null || tuplaReg.getValue("uso")==null){
							yyerror("El registro '"+val_peek(2).sval+"' no existe");
							if(tuplaReg.getValue("uso")==null)
								tds.delTupla(val_peek(2).sval);
						}
						else if(tuplaInterna == null){
							yyerror("El registro "+val_peek(2).sval+" no posee el campo interno "+val_peek(0).sval);
						}else{
							yyval.sval = val_peek(2).sval+"@"+val_peek(0).sval;
							yyval.row = val_peek(2).row;
						}
					}
break;
case 47:
//#line 229 "sintaxis.y"
{
				NodoArbol nodoCpo = crearNodo("CUERPO", val_peek(2).sref, val_peek(0).sref);
				yyval.row = val_peek(3).row;
				yyval.sval = val_peek(3).sval;
				yyval.sref = val_peek(3).sref;
				yyval.sref.setHijoDer(nodoCpo);
			}
break;
case 48:
//#line 236 "sintaxis.y"
{yyerror("Falta sentencias luego del si");}
break;
case 49:
//#line 237 "sintaxis.y"
{
			NodoArbol nodoCpo = crearNodo("CUERPO", val_peek(0).sref, null);
			yyval.row = val_peek(1).row;
			yyval.sval = val_peek(1).sval;
			yyval.sref = val_peek(1).sref;
			yyval.sref.setHijoDer(nodoCpo);
		}
break;
case 50:
//#line 245 "sintaxis.y"
{
				NodoArbol nodoCond = crearNodo("CONDICION", val_peek(2).sref, null);
				NodoArbol nodoSi = crearNodo("SI", nodoCond, null);
				yyval = val_peek(4);
				yyval.sref = nodoSi;
			}
break;
case 51:
//#line 251 "sintaxis.y"
{yyerror("Error en la comparacion");}
break;
case 53:
//#line 252 "sintaxis.y"
{yyerror("Falta caracter '('");}
break;
case 54:
//#line 253 "sintaxis.y"
{yyerror("No se encontro el caracter ')'"); }
break;
case 55:
//#line 254 "sintaxis.y"
{yyerror("Falta la palabra reservada 'entonces'");}
break;
case 56:
//#line 257 "sintaxis.y"
{
				NodoArbol nodo = crearNodo(val_peek(1).sval, val_peek(2).sref, val_peek(0).sref);
				yyval.row = val_peek(2).row;
				yyval.sval = val_peek(1).sval;
				yyval.sref = nodo;				
			}
break;
case 57:
//#line 265 "sintaxis.y"
{
					ParserVal a = val_peek(0);
					yyval = val_peek(0);
					yyval.sref = crearNodo("ENTONCES", val_peek(0).sref, null);
					}
break;
case 58:
//#line 271 "sintaxis.y"
{
					yyval = val_peek(0);
					ParserVal a = val_peek(0);
					yyval.sref = crearNodo("ENTONCES", val_peek(0).sref, null);
				}
break;
case 59:
//#line 278 "sintaxis.y"
{yyval = val_peek(1);}
break;
case 60:
//#line 281 "sintaxis.y"
{
				yyval = val_peek(0);
				yyval.sref = crearNodo("SINO", val_peek(0).sref, null);
			}
break;
case 61:
//#line 285 "sintaxis.y"
{
		yyval = val_peek(0);
		yyval.sref = crearNodo("SINO", val_peek(0).sref, null);
	}
break;
case 62:
//#line 291 "sintaxis.y"
{
				NodoArbol nodoCpo = crearNodo("CUERPO_IT", val_peek(0).sref, null);
				yyval.row = val_peek(1).row;
				yyval.sval = val_peek(1).sval;
				yyval.sref = val_peek(1).sref;
				yyval.sref.setHijoDer(nodoCpo);
			}
break;
case 63:
//#line 299 "sintaxis.y"
{
						NodoArbol nodoCond = crearNodo("CONDICION_IT", val_peek(2).sref, null);
						NodoArbol nodoIt = crearNodo("ITERACION", nodoCond, null);
						yyval = val_peek(4);
						yyval.sref = nodoIt;
					}
break;
case 64:
//#line 305 "sintaxis.y"
{yyerror("Falta caracter '('");}
break;
case 65:
//#line 306 "sintaxis.y"
{yyerror("Error en la comparacion");}
break;
case 67:
//#line 307 "sintaxis.y"
{yyerror("No se encontro el caracter ')'"); }
break;
case 68:
//#line 308 "sintaxis.y"
{yyerror("Falta la palabra reservada 'iterar'");}
break;
case 70:
//#line 311 "sintaxis.y"
{yyval = val_peek(0);}
break;
case 71:
//#line 312 "sintaxis.y"
{yyval = val_peek(0);}
break;
case 72:
//#line 315 "sintaxis.y"
{
			yyval = val_peek(4);
			NodoArbol hoja = crearHoja(val_peek(2).sval);
			yyval.sref = crearNodo("IMPRIMIR", hoja, null);
		}
break;
case 73:
//#line 320 "sintaxis.y"
{yyerror("Cadena de caracteres mal definida");}
break;
case 75:
//#line 321 "sintaxis.y"
{yyerror("la sentencia debe finalizar con el caracter ';'");}
break;
//#line 1120 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
