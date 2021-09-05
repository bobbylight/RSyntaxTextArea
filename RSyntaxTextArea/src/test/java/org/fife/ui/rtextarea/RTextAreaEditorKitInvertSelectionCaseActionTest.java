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
 * Unit tests for the {@link RTextAreaEditorKit.InvertSelectionCaseAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitInvertSelectionCaseActionTest {


	@Test
	void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(5);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.InvertSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("hello world", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("helLo world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(5);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.InvertSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("heLlO world", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaInvertSelectionCaseAction,
			new RTextAreaEditorKit.InvertSelectionCaseAction().getMacroID());
	}
}
