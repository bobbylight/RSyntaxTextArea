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
 * Unit tests for the {@link org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.ChangeFoldStateAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaChangeFoldStateActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_foldingEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertFalse(textArea.getFoldManager().getFold(0).isCollapsed());

		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a = new RSyntaxTextAreaEditorKit.ChangeFoldStateAction(
			"foo", true);
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseFoldAction);
		a.actionPerformedImpl(e, textArea);

		Assertions.assertTrue(textArea.getFoldManager().getFold(0).isCollapsed());
	}

	@Test
	void testActionPerformedImpl_foldingNotEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCodeFoldingEnabled(false);

		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a = new RSyntaxTextAreaEditorKit.ChangeFoldStateAction(
			"foo", true);
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseFoldAction);
		a.actionPerformedImpl(e, textArea);
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a = new RSyntaxTextAreaEditorKit.ChangeFoldStateAction(
			"foo", true);
		Assertions.assertEquals("foo", a.getMacroID());
	}
}
