/*
 * 12/21/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.*;
import java.awt.event.MouseEvent;

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
	void testGetPreferredSize() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assertions.assertEquals(textArea.getHeight(), fi.getPreferredSize().height);
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
	void testGetSetAdditionalLeftMargin() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assertions.assertEquals(0, fi.getAdditionalLeftMargin());
		fi.setAdditionalLeftMargin(5);
		Assertions.assertEquals(5, fi.getAdditionalLeftMargin());
	}


	@Test
	void testGetSetAdditionalLeftMargin_error_negativeValue() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> fi.setAdditionalLeftMargin(-1));
	}


	@Test
	void testGetSetArmedForeground() {

		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);

		Color color = Color.red;
		fi.setArmedForeground(color);
		Assertions.assertEquals(color, fi.getArmedForeground());

		color = Color.green;
		fi.setArmedForeground(color);
		Assertions.assertEquals(color, fi.getArmedForeground());

		// Sets to default - not a public value, but also not Color.green.
		fi.setArmedForeground(null);
		Assertions.assertNotNull(fi.getArmedForeground());
		Assertions.assertNotEquals(color, fi.getArmedForeground());

	}


	@Test
	void testGetSetExpandedFoldRenderStrategy() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		Assertions.assertEquals(ExpandedFoldRenderStrategy.ON_HOVER, fi.getExpandedFoldRenderStrategy());
		fi.setExpandedFoldRenderStrategy(ExpandedFoldRenderStrategy.ALWAYS);
		Assertions.assertEquals(ExpandedFoldRenderStrategy.ALWAYS, fi.getExpandedFoldRenderStrategy());
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
	void testGetToolTipLocation_codeFoldingNotEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setCodeFoldingEnabled(false);
		FoldIndicator fi = new FoldIndicator(textArea);
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 0, 0, 1, false);
		// Code folding disabled -> component never renders tool tip text
		Assertions.assertNull(fi.getToolTipLocation(e));
	}


	@Test
	void testGetToolTipLocation_codeFoldingEnabledButStillNoToolTipText() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 3, 3, 1, false);
		Assertions.assertNull(fi.getToolTipLocation(e));
	}


	@Test
	void testGetToolTipLocation_codeFoldingEnabledAndHoverOverCollapsedFold() {

		RSyntaxTextArea textArea = createTextArea("{\n  println(\"hi\");\n}\n");

		// FoldIndicator needs a parent Gutter to calculate where to display
		// the tool tip, but Gutter doesn't expose its child FoldIndicator
		// via its API.  So here we really hack things to get around this.
		FoldIndicator fi = new FoldIndicator(textArea);
		Gutter hackyGutter = new Gutter(textArea);
		hackyGutter.add(fi);

		// Collapse the top-level fold, and create a synthetic mouse-over
		// event over its fold indicator.
		textArea.getFoldManager().getFold(0).setCollapsed(true);
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 3, 3, 0, false);

		// Don't check against a specific pixel location in case tests run
		// with slightly different default LaFs
		Assertions.assertNotNull(fi.getToolTipLocation(e));
	}


	@Test
	void testGetToolTipText_codeFoldingNotEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setCodeFoldingEnabled(false);
		FoldIndicator fi = new FoldIndicator(textArea);
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 0, 0, 1, false);
		// Code folding disabled -> component never renders tool tip text
		Assertions.assertNull(fi.getToolTipText(e));
	}


	@Test
	void testGetToolTipText_codeFoldingEnabledButNotOverACollapsedFold() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 3, 3, 0, false);
		Assertions.assertNull(fi.getToolTipText(e));
	}


	@Test
	void testGetToolTipText_codeFoldingEnabledAndHoverOverCollapsedFold() {

		RSyntaxTextArea textArea = createTextArea("{\n  println(\"hi\");\n}\n");

		// FoldIndicator needs a parent Gutter to calculate where to display
		// the tool tip, but Gutter doesn't expose its child FoldIndicator
		// via its API.  So here we really hack things to get around this.
		FoldIndicator fi = new FoldIndicator(textArea);
		Gutter hackyGutter = new Gutter(textArea);
		hackyGutter.add(fi);

		// Collapse the top-level fold, and create a synthetic mouse-over
		// event over its fold indicator.
		textArea.getFoldManager().getFold(0).setCollapsed(true);
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 3, 3, 0, false);

		// HTML-ified version of the text above. No specific font since it
		// varies depending on the OS
		String expected = "<html><nobr><font face=\"[\\w ]+\" color=\"#ff0000\">\\{" +
			"</font><br><font face=\"[\\w ]+\"> &nbsp;</font>" +
			"<font face=\"[\\w ]+\" color=\"#000000\">println</font>" +
			"<font face=\"[\\w ]+\" color=\"#ff0000\">\\(</font>" +
			"<font face=\"[\\w ]+\" color=\"#dc009c\">&#34;hi&#34;</font>" +
			"<font face=\"[\\w ]+\" color=\"#ff0000\">\\)</font>" +
			"<font face=\"[\\w ]+\" color=\"#000000\">;</font><br>" +
			"<font face=\"[\\w ]+\" color=\"#ff0000\">}</font><br>";
		Assertions.assertTrue(fi.getToolTipText(e).matches(expected));
	}


	@Test
	void testPaintComponent_classicLook() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setStyle(FoldIndicatorStyle.CLASSIC);
		fi.paintComponent(createTestGraphics());
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


	@Test
	void testPaintComponent_modernLook() {

		RSyntaxTextArea textArea = createTextArea();

		// FoldIndicator needs a parent Gutter to calculate where to display
		// the tool tip, but Gutter doesn't expose its child FoldIndicator
		// via its API.  So here we really hack things to get around this.
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setStyle(FoldIndicatorStyle.MODERN);
		Gutter hackyGutter = new Gutter(textArea);
		hackyGutter.add(fi);

		fi.paintComponent(createTestGraphics());
	}


	@Test
	void testSetFoldIcons() {
		RSyntaxTextArea textArea = createTextArea();
		FoldIndicator fi = new FoldIndicator(textArea);
		fi.setFoldIcons(new EmptyTestFoldIndicatorIcon(true), new EmptyTestFoldIndicatorIcon(false));
	}
}
