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
 * Unit tests for the {@link RTextAreaEditorKit.EndAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitEndActionTest {


	@Test
	void testActionPerformedImpl_selectFalse() {

		String text = "line 1\nline 2\nline 3";
		RTextArea textArea = new RTextArea(text);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.EndAction("foo", false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(text.length(), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_selectTrue() {

		String text = "line 1\nline 2\nline 3";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.EndAction("foo", true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(text.length(), textArea.getCaretPosition());
		Assertions.assertNotNull(textArea.getSelectedText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("foo",
			new RTextAreaEditorKit.EndAction("foo", false).getMacroID());
	}
}
