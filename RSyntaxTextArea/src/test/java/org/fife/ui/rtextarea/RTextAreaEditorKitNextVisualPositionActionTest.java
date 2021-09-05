/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.NextVisualPositionAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitNextVisualPositionActionTest extends AbstractRTextAreaTest {

	private RTextArea textArea;


	@BeforeEach
	void setUp() {

		textArea = new RTextArea("one two one two\none two");
		textArea.setSize(800, 800);

		// BasicTextUI requires at least one paint cycle for visual positions to be computed
		textArea.paint(createTestGraphics());
	}


	@Test
	void testActionPerformedImpl_noSelection_noSelect_east() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.EAST).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(7, textArea.getCaret().getDot());
	}


	@Test
	void testActionPerformedImpl_noSelection_noSelect_north() {

		textArea.setCaretPosition(16);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.NORTH).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(0, textArea.getCaret().getDot());
	}


	@Test
	void testActionPerformedImpl_noSelection_noSelect_south() {

		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.SOUTH).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(16, textArea.getCaret().getDot());
	}


	@Test
	void testActionPerformedImpl_noSelection_noSelect_west() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.WEST).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(5, textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_noSelection_select_east() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", true, SwingConstants.EAST).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(6, textArea.getSelectionStart());
		Assertions.assertEquals(7, textArea.getSelectionEnd());
		Assertions.assertEquals(7, textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_noSelection_select_west() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", true, SwingConstants.WEST).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(5, textArea.getSelectionStart());
		Assertions.assertEquals(6, textArea.getSelectionEnd());
		Assertions.assertEquals(5, textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_selection_noSelect_east() {

		textArea.setSelectionStart(3);
		textArea.setSelectionEnd(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection removed, caret at end of prior selection
		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.EAST).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(6, textArea.getSelectionStart());
		Assertions.assertEquals(6, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_selection_noSelect_west() {

		textArea.setSelectionStart(3);
		textArea.setSelectionEnd(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection removed, caret at start of prior selection
		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.WEST).
			actionPerformedImpl(e, textArea);
		Assertions.assertEquals(3, textArea.getSelectionStart());
		Assertions.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	void testGetMacroId() {
		Assertions.assertEquals("foo",
			new RTextAreaEditorKit.NextVisualPositionAction("foo", false,
				SwingConstants.EAST).getMacroID());
	}
}
