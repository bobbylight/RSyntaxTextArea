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


	public static Graphics2D createTestGraphics() {
		Graphics2D g = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB).createGraphics();
		g.setClip(0, 0, 80, 80);
		return g;
	}
}
