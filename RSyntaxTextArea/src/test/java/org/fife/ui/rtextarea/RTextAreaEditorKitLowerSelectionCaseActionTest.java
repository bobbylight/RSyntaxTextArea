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
 * Unit tests for the {@link RTextAreaEditorKit.LowerSelectionCaseAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitLowerSelectionCaseActionTest {


	@Test
	void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("HELLO WORLD");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(5);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LowerSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("HELLO WORLD", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("HELLO WORLD");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(5);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LowerSelectionCaseAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("HEllo WORLD", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaLowerSelectionCaseAction,
			new RTextAreaEditorKit.LowerSelectionCaseAction().getMacroID());
	}
}
