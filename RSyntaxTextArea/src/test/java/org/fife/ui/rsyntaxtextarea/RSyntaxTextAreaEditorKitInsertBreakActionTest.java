/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rtextarea.RecordableTextAction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.InsertBreakAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitInsertBreakActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_closingCurlyBraceAdded() {

		String origContent = "public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = origContent + "\n\t\n}\n";
		Assert.assertEquals(expectedResultContent, textArea.getText());
	}

	@Test
	public void testActionPerformedImpl_allWhiteSpaceLineCleared() {

		String origContent = "public void foo() {\n\t";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = "public void foo() {\n\n\t";
		Assert.assertEquals(expectedResultContent, textArea.getText());
	}

	@Test
	public void testActionPerformedImpl_autoIndentNotEnabled() {

		String origContent = "public void foo() {";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setAutoIndentEnabled(false);

		textArea.setCaretPosition(origContent.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.insertBreakAction);
		a.actionPerformedImpl(e, textArea);

		String expectedResultContent = "public void foo() {\n\n}\n";
		Assert.assertEquals(expectedResultContent, textArea.getText());
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.InsertBreakAction a = new RSyntaxTextAreaEditorKit.InsertBreakAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.insertBreakAction, a.getMacroID());
	}
}
