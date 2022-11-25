/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.InsertTabAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitInsertTabActionTest {


	@Test
	void testActionPerformedImpl_notEditable() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("hello world", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("hello world", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setCaretPosition(textArea.getText().indexOf(' '));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("hello\t world", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(DefaultEditorKit.insertTabAction,
			new RTextAreaEditorKit.InsertTabAction().getMacroID());
	}
}
