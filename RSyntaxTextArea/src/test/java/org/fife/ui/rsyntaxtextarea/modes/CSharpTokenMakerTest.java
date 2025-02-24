/*
 * 06/05/2016
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
 * Unit tests for the {@link CSharpTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class CSharpTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new CSharpTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("//", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER || i == TokenTypes.FUNCTION;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
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

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testCharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_CHAR, token.getType(), "Not a char literal: " + code);
		}

	}


	@Test
	void testCharLiterals_errors() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'too long'",
			"'a", // unterminated
			"'\\u00fg'" // invalid unicode escape
		);
	}


	@Test
	void testDataTypes() {

		String code = "byte sbyte int uint short ushort long ulong float double char bool";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] dataTypes = code.split(" +");
		for (int i = 0; i < dataTypes.length; i++) {
			Assertions.assertEquals(dataTypes[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.DATA_TYPE, token.getType(), "Not a data type: " + dataTypes[i]);
			if (i < dataTypes.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testDocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			"/// Hello world"
		);
	}


	@Test
	void testDocComments_tag() {

		String code = "/// <some-tag>foo";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_DOCUMENTATION, "/// "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.PREPROCESSOR, "<some-tag>"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_DOCUMENTATION, "foo"));
	}


	@Test
	void testDocComments_url() {

		String code = "/// https://www.google.com foo";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_DOCUMENTATION, "/// "));
		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_DOCUMENTATION, "https://www.google.com"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_DOCUMENTATION, " foo"));
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"// Hello world"
		);
	}


	@Test
	void testErrorNumberFormat() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			"54for",
			"0b10xxx"
		);
	}


	@Test
	void testFloatingPointLiterals() {

		String code =
			// Basic doubles
			"3.0 4.2 3.0 4.2 .111";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] literals = code.split(" +");
		for (int i = 0; i < literals.length; i++) {
			Assertions.assertEquals(literals[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType(), "Not a float: " + literals[i]);
			if (i < literals.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] literals = code.split(" +");
		for (int i = 0; i < literals.length; i++) {
			Assertions.assertEquals(literals[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType(), "Not a hex literal: " + literals[i]);
			if (i < literals.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"Foobar",
			",",
			".",
			";"
		);
	}


	@Test
	void testIdentifiers_errors() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			"###"
		);
	}


	@Test
	void testIntegers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"0",
			"1",
			"11111",
			"1234567890"
		);
	}


	@Test
	void testKeywords() {

		String code = "abstract as base break case catch checked class const continue " +
				"default delegate do else enum event explicit extern finally fixed for foreach goto if " +
				"implicit in interface internal is lock namespace new null operator out override " +
				"params private protected public readonly ref return sealed sizeof stackalloc static struct switch " +
				"this throw try typeof using virtual void volatile while";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType(), "Not a keyword: " + keywords[i]);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testMultiLineComments() {

		String[] mlcLiterals = {
			"/* Hello world */",
			"/* Hello world unterminated",
			"/**/"
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
		}

	}


	@Test
	void testMultiLineComments_fromPreviousLine() {

		String[] mlcLiterals = {
			"continued from a prior line unterminated",
			"continued from a prior line */",
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.COMMENT_MULTILINE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
		}

	}


	@Test
	void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world https://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | &&";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType());
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
	void testPreprocessors() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"#define foo",
			"# define foo",
			"   #define foo",
			"#undef",
			"#if",
			"#elif",
			"#else",
			"#endif",
			"#line",
			"#error",
			"#warning",
			"#region",
			"#endregion"
		);
	}


	@Test
	void testSeparators() {

		String code = "( ) [ ] { }";

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
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
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
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated string",
			"\" string with invalid unicode \\u00fg escape\""
		);
	}


	@Test
	void testVerbatimStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"@\"foo\"",
			"@\"foo with \\ single slash\"",
			"@\"foo with \"\" an escaped double quote\"",
			"@\"unterminated verbatim string"
		);
	}


	@Test
	void testVerbatimStringLiterals_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued and terminated\"",
			"continued and still unterminated"
		);
	}
}
