/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.EndRecordingMacroAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitEndRecordingMacroActionTest {


	@Test
	public void testConstructor_multiArg() {
		RTextAreaEditorKit.EndRecordingMacroAction action = new RTextAreaEditorKit.EndRecordingMacroAction(
			"name", null, "Description", 0, null);
		Assert.assertEquals("name", action.getName());
		Assert.assertEquals("Description", action.getDescription());
	}


	@Test
	public void testActionPerformedImpl() {

		RTextAreaEditorKit.EndRecordingMacroAction action = new RTextAreaEditorKit.EndRecordingMacroAction();

		RTextArea.beginRecordingMacro();
		Assert.assertTrue(RTextArea.isRecordingMacro()); // Sanity check

		try {
			action.actionPerformedImpl(null, new RTextArea());
			Assert.assertFalse(RTextArea.isRecordingMacro());
		} finally {
			RTextArea.endRecordingMacro();
		}
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaEndRecordingMacroAction,
			new RTextAreaEditorKit.EndRecordingMacroAction().getMacroID());
	}


	@Test
	public void testIsRecordable() {
		RTextAreaEditorKit.EndRecordingMacroAction action = new RTextAreaEditorKit.EndRecordingMacroAction();
		Assert.assertFalse(action.isRecordable());
	}
}
