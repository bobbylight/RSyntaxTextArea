/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.*;

/**
 * An empty fold icon for test purposes.
 */
class EmptyTestFoldIndicatorIcon extends FoldIndicatorIcon {

	protected EmptyTestFoldIndicatorIcon(boolean collapsed) {
		super(collapsed);
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// Do nothing - comment for Sonar
	}

	@Override
	public int getIconWidth() {
		return 0;
	}

	@Override
	public int getIconHeight() {
		return 0;
	}
}
