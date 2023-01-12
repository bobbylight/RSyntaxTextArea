package org.fife.util;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Utilities to handle fractional scaling on Windows by leveraging the floating point API.
 * Compare to deprecated <code>javax.swing.SwingUtilities2</code>
 */
public class SwingUtils {

	public static void drawChars(Graphics2D g, float x, float y, char[] text, int index, int length) {
		g.drawString(new String(text, index, length), x, y);
	}

	public static float charWidth(FontMetrics fm, char c) {
		return charsWidth(fm, new char[]{c}, 1, 0);
	}

	public static float charsWidth(FontMetrics fm, char[] text, int offset, int length) {
		Rectangle2D bounds = fm.getFont().getStringBounds(text, offset, offset + length, fm.getFontRenderContext());
		return (float) bounds.getWidth();
	}
}
