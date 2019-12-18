package org.fife.ui.rsyntaxtextarea;

import java.awt.*;

/**
 * Utility methods useful when generating HTML representations of RSTA content.
 */
public final class HtmlUtil {

	private HtmlUtil() {
		// Do nothing (comment for Sonar)
	}


	/**
	 * Returns a string with characters that are special to HTML (such as
	 * <code>&lt;</code>, <code>&gt;</code> and <code>&amp;</code>) replaced
	 * by their HTML escape sequences.
	 *
	 * @param s The input string.
	 * @param newlineReplacement What to replace newline characters with.
	 *        If this is <code>null</code>, they are simply removed.
	 * @param inPreBlock Whether this HTML will be in within <code>pre</code>
	 *        tags.  If this is <code>true</code>, spaces will be kept as-is;
	 *        otherwise, they will be converted to "<code>&nbsp;</code>".
	 * @return The escaped version of <code>s</code>.
	 */
	public static String escapeForHtml(String s, String newlineReplacement,
									   boolean inPreBlock) {

		if (newlineReplacement==null) {
			newlineReplacement = "";
		}
		String tabString = inPreBlock ? "    " : "&nbsp;&nbsp;&nbsp;&nbsp;";

		StringBuilder sb = new StringBuilder();

		for (int i=0; i<s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
				case ' ':
					if (inPreBlock) {
						sb.append(' ');
					}
					else {
						sb.append("&nbsp;");
					}
					break;
				case '\n':
					sb.append(newlineReplacement);
					break;
				case '&':
					sb.append("&amp;");
					break;
				case '\t':
					sb.append(tabString);
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				default:
					sb.append(ch);
					break;
			}
		}

		return sb.toString();
	}


	/**
	 * Returns a hex string for the specified color, suitable for HTML.
	 *
	 * @param c The color.
	 * @return The string representation, in the form "<code>#rrggbb</code>",
	 *         or <code>null</code> if <code>c</code> is <code>null</code>.
	 */
	public static String getHexString(Color c) {

		if (c == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder("#");

		int r = c.getRed();
		if (r<16) {
			sb.append('0');
		}
		sb.append(Integer.toHexString(r));
		int g = c.getGreen();
		if (g<16) {
			sb.append('0');
		}
		sb.append(Integer.toHexString(g));
		int b = c.getBlue();
		if (b<16) {
			sb.append('0');
		}
		sb.append(Integer.toHexString(b));

		return sb.toString();
	}

	public static String getTextAsHtml(RSyntaxTextArea textArea, int start, int end) {

		// Create the selection as HTML
		StringBuilder sb = new StringBuilder("<pre style='")
			.append("font-family: \"").append(textArea.getFont().getFamily()).append("\", courier;");
		if (textArea.getBackground() != null) { // May be null if it is an image
			sb.append(" background: ")
				.append(HtmlUtil.getHexString(textArea.getBackground()))
				.append("'>");
		}

		Token token = textArea.getTokenListFor(start, end);
		for (Token t = token; t != null; t = t.getNextToken()) {

			if (t.isPaintable()) {

				if (t.isSingleChar('\n')) {
					sb.append("<br>");
				}
				else {
					sb.append(TokenUtils.tokenToHtml(textArea, t));
				}
			}
		}

		sb.append("</pre>");
		return sb.toString();
	}
}
