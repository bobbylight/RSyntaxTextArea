/*
 * 03/23/2015
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
 * Unit tests for the {@link XMLTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class XMLTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new XMLTokenMaker();
	}


	@Test
	void testXML_closingTag_tagNames() {
		String[] openingTags = {
			"</foo",
			"</value123",
			"</cszčšž",
			"</xmlns:cszčšž"
		};

		for (String code : openingTags) {
			Segment seg = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.MARKUP_TAG_NAME, token.getType());
			Assertions.assertEquals(code.substring(2), token.getLexeme());
		}
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("<!--", startAndEnd[0]);
		Assertions.assertEquals("-->", startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.MARKUP_TAG_NAME;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testXML_cdata() {
		assertAllTokensOfType(TokenTypes.MARKUP_CDATA_DELIMITER,
			"<![CDATA["
		);
	}


	@Test
	void testXML_cdata_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_CDATA,
			TokenTypes.MARKUP_CDATA,
			"continuing to next line"
		);
	}


	@Test
	void testXML_comment() {

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
	void testXML_comment_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			XMLTokenMaker.INTERNAL_IN_XML_COMMENT,
			"continued to next line",
			"ends on this line -->"
		);
	}


	@Test
	void testXML_comment_URL() {

		String code = "<!-- Hello world https://www.google.com -->";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- Hello world "), "Bad MARKUP_COMMENT: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "https://www.google.com"));
		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, " -->"));

	}


	@Test
	void testXML_doctype() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			"<!doctype html>",
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
				"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
			"<!DOCTYPE note [ xxx ]>"
		);
	}


	@Test
	void testXML_doctype_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			XMLTokenMaker.INTERNAL_DTD,
			"continued from prior line unterminated",
			"continued from prior line>"
		);
	}


	@Test
	void testXML_doctypeInternal_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			XMLTokenMaker.INTERNAL_DTD_INTERNAL,
			"continued from prior line unterminated",
			"continued from prior line]>"
		);
	}


	@Test
	void testXML_entityReferences() {

		String[] entityReferences = {
			"&nbsp;", "&lt;", "&gt;", "&#4012",
		};

		for (String code : entityReferences) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_ENTITY_REFERENCE, token.getType());
		}

	}


	@Test
	void testXML_getSetCompleteCloseTags() {
		try {
			Assertions.assertTrue(XMLTokenMaker.getCompleteCloseMarkupTags());
			XMLTokenMaker.setCompleteCloseTags(false);
			Assertions.assertFalse(XMLTokenMaker.getCompleteCloseMarkupTags());
		} finally {
			XMLTokenMaker.setCompleteCloseTags(true);
		}
	}


	@Test
	void testXML_happyPath_tagWithAttributes() {

		String code = "<body onload=\"doSomething()\" data-extra='true'>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "onload"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"doSomething()\""),
			"Unexpected token: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "data-extra"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'true'"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));

	}


	@Test
	void testXML_inAttrDouble() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			XMLTokenMaker.INTERNAL_ATTR_DOUBLE,
			"continued to next line",
			"ends on this line\""
		);
	}


	@Test
	void testXML_inAttrSingle() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			XMLTokenMaker.INTERNAL_ATTR_SINGLE,
			"continued to next line",
			"ends on this line'"
		);
	}


	@Test
	void testXML_inProcessingInstruction() {
		assertAllTokensOfType(TokenTypes.MARKUP_PROCESSING_INSTRUCTION,
			TokenTypes.MARKUP_PROCESSING_INSTRUCTION,
			"continued to next line",
			"ends on this line ?>"
		);
	}


	@Test
	void testXML_inTag_attributeNames() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE,
			XMLTokenMaker.INTERNAL_INTAG,
			"foo",
			"value123",
			"cszčšž",
			"xmlns:cszčšž"
		);
	}


	@Test
	void testXML_inTagScript_attributeNames() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE,
			XMLTokenMaker.INTERNAL_INTAG,
			"foo",
			"value123",
			"cszčšž",
			"xmlns:cszčšž"
		);
	}


	@Test
	void testXML_openingTag_tagNames() {
		String[] openingTags = {
			"<foo",
			"<value123",
			"<cszčšž",
			"<xmlns:cszčšž"
		};

		for (String code : openingTags) {
			Segment seg = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.MARKUP_TAG_NAME, token.getType());
			Assertions.assertEquals(code.substring(1), token.getLexeme());
		}
	}


	@Test
	void testXML_processingInstructions() {

		String[] doctypes = {
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
			"<?xml version='1.0' encoding='UTF-8' ?>",
			"<?xml-stylesheet type=\"text/css\" href=\"style.css\"?>",
		};

		for (String code : doctypes) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_PROCESSING_INSTRUCTION, token.getType());
		}

	}
}
