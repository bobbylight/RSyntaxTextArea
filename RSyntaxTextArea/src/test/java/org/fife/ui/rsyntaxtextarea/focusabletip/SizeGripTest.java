/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;


/**
 * Unit tests for the {@link SizeGrip} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class SizeGripTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testPaintComponent_ltr() {
		SizeGrip grip = new SizeGrip();
		grip.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_rtl() {
		SizeGrip grip = new SizeGrip();
		grip.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		grip.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_osx() {

		String osName = System.getProperty("os.name");
		System.setProperty("os.name", "OS X");

		try {
			SizeGrip grip = new SizeGrip();
			grip.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			grip.paintComponent(createTestGraphics());
		} finally {
			System.setProperty("os.name", osName);
		}
	}
}
