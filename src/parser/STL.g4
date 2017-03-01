grammar STL;

property:	'('property')'
	|	'!('property')'
	|	boolean_expr
	|	property '&&' property
	|	property '||' property
	|	property 'U[' RATIONAL ',' RATIONAL ']' property
	|	'F[' RATIONAL ',' RATIONAL ']' property
	|	'G[' RATIONAL ',' RATIONAL ']' property
	;
expr:	('-('|'(') expr ')'
	|	expr '^' expr
	|	('sqrt('|'log('|'ln('|'abs('|'der('|'int(') expr ')'
	|	expr ('*'|'/') expr
	|	expr ('+'|'-') expr
    |	RATIONAL
    |   VARIABLE
    ;
boolean_expr:	expr ('<'|'<='|'='|'>='|'>') expr
    |	BOOLEAN
    ;
BOOLEAN : ('true'|'false');
VARIABLE : ([a-z]|[A-Z])([a-z]|[A-Z]|[0-9]|'_')*;
RATIONAL : ('-')?[0-9]*('.')?[0-9]+('E'|'E-')?[0-9]* ;
WS : ( ' ' | '\t' | '\r' | '\n' )+ { skip(); };
