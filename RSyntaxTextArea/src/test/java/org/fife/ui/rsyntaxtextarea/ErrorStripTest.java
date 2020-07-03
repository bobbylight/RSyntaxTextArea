/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * Unit tests for the {@link ErrorStrip} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class ErrorStripTest extends AbstractRSyntaxTextAreaTest {

	private RSyntaxTextArea textArea;
	private ErrorStrip strip;


	@Before
	public void setUp() {

		textArea = createTextArea();

		// Force reparsing by instance, not by index, to avoid the
		// code folding parser
		TestParser testParser = new TestParser();
		textArea.addParser(testParser);
		textArea.forceReparsing(testParser);

		strip = new ErrorStrip(textArea);
	}


	@Test
	public void testAddRemoveNotify() {
		strip.addNotify();
		strip.removeNotify();
	}


	@Test
	public void testDoLayout() {
		strip.doLayout();
	}

	@Test
	public void testGetSetCaretMarkerColor() {
		Assert.assertEquals(Color.BLACK, strip.getCaretMarkerColor());
		strip.setCaretMarkerColor(Color.RED);
		Assert.assertEquals(Color.RED, strip.getCaretMarkerColor());
	}


	@Test
	public void testGetSetFollowCaret() {
		Assert.assertTrue(strip.getFollowCaret());
		strip.setFollowCaret(false);
		Assert.assertFalse(strip.getFollowCaret());
	}


	@Test
	public void testGetSetLevelThreshold() {
		Assert.assertEquals(ParserNotice.Level.WARNING, strip.getLevelThreshold());
		strip.setLevelThreshold(ParserNotice.Level.INFO);
		Assert.assertEquals(ParserNotice.Level.INFO, strip.getLevelThreshold());
	}


	@Test
	public void testGetSetShowMarkAll() {
		Assert.assertTrue(strip.getShowMarkAll());
		strip.setShowMarkAll(false);
		Assert.assertFalse(strip.getShowMarkAll());
	}


	@Test
	public void testGetSetShowMarkedOccurrences() {
		Assert.assertTrue(strip.getShowMarkedOccurrences());
		strip.setShowMarkedOccurrences(false);
		Assert.assertFalse(strip.getShowMarkedOccurrences());
	}


	@Test
	public void testGetPreferredSize() {
		Assert.assertNotNull(strip.getPreferredSize());
	}


	@Test
	public void testGetToolTipText() {
		MouseEvent e = new MouseEvent(strip, 0, 0, 0, 1, 1, 1, false);
		Assert.assertEquals("Line: 1", strip.getToolTipText(e));
	}


	@Test
	public void testPaintComponent() {

		Graphics g = createTestGraphics();
		strip.paintComponent(g);
	}


	@Test
	public void testSetMarkerToolTipProvider_null() {
		strip.setMarkerToolTipProvider(null); // No exception thrown
	}


	@Test
	public void testSetMarkerToolTipProvider_notNull() {
		ErrorStrip.ErrorStripMarkerToolTipProvider provider = notices -> "test";
		strip.setMarkerToolTipProvider(provider);
	}


	/**
	 * A dummy parser that returns a single parser notice for test purposes.
	 */
	private static class TestParser extends AbstractParser {

		@Override
		public ParseResult parse(RSyntaxDocument doc, String style) {

			DefaultParseResult result = new DefaultParseResult(this);

			result.addNotice(new DefaultParserNotice(this, "test notice", 1));
			return result;
		}
	}
}
