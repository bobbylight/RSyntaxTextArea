/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.DeletePrevCharAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitDeletePrevCharActionTest {


	@Test
	void testActionPerformedImpl_notEditable() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeletePrevCharAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1\nline 2\nline 3", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_noSelection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeletePrevCharAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1\nline2\nline 3", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_selection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf("ine 2"));
		textArea.moveCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeletePrevCharAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1\nl2\nline 3", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.deletePrevCharAction,
			new RTextAreaEditorKit.DeletePrevCharAction().getMacroID());
	}
}
