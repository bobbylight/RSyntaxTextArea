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
 * Unit tests for the {@link RTextAreaEditorKit.DeleteLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitDeleteLineActionTest {


	@Test
	void testActionPerformedImpl_notEditable() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeleteLineAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1\nline 2\nline 3", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_noSelection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeleteLineAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1\nline 3", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_selectionWithNoCharsSelectedOnLastLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));
		textArea.moveCaretPosition(textArea.getText().indexOf('\n') + 1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeleteLineAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 2\nline 3", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaDeleteLineAction,
			new RTextAreaEditorKit.DeleteLineAction().getMacroID());
	}
}
