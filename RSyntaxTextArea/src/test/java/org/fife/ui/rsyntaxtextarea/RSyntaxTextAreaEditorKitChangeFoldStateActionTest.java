/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;


/**
 * Unit tests for this action.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitChangeFoldStateActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testConstructor_4Arg() {
		Action a = new RSyntaxTextAreaEditorKit.ChangeFoldStateAction(
			"name", null, "desc", 1, null
		);
		Assertions.assertEquals("name", a.getValue(Action.NAME));
		Assertions.assertNull(a.getValue(Action.LARGE_ICON_KEY));
		Assertions.assertNull(a.getValue(Action.SMALL_ICON));
		Assertions.assertEquals("desc", a.getValue(Action.SHORT_DESCRIPTION));
		Assertions.assertEquals(1, a.getValue(Action.MNEMONIC_KEY));
		Assertions.assertNull(a.getValue(Action.ACCELERATOR_KEY));
	}


	@Test
	void testActionPerformedImpl_codeFoldingNotEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setCodeFoldingEnabled(false);

		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a =
			new RSyntaxTextAreaEditorKit.ChangeFoldStateAction("name", true);

		a.actionPerformedImpl(null, textArea);
		// TODO: Mock and verify error feedback is triggered
	}


	@Test
	void testActionPerformedImpl_codeFoldingEnabled_closesClosestFold() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(0); // On the first fold's line

		Assertions.assertFalse(textArea.getFoldManager().getFold(0).isCollapsed());

		RSyntaxTextAreaEditorKit.ChangeFoldStateAction a =
			new RSyntaxTextAreaEditorKit.ChangeFoldStateAction("name", true);

		a.actionPerformedImpl(null, textArea);
		Assertions.assertTrue(textArea.getFoldManager().getFold(0).isCollapsed());
	}
}
