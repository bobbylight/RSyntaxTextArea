/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitToggleCurrentFoldActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testConstructor_5Arg() {
		Action a = new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction(
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
	void testActionPerformedImpl_happyPath() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction().actionPerformedImpl(e, textArea);

		FoldManager foldManager = textArea.getFoldManager();
		Assertions.assertEquals(1, foldManager.getFoldCount());
		Assertions.assertTrue(foldManager.getFold(0).isCollapsed());
	}


	@Test
	void testActionPerformedImpl_noFoldsInDocument() {

		RSyntaxTextArea textArea = createTextArea("this is code");
		textArea.setCaretPosition(1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction().actionPerformedImpl(e, textArea);

		FoldManager foldManager = textArea.getFoldManager();
		Assertions.assertEquals(0, foldManager.getFoldCount());
	}


	@Test
	void testActionPerformedImpl_foldingDisabled() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(1);
		textArea.setCodeFoldingEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction().actionPerformedImpl(e, textArea);
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaToggleCurrentFoldAction,
			new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction().getMacroID());
	}
}
