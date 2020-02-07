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
 * Unit tests for the {@link RTextAreaEditorKit.LineMoveAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitLineMoveActionTest {


	@Test
	public void testActionPerformedImpl_notEnabled() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LineMoveAction("foo", 1).actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1\nline 2", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_moveDown_happyPath() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LineMoveAction("foo", 1).actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 2\nline 1", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_moveDown_doesNothingWhenOnLastLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LineMoveAction("foo", 1).actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1\nline 2", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_moveUp_happyPath() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LineMoveAction("foo", -1).actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 2\nline 1", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_moveUp_doesNothingWhenOnFirstLine() {

		RTextArea textArea = new RTextArea("line 1\nline 2");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.LineMoveAction("foo", -1).actionPerformedImpl(e, textArea);

		Assert.assertEquals("line 1\nline 2", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.LineMoveAction("foo", 1).getMacroID());
	}
}
