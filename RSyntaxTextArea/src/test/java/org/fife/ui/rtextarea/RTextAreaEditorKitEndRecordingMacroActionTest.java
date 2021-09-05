/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.EndRecordingMacroAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitEndRecordingMacroActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.EndRecordingMacroAction action = new RTextAreaEditorKit.EndRecordingMacroAction(
			"name", null, "Description", 0, null);
		Assertions.assertEquals("name", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		RTextAreaEditorKit.EndRecordingMacroAction action = new RTextAreaEditorKit.EndRecordingMacroAction();

		RTextArea.beginRecordingMacro();
		Assertions.assertTrue(RTextArea.isRecordingMacro()); // Sanity check

		try {
			action.actionPerformedImpl(null, new RTextArea());
			Assertions.assertFalse(RTextArea.isRecordingMacro());
		} finally {
			RTextArea.endRecordingMacro();
		}
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaEndRecordingMacroAction,
			new RTextAreaEditorKit.EndRecordingMacroAction().getMacroID());
	}


	@Test
	void testIsRecordable() {
		RTextAreaEditorKit.EndRecordingMacroAction action = new RTextAreaEditorKit.EndRecordingMacroAction();
		Assertions.assertFalse(action.isRecordable());
	}
}
