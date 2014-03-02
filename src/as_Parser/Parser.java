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






//#line 2 "/home/lucas/workspace/compi/sources/Gramatica.y"
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
    3,    0,    4,    0,    1,    1,    1,    1,    5,    5,
    7,    8,    9,    9,    6,    6,   10,   12,   10,   13,
   10,   11,   11,   16,   11,   14,   14,   15,   15,   17,
   17,   18,   20,    2,    2,   21,    2,   19,   19,   19,
   19,   19,   19,   22,   22,   28,   26,   30,   26,   29,
   29,   29,   32,   29,   31,   31,   31,   34,   31,   35,
   31,   33,   33,   33,   27,   36,   27,   40,   23,   23,
   37,   42,   37,   43,   37,   38,   39,   44,   44,   45,
   44,   46,   44,   41,   24,   24,   47,   47,   48,   50,
   48,   48,   49,   25,   51,   25,   52,   25,
};
final static short yylen[] = {                            2,
    0,    3,    0,    3,    2,    1,    3,    2,    2,    1,
    3,    1,    3,    1,    2,    1,    4,    0,    5,    0,
    4,    2,    2,    0,    3,    2,    1,    2,    1,    1,
    1,    2,    2,    2,    1,    0,    4,    2,    1,    1,
    2,    2,    2,    3,    3,    1,    4,    0,    5,    3,
    3,    1,    0,    4,    3,    3,    1,    0,    5,    0,
    5,    1,    1,    1,    3,    0,    5,    0,    5,    2,
    5,    0,    6,    0,    7,    1,    1,    1,    3,    0,
    4,    0,    5,    3,    2,    3,    6,    6,    3,    0,
    5,    3,    1,    4,    0,    5,    0,    6,
};
final static short yydefred[] = {                         0,
    3,   12,    0,    0,    1,    0,    0,   10,    0,   16,
    0,   20,    0,    0,    0,    9,    8,    0,   15,   14,
    0,    0,    0,    0,    0,    0,    0,   35,    0,   39,
   40,    0,    0,    0,    0,    0,    0,    0,    0,   27,
    0,    0,    0,    7,   11,    0,    0,    0,    0,    0,
    0,   36,   34,   38,   41,   42,   43,    0,    0,   78,
    0,   76,    0,   85,   21,    0,    0,   17,   23,    0,
   26,    0,   29,   30,   31,    0,   13,   66,   65,   48,
    0,   63,   64,    0,    0,   57,   72,    0,    0,   46,
    0,   95,    0,    0,   45,    0,   80,    0,   68,   86,
   19,    0,   33,   28,   32,    0,    0,   53,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,   97,   94,
   37,    0,    0,   79,    0,   67,   49,    0,    0,    0,
   60,   56,   58,   55,    0,    0,   74,   71,    0,   93,
    0,    0,   96,    0,   81,    0,   69,   77,   54,    0,
    0,   73,    0,   88,   87,    0,    0,   98,   83,   61,
   59,   75,   92,   90,    0,    0,   91,
};
final static short yydgoto[] = {                          4,
    5,   27,   14,   11,    6,    7,    8,    9,   21,   10,
   41,   66,   38,   42,   72,   67,   73,   74,   75,   76,
   94,   29,   30,   31,   32,   33,   34,   35,   84,  107,
   85,  128,   86,  151,  150,  106,   36,   61,  147,  125,
   89,  114,  153,   62,  122,  146,   37,  141,  142,  166,
  118,  144,
};
final static short yysindex[] = {                      -132,
    0,    0, -171,    0,    0, -119, -104,    0, -209,    0,
  -88,    0, -189,  -88, -208,    0,    0, -140,    0,    0,
    6,   20,   51,   65,   68,   80, -134,    0,   66,    0,
    0,   91,  102,  109,  -92, -109, -199,  -85,    0,    0,
  -82, -183, -134,    0,    0,  -68,  -41, -176, -173,  -66,
 -211,    0,    0,    0,    0,    0,    0, -163, -124,    0,
  -73,    0,  137,    0,    0,  -64,  -87,    0,    0,   -4,
    0,  -87,    0,    0,    0,  -88,    0,    0,    0,    0,
   20,    0,    0,  -24,   67,    0,    0,  -27,  158,    0,
  141,    0,  -17,  -88,    0,  -18,    0, -150,    0,    0,
    0,  -87,    0,    0,    0,  142,  144,    0, -186, -186,
    0, -160, -127,  164, -186, -216,  -99,  165,    0,    0,
    0,  -54,    0,    0, -109,    0,    0,  149,   67,   67,
    0,    0,    0,    0,  -59,  -18,    0,    0,  169,    0,
  170, -214,    0,  153,    0,  154,    0,    0,    0,  155,
  157,    0,  159,    0,    0,  160, -114,    0,    0,    0,
    0,    0,    0,    0,  -18,  162,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -77,    0,    0,    0,    0,
    0,    0,    0,    0,  -67,    0,    0,    0,    0,    0,
    0,  -42,    0,    0,    0,    0,  223,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -98,    0,
    0,    0,  224,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,    0,    0,    0,  -42,
    0,  -34,    0,    0,    0,    0,    0,    0,    0,    0,
  -39,    0,    0,    0,  -36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  171,    0,    0,    0,    0,
    0,  -28,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -16,    0,    0,    0,    0,    0,  -31,  -30,
    0,    0,    0,    0,    0,  193,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  194,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   17,    0,    0,  235,  237,   26,    0,    0,   34,
    0,    0,    0,    0,  177,    0,  -38,    0,   19,    0,
    0,  195,    0,    0,    0,    0,  -11,    0,  -23,    0,
   56,    0,   74,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -15,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=272;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         79,
   70,   62,   62,   62,   52,   62,   52,   62,   52,   50,
   51,   50,   51,   50,   51,  109,  111,  110,  109,   62,
  110,   64,   52,  120,  109,   88,  110,   50,   51,   28,
   43,   16,   28,  104,   96,   47,   83,   83,   40,  137,
   19,  156,   82,   16,   92,   53,   83,   20,   19,   46,
    3,  138,  157,  103,   60,   60,   63,   22,   93,   47,
   59,   53,   23,  104,   45,   24,   39,   71,    2,   25,
   81,   26,   69,   70,    2,   98,   82,   28,   23,   80,
   81,   24,   87,   81,   12,   25,   82,   26,   13,   82,
   48,  136,   95,   81,  105,  131,   81,   83,   83,   82,
   83,   83,   82,   83,   49,  123,   22,   50,  113,  148,
  124,   23,  121,  112,   24,   44,   53,    2,   25,   51,
   26,   52,   22,    1,   54,    2,    3,   23,  133,   81,
   24,   97,   22,  165,   25,   82,   26,   23,    2,    3,
   24,  164,   81,   60,   25,   83,   26,   22,   82,   55,
   59,   17,   23,    2,    3,   24,  139,  140,   24,   25,
   56,   26,   18,   24,  129,  130,   24,   57,   22,   70,
   24,   58,   24,   23,   23,   65,   24,   24,   68,    6,
   25,   25,   26,   26,    6,  132,  134,    6,   77,    5,
   90,    6,   99,    6,    5,  100,  101,    5,  116,  117,
  126,    5,  127,    5,  135,  143,  145,  149,  152,  154,
  155,  158,  159,  160,   78,  161,   62,  162,  163,   52,
  167,   46,    4,    2,   50,   51,   22,   62,  108,   44,
   52,  108,   25,   84,   89,   50,   51,  108,  119,  115,
   36,   18,   15,  102,   91,   36,    0,    0,   36,    0,
    0,    0,   36,    0,   36,    0,   70,   70,    0,    0,
    0,   70,   70,    0,    0,   70,    0,    0,    0,   70,
    0,   70,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   42,   43,   41,   45,   43,   47,   45,   41,
   41,   43,   43,   45,   45,   43,   41,   45,   43,   59,
   45,   37,   59,   41,   43,   49,   45,   59,   59,   11,
   14,    6,   14,   72,   58,   40,   48,   49,   13,  256,
    7,  256,   59,   18,  256,   27,   58,  257,   15,   44,
  259,  268,  267,   58,   36,   37,  256,  257,  270,   40,
  260,   43,  262,  102,   59,  265,  256,   42,  258,  269,
  257,  271,  256,  257,  258,   59,  263,   59,  262,  256,
  257,  265,  256,  257,  256,  269,  263,  271,  260,  263,
   40,  115,  256,  257,   76,  256,  257,  109,  110,  263,
  112,  113,  263,  115,   40,  256,  257,   40,   42,  125,
  261,  262,   94,   47,  265,  256,   98,  258,  269,   40,
  271,  256,  257,  256,   59,  258,  259,  262,  256,  257,
  265,  256,  257,  157,  269,  263,  271,  262,  258,  259,
  265,  256,  257,  125,  269,  157,  271,  257,  263,   59,
  260,  256,  262,  258,  259,  265,  256,  257,  257,  269,
   59,  271,  261,  262,  109,  110,  265,   59,  257,  257,
  269,  264,  271,  262,  262,  261,  265,  265,  261,  257,
  269,  269,  271,  271,  262,  112,  113,  265,  257,  257,
  257,  269,  266,  271,  262,   59,  261,  265,   41,   59,
   59,  269,   59,  271,   41,   41,  261,   59,  268,   41,
   41,   59,   59,   59,  256,   59,  256,   59,   59,  256,
   59,  264,    0,    0,  256,  256,  261,  267,  256,   59,
  267,  256,  261,   41,   41,  267,  267,  256,  256,  267,
  257,    7,    6,   67,   50,  262,   -1,   -1,  265,   -1,
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
"$$2 :",
"Programa : error $$2 Sentencias",
"Declaraciones : DeclaracionesVariables DeclaracionesFunciones",
"Declaraciones : DeclaracionesVariables",
"Declaraciones : DeclaracionesFunciones DeclaracionesVariables error",
"Declaraciones : DeclaracionesFunciones error",
"DeclaracionesVariables : DeclaracionesVariables DeclaracionVariables",
"DeclaracionesVariables : DeclaracionVariables",
"DeclaracionVariables : Tipo ListaVariables ';'",
"Tipo : UINT",
"ListaVariables : ListaVariables ',' ID",
"ListaVariables : ID",
"DeclaracionesFunciones : DeclaracionesFunciones DeclaracionFunciones",
"DeclaracionesFunciones : DeclaracionFunciones",
"DeclaracionFunciones : FUNCTION BEGIN CuerpoFuncion END",
"$$3 :",
"DeclaracionFunciones : FUNCTION BEGIN error $$3 END",
"$$4 :",
"DeclaracionFunciones : FUNCTION error $$4 END",
"CuerpoFuncion : DeclaracionesFuncion InstanciasFuncion",
"CuerpoFuncion : DeclaracionesFuncion error",
"$$5 :",
"CuerpoFuncion : error $$5 InstanciasFuncion",
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
"$$6 :",
"Sentencias : Sentencias error $$6 Sentencia",
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
"$$7 :",
"Return : RETURN '(' error $$7 ';'",
"Expresion : Expresion '+' Termino",
"Expresion : Expresion '-' Termino",
"Expresion : Termino",
"$$8 :",
"Expresion : Expresion error $$8 ';'",
"Termino : Termino '*' Factor",
"Termino : Termino '/' Factor",
"Termino : Factor",
"$$9 :",
"Termino : Termino '*' error $$9 ';'",
"$$10 :",
"Termino : Termino '/' error $$10 ';'",
"Factor : ID",
"Factor : CTE",
"Factor : LlamadaFn",
"LlamadaFn : ID '(' ')'",
"$$11 :",
"LlamadaFn : ID '(' error $$11 ';'",
"$$12 :",
"Seleccion : CabeceraIf BloqueThen ELSE $$12 BloqueElse",
"Seleccion : CabeceraIf BloqueThen",
"CabeceraIf : IF '(' Comparacion ')' THEN",
"$$13 :",
"CabeceraIf : IF '(' error $$13 ')' THEN",
"$$14 :",
"CabeceraIf : IF '(' Comparacion ')' error $$14 ';'",
"BloqueThen : BloqueSentencias",
"BloqueElse : BloqueSentencias",
"BloqueSentencias : Sentencia",
"BloqueSentencias : BEGIN Sentencias END",
"$$15 :",
"BloqueSentencias : BEGIN error $$15 END",
"$$16 :",
"BloqueSentencias : BEGIN Sentencias error $$16 ';'",
"Comparacion : Expresion COMP Expresion",
"Iteracion : CabeceraIteracion BloqueSentencias",
"Iteracion : CabeceraIteracion error ';'",
"CabeceraIteracion : FOR '(' Asignacion ';' Comparacion2 ')'",
"CabeceraIteracion : FOR '(' Asignacion ';' error ')'",
"Comparacion2 : Comp2Izq COMP Expresion",
"$$17 :",
"Comparacion2 : Comp2Izq COMP error $$17 ';'",
"Comparacion2 : Comp2Izq error ';'",
"Comp2Izq : ID",
"SalidaPantalla : PRINT '(' STR ')'",
"$$18 :",
"SalidaPantalla : PRINT '(' error $$18 ')'",
"$$19 :",
"SalidaPantalla : PRINT '(' STR error $$19 ';'",
};

//#line 274 "/home/lucas/workspace/compi/sources/Gramatica.y"

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
	for(int i = 0; i<erroresSem.size();i++)
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
private void imprimirCodigoIntermedio() {
	
	
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
//#line 569 "Parser.java"
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
//#line 32 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
					alcance = "Main";
					push("MAIN");
					}
break;
case 3:
//#line 36 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("Error en delcaracion");}
break;
case 7:
//#line 42 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("Las declaraciones de variables estan despues de las declaraciones de funciones");}
break;
case 8:
//#line 43 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("No se encontro declaraciones de variables ");}
break;
case 9:
//#line 46 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
			for(ParserVal a : val_peek(0).list){
				if(_tds.isDeclared(a.sval, "Main"))
					guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
				tupla.set_scope("Main");
			}
		}
break;
case 10:
//#line 54 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
			for(ParserVal a : val_peek(0).list){
				if(_tds.isDeclared(a.sval, "Main"))
					guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
				tupla.set_scope("Main");
			}
		}
break;
case 11:
//#line 64 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
				for(ParserVal a : val_peek(1).list){
	  				TuplaTablaSimbolos tupla = _tds.getTupla(a.sval);
	  				tupla.set_type(val_peek(2).sval);
	  				tupla.set_use("Variable");
				}
				yyval = val_peek(1);				  
				agregarRegla("declaracion de variables");}
break;
case 12:
//#line 74 "/home/lucas/workspace/compi/sources/Gramatica.y"
{yyval = val_peek(0);}
break;
case 13:
//#line 77 "/home/lucas/workspace/compi/sources/Gramatica.y"
{val_peek(2).addToList(val_peek(0)); yyval = val_peek(2);}
break;
case 14:
//#line 78 "/home/lucas/workspace/compi/sources/Gramatica.y"
{yyval.addToList(val_peek(0));}
break;
case 17:
//#line 84 "/home/lucas/workspace/compi/sources/Gramatica.y"
{setPEFlag(); agregarRegla("Declaracion de funcion");alcance = "Main";}
break;
case 18:
//#line 85 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("el cuerpo de la funcion esta mal definido");}
break;
case 20:
//#line 86 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("falta el begin");}
break;
case 23:
//#line 91 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("error en una de las instancias de la funcion");}
break;
case 24:
//#line 92 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("error en las declaraciones de funcion");}
break;
case 26:
//#line 95 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
				for(ParserVal a : val_peek(0).list){
					if(_tds.isDeclared(a.sval, "funcion"))
						guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
					_tds.changeScope(a.sval);
				}
			  }
break;
case 27:
//#line 102 "/home/lucas/workspace/compi/sources/Gramatica.y"
{	
				for(ParserVal a : val_peek(0).list){
					if(_tds.isDeclared(a.sval, "funcion"))
						guardarErrorSem("El identificador "+a.sval+" ya ha sido declarado");
					_tds.changeScope(a.sval);
				}
			  }
break;
case 33:
//#line 122 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
			setPEFlag();
			push(val_peek(1).sval);
			push("HEADER");
			TuplaTablaSimbolos tupla = _tds.getTupla(val_peek(1).sval);
			if(_tds.isDeclared(val_peek(1).sval, "Main"))
					guardarErrorSem("El identificador "+val_peek(1).sval+" ya ha sido declarado");
			tupla.set_use("Punto de entrada");
			tupla._scope = "Main";
		}
break;
case 36:
//#line 136 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("Sentencia mal declarada o invalida");}
break;
case 38:
//#line 139 "/home/lucas/workspace/compi/sources/Gramatica.y"
{agregarRegla("Asignacion");}
break;
case 39:
//#line 140 "/home/lucas/workspace/compi/sources/Gramatica.y"
{agregarRegla("Sentencia If");}
break;
case 40:
//#line 141 "/home/lucas/workspace/compi/sources/Gramatica.y"
{agregarRegla("Iteracion For");}
break;
case 41:
//#line 142 "/home/lucas/workspace/compi/sources/Gramatica.y"
{agregarRegla("Salida por pantalla Print");}
break;
case 44:
//#line 147 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
				push(val_peek(1).sval);	
				yyval = val_peek(2);
			}
break;
case 45:
//#line 151 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("se produjo error en la asignacion");}
break;
case 46:
//#line 154 "/home/lucas/workspace/compi/sources/Gramatica.y"
{push(val_peek(0).sval);
		verificarDeclaracion(val_peek(0).sval);
		yyval = val_peek(0);
	}
break;
case 47:
//#line 160 "/home/lucas/workspace/compi/sources/Gramatica.y"
{push("RETURN");}
break;
case 48:
//#line 161 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("sentencia return mal definida");}
break;
case 50:
//#line 164 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
			push('+');
		}
break;
case 51:
//#line 167 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
	   		push('-');
   		}
break;
case 53:
//#line 171 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("Expresion mal definida");}
break;
case 55:
//#line 174 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
		push('*');
	   }
break;
case 56:
//#line 177 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
	 	push('/');
	 	}
break;
case 58:
//#line 181 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("termino mal definico");}
break;
case 60:
//#line 182 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("termino mal definido");}
break;
case 62:
//#line 185 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
		yyval = val_peek(0);
		push(val_peek(0).sval);
		verificarDeclaracion(val_peek(0).sval);
	}
break;
case 63:
//#line 190 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
		yyval = val_peek(0);
		push(val_peek(0).sval);}
break;
case 65:
//#line 196 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
		agregarRegla("llamada a funcion");
		push(val_peek(2).sval);
		push("CALL");
		}
break;
case 66:
//#line 201 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("falta el caracter de cierre ')'. Recuerde que las llamadas a funcion no lleva argumentos");}
break;
case 68:
//#line 205 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
	    	pila.setSaltoPrevio(2);
	    	pila.nuevoSalto("BI");
	    }
break;
case 69:
//#line 209 "/home/lucas/workspace/compi/sources/Gramatica.y"
{pila.setSaltoPrevio(0);}
break;
case 70:
//#line 210 "/home/lucas/workspace/compi/sources/Gramatica.y"
{pila.setSaltoPrevio(0);}
break;
case 71:
//#line 214 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
	    	pila.nuevoSalto("BF");
	    }
break;
case 72:
//#line 217 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("se produjo un error en la comparacion");}
break;
case 74:
//#line 218 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("no se encontro la palabra reservada \"then\"");}
break;
case 80:
//#line 229 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("error sintactico dentro del bloque de codigo");}
break;
case 82:
//#line 230 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("no se encontro el cierre de bloque de codigo \"end\"");}
break;
case 84:
//#line 235 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
			push(val_peek(1).sval);
		}
break;
case 85:
//#line 241 "/home/lucas/workspace/compi/sources/Gramatica.y"
{
			push(varFor);
			push(varFor);
			push("1");
			push("+");
			push("=");
			pila.nuevoSalto("BI");
			pila.setSaltoPrevio(pila.getLastFlag());
			pila.setSaltoPrevio(0);
		}
break;
case 87:
//#line 254 "/home/lucas/workspace/compi/sources/Gramatica.y"
{pila.nuevoSalto("BF"); varFor = val_peek(3).sval;}
break;
case 89:
//#line 258 "/home/lucas/workspace/compi/sources/Gramatica.y"
{yyval = val_peek(2); push(val_peek(1).sval);}
break;
case 90:
//#line 259 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("del lado derecho de la comparacion debe ir una expresion");}
break;
case 93:
//#line 263 "/home/lucas/workspace/compi/sources/Gramatica.y"
{yyval = val_peek(0); pila.setFlag(); push(val_peek(0).sval);
			verificarDeclaracion(val_peek(0).sval);
		}
break;
case 94:
//#line 268 "/home/lucas/workspace/compi/sources/Gramatica.y"
{push(val_peek(1).sval); push("PRINT");}
break;
case 95:
//#line 269 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("falta cadena de caracteres o se escrio mal");}
break;
case 97:
//#line 270 "/home/lucas/workspace/compi/sources/Gramatica.y"
{guardarError("la sentencia print se debe cerrar con el caracter especial ')'");}
break;
//#line 1029 "Parser.java"
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
