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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CloseCurlyBraceAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaCloseCurlyBraceActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_closeCurlyBrace() {

		RSyntaxTextArea textArea = createTextArea();

		// Remove the final closing curly
		textArea.setSelectionStart(textArea.getDocument().getLength() - 2);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.replaceSelection("\n\n     ");

		RSyntaxTextAreaEditorKit.CloseCurlyBraceAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction);
		a.actionPerformedImpl(e, textArea);

		Assert.assertTrue(textArea.getText().endsWith("\n}"));
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CloseCurlyBraceAction a = new RSyntaxTextAreaEditorKit.CloseCurlyBraceAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction, a.getMacroID());
	}
}
