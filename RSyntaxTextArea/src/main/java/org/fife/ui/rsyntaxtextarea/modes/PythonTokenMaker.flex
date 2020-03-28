/*
 * 12/06/2005
 *
 * PythonTokenMaker.java - Token maker for the Python programming language.
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
 * Scanner for the Python programming language.
 *
 * @author Robert Futrell
 * @version 0.3
 */
%%

%public
%class PythonTokenMaker
%extends AbstractJFlexTokenMaker
%unicode
%type org.fife.ui.rsyntaxtextarea.Token


%{


	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public PythonTokenMaker() {
		super();
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
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
		super.addToken(array, start,end, tokenType, startOffset);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "#", null };
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

		// Start off in the proper state.
		int state = Token.NULL;
		switch (initialTokenType) {
			case Token.LITERAL_STRING_DOUBLE_QUOTE:
				state = LONG_STRING_2;
				break;
			case Token.LITERAL_CHAR:
				state = LONG_STRING_1;
				break;
			default:
				state = Token.NULL;
		}

		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
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

/* This part is taken from http://www.python.org/doc/2.2.3/ref/grammar.txt */
identifier		= (({letter}|"_")({letter}|{digit}|"_")*)
letter			= ({lowercase}|{uppercase})
lowercase			= ([a-z])
uppercase			= ([A-Z])
digit			= ([0-9])
stringliteral		= ({stringprefix}?{shortstring})
stringprefix		= ("r"|"u"[rR]?|"R"|"U"[rR]?)
shortstring1		= ([\']{shortstring1item}*[\']?)
shortstring2		= ([\"]{shortstring2item}*[\"]?)
shortstring		= ({shortstring1}|{shortstring2})
shortstring1item	= ({shortstring1char}|{escapeseq})
shortstring2item	= ({shortstring2char}|{escapeseq})
shortstring1char	= ([^\\\n\'])
shortstring2char	= ([^\\\n\"])
escapeseq			= ([\\].)
longinteger		= ({integer}[lL])
integer			= ({decimalinteger}|{octinteger}|{hexinteger})
decimalinteger		= ({nonzerodigit}{digit}*|"0")
octinteger		= ("0"{octdigit}+)
hexinteger		= ("0"[xX]{hexdigit}+)
nonzerodigit		= ([1-9])
octdigit			= ([0-7])
hexdigit			= ({digit}|[a-f]|[A-F])
floatnumber		= ({pointfloat}|{exponentfloat})
pointfloat		= ({intpart}?{fraction}|{intpart}".")
exponentfloat		= (({intpart}|{pointfloat}){exponent})
intpart			= ({digit}+)
fraction			= ("."{digit}+)
exponent			= ([eE][\+\-]?{digit}+)
imagnumber		= (({floatnumber}|{intpart})[jJ])
annotation      = ("@"{identifier})

ErrorNumberFormat	= ({digit}{NonSeparator}+)
NonSeparator		= ([^\t\f\r\n\ \(\)\{\}\[\]\;\,\.\=\>\<\!\~\?\:\+\-\*\/\&\|\^\%\"\']|"#")

LongStringStart1	= ({stringprefix}?\'\'\')
LongStringStart2	= ({stringprefix}?\"\"\")

LineTerminator		= (\n)
WhiteSpace		= ([ \t\f])

LineComment		= ("#".*)


%state LONG_STRING_1
%state LONG_STRING_2


%%

<YYINITIAL> {

    /* Keywords */
    "and" |
    "as" |
    "assert" |
    "break" |
    "class" |
    "continue" |
    "def" |
    "del" |
    "elif" |
    "else" |
    "except" |
    "exec" |
    "finally" |
    "for" |
    "from" |
    "global" |
    "if" |
    "import" |
    "in" |
    "is" |
    "lambda" |
    "not" |
    "or" |
    "pass" |
    "print" |
    "raise" |
    "return" |
    "try" |
    "while" |
    "yield"					{ addToken(Token.RESERVED_WORD); }

    /* Data types. */
    "char" |
    "double" |
    "float" |
    "int" |
    "long" |
    "short" |
    "signed" |
    "unsigned" |
    "void"					{ addToken(Token.DATA_TYPE); }

    /* Standard functions */
	"abs" |
	"apply" |
	"bool" |
	"buffer" |
	"callable" |
	"chr" |
	"classmethod" |
	"cmp" |
	"coerce" |
	"compile" |
	"complex" |
	"delattr" |
	"dict" |
	"dir" |
	"divmod" |
	"enumerate" |
	"eval" |
	"execfile" |
	"file" |
	"filter" |
	"float" |
	"getattr" |
	"globals" |
	"hasattr" |
	"hash" |
	"hex" |
	"id" |
	"input" |
	"int" |
	"intern" |
	"isinstance" |
	"issubclass" |
	"iter" |
	"len" |
	"list" |
	"locals" |
	"long" |
	"map" |
	"max" |
	"min" |
	"object" |
	"oct" |
	"open" |
	"ord" |
	"pow" |
	"property" |
	"range" |
	"raw_input" |
	"reduce" |
	"reload" |
	"repr" |
	"round" |
	"setattr" |
	"slice" |
	"staticmethod" |
	"str" |
	"sum" |
	"super" |
	"tuple" |
	"type" |
	"unichr" |
	"unicode" |
	"vars" |
	"xrange" |
	"zip"					{ addToken(Token.FUNCTION); }

    "True" |
    "False" |
    "None"                          { addToken(Token.LITERAL_BOOLEAN); }

	{LineTerminator}				{ addNullToken(); return firstToken; }

	{identifier}					{ addToken(Token.IDENTIFIER); }

	{WhiteSpace}+					{ addToken(Token.WHITESPACE); }

	/* String/Character Literals. */
	{stringliteral}				{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
	{LongStringStart1}				{ yybegin(LONG_STRING_1); addToken(Token.LITERAL_CHAR); }
	{LongStringStart2}				{ yybegin(LONG_STRING_2); addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }

	/* Comment Literals. */
	{LineComment}					{ addToken(Token.COMMENT_EOL); }

	/* Separators. */
	"(" |
	")" |
	"[" |
	"]" |
	"{" |
	"}"							{ addToken(Token.SEPARATOR); }

	/* Operators. */
	"=" |
	"+" |
	"-" |
	"*" |
	"/" |
	"%" |
	"**" |
	"~" |
	"<" |
	">" |
	"<<" |
	">>" |
	"==" |
	"+=" |
	"-=" |
	"*=" |
	"/=" |
	"%=" |
	">>=" |
	"<<=" |
	"^" |
	"&" |
	"&&" |
	"|" |
	"||" |
	"?" |
	":" |
	"," |
	"!" |
	"++" |
	"--" |
	"." |
	","							{ addToken(Token.OPERATOR); }

	/* Numbers */
	{longinteger}|{integer}			{ addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
	{floatnumber}|{imagnumber}		{ addToken(Token.LITERAL_NUMBER_FLOAT); }
	{ErrorNumberFormat}				{ addToken(Token.ERROR_NUMBER_FORMAT); }

	/* Other punctuation, we'll highlight it as "identifiers." */
    {annotation}                { addToken(Token.ANNOTATION); }
	";"							{ addToken(Token.IDENTIFIER); }

	/* Ended with a line not in a string or comment. */
	<<EOF>>						{ addNullToken(); return firstToken; }

	/* Catch any other (unhandled) characters and flag them as bad. */
	.							{ addToken(Token.ERROR_IDENTIFIER); }

}

<LONG_STRING_1> {
	[^\']+						{ addToken(Token.LITERAL_CHAR); }
	"'''"						{ yybegin(YYINITIAL); addToken(Token.LITERAL_CHAR); }
	"'"							{ addToken(Token.LITERAL_CHAR); }
	<<EOF>>						{
									if (firstToken==null) {
										addToken(Token.LITERAL_CHAR); 
									}
									return firstToken;
								}
}

<LONG_STRING_2> {
	[^\"]+						{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
	\"\"\"						{ yybegin(YYINITIAL); addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
	\"							{ addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }
	<<EOF>>						{
									if (firstToken==null) {
										addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); 
									}
									return firstToken;
								}
}
