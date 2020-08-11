package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class AssemblerAntlrParser extends AntlrParserBase<AssemblerLexer, AssemblerParser> {
	@Override
	protected AssemblerLexer createLexer(CharStream input) {
		return new AssemblerLexer(input);
	}

	@Override
	protected AssemblerParser createParser(TokenStream input) {
		return new AssemblerParser(input);
	}

	@Override
	protected void parse(AssemblerParser parser) {
		parser.prog();
	}
}
