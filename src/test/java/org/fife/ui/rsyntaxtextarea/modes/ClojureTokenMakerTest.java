/*
 * 07/09/2016
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
 * Unit tests for the {@link ClojureTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ClojureTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new ClojureTokenMaker();
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
	public void testFunctions() {
		String[] functions = {
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
				"zipmap",
		};
		assertAllTokensOfType(functions, TokenTypes.FUNCTION);
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
				"when-not",
		};
		assertAllTokensOfType(keywords, TokenTypes.RESERVED_WORD);
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


	@Test
	public void testVariables() {
		String[] variables = {
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
				"*use-context-classloader*",
		};
		assertAllTokensOfType(variables, TokenTypes.VARIABLE);
	}


}