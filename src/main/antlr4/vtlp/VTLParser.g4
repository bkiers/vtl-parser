parser grammar VTLParser;

options {
  tokenVocab=VTLLexer;
}

parse
 : ANY* EOF
 ;
