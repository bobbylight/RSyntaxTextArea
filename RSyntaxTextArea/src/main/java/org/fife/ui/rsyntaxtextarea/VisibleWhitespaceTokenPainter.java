/*
 * 03/16/2013
 *
 * VisibleWhitespaceTokenPainter - Renders tokens in an instance of
 * RSyntaxTextArea, with special glyphs to denote spaces and tabs.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.util.SwingUtils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.swing.text.TabExpander;


/**
 * A token painter that visibly renders whitespace (spaces and tabs).<p>
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
 * @version 1.0
 * @see DefaultTokenPainter
 */
public class VisibleWhitespaceTokenPainter extends DefaultTokenPainter {


	@Override
	protected float paintImpl(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart,
			boolean selected, boolean useSTC) {

		float origX = x;
		int textOffs = token.getTextOffset();
		char[] text = token.getTextArray();
		int end = textOffs + token.length();
		float nextX = x;
		int flushLen = 0;
		int flushIndex = textOffs;
		Color fg = useSTC ? host.getSelectedTextColor() :
			host.getForegroundForToken(token);
		Color bg = selected ? null : host.getBackgroundForToken(token);
		g.setFont(host.getFontForToken(token));
		FontMetrics fm = host.getFontMetricsForToken(token);

		int ascent = fm.getAscent();
		int height = fm.getHeight();

		for (int i=textOffs; i<end; i++) {

			switch (text[i]) {

				case '\t':

					// Fill in background.
					nextX = x + SwingUtils.charsWidth(fm, text, flushIndex,flushLen);
					float nextTabStop = e.nextTabStop(nextX, 0);
					if (bg!=null) {
						paintBackground(x,y, nextTabStop-x,height, g,
										ascent, host, bg);
					}
					g.setColor(fg);

					// Paint chars cached before the tab.
					if (flushLen > 0) {
						SwingUtils.drawChars(g, x, y, text, flushIndex, flushLen);
						flushLen = 0;
					}
					flushIndex = i + 1;

					paintTabText(g, nextX, y, nextTabStop, ascent, height);
					x = nextTabStop;
					break;

				case ' ':

					// NOTE:  There is a little bit of a "fudge factor"
					// here when "smooth text" is enabled, as "width"
					// below may well not be the width given to the space
					// by charsWidth(fm, ) (it depends on how it places the
					// space with respect to the preceding character).
					// But, we assume the approximation is close enough for
					// our drawing a dot for the space.

					// "flushLen+1" ensures text is aligned correctly (or,
					// aligned the same as in getWidth()).
					nextX = x + SwingUtils.charsWidth(fm, text, flushIndex,flushLen+1);
					float width = SwingUtils.charWidth(fm, ' ');

					// Paint background.
					if (bg!=null) {
						paintBackground(x,y, nextX-x,height, g,
										ascent, host, bg);
					}
					g.setColor(fg);

					// Paint chars before space.
					if (flushLen>0) {
						SwingUtils.drawChars(g, x, y, text, flushIndex, flushLen);
						flushLen = 0;
					}

					paintSpaceText(g, nextX, y, ascent, width, height);
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

		nextX = x + SwingUtils.charsWidth(fm, text, flushIndex,flushLen);

		if (flushLen>0 && nextX>=clipStart) {
			if (bg!=null) {
				paintBackground(x,y, nextX-x,height, g,
							ascent, host, bg);
			}
			g.setColor(fg);
			SwingUtils.drawChars(g, x, y, text, flushIndex, flushLen);
		}

		if (host.getUnderlineForToken(token)) {
			g.setColor(fg);
			float y2 = y+1;
			SwingUtils.drawLine(g, origX,y2, nextX,y2);
		}

		// Don't check if it's whitespace - some TokenMakers may return types
		// other than Token.WHITESPACE for spaces (such as Token.IDENTIFIER).
		// This also allows us to paint tab lines for MLC's.
		if (host.getPaintTabLines() && origX==host.getMargin().left) {// && isWhitespace()) {
			paintTabLines(token, origX, y, nextX, g, e, host);
		}

		return nextX;

	}


	/**
	 * Draws the textual indication of a space, i.e. a single dot. The
	 * background has already been filled in properly, and the foreground
	 * color properly set.
	 *
	 * @param g The graphics context.
	 * @param x The x-value at which to paint.
	 * @param y The y-value of the current line.
	 * @param ascent The ascent of the current font.
	 * @param width The width of the space character.
	 * @param height The height of the current line of text being painted.
	 */
	protected void paintSpaceText(Graphics2D g, float x, float y, int ascent,
								  int width, int height) {
		float dotX = x - width/2f; // "2.0f" for FindBugs
		float dotY = y - ascent + height/2f; // Ditto
		SwingUtils.drawLine(g, dotX, dotY, dotX, dotY);
	}


	/**
	 * Draws the text representing a tab. The background has already been
	 * filled in if necessary, and the foreground color properly set.
	 *
	 * @param g The graphics context.
	 * @param x The x-value at which to paint.
	 * @param y The y-value of the current line.
	 * @param nextTabStop Where the next tab stop would start.
	 * @param ascent The ascent of the current font.
	 * @param height The height of the line of text being painted.
	 */
	protected void paintTabText(Graphics2D g, float x, float y,
						float nextTabStop, int ascent, int height) {
		float halfHeight = height / 2;
		float quarterHeight = halfHeight / 2;
		float ymid = (int)y - ascent + halfHeight;
		SwingUtils.drawLine(g, x,ymid, nextTabStop,ymid);
		SwingUtils.drawLine(g, nextTabStop,ymid, nextTabStop-4,ymid-quarterHeight);
		SwingUtils.drawLine(g, nextTabStop,ymid, nextTabStop-4,ymid+quarterHeight);
	}

}
