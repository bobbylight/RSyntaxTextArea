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

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CollapseAllFoldsAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitCollapseAllFoldsActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testConstructor_5Arg() {
		Action a = new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction(
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
	void testActionPerformedImpl_collapseAllFolds() {

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
		Assertions.assertEquals(2, foldManager.getFoldCount());
		Assertions.assertTrue(foldManager.getFold(0).isCollapsed());
		Assertions.assertTrue(foldManager.getFold(1).isCollapsed());
		Assertions.assertTrue(foldManager.getFold(1).getChild(0).isCollapsed());
	}


	@Test
	void testActionPerformedImpl_codeFoldingDisabled() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"/*\n" +
				"* comment\n" +
				"*/\n" +
				"public void foo() {\n" +
				"  /* comment\n" +
				"     two */\n" +
				"}");
		textArea.setCodeFoldingEnabled(false);
		textArea.setCaretPosition(textArea.getDocument().getLength());

		RSyntaxTextAreaEditorKit.CollapseAllFoldsAction a = new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCollapseAllFoldsAction);
		a.actionPerformedImpl(e, textArea);

		FoldManager foldManager = textArea.getFoldManager();
		Assertions.assertEquals(0, foldManager.getFoldCount());
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CollapseAllFoldsAction a = new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCollapseAllFoldsAction, a.getMacroID());
	}
}
