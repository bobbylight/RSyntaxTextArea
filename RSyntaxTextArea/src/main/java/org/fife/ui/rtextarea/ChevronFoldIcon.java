/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.*;
import java.awt.geom.Path2D;


/**
 * A fold icon represented by a chevron.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see PlusMinusFoldIcon
 */
class ChevronFoldIcon extends FoldIndicatorIcon {

	ChevronFoldIcon(boolean collapsed) {
		super(collapsed);
	}

	/**
	 * Creates a closed path for the given points.
	 */
	private Path2D createPath(double... points) {
		Path2D path = new Path2D.Float();
		path.moveTo(points[0], points[1]);
		for (int i = 2; i < points.length; i += 2) {
			path.lineTo(points[i], points[i + 1]);
		}
		path.closePath();
		return path;
	}

	@Override
	public int getIconHeight() {
		return 11;
	}

	@Override
	public int getIconWidth() {
		return 11;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {

		FoldIndicator fi = (FoldIndicator)c;
		Color fg = c.getForeground();
		if (isArmed() && fi.getArmedForeground() != null) {
			fg = fi.getArmedForeground();
		}

		int width = getIconWidth();
		int height = getIconHeight();
		Graphics2D g2d = (Graphics2D)g.create();

		try {

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(fg);
			g2d.translate(x, y);
			if (!isCollapsed()) {
				g2d.rotate(Math.toRadians(90), width / 2f, height / 2f);
			}
			g2d.fill(createPath(3, 1, 3, 2.5, 6, 5.5, 3, 8.5, 3, 10, 4.5, 10, 9, 5.5, 4.5, 1));
		} finally {
			g2d.dispose();
		}
	}

}
