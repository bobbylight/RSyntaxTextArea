/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.junit.jupiter.api.Test;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;


/**
 * Unit tests for the {@link VhdlTokenMaker} class.
 *
 * @author DOUDOU DIAWARA
 * @version 0.0
 */
public class VhdlTokenMakerTest extends  AbstractJFlexTokenMakerTest {

	@Override
	protected TokenMaker createTokenMaker() {
		return new VhdlTokenMaker();
	}

	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("--", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}

	@Test
	void testComments(){
		assertAllTokensOfType(TokenTypes.COMMENT_EOL, "-- This is a comment in VHDL");
	}

	@Test
	void testBooleans() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"true",
			"false");
	}

	@Test
	void testDataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			"bit",
				"bit_vector",
				"boolean",
				"integer",
				"real",
				"natural",
				"positive",
				"std_logic",
				"std_logic_unsigned",
				"std_logic_vector",
				"std_logic_signed"
		);
	}

	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"&",
				"**",
				"abs",
				"mod",
				"rem",
				"sll",
				"srl",
				"sla",
				"sra",
				"rol",
				"ror",
				"not",
				"and",
				"or",
				"nand",
				"nor",
				"xor",
				"xnor"
		);
	}

	@Test
	void testFunction(){
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"'-'",
				"'0'",
				"'1'",
				"'H'",
				"'L'",
				"'U'",
				"'W'",
				"'X'",
				"'Z'",
				"false",
				"true");
	}

	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo"
		);
	}

	@Test
	void testIdentifiers_error() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			"^"
		);
	}

	@Test
	void testReserveWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"access" ,
				"after",
				"alias",
				"all",
				"architecture",
				"array",
				"assert",
				"attribute",
				"begin",
				"block",
				"body",
				"buffer",
				"bus",
				"case",
				"component",
				"configuration",
				"constant",
				"disconnect",
				"downto",
				"else",
				"elsif",
				"end",
				"end",
				"entity",
				"exit",
				"file",
				"for",
				"function",
				"generate",
				"generic",
				"group",
				"guarded",
				"if",
				"impure",
				"in",
				"inertial",
				"inout",
				"is",
				"label",
				"library",
				"linkage",
				"literal",
				"loop",
				"map",
				"new",
				"rising_edge",
				"next",
				"null",
				"of" ,
				"on" ,
				"open" ,
				"others",
				"out",
				"package",
				"port",
				"postponed",
				"procedure",
				"process",
				"pure",
				"range" ,
				"record" ,
				"register",
				"reject" ,
				"report" ,
				"return" ,
				"select" ,
				"severity",
				"shared",
				"signal",
				"subtype",
				"then",
				"to",
				"transport",
				"type",
				"unaffected",
				"units",
				"until",
				"use",
				"variable",
				"wait",
				"when",
				"while",
				"with"
			);
	}

	@Test
	void testOptionalReservedWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"'ACTIVE",
				"'ASCENDING",
				"'BASE",
				"'DELAYED",
				"'DRIVING",
				"'DRIVING_VALUE",
				"'EVENT",
				"'HIGH",
				"'IMAGE",
				"'INSTANCE_NAME",
				"'LAST_ACTIVE",
				"'LAST_EVENT",
				"'LAST_VALUE",
				"'LEFT",
				"'LEFTOF",
				"'LOW",
				"'PATH_NAME",
				"'POS",
				"'PRED",
				"'QUIET",
				"'RANGE",
				"'REVERSE_RANGE",
				"'RIGHT",
				"'RIGHTOF",
				"'SIMPLE_NAME",
				"'STABLE",
				"'SUCCESS",
				"'TRANSACTION",
				"'VAL",
				"'VALUE"
		);
	}

	@Test
	void testNumber() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"42",
			"-1",
			"3.14",
			"3.14E2",
			"2.17",
			"1010",
			"0001",
			"b\"1111\"",
			"x\"2A\"",
			"X\"FF\"",
			"o\"55\"",
			"O\"37\""
			);
	}

	@Test
	void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(",
				")",
				"[",
				"]",
				";",
				",",
				":",
				"|",
				"=>",
				"<=",
				":=");

	}

	@Test
	void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"00\"",
			"\"test started\"",
			"\"test finished\"",
			"\"1111\"",
			"\"Value\"",
			"\"foobar\"",
			"\"foobar\""
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
			"\t\t"
		);

	}
}
