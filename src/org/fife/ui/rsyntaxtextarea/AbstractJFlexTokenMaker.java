/*
 * 03/23/2005
 *
 * AbstractJFlexTokenMaker.java - Base class for token makers generated from
 * programs such as JFlex.
 * Copyright (C) 2005 Robert Futrell
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

import javax.swing.text.Segment;


/**
 * Base class for JFlex-generated token makers.  This class attempts to factor
 * out all common code from these classes.  Many methods <em>almost</em> could
 * be factored out into this class, but cannot because they reference JFlex
 * variables that we cannot access from this class.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public abstract class AbstractJFlexTokenMaker implements TokenMaker {

	/**
	 * The first token in the returned linked list.
	 */
	protected Token firstToken;
	
	/**
	 * Used in the creation of the linked list.
	 */
	protected Token currentToken;
	
	/**
	 * Used in the creation of the linked list.
	 */
	protected Token previousToken;

	/**
	 * The factory that gives us our tokens to use.
	 */
	protected TokenFactory tokenFactory;

	protected Segment s;

	protected int start;		// Just for states.
	protected int offsetShift;	// As parser always starts at 0, but our line doesn't.


	/**
	 * Constructor.
	 */
	public AbstractJFlexTokenMaker() {
		firstToken = currentToken = previousToken = null;
		tokenFactory = new DefaultTokenFactory();
	}


	/**
	 * Adds a null token to the end of the current linked list of tokens.
	 * This should be put at the end of the linked list whenever the last
	 * token on the current line is NOT a multiline token.
	 */
	public void addNullToken() {
		if (firstToken==null) {
			firstToken = tokenFactory.createToken();
			currentToken = firstToken;
		}
		else {
			currentToken.setNextToken(tokenFactory.createToken());
			previousToken = currentToken;
			currentToken = currentToken.getNextToken();
		}
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *        occurs.
	 */
	public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {

		if (firstToken==null) {
			firstToken = tokenFactory.createToken(array, start, end,
									startOffset, tokenType);
			currentToken = firstToken; // previous token is still null.
		}
		else {
			currentToken.setNextToken(tokenFactory.createToken(array,
								start,end, startOffset, tokenType));
			previousToken = currentToken;
			currentToken = currentToken.getNextToken();
		}

	}


	/**
	 * Returns the last token on this line's type if the token is "unfinished",
	 * or <code>Token.NULL</code> if it was finished.  For example, if C-style
	 * syntax highlighting is being implemented, and <code>text</code>
	 * contained a line of code that contained the beginning of a comment but
	 * no end-comment marker ("*\/"), then this method would return
	 * <code>Token.COMMENT_MULTILINE</code> for that line.  This is useful
	 * for doing syntax highlighting.
	 *
	 * @param text The line of tokens to examine.
	 * @param initialTokenType The token type to start with (i.e., the value
	 *        of <code>getLastTokenTypeOnLine</code> for the line before
	 *        <code>text</code>).
	 * @return The last token on this line's type, or <code>Token.NULL</code>
	 *         if the line was completed.
	 */
	public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {

		// Last param doesn't matter if we're not painting.
		Token t = getTokenList(text, initialTokenType, 0);

		while (t.getNextToken()!=null)
			t = t.getNextToken();

		return t.type;

	}


	/**
	 * Returns the text to place at the beginning and end of a
	 * line to "comment" it in a this programming language.
	 *
	 * @return The start and end strings to add to a line to "comment"
	 *         it out.  A <code>null</code> value for either means there
	 *         is no string to add for that part.  A value of
	 *         <code>null</code> for the array means this language
	 *         does not support commenting/uncommenting lines.
	 */
	public String[] getLineCommentStartAndEnd() {
		return null;
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	public abstract Token getTokenList(Segment text, int initialTokenType,
								int startOffset);


	/**
	 * Deletes the linked list of tokens so we can begin anew.  This should
	 * never have to be called by the programmer, as it is automatically
	 * called whenever the user calls <code>getLastTokenTypeOnLine</code> or
	 * <code>getTokenList</code>.
	 */
	protected void resetTokenList() {
		firstToken = currentToken = previousToken = null;
		tokenFactory.resetAllTokens();
	}


	/**
	 * Sets whether tokens are generated that "show" whitespace.
	 *
	 * @param visible Whether whitespace should be visible.
	 */
	public void setWhitespaceVisible(boolean visible, RSyntaxTextArea textArea) {
		// FIXME:  Initialize with the proper sizes.
		if (visible)
			tokenFactory = new VisibleWhitespaceTokenFactory();
		else
			tokenFactory = new DefaultTokenFactory();
			//tokenFactory = new GlyphVectorTokenFactory(textArea);
	}


}