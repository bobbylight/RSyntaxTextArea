/*
 * 03/12/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;


/**
 * Unit tests for the {@link PlainTextTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class PlainTextTokenMakerTest extends AbstractTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new PlainTextTokenMaker();
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
	void testIdentifiers() {

		String code =  "   foo bar\t\tbas\t  \tbaz ";
		TokenMaker tm = createTokenMaker();

		Segment segment = createSegment(code);

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();

		while (token != null && token.isPaintable()) {
			Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType(), "Not an identifier: " + token);
			token = token.getNextToken();
			Assertions.assertTrue(token.isWhitespace());
			token = token.getNextToken();
		}

	}


	@Test
	void testUrls() {

		String code =  "http://www.sas.com foo ftp://fifesoft.com bar https://google.com goo www.yahoo.com ber file://test.txt";
		TokenMaker tm = createTokenMaker();

		Segment segment = createSegment(code);

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assertions.assertEquals("http://www.sas.com", token.getLexeme());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assertions.assertEquals("ftp://fifesoft.com", token.getLexeme());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assertions.assertEquals("https://google.com", token.getLexeme());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assertions.assertEquals("www.yahoo.com", token.getLexeme());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		token = token.getNextToken();
		Assertions.assertTrue(token.isWhitespace());

		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		Assertions.assertEquals("file://test.txt", token.getLexeme());

	}


	@Test
	void testWhitespace() {

		String code =  "   foo bar\t\tbas\t  \tbaz ";
		TokenMaker tm = createTokenMaker();

		Segment segment = createSegment(code);

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.isWhitespace());
		token = token.getNextToken();

		while (token != null && token.isPaintable()) {
			Assertions.assertEquals(TokenTypes.IDENTIFIER, token.getType(), "Not an identifier: " + token);
			token = token.getNextToken();
			Assertions.assertTrue(token.isWhitespace());
			token = token.getNextToken();
		}

	}


}
