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
 * Unit tests for the {@link RTextAreaEditorKit.ToggleTextModeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitToggleTextModeActionTest {


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		Assertions.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());

		// Toggle to overwrite
		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.ToggleTextModeAction().actionPerformedImpl(e, textArea);
		Assertions.assertEquals(RTextArea.OVERWRITE_MODE, textArea.getTextMode());

		// And back to insert
		new RTextAreaEditorKit.ToggleTextModeAction().actionPerformedImpl(e, textArea);
		Assertions.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaToggleTextModeAction,
			new RTextAreaEditorKit.ToggleTextModeAction().getMacroID());
	}
}
