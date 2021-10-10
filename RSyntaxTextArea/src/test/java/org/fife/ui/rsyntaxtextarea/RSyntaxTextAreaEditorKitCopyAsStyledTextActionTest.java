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

import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitCopyAsStyledTextActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_copyAsStyledText() throws Exception {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"/*\n" +
				"* comment\n" +
				"*/\n" +
				"public void foo() {\n" +
				"  /* comment\n" +
				"     two */\n" +
				"}");

		textArea.setCaretPosition(5);
		textArea.moveCaretPosition(8);

		RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(false);
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCopyAsStyledTextAction);
		a.actionPerformedImpl(e, textArea);

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assertions.assertEquals("com", clipboardContent);
	}

	@Test
	void testActionPerformedImpl_cutAsStyledText() throws Exception {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"/*\n" +
				"* comment\n" +
				"*/\n" +
				"public void foo() {\n" +
				"  /* comment\n" +
				"     two */\n" +
				"}");

		textArea.setCaretPosition(5);
		textArea.moveCaretPosition(8);

		RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(true);
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCutAsStyledTextAction);
		a.actionPerformedImpl(e, textArea);

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assertions.assertEquals("com", clipboardContent);


		String expected = 	"/*\n" +
			"* ment\n" +
			"*/\n" +
			"public void foo() {\n" +
			"  /* comment\n" +
			"     two */\n" +
			"}";

		Assertions.assertEquals(expected, textArea.getText());
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(false);
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCopyAsStyledTextAction, a.getMacroID());

		a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(true);
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCutAsStyledTextAction, a.getMacroID());

	}
}
