/*
 * 06/21/2015
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
 * Unit tests for the {@link DartTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DartTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new DartTokenMaker();
	}


	@Test
	void testAnnotations() {
		assertAllTokensOfType(TokenTypes.ANNOTATION,
			"@foo",
			"@foo123"
		);
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

		String[] booleans = { "true", "false" };

		for (String code : booleans) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.LITERAL_BOOLEAN, code));
		}

	}


	@Test
	void testCharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'",
			"'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_CHAR, token.getType(), "Invalid char literal: " + token);
		}

	}


	@Test
	void testCharLiterals_continuedFromPriorLine_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			DartTokenMaker.INTERNAL_IN_JS_CHAR_VALID,
			"continued from prior line and terminated'"
		);
	}


	@Test
	void testCharLiterals_continuedFromPriorLine_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			DartTokenMaker.INTERNAL_IN_JS_CHAR_VALID,
			"continued from prior line unterminated"
		);
	}


	@Test
	void testCharLiteral_error() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'unterminated char",
			"'string with an invalid \\x escape in it'"
		);
	}


	@Test
	void testCharLiteral_error_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			DartTokenMaker.INTERNAL_IN_JS_CHAR_INVALID,
			"finally terminated'",
			"still unterminated"
		);
	}


	@Test
	void testDataTypes() {

		String[] dataTypes = {
			"bool", "int", "double", "num", "void",
		};

		for (String code : dataTypes) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, code));
		}

	}


	@Test
	void testEolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world https://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

		}

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
			"3.0 4.2 3.0 4.2 .111 " +
			// Basic floats ending in f, F, d, or D
			"3.f 3.F 3.0f 3.0F .111f .111F " +
			// lower-case exponent, no sign
			"3.e7f 3.e7F 3.0e7f 3.0e7F .111e7f .111e7F " +
			// Upper-case exponent, no sign
			"3.E7f 3.E7F 3.0E7f 3.0E7F .111E7f .111E7F " +
			// Lower-case exponent, positive
			"3.e+7f 3.e+7F 3.0e+7f 3.0e+7F .111e+7f .111e+7F " +
			// Upper-case exponent, positive
			"3.E+7f 3.E+7F 3.0E+7f 3.0E+7F .111E+7f .111E+7F " +
			// Lower-case exponent, negative
			"3.e-7f 3.e-7F 3.0e-7f 3.0e-7F .111e-7f .111e-7F " +
			// Upper-case exponent, negative
			"3.E-7f 3.E-7F 3.0E-7f 3.0E-7F .111E-7f .111E-7F";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType(), "Invalid floating point: " + token);
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

		Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE,
			tm.getClosestStandardTokenTypeForInternalType(DartTokenMaker.INTERNAL_IN_JS_MLC));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(DartTokenMaker.INTERNAL_IN_JS_STRING_INVALID));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(DartTokenMaker.INTERNAL_IN_JS_STRING_VALID));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(DartTokenMaker.INTERNAL_IN_JS_CHAR_INVALID));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(DartTokenMaker.INTERNAL_IN_JS_CHAR_VALID));
		Assertions.assertEquals(TokenTypes.IDENTIFIER,
			tm.getClosestStandardTokenTypeForInternalType(TokenTypes.IDENTIFIER));
	}


	@Test
	void testGetJavaScriptVersion() {
		String origVersion = DartTokenMaker.getJavaScriptVersion();
		try {
			Assertions.assertEquals("1.0", DartTokenMaker.getJavaScriptVersion());
			DartTokenMaker.setJavaScriptVersion("1.5");
			Assertions.assertEquals("1.5", DartTokenMaker.getJavaScriptVersion());
		} finally {
			DartTokenMaker.setJavaScriptVersion(origVersion);
		}
	}


	@Test
	void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType(), "Invalid hex literal: " + token);
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
			"foo123"
		);
	}


	@Test
	void testIdentifiers_error() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			"foo\\bar"
		);
	}


	@Test
	void testIsJavaScriptCompatible() {
		Assertions.assertTrue(DartTokenMaker.isJavaScriptCompatible("0.9"));
		Assertions.assertTrue(DartTokenMaker.isJavaScriptCompatible("1.0"));
		Assertions.assertFalse(DartTokenMaker.isJavaScriptCompatible("1.1"));
		Assertions.assertFalse(DartTokenMaker.isJavaScriptCompatible("1.2"));
		Assertions.assertFalse(DartTokenMaker.isJavaScriptCompatible("1.3"));
	}


	@Test
	void testStandardFunctions() {

		String[] functions = {

			// stdlib types
			"AssertionError",
			"Clock",
			"Collection",
			"Comparable",
			"Date",
			"Dispatcher",
			"Duration",
			"Expect",
			"FallThroughError",
			"Function",
			"HashMap",
			"HashSet",
			"Hashable",
			"Isolate",
			"Iterable",
			"Iterator",
			"LinkedHashMap",
			"List",
			"Map",
			"Match",
			"Math",
			"Object",
			"Pattern",
			"Promise",
			"Proxy",
			"Queue",
			"ReceivePort",
			"RegExp",
			"SendPort",
			"Set",
			"StopWatch",
			"String",
			"StringBuffer",
			"Strings",
			"TimeZone",
			"TypeError",

			// stdlib exceptions
			"BadNumberFormatException",
			"ClosureArgumentMismatchException",
			"EmptyQueueException",
			"Exception",
			"ExpectException",
			"IllegalAccessException",
			"IllegalArgumentException",
			"IllegalJSRegExpException",
			"IndexOutOfRangeException",
			"IntegerDivisionByZeroException",
			"NoMoreElementsException",
			"NoSuchMethodException",
			"NotImplementedException",
			"NullPointerException",
			"ObjectNotClosureException",
			"OutOfMemoryException",
			"StackOverflowException",
			"UnsupportedOperationException",
			"WrongArgumentCountException"

		};

		for (String code : functions) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.FUNCTION, token.getType(),
				"Not a standard function: " + token);
		}

	}


	@Test
	void testKeywords() {

		String[] keywords = {
			"abstract",
			"assert",
			"class",
			"const",
			"extends",
			"factory",
			"get",
			"implements",
			"import",
			"interface",
			"library",
			"negate",
			"new",
			"null",
			"operator",
			"set",
			"source",
			"static",
			"super",
			"this",
			"typedef",
			"var",
			"final",
			"if",
			"else",
			"for",
			"in",
			"is",
			"while",
			"do",
			"switch",
			"case",
			"default",
			"in",
			"try",
			"catch",
			"finally",
			"break",
			"continue",
			"throw",
			"assert",
			"NaN",
			"Infinity",
		};

		for (String code : keywords) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, code));
		}

		Segment segment = createSegment("return");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD_2, "return"));

	}


	@Test
	void testMultiLineChar_allOnOneLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a text block\"\"\""
		);
	}


	@Test
	void testMultiLineChar_continuingToAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'''this is a text block"
		);
	}


	@Test
	void testMultiLineChar_continuedFromAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued from another line and unterminated",
			"continued from another line and terminated'''"
		);
	}


	@Test
	void testMultiLineChar_notContinuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'''this is a \\''' text block'''",
			"'''this is a \\''' text block"
		);
	}


	@Test
	void testMultiLineChar_continuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued from another \\''' line and unterminated",
			"continued from another \\''' line and terminated'''"
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
			DartTokenMaker.INTERNAL_IN_JS_MLC,
			"continued from a previous ine and unterminated",
			"continued from a previous line */"
		);
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
	void testMultiLineString_allOnOneLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a text block\"\"\""
		);
	}


	@Test
	void testMultiLineString_continuingToAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a text block"
		);
	}


	@Test
	void testMultiLineString_continuedFromAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from another line and unterminated",
			"continued from another line and terminated\"\"\""
		);
	}


	@Test
	void testMultiLineString_notContinuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a \\\"\"\" text block\"\"\"",
			"\"\"\"this is a \\\"\"\" text block"
		);
	}


	@Test
	void testMultiLineString_continuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from another \\\"\"\" line and unterminated",
			"continued from another \\\"\"\" line and terminated\"\"\""
		);
	}


	@Test
	void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
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
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"",
			"\"hi\"",
			"\"\\u00fe\"",
			"\"\\\"\""
		);
	}


	@Test
	void testStringLiterals_continuedFromPriorLine_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			DartTokenMaker.INTERNAL_IN_JS_STRING_VALID,
			"continued from prior line and terminated\""
		);
	}


	@Test
	void testStringLiterals_continuedFromPriorLine_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			DartTokenMaker.INTERNAL_IN_JS_STRING_VALID,
			"continued from prior line unterminated"
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
			"\"unterminated string",
			"\"string with an invalid \\x escape in it\"",
			"\"string with invalid \\u09KK unicode escape in it \""
		);
	}


	@Test
	void testStringLiteral_error_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			DartTokenMaker.INTERNAL_IN_JS_STRING_INVALID,
			"finally terminated\"",
			"still unterminated"
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
