/*
 * 10/17/2015
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
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link HostsTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class HostsTokenMakerTest extends AbstractTokenMakerTest {


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	private TokenMaker createTokenMaker() {
		return new HostsTokenMaker();
	}


	@Test
	void testEolComments() {

		String[] eolCommentLiterals = {
			"# Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"# Hello world http://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("http://www.sas.com", token.getLexeme());

		}

	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	void testGetMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertTrue(tm.getMarkOccurrencesOfTokenType(
				TokenTypes.RESERVED_WORD));
		Assertions.assertFalse(tm.getMarkOccurrencesOfTokenType(
				TokenTypes.VARIABLE));
	}


	@Test
	void testKeywords() {

		String code = "127.0.0.1 localhost";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "127.0.0.1"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "localhost"));

	}


}
