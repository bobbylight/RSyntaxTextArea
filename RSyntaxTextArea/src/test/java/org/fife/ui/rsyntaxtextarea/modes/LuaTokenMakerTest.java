/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link LuaTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LuaTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@BeforeEach
	void setUp() {
	}


	@AfterEach
	void tearDown() {
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new LuaTokenMaker();
	}


	@Test
	void testAllNumbers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_FLOAT,
			"34",
			"34.",
			"34.1",
			".34",
			"34e7",
			"34e+7",
			"34e-7",
			"34.e7",
			"34.1e7",
			"34.1e+7",
			"34.1e-7"
		);
	}


	@Test
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'foobar'",
			"'Dwayne \\'The Rock\\' Johnson'"
		);
	}


	@Test
	void testUnterminatedCharLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'unterminated"
		);
	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("--", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER, "foo");
	}


	@Test
	void testComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL, "-- This is a comment");
	}


	@Test
	void testDataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			"<number>",
			"<name>",
			"<string>",
			"<eof>",
			"NULL"
		);
	}


	@Test
	void testFunctions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"_G",
			"_VERSION",
			"assert",
			"collectgarbage",
			"dofile",
			"error",
			"getfenv",
			"getmetatable",
			"ipairs",
			"load",
			"loadfile",
			"loadstring",
			"module",
			"next",
			"pairs",
			"pcall",
			"print",
			"rawequal",
			"rawget",
			"rawset",
			"require",
			"select",
			"setfenv",
			"setmetatable",
			"tonumber",
			"tostring",
			"type",
			"unpack",
			"xpcall"
		);
	}


	@Test
	void testLongStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"[[Unterminated long string",
			"[[Terminated long string]]"
		);
	}


	@Test
	void testMultilineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"--[[ This is a multi-line comment ]]"
		);
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,

			// arithmetic operators
			"+",
			"-",
			"*",
			"/",
			"^",
			"%",

			// relational operators
			"<",
			">",
			"<=",
			">=",
			"==",
			"~=",

			// logical operators
			"and",
			"or",
			"not",
			"#",

			"..",
			"..."
		);
	}


	@Test
	void testReservedWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"break",
			"do",
			"else",
			"elseif",
			"end",
			"for",
			"function",
			"goto",
			"if",
			"in",
			"local",
			"nil",
			"repeat",
			"return",
			"then",
			"until",
			"while"
		);
	}


	@Test
	void testSeparators() {
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
	void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"foobar\"",
			"\"Dwayne \\\"The Rock\\\" Johnson\""
		);
	}


	@Test
	void testUnterminatedStrings() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated"
		);
	}


	@Test
	void testWhitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"\t",
			"\f",
			"     ",
			"  \t  ",
			"\t\t");
	}
}
