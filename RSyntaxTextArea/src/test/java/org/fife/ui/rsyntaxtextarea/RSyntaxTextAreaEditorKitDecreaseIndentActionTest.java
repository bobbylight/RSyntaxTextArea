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
	void testActionPerformedImpl_decreaseIndent() {

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

		RSyntaxTextAreaEditorKit.DecreaseIndentAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaDecreaseIndentAction);
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
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.DecreaseIndentAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaDecreaseIndentAction, a.getMacroID());
	}
}
