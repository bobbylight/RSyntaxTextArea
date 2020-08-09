package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.modes.antlr.AntlrTokenMaker;

public class MySqlTokenMaker extends AntlrTokenMaker {
	public MySqlTokenMaker() {
		super(new MultiLineTokenInfo(0,Token.COMMENT_DOCUMENTATION,"/*!","*/"),
			new MultiLineTokenInfo(0,Token.COMMENT_MULTILINE,"/*","*/"));
	}

	@Override
	protected Lexer createLexer(String text) {
		return new MySqlLexer(CharStreams.fromString(text));
	}

	@Override
	protected int convertType(int type) {
		// fast path for keywords (they are neatly aligned)
		if (type >= MySqlLexer.ADD && type <= MySqlLexer.ZEROFILL) {
			return Token.RESERVED_WORD;
		}
		switch (type) {
			case MySqlLexer.LINE_COMMENT:
				return Token.COMMENT_EOL;
			case MySqlLexer.SPEC_MYSQL_COMMENT:
				return Token.COMMENT_DOCUMENTATION;
			case MySqlLexer.COMMENT_INPUT:
				return Token.COMMENT_MULTILINE;

			case MySqlLexer.DECIMAL_LITERAL:
				return Token.LITERAL_NUMBER_DECIMAL_INT;
			case MySqlLexer.REAL_LITERAL:
				return Token.LITERAL_NUMBER_FLOAT;
			case MySqlLexer.HEXADECIMAL_LITERAL:
				return Token.LITERAL_NUMBER_HEXADECIMAL;
			case MySqlLexer.STRING_LITERAL:
				return Token.LITERAL_STRING_DOUBLE_QUOTE;
		}
		return Token.IDENTIFIER;
	}

	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[]{"-- ",null};
	}
}
