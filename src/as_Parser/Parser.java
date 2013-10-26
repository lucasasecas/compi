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






//#line 2 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
package as_Parser;
import java.util.Stack;
import java.io.*;
import al_Main.AnalizadorLexico;
import java.util.Vector;
import Utils.TablaSimbolo;
import Utils.TuplaTablaSimbolos;
import Utils.Pila;
//#line 26 "Parser.java"




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
public final static short UINT=258;
public final static short FUNCTION=259;
public final static short BEGIN=260;
public final static short END=261;
public final static short RETURN=262;
public final static short CTE=263;
public final static short ASIGN=264;
public final static short IF=265;
public final static short ELSE=266;
public final static short COMP=267;
public final static short THEN=268;
public final static short FOR=269;
public final static short STR=270;
public final static short PRINT=271;
public final static short IT=272;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    3,    3,    4,    6,    7,    7,
    5,    9,    5,   10,    5,    8,    8,   13,    8,   11,
   11,   12,   12,   12,   14,    2,    2,   16,    2,   15,
   15,   15,   15,   15,   15,   17,   17,   23,   21,   25,
   21,   24,   24,   24,   27,   24,   26,   26,   26,   29,
   26,   30,   26,   28,   28,   28,   22,   31,   22,   35,
   18,   18,   32,   37,   32,   38,   32,   33,   34,   39,
   39,   40,   39,   41,   39,   36,   19,   19,   42,   42,
   43,   45,   43,   43,   44,   20,   46,   20,   47,   20,
};
final static short yylen[] = {                            2,
    2,    1,    2,    1,    1,    1,    3,    1,    3,    1,
    4,    0,    5,    0,    4,    2,    2,    0,    3,    2,
    1,    1,    2,    2,    3,    2,    1,    0,    4,    2,
    1,    1,    2,    2,    2,    3,    3,    1,    4,    0,
    5,    3,    3,    1,    0,    4,    3,    3,    1,    0,
    5,    0,    5,    1,    1,    1,    3,    0,    5,    0,
    5,    2,    5,    0,    6,    0,    7,    1,    1,    1,
    3,    0,    4,    0,    5,    3,    2,    3,    6,    6,
    3,    0,    5,    3,    1,    4,    0,    5,    0,    6,
};
final static short yydefred[] = {                         0,
    2,    8,    0,    0,    0,    4,    5,    6,    0,   14,
    0,    0,    0,    0,    0,    0,    0,    3,   27,    0,
   31,   32,    0,    0,    0,    0,    0,    0,   10,    0,
    0,    0,   21,    0,    0,    0,    0,    0,    0,    0,
   28,   26,   30,   33,   34,   35,    0,    0,   70,    0,
   68,    0,   77,    7,    0,   15,    0,    0,   11,   17,
    0,   20,    0,   22,   58,   57,   40,    0,   55,   56,
    0,    0,   49,   64,    0,    0,   38,    0,   87,    0,
    0,   37,    0,   72,    0,   60,   78,    9,   13,    0,
    0,    0,   23,   24,    0,    0,   45,    0,    0,   39,
    0,    0,    0,    0,    0,    0,    0,   89,   86,   29,
    0,    0,   71,    0,   25,   59,   41,    0,    0,    0,
   52,   48,   50,   47,    0,    0,   66,   63,    0,   85,
    0,    0,   88,    0,   73,    0,   61,   69,   46,    0,
    0,   65,    0,   80,   79,    0,    0,   90,   75,   53,
   51,   67,   84,   82,    0,    0,   83,
};
final static short yydgoto[] = {                          4,
    5,   17,    6,    7,    8,    9,   30,   34,   57,   31,
   35,   63,   58,   64,   49,   81,   20,   21,   22,   23,
   24,   25,   26,   71,   96,   72,  118,   73,  141,  140,
   95,   27,   50,  137,  114,   76,  103,  143,   51,  111,
  136,   28,  131,  132,  156,  107,  134,
};
final static short yysindex[] = {                       -96,
    0,    0, -186,    0, -123,    0,    0,    0, -210,    0,
 -194,   -3,   12,   15,   37,   45, -149,    0,    0,   29,
    0,    0,   50,   74,   84,  -98, -113, -206,    0,   -2,
 -110,    0,    0,  -94, -129,  -41, -170, -167,  -89, -212,
    0,    0,    0,    0,    0,    0, -153, -139,    0,  -97,
    0,  111,    0,    0,  -86,    0,  -88,  -85,    0,    0,
  116,    0, -189,    0,    0,    0,    0,   -3,    0,    0,
  -24,   36,    0,    0,  -27,  134,    0,  117,    0,  -17,
 -112,    0,  -18,    0, -190,    0,    0,    0,    0, -189,
 -112,   -5,    0,    0,  118,  119,    0, -218, -218,    0,
 -142, -132,  138, -218, -220, -157,  139,    0,    0,    0,
  -80,    0,    0, -113,    0,    0,    0,  123,   36,   36,
    0,    0,    0,    0,  -84,  -18,    0,    0,  142,    0,
  144, -207,    0,  127,    0,  128,    0,    0,    0,  129,
  130,    0,  131,    0,    0,  132, -102,    0,    0,    0,
    0,    0,    0,    0,  -18,  133,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -71,    0,    0,    0,    0,  194,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -156,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -66,    0,    0,    0,    0,  -39,    0,    0,
    0,  -36,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  137,    0,    0,    0,    0,    0,    0,  -64,
    0,  -71,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -16,    0,    0,    0,    0,    0,    0,  -31,  -30,
    0,    0,    0,    0,    0,  157,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  158,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  152,  196,   11,    0,    0,    0,    0,    0,    0,
    0,  145,    0,  -29,   21,    0,  163,    0,    0,    0,
    0,   -7,    0,   -6,    0,   39,    0,   63,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    5,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=272;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         66,
   62,   54,   54,   54,   44,   54,   44,   54,   44,   42,
   43,   42,   43,   42,   43,   98,  100,   99,   98,   54,
   99,   33,   44,  109,   98,   19,   99,   42,   43,   70,
   70,   75,   53,   93,   36,  127,   36,   42,   68,   70,
   83,   55,   74,   79,   69,   62,   29,  128,  146,   52,
   12,   37,   91,   48,   38,   13,   54,   80,   14,  147,
   93,   32,   15,    2,   16,  112,   12,   92,   19,   10,
  113,   13,   13,   11,   14,   14,   39,  102,   15,   15,
   16,   16,  101,   94,   40,   67,   68,   43,   74,   68,
   70,   70,   69,   70,   70,   69,   70,  126,  129,  130,
   18,  110,   82,   68,   12,   42,   41,   12,   44,   69,
   94,  115,   13,  121,   68,   14,   84,   12,  138,   15,
   69,   16,   13,  123,   68,   14,   60,   61,    2,   15,
   69,   16,   45,   12,    2,    3,  119,  120,   13,   70,
  155,   14,   46,   12,   12,   15,   48,   16,   13,   13,
   56,   14,   14,  154,   68,   15,   15,   16,   16,    1,
   69,    2,    3,  122,  124,   47,   59,   77,   86,   87,
   88,   61,   89,   91,  105,  106,  116,  117,  125,  133,
  135,  139,  144,  142,  145,  148,  149,  150,  151,  152,
  153,  157,   38,    1,   16,   36,   19,   76,   81,   85,
   18,   78,   90,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   65,    0,   54,    0,    0,   44,
    0,    0,    0,    0,   42,   43,    0,   54,   97,    0,
   44,   97,    0,    0,    0,   42,   43,   97,  108,  104,
   28,    0,    0,    0,    0,   28,    0,    0,   28,    0,
    0,    0,   28,    0,   28,    0,   62,   62,    0,    0,
    0,   62,   62,    0,    0,   62,    0,    0,    0,   62,
    0,   62,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   42,   43,   41,   45,   43,   47,   45,   41,
   41,   43,   43,   45,   45,   43,   41,   45,   43,   59,
   45,   11,   59,   41,   43,    5,   45,   59,   59,   37,
   38,   38,   28,   63,   40,  256,   40,   17,  257,   47,
   47,   44,   59,  256,  263,   35,  257,  268,  256,  256,
  257,   40,   58,  260,   40,  262,   59,  270,  265,  267,
   90,  256,  269,  258,  271,  256,  257,  257,   48,  256,
  261,  262,  262,  260,  265,  265,   40,   42,  269,  269,
  271,  271,   47,   63,   40,  256,  257,   59,  256,  257,
   98,   99,  263,  101,  102,  263,  104,  104,  256,  257,
  257,   81,  256,  257,  261,   85,  256,  257,   59,  263,
   90,   91,  262,  256,  257,  265,  256,  257,  114,  269,
  263,  271,  262,  256,  257,  265,  256,  257,  258,  269,
  263,  271,   59,  257,  258,  259,   98,   99,  262,  147,
  147,  265,   59,  257,  257,  269,  260,  271,  262,  262,
  261,  265,  265,  256,  257,  269,  269,  271,  271,  256,
  263,  258,  259,  101,  102,  264,  261,  257,  266,   59,
  257,  257,  261,   58,   41,   59,   59,   59,   41,   41,
  261,   59,   41,  268,   41,   59,   59,   59,   59,   59,
   59,   59,  264,    0,  261,   59,  261,   41,   41,   48,
    5,   39,   58,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,   -1,  256,   -1,   -1,  256,
   -1,   -1,   -1,   -1,  256,  256,   -1,  267,  256,   -1,
  267,  256,   -1,   -1,   -1,  267,  267,  256,  256,  267,
  257,   -1,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,
   -1,   -1,  269,   -1,  271,   -1,  256,  257,   -1,   -1,
   -1,  261,  262,   -1,   -1,  265,   -1,   -1,   -1,  269,
   -1,  271,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=272;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,"ID","UINT","FUNCTION","BEGIN","END","RETURN","CTE",
"ASIGN","IF","ELSE","COMP","THEN","FOR","STR","PRINT","IT",
};
final static String yyrule[] = {
"$accept : Programa",
"Programa : Declaraciones Sentencias",
"Programa : error",
"Declaraciones : Declaraciones Declaracion",
"Declaraciones : Declaracion",
"Declaracion : DeclaracionVariables",
"Declaracion : DeclaracionFunciones",
"DeclaracionVariables : Tipo ListaVariables ';'",
"Tipo : UINT",
"ListaVariables : ListaVariables ',' ID",
"ListaVariables : ID",
"DeclaracionFunciones : FUNCTION BEGIN CuerpoFuncion END",
"$$1 :",
"DeclaracionFunciones : FUNCTION BEGIN error $$1 END",
"$$2 :",
"DeclaracionFunciones : FUNCTION error $$2 END",
"CuerpoFuncion : DeclaracionesFuncion InstanciasFuncion",
"CuerpoFuncion : DeclaracionesFuncion error",
"$$3 :",
"CuerpoFuncion : error $$3 InstanciasFuncion",
"DeclaracionesFuncion : DeclaracionesFuncion DeclaracionVariables",
"DeclaracionesFuncion : DeclaracionVariables",
"InstanciasFuncion : PuntoEntrada",
"InstanciasFuncion : InstanciasFuncion PuntoEntrada",
"InstanciasFuncion : InstanciasFuncion Sentencia",
"PuntoEntrada : ID ':' Sentencia",
"Sentencias : Sentencias Sentencia",
"Sentencias : Sentencia",
"$$4 :",
"Sentencias : Sentencias error $$4 Sentencia",
"Sentencia : Asignacion ';'",
"Sentencia : Seleccion",
"Sentencia : Iteracion",
"Sentencia : SalidaPantalla ';'",
"Sentencia : Return ';'",
"Sentencia : LlamadaFn ';'",
"Asignacion : AsigIzq ASIGN Expresion",
"Asignacion : AsigIzq ASIGN error",
"AsigIzq : ID",
"Return : RETURN '(' Expresion ')'",
"$$5 :",
"Return : RETURN '(' error $$5 ';'",
"Expresion : Expresion '+' Termino",
"Expresion : Expresion '-' Termino",
"Expresion : Termino",
"$$6 :",
"Expresion : Expresion error $$6 ';'",
"Termino : Termino '*' Factor",
"Termino : Termino '/' Factor",
"Termino : Factor",
"$$7 :",
"Termino : Termino '*' error $$7 ';'",
"$$8 :",
"Termino : Termino '/' error $$8 ';'",
"Factor : ID",
"Factor : CTE",
"Factor : LlamadaFn",
"LlamadaFn : ID '(' ')'",
"$$9 :",
"LlamadaFn : ID '(' error $$9 ';'",
"$$10 :",
"Seleccion : CabeceraIf BloqueThen ELSE $$10 BloqueElse",
"Seleccion : CabeceraIf BloqueThen",
"CabeceraIf : IF '(' Comparacion ')' THEN",
"$$11 :",
"CabeceraIf : IF '(' error $$11 ')' THEN",
"$$12 :",
"CabeceraIf : IF '(' Comparacion ')' error $$12 ';'",
"BloqueThen : BloqueSentencias",
"BloqueElse : BloqueSentencias",
"BloqueSentencias : Sentencia",
"BloqueSentencias : BEGIN Sentencias END",
"$$13 :",
"BloqueSentencias : BEGIN error $$13 END",
"$$14 :",
"BloqueSentencias : BEGIN Sentencias error $$14 ';'",
"Comparacion : Expresion COMP Expresion",
"Iteracion : CabeceraIteracion BloqueSentencias",
"Iteracion : CabeceraIteracion error ';'",
"CabeceraIteracion : FOR '(' Asignacion ';' Comparacion2 ')'",
"CabeceraIteracion : FOR '(' Asignacion ';' error ')'",
"Comparacion2 : Comp2Izq COMP Expresion",
"$$15 :",
"Comparacion2 : Comp2Izq COMP error $$15 ';'",
"Comparacion2 : Comp2Izq error ';'",
"Comp2Izq : ID",
"SalidaPantalla : PRINT '(' STR ')'",
"$$16 :",
"SalidaPantalla : PRINT '(' error $$16 ')'",
"$$17 :",
"SalidaPantalla : PRINT '(' STR error $$17 ';'",
};

//#line 196 "/home/lucas/workspace/Compilador/sources/Gramatica.y"

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
//#line 497 "Parser.java"
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
case 2:
//#line 33 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("error sintactico no identificado");}
break;
case 7:
//#line 44 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("declaracion de variables");}
break;
case 11:
//#line 54 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("Declaracion de funcion");}
break;
case 12:
//#line 55 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("el cuerpo de la funcion esta mal definido");}
break;
case 14:
//#line 56 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("falta el begin");}
break;
case 17:
//#line 61 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("error en una de las instancias de la funcion");}
break;
case 18:
//#line 62 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("error en las declaraciones de funcion");}
break;
case 28:
//#line 80 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("Sentencia mal declarada o invalida");}
break;
case 30:
//#line 83 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("Asignacion");}
break;
case 31:
//#line 84 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("Sentencia If");}
break;
case 32:
//#line 85 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("Iteracion For");}
break;
case 33:
//#line 86 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("Salida por pantalla Print");}
break;
case 34:
//#line 87 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("Sentencia Return");}
break;
case 36:
//#line 92 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
			pila.push(val_peek(1).sval);	
						}
break;
case 37:
//#line 95 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("se produjo error en la asignacion");}
break;
case 38:
//#line 98 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.push(val_peek(0).sval);}
break;
case 40:
//#line 102 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("sentencia return mal definida");}
break;
case 42:
//#line 105 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
			pila.push('+');
		}
break;
case 43:
//#line 108 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
	   		pila.push('-');
   		}
break;
case 45:
//#line 112 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("Expresion mal definida");}
break;
case 47:
//#line 115 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
		pila.push('*');
	   }
break;
case 48:
//#line 118 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
	 	pila.push('/');
	 	}
break;
case 50:
//#line 122 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("termino mal definico");}
break;
case 52:
//#line 123 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("termino mal definido");}
break;
case 54:
//#line 126 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{yyval = val_peek(0);
		pila.push(val_peek(0).sval);}
break;
case 55:
//#line 128 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{yyval = val_peek(0);
		 pila.push(val_peek(0).sval);}
break;
case 57:
//#line 133 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{agregarRegla("llamada a funcion");}
break;
case 58:
//#line 134 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("falta el caracter de cierre ')'. Recuerde que las llamadas a funcion no lleva argumentos");}
break;
case 60:
//#line 138 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
	    	pila.setSaltoPrevio(2);
	    	pila.nuevoSalto("BI");
	    }
break;
case 61:
//#line 142 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.setSaltoPrevio(0);}
break;
case 62:
//#line 143 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.setSaltoPrevio(0);}
break;
case 63:
//#line 147 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
	    	pila.nuevoSalto("BF");
	    }
break;
case 64:
//#line 150 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("se produjo un error en la comparacion");}
break;
case 66:
//#line 151 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("no se encontro la palabra reservada \"then\"");}
break;
case 72:
//#line 162 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("error sintactico dentro del bloque de codigo");}
break;
case 74:
//#line 163 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("no se encontro el cierre de bloque de codigo \"end\"");}
break;
case 76:
//#line 168 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{
			pila.push(val_peek(1).sval);
		}
break;
case 77:
//#line 174 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.nuevoSalto("BI"); pila.setSaltoPrevio(pila.getLastFlag()); pila.setSaltoPrevio(0);}
break;
case 79:
//#line 178 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.nuevoSalto("BF");}
break;
case 81:
//#line 182 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.push(val_peek(1).sval);}
break;
case 82:
//#line 183 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("del lado derecho de la comparacion debe ir una expresion");}
break;
case 85:
//#line 187 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.setFlag(); pila.push(val_peek(0).sval);}
break;
case 86:
//#line 190 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{pila.push(val_peek(1).sval); pila.push(val_peek(3).sval);}
break;
case 87:
//#line 191 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("falta cadena de caracteres o se escrio mal");}
break;
case 89:
//#line 192 "/home/lucas/workspace/Compilador/sources/Gramatica.y"
{guardarError("la sentencia print se debe cerrar con el caracter especial ')'");}
break;
//#line 845 "Parser.java"
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
