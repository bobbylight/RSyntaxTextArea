/*
 * 08/12/2025
 *
 * LatexTokenMaker.java - Scanner for LaTeX.
 * 
 * This library is distributed under a modified BSD license. See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * Scanner for LaTeX.<p>
 *
 * This implementation was created using <a href="https://www.jflex.de/">JFlex</a> 1.9.1.
 *
 * @author Mattia Marelli
 * @version 1.0
 *
 */
%%

%public
%class LatexTokenMaker
%extends AbstractJFlexTokenMaker
%unicode
%type org.fife.ui.rsyntaxtextarea.Token


%{


	/**
	 * Constructor. This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public LatexTokenMaker() {
	}

    @Override
    public void addNullToken() {
        if(currentState >= 0) {
            super.addNullToken();
        } else {
            super.addToken(s.array, start, start - 1, currentState, start);
        }
    }

    protected void addNullToken(int value) {
        super.addToken(s.array, start, start - 1, value, start);
    }

    /**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
        // take care of the math mode
        if(currentState != 0) {
            super.setLanguageIndex(1);
        } else {
            super.setLanguageIndex(0);
        }

		super.addToken(s.array, s.offset + zzStartRead, s.offset + zzMarkedPos - 1, tokenType, start + zzStartRead);
	}

    private void addToken(int tokenType, int languageIndex) {
        super.setLanguageIndex(languageIndex);
        super.addToken(s.array, s.offset + zzStartRead, s.offset + zzMarkedPos - 1, tokenType, start + zzStartRead);
    }

    private void addHyperlinkToken(int tokenType) {
        super.setLanguageIndex(0); // Hyperlinks cannot be found into math blocks
		super.addToken(s.array, s.offset + zzStartRead, s.offset + zzMarkedPos - 1, tokenType, start + zzStartRead, true);
	}

	/**
	 * ${inheritDoc}
	 */
	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "%", null };
	}

    @Override
	public void yyclose() {
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
        super.s = text;
        super.start = startOffset;

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;

        int state = YYINITIAL;
        setLanguageIndex(0);
        currentState = 0;

		if(initialTokenType == STATE_OPT) {
            state = OPT;
        } else if (initialTokenType < -1) { // math mode
            currentState = (byte) initialTokenType;
        }

		s = text;

		try {
			reset(text, 0, text.count, state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}

    private void switchMath(byte type) {
        if(type == currentState) {
            currentState = 0;
        } else {
            currentState = type;
        }
    }

    private void addTokenStripWS(int tokenType) {
        // take care of the math mode
        if(currentState != 0) {
            super.setLanguageIndex(1);
        } else {
            super.setLanguageIndex(0);
        }

        String text = yytext().trim();
        int textL = yylength() - text.length();

        if(textL != 0) {
            textL = text.length();
            super.addToken(s.array, s.offset + zzStartRead, s.offset + zzStartRead + textL - 1, tokenType, start + zzStartRead);
            super.addToken(s.array, s.offset + zzStartRead + textL, s.offset + zzMarkedPos - 1, TokenTypes.WHITESPACE, start + zzStartRead + textL);
        } else {
            super.addToken(s.array, s.offset + zzStartRead, s.offset + zzMarkedPos - 1, tokenType, start + zzStartRead);
        }
    }

    byte currentState;
    static byte STATE_INLINE_MATH_1 = -2;
    static byte STATE_INLINE_MATH_2 = -3;
    static byte STATE_DISPLAY_MATH_1 = -4;
    static byte STATE_DISPLAY_MATH_2 = -5;
    static byte STATE_OPT = -1;
%}

Spaces                  = "\\" [;:,!]
Sectioning              = "\\" ( "part" | "chapter" | "section" | "subsection" | "subsubsection" | "paragraph" | "subparagraph" ) "*"?
SpecialCommand          = "\\" ("newcommand" | "renewcommand" | "newenvironment" | "renewenvironment" | "newcounter" | "newlength"
                            | "def"
                            | "documentclass" | "usepackage" )
Letter					= ([A-Za-z])
Digit					= ([0-9])
LetterOrUnderscore		= ({Letter}|[_])
AnyChar					= ({LetterOrUnderscore}|{Digit}|[\-])
Whitespace				= ([ \t\f])
LineCommentBegin		= "%"

URLGenDelim				= ([:\/\?#\[\]@])
URLSubDelim				= ([\!\$&'\(\)\*\+,;=])
URLUnreserved			= ({LetterOrUnderscore}|{Digit}|[\-\.\~])
URLCharacter			= ({URLGenDelim}|{URLSubDelim}|{URLUnreserved}|[%])
URLCharacters			= ({URLCharacter}*)
URLEndCharacter			= ([\/\$]|{Letter}|{Digit})
URL						= (((https?|f(tp|ile))"://"|"www.")({URLCharacters}{URLEndCharacter})?)


%state EOL_COMMENT
%state ENV
%state OPT

%%

<YYINITIAL> {
    "\\usepackage"              { addToken(Token.RESERVED_WORD); yybegin(OPT); }
    "\\documentclass"           { addToken(Token.RESERVED_WORD); yybegin(OPT); }
    "\\begin" {Whitespace}* "{" { yypushback(1); addTokenStripWS(Token.RESERVED_WORD); yybegin(ENV); }
    "\\end" {Whitespace}* "{"   { yypushback(1); addTokenStripWS(Token.RESERVED_WORD); yybegin(ENV); }
    "\\label" {Whitespace}* "{" { yypushback(1); addTokenStripWS(Token.RESERVED_WORD); yybegin(ENV); }
    "\\" ((eq|page)? "ref")
       {Whitespace}* "{"        { yypushback(1); addTokenStripWS(Token.FUNCTION); yybegin(ENV); }

    {Spaces}                    { addToken(Token.FUNCTION); }
    {Sectioning}                { addToken(Token.VARIABLE); }
    {SpecialCommand}            { addToken(Token.RESERVED_WORD); }
	([\\]{AnyChar}+)			{ addToken(Token.FUNCTION); }
	([\\]%)						{ addToken(Token.FUNCTION); }
	[\{\}]						{ addToken(Token.SEPARATOR); }

	{Whitespace}				{ addToken(Token.WHITESPACE); }

	{LineCommentBegin}			{ addToken(Token.COMMENT_EOL); yybegin(EOL_COMMENT); }

    // MATH BLOCKS
    "$$"                        { addToken(Token.SEPARATOR, 1); switchMath(STATE_DISPLAY_MATH_1); }
    "\\["                       { addToken(Token.SEPARATOR, 1); currentState = STATE_DISPLAY_MATH_2; }
    "\\]"                       { addToken(Token.SEPARATOR, 1); currentState = 0; }
    "\\("                       { addToken(Token.SEPARATOR, 1); currentState = STATE_INLINE_MATH_2; }
    "\\)"                       { addToken(Token.SEPARATOR, 1); currentState = 0; }
    "$"                         { addToken(Token.SEPARATOR, 1); switchMath(STATE_INLINE_MATH_1); }

	"\n" 						{ addNullToken(); return firstToken; }
	<<EOF>>						{ addNullToken(); return firstToken; }

	/* Catch any other (unhandled) characters and flag them as identifiers. */
	[^]							{ addToken(Token.IDENTIFIER); }

}

<OPT> {
    ","                     { addToken(Token.OPERATOR); }
    "{"                     { addToken(Token.SEPARATOR); yybegin(ENV); }
    "["                     { addToken(Token.SEPARATOR); }
    "]"                     { addToken(Token.SEPARATOR); yybegin(ENV);}
    {Whitespace}            { addToken(Token.WHITESPACE); }
    [^]                     { addToken(Token.PREPROCESSOR); }
    <<EOF>> 				{ addNullToken(); return firstToken; }
}

<ENV> {
    {Whitespace}            { addToken(Token.WHITESPACE); }
    "{"                     { addToken(Token.SEPARATOR); }
    "}"                     { addToken(Token.SEPARATOR); yybegin(YYINITIAL); }
    [^{}]+                  { addToken(Token.REGEX); }
    <<EOF>> 				{ addNullToken(); return firstToken; }
}

<EOL_COMMENT> {
	[^hwf\n]+				{ addToken(Token.COMMENT_EOL); }
	{URL}					{ addHyperlinkToken(Token.COMMENT_EOL);  }
	[hwf]					{ addToken(Token.COMMENT_EOL); }
	\n  					{ addNullToken(); return firstToken; }
	<<EOF>>					{ addNullToken(); return firstToken; }
}
