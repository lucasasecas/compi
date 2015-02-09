%{
	package as_Parser;
%}

%token ID
%token REGISTRO
%token ENTERO
%token ENTEROL

%%

Programa: Declaraciones;

Declaraciones:  Declaraciones Declaracion 
	|	Declaracion
	;
			 
 Declaracion:	Declaracion_reg
	|	Declaracion_var
	;
			
Declaracion_reg:	REGISTRO Cuerpo_reg Lista_var;
	

Cuerpo_reg:		'{' Declaraciones_reg '}'
		  ;
		  
Declaraciones_reg: 	Declaraciones_reg Declaracion_unit
				 |	Declaracion_unit
				 ;

Declaracion_unit:	Tipo ID ';'
				;
 
Tipo:	ENTERO
	|	ENTEROL
	;

Lista_var: 	Lista_var ',' ID
		 |	ID
		 ;

Declaracion_var: Tipo Lista_var ';'

