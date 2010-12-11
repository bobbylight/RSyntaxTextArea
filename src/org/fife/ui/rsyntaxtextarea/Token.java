/*
 * 02/21/2004
 *
 * Token.java - A token used in syntax highlighting.
 * Copyright (C) 2004 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.text.TabExpander;


/**
 * A generic token that functions as a node in a linked list of syntax
 * highlighted tokens for some language.<p>
 *
 * A <code>Token</code> is a piece of text representing some logical token in
 * source code for a programming language.  For example, the line of C code:<p>
 * <pre>
 * int i = 0;
 * </pre>
 * would be broken into 8 <code>Token</code>s: the first representing
 * <code>int</code>, the second whitespace, the third <code>i</code>, the fourth
 * whitespace, the fifth <code>=</code>, etc.<p>
 *
 * @author Robert Futrell
 * @version 0.3
 */
public abstract class Token {

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
	public int offset;

	/**
	 * The type of token this is; for example, {@link #FUNCTION}.
	 */
	public int type;

	/**
	 * Whether this token is a hyperlink.
	 */
	private boolean hyperlink;

	/**
	 * The next token in this linked list.
	 */
	private Token nextToken;

	/**
	 * Rectangle used for filling token backgrounds.
	 */
	private Rectangle2D.Float bgRect;


	// NOTE: All valid token types are >= 0, so extensions of the TokenMaker
	// class are free to internally use all ints < 0 ONLY for "end-of-line"
	// style markers; they are ignored by painting implementations.

	public static final int NULL							= 0;	// Marks EOL with no multiline token at end.

	public static final int COMMENT_EOL						= 1;
	public static final int COMMENT_MULTILINE				= 2;
	public static final int COMMENT_DOCUMENTATION			= 3;

	public static final int RESERVED_WORD					= 4;

	public static final int FUNCTION						= 5;

	public static final int LITERAL_BOOLEAN					= 6;
	public static final int LITERAL_NUMBER_DECIMAL_INT		= 7;
	public static final int LITERAL_NUMBER_FLOAT				= 8;
	public static final int LITERAL_NUMBER_HEXADECIMAL		= 9;
	public static final int LITERAL_STRING_DOUBLE_QUOTE		= 10;
	public static final int LITERAL_CHAR					= 11;	// Char or single-quote string.
	public static final int LITERAL_BACKQUOTE				= 12;	// Used in UNIX/Perl scripts.

	public static final int DATA_TYPE						= 13;

	public static final int VARIABLE						= 14;

	public static final int IDENTIFIER						= 15;

	public static final int WHITESPACE						= 16;

	public static final int SEPARATOR						= 17;

	public static final int OPERATOR						= 18;

	public static final int PREPROCESSOR					= 19;

	public static final int MARKUP_TAG_DELIMITER			= 20;
	public static final int MARKUP_TAG_NAME					= 21;
	public static final int MARKUP_TAG_ATTRIBUTE			= 22;

	public static final int ERROR_IDENTIFIER				= 23;
	public static final int ERROR_NUMBER_FORMAT				= 24;
	public static final int ERROR_STRING_DOUBLE 			= 25;
	public static final int ERROR_CHAR						= 26;	// Char or single-quote string.

	public static final int NUM_TOKEN_TYPES					= 27;


	/**
	 * Creates a "null token."  The token itself is not null; rather, it
	 * signifies that it is the last token in a linked list of tokens and
	 * that it is not part of a "multi-line token."
	 */
	public Token() {
		this.text = null;
		this.textOffset = -1;
		this.textCount = -1;
		this.type = NULL;
		offset = -1;
		hyperlink = false;
		nextToken = null;
		bgRect = new Rectangle2D.Float();
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
	 */
	public Token(final char[] line, final int beg, final int end,
							final int startOffset, final int type) {
		this();
		set(line, beg,end, startOffset, type);
	}


	/**
	 * Creates this token as a deep copy of the passed-in token.
	 *
	 * @param t2 The token from which to make a copy.
	 */
	public Token(Token t2) {
		this();
		copyFrom(t2);
	}


	/**
	 * Appends HTML code for painting this token, using the given text area's
	 * color scheme.
	 *
	 * @param sb The buffer to append to.
	 * @param textArea The text area whose color scheme to use.
	 * @param fontFamily Whether to include the font family in the HTML for
	 *        this token.  You can pass <code>false</code> for this parameter
	 *        if, for example, you are making all your HTML be monospaced,
	 *        and don't want any crazy fonts being used in the editor to be
	 *        reflected in your HTML.
	 * @return The buffer appended to.
	 * @see #getHTMLRepresentation(RSyntaxTextArea)
	 */
	public StringBuffer appendHTMLRepresentation(StringBuffer sb,
											RSyntaxTextArea textArea,
											boolean fontFamily) {

		SyntaxScheme colorScheme = textArea.getSyntaxScheme();
		Style scheme = colorScheme.styles[type];
		Font font = textArea.getFontForTokenType(type);//scheme.font;

		if (font.isBold()) sb.append("<b>");
		if (font.isItalic()) sb.append("<em>");
		if (scheme.underline) sb.append("<u>");

		sb.append("<font");
		if (fontFamily) {
			sb.append(" face=\"").append(font.getFamily()).append("\"");
		}
		sb.append(" color=\"").
			append(getHTMLFormatForColor(scheme.foreground)).
			append("\">");

		// NOTE: Don't use getLexeme().trim() because whitespace tokens will
		// be turned into NOTHING.
		appendHtmlLexeme(sb);//sb.append(getHtmlLexeme());

		sb.append("</font>");
		if (scheme.underline) sb.append("</u>");
		if (font.isItalic()) sb.append("</em>");
		if (font.isBold()) sb.append("</b>");

		return sb;

	}


	/**
	 * Appends an HTML version of the lexeme of this token (i.e. no style
	 * HTML, but replacing chars such as <code>\t</code>, <code>&lt;</code>
	 * and <code>&gt;</code> with their escapes).
	 *
	 * @param sb The buffer to append to.
	 * @return The same buffer.
	 */
	private final StringBuffer appendHtmlLexeme(StringBuffer sb) {
		int i = textOffset;
		int lastI = i;
		while (i<textOffset+textCount) {
			char ch = text[i];
			switch (ch) {
				case '\t':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&nbsp;");
					break;
				case '<':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&lt;");
					break;
				case '>':
					sb.append(text, lastI, i-lastI);
					lastI = i+1;
					sb.append("&gt;");
					break;
			}
			i++;
		}
		if (lastI<textOffset+textCount) {
			sb.append(text, lastI, textOffset+textCount-lastI);
		}
		return sb;
	}


	/**
	 * Returns whether the token straddles the specified position in the
	 * document.
	 *
	 * @param pos The position in the document to check.
	 * @return Whether the specified position is straddled by this token.
	 */
	public boolean containsPosition(int pos) {
		return pos>=offset && pos<offset+textCount;
	}


	/**
	 * Makes one token point to the same text segment, and have the same value
	 * as another token.
	 *
	 * @param t2 The token from which to copy.
	 */
	public void copyFrom(Token t2) {
		text = t2.text;
		textOffset = t2.textOffset;
		textCount = t2.textCount;
		offset = t2.offset;
		type = t2.type;
		nextToken = t2.nextToken;
	}


	/**
	 * Returns the position in the token's internal char array corresponding
	 * to the specified document position.<p>
	 * Note that this method does NOT do any bounds checking; you can pass in
	 * a document position that does not correspond to a position in this
	 * token, and you will not receive an Exception or any other notification;
	 * it is up to the caller to ensure valid input.
	 *
	 * @param pos A position in the document that is represented by this token.
	 * @return The corresponding token position >= <code>textOffset</code> and
	 *         < <code>textOffset+textCount</code>.
	 * @see #tokenToDocument
	 */
	public int documentToToken(int pos) {
		return pos + (textOffset-offset);
	}


	/**
	 * Returns a <code>String</code> of the form "#xxxxxx" good for use
	 * in HTML, representing the given color.
	 *
	 * @param color The color to get a string for.
	 * @return The HTML form of the color.  If <code>color</code> is
	 *         <code>null</code>, <code>#000000</code> is returned.
	 */
	private static final String getHTMLFormatForColor(Color color) {
		if (color==null) {
			return "black";
		}
		String hexRed = Integer.toHexString(color.getRed());
		if (hexRed.length()==1)
			hexRed = "0" + hexRed;
		String hexGreen = Integer.toHexString(color.getGreen());
		if (hexGreen.length()==1)
			hexGreen = "0" + hexGreen;
		String hexBlue = Integer.toHexString(color.getBlue());
		if (hexBlue.length()==1)
			hexBlue = "0" + hexBlue;
		return "#" + hexRed + hexGreen + hexBlue;
	}


	/**
	 * Returns a <code>String</code> containing HTML code for painting this
	 * token, using the given text area's color scheme.
	 *
	 * @param textArea The text area whose color scheme to use.
	 * @return The HTML representation of the token.
	 * @see #appendHTMLRepresentation(StringBuffer, RSyntaxTextArea, boolean)
	 */
	public String getHTMLRepresentation(RSyntaxTextArea textArea) {
		StringBuffer buf = new StringBuffer();
		appendHTMLRepresentation(buf, textArea, true);
		return buf.toString();
	}


	/**
	 * Returns the last token in this list that is not whitespace or a
	 * comment.
	 *
	 * @return The last non-comment, non-whitespace token, or <code>null</code>
	 *         if there isn't one.
	 */
	public Token getLastNonCommentNonWhitespaceToken() {

		Token last = null;

		for (Token t=this; t!=null && t.isPaintable(); t=t.nextToken) {
			switch (t.type) {
				case COMMENT_DOCUMENTATION:
				case COMMENT_EOL:
				case COMMENT_MULTILINE:
				case WHITESPACE:
					break;
				default:
					last = t;
					break;
			}
		}

		return last;

	}


	/**
	 * Returns the last paintable token in this token list, or <code>null</code>
	 * if there is no paintable token.
	 *
	 * @return The last paintable token in this token list.
	 */
	public Token getLastPaintableToken() {
		Token t = this;
		while (t.isPaintable()) {
			if (t.nextToken==null || !t.nextToken.isPaintable()) {
				return t;
			}
			t = t.nextToken;
		}
		return null;
	}


	/**
	 * Returns the text of this token, as a string.<p>
	 *
	 * Note that this method isn't used much by the
	 * <code>rsyntaxtextarea</code> package internally, as it tries to limit
	 * memory allocation.
	 *
	 * @return The text of this token.
	 */
	public String getLexeme() {
		return new String(text, textOffset, textCount);
	}


	/**
	 * Determines the offset into this token list (i.e., into the
	 * document) that covers pixel location <code>x</code> if the token list
	 * starts at pixel location <code>x0</code><p>.
	 * This method will return the document position "closest" to the
	 * x-coordinate (i.e., if they click on the "right-half" of the
	 * <code>w</code> in <code>awe</code>, the caret will be placed in
	 * between the <code>w</code> and <code>e</code>; similarly, clicking on
	 * the left-half places the caret between the <code>a</code> and
	 * <code>w</code>).  This makes it useful for methods such as
	 * <code>viewToModel</code> found in <code>javax.swing.text.View</code>
	 * subclasses.<p>
	 *
	 * This method is abstract so subclasses who paint themselves differently
	 * (i.e., {@link VisibleWhitespaceToken} is painted a tad differently than
	 * {@link DefaultToken} when rendering hints are enabled) can still return
	 * accurate results.
	 *
	 * @param textArea The text area from which the token list was derived.
	 * @param e How to expand tabs.
	 * @param x0 The pixel x-location that is the beginning of
	 *        <code>tokenList</code>.
	 * @param x The pixel-position for which you want to get the corresponding
	 *        offset.
	 * @return The position (in the document, NOT into the token list!) that
	 *         covers the pixel location.  If <code>tokenList</code> is
	 *         <code>null</code> or has type <code>Token.NULL</code>, then
	 *         <code>-1</code is returned; the caller should recognize this and
	 *         return the actual end position of the (empty) line.
	 */
	public abstract int getListOffset(RSyntaxTextArea textArea, TabExpander e,
								float x0, float x);


	/**
	 * Returns the token after this one in the linked list.
	 *
	 * @return The next token.
	 * @see #setNextToken
	 */
	public Token getNextToken() {
		return nextToken;
	}


	/**
	 * Returns the position in the document that represents the last character
	 * in the token that will fit into <code>endBeforeX-startX</code> pixels.
	 * For example, if you're using a monospaced 8-pixel-per-character font,
	 * have the token "while" and <code>startX</code> is <code>0</code> and
	 * <code>endBeforeX</code> is <code>30</code>, this method will return the
	 * document position of the "i" in "while", because the "i" ends at pixel
	 * <code>24</code>, while the "l" ends at <code>32</code>.  If not even the
	 * first character fits in <code>endBeforeX-startX</code>, the first
	 * character's position is still returned so calling methods don't go into
	 * infinite loops.
	 *
	 * @param textArea The text area in which this token is being painted.
	 * @param e How to expand tabs.
	 * @param startX The x-coordinate at which the token will be painted.  This
	 *        is needed because of tabs.
	 * @param endBeforeX The x-coordinate for which you want to find the last
	 *        character of <code>t</code> which comes before it.
	 * @return The last document position that will fit in the specified amount
	 *         of pixels.
	 */
	/*
	 * @see #getTokenListOffsetBeforeX
	 * FIXME:  This method does not compute correctly!  It needs to be abstract
	 * and implemented by subclasses.
	 */
	public int getOffsetBeforeX(RSyntaxTextArea textArea, TabExpander e,
							float startX, float endBeforeX) {

		FontMetrics fm = textArea.getFontMetricsForTokenType(type);
		int i = textOffset;
		int stop = i + textCount;
		float x = startX;

		while (i<stop) {
			if (text[i]=='\t')
				x = e.nextTabStop(x, 0);
			else
				x += fm.charWidth(text[i]);
			if (x>endBeforeX) {
				// If not even the first character fits into the space, go
				// ahead and say the first char does fit so we don't go into
				// an infinite loop.
				int intoToken = Math.max(i-textOffset, 1);
				return offset + intoToken;
			}
			i++;
		}

		// If we got here, the whole token fit in (endBeforeX-startX) pixels.
		return offset + textCount - 1;

	}


	/**
	 * Returns the width of this token given the specified parameters.
	 *
	 * @param textArea The text area in which the token is being painted.
	 * @param e Describes how to expand tabs.  This parameter cannot be
	 *        <code>null</code>.
	 * @param x0 The pixel-location at which the token begins.  This is needed
	 *        because of tabs.
	 * @return The width of the token, in pixels.
	 * @see #getWidthUpTo
	 */
	public float getWidth(RSyntaxTextArea textArea, TabExpander e, float x0) {
		return getWidthUpTo(textCount, textArea, e, x0);
	}


	/**
	 * Returns the width of a specified number of characters in this token.
	 * For example, for the token "while", specifying a value of <code>3</code>
	 * here returns the width of the "whi" portion of the token.<p>
	 *
	 * This method is abstract so subclasses who paint themselves differently
	 * (i.e., {@link VisibleWhitespaceToken} is painted a tad differently than
	 * {@link DefaultToken} when rendering hints are enabled) can still return
	 * accurate results.
	 *
	 * @param numChars The number of characters for which to get the width.
	 * @param textArea The text area in which the token is being painted.
	 * @param e How to expand tabs.  This value cannot be <code>null</code>.
	 * @param x0 The pixel-location at which this token begins.  This is needed
	 *        because of tabs.
	 * @return The width of the specified number of characters in this token.
	 * @see #getWidth
	 */
	public abstract float getWidthUpTo(int numChars, RSyntaxTextArea textArea,
										TabExpander e, float x0);


	/**
	 * Returns whether this token is of the specified type, with the specified
	 * lexeme.
	 *
	 * @param type The type to check for.
	 * @param lexeme The lexeme to check for.
	 * @return Whether this token has that type and lexeme.
	 */
	public boolean is(int type, String lexeme) {
		return this.type==type && textCount==lexeme.length() &&
				lexeme.equals(getLexeme());
	}


	/**
	 * Returns whether this token is a comment.
	 *
	 * @return Whether this token is a comment.
	 * @see #isWhitespace()
	 */
	public boolean isComment() {
		return type>=Token.COMMENT_EOL && type<=Token.COMMENT_DOCUMENTATION;
	}


	/**
	 * Returns whether this token is a hyperlink.
	 *
	 * @return Whether this token is a hyperlink.
	 * @see #setHyperlink(boolean)
	 */
	public boolean isHyperlink() {
		return hyperlink;
	}


	/**
	 * Returns whether this token is a {@link #SEPARATOR} representing a single
	 * left curly brace.
	 *
	 * @return Whether this token is a left curly brace.
	 * @see #isRightCurly()
	 */
	public boolean isLeftCurly() {
		return type==SEPARATOR && isSingleChar('{');
	}


	/**
	 * Returns whether this token is a {@link #SEPARATOR} representing a single
	 * right curly brace.
	 *
	 * @return Whether this token is a right curly brace.
	 * @see #isLeftCurly()
	 */
	public boolean isRightCurly() {
		return type==SEPARATOR && isSingleChar('}');
	}


	/**
	 * Returns whether or not this token is "paintable;" i.e., whether or not
	 * the type of this token is one such that it has an associated syntax
	 * style.  What this boils down to is whether the token type is greater
	 * than <code>Token.NULL</code>.
	 *
	 * @return Whether or not this token is paintable.
	 */
	public boolean isPaintable() {
		return type>Token.NULL;
	}


	/**
	 * Returns whether this token is the specified single character.
	 *
	 * @param ch The character to check for.
	 * @return Whether this token's lexeme is the single character specified.
	 */
	public boolean isSingleChar(char ch) {
		return textCount==1 && text[textOffset]==ch;
	}


	/**
	 * Returns whether or not this token is whitespace.
	 *
	 * @return <code>true</code> iff this token is whitespace.
	 * @see #isComment()
	 */
	public boolean isWhitespace() {
		return type==WHITESPACE;
	}


	/**
	 * Returns the bounding box for the specified document location.  The
	 * location must be in the specified token list; if it isn't,
	 * <code>null</code> is returned.
	 *
	 * @param textArea The text area from which the token list was derived.
	 * @param e How to expand tabs.
	 * @param pos The position in the document for which to get the bounding
	 *        box in the view.
	 * @param x0 The pixel x-location that is the beginning of
	 *        <code>tokenList</code>.
	 * @param rect The rectangle in which we'll be returning the results.  This
	 *        object is reused to keep from frequent memory allocations.
	 * @return The bounding box for the specified position in the model.
	 */
	public abstract Rectangle listOffsetToView(RSyntaxTextArea textArea,
								TabExpander e, int pos, int x0,
								Rectangle rect);


	/**
	 * Makes this token start at the specified offset into the document.
	 *
	 * @param pos The offset into the document this token should start at.
	 *        Note that this token must already contain this position; if
	 *        it doesn't, an exception is thrown.
	 * @throws IllegalArgumentException If pos is not already contained by
	 *         this token.
	 * @see #moveOffset(int)
	 */
	public void makeStartAt(int pos) {
		if (pos<offset || pos>=(offset+textCount)) {
			throw new IllegalArgumentException("pos " + pos +
				" is not in range " + offset + "-" + (offset+textCount-1));
		}
		int shift = pos - offset;
		offset = pos;
		textOffset += shift;
		textCount -= shift;
	}


	/**
	 * Moves the starting offset of this token.
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
		offset += amt;
		textOffset += amt;
		textCount -= amt;
	}


	/**
	 * Paints this token.
	 *
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @return The x-coordinate representing the end of the painted text.
	 */
	public final float paint(Graphics2D g, float x, float y,
						RSyntaxTextArea host, TabExpander e) {
		return paint(g, x,y, host, e, 0);
	}


	/**
	 * Paints this token.
	 *
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @param clipStart The left boundary of the clip rectangle in which we're
	 *        painting.  This optimizes painting by allowing us to not paint
	 *        paint when this token is "to the left" of the clip rectangle.
	 * @return The x-coordinate representing the end of the painted text.
	 */
	public abstract float paint(Graphics2D g, float x, float y,
							RSyntaxTextArea host,
							TabExpander e, float clipStart);


	/**
	 * Paints the background of a token.
	 *
	 * @param x The x-coordinate of the token.
	 * @param y The y-coordinate of the token.
	 * @param width The width of the token (actually, the width of the part of
	 *        the token to paint).
	 * @param height The height of the token.
	 * @param g The graphics context with which to paint.
	 * @param fontAscent The ascent of the token's font.
	 * @param host The text area.
	 * @param color The color with which to paint.
	 */
	protected void paintBackground(float x, float y, float width, float height,
							Graphics2D g, int fontAscent,
							RSyntaxTextArea host, Color color) {
		// RSyntaxTextArea's bg can be null, so we must check for this.
		Color temp = host.getBackground();
		g.setXORMode(temp!=null ? temp : Color.WHITE);
		g.setColor(color);
		bgRect.setRect(x,y-fontAscent, width,height);
		g.fill(bgRect);
		g.setPaintMode();
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
		this.type = type;
		this.offset = offset;
		nextToken = null;
	}


	/**
	 * Sets whether this token is a hyperlink.
	 *
	 * @param hyperlink Whether this token is a hyperlink.
	 * @see #isHyperlink()
	 */
	public void setHyperlink(boolean hyperlink) {
		this.hyperlink = hyperlink;
	}


	/**
	 * Sets the "next token" pointer of this token to point to the specified
	 * token.
	 *
	 * @param nextToken The new next token.
	 * @see #getNextToken
	 */
	public void setNextToken(Token nextToken) {
		this.nextToken = nextToken;
	}


	/**
	 * Returns the position in the document corresponding to the specified
	 * position in this token's internal char array (<code>textOffset</code> -
	 * <code>textOffset+textCount-1</code>).<p>
	 * Note that this method does NOT do any bounds checking; you can pass in
	 * an invalid token position, and you will not receive an Exception or any
	 * other indication that the returned document position is invalid.  It is
	 * up to the user to ensure valid input.
	 *
	 * @param pos A position in the token's internal char array
	 *        (<code>textOffset</code> - <code>textOffset+textCount</code>).
	 * @return The corresponding position in the document.
	 * @see #documentToToken
	 */
	public int tokenToDocument(int pos) {
		return pos + (offset-textOffset);
	}


	/**
	 * Returns this token as a <code>String</code>, which is useful for
	 * debugging.
	 *
	 * @return A string describing this token.
	 */
	public String toString() {
		return "[Token: " +
			(type==Token.NULL ? "<null token>" :
				"text: '" +
					(text==null ? "<null>" : getLexeme() + "'; " +
	       		"offset: " + offset + "; type: " + type + "; " +
		   		"isPaintable: " + isPaintable() +
		   		"; nextToken==null: " + (nextToken==null))) +
		   "]";
	}


}