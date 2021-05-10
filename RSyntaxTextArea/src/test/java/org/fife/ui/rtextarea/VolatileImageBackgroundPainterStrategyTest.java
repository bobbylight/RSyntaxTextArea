/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.image.BufferedImage;


/**
 * Unit tests for the {@link VolatileImageBackgroundPainterStrategy} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class VolatileImageBackgroundPainterStrategyTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testPaintImage_noImage() {
		RSyntaxTextArea textArea = createTextArea();
		VolatileImageBackgroundPainterStrategy strategy = new VolatileImageBackgroundPainterStrategy(textArea);
		strategy.paintImage(createTestGraphics(), 0, 0);
	}


	@Test
	public void testPaintImage_withImage() {

		RSyntaxTextArea textArea = createTextArea();
		BufferedImageBackgroundPainterStrategy strategy = new BufferedImageBackgroundPainterStrategy(textArea);

		BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
		strategy.setImage(image);

		strategy.paintImage(createTestGraphics(), 0, 0);
	}
}
