/*
 * 06/05/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;


/**
 * Adds some niceties to {@code AbstractTokenMakerTest}.  These two classes
 * should be merged when all unit tests in this package follow this
 * convention.
 *
 * @author Robert Futrell
 * @version 1.0
 */
abstract class AbstractTokenMakerTest2 extends AbstractTokenMakerTest {


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	protected abstract TokenMaker createTokenMaker();


	/**
	 * Verifies that all tokens in an array have a specific token type.
	 *
	 * @param tokens The tokens.
	 * @param expectedType The expected token type.
	 */
	protected void assertAllTokensOfType(String[] tokens, int expectedType) {

		for (String token : tokens) {
			Segment segment = createSegment(token);
			TokenMaker tm = createTokenMaker();
			Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(expectedType, t.getType(),
				"Token has unexpected type: orig=" + token + ", actual=" + t);
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
					"Next token does not denote end-of-line: " + t.getNextToken());
			}
		}

	}


}
