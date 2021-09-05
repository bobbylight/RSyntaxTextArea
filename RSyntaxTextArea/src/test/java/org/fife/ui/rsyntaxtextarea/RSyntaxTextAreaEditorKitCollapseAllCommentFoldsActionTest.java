/*
 * 08/22/2015
 *
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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitCollapseAllCommentFoldsActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_collapseAllCommentFolds() {

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
		Assertions.assertEquals(2, foldManager.getFoldCount());
		Assertions.assertTrue(foldManager.getFold(0).isCollapsed());
		Assertions.assertFalse(foldManager.getFold(1).isCollapsed());
		Assertions.assertTrue(foldManager.getFold(1).getChild(0).isCollapsed());
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction a = new RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCollapseAllCommentFoldsAction, a.getMacroID());
	}
}
