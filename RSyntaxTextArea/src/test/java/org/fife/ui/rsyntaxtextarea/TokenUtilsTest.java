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
