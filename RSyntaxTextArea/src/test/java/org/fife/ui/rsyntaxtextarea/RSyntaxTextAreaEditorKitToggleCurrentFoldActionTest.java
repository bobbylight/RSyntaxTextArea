/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitToggleCurrentFoldActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testActionPerformedImpl_happyPath() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction().actionPerformedImpl(e, textArea);

		FoldManager foldManager = textArea.getFoldManager();
		Assert.assertEquals(1, foldManager.getFoldCount());
		Assert.assertTrue(foldManager.getFold(0).isCollapsed());
	}


	@Test
	public void testActionPerformedImpl_foldingDisabled() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setCaretPosition(1);
		textArea.setCodeFoldingEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction().actionPerformedImpl(e, textArea);
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaToggleCurrentFoldAction,
			new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction().getMacroID());
	}
}
