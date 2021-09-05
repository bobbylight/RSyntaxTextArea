/*
 * 08/22/2015
 *
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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.InsertBreakAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitInsertBreakActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_closingCurlyBraceAdded() {

		String origContent = "public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = origContent + "\n\t\n}\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_allWhiteSpaceLineCleared() {

		String origContent = "public void foo() {\n\t";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = "public void foo() {\n\n\t";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_autoIndentNotEnabled() {

		String origContent = "public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = "public void foo() {\n\n}\n";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.InsertBreakAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.insertBreakAction, a.getMacroID());
	}
}
