package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class ErlangAntlrParser extends AntlrParserBase<ErlangLexer, ErlangParser> {
	@Override
	protected ErlangLexer createLexer(CharStream input) {
		return new ErlangLexer(input);
	}

	@Override
	protected ErlangParser createParser(TokenStream input) {
		return new ErlangParser(input);
	}

	@Override
	protected void parse(ErlangParser parser) {
		parser.forms();
	}
}
