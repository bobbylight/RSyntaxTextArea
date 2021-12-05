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
 * Unit tests for the {@link RTextAreaEditorKit.SetReadOnlyAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitSetReadOnlyActionTest {


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		Assertions.assertTrue(textArea.isEditable());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.SetReadOnlyAction().actionPerformedImpl(e, textArea);

		Assertions.assertFalse(textArea.isEditable());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(DefaultEditorKit.readOnlyAction,
			new RTextAreaEditorKit.SetReadOnlyAction().getMacroID());
	}


	@Test
	void testIsRecordable() {
		RTextAreaEditorKit.SetReadOnlyAction a = new RTextAreaEditorKit.SetReadOnlyAction();
		Assertions.assertFalse(a.isRecordable());
	}
}
