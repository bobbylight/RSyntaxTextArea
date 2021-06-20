package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class JSONAntlrParser extends AntlrParserBase<JSONLexer, JSONParser> {
	@Override
	protected JSONLexer createLexer(CharStream input) {
		return new JSONLexer(input);
	}

	@Override
	protected JSONParser createParser(TokenStream input) {
		return new JSONParser(input);
	}

	@Override
	protected void parse(JSONParser parser) {
		parser.json();
	}
}
