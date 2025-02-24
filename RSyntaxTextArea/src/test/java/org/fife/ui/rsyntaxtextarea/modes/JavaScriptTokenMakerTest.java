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
import org.junit.jupiter.api.*;


/**
 * Unit tests for the {@link JavaScriptTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class JavaScriptTokenMakerTest extends AbstractCDerivedTokenMakerTest {

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as JS.  This constant is only here so we can
	 * copy and paste tests from the JavaScriptTokenMakerTest class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as
	 * possible.
	 */
	private static final int JS_PREV_TOKEN_TYPE = TokenTypes.NULL;

	private static final int JS_DOC_COMMENT_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_COMMENT_DOCUMENTATION;

	private static final int JS_MLC_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_MLC;

	private static final int JS_INVALID_STRING_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_STRING_INVALID;

	private static final int JS_VALID_STRING_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_STRING_VALID;

	private static final int JS_INVALID_CHAR_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_CHAR_INVALID;

	private static final int JS_VALID_CHAR_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_CHAR_VALID;

	private static final int JS_INVALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_INVALID;

	private static final int JS_VALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_VALID;

	private static final int JS_E4X_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X;

	private static final int JS_E4X_INTAG_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_INTAG;

	private static final int JS_E4X_PI_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_MARKUP_PROCESSING_INSTRUCTION;

	private static final int JS_E4X_DTD_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_DTD;

	private static final int JS_E4X_INTERNAL_DTD_PREV_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_DTD_INTERNAL;

	private static final int JS_E4X_ATTR_SINGLE_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_ATTR_SINGLE;

	private static final int JS_E4X_ATTR_DOUBLE_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_ATTR_DOUBLE;

	private static final int JS_E4X_CDATA_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_MARKUP_CDATA;

	private static final int JS_E4X_COMMENT_TOKEN_TYPE = JavaScriptTokenMaker.INTERNAL_E4X_COMMENT;

	@BeforeEach
	void setUp() {
		JavaScriptTokenMaker.setE4xSupported(false);
		JavaScriptTokenMaker.setJavaScriptVersion("1.7");
	}


	@AfterEach
	void tearDown() {
		JavaScriptTokenMaker.setE4xSupported(false);
		JavaScriptTokenMaker.setJavaScriptVersion("1.7");
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new JavaScriptTokenMaker();
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
	void testCommon_getClosestStandardTokenTypeForInternalType() {

		TokenMaker tm = createTokenMaker();

		Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_MLC));
		Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_COMMENT_DOCUMENTATION));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_STRING_INVALID));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_STRING_VALID));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_CHAR_INVALID));
		Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_CHAR_VALID));

		Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_VALID));
		Assertions.assertEquals(TokenTypes.ERROR_STRING_DOUBLE,
			tm.getClosestStandardTokenTypeForInternalType(JavaScriptTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_INVALID));

		Assertions.assertEquals(TokenTypes.IDENTIFIER,
			tm.getClosestStandardTokenTypeForInternalType(TokenTypes.IDENTIFIER));
	}


	@Test
	void testJS_api_getJavaScriptVersion() {
		Assertions.assertEquals("1.7", JavaScriptTokenMaker.getJavaScriptVersion());
		JavaScriptTokenMaker.setJavaScriptVersion("1.6");
		Assertions.assertEquals("1.6", JavaScriptTokenMaker.getJavaScriptVersion());
	}


	@Test
	void testJS_api_getLineCommentStartAndEnd() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertEquals("//", tm.getLineCommentStartAndEnd(0)[0]);
		Assertions.assertNull(tm.getLineCommentStartAndEnd(0)[1]);
	}


	@Test
	void testJS_api_isE4XSupported() {
		Assertions.assertFalse(JavaScriptTokenMaker.isE4xSupported());
		JavaScriptTokenMaker.setE4xSupported(true);
		Assertions.assertTrue(JavaScriptTokenMaker.isE4xSupported());
	}


	@Test
	void testJS_api_setJavaScriptVersion() {
		Assertions.assertEquals("1.7", JavaScriptTokenMaker.getJavaScriptVersion());
		JavaScriptTokenMaker.setJavaScriptVersion("1.6");
		Assertions.assertEquals("1.6", JavaScriptTokenMaker.getJavaScriptVersion());
	}


	@Test
	void testJS_api_setE4XSupported() {
		Assertions.assertFalse(JavaScriptTokenMaker.isE4xSupported());
		JavaScriptTokenMaker.setE4xSupported(true);
		Assertions.assertTrue(JavaScriptTokenMaker.isE4xSupported());
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
	void testJS_DocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			JS_PREV_TOKEN_TYPE,
			"/** Hello world */"
		);
	}


	@Test
	void testJS_DocComments_BlockTags() {

		String[] blockTags = {
			"abstract", "access", "alias", "augments", "author", "borrows",
			"callback", "classdesc", "constant", "constructor", "constructs",
			"copyright", "default", "deprecated", "desc", "enum", "event",
			"example", "exports", "external", "file", "fires", "global",
			"ignore", "inner", "instance", "kind", "lends", "license",
			"link", "member", "memberof", "method", "mixes", "mixin", "module",
			"name", "namespace", "param", "private", "property", "protected",
			"public", "readonly", "requires", "return", "returns", "see", "since",
			"static", "summary", "this", "throws", "todo",
			"type", "typedef", "variation", "version"
		};

		for (String blockTag : blockTags) {
			blockTag = "@" + blockTag;
			Segment segment = createSegment(blockTag);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_DOC_COMMENT_PREV_TOKEN_TYPE, 0);
			// Can sometimes produce empty tokens, if e.g. @foo is first token
			// on a line. We could technically make that better, but it is not
			// the common case
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_KEYWORD, token.getType(), "Invalid block tag: " + blockTag);
		}

	}


	@Test
	@Disabled("Fails because we create a (possibly) 0-length token before this - yuck!")
	void testJS_DocComments_InlineTags() {
		assertAllTokensOfType(TokenTypes.COMMENT_KEYWORD,
			JS_DOC_COMMENT_PREV_TOKEN_TYPE,
			"@link",
			"@linkplain",
			"@linkcode",
			"@tutorial"
		);
	}


	@Test
	void testJS_DocComments_Markup() {
		String text = "<code>";
		Segment segment = createSegment(text);
		TokenMaker tm = createTokenMaker();
		final int internalInJsCommentDocumentation = -9;
		Token token = tm.getTokenList(segment, internalInJsCommentDocumentation, 0);
		// Can sometimes produce empty tokens, if e.g. @foo is first token
		// on a line. We could technically make that better, but it is not
		// the common case
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_MARKUP, "<code>"));
	}


	@Test
	void testJS_DocComments_URL() {

		String[] docCommentLiterals = {
			"/** Hello world https://www.sas.com */",
			"/** Hello world https://www.sas.com */",
			"/** Hello world www.sas.com */",
			"/** Hello world ftp://sas.com */",
			"/** Hello world file://test.txt */",
		};

		for (String code : docCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testDocComments_URL_onlyUrlRegionIsHyperlinked() {

		String text = "The URL https://www.google.com is the place";
		Segment segment = createSegment(text);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, JS_DOC_COMMENT_PREV_TOKEN_TYPE, 0);
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
	void testJS_e4x() {

		JavaScriptTokenMaker.setE4xSupported(true);

		// Simple XML
		String e4x = "var foo = <one attr1=\"yes\" attr2='no'>foobar</one>;";
		Segment seg = createSegment(e4x);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "one"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "attr1"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"yes\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "attr2"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'no'"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foobar"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "one"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// Comment
		e4x = "var foo = <!-- Hello world -->;";
		seg = createSegment(e4x);
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- Hello world -->"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// Comment with URL
		e4x = "var foo = <!-- https://www.google.com -->;";
		seg = createSegment(e4x);
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- "));
		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "https://www.google.com"));
		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, " -->"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// CDATA
		e4x = "var foo = <![CDATA[foo]]>;";
		seg = createSegment(e4x);
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA_DELIMITER, "<![CDATA["));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA_DELIMITER, "]]>"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// DTD
		e4x = "var foo = <!doctype FOO>;";
		seg = createSegment(e4x);
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_DTD, "<!doctype FOO>"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// DTD containing a comment
		e4x = "var foo = <!doctype FOO <!-- foo -->>;";
		seg = createSegment(e4x);
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_DTD, "<!doctype FOO "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- foo -->"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_DTD, ">"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// Processing instruction
		e4x = "var foo = <?xml version=\"1.0\"?>;";
		seg = createSegment(e4x);
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_PROCESSING_INSTRUCTION, "<?xml version=\"1.0\"?>"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// "each" keyword, valid when e4x is enabled
		seg = createSegment("each");
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "each"));

		// e4x attribute
		String attr = "@foo";
		seg = createSegment(attr);
		tm = createTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, attr));

	}


	@Test
	void testJS_e4x_entityReference() {
		JavaScriptTokenMaker.setE4xSupported(true);
		assertAllTokensOfType(TokenTypes.MARKUP_ENTITY_REFERENCE,
			JS_E4X_PREV_TOKEN_TYPE,
			"&#42;"
		);
	}


	@Test
	void testJS_e4x_priorLine() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			JS_E4X_PREV_TOKEN_TYPE,
			"foo");
	}


	@Test
	void testJS_e4x_priorLine_inComment() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			JavaScriptTokenMaker.INTERNAL_E4X_COMMENT - JavaScriptTokenMaker.E4X,
			"foo",
			"foo -->"
		);
	}


	@Test
	void testJS_e4x_priorLine_intag() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE,
			JS_E4X_INTAG_PREV_TOKEN_TYPE,
			"foo");
	}


	@Test
	void testJS_e4x_priorLine_inPI() {
		assertAllTokensOfType(TokenTypes.MARKUP_PROCESSING_INSTRUCTION,
			JS_E4X_PI_PREV_TOKEN_TYPE,
			"foo",
			"?",
			"?>"
			);
	}


	@Test
	void testJS_e4x_priorLine_inDTD() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			JS_E4X_DTD_PREV_TOKEN_TYPE,
			"foo");
	}


	@Test
	void testJS_e4x_priorLine_inInternalDTD() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			JS_E4X_INTERNAL_DTD_PREV_TOKEN_TYPE,
			"foo");
	}


	@Test
	void testJS_e4x_priorLine_inAttrDouble() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			JS_E4X_ATTR_DOUBLE_TOKEN_TYPE,
			"foo",
			"foo\"");
	}


	@Test
	void testJS_e4x_priorLine_inCDATA() {
		assertAllTokensOfType(TokenTypes.MARKUP_CDATA,
			JS_E4X_CDATA_TOKEN_TYPE,
			"foo"
		);
	}


	@Test
	void testJS_e4x_priorLine_inAttrSingle() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			JS_E4X_ATTR_SINGLE_TOKEN_TYPE,
			"foo",
			"foo'");
	}


	@Test
	void testJS_e4x_processingInstruction() {
		assertAllTokensOfType(TokenTypes.MARKUP_PROCESSING_INSTRUCTION,
			JS_E4X_PREV_TOKEN_TYPE,
			"<?"
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
			"// Hello world https://www.google.com",
			"// Hello world https://www.google.com extra",
			"// Hello world http://www.google.com",
			"// Hello world www.google.com",
			"// Hello world ftp://google.com",
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

		Assertions.assertEquals(JS_PREV_TOKEN_TYPE, token.getType());

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
	void testJS_regex() {
		assertAllTokensOfType(TokenTypes.REGEX,
			JS_PREV_TOKEN_TYPE,
			"/foobar/",
			"/foobar/gim",
			"/foo\\/bar\\/bas/g"
		);
	}


	@Test
	void testJS_regex_followingCertainOperators() {
		assertAllSecondTokensAreRegexes(
			"=/foo/",
			"(/foo/",
			",/foo/",
			"?/foo/",
			":/foo/",
			"[/foo/",
			"!/foo/",
			"&/foo/",
			"=/foo/",
			"==/foo/",
			"!=/foo/",
			"<<=/foo/",
			">>=/foo/"
		);
	}


	@Test
	void testJS_regex_notWhenFollowingCertainTokens() {
		assertAllSecondTokensAreNotRegexes(
			"^/foo/",
			">>/foo/",
			"<</foo/",
			"--/foo/",
			"4/foo/"
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
			"`My name is \\ubar and I " // Continued onto another line
		);
	}


	@Test
	void testJS_TemplateLiterals_invalid_unclosedExpression() {

		String code = "`Hello ${unclosedName";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`Hello "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${unclosedName"));
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
