/*
 * 08/28/2013
 *
 * TokenIterator.java - An iterator over the Tokens in an RSyntaxDocument.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.util.Iterator;


/**
 * Allows you to iterate through all paintable tokens in an
 * <code>RSyntaxDocument</code>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class TokenIterator implements Iterator<Token> {

	private RSyntaxDocument doc;
	private int curLine;
	private Token token;


	/**
	 * Constructor.
	 *
	 * @param doc The document whose tokens we should iterate over.
	 */
	TokenIterator(RSyntaxDocument doc) {
		this.doc = doc;
		loadTokenListForCurLine();
		int lineCount = getLineCount();
		while ((token==null || !token.isPaintable()) && curLine<lineCount-1) {
			curLine++;
			loadTokenListForCurLine();
		}
	}


	private int getLineCount() {
		return doc.getDefaultRootElement().getElementCount();
	}


	/**
	 * Returns whether any more paintable tokens are in the document.
	 *
	 * @return Whether there are any more paintable tokens.
	 * @see #next()
	 */
	@Override
	public boolean hasNext() {
		return token!=null;
	}


	private void loadTokenListForCurLine() {
		token = doc.getTokenListForLine(curLine);
		if (token!=null && !token.isPaintable()) {
			// Common end of document scenario
			token = null;
		}
	}


	/**
	 * Returns the next paintable token in the document.
	 *
	 * @return The next paintable token in the document.
	 * @see #hasNext()
	 */
	@Override
	public Token next() {

		Token t = token;
		boolean tIsCloned = false;
		int lineCount = getLineCount();

		// Get the next token, going to the next line if necessary.
		if (token!=null && token.isPaintable()) {
			token = token.getNextToken();
		}
		else if (curLine<lineCount-1) {
			t = new TokenImpl(t); // Clone t since tokens are pooled
			tIsCloned = true;
			curLine++;
			loadTokenListForCurLine();
		}
		else if (token!=null && !token.isPaintable()) {
			// Ends with a non-paintable token (not sure this ever happens)
			token = null;
		}

		while ((token==null || !token.isPaintable()) && curLine<lineCount-1) {
			if (!tIsCloned) {
				t = new TokenImpl(t); // Clone t since tokens are pooled
				tIsCloned = true;
			}
			curLine++;
			loadTokenListForCurLine();
		}
		if (token!=null && !token.isPaintable() && curLine==lineCount-1) {
			token = null;
		}

		return t;

	}


	/**
	 * Always throws {@link UnsupportedOperationException}, as
	 * <code>Token</code> removal is not supported.
	 *
	 * @throws UnsupportedOperationException always.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}


}