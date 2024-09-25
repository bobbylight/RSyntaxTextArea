/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.ToggleCommentAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitToggleCommentActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testActionPerformedImpl_notEditable() {

		String code = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			code);
		textArea.setCaretPosition(8);
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(code, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {

		String code = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			code);
		textArea.setCaretPosition(8);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(code, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_singleLine_addComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"line 1\nline 2\nline 3");
		textArea.setCaretPosition(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "line 1\n//line 2\nline 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_singleLine_removeComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"line 1\n//line 2\nline 3");
		textArea.setCaretPosition(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "line 1\nline 2\nline 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_multipleLines_addComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"line 1\nline 2\nline 3");
		textArea.setCaretPosition(2);
		textArea.moveCaretPosition(8);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "//line 1\n//line 2\nline 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_multipleLines_removeComment() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C,
			"//line 1\n//line 2\nline 3");
		textArea.setCaretPosition(2);
		textArea.moveCaretPosition(11);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCommentAction().actionPerformedImpl(e, textArea);

		String expectedText = "line 1\nline 2\nline 3";
		Assertions.assertEquals(expectedText, textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaToggleCommentAction,
			new RSyntaxTextAreaEditorKit.ToggleCommentAction().getMacroID());
	}

	@Test
	void testLeadingTrailingWhitespaces() {
		RSyntaxTextAreaEditorKit.ToggleCommentAction commentAction = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, "");
		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// multiline with leading WS
		textArea.setText("  //line 1\n//line 2\nline 3");
		textArea.setCaretPosition(2);
		textArea.moveCaretPosition(15);

		commentAction.actionPerformedImpl(e, textArea);

		String expectedText = "  line 1\nline 2\nline 3";
		Assertions.assertEquals(expectedText, textArea.getText());

		// start/end marks and leading and trailing WS
		textArea.setText("  <!-- xml -->  ");
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(0);

		commentAction.actionPerformedImpl(e, textArea);

		expectedText = "   xml   ";
		Assertions.assertEquals(expectedText, textArea.getText());

		// multiline with leading and trailing WS and start/end marks
		textArea.setText("  <!-- line 1 -->\n<!--line 2 -->  ");
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(textArea.getDocument().getLength());

		commentAction.actionPerformedImpl(e, textArea);

		expectedText = "   line 1 \nline 2   ";
		Assertions.assertEquals(expectedText, textArea.getText());
	}

	/**
	 * Test for {@link org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.ToggleCommentAction#startMatch(String, String[])}
	 * and {@link org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.ToggleCommentAction#endMatch(String, String[])}
	 */
	@Test
	void startEndSearch() {
		String[] markers = {"<!--", "-->"};
		RSyntaxTextAreaEditorKit.ToggleCommentAction action = new RSyntaxTextAreaEditorKit.ToggleCommentAction();

		Assertions.assertEquals(0, action.startMatch("<!-- text -->", markers));
		Assertions.assertEquals(2, action.startMatch("  <!-- text -->", markers));
		Assertions.assertEquals(-1, action.startMatch("// text", markers));
		Assertions.assertEquals(-1, action.startMatch("   // text", markers));
		Assertions.assertEquals(-1, action.startMatch("", markers));

		Assertions.assertEquals(10, action.endMatch("<!-- text -->", markers));
		Assertions.assertEquals(12, action.endMatch("  <!-- text -->", markers));
		Assertions.assertEquals(-1, action.endMatch("// text", markers));
		Assertions.assertEquals(-1, action.endMatch("   // text", markers));
		Assertions.assertEquals(-1, action.endMatch("", markers));
	}
}
