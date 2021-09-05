/*
 * 03/04/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.util.List;
import javax.swing.text.BadLocationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link LineHighlightManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LineHighlightManagerTest {

	private LineHighlightManager lhm;

	@BeforeEach
	void setUp() {
		RTextArea textArea = new RTextArea("0123456789\n0123456789\n0123456789");
		lhm = new LineHighlightManager(textArea);
	}


	@Test
	void testAddLineHighlight_happyPath() throws BadLocationException {

		Assertions.assertEquals(0, lhm.getLineHighlightCount());

		lhm.addLineHighlight(1, Color.BLUE);
		Assertions.assertEquals(1, lhm.getLineHighlightCount());

		lhm.addLineHighlight(2, Color.BLUE);
		Assertions.assertEquals(2, lhm.getLineHighlightCount());
	}


	@Test
	void testAddLineHighlight_twoOnSameLine()
			throws BadLocationException {

		Assertions.assertEquals(0, lhm.getLineHighlightCount());

		lhm.addLineHighlight(1, Color.BLUE);
		Assertions.assertEquals(1, lhm.getLineHighlightCount());

		lhm.addLineHighlight(1, Color.RED);
		Assertions.assertEquals(2, lhm.getLineHighlightCount());
	}


	@Test
	void testAddLineHighlight_removeOneOfTwoOnOneLine_differentColors()
			throws BadLocationException {

		Object tag1 = lhm.addLineHighlight(1, Color.BLUE);
		Object tag2 = lhm.addLineHighlight(1, Color.RED);
		Assertions.assertEquals(2, lhm.getLineHighlightCount());

		// We're testing a bug here with LineHighlightInfo's equals() method,
		// so we use a foolproof method of telling the two tags apart.
		lhm.removeLineHighlight(tag1);
		List<Object> remainingTags = lhm.getCurrentLineHighlightTags();
		Assertions.assertEquals(1, remainingTags.size());
		Assertions.assertSame(tag2, remainingTags.get(0));
	}


	@Test
	void testAddLineHighlight_removeOneOfTwoOnOneLine_sameColors()
		throws BadLocationException {

		Object tag1 = lhm.addLineHighlight(1, Color.BLUE);
		Object tag2 = lhm.addLineHighlight(2, Color.BLUE);
		Assertions.assertEquals(2, lhm.getLineHighlightCount());

		// We're testing a bug here with LineHighlightInfo's equals() method,
		// so we use a foolproof method of telling the two tags apart.
		lhm.removeLineHighlight(tag2);
		List<Object> remainingTags = lhm.getCurrentLineHighlightTags();
		Assertions.assertEquals(1, remainingTags.size());
		Assertions.assertSame(tag1, remainingTags.get(0));
	}


	@Test
	void testGetCurrentLineHighlightTags() throws BadLocationException {

		Assertions.assertEquals(0, lhm.getCurrentLineHighlightTags().size());

		Object tag1 = lhm.addLineHighlight(0, Color.RED);
		Object tag2 = lhm.addLineHighlight(2, Color.BLUE);
		List<Object> actualTags = lhm.getCurrentLineHighlightTags();
		Assertions.assertEquals(2, actualTags.size());
		Assertions.assertEquals(tag1, actualTags.get(0));
		Assertions.assertEquals(tag2, actualTags.get(1));
	}


	@Test
	void testRemoveAllHighlights() throws BadLocationException {
		lhm.addLineHighlight(1, Color.BLUE);
		lhm.addLineHighlight(1, Color.RED);
		Assertions.assertEquals(2, lhm.getLineHighlightCount());
		lhm.removeAllLineHighlights();
		Assertions.assertEquals(0, lhm.getLineHighlightCount());
	}
}
