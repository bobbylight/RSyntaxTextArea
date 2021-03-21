package org.fife.ui.rsyntaxtextarea.modes.antlr;

/**
 * Holds information about multi-line tokens, so that the line based {@link AntlrTokenMaker}
 * can resume an unfinished token from the previous line.
 *
 * @author Markus Heberling
 */
public class MultiLineTokenInfo {
	final int languageIndex;

	final int token;

	final String tokenStart;

	final String tokenEnd;

	public MultiLineTokenInfo(int languageIndex, int token, String tokenStart, String tokenEnd) {
		this.languageIndex = languageIndex;
		this.token = token;
		this.tokenStart = tokenStart;
		this.tokenEnd = tokenEnd;
	}
}
