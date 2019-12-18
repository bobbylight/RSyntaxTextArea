/*
 * 03/12/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link GoTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class GoTokenMakerTest extends AbstractTokenMakerTest {


	/**
	 * Returns a new instance of the <code>TokenMaker</code> to test.
	 *
	 * @return The <code>TokenMaker</code> to test.
	 */
	private TokenMaker createTokenMaker() {
		return new GoTokenMaker();
	}


	@Test
	public void testCharLiterals() {

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
			Assert.assertEquals("Invalid char literal: " + token, TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	public void testDataTypes() {

		String code = "bool string int int8 int16 int32 int64 uint uint8 uint16 uint32 uint64 uintptr byte rune " +
			"float32 float64 complex64 complex128";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

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

		Assert.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	public void testEolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world http://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

		}

	}


	@Test
	public void testFloatingPointLiterals() {

		String code =
			// Basic doubles
			"3.0 4.2 3.0 4.2 .111";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Invalid floating point: " + token, TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	@Override
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("//", startAndEnd[0]);
		Assert.assertNull(null, startAndEnd[1]);
	}


	@Test
	public void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Invalid hex literal: " + token, TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	public void testStandardFunctions() {

		String[] functions = {

			/* Builtins */
			"append",
			"cap",
			"close",
			"complex",
			"copy",
			"delete",
			"imag",
			"len",
			"make",
			"new",
			"panic",
			"print",
			"println",
			"real",
			"recover",

			/* Strings package */
			"Compare",
			"Contains",
			"ContainsAny",
			"ContainsRune",
			"Count",
			"EqualsFold",
			"Fields",
			"FieldsFunc",
			"HasPrefix",
			"HasSuffix",
			"Index",
			"IndexAny",
			"IndexByte",
			"IndexFunc",
			"IndexRune",
			"Join",
			"LastIndex",
			"LastIndexAny",
			"LastIndexByte",
			"LastIndexFunc",
			"Map",
			"Repeat",
			"Replace",
			"ReplaceAll",
			"Split",
			"SplitAfter",
			"SplitAfterN",
			"SplitN",
			"Title",
			"ToLower",
			"ToLowerSpecial",
			"ToTitle",
			"ToTitleSpecial",
			"ToUpper",
			"ToUppserSpecial",
			"Trim",
			"TrimFunc",
			"TrimLeft",
			"TrimLeftFunc",
			"TrimPrefix",
			"TrimRight",
			"TrimRightFunc",
			"TrimSpace",
			"TrimSuffix"
		};

		for (String code : functions) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.FUNCTION, token.getType());
		}

	}


	@Test
	public void testKeywords() {

		String code = "break case chan const continue default defer else fallthrough for func go goto if import " +
			"interface map package range select struct switch type var";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Not a keyword: " + token, TokenTypes.RESERVED_WORD, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(token.getType(), TokenTypes.NULL);

		segment = createSegment("return");
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertEquals("return", token.getLexeme());
		Assert.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());
		token = token.getNextToken();
		Assert.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	public void testMultiLineComments() {

		String[] mlcLiterals = {
			"/* Hello world */",
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
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
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
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

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | &&";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.OPERATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	public void testSeparators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

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

		Assert.assertEquals(token.getType(), TokenTypes.NULL);

	}


	@Test
	public void testStringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\\"\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


}
