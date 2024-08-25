/*
 * 06/06/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link SASTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class SASTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new SASTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("*", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER || i == TokenTypes.VARIABLE;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testChars() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'"
		);
	}


	@Test
	void testChars_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"'",
			"continued'",
			"continued and still unterminated"
		);
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"* Hello world"
		);
	}


	/**
	 * Our lexer is unfortunately complex and needs this test case.
	 */
	@Test
	void testEolComments_withLeadingWhitespace() {

		String code = "  * this is a comment with leading whitespace";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, "  "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, "* this is a comment with leading whitespace"));
	}


	/**
	 * Our lexer is unfortunately complex and needs this test case.
	 */
	@Test
	void testEolComments_withLeadingWhitespace_dontMisidentifyLaterInLine() {

		String code = "foo  * notComment";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, "  "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, "*"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, "notComment"));
	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"foo123"
		);
	}


	@Test
	void testKeywords() {
		String[] keywords = {
				"_all_",
				"_character_",
				"_data_",
				"_infile_",
				"_last_",
				"_null_",
				"_numeric_",
				"_page_",
				"_temporary_",
				"abend",
				"abort",
				"all",
				"alter",
				"and",
				"array",
				"as",
				"ascending",
				"attrib",
				"axis",
				"bell",
				"blank",
				"border",
				"bounds",
				"by",
				"call",
				"cancel",
				"cards",
				"cards4",
				"choro",
				"class",
				"classes",
				"clear",
				"close",
				"compute",
				"contrast",
				"coord",
				"coordinates",
				"cov",
				"create",
				"data",
				"datalines",
				"datalines4",
				"delete",
				"descending",
				"describe",
				"discrete",
				"disk",
				"display",
				"dm",
				"do",
				"drop",
				"dummy",
				"else",
				"end",
				"endrsubmit",
				"endsas",
				"error",
				"except",
				"expandtabs",
				"factors",
				"file",
				"filename",
				"flowover",
				"footnote",
				"frame",
				"freq",
				"from",
				"go",
				"goption",
				"goptions",
				"goto",
				"grid",
				"group",
				"groupby",
				"groupformat",
				"having",
				"haxis",
				"hbar",
				"heading",
				"high",
				"html",
				"id",
				"if",
				"infile",
				"informat",
				"inner",
				"input",
				"insert",
				"intersect",
				"keep",
				"keylabel",
				"label",
				"lable",
				"legend",
				"length",
				"libname",
				"lineqs",
				"link",
				"list",
				"listing",
				"log",
				"lostcard",
				"low",
				"mark",
				"matings",
				"mean",
				"merge",
				"missing",
				"missover",
				"mod",
				"model",
				"modify",
				"n",
				"nocell",
				"nocharacters",
				"nodupkey",
				"noexpandtabs",
				"noframe",
				"noheading",
				"noinput",
				"nolegend",
				"nopad",
				"noprint",
				"nosharebuffers",
				"not",
				"note",
				"notitle",
				"notitles",
				"notsorted",
				"ods",
				"old",
				"option",
				"or",
				"order",
				"orderby",
				"other",
				"otherwise",
				"outer",
				"output",
				"over",
				"overlay",
				"overprint",
				"pad",
				"pageby",
				"pagesize",
				"parmcards",
				"parmcards4",
				"parms",
				"pattern",
				"pct",
				"pctn",
				"pctsum",
				"picture",
				"pie",
				"pie3d",
				"plotter",
				"predict",
				"prefix",
				"printer",
				"proc",
				"ps",
				"put",
				"quit",
				"random",
				"range",
				"remove",
				"rename",
				"response",
				"replace",
				"reset",
				"retain",
				"return",
				"rsubmit",
				"run",
				"s2",
				"select",
				"set",
				"sharebuffers",
				"signoff",
				"signon",
				"sim",
				"skip",
				"source2",
				"startsas",
				"std",
				"stop",
				"stopover",
				"strata",
				"sum",
				"sumby",
				"supvar",
				"symbol",
				"table",
				"tables",
				"tape",
				"terminal",
				"test",
				"then",
				"time",
				"title",
				"to",
				"transform",
				"treatments",
				"truncover",
				"unbuf",
				"unbuffered",
				"union",
				"until",
				"update",
				"validate",
				"value",
				"var",
				"variables",
				"vaxis",
				"vbar",
				"weight",
				"when",
				"where",
				"while",
				"with",
				"window",
				"x",
		};
		assertAllTokensOfType(TokenTypes.RESERVED_WORD, keywords);
	}


	@Test
	void testMacros() {

		String[] macros = {
				"%abort",
				"%bquote",
				"%by",
				"%cms",
				"%copy",
				"%display",
				"%do",
				"%else",
				"%end",
				"%eval",
				"%global",
				"%go",
				"%goto",
				"%if",
				"%inc",
				"%include",
				"%index",
				"%input",
				"%keydef",
				"%length",
				"%let",
				"%local",
				"%macro",
				"%mend",
				"%nrbquote",
				"%nrquote",
				"%nrstr",
				"%put",
				"%qscan",
				"%qsubstr",
				"%qsysfunc",
				"%quote",
				"%qupcase",
				"%scan",
				"%str",
				"%substr",
				"%superq",
				"%syscall",
				"%sysevalf",
				"%sysexec",
				"%sysfunc",
				"%sysget",
				"%sysprod",
				"%sysrput",
				"%then",
				"%to",
				"%tso",
				"%unquote",
				"%until",
				"%upcase",
				"%while",
				"%window",
		};

		assertAllTokensOfType(TokenTypes.FUNCTION, macros);
	}


	@Test
	void testMacroVariables() {
		assertAllTokensOfType(TokenTypes.VARIABLE,
			"&foo",
			"&foo123"
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
			"continued from a previous ine and unterminated",
			"continued from a previous line */"
		);
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"+", "-", "/", "^", "|",
			"=", "^=", "~=",
			">", ">=",
			"<", "<=",
			"eq", "ne", "gt", "lt", "ge", "le", "in"
		);
	}

	@Test
	void testProcs() {

		String[] procs = {
				/* Base SAS procs. */
				"append",
				"calendar",
				"catalog",
				"chart",
				"cimport",
				"compare",
				"contents",
				"copy",
				"cpm",
				"cport",
				"datasets",
				//"display",
				"explode",
				"export",
				"fontreg",
				"format",
				"forms",
				"fslist",
				"import",
				"means",
				"migrate",
				"options",
				"optload",
				"optsave",
				"plot",
				"pmenu",
				"print",
				"printto",
				"proto",
				"prtdef",
				"prtexp",
				"pwencode",
				"rank",
				"registry",
				"report",
				"sort",
				"sql",
				"standard",
				"summary",
				"tabulate",
				"template",
				"timeplot",
				"transpose",

				/* SAS/STAT procs. */
				"corr",
				//"freq",
				"univariate",
		};

		assertAllTokensOfType(TokenTypes.DATA_TYPE, procs);
	}


	@Test
	void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(",
			")"
		);
	}


	@Test
	void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"",
			"\"hi\"",
			"\"\\u00fe\"",
			"\"unterminated string"
		);
	}


	@Test
	void testStrings_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"",
			"continued text\"",
			"still unterminated"
		);
	}


	@Test
	void testWhiteSpace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"   ",
			"\t",
			"\t\t",
			"\t  "
		);
	}
}
