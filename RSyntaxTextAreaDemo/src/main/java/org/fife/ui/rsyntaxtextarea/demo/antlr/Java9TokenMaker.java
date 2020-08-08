package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.modes.antlr.AntlrTokenMaker;

public class Java9TokenMaker extends AntlrTokenMaker {
	public Java9TokenMaker() {
		super(new MultiLineTokenInfo(0,Token.COMMENT_MULTILINE,"/*","*/"));
	}

	@Override
	protected Lexer createLexer(String text) {
		return new Java9Lexer(CharStreams.fromString(text)){
			@Override
			public void skip() {
				setChannel(HIDDEN);
			}
		};
	}

	@Override
	protected int convertType(int type) {
		switch (type){
			case Java9Lexer.LINE_COMMENT:
				return Token.COMMENT_EOL;
			case Java9Lexer.COMMENT:
				return Token.COMMENT_MULTILINE;
			case Java9Lexer.BooleanLiteral:
				return Token.LITERAL_BOOLEAN;
			case Java9Lexer.CharacterLiteral:
				return Token.LITERAL_CHAR;
			case Java9Lexer.FloatingPointLiteral:
				return Token.LITERAL_NUMBER_FLOAT;
			case Java9Lexer.IntegerLiteral:
				return Token.LITERAL_NUMBER_DECIMAL_INT;
			case Java9Lexer.NullLiteral:
				return Token.RESERVED_WORD;
			case Java9Lexer.StringLiteral:
				return Token.LITERAL_STRING_DOUBLE_QUOTE;
			case Java9Lexer.Identifier:
				return Token.IDENTIFIER;
			case Java9Lexer.LBRACK:
			case Java9Lexer.RBRACK:
			case Java9Lexer.LPAREN:
			case Java9Lexer.RPAREN:
			case Java9Lexer.LBRACE:
			case Java9Lexer.RBRACE:
				return Token.SEPARATOR;
		}
		return Token.IDENTIFIER;
	}

	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[]{"//", null};
	}

	@Override
	public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
		return true;
	}
}
