/*
 * 08/22/2015
 *
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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CollapseAllFoldsAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitCollapseAllFoldsActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_collapseAllFolds() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"/*\n" +
				"* comment\n" +
				"*/\n" +
				"public void foo() {\n" +
				"  /* comment\n" +
				"     two */\n" +
				"}");

		textArea.setCaretPosition(textArea.getDocument().getLength());

		RSyntaxTextAreaEditorKit.CollapseAllFoldsAction a = new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseAllFoldsAction);
		a.actionPerformedImpl(e, textArea);

		FoldManager foldManager = textArea.getFoldManager();
		Assert.assertEquals(2, foldManager.getFoldCount());
		Assert.assertTrue(foldManager.getFold(0).isCollapsed());
		Assert.assertTrue(foldManager.getFold(1).isCollapsed());
		Assert.assertTrue(foldManager.getFold(1).getChild(0).isCollapsed());
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CollapseAllFoldsAction a = new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaCollapseAllFoldsAction, a.getMacroID());
	}
}
