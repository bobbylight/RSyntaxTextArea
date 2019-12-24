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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link Assembler6502TokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Assembler6502TokenMakerTest extends AbstractTokenMakerTest2 {


	@Before
	public void setUp() {
	}


	@After
	public void tearDown() {
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new Assembler6502TokenMaker();
	}


	@Test
	public void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'foobar'");
	}


	@Test
	public void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"; Hello world");
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
	public void testErrorCharLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'unterminated");
	}


	@Test
	public void testErrorStringLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated");
	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals(";", startAndEnd[0]);
		Assert.assertNull(startAndEnd[1]);
	}

	@Test
	public void testPreprocessor() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"@foo",
			"#foo",
			"#\"string\"",
			"#\"unclosed string",

			"label:"
		);
	}


	@Test
	public void testNumbers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"34",
			"$0a",
			"ffh",
			"%10101"
		);
	}


	@Test
	public void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER, "foo");
	}


	@Test
	public void testInstructions() {
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
	public void testVariables() {
		assertAllTokensOfType(TokenTypes.VARIABLE, ".varName");
	}


	@Test
	public void testFunctions() {
		assertAllTokensOfType(TokenTypes.FUNCTION, "%function");
	}


	@Test
	public void testOperators() {
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
