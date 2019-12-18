/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for the {@link PythonTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class PythonTokenMakerTest extends AbstractTokenMakerTest2 {


	@Before
	public void setUp() {
	}


	@After
	public void tearDown() {
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new PythonTokenMaker();
	}


	@Test
	public void testAnnotations() {
		assertAllTokensOfType(TokenTypes.ANNOTATION,
			"@foo"
		);
	}


	@Test
	public void testFloatsAndImaginaries() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_FLOAT,
			"34.",
			"34.1",
			".34",
			"34e7",
			"34.e7",
			"34.1e7",

			"34j",
			"34.j",
			"34.1j",
			".34j",
			"34e7j",
			"34.e7j",
			"34.1e7j"
		);
	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("#", startAndEnd[0]);
		Assert.assertNull(startAndEnd[1]);
	}


	@Test
	public void testIntegers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"34",
			"34L",
			"34l"
		);
	}


	@Test
	public void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER, "foo");
	}


	@Test
	public void testComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL, "# This is a comment");
	}


	@Test
	public void testDataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
		"char",
			"double",
			"float",
			"int",
			"long",
			"short",
			"signed",
			"unsigned",
			"void"
		);
	}


	@Test
	public void testLongStrings() {

		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"\"long string\"\"\"",
			"\"\"\"long \"embedded quotes\" string\"\"\""
		);

		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'''long string'''",
			"'''long 'embedded quotes' string '''"
		);
	}


	@Test
	public void testFunctions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"abs",
			"apply",
			"bool",
			"buffer",
			"callable",
			"chr",
			"classmethod",
			"cmp",
			"coerce",
			"compile",
			"complex",
			"delattr",
			"dict",
			"dir",
			"divmod",
			"enumerate",
			"eval",
			"execfile",
			"file",
			"filter",
			"getattr",
			"globals",
			"hasattr",
			"hash",
			"hex",
			"id",
			"input",
			"intern",
			"isinstance",
			"issubclass",
			"iter",
			"len",
			"list",
			"locals",
			"map",
			"max",
			"min",
			"object",
			"oct",
			"open",
			"ord",
			"pow",
			"property",
			"range",
			"raw_input",
			"reduce",
			"reload",
			"repr",
			"round",
			"setattr",
			"slice",
			"staticmethod",
			"str",
			"sum",
			"super",
			"tuple",
			"type",
			"unichr",
			"unicode",
			"vars",
			"xrange",
			"zip"
		);
	}


	@Test
	public void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"=",
			"+",
			"-",
			"*",
			"/",
			"%",
			"**",
			"~",
			"<",
			">",
			"<<",
			">>",
			"==",
			"+=",
			"-=",
			"*=",
			"/=",
			"%=",
			">>=",
			"<<=",
			"^",
			"&",
			"&&",
			"|",
			"||",
			"?",
			":",
			",",
			"!",
			"++",
			"--",
			".",
			","
		);
	}


	@Test
	public void testReservedWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
		"and",
			"as",
			"assert",
			"break",
			"class",
			"continue",
			"def",
			"del",
			"elif",
			"else",
			"except",
			"exec",
			"finally",
			"for",
			"from",
			"global",
			"if",
			"import",
			"in",
			"is",
			"lambda",
			"not",
			"or",
			"pass",
			"print",
			"raise",
			"return",
			"try",
			"while",
			"yield"
		);
	}


	@Test
	public void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(",
			")",
			"[",
			"]",
			"{",
			"}"
		);
	}


	@Test
	public void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"foobar\"",
			"\"unterminated",
			"r\"foobar\"",
			"R\"foobar\"",
			"r\"unterminated",
			"R\"unterminated",
			"u\"foobar\"",
			"ur\"foobar\"",
			"uR\"foobar\"",
			"u\"unterminated",
			"ur\"unterminated",
			"uR\"unterminated",
			"U\"foobar\"",
			"Ur\"foobar\"",
			"UR\"foobar\"",
			"U\"unterminated",
			"Ur\"unterminated",
			"UR\"unterminated"
		);
	}


	@Test
	public void testWhitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"\t",
			"\f",
			"     ",
			"  \t  ",
			"\t\t");
	}
}
