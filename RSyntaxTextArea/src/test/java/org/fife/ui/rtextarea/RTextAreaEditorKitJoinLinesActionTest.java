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
 * Unit tests for the {@link RTextAreaEditorKit.JoinLinesAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitJoinLinesActionTest {


	@Test
	public void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.JoinLinesAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1\nline 2", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_happyPath() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.JoinLinesAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1line 2", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_doesNothingWhenOnLastLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.JoinLinesAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1\nline 2", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaJoinLinesAction,
			new RTextAreaEditorKit.JoinLinesAction().getMacroID());
	}
}
