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
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link BBCodeTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class BBCodeTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new BBCodeTokenMaker();
	}


	@Test
	public void testOutsideTag() {

		Segment segment = createSegment("one two");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(t.isIdentifier());
		t = t.getNextToken();
		Assert.assertTrue(t.isWhitespace());
		t = t.getNextToken();
		Assert.assertTrue(t.isIdentifier());
	}


	@Test
	public void testInsideTag() {

		Segment segment = createSegment("[b foo=bar]");
		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(t.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '['));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.MARKUP_TAG_NAME, "b"));
		t = t.getNextToken();
		Assert.assertTrue(t.isWhitespace());
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "foo"));
		t = t.getNextToken();
		Assert.assertTrue(t.isSingleChar(TokenTypes.OPERATOR, '='));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "bar"));
		t = t.getNextToken();
		Assert.assertTrue(t.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, ']'));

	}


	@Test
	public void testTagNames() {
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


	@Override
	public void testGetLineCommentStartAndEnd() {
		Assert.assertNull(createTokenMaker().getLineCommentStartAndEnd(0));
	}
}
