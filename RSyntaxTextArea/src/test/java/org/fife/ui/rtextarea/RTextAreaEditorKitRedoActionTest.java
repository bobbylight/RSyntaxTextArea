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
 * Unit tests for the {@link RTextAreaEditorKit.RedoAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitRedoActionTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		textArea.append("foo");

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.UndoAction().actionPerformedImpl(e, textArea);
		new RTextAreaEditorKit.RedoAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("foo", textArea.getText());
		Assert.assertTrue(textArea.canUndo());
		Assert.assertFalse(textArea.canRedo());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaRedoAction,
			new RTextAreaEditorKit.RedoAction().getMacroID());
	}
}
