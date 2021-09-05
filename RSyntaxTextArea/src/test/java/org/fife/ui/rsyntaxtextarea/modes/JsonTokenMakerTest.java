/*
 * 03/15/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link JsonTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JsonTokenMakerTest extends AbstractTokenMakerTest {


	@Test
	void testBooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		JsonTokenMaker tm = new JsonTokenMaker();
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
		JsonTokenMaker tm = new JsonTokenMaker();
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
	@Override
	public void testGetLineCommentStartAndEnd() {
		Assertions.assertNull(new JsonTokenMaker().getLineCommentStartAndEnd(0));
	}


	@Test
	void testIntegerLiterals() {

		String code = "1 42 0 -1 -42";

		Segment segment = createSegment(code);
		JsonTokenMaker tm = new JsonTokenMaker();
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
		JsonTokenMaker tm = new JsonTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		// Only need to check the first token
		Assertions.assertFalse(token.isComment());
	}


	@Test
	void testNullLiterals() {
		String code = "null";
		Segment segment = createSegment(code);
		JsonTokenMaker tm = new JsonTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "null"));
	}


	@Test
	void testSeparators() {

		String code = "[ ] { }";

		Segment segment = createSegment(code);
		JsonTokenMaker tm = new JsonTokenMaker();
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
			JsonTokenMaker tm = new JsonTokenMaker();
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
			JsonTokenMaker tm = new JsonTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.ERROR_STRING_DOUBLE, token.getType());
		}

	}


}
