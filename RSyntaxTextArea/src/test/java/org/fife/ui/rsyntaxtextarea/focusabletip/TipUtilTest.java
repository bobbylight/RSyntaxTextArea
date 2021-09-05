/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.focusabletip;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Unit tests for the {@link TipUtil} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class TipUtilTest {

	@Test
	void getToolTipBackground_zeroArg() {
		Assertions.assertNotNull(TipUtil.getToolTipBackground());
	}

	/**
	 * If the text area uses a white background (typical for "light"
	 * application themes), the Look and Feel's default tool tip
	 * color should be returned.
	 */
	@Test
	void testToolTipBackground_oneArg_whiteBackground() {

		Color defaultTipBG = TipUtil.getToolTipBackground();

		RTextArea textArea = new RTextArea();
		textArea.setBackground(Color.WHITE);

		Assertions.assertEquals(defaultTipBG, TipUtil.getToolTipBackground(textArea));
	}

	/**
	 * If the text area uses some oddball background color, that color
	 * should be returned to ensure the tip's content matches the style
	 * of the content of the text area.
	 */
	@Test
	void testToolTipBackground_oneArg_nonWhiteBackground() {

		RTextArea textArea = new RTextArea();
		textArea.setBackground(Color.RED);

		Assertions.assertEquals(Color.RED, TipUtil.getToolTipBackground(textArea));
	}

	@Test
	void getToolTipBorder_zeroArg() {
		Assertions.assertNotNull(TipUtil.getToolTipBorder());
	}

	/**
	 * If the text area uses a white background (typical for "light"
	 * application themes), the Look and Feel's default tool tip
	 * border should be returned.
	 */
	@Test
	void testToolTipBorder_oneArg_whiteBackground() {

		RTextArea textArea = new RTextArea();
		textArea.setBackground(Color.WHITE);

		Assertions.assertNotNull(TipUtil.getToolTipBackground(textArea));
	}

	/**
	 * If the text area uses some oddball background color, the border
	 * returned should coordinate since that's what the tool tip's
	 * background color will be.
	 */
	@Test
	void testToolTipBorder_oneArg_nonWhiteBackground() {

		RTextArea textArea = new RTextArea();
		textArea.setBackground(Color.RED);

		Border actual = TipUtil.getToolTipBorder(textArea);
		Assertions.assertTrue(actual instanceof LineBorder);
		Border expected = BorderFactory.createLineBorder(Color.RED.brighter());
		Assertions.assertEquals(Color.RED.brighter(), ((LineBorder)actual).getLineColor());

	}
}
