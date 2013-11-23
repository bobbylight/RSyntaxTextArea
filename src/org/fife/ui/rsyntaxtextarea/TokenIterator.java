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
	public TokenIterator(RSyntaxDocument doc) {
		this.doc = doc;
		token = doc.getTokenListForLine(0);
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
	public boolean hasNext() {
		return token!=null;
	}


	/**
	 * Returns the next paintable token in the document.
	 *
	 * @return The next paintable token in the document.
	 * @see #hasNext()
	 */
	public Token next() {
		Token t = token;
		if (token!=null) {
			if (token.isPaintable()) {
				token = token.getNextToken();
				int lineCount = getLineCount();
				while (token!=null && !token.isPaintable() &&
						++curLine<lineCount) {
					t = new TokenImpl(t);
					token = doc.getTokenListForLine(curLine);
				}
			}
			else {
				token = null;
			}
		}
		return t;
	}


	/**
	 * Always throws {@link UnsupportedOperationException}, as
	 * <code>Token</code> removal is not supported.
	 *
	 * @throws UnsupportedOperationException always.
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}


}