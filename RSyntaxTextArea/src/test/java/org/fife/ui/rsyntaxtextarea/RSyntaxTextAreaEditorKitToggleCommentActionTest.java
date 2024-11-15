/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RecordableTextAction;
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
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEditable(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEnabled(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_multiLine_endsAtStartOfLine() {
		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('1'));
		textArea.moveCaretPosition(text.indexOf("line 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// Final line is not commented out
		String expected = "//line 1\n//line 2\nline 3";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_multiLine_endsInMiddleOfLine() {
		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('1'));
		textArea.moveCaretPosition(text.indexOf("ne 3"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		String expected = "//line 1\n//line 2\n//line 3";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_startTokenOnly() {
		String text = "this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("//" + text, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_startAndEnd_missingEnd() {
		String text = "<!-- this is code";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);

		// If the "end" token isn't there, we assume the whole thing needs to be commented out
		Assertions.assertEquals("<!--" + text + "-->", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_add_singleLine_languageWithoutComments() {
		String text = "this is code";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_BBCODE, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(text, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_caretAtStartOfLine() {
		String text = "// this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(" this is code", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_caretAtStartOfLine_startAndEnd() {
		String text = "<!-- this is code -->";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, text);
		textArea.setCaretPosition(0);
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(" this is code ", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_remove_singleLine_caretInMiddleOfLine() {
		String text = "// this is code";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf("is"));
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.ToggleCommentAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(" this is code", textArea.getText());
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

		// comment
		textArea.setText("  int a = 2;  ");
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(5);

		commentAction.actionPerformedImpl(e, textArea);

		expectedText = "  //int a = 2;  ";
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