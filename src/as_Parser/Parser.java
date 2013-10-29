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






//#line 2 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
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
   11,   12,   12,   14,   14,   15,   17,    2,    2,   18,
    2,   16,   16,   16,   16,   16,   16,   19,   19,   25,
   23,   27,   23,   26,   26,   26,   29,   26,   28,   28,
   28,   31,   28,   32,   28,   30,   30,   30,   24,   33,
   24,   37,   20,   20,   34,   39,   34,   40,   34,   35,
   36,   41,   41,   42,   41,   43,   41,   38,   21,   21,
   44,   44,   45,   47,   45,   45,   46,   22,   48,   22,
   49,   22,
};
final static short yylen[] = {                            2,
    2,    1,    2,    1,    1,    1,    3,    1,    3,    1,
    4,    0,    5,    0,    4,    2,    2,    0,    3,    2,
    1,    2,    1,    1,    1,    2,    2,    2,    1,    0,
    4,    2,    1,    1,    2,    2,    2,    3,    3,    1,
    4,    0,    5,    3,    3,    1,    0,    4,    3,    3,
    1,    0,    5,    0,    5,    1,    1,    1,    3,    0,
    5,    0,    5,    2,    5,    0,    6,    0,    7,    1,
    1,    1,    3,    0,    4,    0,    5,    3,    2,    3,
    6,    6,    3,    0,    5,    3,    1,    4,    0,    5,
    0,    6,
};
final static short yydefred[] = {                         0,
    2,    8,    0,    0,    0,    4,    5,    6,    0,   14,
    0,    0,    0,    0,    0,    0,    0,    3,   29,    0,
   33,   34,    0,    0,    0,    0,    0,    0,   10,    0,
    0,    0,   21,    0,    0,    0,    0,    0,    0,    0,
   30,   28,   32,   35,   36,   37,    0,    0,   72,    0,
   70,    0,   79,    7,    0,   15,    0,    0,   11,   17,
    0,   20,    0,   23,   24,   25,    0,   60,   59,   42,
    0,   57,   58,    0,    0,   51,   66,    0,    0,   40,
    0,   89,    0,    0,   39,    0,   74,    0,   62,   80,
    9,   13,    0,   27,   22,   26,    0,    0,   47,    0,
    0,   41,    0,    0,    0,    0,    0,    0,    0,   91,
   88,   31,    0,    0,   73,    0,   61,   43,    0,    0,
    0,   54,   50,   52,   49,    0,    0,   68,   65,    0,
   87,    0,    0,   90,    0,   75,    0,   63,   71,   48,
    0,    0,   67,    0,   82,   81,    0,    0,   92,   77,
   55,   53,   69,   86,   84,    0,    0,   85,
};
final static short yydgoto[] = {                          4,
    5,   17,    6,    7,    8,    9,   30,   34,   57,   31,
   35,   63,   58,   64,   65,   66,   67,   84,   20,   21,
   22,   23,   24,   25,   26,   74,   98,   75,  119,   76,
  142,  141,   97,   27,   50,  138,  116,   79,  105,  144,
   51,  113,  137,   28,  132,  133,  157,  109,  135,
};
final static short yysindex[] = {                       -89,
    0,    0, -202,    0, -149,    0,    0,    0, -154,    0,
 -173,   -1,   87,   94,  106,  115, -167,    0,    0,   52,
    0,    0,  104,  114,  116,  -90, -134, -210,    0,   -8,
  -96,    0,    0,  -85, -194,  -41, -187, -176,  -80, -214,
    0,    0,    0,    0,    0,    0, -107, -150,    0,  -88,
    0,  120,    0,    0,  -77,    0,  -79, -133,    0,    0,
   -5,    0, -133,    0,    0,    0, -117,    0,    0,    0,
   -1,    0,    0,  -24,   83,    0,    0,  -27,  140,    0,
  124,    0,  -17, -117,    0,  -18,    0, -183,    0,    0,
    0,    0, -133,    0,    0,    0,  125,  126,    0, -191,
 -191,    0,  -99,  -97,  145, -191, -208, -165,  146,    0,
    0,    0,  -73,    0,    0, -134,    0,    0,  130,   83,
   83,    0,    0,    0,    0,  -78,  -18,    0,    0,  150,
    0,  151, -218,    0,  134,    0,  135,    0,    0,    0,
  136,  137,    0,  138,    0,    0,  139,  -95,    0,    0,
    0,    0,    0,    0,    0,  -18,  141,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -65,    0,    0,    0,    0,  201,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -118,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -65,    0,  -59,    0,    0,    0,    0,    0,    0,    0,
  -39,    0,    0,    0,  -36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  144,    0,    0,    0,    0,
    0,    0,  -57,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -16,    0,    0,    0,    0,    0,  -31,
  -30,    0,    0,    0,    0,    0,  164,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  165,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  159,  203,   22,    0,    0,    0,    0,    0,    0,
    0,  152,    0,  -26,    0,   17,    0,    0,  170,    0,
    0,    0,    0,   -7,    0,   -6,    0,   71,    0,   14,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -2,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=272;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         69,
   64,   56,   56,   56,   46,   56,   46,   56,   46,   44,
   45,   44,   45,   44,   45,  100,  102,  101,  100,   56,
  101,   19,   46,  111,  100,   53,  101,   44,   45,   73,
   73,   78,   33,   42,   36,   55,   95,  147,   36,   73,
   86,   82,   76,   49,   49,   52,   12,  128,  148,   48,
   54,   13,   94,   10,   14,   83,   62,   11,   15,  129,
   16,   60,   61,    2,   19,   71,   95,   13,   70,   71,
   14,   72,  114,   12,   15,   72,   16,  115,   13,   77,
   71,   14,   32,   96,    2,   15,   72,   16,   41,   12,
  130,  131,   73,   73,   13,   73,   73,   14,   73,  127,
  112,   15,   29,   16,   42,   87,   12,   12,    2,    3,
   43,   13,   13,  139,   14,   14,  123,  125,   15,   15,
   16,   16,   12,   61,  104,   48,   37,   13,   13,  103,
   14,   14,   49,   38,   15,   15,   16,   16,   18,   12,
   73,  156,   12,   18,   13,   39,   18,   14,   85,   71,
   18,   15,   18,   16,   40,   72,  122,   71,  124,   71,
  155,   71,   44,   72,   56,   72,    1,   72,    2,    3,
  120,  121,   45,   47,   46,   59,   80,   89,   90,   91,
  107,   92,  108,  117,  118,  126,  134,  136,  140,  143,
  145,  146,  149,  150,  151,  152,  153,  154,   40,  158,
    1,   16,   38,   19,   78,   83,   88,   18,   81,   93,
    0,    0,    0,    0,   68,    0,   56,    0,    0,   46,
    0,    0,    0,    0,   44,   45,    0,   56,   99,    0,
   46,   99,    0,    0,    0,   44,   45,   99,  110,  106,
   30,    0,    0,    0,    0,   30,    0,    0,   30,    0,
    0,    0,   30,    0,   30,    0,   64,   64,    0,    0,
    0,   64,   64,    0,    0,   64,    0,    0,    0,   64,
    0,   64,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   42,   43,   41,   45,   43,   47,   45,   41,
   41,   43,   43,   45,   45,   43,   41,   45,   43,   59,
   45,    5,   59,   41,   43,   28,   45,   59,   59,   37,
   38,   38,   11,   17,   40,   44,   63,  256,   40,   47,
   47,  256,   59,   27,   28,  256,  257,  256,  267,  260,
   59,  262,   58,  256,  265,  270,   35,  260,  269,  268,
  271,  256,  257,  258,   48,  257,   93,  262,  256,  257,
  265,  263,  256,  257,  269,  263,  271,  261,  262,  256,
  257,  265,  256,   67,  258,  269,  263,  271,  256,  257,
  256,  257,  100,  101,  262,  103,  104,  265,  106,  106,
   84,  269,  257,  271,   88,  256,  257,  257,  258,  259,
   59,  262,  262,  116,  265,  265,  103,  104,  269,  269,
  271,  271,  257,  257,   42,  260,   40,  262,  262,   47,
  265,  265,  116,   40,  269,  269,  271,  271,  257,  257,
  148,  148,  261,  262,  262,   40,  265,  265,  256,  257,
  269,  269,  271,  271,   40,  263,  256,  257,  256,  257,
  256,  257,   59,  263,  261,  263,  256,  263,  258,  259,
  100,  101,   59,  264,   59,  261,  257,  266,   59,  257,
   41,  261,   59,   59,   59,   41,   41,  261,   59,  268,
   41,   41,   59,   59,   59,   59,   59,   59,  264,   59,
    0,  261,   59,  261,   41,   41,   48,    5,   39,   58,
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
"InstanciasFuncion : InstanciasFuncion InstanciaFuncion",
"InstanciasFuncion : InstanciaFuncion",
"InstanciaFuncion : PuntoEntrada",
"InstanciaFuncion : Sentencia",
"PuntoEntrada : CabeceraPE Sentencia",
"CabeceraPE : ID ':'",
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

//#line 210 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"

public Pila pila;
Vector<Vector<String>> lista;
AnalizadorLexico an; 
Vector<String> errores;
Vector<String> reglas;
TablaSimbolo _tds;
boolean pEFlag = false;
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

public void setPEFlag(){
	if(pEFlag)
		pila.push(":");
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
//#line 508 "Parser.java"
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
//#line 33 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("error sintactico no identificado");}
break;
case 7:
//#line 44 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{agregarRegla("declaracion de variables");}
break;
case 11:
//#line 54 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{setPEFlag(); agregarRegla("Declaracion de funcion");}
break;
case 12:
//#line 55 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("el cuerpo de la funcion esta mal definido");}
break;
case 14:
//#line 56 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("falta el begin");}
break;
case 17:
//#line 61 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("error en una de las instancias de la funcion");}
break;
case 18:
//#line 62 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("error en las declaraciones de funcion");}
break;
case 27:
//#line 80 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{setPEFlag(); pila.push(val_peek(1).sval); pila.push("HEADER");}
break;
case 30:
//#line 85 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("Sentencia mal declarada o invalida");}
break;
case 32:
//#line 88 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{agregarRegla("Asignacion");}
break;
case 33:
//#line 89 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{agregarRegla("Sentencia If");}
break;
case 34:
//#line 90 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{agregarRegla("Iteracion For");}
break;
case 35:
//#line 91 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{agregarRegla("Salida por pantalla Print");}
break;
case 38:
//#line 97 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
			pila.push(val_peek(1).sval);	
						}
break;
case 39:
//#line 100 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("se produjo error en la asignacion");}
break;
case 40:
//#line 103 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{pila.push(val_peek(0).sval);}
break;
case 41:
//#line 106 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{pila.push("RETURN");}
break;
case 42:
//#line 107 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("sentencia return mal definida");}
break;
case 44:
//#line 110 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
			pila.push('+');
		}
break;
case 45:
//#line 113 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
	   		pila.push('-');
   		}
break;
case 47:
//#line 117 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("Expresion mal definida");}
break;
case 49:
//#line 120 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
		pila.push('*');
	   }
break;
case 50:
//#line 123 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
	 	pila.push('/');
	 	}
break;
case 52:
//#line 127 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("termino mal definico");}
break;
case 54:
//#line 128 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("termino mal definido");}
break;
case 56:
//#line 131 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
		yyval = val_peek(0);
		pila.push(val_peek(0).sval);}
break;
case 57:
//#line 134 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
		yyval = val_peek(0);
		pila.push(val_peek(0).sval);}
break;
case 58:
//#line 137 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{pila.push("CALL");}
break;
case 59:
//#line 140 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
		agregarRegla("llamada a funcion");
		pila.push(val_peek(2).sval);}
break;
case 60:
//#line 143 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("falta el caracter de cierre ')'. Recuerde que las llamadas a funcion no lleva argumentos");}
break;
case 62:
//#line 147 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
	    	pila.setSaltoPrevio(2);
	    	pila.nuevoSalto("BI");
	    }
break;
case 63:
//#line 151 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{pila.setSaltoPrevio(0);}
break;
case 64:
//#line 152 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{pila.setSaltoPrevio(0);}
break;
case 65:
//#line 156 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
	    	pila.nuevoSalto("BF");
	    }
break;
case 66:
//#line 159 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("se produjo un error en la comparacion");}
break;
case 68:
//#line 160 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("no se encontro la palabra reservada \"then\"");}
break;
case 74:
//#line 171 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("error sintactico dentro del bloque de codigo");}
break;
case 76:
//#line 172 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("no se encontro el cierre de bloque de codigo \"end\"");}
break;
case 78:
//#line 177 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
			pila.push(val_peek(1).sval);
		}
break;
case 79:
//#line 183 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{
			
			pila.nuevoSalto("BI");
			pila.setSaltoPrevio(pila.getLastFlag());
			pila.setSaltoPrevio(0);
		}
break;
case 81:
//#line 192 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{pila.nuevoSalto("BF");}
break;
case 83:
//#line 196 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{yyval = val_peek(2); pila.push(val_peek(1).sval);}
break;
case 84:
//#line 197 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("del lado derecho de la comparacion debe ir una expresion");}
break;
case 87:
//#line 201 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{yyval = val_peek(0); pila.setFlag(); pila.push(val_peek(0).sval);}
break;
case 88:
//#line 204 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{pila.push(val_peek(1).sval); pila.push("PRINT");}
break;
case 89:
//#line 205 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("falta cadena de caracteres o se escrio mal");}
break;
case 91:
//#line 206 "/home/lucas/workspace/Compilador2/sources/Gramatica.y"
{guardarError("la sentencia print se debe cerrar con el caracter especial ')'");}
break;
//#line 873 "Parser.java"
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
