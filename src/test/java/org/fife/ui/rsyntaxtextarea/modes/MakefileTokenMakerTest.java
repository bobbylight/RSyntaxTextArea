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
 * Unit tests for the {@link MakefileTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class MakefileTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new MakefileTokenMaker();
	}


	@Test
	public void testBacktickLiterals() {
		String[] chars = {
				"`a`", "`foobar`",
		};
		assertAllTokensOfType(chars, TokenTypes.LITERAL_BACKQUOTE);
	}


	@Test
	public void testCharLiterals() {
		String[] chars = {
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
		};
		assertAllTokensOfType(chars, TokenTypes.LITERAL_CHAR);
	}


	@Test
	public void testEolComments() {
		String[] eolCommentLiterals = {
			"# Hello world",
		};
		assertAllTokensOfType(eolCommentLiterals, TokenTypes.COMMENT_EOL);
	}


	@Test
	public void testIntegerLiterals() {
		String[] intLiterals = {
				"0", "42", "77777",
		};
		assertAllTokensOfType(intLiterals, TokenTypes.LITERAL_NUMBER_DECIMAL_INT);
	}


	@Test
	public void testKeywords() {
		String[] keywords = {
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
				"ifndef",
		};
		assertAllTokensOfType(keywords, TokenTypes.RESERVED_WORD);
	}


	@Test
	public void testStringLiterals() {
		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"",
		};
		assertAllTokensOfType(stringLiterals, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE);
	}


}