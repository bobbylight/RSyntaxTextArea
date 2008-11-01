/*
 * 10/28/2004
 *
 * VisibleWhitespaceToken.java - Token that paints special symbols for its
 *                               whitespace characters (space and tab).
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
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;


/**
 * This token class paints spaces and tabs with special symbols so the user
 * can see the whitespace in his document.  Rendering hints are honored.<p>
 *
 * The current implementation paints as follows:
 * <ul>
 *   <li>The first tab or space, if any, is found in the token.</li>
 *   <li>If a tab was found, all characters up to it are painted as a
 *       group.</li>
 *   <li>If a space was found, all characters up to and including it are
 *       painted (it is painted with a special symbol to denote it as
 *       a space).</li>
 *   <li>If neither a tab nor a whitespace was found, all characters in the
 *       token are painted.</li>
 *   <li>Repeat until all characters are painted.</li>
 * </ul>
 * This means that rendering hints are applied to all groups of characters
 * within a token, excluding whitespace and tabs.<p>
 *
 * A problem with this implementation is that FontMetrics.charsWidth() is still
 * used to calculate the width of a group of chars painted.  Thus, the group of
 * characters will be painted with the rendering hints specified, but the
 * following tab (or group of characters if the current group was the end of a
 * token) will not necessarily be painted at the proper x-coordinate (as
 * FontMetrics.charsWidth() returns an <code>int</code> and not a
 * <code>float</code>).  The way around this would be to calculate the token's
 * width in such a way that a float is returned (Font.getStringBounds()?).
 *
 * @author Robert Futrell
 * @version 0.3
 * @see Token
 * @see DefaultToken
 */
public class VisibleWhitespaceToken extends Token {

	private Rectangle2D.Float dotRect;


	/**
	 * Creates a "null token."  The token itself is not null; rather, it
	 * signifies that it is the last token in a linked list of tokens and
	 * that it is not part of a "multiline token."
	 */
	public VisibleWhitespaceToken() {
		super();
		dotRect = new Rectangle2D.Float(0,0, 1,1);
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
	public VisibleWhitespaceToken(final Segment line, final int beg,
					final int end, final int startOffset, final int type) {
		this(line.array, beg,end, startOffset, type);
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
	public VisibleWhitespaceToken(final char[] line, final int beg,
					final int end, final int startOffset, final int type) {
		super(line, beg,end, startOffset, type);
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
	 * subclasses.
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
	public int getListOffset(RSyntaxTextArea textArea, TabExpander e,
								float x0, float x) {

		// If the coordinate in question is before this line's start, quit.
		if (x0 >= x)
			return offset;

		float currX = x0;	// x-coordinate of current char.
		float nextX = x0;	// x-coordinate of next char.
		float stableX = x0;	// Cached ending x-coord. of last tab or token.
		Token token = this;
		int last = offset;
		FontMetrics fm = null;

		while (token!=null && token.isPaintable()) {

			fm = textArea.getFontMetricsForTokenType(token.type);
			char[] text = token.text;
			int start = token.textOffset;
			int end = start + token.textCount;

			for (int i=start; i<end; i++) {
				currX = nextX;
				switch (text[i]) {
					case '\t':
						nextX = e.nextTabStop(nextX, 0);
						stableX = nextX;	// Cache ending x-coord. of tab.
						start = i+1;		// Do charsWidth() from next char.
						break;
					case ' ':
						nextX = stableX + fm.charsWidth(text, start, i-start+1);
						stableX = nextX;	// Cache ending x-coord. of tab.
						start = i+1;		// Do charsWidth() from next char.
						break;
					default:
						nextX = stableX + fm.charsWidth(text, start, i-start+1);
						break;
				}
				if (x>=currX && x<nextX) {
					if ((x-currX) < (nextX-x))
						return last + i-token.textOffset;
					else
						return last + i+1-token.textOffset;
				}
			}

			stableX = nextX;		// Cache ending x-coordinate of token.
			last += token.textCount;
			token = token.getNextToken();

		}

		// If we didn't find anything, return the end position of the text.
		return last;

	}


	/**
	 * Returns the width of a specified number of characters in this token.
	 * For example, for the token "while", specifying a value of <code>3</code>
	 * here returns the width of the "whi" portion of the token.<p>
	 *
	 * This method is overridden to account for the case when "smooth fonts"
	 * are enabled; smooth fonts cause the token painting code to align things
	 * differently (since we "break" at spaces), so we need to reflect that
	 * behavior here as well.
	 *
	 * @param numChars The number of characters for which to get the width.
	 * @param textArea The text area in which this token is being painted.
	 * @param e How to expand tabs.  This value cannot be <code>null</code>.
	 * @param x0 The pixel-location at which this token begins.  This is
	 *        needed because of tabs.
	 * @return The width of the specified number of characters in this token.
	 * @see #getWidth
	 */
	public float getWidthUpTo(int numChars, RSyntaxTextArea textArea,
							TabExpander e, float x0) {
		float width = x0;
		FontMetrics fm = textArea.getFontMetricsForTokenType(type);
		if (fm!=null) {
			int w;
			int currentStart = textOffset;
			int endBefore = textOffset + numChars;
			for (int i=currentStart; i<endBefore; i++) {
				switch (text[i]) {
					case '\t':
						// Since TokenMaker implementations usually group
						// all adjacent whitespace into a single token,
						// there aren't usually any characters to compute
						// a width for here, so we check before calling.
						w = i - currentStart;
						if (w>0)
							width += fm.charsWidth(text, currentStart, w);
						currentStart = i+1;
						width = e.nextTabStop(width, 0);
						break;
					case ' ':
						w = i - currentStart + 1;
						width += fm.charsWidth(text, currentStart, w);
						currentStart = i+1;
						break;
					default:
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
	public Rectangle listOffsetToView(RSyntaxTextArea textArea,
								TabExpander e, int pos, int x0,
								Rectangle rect) {

		float stableX = x0;	// Cached ending x-coord. of last tab or token.
		Token token = this;
		int last = offset;
		FontMetrics fm = null;

		while (token!=null && token.isPaintable()) {

			fm = textArea.getFontMetricsForTokenType(token.type);
			if (fm==null) {
				return rect; // Don't return null as things'll error.
			}
			char[] text = token.text;
			int start = token.textOffset;
			int end = start + token.textCount;
			int i;

			if (token.containsPosition(pos)) {
				end = start + (pos-token.offset);
				for (i=start; i<end; i++) {
					switch (text[i]) {
						case '\t':
							stableX += fm.charsWidth(text, start, i-start+1);
							stableX = e.nextTabStop(stableX, 0);
							start = i+1;		// Do charsWidth() from next char.
							break;
						case ' ':
							stableX += fm.charsWidth(text, start, i-start+1);
							start = i + 1;
							break;
						default:
							break;
					}
				}
				int temp = fm.charsWidth(text, start, i-start);
				rect.x = (int)stableX + temp;
				if (text[i]=='\t')
					rect.width = fm.charWidth(' ');
				else
					rect.width = fm.charsWidth(text, start,i-start+1) - temp;
				return rect;
			}

			else {
				for (i=start; i<end; i++) {
					switch (text[i]) {
						case '\t':
							stableX += fm.charsWidth(text, start, i-start+1);
							stableX = e.nextTabStop(stableX, 0);
							start = i+1;		// Do charsWidth() from next char.
							break;
						case ' ':
							stableX += fm.charsWidth(text, start, i-start+1);
							start = i + 1;
							break;
						default:
							break;
					}
				}
				stableX += fm.charsWidth(text, start, i-start);
			}

			last += token.textCount;
			token = token.getNextToken();

		}

		// If we didn't find anything, we're at the end of the line.  Return
		// a width of 1 (so selection highlights don't extend way past line's
		// text).  A ConfigurableCaret will no to paint itself with a larger
		// width.
		rect.x = (int)stableX;
		rect.width = 1;
		return rect;

	}


	/**
	 * Makes this token start at the specified offset into the document.
	 *
	 * @param pos The offset into the document this token should start at.
	 *        Note that this token must already contain this position; if
	 *        it doesn't, an exception is thrown.
	 * @throws IllegalArgumentException If pos is not already contained by this
	 *         token.
	 */
	public void makeStartAt(int pos) {
		if (pos<offset || pos>=(offset+textCount)) {
			throw new IllegalArgumentException("pos not in range " + offset +
							"-" + (offset+textCount-1));
		}
		int shift = pos - offset;
		offset = pos;
		textOffset += shift;
		textCount -= shift;
	}


	/**
	 * Paints this token, using special symbols for whitespace characters.
	 *
	 * @param g The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param host The text area this token is in.
	 * @param e How to expand tabs.
	 * @param clipStart The left boundary of the clip rectangle in which we're
	 *        painting.  This optimizes painting by allowing us to not paint
	 *        not paint when this token is "to the left" of the clip rectangle.
	 * @return The x-coordinate representing the end of the painted text.
	 */
	public final float paint(Graphics2D g, float x, float y,
						RSyntaxTextArea host, TabExpander e,
						float clipStart) {

		int origX = (int)x;
		int end = textOffset + textCount;
		float nextX = x;
		int flushLen = 0;
		int flushIndex = textOffset;
		Color fg = host.getForegroundForToken(this);
		Color bg = host.getBackgroundForTokenType(type);
		g.setFont(host.getFontForTokenType(type));
		FontMetrics fm = host.getFontMetricsForTokenType(type);

		int ascent = fm.getAscent();
		int height = fm.getHeight();

		for (int i=textOffset; i<end; i++) {

			switch (text[i]) {

				case '\t':

					// Fill in background.
					nextX = x+fm.charsWidth(text, flushIndex,flushLen);
					float nextNextX = e.nextTabStop(nextX, 0);
					if (bg!=null) {
						paintBackground(x,y, nextNextX-x,height, g,
										ascent, host, bg);
					}
					g.setColor(fg);

					// Paint chars cached before the tab.
					if (flushLen > 0) {
						g.drawString(
							new String(text, flushIndex, flushLen), x,y);
						flushLen = 0;
					}
					flushIndex = i + 1;

					/*
					if (host.showIndentGuide()) {
						g.setColor(Color.GRAY);
						int y2 = y - ascent;
						int end2 = y + fm.getDescent() - 1;
						while (y2<end2) {
							g.drawLine(nextNextX,y2, nextNextX,y2);
							y2 += 2;
						}
					*/

					// Draw an arrow representing the tab.
					int halfHeight = height / 2;
					int quarterHeight = halfHeight / 2;
					int ymid = (int)y - ascent + halfHeight;
					g.drawLine((int)nextX,ymid, (int)nextNextX,ymid);
					g.drawLine((int)nextNextX,ymid, (int)nextNextX-4,ymid-quarterHeight);
					g.drawLine((int)nextNextX,ymid, (int)nextNextX-4,ymid+quarterHeight);

					x = nextNextX;
					break;

				case ' ':

					// NOTE:  There is a little bit of a "fudge factor"
					// here when "smooth text" is enabled, as "width"
					// below may well not be the width given to the space
					// by fm.charsWidth() (it depends on how it places the
					// space with respect to the preceding character).
					// But, we assume the approximation is close enough for
					// our drawing a dot for the space.

					// "flushLen+1" ensures text is aligned correctly (or,
					// aligned the same as in getWidth()).
					nextX = x+fm.charsWidth(text, flushIndex,flushLen+1);
					int width = fm.charWidth(' ');

					// Paint background.
					if (bg!=null) {
						paintBackground(x,y, nextX-x,height, g,
										ascent, host, bg);
					}
					g.setColor(fg);

					// Paint chars before space.
					if (flushLen>0) {
						g.drawString(
							new String(text, flushIndex, flushLen), x,y);
						flushLen = 0;
					}

					// Paint a dot representing the space.
					dotRect.x = nextX - width/2.0f; // "2.0f" for FindBugs
					dotRect.y = y - ascent + height/2.0f; // Ditto
					g.fill(dotRect);
					flushIndex = i + 1;
					x = nextX;
					break;


				case '\f':
					// ???
					// fall-through for now.

				default:
					flushLen += 1;
					break;

			}
		}

		nextX = x+fm.charsWidth(text, flushIndex,flushLen);

		if (flushLen>0 && nextX>=clipStart) {
			if (bg!=null) {
				paintBackground(x,y, nextX-x,height, g,
							ascent, host, bg);
			}
			g.setColor(fg);
			g.drawString(new String(text, flushIndex, flushLen), x,y);
		}

		if (host.getUnderlineForToken(this)) {
			g.setColor(fg);
			int y2 = (int)(y+1);
			g.drawLine(origX,y2, (int)nextX,y2);
		}

		return nextX;

	}


}