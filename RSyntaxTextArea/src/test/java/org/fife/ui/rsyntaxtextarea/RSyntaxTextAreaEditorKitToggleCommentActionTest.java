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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.ToggleCommentAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitToggleCommentActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testActionPerformedImpl_singleLine_addComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"line 1\nline 2\nline 3");
		textArea.setCaretPosition(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "line 1\n//line 2\nline 3";
		Assert.assertEquals(expectedText, textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_singleLine_removeComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"line 1\n//line 2\nline 3");
		textArea.setCaretPosition(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "line 1\nline 2\nline 3";
		Assert.assertEquals(expectedText, textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_multipleLines_addComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"line 1\nline 2\nline 3");
		textArea.setCaretPosition(2);
		textArea.moveCaretPosition(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "//line 1\n//line 2\nline 3";
		Assert.assertEquals(expectedText, textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_multipleLines_removeComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"//line 1\n//line 2\nline 3");
		textArea.setCaretPosition(2);
		textArea.moveCaretPosition(11);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "line 1\nline 2\nline 3";
		Assert.assertEquals(expectedText, textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaToggleCommentAction,
			new RSyntaxTextAreaEditorKit.ToggleCommentAction().getMacroID());
	}
}
