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


/**
 * Unit tests for this action.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitCloseCurlyBraceActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testActionPerformedImpl_enabledAndEditable_alignCurlyBraces_emptyDocument() {

		RSyntaxTextArea textArea = createTextArea("");

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		a.actionPerformedImpl(null, textArea);

		String expected = "}";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_enabledAndEditable_alignCurlyBraces_newCloseCurly() {

		RSyntaxTextArea textArea = createTextArea("{\n  printf(\"hi\");\n   ");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		a.actionPerformedImpl(null, textArea);

		// Verify the last line's whitespace is removed
		String expected = "{\n  printf(\"hi\");\n}";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_enabledAndEditable_alignCurlyBraces_newCloseCurly_languageWithoutCurlies() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_CSV, "{\n   hi\n   ");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		a.actionPerformedImpl(null, textArea);

		// Verify the closing curly is simply inserted since it's not semantically meaningful
		String expected = "{\n   hi\n   }";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_enabledAndEditable_alignCurlyBraces_newCloseCurly_nonWhitespace() {

		RSyntaxTextArea textArea = createTextArea("{\n  printf(\"hi\");\n  xxx ");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		a.actionPerformedImpl(null, textArea);

		// Verify the last line's whitespace isn't removed due to "xxx"
		String expected = "{\n  printf(\"hi\");\n  xxx }";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_enabledAndEditable_alignCurlyBracesFalse_newCloseCurly() {

		RSyntaxTextArea textArea = createTextArea("{\n  printf(\"hi\");\n   ");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.setAutoIndentEnabled(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		a.actionPerformedImpl(null, textArea);

		// Verify the last line's whitespace isn't removed
		String expected = "{\n  printf(\"hi\");\n   }";
		Assertions.assertEquals(expected, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEditable() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEditable(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setEnabled(false);
		String origText = textArea.getText();
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		a.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(origText, textArea.getText());
	}


	@Test
	void getGetMacroId() {
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction, a.getMacroID());
	}
}
