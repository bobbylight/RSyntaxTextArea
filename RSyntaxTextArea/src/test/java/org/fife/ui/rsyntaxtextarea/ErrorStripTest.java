/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;


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
		strip = new ErrorStrip(textArea);
	}


	@Test
	public void testAddRemoveNotify() {
		strip.addNotify();
		strip.removeNotify();
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
	public void testPaintComponent() {

		Graphics g = createTestGraphics();
		strip.paintComponent(g);
	}
}
