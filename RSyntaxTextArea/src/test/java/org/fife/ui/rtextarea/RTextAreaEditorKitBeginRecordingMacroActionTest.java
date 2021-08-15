/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginRecordingMacroAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitBeginRecordingMacroActionTest {


	@Test
	public void testConstructor_multiArg() {
		RTextAreaEditorKit.BeginRecordingMacroAction action = new RTextAreaEditorKit.BeginRecordingMacroAction(
			"recordMacro", null, "Description", 0, null);
		Assert.assertEquals("recordMacro", action.getName());
		Assert.assertEquals("Description", action.getDescription());
	}


	@Test
	public void testActionPerformedImpl() {

		Assert.assertFalse(RTextArea.isRecordingMacro()); // Sanity check

		RTextAreaEditorKit.BeginRecordingMacroAction action = new RTextAreaEditorKit.BeginRecordingMacroAction();

		try {
			action.actionPerformedImpl(null, new RTextArea());
			Assert.assertTrue(RTextArea.isRecordingMacro());
		} finally {
			RTextArea.endRecordingMacro();
		}
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaBeginRecordingMacroAction,
			new RTextAreaEditorKit.BeginRecordingMacroAction().getMacroID());
	}


	@Test
	public void testIsRecordable() {
		RTextAreaEditorKit.BeginRecordingMacroAction action = new RTextAreaEditorKit.BeginRecordingMacroAction();
		Assert.assertFalse(action.isRecordable());
	}
}
