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
 * Unit tests for the {@link CTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class CTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new CTokenMaker();
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
	void testCharLiterals() {

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
			Assertions.assertEquals(TokenTypes.LITERAL_CHAR, token.getType(), "Invalid char literal: " + token);
		}

	}


	@Test
	void testDataTypes() {

		String code = "char div_t double float int ldiv_t long short signed size_t unsigned void wchar_t";

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
			"3.f 3.F 3.0f 3.0F .111f .111F " +
			// lower-case exponent, no sign
			"3.e7f 3.e7F 3.0e7f 3.0e7F .111e7f .111e7F " +
			// Upper-case exponent, no sign
			"3.E7f 3.E7F 3.0E7f 3.0E7F .111E7f .111E7F " +
			// Lower-case exponent, positive
			"3.e+7f 3.e+7F 3.0e+7f 3.0e+7F .111e+7f .111e+7F " +
			// Upper-case exponent, positive
			"3.E+7f 3.E+7F 3.0E+7f 3.0E+7F .111E+7f .111E+7F " +
			// Lower-case exponent, negative
			"3.e-7f 3.e-7F 3.0e-7f 3.0e-7F .111e-7f .111e-7F " +
			// Upper-case exponent, negative
			"3.E-7f 3.E-7F 3.0E-7f 3.0E-7F .111E-7f .111E-7F";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType(), "Invalid floating point: " + token);
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
				"0x1u 0xfeu 0x333333333333u 0X1u 0Xfeu 0X33333333333u 0xFEu 0XFEu " +
				"0x1U 0xfeU 0x333333333333U 0X1U 0XfeU 0X33333333333U 0xFEU 0XFEU " +
				"0x1lu 0xfelu 0x333333333333lu 0X1lu 0Xfelu 0X33333333333lu 0xFElu 0XFElu " +
				"0x1LU 0xfeLU 0x333333333333LU 0X1LU 0XfeLU 0X33333333333LU 0xFELU 0XFELU " +
				"0x1ul 0xfeul 0x333333333333ul 0X1ul 0Xfeul 0X33333333333ul 0xFEul 0XFEul " +
				"0x1UL 0xfeUL 0x333333333333UL 0X1UL 0XfeUL 0X33333333333UL 0xFEUL 0XFEUL";

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
	void testStandardFunctions() {

		String[] functions = {
			"abort",
			"abs",
			"acos",
			"asctime",
			"asin",
			"assert",
			"atan2",
			"atan",
			"atexit",
			"atof",
			"atoi",
			"atol",
			"bsearch",
			"btowc",
			"calloc",
			"ceil",
			"clearerr",
			"clock",
			"cosh",
			"cos",
			"ctime",
			"difftime",
			"div",
			"errno",
			"exit",
			"exp",
			"fabs",
			"fclose",
			"feof",
			"ferror",
			"fflush",
			"fgetc",
			"fgetpos",
			"fgetwc",
			"fgets",
			"fgetws",
			"floor",
			"fmod",
			"fopen",
			"fprintf",
			"fputc",
			"fputs",
			"fputwc",
			"fputws",
			"fread",
			"free",
			"freopen",
			"frexp",
			"fscanf",
			"fseek",
			"fsetpos",
			"ftell",
			"fwprintf",
			"fwrite",
			"fwscanf",
			"getchar",
			"getc",
			"getenv",
			"gets",
			"getwc",
			"getwchar",
			"gmtime",
			"isalnum",
			"isalpha",
			"iscntrl",
			"isdigit",
			"isgraph",
			"islower",
			"isprint",
			"ispunct",
			"isspace",
			"isupper",
			"isxdigit",
			"labs",
			"ldexp",
			"ldiv",
			"localeconv",
			"localtime",
			"log10",
			"log",
			"longjmp",
			"malloc",
			"mblen",
			"mbrlen",
			"mbrtowc",
			"mbsinit",
			"mbsrtowcs",
			"mbstowcs",
			"mbtowc",
			"memchr",
			"memcmp",
			"memcpy",
			"memmove",
			"memset",
			"mktime",
			"modf",
			"offsetof",
			"perror",
			"pow",
			"printf",
			"putchar",
			"putc",
			"puts",
			"putwc",
			"putwchar",
			"qsort",
			"raise",
			"rand",
			"realloc",
			"remove",
			"rename",
			"rewind",
			"scanf",
			"setbuf",
			"setjmp",
			"setlocale",
			"setvbuf",
			"setvbuf",
			"signal",
			"sinh",
			"sin",
			"sprintf",
			"sqrt",
			"srand",
			"sscanf",
			"strcat",
			"strchr",
			"strcmp",
			"strcmp",
			"strcoll",
			"strcpy",
			"strcspn",
			"strerror",
			"strftime",
			"strlen",
			"strncat",
			"strncmp",
			"strncpy",
			"strpbrk",
			"strrchr",
			"strspn",
			"strstr",
			"strtod",
			"strtok",
			"strtol",
			"strtoul",
			"strxfrm",
			"swprintf",
			"swscanf",
			"system",
			"tanh",
			"tan",
			"time",
			"tmpfile",
			"tmpnam",
			"tolower",
			"toupper",
			"ungetc",
			"ungetwc",
			"va_arg",
			"va_end",
			"va_start",
			"vfprintf",
			"vfwprintf",
			"vprintf",
			"vsprintf",
			"vswprintf",
			"vwprintf",
			"wcrtomb",
			"wcscat",
			"wcschr",
			"wcscmp",
			"wcscoll",
			"wcscpy",
			"wcscspn",
			"wcsftime",
			"wcslen",
			"wcsncat",
			"wcsncmp",
			"wcsncpy",
			"wcspbrk",
			"wcsrchr",
			"wcsrtombs",
			"wcsspn",
			"wcsstr",
			"wcstod",
			"wcstok",
			"wcstol",
			"wcstombs",
			"wcstoul",
			"wcsxfrm",
			"wctob",
			"wctomb",
			"wmemchr",
			"wmemcmp",
			"wmemcpy",
			"wmemmove",
			"wmemset",
			"wprintf",
			"wscanf",
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

		String code = "auto break case const continue default do else enum " +
				"extern for goto if register sizeof static struct " +
				"switch typedef union volatile while";

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

		String[] mlcLiterals = {
			"continued from a prior line unterminated",
			"continued from a prior line */",
		};

		for (String code : mlcLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.COMMENT_MULTILINE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
		}

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
	void testPreprocessorWords() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"#define",
			"#elif",
			"#else",
			"#endif",
			"#error",
			"#if",
			"#ifdef",
			"#ifndef",
			"#include",
			"# include",
			"#  include",
			"#\tinclude",
			"#\finclude",
			"#line",
			"#pragma",
			"#undef"
		);
	}


	@Test
	void testPreprocessor_renderIncludesAsStrings() {

		String[] codeSnippets = {
			"#include <something.h>",
			"#include <sys/types.h>",
			"#include <gnu/libc-version.h>",
			"#include  <gnu/libc-version.h>",
			"#include\t<gnu/libc-version.h>",
			"#include\f  \t <gnu/libc-version.h>",
		};

		for (String code: codeSnippets) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token t = tm.getTokenList(segment, TokenTypes.NULL, 0);

			Assertions.assertTrue(t.is(TokenTypes.PREPROCESSOR, "#include"));
			t = t.getNextToken();
			Assertions.assertTrue(t.isWhitespace());
			t = t.getNextToken();
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, t.getType(), "Failed for " + code);
			Assertions.assertTrue(t.getLexeme().endsWith(">")); // Assert it's the entire included file
		}
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
			"\"\"", "\"hi\"", "\"\\\"\"",
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	void testTrigraph() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"??=",
			"??(",
			"??)",
			"??/",
			"??'",
			"??<",
			"??>",
			"??!",
			"??-"
		);
	}


}
