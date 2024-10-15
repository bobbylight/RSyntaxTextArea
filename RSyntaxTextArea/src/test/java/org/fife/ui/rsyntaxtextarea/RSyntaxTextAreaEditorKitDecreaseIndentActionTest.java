/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RecordableTextAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.DecreaseIndentAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitDecreaseIndentActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea("\t\tfoo");
		textArea.setEditable(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		// Text is unchanged
		Assertions.assertEquals("\t\tfoo", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_notEnabled() {

		RSyntaxTextArea textArea = createTextArea("\t\tfoo");
		textArea.setEnabled(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		// Text is unchanged
		Assertions.assertEquals("\t\tfoo", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_multipleLinesSelected() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"/*\n" +
				"    * comment\n" +
				"    */\n" +
				"    public void foo() {\n" +
				"        /* comment\n" +
				"          two */\n" +
				"    }");

		textArea.setTabSize(4);
		textArea.setCaretPosition(0);
		textArea.moveCaretPosition(textArea.getText().length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		String expected = "/*\n" +
			"* comment\n" +
			"*/\n" +
			"public void foo() {\n" +
			"    /* comment\n" +
			"      two */\n" +
			"}";
		String actual = textArea.getText();
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testActionPerformedImpl_singleLine_noSelection() {

		RSyntaxTextArea textArea = createTextArea("\t\tfoo");

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals("\tfoo", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_singleLine_selection() {

		RSyntaxTextArea textArea = createTextArea("\t\tfoo");
		textArea.setCaretPosition(1);
		textArea.moveCaretPosition(5);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals("\tfoo", textArea.getText());
	}

	@Test
	void testGetMacroId() {
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaDecreaseIndentAction, a.getMacroID());
	}
}
