/*
 * 08/26/2004
 *
 * TokenMakerBase.java - A base class for token makers.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.Action;
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


	/**
	 * {@inheritDoc}
	 */
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


	/**
	 * {@inheritDoc}
	 */
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
	 * Returns the closest {@link TokenTypes "standard" token type} for a given
	 * "internal" token type (e.g. one whose value is <code>&lt; 0</code>).<p>
	 *
	 * The default implementation returns <code>type</code> always, which
	 * denotes that a mapping from internal token types to standard token types
	 * is not defined; subclasses can override.
	 *
	 * @param type The token type.
	 * @return The closest "standard" token type.
	 */
	@Override
	public int getClosestStandardTokenTypeForInternalType(int type) {
		return type;
	}


	/**
	 * Returns whether this programming language uses curly braces
	 * ('<code>{</code>' and '<code>}</code>') to denote code blocks.
	 *
	 * The default implementation returns <code>false</code>; subclasses can
	 * override this method if necessary.
	 *
	 * @param languageIndex The language index at the offset in question.
	 *        Since some <code>TokenMaker</code>s effectively have nested
	 *        languages (such as JavaScript in HTML), this parameter tells the
	 *        <code>TokenMaker</code> what sub-language to look at.
	 * @return Whether curly braces denote code blocks.
	 */
	@Override
	public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
		return false;
	}


	/**
	 * Returns an action to handle "insert break" key presses (i.e. Enter).
	 * The default implementation returns <code>null</code>.  Subclasses
	 * can override.
	 *
	 * @return The default implementation always returns <code>null</code>.
	 */
	@Override
	public Action getInsertBreakAction() {
		return null;
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
	 * {@inheritDoc}
	 */
	@Override
	public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {

		// Last parameter doesn't matter if we're not painting.
		Token t = getTokenList(text, initialTokenType, 0);

		while (t.getNextToken()!=null) {
			t = t.getNextToken();
		}

		return t.getType();

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return null;
	}


	/**
	 * Returns whether tokens of the specified type should have "mark
	 * occurrences" enabled for the current programming language.  The default
	 * implementation returns true if <tt>type</tt> is {@link Token#IDENTIFIER}.
	 * Subclasses can override this method to support other token types, such
	 * as {@link Token#VARIABLE}.
	 *
	 * @param type The token type.
	 * @return Whether tokens of this type should have "mark occurrences"
	 *         enabled.
	 */
	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return type==Token.IDENTIFIER;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public OccurrenceMarker getOccurrenceMarker() {
		if (occurrenceMarker==null) {
			occurrenceMarker = createOccurrenceMarker();
		}
		return occurrenceMarker;
	}


	/**
	 * The default implementation returns <code>false</code> always.  Languages
	 * that wish to better support auto-indentation can override this method.
	 *
	 * @param token The token the previous line ends with.
	 * @return Whether the next line should be indented.
	 */
	@Override
	public boolean getShouldIndentNextLineAfter(Token token) {
		return false;
	}


	/**
	 * Returns whether a character could be part of an "identifier" token
	 * in a specific language.  The default implementation returns
	 * <code>true</code> for letters, numbers, and certain symbols.
	 */
	@Override
	public boolean isIdentifierChar(int languageIndex, char ch) {
		return Character.isLetterOrDigit(ch) || ch=='_' || ch=='$';
	}


	/**
	 * The default implementation returns <code>false</code> always.
	 * Subclasses that are highlighting a markup language should override this
	 * method to return <code>true</code>.
	 *
	 * @return <code>false</code> always.
	 */
	@Override
	public boolean isMarkupLanguage() {
		return false;
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