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
 * Unit tests for the {@link org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.ChangeFoldStateAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaChangeFoldStateActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_foldingEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		Assert.assertFalse(textArea.getFoldManager().getFold(0).isCollapsed());

		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a = new RSyntaxTextAreaEditorKit.ChangeFoldStateAction(
			"foo", true);
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseFoldAction);
		a.actionPerformedImpl(e, textArea);

		Assert.assertTrue(textArea.getFoldManager().getFold(0).isCollapsed());
	}

	@Test
	public void testActionPerformedImpl_foldingNotEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCodeFoldingEnabled(false);

		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a = new RSyntaxTextAreaEditorKit.ChangeFoldStateAction(
			"foo", true);
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseFoldAction);
		a.actionPerformedImpl(e, textArea);
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a = new RSyntaxTextAreaEditorKit.ChangeFoldStateAction(
			"foo", true);
		Assert.assertEquals("foo", a.getMacroID());
	}
}
