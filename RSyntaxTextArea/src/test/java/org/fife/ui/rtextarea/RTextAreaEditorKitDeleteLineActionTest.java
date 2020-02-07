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
 * Unit tests for the {@link RTextAreaEditorKit.DeleteLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitDeleteLineActionTest {


	@Test
	public void testActionPerformedImpl_notEditable() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeleteLineAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1\nline 2\nline 3", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_noSelection() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeleteLineAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1\nline 3", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_selectionWithNoCharsSelectedOnLastLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));
		textArea.moveCaretPosition(textArea.getText().indexOf('\n') + 1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.DeleteLineAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 2\nline 3", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaDeleteLineAction,
			new RTextAreaEditorKit.DeleteLineAction().getMacroID());
	}
}
