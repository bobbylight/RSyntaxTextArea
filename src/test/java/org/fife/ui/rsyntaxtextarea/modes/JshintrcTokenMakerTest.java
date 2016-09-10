/*
 * 03/15/2015
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
 * Unit tests for the {@link JshintrcTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JshintrcTokenMakerTest extends AbstractTokenMakerTest {


	@Test
	public void testBooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		JshintrcTokenMaker tm = new JshintrcTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testFloatingPointLiterals() {

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
		JshintrcTokenMaker tm = new JshintrcTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals("Unexpected number for token " + i, keywords[i], token.getLexeme());
			Assert.assertEquals("Invalid float: " + token, TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testIntegerLiterals() {

		String code = "1 42 0 -1 -42";

		Segment segment = createSegment(code);
		JshintrcTokenMaker tm = new JshintrcTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals("Unexpected number for token " + i, keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testLineComments() {
		String code = "// Hello world";
		Segment segment = createSegment(code);
		JshintrcTokenMaker tm = new JshintrcTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(token.is(Token.COMMENT_EOL, code));
	}


	@Test
	public void testNullLiterals() {
		String code = "null";
		Segment segment = createSegment(code);
		JshintrcTokenMaker tm = new JshintrcTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "null"));
	}


	@Test
	public void testSeparators() {

		String code = "[ ] { }";

		Segment segment = createSegment(code);
		JshintrcTokenMaker tm = new JshintrcTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assert.assertEquals(separators[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.SEPARATOR, token.getType());
			// Just one extra test here
			Assert.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, separators[i].charAt(0)));
			if (i < separators.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testStringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"", "\"\\\\/\\b\\f\\n\\r\\t\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			JshintrcTokenMaker tm = new JshintrcTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals("Invalid string: " + token, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	public void testStringLiterals_errors() {

		String[] stringLiterals = {
			"\"foo \\x bar\"",
			"\"foo unterminated string",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			JshintrcTokenMaker tm = new JshintrcTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals("Invalid error-string: " + token, TokenTypes.ERROR_STRING_DOUBLE, token.getType());
		}

	}


}