package org.fife.ui.rsyntaxtextarea.demo.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.parser.antlr.AntlrParserBase;

public class MySqlAntlrParser extends AntlrParserBase<MySqlLexer, MySqlParser> {
	@Override
	protected MySqlLexer createLexer(CharStream input) {
		return new MySqlLexer(input);
	}

	@Override
	protected MySqlParser createParser(TokenStream input) {
		return new MySqlParser(input);
	}

	@Override
	protected void parse(MySqlParser parser) {
		parser.root();
	}
}
