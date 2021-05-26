lexer grammar VTLLexer;

tokens {
  OPAR, CPAR, OBRACK, CBRACK, CBRACE, STRING, INTEGER, ID, REFERENCE, DOT, COMMA, ASSIGN
}

SNGLE_LINE_COMMENT
 : '##' ~[\r\n]* -> skip
 ;

VTL_COMMENT_BLOCK
 : '#**' .*? '*#' -> channel(HIDDEN)
 ;

MULTI_LINE_COMMENT
 : '#*' .*? '*#' -> skip
 ;

SET
 : '#set' SPACES? '(' -> pushMode(CODE_)
 ;

IF
 : '#if' SPACES? '(' -> pushMode(CODE_)
 ;

END
 : '#end'
 ;

DOLLAR_EXCL_OBRACE
 : '$!{' -> pushMode(FRM_)
 ;

DOLLAR_OBRACE
 : '${' -> pushMode(FRM_)
 ;

DOLLAR_EXCL
 : '$!' -> pushMode(VAR_)
 ;

DOLLAR
 : '$' -> pushMode(VAR_)
 ;

TEXT
 : .
 ;

mode FRM_;

FRM_ID
 : ID -> type(ID)
 ;

FRM_DOT
 : '.' -> type(DOT)
 ;

FRM_OBRACK
 : '[' -> type(OBRACK), pushMode(IDX_)
 ;

FRM_OPAR
 : '(' -> type(OPAR), pushMode(CODE_)
 ;

FRM_CBRACE
 : '}' -> type(CBRACE), popMode
 ;

mode VAR_;

VAR_ID
 : ID -> type(ID)
 ;

VAR_DOT
 : '.' -> type(DOT)
 ;

VAR_OBRACK
 : '[' -> type(OBRACK), pushMode(IDX_)
 ;

VAR_OPAR
 : '(' -> type(OPAR), pushMode(CODE_)
 ;

VAR_TEXT
 : . -> type(TEXT), popMode
 ;

mode CODE_;

CODE_ID
 : ID -> type(ID)
 ;

CODE_OR
 : '||'
 ;

CODE_AND
 : '&&'
 ;

CODE_ASSIGN
 : '=' -> type(ASSIGN)
 ;

CODE_EQUALS
 : '=='
 ;

CODE_SPACES
 : SPACES -> skip
 ;

CODE_REFERENCE
 : '$' ID -> type(REFERENCE)
 ;

CODE_CPAR
 : ')' -> type(CPAR), popMode
 ;

CODE_DOT
 : '.' -> type(DOT)
 ;

CODE_INTEGER
 : INTEGER -> type(INTEGER)
 ;

CODE_STRING
 : STRING -> type(STRING)
 ;

CODE_OBRACK
 : '[' -> type(OBRACK)
 ;

CODE_CBRACK
 : ']' -> type(CBRACK)
 ;

CODE_COMMA
 : ',' -> type(COMMA)
 ;

mode IDX_;

IDX_CBRACK
 : ']' -> type(CBRACK), popMode
 ;

IDX_REFERENCE
 : '$' ID -> type(REFERENCE)
 ;

IDX_STRING
 : STRING -> type(STRING)
 ;

IDX_INTEGER
 : INTEGER -> type(INTEGER)
 ;

fragment SPACES : [ \t\r\n];
fragment ID : [a-zA-Z] [a-zA-Z0-9_-]*;
fragment STRING : STRING_DQ | STRING_SQ;
fragment STRING_DQ : '"' ~'"'* '"';
fragment STRING_SQ : '\'' ~'\''* '\'';
fragment INTEGER : [0-9]+;