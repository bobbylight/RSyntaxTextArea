/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;

import org.fife.ui.SwingRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Unit tests for the {@link SizeGrip} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class SizeGripTest {


	private static Graphics createTestGraphics() {
		Graphics g = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB).getGraphics();
		g.setClip(0, 0, 80, 80);
		return g;
	}


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
