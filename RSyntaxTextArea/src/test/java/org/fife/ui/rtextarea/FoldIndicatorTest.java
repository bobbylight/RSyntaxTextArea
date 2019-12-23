/*
 * 12/21/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link FoldIndicator} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class FoldIndicatorTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testCreateToolTip() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setBackground(Color.BLACK); // Something other than white
		FoldIndicator fi = new FoldIndicator(textArea);

		// Unfortunately we cannot verify the background matching the
		// text area's background since we cannot control the default
		// LaF's tool tip background color, which is used in the
		// calculation
		Assert.assertNotNull(fi.createToolTip());
	}


	@Test
	public void testGetSetFoldIconBackground() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setFoldIconBackground(Color.RED);
		Assert.assertEquals(Color.RED, fi.getFoldIconBackground());
		fi.setFoldIconBackground(Color.GREEN);
		Assert.assertEquals(Color.GREEN, fi.getFoldIconBackground());
	}


	@Test
	public void testGetSetFoldIconArmedBackground() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setFoldIconArmedBackground(Color.RED);
		Assert.assertEquals(Color.RED, fi.getFoldIconArmedBackground());
		fi.setFoldIconArmedBackground(Color.GREEN);
		Assert.assertEquals(Color.GREEN, fi.getFoldIconArmedBackground());
	}


	@Test
	public void testGetPreferredSize() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assert.assertEquals(textArea.getHeight(), fi.getPreferredSize().height);
	}


	@Test
	public void testGetSetShowCollapsedRegionToolTips() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assert.assertTrue(fi.getShowCollapsedRegionToolTips());
		fi.setShowCollapsedRegionToolTips(false);
		Assert.assertFalse(fi.getShowCollapsedRegionToolTips());
	}


	@Test
	public void testPaintComponent_lineWrap() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		// Paint collapsed and uncollapsed folds
		textArea.getFoldManager().getFold(0).getChild(0).setCollapsed(true);
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.paintComponent(createTestGraphics());
	}


	@Test
	public void testPaintComponent_noLineWrap() {
		RSyntaxTextArea textArea = createTextArea();
		// Paint collapsed and uncollapsed folds
		textArea.getFoldManager().getFold(0).getChild(0).setCollapsed(true);
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.paintComponent(createTestGraphics());
	}
}
