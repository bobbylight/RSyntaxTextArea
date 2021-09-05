/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginRecordingMacroAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitBeginRecordingMacroActionTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.BeginRecordingMacroAction action = new RTextAreaEditorKit.BeginRecordingMacroAction(
			"recordMacro", null, "Description", 0, null);
		Assertions.assertEquals("recordMacro", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		Assertions.assertFalse(RTextArea.isRecordingMacro()); // Sanity check

		RTextAreaEditorKit.BeginRecordingMacroAction action = new RTextAreaEditorKit.BeginRecordingMacroAction();

		try {
			action.actionPerformedImpl(null, new RTextArea());
			Assertions.assertTrue(RTextArea.isRecordingMacro());
		} finally {
			RTextArea.endRecordingMacro();
		}
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaBeginRecordingMacroAction,
			new RTextAreaEditorKit.BeginRecordingMacroAction().getMacroID());
	}


	@Test
	void testIsRecordable() {
		RTextAreaEditorKit.BeginRecordingMacroAction action = new RTextAreaEditorKit.BeginRecordingMacroAction();
		Assertions.assertFalse(action.isRecordable());
	}
}
