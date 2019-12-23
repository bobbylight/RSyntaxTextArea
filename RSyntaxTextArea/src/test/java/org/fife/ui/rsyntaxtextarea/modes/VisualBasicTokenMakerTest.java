/*
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
 * Unit tests for the {@link VisualBasicTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class VisualBasicTokenMakerTest extends AbstractTokenMakerTest2 {


	@Before
	public void setUp() {
	}


	@After
	public void tearDown() {
	}


	@Override
	protected TokenMaker createTokenMaker() {
		return new VisualBasicTokenMaker();
	}


	@Test
	public void testBooleans() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			"true",
			"false"
		);
	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("'", startAndEnd[0]);
		Assert.assertNull(startAndEnd[1]);
	}


	@Test
	public void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER, "foo");
	}


	@Test
	public void testDataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			"Boolean",
			"Byte",
			"Char",
			"Date",
			"Decimal",
			"Double",
			"Integer",
			"Long",
			"Object",
			"SByte",
			"Short",
			"Single",
			"String",
			"UInteger",
			"ULong",
			"UShort"
		);
	}


	@Test
	public void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"' Hello world");
	}


	@Test
	public void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"' Hello world http://www.sas.com",
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
	public void testNumbers_floats() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_FLOAT,
			"34.",
			"34.1",
			".34",
			"34e7",
			"34.e7",
			"34.1e7",
			"34.1e+7",
			"34.1e-7"
		);
	}


	@Test
	public void testNumbers_hex() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_HEXADECIMAL,
			"&h3a",
			"&h3aUI",
			"&h3aUL",
			"&h3aUS"
		);
	}


	@Test
	public void testNumbers_integers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"34",
			"34UI",
			"34UL",
			"34US"
		);
	}


	@Test
	public void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"&",
			"&=",
			"*",
			"*=",
			"+",
			"+=",
			"=",
			"-",
			"-=",
			"<<",
			"<<=",
			">>",
			">>=",
			"/",
			"/=",
			"\\",
			"\\=",
			"^",
			"^="
		);
	}


	@Test
	public void testReservedWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"AddHandler",
			"AddressOf",
			"Alias",
			"And",
			"AndAlso",
			"As",
			"ByRef",
			"ByVal",
			"Call",
			"Case",
			"Catch",
			"CBool",
			"CByte",
			"CChar",
			"CDate",
			"CDbl",
			"CDec",
			"CInt",
			"Class",
			"CLng",
			"CObj",
			"Const",
			"Continue",
			"CSByte",
			"CShort",
			"CSng",
			"CStr",
			"CType",
			"CUInt",
			"CULng",
			"CUShort",
			"Declare",
			"Default",
			"Delegate",
			"Dim",
			"DirectCast",
			"Do",
			"Each",
			"Else",
			"ElseIf",
			"End",
			"EndIf",
			"Enum",
			"Erase",
			"Error",
			"Event",
			"Exit",
			"Finally",
			"For",
			"Friend",
			"Function",
			"Get",
			"GetType",
			"GetXMLNamespace",
			"Global",
			"GoSub",
			"GoTo",
			"Handles",
			"If",
			"If",
			"Implements",
			"Imports",
			"In",
			"Inherits",
			"Interface",
			"Is",
			"IsNot",
			"Let",
			"Lib",
			"Like",
			"Loop",
			"Me",
			"Mod",
			"Module",
			"Module Statement",
			"MustInherit",
			"MustOverride",
			"MyBase",
			"MyClass",
			"Namespace",
			"Narrowing",
			"New",
			"New",
			"Next",
			"Not",
			"Nothing",
			"NotInheritable",
			"NotOverridable",
			"Of",
			"On",
			"Operator",
			"Option",
			"Optional",
			"Or",
			"OrElse",
			"Out",
			"Overloads",
			"Overridable",
			"Overrides",
			"ParamArray",
			"Partial",
			"Private",
			"Property",
			"Protected",
			"Public",
			"RaiseEvent",
			"ReadOnly",
			"ReDim",
			"REM",
			"RemoveHandler",
			"Resume",
			"Select",
			"Set",
			"Shadows",
			"Shared",
			"Static",
			"Step",
			"Stop",
			"Structure",
			"Sub",
			"SyncLock",
			"Then",
			"Throw",
			"To",
			"Try",
			"TryCast",
			"TypeOf",
			"Using",
			"Variant",
			"Wend",
			"When",
			"While",
			"Widening",
			"With",
			"WithEvents",
			"WriteOnly",
			"Xor"
		);
	}


	@Test
	public void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(",
			")"
		);
	}


	@Test
	public void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"foobar\""
		);
	}


	@Test
	public void testUnterminatedStrings() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated"
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
