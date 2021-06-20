package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.modes.antlr.AntlrTokenMaker;
import org.fife.ui.rsyntaxtextarea.modes.antlr.MultiLineTokenInfo;

public class Python3TokenMaker extends AntlrTokenMaker {
	public Python3TokenMaker() {
		super(new MultiLineTokenInfo(0, Token.LITERAL_STRING_DOUBLE_QUOTE,"\"\"\"","\"\"\""));
	}

	@Override
	protected Lexer createLexer(String text) {
		return new Python3Lexer(CharStreams.fromString(text)) {
			@Override
			public void skip() {
				setChannel(HIDDEN);
			}

			@Override
			public org.antlr.v4.runtime.Token nextToken() {
				org.antlr.v4.runtime.Token token = super.nextToken();
				if (token.getType() == Python3Lexer.NEWLINE ||
					token.getType() == Python3Parser.DEDENT) {
					// the Python3 grammar inserts some extra tokens, we need to filter out again
					return nextToken();
				} else {
					return token;
				}
			}
		};
	}

	@Override
	protected int convertType(int type) {
		switch (type) {
			case Python3Lexer.SKIP_:
				return Token.COMMENT_EOL;
			case Python3Lexer.TRUE:
			case Python3Lexer.FALSE:
				return Token.LITERAL_BOOLEAN;

			case Python3Lexer.DEF:
			case Python3Lexer.RETURN:
			case Python3Lexer.RAISE:
			case Python3Lexer.FROM:
			case Python3Lexer.IMPORT:
			case Python3Lexer.AS:
			case Python3Lexer.GLOBAL:
			case Python3Lexer.NONLOCAL:
			case Python3Lexer.ASSERT:
			case Python3Lexer.IF:
			case Python3Lexer.ELIF:
			case Python3Lexer.ELSE:
			case Python3Lexer.WHILE:
			case Python3Lexer.FOR:
			case Python3Lexer.IN:
			case Python3Lexer.TRY:
			case Python3Lexer.FINALLY:
			case Python3Lexer.WITH:
			case Python3Lexer.EXCEPT:
			case Python3Lexer.LAMBDA:
			case Python3Lexer.OR:
			case Python3Lexer.AND:
			case Python3Lexer.NOT:
			case Python3Lexer.IS:
			case Python3Lexer.NONE:
			case Python3Lexer.CLASS:
			case Python3Lexer.YIELD:
			case Python3Lexer.DEL:
			case Python3Lexer.PASS:
			case Python3Lexer.CONTINUE:
			case Python3Lexer.BREAK:
			case Python3Lexer.ASYNC:
			case Python3Lexer.AWAIT:
				return Token.RESERVED_WORD;
			case Python3Lexer.STRING_LITERAL:
			case Python3Lexer.STRING:
				return Token.LITERAL_STRING_DOUBLE_QUOTE;
			case Python3Lexer.NUMBER:
				return Token.LITERAL_NUMBER_DECIMAL_INT;
			case Python3Lexer.FLOAT_NUMBER:
				return Token.LITERAL_NUMBER_FLOAT;
			case Python3Lexer.OPEN_BRACE:
			case Python3Lexer.CLOSE_BRACE:
			case Python3Lexer.OPEN_BRACK:
			case Python3Lexer.CLOSE_BRACK:
			case Python3Lexer.OPEN_PAREN:
			case Python3Lexer.CLOSE_PAREN:
				return Token.SEPARATOR;

		}
		return Token.IDENTIFIER;
	}

	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[]{"#", null};
	}
}
