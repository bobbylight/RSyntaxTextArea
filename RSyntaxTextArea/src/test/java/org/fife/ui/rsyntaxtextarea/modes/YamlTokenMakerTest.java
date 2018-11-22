/*
 * 12/29/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link YamlTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class YamlTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new YamlTokenMaker();
	}


	@Test
	public void testEolComments() {
		String[] eolCommentLiterals = {
			"# Hello world",
		};
		assertAllTokensOfType(eolCommentLiterals, TokenTypes.COMMENT_EOL);
	}


	@Test
	public void testOperators() {

		String code = "- : ? & * ! % @ `";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.OPERATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testSeparators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
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
	public void testSeparators2() {

		String code = "; , .";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assert.assertEquals(separators[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
			// Just one extra test here
			Assert.assertTrue(token.isSingleChar(TokenTypes.IDENTIFIER, separators[i].charAt(0)));
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
	public void testStringLiterals_doubleQuote() {
		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
		};
		assertAllTokensOfType(stringLiterals, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE);
	}


	@Test
	public void testStringLiterals_doubleQuote_error() {
		String[] stringLiterals = {
			"\"Hello world unterminated",
			"\"Invalid escape \\uINVALID\"",
		};
		assertAllTokensOfType(stringLiterals, TokenTypes.ERROR_STRING_DOUBLE);
	}


	@Test
	public void testStringLiterals_singleQuote() {
		String[] stringLiterals = {
			"''", "'hi'", "'\\u00fe'", "'\\\''",
		};
		assertAllTokensOfType(stringLiterals, TokenTypes.LITERAL_CHAR);
	}


	@Test
	public void testStringLiterals_singleQuote_error() {
		String[] stringLiterals = {
			"'Hello world unterminated",
			"'Invalid escape \\uINVALID'",
		};
		assertAllTokensOfType(stringLiterals, TokenTypes.ERROR_CHAR);
	}


}