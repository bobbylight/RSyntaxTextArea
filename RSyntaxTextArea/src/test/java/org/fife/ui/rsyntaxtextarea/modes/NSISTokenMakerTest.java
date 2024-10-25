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
 * Unit tests for the {@link NSISTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class NSISTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new NSISTokenMaker();
	}


	@Test
	void testBackquoteLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			"`This is a literal`"
		);
	}


	@Test
	void testBackquoteLiterals_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			TokenTypes.LITERAL_BACKQUOTE,
			"continued from prior line`",
			"continued and still unterminated"
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
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
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
	void testCharLiterals() {

		String[] chars = {
			"''",
			"'hi'",
			"'\"\"'",
			"'$\\'escaped single quote'",
			"'Ending with an escape to continue to the next line \\"
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_CHAR, token.getType(), "Invalid char literal: " + token);
		}

	}


	@Test
	void testCharLiterals_continuedFromPriorLine_closed() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued from prior line'"
		);
	}


	@Test
	void testCharLiterals_continuedFromPriorLine_unclosed() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued and still unterminated"
		);
	}


	@Test
	void testEolComments() {

		String[] eolCommentLiterals = {
			"# Hello world",
			"; Hello world",
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
			"# Hello world https://www.google.com",
			"; Hello world https://www.google.com",
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
	void testErrorNumberFormat() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			"54for",
			"0b10xxx"
		);
	}


	@Test
	void testHexLiterals() {

		String code = "0x1 0xf 0x333333333333 0X1 0Xf 0X33333333333 0xF 0XF " +
				"0x1 0xf 0x333333333333 0X1 0Xf 0X33333333333 0xF 0XF";

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
	void testIntegers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"0",
			"1",
			"123456790"
		);
	}


	@Test
	void testStandardFunctions() {

		String[] functions = {
			"abort",
			"call",
			"clearerrors",
			"goto",
			"ifabort",
			"iferrors",
			"iffileexists",
			"ifrebootflag",
			"intcmp",
			"intcmpu",
			"iswindow",
			"messagebox",
			"reboot",
			"return",
			"quit",
			"seterrors",
			"strcmp",
			"strcmps",
		};

		for (String code : functions) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.FUNCTION, token.getType());
		}

	}


	@Test
	void testKeywords() {

		String code = "function functionend section sectionend subsection subsectionend";

		// Conditional compilation
		code += " !ifdef !ifndef !if !else !endif !macro !macroend";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.RESERVED_WORD, token.getType(), "Not a keyword: " + token);
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
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			TokenTypes.COMMENT_MULTILINE,
			"continued from a previous line */",
			"continued from a previous line unterminated"
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
			"\"\"",
			"\"hi\"",
			"\"$\\\"escaped double quote\"",
			"\"ending with escape to continue to the next line \\"
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType(), "Not a string: " + token);
		}

	}


	@Test
	void testStrings_continuedFromPriorLine_closed() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from prior line\""
		);
	}


	@Test
	void testStrings_continuedFromPriorLine_unterminated() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued and still unterminated"
		);
	}
}
