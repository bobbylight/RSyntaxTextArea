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
 * Unit tests for the {@link RTextAreaEditorKit.UndoAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitUndoActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.UndoAction action = new RTextAreaEditorKit.UndoAction(
			"name", null, "Description", 0, null);
		Assertions.assertEquals("name", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		textArea.append("foo");

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.UndoAction().actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.getText().isEmpty());
		Assertions.assertFalse(textArea.canUndo());
		Assertions.assertTrue(textArea.canRedo());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaUndoAction,
			new RTextAreaEditorKit.UndoAction().getMacroID());
	}
}
