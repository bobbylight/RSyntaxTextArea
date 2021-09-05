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
 * Unit tests for the {@link RTextAreaEditorKit.JoinLinesAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitJoinLinesActionTest {


	@Test
	void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.JoinLinesAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1\nline 2", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.JoinLinesAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1line 2", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_doesNothingWhenOnLastLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.JoinLinesAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("line 1\nline 2", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaJoinLinesAction,
			new RTextAreaEditorKit.JoinLinesAction().getMacroID());
	}
}
