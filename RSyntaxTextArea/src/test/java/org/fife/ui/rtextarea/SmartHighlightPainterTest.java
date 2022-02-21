/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;


/**
 * Unit tests for the {@code SmartHighlightPainter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class SmartHighlightPainterTest extends AbstractRTextAreaTest {


	@Test
	void testConstructor_zeroArg() {
		SmartHighlightPainter p = new SmartHighlightPainter();
		Assertions.assertEquals(SmartHighlightPainter.DEFAULT_HIGHLIGHT_COLOR, p.getPaint());
	}


	@Test
	void testConstructor_paintArg() {
		SmartHighlightPainter p = new SmartHighlightPainter(Color.BLUE);
		Assertions.assertEquals(Color.BLUE, p.getPaint());
	}


	@Test
	void testGetSetPaint() {
		SmartHighlightPainter p = new SmartHighlightPainter();
		Assertions.assertEquals(SmartHighlightPainter.DEFAULT_HIGHLIGHT_COLOR, p.getPaint());
		p.setPaint(Color.BLUE);
		Assertions.assertEquals(Color.BLUE, p.getPaint());
	}


	@Test
	void testGetSetPaintBorder() {
		SmartHighlightPainter p = new SmartHighlightPainter();
		Assertions.assertFalse(p.getPaintBorder());
		p.setPaintBorder(true);
		Assertions.assertTrue(p.getPaintBorder());
	}


	@Test
	void testPaintLayer_happyPath_noPaintBorder() {
		SmartHighlightPainter p = new SmartHighlightPainter();
		testPaintLayerImpl(p, 2, 4);
	}


	@Test
	void testPaintLayer_happyPath_paintBorder() {
		SmartHighlightPainter p = new SmartHighlightPainter();
		p.setPaintBorder(true);
		testPaintLayerImpl(p, 2, 4);
	}


	@Test
	void testPaintLayer_emptyHighlight() {
		SmartHighlightPainter p = new SmartHighlightPainter();
		p.setPaintBorder(true);
		testPaintLayerImpl(p, 2, 2);
	}


	private void testPaintLayerImpl(SmartHighlightPainter p, int p0, int p1) {

		JTextArea textArea = new JTextArea("line 1\nline 2\nline 3");
		Rectangle viewBounds = textArea.getVisibleRect();
		View view = textArea.getUI().getRootView(textArea);
		Graphics g = createTestGraphics();
		p.paintLayer(g, p0, p1, viewBounds, textArea, view);
	}
}
