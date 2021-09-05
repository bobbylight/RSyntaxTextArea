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
 * Unit tests for the {@link RTextAreaEditorKit.NextOccurrenceAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitNextOccurrenceActionTest {

	private RTextArea textArea;


	@BeforeEach
	void setUp() {

		textArea = new RTextArea("one two one two\none two");
		textArea.setCaretPosition(0);
	}


	@AfterEach
	void tearDown() {
		RTextArea.setSelectedOccurrenceText(null);
	}


	@Test
	void testActionPerformedImpl_noSelection_alsoNoPriorNextOccurrence() {

		// No selection and no previous "next occurrence" search

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_noSelection_nextOnSameLine() {

		// No selection but "two" was previously searched for this way
		RTextArea.setSelectedOccurrenceText("two");

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(4, textArea.getSelectionStart());
		Assertions.assertEquals(7, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_selection_nextOnSameLine() {

		// Select "one"
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(8, textArea.getSelectionStart());
		Assertions.assertEquals(11, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_selection_nextOnNextLine() {

		// Select second "one"
		textArea.setSelectionStart(8);
		textArea.setSelectionEnd(11);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(16, textArea.getSelectionStart());
		Assertions.assertEquals(19, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_selection_noNextMatch() {

		// Select last "one"
		textArea.setSelectionStart(16);
		textArea.setSelectionEnd(19);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection didn't change
		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assertions.assertEquals(16, textArea.getSelectionStart());
		Assertions.assertEquals(19, textArea.getSelectionEnd());
	}


	@Test
	void testGetMacroId() {
		Assertions.assertEquals("foo",
			new RTextAreaEditorKit.NextOccurrenceAction("foo").getMacroID());
	}
}
