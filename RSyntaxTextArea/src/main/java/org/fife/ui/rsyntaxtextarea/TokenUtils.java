package org.fife.ui.rsyntaxtextarea;

import javax.swing.text.TabExpander;
import java.awt.*;


/**
 * Utility methods for dealing with tokens.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public final class TokenUtils {


	private TokenUtils() {
		// Do nothing (comment for Sonar)
	}


	/**
	 * Modifies the passed-in token list to start at the specified offset.
	 * For example, if the token list covered positions 20-60 in the document
	 * (inclusive) like so:
	 * <pre>
	 *   [token1] -&gt; [token2] -&gt; [token3] -&gt; [token4]
	 *   20     30   31     40   41     50   51     60
	 * </pre>
	 * and you used this method to make the token list start at position 44,
	 * then the token list would be modified to be the following:
	 * <pre>
	 *   [part-of-old-token3] -&gt; [token4]
	 *   44                 50   51     60
	 * </pre>
	 * Tokens that come before the specified position are forever lost, and
	 * the token containing that position is made to begin at that position if
	 * necessary.  All token types remain the same as they were originally.<p>
	 *
	 * This method can be useful if you are only interested in part of a token
	 * list (i.e., the line it represents), but you don't want to modify the
	 * token list yourself.
	 *
	 * @param tokenList The list to make start at the specified position.
	 *        This parameter is modified.
	 * @param pos The position at which the new token list is to start.  If
	 *        this position is not in the passed-in token list, the
	 *        returned token list will either be <code>null</code> or the
	 *        unpaintable token(s) at the end of the passed-in token list.
	 * @param e How to expand tabs.
	 * @param textArea The text area from which the token list came.
	 * @param x0 The initial x-pixel position of the old token list.
	 * @return Information about the "sub" token list.  This will be
	 *         <code>null</code> if <code>pos</code> was not a valid offset
	 *         into the token list.
	 * @see #getSubTokenList(Token, int, TabExpander, RSyntaxTextArea, float, TokenImpl)
	 */
	public static TokenSubList getSubTokenList(Token tokenList, int pos,
									TabExpander e,
									final RSyntaxTextArea textArea,
									float x0) {
		return getSubTokenList(tokenList, pos, e, textArea, x0, null);
	}


	/**
	 * Modifies the passed-in token list to start at the specified offset.
	 * For example, if the token list covered positions 20-60 in the document
	 * (inclusive) like so:
	 * <pre>
	 *   [token1] -&gt; [token2] -&gt; [token3] -&gt; [token4]
	 *   20     30   31     40   41     50   51     60
	 * </pre>
	 * and you used this method to make the token list start at position 44,
	 * then the token list would be modified to be the following:
	 * <pre>
	 *   [part-of-old-token3] -&gt; [token4]
	 *   44                 50   51     60
	 * </pre>
	 * Tokens that come before the specified position are forever lost, and
	 * the token containing that position is made to begin at that position if
	 * necessary.  All token types remain the same as they were originally.<p>
	 *
	 * This method can be useful if you are only interested in part of a token
	 * list (i.e., the line it represents), but you don't want to modify the
	 * token list yourself.
	 *
	 * @param tokenList The list to make start at the specified position.
	 *        This parameter is modified.
	 * @param pos The position at which the new token list is to start.  If
	 *        this position is not in the passed-in token list, the
	 *        returned token list will either be <code>null</code> or the
	 *        unpaintable token(s) at the end of the passed-in token list.
	 * @param e How to expand tabs.
	 * @param textArea The text area from which the token list came.
	 * @param x0 The initial x-pixel position of the old token list.
	 * @param tempToken A temporary token to use  when creating the token list
	 *        result.  This may be <code>null</code> but callers can pass in
	 *        a "buffer" token for performance if desired.
	 * @return Information about the "sub" token list.  This will be
	 *         <code>null</code> if <code>pos</code> was not a valid offset
	 *         into the token list.
	 * @see #getSubTokenList(Token, int, TabExpander, RSyntaxTextArea, float)
	 */
	public static TokenSubList getSubTokenList(Token tokenList, int pos,
									TabExpander e,
									final RSyntaxTextArea textArea,
									float x0,
									TokenImpl tempToken) {

		if (tempToken==null) {
			tempToken = new TokenImpl();
		}
		Token t = tokenList;

		// Loop through the token list until you find the one that contains
		// pos.  Remember the cumulative width of all of these tokens.
		while (t!=null && t.isPaintable() && !t.containsPosition(pos)) {
			x0 += t.getWidth(textArea, e, x0);
			t = t.getNextToken();
		}

		// Make the token that contains pos start at pos.
		if (t!=null && t.isPaintable()) {

			if (t.getOffset()!=pos) {
				// Number of chars between p0 and token start.
				int difference = pos - t.getOffset();
				x0 += t.getWidthUpTo(t.length()-difference+1, textArea, e, x0);
				tempToken.copyFrom(t);
				tempToken.makeStartAt(pos);

				return new TokenSubList(tempToken, x0);

			}
			else { // t.getOffset()==pos
				return new TokenSubList(t, x0);
			}

		}

		// This could be a null token, so we need to just return it.
		return new TokenSubList(tokenList, x0);
		//return null;

	}


	/**
	 * Returns the length, in characters, of a whitespace token, taking tabs
	 * into account.
	 *
	 * @param t The token.  This should be of type {@link TokenTypes#WHITESPACE}.
	 * @param tabSize The tab size in the editor.
	 * @param curOffs The offset of the token in the current line.
	 * @return The length of the token, in characters.
	 */
	public static int getWhiteSpaceTokenLength(Token t, int tabSize, int curOffs) {

		int length = 0;

		for (int i = 0; i < t.length(); i++) {
			char ch = t.charAt(i);
			if (ch == '\t') {
				int newCurOffs = (curOffs + tabSize) / tabSize * tabSize;
				length += newCurOffs - curOffs;
				curOffs = newCurOffs;
			}
			else {
				length++;
				curOffs++;
			}
		}

		return length;
	}


	/**
	 * Returns whether a token list is {@code null}, empty, or all
	 * whitespace.
	 *
	 * @param t The token.
	 * @return Whether the token list starting with that token is {@code null},
	 *         empty, or all whitespace.
	 */
	public static boolean isBlankOrAllWhiteSpace(Token t) {

		while (t != null && t.isPaintable()) {
			if (!t.isCommentOrWhitespace()) {
				return false;
			}
			t = t.getNextToken();
		}
		return true;
	}


	/**
	 * Generates HTML that renders a token with the style used in an RSTA instance.
	 * Note this HTML is not concise.  It is a straightforward implementation to be
	 * used to generate markup used in copy/paste and dnd scenarios.
	 *
	 * @param textArea The text area whose styles to use.
	 * @param token The token to get equivalent HTML for.
	 * @return The HTML.
	 */
	public static String tokenToHtml(RSyntaxTextArea textArea, Token token) {

		StringBuilder style = new StringBuilder();

		Font font = textArea.getFontForTokenType(token.getType());
		if (font.isBold()) {
			style.append("font-weight: bold;");
		}
		if (font.isItalic()) {
			style.append("font-style: italic;");
		}

		Color c = textArea.getForegroundForToken(token);
		style.append("color: ").append(HtmlUtil.getHexString(c)).append(";");

		return "<span style=\"" + style + "\">" +
			HtmlUtil.escapeForHtml(token.getLexeme(), "\n", true) +
			"</span>";
	}


	/**
	 * A sub-list of tokens.
	 */
	@SuppressWarnings({ "checkstyle:visibilitymodifier" })
	public static class TokenSubList {

		/**
		 * The "sub" token list.
		 */
		public Token tokenList;

		/**
		 * The width, in pixels, of the part of the token list "removed from
		 * the front."  This way, you know the x-offset of the "new" token list.
		 */
		public float x;

		public TokenSubList(Token tokenList, float x) {
			this.tokenList = tokenList;
			this.x = x;
		}

	}


}
