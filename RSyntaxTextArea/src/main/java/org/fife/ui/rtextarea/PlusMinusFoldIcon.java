/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.*;

/**
 * The default +/- icon for expanding and collapsing folds.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see ChevronFoldIcon
 */
class PlusMinusFoldIcon extends FoldIndicatorIcon {

	PlusMinusFoldIcon(boolean collapsed) {
		super(collapsed);
	}

	@Override
	public int getIconHeight() {
		return 8;
	}

	@Override
	public int getIconWidth() {
		return 8;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {

		FoldIndicator fi = (FoldIndicator)c;
		Color fg = fi.getForeground();
		Color bg = fi.getFoldIconBackground();
		if (isArmed()) {
			if (fi.getArmedForeground() != null) {
				fg = fi.getArmedForeground();
			}
			if (fi.getFoldIconArmedBackground() != null) {
				bg = fi.getFoldIconArmedBackground();
			}
		}

		g.setColor(bg);
		g.fillRect(x, y, 8, 8);

		g.setColor(fg);
		g.drawRect(x, y, 8, 8);
		g.drawLine(x + 2, y + 4, x + 2 + 4, y + 4);
		if (isCollapsed()) {
			g.drawLine(x + 4, y + 2, x + 4, y + 6);
		}
	}

}
