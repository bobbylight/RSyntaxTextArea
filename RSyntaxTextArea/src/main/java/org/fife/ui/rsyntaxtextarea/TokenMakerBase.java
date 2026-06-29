/*
 * 08/26/2004
 *
 * TokenMakerBase.java - A base class for token makers.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.text.Segment;


/**
 * Base class for token makers.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class TokenMakerBase implements TokenMaker {

	/**
	 * The first token in the returned linked list.
	 */
	protected TokenImpl firstToken;

	/**
	 * Used in the creation of the linked list.
	 */
	protected TokenImpl currentToken;

	/**
	 * Used in the creation of the linked list.
	 */
	protected TokenImpl previousToken;

	/**
	 * The factory that gives us our tokens to use.
	 */
	private TokenFactory tokenFactory;

	/**
	 * Highlights occurrences of the current token in the editor, if it is
	 * relevant.
	 */
	private OccurrenceMarker occurrenceMarker;

	/**
	 * "0" implies this is the "main" language being highlighted.  Positive
	 * values imply various "secondary" or "embedded" languages, such as CSS
	 * or JavaScript in HTML.  While this value is non-zero, tokens will be
	 * generated with this language index so they can (possibly) be painted
	 * differently, so "embedded" languages can be rendered with a special
	 * background.
	 */
	private int languageIndex;


	/**
	 * Constructor.
	 */
	public TokenMakerBase() {
		firstToken = currentToken = previousToken = null;
		tokenFactory = new DefaultTokenFactory();
	}


	@Override
	public void addNullToken() {
		if (firstToken==null) {
			firstToken = tokenFactory.createToken();
			currentToken = firstToken;
		}
		else {
			TokenImpl next = tokenFactory.createToken();
			currentToken.setNextToken(next);
			previousToken = currentToken;
			currentToken = next;
		}
		currentToken.setLanguageIndex(languageIndex);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param segment <code>Segment</code> to get text from.
	 * @param start Start offset in <code>segment</code> of token.
	 * @param end End offset in <code>segment</code> of token.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *        occurs.
	 */
	public void addToken(Segment segment, int start, int end, int tokenType,
							int startOffset) {
		addToken(segment.array, start,end, tokenType, startOffset);
	}


	@Override
	public void addToken(char[] array, int start, int end, int tokenType,
						int startOffset) {
		addToken(array, start, end, tokenType, startOffset, false);
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
	 * @param hyperlink Whether this token is a hyperlink.
	 */
	public void addToken(char[] array, int start, int end, int tokenType,
						int startOffset, boolean hyperlink) {

		if (firstToken==null) {
			firstToken = tokenFactory.createToken(array, start, end,
									startOffset, tokenType);
			currentToken = firstToken; // previous token is still null.
		}
		else {
			TokenImpl next = tokenFactory.createToken(array, start,end,
													startOffset, tokenType);
			currentToken.setNextToken(next);
			previousToken = currentToken;
			currentToken = next;
		}

		currentToken.setLanguageIndex(languageIndex);
		currentToken.setHyperlink(hyperlink);

	}


	/**
	 * Returns the occurrence marker to use for this token maker.  Subclasses
	 * can override to use different implementations.
	 *
	 * @return The occurrence marker to use.
	 */
	protected OccurrenceMarker createOccurrenceMarker() {
		return new DefaultOccurrenceMarker();
	}


	/**
	 * Returns the current language index.
	 *
	 * @return The current language index.
	 * @see #setLanguageIndex(int)
	 */
	protected int getLanguageIndex() {
		return languageIndex;
	}




	/**
	 * Returns whether no tokens have been identified yet.  Should only be
	 * called by subclasses that need to identify tokens depending on whether
	 * they are the "first" token on the line or not.
	 *
	 * @return Whether no tokens have been identified yet.
	 */
	protected boolean getNoTokensIdentifiedYet() {
		return firstToken == null;
	}


	@Override
	public OccurrenceMarker getOccurrenceMarker() {
		if (occurrenceMarker==null) {
			occurrenceMarker = createOccurrenceMarker();
		}
		return occurrenceMarker;
	}




	/**
	 * Deletes the linked list of tokens so we can begin anew.  This should
	 * never have to be called by the programmer, as it is automatically
	 * called whenever the user calls
	 * {@link #getLastTokenTypeOnLine(Segment, int)} or
	 * {@link #getTokenList(Segment, int, int)}.
	 */
	protected void resetTokenList() {
		firstToken = currentToken = previousToken = null;
		tokenFactory.resetAllTokens();
	}


	/**
	 * Sets the language index to assign to tokens moving forward.  This
	 * property is used to designate tokens as being in "secondary" languages
	 * (such as CSS or JavaScript in HTML).
	 *
	 * @param languageIndex The new language index.  A value of
	 *        <code>0</code> denotes the "main" language, any positive value
	 *        denotes a specific secondary language.  Negative values will
	 *        be treated as <code>0</code>.
	 * @see #getLanguageIndex()
	 */
	protected void setLanguageIndex(int languageIndex) {
		this.languageIndex = Math.max(0, languageIndex);
	}


}
