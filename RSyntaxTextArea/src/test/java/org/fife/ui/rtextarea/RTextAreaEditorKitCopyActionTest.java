/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.CopyAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitCopyActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.CopyAction action = new RTextAreaEditorKit.CopyAction(
			"copy", null, "Description", 0, null);
		Assertions.assertEquals("copy", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(9);

		RTextAreaEditorKit.CopyAction action = new RTextAreaEditorKit.CopyAction();
		action.actionPerformedImpl(null, textArea);

	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.copyAction,
			new RTextAreaEditorKit.CopyAction().getMacroID());
	}
}
