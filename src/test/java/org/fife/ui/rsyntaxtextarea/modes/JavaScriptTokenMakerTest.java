/*
 * 03/12/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Unit tests for the {@link JavaScriptTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JavaScriptTokenMakerTest extends AbstractTokenMakerTest {

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as JS.  This constant is only here so we can
	 * copy and paste tests from this class into others, such as HTML, PHP, and
	 * JSP token maker tests, with as little change as possible.
	 */
	private static final int JS_PREV_TOKEN_TYPE = TokenTypes.NULL;


	@Before
	public void setUp() {
		JavaScriptTokenMaker.setE4xSupported(false);
		JavaScriptTokenMaker.setJavaScriptVersion("1.7");
	}


	@After
	public void tearDown() {
		JavaScriptTokenMaker.setE4xSupported(false);
		JavaScriptTokenMaker.setJavaScriptVersion("1.7");
	}


	@Test
	@Ignore("Not yet implemented")
	public void testJS_api_getClosestStandardTokenTypeForInternalType() {
		// TODO
	}


	@Test
	public void testJS_api_getJavaScriptVersion() {
		Assert.assertEquals("1.7", JavaScriptTokenMaker.getJavaScriptVersion());
		JavaScriptTokenMaker.setJavaScriptVersion("1.6");
		Assert.assertEquals("1.6", JavaScriptTokenMaker.getJavaScriptVersion());
	}


	@Test
	public void testJS_api_getLineCommentStartAndEnd() {
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Assert.assertEquals("//", tm.getLineCommentStartAndEnd(0)[0]);
		Assert.assertNull(tm.getLineCommentStartAndEnd(0)[1]);
	}


	@Test
	public void testJS_api_isE4XSupported() {
		Assert.assertFalse(JavaScriptTokenMaker.isE4xSupported());
		JavaScriptTokenMaker.setE4xSupported(true);
		Assert.assertTrue(JavaScriptTokenMaker.isE4xSupported());
	}


	@Test
	public void testJS_api_setJavaScriptVersion() {
		Assert.assertEquals("1.7", JavaScriptTokenMaker.getJavaScriptVersion());
		JavaScriptTokenMaker.setJavaScriptVersion("1.6");
		Assert.assertEquals("1.6", JavaScriptTokenMaker.getJavaScriptVersion());
	}


	@Test
	public void testJS_api_setE4XSupported() {
		Assert.assertFalse(JavaScriptTokenMaker.isE4xSupported());
		JavaScriptTokenMaker.setE4xSupported(true);
		Assert.assertTrue(JavaScriptTokenMaker.isE4xSupported());
	}


	@Test
	public void testJS_BooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testJS_CharLiterals_invalid() {

		String[] charLiterals = {
			"'\\xG7'", // Invalid hex/octal escape
			"'foo\\ubar'", "'\\u00fg'", // Invalid Unicode escape
			"'My name is \\ubar and I \\", // Continued onto another line
			"'This is unterminated and ", // Unterminated string
		};

		for (String code : charLiterals) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.ERROR_CHAR, token.getType());
		}

	}


	@Test
	public void testJS_CharLiterals_valid() {

		String[] charLiterals = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\x77'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
			"'My name is Robert and I \\", // Continued onto another line
		};

		for (String code : charLiterals) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	public void testJS_DataTypes() {

		String code = "boolean byte char double float int long short";

		Segment segment = createSegment(code);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.DATA_TYPE, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testJS_DocComments() {

		String[] docCommentLiterals = {
			"/** Hello world */",
		};

		for (String code : docCommentLiterals) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
		}

	}


	@Test
	public void testJS_DocComments_BlockTags() {

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
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			final int INTERNAL_IN_JS_COMMENT_DOCUMENTATION = -9;
			Token token = tm.getTokenList(segment, INTERNAL_IN_JS_COMMENT_DOCUMENTATION, 0);
			// Can sometimes produce empty tokens, if e.g. @foo is first token
			// on a line. We could technically make that better, but it is not
			// the common case
			token = token.getNextToken();
			Assert.assertEquals("Invalid block tag: " + blockTag,
					TokenTypes.COMMENT_KEYWORD, token.getType());
		}

	}


	@Test
	public void testJS_DocComments_InlineTags() {

		String[] inlineTags = { "link", "linkplain", "linkcode", "tutorial" };

		for (String inlineTag : inlineTags) {
			inlineTag = "{@" + inlineTag + "}";
			Segment segment = createSegment(inlineTag);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			final int INTERNAL_IN_JS_COMMENT_DOCUMENTATION = -9;
			Token token = tm.getTokenList(segment, INTERNAL_IN_JS_COMMENT_DOCUMENTATION, 0);
			//System.out.println("--- " + token + ", " + token.length());
			// Can sometimes produce empty tokens, if e.g. {@foo} is first token
			// on a line. We could technically make that better, but it is not
			// the common case
			token = token.getNextToken();
			Assert.assertEquals("Invalid inline tag: " + inlineTag,
					TokenTypes.COMMENT_KEYWORD, token.getType());
		}

	}


	@Test
	public void testJS_DocComments_Markup() {
		String text = "<code>";
		Segment segment = createSegment(text);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		final int INTERNAL_IN_JS_COMMENT_DOCUMENTATION = -9;
		Token token = tm.getTokenList(segment, INTERNAL_IN_JS_COMMENT_DOCUMENTATION, 0);
		// Can sometimes produce empty tokens, if e.g. @foo is first token
		// on a line. We could technically make that better, but it is not
		// the common case
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.COMMENT_MARKUP, "<code>"));
	}


	@Test
	public void testJS_DocComments_URL() {

		String[] docCommentLiterals = {
			"/** Hello world http://www.sas.com */",
		};

		for (String code : docCommentLiterals) {

			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assert.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	public void testJS_e4x() {

		JavaScriptTokenMaker.setE4xSupported(true);

		// Simple XML
		String e4x = "var foo = <one attr1=\"yes\" attr2='no'>foobar</one>;";
		Segment seg = createSegment(e4x);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "one"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "attr1"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"yes\""));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "attr2"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'no'"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foobar"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "one"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// Comment
		e4x = "var foo = <!-- Hello world -->;";
		seg = createSegment(e4x);
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- Hello world -->"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// Comment with URL
		e4x = "var foo = <!-- http://www.google.com -->;";
		seg = createSegment(e4x);
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- "));
		token = token.getNextToken();
		Assert.assertTrue(token.isHyperlink());
		Assert.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "http://www.google.com"));
		token = token.getNextToken();
		Assert.assertFalse(token.isHyperlink());
		Assert.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, " -->"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// CDATA
		e4x = "var foo = <![CDATA[foo]]>;";
		seg = createSegment(e4x);
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_CDATA_DELIMITER, "<![CDATA["));
		token = token.getNextToken();
		Assert.assertTrue("nope - " + token, token.is(TokenTypes.MARKUP_CDATA, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_CDATA_DELIMITER, "]]>"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// DTD
		e4x = "var foo = <!doctype FOO>;";
		seg = createSegment(e4x);
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_DTD, "<!doctype FOO>"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// DTD containing a comment
		e4x = "var foo = <!doctype FOO <!-- foo -->>;";
		seg = createSegment(e4x);
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_DTD, "<!doctype FOO "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- foo -->"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_DTD, ">"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// Processing instruction
		e4x = "var foo = <?xml version=\"1.0\"?>;";
		seg = createSegment(e4x);
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "var"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.OPERATOR, "="));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.MARKUP_PROCESSING_INSTRUCTION, "<?xml version=\"1.0\"?>"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));

		// "each" keyword, valid when e4x is enabled
		seg = createSegment("each");
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.RESERVED_WORD, "each"));

		// e4x attribute
		String attr = "@foo";
		seg = createSegment(attr);
		tm = new JavaScriptTokenMaker();
		token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, attr));

	}


	@Test
	public void testJS_EolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testJS_EolComments_URL() {

		String[] eolCommentLiterals = {
			// Note: The 0-length token at the end of the first example is a
			// minor bug/performance thing
			"// Hello world http://www.sas.com",
			"// Hello world http://www.sas.com extra",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			// Note: The 0-length token at the end of the first example is a
			// minor bug/performance thing
			if (token != null && token.isPaintable() && token.length() > 0) {
				Assert.assertFalse(token.isHyperlink());
				Assert.assertTrue(token.is(TokenTypes.COMMENT_EOL, " extra"));
			}

		}

	}


	@Test
	public void testJS_FloatingPointLiterals() {

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
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testJS_Functions() {

		String code = "eval parseInt parseFloat escape unescape isNaN isFinite";

		Segment segment = createSegment(code);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] functions = code.split(" +");
		for (int i = 0; i < functions.length; i++) {
			Assert.assertEquals(functions[i], token.getLexeme());
			Assert.assertEquals("Not a function token: " + token, TokenTypes.FUNCTION, token.getType());
			if (i < functions.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testJS_HexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL ";

		Segment segment = createSegment(code);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] literals = code.split(" +");
		for (int i = 0; i < literals.length; i++) {
			Assert.assertEquals(literals[i], token.getLexeme());
			Assert.assertEquals("Not a hex number: " + token, TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < literals.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

	}


	@Test
	public void testJS_Keywords() {

		String code = "break case catch class const continue " +
				"debugger default delete do else export extends finally for function if " +
				"import in instanceof let new super switch " +
				"this throw try typeof void while with " +
				"NaN Infinity " +
				"let"; // As of 1.7, which is our default version

		Segment segment = createSegment(code);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Not a keyword token: " + token, TokenTypes.RESERVED_WORD, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

		segment = createSegment("return");
		token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
		Assert.assertEquals("return", token.getLexeme());
		Assert.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());
		token = token.getNextToken();
		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testJS_MultiLineComments() {

		String[] mlcLiterals = {
			"/* Hello world */",
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
		}

	}


	@Test
	public void testJS_MultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world http://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assert.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	public void testJS_Numbers() {

		String[] ints = {
			"0", "42", /*"-7",*/
			"0l", "42l",
			"0L", "42L",
		};

		for (String code : ints) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
		}

		String[] floats = {
			"1e17", "3.14159", "5.7e-8", "2f", "2d",
		};

		for (String code : floats) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
		}

		String[] hex = {
			"0x1f", "0X1f", "0x1F", "0X1F",
		};

		for (String code : hex) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
		}

		String[] errors = {
			"42foo", "1e17foo", "0x1ffoo",
		};

		for (String code : errors) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.ERROR_NUMBER_FORMAT, token.getType());
		}

	}


	@Test
	public void testJS_Operators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Not an operator: " + token, TokenTypes.OPERATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testJS_Regexes() {

		String[] regexes = {
			"/foobar/", "/foobar/gim", "/foo\\/bar\\/bas/g",
		};

		for (String code : regexes) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.REGEX, token.getType());
		}

	}


	@Test
	public void testJS_Separators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assert.assertEquals(separators[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.SEPARATOR, token.getType());
			// Just one extra test here
			Assert.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, separators[i].charAt(0)));
			if (i < separators.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testJS_Separators_renderedAsIdentifiers() {

		String[] separators2 = { ";", ",", "." };

		for (String code : separators2) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.IDENTIFIER, token.getType());
		}

	}


	@Test
	public void testJS_StringLiterals_invalid() {

		String[] stringLiterals = {
			"\"\\xG7\"", // Invalid hex/octal escape
			"\"foo\\ubar\"", "\"\\u00fg\"", // Invalid Unicode escape
			"\"My name is \\ubar and I \\", // Continued onto another line
			"\"This is unterminated and ", // Unterminated string
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals("Not an ERROR_STRING_DOUBLE: " + token,
					TokenTypes.ERROR_STRING_DOUBLE, token.getType());
		}

	}


	@Test
	public void testJS_StringLiterals_valid() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\x77\"", "\"\\u00fe\"", "\"\\\"\"",
			"\"My name is Robert and I \\", // String continued on another line
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	public void testJS_Whitespace() {

		String[] whitespace = {
			" ", "\t", "\f", "   \t   ",
		};

		for (String code : whitespace) {
			Segment segment = createSegment(code);
			JavaScriptTokenMaker tm = new JavaScriptTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assert.assertEquals(TokenTypes.WHITESPACE, token.getType());
		}

	}


}