/*
 * 03/12/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.junit.Assert;
import org.junit.Test;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;


/**
 * Unit tests for the {@link PlainTextTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class PlainTextTokenMakerTest extends AbstractTokenMakerTest {


	@Test
	public void testIdentifiers() {

		String code =  "   foo bar\t\tbas\t  \tbaz ";
		PlainTextTokenMaker tm = new PlainTextTokenMaker();

		Segment segment = createSegment(code);

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(token.isWhitespace());
		token = token.getNextToken();

		while (token != null && token.isPaintable()) {
			Assert.assertEquals("Not an identifier: " + token, TokenTypes.IDENTIFIER, token.getType());
			token = token.getNextToken();
			Assert.assertTrue(token.isWhitespace());
			token = token.getNextToken();
		}

	}


	@Test
	public void testUrls() {

		String code =  "http://www.sas.com foo ftp://fifesoft.com bar https://google.com goo www.yahoo.com ber file://test.txt";
		PlainTextTokenMaker tm = new PlainTextTokenMaker();

		Segment segment = createSegment(code);

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assert.assertEquals("http://www.sas.com", token.getLexeme());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertFalse(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertTrue(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assert.assertEquals("ftp://fifesoft.com", token.getLexeme());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertFalse(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertTrue(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assert.assertEquals("https://google.com", token.getLexeme());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertFalse(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertTrue(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assert.assertEquals("www.yahoo.com", token.getLexeme());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertFalse(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assert.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assert.assertTrue(token.isHyperlink());
		Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assert.assertEquals("file://test.txt", token.getLexeme());

	}


	@Test
	public void testWhitespace() {

		String code =  "   foo bar\t\tbas\t  \tbaz ";
		PlainTextTokenMaker tm = new PlainTextTokenMaker();

		Segment segment = createSegment(code);

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(token.isWhitespace());
		token = token.getNextToken();

		while (token != null && token.isPaintable()) {
			Assert.assertEquals("Not an identifier: " + token, TokenTypes.IDENTIFIER, token.getType());
			token = token.getNextToken();
			Assert.assertTrue(token.isWhitespace());
			token = token.getNextToken();
		}

	}


}