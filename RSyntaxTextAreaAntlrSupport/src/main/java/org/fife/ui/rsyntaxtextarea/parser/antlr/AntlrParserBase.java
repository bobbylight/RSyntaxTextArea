package org.fife.ui.rsyntaxtextarea.parser.antlr;

import javax.swing.text.BadLocationException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;

public abstract class AntlrParserBase<L extends Lexer, P extends Parser> extends AbstractParser {

	protected abstract L createLexer(CharStream input);

	protected abstract P createParser(TokenStream input);

	protected abstract void parse(P parser);

	@Override
	public ParseResult parse(RSyntaxDocument doc, String style) {
		DefaultParseResult parseResult = new DefaultParseResult(this);
		parseResult.setParsedLines(0, doc.getDefaultRootElement().getElementCount());
		long start = System.currentTimeMillis();
		try {
			L lexer = createLexer(CharStreams.fromString(doc.getText(0, doc.getLength())));
			lexer.removeErrorListeners();
			lexer.addErrorListener(new ParseResultErrorListener(parseResult));
			P parser = createParser(new CommonTokenStream(lexer));
			parser.removeErrorListeners();
			parser.addErrorListener(new ParseResultErrorListener(parseResult));
			parse(parser);
		} catch (BadLocationException e) {
			parseResult.setError(e);
		}
		parseResult.setParseTime(System.currentTimeMillis() - start);
		return parseResult;
	}
}
