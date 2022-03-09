/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A base class for unit tests that need to create {@code RTextArea}s.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractRTextAreaTest {


	protected static Graphics2D createTestGraphics() {
		return createTestGraphics(80, 80);
	}


	protected static Graphics2D createTestGraphics(int width, int height) {
		Graphics2D g = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB).
			createGraphics();
		g.setClip(0, 0, width, height);
		return g;
	}
}
