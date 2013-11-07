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






//#line 2 "G.y"
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
    3,    0,    0,    1,    1,    4,    4,    5,    7,    8,
    8,    6,   10,    6,   11,    6,    9,    9,   14,    9,
   12,   13,   13,   15,   15,   16,   18,    2,    2,   19,
    2,   17,   17,   17,   17,   17,   17,   20,   20,   26,
   24,   28,   24,   27,   27,   27,   30,   27,   29,   29,
   29,   32,   29,   33,   29,   31,   31,   31,   25,   34,
   25,   38,   21,   21,   35,   40,   35,   41,   35,   36,
   37,   42,   42,   43,   42,   44,   42,   39,   22,   22,
   45,   45,   46,   48,   46,   46,   47,   23,   49,   23,
   50,   23,
};
final static short yylen[] = {                            2,
    0,    3,    1,    2,    1,    1,    1,    3,    1,    3,
    1,    4,    0,    5,    0,    4,    2,    2,    0,    3,
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
    3,    9,    0,    0,    0,    5,    6,    7,    0,   15,
    0,    0,    4,   11,    0,    0,    0,   21,    0,    0,
    0,    0,    0,    0,    0,    0,   29,    0,   33,   34,
    0,    0,    0,    0,    0,    0,    8,    0,   16,    0,
    0,   12,   18,    0,    0,   23,   24,   25,    0,    0,
    0,    0,    0,    0,   30,   28,   32,   35,   36,   37,
    0,    0,   72,    0,   70,    0,   79,   10,   14,    0,
   27,   22,   26,   60,   59,   42,    0,   57,   58,    0,
    0,   51,   66,    0,    0,   40,    0,   89,    0,    0,
   39,    0,   74,    0,   62,   80,    0,    0,   47,    0,
    0,   41,    0,    0,    0,    0,    0,    0,    0,   91,
   88,   31,    0,    0,   73,    0,   61,   43,    0,    0,
    0,   54,   50,   52,   49,    0,    0,   68,   65,    0,
   87,    0,    0,   90,    0,   75,    0,   63,   71,   48,
    0,    0,   67,    0,   82,   81,    0,    0,   92,   77,
   55,   53,   69,   86,   84,    0,    0,   85,
};
final static short yydgoto[] = {                          4,
    5,   26,   12,    6,    7,    8,    9,   15,   19,   40,
   16,   20,   45,   41,   46,   47,   48,   49,   90,   28,
   29,   30,   31,   32,   33,   34,   80,   98,   81,  119,
   82,  142,  141,   97,   35,   64,  138,  116,   85,  105,
  144,   65,  113,  137,   36,  132,  133,  157,  109,  135,
};
final static short yysindex[] = {                      -138,
    0,    0, -182,    0, -120,    0,    0,    0, -180,    0,
 -161, -200,    0,    0,   -9, -174,    0,    0, -170, -181,
   56,   59,   74,   82,   85, -164,    0,   72,    0,    0,
   90,   98,  107, -131, -128, -218,    0,  -83,    0,  -86,
 -107,    0,    0,   -6, -107,    0,    0,    0, -200,  -41,
 -147, -144,  -81, -207,    0,    0,    0,    0,    0,    0,
 -133, -154,    0,  -89,    0,  119,    0,    0,    0, -107,
    0,    0,    0,    0,    0,    0,   56,    0,    0,  -24,
   47,    0,    0,  -27,  138,    0,  121,    0,  -17, -200,
    0,  -18,    0, -201,    0,    0,  122,  123,    0, -190,
 -190,    0, -121, -110,  142, -190, -202,  -88,  143,    0,
    0,    0,  -76,    0,    0, -128,    0,    0,  127,   47,
   47,    0,    0,    0,    0,  -80,  -18,    0,    0,  146,
    0,  148, -219,    0,  131,    0,  132,    0,    0,    0,
  133,  134,    0,  135,    0,    0,  136,  -96,    0,    0,
    0,    0,    0,    0,    0,  -18,  137,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0, -106,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -117,    0,    0,    0,
  -67,    0,    0,    0,    0,  198,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -67,  -62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    1,    0,    0,    0,    0,    0,  -61,
    0,    0,    0,    0,    0,    0,  -39,    0,    0,    0,
  -36,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  144,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -16,    0,    0,    0,    0,    0,  -31,
  -30,    0,    0,    0,    0,    0,  160,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  161,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  145,    0,  199,  194,    0,    0,    0,    0,    0,
    0,    0,  165,    0,  -12,    0,   10,    0,    0,  155,
    0,    0,    0,    0,  -21,    0,  -20,    0,   70,    0,
   69,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -10,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=272;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         75,
   64,   56,   56,   56,   46,   56,   46,   56,   46,   44,
   45,   44,   45,   44,   45,  100,  102,  101,  100,   56,
  101,   27,   46,  111,  100,   67,  101,   44,   45,   79,
   79,   84,   72,   50,   38,   56,  147,   66,   21,   79,
   92,   62,   76,   22,   63,   63,   23,  148,   88,   37,
   24,   71,   25,  128,  114,   21,   21,   72,   73,  115,
   22,   22,   89,   23,   23,  129,   77,   24,   24,   25,
   25,   27,   78,   10,   43,   44,   14,   11,   79,   79,
   22,   79,   79,   23,   79,  127,   39,   24,  104,   25,
   42,   55,   21,  103,   17,   50,    2,   22,   51,  112,
   23,   93,   21,   56,   24,  139,   25,   22,   76,   77,
   23,   83,   77,   52,   24,   78,   25,    1,   78,    2,
    3,   53,   91,   77,   54,   63,   79,  156,   21,   78,
   57,   62,   61,   22,  122,   77,   23,    2,    3,   19,
   24,   78,   25,   13,   19,  124,   77,   19,   58,   44,
    1,   19,   78,   19,   22,    1,   59,   23,    1,  155,
   77,   24,    1,   25,    1,   60,   78,  130,  131,  120,
  121,  123,  125,   68,   69,   86,   95,   96,  107,  108,
  117,  118,  126,  134,  136,  140,  145,  143,  146,  149,
  150,  151,  152,  153,  154,  158,   40,    2,   17,   20,
   78,   83,   38,   13,   18,   70,   94,   87,    0,    0,
    0,    0,    0,    0,   74,    0,   56,    0,    0,   46,
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
   45,   12,   59,   41,   43,   36,   45,   59,   59,   51,
   52,   52,   45,   40,   44,   26,  256,  256,  257,   61,
   61,  260,   59,  262,   35,   36,  265,  267,  256,   59,
  269,   58,  271,  256,  256,  257,  257,   70,   49,  261,
  262,  262,  270,  265,  265,  268,  257,  269,  269,  271,
  271,   62,  263,  256,  256,  257,  257,  260,  100,  101,
  262,  103,  104,  265,  106,  106,  261,  269,   42,  271,
  261,  256,  257,   47,  256,   40,  258,  262,   40,   90,
  265,  256,  257,   94,  269,  116,  271,  262,  256,  257,
  265,  256,  257,   40,  269,  263,  271,  256,  263,  258,
  259,   40,  256,  257,   40,  116,  148,  148,  257,  263,
   59,  260,  264,  262,  256,  257,  265,  258,  259,  257,
  269,  263,  271,  261,  262,  256,  257,  265,   59,  257,
  257,  269,  263,  271,  262,  262,   59,  265,  265,  256,
  257,  269,  269,  271,  271,   59,  263,  256,  257,  100,
  101,  103,  104,  257,  261,  257,  266,   59,   41,   59,
   59,   59,   41,   41,  261,   59,   41,  268,   41,   59,
   59,   59,   59,   59,   59,   59,  264,    0,  261,  261,
   41,   41,   59,    5,   11,   41,   62,   53,   -1,   -1,
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
"$$1 :",
"Programa : Declaraciones $$1 Sentencias",
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
"$$2 :",
"DeclaracionFunciones : FUNCTION BEGIN error $$2 END",
"$$3 :",
"DeclaracionFunciones : FUNCTION error $$3 END",
"CuerpoFuncion : DeclaracionesFuncion InstanciasFuncion",
"CuerpoFuncion : DeclaracionesFuncion error",
"$$4 :",
"CuerpoFuncion : error $$4 InstanciasFuncion",
"DeclaracionesFuncion : DeclaracionVariables",
"InstanciasFuncion : InstanciasFuncion InstanciaFuncion",
"InstanciasFuncion : InstanciaFuncion",
"InstanciaFuncion : PuntoEntrada",
"InstanciaFuncion : Sentencia",
"PuntoEntrada : CabeceraPE Sentencia",
"CabeceraPE : ID ':'",
"Sentencias : Sentencias Sentencia",
"Sentencias : Sentencia",
"$$5 :",
"Sentencias : Sentencias error $$5 Sentencia",
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
"$$6 :",
"Return : RETURN '(' error $$6 ';'",
"Expresion : Expresion '+' Termino",
"Expresion : Expresion '-' Termino",
"Expresion : Termino",
"$$7 :",
"Expresion : Expresion error $$7 ';'",
"Termino : Termino '*' Factor",
"Termino : Termino '/' Factor",
"Termino : Factor",
"$$8 :",
"Termino : Termino '*' error $$8 ';'",
"$$9 :",
"Termino : Termino '/' error $$9 ';'",
"Factor : ID",
"Factor : CTE",
"Factor : LlamadaFn",
"LlamadaFn : ID '(' ')'",
"$$10 :",
"LlamadaFn : ID '(' error $$10 ';'",
"$$11 :",
"Seleccion : CabeceraIf BloqueThen ELSE $$11 BloqueElse",
"Seleccion : CabeceraIf BloqueThen",
"CabeceraIf : IF '(' Comparacion ')' THEN",
"$$12 :",
"CabeceraIf : IF '(' error $$12 ')' THEN",
"$$13 :",
"CabeceraIf : IF '(' Comparacion ')' error $$13 ';'",
"BloqueThen : BloqueSentencias",
"BloqueElse : BloqueSentencias",
"BloqueSentencias : Sentencia",
"BloqueSentencias : BEGIN Sentencias END",
"$$14 :",
"BloqueSentencias : BEGIN error $$14 END",
"$$15 :",
"BloqueSentencias : BEGIN Sentencias error $$15 ';'",
"Comparacion : Expresion COMP Expresion",
"Iteracion : CabeceraIteracion BloqueSentencias",
"Iteracion : CabeceraIteracion error ';'",
"CabeceraIteracion : FOR '(' Asignacion ';' Comparacion2 ')'",
"CabeceraIteracion : FOR '(' Asignacion ';' error ')'",
"Comparacion2 : Comp2Izq COMP Expresion",
"$$16 :",
"Comparacion2 : Comp2Izq COMP error $$16 ';'",
"Comparacion2 : Comp2Izq error ';'",
"Comp2Izq : ID",
"SalidaPantalla : PRINT '(' STR ')'",
"$$17 :",
"SalidaPantalla : PRINT '(' error $$17 ')'",
"$$18 :",
"SalidaPantalla : PRINT '(' STR error $$18 ';'",
};

//#line 258 "G.y"

public Pila pila;
Vector<Vector<String>> lista;
AnalizadorLexico an; 
Vector<String> errores;
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
//#line 510 "Parser.java"
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
//#line 32 "G.y"
{
					alcance = "Main";
					pila.push("MAIN");
					}
break;
case 3:
//#line 36 "G.y"
{guardarError("error sintactico no identificado");}
break;
case 6:
//#line 43 "G.y"
{
			for(ParserVal a : val_peek(0).list){
				if(_tds.idDeclared(a.sval, "Main"))
					guardarError("El identificador "+a.sval+" ya ha sido declarado");
				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
				tupla.set_scope("Main");
			}
		}
break;
case 8:
//#line 54 "G.y"
{
				for(ParserVal a : val_peek(1).list){
	  				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
	  				tupla.set_type(val_peek(2).sval);
	  				tupla.set_use("Variable");
				}
				yyval = val_peek(1);				  
				agregarRegla("declaracion de variables");}
break;
case 9:
//#line 64 "G.y"
{yyval = val_peek(0);}
break;
case 10:
//#line 67 "G.y"
{val_peek(2).addToList(val_peek(0)); yyval = val_peek(2);}
break;
case 11:
//#line 68 "G.y"
{yyval.addToList(val_peek(0));}
break;
case 12:
//#line 71 "G.y"
{setPEFlag(); agregarRegla("Declaracion de funcion");alcance = "Main";}
break;
case 13:
//#line 72 "G.y"
{guardarError("el cuerpo de la funcion esta mal definido");}
break;
case 15:
//#line 73 "G.y"
{guardarError("falta el begin");}
break;
case 18:
//#line 78 "G.y"
{guardarError("error en una de las instancias de la funcion");}
break;
case 19:
//#line 79 "G.y"
{guardarError("error en las declaraciones de funcion");}
break;
case 21:
//#line 82 "G.y"
{
				for(ParserVal a : val_peek(0).list){
					if(_tds.idDeclared(a.sval, "funcion"))
						guardarError("El identificador "+a.sval+" ya ha sido declarado");
					_tds.changeScope(a.sval);
				}
			  }
break;
case 27:
//#line 102 "G.y"
{
			setPEFlag();
			pila.push(val_peek(1).sval);
			pila.push("HEADER");
			if(_tds.idDeclared(val_peek(1).sval, "Main"))
				guardarError("El identificador "+val_peek(1).sval+" ya ha sido declarado");
			TuplaTablaSimbolos tupla = _tds.getTupla(val_peek(1).sval);
			tupla.set_use("Punto de entrada");
			tupla._scope = "Main";
		}
break;
case 30:
//#line 116 "G.y"
{guardarError("Sentencia mal declarada o invalida");}
break;
case 32:
//#line 119 "G.y"
{agregarRegla("Asignacion");}
break;
case 33:
//#line 120 "G.y"
{agregarRegla("Sentencia If");}
break;
case 34:
//#line 121 "G.y"
{agregarRegla("Iteracion For");}
break;
case 35:
//#line 122 "G.y"
{agregarRegla("Salida por pantalla Print");}
break;
case 38:
//#line 128 "G.y"
{
				pila.push(val_peek(1).sval);	
				yyval = val_peek(2);
			}
break;
case 39:
//#line 132 "G.y"
{guardarError("se produjo error en la asignacion");}
break;
case 40:
//#line 135 "G.y"
{pila.push(val_peek(0).sval);
		if(!_tds.idDeclared(val_peek(0).sval, alcance))
			guardarError("La variable '"+val_peek(0).sval+"' no ha sido declarada");
		yyval = val_peek(0);
	}
break;
case 41:
//#line 142 "G.y"
{pila.push("RETURN");}
break;
case 42:
//#line 143 "G.y"
{guardarError("sentencia return mal definida");}
break;
case 44:
//#line 146 "G.y"
{
			pila.push('+');
		}
break;
case 45:
//#line 149 "G.y"
{
	   		pila.push('-');
   		}
break;
case 47:
//#line 153 "G.y"
{guardarError("Expresion mal definida");}
break;
case 49:
//#line 156 "G.y"
{
		pila.push('*');
	   }
break;
case 50:
//#line 159 "G.y"
{
	 	pila.push('/');
	 	}
break;
case 52:
//#line 163 "G.y"
{guardarError("termino mal definico");}
break;
case 54:
//#line 164 "G.y"
{guardarError("termino mal definido");}
break;
case 56:
//#line 167 "G.y"
{
		yyval = val_peek(0);
		pila.push(val_peek(0).sval);
		if(!_tds.idDeclared(val_peek(0).sval, alcance))
			guardarError("La variable '"+val_peek(0).sval+"' no ha sido declarada");
	}
break;
case 57:
//#line 173 "G.y"
{
		yyval = val_peek(0);
		pila.push(val_peek(0).sval);}
break;
case 59:
//#line 179 "G.y"
{
		agregarRegla("llamada a funcion");
		pila.push(val_peek(2).sval);
		pila.push("CALL");
		}
break;
case 60:
//#line 184 "G.y"
{guardarError("falta el caracter de cierre ')'. Recuerde que las llamadas a funcion no lleva argumentos");}
break;
case 62:
//#line 188 "G.y"
{
	    	pila.setSaltoPrevio(2);
	    	pila.nuevoSalto("BI");
	    }
break;
case 63:
//#line 192 "G.y"
{pila.setSaltoPrevio(0);}
break;
case 64:
//#line 193 "G.y"
{pila.setSaltoPrevio(0);}
break;
case 65:
//#line 197 "G.y"
{
	    	pila.nuevoSalto("BF");
	    }
break;
case 66:
//#line 200 "G.y"
{guardarError("se produjo un error en la comparacion");}
break;
case 68:
//#line 201 "G.y"
{guardarError("no se encontro la palabra reservada \"then\"");}
break;
case 74:
//#line 212 "G.y"
{guardarError("error sintactico dentro del bloque de codigo");}
break;
case 76:
//#line 213 "G.y"
{guardarError("no se encontro el cierre de bloque de codigo \"end\"");}
break;
case 78:
//#line 218 "G.y"
{
			pila.push(val_peek(1).sval);
		}
break;
case 79:
//#line 224 "G.y"
{
			pila.push(varFor);
			pila.push(varFor);
			pila.push("1");
			pila.push("+");
			pila.push("=");
			pila.nuevoSalto("BI");
			pila.setSaltoPrevio(pila.getLastFlag());
			pila.setSaltoPrevio(0);
		}
break;
case 81:
//#line 237 "G.y"
{pila.nuevoSalto("BF"); varFor = val_peek(3).sval;}
break;
case 83:
//#line 241 "G.y"
{yyval = val_peek(2); pila.push(val_peek(1).sval);}
break;
case 84:
//#line 242 "G.y"
{guardarError("del lado derecho de la comparacion debe ir una expresion");}
break;
case 87:
//#line 246 "G.y"
{yyval = val_peek(0); pila.setFlag(); pila.push(val_peek(0).sval);
			if(!_tds.idDeclared(val_peek(0).sval, alcance))
				guardarError("La variable '"+val_peek(0).sval+"' no ha sido declarada");	
		}
break;
case 88:
//#line 252 "G.y"
{pila.push(val_peek(1).sval); pila.push("PRINT");}
break;
case 89:
//#line 253 "G.y"
{guardarError("falta cadena de caracteres o se escrio mal");}
break;
case 91:
//#line 254 "G.y"
{guardarError("la sentencia print se debe cerrar con el caracter especial ')'");}
break;
//#line 944 "Parser.java"
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
