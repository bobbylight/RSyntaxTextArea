/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;


import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

/**
 * Unit tests for the {@link FocusableTip} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class FocusableTipTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testGetSetImageBase() throws IOException {

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new FocusableTip(textArea, null);

		Assert.assertNull(tip.getImageBase());
		URL url = new URL("https://www.google.com");
		tip.setImageBase(url);
		Assert.assertEquals(url, tip.getImageBase());
	}


	@Test
	public void testGetSetMaxSize() throws IOException {

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new FocusableTip(textArea, null);

		Assert.assertNull(tip.getMaxSize());
		Dimension maxSize = new Dimension(50, 50);
		tip.setMaxSize(maxSize);
		Assert.assertEquals(maxSize, tip.getMaxSize());
	}


	@Test
	public void testShowFocusableTip_happyPath() {

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new FocusableTip(textArea, null);
		MouseEvent e = new MouseEvent(textArea, 0, System.currentTimeMillis(),
			0, 10, 10, 1, false);
		tip.toolTipRequested(e, "Hello world");

		tip.possiblyDisposeOfTipWindow();
	}
}
