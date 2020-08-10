package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class GoAntlrParser extends AntlrParserBase<GoLexer, GoParser> {
	@Override
	protected GoLexer createLexer(CharStream input) {
		return new GoLexer(input);
	}

	@Override
	protected GoParser createParser(TokenStream input) {
		return new GoParser(input);
	}

	@Override
	protected void parse(GoParser parser) {
		parser.sourceFile();
	}
}
