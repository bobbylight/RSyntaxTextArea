/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;


/**
 * Unit tests for the {@link VolatileImageBackgroundPainterStrategy} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class VolatileImageBackgroundPainterStrategyTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testPaintImage_noImage() {
		RSyntaxTextArea textArea = createTextArea();
		VolatileImageBackgroundPainterStrategy strategy = new VolatileImageBackgroundPainterStrategy(textArea);
		strategy.paintImage(createTestGraphics(), 0, 0);
	}


	@Test
	void testPaintImage_withConcreteImage() {

		RSyntaxTextArea textArea = createTextArea();
		VolatileImageBackgroundPainterStrategy strategy = new VolatileImageBackgroundPainterStrategy(textArea);

		BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
		strategy.setImage(image);

		Rectangle bounds = new Rectangle(0, 0, 80, 80);
		strategy.paint(createTestGraphics(), bounds);
	}


	@Test
	void testPaintImage_withImageFromUrl() {

		RSyntaxTextArea textArea = createTextArea();
		VolatileImageBackgroundPainterStrategy strategy = new VolatileImageBackgroundPainterStrategy(textArea);

		URL url = getClass().getResource("example-image.png");
		strategy.setImage(url);

		Rectangle bounds = new Rectangle(0, 0, 80, 80);
		strategy.paint(createTestGraphics(), bounds);
	}
}
