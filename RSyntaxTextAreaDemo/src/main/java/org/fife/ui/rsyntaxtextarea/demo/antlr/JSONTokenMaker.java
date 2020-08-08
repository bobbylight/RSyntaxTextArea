package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.modes.antlr.AntlrTokenMaker;

public class JSONTokenMaker extends AntlrTokenMaker {
	@Override
	protected Lexer createLexer(String text) {
		return new JSONLexer(CharStreams.fromString(text)){
			@Override
			public void skip() {
				setChannel(HIDDEN);
			}
		};
	}

	@Override
	protected int convertType(int type) {
		switch (type){
			case JSONLexer.NUMBER:
				return Token.LITERAL_NUMBER_FLOAT;
			case JSONLexer.STRING:
				return Token.LITERAL_STRING_DOUBLE_QUOTE;
			case JSONLexer.T__0:
			case JSONLexer.T__1:
			case JSONLexer.T__2:
			case JSONLexer.T__3:
			case JSONLexer.T__4:
			case JSONLexer.T__5:
				return Token.SEPARATOR;
			case JSONLexer.T__6:
			case JSONLexer.T__7:
				return Token.LITERAL_BOOLEAN;
			case JSONLexer.T__8:
				return Token.RESERVED_WORD;
		}
		return Token.IDENTIFIER;
	}

	@Override
	public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
		return true;
	}
}
