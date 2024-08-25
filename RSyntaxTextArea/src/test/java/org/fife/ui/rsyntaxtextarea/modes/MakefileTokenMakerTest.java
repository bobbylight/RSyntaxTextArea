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
 * Unit tests for the {@link MakefileTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class MakefileTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new MakefileTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER || i == TokenTypes.VARIABLE;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testBacktickLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			"`a`",
			"`foobar`"
		);
	}


	@Test
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'a'",
			"'\\b'",
			"'\\t'",
			"'\\r'",
			"'\\f'",
			"'\\n'",
			"'\\u00fe'",
			"'\\u00FE'",
			"'\\111'",
			"'\\222'",
			"'\\333'",
			"'\\11'",
			"'\\22'",
			"'\\33'",
			"'\\1'"
		);
	}


	@Test
	void testCharLiterals_unclosed() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'a",
			"'another unclosed char literal",
			"'unclosed with \\' an escaped single quote"
		);
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"# Hello world"
		);
	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"clean",
			"build",
			"build.all",
			"build-all",
			"build_all"
		);
	}


	@Test
	void testIntegerLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"0",
			"42",
			"77777"
		);
	}


	@Test
	void testKeywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"addprefix",
			"addsuffix",
			"basename",
			"dir",
			"filter",
			"filter-out",
			"findstring",
			"firstword",
			"foreach",
			"join",
			"notdir",
			"origin",
			"pathsubst",
			"shell",
			"sort",
			"strip",
			"suffix",
			"wildcard",
			"word",
			"words",
			"ifeq",
			"ifneq",
			"else",
			"endif",
			"define",
			"endef",
			"ifdef",
			"ifndef"
		);
	}


	@Test
	void testLabels() {
		assertAllTokensOfType(TokenTypes.PREPROCESSOR,
			"clean:",
			"build:",
			"build.all:",
			"build-all:",
			"build_all:"
		);
	}


	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"=",
			":=",
			"+=",
			"?="
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
	void testStringLiterals_unclosed() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unclosed string literal",
			"\"unclosed \\\" with an escaped double quote"
		);
	}


}
