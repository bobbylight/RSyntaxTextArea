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
 * Unit tests for the {@link RTextAreaEditorKit.TimeDateAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitTimeDateActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.TimeDateAction action = new RTextAreaEditorKit.TimeDateAction(
			"name", null, "Description", 0, null);
		Assertions.assertEquals("name", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.TimeDateAction().actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.getText().length() > 0);
	}


	@Test
	void testActionPerformedImpl_notEditable() {

		RTextArea textArea = new RTextArea();
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.TimeDateAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(0, textArea.getText().length());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaTimeDateAction,
			new RTextAreaEditorKit.TimeDateAction().getMacroID());
	}
}
