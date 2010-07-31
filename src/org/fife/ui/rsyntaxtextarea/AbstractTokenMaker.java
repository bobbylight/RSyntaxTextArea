/*
 * 11/07/2004
 *
 * AbstractTokenMaker.java - An abstract implementation of TokenMaker.
 * Copyright (C) 2004 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * An abstract implementation of the
 * {@link org.fife.ui.rsyntaxtextarea.TokenMaker} interface.  It should
 * be overridden for every language for which you want to provide
 * syntax highlighting.<p>
 *
 * @see Token
 *
 * @author Robert Futrell
 * @version 0.2
 */
public abstract class AbstractTokenMaker extends TokenMakerBase {

	/**
	 * Hash table of words to highlight and what token type they are.
	 * The keys are the words to highlight, and their values are the
	 * token types, for example, <code>Token.RESERVED_WORD</code> or
	 * <code>Token.FUNCTION</code>.
	 */
	protected TokenMap wordsToHighlight;


	/**
	 * Constructor.
	 */
	public AbstractTokenMaker() {
		wordsToHighlight = getWordsToHighlight();
	}


	/**
	 * Returns the words to highlight for this programming language.
	 *
	 * @return A <code>TokenMap</code> containing the words to highlight for
	 *         this programming language.
	 */
	public abstract TokenMap getWordsToHighlight();


	/**
	 * Removes the token last added from the linked list of tokens.  The
	 * programmer should never have to call this directly; it can be called
	 * by subclasses of <code>TokenMaker</code> if necessary.
	 */
	public void removeLastToken() {
		if (previousToken==null) {
			firstToken = currentToken = null;
		}
		else {
			currentToken = previousToken;
			currentToken.setNextToken(null);
		}
	}


}