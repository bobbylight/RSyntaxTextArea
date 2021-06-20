package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.modes.antlr.AntlrTokenMaker;

public class ErlangTokenMaker extends AntlrTokenMaker {
	@Override
	protected Lexer createLexer(String text) {
		return new ErlangLexer(CharStreams.fromString(text)){
			@Override
			public void skip() {
				setChannel(HIDDEN);
			}
		};
	}

	@Override
	protected int convertType(int type) {
		switch (type){
			case ErlangLexer.Comment:
				return Token.COMMENT_EOL;
			case ErlangLexer.AttrName:
				return Token.VARIABLE;
			case ErlangLexer.TokAtom:
				return Token.RESERVED_WORD;
			case ErlangLexer.TokChar:
				return Token.LITERAL_CHAR;
			case ErlangLexer.TokFloat:
				return Token.LITERAL_NUMBER_FLOAT;
			case ErlangLexer.TokInteger:
				return Token.LITERAL_NUMBER_DECIMAL_INT;
			case ErlangLexer.TokString:
				return Token.LITERAL_STRING_DOUBLE_QUOTE;
		}
		return Token.IDENTIFIER;
	}

	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[]{"%", null};
	}
}
