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
 * Unit tests for the {@link RTextAreaEditorKit.SetWritableAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitSetWritableActionTest {


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.SetWritableAction().actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.isEditable());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(DefaultEditorKit.writableAction,
			new RTextAreaEditorKit.SetWritableAction().getMacroID());
	}


	@Test
	void testIsRecordable() {
		RTextAreaEditorKit.SetWritableAction a = new RTextAreaEditorKit.SetWritableAction();
		Assertions.assertFalse(a.isRecordable());
	}
}
