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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitCollapseAllCommentFoldsActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_collapseAllCommentFolds() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"/*\n" +
				"* comment\n" +
				"*/\n" +
				"public void foo() {\n" +
				"  /* comment\n" +
				"     two */\n" +
				"}");

		textArea.setCaretPosition(textArea.getDocument().getLength());

		RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction a = new RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseAllCommentFoldsAction);
		a.actionPerformedImpl(e, textArea);

		FoldManager foldManager = textArea.getFoldManager();
		Assert.assertEquals(2, foldManager.getFoldCount());
		Assert.assertTrue(foldManager.getFold(0).isCollapsed());
		Assert.assertFalse(foldManager.getFold(1).isCollapsed());
		Assert.assertTrue(foldManager.getFold(1).getChild(0).isCollapsed());
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction a = new RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaCollapseAllCommentFoldsAction, a.getMacroID());
	}
}
