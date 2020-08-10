package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class CAntlrParser extends AntlrParserBase<CLexer, CParser> {
	@Override
	protected CLexer createLexer(CharStream input) {
		return new CLexer(input);
	}

	@Override
	protected CParser createParser(TokenStream input) {
		return new CParser(input);
	}

	@Override
	protected void parse(CParser parser) {
		parser.compilationUnit();
	}
}
