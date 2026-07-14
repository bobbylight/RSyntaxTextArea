/*
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
 * Unit tests for the {@link EnvTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class EnvTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new EnvTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertTrue(tm.getMarkOccurrencesOfTokenType(TokenTypes.VARIABLE));
		Assertions.assertFalse(tm.getMarkOccurrencesOfTokenType(TokenTypes.IDENTIFIER));
	}


	@Test
	void testGetBracketPairs() {
		Assertions.assertEquals("", createTokenMaker().getBracketPairs());
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"# Hello world",
			"#Hello world"
		);
	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"# See https://www.example.com for details",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertFalse(token.isHyperlink());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.example.com", token.getLexeme());

		}

	}


	@Test
	void testKeyValuePair_unquotedValue() {

		String code = "DATABASE_HOST=localhost";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "DATABASE_HOST"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "localhost"));

	}


	@Test
	void testKeyValuePair_emptyValue() {

		String code = "FOO=";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "FOO"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testKeyValuePair_doubleQuotedValue() {

		String code = "GREETING=\"hello world\"";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "GREETING"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"hello world\""));

	}


	@Test
	void testKeyValuePair_singleQuotedValue() {

		String code = "GREETING='hello world'";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "GREETING"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'hello world'"));

	}


	@Test
	void testKeyValuePair_unterminatedDoubleQuotedValue() {

		String code = "GREETING=\"hello world";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "GREETING"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"hello world"));

	}


	@Test
	void testKeyValuePair_unterminatedSingleQuotedValue() {

		String code = "GREETING='hello world";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "GREETING"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'hello world"));

	}


	@Test
	void testKeyValuePair_booleanValue() {

		String[] values = { "true", "false", "TRUE", "False" };

		for (String value : values) {
			String code = "DEBUG=" + value;
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "DEBUG"));

			token = token.getNextToken();
			Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

			token = token.getNextToken();
			Assertions.assertTrue(token.is(TokenTypes.LITERAL_BOOLEAN, value));
		}

	}


	@Test
	void testKeyValuePair_integerValue() {

		String code = "PORT=8080";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "PORT"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "8080"));

	}


	@Test
	void testKeyValuePair_negativeIntegerValue() {

		String code = "OFFSET=-5";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "OFFSET"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "-5"));

	}


	@Test
	void testKeyValuePair_floatValue() {

		String code = "TIMEOUT_SECONDS=3.5";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "TIMEOUT_SECONDS"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_FLOAT, "3.5"));

	}


	@Test
	void testKeyValuePair_valueContainingEquals() {

		// Base64-esque values may legitimately contain '=' characters after
		// the first one, which is the actual key/value separator.
		String code = "API_TOKEN=abc123==";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "API_TOKEN"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "abc123=="));

	}


	@Test
	void testExportKeyword() {

		String code = "export FOO=bar";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "export"));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "FOO"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "bar"));

	}


	@Test
	void testKeyNamedExport() {

		// "export" is only a keyword when followed by whitespace; here it's
		// a key in its own right.
		String code = "export=1";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "export"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "1"));

	}


	@Test
	void testKeyWithUnderscoresAndDigits() {

		String code = "_FOO_BAR_123=baz";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "_FOO_BAR_123"));

	}


	@Test
	void testInlineComment_afterUnquotedValue() {

		String code = "FOO=bar # a comment";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "FOO"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "bar"));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, "# a comment"));

	}


	@Test
	void testInlineComment_afterQuotedValue() {

		String code = "FOO=\"bar\" # a comment";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "FOO"));

		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"bar\""));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, "# a comment"));

	}


	@Test
	void testWhitespace_leadingBeforeKey() {

		String code = "  FOO=bar";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, "  "));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "FOO"));

	}


	@Test
	void testBlankLine() {

		String code = "";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testMultipleKeyValuePairsAcrossLines() {

		String code = "FOO=bar\nBAZ=bin";

		for (String line : code.split("\n")) {
			Segment segment = createSegment(line);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.VARIABLE, token.getType());
		}

	}


}
