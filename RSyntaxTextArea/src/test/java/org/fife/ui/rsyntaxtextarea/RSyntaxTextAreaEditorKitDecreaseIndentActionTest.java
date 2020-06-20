/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.DecreaseIndentAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitDecreaseIndentActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_decreaseIndent() {

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
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.DecreaseIndentAction a = new RSyntaxTextAreaEditorKit.DecreaseIndentAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaDecreaseIndentAction, a.getMacroID());
	}
}
