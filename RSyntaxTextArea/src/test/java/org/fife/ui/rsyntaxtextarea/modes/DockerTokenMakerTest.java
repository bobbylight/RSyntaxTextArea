/*
 * 11/24/2015
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
 * Unit tests for the {@link DockerTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DockerTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new DockerTokenMaker();
	}


	@Test
	void testCharLiterals() {

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
			Assertions.assertEquals(TokenTypes.LITERAL_CHAR, token.getType(), "Invalid char literal: " + token);
		}

	}


	@Test
	void testEolComments() {

		String[] eolCommentLiterals = {
			"# Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		TokenMaker tm = createTokenMaker();
		String[] startAndEnd = tm.getLineCommentStartAndEnd(0);
		Assertions.assertArrayEquals(new String[] { "#", null }, startAndEnd);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER || i == TokenTypes.RESERVED_WORD;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testKeywords() {

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
			Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType(),
				"Not a keyword: " + token);
		}

	}


	@Test
	void testOperators() {

		String code = "| > >>";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType(), "Not an operator: " + token);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void getTestMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertTrue(tm.getMarkOccurrencesOfTokenType(TokenTypes.RESERVED_WORD));
		Assertions.assertTrue(tm.getMarkOccurrencesOfTokenType(TokenTypes.IDENTIFIER));
		Assertions.assertFalse(tm.getMarkOccurrencesOfTokenType(TokenTypes.OPERATOR));
	}


	@Test
	void testSeparators() {

		String code = "[ ]";

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

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testStringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\\"\"",
			"\"Unterminated string",
			"\"Unterminated string (this case does not occur in RSTA)\n",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


}
