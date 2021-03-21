package org.fife.ui.rsyntaxtextarea.modes.antlr;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

/**
 * A {@link ANTLRErrorListener} that throws a RuntimeException for every error.
 *
 * @author Markus Heberling
 */
class AlwaysThrowingErrorListener implements ANTLRErrorListener {
	@Override
	public void syntaxError(
		Recognizer<?, ?> recognizer,
		Object offendingSymbol,
		int line,
		int charPositionInLine,
		String msg,
		RecognitionException e) {
		throw new AntlrException();
	}

	@Override
	public void reportAmbiguity(
		Parser recognizer,
		DFA dfa,
		int startIndex,
		int stopIndex,
		boolean exact,
		BitSet ambigAlts,
		ATNConfigSet configs) {
		throw new AntlrException();
	}

	@Override
	public void reportAttemptingFullContext(
		Parser recognizer,
		DFA dfa,
		int startIndex,
		int stopIndex,
		BitSet conflictingAlts,
		ATNConfigSet configs) {
		throw new AntlrException();
	}

	@Override
	public void reportContextSensitivity(
		Parser recognizer,
		DFA dfa,
		int startIndex,
		int stopIndex,
		int prediction,
		ATNConfigSet configs) {
		throw new AntlrException();
	}

	static class AntlrException extends RuntimeException {
	}
}
