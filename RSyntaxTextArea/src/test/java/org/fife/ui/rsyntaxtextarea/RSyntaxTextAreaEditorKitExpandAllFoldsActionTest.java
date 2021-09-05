/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
import org.fife.ui.rtextarea.RecordableTextAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.ExpandAllFoldsAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitExpandAllFoldsActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_expandAllFolds() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"/*\n" +
				"* comment\n" +
				"*/\n" +
				"public void foo() {\n" +
				"  /* comment\n" +
				"     two */\n" +
				"}");

		textArea.setCaretPosition(textArea.getDocument().getLength());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseAllFoldsAction);
		a.actionPerformedImpl(e, textArea);

		FoldManager foldManager = textArea.getFoldManager();
		Assertions.assertEquals(2, foldManager.getFoldCount());
		Assertions.assertTrue(foldManager.getFold(0).isCollapsed());
		Assertions.assertTrue(foldManager.getFold(1).isCollapsed());
		Assertions.assertTrue(foldManager.getFold(1).getChild(0).isCollapsed());

		a = new RSyntaxTextAreaEditorKit.ExpandAllFoldsAction();
		e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaExpandAllFoldsAction);
		a.actionPerformedImpl(e, textArea);

		Assertions.assertFalse(foldManager.getFold(0).isCollapsed());
		Assertions.assertFalse(foldManager.getFold(1).isCollapsed());
		Assertions.assertFalse(foldManager.getFold(1).getChild(0).isCollapsed());
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.ExpandAllFoldsAction a = new RSyntaxTextAreaEditorKit.ExpandAllFoldsAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaExpandAllFoldsAction, a.getMacroID());
	}
}
