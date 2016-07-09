/*
 * 02/24/2004
 *
 * TokenMaker.java - An object that can take a chunk of text and return a
 * linked list of <code>Token</code>s representing it.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.Action;
import javax.swing.text.Segment;


/**
 * An implementation of <code>TokenMaker</code> is a class that turns text into
 * a linked list of <code>Token</code>s for syntax highlighting
 * in a particular language.
 *
 * @see Token
 * @see AbstractTokenMaker
 *
 * @author Robert Futrell
 * @version 0.2
 */
public interface TokenMaker {


	/**
	 * Adds a null token to the end of the current linked list of tokens.
	 * This should be put at the end of the linked list whenever the last
	 * token on the current line is NOT a multi-line token.
	 */
	public void addNullToken();


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array from which to get the text.
	 * @param start Start offset in <code>segment</code> of token.
	 * @param end End offset in <code>segment</code> of token.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *        occurs.
	 */
	public void addToken(char[] array, int start, int end, int tokenType,
							int startOffset);


	/**
	 * Returns the closest {@link TokenTypes "standard" token type} for a given
	 * "internal" token type (e.g. one whose value is <code>&lt; 0</code>).
	 *
	 * @param type The token type.
	 * @return The closest "standard" token type.  If a mapping is not defined
	 *         for this language, then <code>type</code> is returned.
	 */
	public int getClosestStandardTokenTypeForInternalType(int type);


	/**
	 * Returns whether this programming language uses curly braces
	 * ('<code>{</code>' and '<code>}</code>') to denote code blocks.
	 *
	 * @param languageIndex The language index at the offset in question.
	 *        Since some <code>TokenMaker</code>s effectively have nested
	 *        languages (such as JavaScript in HTML), this parameter tells the
	 *        <code>TokenMaker</code> what sub-language to look at.
	 * @return Whether curly braces denote code blocks.
	 */
	public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex);


	/**
	 * Returns the last token on this line's type if the token is "unfinished",
	 * or {@link Token#NULL} if it was finished.  For example, if C-style
	 * syntax highlighting is being implemented, and <code>text</code>
	 * contained a line of code that contained the beginning of a comment but
	 * no end-comment marker ("*\/"), then this method would return
	 * {@link Token#COMMENT_MULTILINE} for that line.  This is useful
	 * for doing syntax highlighting.
	 *
	 * @param text The line of tokens to examine.
	 * @param initialTokenType The token type to start with (i.e., the value
	 *        of <code>getLastTokenTypeOnLine</code> for the line before
	 *        <code>text</code>).
	 * @return The last token on this line's type, or {@link Token#NULL}
	 *         if the line was completed.
	 */
	public int getLastTokenTypeOnLine(Segment text, int initialTokenType);


	/**
	 * Returns the text to place at the beginning and end of a
	 * line to "comment" it in this programming language.
	 *
	 * @param languageIndex The language index at the offset in question.
	 *        Since some <code>TokenMaker</code>s effectively have nested
	 *        languages (such as JavaScript in HTML), this parameter tells the
	 *        <code>TokenMaker</code> what sub-language to look at.
	 * @return The start and end strings to add to a line to "comment"
	 *         it out.  A <code>null</code> value for either means there
	 *         is no string to add for that part.  A value of
	 *         <code>null</code> for the array means this language
	 *         does not support commenting/uncommenting lines.
	 */
	public String[] getLineCommentStartAndEnd(int languageIndex);


	/**
	 * Returns an action to handle "insert break" key presses (i.e. Enter).
	 *
	 * @return The action, or <code>null</code> if the default action should
	 *         be used.
	 */
	public Action getInsertBreakAction();


	/**
	 * Returns whether tokens of the specified type should have "mark
	 * occurrences" enabled for the current programming language.
	 *
	 * @param type The token type.
	 * @return Whether tokens of this type should have "mark occurrences"
	 *         enabled.
	 */
	public boolean getMarkOccurrencesOfTokenType(int type);


	/**
	 * Returns the object in charge of marking all occurrences of the token
	 * at the current caret position, if it is a relevant token.  If
	 * <code>null</code> is returned, a default <code>OccurrenceMarker</code>
	 * is used.
	 *
	 * @return The occurrence marker for this language, or <code>null</code>
	 *         for none.
	 */
	public OccurrenceMarker getOccurrenceMarker();


	/**
	 * If a line ends in the specified token, this method returns whether
	 * a new line inserted after that line should be indented.
	 *
	 * @param token The token the previous line ends with.
	 * @return Whether the next line should be indented.
	 */
	public boolean getShouldIndentNextLineAfter(Token token);


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
	public Token getTokenList(Segment text, int initialTokenType,
											int startOffset);


	/**
	 * Returns whether a character could be part of an "identifier" token
	 * in a specific language.  This is used to identify such things as the
	 * bounds of the "word" to select on double-clicking.
	 *
	 * @param languageIndex The language index the character was found in.
	 * @param ch The character.
	 * @return Whether the character could be part of an "identifier" token.
	 */
	public boolean isIdentifierChar(int languageIndex, char ch);


	/**
	 * Returns whether this language is a markup language.
	 *
	 * @return Whether this language is markup.
	 */
	public boolean isMarkupLanguage();


}