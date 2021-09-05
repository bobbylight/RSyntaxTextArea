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
