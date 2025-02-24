/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;


import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;
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
@ExtendWith(SwingRunnerExtension.class)
class FocusableTipTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testGetSetImageBase() throws IOException {

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new TestableFocusableTip(textArea, null);

		Assertions.assertNull(tip.getImageBase());
		URL url = new URL("https://www.google.com");
		tip.setImageBase(url);
		Assertions.assertEquals(url, tip.getImageBase());
	}


	@Test
	void testGetSetMaxSize() {

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new TestableFocusableTip(textArea, null);

		Assertions.assertNull(tip.getMaxSize());
		Dimension maxSize = new Dimension(50, 50);
		tip.setMaxSize(maxSize);
		Assertions.assertEquals(maxSize, tip.getMaxSize());
	}


	@Test
	void testHide_onEmptyContent() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new TestableFocusableTip(textArea, null);
		MouseEvent e = new MouseEvent(textArea, 0, System.currentTimeMillis(),
			0, 10, 10, 1, false);
		tip.toolTipRequested(e, "Hello world");
		Assertions.assertTrue(tip.isVisible());

		tip.toolTipRequested(e, "");

		// Verify tool tip was hidden and disposed of
		Assertions.assertFalse(tip.possiblyDisposeOfTipWindow());
	}


	@Test
	void testHide_onNullContent() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new TestableFocusableTip(textArea, null);
		MouseEvent e = new MouseEvent(textArea, 0, System.currentTimeMillis(),
			0, 10, 10, 1, false);
		tip.toolTipRequested(e, "Hello world");
		Assertions.assertTrue(tip.isVisible());

		tip.toolTipRequested(e, null);

		// Verify tool tip was hidden and disposed of
		Assertions.assertFalse(tip.possiblyDisposeOfTipWindow());
	}


	@Test
	void testHide_onTextAreaContentUpdated() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new TestableFocusableTip(textArea, null);
		MouseEvent e = new MouseEvent(textArea, 0, System.currentTimeMillis(),
			0, 10, 10, 1, false);
		tip.toolTipRequested(e, "Hello world");
		Assertions.assertTrue(tip.isVisible());

		textArea.insert("foo", 0);

		// Verify tool tip was hidden and disposed of
		Assertions.assertFalse(tip.possiblyDisposeOfTipWindow());
	}


	@Test
	void testShowFocusableTip_display_happyPath() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RSyntaxTextArea textArea = createTextArea();
		FocusableTip tip = new TestableFocusableTip(textArea, null);
		MouseEvent e = new MouseEvent(textArea, 0, System.currentTimeMillis(),
			0, 10, 10, 1, false);
		tip.toolTipRequested(e, "Hello world");

		tip.possiblyDisposeOfTipWindow();
	}


	/**
	 * Needed to avoid a call to {@code SwingUtilities.invokeLater()}
	 * which doesn't seem possible to unit test.
	 */
	static class TestableFocusableTip extends FocusableTip {

		TestableFocusableTip(JTextArea textArea, HyperlinkListener listener) {
			super(textArea, listener);
		}

		@Override
		protected void invokeLater(Runnable r) {
			r.run();
		}
	}
}
