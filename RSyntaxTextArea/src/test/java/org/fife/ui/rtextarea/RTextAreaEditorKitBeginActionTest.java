/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitBeginActionTest extends AbstractRTextAreaTest {


	@Test
	void testActionPerformedImpl_select() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setCaretPosition(3);

		RTextAreaEditorKit.BeginAction action = new RTextAreaEditorKit.BeginAction("begin", true);
		action.actionPerformedImpl(null, textArea);

		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_noSelect() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setCaretPosition(3);

		RTextAreaEditorKit.BeginAction action = new RTextAreaEditorKit.BeginAction("begin", false);
		action.actionPerformedImpl(null, textArea);

		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("begin", new RTextAreaEditorKit.BeginAction("begin", false).getMacroID());
	}
}
