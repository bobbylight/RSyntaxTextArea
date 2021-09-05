/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.EndWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitEndWordActionTest {


	@Test
	void testActionPerformedImpl_noSelect() {

		String text = "word word word";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(6);

		RTextAreaEditorKit.EndWordAction action = new RTextAreaEditorKit.EndWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(9, textArea.getSelectionStart());
		Assertions.assertEquals(9, textArea.getSelectionEnd());

	}


	@Test
	void testActionPerformedImpl_select() {

		String text = "word word word";
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(6);

		RTextAreaEditorKit.EndWordAction action = new RTextAreaEditorKit.EndWordAction("name", true);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(6, textArea.getSelectionStart());
		Assertions.assertEquals(9, textArea.getSelectionEnd());

	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("end", new RTextAreaEditorKit.EndWordAction("end", false).getMacroID());
	}
}
