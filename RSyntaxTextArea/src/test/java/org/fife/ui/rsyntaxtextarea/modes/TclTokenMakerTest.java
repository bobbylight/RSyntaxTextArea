/*
 * 12/15/2019
 *
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
 * Unit tests for the {@link TclTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TclTokenMakerTest extends AbstractTokenMakerTest2 {


	@Before
	public void setUp() {
	}


	@After
	public void tearDown() {
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new TclTokenMaker();
	}


	@Test
	public void testDecimalNumbers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_FLOAT,
			"34f",
			"34F",
			"34d",
			"34D",
			"34.0",
			"34.0f",
			"34.0F",
			"34.0d",
			"34.0D",
			"3e7"
		);
	}


	@Test
	public void testErrorStringLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated");
	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("#", startAndEnd[0]);
		Assert.assertNull(startAndEnd[1]);
	}


	@Test
	public void testHexNumbers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_HEXADECIMAL,
			"0x34",
			"0x34L",
			"0x34l"
		);
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
		assertAllTokensOfType( TokenTypes.IDENTIFIER, "foo");
	}


	@Test
	public void testReservedWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"append",
			"array",
			"auto_mkindex",
			"concat",
			"console",
			"eval",
			"expr",
			"format",
			"global",
			"set",
			"trace",
			"unset",
			"upvar",
			"join",
			"lappend",
			"lindex",
			"linsert",
			"list",
			"llength",
			"lrange",
			"lreplace",
			"lsearch",
			"lsort",
			"split",
			"scan",
			"string",
			"regexp",
			"regsub",
			"if",
			"else",
			"elseif",
			"switch",
			"for",
			"foreach",
			"while",
			"break",
			"continue",
			"proc",
			"return",
			"source",
			"unkown",
			"uplevel",
			"cd",
			"close",
			"eof",
			"file",
			"flush",
			"gets",
			"glob",
			"open",
			"read",
			"puts",
			"pwd",
			"seek",
			"tell",
			"catch",
			"error",
			"exec",
			"pid",
			"after",
			"time",
			"exit",
			"history",
			"rename",
			"info",
			"ceil",
			"floor",
			"round",
			"incr",
			"hypot",
			"abs",
			"acos",
			"cos",
			"cosh",
			"asin",
			"sin",
			"sinh",
			"atan",
			"atan2",
			"tan",
			"tanh",
			"log",
			"log10",
			"fmod",
			"pow",
			"hypot",
			"sqrt",
			"double",
			"int",

			"bind",
			"button",
			"canvas",
			"checkbutton",
			"destroy",
			"entry",
			"focus",
			"frame",
			"grab",
			"image",
			"label",
			"listbox",
			"lower",
			"menu",
			"menubutton",
			"message",
			"option",
			"pack",
			"placer",
			"radiobutton",
			"raise",
			"scale",
			"scrollbar",
			"selection",
			"send",
			"text",
			"tk",
			"tkerror",
			"tkwait",
			"toplevel",
			"update",
			"winfo",
			"wm"
		);
	}


	@Test
	public void testComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL, "# This is a comment");
	}


	@Test
	public void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"=", "!", "+", "-", "*", "/", ">", ">=", "<", "<=", "%", "&", "|", "^", "~"
		);
	}


	@Test
	public void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(", ")", "[", "]", "{", "}"
		);
	}


	@Test
	public void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"foobar\"");
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
