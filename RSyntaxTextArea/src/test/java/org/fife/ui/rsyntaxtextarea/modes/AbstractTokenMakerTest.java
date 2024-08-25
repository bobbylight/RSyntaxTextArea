/*
 * 06/05/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Utility methods for unit tests for <code>TokenMaker</code> implementations.
 *
 * @author Robert Futrell
 * @version 1.0
 */
abstract class AbstractTokenMakerTest {


	/**
	 * Verifies that the second token in an array of token lists is always a regex.
	 *
	 * @param codes The array of token lists.
	 */
	protected void assertAllSecondTokensAreRegexes(String... codes) {
		assertAllSecondTokensAreRegexes(TokenTypes.NULL, codes);
	}


	/**
	 * Verifies that the second token in an array of token lists is always a regex.
	 *
	 * @param initialTokenType The initial token tpe of the line.
	 * @param codes The array of token lists.
	 */
	protected void assertAllSecondTokensAreRegexes(int initialTokenType, String... codes) {
		for (String code : codes) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, initialTokenType, 0);

			// Skip the first token
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.REGEX, token.getType(), "not a regex: " +
				token + " (code snippet: \"" + code + "\"");
		}
	}


	/**
	 * Verifies that the second token in an array of token lists is always NOT a regex.
	 *
	 * @param codes The array of token lists.
	 */
	protected void assertAllSecondTokensAreNotRegexes(String... codes) {
		assertAllSecondTokensAreNotRegexes(TokenTypes.NULL, codes);
	}


	/**
	 * Verifies that the second token in an array of token lists is always NOT a regex.
	 *
	 * @param initialTokenType The initial token tpe of the line.
	 * @param codes The array of token lists.
	 */
	protected void assertAllSecondTokensAreNotRegexes(int initialTokenType, String... codes) {
		for (String code : codes) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, initialTokenType, 0);

			// Skip the first token
			token = token.getNextToken();
			Assertions.assertNotEquals(TokenTypes.REGEX, token.getType(), "is a regex: " +
				token + " (code snippet: \"" + code + "\"");
		}
	}


	/**
	 * Verifies that all tokens in an array have a specific token type.
	 *
	 * @param expectedType The expected token type.
	 * @param tokens The tokens.
	 */
	protected void assertAllTokensOfType(int expectedType, String... tokens) {
		assertAllTokensOfType(expectedType, TokenTypes.NULL, tokens);
	}


	/**
	 * Verifies that all tokens in an array have a specific token type.
	 *
	 * @param expectedType The expected token type.
	 * @param initialTokenType The initial token type.
	 * @param tokens The tokens.
	 */
	protected void assertAllTokensOfType(int expectedType, int initialTokenType,
										 String... tokens) {
		assertAllTokensOfType(expectedType, initialTokenType, true, tokens);
	}


	/**
	 * Verifies that all tokens in an array have a specific token type.
	 *
	 * @param expectedType The expected token type.
	 * @param initialTokenType The initial token type.
	 * @param verifyNoMoreImportantTokens If {@code true}, each value in {@code tokens}
	 *        must be a single token, with the next token in the list somehow
	 *        denoting the end of the line.  If this is {@code false}, we verify the
	 *        type of the first token returned from each value in {@code tokens}
	 *        but ignore the rest.  This is useful e.g. when a token's type depends
	 *        on context provided by subsequent functions, such as "func" in
	 *        "{@code func(}" being identified as a function.
	 * @param tokens The tokens.
	 */
	protected void assertAllTokensOfType(int expectedType, int initialTokenType,
										 boolean verifyNoMoreImportantTokens,
										 String... tokens) {

		for (String token : tokens) {

			Segment segment = createSegment(token);
			TokenMaker tm = createTokenMaker();
			Token t = tm.getTokenList(segment, initialTokenType, 0);

			Assertions.assertEquals(expectedType, t.getType(),
				"Token has unexpected type: orig=" + token + ", actual=" + t);
			// The assertion below doesn't work as some tests have trailing '\n' chars
			//Assertions.assertEquals(token, t.getLexeme(),
			//	"Token has unexpected lexeme: expected='" + token + "', actual=" + t);

			// The token array passed to this method is supposed to be single tokens.
			// The next token should denote it's the end of a line, and what the next
			// line's "start" token type should be.  There are 3 ways this information
			// can be encoded, unfortunately:
			if (verifyNoMoreImportantTokens) {
				// 1. No next token at all (e.g. last type is string literal, to continue to next line)
				Assertions.assertTrue(t.getNextToken() == null ||
						// 2. The next token is some special "end-of-line" internal/non-paintable token type
						!t.getNextToken().isPaintable() ||
						// 3. The next token is a token type to continue, e.g. string or MLC
						t.getNextToken().length() == 0,
					"Next token does not denote end-of-line! lexeme: " + t.getLexeme() + ", next: " + t.getNextToken());
			}
		}

	}


	/**
	 * Creates a <code>Segment</code> from a <code>String</code>.
	 *
	 * @param code The string representing some code.
	 * @return The code, as a <code>Segment</code>.
	 */
	protected Segment createSegment(String code) {
		return new Segment(code.toCharArray(), 0, code.length());
	}


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	protected abstract TokenMaker createTokenMaker();


	/**
	 * Verifies whether curly braces denote code blocks for the default language of
	 * this token maker. The default implementation checks for {@code false};
	 * subclasses should override appropriately.
	 */
	@Test
	protected void testCommon_getCurlyBracesDenoteCodeBlocks() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertFalse(tm.getCurlyBracesDenoteCodeBlocks(0));
	}


	/**
	 * Verifies whether the line comment delimiters returned by this token maker are
	 * correct for the primary language.
	 */
	public abstract void testCommon_GetLineCommentStartAndEnd();


	/**
	 * Verifies whether the {@code getMarkOccurrencesOfTokenType()} method
	 * functions properly.
	 */
	@Test
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	/**
	 * Verifies whether the line comment delimiters returned by this token maker are
	 * correct for the primary language.
	 */
	@Test
	void testCommon_GetOccurrenceMarker() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertNotNull(tm.getOccurrenceMarker());
	}


	/**
	 * Verifies the {@code getShouldIndentNextLineAfter()} method.
	 */
	@Test
	protected void testCommon_getShouldIndentNextLineAfter() {

		// NOTE: This is a pretty sorry test, but it's hard to test "no token implies indent the next
		// line," which is the default behavior.
		TokenMaker tm = createTokenMaker();
		for (int tokenType = 0; tokenType < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; tokenType++) {
			Token token = new TokenImpl("{".toCharArray(), 0, 0, 0, tokenType, 0);
			Assertions.assertFalse(tm.getShouldIndentNextLineAfter(token));
		}
	}


	/**
	 * Helper method for {@link #testCommon_getShouldIndentNextLineAfter()} to verify that indentation of
	 * the next line is done after curly braces and open parens for a specific language index.
	 *
	 * @param languageIndex The language index to check.
	 */
	protected void testCommonHelper_getShouldIndentNextLineAfterCurliesAndParensForLanguageIndex(int languageIndex) {
		TokenMaker tm = createTokenMaker();
		Token[] indentAfter = {
			new TokenImpl("{".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, languageIndex),
			new TokenImpl("(".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, languageIndex),
		};
		for (Token token : indentAfter) {
			Assertions.assertTrue(tm.getShouldIndentNextLineAfter(token));
		}
	}
}
