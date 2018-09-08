/*
 * 06/06/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Test;


/**
 * Unit tests for the {@link SASTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SASTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new SASTokenMaker();
	}


	@Test
	public void testCharLiterals() {
		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};
		assertAllTokensOfType(chars, TokenTypes.LITERAL_CHAR);
	}


	@Test
	public void testEolComments() {
		String[] eolCommentLiterals = {
			"* Hello world",
		};
		assertAllTokensOfType(eolCommentLiterals, TokenTypes.COMMENT_EOL);
	}


	@Test
	public void testKeywords() {
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
		assertAllTokensOfType(keywords, TokenTypes.RESERVED_WORD);
	}


	@Test
	public void testMacros() {

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

		assertAllTokensOfType(macros, TokenTypes.FUNCTION);
	}


	@Test
	public void testProcs() {

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

		assertAllTokensOfType(procs, TokenTypes.DATA_TYPE);
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