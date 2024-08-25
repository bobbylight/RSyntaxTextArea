/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.InsertTabAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitInsertTabActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testConstructor_nameArg() {
		Action a = new RSyntaxTextAreaEditorKit.InsertTabAction("foo");
		Assertions.assertEquals("foo", a.getValue(Action.NAME));
	}


	@Test
	void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setEditable(false);
		String origContent = textArea.getText();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent, textArea.getText()); // Unchanged
	}


	@Test
	void testActionPerformedImpl_notEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setEnabled(false);
		String origContent = textArea.getText();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent, textArea.getText()); // Unchanged
	}


	@Test
	void testActionPerformedImpl_noSelection() {

		RSyntaxTextArea textArea = createTextArea();
		String origContent = textArea.getText();
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals('\t' + origContent, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_multiLineSelection_dotAtStartOfLine() {

		String origContent = "int main() {\n" +
			"\tprintf(\"Hello world\n\");\n" +
			"}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, origContent);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(origContent.indexOf('\t'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		// End line does not get indented since the caret was at the start of it
		String expectedContent = "\tint main() {\n" +
			"\tprintf(\"Hello world\n\");\n" +
			"}";
		Assertions.assertEquals(expectedContent, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_multiLineSelection_dotNotAtStartOfLine() {

		String origContent = "int main() {\n" +
			"\tprintf(\"Hello world\n\");\n" +
			"}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, origContent);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(origContent.indexOf('p'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		// End line gets indented since the caret was NOT at the start of it
		String expectedContent = "\tint main() {\n" +
			"\t\tprintf(\"Hello world\n\");\n" +
			"}";
		Assertions.assertEquals(expectedContent, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_multiLineSelection_tabsEmulatedWithWhiteSpace() {

		String origContent = "int main() {\n" +
			"\tprintf(\"Hello world\n\");\n" +
			"}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, origContent);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(origContent.indexOf('p'));
		textArea.setTabSize(4);
		textArea.setTabsEmulated(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		String expectedContent = "    int main() {\n" +
			"    \tprintf(\"Hello world\n\");\n" +
			"}";
		Assertions.assertEquals(expectedContent, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_singleLineSelection() {

		String origContent = "int main() {\n" +
			"\tprintf(\"Hello world\n\");\n" +
			"}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, origContent);
		textArea.setCaretPosition(origContent.indexOf('m'));
		textArea.moveCaretPosition(origContent.indexOf('('));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		String expectedContent = "\tint main() {\n" +
			"\tprintf(\"Hello world\n\");\n" +
			"}";
		Assertions.assertEquals(expectedContent, textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(DefaultEditorKit.insertTabAction,
			new RSyntaxTextAreaEditorKit.InsertTabAction().getMacroID());
	}
}
