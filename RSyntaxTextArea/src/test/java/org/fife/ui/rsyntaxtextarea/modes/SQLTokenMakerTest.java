/*
 * 06/06/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link SQLTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class SQLTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new SQLTokenMaker();
	}


	@Test
	@Override
	protected void testCommon_getCurlyBracesDenoteCodeBlocks() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(0));
	}


	@Test
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'a'",
			"'\\b'",
			"''''"
		);
	}


	@Test
	void testCharLiterals_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			TokenTypes.LITERAL_CHAR,
			"continued from previous line'",
			"continued from previous line but also still not terminated"
		);
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"-- Hello world"
		);
	}


	@Test
	void testFloatingPointLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_FLOAT,
			"3.0",
			"4.2",
			"3.0",
			".111"
		);
	}


	@Test
	void testFunctions() {

		assertAllTokensOfType(TokenTypes.FUNCTION,
			/* SQL99 aggregate functions */
			"AVG",
			//"COUNT",
			//"MIN",
			//"MAX",
			//"SUM",

			/* SQL99 built-in scalar functions */
			"CURRENT_DATE",
			"CURRENT_TIME",
			"CURRENT_TIMESTAMP",
			"CURRENT_USER",
			"SESSION_USER",
			"SYSTEM_USER",

			/* SQL99 numeric scalar functions */
			"BIT_LENGTH",
			"CHAR_LENGTH",
			"EXTRACT",
			"OCTET_LENGTH",
			"POSITION",

			/* SQL99 string functions */
			"CONCATENATE",
			"CONVERT",
			"LOWER",
			"SUBSTRING",
			"TRANSLATE",
			"TRIM",
			"UPPER"
		);
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("--", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
	}


	@Test
	void testIntegerLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"0",
			"598"
		);
	}


	@Test
	void testKeywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"ADD",
			"ALL",
			"ALTER",
			"AND",
			"ANY",
			"AS",
			"ASC",
			"AUTOINCREMENT",
			"AVA",
			"BETWEEN",
			"BINARY",
			"BIT",
			"BOOLEAN",
			"BY",
			"BYTE",
			"CASE",
			"CHAR",
			"CHARACTER",
			"COLUMN",
			"CONSTRAINT",
			"COUNT",
			"COUNTER",
			"CREATE",
			"CURRENCY",
			"DATABASE",
			"DATE",
			"DATETIME",
			"DELETE",
			"DESC",
			"DISALLOW",
			"DISTINCT",
			"DISTINCTROW",
			"DOUBLE",
			"DROP",
			"END",
			"ELSE",
			"EXISTS",
			"FLOAT",
			"FLOAT4",
			"FLOAT8",
			"FOREIGN",
			"FROM",
			"GENERAL",
			"GROUP",
			"GUID",
			"HAVING",
			"INNER",
			"INSERT",
			"IGNORE",
			"IMP",
			"IN",
			"INDEX",
			"INT",
			"INTEGER",
			"INTEGER1",
			"INTEGER2",
			"INTEGER4",
			"INTO",
			"IS",
			"JOIN",
			"KEY",
			"LEFT",
			"LEVEL",
			"LIKE",
			"LOGICAL",
			"LONG",
			"LONGBINARY",
			"LONGTEXT",
			"MATCHED",
			"MAX",
			"MEMO",
			"MERGE",
			"MIN",
			"MOD",
			"MONEY",
			"NOT",
			"NULL",
			"NUMBER",
			"NUMERIC",
			"OLEOBJECT",
			"ON",
			"OPTION",
			"OR",
			"ORDER",
			"OUTER",
			"OWNERACCESS",
			"PARAMETERS",
			"PASSWORD",
			"PERCENT",
			"PIVOT",
			"PRIMARY",
			"REAL",
			"REFERENCES",
			"RIGHT",
			"SELECT",
			"SET",
			"SHORT",
			"SINGLE",
			"SMALLINT",
			"SOME",
			"STDEV",
			"STDEVP",
			"STRING",
			"SUM",
			"TABLE",
			"TABLEID",
			"TEXT",
			"THEN",
			"TIME",
			"TIMESTAMP",
			"TOP",
			"TRANSFORM",
			"TYPE",
			"UNION",
			"UNIQUE",
			"UPDATE",
			"USER",
			"USING",
			"VALUE",
			"VALUES",
			"VAR",
			"VARBINARY",
			"VARCHAR",
			"VARP",
			"WHEN",
			"WHERE",
			"WITH",
			"YESNO"
		);
	}


	@Test
	void testMultiLineComments() {

		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"/* Hello world */",
			"/* Hello world unterminated",
			"/**/"
		);
	}


	@Test
	void testMultiLineComments_fromPreviousLine() {

		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE, TokenTypes.COMMENT_MULTILINE,
			" this is a continued comment */",
			"continued from prior line unterminated"
		);
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			">=",
			"<=",
			"<>",
			">",
			"<",
			"=",
			"+",
			"-",
			"*",
			"/"
		);
	}


	@Test
	void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(",
			")"
		);
	}


	@Test
	void testSquareBracketIdentifiers() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"[users]",
			"[user id]"
		);
	}


	@Test
	void testStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"",
			"\"hi\"",
			"\"\\u00fe\"",
			"\"\\\"\""
		);
	}


	@Test
	void testStringLiterals_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"this is continued from the previous line\"",
			"this is continued from the previous line but also still not terminated"
		);
	}


	@Test
	void testWhitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"\t",
			"   ",
			"\t\t",
			"   \t\t   "
		);
	}
}
