/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;

/**
 * Unit tests for the {@code AbstractTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class AbstractTokenMakerTest {


	@Test
	void testRemoveLastToken_noTokensFoundSoFar() {
		ConcreteTestTokenMaker tm = new ConcreteTestTokenMaker();

		tm.removeLastToken();
		Assertions.assertNull(tm.firstToken);
		Assertions.assertNull(tm.previousToken);
		Assertions.assertNull(tm.currentToken);
	}


	@Test
	void testRemoveLastToken_multipleTokensFoundSoFar() {
		ConcreteTestTokenMaker tm = new ConcreteTestTokenMaker();
		tm.initializeForTesting();

		tm.removeLastToken();
		Assertions.assertTrue(tm.currentToken.is(TokenTypes.WHITESPACE, " "));
	}


	/**
	 * A concrete implementation for test purposes.
	 */
	private static final class ConcreteTestTokenMaker extends AbstractTokenMaker {

		@Override
		public TokenMap getWordsToHighlight() {

			TokenMap tm = new TokenMap();
			tm.put("one", TokenTypes.RESERVED_WORD);
			tm.put("two", TokenTypes.RESERVED_WORD);
			tm.put("three", TokenTypes.RESERVED_WORD);

			return tm;
		}

		@Override
		public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
			return null;
		}

		private void initializeForTesting() {

			char[] text = "one two".toCharArray();

			firstToken = new TokenImpl(text, 0, 2, 0, TokenTypes.RESERVED_WORD, 0);
			currentToken = new TokenImpl(text, 3, 3, 3, TokenTypes.WHITESPACE, 0);
			firstToken.setNextToken(currentToken);
			TokenImpl nextToken = new TokenImpl(text, 4, 6, 4, TokenTypes.RESERVED_WORD, 0);
			currentToken.setNextToken(nextToken);
			previousToken = currentToken;
			currentToken = nextToken;
		}
	}
}
