grammar Test;

@header{
package org.fife.ui.rsyntaxtextarea.modes.antlr;
}

MUL : '*';
DIV : '/';


// note, that the start of the COMMENT_DOC token is a valid combination of 2 other token types
// also a COMMENT_DOC would also be a valid COMMENT, so it needs to be before it
COMMENT_DOC
    :   '/**' .*? '*/' -> channel(HIDDEN)
    ;

// note, that the start of the COMMENT token is a valid combination of 2 other token types
COMMENT
    :   '/*' .*? '*/' -> channel(HIDDEN)
    ;


// skipped tokens
WS  :  [ \t\r\n\u000C]+ -> skip
    ;

STRING_LITERAL
 : ( SHORT_STRING | LONG_STRING )
 ;

/// shortstring     ::=  "'" shortstringitem* "'" | '"' shortstringitem* '"'
/// shortstringitem ::=  shortstringchar | stringescapeseq
/// shortstringchar ::=  <any source character except "\" or newline or the quote>
fragment SHORT_STRING
 : '\'' ( STRING_ESCAPE_SEQ | ~[\\\r\n\f'] )* '\''
 | '"' ( STRING_ESCAPE_SEQ | ~[\\\r\n\f"] )* '"'
 ;
/// longstring      ::=  "'''" longstringitem* "'''" | '"""' longstringitem* '"""'
fragment LONG_STRING
 : '\'\'\'' LONG_STRING_ITEM*? '\'\'\''
 | '"""' LONG_STRING_ITEM*? '"""'
 ;

/// longstringitem  ::=  longstringchar | stringescapeseq
fragment LONG_STRING_ITEM
 : LONG_STRING_CHAR
 | STRING_ESCAPE_SEQ
 ;

/// longstringchar  ::=  <any source character except "\">
fragment LONG_STRING_CHAR
 : ~'\\'
 ;

/// stringescapeseq ::=  "\" <any source character>
fragment STRING_ESCAPE_SEQ
 : '\\' .
 | '\\' '\n'
 ;

// dummy rule to show handling of 'embeded' tokens
dummy
    : '+';