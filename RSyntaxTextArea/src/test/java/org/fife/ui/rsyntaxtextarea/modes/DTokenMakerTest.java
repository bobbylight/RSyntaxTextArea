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
 * Unit tests for the {@link DTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new DTokenMaker();
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
	void testAnnotations() {
		assertAllTokensOfType(TokenTypes.ANNOTATION,
			"@",
			"@foo",
			"@foobar123"
		);
	}


	@Test
	void testBackquoteLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			"`Some text`",
			"`Some text unterminated"
		);
	}


	@Test
	void testBackquoteLiterals_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			TokenTypes.LITERAL_BACKQUOTE,
			"Some text`",
			"Some text unterminated"
		);
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
	void testDataTypes() {

		String[] dataTypes = {
			"string", "wstring", "dstring", "size_t", "ptrdiff_t", "bool",
			"byte", "cdouble", "cent", "cfloat", "char", "creal", "dchar",
			"double", "float", "idouble", "ifloat", "ireal", "int", "long",
			"real", "short", "ubyte", "ucent", "uint", "ulong", "ushort",
			"wchar",
		};

		for (String code : dataTypes) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, code));
		}

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
			"continued from a previous line",
			"continued from a previous line */"
		);
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
	void testErrorStringLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"",
			"\"hi",
			"\"\\\"",
			"\"unterminated string"
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

		Assertions.assertEquals(
			TokenTypes.COMMENT_MULTILINE,
			tm.getClosestStandardTokenTypeForInternalType(DTokenMaker.INTERNAL_IN_NESTABLE_MLC));

		// One without a mapping
		Assertions.assertEquals(
			TokenTypes.RESERVED_WORD,
			tm.getClosestStandardTokenTypeForInternalType(TokenTypes.RESERVED_WORD));
	}


	@Test
	void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL " +
				"0x1u 0xfeu 0x333333333333u 0X1u 0Xfeu 0X33333333333u 0xFEu 0XFEu " +
				"0x1U 0xfeU 0x333333333333U 0X1U 0XfeU 0X33333333333U 0xFEU 0XFEU " +
				"0x1lu 0xfelu 0x333333333333lu 0X1lu 0Xfelu 0X33333333333lu 0xFElu 0XFElu " +
				"0x1LU 0xfeLU 0x333333333333LU 0X1LU 0XfeLU 0X33333333333LU 0xFELU 0XFELU " +
				"0x1ul 0xfeul 0x333333333333ul 0X1ul 0Xfeul 0X33333333333ul 0xFEul 0XFEul " +
				"0x1UL 0xfeUL 0x333333333333UL 0X1UL 0XfeUL 0X33333333333UL 0xFEUL 0XFEUL";

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
	void testKeywords() {

		String[] keywords = {

		 	"abstract",
		    "alias",
		    "align",
		    "asm",
		    "assert",
		    "auto",

		    "body",
		    "break",

		    "case",
		    "cast",
		    "catch",
		    "class",
		    "const",
		    "continue",

		    "debug",
		    "default",
		    "delegate",
		    "delete",
		    "deprecated",
		    "do",

		    "else",
		    "enum",
		    "export",
		    "extern",

		    "final",
		    "finally",
		    "for",
		    "foreach",
		    "foreach_reverse",
		    "function",

		    "goto",

		    "if",
		    "immutable",
		    "import",
		    "in",
		    "inout",
		    "interface",
		    "invariant",
		    "is",

		    "lazy",

		    "macro",
		    "mixin",
		    "module",

		    "new",
		    "nothrow",
		    "null",

		    "out",
		    "override",

		    "package",
		    "pragma",
		    "private",
		    "protected",
		    "public",
		    "pure",

		    "ref",

		    "scope",
		    "shared",
		    "static",
		    "struct",
		    "super",
		    "switch",
		    "synchronized",

		    "template",
		    "this",
		    "throw",
		    "try",
		    "typedef",
		    "typeid",
		    "typeof",

		    "union",
		    "unittest",

		    "version",
		    "void",
		    "volatile",

		    "while",
		    "with",

		    "__FILE__",
		    "__MODULE__",
		    "__LINE__",
		    "__FUNCTION__",
		    "__PRETTY_FUNCTION__",

		    "__gshared",
		    "__traits",
		    "__vector",
		    "__parameters"

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
	void testNestableMultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"/+ Hello world +/",
			"/+ 1 deep /+ 2 deep +/ back to 1 deep +/",
			"/+ 1 deep /+ 2 deep /+ 3 deep +/ back to 2 deep +/ back to 1 deep +/"
		);
	}


	@Test
	void testNestableMultiLineComment_fromPriorLine_1deep() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			DTokenMaker.INTERNAL_IN_NESTABLE_MLC,
			"continuing +/",
			"continuing and unterminated"
		);
	}


	@Test
	void testNestableMultiLineComment_fromPriorLine_2deep() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			DTokenMaker.INTERNAL_IN_NESTABLE_MLC - 2,
			"continuing 2-deep +/ continuing 1 deep +/",
			"continuing 2-deep and unterminated",
			"continuing 2-deep +/ continuing 1 deep and unterminated"
		);
	}


	@Test
	void testNestableMultiLineComment_fromPriorLine_3deep() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			DTokenMaker.INTERNAL_IN_NESTABLE_MLC - 3,
			"continuing 3 deep +/ continuing 2 deep +/ continuing 1 deep +/",
			"continuing 3 deep and unterminated",
			"continuing 3 deep +/ continuing 2 deep +/ continuing 1 deep and unterminated"
		);
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

		Assertions.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	void testStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"",
			"\"hi\"",
			"\"\\\"\""
		);
	}


}
