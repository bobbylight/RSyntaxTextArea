/*
 * ORIGINAL VERSION SOURCE LINK
 * https://github.com/logisim-evolution/
 *
 *
 * THIS IS MY VERSION
 * File current version https://github.com/Var7600/VHDL_GENERATOR
 *
 *
 * VhdlTokenMaker.java - Scanner for the VHDL hardware description language.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;

 
 
/**
 * A parser for the VHDL hardware description programming language.
 *
 * @author DOUDOU DIAWARA
 * @version 0.0
 *
 */
%%


%public
%class VhdlTokenMaker
%extends AbstractJFlexTokenMaker
%unicode
%type org.fife.ui.rsyntaxtextarea.Token 
%ignorecase

%{
    /**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public VhdlTokenMaker() {
		super();
	}
    
    /**
	* Adds the token specified to the current linked list of tokens.
	*
	*@param tokenType The token's type.
	*/
    private void addToken(int tokenType){
          addToken(zzStartRead, zzMarkedPos-1, tokenType);
    }
	
	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so);
	}
	
	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *                    occurs.
	 */
	 @Override
	 public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
		super.addToken(array, start, end, tokenType, startOffset);
		zzStartRead = zzMarkedPos;
	}
	
		@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "--", null };
	}
	
	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	@Override
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;
        // VHDL only needs the initial state
		s = text;
		try {
			yyreset(zzReader);
			yybegin(YYINITIAL);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}
	
	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream
	 */
	public final void yyreset(Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtBOL  = true;
		zzAtEOF  = false;
	}
	
	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}
  
%}

LineTerminator				= (\n|\r|\r\n)
WhiteSpace				= ([ \t\f])
NonzeroDigit						= [1-9]
Digit							= ("0"|{NonzeroDigit})
HexDigit							= ({Digit}|[A-Fa-f])
EscapedSourceCharacter				= ("\\"{HexDigit}{HexDigit}{HexDigit}{HexDigit})
Letter							= [A-Za-z]
LetterOrUnderscore				= ({Letter}|"_")
IdentifierStart					= ({LetterOrUnderscore})
IdentifierPart						= ({IdentifierStart}|{Digit})
Identifier				=  ({IdentifierStart}{IdentifierPart}*)

/*String literals */
stringliteral           = \"([^\"\n\r])*\"

/* Numbers */

integer_literal         = ([-]?[+]?{NonzeroDigit}{Digit}*|"0")
exponent			    = ({integer_literal}+.{integer_literal}+[e|E]{integer_literal}+)
floatnumber		        = ({integer_literal}+.{integer_literal}+)
binary_literal          =  [Bb]\"[01](_?[01])*\"
octal_literal           =  [Oo]\"[0-7](_?[0-7])*\"
hex_literal             =  [Xx]\"[0-9A-Fa-f](_?[0-9A-Fa-f])*\"

/* Comments */
Comment = "--".*

%%

<YYINITIAL> {

/* Keywords */
"access" |
"after" |
"alias" |
"all" |
"architecture" |
"array" |
"assert" |
"attribute" |
"begin" |
"block" |
"body" |
"buffer" |
"bus" |
"case" |
"component" |
"configuration" |
"constant" |
"disconnect" |
"downto" |
"else" |
"elsif" |
"end" |
"end" |
"entity" |
"exit" |
"file" |
"for" |
"function" |
"generate" |
"generic" |
"group" |
"guarded" |
"if" |
"impure" |
"in" |
"inertial" |
"inout" |
"is" |
"label" |
"library" |
"linkage" |
"literal" |
"loop" |
"map" |
"new" |
"rising\_edge" |
"next" |
"null" |
"of" |
"on" |
"open" |
"others" |
"out" |
"package" |
"port" |
"postponed" |
"procedure" |
"process" |
"pure" |
"range" |
"record" |
"register" |
"reject" |
"report" |
"return" |
"select" |
"severity" |
"shared" |
"signal" |
"subtype" |
"then" |
"to" |
"transport" |
"type" |
"unaffected" |
"units" |
"until" |
"use" |
"variable" |
"wait" |
"when" |
"while" |
"with"  { addToken(Token.RESERVED_WORD); }

/* Keywords 2 (JUST AN OPTIONAL SET OF KEYWORDS COLORED DIFFERENTLY) */
"'ACTIVE" |
"'ASCENDING" |
"'BASE" |
"'DELAYED" |
"'DRIVING" |
"'DRIVING_VALUE" |
"'EVENT" |
"'HIGH" |
"'IMAGE" |
"'INSTANCE_NAME" |
"'LAST_ACTIVE" |
"'LAST_EVENT" |
"'LAST_VALUE" |
"'LEFT" |
"'LEFTOF" |
"'LOW" |
"'PATH_NAME" |
"'POS" |
"'PRED" |
"'QUIET" |
"'RANGE" |
"'REVERSE_RANGE" |
"'RIGHT" |
"'RIGHTOF" |
"'SIMPLE_NAME" |
"'STABLE" |
"'SUCCESS" |
"'TRANSACTION" |
"'VAL" |
"'VALUE"    { addToken(Token.RESERVED_WORD); }

/* Numbers */
{integer_literal}    { addToken(Token.LITERAL_NUMBER_DECIMAL_INT) ;}
{binary_literal}     { addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
{octal_literal}      { addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
{hex_literal}        { addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
{exponent}           { addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
{floatnumber}        { addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }

/* WhiteSpace */
{WhiteSpace}+        { addToken(Token.WHITESPACE); }

 /* Separators */
"(" |
")" |
"[" |
"]" |
";" |
"," |
":" |
"|" |
"=>"|
"<="|
":="                 { addToken(Token.SEPARATOR); }

/* Data Types */
"bit" |
"bit_vector" |
"boolean" |
"integer" |
"real" |
"natural" |
"positive" |
"std_logic" |
"std_logic_unsigned" |
"std_logic_vector" |
"std_logic_signed"		{ addToken(Token.DATA_TYPE); }
	
/* Functions */
"'-'" |
"'0'" |
"'1'" |
"'H'" |
"'L'" |
"'U'" |
"'W'" |
"'X'" |
"'Z'" |
"false" |
"true"		{ addToken(Token.FUNCTION); }

/* OPERATORS. */
"&" |
"**" |
"abs" |
"mod" |
"rem" |
"sll" |
"srl" |
"sla" |
"sra" |
"rol" |
"ror" |
"not" |
"and" |
"or" |
"nand" |
"nor" |
"xor" |
"xnor"		{ addToken(Token.OPERATOR); }

/* String/Character Literals. */
{stringliteral}				{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }

/*COMMENTS*/
{Comment}                 {  addToken(Token.COMMENT_EOL); }


{Identifier}             { addToken(Token.IDENTIFIER);}

.                        { addToken(Token.ERROR_IDENTIFIER); }

{LineTerminator}         { addNullToken(); return firstToken; }

/* Ended with a line not in a string or comment. */
	<<EOF>>				{ addNullToken(); return firstToken; }
}
