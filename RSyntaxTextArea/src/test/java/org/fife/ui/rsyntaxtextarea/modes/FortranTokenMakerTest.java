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
 * Unit tests for the {@link FortranTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class FortranTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@BeforeEach
	void setUp() {
	}


	@AfterEach
	void tearDown() {
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new FortranTokenMaker();
	}


	@Test
	void testBeginningOfLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"C* This is a comment"
		);
	}


	@Test
	void testBeginningOfLineDocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			"D This is a comment"
		);
	}


	@Test
	void testBoolean() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			".true.",
			".false."
		);
	}


	@Test
	void testChars() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'foobar'",
			"'unterminated"
		);
	}


	@Test
	void testEndOfLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL, "! This is a comment");
	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("!", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER, "foo");
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"<",
			">",
			"<=",
			">=",
			"&",
			"/=",
			"==",

			".lt.",
			".gt.",
			".eq.",
			".ne.",
			".le.",
			".ge.",
			".and.",
			".or."
		);
	}


	@Test
	void testReservedWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"INCLUDE",
			"PROGRAM",
			"MODULE",
			"SUBROUTINE",
			"FUNCTION",
			"CONTAINS",
			"USE",
			"CALL",
			"RETURN",
			"IMPLICIT",
			"EXPLICIT",
			"NONE",
			"DATA",
			"PARAMETER",
			"ALLOCATE",
			"ALLOCATABLE",
			"ALLOCATED",
			"DEALLOCATE",
			"INTEGER",
			"REAL",
			"DOUBLE",
			"PRECISION",
			"COMPLEX",
			"LOGICAL",
			"CHARACTER",
			"DIMENSION",
			"KIND",
			"CASE",
			"SELECT",
			"DEFAULT",
			"CONTINUE",
			"CYCLE",
			"DO",
			"WHILE",
			"ELSE",
			"IF",
			"ELSEIF",
			"THEN",
			"ELSEWHERE",
			"END",
			"ENDIF",
			"ENDDO",
			"FORALL",
			"WHERE",
			"EXIT",
			"GOTO",
			"PAUSE",
			"STOP",
			"BACKSPACE",
			"CLOSE",
			"ENDFILE",
			"INQUIRE",
			"OPEN",
			"PRINT",
			"READ",
			"REWIND",
			"WRITE",
			"FORMAT",
			"AIMAG",
			"AINT",
			"AMAX0",
			"AMIN0",
			"ANINT",
			"CEILING",
			"CMPLX",
			"CONJG",
			"DBLE",
			"DCMPLX",
			"DFLOAT",
			"DIM",
			"DPROD",
			"FLOAT",
			"FLOOR",
			"IFIX",
			"IMAG",
			"INT",
			"LOGICAL",
			"MODULO",
			"NINT",
			"REAL",
			"SIGN",
			"SNGL",
			"TRANSFER",
			"ZEXT",
			"ABS",
			"ACOS",
			"AIMAG",
			"AINT",
			"ALOG",
			"ALOG10",
			"AMAX0",
			"AMAX1",
			"AMIN0",
			"AMIN1",
			"AMOD",
			"ANINT",
			"ASIN",
			"ATAN",
			"ATAN2",
			"CABS",
			"CCOS",
			"CHAR",
			"CLOG",
			"CMPLX",
			"CONJG",
			"COS",
			"COSH",
			"CSIN",
			"CSQRT",
			"DABS",
			"DACOS",
			"DASIN",
			"DATAN",
			"DATAN2",
			"DBLE",
			"DCOS",
			"DCOSH",
			"DDIM",
			"DEXP",
			"DIM",
			"DINT",
			"DLOG",
			"DLOG10",
			"DMAX1",
			"DMIN1",
			"DMOD",
			"DNINT",
			"DPROD",
			"DREAL",
			"DSIGN",
			"DSIN",
			"DSINH",
			"DSQRT",
			"DTAN",
			"DTANH",
			"EXP",
			"FLOAT",
			"IABS",
			"ICHAR",
			"IDIM",
			"IDINT",
			"IDNINT",
			"IFIX",
			"INDEX",
			"INT",
			"ISIGN",
			"LEN",
			"LGE",
			"LGT",
			"LLE",
			"LLT",
			"LOG",
			"LOG10",
			"MAX",
			"MAX0",
			"MAX1",
			"MIN",
			"MIN0",
			"MIN1",
			"MOD",
			"NINT",
			"REAL",
			"SIGN",
			"SIN",
			"SINH",
			"SNGL",
			"SQRT",
			"TAN",
			"TANH"
		);
	}


	@Test
	void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"foobar\"",
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
