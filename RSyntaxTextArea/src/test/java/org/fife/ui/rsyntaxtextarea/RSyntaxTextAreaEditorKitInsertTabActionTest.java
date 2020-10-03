/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.InsertTabAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitInsertTabActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testActionPerformedImpl_notEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setEnabled(false);
		String origContent = textArea.getText();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(origContent, textArea.getText()); // Unchanged
	}


	@Test
	public void testActionPerformedImpl_noSelection() {

		RSyntaxTextArea textArea = createTextArea();
		String origContent = textArea.getText();
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals('\t' + origContent, textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_multiLineSelection() {

		String origContent = "int main() {\n" +
			"\tprintf(\"Hello world\n\");\n" +
			"}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, origContent);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(origContent.indexOf('p'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.InsertTabAction().actionPerformedImpl(e, textArea);

		String expectedContent = "\tint main() {\n" +
			"\t\tprintf(\"Hello world\n\");\n" +
			"}";
		Assert.assertEquals(expectedContent, textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_multiLineSelection_tabsEmulatedWithWhiteSpace() {

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
		Assert.assertEquals(expectedContent, textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_singleLineSelection() {

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
		Assert.assertEquals(expectedContent, textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(DefaultEditorKit.insertTabAction,
			new RSyntaxTextAreaEditorKit.InsertTabAction().getMacroID());
	}
}
