/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.CutAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaEditorKitCutActionTest {


	@Test
	public void testConstructor_multiArg() {
		RTextAreaEditorKit.CutAction action = new RTextAreaEditorKit.CutAction(
			"cut", null, "Description", 0, null);
		Assert.assertEquals("cut", action.getName());
		Assert.assertEquals("Description", action.getDescription());
	}


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(9);

		RTextAreaEditorKit.CutAction action = new RTextAreaEditorKit.CutAction();
		action.actionPerformedImpl(null, textArea);

	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.cutAction,
			new RTextAreaEditorKit.CutAction().getMacroID());
	}
}
