/*
 * 03/23/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link HandlebarsTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class HandlebarsTokenMakerTest extends AbstractJFlexTokenMakerTest {

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as CSS.  This constant is only here so we can
	 * copy and paste tests from this class into others, such as HTML, PHP, and
	 * JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_CSS;

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as part of a CSS multi-line comment.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_MLC_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_CSS_MLC;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a CSS property key/value block.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_PROPERTY_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_CSS_PROPERTY;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a CSS property key/value block.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_VALUE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_CSS_VALUE;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a string property value.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_STRING_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_CSS_STRING;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a char property value.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_CHAR_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_CSS_CHAR;

	private static final int HTML_ATTR_DOUBLE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_ATTR_DOUBLE;

	private static final int HTML_ATTR_SINGLE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_ATTR_SINGLE;

	private static final int HTML_ATTR_SCRIPT_DOUBLE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_ATTR_DOUBLE_QUOTE_SCRIPT;

	private static final int HTML_ATTR_SCRIPT_SINGLE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_ATTR_SINGLE_QUOTE_SCRIPT;

	private static final int HTML_ATTR_STYLE_DOUBLE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_ATTR_DOUBLE_QUOTE_STYLE;

	private static final int HTML_ATTR_STYLE_SINGLE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_ATTR_SINGLE_QUOTE_STYLE;

	private static final int HTML_INTAG_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_INTAG;

	private static final int HTML_INTAG_SCRIPT_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_INTAG_SCRIPT;

	private static final int HTML_INTAG_STYLE_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_INTAG_STYLE;

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as JS.  This constant is only here so we can
	 * copy and paste tests from the JavaScriptTokenMakerTest class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as
	 * possible.
	 */
	private static final int JS_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS;

	private static final int JS_MLC_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS_MLC;

	private static final int JS_INVALID_STRING_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS_STRING_INVALID;

	private static final int JS_VALID_STRING_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS_STRING_VALID;

	private static final int JS_INVALID_CHAR_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS_CHAR_INVALID;

	private static final int JS_VALID_CHAR_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS_CHAR_VALID;

	private static final int JS_INVALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_INVALID;

	private static final int JS_VALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_VALID;

	private static final int HB_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_HB;

	private static final int HB_MLC_1_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_HB_MLC_1;

	private static final int HB_MLC_2_PREV_TOKEN_TYPE = HandlebarsTokenMaker.INTERNAL_IN_HB_MLC_2;

	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	@Override
	protected TokenMaker createTokenMaker() {
		return new HandlebarsTokenMaker();
	}


	@Test
	@Override
	protected void testCommon_getCurlyBracesDenoteCodeBlocks() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertFalse(tm.getCurlyBracesDenoteCodeBlocks(
			HandlebarsTokenMaker.LANG_INDEX_DEFAULT));
		Assertions.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(
			HandlebarsTokenMaker.LANG_INDEX_CSS));
		Assertions.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(
			HandlebarsTokenMaker.LANG_INDEX_JS));
		Assertions.assertFalse(tm.getCurlyBracesDenoteCodeBlocks(
			HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS));
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(
			HandlebarsTokenMaker.LANG_INDEX_DEFAULT);
		Assertions.assertEquals("<!--", startAndEnd[0]);
		Assertions.assertEquals("-->", startAndEnd[1]);
	}


	@Test
	void testCommon_getLineCommentStartAndEnd_css() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(
			HandlebarsTokenMaker.LANG_INDEX_CSS);
		Assertions.assertEquals("/*", startAndEnd[0]);
		Assertions.assertEquals("*/", startAndEnd[1]);
	}


	@Test
	void testCommon_getLineCommentStartAndEnd_js() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(
			HandlebarsTokenMaker.LANG_INDEX_JS);
		Assertions.assertEquals("//", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	void testCommon_getLineCommentStartAndEnd_handlebars() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(
			HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS);
		Assertions.assertEquals("{{!--", startAndEnd[0]);
		Assertions.assertEquals("--}}", startAndEnd[1]);
	}


	@Test
	void testCommon_getSetCloseCompleteTags() {
		HandlebarsTokenMaker tm = (HandlebarsTokenMaker)createTokenMaker();
		Assertions.assertFalse(tm.getCompleteCloseTags());
		try {
			HandlebarsTokenMaker.setCompleteCloseTags(true);
			Assertions.assertTrue(tm.getCompleteCloseTags());
		} finally {
			HandlebarsTokenMaker.setCompleteCloseTags(false);
		}
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {

		TokenMaker tm = createTokenMaker();

		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.MARKUP_TAG_NAME;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	@Override
	protected void testCommon_getShouldIndentNextLineAfter() {
		testCommonHelper_getShouldIndentNextLineAfterCurliesAndParensForLanguageIndex(
			HandlebarsTokenMaker.LANG_INDEX_CSS);
		testCommonHelper_getShouldIndentNextLineAfterCurliesAndParensForLanguageIndex(
			HandlebarsTokenMaker.LANG_INDEX_JS);
	}


	@Test
	void testCommon_isIdentifierChar_css() {
		TokenMaker tm = createTokenMaker();

		// letters
		for (int ch = 'A'; ch <= 'Z'; ch++) {
			Assertions.assertTrue(tm.isIdentifierChar(
				HandlebarsTokenMaker.LANG_INDEX_CSS, (char)ch));
			Assertions.assertTrue(tm.isIdentifierChar(
				HandlebarsTokenMaker.LANG_INDEX_CSS, (char)(ch+('a'-'A'))));
		}

		// some other chars
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_CSS, '-'));
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_CSS, '_'));
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_CSS, '.'));

		// Other stuff isn't identifier chars
		Assertions.assertFalse(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_CSS, '%'));
	}


	@Test
	void testCommon_isIdentifierChar_default() {
		TokenMaker tm = createTokenMaker();

		// letters
		for (int ch = 'A'; ch <= 'Z'; ch++) {
			Assertions.assertTrue(tm.isIdentifierChar(
				HandlebarsTokenMaker.LANG_INDEX_DEFAULT, (char)ch));
			Assertions.assertTrue(tm.isIdentifierChar(
				HandlebarsTokenMaker.LANG_INDEX_DEFAULT, (char)(ch+('a'-'A'))));
		}

		// some other chars
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_DEFAULT, '-'));
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_DEFAULT, '_'));
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_DEFAULT, '.'));

		// Other stuff isn't identifier chars
		Assertions.assertFalse(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_DEFAULT, '%'));
	}


	@Test
	void testCommon_isIdentifierChar_handlebars() {
		TokenMaker tm = createTokenMaker();

		// letters
		for (int ch = 'A'; ch <= 'Z'; ch++) {
			Assertions.assertTrue(tm.isIdentifierChar(
				HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS, (char)ch));
			Assertions.assertTrue(tm.isIdentifierChar(
				HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS, (char)(ch+('a'-'A'))));
		}

		// some other chars
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS, '-'));
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS, '_'));
		Assertions.assertTrue(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS, '.'));

		// Other stuff isn't identifier chars
		Assertions.assertFalse(tm.isIdentifierChar(
			HandlebarsTokenMaker.LANG_INDEX_HANDLEBARS, '%'));
	}


	@Test
	void testCommon_isIdentifierChar_js() {
		TokenMaker tm = createTokenMaker();

		// letters
		for (int ch = 'A'; ch <= 'Z'; ch++) {
			Assertions.assertTrue(tm.isIdentifierChar(HandlebarsTokenMaker.LANG_INDEX_JS, (char)ch));
			Assertions.assertTrue(tm.isIdentifierChar(HandlebarsTokenMaker.LANG_INDEX_JS, (char)(ch+('a'-'A'))));
		}

		// some other chars
		Assertions.assertTrue(tm.isIdentifierChar(HandlebarsTokenMaker.LANG_INDEX_JS, '_'));

		// Other stuff isn't identifier chars
		Assertions.assertFalse(tm.isIdentifierChar(HandlebarsTokenMaker.LANG_INDEX_JS, '%'));
	}


	@Test
	void testCommon_isIdentifierChar_invalidLanguageIndex() {
		TokenMaker tm = createTokenMaker();

		// letters
		for (int ch = 'A'; ch <= 'Z'; ch++) {
			Assertions.assertTrue(tm.isIdentifierChar(99, (char)ch));
			Assertions.assertTrue(tm.isIdentifierChar(99, (char)(ch+('a'-'A'))));
		}

		// some other chars
		Assertions.assertTrue(tm.isIdentifierChar(99, '_'));

		// Other stuff isn't identifier chars
		Assertions.assertFalse(tm.isIdentifierChar(99, '%'));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_false_singleCharThatIsNotParenOrCurly() {
		TokenMaker tm = createTokenMaker();
		Segment code = createSegment("<");
		TokenImpl t = new TokenImpl(code, 0, 0, 0, TokenTypes.OPERATOR,
			HandlebarsTokenMaker.LANG_INDEX_CSS);
		Assertions.assertFalse(tm.getShouldIndentNextLineAfter(t));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_false_tokenMoreThanLength1() {
		TokenMaker tm = createTokenMaker();
		Segment code = createSegment("!=");
		TokenImpl t = new TokenImpl(code, 0, 1, 0, TokenTypes.OPERATOR,
			HandlebarsTokenMaker.LANG_INDEX_CSS);
		Assertions.assertFalse(tm.getShouldIndentNextLineAfter(t));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_null() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertFalse(tm.getShouldIndentNextLineAfter(null));
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
			"and \\'he\\' said so'",
			"continuation from a prior line"
		);
	}


	@Test
	void testCss_happyPath_stateContinuesToNextLine_mainState() {
		TokenMaker tm = createTokenMaker();
		Segment segment = createSegment("");
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);
		Assertions.assertEquals(CSS_PREV_TOKEN_TYPE, token.getType());
	}


	@Test
	void testCss_happyPath_stateContinuesToNextLine_valueState() {
		TokenMaker tm = createTokenMaker();
		Segment segment = createSegment("");
		Token token = tm.getTokenList(segment, CSS_VALUE_PREV_TOKEN_TYPE, 0);
		Assertions.assertEquals(CSS_VALUE_PREV_TOKEN_TYPE, token.getType());
	}


	@Test
	void testCss_happyPath_styleEndTagSwitchesState() {
		Segment segment = createSegment("</style>");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

		Assertions.assertEquals(TokenTypes.MARKUP_TAG_DELIMITER, token.getType());
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.MARKUP_TAG_NAME, token.getType());
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.MARKUP_TAG_DELIMITER, token.getType());
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
		int langIndex = HandlebarsTokenMaker.LANG_INDEX_CSS;

		for (int ch = 'A'; ch <= 'Z'; ch++) {
			Assertions.assertTrue(tm.isIdentifierChar(langIndex, (char)ch));
			Assertions.assertTrue(tm.isIdentifierChar(langIndex, (char)(ch+('a'-'A'))));
		}
		Assertions.assertTrue(tm.isIdentifierChar(langIndex, '-'));
		Assertions.assertTrue(tm.isIdentifierChar(langIndex, '_'));
		Assertions.assertTrue(tm.isIdentifierChar(langIndex, '.'));

		Assertions.assertFalse(tm.isIdentifierChar(0, '!'));
	}


	@Test
	void testCss_lessLineComment_noLess() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			CSS_PREV_TOKEN_TYPE,
			false,
			"//"
		);
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
			" world */",
			"still unterminated"
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
	void testCss_propertyValue_function() {

		String code = "background-image: url(\"test.png\");";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, HandlebarsTokenMaker.INTERNAL_CSS_PROPERTY, 0);

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
		token = tm.getTokenList(segment, HandlebarsTokenMaker.INTERNAL_CSS_PROPERTY, 0);

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
	void testHandlebars_blockEnd_twoCurliesBlock() {

		// Because of the internal state tracking the number of curlies, we need
		// separate setup for each scenario

		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(createSegment("{{}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "}}"));

		t = tm.getTokenList(createSegment("{{}}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "}}"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.IDENTIFIER, "}"));

		t = tm.getTokenList(createSegment("{{}}}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "}}"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.IDENTIFIER, "}}"));
	}


	@Test
	void testHandlebars_blockEnd_threeCurliesBlock() {

		// Because of the internal state tracking the number of curlies, we need
		// separate setup for each scenario

		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(createSegment("{{{}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.IDENTIFIER, "}}"));

		t = tm.getTokenList(createSegment("{{{}}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "}}}"));

		t = tm.getTokenList(createSegment("{{{}}}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "}}}"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.IDENTIFIER, "}"));
	}


	@Test
	void testHandlebars_blockEnd_fourCurliesBlock() {

		// Because of the internal state tracking the number of curlies, we need
		// separate setup for each scenario

		TokenMaker tm = createTokenMaker();

		Token t = tm.getTokenList(createSegment("{{{{}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.IDENTIFIER, "}}"));

		t = tm.getTokenList(createSegment("{{{{}}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.IDENTIFIER, "}}}"));

		t = tm.getTokenList(createSegment("{{{{}}}}"), 0, 0);
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "{{{{"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "}}}}"));
	}


	@Test
	void testHandlebars_blockStart() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"{{",
			"{{{",
			"{{{{"
		);
	}


	@Test
	void testHandlebars_booleanLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			HB_PREV_TOKEN_TYPE,
			"true",
			"false"
		);
	}


	@Test
	void testHandlebars_comments_type1() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"{{! This is a comment }}",
			"{{! This is an unterminated comment",
			"{{!"
		);
	}


	@Test
	void testHandlebars_comments_type1_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			HB_MLC_1_PREV_TOKEN_TYPE,
			"continued from a previous line and unterminated",
			"continued from a previous line}}"
		);
	}


	@Test
	void testHandlebars_comments_type2() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"{{!-- This is a comment --}}",
			"{{!-- This is a comment with }} embedded curlies --}}",
			"{{!-- This is an unterminated comment",
			"{{!--"
		);
	}


	@Test
	void testHandlebars_comments_type2_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			HB_MLC_2_PREV_TOKEN_TYPE,
			"continued from a previous line and unterminated",
			"continued from a previous line and unterminated with }} embedded curlies",
			"continued from a previous line--}}"
		);
	}


	@Test
	void testHandlebars_functions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			HB_PREV_TOKEN_TYPE,
			"#if",
			"#unless",
			"#each",
			"#with",
			"/if",
			"/unless",
			"/each",
			"/with",
			"lookup",
			"log"
		);
	}


	@Test
	void testHandlebars_identifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			HB_PREV_TOKEN_TYPE,
			"foo",
			"snake_case"
		);
	}


	@Test
	void testHandlebars_keywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			HB_PREV_TOKEN_TYPE,
			"else",
			"null",
			"undefined"
		);
	}

	@Test
	void testHandlebars_numericLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_FLOAT,
			HB_PREV_TOKEN_TYPE,
			"0",
			"42",
			"3.987",
			"-42",
			"-3.987"
		);
	}


	@Test
	void testHandlebars_operators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			HB_PREV_TOKEN_TYPE,
			"!",
			"%",
			"&",
			"*",
			"+",
			",",
			".",
			"/",
			";",
			"<",
			"=",
			">",
			"@",
			"`",
			"|",
			"^",
			"~"
		);
	}


	@Test
	void testHandlebars_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			HB_PREV_TOKEN_TYPE,
			"{",
			"}",
			"[",
			"]",
			"(",
			")"
		);
	}


	@Test
	void testHandlebars_strings_doubleQuotes() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			HB_PREV_TOKEN_TYPE,
			"\"This is a string\"",
			"\"This is a \\\"string\\\" with escapes\""
		);
	}


	@Test
	void testHandlebars_strings_doubleQuotes_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			HB_PREV_TOKEN_TYPE,
			"\"Terminated with \\iinvalid escape\"",
			"\"Unterminated string",
			"\"Unterminated with \\iinvalid escape"
		);
	}


	@Test
	void testHandlebars_strings_singleQuotes() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			HB_PREV_TOKEN_TYPE,
			"'This is a string'",
			"'This is a \\'string\\' with escapes'"
		);
	}


	@Test
	void testHandlebars_strings_singleQuotes_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			HB_PREV_TOKEN_TYPE,
			"'Terminated with \\iinvalid escape'",
			"'Unterminated string",
			"'Unterminated with \\iinvalid escape"
		);
	}


	@Test
	void testHtml_attribute_doubleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_PREV_TOKEN_TYPE,
			"\"attribute value\"",
			"\"unclosed attribute value"
		);
	}


	@Test
	void testHtml_attribute_doubleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_DOUBLE_PREV_TOKEN_TYPE,
			"continued from prior line\"",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attribute_singleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HandlebarsTokenMaker.INTERNAL_INTAG,
			"'attribute value'",
			"'unclosed attribute value"
		);
	}


	@Test
	void testHtml_attribute_singleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_SINGLE_PREV_TOKEN_TYPE,
			"continued from prior line'",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeScriptTag_doubleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_SCRIPT_PREV_TOKEN_TYPE,
			"\"attribute value\"",
			"\"unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeScriptTag_doubleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_SCRIPT_DOUBLE_PREV_TOKEN_TYPE,
			"continued from prior line\"",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeScriptTag_singleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HandlebarsTokenMaker.INTERNAL_INTAG_SCRIPT,
			"'attribute value'",
			"'unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeScriptTag_singleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_SCRIPT_SINGLE_PREV_TOKEN_TYPE,
			"continued from prior line'",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeStyleTag_doubleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_STYLE_PREV_TOKEN_TYPE,
			"\"attribute value\"",
			"\"unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeStyleTag_doubleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_STYLE_DOUBLE_PREV_TOKEN_TYPE,
			"continued from prior line\"",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeStyleTag_singleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_STYLE_PREV_TOKEN_TYPE,
			"'attribute value'",
			"'unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeStyleTag_singleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_STYLE_SINGLE_PREV_TOKEN_TYPE,
			"continued from prior line'",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_comment() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			TokenTypes.NULL,
			"<!-- Hello world -->",
			"<!-- unterminated"
		);
	}


	@Test
	void testHtml_comment_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			TokenTypes.MARKUP_COMMENT,
			"continued but unterminated",
			"continued -->"
		);
	}


	@Test
	void testHtml_comment_URL() {

		String[] urls = {
			"file://test.txt",
			"ftp://ftp.google.com",
			"https://www.google.com",
			"https://www.google.com",
			"www.google.com"
		};

		for (String literal : urls) {

			Segment segment = createSegment(literal);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.MARKUP_COMMENT, 0);
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.MARKUP_COMMENT, token.getType());
			Assertions.assertEquals(literal, token.getLexeme());
		}
	}


	@Test
	void testHtml_doctype() {

		String[] doctypes = {
			"<!doctype html>",
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
			"<!doctype unclosed",
		};

		TokenMaker tm = createTokenMaker();
		for (String code : doctypes) {
			Segment segment = createSegment(code);
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_DTD, token.getType());
		}

	}


	@Test
	void testHtml_entityReferences() {

		String[] entityReferences = {
			"&nbsp;", "&lt;", "&gt;", "&#4012",
		};

		for (String code : entityReferences) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_ENTITY_REFERENCE, token.getType());
		}

	}


	@Test
	void testHtml_happyPath_tagWithAttributes() {

		String code = "<body onload=\"doSomething()\" data-extra='true'>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "onload"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"doSomething()\""), "Unexpected token: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "data-extra"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'true'"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));

	}


	@Test
	void testHtml_happyPath_closedTag() {

		String code = "<img src='foo.png'/>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "img"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "src"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'foo.png'"), "Unexpected token: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "/>"));

	}


	@Test
	void testHtml_happyPath_closingTag() {

		String code = "</body>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));

	}


	@Test
	void testHtml_inTag_scriptTag_empty_startsJS() {
		Segment segment = createSegment("<script>for");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "script"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "for"));
	}


	@Test
	void testHtml_inTag_scriptTag_withAttrs_startsJS() {
		Segment segment = createSegment("<script type=\"text/javascript\">for");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "script"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "type"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"text/javascript\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "for"));
	}


	@Test
	void testHtml_inTag_tagName_validHtml5() {
		Segment segment = createSegment("<html");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "html"));
	}


	@Test
	void testHtml_inTag_tagName_unknownTagName() {
		Segment segment = createSegment("<unknown");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "unknown"));
	}


	@Test
	void testHtml_inTag_unterminatedOnThisLine() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, HandlebarsTokenMaker.INTERNAL_INTAG, 0);
		Assertions.assertEquals(HandlebarsTokenMaker.INTERNAL_INTAG, token.getType());
	}


	@Test
	void testHtml_inTagScript_unterminatedOnThisLine() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, HandlebarsTokenMaker.INTERNAL_INTAG_SCRIPT, 0);
		Assertions.assertEquals(HandlebarsTokenMaker.INTERNAL_INTAG_SCRIPT, token.getType());
	}


	@Test
	void testHtml_inTagStyle_unterminatedOnThisLine() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, HandlebarsTokenMaker.INTERNAL_INTAG_STYLE, 0);
		Assertions.assertEquals(HandlebarsTokenMaker.INTERNAL_INTAG_STYLE, token.getType());
	}


	@Test
	void testHtml_processingInstructions() {

		String[] doctypes = {
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
			"<?xml version='1.0' encoding='UTF-8' ?>",
			"<?xml-stylesheet type=\"text/css\" href=\"style.css\"?>",
			"<?xml unterminated",
		};

		for (String code : doctypes) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_PROCESSING_INSTRUCTION, token.getType());
		}

	}


	@Test
	void testHtml_validHtml5TagNames() {

		String[] tagNames = {
			"a", "abbr", "acronym", "address", "applet", "area", "article",
			"aside", "audio", "b", "base", "basefont", "bdo", "bgsound", "big",
			"blink", "blockquote", "body", "br", "button", "canvas", "caption",
			"center", "cite", "code", "col", "colgroup", "command", "comment",
			"dd", "datagrid", "datalist", "datatemplate", "del", "details",
			"dfn", "dialog", "dir", "div", "dl", "dt", "em", "embed",
			"eventsource", "fieldset", "figure", "font", "footer", "form",
			"frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6",
			"head", "header", "hr", "html", "i", "iframe", "ilayer", "img",
			"input", "ins", "isindex", "kbd", "keygen", "label", "layer",
			"legend", "li", "link", "map", "mark", "marquee", "menu", "meta",
			"meter", "multicol", "nav", "nest", "nobr", "noembed", "noframes",
			"nolayer", "noscript", "object", "ol", "optgroup", "option",
			"output", "p", "param", "plaintext", "pre", "progress", "q", "rule",
			"s", "samp", "script", "section", "select", "server", "small",
			"source", "spacer", "span", "strike", "strong", "style",
			"sub", "sup", "table", "tbody", "td", "textarea", "tfoot", "th",
			"thead", "time", "title", "tr", "tt", "u", "ul", "var", "video"
		};

		TokenMaker tm = createTokenMaker();
		for (String tagName : tagNames) {

			for (int i = 0; i < tagName.length(); i++) {

				if (i > 0) {
					tagName = tagName.substring(0, i).toUpperCase() +
							tagName.substring(i);
				}

				String code = "<" + tagName;
				Segment segment = createSegment(code);
				Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
				Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
				token = token.getNextToken();
				Assertions.assertEquals(token.getType(), TokenTypes.MARKUP_TAG_NAME);

			}

		}

	}


	@Test
	void testHtml_loneIdentifier() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"123"
		);
	}


	@Test
	void testJS_BooleanLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			JS_PREV_TOKEN_TYPE,
			"true",
			"false"
		);
	}


	@Test
	void testJS_CharLiterals_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			JS_PREV_TOKEN_TYPE,
			"'\\xG7'", // Invalid hex/octal escape
			"'foo\\ubar'", "'\\u00fg'", // Invalid Unicode escape
			"'My name is \\ubar and I \\", // Continued onto another line
			"'This is unterminated and " // Unterminated string
		);
	}


	@Test
	void testJS_CharLiterals_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			JS_PREV_TOKEN_TYPE,			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\x77'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
			"'My name is Robert and I \\" // Continued onto another line
		);
	}


	@Test
	void testJS_CharLiterals_fromPriorLine_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			JS_INVALID_CHAR_PREV_TOKEN_TYPE,
			"still an invalid char literal",
			"still an invalid char literal even though terminated'"
		);
	}


	@Test
	void testJS_CharLiterals_fromPriorLine_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			JS_VALID_CHAR_PREV_TOKEN_TYPE,
			"still a valid char literal'"
		);
	}


	@Test
	void testJS_DataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			JS_PREV_TOKEN_TYPE,
			"boolean",
			"byte",
			"char",
			"double",
			"float",
			"int",
			"long",
			"short"
		);
	}


	@Test
	void testJS_EolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	void testJS_EolComments_URL() {

		String[] eolCommentLiterals = {
			// Note: The 0-length token at the end of the first example is a
			// minor bug/performance thing
			"// Hello world https://www.sas.com",
			"// Hello world https://www.sas.com extra",
			"// Hello world https://www.sas.com",
			"// Hello world www.sas.com",
			"// Hello world ftp://sas.com",
			"// Hello world file://test.txt",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			// Note: The 0-length token at the end of the first example is a
			// minor bug/performance thing
			if (token != null && token.isPaintable() && token.length() > 0) {
				Assertions.assertFalse(token.isHyperlink());
				Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, " extra"));
			}

		}

	}


	@Test
	void testJS_FloatingPointLiterals() {

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
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] numbers = code.split(" +");
		for (int i = 0; i < numbers.length; i++) {
			Assertions.assertEquals(numbers[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
			if (i < numbers.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(JS_PREV_TOKEN_TYPE, token.getType(), "Wrong type for " + token);

	}


	@Test
	void testJS_Functions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			JS_PREV_TOKEN_TYPE,
			"eval",
			"parseInt",
			"parseFloat",
			"escape",
			"unescape",
			"isNaN",
			"isFinite"
		);
	}


	@Test
	void testJS_HexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
			"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
			"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL ";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] literals = code.split(" +");
		for (int i = 0; i < literals.length; i++) {
			Assertions.assertEquals(literals[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType(), "Not a hex number: " + token);
			if (i < literals.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

	}


	@Test
	void testJS_Identifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			JS_PREV_TOKEN_TYPE,
			"foo",
			"$bar",
			"var1"
		);
	}


	@Test
	void testJS_Identifiers_errors() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			JS_PREV_TOKEN_TYPE,
			"\\"
		);
	}


	@Test
	void testJS_Keywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			JS_PREV_TOKEN_TYPE,
			"async", "await",
			"break", "case", "catch", "class", "const", "continue",
			"debugger", "default", "delete", "do", "else", "export", "extends", "finally", "for", "function", "if",
			"import", "in", "instanceof", "let", "new", "of", "super", "switch",
			"this", "throw", "try", "typeof", "void", "while", "with", "yield",
			"NaN", "Infinity",
			"let" // As of 1.7, which is our default version
		);

		assertAllTokensOfType(TokenTypes.RESERVED_WORD_2,
			JS_PREV_TOKEN_TYPE,
			"return"
		);
	}


	@Test
	void testJS_MultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			JS_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* Hello world unterminated",
			"/**/"
		);
	}


	@Test
	void testJS_MultiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			JS_MLC_PREV_TOKEN_TYPE,
			" this is continued from a prior line */",
			" this is also continued, but not terminated"
		);
	}


	@Test
	void testJS_MultiLineComments_URL() {
		String[] mlcLiterals = {
			"/* Hello world file://test.txt */",
			"/* Hello world ftp://ftp.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world www.google.com */"
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testJS_Numbers() {

		String[] ints = {
			"0", "42", /*"-7",*/
			"0l", "42l",
			"0L", "42L",
		};

		for (String code : ints) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
		}

		String[] floats = {
			"1e17", "3.14159", "5.7e-8", "2f", "2d",
		};

		for (String code : floats) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
		}

		String[] hex = {
			"0x1f", "0X1f", "0x1F", "0X1F",
		};

		for (String code : hex) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
		}

		String[] errors = {
			"42foo", "1e17foo", "0x1ffoo",
		};

		for (String code : errors) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.ERROR_NUMBER_FORMAT, token.getType());
		}

	}


	@Test
	void testJS_Operators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

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

		Assertions.assertEquals(JS_PREV_TOKEN_TYPE, token.getType());

	}


	@Test
	void testJS_Regexes() {
		assertAllTokensOfType(TokenTypes.REGEX,
			JS_PREV_TOKEN_TYPE,
			"/foobar/",
			"/foobar/gim",
			"/foo\\/bar\\/bas/g"
		);
	}


	@Test
	void testJS_Separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			JS_PREV_TOKEN_TYPE,
			"(",
			")",
			"[",
			"]",
			"{",
			"}"
		);
	}


	@Test
	void testJS_Separators_renderedAsIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			JS_PREV_TOKEN_TYPE,
			";",
			",",
			"."
		);
	}


	@Test
	void testJS_StringLiterals_invalid() {

		String[] stringLiterals = {
			"\"\\xG7\"", // Invalid hex/octal escape
			"\"foo\\ubar\"", "\"\\u00fg\"", // Invalid Unicode escape
			"\"My name is \\ubar and I \\", // Continued onto another line
			"\"This is unterminated and ", // Unterminated string
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.ERROR_STRING_DOUBLE, token.getType(), "Not an ERROR_STRING_DOUBLE: " + token);
		}

	}


	@Test
	void testJS_StringLiterals_valid() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\x77\"", "\"\\u00fe\"", "\"\\\"\"",
			"\"My name is Robert and I \\", // String continued on another line
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	void testJS_StringLiteralsFromPreviousLine_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			JS_INVALID_STRING_PREV_TOKEN_TYPE,
			"this is the rest of the string\"",
			"the rest of the string but still unterminated"
		);
	}


	@Test
	void testJS_StringLiteralsFromPreviousLine_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			JS_VALID_STRING_PREV_TOKEN_TYPE,
			"this is the rest of the string\""
		);
	}


	@Test
	void testJS_TemplateLiterals_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			JS_PREV_TOKEN_TYPE,
			"`\\xG7`", // Invalid hex/octal escape
			"`foo\\ubar`", "`\\u00fg`", // Invalid Unicode escape
			"`My name is \\ubar and I ", // Continued onto another line
			"`My name is \\ubar and I \\" // Continued onto another line, with superfluous '\'
		);
	}


	@Test
	void testJS_TemplateLiterals_valid_noInterpolatedExpression() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			JS_PREV_TOKEN_TYPE,
			"``", "`hi`", "`\\x77`", "`\\u00fe`", "`\\\"`",
			"`My name is Robert and I", // String continued on another line
			"`My name is Robert and I \\" // String continued on another line
		);
	}


	@Test
	void testJS_TemplateLiterals_valid_withInterpolatedExpression() {

		// Strings with tokens:  template, interpolated expression, template
		String[] templateLiterals = {
			"`My name is ${name}`",
			"`My name is ${'\"' + name + '\"'}`",
			"`Embedded example: ${2 + ${!!func()}}, wow",
		};

		for (String code : templateLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.VARIABLE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
		}

	}


	@Test
	void testJS_TemplateLiterals_valid_continuedFromPriorLine() {

		String[] templateLiterals = {
			"and my name is ${name}`"
		};

		for (String code : templateLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_VALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE,
				0);
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.VARIABLE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
		}

	}


	@Test
	void testJS_TemplateLiterals_invalid_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			JS_INVALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE,
			"this is still an invalid template literal`");
	}


	@Test
	void testJS_Whitespace() {

		String[] whitespace = {
			" ", "\t", "\f", "   \t   ",
		};

		for (String code : whitespace) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.WHITESPACE, token.getType());
		}

	}


}
