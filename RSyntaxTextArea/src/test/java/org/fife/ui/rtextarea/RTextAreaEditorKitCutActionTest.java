/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.CutAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitCutActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.CutAction action = new RTextAreaEditorKit.CutAction(
			"cut", null, "Description", 0, null);
		Assertions.assertEquals("cut", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(9);

		RTextAreaEditorKit.CutAction action = new RTextAreaEditorKit.CutAction();
		action.actionPerformedImpl(null, textArea);

	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.cutAction,
			new RTextAreaEditorKit.CutAction().getMacroID());
	}
}
