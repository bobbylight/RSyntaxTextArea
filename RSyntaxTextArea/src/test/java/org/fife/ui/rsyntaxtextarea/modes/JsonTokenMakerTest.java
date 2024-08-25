/*
 * 03/15/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link JsonTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class JsonTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new JsonTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		Assertions.assertNull(new JsonTokenMaker().getLineCommentStartAndEnd(0));
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
	protected void testCommon_getShouldIndentNextLineAfter() {
		TokenMaker tm = createTokenMaker();
		Token[] indentAfter = {
			new TokenImpl("{".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, 0),
			new TokenImpl("[".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, 0),
		};
		for (Token token : indentAfter) {
			Assertions.assertTrue(tm.getShouldIndentNextLineAfter(token));
		}
	}


	@Test
	@Override
	public void testCommon_yycharat() {
		Segment segment = createSegment("foobar");
		TokenMaker tm = createTokenMaker();
		tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () ->
			// assertion needed to appease spotbugsTest
			Assertions.assertEquals('\n', ((AbstractJFlexTokenMaker)tm).yycharat(0))
		);
	}


	@Test
	void testBooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	void testFloatingPointLiterals() {

		String code =
			// Basic doubles
			"3.0 4.2 3.000 4.2 0.111 " +
			// lower-case exponent, no sign
			"3e7 3.0e7 0.111e7 -3e7 -3.0e7 -0.111e7 " +
			// Upper-case exponent, no sign
			"3E7 3.0E7 0.111E7 -3E7 -3.0E7 -0.111E7 " +
			// Lower-case exponent, positive
			"3e+7 3.0e+7 0.111e+7 -3e+7 -3.0e+7 -0.111e+7 " +
			// Upper-case exponent, positive
			"3E+7 3.0E+7 0.111E+7 -3E+7 -3.0E+7 -0.111E+7 " +
			// Lower-case exponent, negative
			"3e-7 3.0e-7 0.111e-7 -3e-7 -3.0e-7 -0.111e-7 " +
			// Upper-case exponent, negative
			"3E-7 3.0E-7 0.111E-7 -3E-7 -3.0E-7 -0.111E-7";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType(), "Invalid float: " + token);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	void testIntegerLiterals() {

		String code = "1 42 0 -1 -42";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	void testNoMultiLineComments() {
		String code = "// Hello world";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		// Only need to check the first token
		Assertions.assertFalse(token.isComment());
	}


	@Test
	void testNullLiterals() {
		String code = "null";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "null"));
	}


	@Test
	void testSeparators() {

		String code = "[ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assertions.assertEquals(separators[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());
			// Just one extra test here
			Assertions.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, separators[i].charAt(0)));
			if (i < separators.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	void testStringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"", "\"\\\\/\\b\\f\\n\\r\\t\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	void testStringLiterals_errors() {

		String[] stringLiterals = {
			"\"foo \\x bar\"",
			"\"foo unterminated string",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.ERROR_STRING_DOUBLE, token.getType());
		}

	}


}
