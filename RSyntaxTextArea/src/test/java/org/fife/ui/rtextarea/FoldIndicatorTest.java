/*
 * 12/21/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.*;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link FoldIndicator} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class FoldIndicatorTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testCreateToolTip() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setBackground(Color.BLACK); // Something other than white
		FoldIndicator fi = new FoldIndicator(textArea);

		// Unfortunately we cannot verify the background matching the
		// text area's background since we cannot control the default
		// LaF's tool tip background color, which is used in the
		// calculation
		Assertions.assertNotNull(fi.createToolTip());
	}


	@Test
	void testGetSetFoldIconBackground() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setFoldIconBackground(Color.RED);
		Assertions.assertEquals(Color.RED, fi.getFoldIconBackground());
		fi.setFoldIconBackground(Color.GREEN);
		Assertions.assertEquals(Color.GREEN, fi.getFoldIconBackground());
	}


	@Test
	void testGetSetFoldIconArmedBackground() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setFoldIconArmedBackground(Color.RED);
		Assertions.assertEquals(Color.RED, fi.getFoldIconArmedBackground());
		fi.setFoldIconArmedBackground(Color.GREEN);
		Assertions.assertEquals(Color.GREEN, fi.getFoldIconArmedBackground());
	}


	@Test
	void testGetPreferredSize() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assertions.assertEquals(textArea.getHeight(), fi.getPreferredSize().height);
	}


	@Test
	void testGetSetAdditionalLeftMargin() {

		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assertions.assertEquals(0, fi.getAdditionalLeftMargin());
		fi.setAdditionalLeftMargin(5);
		Assertions.assertEquals(5, fi.getAdditionalLeftMargin());
	}


	@Test
	void testGetSetShowCollapsedRegionToolTips() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assertions.assertTrue(fi.getShowCollapsedRegionToolTips());
		fi.setShowCollapsedRegionToolTips(false);
		Assertions.assertFalse(fi.getShowCollapsedRegionToolTips());
	}


	@Test
	void testPaintComponent_lineWrap() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		// Paint collapsed and uncollapsed folds
		textArea.getFoldManager().getFold(0).getChild(0).setCollapsed(true);
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_noLineWrap() {
		RSyntaxTextArea textArea = createTextArea();
		// Paint collapsed and uncollapsed folds
		textArea.getFoldManager().getFold(0).getChild(0).setCollapsed(true);
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.paintComponent(createTestGraphics());
	}
}
