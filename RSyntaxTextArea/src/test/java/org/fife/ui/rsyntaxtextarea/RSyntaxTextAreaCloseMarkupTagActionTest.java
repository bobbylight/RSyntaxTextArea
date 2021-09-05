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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CloseMarkupTagAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
public class RSyntaxTextAreaCloseMarkupTagActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, "<books>\n<");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.setEditable(false);

		RSyntaxTextAreaEditorKit.CloseMarkupTagAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction);
		a.actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.getText().endsWith("\n<"));
	}

	@Test
	public void testActionPerformedImpl_alignCurlyBracesEnabled() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, "<books>\n<");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RSyntaxTextAreaEditorKit.CloseMarkupTagAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction);
		a.actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.getText().endsWith("\n</books>"));
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CloseMarkupTagAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction, a.getMacroID());
	}
}
