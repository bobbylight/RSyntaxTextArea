/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;


/**
 * Unit tests for the {@code SquiggleUnderlineHighlightPainter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class SquiggleUnderlineHighlightPainterTest extends AbstractRSyntaxTextAreaTest {


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

		SquiggleUnderlineHighlightPainter painter = new SquiggleUnderlineHighlightPainter(Color.RED);
		painter.setAlpha(alpha);
		painter.setPaint(paint);
		painter.setRoundedEdges(roundedEdges);
		painter.paintLayer(g, offs0, offs1, textArea.getBounds(), textArea,
			textArea.getUI().getRootView(textArea));
	}

}
