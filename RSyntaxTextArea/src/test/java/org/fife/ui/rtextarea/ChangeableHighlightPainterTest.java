/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;


/**
 * Unit tests for the {@link ChangeableHighlightPainter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class ChangeableHighlightPainterTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testGetSetAlpha() {
		ChangeableHighlightPainter painter = new ChangeableHighlightPainter();
		Assertions.assertEquals(1f, painter.getAlpha(), 0.00001);
		painter.setAlpha(0.5f);
		Assertions.assertEquals(0.5f, painter.getAlpha(), 0.00001);
	}


	@Test
	void testGetSetAlpha_negative() {
		ChangeableHighlightPainter painter = new ChangeableHighlightPainter();
		Assertions.assertEquals(1f, painter.getAlpha(), 0.00001);
		painter.setAlpha(-1f);
		Assertions.assertEquals(0, painter.getAlpha(), 0.00001);
	}


	@Test
	void testGetSetAlpha_greaterThanOne() {
		ChangeableHighlightPainter painter = new ChangeableHighlightPainter();
		Assertions.assertEquals(1f, painter.getAlpha(), 0.00001);
		painter.setAlpha(2f);
		Assertions.assertEquals(1f, painter.getAlpha(), 0.00001);
	}


	@Test
	void testGetSetPaint() {
		ChangeableHighlightPainter painter = new ChangeableHighlightPainter();
		Assertions.assertNull(painter.getPaint());
		painter.setPaint(Color.RED);
		Assertions.assertEquals(Color.RED, painter.getPaint());
	}


	@Test
	void testGetSetRoundedEdges() {
		ChangeableHighlightPainter painter = new ChangeableHighlightPainter();
		Assertions.assertFalse(painter.getRoundedEdges());
		painter.setRoundedEdges(true);
		Assertions.assertTrue(painter.getRoundedEdges());
	}


	@Test
	void testPaint_highlightOnSingleLine_opaque() {

		// A text snippet not on the first line
		int offs0 = DEFAULT_CODE.indexOf("println");
		int offs1 = offs0 + 7;

		testPaintImpl(offs0, offs1, 1f, null);
	}


	@Test
	void testPaint_highlightOnSingleLine_opaque_withPaint() {

		// A text snippet not on the first line
		int offs0 = DEFAULT_CODE.indexOf("println");
		int offs1 = offs0 + 7;

		testPaintImpl(offs0, offs1, 1f, Color.RED);
	}


	@Test
	void testPaint_highlightOnSingleLine_translucent() {

		// A text snippet not on the first line
		int offs0 = DEFAULT_CODE.indexOf("println");
		int offs1 = offs0 + 7;

		testPaintImpl(offs0, offs1, 0.5f, null);
	}


	@Test
	void testPaint_highlightOnMultipleLines_opaque() {
		testPaintImpl(0, 20, 1f, null);
	}


	@Test
	void testPaint_highlightOnMultipleLines_opaque_withPaint() {
		testPaintImpl(0, 20, 1f, Color.RED);
	}


	@Test
	void testPaint_highlightOnMultipleLines_translucent() {
		testPaintImpl(0, 20, 0.5f, null);
	}


	private static void testPaintImpl(int offs0, int offs1, float alpha, Color paint) {

		RSyntaxTextArea textArea = createTextArea();
		Graphics g = createTestGraphics();

		ChangeableHighlightPainter painter = new ChangeableHighlightPainter();
		painter.setAlpha(alpha);
		painter.setPaint(paint);
		painter.paint(g, offs0, offs1, textArea.getBounds(), textArea);
	}


	@Test
	void testPaintLayer_emptyLayerEgNoSelection_opaque() {
		testPaintLayerImpl(5, 5, 1f, null, true);
	}


	@Test
	void testPaintLayer_highlightOnSingleLine_opaque() {

		// A text snippet not on the first line
		int offs0 = DEFAULT_CODE.indexOf("println");
		int offs1 = offs0 + 7;

		testPaintLayerImpl(offs0, offs1, 1f, null, false);
	}


	@Test
	void testPaintLayer_highlightOnSingleLine_opaque_withPaint() {

		// A text snippet not on the first line
		int offs0 = DEFAULT_CODE.indexOf("println");
		int offs1 = offs0 + 7;

		testPaintLayerImpl(offs0, offs1, 1f, Color.RED, true);
	}


	@Test
	void testPaintLayer_highlightOnSingleLine_translucent() {

		// A text snippet not on the first line
		int offs0 = DEFAULT_CODE.indexOf("println");
		int offs1 = offs0 + 7;

		testPaintLayerImpl(offs0, offs1, 0.5f, null, false);
	}


	@Test
	void testPaintLayer_highlightOnMultipleLines_opaque() {
		// Entire view is a special case
		testPaintLayerImpl(0, DEFAULT_CODE.length() + 1, 1f, null, true);
	}


	@Test
	void testPaintLayer_highlightOnMultipleLines_opaque_withPaint() {
		testPaintLayerImpl(0, 20, 1f, Color.RED, true);
	}


	@Test
	void testPaintLayer_highlightOnMultipleLines_translucent() {
		// Entire view is a special case
		testPaintLayerImpl(0, DEFAULT_CODE.length() + 1, 0.5f, null, false);
	}


	private static void testPaintLayerImpl(int offs0, int offs1, float alpha, Color paint,
										   boolean roundedEdges) {

		RSyntaxTextArea textArea = createTextArea();
		Graphics g = createTestGraphics();

		ChangeableHighlightPainter painter = new ChangeableHighlightPainter();
		painter.setAlpha(alpha);
		painter.setPaint(paint);
		painter.setRoundedEdges(roundedEdges);
		painter.paintLayer(g, offs0, offs1, textArea.getBounds(), textArea,
			textArea.getUI().getRootView(textArea));
	}
}
