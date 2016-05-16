/*
 * 10/17/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


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
	public void testEolComments() {

		String[] eolCommentLiterals = {
			"# Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"# Hello world http://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

		}

	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("#", startAndEnd[0]);
		Assert.assertNull(startAndEnd[1]);
	}


	@Test
	public void testGetMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		Assert.assertTrue(tm.getMarkOccurrencesOfTokenType(
				TokenTypes.RESERVED_WORD));
		Assert.assertFalse(tm.getMarkOccurrencesOfTokenType(
				TokenTypes.VARIABLE));
	}


	@Test
	public void testKeywords() {

		String code = "127.0.0.1 localhost";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "127.0.0.1"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "localhost"));

	}


}