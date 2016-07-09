/*
 * 07/09/2016
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
 * Unit tests for the {@link DtdTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DtdTokenMakerTest extends AbstractTokenMakerTest {


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	private TokenMaker createTokenMaker() {
		return new DtdTokenMaker();
	}


	@Test
	public void testGetMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		Assert.assertFalse(tm.getMarkOccurrencesOfTokenType(
				TokenTypes.IDENTIFIER));
	}


	@Test
	public void testDtd_comment() {

		String[] commentLiterals = {
			"<!-- Hello world -->",
		};

		for (String code : commentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.MARKUP_COMMENT, token.getType());
		}

	}


	@Test
	public void testDtd_comment_URL() {

		String code = "<!-- Hello world http://www.google.com -->";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assert.assertFalse(token.isHyperlink());
		Assert.assertTrue("Token is not type MARKUP_COMMENT: " + token,
				token.is(TokenTypes.MARKUP_COMMENT, "<!-- Hello world "));
		token = token.getNextToken();
		Assert.assertTrue(token.isHyperlink());
		Assert.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "http://www.google.com"));
		token = token.getNextToken();
		Assert.assertFalse(token.isHyperlink());
		Assert.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, " -->"));

	}


}