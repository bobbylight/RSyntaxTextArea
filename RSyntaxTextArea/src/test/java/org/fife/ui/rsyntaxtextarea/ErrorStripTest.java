/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.fife.ui.rtextarea.SmartHighlightPainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * Unit tests for the {@link ErrorStrip} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class ErrorStripTest extends AbstractRSyntaxTextAreaTest {

	private ErrorStrip strip;


	@BeforeEach
	void setUp() {

		RSyntaxTextArea textArea = createTextArea();

		// Hack to highlight some "marked occurrence" tokens for coverage
		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
		Token t = doc.iterator().next(); // Get open curly brace
		RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)textArea.getHighlighter();
		SmartHighlightPainter p = new SmartHighlightPainter();
		new DefaultOccurrenceMarker().markOccurrences(doc, t, h, p);

		// Force reparsing by instance, not by index, to avoid the
		// code folding parser
		TestParser testParser = new TestParser();
		textArea.addParser(testParser);
		textArea.forceReparsing(testParser);

		strip = new ErrorStrip(textArea);
	}


	@Test
	void testAddRemoveNotify() {
		strip.addNotify();
		strip.removeNotify();
	}


	@Test
	void testDoLayout() {
		strip.doLayout();
	}

	@Test
	void testGetSetCaretMarkerColor_darkForeground() {
		Assertions.assertEquals(Color.BLACK, strip.getCaretMarkerColor());
		strip.setCaretMarkerColor(Color.RED);
		Assertions.assertEquals(Color.RED, strip.getCaretMarkerColor());
	}

	@Test
	void testGetSetCaretMarkerColor_nullDoesNothing() {
		Assertions.assertEquals(Color.BLACK, strip.getCaretMarkerColor());
		strip.setCaretMarkerColor(null);
		Assertions.assertEquals(Color.BLACK, strip.getCaretMarkerColor());
	}

	@Test
	@Disabled("Can't set up LookAndFeel where this will have light FG early enough")
	void testGetSetCaretMarkerColor_lightForeground() {
		strip.setForeground(Color.WHITE);
		Assertions.assertEquals(createTextArea().getCaretColor(), strip.getCaretMarkerColor());
		strip.setCaretMarkerColor(Color.RED);
		Assertions.assertEquals(Color.RED, strip.getCaretMarkerColor());
	}

	@Test
	void testGetSetCaretMarkerOnTop() {
		Assertions.assertFalse(strip.isCaretMarkerOnTop());
		strip.setCaretMarkerOnTop(true);
		Assertions.assertTrue(strip.isCaretMarkerOnTop());
	}


	@Test
	void testGetSetFollowCaret() {
		Assertions.assertTrue(strip.getFollowCaret());
		strip.setFollowCaret(false);
		Assertions.assertFalse(strip.getFollowCaret());
		// Repeating the same value does nothing
		strip.setFollowCaret(false);
		Assertions.assertFalse(strip.getFollowCaret());
	}


	@Test
	void testGetSetLevelThreshold() {
		Assertions.assertEquals(ParserNotice.Level.WARNING, strip.getLevelThreshold());
		strip.setLevelThreshold(ParserNotice.Level.INFO);
		Assertions.assertEquals(ParserNotice.Level.INFO, strip.getLevelThreshold());
	}


	@Test
	void testGetSetShowMarkAll() {
		Assertions.assertTrue(strip.getShowMarkAll());
		strip.setShowMarkAll(false);
		Assertions.assertFalse(strip.getShowMarkAll());
		// Calling again with the same value does nothing
		strip.setShowMarkAll(false);
		Assertions.assertFalse(strip.getShowMarkAll());
	}


	@Test
	void testGetSetShowMarkedOccurrences() {
		Assertions.assertTrue(strip.getShowMarkedOccurrences());
		strip.setShowMarkedOccurrences(false);
		Assertions.assertFalse(strip.getShowMarkedOccurrences());
		// Calling again with the same values does nothing
		strip.setShowMarkedOccurrences(false);
		Assertions.assertFalse(strip.getShowMarkedOccurrences());
	}


	@Test
	void testGetPreferredSize() {
		Assertions.assertNotNull(strip.getPreferredSize());
	}


	@Test
	void testGetToolTipText_happyPath() {
		MouseEvent e = new MouseEvent(strip, 0, 0, 0, 1, 1, 1, false);
		Assertions.assertEquals("Line: 1", strip.getToolTipText(e));
	}


	@Test
	void testGetToolTipText_invalidMouseEvent() {
		MouseEvent e = new MouseEvent(strip, 0, 0, 0, -1, Integer.MAX_VALUE, 1,
			false);
		Assertions.assertNull(strip.getToolTipText(e));
	}


	@Test
	void testPaint() {
		strip.addNotify();
		strip.setSize(strip.getPreferredSize());
		strip.doLayout();
		Graphics g = createTestGraphics();
		strip.paint(g);
	}


	@Test
	void testPaint_paintCaretMarkerOnTop() {
		strip.setCaretMarkerOnTop(true);
		strip.addNotify();
		strip.setSize(strip.getPreferredSize());
		strip.doLayout();
		Graphics g = createTestGraphics();
		strip.paint(g);
	}


	@Test
	void testSetMarkerToolTipProvider_null() {
		strip.setMarkerToolTipProvider(null); // No exception thrown
	}


	@Test
	void testSetMarkerToolTipProvider_notNull() {
		ErrorStrip.ErrorStripMarkerToolTipProvider provider = notices -> "test";
		strip.setMarkerToolTipProvider(provider);
	}


	/**
	 * A dummy parser that returns a single parser notice for test purposes.
	 */
	private static final class TestParser extends AbstractParser {

		@Override
		public ParseResult parse(RSyntaxDocument doc, String style) {

			DefaultParseResult result = new DefaultParseResult(this);

			// Note some notices on the same line
			result.addNotice(new DefaultParserNotice(this, "test notice", 1));
			result.addNotice(new DefaultParserNotice(this, "second notice", 1));
			DefaultParserNotice thirdNotice =
				new DefaultParserNotice(this, "third notice", 2);
			thirdNotice.setColor(null);
			result.addNotice(thirdNotice);

			return result;
		}
	}
}
