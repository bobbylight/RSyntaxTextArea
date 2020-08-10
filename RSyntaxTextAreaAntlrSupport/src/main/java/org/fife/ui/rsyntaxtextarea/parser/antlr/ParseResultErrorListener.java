package org.fife.ui.rsyntaxtextarea.parser.antlr;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;

public class ParseResultErrorListener extends BaseErrorListener {
	private final ParseResult parseResult;

	public ParseResultErrorListener(ParseResult parseResult) {
		this.parseResult = parseResult;
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
							String msg, RecognitionException e) {
		int startIndex = -1;
		int length = -1;

		if (offendingSymbol instanceof Token) {
			startIndex = ((Token) offendingSymbol).getStartIndex();
			length = ((Token) offendingSymbol).getStopIndex() - startIndex + 1;
		} else if (e != null) {
			if (e.getOffendingToken() != null) {
				startIndex = e.getOffendingToken().getStartIndex();
				length = e.getOffendingToken().getStopIndex() - startIndex + 1;
			} else if (e instanceof LexerNoViableAltException) {
				startIndex = ((LexerNoViableAltException) e).getStartIndex();
				length = 1;
			}
		}

		// if length is 0, we can't highlight a character, so the whole line needs to be highlighted
		if (length == 0) {
			startIndex = -1;
			length = -1;
		}

		parseResult.getNotices()
			.add(new DefaultParserNotice(parseResult.getParser(), msg, line - 1, startIndex, length));
	}
}
