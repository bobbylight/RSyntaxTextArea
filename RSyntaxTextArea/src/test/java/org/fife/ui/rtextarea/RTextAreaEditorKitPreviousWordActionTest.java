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
 * Unit tests for the {@link RTextAreaEditorKit.PreviousWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitPreviousWordActionTest {


	@Test
	public void testActionPerformedImpl_noSelection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(5);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		Assert.assertEquals(0, textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_noSelection_previousLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(7);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		// If the next word starts on the next line, just select to the end of the current line
		Assert.assertEquals(textArea.getText().indexOf('\n'), textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_selection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(5);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.PreviousWordAction("foo", true).actionPerformedImpl(e, textArea);

		Assert.assertEquals(0, textArea.getCaretPosition());
		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(5, textArea.getSelectionEnd());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.PreviousWordAction("foo", true).getMacroID());
	}
}
