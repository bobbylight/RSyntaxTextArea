/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;


/**
 * Unit tests for the {@link SizeGrip} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class SizeGripTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testPaintComponent_ltr() {
		SizeGrip grip = new SizeGrip();
		grip.paintComponent(createTestGraphics());
	}


	@Test
	public void testPaintComponent_rtl() {
		SizeGrip grip = new SizeGrip();
		grip.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		grip.paintComponent(createTestGraphics());
	}


	@Test
	public void testPaintComponent_osx() {

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
