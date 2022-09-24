/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;


/**
 * Unit tests for the {@code PlusMinusFoldIcon} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class PlusMinusFoldIconTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testGetIconHeight() {
		Assertions.assertEquals(8, new PlusMinusFoldIcon(true).getIconHeight());
	}


	@Test
	void testGetIconWidth() {
		Assertions.assertEquals(8, new PlusMinusFoldIcon(true).getIconWidth());
	}


	@Test
	void testPaintIcon_collapsed_armed() {
		PlusMinusFoldIcon icon = new PlusMinusFoldIcon(true);
		icon.setArmed(true);
		FoldIndicator fi = new FoldIndicator(createTextArea());
		fi.setFoldIconArmedBackground(Color.GREEN);
		icon.paintIcon(fi, createTestGraphics(), 0, 0);
	}


	@Test
	void testPaintIcon_notCollapsed_notArmed() {
		PlusMinusFoldIcon icon = new PlusMinusFoldIcon(false);
		icon.setArmed(false);
		icon.paintIcon(new FoldIndicator(createTextArea()), createTestGraphics(), 0, 0);
	}
}
