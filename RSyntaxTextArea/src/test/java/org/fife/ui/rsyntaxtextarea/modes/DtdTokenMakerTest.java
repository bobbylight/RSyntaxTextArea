/*
 * 07/09/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
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
class DtdTokenMakerTest extends AbstractJFlexTokenMakerTest {


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
	@Override
	public void testCommon_yycharat() {
		Segment segment = createSegment("foo\nbar");
		TokenMaker tm = createTokenMaker();
		tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () ->
			// assertion needed to appease spotbugsTest
			Assertions.assertEquals('\n', ((AbstractJFlexTokenMaker)tm).yycharat(0))
		);
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
	void testDtd_comment_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			DtdTokenMaker.INTERNAL_IN_COMMENT,
			"continued from prior line -->",
			"continued and still unterminated"
		);
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


	@Test
	void testDtd_identifiers() {
		// Not really valid, but not sure how to render. As errors?
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"foo123"
		);
	}


	@Test
	void testDtd_inTag_attlist_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_PROCESSING_INSTRUCTION,
			DtdTokenMaker.INTERNAL_INTAG_ATTLIST,
			"CDATA",
			"#IMPLIED",
			"#REQUIRED"
		);
	}


	@Test
	void testDtd_inTag_attlist_strings() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			DtdTokenMaker.INTERNAL_INTAG_ATTLIST,
			"\"foo bar\"",
			"'foo bar'"
		);
	}


	@Test
	void testDtd_inTag_element_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE,
			DtdTokenMaker.INTERNAL_INTAG_ELEMENT,
			"foobar"
		);
	}


	@Test
	void testDtd_inTag_starting_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_NAME,
			DtdTokenMaker.INTERNAL_INTAG_START,
			"ELEMENT",
			"ATTLIST"
		);
	}


	@Test
	void testDtd_inTag_starting_identifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			DtdTokenMaker.INTERNAL_INTAG_START,
			"foo",
			"foo123"
		);
	}


	@Test
	void testDtd_whitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"   ",
			"\t",
			"\t\t",
			"\t  ",
			"\f"
		);
	}
}
