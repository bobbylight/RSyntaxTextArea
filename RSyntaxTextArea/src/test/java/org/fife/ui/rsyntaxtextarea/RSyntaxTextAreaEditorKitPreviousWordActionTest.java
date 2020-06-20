/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.PreviousWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitPreviousWordActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testActionPerformedImpl_noSelection() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		textArea.setCaretPosition(origContent.indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		Assert.assertEquals(origContent.indexOf("line 2"), textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_noSelection_nextLine() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		// If the prev word starts on the prev line, just select the last word on the prev line
		Assert.assertEquals(textArea.getText().indexOf('1'), textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testActionPerformedImpl_selection() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		textArea.setCaretPosition(origContent.indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", true).actionPerformedImpl(e, textArea);

		Assert.assertEquals(origContent.indexOf("line 2"), textArea.getCaretPosition());
		Assert.assertEquals("line ", textArea.getSelectedText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("foo",
			new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", true).getMacroID());
	}
}
