/*
 * 03/04/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.util.List;
import javax.swing.text.BadLocationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for the {@link LineHighlightManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class LineHighlightManagerTest {

	private RTextArea textArea;
	private LineHighlightManager lhm;

	@Before
	public void setUp() {
		textArea = new RTextArea("0123456789\n0123456789\n0123456789");
		lhm = new LineHighlightManager(textArea);
	}


	@Test
	public void testAddLineHighlight_happyPath() throws BadLocationException {

		Assert.assertEquals(0, lhm.getLineHighlightCount());

		lhm.addLineHighlight(1, Color.BLUE);
		Assert.assertEquals(1, lhm.getLineHighlightCount());

		lhm.addLineHighlight(2, Color.BLUE);
		Assert.assertEquals(2, lhm.getLineHighlightCount());
	}


	@Test
	public void testAddLineHighlight_twoOnSameLine()
			throws BadLocationException {

		Assert.assertEquals(0, lhm.getLineHighlightCount());

		lhm.addLineHighlight(1, Color.BLUE);
		Assert.assertEquals(1, lhm.getLineHighlightCount());

		lhm.addLineHighlight(1, Color.RED);
		Assert.assertEquals(2, lhm.getLineHighlightCount());
	}


	@Test
	public void testAddLineHighlight_removeOneOfTwoOnOneLine()
			throws BadLocationException {

		Object tag1 = lhm.addLineHighlight(1, Color.BLUE);
		Object tag2 = lhm.addLineHighlight(1, Color.RED);
		Assert.assertEquals(2, lhm.getLineHighlightCount());

		// We're testing a bug here with LineHighlightInfo's equals() method,
		// so we use a foolproof method of telling the two tags apart.
		lhm.removeLineHighlight(tag1);
		List<Object> remainingTags = lhm.getCurrentLineHighlightTags();
		Assert.assertEquals(1, remainingTags.size());
		Assert.assertTrue(tag2 == remainingTags.get(0));
	}


	@Test
	public void testGetCurrentLineHighlightTags() throws BadLocationException {

		Assert.assertEquals(0, lhm.getCurrentLineHighlightTags().size());

		Object tag1 = lhm.addLineHighlight(0, Color.RED);
		Object tag2 = lhm.addLineHighlight(2, Color.BLUE);
		List<Object> actualTags = lhm.getCurrentLineHighlightTags();
		Assert.assertEquals(2, actualTags.size());
		Assert.assertEquals(tag1, actualTags.get(0));
		Assert.assertEquals(tag2, actualTags.get(1));
	}


	@Test
	public void testRemoveAllHighlights() throws BadLocationException {
		lhm.addLineHighlight(1, Color.BLUE);
		lhm.addLineHighlight(1, Color.RED);
		Assert.assertEquals(2, lhm.getLineHighlightCount());
		lhm.removeAllLineHighlights();
		Assert.assertEquals(0, lhm.getLineHighlightCount());
	}
}