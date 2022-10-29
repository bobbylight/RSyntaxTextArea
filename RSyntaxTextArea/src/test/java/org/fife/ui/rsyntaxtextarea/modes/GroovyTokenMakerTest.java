/*
 * 06/21/2015
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
 * Unit tests for the {@link GroovyTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class GroovyTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new GroovyTokenMaker();
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
	void testAnnotations() {

		String code = "@Test @Foo @Foo_Bar_Bas @Number7";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.ANNOTATION, token.getType(), "Unexpected token type for token: " + token);
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
	void testBinaryLiterals() {

		String code =
			"0b0 0b1 0B0 0B1 0b010 0B010 0b0_10 0B0_10";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
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
	void testBooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
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
	void testCharLiterals() {

		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};

		for (String code : chars) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_CHAR, token.getType());
		}

	}


	@Test
	void testDataTypes() {

		String code = "boolean byte char double float int long short";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.DATA_TYPE, token.getType());
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
	void testDocComments() {

		String[] docCommentLiterals = {
			"/** Hello world */",
		};

		for (String code : docCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
		}

	}


	@Test
	void testDocComments_URL() {

		String[] docCommentLiterals = {
			"/** Hello world https://www.sas.com */",
		};

		for (String code : docCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

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
			"// Hello world https://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

		}

	}


	@Test
	void testFloatingPointLiterals() {

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
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
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
	void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL " +
				"0x1_1 0xf_e 0x33_3333_333333 0X1_1 0Xf_e 0X333_333_33333 0xF_E 0XF_E " +
				"0x1_1l 0xf_el 0x333_33333_3333l 0X1_1l 0Xf_el 0X333_3333_3333l 0xF_El 0XF_El " +
				"0x1_1L 0xf_eL 0x333_33333_3333L 0X1_1L 0Xf_eL 0X333_3333_3333L 0xF_EL 0XF_EL";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
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
	void testClassNames_java_lang() {

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
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.FUNCTION, token.getType());
		}

	}


	@Test
	void testKeywords() {

		// Java keywords
		String code = "abstract assert break case catch class const continue " +
				"default do else enum extends final finally for goto if " +
				"implements import instanceof interface native new null package " +
				"private protected public static strictfp super switch " +
				"synchronized this throw throws transient try void volatile while";

		// Add Groovy-specific keywords
		code += " as assert def mixin property test using in";

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

		segment = createSegment("return");
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assertions.assertEquals("return", token.getLexeme());
		Assertions.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testMultiLineChar_allOnOneLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a text block\"\"\""
		);
	}


	@Test
	void testMultiLineChar_continuingToAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'''this is a text block"
		);
	}


	@Test
	void testMultiLineChar_continuedFromAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued from another line and unterminated",
			"continued from another line and terminated'''"
		);
	}


	@Test
	void testMultiLineChar_notContinuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'''this is a \\''' text block'''",
			"'''this is a \\''' text block"
		);
	}


	@Test
	void testMultiLineChar_continuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued from another \\''' line and unterminated",
			"continued from another \\''' line and terminated'''"
		);
	}


	@Test
	void testMultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"/* Hello world unterminated",
			"/* Hello world */",
			"/**/"
		);
	}


	@Test
	void testMultiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			TokenTypes.COMMENT_MULTILINE,
			"continued from a previous line and unterminated",
			"continued from a previous line */"
		);
	}


	@Test
	void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world https://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testMultiLineString_allOnOneLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a text block\"\"\""
		);
	}


	@Test
	void testMultiLineString_continuingToAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a text block"
		);
	}


	@Test
	void testMultiLineString_continuedFromAnotherLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from another line and unterminated",
			"continued from another line and terminated\"\"\""
		);
	}


	@Test
	void testMultiLineString_notContinuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"this is a \\\"\"\" text block\"\"\"",
			"\"\"\"this is a \\\"\"\" text block"
		);
	}


	@Test
	void testMultiLineString_continuedFromAnotherLineWithEmbeddedEscapedTextBlockTerminators() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from another \\\"\"\" line and unterminated",
			"continued from another \\\"\"\" line and terminated\"\"\""
		);
	}


	@Test
	void testOctalLiterals() {

		// Note that octal tokens use the token type for hex literals.

		String code = "01 073 0333333333333 01 073 033333333333 073 073 " +
				"01l 073l 0333333333333l 01l 073l 033333333333l 073l 073l " +
				"01L 073L 0333333333333L 01L 073L 033333333333L 073L 073L " +
				"01_1 07_3 033_3333_333333 01_1 07_3 0333_333_33333 07_3 07_3 " +
				"01_1l 07_3l 0333_33333_3333l 01_1l 07_3l 0333_3333_3333l 07_3l 07_3l " +
				"01_1L 07_3L 0333_33333_3333L 01_1L 07_3L 0333_3333_3333L 07_3L 07_3L";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
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
	void testObjectClassMethodAdditions() {

		String[] additions = {
			"addShutdownHook", "any", "asBoolean", "asType", "collect", "dump",
			"each", "eachWithIndex", "every", "find", "findAll", "findIndexOf",
			"findIndexValues", "findLastIndexOf", "getAt", "getMetaClass",
			"getMetaPropertyValues", "getProperties", "grep", "hasProperty",
			"identity", "inject", "inspect", "invokeMethod", "is", "isCase",
			"iterator", "metaClass", "print", "printf", "println", "putAt",
			"respondsTo", "setMetaClass", "sleep", "split", "sprintf",
			"toString", "use", "with",
		};

		for (String code : additions) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.FUNCTION, code));
		}

	}


	@Test
	void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType());
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
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	void testWhiteSpace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"   ",
			"\t",
			"\t\t",
			"\t  \n",
			"\f"
		);
	}


}
