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
 * Unit tests for the {@link RTextAreaEditorKit.RedoAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitRedoActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.RedoAction action = new RTextAreaEditorKit.RedoAction(
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
		new RTextAreaEditorKit.RedoAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("foo", textArea.getText());
		Assertions.assertTrue(textArea.canUndo());
		Assertions.assertFalse(textArea.canRedo());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaRedoAction,
			new RTextAreaEditorKit.RedoAction().getMacroID());
	}
}
