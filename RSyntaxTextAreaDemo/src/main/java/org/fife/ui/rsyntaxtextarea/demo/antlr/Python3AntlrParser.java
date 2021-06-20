package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class Python3AntlrParser extends AntlrParserBase<Python3Lexer, Python3Parser> {
	@Override
	protected Python3Lexer createLexer(CharStream input) {
		return new Python3Lexer(input);
	}

	@Override
	protected Python3Parser createParser(TokenStream input) {
		return new Python3Parser(input);
	}

	@Override
	protected void parse(Python3Parser parser) {
		parser.file_input();
	}
}
