/*
 * 08/22/2015
 *
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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CloseMarkupTagAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaCloseMarkupTagActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, "<books>\n<");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.setEditable(false);

		RSyntaxTextAreaEditorKit.CloseMarkupTagAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction);
		a.actionPerformedImpl(e, textArea);

		Assert.assertTrue(textArea.getText().endsWith("\n<"));
	}

	@Test
	public void testActionPerformedImpl_alignCurlyBracesEnabled() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, "<books>\n<");
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RSyntaxTextAreaEditorKit.CloseMarkupTagAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction);
		a.actionPerformedImpl(e, textArea);

		Assert.assertTrue(textArea.getText().endsWith("\n</books>"));
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CloseMarkupTagAction a = new RSyntaxTextAreaEditorKit.CloseMarkupTagAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction, a.getMacroID());
	}
}
