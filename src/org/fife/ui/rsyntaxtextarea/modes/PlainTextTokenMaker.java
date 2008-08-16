/*
 * 02/22/2004
 *
 * PlainTextTokenMaker.java - Scanner for plain text files.
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
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * A class that turns text into a linked list of <code>Token</code>s
 * representing the text as plain text.  This is needed by
 * <code>RSyntaxTextArea</code> so that, if word-wrap mode is enabled,
 * it knows where it can line-break.
 *
 * @see Token
 *
 * @author Robert Futrell
 * @version 0.2
 */
public class PlainTextTokenMaker extends AbstractTokenMaker {

	/**
	 * These characters are considered the end of an "identifier", thus,
	 * the lexer can be smart and if it sees "He is an elephant-man!"
	 * it knows it can line break at the "-" if it needs to.
	 */
	private static final String punctuation = "!@#$%^&*)-=+\\;:'>?,./~";


	/**
	 * Constructor.
	 */
	public PlainTextTokenMaker() {
		super();	// Initializes tokensToHighlight.
	}


	/**
	 * Returns the last token on this line's type if the token is "unfinished",
	 * or <code>Token.NULL</code> if it was finished.  For this class, this
	 * method will always return <code>Token.NULL</code>, as no tokens span
	 * multiple lines in plain text.
	 *
	 * @param text The line of tokens to examine.
	 * @param initialTokenType The token type to start with (i.e., the value
	 *        of <code>getLastTokenTypeOnLine</code> for the line before
	 *        <code>text</code>).
	 * @return <code>Token.NULL</code>.
	 */
	public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {
		return Token.NULL;
	}


	/**
	 * Returns the words to highlight for plain text, which is an empty set.
	 *
	 * @return A <code>TokenMap</code> containing the words to highlight for
	 *         plain text (no words at all).
	 * @see org.fife.ui.rsyntaxtextarea.AbstractTokenMaker#getWordsToHighlight
	 */
	public TokenMap getWordsToHighlight() {
		return null;
	}


	/**
	 * Returns a list of tokens representing the given text.
	 *
	 * @param text The text to break into tokens.
	 * @param startTokenType The token with which to start tokenizing.
	 * @param startOffset The offset at which the line of tokens begins.
	 * @return A linked list of tokens representing <code>text</code>.
	 */
	public Token getTokenList(Segment text, int startTokenType,
							int startOffset) {

		resetTokenList();

		char[] array = text.array;
		int offset = text.offset;
		int count = text.count;
		int end = offset + count;

		// See, when we find a token, its starting position is always of the
		// form: 'startOffset + (currentTokenStart-offset)'; but since
		// startOffset and offset are constant, tokens' starting positions
		// become: 'newStartOffset+currentTokenStart' for one less subraction
		// operation.
		int newStartOffset = startOffset - offset;

		int currentTokenStart = offset;
		int currentTokenType  = startTokenType;

		for (int i=offset; i<end; i++) {

			char c = array[i];

			switch (currentTokenType) {

				case Token.NULL:
					currentTokenStart = i;
					switch (c) {
						case ' ':
						case '\t':
							currentTokenType = Token.WHITESPACE;
							break;
						default:
							currentTokenType = Token.IDENTIFIER;
							break;
					}
					break;

				case Token.WHITESPACE:
					switch (c) {
						case ' ':
						case '\t':
							break;
						default:
							addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.IDENTIFIER;
					}
					break;

				case Token.IDENTIFIER:
					switch (c) {
						case ' ':
						case '\t':
							addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.WHITESPACE;
							break;
						default:
							// Check to see if an "end-of-identifier"
							// punctuation mark was found...
							int indexOf = punctuation.indexOf(c,0);
							if (indexOf>-1) {
								addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
								currentTokenType = Token.NULL;
							}
							break;
					}
					break;

				default:
					System.err.println("Invalid currentTokenType: " +
								currentTokenType + "; c=='" + c + "'");
					System.exit(0);

			} // End of switch (currentTokenType).

		} // End of for (int i=offset; i<end; i++).

		// Deal with the last token and add a null token to the line's end.
		if (currentTokenType!=Token.NULL)
			addToken(text, currentTokenStart,end-1, currentTokenType,
								newStartOffset+currentTokenStart);
		addNullToken();

		// Return the first token in our linked list.
		return firstToken;

	}


}