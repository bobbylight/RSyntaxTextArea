/*
 * 07/18/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link CSSTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class CSSTokenMakerTest extends AbstractCDerivedTokenMakerTest {

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as CSS.  This constant is only here so we can
	 * copy and paste tests from this class into others, such as HTML, PHP, and
	 * JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_PREV_TOKEN_TYPE = TokenTypes.NULL;

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as part of a CSS multi-line comment.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_MLC_PREV_TOKEN_TYPE = CSSTokenMaker.INTERNAL_CSS_MLC;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a CSS property key/value block.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_PROPERTY_PREV_TOKEN_TYPE = CSSTokenMaker.INTERNAL_CSS_PROPERTY;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a CSS property key/value block.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_VALUE_PREV_TOKEN_TYPE = CSSTokenMaker.INTERNAL_CSS_VALUE;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a string property value.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_STRING_PREV_TOKEN_TYPE = CSSTokenMaker.INTERNAL_CSS_STRING;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a char property value.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_CHAR_PREV_TOKEN_TYPE = CSSTokenMaker.INTERNAL_CSS_CHAR;


	/**
	 * Verifies that all tokens in an array have a specific token type with "less"
	 * syntax highlighting enabled.
	 *
	 * @param expectedType The expected token type.
	 * @param initialTokenType The initial token type.
	 * @param tokens The tokens.
	 */
	private void assertAllTokensOfTypeLessMode(int expectedType, int initialTokenType,
										 String... tokens) {

		for (String token : tokens) {
			Segment segment = createSegment(token);
			CSSTokenMaker tm = (CSSTokenMaker)createTokenMaker();
			tm.setHighlightingLess(true);
			Token t = tm.getTokenList(segment, initialTokenType, 0);
			Assertions.assertEquals(expectedType, t.getType(),
				"Token has unexpected type: orig=" + token + ", actual=" + t);
		}

	}


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	@Override
	protected TokenMaker createTokenMaker() {
		return new CSSTokenMaker();
	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		testCss_getLineCommentStartAndEnd();
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.RESERVED_WORD;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testCss_atRule() {
		assertAllTokensOfType(TokenTypes.REGEX,
			CSS_PREV_TOKEN_TYPE,
			"@charset",
			"@import",
			"@namespace",
			"@media",
			"@document",
			"@page",
			"@font-face",
			"@keyframes",
			"@viewport"
		);
	}


	@Test
	void testCss_chars() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			CSS_PREV_TOKEN_TYPE,
			"'Hello world'",
			"'Hello \\'world\\''"
		);
	}


	@Test
	void testCss_char_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			CSS_CHAR_PREV_TOKEN_TYPE,
			"world'",
			"and \\'he\\' said so'"
		);
	}


	@Test
	protected void testCss_getLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("/*", startAndEnd[0]);
		Assertions.assertEquals("*/", startAndEnd[1]);
	}


	@Test
	void testCss_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertTrue(tm.getMarkOccurrencesOfTokenType(TokenTypes.RESERVED_WORD));
		Assertions.assertFalse(tm.getMarkOccurrencesOfTokenType(TokenTypes.VARIABLE));
	}


	@Test
	void testCss_getShouldIndentNextLineAfter() {

		TokenMaker tm = createTokenMaker();

		Assertions.assertFalse(tm.getShouldIndentNextLineAfter(null));
		Assertions.assertFalse(tm.getShouldIndentNextLineAfter(new TokenImpl()));

		char[] ch = { '{' };
		TokenImpl openCurly = new TokenImpl(ch, 0, 0, 0, TokenTypes.SEPARATOR, 0);
		Assertions.assertTrue(tm.getShouldIndentNextLineAfter(openCurly));

		ch = new char[] { '(' };
		TokenImpl openParen = new TokenImpl(ch, 0, 0, 0, TokenTypes.SEPARATOR, 0);
		Assertions.assertTrue(tm.getShouldIndentNextLineAfter(openParen));

		ch = new char[] { 'x' };
		TokenImpl other = new TokenImpl(ch, 0, 0, 0, TokenTypes.SEPARATOR, 0);
		Assertions.assertFalse(tm.getShouldIndentNextLineAfter(other));
	}


	@Test
	void testCss_happyPath_simpleSelector() {

		String code = "body { padding: 0; }";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "body"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "{"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "padding"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ":"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "0"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ";"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "}"));

	}


	@Test
	void testCss_id() {

		String code = "#mainContent";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "#mainContent"));

	}

	@Test
	void testCss_isIdentifierChar() {
		TokenMaker tm = createTokenMaker();
		for (int ch = 'A'; ch <= 'Z'; ch++) {
			Assertions.assertTrue(tm.isIdentifierChar(0, (char)ch));
			Assertions.assertTrue(tm.isIdentifierChar(0, (char)(ch+('a'-'A'))));
		}
		Assertions.assertTrue(tm.isIdentifierChar(0, '-'));
		Assertions.assertTrue(tm.isIdentifierChar(0, '_'));
		Assertions.assertTrue(tm.isIdentifierChar(0, '.'));

		Assertions.assertFalse(tm.isIdentifierChar(0, '!'));
	}


	@Test
	void testCss_lessLineComment_noLess() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			CSS_PREV_TOKEN_TYPE,
			"//"
		);
	}


	@Test
	void testCss_lessLineComment_lessEnabled() {
		assertAllTokensOfTypeLessMode(TokenTypes.COMMENT_EOL,
			CSS_PREV_TOKEN_TYPE,
			"//",
			"// This is a comment"
		);
	}


	@Test
	void testCss_lessLineComment_lessEnabled_url() {


		String[] eolCommentLiterals = {
			"// Hello world file://test.txt",
			"// Hello world ftp://ftp.google.com",
			"// Hello world https://www.google.com",
			"// Hello world https://www.google.com",
			"// Hello world www.google.com"
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			CSSTokenMaker tm = (CSSTokenMaker)createTokenMaker();
			tm.setHighlightingLess(true);

				Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

		}
	}


	@Test
	void testCss_lessVar_noLess() {
		assertAllTokensOfType(TokenTypes.REGEX,
			CSS_PREV_TOKEN_TYPE,
			"@something",
			"@something-else"
		);
	}


	@Test
	void testCss_lessVar_lessEnabled() {
		assertAllTokensOfTypeLessMode(TokenTypes.VARIABLE,
			CSS_PREV_TOKEN_TYPE,
			"@something",
			"@something-else"
		);
	}


	@Test
	void testCss_multiLineComment() {

		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE, CSS_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* unterminated",
			"/**/"
		);
	}


	@Test
	void testCss_multiLineComment_continuedFromPreviousLine() {

		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE, CSS_MLC_PREV_TOKEN_TYPE,
			" world */"
		);
	}


	@Test
	void testCss_multiLineComment_URL() {

		String[] comments = {
			"/* Hello world file://test.txt */",
			"/* Hello world ftp://ftp.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world www.google.com */"
		};

		for (String comment : comments) {

			Segment segment = createSegment(comment);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

			Assertions.assertFalse(token.isHyperlink());
			Assertions.assertTrue(token.is(TokenTypes.COMMENT_MULTILINE, "/* Hello world "));

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());

			token = token.getNextToken();
			Assertions.assertFalse(token.isHyperlink());
			Assertions.assertTrue(token.is(TokenTypes.COMMENT_MULTILINE, " */"));
		}
	}


	@Test
	void testCss_operators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			CSS_PREV_TOKEN_TYPE,
			"+",
			">",
			"~",
			"^",
			"$",
			"|",
			"="
		);
	}


	@Test
	void testCss_propertyBlock_property_multiLineComment() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* unterminated",
			"/**/"
		);
	}


	@Test
	void testCss_propertyBlock_property_properties() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			"*foo",
			"foo",
			"_",
			"*_",
			"foo9"
		);
	}


	@Test
	void testCss_propertyBlock_property_less_lineComment() {
		assertAllTokensOfTypeLessMode(TokenTypes.COMMENT_EOL,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			"// Hello world"
		);
	}


	@Test
	void testCss_propertyBlock_property_lessLineComment_lessEnabled_url() {


		String[] eolCommentLiterals = {
			"// Hello world file://test.txt",
			"// Hello world ftp://ftp.google.com",
			"// Hello world https://www.google.com",
			"// Hello world https://www.google.com",
			"// Hello world www.google.com"
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			CSSTokenMaker tm = (CSSTokenMaker)createTokenMaker();
			tm.setHighlightingLess(true);

			Token token = tm.getTokenList(segment, CSS_PROPERTY_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

		}
	}


	@Test
	void testCss_propertyBlock_property_operators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			":"
		);
	}


	@Test
	void testCss_propertyBlock_property_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			"{",
			"}"
		);
	}


	@Test
	void testCss_propertyBlock_value_charLiteral() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"'foobar'"
		);
	}


	@Test
	void testCss_propertyBlock_value_function() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			CSS_VALUE_PREV_TOKEN_TYPE,
			false,
			"func("
		);
	}


	@Test
	void testCss_propertyBlock_value_identifier() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"foobar",
			",",
			"."
		);
	}


	@Test
	void testCss_propertyBlock_value_important() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"!important"
		);
	}


	@Test
	void testCss_propertyBlock_value_multiLineComment() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* unterminated",
			"/**/"
		);
	}


	@Test
	void testCss_propertyBlock_value_number() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"42",
			"42.",
			"42.3",
			"-42",
			"-42.",
			"-42.3",
			"4pt",
			"4pc",
			"4in",
			"4mm",
			"4cm",
			"4em",
			"4ex",
			"4px",
			"4ms",
			"4s",
			"4%",
			"#0",
			"#0A",
			"#0a",
			"#ff00ff"
		);
	}


	@Test
	void testCss_propertyBlock_value_less_lineComment() {
		assertAllTokensOfTypeLessMode(TokenTypes.COMMENT_EOL,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"// Hello world"
		);
	}


	@Test
	void testCss_propertyBlock_value_lessLineComment_lessEnabled_url() {


		String[] eolCommentLiterals = {
			"// Hello world file://test.txt",
			"// Hello world ftp://ftp.google.com",
			"// Hello world https://www.google.com",
			"// Hello world https://www.google.com",
			"// Hello world www.google.com"
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			CSSTokenMaker tm = (CSSTokenMaker)createTokenMaker();
			tm.setHighlightingLess(true);

			Token token = tm.getTokenList(segment, CSS_VALUE_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

		}
	}


	@Test
	void testCss_propertyBlock_value_less_lessVar() {
		assertAllTokensOfTypeLessMode(TokenTypes.VARIABLE,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"@foo",
			"@foo-bar"
		);
	}


	@Test
	void testCss_propertyBlock_value_operators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			CSS_VALUE_PREV_TOKEN_TYPE,
			";"
		);
	}


	@Test
	void testCss_propertyBlock_value_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			CSS_VALUE_PREV_TOKEN_TYPE,
			")",
			"}"
		);
	}


	@Test
	void testCss_propertyBlock_value_string() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"\"foobar\""
		);
	}


	@Test
	void testCss_property_lessNestedSelectorWithPseudo_less() {

		String[] tokens = {
			"foo:root",
			"foo:nth-child",
			"foo:nth-last-child",
			"foo:nth-of-type",
			"foo:nth-last-of-type",
			"foo:before",
			"foo:after",
			"foo:not",
		};
		CSSTokenMaker tm = (CSSTokenMaker)createTokenMaker();
		tm.setHighlightingLess(true);

		for (String code: tokens) {
			Segment segment = createSegment(code);
			Token t = tm.getTokenList(segment, CSSTokenMaker.INTERNAL_CSS_PROPERTY, 0);
			Assertions.assertEquals(TokenTypes.RESERVED_WORD, t.getType(),
				"Token not a reserved word: " + t);
		}
	}


	@Test
	void testCss_propertyValue_function() {

		String code = "background-image: url(\"test.png\");";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, CSSTokenMaker.INTERNAL_CSS_PROPERTY, 0);

		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "background-image"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ":"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.FUNCTION, "url"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "("));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"test.png\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, ")"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ";"));

		code = "background-image: url('test.png');";
		segment = createSegment(code);
		tm = createTokenMaker();
		token = tm.getTokenList(segment, CSSTokenMaker.INTERNAL_CSS_PROPERTY, 0);

		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "background-image"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ":"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.FUNCTION, "url"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "("));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'test.png'"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, ")"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ";"));

	}


	@Test
	void testCss_pseudoClass() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			CSS_PREV_TOKEN_TYPE,
			":root",
			":nth-child",
			":nth-last-child",
			":nth-of-type",
			":nth-last-of-type",
			":first-child",
			":last-child",
			":first-of-type",
			":last-of-type",
			":only-child",
			":only-of-type",
			":empty",
			":link",
			":visited",
			":active",
			":hover",
			":focus",
			":target",
			":lang",
			":enabled",
			":disabled",
			":checked",
			"::first-line",
			"::first-letter",
			"::before",
			"::after",
			":not"
		);
	}


	@Test
	void testCss_pseudoClass_unknown() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			CSS_PREV_TOKEN_TYPE,
			":"
		);
	}


	@Test
	void testCss_selectors() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			CSS_PREV_TOKEN_TYPE,
			"*",
			".",
			".foo",
			".foo-bar",
			"foo",
			"-foo-bar",
			"foo-bar");
	}


	@Test
	void testCss_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			CSS_PREV_TOKEN_TYPE,
			";",
			"(",
			")",
			"[",
			"]"
		);
	}


	@Test
	void testCss_strings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			CSS_PREV_TOKEN_TYPE,
			"\"Hello world\"",
			"\"Hello \\\"world\\\"",
			"\"Unterminated string"
		);
	}


	@Test
	void testCss_string_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			CSS_STRING_PREV_TOKEN_TYPE,
			"world\"",
			"and \\\"he\\\" said so\""
		);
	}


	@Test
	void testGetClosestStandardTokenTypeForInternalType() {

		TokenMaker tm = createTokenMaker();

		Assertions.assertEquals(
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(CSSTokenMaker.INTERNAL_CSS_STRING));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(CSSTokenMaker.INTERNAL_CSS_CHAR));
		Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE,
			tm.getClosestStandardTokenTypeForInternalType(CSSTokenMaker.INTERNAL_CSS_MLC));

		Assertions.assertEquals(
			TokenTypes.IDENTIFIER,
			tm.getClosestStandardTokenTypeForInternalType(TokenTypes.IDENTIFIER));
	}
}
