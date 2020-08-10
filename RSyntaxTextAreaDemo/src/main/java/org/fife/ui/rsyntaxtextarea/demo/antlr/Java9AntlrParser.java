package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class Java9AntlrParser extends AntlrParserBase<Java9Lexer, Java9Parser> {
	@Override
	protected Java9Lexer createLexer(CharStream input) {
		return new Java9Lexer(input);
	}

	@Override
	protected Java9Parser createParser(TokenStream input) {
		return new Java9Parser(input);
	}

	@Override
	protected void parse(Java9Parser parser) {
		parser.compilationUnit();
	}
}
