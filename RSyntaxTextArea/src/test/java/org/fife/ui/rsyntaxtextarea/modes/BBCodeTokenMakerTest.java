/*
 * 06/21/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link BBCodeTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class BBCodeTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new BBCodeTokenMaker();
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testOutsideTag() {

		Segment segment = createSegment("one two");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(t.isIdentifier());
		t = t.getNextToken();
		Assertions.assertTrue(t.isWhitespace());
		t = t.getNextToken();
		Assertions.assertTrue(t.isIdentifier());
	}


	@Test
	void testInsideTag() {

		Segment segment = createSegment("[b foo=bar]");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(t.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '['));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.MARKUP_TAG_NAME, "b"));
		t = t.getNextToken();
		Assertions.assertTrue(t.isWhitespace());
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "foo"));
		t = t.getNextToken();
		Assertions.assertTrue(t.isSingleChar(TokenTypes.OPERATOR, '='));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "bar"));
		t = t.getNextToken();
		Assertions.assertTrue(t.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, ']'));

	}


	@Test
	void testTagNames() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_NAME, BBCodeTokenMaker.INTERNAL_INTAG,
			"b",
			"i",
			"u",
			"s",
			"size",
			"color",
			"center",
			"quote",
			"url",
			"img",
			"ul",
			"li",
			"ol",
			"youtube",
			"gvideo");
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		Assertions.assertNull(createTokenMaker().getLineCommentStartAndEnd(0));
	}
}
