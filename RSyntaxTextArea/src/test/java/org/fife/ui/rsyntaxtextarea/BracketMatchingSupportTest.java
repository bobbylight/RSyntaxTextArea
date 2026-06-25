/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.JFrame;
import javax.swing.JViewport;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;


/**
 * Unit tests for the {@link BracketMatchingSupport} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class BracketMatchingSupportTest extends AbstractRSyntaxTextAreaTest {

	/** Offset just after the outer, opening curly brace in {@link #DEFAULT_CODE}. */
	private static final int CARET_AFTER_OUTER_OPEN_BRACE = 1;

	/** Offset just after the inner, opening curly brace in {@link #DEFAULT_CODE}. */
	private static final int CARET_AFTER_INNER_OPEN_BRACE = 5;

	/** Offset with no bracket immediately to either side, in {@link #DEFAULT_CODE}. */
	private static final int CARET_WITH_NO_ADJACENT_BRACKET = 12;


	@Test
	void testConstructor_defaultState() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);

		Assertions.assertFalse(support.isEnabled());
		Assertions.assertFalse(support.getAnimateBracketMatching());
		Assertions.assertFalse(support.getPaintMatchedBracketPair());
		Assertions.assertFalse(support.getShowMatchedBracketPopup());
		Assertions.assertNull(support.getMatchRectangle());
		Assertions.assertNull(support.getDotRectangle());
		Assertions.assertNull(support.getMatchedBracketBGColor());
		Assertions.assertNull(support.getMatchedBracketBorderColor());
	}


	@Test
	void testSetEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		Object[] newValue = new Object[1];
		textArea.addPropertyChangeListener(RSyntaxTextArea.BRACKET_MATCHING_PROPERTY,
			e -> newValue[0] = e.getNewValue());

		support.setEnabled(true);
		Assertions.assertTrue(support.isEnabled());
		Assertions.assertEquals(Boolean.TRUE, newValue[0]);

		support.setEnabled(false);
		Assertions.assertFalse(support.isEnabled());
		Assertions.assertEquals(Boolean.FALSE, newValue[0]);
	}


	@Test
	void testSetEnabled_calledWithExistingValue() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		Object[] newValue = new Object[1];
		textArea.addPropertyChangeListener(RSyntaxTextArea.BRACKET_MATCHING_PROPERTY,
			e -> newValue[0] = e.getNewValue());

		// Default is already false, so this is a no-op
		support.setEnabled(false);
		Assertions.assertFalse(support.isEnabled());
		Assertions.assertNull(newValue[0]);
	}


	@Test
	void testSetAnimateBracketMatching() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		Object[] newValue = new Object[1];
		textArea.addPropertyChangeListener(RSyntaxTextArea.ANIMATE_BRACKET_MATCHING_PROPERTY,
			e -> newValue[0] = e.getNewValue());

		support.setAnimateBracketMatching(true);
		Assertions.assertTrue(support.getAnimateBracketMatching());
		Assertions.assertEquals(Boolean.TRUE, newValue[0]);

		support.setAnimateBracketMatching(false);
		Assertions.assertFalse(support.getAnimateBracketMatching());
		Assertions.assertEquals(Boolean.FALSE, newValue[0]);
	}


	@Test
	void testSetAnimateBracketMatching_calledWithExistingValue() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		Object[] newValue = new Object[1];
		textArea.addPropertyChangeListener(RSyntaxTextArea.ANIMATE_BRACKET_MATCHING_PROPERTY,
			e -> newValue[0] = e.getNewValue());

		// Default is already false, so this is a no-op
		support.setAnimateBracketMatching(false);
		Assertions.assertFalse(support.getAnimateBracketMatching());
		Assertions.assertNull(newValue[0]);
	}


	@Test
	void testGetSetMatchedBracketBGColor() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);

		support.setMatchedBracketBGColor(Color.pink);
		Assertions.assertEquals(Color.pink, support.getMatchedBracketBGColor());

		support.setMatchedBracketBGColor(null);
		Assertions.assertNull(support.getMatchedBracketBGColor());
	}


	@Test
	void testGetSetMatchedBracketBorderColor() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);

		support.setMatchedBracketBorderColor(Color.pink);
		Assertions.assertEquals(Color.pink, support.getMatchedBracketBorderColor());

		support.setMatchedBracketBorderColor(null);
		Assertions.assertNull(support.getMatchedBracketBorderColor());
	}


	@Test
	void testGetSetShowMatchedBracketPopup() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);

		Assertions.assertFalse(support.getShowMatchedBracketPopup());
		support.setShowMatchedBracketPopup(true);
		Assertions.assertTrue(support.getShowMatchedBracketPopup());
		support.setShowMatchedBracketPopup(false);
		Assertions.assertFalse(support.getShowMatchedBracketPopup());
	}


	@Test
	void testSetPaintMatchedBracketPair() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);
		Object[] newValue = new Object[1];
		textArea.addPropertyChangeListener(RSyntaxTextArea.PAINT_MATCHED_BRACKET_PAIR_PROPERTY,
			e -> newValue[0] = e.getNewValue());

		support.setPaintMatchedBracketPair(true);
		Assertions.assertTrue(support.getPaintMatchedBracketPair());
		Assertions.assertEquals(Boolean.TRUE, newValue[0]);

		support.setPaintMatchedBracketPair(false);
		Assertions.assertFalse(support.getPaintMatchedBracketPair());
		Assertions.assertEquals(Boolean.FALSE, newValue[0]);
	}


	@Test
	void testSetPaintMatchedBracketPair_calledWithExistingValue() {

		RSyntaxTextArea textArea = createTextArea();
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		Object[] newValue = new Object[1];
		textArea.addPropertyChangeListener(RSyntaxTextArea.PAINT_MATCHED_BRACKET_PAIR_PROPERTY,
			e -> newValue[0] = e.getNewValue());

		// Default is already false, so this is a no-op
		support.setPaintMatchedBracketPair(false);
		Assertions.assertFalse(support.getPaintMatchedBracketPair());
		Assertions.assertNull(newValue[0]);
	}


	@Test
	void testDoBracketMatching_singleBracketHighlighted() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);

		support.doBracketMatching();

		Assertions.assertNotNull(support.getMatchRectangle());
		Assertions.assertNull(support.getDotRectangle());
	}


	@Test
	void testDoBracketMatching_bothBracketsHighlighted() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);
		support.setPaintMatchedBracketPair(true);

		support.doBracketMatching();

		Assertions.assertNotNull(support.getMatchRectangle());
		Assertions.assertNotNull(support.getDotRectangle());
	}


	@Test
	void testDoBracketMatching_matchesInnermostBracketPair() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_INNER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);

		support.doBracketMatching();

		Assertions.assertNotNull(support.getMatchRectangle());
	}


	@Test
	void testDoBracketMatching_noBracketAtCaretClearsPreviousMatch() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);
		support.setPaintMatchedBracketPair(true);
		support.doBracketMatching();
		Assertions.assertNotNull(support.getMatchRectangle());
		Assertions.assertNotNull(support.getDotRectangle());

		textArea.setCaretPosition(CARET_WITH_NO_ADJACENT_BRACKET);
		support.doBracketMatching();

		Assertions.assertNull(support.getMatchRectangle());
		Assertions.assertNull(support.getDotRectangle());
	}


	@Test
	void testDoBracketMatching_noBracketAtCaretFromTheStart() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_WITH_NO_ADJACENT_BRACKET);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);

		support.doBracketMatching();

		Assertions.assertNull(support.getMatchRectangle());
		Assertions.assertNull(support.getDotRectangle());
	}


	@Test
	void testDoBracketMatching_repeatedCallsWithoutMovingCaretAreStable() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);

		support.doBracketMatching();
		Rectangle firstMatch = support.getMatchRectangle();
		Assertions.assertNotNull(firstMatch);

		support.doBracketMatching();
		Assertions.assertEquals(firstMatch, support.getMatchRectangle());
	}


	@Test
	void testHideMatch() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);
		support.setPaintMatchedBracketPair(true);
		support.doBracketMatching();
		Assertions.assertNotNull(support.getMatchRectangle());
		Assertions.assertNotNull(support.getDotRectangle());

		support.hideMatch();

		Assertions.assertNull(support.getMatchRectangle());
		Assertions.assertNull(support.getDotRectangle());
	}


	@Test
	void testHideMatch_doBracketMatchingWithoutMovingCaretStaysHidden() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);
		support.doBracketMatching();
		Assertions.assertNotNull(support.getMatchRectangle());

		support.hideMatch();
		Assertions.assertNull(support.getMatchRectangle());

		// Since the caret hasn't moved, the cached "last matched" state still
		// matches the recomputed bracket position, so the match isn't redrawn.
		support.doBracketMatching();
		Assertions.assertNull(support.getMatchRectangle());
	}


	@Test
	void testForceReMatch_recomputesMatchEvenWhenCaretHasNotMoved() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);
		support.doBracketMatching();
		Assertions.assertNotNull(support.getMatchRectangle());

		support.hideMatch();
		Assertions.assertNull(support.getMatchRectangle());

		support.forceReMatch();

		Assertions.assertNotNull(support.getMatchRectangle());
	}


	@Test
	void testDoBracketMatching_showMatchedBracketPopupWhenMatchScrolledOffscreen() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		JFrame frame = new JFrame();
		try {
			RSyntaxTextArea textArea = createTextArea();
			JViewport viewport = new JViewport();
			viewport.setBounds(0, 0, 80, 20);
			viewport.setView(textArea);
			frame.add(viewport);
			frame.setSize(100, 100);
			frame.setVisible(true);

			// Scroll down so the match (on line 0) is above the visible area.
			viewport.setViewPosition(new java.awt.Point(0, 60));

			textArea.setCaretPosition(CARET_AFTER_OUTER_OPEN_BRACE);
			BracketMatchingSupport support = new BracketMatchingSupport(textArea);
			support.setAnimateBracketMatching(true);
			support.setShowMatchedBracketPopup(true);

			Assertions.assertDoesNotThrow(support::doBracketMatching);
		}
		finally {
			frame.dispose();
		}
	}


	@Test
	void testForceReMatch_doesNotThrowWhenNeverMatched() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(CARET_WITH_NO_ADJACENT_BRACKET);
		BracketMatchingSupport support = new BracketMatchingSupport(textArea);
		support.setAnimateBracketMatching(true);

		Assertions.assertDoesNotThrow(support::forceReMatch);
		Assertions.assertNull(support.getMatchRectangle());
	}

}
