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

	import unicen.compiladores.gui.ErrorManager;
import unicen.compiladores.gui.ParserError;
import unicen.compiladores.gui.Sentencia;
import unicen.compiladores.gui.SentenciaManager;
import Utils.TablaSimbolo;
import al_Main.AnalizadorLexico;
import Utils.TuplaTablaSimbolos;
//#line 28 "Parser.java"




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
    0,    3,    0,    1,    1,    4,    4,    5,    9,    5,
    8,    8,    7,   11,    7,   10,   10,   12,   13,   13,
   14,   14,    6,    2,    2,   15,   15,   15,   15,   16,
   22,   16,   23,   16,   20,   21,   21,   21,   25,   21,
   26,   21,   24,   24,   24,   28,   24,   29,   24,   27,
   27,   27,   27,   31,   30,   32,   30,   17,   17,   33,
   37,   33,   38,   33,   39,   33,   40,   33,   36,   34,
   34,   41,   35,   35,   18,   42,   44,   42,   45,   42,
   46,   42,   47,   42,   43,   43,   19,   19,   48,   19,
   49,   19,   50,   19,
};
final static short yylen[] = {                            2,
    2,    0,    4,    2,    1,    1,    1,    4,    0,    5,
    3,    1,    3,    0,    5,    2,    1,    3,    1,    1,
    3,    1,    3,    2,    1,    1,    1,    1,    1,    4,
    0,    6,    0,    4,    1,    3,    3,    1,    0,    5,
    0,    5,    3,    3,    1,    0,    5,    0,    5,    1,
    1,    1,    1,    2,    3,    0,    5,    4,    2,    5,
    0,    7,    0,    6,    0,    5,    0,    4,    3,    1,
    1,    3,    1,    1,    2,    5,    0,    7,    0,    6,
    0,    5,    0,    4,    1,    1,    5,    5,    0,    6,
    0,    5,    0,    4,
};
final static short yydefred[] = {                         0,
    0,   19,   20,    0,    0,    5,    6,    7,    0,    0,
    0,    2,   35,    0,    0,    0,    0,    4,   25,   26,
   27,   28,   29,    0,    0,    0,   22,    0,    0,   17,
    0,    9,   12,    0,    0,   67,    0,   83,    0,   93,
    0,   24,   33,    0,    0,   70,    0,   71,   85,   86,
   75,   23,    0,   14,   13,   16,    0,    0,    8,    0,
    3,    0,   65,    0,   51,    0,    0,    0,   45,   52,
   53,    0,    0,   81,    0,    0,   91,    0,    0,    0,
    0,    0,   21,    0,   18,   10,   11,   68,    0,    0,
   54,    0,    0,    0,    0,    0,   63,    0,   84,    0,
   79,    0,   94,    0,   89,    0,   34,   31,   30,   72,
   74,   58,   73,   15,   66,   56,   55,    0,   39,    0,
   41,    0,   48,   44,   46,   43,    0,   61,   60,   82,
    0,   77,   76,   92,    0,   88,   87,    0,    0,    0,
    0,    0,    0,   64,    0,   80,    0,   90,   32,   57,
   40,   42,   49,   47,   62,   78,
};
final static short yydgoto[] = {                          4,
    5,   17,   35,    6,    7,    8,   11,   34,   58,   29,
   84,   30,    9,   28,   19,   20,   21,   22,   23,   24,
   67,  138,   79,   68,  140,  141,   69,  143,  142,   70,
   71,  139,   25,   47,  112,   72,  145,  127,   89,   62,
   48,   26,   51,  147,  131,  100,   73,  135,  104,   76,
};
final static short yysindex[] = {                      -168,
  -96,    0,    0,    0, -188,    0,    0,    0, -222, -182,
 -198,    0,    0,  -40,  -29,  -14, -183,    0,    0,    0,
    0,    0,    0, -201, -101, -101,    0,    4,  -84,    0,
 -215,    0,    0,    8,   14,    0,  -26,    0,  -16,    0,
 -209,    0,    0,   -5, -183,    0, -200,    0,    0,    0,
    0,    0, -164,    0,    0,    0,   35,   38,    0, -159,
    0,   41,    0,   53,    0, -161,  -27,   15,    0,    0,
    0,  -17,   43,    0,  -11,   44,    0,    3,   47,    6,
 -100, -101,    0,   48,    0,    0,    0,    0,   49, -169,
    0,   -5,  -13,   -9,   -6,   -2,    0, -190,    0,   52,
    0, -218,    0,   54,    0,  -38,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   40,    0,   15,
    0,   15,    0,    0,    0,    0,   55,    0,    0,    0,
   56,    0,    0,    0,   57,    0,    0,   58,   59,   60,
   61,   62,   63,    0,   64,    0,   65,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  112,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -39,    0,    0,    0,  -36,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    5,    0,  -31,
    0,  -28,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   80,    0,  122,    0,    0,    0,    0,    0,    0,
    0,   99,   27,    0,   28,    0,    0,    0,    0,    0,
  -10,    0,    0,    2,    0,    0,    9,    0,    0,    0,
    0,    0,    0,    0,    0,   90,    0,    0,    0,    0,
    7,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=270;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   59,   50,   50,   50,   38,   50,   38,   50,   38,   36,
   39,   36,   37,   36,   37,   93,   37,   94,   66,   50,
  137,   45,   38,   98,  110,   41,   10,   36,   66,  102,
   37,   66,   50,   80,   27,   66,   31,  132,   66,   66,
   55,   57,   66,  106,   42,   69,   77,   53,   93,  133,
   94,   60,   46,   49,   43,   31,   96,   32,   33,   44,
   78,   95,   52,   82,  109,  128,   59,   12,   13,    1,
    2,    3,   61,   13,   14,  129,    2,    3,   15,   14,
   16,  118,   93,   15,   94,   16,  116,  117,  113,    1,
    2,    3,   83,   85,  120,  122,   86,   87,   90,   88,
   91,   99,  103,  124,  126,  107,  114,  115,   42,  111,
  130,    1,  134,  144,  146,  148,  149,  150,  151,  152,
  153,  154,  155,  156,   81,   59,   18,   56,   75,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   13,   13,    0,    0,    0,
    0,   14,   14,    0,    0,   15,   15,   16,   16,    0,
    0,   54,    0,    0,    2,    3,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   36,   50,  136,    0,   38,
    0,    0,    0,    0,   36,   50,   38,   37,   38,   63,
   64,    0,    0,   36,    0,   65,   37,   92,   97,   74,
   64,   40,  119,   64,  101,   65,  121,   64,   65,  123,
   64,   64,   65,  125,   64,   65,   65,   59,  105,   65,
   69,  108,    0,   59,    0,    0,    0,   59,    0,   59,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   42,   43,   41,   45,   43,   47,   45,   41,
   40,   43,   41,   45,   43,   43,   45,   45,   45,   59,
   59,  123,   59,   41,  125,   40,  123,   59,   45,   41,
   59,   45,   26,   44,  257,   45,   10,  256,   45,   45,
  125,  257,   45,   41,   17,   41,  256,   44,   43,  268,
   45,   44,   25,   26,  256,   29,   42,  256,  257,  261,
  270,   47,   59,  264,   59,  256,   59,  256,  257,  258,
  259,  260,   59,  257,  263,  266,  259,  260,  267,  263,
  269,   92,   43,  267,   45,  269,  256,  257,   82,  258,
  259,  260,  257,   59,   93,   94,   59,  257,   46,   59,
  262,   59,   59,   95,   96,   59,   59,   59,   81,   82,
   59,    0,   59,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   59,   59,   45,  125,    5,   29,   39,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  257,   -1,   -1,   -1,
   -1,  263,  263,   -1,   -1,  267,  267,  269,  269,   -1,
   -1,  256,   -1,   -1,  259,  260,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  256,  256,   -1,  256,
   -1,   -1,   -1,   -1,  256,  265,  256,  256,  265,  256,
  257,   -1,   -1,  265,   -1,  262,  265,  265,  256,  256,
  257,  256,  256,  257,  256,  262,  256,  257,  262,  256,
  257,  257,  262,  256,  257,  262,  262,  257,  256,  262,
  256,  256,   -1,  263,   -1,   -1,   -1,  267,   -1,  269,
};
}
final static short YYFINAL=4;
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
"$$1 :",
"Programa : Declaraciones error $$1 ';'",
"Declaraciones : Declaraciones Declaracion",
"Declaraciones : Declaracion",
"Declaracion : Declaracion_reg",
"Declaracion : Declaracion_var",
"Declaracion_reg : REGISTRO Cuerpo_reg Lista_var_reg ';'",
"$$2 :",
"Declaracion_reg : REGISTRO Cuerpo_reg error $$2 ';'",
"Lista_var_reg : Lista_var_reg ',' ID",
"Lista_var_reg : ID",
"Cuerpo_reg : '{' Declaraciones_reg '}'",
"$$3 :",
"Cuerpo_reg : '{' Declaraciones_reg error $$3 ';'",
"Declaraciones_reg : Declaraciones_reg Declaracion_unit",
"Declaraciones_reg : Declaracion_unit",
"Declaracion_unit : Tipo ID ';'",
"Tipo : ENTERO",
"Tipo : ENTEROL",
"Lista_var : Lista_var ',' ID",
"Lista_var : ID",
"Declaracion_var : Tipo Lista_var ';'",
"Sentencias : Sentencias Sentencia",
"Sentencias : Sentencia",
"Sentencia : Asignacion",
"Sentencia : Seleccion",
"Sentencia : Iteracion",
"Sentencia : Salida",
"Asignacion : AsignacionIzq ASIGN Expresion ';'",
"$$4 :",
"Asignacion : AsignacionIzq ASIGN Expresion error $$4 ';'",
"$$5 :",
"Asignacion : AsignacionIzq error $$5 ';'",
"AsignacionIzq : ID",
"Expresion : Expresion '+' Termino",
"Expresion : Expresion '-' Termino",
"Expresion : Termino",
"$$6 :",
"Expresion : Expresion '+' error $$6 ';'",
"$$7 :",
"Expresion : Expresion '-' error $$7 ';'",
"Termino : Termino '*' Factor",
"Termino : Termino '/' Factor",
"Termino : Factor",
"$$8 :",
"Termino : Termino '*' error $$8 ';'",
"$$9 :",
"Termino : Termino '/' error $$9 ';'",
"Factor : ID",
"Factor : CTE",
"Factor : CampoReg",
"Factor : CTENeg",
"CTENeg : '-' CTE",
"CampoReg : ID '.' ID",
"$$10 :",
"CampoReg : ID '.' error $$10 ';'",
"Seleccion : CabeceraSi BloqueEntonces SINO BloqueSino",
"Seleccion : CabeceraSi BloqueEntonces",
"CabeceraSi : SI '(' Comparacion ')' ENTONCES",
"$$11 :",
"CabeceraSi : SI '(' Comparacion ')' error $$11 ';'",
"$$12 :",
"CabeceraSi : SI '(' Comparacion error $$12 ';'",
"$$13 :",
"CabeceraSi : SI '(' error $$13 ';'",
"$$14 :",
"CabeceraSi : SI error $$14 ';'",
"Comparacion : Expresion COMP Expresion",
"BloqueEntonces : Sentencia",
"BloqueEntonces : BloqueSentencias",
"BloqueSentencias : '{' Sentencias '}'",
"BloqueSino : BloqueSentencias",
"BloqueSino : Sentencia",
"Iteracion : CabeceraIteracion BloqueIteracion",
"CabeceraIteracion : MIENTRAS '(' Comparacion ')' ITERAR",
"$$15 :",
"CabeceraIteracion : MIENTRAS '(' Comparacion ')' error $$15 ';'",
"$$16 :",
"CabeceraIteracion : MIENTRAS '(' Comparacion error $$16 ';'",
"$$17 :",
"CabeceraIteracion : MIENTRAS '(' error $$17 ';'",
"$$18 :",
"CabeceraIteracion : MIENTRAS error $$18 ';'",
"BloqueIteracion : Sentencia",
"BloqueIteracion : BloqueSentencias",
"Salida : IMPRIMIR '(' STR ')' ';'",
"Salida : IMPRIMIR '(' STR ')' error",
"$$19 :",
"Salida : IMPRIMIR '(' STR error $$19 ';'",
"$$20 :",
"Salida : IMPRIMIR '(' error $$20 ';'",
"$$21 :",
"Salida : IMPRIMIR error $$21 ';'",
};

//#line 159 "sintaxis.y"

TablaSimbolo tds;
AnalizadorLexico al;
Vector<ParserVal> tokens = new Vector<ParserVal>();
ErrorManager errorManager;
SentenciaManager sentenciasManager;

private int yylex(){
	 yylval = new ParserVal();
	 int t = al.getNextToken(yylval);
	 System.out.println(yylval.kind+"--"+yylval.sval);
	 tokens.add(yylval.Clone());
	 return t;
	}

public Parser(TablaSimbolo tds, AnalizadorLexico al, ErrorManager errorManager, SentenciaManager sentenciasManager){

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
	if(tupla!=null)
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



//#line 453 "Parser.java"
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
//#line 40 "sintaxis.y"
{yyerror("Se han encontrado errores en las sentencias");}
break;
case 9:
//#line 52 "sintaxis.y"
{yyerror("se ha encontrado un error en la declaracion de variables del registro");}
break;
case 11:
//#line 55 "sintaxis.y"
{ tds.getTupla(val_peek(0).sval).setValue("es registro", true);}
break;
case 12:
//#line 56 "sintaxis.y"
{ tds.getTupla(val_peek(0).sval).setValue("es registro", true);}
break;
case 14:
//#line 60 "sintaxis.y"
{yyerror("no de encontro el caracter '}'"); }
break;
case 26:
//#line 85 "sintaxis.y"
{nuevaSentencia(val_peek(0),"Asignacion");}
break;
case 27:
//#line 86 "sintaxis.y"
{nuevaSentencia(val_peek(0),"Sentencia 'si'");}
break;
case 28:
//#line 87 "sintaxis.y"
{nuevaSentencia(val_peek(0),"sentencia 'mientras'");}
break;
case 29:
//#line 88 "sintaxis.y"
{nuevaSentencia(val_peek(0),"Sentencia 'imprimir'");}
break;
case 30:
//#line 90 "sintaxis.y"
{yyval = val_peek(2);}
break;
case 31:
//#line 91 "sintaxis.y"
{yyerror("Sentencia debe finalizar con caracter ';'");}
break;
case 33:
//#line 92 "sintaxis.y"
{yyerror("Se esperaba el simbolo de asignacion ':='");}
break;
case 39:
//#line 99 "sintaxis.y"
{yyerror("Termino incorrecto");}
break;
case 41:
//#line 100 "sintaxis.y"
{yyerror("Termino incorrecto");}
break;
case 46:
//#line 105 "sintaxis.y"
{yyerror("Operando invalido");}
break;
case 48:
//#line 106 "sintaxis.y"
{yyerror("Operando invalido");}
break;
case 51:
//#line 109 "sintaxis.y"
{verificarRango(val_peek(0).sval);}
break;
case 54:
//#line 114 "sintaxis.y"
{setToNegative(val_peek(0));}
break;
case 56:
//#line 118 "sintaxis.y"
{yyerror("Campo de registro invalido");}
break;
case 58:
//#line 121 "sintaxis.y"
{yyval = val_peek(3);}
break;
case 59:
//#line 122 "sintaxis.y"
{yyval = val_peek(1);}
break;
case 60:
//#line 124 "sintaxis.y"
{yyval = val_peek(4);}
break;
case 61:
//#line 125 "sintaxis.y"
{yyerror("Falta la palabra reservada 'entonces'");}
break;
case 63:
//#line 126 "sintaxis.y"
{yyerror("No se encontro el caracter ')'"); }
break;
case 65:
//#line 127 "sintaxis.y"
{yyerror("error en la comparacion')'");}
break;
case 67:
//#line 128 "sintaxis.y"
{yyerror("no se encontro el caracter '('");}
break;
case 75:
//#line 140 "sintaxis.y"
{yyval = val_peek(1);}
break;
case 76:
//#line 142 "sintaxis.y"
{yyval = val_peek(4);}
break;
case 77:
//#line 143 "sintaxis.y"
{yyerror("Falta la palabra reservada iterar");}
break;
case 79:
//#line 144 "sintaxis.y"
{yyerror("Falta el caracter ')'");}
break;
case 81:
//#line 145 "sintaxis.y"
{yyerror("Error en la comparacion");}
break;
case 83:
//#line 146 "sintaxis.y"
{yyerror("La sentencia 'mientras' debe ser seguida del caracter '('");}
break;
case 87:
//#line 152 "sintaxis.y"
{yyval = val_peek(4);}
break;
case 88:
//#line 153 "sintaxis.y"
{yyerror("la sentencia debe finalizar con el caracter ';'");}
break;
case 89:
//#line 154 "sintaxis.y"
{yyerror("Falta el caracter ')'");}
break;
case 91:
//#line 155 "sintaxis.y"
{yyerror("Se esperaba una cadena de caracteres");}
break;
case 93:
//#line 156 "sintaxis.y"
{yyerror("La sentencia 'imprimir' debe ser seguida del caracter '('");}
break;
//#line 750 "Parser.java"
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

public static double MAX = Math.pow(2, 15)-1;

private void verificarRango(String sval) {
	double val = Double.parseDouble(sval);
	if (val>MAX){
		this.yyerror("La constante se excede del valor permitido");
	}
	
}
//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
