/*
 * 03/23/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link JSPTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JSPTokenMakerTest extends AbstractTokenMakerTest {


	@Test
	public void testJava_Annotations() {

		String code = "@Test @Foo @Foo_Bar_Bas @Number7";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Unexpected token type for token: " + token, TokenTypes.ANNOTATION, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

	}


	@Test
	public void testJava_BinaryLiterals() {

		String code =
			"0b0 0b1 0B0 0B1 0b010 0B010 0b0_10 0B0_10";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

	}


	@Test
	public void testJava_BooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

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

	}


	@Test
	public void testJava_CharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
			Assert.assertEquals(TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	public void testJava_DataTypes() {

		String code = "boolean byte char double float int long short";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

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

	}


	@Test
	public void testJava_DocComments() {

		String[] docCommentLiterals = {
			"/** Hello world */",
		};

		for (String code : docCommentLiterals) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
			Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
		}

	}


	@Test
	public void testJava_DocComments_URL() {

		String[] docCommentLiterals = {
			"/** Hello world http://www.sas.com */",
		};

		for (String code : docCommentLiterals) {

			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();

			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
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
	public void testJava_EolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testJava_FloatingPointLiterals() {

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
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

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

	}


	@Test
	public void testJava_HexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL " +
				"0x1_1 0xf_e 0x33_3333_333333 0X1_1 0Xf_e 0X333_333_33333 0xF_E 0XF_E " +
				"0x1_1l 0xf_el 0x333_33333_3333l 0X1_1l 0Xf_el 0X333_3333_3333l 0xF_El 0XF_El " +
				"0x1_1L 0xf_eL 0x333_33333_3333L 0X1_1L 0Xf_eL 0X333_3333_3333L 0xF_EL 0XF_EL";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Not a hex literal: " + token, TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

	}


	@Test
	public void testJava_ClassNames_java_lang() {

		String[] classNames = { "Appendable",
				"AutoCloseable",
				"CharSequence",
				"Cloneable",
				"Comparable",
				"Iterable",
				"Readable",
				"Runnable",
				"Thread.UncaughtExceptionHandler",
				"Boolean",
				"Byte",
				"Character",
				"Character.Subset",
				"Character.UnicodeBlock",
				"Class",
				"ClassLoader",
				"ClassValue",
				"Compiler",
				"Double",
				"Enum",
				"Float",
				"InheritableThreadLocal",
				"Integer",
				"Long",
				"Math",
				"Number",
				"Object",
				"Package",
				"Process",
				"ProcessBuilder",
				"ProcessBuilder.Redirect",
				"Runtime",
				"RuntimePermission",
				"SecurityManager",
				"Short",
				"StackTraceElement",
				"StrictMath",
				"String",
				"StringBuffer",
				"StringBuilder",
				"System",
				"Thread",
				"ThreadGroup",
				"ThreadLocal",
				"Throwable",
				"Void",
				"Character.UnicodeScript",
				"ProcessBuilder.Redirect.Type",
				"Thread.State",
				"ArithmeticException",
				"ArrayIndexOutOfBoundsException",
				"ArrayStoreException",
				"ClassCastException",
				"ClassNotFoundException",
				"CloneNotSupportedException",
				"EnumConstantNotPresentException",
				"Exception",
				"IllegalAccessException",
				"IllegalArgumentException",
				"IllegalMonitorStateException",
				"IllegalStateException",
				"IllegalThreadStateException",
				"IndexOutOfBoundsException",
				"InstantiationException",
				"InterruptedException",
				"NegativeArraySizeException",
				"NoSuchFieldException",
				"NoSuchMethodException",
				"NullPointerException",
				"NumberFormatException",
				"RuntimeException",
				"SecurityException",
				"StringIndexOutOfBoundsException",
				"TypeNotPresentException",
				"UnsupportedOperationException",
				"AbstractMethodError",
				"AssertionError",
				"BootstrapMethodError",
				"ClassCircularityError",
				"ClassFormatError",
				"Error",
				"ExceptionInInitializerError",
				"IllegalAccessError",
				"IncompatibleClassChangeError",
				"InstantiationError",
				"InternalError",
				"LinkageError",
				"NoClassDefFoundError",
				"NoSuchFieldError",
				"NoSuchMethodError",
				"OutOfMemoryError",
				"StackOverflowError",
				"ThreadDeath",
				"UnknownError",
				"UnsatisfiedLinkError",
				"UnsupportedClassVersionError",
				"VerifyError",
				"VirtualMachineError",
		};

		for (String code : classNames) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
			Assert.assertEquals(TokenTypes.FUNCTION, token.getType());
		}

	}


	@Test
	public void testJava_Keywords() {

		String code = "abstract assert break case catch class const continue " +
				"default do else enum extends final finally for goto if " +
				"implements import instanceof interface native new null package " +
				"private protected public static strictfp super switch " +
				"synchronized this throw throws transient try void volatile while";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.RESERVED_WORD, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		segment = createSegment("return");
		token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
		Assert.assertEquals("return", token.getLexeme());
		Assert.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());

	}


	@Test
	public void testJava_MultiLineComments() {

		String[] mlcLiterals = {
			"/* Hello world */",
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
		}

	}


	@Test
	public void testJava_MultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world http://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();

			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
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
	public void testJava_OctalLiterals() {

		// Note that octal tokens use the token type for hex literals.

		String code = "01 073 0333333333333 01 073 033333333333 073 073 " +
				"01l 073l 0333333333333l 01l 073l 033333333333l 073l 073l " +
				"01L 073L 0333333333333L 01L 073L 033333333333L 073L 073L " +
				"01_1 07_3 033_3333_333333 01_1 07_3 0333_333_33333 07_3 07_3 " +
				"01_1l 07_3l 0333_33333_3333l 01_1l 07_3l 0333_3333_3333l 07_3l 07_3l " +
				"01_1L 07_3L 0333_33333_3333L 01_1L 07_3L 0333_3333_3333L 07_3L 07_3L";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

	}


	@Test
	public void testJava_Operators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

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

	}


	@Test
	public void testJava_Separators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);

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

	}


	@Test
	public void testJava_StringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JAVA_EXPRESSION, 0);
			Assert.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	public void testJS_BooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);

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

	}


	@Test
	public void testJS_CharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment,
					JSPTokenMaker.INTERNAL_IN_JS, 0);
			Assert.assertEquals("Invalid char token: " + token, TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	public void testJS_DataTypes() {

		String code = "boolean byte char double float int long short";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);

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

	}


	@Test
	public void testJS_EolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment,
					JSPTokenMaker.INTERNAL_IN_JS, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	public void testJS_EolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world http://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();

			Token token = tm.getTokenList(segment,
					JSPTokenMaker.INTERNAL_IN_JS, 0);
			Assert.assertEquals("Unexpected token: " + token, TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

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
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);

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

	}


	@Test
	public void testJS_Functions() {

		String code = "eval parseInt parseFloat escape unescape isNaN isFinite";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment,
				JSPTokenMaker.INTERNAL_IN_JS, 0);

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

	}


	@Test
	public void testJS_HexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL ";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment,
				JSPTokenMaker.INTERNAL_IN_JS, 0);

		String[] literals = code.split(" +");
		for (int i = 0; i < literals.length; i++) {
			Assert.assertEquals("Not expected hex literal: " + token, literals[i], token.getLexeme());
			Assert.assertEquals("Not a hex literal: " + token, TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
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
				"NaN Infinity";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);

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

		segment = createSegment("return");
		token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);
		Assert.assertEquals("return", token.getLexeme());
		Assert.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());
		token = token.getNextToken();

	}


	@Test
	public void testJS_MultiLineComments() {

		String[] mlcLiterals = {
			"/* Hello world */",
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);
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
			JSPTokenMaker tm = new JSPTokenMaker();

			Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);
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
	public void testJS_Operators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment,
				JSPTokenMaker.INTERNAL_IN_JS, 0);

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

	}


	@Test
	public void testJS_Separators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		JSPTokenMaker tm = new JSPTokenMaker();
		Token token = tm.getTokenList(segment, JSPTokenMaker.INTERNAL_IN_JS, 0);

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

	}


	@Test
	public void testJS_StringLiterals() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			JSPTokenMaker tm = new JSPTokenMaker();
			Token token = tm.getTokenList(segment,
					JSPTokenMaker.INTERNAL_IN_JS, 0);
			Assert.assertEquals("Invalid string token: " + token, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


}