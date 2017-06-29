/*
 * 02/21/2004
 *
 * Token.java - A token used in syntax highlighting.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;


/**
 * The default implementation of {@link Token}.<p>
 *
 * <b>Note:</b> The instances of <code>Token</code> returned by
 * {@link RSyntaxDocument}s are pooled and should always be treated as
 * immutable.  They should not be cast to <code>TokenImpl</code> and modified.
 * Modifying tokens you did not create yourself can and will result in
 * rendering issues and/or runtime exceptions. You have been warned!
 *
 * @author Robert Futrell
 * @version 0.3
 */
@SuppressWarnings({ "checkstyle:visibilitymodifier" })
public class TokenImpl implements Token {

	/**
	 * The text this token represents.  This is implemented as a segment so we
	 * can point directly to the text in the document without having to make a
	 * copy of it.
	 */
	public char[] text;
	public int textOffset;
	public int textCount;

	/**
	 * The offset into the document at which this token resides.
	 */
	private int offset;

	/**
	 * The type of token this is; for example, {@link #FUNCTION}.
	 */
	private int type;

	/**
	 * Whether this token is a hyperlink.
	 */
	private boolean hyperlink;

	/**
	 * The next token in this linked list.
	 */
	private Token nextToken;

	/**
	 * The language this token is in, <code>&gt;= 0</code>.
	 */
	private int languageIndex;


	/**
	 * Creates a "null" token.  The token itself is not null; rather, it
	 * signifies that it is the last token in a linked list of tokens and
	 * that it is not part of a "multi-line token."
	 */
	public TokenImpl() {
		this.text = null;
		this.textOffset = -1;
		this.textCount = -1;
		this.setType(NULL);
		setOffset(-1);
		hyperlink = false;
		nextToken = null;
	}


	/**
	 * Constructor.
	 *
	 * @param line The segment from which to get the token.
	 * @param beg The first character's position in <code>line</code>.
	 * @param end The last character's position in <code>line</code>.
	 * @param startOffset The offset into the document at which this
	 *        token begins.
	 * @param type A token type listed as "generic" above.
	 * @param languageIndex The language index for this token.
	 */
	public TokenImpl(Segment line, int beg, int end, int startOffset, int type,
			int languageIndex) {
		this(line.array, beg,end, startOffset, type, languageIndex);
	}


	/**
	 * Constructor.
	 *
	 * @param line The segment from which to get the token.
	 * @param beg The first character's position in <code>line</code>.
	 * @param end The last character's position in <code>line</code>.
	 * @param startOffset The offset into the document at which this
	 *        token begins.
	 * @param type A token type listed as "generic" above.
	 * @param languageIndex The language index for this token.
	 */
	public TokenImpl(char[] line, int beg, int end, int startOffset, int type,
			int languageIndex) {
		this();
		set(line, beg,end, startOffset, type);
		setLanguageIndex(languageIndex);
	}


	/**
	 * Creates this token as a copy of the passed-in token.
	 *
	 * @param t2 The token from which to make a copy.
	 */
	public TokenImpl(Token t2) {
		this();
		copyFrom(t2);
	}


	@Override
	public StringBuilder appendHTMLRepresentation(StringBuilder sb,
											RSyntaxTextArea textArea,
											boolean fontFamily) {
		return appendHTMLRepresentation(sb, textArea, fontFamily, false);
	}


	@Override
	public StringBuilder appendHTMLRepresentation(StringBuilder sb,
								RSyntaxTextArea textArea, boolean fontFamily,
								boolean tabsToSpaces) {

		SyntaxScheme colorScheme = textArea.getSyntaxScheme();
		Style scheme = colorScheme.getStyle(getType());
		Font font = textArea.getFontForTokenType(getType());//scheme.font;

		if (font.isBold()) {
			sb.append("<b>");
		}
		if (font.isItalic()) {
			sb.append("<em>");
		}
		if (scheme.underline || isHyperlink()) {
			sb.append("<u>");
		}

		boolean needsFontTag = fontFamily || !isWhitespace();
		if (needsFontTag) {
			sb.append("<font");
			if (fontFamily) {
				sb.append(" face=\"").append(font.getFamily()).append('"');
			}
			if (!isWhitespace()) {
				sb.append(" color=\"").append(
						getHTMLFormatForColor(scheme.foreground)).append('"');
			}
			sb.append('>');
		}

		// NOTE: Don't use getLexeme().trim() because whitespace tokens will
		// be turned into NOTHING.
		appendHtmlLexeme(textArea, sb, tabsToSpaces);

		if (needsFontTag) {
			sb.append("</font>");
		}
		if (scheme.underline || isHyperlink()) {
			sb.append("</u>");
		}
		if (font.isItalic()) {
			sb.append("</em>");
		}
		if (font.isBold()) {
			sb.append("</b>");
		}

		return sb;

	}


	/**
	 * Appends an HTML version of the lexeme of this token (i.e. no style
	 * HTML, but replacing chars such as <code>\t</code>, <code>&lt;</code>
	 * and <code>&gt;</code> with their escapes).
	 *
	 * @param textArea The text area.
	 * @param sb The buffer to append to.
	 * @param tabsToSpaces Whether to convert tabs into spaces.
	 * @return The same buffer.
	 */
	private StringBuilder appendHtmlLexeme(RSyntaxTextArea textArea,
								StringBuilder sb, boolean tabsToSpaces) {

		boolean lastWasSpace = false;
		int i = textOffset;
		int lastI = i;
		String tabStr = null;

		while (i<textOffset+textCount) {
			char ch = text[i];
			switch (ch) {
				case ' ':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append(lastWasSpace ? "&nbsp;" : " ");
					lastWasSpace = true;
					break;
				case '\t':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					if (tabsToSpaces && tabStr==null) {
                        StringBuilder stringBuilder = new StringBuilder();
						for (int j=0; j<textArea.getTabSize(); j++) {
                            stringBuilder.append("&nbsp;");
						}
                        tabStr = stringBuilder.toString();
					}
					sb.append(tabsToSpaces ? tabStr : "&#09;");
					lastWasSpace = false;
					break;
				case '&':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&amp;");
					lastWasSpace = false;
					break;
				case '<':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&lt;");
					lastWasSpace = false;
					break;
				case '>':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&gt;");
					lastWasSpace = false;
					break;
				case '\'':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&#39;");
					lastWasSpace = false;
					break;
				case '"':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&#34;");
					lastWasSpace = false;
					break;
				case '/': // OWASP-recommended to escape even though unnecessary
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&#47;");
					lastWasSpace = false;
					break;
				default:
					lastWasSpace = false;
					break;
			}
			i++;
		}
		if (lastI<textOffset+textCount) {
			sb.append(text, lastI, textOffset+textCount-lastI);
		}
		return sb;
	}


	@Override
	public char charAt(int index) {
		return text[textOffset + index];
	}


	@Override
	public boolean containsPosition(int pos) {
		return pos>=getOffset() && pos<getOffset()+textCount;
	}


	/**
	 * Makes one token point to the same text segment, and have the same value
	 * as another token.
	 *
	 * @param t2 The token from which to copy.
	 */
	public void copyFrom(Token t2) {
		text = t2.getTextArray();
		textOffset = t2.getTextOffset();
		textCount = t2.length();
		setOffset(t2.getOffset());
		setType(t2.getType());
		hyperlink = t2.isHyperlink();
		languageIndex = t2.getLanguageIndex();
		nextToken = t2.getNextToken();
	}


	@Override
	public int documentToToken(int pos) {
		return pos + (textOffset-getOffset());
	}


	@Override
	public boolean endsWith(char[] ch) {
		if (ch==null || ch.length>textCount) {
			return false;
		}
		final int start = textOffset + textCount - ch.length;
		for (int i=0; i<ch.length; i++) {
			if (text[start+i]!=ch[i]) {
				return false;
			}
		}
		return true;
	}


	@Override
	public boolean equals(Object obj) {

		if (obj==this) {
			return true;
		}
		if (!(obj instanceof Token)) {
			return false;
		}

		Token t2 = (Token)obj;
		return offset==t2.getOffset() &&
				type==t2.getType() &&
				languageIndex==t2.getLanguageIndex() &&
				hyperlink==t2.isHyperlink() &&
				((getLexeme()==null && t2.getLexeme()==null) ||
					(getLexeme()!=null && getLexeme().equals(t2.getLexeme())));

	}


	@Override
	public int getEndOffset() {
		return offset + textCount;
	}


	/**
	 * Returns a <code>String</code> of the form "#xxxxxx" good for use
	 * in HTML, representing the given color.
	 *
	 * @param color The color to get a string for.
	 * @return The HTML form of the color.  If <code>color</code> is
	 *         <code>null</code>, <code>#000000</code> is returned.
	 */
	private static String getHTMLFormatForColor(Color color) {
		if (color==null) {
			return "black";
		}
		String hexRed = Integer.toHexString(color.getRed());
		if (hexRed.length()==1) {
			hexRed = "0" + hexRed;
		}
		String hexGreen = Integer.toHexString(color.getGreen());
		if (hexGreen.length()==1) {
			hexGreen = "0" + hexGreen;
		}
		String hexBlue = Integer.toHexString(color.getBlue());
		if (hexBlue.length()==1) {
			hexBlue = "0" + hexBlue;
		}
		return "#" + hexRed + hexGreen + hexBlue;
	}


	@Override
	public String getHTMLRepresentation(RSyntaxTextArea textArea) {
		StringBuilder buf = new StringBuilder();
		appendHTMLRepresentation(buf, textArea, true);
		return buf.toString();
	}


	@Override
	public int getLanguageIndex() {
		return languageIndex;
	}


	@Override
	public Token getLastNonCommentNonWhitespaceToken() {

		Token last = null;

		for (Token t=this; t!=null && t.isPaintable(); t=t.getNextToken()) {
			switch (t.getType()) {
				case COMMENT_DOCUMENTATION:
				case COMMENT_EOL:
				case COMMENT_MULTILINE:
				case COMMENT_KEYWORD:
				case COMMENT_MARKUP:
				case WHITESPACE:
					break;
				default:
					last = t;
					break;
			}
		}

		return last;

	}


	@Override
	public Token getLastPaintableToken() {
		Token t = this;
		while (t.isPaintable()) {
			Token next = t.getNextToken();
			if (next==null || !next.isPaintable()) {
				return t;
			}
			t = next;
		}
		return null;
	}


	@Override
	public String getLexeme() {
		return text==null ? null : new String(text, textOffset, textCount);
	}


	@Override
	public int getListOffset(RSyntaxTextArea textArea, TabExpander e,
			float x0, float x) {

		// If the coordinate in question is before this line's start, quit.
		if (x0 >= x) {
			return getOffset();
		}

		float currX = x0; // x-coordinate of current char.
		float nextX = x0; // x-coordinate of next char.
		float stableX = x0; // Cached ending x-coord. of last tab or token.
		TokenImpl token = this;
		int last = getOffset();
		FontMetrics fm = null;

		while (token != null && token.isPaintable()) {

			fm = textArea.getFontMetricsForTokenType(token.getType());
			char[] text = token.text;
			int start = token.textOffset;
			int end = start + token.textCount;

			for (int i = start; i < end; i++) {
				currX = nextX;
				if (text[i] == '\t') {
					nextX = e.nextTabStop(nextX, 0);
					stableX = nextX; // Cache ending x-coord. of tab.
					start = i + 1; // Do charsWidth() from next char.
				}
				else {
					nextX = stableX + fm.charsWidth(text, start, i - start + 1);
				}
				if (x >= currX && x < nextX) {
					if ((x - currX) < (nextX - x)) {
						return last + i - token.textOffset;
					}
					return last + i + 1 - token.textOffset;
				}
			}

			stableX = nextX; // Cache ending x-coordinate of token.
			last += token.textCount;
			token = (TokenImpl)token.getNextToken();

		}

		// If we didn't find anything, return the end position of the text.
		return last;

	}


	@Override
	public Token getNextToken() {
		return nextToken;
	}


	@Override
	public int getOffset() {
		return offset;
	}


	@Override
	public int getOffsetBeforeX(RSyntaxTextArea textArea, TabExpander e,
							float startX, float endBeforeX) {

		FontMetrics fm = textArea.getFontMetricsForTokenType(getType());
		int i = textOffset;
		int stop = i + textCount;
		float x = startX;

		while (i<stop) {
			if (text[i]=='\t') {
				x = e.nextTabStop(x, 0);
			}
			else {
				x += fm.charWidth(text[i]);
			}
			if (x>endBeforeX) {
				// If not even the first character fits into the space, go
				// ahead and say the first char does fit so we don't go into
				// an infinite loop.
				int intoToken = Math.max(i-textOffset, 1);
				return getOffset() + intoToken;
			}
			i++;
		}

		// If we got here, the whole token fit in (endBeforeX-startX) pixels.
		return getOffset() + textCount - 1;

	}


	@Override
	public char[] getTextArray() {
		return text;
	}


	@Override
	public int getTextOffset() {
		return textOffset;
	}


	@Override
	public int getType() {
		return type;
	}


	@Override
	public float getWidth(RSyntaxTextArea textArea, TabExpander e, float x0) {
		return getWidthUpTo(textCount, textArea, e, x0);
	}


	@Override
	public float getWidthUpTo(int numChars, RSyntaxTextArea textArea,
			TabExpander e, float x0) {
		float width = x0;
		FontMetrics fm = textArea.getFontMetricsForTokenType(getType());
		if (fm != null) {
			int w;
			int currentStart = textOffset;
			int endBefore = textOffset + numChars;
			for (int i = currentStart; i < endBefore; i++) {
				if (text[i] == '\t') {
					// Since TokenMaker implementations usually group all
					// adjacent whitespace into a single token, there
					// aren't usually any characters to compute a width
					// for here, so we check before calling.
					w = i - currentStart;
					if (w > 0) {
						width += fm.charsWidth(text, currentStart, w);
					}
					currentStart = i + 1;
					width = e.nextTabStop(width, 0);
				}
			}
			// Most (non-whitespace) tokens will have characters at this
			// point to get the widths for, so we don't check for w>0 (mini-
			// optimization).
			w = endBefore - currentStart;
			width += fm.charsWidth(text, currentStart, w);
		}
		return width - x0;
	}


	@Override
	public int hashCode() {
		return offset + (getLexeme()==null ? 0 : getLexeme().hashCode());
	}


	@Override
	public boolean is(char[] lexeme) {
		if (textCount==lexeme.length) {
			for (int i=0; i<textCount; i++) {
				if (text[textOffset+i]!=lexeme[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}


	@Override
	public boolean is(int type, char[] lexeme) {
		if (this.getType()==type && textCount==lexeme.length) {
			for (int i=0; i<textCount; i++) {
				if (text[textOffset+i]!=lexeme[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}


	@Override
	public boolean is(int type, String lexeme) {
		return this.getType()==type && textCount==lexeme.length() &&
				lexeme.equals(getLexeme());
	}


	@Override
	public boolean isComment() {
		return getType()>=Token.COMMENT_EOL && getType()<=Token.COMMENT_MARKUP;
	}


	@Override
	public boolean isCommentOrWhitespace() {
		return isComment() || isWhitespace();
	}


	@Override
	public boolean isHyperlink() {
		return hyperlink;
	}


	@Override
	public boolean isIdentifier() {
		return getType()==IDENTIFIER;
	}


	@Override
	public boolean isLeftCurly() {
		return getType()==SEPARATOR && isSingleChar('{');
	}


	@Override
	public boolean isRightCurly() {
		return getType()==SEPARATOR && isSingleChar('}');
	}


	@Override
	public boolean isPaintable() {
		return getType()>Token.NULL;
	}


	@Override
	public boolean isSingleChar(char ch) {
		return textCount==1 && text[textOffset]==ch;
	}


	@Override
	public boolean isSingleChar(int type, char ch) {
		return this.getType()==type && isSingleChar(ch);
	}


	@Override
	public boolean isWhitespace() {
		return getType()==WHITESPACE;
	}


	@Override
	public int length() {
		return textCount;
	}


	@Override
	public Rectangle listOffsetToView(RSyntaxTextArea textArea, TabExpander e,
			int pos, int x0, Rectangle rect) {

		int stableX = x0; // Cached ending x-coord. of last tab or token.
		TokenImpl token = this;
		FontMetrics fm = null;
		Segment s = new Segment();

		while (token != null && token.isPaintable()) {

			fm = textArea.getFontMetricsForTokenType(token.getType());
			if (fm == null) {
				return rect; // Don't return null as things'll error.
			}
			char[] text = token.text;
			int start = token.textOffset;
			int end = start + token.textCount;

			// If this token contains the position for which to get the
			// bounding box...
			if (token.containsPosition(pos)) {

				s.array = token.text;
				s.offset = token.textOffset;
				s.count = pos - token.getOffset();

				// Must use this (actually fm.charWidth()), and not
				// fm.charsWidth() for returned value to match up with where
				// text is actually painted on OS X!
				int w = Utilities.getTabbedTextWidth(s, fm, stableX, e,
						token.getOffset());
				rect.x = stableX + w;
				end = token.documentToToken(pos);

				if (text[end] == '\t') {
					rect.width = fm.charWidth(' ');
				}
				else {
					rect.width = fm.charWidth(text[end]);
				}

				return rect;

			}

			// If this token does not contain the position for which to get
			// the bounding box...
			else {
				s.array = token.text;
				s.offset = token.textOffset;
				s.count = token.textCount;
				stableX += Utilities.getTabbedTextWidth(s, fm, stableX, e,
						token.getOffset());
			}

			token = (TokenImpl)token.getNextToken();

		}

		// If we didn't find anything, we're at the end of the line. Return
		// a width of 1 (so selection highlights don't extend way past line's
		// text). A ConfigurableCaret will know to paint itself with a larger
		// width.
		rect.x = stableX;
		rect.width = 1;
		return rect;

	}


	/**
	 * Makes this token start at the specified offset into the document.<p>
	 *
	 * <b>Note:</b> You should not modify <code>Token</code> instances you
	 * did not create yourself (e.g., came from an
	 * <code>RSyntaxDocument</code>).  If you do, rendering issues and/or
	 * runtime exceptions will likely occur.  You have been warned!
	 *
	 * @param pos The offset into the document this token should start at.
	 *        Note that this token must already contain this position; if
	 *        it doesn't, an exception is thrown.
	 * @throws IllegalArgumentException If pos is not already contained by
	 *         this token.
	 * @see #moveOffset(int)
	 */
	public void makeStartAt(int pos) {
		if (pos<getOffset() || pos>=(getOffset()+textCount)) {
			throw new IllegalArgumentException("pos " + pos +
				" is not in range " + getOffset() + "-" + (getOffset()+textCount-1));
		}
		int shift = pos - getOffset();
		setOffset(pos);
		textOffset += shift;
		textCount -= shift;
	}


	/**
	 * Moves the starting offset of this token.<p>
	 *
	 * <b>Note:</b> You should not modify <code>Token</code> instances you
	 * did not create yourself (e.g., came from an
	 * <code>RSyntaxDocument</code>).  If you do, rendering issues and/or
	 * runtime exceptions will likely occur.  You have been warned!
	 *
	 * @param amt The amount to move the starting offset.  This should be
	 *        between <code>0</code> and <code>textCount</code>, inclusive.
	 * @throws IllegalArgumentException If <code>amt</code> is an invalid value.
	 * @see #makeStartAt(int)
	 */
	public void moveOffset(int amt) {
		if (amt<0 || amt>textCount) {
			throw new IllegalArgumentException("amt " + amt +
					" is not in range 0-" + textCount);
		}
		setOffset(getOffset() + amt);
		textOffset += amt;
		textCount -= amt;
	}


	/**
	 * Sets the value of this token to a particular segment of a document.
	 * The "next token" value is cleared.
	 *
	 * @param line The segment from which to get the token.
	 * @param beg The first character's position in <code>line</code>.
	 * @param end The last character's position in <code>line</code>.
	 * @param offset The offset into the document at which this token begins.
	 * @param type A token type listed as "generic" above.
	 */
	public void set(final char[] line, final int beg, final int end,
							final int offset, final int type) {
		this.text = line;
		this.textOffset = beg;
		this.textCount = end - beg + 1;
		this.setType(type);
		this.setOffset(offset);
		nextToken = null;
	}


	/**
	 * Sets whether this token is a hyperlink.
	 *
	 * @param hyperlink Whether this token is a hyperlink.
	 * @see #isHyperlink()
	 */
	@Override
	public void setHyperlink(boolean hyperlink) {
		this.hyperlink = hyperlink;
	}


	/**
	 * Sets the language index for this token.  If this value is positive, it
	 * denotes a specific "secondary" language this token represents (such as
	 * JavaScript code or CSS embedded in an HTML file).  If this value is
	 * <code>0</code>, this token is in the "main" language being edited.
	 * Negative values are invalid and treated as <code>0</code>.
	 *
	 * @param languageIndex The new language index.  A value of
	 *        <code>0</code> denotes the "main" language, any positive value
	 *        denotes a specific secondary language.  Negative values will
	 *        be treated as <code>0</code>.
	 * @see #getLanguageIndex()
	 */
	@Override
	public void setLanguageIndex(int languageIndex) {
		this.languageIndex = languageIndex;
	}


	/**
	 * Sets the "next token" pointer of this token to point to the specified
	 * token.
	 *
	 * @param nextToken The new next token.
	 * @see #getNextToken()
	 */
	public void setNextToken(Token nextToken) {
		this.nextToken = nextToken;
	}


	/**
	 * Sets the offset into the document at which this token resides.
	 *
	 * @param offset The new offset into the document.
	 * @see #getOffset()
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setType(int type) {
		this.type = type;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean startsWith(char[] chars) {
		if (chars.length<=textCount){
			for (int i=0; i<chars.length; i++) {
				if (text[textOffset+i]!=chars[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int tokenToDocument(int pos) {
		return pos + (getOffset()-textOffset);
	}


	/**
	 * Returns this token as a <code>String</code>, which is useful for
	 * debugging.
	 *
	 * @return A string describing this token.
	 */
	@Override
	public String toString() {
		return "[Token: " +
			(getType()==Token.NULL ? "<null token>" :
				"text: '" +
					(text==null ? "<null>" : getLexeme() + "'; " +
	       		"offset: " + getOffset() + "; type: " + getType() + "; " +
		   		"isPaintable: " + isPaintable() +
		   		"; nextToken==null: " + (nextToken==null))) +
		   "]";
	}


}