/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitCopyCutAsStyledTextActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_copyAsStyledText() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

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
	void testActionPerformedImpl_copyAsStyledText_rTextArea() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RTextArea textArea = new RTextArea(
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

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

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
	void testActionPerformedImpl_cutAsStyledText_rTextArea() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RTextArea textArea = new RTextArea(
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
	void test_disabledStyledCopyCutAction() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		String text = "/*\n" +
			"* comment\n" +
			"*/\n" +
			"public void foo() {\n" +
			"  /* comment\n" +
			"     two */\n" +
			"}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, text);
		textArea.setEditable(false);

		textArea.setCaretPosition(5);
		textArea.moveCaretPosition(8);

		RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(false);
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCopyAsStyledTextAction);
		a.actionPerformedImpl(e, textArea);

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		Assertions.assertEquals("com", clipboardContent);
		Assertions.assertEquals(text, textArea.getText());

		textArea.setCaretPosition(8);
		textArea.moveCaretPosition(11);

		// Trying to cut "men". This shouldn't happen as the textArea is not editable.
		a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(true);
		e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaCutAsStyledTextAction);
		a.actionPerformedImpl(e, textArea);
		clipboardContent = (String)textArea.getToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		Assertions.assertEquals("com", clipboardContent, "Clipboard content should be unchanged");
		Assertions.assertEquals(text, textArea.getText());

	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(false);
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCopyAsStyledTextAction, a.getMacroID());

		a = new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(true);
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaCutAsStyledTextAction, a.getMacroID());

	}
}
