/*
 * 03/12/2015
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
 * Unit tests for the {@link DelphiTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DelphiTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new DelphiTokenMaker();
	}


	@Test
	void testBooleans() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			"true",
			"false"
		);
	}


	@Test
	void testCompilerDirective1() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"{$Hello world }",
			"{$unterminated comment"
		);
	}


	@Test
	void testCompilerDirective1_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			DelphiTokenMaker.INTERNAL_COMPILER_DIRECTIVE,
			"continued from prior line }",
			"continued from prior line and unterminated"
		);
	}


	@Test
	void testCompilerDirective2() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"(*$Hello world *)",
			"(*$unterminated comment"
		);
	}


	@Test
	void testCompilerDirective2_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			DelphiTokenMaker.INTERNAL_COMPILER_DIRECTIVE2,
			"continued from prior line *)",
			"continued from prior line and unterminated"
		);
	}


	@Test
	void testDataTypes() {

		String[] dataTypes = {
			"shortint",
			"byte",
			"char",
			"smallint",
			"integer",
			"word",
			"longint",
			"cardinal",
			"boolean",
			"bytebool",
			"wordbool",
			"longbool",
			"real",
			"single",
			"double",
			"extended",
			"comp",
			"currency",
			"pointer",
		};

		for (String code : dataTypes) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.DATA_TYPE, token.getType());
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
	void testEscapeSequences() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"#123",
			"#1",
			"#9876543210"
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
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("//", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
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
	void testStandardFunctions() {

		String[] functions = {
				"absolute",
				"abstract",
				"assembler",
				"automated",
				"cdecl",
				"contains",
				"default",
				"deprecated",
				"dispid",
				"dynamic",
				"export",
				"external",
				"far",
				"forward",
				"implements",
				"index",
				"library",
				"local",
				"message",
				"name",
				"namespaces",
				"near",
				"nil",
				"nodefault",
				"overload",
				"override",
				"package",
				"pascal",
				"platform",
				"private",
				"protected",
				"public",
				"published",
				"read",
				"readonly",
				"register",
				"reintroduce",
				"requires",
				"resident",
				"safecall",
				"self",
				"stdcall",
				"stored",
				"varargs",
				"virtual",
				"write",
				"writeonly",
		};

		for (String code : functions) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.FUNCTION, token.getType());
		}

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
			"###"
		);
	}


	@Test
	void testIntegers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"44",
			"0"
		);
	}


	@Test
	void testIntegers_error() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			"334#",
			"0x08#"
		);
	}


	@Test
	void testKeywords() {

		String[] words = {
			"array",
			"as",
			"at",
			"asm",
			"begin",
			"case",
			"class",
			"const",
			"constructor",
			"destructor",
			"dispinterface",
			"div",
			"do",
			"downto",
			"else",
			"end",
			"except",
			"exports",
			"file",
			"final",
			"finalization",
			"finally",
			"for",
			"function",
			"goto",
			"if",
			"implementation",
			"in",
			"inherited",
			"initialization",
			"inline",
			"interface",
			"is",
			"label",
			"mod",
			"not",
			"object",
			"of",
			"on",
			"or",
			"out",
			"packed",
			"procedure",
			"program",
			"property",
			"raise",
			"record",
			"repeat",
			"resourcestring",
			"set",
			"sealed",
			"shl",
			"shr",
			"static",
			"string",
			"then",
			"threadvar",
			"to",
			"try",
			"type",
			"unit",
			"unsafe",
			"until",
			"uses",
			"var",
			"while",
			"with",
			"xor",
		};

		for (String code : words) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType(), "Not a keyword: " + token);
		}

	}


	@Test
	void testMultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"{Hello world }",
			"{unterminated comment"
		);
	}


	@Test
	void testMultiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			TokenTypes.COMMENT_MULTILINE,
			"continued from prior line }",
			"continued from prior line and unterminated"
		);
	}


	@Test
	void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"{ Hello world https://www.google.com }",
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
			Assertions.assertEquals(" }", token.getLexeme());

		}

	}


	@Test
	void testMultiLineComments2() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"(*Hello world *)",
			"(*unterminated comment"
		);
	}


	@Test
	void testMultiLineComments2_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			DelphiTokenMaker.INTERNAL_MLC2,
			"continued from prior line *)",
			"continued from prior line and unterminated"
		);
	}


	@Test
	void testOperators() {

		String code = "^ @ : = < > + - / *";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] operators = code.split(" +");
		for (int i = 0; i < operators.length; i++) {
			Assertions.assertEquals(operators[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType());
			if (i < operators.length - 1) {
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

		String code = "( ) [ ]";

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
			"''", "'hi'",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType(), "Not a string: " + token);
		}

	}


	@Test
	void testStringLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"'Unterminated string"
		);
	}
}
