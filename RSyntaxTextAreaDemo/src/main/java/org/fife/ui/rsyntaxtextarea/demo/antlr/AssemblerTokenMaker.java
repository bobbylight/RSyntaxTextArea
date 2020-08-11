package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.modes.antlr.AntlrTokenMaker;

public class AssemblerTokenMaker extends AntlrTokenMaker {
	@Override
	protected Lexer createLexer(String text) {
		return new AssemblerLexer(CharStreams.fromString(text)) {
			@Override
			public void skip() {
				setChannel(HIDDEN);
			}
		};
	}

	@Override
	protected int convertType(int type) {
		switch (type) {
			case AssemblerParser.OPCODE:
				return Token.OPERATOR;
			case AssemblerParser.COMMENT:
				return Token.COMMENT_EOL;
			case AssemblerParser.INT:
				return Token.LITERAL_NUMBER_DECIMAL_INT;
			case AssemblerParser.HEX:
				return Token.LITERAL_NUMBER_HEXADECIMAL;
			case AssemblerParser.OCT:
				return Token.LITERAL_NUMBER_FLOAT;
			case AssemblerParser.BIN:
				return Token.LITERAL_BOOLEAN;
			case AssemblerParser.STRING:
				return Token.LITERAL_STRING_DOUBLE_QUOTE;
			case AssemblerParser.CHAR:
				return Token.LITERAL_CHAR;
			case AssemblerParser.DIRECTIVE:
				return Token.RESERVED_WORD;
			case AssemblerParser.ID:
				return Token.VARIABLE;
		}
		return Token.IDENTIFIER;
	}

	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[]{";", null};
	}

}
