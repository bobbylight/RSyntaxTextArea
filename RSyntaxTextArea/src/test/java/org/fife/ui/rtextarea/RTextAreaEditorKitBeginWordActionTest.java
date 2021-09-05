/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitBeginWordActionTest {


	@Test
	void testActionPerformedImpl_noSelect() {

		String text = "word word word";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(12);

		RTextAreaEditorKit.BeginWordAction action = new RTextAreaEditorKit.BeginWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(10, textArea.getSelectionStart());
		Assertions.assertEquals(10, textArea.getSelectionEnd());

	}


	@Test
	void testActionPerformedImpl_select() {

		String text = "word word word";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(12);

		RTextAreaEditorKit.BeginWordAction action = new RTextAreaEditorKit.BeginWordAction("name", true);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(10, textArea.getSelectionStart());
		Assertions.assertEquals(12, textArea.getSelectionEnd());

	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("begin", new RTextAreaEditorKit.BeginWordAction("begin", false).getMacroID());
	}
}
