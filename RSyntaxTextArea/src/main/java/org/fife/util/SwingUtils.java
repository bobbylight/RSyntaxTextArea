package org.fife.util;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Utilities to handle fractional scaling on Windows by leveraging the floating point API.
 * Compare to deprecated <code>javax.swing.SwingUtilities2</code>
 */
public class SwingUtils {
	private SwingUtils() {
	}

	public static void drawChars(Graphics2D g, float x, float y, char[] text, int index, int length) {
		g.drawString(new String(text, index, length), x, y);
	}

	public static float charWidth(FontMetrics fm, char c) {
		return charsWidth(fm, new char[]{c}, 0, 1);
	}

	public static float charsWidth(FontMetrics fm, char[] text, int offset, int length) {
		Rectangle2D bounds = fm.getFont().getStringBounds(text, offset, offset + length, fm.getFontRenderContext());
		return (float) bounds.getWidth();
	}

	/** Convenience method */
	public static void setY(Rectangle2D r, double y) {
		r.setRect(r.getX(), y, r.getWidth(), r.getHeight());
	}

	/** Convenience method */
	public static void setHeight(Rectangle2D r, double height) {
		r.setRect(r.getX(), r.getY(), r.getWidth(), height);
	}

	/** Convenience method */
	public static void setWidth(Rectangle2D r, double width) {
		r.setRect(r.getX(), r.getY(), width, r.getHeight());
	}

	/** Convenience method */
	public static void setX(Rectangle2D r, double x) {
		r.setRect(x, r.getY(), r.getWidth(), r.getHeight());
	}
}
