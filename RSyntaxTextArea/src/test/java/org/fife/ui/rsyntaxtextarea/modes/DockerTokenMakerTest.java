/*
 * 11/24/2015
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
 * Unit tests for the {@link DockerTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DockerTokenMakerTest extends AbstractTokenMakerTest {


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	private TokenMaker createTokenMaker() {
		return new DockerTokenMaker();
	}


	@Test
	public void testCharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'",
			"'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
			"'Unterminated char literal",
			"'Unterminated char literal (this case does not occur in RSTA)\n",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals("Invalid char literal: " + token, TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	public void testEolComments() {

		String[] eolCommentLiterals = {
			"# Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		TokenMaker tm = createTokenMaker();
		String[] startAndEnd = tm.getLineCommentStartAndEnd(0);
		Assert.assertArrayEquals(new String[] { "#", null }, startAndEnd);
	}


	@Test
	public void testKeywords() {

		String[] keywords = {
				"maintainer",
				"from",
				"onbuild",
				"run",
				"expose",
				"env",
				"add",
				"copy",
				"volume",
				"user",
				"workdir",
				"cmd",
				"entrypoint",
				"label",
				"arg",
				"stopsignal",
		};

		for (String code : keywords) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals("Not a keyword: " + token,
					TokenTypes.RESERVED_WORD, token.getType());
		}

	}


	@Test
	public void testOperators() {

		String code = "| > >>";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Not an operator: " + token, TokenTypes.OPERATOR, token.getType());
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
	public void getTestMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		Assert.assertTrue(tm.getMarkOccurrencesOfTokenType(TokenTypes.RESERVED_WORD));
		Assert.assertTrue(tm.getMarkOccurrencesOfTokenType(TokenTypes.IDENTIFIER));
		Assert.assertFalse(tm.getMarkOccurrencesOfTokenType(TokenTypes.OPERATOR));
	}


	@Test
	public void testSeparators() {

		String code = "[ ]";

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
	public void testStringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\\"\"",
			"\"Unterminated string",
			"\"Unterminated string (this case does not occur in RSTA)\n",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


}