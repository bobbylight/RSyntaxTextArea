/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.ToggleTextModeAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitToggleTextModeActionTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		Assert.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());

		// Toggle to overwrite
		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.ToggleTextModeAction().actionPerformedImpl(e, textArea);
		Assert.assertEquals(RTextArea.OVERWRITE_MODE, textArea.getTextMode());

		// And back to insert
		new RTextAreaEditorKit.ToggleTextModeAction().actionPerformedImpl(e, textArea);
		Assert.assertEquals(RTextArea.INSERT_MODE, textArea.getTextMode());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaToggleTextModeAction,
			new RTextAreaEditorKit.ToggleTextModeAction().getMacroID());
	}
}
