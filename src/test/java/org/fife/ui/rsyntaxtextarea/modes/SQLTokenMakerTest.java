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
 * Unit tests for the {@link SQLTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SQLTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new SQLTokenMaker();
	}


	@Test
	public void testCharLiterals() {
		String[] chars = {
			"'a'", "'\\b'",
		};
		assertAllTokensOfType(chars, TokenTypes.LITERAL_CHAR);
	}


	@Test
	public void testEolComments() {
		String[] eolCommentLiterals = {
			"-- Hello world",
		};
		assertAllTokensOfType(eolCommentLiterals, TokenTypes.COMMENT_EOL);
	}


	@Test
	public void testFloatingPointLiterals() {
		String[] floats = { "3.0", "4.2", "3.0", ".111" };
		assertAllTokensOfType(floats, TokenTypes.LITERAL_NUMBER_FLOAT);
	}


	@Test
	public void testFunctions() {

		String[] functions = {
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
				"UPPER",
		};

		assertAllTokensOfType(functions, TokenTypes.FUNCTION);
	}


	@Test
	public void testKeywords() {
		String[] keywords = {
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
				"MAX",
				"MEMO",
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
				"TIME",
				"TIMESTAMP",
				"TOP",
				"TRANSFORM",
				"TYPE",
				"UNION",
				"UNIQUE",
				"UPDATE",
				"USER",
				"VALUE",
				"VALUES",
				"VAR",
				"VARBINARY",
				"VARCHAR",
				"VARP",
				"WHERE",
				"WITH",
				"YESNO",
		};
		assertAllTokensOfType(keywords, TokenTypes.RESERVED_WORD);
	}


	@Test
	public void testMultiLineComments() {

		String[] mlcLiterals = {
			"/* Hello world */",
		};

		assertAllTokensOfType(mlcLiterals, TokenTypes.COMMENT_MULTILINE);
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