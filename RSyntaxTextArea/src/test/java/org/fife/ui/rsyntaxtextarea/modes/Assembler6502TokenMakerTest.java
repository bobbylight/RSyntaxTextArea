/*
 * 06/05/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link Assembler6502TokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class Assembler6502TokenMakerTest extends AbstractJFlexTokenMakerTest {


	@BeforeEach
	void setUp() {
	}


	@AfterEach
	void tearDown() {
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new Assembler6502TokenMaker();
	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals(";", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
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
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'foobar'");
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"; Hello world");
	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"; Hello world https://www.sas.com",
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
	void testErrorCharLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'unterminated");
	}


	@Test
	void testErrorStringLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated");
	}

	@Test
	void testPreprocessor() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"@foo",
			"#foo",
			"#\"string\"",
			"#\"unclosed string",

			"label:"
		);
	}


	@Test
	void testNumbers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"34",
			"$0a",
			"ffh",
			"%10101"
		);
	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER, "foo");
	}


	@Test
	void testInstructions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"ADC",
			"AND",
			"ASL",
			"BCC",
			"BCS",
			"BEQ",
			"BIT",
			"BMI",
			"BNE",
			"BPL",
			"BRK",
			"BVC",
			"BVS",
			"CLC",
			"CLD",
			"CLI",
			"CLV",
			"CMP",
			"CPX",
			"CPY",
			"DEC",
			"DEX",
			"DEY",
			"EOR",
			"INC",
			"INX",
			"INY",
			"JMP",
			"JSR",
			"LDA",
			"LDX",
			"LDY",
			"LSR",
			"NOP",
			"ORA",
			"PHA",
			"PHP",
			"PLA",
			"PLP",
			"ROL",
			"ROR",
			"RTI",
			"RTS",
			"SBC",
			"SEC",
			"SED",
			"SEI",
			"STA",
			"STX",
			"STY",
			"TAX",
			"TAY",
			"TSX",
			"TXA",
			"TXS",
			"TYA",

			"DB",
			"DW",
			"DD",
			"DF",
			"DQ",
			"DT",
			"RESB",
			"RESW",
			"RESD",
			"RESQ",
			"REST",
			"EQU",
			"TEXTEQU",
			"TIMES",
			"DUP",

			/* 6502 illegal instructions */
			"LR",
			"ANC",
			"ARR",
			"AXS",
			"DCP",
			"ISC",
			"LAS",
			"LAX",
			"RLA",
			"RRA",
			"SAX",
			"SLO",
			"SRE"
		);
	}


	@Test
	void testVariables() {
		assertAllTokensOfType(TokenTypes.VARIABLE, ".varName");
	}


	@Test
	void testFunctions() {
		assertAllTokensOfType(TokenTypes.FUNCTION, "%function");
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"+",
			"-",
			"~",
			".BITNOT",
			".LOBYTE",
			".HIBYTE",
			"^",
			".BANKBYTE",
			"*",
			"/",
			".MOD",
			"&",

			".BITAND",
			".BITXOR",
			"<<",
			".SHL",
			">>",
			".SHR",
			"|",
			".BITOR",
			"=",
			"<>",
			"<",
			">",
			"<=",
			">=",

			"&&",
			".AND",
			".XOR",
			"||",
			".OR",
			"!",
			".NOT");
	}


	@Test
	void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"foobar\"");
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
