/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


import org.fife.ui.rsyntaxtextarea.DocumentRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.BadLocationException;
import javax.swing.text.View;
import java.awt.*;
import java.util.List;

/**
 * Unit tests for the {@code RTextAreaHighlighter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaHighlighterTest extends AbstractRTextAreaTest {

	private RTextArea textArea;
	private RTextAreaHighlighter h;

	private static final String CONTENT = "This is the test content";


	@BeforeEach
	void setUp() {
		h = new RTextAreaHighlighter();
		textArea = new RTextArea(CONTENT);
		textArea.setSize(80, 80);
	}


	@Test
	void testAddMarkAllHighlight() throws BadLocationException {
		textArea.setHighlighter(h);
		Assertions.assertEquals(0, h.getMarkAllHighlightCount());
		h.addMarkAllHighlight(1, 3, new ChangeableHighlightPainter());
		Assertions.assertEquals(1, h.getMarkAllHighlightCount());
	}


	@Test
	void testClearMarkAllHighlights() throws BadLocationException {
		textArea.setHighlighter(h);
		h.addMarkAllHighlight(1, 3, new ChangeableHighlightPainter());
		Assertions.assertEquals(1, h.getMarkAllHighlightCount());
		h.clearMarkAllHighlights();
		Assertions.assertEquals(0, h.getMarkAllHighlightCount());
	}


	@Test
	void testInstallDeinstall() {
		RTextArea textArea = new RTextArea();
		h.install(textArea);
		h.deinstall(textArea);
	}


	@Test
	void testGetMarkAllHighlightCount() throws BadLocationException {
		textArea.setHighlighter(h);
		Assertions.assertEquals(0, h.getMarkAllHighlightCount());
		h.addMarkAllHighlight(1, 3, new ChangeableHighlightPainter());
		Assertions.assertEquals(1, h.getMarkAllHighlightCount());
	}


	@Test
	void testGetMarkAllHighlightRanges() throws BadLocationException {

		textArea.setHighlighter(h);
		Assertions.assertEquals(0, h.getMarkAllHighlightRanges().size());

		h.addMarkAllHighlight(1, 3, new ChangeableHighlightPainter());
		List<DocumentRange> ranges = h.getMarkAllHighlightRanges();
		Assertions.assertEquals(1, ranges.size());
		DocumentRange range = ranges.get(0);
		Assertions.assertEquals(new DocumentRange(1, 3), range);
	}


	@Test
	void testPaintLayeredHighlights() throws BadLocationException {
		textArea.setHighlighter(h);
		h.addMarkAllHighlight(1, 3, new ChangeableHighlightPainter());

		Rectangle viewBounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea);
		h.paintLayeredHighlights(createTestGraphics(), 0, 2, viewBounds,
			textArea, view);
	}
}
