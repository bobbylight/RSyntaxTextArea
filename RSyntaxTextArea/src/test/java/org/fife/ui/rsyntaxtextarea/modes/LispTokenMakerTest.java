/*
 * 06/06/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link LispTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class LispTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new LispTokenMaker();
	}


	@Test
	public void testEolComments() {
		String[] eolCommentLiterals = {
			"; Hello world",
		};
		assertAllTokensOfType(eolCommentLiterals, TokenTypes.COMMENT_EOL);
	}


	@Test
	public void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"; Hello world http://www.sas.com",
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
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

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
	public void testHexLiterals() {
		String[] hexLiterals = {
				"0x1", "0xfe", "0x333333", "0xFE"
		};
		assertAllTokensOfType(hexLiterals, TokenTypes.LITERAL_NUMBER_HEXADECIMAL);
	}


	@Test
	public void testKeywords() {
		String[] keywords = {
				"defclass",
				"defconstant",
				"defgeneric",
				"define-compiler-macro",
				"define-condition",
				"define-method-combination",
				"define-modify-macro",
				"define-setf-expander",
				"define-symbol-macro",
				"defmacro",
				"defmethod",
				"defpackage",
				"defparameter",
				"defsetf",
				"defstruct",
				"deftype",
				"defun",
				"defvar",

				"abort",
				"assert",
				"block",
				"break",
				"case",
				"catch",
				"ccase",
				"cerror",
				"cond",
				"ctypecase",
				"declaim",
				"declare",
				"do",
				"do*",
				"do-all-symbols",
				"do-external-symbols",
				"do-symbols",
				"dolist",
				"dotimes",
				"ecase",
				"error",
				"etypecase",
				"eval-when",
				"flet",
				"handler-bind",
				"handler-case",
				"if",
				"ignore-errors",
				"in-package",
				"labels",
				"lambda",
				"let",
				"let*",
				"locally",
				"loop",
				"macrolet",
				"multiple-value-bind",
				"proclaim",
				"prog",
				"prog*",
				"prog1",
				"prog2",
				"progn",
				"progv",
				"provide",
				"require",
				"restart-bind",
				"restart-case",
				"restart-name",
				"return",
				"return-from",
				"signal",
				"symbol-macrolet",
				"tagbody",
				"the",
				"throw",
				"typecase",
				"unless",
				"unwind-protect",
				"when",
				"with-accessors",
				"with-compilation-unit",
				"with-condition-restarts",
				"with-hash-table-iterator",
				"with-input-from-string",
				"with-open-file",
				"with-open-stream",
				"with-output-to-string",
				"with-package-iterator",
				"with-simple-restart",
				"with-slots",
				"with-standard-io-syntax",
		};
		assertAllTokensOfType(keywords, TokenTypes.RESERVED_WORD);
	}


	@Test
	public void testMultiLineComments() {

		String[] mlcLiterals = {
			"#| Hello world |#",
		};

		assertAllTokensOfType(mlcLiterals, TokenTypes.COMMENT_MULTILINE);
	}


	@Test
	public void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"#| Hello world http://www.sas.com |#",
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
			Assert.assertEquals(" |#", token.getLexeme());

		}

	}


	@Test
	public void testSeparators() {
		String[] separators = { "(", ")" };
		assertAllTokensOfType(separators, TokenTypes.SEPARATOR);
	}


	@Test
	public void testStringLiterals() {
		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
		};
		assertAllTokensOfType(stringLiterals, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE);
	}


}