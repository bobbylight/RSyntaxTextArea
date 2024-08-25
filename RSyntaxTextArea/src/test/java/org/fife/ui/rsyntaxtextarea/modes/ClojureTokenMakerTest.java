/*
 * 07/09/2016
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
 * Unit tests for the {@link ClojureTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ClojureTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new ClojureTokenMaker();
	}


	@Test
	void testBooleanLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			"true",
			"false"
		);
	}


	@Test
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"\\\"",
			"\\a",
			"\\s",
			"\\d",
			"\\newline",
			"\\space",
			"\\tab"
		);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"; Hello world"
		);
	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"; Hello world https://www.google.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.google.com", token.getLexeme());

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
	void testFunctions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"*current-namespace*",
			//"*in*",
			//"*out*",
			//"*print-meta*",
			"->",
			"..",
			"agent",
			"agent-errors",
			"agent-of",
			"aget",
			"alter",
			"and",
			"any",
			"appl",
			"apply",
			"array",
			"aset",
			"aset-boolean",
			"aset-byte",
			"aset-double",
			"aset-float",
			"aset-int",
			"aset-long",
			"aset-short",
			"assoc",
			"binding",
			"boolean",
			"byte",
			"char",
			"clear-agent-errors",
			"commute",
			"comp",
			"complement",
			"concat",
			"conj",
			"cons",
			"constantly",
			"count",
			"cycle",
			"dec",
			"defmethod",
			"defmulti",
			"delay",
			"deref",
			"dissoc",
			"doseq",
			"dotimes",
			"doto",
			"double",
			"drop",
			"drop-while",
			"ensure",
			"eql-ref?",
			"eql?",
			"eval",
			"every",
			"ffirst",
			"filter",
			"find",
			"find-var",
			"first",
			"float",
			"fnseq",
			"frest",
			"gensym",
			"get",
			"hash-map",
			"identity",
			"implement",
			"import",
			"in-namespace",
			"inc",
			"int",
			"into",
			"into-array",
			"iterate",
			"key",
			"keys",
			"lazy-cons",
			"list",
			"list*",
			"load-file",
			"locking",
			"long",
			"make-array",
			"make-proxy",
			"map",
			"mapcat",
			"max",
			"memfn",
			"merge",
			"meta",
			"min",
			"name",
			"namespace",
			"neg?",
			"newline",
			"nil?",
			"not",
			"not-any",
			"not-every",
			"nth",
			"or",
			"peek",
			"pmap",
			"pop",
			"pos?",
			"print",
			"prn",
			"quot",
			"range",
			"read",
			"reduce",
			"ref",
			"refer",
			"rem",
			"remove-method",
			"repeat",
			"replicate",
			"rest",
			"reverse",
			"rfirst",
			"rrest",
			"rseq",
			"second",
			"seq",
			"set",
			"short",
			"sorted-map",
			"sorted-map-by",
			"split-at",
			"split-with",
			"str",
			"strcat",
			"sym",
			"sync",
			"take",
			"take-while",
			"time",
			"unimport",
			"unintern",
			"unrefer",
			"val",
			"vals",
			"vector",
			"with-meta",
			"zero?",
			"zipmap"
		);
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals(";", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
	}


	@Test
	void testHexLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_HEXADECIMAL,
			"0x1", "0xfe", "0x333333", "0xFE",
			"-0x1", "-0xfe", "-0x333333", "-0xFE"
		);
	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"bar",
			"bas123"
		);
	}


	@Test
	void testIntLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"-5", "0", "42",
			"-5l", "0l", "42l",
			"-5L", "0L", "42L"
		);
	}


	@Test
	void testIntLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			"-5x", "0x", "42x",
			"-5lx", "0lx", "42lx",
			"-5Lx", "0Lx", "42Lx",
			"0x42r",
			"4.2r"
		);
	}


	@Test
	void testKeywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"case",
			"class",
			"cond",
			"condp",
			"def",
			"defmacro",
			"defn",
			"do",
			"fn",
			"for",
			"if",
			"if-let",
			"if-not",
			"instance?",
			"let",
			"loop",
			"monitor-enter",
			"monitor-exit",
			"new",
			"quote",
			"recur",
			"set!",
			"this",
			"throw",
			"try-finally",
			"var",
			"when",
			"when-first",
			"when-let",
			"when-not"
		);
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"+",
			"-",
			"<=",
			"^",
			"<",
			"*",
			">=",
			"%",
			">",
			"/",
			"!=",
			"?",
			">>",
			"!",
			"&",
			"==",
			":",
			">>",
			"~",
			">>>",
			"="
		);
	}

	@Test
	void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(",
			")"
		);
	}


	@Test
	void testStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\""
		);
	}


	@Test
	void testStringLiterals_continuedFromPriorLine() {

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"continued from prior line",
			"continued from prior line\""
		);
	}


	@Test
	void testVariables() {
		assertAllTokensOfType(TokenTypes.VARIABLE,
			"*warn-on-reflection*",
			"*1",
			"*2",
			"*3",
			"*agent*",
			"*allow-unresolved-args*",
			"*assert*",
			"*clojure-version*",
			"*command-line-args*",
			"*compile-files*",
			"*compile-path*",
			"*e",
			"*err*",
			"*file*",
			"*flush-on-newline*",
			"*fn-loader*",
			"*in*",
			"*math-context*",
			"*ns*",
			"*out*",
			"*print-dup*",
			"*print-length*",
			"*print-level*",
			"*print-meta*",
			"*print-readably*",
			"*read-eval*",
			"*source-path*",
			"*unchecked-math*",
			"*use-context-classloader*"
		);
	}
}
