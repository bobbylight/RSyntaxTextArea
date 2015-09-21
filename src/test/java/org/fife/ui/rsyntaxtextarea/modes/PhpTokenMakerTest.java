package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link PHPTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class PhpTokenMakerTest extends AbstractTokenMakerTest {


	@Test
	public void testBooleanLiterals() {

		String code = "<?php true false TRUE FALSE ?>";

		Segment segment = createSegment(code);
		PHPTokenMaker tm = new PHPTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");

		Assert.assertTrue("Unexpected token: " + token, token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<?php"));
		token = token.getNextToken();
		Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();

		for (int i = 1; i < 5; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
			token = token.getNextToken();
			Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
			Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			token = token.getNextToken();
		}

		Assert.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "?>"));
		token = token.getNextToken();
		Assert.assertTrue(token.getType() == TokenTypes.NULL);

	}


	@Test
	public void testCharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			PHPTokenMaker tm = new PHPTokenMaker();
			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assert.assertEquals(TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	public void testEolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			PHPTokenMaker tm = new PHPTokenMaker();
			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}

/* Not in PHPTokenMaker ???
	@Test
	public void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world http://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			PHPTokenMaker tm = new PHPTokenMaker();

			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

		}

	}
*/

	@Test
	public void testFloatingPointLiterals() {

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
		PHPTokenMaker tm = new PHPTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

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

		Assert.assertTrue(token.getType() == PHPTokenMaker.INTERNAL_IN_PHP);

	}


	@Test
	public void testKeywords() {

		String[] keywords = {
			"__CLASS__",
			"__DIR__",
			"__FILE__",
			"__FUNCTION__",
			"__METHOD__",
			"__NAMESPACE__",
			"abstract",
			"and",
			"array",
			"as",
			"break",
			"case",
			"catch",
			"cfunction",
			"class",
			"clone",
			"const",
			"continue",
			"declare",
			"default",
			"die",
			"do",
			"echo",
			"else",
			"elseif",
			"empty",
			"enddeclare",
			"endfor",
			"endforeach",
			"endif",
			"endswitch",
			"endwhile",
			"eval",
			"extends",
			"final",
			"for",
			"foreach",
			"function",
			"global",
			"goto",
			"if",
			"implements",
			"include",
			"include_once",
			"interface",
			"instanceof",
			"isset",
			"list",
			"namespace",
			"new",
			"old_function",
			"or",
			"print",
			"private",
			"protected",
			"public",
			"require",
			"require_once",
			"static",
			"switch",
			"throw",
			"try",
			"unset",
			"use",
			"var",
			"while",
			"xor",

			"parent",
			"self",
			"stdClass",
		};

		for (String code : keywords) {
			Segment segment = createSegment(code);
			PHPTokenMaker tm = new PHPTokenMaker();
			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assert.assertEquals(TokenTypes.RESERVED_WORD, token.getType());
		}

	}


	@Test
	public void testKeywords2() {

		String code = "exit return";

		Segment segment = createSegment(code);
		PHPTokenMaker tm = new PHPTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertTrue(token.getType() == PHPTokenMaker.INTERNAL_IN_PHP);

	}


	@Test
	public void testMultiLineComments() {

		String[] mlcLiterals = {
			"/* Hello world */",
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			PHPTokenMaker tm = new PHPTokenMaker();
			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
		}

	}


	@Test
	public void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world http://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			PHPTokenMaker tm = new PHPTokenMaker();

			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
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
	public void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		PHPTokenMaker tm = new PHPTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

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

		Assert.assertTrue(token.getType() == PHPTokenMaker.INTERNAL_IN_PHP);

	}


	@Test
	public void testSeparators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		PHPTokenMaker tm = new PHPTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

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

		Assert.assertTrue(token.getType() == PHPTokenMaker.INTERNAL_IN_PHP);

	}


	@Test
	public void testStringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			PHPTokenMaker tm = new PHPTokenMaker();
			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


}