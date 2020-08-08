package org.fife.ui.rsyntaxtextarea.modes.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerBase;

import javax.swing.text.Segment;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public abstract class AntlrTokenMaker extends TokenMakerBase {

	private final List<MultiLineTokenInfo> multiLineTokenInfos;

	protected AntlrTokenMaker(MultiLineTokenInfo... multiLineTokenInfos) {
		super();
		this.multiLineTokenInfos = Arrays.asList(multiLineTokenInfos);
	}

	@Override
	public int getClosestStandardTokenTypeForInternalType(int type) {
		if (type == CommonToken.INVALID_TYPE) {
			// mark as error
			return Token.ERROR_IDENTIFIER;
		} else {
			return convertType(type);
		}
	}

	protected abstract int convertType(int type);

	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
		String line = text.toString();
		resetTokenList();
		MultiLineTokenInfo initialMultiLineTokenInfo = getMultiLineTokenInfo(getLanguageIndex(), initialTokenType);
		String multilineTokenStart = initialMultiLineTokenInfo == null ? null : initialMultiLineTokenInfo.tokenStart;
		if (initialMultiLineTokenInfo != null) {
			// we are inside a multi line token, so prefix the text with the token start
			line = multilineTokenStart + line;
		}

		// check if we have a multi line token start without an end
		String multilineTokenEnd = null;
		for (MultiLineTokenInfo info : multiLineTokenInfos) {
			int tokenStartPos = line.indexOf(info.tokenStart);
			if (tokenStartPos > -1 && line.indexOf(info.tokenEnd, tokenStartPos + info.tokenStart.length()) == -1) {
				//we are in the middle of a multi line token, we need to end it so the lexer can recognize it
				multilineTokenEnd = info.tokenEnd;
				line += multilineTokenEnd;
				break;
			}
		}

		Lexer lexer = createLexer(line);
		lexer.removeErrorListeners();
		lexer.addErrorListener(new AlwaysThrowingErrorListener());

		int currentArrayOffset = text.getBeginIndex();
		int currentDocumentOffset = startOffset;

		try {
			while (true) {
				org.antlr.v4.runtime.Token at = lexer.nextToken();
				if (at.getType() == CommonToken.EOF) {
					if (multilineTokenEnd == null) {
						addNullToken();
					}
					break;
				} else {
					int end = currentArrayOffset + at.getText().length() - 1;
					if (initialMultiLineTokenInfo != null && multilineTokenStart != null
						&& at.getText().startsWith(multilineTokenStart)) {
						// need to subtract our inserted token start
						end -= multilineTokenStart.length();
					}
					if (multilineTokenEnd != null &&
						at.getText().endsWith(multilineTokenEnd)) {
						//need to subtract our inserted token end
						end -= multilineTokenEnd.length();
					}
					addToken(text, currentArrayOffset, end, getClosestStandardTokenTypeForInternalType(at.getType()),
						currentDocumentOffset);
					// update from current token
					currentArrayOffset = currentToken.textOffset + currentToken.textCount;
					currentDocumentOffset = currentToken.getEndOffset();
				}
			}
		} catch (AntlrException exceptionInstanceNotNeeded) {
			// mark the rest of the line as error
			final String remainingText = String.valueOf(text.array, currentArrayOffset,
				text.offset - currentArrayOffset + text.count);
			int type;

			if (initialMultiLineTokenInfo != null) {
				type = initialMultiLineTokenInfo.token;
			} else {
				type = Token.ERROR_IDENTIFIER;
			}

			addToken(text, currentArrayOffset, currentArrayOffset + remainingText.length() - 1, type, currentDocumentOffset);

			if (initialMultiLineTokenInfo == null) {
				//we are not in a multiline token, so we assume the line ends here
				addNullToken();
			}
		}

		if (firstToken == null) {
			// make sure we always have a token
			addNullToken();
		}

		if (firstToken.getType() == Token.NULL && firstToken == currentToken) {
			//empty line, copy type from last line
			firstToken.setType(initialTokenType);
			firstToken.text = new char[0];
			firstToken.textCount = 0;
		}
		return firstToken;
	}

	private MultiLineTokenInfo getMultiLineTokenInfo(int languageIndex, int token) {
		return multiLineTokenInfos.stream().filter(i -> i.languageIndex == languageIndex).filter(i -> i.token == token).findFirst().orElse(null);
	}

	protected abstract Lexer createLexer(String text);

	protected static class MultiLineTokenInfo {
		private final int languageIndex;

		private final int token;

		private final String tokenStart;

		private final String tokenEnd;

		public MultiLineTokenInfo(int languageIndex, int token, String tokenStart, String tokenEnd) {
			this.languageIndex = languageIndex;
			this.token = token;
			this.tokenStart = tokenStart;
			this.tokenEnd = tokenEnd;
		}
	}

	/**
	 * A {@link ANTLRErrorListener} that throws a RuntimeException for every error
	 */
	private static class AlwaysThrowingErrorListener implements ANTLRErrorListener {
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
			throw new AntlrException();
		}

		@Override
		public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
			throw new AntlrException();
		}

		@Override
		public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
			throw new AntlrException();
		}

		@Override
		public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
			throw new AntlrException();
		}
	}

	private static class AntlrException extends RuntimeException {

	}
}
