/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.PreviousOccurrenceAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitPreviousOccurrenceActionTest {

	private RTextArea textArea;


	@BeforeEach
	void setUp() {

		textArea = new RTextArea("one two one two\none two");
		textArea.setCaretPosition(textArea.getText().length());
	}


	@AfterEach
	void tearDown() {
		RTextArea.setSelectedOccurrenceText(null);
	}


	@Test
	void testActionPerformedImpl_noSelection_alsoNoPriorPreviousOccurrence() {

		// No selection and no previous "next occurrence" search

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		int endOffs = textArea.getText().length();
		Assertions.assertEquals(endOffs, textArea.getSelectionStart());
		Assertions.assertEquals(endOffs, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_noSelection_nextOnSameLine() {

		// No selection but "two" was previously searched for this way
		RTextArea.setSelectedOccurrenceText("two");

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(20, textArea.getSelectionStart());
		Assertions.assertEquals(23, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_selection_nextOnSameLine() {

		// Select "two" on the first line
		textArea.setCaretPosition(15);
		textArea.moveCaretPosition(12);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Prior instance of "two" on the first line is selected
		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(4, textArea.getSelectionStart());
		Assertions.assertEquals(7, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_selection_nextOnPreviousLine() {

		// Select "one" on line 2
		textArea.setCaretPosition(16);
		textArea.moveCaretPosition(19);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Select second "one" on line 1
		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(8, textArea.getSelectionStart());
		Assertions.assertEquals(11, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_selection_noNextMatch() {

		// Select first "one"
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection didn't change
		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	void testGetMacroId() {
		Assertions.assertEquals("foo",
			new RTextAreaEditorKit.PreviousOccurrenceAction("foo").getMacroID());
	}
}
