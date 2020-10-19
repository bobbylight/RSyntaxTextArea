/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.TabExpander;

/**
 * Unit tests for the {@link TokenUtils} class.
 */
public class TokenUtilsTest {

	@Test
	public void testIsBlankOrAllWhiteSpace_null() {
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpace(null));
	}

	@Test
	public void testIsBlankOrAllWhiteSpace_nullToken() {
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpace(new TokenImpl()));
	}

	@Test
	public void testIsBlankOrAllWhiteSpace_internalNonPaintableTokenType() {
		char[] chars = { 'a', 'b', 'c' };
		Token t = new TokenImpl(chars, 0, 2, 0, -7, 0);
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpace(t));
	}

	@Test
	public void testIsBlankOrAllWhiteSpace_singleWhiteSpaceToken() {
		char[] chars = { ' ' };
		Token t = new TokenImpl(chars, 0, 0, 0,
				TokenTypes.WHITESPACE, 0);
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpace(t));
	}

	@Test
	public void testIsBlankOrAllWhiteSpace_singleCommentToken() {
		char[] chars = "// This is a comment".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.COMMENT_EOL, 0);
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpace(t));
	}

	@Test
	public void testIsBlankOrAllWhiteSpace_spacesFollowedByNonSpaceNonComment() {

		char[] chars = "    ".toCharArray();
		TokenImpl t = new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.WHITESPACE, 0);

		chars = "if".toCharArray();
		t.setNextToken(new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.RESERVED_WORD, 0));

		Assert.assertFalse(TokenUtils.isBlankOrAllWhiteSpace(t));
	}

	@Test
	public void testIsBlankOrAllWhiteSpaceWithoutComments_null() {
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(null));
	}

	@Test
	public void testIsBlankOrAllWhiteSpaceWithoutComments_nullToken() {
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(new TokenImpl()));
	}

	@Test
	public void testIsBlankOrAllWhiteSpaceWithoutComments_internalNonPaintableTokenType() {
		char[] chars = { 'a', 'b', 'c' };
		Token t = new TokenImpl(chars, 0, 2, 0, -7, 0);
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(t));
	}

	@Test
	public void testIsBlankOrAllWhiteSpaceWithoutComments_singleWhiteSpaceToken() {
		char[] chars = { ' ' };
		Token t = new TokenImpl(chars, 0, 0, 0,
			TokenTypes.WHITESPACE, 0);
		Assert.assertTrue(TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(t));
	}

	@Test
	public void testIsBlankOrAllWhiteSpaceWithoutComments_singleCommentToken() {
		char[] chars = "// This is a comment".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.COMMENT_EOL, 0);
		Assert.assertFalse(TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(t));
	}

	@Test
	public void testIsBlankOrAllWhiteSpaceWithoutComments_spacesFollowedByNonSpaceNonComment() {

		char[] chars = "    ".toCharArray();
		TokenImpl t = new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.WHITESPACE, 0);

		chars = "if".toCharArray();
		t.setNextToken(new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.RESERVED_WORD, 0));

		Assert.assertFalse(TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(t));
	}

	@Test
	public void getTokenSubList_happyPath() {

		TabExpander e = (x, tabOffset) -> x + 5;

		RSyntaxTextArea textArea = new RSyntaxTextArea("package foo;\npublic class Foobar {}");
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		Token tokenList = textArea.getTokenListFor(0, textArea.getDocument().getLength());
		TokenUtils.TokenSubList actual = TokenUtils.getSubTokenList(tokenList, 15, e, textArea, 0);

		// Only verify the token list, not the x-offset, since that is font-specific, and thus
		// might vary between OS's

		Token token = actual.tokenList;
		Assert.assertEquals("blic", token.getLexeme());
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assert.assertEquals("class", token.getLexeme());
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assert.assertEquals("Foobar", token.getLexeme());
		token = token.getNextToken();
		Assert.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));
		token = token.getNextToken();
		Assert.assertTrue(token.isLeftCurly());
		token = token.getNextToken();
		Assert.assertTrue(token.isRightCurly());
		Assert.assertEquals(TokenTypes.NULL, token.getNextToken().getType());
	}

	@Test
	public void testGetWhiteSpaceTokenLength_allSpaces() {

		char[] chars = "      ".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(6, TokenUtils.getWhiteSpaceTokenLength(t, 4, 0));
	}

	@Test
	public void testGetWhiteSpaceTokenLength_tab_toNextTabStop() {

		char[] chars = "  \t".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(4, TokenUtils.getWhiteSpaceTokenLength(t, 4, 0));
	}

	@Test
	public void testGetWhiteSpaceTokenLength_tabOnly_toNextTabStop() {

		char[] chars = "\t".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 2,
			TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(2, TokenUtils.getWhiteSpaceTokenLength(t, 4, 2));
	}

	@Test
	public void testGetWhiteSpaceTokenLength_tab_onATabStop() {

		char[] chars = "    \t".toCharArray();
		Token t = new TokenImpl(chars, 0, chars.length - 1, 0,
			TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(8, TokenUtils.getWhiteSpaceTokenLength(t, 4, 0));
	}

	@Test
	public void testTokenToHtml_bold() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		char[] line = "package".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.RESERVED_WORD, 0);

		String expected = "<span style=\"font-weight: bold;color: #0000ff;\">package</span>";
		Assert.assertEquals(expected, TokenUtils.tokenToHtml(textArea, token));
	}

	@Test
	public void testTokenToHtml_italics() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		char[] line = "// comment".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.COMMENT_EOL, 0);

		String expected = "<span style=\"font-style: italic;color: #008000;\">// comment</span>";
		Assert.assertEquals(expected, TokenUtils.tokenToHtml(textArea, token));
	}

	@Test
	public void testTokenToHtml_htmlIsEscaped() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		char[] line = "<&>".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.COMMENT_EOL, 0);

		String expected = "<span style=\"font-style: italic;color: #008000;\">&lt;&amp;&gt;</span>";
		Assert.assertEquals(expected, TokenUtils.tokenToHtml(textArea, token));
	}
}
