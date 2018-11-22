/*
 * 03/23/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link MxmlTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class MxmlTokenMakerTest extends AbstractTokenMakerTest {


	@Test
	public void testMxml_comment() {

		String[] commentLiterals = {
			"<!-- Hello world -->",
		};

		for (String code : commentLiterals) {
			Segment segment = createSegment(code);
			MxmlTokenMaker tm = new MxmlTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.MARKUP_COMMENT, token.getType());
		}

	}


	@Test
	public void testMxml_comment_URL() {

		String code = "<!-- Hello world http://www.google.com -->";
		Segment segment = createSegment(code);
		MxmlTokenMaker tm = new MxmlTokenMaker();
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


	@Test
	public void testMxml_doctype() {

		String[] doctypes = {
			"<!doctype html>",
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
		};

		for (String code : doctypes) {
			Segment segment = createSegment(code);
			MxmlTokenMaker tm = new MxmlTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.MARKUP_DTD, token.getType());
		}

	}


	@Test
	public void testMxml_entityReferences() {

		String[] entityReferences = {
			"&nbsp;", "&lt;", "&gt;", "&#4012",
		};

		for (String code : entityReferences) {
			Segment segment = createSegment(code);
			MxmlTokenMaker tm = new MxmlTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.MARKUP_ENTITY_REFERENCE, token.getType());
		}

	}


	@Test
	public void testMxml_happyPath_tagWithAttributes() {

		String code = "<body onload=\"doSomething()\" data-extra='true'>";
		Segment segment = createSegment(code);
		MxmlTokenMaker tm = new MxmlTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assert.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "onload"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assert.assertTrue("Unexpected token: " + token, token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"doSomething()\""));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "data-extra"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'true'"));
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));
		
	}


	@Test
	public void testMxml_processingInstructions() {

		String[] doctypes = {
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
			"<?xml version='1.0' encoding='UTF-8' ?>",
			"<?xml-stylesheet type=\"text/css\" href=\"style.css\"?>",
		};

		for (String code : doctypes) {
			Segment segment = createSegment(code);
			MxmlTokenMaker tm = new MxmlTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.MARKUP_PROCESSING_INSTRUCTION, token.getType());
		}

	}


}