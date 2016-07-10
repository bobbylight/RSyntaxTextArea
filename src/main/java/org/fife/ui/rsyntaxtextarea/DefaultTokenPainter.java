/*
 * 03/16/2013
 *
 * DefaultTokenPainter - Standard implementation of a token painter.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.text.TabExpander;


/**
 * Standard implementation of a token painter.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DefaultTokenPainter implements TokenPainter {

	/**
	 * Rectangle used for filling token backgrounds.
	 */
	private Rectangle2D.Float bgRect;

	/**
	 * Micro-optimization; buffer used to compute tab width.  If the width is
	 * correct it's not re-allocated, to prevent lots of very small garbage.
	 * Only used when painting tab lines.
	 */
	private static char[] tabBuf;


	DefaultTokenPainter() {
		bgRect = new Rectangle2D.Float();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public final float paint(Token token, Graphics2D g, float x, float y,
						RSyntaxTextArea host, TabExpander e) {
		return paint(token, g, x,y, host, e, 0);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public float paint(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart) {
		return paintImpl(token, g, x, y, host, e, clipStart, false, false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public float paint(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart,
			boolean paintBG) {
		return paintImpl(token, g, x, y, host, e, clipStart, !paintBG, false);
	}


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
							Graphics2D g, int fontAscent, RSyntaxTextArea host,
							Color color) {
		g.setColor(color);
		bgRect.setRect(x,y-fontAscent, width,height);
		//g.fill(bgRect);
		g.fillRect((int)x, (int)(y-fontAscent), (int)width, (int)height);
	}


	/**
	 * Does the dirty-work of actually painting the token.
	 */
	protected float paintImpl(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart,
			boolean selected, boolean useSTC) {

		int origX = (int)x;
		int textOffs = token.getTextOffset();
		char[] text = token.getTextArray();
		int end = textOffs + token.length();
		float nextX = x;
		int flushLen = 0;
		int flushIndex = textOffs;
		Color fg = useSTC ? host.getSelectedTextColor() :
			host.getForegroundForToken(token);
		Color bg = selected ? null : host.getBackgroundForToken(token);
		g.setFont(host.getFontForTokenType(token.getType()));
		FontMetrics fm = host.getFontMetricsForTokenType(token.getType());

		for (int i=textOffs; i<end; i++) {
			switch (text[i]) {
				case '\t':
					nextX = e.nextTabStop(
						x + fm.charsWidth(text, flushIndex,flushLen), 0);
					if (bg!=null) {
						paintBackground(x,y, nextX-x,fm.getHeight(),
									g, fm.getAscent(), host, bg);
					}
					if (flushLen > 0) {
						g.setColor(fg);
						g.drawChars(text, flushIndex, flushLen, (int)x,(int)y);
						flushLen = 0;
					}
					flushIndex = i + 1;
					x = nextX;
					break;
				default:
					flushLen += 1;
					break;
			}
		}

		nextX = x+fm.charsWidth(text, flushIndex,flushLen);
java.awt.Rectangle r = host.getMatchRectangle();

		if (flushLen>0 && nextX>=clipStart) {
			if (bg!=null) {
				paintBackground(x,y, nextX-x,fm.getHeight(),
								g, fm.getAscent(), host, bg);
				if (token.length()==1 && r!=null && r.x==x) {
					((RSyntaxTextAreaUI)host.getUI()).paintMatchedBracketImpl(
							g, host, r);
				}
			}
			g.setColor(fg);
			g.drawChars(text, flushIndex, flushLen, (int)x,(int)y);
		}

		if (host.getUnderlineForToken(token)) {
			g.setColor(fg);
			int y2 = (int)(y+1);
			g.drawLine(origX,y2, (int)nextX,y2);
		}

		// Don't check if it's whitespace - some TokenMakers may return types
		// other than Token.WHITESPACE for spaces (such as Token.IDENTIFIER).
		// This also allows us to paint tab lines for MLC's.
		if (host.getPaintTabLines() && origX==host.getMargin().left) {// && isWhitespace()) {
			paintTabLines(token, origX, (int)y, (int)nextX, g, e, host);
		}

		return nextX;

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public float paintSelected(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, boolean useSTC) {
		return paintSelected(token, g, x, y, host, e, 0, useSTC);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public float paintSelected(Token token, Graphics2D g, float x, float y,
			RSyntaxTextArea host, TabExpander e, float clipStart,
			boolean useSTC) {
		return paintImpl(token, g, x, y, host, e, clipStart, true, useSTC);
	}


	/**
	 * Paints dotted "tab" lines; that is, lines that show where your caret
	 * would go to on the line if you hit "tab".  This visual effect is usually
	 * done in the leading whitespace token(s) of lines.
	 *
	 * @param token The token to render.
	 * @param x The starting x-offset of this token.  It is assumed that this
	 *        is the left margin of the text area (may be non-zero due to
	 *        insets), since tab lines are only painted for leading whitespace.
	 * @param y The baseline where this token was painted.
	 * @param endX The ending x-offset of this token.
	 * @param g The graphics context.
	 * @param e Used to expand tabs.
	 * @param host The text area.
	 */
	protected void paintTabLines(Token token, int x, int y, int endX,
				Graphics2D g, TabExpander e, RSyntaxTextArea host) {

		// We allow tab lines to be painted in more than just Token.WHITESPACE,
		// i.e. for MLC's and Token.IDENTIFIERS (for TokenMakers that return
		// whitespace as identifiers for performance).  But we only paint tab
		// lines for the leading whitespace in the token.  So, if this isn't a
		// WHITESPACE token, figure out the leading whitespace's length.
		if (token.getType()!=Token.WHITESPACE) {
			int offs = 0;
			for (; offs<token.length(); offs++) {
				if (!RSyntaxUtilities.isWhitespace(token.charAt(offs))) {
					break; // MLC text, etc.
				}
			}
			if (offs<2) { // Must be at least two spaces to see tab line
				return;
			}
			//endX = x + (int)getWidthUpTo(offs, host, e, x);
			endX = (int)token.getWidthUpTo(offs, host, e, 0);
		}

		// Get the length of a tab.
		FontMetrics fm = host.getFontMetricsForTokenType(token.getType());
		int tabSize = host.getTabSize();
		if (tabBuf==null || tabBuf.length<tabSize) {
			tabBuf = new char[tabSize];
			for (int i=0; i<tabSize; i++) {
				tabBuf[i] = ' ';
			}
		}
		// Note different token types (MLC's, whitespace) could possibly be
		// using different fonts, which means we can't cache the actual width
		// of a tab as it may be different per-token-type.  We could keep a
		// per-token-type cache, but we'd have to clear it whenever they
		// modified token styles.
		int tabW = fm.charsWidth(tabBuf, 0, tabSize);

		// Draw any tab lines.  Here we're assuming that "x" is the left
		// margin of the editor.
		g.setColor(host.getTabLineColor());
		int x0 = x + tabW;
		int y0 = y - fm.getAscent();
		if ((y0&1)>0) {
			// Only paint on even y-pixels to prevent doubling up between lines
			y0++;
		}

		// TODO: Go to endX (inclusive) if this token is last token in the line
		Token next = token.getNextToken();
		if (next==null || !next.isPaintable()) {
			endX++;
		}
		while (x0<endX) {
			int y1 = y0;
			int y2 = y0 + host.getLineHeight();
			while (y1<y2) {
				g.drawLine(x0, y1, x0, y1);
				y1 += 2;
			}
			//g.drawLine(x0,y0, x0,y0+host.getLineHeight());
			x0 += tabW;
		}

	}


}