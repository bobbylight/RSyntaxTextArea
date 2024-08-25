/*
 * 09/01/2014
 *
 * DartTokenMaker.java - Token parser for Dart.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * Scanner for Dart files.  Its states could be simplified, but are
 * kept the way they are to keep a degree of similarity (i.e. copy/paste)
 * between it and HTML/JSP/PHPTokenMaker.  This should cause no difference in
 * performance.<p>
 *
 * This implementation was created using
 * <a href="https://www.jflex.de/">JFlex</a> 1.4.1; however, the generated file
 * was modified for performance.  Memory allocation needs to be almost
 * completely removed to be competitive with the handwritten lexers (subclasses
 * of <code>AbstractTokenMaker</code>), so this class has been modified so that
 * Strings are never allocated (via yytext()), and the scanner never has to
 * worry about refilling its buffer (needlessly copying chars around).
 * We can achieve this because RText always scans exactly 1 line of tokens at a
 * time, and hands the scanner this line as an array of characters (a Segment
 * really).  Since tokens contain pointers to char arrays instead of Strings
 * holding their contents, there is no need for allocating new memory for
 * Strings.<p>
 *
 * The actual algorithm generated for scanning has, of course, not been
 * modified.<p>
 *
 * If you wish to regenerate this file yourself, keep in mind the following:
 * <ul>
 *   <li>The generated <code>DartTokenMaker.java</code> file will contain two
 *       definitions of both <code>zzRefill</code> and <code>yyreset</code>.
 *       You should hand-delete the second of each definition (the ones
 *       generated by the lexer), as these generated methods modify the input
 *       buffer, which we'll never have to do.</li>
 *   <li>You should also change the declaration/definition of zzBuffer to NOT
 *       be initialized.  This is a needless memory allocation for us since we
 *       will be pointing the array somewhere else anyway.</li>
 *   <li>You should NOT call <code>yylex()</code> on the generated scanner
 *       directly; rather, you should use <code>getTokenList</code> as you would
 *       with any other <code>TokenMaker</code> instance.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.0
 */
%%

%public
%class DartTokenMaker
%extends AbstractJFlexCTokenMaker
%unicode
%type org.fife.ui.rsyntaxtextarea.Token


%{

	/**
	 * Token type specifying we're in a JavaScript multi-line comment.
	 */
	static final int INTERNAL_IN_JS_MLC				= -8;

	/**
	 * Token type specifying we're in an invalid multi-line JS string.
	 */
	static final int INTERNAL_IN_JS_STRING_INVALID	= -10;

	/**
	 * Token type specifying we're in a valid multi-line JS string.
	 */
	static final int INTERNAL_IN_JS_STRING_VALID		= -11;

	/**
	 * Token type specifying we're in an invalid multi-line JS single-quoted string.
	 */
	static final int INTERNAL_IN_JS_CHAR_INVALID	= -12;

	/**
	 * Token type specifying we're in a valid multi-line JS single-quoted string.
	 */
	static final int INTERNAL_IN_JS_CHAR_VALID		= -13;

	/**
	 * When in the JS_STRING state, whether the current string is valid.
	 */
	private boolean validJSString;

	/**
	 * The version of JavaScript being highlighted.
	 */
	private static String jsVersion;

	/**
	 * Language state set on JS tokens.  Must be 0.
	 */
	private static final int LANG_INDEX_DEFAULT	= 0;

	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public DartTokenMaker() {
		super();
	}


	static {
		jsVersion = "1.0";
	}


	/**
	 * Adds the token specified to the current linked list of tokens as an
	 * "end token;" that is, at <code>zzMarkedPos</code>.
	 *
	 * @param tokenType The token's type.
	 */
	private void addEndToken(int tokenType) {
		addToken(zzMarkedPos,zzMarkedPos, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
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
	 * Returns the closest {@link TokenTypes "standard" token type} for a given
	 * "internal" token type (e.g. one whose value is <code>&lt; 0</code>).
	 */
	 @Override
	public int getClosestStandardTokenTypeForInternalType(int type) {
		switch (type) {
			case INTERNAL_IN_JS_MLC:
				return TokenTypes.COMMENT_MULTILINE;
			case INTERNAL_IN_JS_STRING_INVALID:
			case INTERNAL_IN_JS_STRING_VALID:
			case INTERNAL_IN_JS_CHAR_INVALID:
			case INTERNAL_IN_JS_CHAR_VALID:
				return TokenTypes.LITERAL_STRING_DOUBLE_QUOTE;
		}
		return type;
	}


	/**
	 * Returns the JavaScript version being highlighted.
	 *
	 * @return Supported JavaScript version.
	 * @see #isJavaScriptCompatible(String)
	 */
	public static String getJavaScriptVersion() {
		return jsVersion;
	}


	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "//", null };
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
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;
		validJSString = true;
		int languageIndex = LANG_INDEX_DEFAULT;

		// Start off in the proper state.
		int state;
		switch (initialTokenType) {
			case Token.LITERAL_STRING_DOUBLE_QUOTE:
				state = DART_MULTILINE_STRING_DOUBLE;
				break;
			case Token.LITERAL_CHAR:
				state = DART_MULTILINE_STRING_SINGLE;
				break;
			case INTERNAL_IN_JS_MLC:
				state = JS_MLC;
				break;
			case INTERNAL_IN_JS_STRING_INVALID:
				state = JS_STRING;
				validJSString = false;
				break;
			case INTERNAL_IN_JS_STRING_VALID:
				state = JS_STRING;
				break;
			case INTERNAL_IN_JS_CHAR_INVALID:
				state = JS_CHAR;
				validJSString = false;
				break;
			case INTERNAL_IN_JS_CHAR_VALID:
				state = JS_CHAR;
				break;
			default:
				state = YYINITIAL;
				break;
		}

		setLanguageIndex(languageIndex);
		start = text.offset;
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
	 * Returns whether features for a specific JS version should be honored
	 * while highlighting.
	 * 
	 * @param version JavaScript version required 
	 * @return Whether the JavaScript version is the same or greater than
	 *         version required. 
	 */
	public static boolean isJavaScriptCompatible(String version) {
		return jsVersion.compareTo(version) >= 0;
	}


	/**
	 * Set the supported JavaScript version because some keywords were
	 * introduced on or after this version.
	 *
	 * @param javaScriptVersion The version of JavaScript to support, such as
	 *        "<code>1.5</code>" or "<code>1.6</code>".
	 * @see #isJavaScriptCompatible(String)
	 * @see #getJavaScriptVersion()
	 */
	public static void setJavaScriptVersion(String javaScriptVersion) {
		jsVersion = javaScriptVersion;
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


%}

Whitespace			= ([ \t\f]+)
LineTerminator			= ([\n])

Letter							= [A-Za-z]
NonzeroDigit						= [1-9]
Digit							= ("0"|{NonzeroDigit})
HexDigit							= ({Digit}|[A-Fa-f])
OctalDigit						= ([0-7])
LetterOrDigit					= ({Letter}|{Digit})
EscapedSourceCharacter				= ("u"{HexDigit}{HexDigit}{HexDigit}{HexDigit})
NonSeparator						= ([^\t\f\r\n\ \(\)\{\}\[\]\;\,\.\=\>\<\!\~\?\:\+\-\*\/\&\|\^\%\"\']|"#"|"\\")
IdentifierStart					= ({Letter}|"_"|"$")
IdentifierPart						= ({IdentifierStart}|{Digit}|("\\"{EscapedSourceCharacter}))
JS_MLCBegin				= "/*"
JS_MLCEnd					= "*/"
JS_LineCommentBegin			= "//"
JS_IntegerHelper1			= (({NonzeroDigit}{Digit}*)|"0")
JS_IntegerHelper2			= ("0"(([xX]{HexDigit}+)|({OctalDigit}*)))
JS_IntegerLiteral			= ({JS_IntegerHelper1}[lL]?)
JS_HexLiteral				= ({JS_IntegerHelper2}[lL]?)
JS_FloatHelper1			= ([fFdD]?)
JS_FloatHelper2			= ([eE][+-]?{Digit}+{JS_FloatHelper1})
JS_FloatLiteral1			= ({Digit}+"."({JS_FloatHelper1}|{JS_FloatHelper2}|{Digit}+({JS_FloatHelper1}|{JS_FloatHelper2})))
JS_FloatLiteral2			= ("."{Digit}+({JS_FloatHelper1}|{JS_FloatHelper2}))
JS_FloatLiteral3			= ({Digit}+{JS_FloatHelper2})
JS_FloatLiteral			= ({JS_FloatLiteral1}|{JS_FloatLiteral2}|{JS_FloatLiteral3}|({Digit}+[fFdD]))
JS_ErrorNumberFormat		= (({JS_IntegerLiteral}|{JS_HexLiteral}|{JS_FloatLiteral}){NonSeparator}+)
JS_Separator				= ([\(\)\{\}\[\]\]])
JS_Separator2				= ([\;,.])
JS_NonAssignmentOperator		= ("+"|"-"|"<="|"^"|"++"|"<"|"*"|">="|"%"|"--"|">"|"/"|"!="|"?"|">>"|"!"|"&"|"=="|":"|">>"|"~"|"||"|"&&"|">>>")
JS_AssignmentOperator		= ("="|"-="|"*="|"/="|"|="|"&="|"^="|"+="|"%="|"<<="|">>="|">>>=")
JS_Operator				= ({JS_NonAssignmentOperator}|{JS_AssignmentOperator})
JS_Identifier				= ({IdentifierStart}{IdentifierPart}*)
JS_ErrorIdentifier			= ({NonSeparator}+)

DART_Annotation				= ("@"{Letter}{LetterOrDigit}*)

URLGenDelim				= ([:\/\?#\[\]@])
URLSubDelim				= ([\!\$&'\(\)\*\+,;=])
URLUnreserved			= ({LetterOrDigit}|"_"|[\-\.\~])
URLCharacter			= ({URLGenDelim}|{URLSubDelim}|{URLUnreserved}|[%])
URLCharacters			= ({URLCharacter}*)
URLEndCharacter			= ([\/\$]|{LetterOrDigit})
URL						= (((https?|f(tp|ile))"://"|"www.")({URLCharacters}{URLEndCharacter})?)


%state JS_STRING
%state JS_CHAR
%state JS_MLC
%state JS_EOL_COMMENT
%state DART_MULTILINE_STRING_DOUBLE
%state DART_MULTILINE_STRING_SINGLE


%%

<YYINITIAL> {

	// Keywords
	"abstract" |
	"assert" |
	"class" |
	"const" |
	"extends" |
	"factory" |
	"get" |
	"implements" |
	"import" |
	"interface" |
	"library" |
	"negate" |
	"new" |
	"null" |
	"operator" |
	"set" |
	"source" |
	"static" |
	"super" |
	"this" |
	"typedef" |
	"var" |
	"final" |
	"if" |
	"else" |
	"for" |
	"in" |
	"is" |
	"while" |
	"do" |
	"switch" |
	"case" |
	"default" |
	"in" |
	"try" |
	"catch" |
	"finally" |
	"break" |
	"continue" |
	"throw" |
	"assert" 					{ addToken(Token.RESERVED_WORD); }
	"return"					{ addToken(Token.RESERVED_WORD_2); }
	
	// Literals.
	"false" |
	"true"						{ addToken(Token.LITERAL_BOOLEAN); }
	"NaN"						{ addToken(Token.RESERVED_WORD); }
	"Infinity"					{ addToken(Token.RESERVED_WORD); }

	// Data types
	"bool" |
	"int" |
	"double" |
	"num" |
	"void"						{ addToken(Token.DATA_TYPE); }
	
	// stdlib types
	"AssertionError" |
	"Clock" |
	"Collection" |
	"Comparable" |
	"Date" |
	"Dispatcher" |
	"Duration" |
	"Expect" |
	"FallThroughError" |
	"Function" |
	"HashMap" |
	"HashSet" |
	"Hashable" |
	"Isolate" |
	"Iterable" |
	"Iterator" |
	"LinkedHashMap" |
	"List" |
	"Map" |
	"Match" |
	"Math" |
	"Object" |
	"Pattern" |
	"Promise" |
	"Proxy" |
	"Queue" |
	"ReceivePort" |
	"RegExp" |
	"SendPort" |
	"Set" |
	"StopWatch" |
	"String" |
	"StringBuffer" |
	"Strings" |
	"TimeZone" |
	"TypeError" |
	
	// stdlib exceptions
	"BadNumberFormatException" |
	"ClosureArgumentMismatchException" |
	"EmptyQueueException" |
	"Exception" |
	"ExpectException" |
	"IllegalAccessException" |
	"IllegalArgumentException" |
	"IllegalJSRegExpException" |
	"IndexOutOfRangeException" |
	"IntegerDivisionByZeroException" |
	"NoMoreElementsException" |
	"NoSuchMethodException" |
	"NotImplementedException" |
	"NullPointerException" |
	"ObjectNotClosureException" |
	"OutOfMemoryException" |
	"StackOverflowException" |
	"UnsupportedOperationException" |
	"WrongArgumentCountException"	{ addToken(Token.FUNCTION); }
	
	{LineTerminator}				{ addNullToken(); return firstToken; }
	{JS_Identifier}					{ addToken(Token.IDENTIFIER); }
	{Whitespace}					{ addToken(Token.WHITESPACE); }

	/* Multi-line string literals. */
	\"\"\"						{ start = zzMarkedPos-3; yybegin(DART_MULTILINE_STRING_DOUBLE); }
	\'\'\'						{ start = zzMarkedPos-3; yybegin(DART_MULTILINE_STRING_SINGLE); }

	/* String/Character literals. */
	[\']							{ start = zzMarkedPos-1; validJSString = true; yybegin(JS_CHAR); }
	[\"]							{ start = zzMarkedPos-1; validJSString = true; yybegin(JS_STRING); }

	{DART_Annotation}				{ addToken(Token.ANNOTATION); }
	/* Comment literals. */
	"/**/"							{ addToken(Token.COMMENT_MULTILINE); }
	{JS_MLCBegin}					{ start = zzMarkedPos-2; yybegin(JS_MLC); }
	{JS_LineCommentBegin}			{ start = zzMarkedPos-2; yybegin(JS_EOL_COMMENT); }

	/* Deprecated stuff */
	"#library" |
	"#import" |
	"#source" |
	"#resource"						{ addToken(Token.RESERVED_WORD); }
	
	/* Separators. */
	{JS_Separator}					{ addToken(Token.SEPARATOR); }
	{JS_Separator2}					{ addToken(Token.IDENTIFIER); }

	/* Operators. */
	{JS_Operator}					{ addToken(Token.OPERATOR); }

	/* Numbers */
	{JS_IntegerLiteral}				{ addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
	{JS_HexLiteral}				{ addToken(Token.LITERAL_NUMBER_HEXADECIMAL); }
	{JS_FloatLiteral}				{ addToken(Token.LITERAL_NUMBER_FLOAT); }
	{JS_ErrorNumberFormat}			{ addToken(Token.ERROR_NUMBER_FORMAT); }

	{JS_ErrorIdentifier}			{ addToken(Token.ERROR_IDENTIFIER); }

	/* Ended with a line not in a string or comment. */
	<<EOF>>						{ addNullToken(); return firstToken; }

	/* Catch any other (unhandled) characters and flag them as bad. */
	.							{ addToken(Token.ERROR_IDENTIFIER); }

}

<DART_MULTILINE_STRING_DOUBLE> {
	[^\"\\\n]*				{}
	\\.?						{ /* Skip escaped chars, handles case: '\"""'. */ }
	\"\"\"					{ addToken(start,zzStartRead+2, Token.LITERAL_STRING_DOUBLE_QUOTE); yybegin(YYINITIAL); }
	\"						{}
	\n |
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.LITERAL_STRING_DOUBLE_QUOTE); return firstToken; }
}


<DART_MULTILINE_STRING_SINGLE> {
	[^\'\\\n]*				{}
	\\.?						{ /* Skip escaped chars, handles case: "\'''". */ }
	\'\'\'					{ addToken(start,zzStartRead+2, Token.LITERAL_CHAR); yybegin(YYINITIAL); }
	\'						{}
	\n |
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.LITERAL_CHAR); return firstToken; }
}

<JS_STRING> {
	[^\n\\\"]+				{}
	\\x{HexDigit}{2}		{}
	\\x						{ /* Invalid latin-1 character \xXX */ validJSString = false; }
	\\u{HexDigit}{4}		{}
	\\u						{ /* Invalid Unicode character \\uXXXX */ validJSString = false; }
	\\.						{ /* Skip all escaped chars. */ }
	\\						{ /* Line ending in '\' => continue to next line. */
								if (validJSString) {
									addToken(start,zzStartRead, Token.LITERAL_STRING_DOUBLE_QUOTE);
									addEndToken(INTERNAL_IN_JS_STRING_VALID);
								}
								else {
									addToken(start,zzStartRead, Token.ERROR_STRING_DOUBLE);
									addEndToken(INTERNAL_IN_JS_STRING_INVALID);
								}
								return firstToken;
							}
	\"						{ int type = validJSString ? Token.LITERAL_STRING_DOUBLE_QUOTE : Token.ERROR_STRING_DOUBLE; addToken(start,zzStartRead, type); yybegin(YYINITIAL); }
	\n |
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken; }
}

<JS_CHAR> {
	[^\n\\\']+				{}
	\\x{HexDigit}{2}		{}
	\\x						{ /* Invalid latin-1 character \xXX */ validJSString = false; }
	\\u{HexDigit}{4}		{}
	\\u						{ /* Invalid Unicode character \\uXXXX */ validJSString = false; }
	\\.						{ /* Skip all escaped chars. */ }
	\\						{ /* Line ending in '\' => continue to next line. */
								if (validJSString) {
									addToken(start,zzStartRead, Token.LITERAL_CHAR);
									addEndToken(INTERNAL_IN_JS_CHAR_VALID);
								}
								else {
									addToken(start,zzStartRead, Token.ERROR_CHAR);
									addEndToken(INTERNAL_IN_JS_CHAR_INVALID);
								}
								return firstToken;
							}
	\'						{ int type = validJSString ? Token.LITERAL_CHAR : Token.ERROR_CHAR; addToken(start,zzStartRead, type); yybegin(YYINITIAL); }
	\n |
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.ERROR_CHAR); addNullToken(); return firstToken; }
}

<JS_MLC> {
	// JavaScript MLC's.  This state is essentially Java's MLC state.
	[^hwf\n\*]+			{}
	{URL}					{ int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_MULTILINE); start = zzMarkedPos; }
	[hwf]					{}
	{JS_MLCEnd}				{ yybegin(YYINITIAL); addToken(start,zzStartRead+1, Token.COMMENT_MULTILINE); }
	\*						{}
	\n |
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addEndToken(INTERNAL_IN_JS_MLC); return firstToken; }
}

<JS_EOL_COMMENT> {
	[^hwf\n]+				{}
	{URL}					{ int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_EOL); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_EOL); start = zzMarkedPos; }
	[hwf]					{}
	\n |
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken; }
}
