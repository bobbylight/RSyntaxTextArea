/*
 * 07/09/2016
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
 * Unit tests for the {@link DtdTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DtdTokenMakerTest extends AbstractTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new DtdTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		Assertions.assertNull(createTokenMaker().getLineCommentStartAndEnd(0));
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			Assertions.assertFalse(tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testDtd_comment() {

		String[] commentLiterals = {
			"<!-- Hello world -->",
		};

		for (String code : commentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_COMMENT, token.getType());
		}

	}


	@Test
	void testDtd_comment_URL() {

		String code = "<!-- Hello world https://www.google.com -->";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- Hello world "),
			"Token is not type MARKUP_COMMENT: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "https://www.google.com"));
		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, " -->"));

	}


}
