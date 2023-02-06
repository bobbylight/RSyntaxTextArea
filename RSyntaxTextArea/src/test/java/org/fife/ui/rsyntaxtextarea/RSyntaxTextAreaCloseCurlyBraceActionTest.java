/*
 * 08/22/2015
 *
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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CloseCurlyBraceAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaCloseCurlyBraceActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea();

		// Remove the final closing curly
		textArea.setSelectionStart(textArea.getDocument().getLength() - 2);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.replaceSelection("\n\n     ");
		String expected = textArea.getText();
		textArea.setEditable(false);

		RSyntaxTextAreaEditorKit.CloseCurlyBraceAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction);
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals(expected, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_notEnabled() {

		RSyntaxTextArea textArea = createTextArea();

		// Remove the final closing curly
		textArea.setSelectionStart(textArea.getDocument().getLength() - 2);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.replaceSelection("\n\n     ");
		String expected = textArea.getText();
		textArea.setEnabled(false);

		RSyntaxTextAreaEditorKit.CloseCurlyBraceAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction);
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals(expected, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_closeCurlyBrace() {

		RSyntaxTextArea textArea = createTextArea();

		// Remove the final closing curly
		textArea.setSelectionStart(textArea.getDocument().getLength() - 2);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.replaceSelection("\n\n     ");

		RSyntaxTextAreaEditorKit.CloseCurlyBraceAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction);
		a.actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.getText().endsWith("\n}"));
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CloseCurlyBraceAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction, a.getMacroID());
	}
}
