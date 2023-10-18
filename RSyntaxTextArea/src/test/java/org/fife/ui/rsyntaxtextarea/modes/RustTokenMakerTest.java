/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link RustTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RustTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new RustTokenMaker();
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
	void testBinaryLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"0b0",
			"0b1",
			"0B0",
			"0B1",
			"0b010",
			"0B010",
			"0b0_10",
			"0B0_10"
		);
	}


	@Test
	void testBooleanLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			"true",
			"false"
		);
	}


	@Test
	void testByteStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"b\"unterminated string",
			"b\"\"",
			"b\"hi\"",
			"b\"\\xFE\"",
			"b\"\\\"\""
		);
	}


	@Test
	void testByteStringLiterals_validEscapeSequences() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"b\"\\xFE\\n\\r\\t\\\\\\0\\'\\\"\""
		);
	}


	@Test
	void testByteStringLiteral_error() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"b\"string with an invalid \\x latin-1 escape in it\"",
			"b\"string with an invalid \\u Unicode escape in it\"",
			"b\"even valid \\u00fe Unicode escapes are not allowed in byte strings\""
		);
	}


	@Test
	void testByteStringLiteral_error_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			RustTokenMaker.INTERNAL_IN_BYTE_STRING_INVALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testByteStringLiteral_valid_continuedFromPriorLine_continueToBeValid() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RustTokenMaker.INTERNAL_IN_BYTE_STRING_VALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testByteStringLiteral_valid_continuedFromPriorLine_newlyInvalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			RustTokenMaker.INTERNAL_IN_BYTE_STRING_VALID,
			"string with an invalid \\x latin-1 escape in it\"",
			"string with an invalid \\u Unicode escape in it\"",
			"even valid \\u00fe Unicode escapes are not allowed in byte strings\""
		);
	}


	@Test
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'a'",
			"'\\b'",
			"'\\s'",
			"'\\t'",
			"'\\r'",
			"'\\f'",
			"'\\n'",
			"'\\u00fe'",
			"'\\u00FE'",
			"'\\111'",
			"'\\222'",
			"'\\333'",
			"'\\11'",
			"'\\22'",
			"'\\33'",
			"'\\1'"
		);
	}


	@Test
	void testCharLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'\\x'"
		);
	}


	@Test
	void testDataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			"i8",
				"u8",
				"i16",
				"u16",
				"i32",
				"u32",
				"i64",
				"u64",
				"i128",
				"u128",
				"isize",
				"usize",
				"f32",
				"f64",
				"bool",
				"char",
				"str",
				"tup"
		);
	}


	@Test
	void testDocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			"/** Hello world */");
	}


	@Test
	void testDocComments_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			TokenTypes.COMMENT_DOCUMENTATION,
			"continued from a previous line */",
			"continued from a previous line but still unterminated"
		);
	}


	@Test
	void testDocComments_URL() {

		String[] docCommentLiterals = {
			"file://test.txt",
			"ftp://ftp.google.com",
			"https://www.google.com",
			"https://www.google.com",
			"www.google.com"
		};

		for (String literal : docCommentLiterals) {

			Segment segment = createSegment(literal);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.COMMENT_DOCUMENTATION, 0);
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals(literal, token.getLexeme());
		}

	}


	@Test
	void testDocComments_URL_onlyUrlRegionIsHyperlinked() {

		String text = "The URL https://www.google.com is the place";
		Segment segment = createSegment(text);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, TokenTypes.COMMENT_DOCUMENTATION, 0);
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
		Assertions.assertEquals("The URL ", token.getLexeme());

		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
		Assertions.assertEquals("https://www.google.com", token.getLexeme());

		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
		Assertions.assertEquals(" is the place", token.getLexeme());
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"// Hello world");
	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world https://www.google.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.google.com", token.getLexeme());

		}

	}


	@Test
	void testFloatingPointLiterals() {

		String code =
			// Basic doubles
			"3.0 4.2 3.0 4.2 .111 " +
			// Basic floats ending in f, F, d, or D
			"3f 3F 3d 3D 3.f 3.F 3.d 3.D 3.0f 3.0F 3.0d 3.0D .111f .111F .111d .111D " +
			// lower-case exponent, no sign
			"3e7f 3e7F 3e7d 3e7D 3.e7f 3.e7F 3.e7d 3.e7D 3.0e7f 3.0e7F 3.0e7d 3.0e7D .111e7f .111e7F .111e7d .111e7D " +
			// Upper-case exponent, no sign
			"3E7f 3E7F 3E7d 3E7D 3.E7f 3.E7F 3.E7d 3.E7D 3.0E7f 3.0E7F 3.0E7d 3.0E7D .111E7f .111E7F .111E7d .111E7D " +
			// Lower-case exponent, positive
			"3e+7f 3e+7F 3e+7d 3e+7D 3.e+7f 3.e+7F 3.e+7d 3.e+7D 3.0e+7f 3.0e+7F 3.0e+7d 3.0e+7D .111e+7f .111e+7F .111e+7d .111e+7D " +
			// Upper-case exponent, positive
			"3E+7f 3E+7F 3E+7d 3E+7D 3.E+7f 3.E+7F 3.E+7d 3.E+7D 3.0E+7f 3.0E+7F 3.0E+7d 3.0E+7D .111E+7f .111E+7F .111E+7d .111E+7D " +
			// Lower-case exponent, negative
			"3e-7f 3e-7F 3e-7d 3e-7D 3.e-7f 3.e-7F 3.e-7d 3.e-7D 3.0e-7f 3.0e-7F 3.0e-7d 3.0e-7D .111e-7f .111e-7F .111e-7d .111e-7D " +
			// Upper-case exponent, negative
			"3E-7f 3E-7F 3E-7d 3E-7D 3.E-7f 3.E-7F 3.E-7d 3.E-7D 3.0E-7f 3.0E-7F 3.0E-7d 3.0E-7D .111E-7f .111E-7F .111E-7d .111E-7D";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
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
	void testGetClosestStandardTokenTypeForInternalType() {

		TokenMaker tm = createTokenMaker();

		Assertions.assertEquals(
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(RustTokenMaker.INTERNAL_IN_STRING_INVALID));
		Assertions.assertEquals(
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(RustTokenMaker.INTERNAL_IN_STRING_VALID));
		Assertions.assertEquals(
			TokenTypes.RESERVED_WORD,
			tm.getClosestStandardTokenTypeForInternalType(TokenTypes.RESERVED_WORD));
	}


	@Test
	void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL " +
				"0x1_1 0xf_e 0x33_3333_333333 0X1_1 0Xf_e 0X333_333_33333 0xF_E 0XF_E " +
				"0x1_1l 0xf_el 0x333_33333_3333l 0X1_1l 0Xf_el 0X333_3333_3333l 0xF_El 0XF_El " +
				"0x1_1L 0xf_eL 0x333_33333_3333L 0X1_1L 0Xf_eL 0X333_3333_3333L 0xF_EL 0XF_EL";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
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
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			// Cyrillic chars - most Unicode chars are valid identifier chars
			"\u0438\u0439"
		);
	}


	@Test
	void testIdentifiers_error() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			"foo\\bar"
		);
	}


	@Test
	void testIntegerLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"0",
			"0l",
			"0L",
			"42",
			"42l",
			"42L",
			"123_456",
			"123_456l",
			"123456L"
		);
	}

	@Test
	void testIntegerLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			"42rst"
		);
	}


	@Test
	void testKeywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"_",
			"abstract",
			"alignof",
			"as",
			"become",
			"box",
			"break",
			"const",
			"continue",
			"crate",
			"do",
			"dyn",
			"else",
			"enum",
			"extern",
			"final",
			"fn",
			"for",
			"if",
			"impl",
			"in",
			"let",
			"loop",
			"macro",
			"match",
			"mod",
			"move",
			"mut",
			"offsetof",
			"override",
			"priv",
			"proc",
			"pub",
			"pure",
			"ref",
			"self",
			"sizeof",
			"static",
			"struct",
			"super",
			"trait",
			"type",
			"typeof",
			"union",
			"unsafe",
			"unsized",
			"use",
			"virtual",
			"where",
			"while",
			"yield"
		);
	}


	@Test
	void testKeywords_exitingMethod() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD_2,
			"return"
		);
	}


	@Test
	void testMultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"/* Hello world unterminated",
			"/* Hello world */",
			"/**/"
		);
	}


	@Test
	void testMultiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			TokenTypes.COMMENT_MULTILINE,
			"continued from a previous ine and unterminated",
			"continued from a previous line */"
		);
	}


	@Test
	void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world https://www.google.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals("https://www.google.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testOctalLiterals() {

		// Note that octal tokens use the token type for hex literals.

		String code = "01 073 0333333333333 01 073 033333333333 073 073 " +
				"01l 073l 0333333333333l 01l 073l 033333333333l 073l 073l " +
				"01L 073L 0333333333333L 01L 073L 033333333333L 073L 073L " +
				"01_1 07_3 033_3333_333333 01_1 07_3 0333_333_33333 07_3 07_3 " +
				"01_1l 07_3l 0333_33333_3333l 01_1l 07_3l 0333_3333_3333l 07_3l 07_3l " +
				"01_1L 07_3L 0333_33333_3333L 01_1L 07_3L 0333_3333_3333L 07_3L 07_3L";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
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
	void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
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
	void testRawByteStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"r\"unterminated string",
			"br\"terminated string\"",
			"br\"", // empty string
			"br\"hi\"",
			"br#\"one pound sign\"#",
			"br##\"two pound signs\"##",
			"br###\"three pound signs\"###",
			"br\"\\\"", // escapes have no meaning
			"br\"string with an invalid \\x latin-1 escape in it\"",
			"br\"string with an invalid \\u Unicode escape in it\""
		);
	}


	@Test
	void testRawByteStringLiteral_error_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			RustTokenMaker.INTERNAL_IN_RAW_BYTE_STRING_INVALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testRawByteStringLiteral_error_continuedFromPriorLine_withPoundSigns() {
		for (int poundCount = 1; poundCount < 5; poundCount++) {

			StringBuilder pounds = new StringBuilder();
			for (int i = 0; i < poundCount; i++) {
				pounds.append('#');
			}

			assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
				RustTokenMaker.INTERNAL_IN_RAW_BYTE_STRING_INVALID,
				"continued from a prior line and closed" + pounds + "\""
			);
		}
	}


	@Test
	void testRawByteStringLiteral_valid_continuedFromPriorLine_continueToBeValid() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RustTokenMaker.INTERNAL_IN_RAW_BYTE_STRING_VALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testRawByteStringLiteral_valid_continuedFromPriorLine_continueToBeValid_withPoundSigns() {
		for (int poundCount = 1; poundCount < 5; poundCount++) {

			StringBuilder pounds = new StringBuilder();
			for (int i = 0; i < poundCount; i++) {
				pounds.append('#');
			}

			assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
				RustTokenMaker.INTERNAL_IN_RAW_BYTE_STRING_VALID,
				"continued from a prior line and closed" + pounds + "\""
			);
		}
	}


	@Test
	void testRawStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"r\"unterminated string",
			"r\"terminated string\"",
			"r\"", // empty string
			"r\"hi\"",
			"r#\"one pound sign\"#",
			"r##\"two pound signs\"##",
			"r###\"three pound signs\"###",
			"r\"\\\"", // escapes have no meaning
			"r\"string with an invalid \\x latin-1 escape in it\"",
			"r\"string with an invalid \\u Unicode escape in it\""
		);
	}


	@Test
	void testRawStringLiteral_error_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			RustTokenMaker.INTERNAL_IN_RAW_STRING_INVALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testRawStringLiteral_error_continuedFromPriorLine_withPoundSigns() {
		for (int poundCount = 1; poundCount < 5; poundCount++) {

			StringBuilder pounds = new StringBuilder();
			for (int i = 0; i < poundCount; i++) {
				pounds.append('#');
			}

			assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
				RustTokenMaker.INTERNAL_IN_RAW_STRING_INVALID - poundCount,
				"continued from a prior line and closed" + pounds + "\""
			);
		}
	}


	@Test
	void testRawStringLiteral_valid_continuedFromPriorLine_continueToBeValid() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RustTokenMaker.INTERNAL_IN_RAW_STRING_VALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testRawStringLiteral_valid_continuedFromPriorLine_continueToBeValid_withPoundSigns() {
		for (int poundCount = 1; poundCount < 5; poundCount++) {

			StringBuilder pounds = new StringBuilder();
			for (int i = 0; i < poundCount; i++) {
				pounds.append('#');
			}

			assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
				RustTokenMaker.INTERNAL_IN_RAW_STRING_VALID,
				"continued from a prior line and closed" + pounds + "\""
			);
		}
	}


	@Test
	void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(",
			")",
			"[",
			"]",
			"{",
			"}"
		);
	}


	@Test
	void testStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"unterminated string",
			"\"\"",
			"\"hi\"",
			"\"\\xFE\"",
			"\"\\u00fe\"",
			"\"\\\"\""
		);
	}


	@Test
	void testStringLiterals_validEscapeSequences() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\\b\\s\\t\\n\\f\\r\\n\\\"\\'\\\\\""
		);
	}


	@Test
	void testStringLiteral_error() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"string with an invalid \\x latin-1 escape in it\"",
			"\"string with an invalid \\u Unicode escape in it\""
		);
	}


	@Test
	void testStringLiteral_error_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			RustTokenMaker.INTERNAL_IN_STRING_INVALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testStringLiteral_valid_continuedFromPriorLine() {
			assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			RustTokenMaker.INTERNAL_IN_STRING_VALID,
			"continued from a prior line and still unclosed",
			"continued from a prior line and closed\""
		);
	}


	@Test
	void testWhiteSpace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"   ",
			"\t",
			"\t\t",
			"\t  \n",
			"\f"
		);
	}
}
