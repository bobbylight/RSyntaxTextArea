/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.PreviousWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitPreviousWordActionTest {


	@Test
	void testActionPerformedImpl_noSelection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(5);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(0, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_noSelection_previousLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(7);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		// If the next word starts on the next line, just select to the end of the current line
		Assertions.assertEquals(textArea.getText().indexOf('\n'), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_selection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(5);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PreviousWordAction("foo", true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(0, textArea.getCaretPosition());
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(5, textArea.getSelectionEnd());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("foo",
			new RTextAreaEditorKit.PreviousWordAction("foo", true).getMacroID());
	}
}
